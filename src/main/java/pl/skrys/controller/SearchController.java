package pl.skrys.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.skrys.app.Project;
import pl.skrys.app.SearchedObject;
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpStation;
import pl.skrys.service.ProjectService;
import pl.skrys.service.SpRobotService;
import pl.skrys.service.SpStationService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Secured({"ROLE_MANAGER", "ROLE_ROBPROG", "ROLE_ADMIN"})
@Controller
public class SearchController {

    private SpRobotService spRobotService;


    public SearchController(SpRobotService spRobotService, SpStationService spStationService, ProjectService projectService){
        this.spRobotService = spRobotService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchRobot(Model model, @RequestParam String robotNumber ){
        List<SpRobot> robot = spRobotService.getRobotsByName(robotNumber);
        List<SpStation> station = new ArrayList<SpStation>();
        List<Project> project = new ArrayList<Project>();
        List<SearchedObject> searchedObject = new ArrayList<SearchedObject>();
        for (int i = 0 ; i < robot.size(); i++){
            station.add(robot.get(i).getStation());
            project.add(station.get(i).getProject());
            SearchedObject tmp = new SearchedObject(
                    station.get(i).getNazwa(),
                    station.get(i).getNazwa(),
                    robot.get(i).getRobotNumber(),
                    robot.get(i).getId());
            searchedObject.add(tmp);
        }


//
//        model.addAttribute("robot", robot);
//        model.addAttribute("station", station);
//        model.addAttribute("project", project);
        model.addAttribute("object",searchedObject);
        return "in_search_robot";
    }
}
