package pl.skrys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.skrys.app.SpUserApp;
import pl.skrys.app.SpVerifyToken;
import pl.skrys.service.*;
import pl.skrys.validator.SpUserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.UUID;

@Controller
public class SpLoginRegisterControl {

    private SpUserValidator spUserValidator = new SpUserValidator();
    private SpUserService spUserService;
    private UserRoleService userRoleService;
    private ReCaptchaService reCaptchaService;
    private SpVerifyTokenService verifyTokenService;
    private SpMailService spMailService;
    private MessageSource messageSource;

    public SpLoginRegisterControl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public SpLoginRegisterControl(SpUserService userAppService, UserRoleService userRoleService, ReCaptchaService reCaptchaService, SpVerifyTokenService verifyTokenService, SpMailService spMailService){
        this.spUserService = userAppService;
        this.userRoleService = userRoleService;
        this.reCaptchaService = reCaptchaService;
        this.verifyTokenService = verifyTokenService;
        this.spMailService = spMailService;
    }


    @RequestMapping(value = "/in_home")
    public String homePage(){
        System.out.println("Logged In");
        return "in_home";
    }

    @RequestMapping(value ={ "/login", "/"})
    public String customLogin(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              @RequestParam(value = "activated", required = false) String activated,
                              @RequestParam(value = "registered", required = false) String registered,
                              Model model){
        if(error != null){
            model.addAttribute("error", "Invalid PESEL or password");
        }

        if(logout != null){
            model.addAttribute("msg", "Logged out successfull");
        }

        if(activated != null){
            model.addAttribute("msg", "Account activated");
        }

        if(registered != null){
            model.addAttribute("msg", "Account registered\r\nPlease Confirm email");
        }




        return "login";
    }

    @RequestMapping(value = "/accessDenied")
    public String accessDenied(){
        System.out.println("accessDenied");
        return "access_denied";
    }


    @RequestMapping(value = "/register")
    public String showUsersApp(Model model, HttpServletRequest request){
        int userRegisterId = ServletRequestUtils.getIntParameter(request, "userRegisterId", -1);
        if(userRegisterId > 0){
            SpUserApp userApp = spUserService.getUserApp(userRegisterId);
            userApp.setPassword("");
            model.addAttribute("userRegister", userApp);
            //model.addAttribute("userApp", userAppService.getUserApp(userAppId));// attributeName "userApp" musi sie pokrywać z tym co jest w *.jsp <form:form modelAttribute="userApp">
        }else{
            model.addAttribute("userRegister", new SpUserApp());// attributeName "userApp" musi sie pokrywać z tym co jest w *.jsp <form:form modelAttribute="userApp">
            model.addAttribute("recaptchaError", false);

        }
        /*
        model.addAttribute("userAppList", userAppService.listUserApp());
        model.addAttribute("userRoleList", userRoleService.listUserRole());
        model.addAttribute("addressesList", addressService.listAddress());

         */
        return "register";
    }

    @RequestMapping(value = "/registerUSER", method = RequestMethod.POST)
    public String addAppUser(@Valid @ModelAttribute("userRegister") SpUserApp userApp, BindingResult result, Model model, HttpServletRequest request){// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"
        System.out.println(userApp.getId()+" 1name: "+userApp.getFirstName()+" lstName: "+userApp.getLastName()+" tel: "+userApp.getTelephone()+" email: "+userApp.getEmail()+" pesel: "+userApp.getPesel());

        spUserValidator.validate(userApp, result);
        boolean recaptchaVerify = reCaptchaService.verify(request.getParameter("g-recaptcha-response"));

        if(spUserService.findByPesel(userApp.getPesel())!=null){
            //todo duplikat pesel
            result.rejectValue("pesel", "error.alreadyExists");
        }
        if(spUserService.findByTel(userApp.getTelephone()).size()!=0){
            //todo duplikat tele
            result.rejectValue("telephone", "error.alreadyExists");
        }
        if(spUserService.findByEmail(userApp.getEmail()).size()!=0){
            //todo duplikat email
            result.rejectValue("email", "error.alreadyExists");
        }



        if(result.getErrorCount() == 0 && recaptchaVerify){
            if(userApp.getId()==0){
                System.out.println("add user spLoginRegisterCtr");
                spUserService.addUser(userApp);
                String USRtoken = UUID.randomUUID().toString();
                userApp.getVerifyToken().setVerifyToken(USRtoken);
                verifyTokenService.addVerifyToken(userApp.getVerifyToken());//todo
            }else{
                spUserService.editUserApp(userApp);
            }
            //return "redirect:registered.html";
            //SpVerifyToken verifyToken = new SpVerifyToken();
            //String USRtoken = UUID.randomUUID().toString();
            //verifyToken.setVerifyToken(USRtoken);
            //verifyTokenService.addVerifyToken(verifyToken);
            System.out.println("TOKEN: "+verifyTokenService.getVerifyToken(userApp.getVerifyToken().getId()).getVerifyToken());

            spMailService.sendSimpleMessage(userApp.getEmail(), "Confirm Registration", "Confirm email: \r\n http://localhost:8080/confirmEmail/"+verifyTokenService.getVerifyToken(userApp.getVerifyToken().getId()).getVerifyToken()+"_"+userApp.getId());
            System.out.println("Wyslany: "+userApp.getEmail());

            model.addAttribute("registered", 1);
            return"redirect:login.html";
        }else if(!recaptchaVerify){
            System.out.println("recaptcha NOT Verify");//TODO
            model.addAttribute("recaptchaError", true);

        }
        /*
        model.addAttribute("firstname", appUser.getFirstName());
        model.addAttribute("lastname", appUser.getLastName());
        model.addAttribute("tel", appUser.getTelephone());
        model.addAttribute("email", appUser.getEmail());
        model.addAttribute("age", appUser.getAge());
        //return "redirect:appUsers";
         */
        //model.addAttribute("userAppList", userAppService.listUserApp());
        return "register";
    }



    @RequestMapping("/confirmEmail/{token}_{uId}")
    public String confirmUserRegister(@PathVariable("token") String token, @PathVariable("uId") Long uID, Model model, HttpServletRequest request){
        Locale locale = request.getLocale();

        SpVerifyToken verifyToken = verifyTokenService.getVerifyTokenByToken(token);
        if (verifyToken!=null){

            SpUserApp userApp = verifyToken.getUserApp();

            if(userApp.getId()==uID){
                spUserService.activateUserApp(userApp, true);

                System.out.println("userID:"+verifyToken.getUserApp().getId()+" AKTYWOWANY!!!!");//TODO po testach usunac
            }else{
                //todo bledna id usera
                System.out.println("bledna id usera przy aktywacji");
            }




        }else{
            System.out.println("token null");//todo token null
            return "redirect:accessDenied.html";
        }



        model.addAttribute("activated", 1);
        return"redirect:/login.html";
    }
}
