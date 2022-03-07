package pl.skrys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.skrys.app.UserRole;
import pl.skrys.service.UserRoleService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserRoleControl {

    private UserRoleService userRoleService;

    @Autowired
    public UserRoleControl(UserRoleService userRoleService){
        this.userRoleService = userRoleService;
    }

    @RequestMapping(value = "/userRole")
    public String showUserRole(Model model){
        model.addAttribute("userRole", new UserRole());



        return "userRole";
    }

    @RequestMapping(value = "/addUserRole")
    public String addUserRole(@ModelAttribute("userRole") UserRole userRole, BindingResult result){
        userRoleService.addUserRole(userRole);
        return "redirect:appUsers.html";
    }

    @ModelAttribute("rolesStringList")
    public List<String> getRolesStringList() {
        List<String> webFrameworkList = new ArrayList<String>();
        webFrameworkList.add("ROLE_ADMIN");
        webFrameworkList.add("ROLE_MANAGER");
        webFrameworkList.add("ROLE_ROBPROG");
        return webFrameworkList;
    }

}
