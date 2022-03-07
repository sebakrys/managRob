package pl.skrys.controller;

import org.springframework.data.domain.Sort;
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
public class SpBuildingsControl {


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
    private SpBuildingService buildingService;
    private SpFlatService flatService;
    private SpFlatChargesService flatChargesService;
    private SpUserRepository userRepository;
    private SpUserValidator validator  = new SpUserValidator();

    public SpBuildingsControl(SpUserService userService, UserRoleService userRoleService, SpBuildingService buildingService, SpFlatService flatService, SpFlatChargesService flatChargesService, SpUserRepository userRepository) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.buildingService = buildingService;
        this.flatService = flatService;
        this.flatChargesService = flatChargesService;
        this.userRepository = userRepository;
    }


    @Secured({"ROLE_MANAGER", "ROLE_ROBPROG", "ROLE_ADMIN"})
    @RequestMapping(value = "/inBuildings")
    public String zarzBuildingsList(Model model, HttpServletRequest request, Principal principal){

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

                model.addAttribute("addBuilding", new SpBuilding());
                model.addAttribute("buildingsList", buildingService.listBuildings());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
                model.addAttribute("flatList", flatService.listFlats());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace


                Set<Long> spBuildingIdManagSet = new HashSet<Long>(0);
                for (SpBuilding zarzBuild:
                        buildingService.listBuildings()) {

                    spBuildingIdManagSet.add(zarzBuild.getId());
                }
                model.addAttribute("buildingsManagList", spBuildingIdManagSet);
                return "in_buildings";
            }

            Set<SpBuilding> spBuildingSet = new HashSet<SpBuilding>(0);
            Set<Long> spBuildingIdManagSet = new HashSet<Long>(0);
            Set<SpFlat> spFlatSet = userApp.getFlat();



            if(robprog){


                for (SpFlat tempFlat : spFlatSet) {
                    spBuildingSet.add(tempFlat.getBuilding());
                }
            }



            if(manager){
                for (SpBuilding zarzBuild:
                userApp.getBuildings()) {
                    spBuildingSet.add(zarzBuild);
                    spBuildingIdManagSet.add(zarzBuild.getId());
                }
            }

            //model.addAttribute("addBuilding", new SpBuilding());
            //todo dla mieszkanca i zarzadcy
            model.addAttribute("buildingsList", spBuildingSet);//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
            model.addAttribute("flatList", spFlatSet);//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace

            model.addAttribute("buildingsManagList", spBuildingIdManagSet);

        }


        return "in_buildings";

    }




    @RequestMapping(value = "/inAddNewBuilding", method = RequestMethod.POST)
    public String addNewBuilding(@Valid @ModelAttribute("addBuilding") SpBuilding building, BindingResult result, Model model, HttpServletRequest request) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"


        model.addAttribute("buildingsList", buildingService.listBuildings());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
        model.addAttribute("flatList", flatService.listFlats());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace




        System.out.println(building.getNazwa()+" "+building.getId()+" "+building.getCity());
        validator.validateBuilding(building, result);




        if (result.getErrorCount() == 0) {
            if (building.getId() == 0) {
                //dodawanie budynku
                System.out.println("dodawanie budynku");
                buildingService.addBuilding(building);
                return "redirect:/inBuildings.html";
            } else {
                buildingService.editBuilding(building);

                return "redirect:/inBuildings.html";
            }

        }

        System.out.println("są bledy validatora");


        return "in_buildings";
    }

    @Secured({"ROLE_MANAGER", "ROLE_ROBPROG", "ROLE_ADMIN"})
    @RequestMapping(value = "/buildingFlatsManag")
    public String flatsManagList(Model model, HttpServletRequest request, Principal principal){

        int buildingId = ServletRequestUtils.getIntParameter(request, "bId", -1);
        boolean flatExists = ServletRequestUtils.getBooleanParameter(request, "flatExists", false);
        String userPesel = principal.getName();

        boolean managerBuilding = false;
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

            Set<SpBuilding> userBuildings =  userApp.getBuildings();
            for (SpBuilding tempBuilding:
            userBuildings) {
                if(tempBuilding.getId()==buildingId){
                    managerBuilding=true;
                    break;
                }
            }




            if(managerBuilding && manager || admin){
                System.out.println("MANAGER I POSIADA TEN BUILDING");
                model.addAttribute("managerB", true);
                model.addAttribute("flatsList", buildingService.getBuilding(buildingId).getFlat());

            }else{
                System.out.println("NIE JEST !!!!!MANAGER I POSIADA TEN BUILDING");
                model.addAttribute("managerB", false);

                Set<SpFlat> userFlats = userApp.getFlat();
                Set<SpFlat> nUserFlats = new HashSet<SpFlat>(0);

                for (SpFlat tempFlat :
                     userFlats) {
                    if(tempFlat.getBuilding().getId()==buildingId){
                        nUserFlats.add(tempFlat);
                    }
                }

                model.addAttribute("flatsList", nUserFlats);

            }
        }



        SpFlat flat = new SpFlat();
        flat.setBuilding(buildingService.getBuilding(buildingId));




        List<SpUserApp> zarzadcyUsers = userRepository.findBySpecificRoles("ROLE_MANAGER");

        List<SpUserApp> zarzadcyBudynku = userService.getUserAppByBuilding(buildingId);
        System.out.println("l wszy ZARZADCOW "+zarzadcyUsers.size());
        System.out.println("l  ZARZADCOW "+zarzadcyBudynku.size());

        model.addAttribute("zarzadcy", zarzadcyUsers);
        model.addAttribute("zarzadcyBudynku", zarzadcyBudynku);





        model.addAttribute("addFlat", flat);
        model.addAttribute("addZarzadca", flat);

        model.addAttribute("selectedBuilding", buildingService.getBuilding(buildingId));

        System.out.println("Liczba mieszkań "+buildingService.getBuilding(buildingId).getFlat().size()+" budynek "+buildingService.getBuilding(buildingId).getId());




        //model.addAttribute("manager", buildingService.listZarzadcy(buildingService.getBuilding(buildingId)));//todo wyswietlic liste wszytskich zarzadcow i miec jako selected tych co są aktualnie



        model.addAttribute("flatExists", flatExists);



        return "in_building_flats_manager";

    }






    @RequestMapping(value = "/inAddNewFlat", method = RequestMethod.POST)
    public String addNewFlat(@Valid @ModelAttribute("addFlat") SpFlat flat, BindingResult result, Model model, HttpServletRequest request) {// nie wiem po co to jest, ale powinno(ale nie musi) być tak jak w attributeName "userApp"


        //model.addAttribute("buildingsList", buildingService.listBuildings());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
        //model.addAttribute("flatList", flatService.listFlats());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace


        System.out.println("Id Budynku z mieszkania: "+flat.getBuilding().getId());
        System.out.println("FlatNumer: "+flat.getFlatNumber()+" ");
        SpBuilding building = buildingService.getBuilding(flat.getBuilding().getId());
        System.out.println("FlatNumer: "+ flat.getFlatNumber()+" "+flat.getBuilding().getNazwa()+" ");
        flat.setBuilding(building);


        validator.validateFlat(flat, result);


        model.addAttribute("flatsList", building.getFlat());

        //boolean duplicate = false;

        for (SpFlat tempFlat:
        building.getFlat()) {//todo flatNumber duplikaty
            System.out.println("spr duplikaty "+tempFlat.getFlatNumber()+" _ "+flat.getFlatNumber());

            if(tempFlat.getFlatNumber().equals(flat.getFlatNumber())){
                System.out.println("Duplikat nr mieszkania");
                //duplicate = true;
                result.rejectValue("flatNumber", "error.alreadyExists");
                model.addAttribute("flatExists", true);
                break;
            }
        }



        if (result.getErrorCount() == 0) {
            if (flat.getId() == 0) {
                //dodawanie mieszkania

                SpFlatCharges flatCharges = new SpFlatCharges();
                flatCharges.setFlat(flat);
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
                flatCharges.setFunduszRemontowy_stawka(cfunduszRemontowy_stawka);
                flatCharges.setGaz_stawka(cgaz_stawka);
                flatCharges.setOgrzewanie_stawka(cogrzewanie_stawka);
                flatCharges.setPrad_stawka(cprad_stawka);
                flatCharges.setScieki_stawka(cscieki_stawka);
                flatCharges.setWoda_zimna_stawka(cwoda_zimna_stawka);
                flatCharges.setWoda_ciepla_stawka(cwoda_ciepla_stawka);
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
                flatCharges.setData(date);

                List<SpFlatCharges> flatChargesList = new ArrayList<SpFlatCharges>();
                flatChargesList.add(flatCharges);

                flat.setFlatCharges(flatChargesList);

                System.out.println("dodawanie mieszkania");
                flatService.addFlat(flat);//dodac doawanie do flatCharges
                flatChargesService.addFlatCharges(flatCharges);



                return "redirect:/buildingFlatsManag.html?bId="+building.getId();
            } else {
                //flatService.editFlat(flat);

                return "redirect:/buildingFlatsManag.html?bId="+building.getId();
            }

        }

        model.addAttribute("addZarzadca", flat);
        System.out.println("są bledy validatora");


        return "redirect:/buildingFlatsManag.html?bId="+building.getId();
    }


    @RequestMapping(value = "/inBuildingAddZarzadca", method = RequestMethod.POST)
    public String addZarzadcyToBuilding(@RequestParam(required = false, name = "ZaIds") List<Long> zaIds,@RequestParam("building.id") long bID, Model model, HttpServletRequest request) {


        List<SpUserApp> zarzadcyUsers = userRepository.findBySpecificRoles("ROLE_MANAGER");



        if(zaIds!=null) {
            System.out.println("ilosc para: " + zaIds.size());
        }
            System.out.println("id budynku: " + bID);


            if (bID > 0) {
                List<SpUserApp> zarzadcyBudynku = userService.getUserAppByBuilding(bID);
                SpBuilding building = buildingService.getBuilding(bID);

                for (SpUserApp tempZarz : zarzadcyBudynku) {
                    //usuwanie zarzadców
                    userService.removeUserBuilding(tempZarz, building);
                }
                if (zaIds!=null) {
                    for (long zaId : zaIds) {//dodawanie zarzadcow
                        System.out.println("para: " + zaId);
                        Set<SpBuilding> newBuildings = new HashSet<SpBuilding>(0);
                        newBuildings.add(building);
                        SpUserApp tempUserApp = userService.getUserApp(zaId);
                        tempUserApp.setBuildings(newBuildings);
                        userService.addUserBuildings(tempUserApp);
                    }
                }

            }

        return "redirect:/buildingFlatsManag.html?bId="+bID;//todo
    }


    @RequestMapping("/deleteBuilding/{buildingId}")
    public String deleteBuilding(@PathVariable("buildingId") Long buildingId){
        System.out.println("Usuwanie  budynku "+buildingId);

        List<SpUserApp> zarzadcyBudynku = userService.getUserAppByBuilding(buildingId);
        SpBuilding building = buildingService.getBuilding(buildingId);
        for (SpUserApp tempZarz : zarzadcyBudynku) {
            //usuwanie zarzadców
            userService.removeUserBuilding(tempZarz, building);
        }

        buildingService.removeBuilding(buildingId);
        return "redirect:/inBuildings";
    }




    @RequestMapping(value = "/inFlatManag")
    public String flatManag(Model model, HttpServletRequest request, Principal principal){
        int flatId = ServletRequestUtils.getIntParameter(request, "fId", -1);
        SpFlat sflat = flatService.getFlat(flatId);

        String userPesel = principal.getName();

        boolean managerBuilding = false;
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

            Set<SpBuilding> userBuildings = userApp.getBuildings();
            for (SpBuilding tempBuilding :
                    userBuildings) {
                if (tempBuilding.getId() == sflat.getBuilding().getId()) {
                    managerBuilding = true;
                    break;
                }
            }


            if(managerBuilding && manager || admin){
                System.out.println("MANAGER I POSIADA TEN BUILDING");
                model.addAttribute("managerB", true);


            }else{
                System.out.println("NIE JEST !!!!!MANAGER I POSIADA TEN BUILDING");
                model.addAttribute("managerB", false);


            }


        }



        model.addAttribute("selectedFlat", sflat);
        model.addAttribute("selectedBuilding", sflat.getBuilding());

        List<SpUserApp> mieszkancyUsers = userRepository.findBySpecificRoles("ROLE_ROBPROG");



        List<SpUserApp> locatorsFlat = userService.getUserAppByFlat(flatId);

        model.addAttribute("locators", mieszkancyUsers);//todo wszyscy uzytkownicy z rola ROBPROG
        model.addAttribute("locatorsFlat", locatorsFlat);// wszyscy Lokatorzy
        model.addAttribute("addLocator", sflat);

        SpFlatCharges lastFlatCharges = null;
        for (SpFlatCharges tempFlatCharges : sflat.getFlatCharges()) {
            if(lastFlatCharges==null){
                lastFlatCharges=tempFlatCharges;
            }else if(lastFlatCharges.getData().getTime()<tempFlatCharges.getData().getTime()){
                lastFlatCharges = tempFlatCharges;
            }
        }

        model.addAttribute("addCharges", lastFlatCharges);


        double kwota = 0.0;
        kwota+=lastFlatCharges.getFunduszRemontowy()*lastFlatCharges.getFunduszRemontowy_stawka();
        kwota+=lastFlatCharges.getGaz()*lastFlatCharges.getGaz_stawka();
        kwota+=lastFlatCharges.getOgrzewanie()*lastFlatCharges.getOgrzewanie_stawka();
        kwota+=lastFlatCharges.getPrad()*lastFlatCharges.getPrad_stawka();
        kwota+=lastFlatCharges.getScieki()*lastFlatCharges.getScieki_stawka();
        kwota+=lastFlatCharges.getWoda_ciepla()*lastFlatCharges.getWoda_ciepla_stawka();
        kwota+=lastFlatCharges.getWoda_zimna()*lastFlatCharges.getWoda_zimna_stawka();
        model.addAttribute("doZaplaty", kwota);



        //model.addAttribute("userRoleList", userRoleService.listUserRole());


        //model.addAttribute("addBuilding", new SpBuilding());
        //model.addAttribute("buildingsList", buildingService.listBuildings());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace
        //model.addAttribute("flatList", flatService.listFlats());//dla ROLE_ADMIN, dla reszty ma pokazywac tylko przynależace



        return "in_flat";

    }

    //inFLatAddLocator
    @RequestMapping(value = "/inFLatAddLocator", method = RequestMethod.POST)
    public String addLocatorToFlat(@RequestParam(required = false, name = "ZaIds") List<Long> zaIds, @RequestParam("building.id") long bID, @RequestParam("id") long fID, Model model, HttpServletRequest request) {


        List<SpUserApp> mieszkancyUsers = userRepository.findBySpecificRoles("ROLE_ROBPROG");


        System.out.println("fid "+fID+" bid "+bID);


        if(zaIds!=null) {
            System.out.println("Dodawanie lokatora ilosc para: " + zaIds.size());
        }
        System.out.println("id mieszkania: " + fID);


        if (fID > 0) {
            List<SpUserApp> mieszkancyFLat = userService.getUserAppByFlat(fID);

             SpFlat flat = flatService.getFlat(fID);

            for (SpUserApp tempMiesz : mieszkancyFLat) {
                //usuwanie mieszkancow
                userService.removeUserFlat(tempMiesz, flat);
            }
            if (zaIds!=null) {
                for (long zaId : zaIds) {//dodawanie mieszkancow
                    System.out.println("para: " + zaId);
                    Set<SpFlat> newFlat = new HashSet<SpFlat>(0);
                    newFlat.add(flat);
                    SpUserApp tempUserApp = userService.getUserApp(zaId);
                    tempUserApp.setFlat(newFlat);
                    userService.addUserFlat(tempUserApp);
                }
            }

        }

        return "redirect:/inFlatManag.html?fId="+fID;//todo
    }


    @RequestMapping(value = "/inFlatAddCharges", method = RequestMethod.POST)
    public String addChargesToFlat(@Valid @ModelAttribute("addCharges") SpFlatCharges flatCharges, @RequestParam(value = "acceptedCheck", required = false) boolean acceptedCheck, @RequestParam(value = "zaplaconeCheck", required = false) boolean zaplaconeCheck, Model model, HttpServletRequest request) {

        flatCharges.setAccepted(acceptedCheck);

        if(!acceptedCheck){
            zaplaconeCheck = false;
        }

        flatCharges.setZaplacone(zaplaconeCheck);

        SpFlatCharges oldflatCharges = flatChargesService.getFlatCharges(flatCharges.getId());

        long fID = oldflatCharges.getFlat().getId();
        long bID = oldflatCharges.getFlat().getBuilding().getId();

        //List<SpUserApp> mieszkancyUsers = userRepository.findBySpecificRoles("ROLE_ROBPROG");


        flatCharges.setFlat(oldflatCharges.getFlat());
        flatCharges.setData(oldflatCharges.getData());
        flatCharges.setId(oldflatCharges.getId());

        flatCharges.setWoda_ciepla_stawka(oldflatCharges.getWoda_ciepla_stawka());
        flatCharges.setWoda_zimna_stawka(oldflatCharges.getWoda_zimna_stawka());
        flatCharges.setScieki_stawka(oldflatCharges.getScieki_stawka());
        flatCharges.setPrad_stawka(oldflatCharges.getPrad_stawka());
        flatCharges.setOgrzewanie_stawka(oldflatCharges.getOgrzewanie_stawka());
        flatCharges.setGaz_stawka(oldflatCharges.getGaz_stawka());
        flatCharges.setFunduszRemontowy_stawka(oldflatCharges.getFunduszRemontowy_stawka());





        System.out.println("dateCharges "+oldflatCharges.getData().getYear()+" "+oldflatCharges.getData().getMonth());
        System.out.println("accepted: "+flatCharges.isAccepted()+ " "+acceptedCheck);
        System.out.println("fid "+fID+" bid "+bID);

        flatChargesService.editFlatCharges(flatCharges);


        return "redirect:/inFlatManag.html?fId="+fID;//todo
    }

    @RequestMapping("/deleteFlat/{flatId}")
    public String deleteFlat(@PathVariable("flatId") Long flatId, HttpServletRequest request){

        int buildingId = ServletRequestUtils.getIntParameter(request, "bId", -1);

        List<SpUserApp> mieszkFlat = userService.getUserAppByFlat(flatId);
        SpFlat flat = flatService.getFlat(flatId);
        List<SpFlatCharges> flatCharges = flat.getFlatCharges();




        System.out.println("przed nullowanie flat charges");
        //todo flat nullowanie charges

        //flat.setFlatCharges(null);
        //flatService.editFlat(flat);
        System.out.println("nullowanie flat charges");
        System.out.println("usuwanie localCharges");
        //todo usuwanie localCharges

        for (Iterator<SpFlatCharges> iterator = flatCharges.iterator(); iterator.hasNext(); ) {
            SpFlatCharges tempFlatCharges = iterator.next();
            System.out.println("usuwanie localCharges: "+tempFlatCharges.getId());
            flatChargesService.removeFlatCharges(tempFlatCharges.getId());
        }

        /*
        for (SpFlatCharges tempFlatCharges : flatCharges) {
            flat.removeFlatCharges(tempFlatCharges);
            //flatService.editFlat(flat);
            System.out.println("usuwanie localCharges: "+tempFlatCharges.getId());
            flatChargesService.removeFlatCharges(tempFlatCharges.getId());

        }*/



        System.out.println("usuwanie mieszkancow");
        //todo usuwanie mieszkancow dodac jesli beda problemy
        for (SpUserApp tempMieszk : mieszkFlat) {
            //usuwanie mieszkancow
            userService.removeUserFlat(tempMieszk, flat);
        }


        System.out.println("Usuwanie mieszkania "+flatId);
        flatService.removeFlat(flatId);

        return "redirect:/buildingFlatsManag.html?bId="+buildingId;
    }



}
