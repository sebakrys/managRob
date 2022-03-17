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
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpStation;
import pl.skrys.service.ProjectService;
import pl.skrys.service.SpRobotService;
import pl.skrys.service.SpStationService;

import javax.validation.Valid;

@Secured({"ROLE_MANAGER", "ROLE_ROBPROG", "ROLE_ADMIN"})
@Controller
public class SearchController {

    private SpRobotService spRobotService;
    private SpStationService spStationService;
    private ProjectService projectService;

    public SearchController(SpRobotService spRobotService, SpStationService spStationService, ProjectService projectService){
        this.spRobotService = spRobotService;
        this.spStationService = spStationService;
        this.projectService = projectService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchRobot(Model model, @RequestParam String robotNumber ){
        SpRobot robot = spRobotService.getSpRobot(robotNumber);
        SpStation station = robot.getStation();
        Project project = station.getProject();

        model.addAttribute("robot", robot);
        model.addAttribute("station", station);
        model.addAttribute("project", project);
        return "in_search_robot";
    }
}
