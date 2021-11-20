package pl.skrys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.skrys.configuration.SecurityConfig;

//@Controller
public class SpringSecurityCustomPagesControl {

    @RequestMapping(value = "/login")
    public String customLogin(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              Model model){
        if(error != null){
            model.addAttribute("error", "Invalid username or password");
        }

        if(logout != null){
            model.addAttribute("msg", "Logged out successfull");
        }

        return "login";
    }

    @RequestMapping(value = "/accessDenied")
    public String accessDenied(){
        System.out.println("accessDenied");
        return "access_denied";
    }

}
