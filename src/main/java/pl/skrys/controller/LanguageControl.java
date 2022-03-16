package pl.skrys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class LanguageControl {

    @ResponseBody
    @RequestMapping(value = "?lang", method = RequestMethod.GET)
    public String changeLang(){
        System.out.println("lang change");
        return"refresh";//lub "reload"
    }
}
