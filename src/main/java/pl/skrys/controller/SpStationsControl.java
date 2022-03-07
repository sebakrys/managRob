package pl.skrys.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import pl.skrys.app.*;
import pl.skrys.dao.SpUserRepository;
import pl.skrys.service.*;
import pl.skrys.validator.SpUserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
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
    private SpRobotChargesService robotChargesService;
    private SpUserRepository userRepository;
    private SpUserValidator validator  = new SpUserValidator();

    public SpStationsControl(SpUserService userService, UserRoleService userRoleService, SpStationService stationService, SpRobotService robotService, SpRobotChargesService robotChargesService, SpUserRepository userRepository) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.stationService = stationService;
        this.robotService = robotService;
        this.robotChargesService = robotChargesService;
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




        System.out.println(station.getNazwa()+" "+station.getId()+" "+station.getCity());
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
    public String addNewRobot(@Valid @ModelAttribute("addRobot") SpRobot robot, BindingResult result, Model model, HttpServletRequest request) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"


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

                SpRobotCharges robotCharges = new SpRobotCharges();
                robotCharges.setRobot(robot);
                Date date = new Date();
                LocalDate currentdate = LocalDate.now();

                date.setYear(currentdate.getYear());
                //System.out.println("Y: "+date.getYear());
                date.setDate(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
                //System.out.println("D: "+date.getDate());
                date.setMonth(currentdate.getMonthValue());
                //System.out.println("M: "+date.getMonth());
                date.setHours(23);
                date.setMinutes(59);
                date.setSeconds(59);

                //todo stawki
                //fundusz remontowy 2 zł za m2
                //gaz 1.3 zł za m3
                //ogrzewanie 0.3 zł za kWh
                //prad 0.65 zł za kWh
                //ścieki 9 zł 1m2
                //woda zimna 6 zł
                //woda ciepla 35zł
                robotCharges.setFunduszRemontowy_stawka(cfunduszRemontowy_stawka);
                robotCharges.setGaz_stawka(cgaz_stawka);
                robotCharges.setOgrzewanie_stawka(cogrzewanie_stawka);
                robotCharges.setPrad_stawka(cprad_stawka);
                robotCharges.setScieki_stawka(cscieki_stawka);
                robotCharges.setWoda_zimna_stawka(cwoda_zimna_stawka);
                robotCharges.setWoda_ciepla_stawka(cwoda_ciepla_stawka);
                //todo stawki




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
                robotCharges.setData(date);

                List<SpRobotCharges> robotChargesList = new ArrayList<SpRobotCharges>();
                robotChargesList.add(robotCharges);

                robot.setRobotCharges(robotChargesList);

                System.out.println("dodawanie mieszkania");
                robotService.addRobot(robot);//dodac doawanie do robotCharges
                robotChargesService.addRobotCharges(robotCharges);



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


            }else{
                System.out.println("NIE JEST !!!!!MANAGER I POSIADA TEN STATION");
                model.addAttribute("managerB", false);


            }


        }



        model.addAttribute("selectedRobot", srobot);
        model.addAttribute("selectedStation", srobot.getStation());

        List<SpUserApp> mieszkancyUsers = userRepository.findBySpecificRoles("ROLE_ROBPROG");



        List<SpUserApp> robprogsRobot = userService.getUserAppByRobot(robotId);

        model.addAttribute("robprogs", mieszkancyUsers);//todo wszyscy uzytkownicy z rola ROBPROG
        model.addAttribute("robprogsRobot", robprogsRobot);// wszyscy Lokatorzy
        model.addAttribute("addRobprog", srobot);

        SpRobotCharges lastRobotCharges = null;
        for (SpRobotCharges tempRobotCharges : srobot.getRobotCharges()) {
            if(lastRobotCharges==null){
                lastRobotCharges=tempRobotCharges;
            }else if(lastRobotCharges.getData().getTime()<tempRobotCharges.getData().getTime()){
                lastRobotCharges = tempRobotCharges;
            }
        }

        model.addAttribute("addCharges", lastRobotCharges);


        double kwota = 0.0;
        kwota+=lastRobotCharges.getFunduszRemontowy()*lastRobotCharges.getFunduszRemontowy_stawka();
        kwota+=lastRobotCharges.getGaz()*lastRobotCharges.getGaz_stawka();
        kwota+=lastRobotCharges.getOgrzewanie()*lastRobotCharges.getOgrzewanie_stawka();
        kwota+=lastRobotCharges.getPrad()*lastRobotCharges.getPrad_stawka();
        kwota+=lastRobotCharges.getScieki()*lastRobotCharges.getScieki_stawka();
        kwota+=lastRobotCharges.getWoda_ciepla()*lastRobotCharges.getWoda_ciepla_stawka();
        kwota+=lastRobotCharges.getWoda_zimna()*lastRobotCharges.getWoda_zimna_stawka();
        model.addAttribute("doZaplaty", kwota);



        //model.addAttribute("userRoleList", userRoleService.listUserRole());


        //model.addAttribute("addStation", new SpStation());
        //model.addAttribute("stationsList", stationService.listStations());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
        //model.addAttribute("robotList", robotService.listRobots());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace



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


    @RequestMapping(value = "/inRobotAddCharges", method = RequestMethod.POST)
    public String addChargesToRobot(@Valid @ModelAttribute("addCharges") SpRobotCharges robotCharges, @RequestParam(value = "acceptedCheck", required = false) boolean acceptedCheck, @RequestParam(value = "zaplaconeCheck", required = false) boolean zaplaconeCheck, Model model, HttpServletRequest request) {

        robotCharges.setAccepted(acceptedCheck);

        if(!acceptedCheck){
            zaplaconeCheck = false;
        }

        robotCharges.setZaplacone(zaplaconeCheck);

        SpRobotCharges oldrobotCharges = robotChargesService.getRobotCharges(robotCharges.getId());

        long fID = oldrobotCharges.getRobot().getId();
        long bID = oldrobotCharges.getRobot().getStation().getId();

        //List<SpUserApp> mieszkancyUsers = userRepository.findBySpecificRoles("ROLE_ROBPROG");


        robotCharges.setRobot(oldrobotCharges.getRobot());
        robotCharges.setData(oldrobotCharges.getData());
        robotCharges.setId(oldrobotCharges.getId());

        robotCharges.setWoda_ciepla_stawka(oldrobotCharges.getWoda_ciepla_stawka());
        robotCharges.setWoda_zimna_stawka(oldrobotCharges.getWoda_zimna_stawka());
        robotCharges.setScieki_stawka(oldrobotCharges.getScieki_stawka());
        robotCharges.setPrad_stawka(oldrobotCharges.getPrad_stawka());
        robotCharges.setOgrzewanie_stawka(oldrobotCharges.getOgrzewanie_stawka());
        robotCharges.setGaz_stawka(oldrobotCharges.getGaz_stawka());
        robotCharges.setFunduszRemontowy_stawka(oldrobotCharges.getFunduszRemontowy_stawka());





        System.out.println("dateCharges "+oldrobotCharges.getData().getYear()+" "+oldrobotCharges.getData().getMonth());
        System.out.println("accepted: "+robotCharges.isAccepted()+ " "+acceptedCheck);
        System.out.println("fid "+fID+" bid "+bID);

        robotChargesService.editRobotCharges(robotCharges);


        return "redirect:/inRobotManag.html?fId="+fID;//todo
    }

    @RequestMapping("/deleteRobot/{robotId}")
    public String deleteRobot(@PathVariable("robotId") Long robotId, HttpServletRequest request){

        int stationId = ServletRequestUtils.getIntParameter(request, "bId", -1);

        List<SpUserApp> mieszkRobot = userService.getUserAppByRobot(robotId);
        SpRobot robot = robotService.getRobot(robotId);
        List<SpRobotCharges> robotCharges = robot.getRobotCharges();




        System.out.println("przed nullowanie robot charges");
        //todo robot nullowanie charges

        //robot.setRobotCharges(null);
        //robotService.editRobot(robot);
        System.out.println("nullowanie robot charges");
        System.out.println("usuwanie localCharges");
        //todo usuwanie localCharges

        for (Iterator<SpRobotCharges> iterator = robotCharges.iterator(); iterator.hasNext(); ) {
            SpRobotCharges tempRobotCharges = iterator.next();
            System.out.println("usuwanie localCharges: "+tempRobotCharges.getId());
            robotChargesService.removeRobotCharges(tempRobotCharges.getId());
        }

        /*
        for (SpRobotCharges tempRobotCharges : robotCharges) {
            robot.removeRobotCharges(tempRobotCharges);
            //robotService.editRobot(robot);
            System.out.println("usuwanie localCharges: "+tempRobotCharges.getId());
            robotChargesService.removeRobotCharges(tempRobotCharges.getId());

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
