package pl.skrys.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import pl.skrys.app.SpUserApp;
import pl.skrys.app.UserRole;
import pl.skrys.dao.SpUserRepository;
import pl.skrys.service.SpUserService;
import pl.skrys.service.UserRoleService;
import pl.skrys.validator.SpUserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SpAdmListUsers {

    private SpUserService userService;
    private UserRoleService userRoleService;
    private SpUserValidator spUserValidator = new SpUserValidator();

    public SpAdmListUsers(SpUserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @RequestMapping(value = "/inAdmUsers")
    public String admUsersList(Model model, HttpServletRequest request){

        model.addAttribute("userAppList", userService.listUserApp());
        model.addAttribute("userRoleList", userRoleService.listUserRole());


        return "in_adm_users";

    }

    @RequestMapping(value = "/inAdmEditUser")
    public String admEditUser(Model model, HttpServletRequest request){//todo liste ROLE zrobic jako checkboxy

        int userAppId = ServletRequestUtils.getIntParameter(request, "userId", -1);

        if(userAppId > 0){
            SpUserApp userApp = userService.getUserApp(userAppId);
            userApp.setPassword("");
            //model.addAttribute("userRole", );
            model.addAttribute("admUserEdit", userApp);


            userApp.getUserRole();
            List<String> rolesStringList = new ArrayList<String>();
            for (UserRole ur:userApp.getUserRole()) {
                rolesStringList.add(ur.getRole());
                System.out.println(ur.getRole());

            }
            
            model.addAttribute("admUserRoleEdit", userApp);

            System.out.println(userApp.getUserRole().size());



        }else{
            System.out.println("bledny Id usera");//todo usunac
        }

        return "in_adm_user_edit";

    }


    @RequestMapping(value = "/inAdmUserEdit", method = RequestMethod.POST)
    public String editAdmUserWoPass(@Valid @ModelAttribute("admUserEdit") SpUserApp userApp, BindingResult result, Model model, HttpServletRequest request) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"
        userApp.setPassword("");
        userApp.setConfirmPassword("");
        userApp.setUserRole(userService.getUserApp(userApp.getId()).getUserRole());
        System.out.println(userApp.getPesel()+" "+userApp.getId()+" "+userApp.getPassword());
        spUserValidator.validateWoPassword(userApp, result);

        if (result.getErrorCount() == 0) {
            if (userApp.getId() == 0) {
                //teoretycznei dodawanie usera
                System.out.println("nie powinno sie to pojawić");
            } else {
                userService.editUserAppWoPassword(userApp);
                return "redirect:/inAdmEditUser.html?userId="+userApp.getId();
            }
            //return "redirect:registered.html";
            //SpVerifyToken verifyToken = new SpVerifyToken();
            //String USRtoken = UUID.randomUUID().toString();
            //verifyToken.setVerifyToken(USRtoken);
            //verifyTokenService.addVerifyToken(verifyToken);


        }



        System.out.println("są bledy validatora");
        //todo SpUserApp user = userService.findByPesel(userApp.getPesel());/
        SpUserApp user = userService.getUserApp(userApp.getId());
        user.setPassword("");
        user.setConfirmPassword("");
        model.addAttribute("userPassEdit", user);
        model.addAttribute("admUserRoleEdit", user);


        return "in_adm_user_edit";
    }


    @RequestMapping(value = "/inAdmRoleUserEdit", method = RequestMethod.POST)
    public String editAdmUserRoleWoPass(@RequestParam("id") long uID, @RequestParam(required = false, name = "ROLE_ADMIN") boolean rAdmin, @RequestParam(required = false, name = "ROLE_MANAGER") boolean rManager, @RequestParam(required = false, name = "ROLE_ROBPROG") boolean rMieszkaniec, Model model, HttpServletRequest request) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"
        SpUserApp userApp = userService.getUserApp(uID);
        userApp.setPassword("");
        userApp.setConfirmPassword("");
        System.out.println(userApp.getPesel()+" "+userApp.getId()+" "+userApp.getPassword());
        model.addAttribute("admUserEdit", userApp);

        System.out.println("a "+rAdmin+" z "+rManager+" m "+rMieszkaniec);

        Set<UserRole> tempUserRoleSet = new HashSet<UserRole>(0);
        if (rAdmin){
            UserRole tempUserRole = userRoleService.getUserRoleByName("ROLE_ADMIN");
            tempUserRoleSet.add(tempUserRole);

        }
        if (rManager){
            UserRole tempUserRole = userRoleService.getUserRoleByName("ROLE_MANAGER");
            tempUserRoleSet.add(tempUserRole);
        }
        if (rMieszkaniec){
            UserRole tempUserRole = userRoleService.getUserRoleByName("ROLE_ROBPROG");
            tempUserRoleSet.add(tempUserRole);
        }
        userApp.setUserRole(tempUserRoleSet);
        System.out.println("roles: "+userApp.getUserRole());//todo delete

        //spUserValidator.validate(userApp, result);


            if (userApp.getId() == 0) {
                //teoretycznei dodawanie usera
                System.out.println("nie powinno sie to pojawić");
            } else {
                userService.editUserAppRole(userApp);
                return "redirect:/inAdmEditUser.html?userId="+userApp.getId();
            }
            //return "redirect:registered.html";
            //SpVerifyToken verifyToken = new SpVerifyToken();
            //String USRtoken = UUID.randomUUID().toString();
            //verifyToken.setVerifyToken(USRtoken);
            //verifyTokenService.addVerifyToken(verifyToken);




        System.out.println("są bledy validatora");
        SpUserApp user = userService.findByPesel(userApp.getPesel());
        user.setPassword("");
        user.setConfirmPassword("");
        model.addAttribute("userPassEdit", user);


        return "in_adm_user_edit";
    }

    @RequestMapping("/admUserDelete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId){
        userService.removeUserApp(userId);
        return "redirect:/inAdmUsers.html";
    }


    @RequestMapping(value = "/inAdmActivateUser")
    public String admActivUser(Model model, HttpServletRequest request){

        int userAppId = ServletRequestUtils.getIntParameter(request, "userId", -1);
        System.out.println("e val "+ServletRequestUtils.getBooleanParameter(request, "e", true));
        boolean enable = ServletRequestUtils.getBooleanParameter(request, "e", true);



        if(userAppId > 0){
            SpUserApp userApp = userService.getUserApp(userAppId);

            System.out.println("user "+userApp.getPesel()+" "+enable);

            userService.activateUserApp(userApp, !enable);

            model.addAttribute("userAppList", userService.listUserApp());
            model.addAttribute("userRoleList", userRoleService.listUserRole());



        }else{
            System.out.println("bledny Id usera");//todo usunac
        }

        return "redirect:/inAdmUsers.html";

    }


/*
    @ModelAttribute("rolesStringList")
    public List<String> getRolesStringList() {
        List<String> rolesStringList = new ArrayList<String>();
        rolesStringList.add("ROLE_ADMIN");
        rolesStringList.add("ROLE_MANAGER");
        rolesStringList.add("ROLE_ROBPROG");
        return rolesStringList;
    }*/



}
