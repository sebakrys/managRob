package pl.skrys.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.skrys.app.*;
import pl.skrys.service.ProjectService;
import pl.skrys.service.SpRobotService;
import pl.skrys.service.SpStationService;
import pl.skrys.service.SpUserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Secured({"ROLE_MANAGER", "ROLE_ROBPROG", "ROLE_ADMIN"})
@Controller
public class SearchController {

    private SpRobotService spRobotService;
    private SpUserService userService;

    public SearchController(SpRobotService spRobotService, SpUserService userService) {
        this.spRobotService = spRobotService;
        this.userService = userService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchRobot(Model model, @RequestParam String robotNumber, Principal principal){
        String userEmail = principal.getName();
        SpUserApp userApp = null;
        boolean admin = false;
        boolean robprog = false;
        boolean manager = false;
        if(userEmail != null) {

            userApp = userService.findByEmail(userEmail);

            for (UserRole ur: userApp.getUserRole()) {
                if(ur.getRole().equals("ROLE_ADMIN")){
                    admin = true;
                }else if(ur.getRole().equals("ROLE_MANAGER")){
                    manager = true;
                }else if(ur.getRole().equals("ROLE_ROBPROG")){
                    robprog = true;
                }
            }
        }
        List<SpRobot> robot = spRobotService.getRobotsByName(robotNumber);
        List<SpStation> station = new ArrayList<SpStation>();
        List<Project> project = new ArrayList<Project>();
        List<SearchedObject> searchedObject = new ArrayList<SearchedObject>();
        Set<SpRobot> userRobots = userApp.getRobot();
        Set<Project> userProjects = userApp.getProjects();
        int j = 0;
        for (int i = 0 ; i < robot.size(); i++){
            boolean nr = false;
            if(robprog) {
                for (SpRobot r : userRobots) {
                    if (robot.get(i).getId() == r.getId()) {
                        nr = true;
                        break;
                    }
                }
            }
            if(manager){
                for (Project p : userProjects) {
                    if (robot.get(i).getStation().getProject().getId() == p.getId()) {
                        nr = true;
                        break;
                    }
                }
            }

            if(admin || robprog && nr || manager && nr) {
                station.add(robot.get(i).getStation());
                //project.add(station.get(i).getProject());
                SearchedObject tmp = new SearchedObject(
                        station.get(j).getProject().getNazwa(),
                        station.get(j).getNazwa(),
                        robot.get(i).getRobotNumber(),
                        robot.get(i).getId());
                searchedObject.add(tmp);
                j++;
            }
        }


//
//        model.addAttribute("robot", robot);
//        model.addAttribute("station", station);
//        model.addAttribute("project", project);
        model.addAttribute("object",searchedObject);
        return "in_search_robot";
    }
}
