package pl.skrys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.skrys.app.SpUserApp;
import pl.skrys.service.SpUserService;
import pl.skrys.service.UserRoleService;
import pl.skrys.validator.SpUserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@Controller
public class SpUserEdit {


    private SpUserService userService;
    private UserRoleService userRoleService;
    private SpUserValidator spUserValidator = new SpUserValidator();

    public SpUserEdit(SpUserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @RequestMapping(value = "/inUser")
    public String showUserApp(Model model, HttpServletRequest request, Principal principal){//
        // todo poprawić zmienianie PESELu, zeby przelogowało uzytkownika
        //todo albo na sztywno zablokowac mozliwosc  zmieniania PESELu

        System.out.println("Principal Name: "+principal.getName());



        String userPesel = principal.getName();

        if(userPesel != null){
            System.out.println("jest jakis user ID");

            SpUserApp userApp = userService.findByPesel(userPesel);
            userApp.setPassword("");
            userApp.setConfirmPassword("");
            model.addAttribute("userEdit", userApp);
            model.addAttribute("userPassEdit", userApp);


            //model.addAttribute("userApp", userAppService.getUserApp(userAppId));//todo attributeName "userApp" musi sie pokrywać z tym co jest w *.jsp <form:form modelAttribute="userApp">
        }else{
            System.out.println("nie ma user ID");

            SpUserApp userApp = new SpUserApp();
            userApp.setPassword("");
            userApp.setConfirmPassword("");

            model.addAttribute("userEdit", userApp);//attributeName "userApp" musi sie pokrywać z tym co jest w *.jsp <form:form modelAttribute="userApp">
            model.addAttribute("userPassEdit", userApp);


        }
        //model.addAttribute("userAppList", userService.listUserApp());
        //model.addAttribute("userRoleList", userRoleService.listUserRole());
        return "in_user_edit";
    }



    @RequestMapping(value = "/inUserEdit", method = RequestMethod.POST)
    public String editUserWoPass(@Valid @ModelAttribute("userEdit") SpUserApp userApp, BindingResult result, Model model, HttpServletRequest request) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"
        System.out.println("1name: " + userApp.getFirstName() + " lstName: " + userApp.getLastName() + " tel: " + userApp.getTelephone() + " email: " + userApp.getEmail() + " pesel: " + userApp.getPesel());
        userApp.setPassword("      ");
        userApp.setConfirmPassword("      ");
        System.out.println(userApp.getPesel()+" "+userApp.getId()+" -"+userApp.getPassword()+"-");
        spUserValidator.validateWoPassword(userApp, result);

        if (result.getErrorCount() == 0) {
            if (userApp.getId() == 0) {
                //teoretycznei dodawanie usera
                System.out.println("nie powinno sie to pojawić");
            } else {
                userService.editUserAppWoPassword(userApp);
                return "redirect:/inUser.html";
            }
            //return "redirect:registered.html";
            //SpVerifyToken verifyToken = new SpVerifyToken();
            //String USRtoken = UUID.randomUUID().toString();
            //verifyToken.setVerifyToken(USRtoken);
            //verifyTokenService.addVerifyToken(verifyToken);


        }

        System.out.println("są bledy validatora");
        //SpUserApp user = userService.findByPesel(userApp.getPesel());
        SpUserApp user = userService.getUserApp(userApp.getId());
        user.setPassword("");
        user.setConfirmPassword("");
        model.addAttribute("userPassEdit", user);


        return "in_user_edit";
    }




    @RequestMapping(value = "/inUserPassEdit", method = RequestMethod.POST)
    public String editUserPass(@Valid @ModelAttribute("userPassEdit") SpUserApp userApp, BindingResult result, Model model, HttpServletRequest request) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"
        System.out.println("1name: " + userApp.getFirstName() + " lstName: " + userApp.getLastName() + " tel: " + userApp.getTelephone() + " email: " + userApp.getEmail() + " pesel: " + userApp.getPesel());


        System.out.println(userApp.getPesel()+" "+userApp.getId()+" "+userApp.getPassword());
        spUserValidator.validatePass(userApp, result);

        if (result.getErrorCount() == 0) {
            if (userApp.getId() == 0) {
                //teoretycznei dodawanie usera
                System.out.println("nie powinno sie to pojawić");
            } else {
                userService.editPassword(userApp);
                return "redirect:/inUser.html";//todo dodać wiadomosci wysakkujace jak przy logowaniu itp...
            }
            //return "redirect:registered.html";
            //SpVerifyToken verifyToken = new SpVerifyToken();
            //String USRtoken = UUID.randomUUID().toString();
            //verifyToken.setVerifyToken(USRtoken);
            //verifyTokenService.addVerifyToken(verifyToken);


        }
        System.out.println("są bledy validatora");
        SpUserApp user = userService.findByPesel(userApp.getPesel());
        user.setPassword("");
        user.setConfirmPassword("");
        model.addAttribute("userEdit", user);




        return "in_user_edit";
    }


}
