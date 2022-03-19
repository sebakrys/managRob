package pl.skrys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.skrys.app.Project;
import pl.skrys.app.SpStation;
import pl.skrys.service.SpStationService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RestController
public class AjaxControl {

    private SpStationService stationService;

    public AjaxControl(SpStationService stationService) {
        this.stationService = stationService;
    }

    @ResponseBody
    @RequestMapping(value = "/searchStation")
    public String searchStation(@RequestParam("nazwa") String nazwa, @RequestParam("pId") Long pId, Model model, HttpServletRequest request, Principal principal){

        List<SpStation> stations = stationService.searchStationsWithName(nazwa);


        System.out.println("searchStation "+nazwa+" "+stations.size()+" ");

        String responseString = "<table>";
        //String responseString = "";
        int i = 0;
        for (SpStation sta: stations) {
            //wersja błękitna responseString=responseString+"<tr id=\"seRe"+i+"\"><td><a type=\"button\" class=\"btn btn-info p-1 w-100\" href=\"projectStationsManag.html?sId="+sta.getId()+"&bId="+pId+"\">"+sta.getNazwa()+" "+sta.getHala()+" "+sta.getLinia()+"</a></td></tr>";
            responseString=responseString+"<tr id=\"seRe"+i+"\"><td><a type=\"button\" class=\"btn btn-outline-info p-1 w-100\" href=\"projectStationsManag.html?sId="+sta.getId()+"&bId="+pId+"\">"+sta.getNazwa()+" "+sta.getHala()+" "+sta.getLinia()+"</a></td></tr>";

            i++;
        }
        responseString=responseString+"</table>";

        model.addAttribute("foundStations", stations);


        return responseString;
    }
}
