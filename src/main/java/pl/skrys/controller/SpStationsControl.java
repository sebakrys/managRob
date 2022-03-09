package pl.skrys.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.skrys.app.*;
import pl.skrys.dao.SpUserRepository;
import pl.skrys.service.*;
import pl.skrys.validator.SpUserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class SpStationsControl {


    public static double cprad_stawka = 0.65;
    public static double cgaz_stawka = 1.3;
    public static double cwoda_ciepla_stawka = 35.0;
    public static double cwoda_zimna_stawka = 6.0;
    public static double cscieki_stawka = 9.1;
    public static double cogrzewanie_stawka = 0.3;
    public static double cfunduszRemontowy_stawka = 2.00;

    public static double prad_ryczalt = 65.0;//kWh na osobe
    public static double gaz_ryczalt = 16;//m3 na osobe
    public static double woda_ciepla_ryczalt = 0.500; //m3 na osobe
    public static double woda_zimna_ryczalt = 1.000; // m3 na osobe
    public static double scieki_ryczalt = 1.500; // m3 na osobe
    public static double ogrzewanie_ryczalt = 50;// kwh na osobe
    public static double funduszRemontowy_ryczalt = 60;// średnio na mieszkanie






    private SpUserService userService;
    private UserRoleService userRoleService;
    private SpStationService stationService;
    private SpRobotService robotService;
    private SpRobotStatusService robotStatusService;
    private SpUserRepository userRepository;
    private SpUserValidator validator  = new SpUserValidator();

    public SpStationsControl(SpUserService userService, UserRoleService userRoleService, SpStationService stationService, SpRobotService robotService, SpRobotStatusService robotStatusService, SpUserRepository userRepository) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.stationService = stationService;
        this.robotService = robotService;
        this.robotStatusService = robotStatusService;
        this.userRepository = userRepository;
    }


    @Secured({"ROLE_MANAGER", "ROLE_ROBPROG", "ROLE_ADMIN"})
    @RequestMapping(value = "/inStations")
    public String managStationsList(Model model, HttpServletRequest request, Principal principal){

        System.out.println("ROLE_MANAGER lub ROBPROG");

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

                model.addAttribute("addStation", new SpStation());
                model.addAttribute("stationsList", stationService.listStations());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
                model.addAttribute("robotList", robotService.listRobots());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace


                Set<Long> spStationIdManagSet = new HashSet<Long>(0);
                for (SpStation managBuild:
                        stationService.listStations()) {

                    spStationIdManagSet.add(managBuild.getId());
                }
                model.addAttribute("stationsManagList", spStationIdManagSet);
                return "in_stations";
            }

            Set<SpStation> spStationSet = new HashSet<SpStation>(0);
            Set<Long> spStationIdManagSet = new HashSet<Long>(0);
            Set<SpRobot> spRobotSet = userApp.getRobot();



            if(robprog){


                for (SpRobot tempRobot : spRobotSet) {
                    spStationSet.add(tempRobot.getStation());
                }
            }



            if(manager){
                for (SpStation managBuild:
                userApp.getStations()) {
                    spStationSet.add(managBuild);
                    spStationIdManagSet.add(managBuild.getId());
                }
            }

            //model.addAttribute("addStation", new SpStation());
            //todo dla mieszkanca i managers
            model.addAttribute("stationsList", spStationSet);//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
            model.addAttribute("robotList", spRobotSet);//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace

            model.addAttribute("stationsManagList", spStationIdManagSet);

        }


        return "in_stations";

    }




    @RequestMapping(value = "/inAddNewStation", method = RequestMethod.POST)
    public String addNewStation(@Valid @ModelAttribute("addStation") SpStation station, BindingResult result, Model model, HttpServletRequest request) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"


        model.addAttribute("stationsList", stationService.listStations());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
        model.addAttribute("robotList", robotService.listRobots());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace




        System.out.println(station.getNazwa()+" "+station.getId()+" ");
        validator.validateStation(station, result);




        if (result.getErrorCount() == 0) {
            if (station.getId() == 0) {
                //dodawanie stacji
                System.out.println("dodawanie stacji");
                stationService.addStation(station);
                return "redirect:/inStations.html";
            } else {
                stationService.editStation(station);

                return "redirect:/inStations.html";
            }

        }

        System.out.println("są bledy validatora");


        return "in_stations";
    }

    @Secured({"ROLE_MANAGER", "ROLE_ROBPROG", "ROLE_ADMIN"})
    @RequestMapping(value = "/stationRobotsManag")
    public String robotsManagList(Model model, HttpServletRequest request, Principal principal){

        int stationId = ServletRequestUtils.getIntParameter(request, "bId", -1);
        boolean robotExists = ServletRequestUtils.getBooleanParameter(request, "robotExists", false);
        String userPesel = principal.getName();

        boolean managerStation = false;
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

            Set<SpStation> userStations =  userApp.getStations();
            for (SpStation tempStation:
            userStations) {
                if(tempStation.getId()==stationId){
                    managerStation=true;
                    break;
                }
            }




            if(managerStation && manager || admin){
                System.out.println("MANAGER I POSIADA TEN STATION");
                model.addAttribute("managerB", true);
                model.addAttribute("robotsList", stationService.getStation(stationId).getRobot());

            }else{
                System.out.println("NIE JEST !!!!!MANAGER I POSIADA TEN STATION");
                model.addAttribute("managerB", false);

                Set<SpRobot> userRobots = userApp.getRobot();
                Set<SpRobot> nUserRobots = new HashSet<SpRobot>(0);

                for (SpRobot tempRobot :
                     userRobots) {
                    if(tempRobot.getStation().getId()==stationId){
                        nUserRobots.add(tempRobot);
                    }
                }

                model.addAttribute("robotsList", nUserRobots);

            }
        }



        SpRobot robot = new SpRobot();
        robot.setStation(stationService.getStation(stationId));




        List<SpUserApp> managersUsers = userRepository.findBySpecificRoles("ROLE_MANAGER");

        List<SpUserApp> managersStacji = userService.getUserAppByStation(stationId);
        System.out.println("l wszy MANAGERS "+managersUsers.size());
        System.out.println("l  MANAGERS "+managersStacji.size());

        model.addAttribute("managers", managersUsers);
        model.addAttribute("managersStacji", managersStacji);





        model.addAttribute("addRobot", robot);
        model.addAttribute("addManager", robot);

        model.addAttribute("selectedStation", stationService.getStation(stationId));

        System.out.println("Liczba mieszkań "+stationService.getStation(stationId).getRobot().size()+" stacja "+stationService.getStation(stationId).getId());




        //model.addAttribute("manager", stationService.listManagers(stationService.getStation(stationId)));//todo wyswietlic liste wszytskich managers i miec jako selected tych co są aktualnie



        model.addAttribute("robotExists", robotExists);



        return "in_station_robots_manager";

    }






    @RequestMapping(value = "/inAddNewRobot", method = RequestMethod.POST)
    public String addNewRobot(@Valid @ModelAttribute("addRobot") SpRobot robot, BindingResult result, Model model, HttpServletRequest request, Principal principal) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"


        //model.addAttribute("stationsList", stationService.listStations());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
        //model.addAttribute("robotList", robotService.listRobots());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace


        System.out.println("Id Stacji z mieszkania: "+robot.getStation().getId());
        System.out.println("RobotNumer: "+robot.getRobotNumber()+" ");
        SpStation station = stationService.getStation(robot.getStation().getId());
        System.out.println("RobotNumer: "+ robot.getRobotNumber()+" "+robot.getStation().getNazwa()+" ");
        robot.setStation(station);


        validator.validateRobot(robot, result);


        model.addAttribute("robotsList", station.getRobot());

        //boolean duplicate = false;

        for (SpRobot tempRobot:
        station.getRobot()) {//todo robotNumber duplikaty
            System.out.println("spr duplikaty "+tempRobot.getRobotNumber()+" _ "+robot.getRobotNumber());

            if(tempRobot.getRobotNumber().equals(robot.getRobotNumber())){
                System.out.println("Duplikat nr mieszkania");
                //duplicate = true;
                result.rejectValue("robotNumber", "error.alreadyExists");
                model.addAttribute("robotExists", true);
                break;
            }
        }



        if (result.getErrorCount() == 0) {
            if (robot.getId() == 0) {
                //dodawanie mieszkania

                SpRobotStatus robotStatus = new SpRobotStatus();
                robotStatus.setRobot(robot);

                String userPesel = principal.getName();
                if(userPesel!=null){
                    robotStatus.setRobotyk(userService.findByPesel(userPesel));
                }

                Date date = new Date();
                LocalDateTime currentdateTime = LocalDateTime.now();

                date.setYear(currentdateTime.getYear());
                date.setDate(currentdateTime.getDayOfMonth());
                date.setMonth(currentdateTime.getMonthValue());
                date.setHours(currentdateTime.getHour());
                date.setMinutes(currentdateTime.getMinute());
                date.setSeconds(currentdateTime.getSecond());
                //System.out.println("Y: "+date.getYear());
                //date.setDate(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));

                //System.out.println("D: "+date.getDate());

                //System.out.println("M: "+date.getMonth());


                /*
                //todo stawki
                //fundusz remontowy 2 zł za m2
                //gaz 1.3 zł za m3
                //ogrzewanie 0.3 zł za kWh
                //prad 0.65 zł za kWh
                //ścieki 9 zł 1m2
                //woda zimna 6 zł
                //woda ciepla 35zł
                robotStatus.setFunduszRemontowy_stawka(cfunduszRemontowy_stawka);
                robotStatus.setGaz_stawka(cgaz_stawka);
                robotStatus.setOgrzewanie_stawka(cogrzewanie_stawka);
                robotStatus.setPrad_stawka(cprad_stawka);
                robotStatus.setScieki_stawka(cscieki_stawka);
                robotStatus.setWoda_zimna_stawka(cwoda_zimna_stawka);
                robotStatus.setWoda_ciepla_stawka(cwoda_ciepla_stawka);
                //todo stawki
                 */




                //System.out.println("Y: "+date.getYear());
                //System.out.println("D: "+date.getDate());
                //System.out.println("M: "+date.getMonth());

                /*
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, currentdate.getYear());
                cal.set(Calendar.MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);


                date = cal.getTime();*/



                System.out.println(date.getYear()+" miesiac: "+date.getMonth()+" dzien "+date.getDate());
                //Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

                //System.out.println(date.getYear()+" miesiac: "+date.getMonth()+" dzien "+date.getDate());/
                //System.out.println("data:"+date);
                robotStatus.setData(date);

                List<SpRobotStatus> robotStatusList = new ArrayList<SpRobotStatus>();
                robotStatusList.add(robotStatus);

                robot.setRobotStatus(robotStatusList);

                System.out.println("dodawanie mieszkania");
                robotService.addRobot(robot);//dodac doawanie do robotStatus
                robotStatusService.addRobotStatus(robotStatus);



                return "redirect:/stationRobotsManag.html?bId="+station.getId();
            } else {
                //robotService.editRobot(robot);

                return "redirect:/stationRobotsManag.html?bId="+station.getId();
            }

        }

        model.addAttribute("addManager", robot);
        System.out.println("są bledy validatora");


        return "redirect:/stationRobotsManag.html?bId="+station.getId();
    }


    @RequestMapping(value = "/inStationAddManager", method = RequestMethod.POST)
    public String addManagersToStation(@RequestParam(required = false, name = "ZaIds") List<Long> zaIds,@RequestParam("station.id") long bID, Model model, HttpServletRequest request) {


        List<SpUserApp> managersUsers = userRepository.findBySpecificRoles("ROLE_MANAGER");



        if(zaIds!=null) {
            System.out.println("ilosc para: " + zaIds.size());
        }
            System.out.println("id stacji: " + bID);


            if (bID > 0) {
                List<SpUserApp> managersStacji = userService.getUserAppByStation(bID);
                SpStation station = stationService.getStation(bID);

                for (SpUserApp tempManag : managersStacji) {
                    //usuwanie managers
                    userService.removeUserStation(tempManag, station);
                }
                if (zaIds!=null) {
                    for (long zaId : zaIds) {//dodawanie managers
                        System.out.println("para: " + zaId);
                        Set<SpStation> newStations = new HashSet<SpStation>(0);
                        newStations.add(station);
                        SpUserApp tempUserApp = userService.getUserApp(zaId);
                        tempUserApp.setStations(newStations);
                        userService.addUserStations(tempUserApp);
                    }
                }

            }

        return "redirect:/stationRobotsManag.html?bId="+bID;//todo
    }


    @RequestMapping("/deleteStation/{stationId}")
    public String deleteStation(@PathVariable("stationId") Long stationId){
        System.out.println("Usuwanie  stacji "+stationId);

        List<SpUserApp> managersStacji = userService.getUserAppByStation(stationId);
        SpStation station = stationService.getStation(stationId);
        for (SpUserApp tempManag : managersStacji) {
            //usuwanie managers
            userService.removeUserStation(tempManag, station);
        }

        stationService.removeStation(stationId);
        return "redirect:/inStations";
    }




    @RequestMapping(value = "/inRobotManag")
    public String robotManag(Model model, HttpServletRequest request, Principal principal){



        int robotId = ServletRequestUtils.getIntParameter(request, "fId", -1);
        SpRobot srobot = robotService.getRobot(robotId);


        String userPesel = principal.getName();

        boolean managerStation = false;
        boolean admin = false;

        if(userPesel != null) {

            boolean robprog = false;
            boolean manager = false;


            SpUserApp userApp = userService.findByPesel(userPesel);
            for (UserRole ur : userApp.getUserRole()) {
                if (ur.getRole().equals("ROLE_ADMIN")) {
                    admin = true;
                } else if (ur.getRole().equals("ROLE_MANAGER")) {
                    manager = true;
                } else if (ur.getRole().equals("ROLE_ROBPROG")) {
                    robprog = true;
                }
            }

            Set<SpStation> userStations = userApp.getStations();
            for (SpStation tempStation :
                    userStations) {
                if (tempStation.getId() == srobot.getStation().getId()) {
                    managerStation = true;
                    break;
                }
            }


            if(managerStation && manager || admin){
                System.out.println("MANAGER I POSIADA TEN STATION");
                model.addAttribute("managerB", true);
                model.addAttribute("robotStatusList", robotStatusService.getRobotStatusFromRobot(srobot.getId()));//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace


            }else{
                System.out.println("NIE JEST !!!!!MANAGER I POSIADA TEN STATION");
                model.addAttribute("managerB", false);
                model.addAttribute("robotStatusList", null);//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace


            }


        }



        model.addAttribute("selectedRobot", srobot);
        model.addAttribute("selectedStation", srobot.getStation());

        List<SpUserApp> mieszkancyUsers = userRepository.findBySpecificRoles("ROLE_ROBPROG");



        List<SpUserApp> robprogsRobot = userService.getUserAppByRobot(robotId);

        model.addAttribute("robprogs", mieszkancyUsers);//todo wszyscy uzytkownicy z rola ROBPROG
        model.addAttribute("robprogsRobot", robprogsRobot);// wszyscy Lokatorzy
        model.addAttribute("addRobprog", srobot);

        SpRobotStatus lastRobotStatus = null;
        for (SpRobotStatus tempRobotStatus : srobot.getRobotStatus()) {
            if(lastRobotStatus==null){
                lastRobotStatus=tempRobotStatus;
            }else if(lastRobotStatus.getData().getTime()<tempRobotStatus.getData().getTime()){
                lastRobotStatus = tempRobotStatus;
            }
        }

        model.addAttribute("addStatus", lastRobotStatus);

        /*
        double kwota = 0.0;
        kwota+=lastRobotStatus.getFunduszRemontowy()*lastRobotStatus.getFunduszRemontowy_stawka();
        kwota+=lastRobotStatus.getGaz()*lastRobotStatus.getGaz_stawka();
        kwota+=lastRobotStatus.getOgrzewanie()*lastRobotStatus.getOgrzewanie_stawka();
        kwota+=lastRobotStatus.getPrad()*lastRobotStatus.getPrad_stawka();
        kwota+=lastRobotStatus.getScieki()*lastRobotStatus.getScieki_stawka();
        kwota+=lastRobotStatus.getWoda_ciepla()*lastRobotStatus.getWoda_ciepla_stawka();
        kwota+=lastRobotStatus.getWoda_zimna()*lastRobotStatus.getWoda_zimna_stawka();
        model.addAttribute("doZaplaty", kwota);
*/


        //model.addAttribute("userRoleList", userRoleService.listUserRole());


        //model.addAttribute("addStation", new SpStation());
        //model.addAttribute("stationsList", stationService.listStations());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
        //model.addAttribute("robotList", robotService.listRobots());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace

        String uploadDir = "/resources/uploads";
        String realPath = request.getServletContext().getRealPath(uploadDir);

        System.out.println(new File("/resources/uploads/"+robotId+".png").exists());
        System.out.println(new File(realPath + "/" + robotId+".png").exists());

        if(new File(realPath + "/" + robotId+".png").exists()){
            model.addAttribute("imgExists", "png");
        }else if(new File(realPath + "/" + robotId+".jpg").exists()){
            model.addAttribute("imgExists", "jpg");
        }else{
            model.addAttribute("imgExists", null);
        }





        return "in_robot";

    }

    //inRobotAddRobprog
    @RequestMapping(value = "/inRobotAddRobprog", method = RequestMethod.POST)
    public String addRobprogToRobot(@RequestParam(required = false, name = "ZaIds") List<Long> zaIds, @RequestParam("station.id") long bID, @RequestParam("id") long fID, Model model, HttpServletRequest request) {


        List<SpUserApp> mieszkancyUsers = userRepository.findBySpecificRoles("ROLE_ROBPROG");


        System.out.println("fid "+fID+" bid "+bID);


        if(zaIds!=null) {
            System.out.println("Dodawanie lokatora ilosc para: " + zaIds.size());
        }
        System.out.println("id mieszkania: " + fID);


        if (fID > 0) {
            List<SpUserApp> mieszkancyRobot = userService.getUserAppByRobot(fID);

             SpRobot robot = robotService.getRobot(fID);

            for (SpUserApp tempMiesz : mieszkancyRobot) {
                //usuwanie mieszkancow
                userService.removeUserRobot(tempMiesz, robot);
            }
            if (zaIds!=null) {
                for (long zaId : zaIds) {//dodawanie mieszkancow
                    System.out.println("para: " + zaId);
                    Set<SpRobot> newRobot = new HashSet<SpRobot>(0);
                    newRobot.add(robot);
                    SpUserApp tempUserApp = userService.getUserApp(zaId);
                    tempUserApp.setRobot(newRobot);
                    userService.addUserRobot(tempUserApp);
                }
            }

        }

        return "redirect:/inRobotManag.html?fId="+fID;//todo
    }


    @RequestMapping(value = "/inRobotAddStatus", method = RequestMethod.POST)
    public String addStatusToRobot(@Valid @ModelAttribute("addStatus") SpRobotStatus robotStatus, @RequestParam(value = "acceptedCheck", required = false) boolean acceptedCheck, @RequestParam(value = "potCheck", required = false) boolean potCheck, @RequestParam(value = "prodCheck", required = false) boolean prodCheck, Model model, HttpServletRequest request, Principal principal) {

        robotStatus.setAccepted(acceptedCheck);
        robotStatus.setProd(prodCheck);
        robotStatus.setPot(potCheck);

        String userPesel = principal.getName();

        SpRobotStatus oldrobotStatus = robotStatusService.getRobotStatus(robotStatus.getId());
        long fID = oldrobotStatus.getRobot().getId();
        long bID = oldrobotStatus.getRobot().getStation().getId();


        if(userPesel != null){

            SpRobotStatus nRobotStatus = new SpRobotStatus();


            //robotStatus.setRobotyk(userService.findByPesel(userPesel));
            nRobotStatus.setRobotyk(userService.findByPesel(userPesel));


            //robotStatus.setRobot(oldrobotStatus.getRobot());
            nRobotStatus.setRobot(oldrobotStatus.getRobot());

            Date date = new Date();
            LocalDateTime currentdateTime = LocalDateTime.now();

            date.setYear(currentdateTime.getYear());
            date.setDate(currentdateTime.getDayOfMonth());
            date.setMonth(currentdateTime.getMonthValue());
            date.setHours(currentdateTime.getHour());
            date.setMinutes(currentdateTime.getMinute());
            date.setSeconds(currentdateTime.getSecond());

            //robotStatus.setData(date);
            nRobotStatus.setData(date);

            //Przekopiowanie wartosci z formularza do nowego
            nRobotStatus.setProd(robotStatus.isProd());
            nRobotStatus.setPot(robotStatus.isPot());
            nRobotStatus.setAccepted(robotStatus.isAccepted());
            nRobotStatus.setComment(robotStatus.getComment());
            nRobotStatus.setTz(robotStatus.getTz());
            nRobotStatus.setVel_pot(robotStatus.getVel_pot());
            nRobotStatus.setVel_prod(robotStatus.getVel_prod());



            //robotStatus.setId(oldrobotStatus.getId());

            System.out.println("dateStatus "+oldrobotStatus.getData().getYear()+" "+oldrobotStatus.getData().getMonth());
            System.out.println("accepted: "+robotStatus.isAccepted()+ " "+acceptedCheck);
            System.out.println("fid "+fID+" bid "+bID);


            //List<SpRobotStatus> robotStatusList = new ArrayList<SpRobotStatus>();
            //robotStatusList.add(robotStatus);

            SpRobot robot = robotService.getRobot(oldrobotStatus.getRobot().getId());


            //robot.addRobotStatus(robotStatus);
            robot.addRobotStatus(nRobotStatus);

            robotStatusService.addRobotStatus(nRobotStatus);
            robotService.editRobot(robot);


            //todo robotStatusService.editRobotStatus(robotStatus);

        }
        return "redirect:/inRobotManag.html?fId="+fID;//todo


        /*
        if(!acceptedCheck){
            zaplaconeCheck = false;
        }*/




        //List<SpUserApp> mieszkancyUsers = userRepository.findBySpecificRoles("ROLE_ROBPROG");


/*
        robotStatus.setWoda_ciepla_stawka(oldrobotStatus.getWoda_ciepla_stawka());
        robotStatus.setWoda_zimna_stawka(oldrobotStatus.getWoda_zimna_stawka());
        robotStatus.setScieki_stawka(oldrobotStatus.getScieki_stawka());
        robotStatus.setPrad_stawka(oldrobotStatus.getPrad_stawka());
        robotStatus.setOgrzewanie_stawka(oldrobotStatus.getOgrzewanie_stawka());
        robotStatus.setGaz_stawka(oldrobotStatus.getGaz_stawka());
        robotStatus.setFunduszRemontowy_stawka(oldrobotStatus.getFunduszRemontowy_stawka());
*/



    }

    @RequestMapping("/deleteRobot/{robotId}")
    public String deleteRobot(@PathVariable("robotId") Long robotId, HttpServletRequest request){

        int stationId = ServletRequestUtils.getIntParameter(request, "bId", -1);

        List<SpUserApp> mieszkRobot = userService.getUserAppByRobot(robotId);
        SpRobot robot = robotService.getRobot(robotId);
        List<SpRobotStatus> robotStatus = robot.getRobotStatus();




        System.out.println("przed nullowanie robot status");
        //todo robot nullowanie status

        //robot.setRobotStatus(null);
        //robotService.editRobot(robot);
        System.out.println("nullowanie robot status");
        System.out.println("usuwanie localStatus");
        //todo usuwanie localStatus

        for (Iterator<SpRobotStatus> iterator = robotStatus.iterator(); iterator.hasNext(); ) {
            SpRobotStatus tempRobotStatus = iterator.next();
            System.out.println("usuwanie localStatus: "+tempRobotStatus.getId());
            robotStatusService.removeRobotStatus(tempRobotStatus.getId());
        }

        /*
        for (SpRobotStatus tempRobotStatus : robotStatus) {
            robot.removeRobotStatus(tempRobotStatus);
            //robotService.editRobot(robot);
            System.out.println("usuwanie localStatus: "+tempRobotStatus.getId());
            robotStatusService.removeRobotStatus(tempRobotStatus.getId());

        }*/



        System.out.println("usuwanie mieszkancow");
        //todo usuwanie mieszkancow dodac jesli beda problemy
        for (SpUserApp tempMieszk : mieszkRobot) {
            //usuwanie mieszkancow
            userService.removeUserRobot(tempMieszk, robot);
        }


        System.out.println("Usuwanie mieszkania "+robotId);
        robotService.removeRobot(robotId);

        return "redirect:/stationRobotsManag.html?bId="+stationId;
    }



}
