package pl.skrys.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import pl.skrys.app.*;
import pl.skrys.dao.ProjectRepository;
import pl.skrys.dao.SpUserRepository;
import pl.skrys.service.*;
import pl.skrys.validator.SpUserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ProjectsControl {

    private ProjectService projectService;
    private ProjectRepository projectRepository;

    private SpUserService userService;
    private UserRoleService userRoleService;
    private SpStationService stationService;
    private SpRobotService robotService;
    private SpRobotStatusService robotStatusService;
    private SpUserRepository userRepository;
    private SpUserValidator validator  = new SpUserValidator();

    public ProjectsControl(ProjectService projectService, ProjectRepository projectRepository, SpUserService userService, UserRoleService userRoleService, SpStationService stationService, SpRobotService robotService, SpRobotStatusService robotStatusService, SpUserRepository userRepository) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.stationService = stationService;
        this.robotService = robotService;
        this.robotStatusService = robotStatusService;
        this.userRepository = userRepository;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ROBPROG"})
    @RequestMapping(value = "/inProjects")
    public String managProjectsList(Model model, HttpServletRequest request, Principal principal){

        System.out.println("ROLE_ADMIN");

        String userPesel = principal.getName();

        if(userPesel != null) {
            boolean admin = false;
            boolean robprog = false;
            boolean manager = false;

            SpUserApp userApp = userService.findByPesel(userPesel);

            for (UserRole ur: userApp.getUserRole()) {
                if(ur.getRole().equals("ROLE_ADMIN")){
                    admin = true;
                }else if(ur.getRole().equals("ROLE_MANAGER")){
                    manager = true;
                }else if(ur.getRole().equals("ROLE_ROBPROG")){
                    robprog = true;
                }
            }





            if(admin){
                System.out.println("ROLE_ADMIN");
                projectService.listProjects();
                System.out.println("ROLE_ADMIN2");

                model.addAttribute("addProject", new Project());
                model.addAttribute("projectsList", projectService.listProjects());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
                model.addAttribute("stationsList", stationService.listStations());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace


                Set<Long> projectIdManagSet = new HashSet<Long>(0);
                for (Project managProj:
                        projectService.listProjects()) {

                    projectIdManagSet.add(managProj.getId());
                }
                model.addAttribute("projectsManagList", projectIdManagSet);
                return "in_projects";
            }

            Set<Project> ProjectSet = new HashSet<Project>(0);
            Set<Long> ProjectIdManagSet = new HashSet<Long>(0);
            Set<SpStation> StationSet = userApp.getStations();



            if(robprog){


                for (SpStation tempStation : StationSet) {
                    ProjectSet.add(tempStation.getProject());
                }
            }



            if(manager){
                for (Project managProj:
                        userApp.getProjects()) {
                    ProjectSet.add(managProj);
                    ProjectIdManagSet.add(managProj.getId());
                }
            }

            //model.addAttribute("addStation", new SpStation());
            //todo dla mieszkanca i managers
            model.addAttribute("projectsList", ProjectSet);//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
            model.addAttribute("robotList", StationSet);//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace

            model.addAttribute("projectsManagList", ProjectIdManagSet);

        }


        return "in_projects";

    }

    @RequestMapping(value = "/inAddNewProject", method = RequestMethod.POST)
    public String addNewProject(@Valid @ModelAttribute("addProject") Project project, BindingResult result, Model model, HttpServletRequest request) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"

        System.out.println(project.getNazwa()+project.getCity()+project.getCountry()+project.getStandard());

        model.addAttribute("projectsList", projectService.listProjects());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
        model.addAttribute("stationsList", stationService.listStations());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace




        System.out.println(project.getNazwa()+" "+project.getId()+" ");
        validator.validateProject(project, result);




        if (result.getErrorCount() == 0) {
            if (project.getId() == 0) {
                //dodawanie projektu
                System.out.println("dodawanie projektu");
                projectService.addProject(project);
                return "redirect:/inProjects.html";
            } else {
                projectService.editProject(project);

                return "redirect:/inProjects.html";
            }

        }

        System.out.println("są bledy validatora");


        return "in_projects";
    }

    @RequestMapping(value = "/inProjectAddManager", method = RequestMethod.POST)
    public String addManagersToProject(@RequestParam(required = false, name = "ZaIds") List<Long> zaIds, @RequestParam("project.id") long bID, Model model, HttpServletRequest request) {


        List<SpUserApp> managersUsers = userRepository.findBySpecificRoles("ROLE_MANAGER");



        if(zaIds!=null) {
            System.out.println("ilosc para: " + zaIds.size());
        }
        System.out.println("(addManagersToProject)id stacji: " + bID);


        if (bID > 0) {
            List<SpUserApp> managersProject = userService.getUserAppByProject(bID);
            Project project = projectService.getProject(bID);

            for (SpUserApp tempManag : managersProject) {
                //usuwanie managers
                System.out.println("usuwanie managers1");
                userService.removeUserProject(tempManag, project);
                System.out.println("usuwanie managers1");
            }
            if (zaIds!=null) {
                for (long zaId : zaIds) {//dodawanie managers
                    System.out.println("para: " + zaId);
                    Set<Project> newProjects = new HashSet<Project>(0);
                    newProjects.add(project);
                    SpUserApp tempUserApp = userService.getUserApp(zaId);
                    tempUserApp.setProjects(newProjects);
                    userService.addUserProjects(tempUserApp);
                }
            }

        }

        return "redirect:/projectStationsManag.html?bId="+bID;//todo
    }

    @Secured({"ROLE_MANAGER", "ROLE_ROBPROG", "ROLE_ADMIN"})
    @RequestMapping(value = "/projectStationsManag")
    public String robotsManagList(Model model, HttpServletRequest request, Principal principal){

        int projectId = ServletRequestUtils.getIntParameter(request, "bId", -1);
        boolean stationExists = ServletRequestUtils.getBooleanParameter(request, "stationExists", false);
        String userPesel = principal.getName();

        boolean managerProject = false;
        boolean admin = false;

        if(userPesel != null) {

            boolean robprog = false;
            boolean manager = false;


            SpUserApp userApp = userService.findByPesel(userPesel);
            for (UserRole ur: userApp.getUserRole()) {
                if(ur.getRole().equals("ROLE_ADMIN")){
                    admin = true;
                }else if(ur.getRole().equals("ROLE_MANAGER")){
                    manager = true;
                }else if(ur.getRole().equals("ROLE_ROBPROG")){
                    robprog = true;
                }
            }

            Set<Project> userProjects =  userApp.getProjects();
            for (Project tempProject:
                    userProjects) {
                if(tempProject.getId()==projectId){
                    managerProject=true;
                    break;
                }
            }




            if(managerProject && manager || admin){
                System.out.println("MANAGER I POSIADA TEN Project");
                model.addAttribute("managerB", true);

                Project tempProj = projectService.getProject(projectId);
                List<SpStation> tempListStacje = stationService.listStationsByProject(tempProj.getId());

                model.addAttribute("stationsList", tempListStacje);

            }else{
                System.out.println("NIE JEST !!!!!MANAGER I POSIADA TEN STATION");
                model.addAttribute("managerB", false);

                Set<SpStation> userStations = userApp.getStations();
                Set<SpStation> nUserStations = new HashSet<SpStation>(0);

                for (SpStation tempStation :
                        userStations) {
                    if(tempStation.getProject().getId()==projectId){
                        nUserStations.add(tempStation);
                    }
                }

                model.addAttribute("stationsList", nUserStations);

            }

            //todo
            Set<Long> spStationIdManagSet = new HashSet<Long>(0);
            if(manager){
                for (SpStation managBuild:
                        userApp.getStations()) {

                    spStationIdManagSet.add(managBuild.getId());
                }
            }
            model.addAttribute("stationsManagList", spStationIdManagSet);
            //todo
        }



        SpStation station = new SpStation();
        station.setProject(projectService.getProject(projectId));




        List<SpUserApp> managersUsers = userRepository.findBySpecificRoles("ROLE_MANAGER");

        List<SpUserApp> managersProject = userService.getUserAppByProject(projectId);
        System.out.println("l wszy MANAGERS "+managersUsers.size());
        System.out.println("l  MANAGERS "+managersProject.size());

        model.addAttribute("managers", managersUsers);
        model.addAttribute("managersStacji", managersProject);





        model.addAttribute("addStation", station);
        model.addAttribute("addManager", station);

        model.addAttribute("selectedProject", projectService.getProject(projectId));

        //System.out.println("Liczba mieszkań "+projectService.getProject(projectId).getStacje().size()+" stacja "+projectService.getProject(projectId).getId());




        //model.addAttribute("manager", stationService.listManagers(stationService.getStation(stationId)));//todo wyswietlic liste wszytskich managers i miec jako selected tych co są aktualnie



        model.addAttribute("stationExists", stationExists);



        return "in_project_stations_manager";

    }




    @RequestMapping("/deleteProject/{projectId}")
    public String deleteStation(@PathVariable("projectId") Long projectId){
        System.out.println("Usuwanie  projektu "+projectId);

        List<SpUserApp> managersProject = userService.getUserAppByProject(projectId);
        Project project = projectService.getProject(projectId);
        for (SpUserApp tempManag : managersProject) {
            //usuwanie managers
            userService.removeUserProject(tempManag, project);
        }

        projectService.removeProject(projectId);
        return "redirect:/inProjects";
    }
}