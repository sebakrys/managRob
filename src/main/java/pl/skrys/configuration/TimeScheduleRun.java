package pl.skrys.configuration;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.skrys.app.SpFlat;
import pl.skrys.app.SpFlatCharges;
import pl.skrys.app.SpUserApp;
import pl.skrys.controller.SpBuildingsControl;
import pl.skrys.dao.SpUserRepository;
import pl.skrys.service.*;
import pl.skrys.validator.SpUserValidator;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class TimeScheduleRun {


    private SpUserService userService;
    private UserRoleService userRoleService;
    private SpBuildingService buildingService;
    private SpFlatService flatService;
    private SpFlatChargesService flatChargesService;
    private SpUserRepository userRepository;
    private SpMailService mailService;
    private SpUserValidator validator  = new SpUserValidator();

    public TimeScheduleRun(SpUserService userService, UserRoleService userRoleService, SpBuildingService buildingService, SpFlatService flatService, SpFlatChargesService flatChargesService, SpUserRepository userRepository, SpMailService mailService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.buildingService = buildingService;
        this.flatService = flatService;
        this.flatChargesService = flatChargesService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }


    @Scheduled(cron = "0 0/5 * * * ?")//@Scheduled(cron = "0 1/2 * * * ?")////todo co godzine //todo co druga minute nieparzysta /wysylanie co 2 minuty @Scheduled(cron = "0 1/2 * * * ?")
    public void mailSendPayment(){//todo wysłanie rachunku, jesli odczyty nie zaakceptowana uzupełnić ryczałatem
        System.out.println("Wyzwolenie co 2 min mail rachunek");

        List<SpFlat> flatList = flatService.listFlats();

        for (SpFlat tempFlat : flatList) {
            SpFlatCharges lastFlatCharges =  flatChargesService.getLastFlatChargesFromFlat(tempFlat.getId());
            System.out.println("DATA: "+lastFlatCharges.getData().getYear()+" _ "+lastFlatCharges.getData().getMonth());

            if(lastFlatCharges.isAccepted()){//todo zaakceptowne odczyty, wysłać tylko rachunek
                //wysylanie maila z rachunkiem
                System.out.println("wysylanie maila z rachunkiem "+tempFlat.getId());

                List<SpUserApp> flatUsers = userService.getUserAppByFlat(tempFlat.getId());

                //wysylanie maila z rachunkiem
                for (SpUserApp locator : flatUsers) {
                    System.out.println("wysylanie maila z rachunkiem "+tempFlat.getId()+" usr "+locator.getEmail());
                    mailService.sendSimpleMessage(locator.getEmail(), "Bill for "+lastFlatCharges.getData().getMonth()+" "+lastFlatCharges.getData().getYear(), "Hello "+locator.getFirstName()+" "+locator.getLastName()+"\r\nHere we are sending you bill for this month: "+"http://localhost:8080/rachuneko_"+locator.getId()+"_"+lastFlatCharges.getId());

                }



            }else{// RYCZALT, odczyty nie zakceptowane, wygenerowac ryczałt

                System.out.println("przed getCountOfFlatChargesAcceptedFromFlat");
                if(flatChargesService.getCountOfFlatChargesAcceptedFromFlat(tempFlat.getId())>0){//jest conajmniej jeden zaakceptowany wpis

                    System.out.println("przed getLast12AcceptedFlatChargesByFlat");
                    List<SpFlatCharges> last12FlatCharges = flatChargesService.getLast12AcceptedFlatChargesByFlat(tempFlat.getId());


                    double pradSr = 0;
                    double gazSr = 0;
                    double woda_cieplaSr = 0;
                    double woda_zimnaSr = 0;
                    double sciekiSr = 0;
                    double ogrzewanieSr = 0;
                    double funduszRemontowySr = 0;

                    for (SpFlatCharges fc1z12 : last12FlatCharges) {
                        pradSr+=fc1z12.getPrad();
                        gazSr+=fc1z12.getGaz();
                        woda_cieplaSr+=fc1z12.getWoda_ciepla();
                        woda_zimnaSr+=fc1z12.getWoda_zimna();
                        sciekiSr+=fc1z12.getScieki();
                        ogrzewanieSr+=fc1z12.getOgrzewanie();
                        funduszRemontowySr+=fc1z12.getFunduszRemontowy();
                    }
                    pradSr/=last12FlatCharges.size();
                    gazSr/=last12FlatCharges.size();
                    woda_cieplaSr/=last12FlatCharges.size();
                    woda_zimnaSr/=last12FlatCharges.size();
                    sciekiSr/=last12FlatCharges.size();
                    ogrzewanieSr/=last12FlatCharges.size();
                    funduszRemontowySr/=last12FlatCharges.size();

                    lastFlatCharges.setPrad(pradSr);
                    lastFlatCharges.setGaz(gazSr);
                    lastFlatCharges.setWoda_ciepla(woda_cieplaSr);
                    lastFlatCharges.setWoda_zimna(woda_zimnaSr);
                    lastFlatCharges.setScieki(sciekiSr);
                    lastFlatCharges.setOgrzewanie(ogrzewanieSr);
                    lastFlatCharges.setFunduszRemontowy(funduszRemontowySr);

                    System.out.println("Zapisywanie kwot ryczalt srednia");
                    lastFlatCharges.setAccepted(true);

                }else{//brak zaakceptowanych wpisów
                    //ryczalt * liczba mieszkancow

                    int liczLokatorow = userService.getUserAppByFlat(tempFlat.getId()).size();

                    lastFlatCharges.setPrad(liczLokatorow*SpBuildingsControl.prad_ryczalt);
                    lastFlatCharges.setGaz(liczLokatorow*SpBuildingsControl.gaz_ryczalt);
                    lastFlatCharges.setWoda_ciepla(liczLokatorow*SpBuildingsControl.woda_ciepla_ryczalt);
                    lastFlatCharges.setWoda_zimna(liczLokatorow*SpBuildingsControl.woda_zimna_ryczalt);
                    lastFlatCharges.setScieki(liczLokatorow*SpBuildingsControl.scieki_ryczalt);
                    lastFlatCharges.setOgrzewanie(liczLokatorow*SpBuildingsControl.ogrzewanie_ryczalt);
                    lastFlatCharges.setFunduszRemontowy(SpBuildingsControl.funduszRemontowy_ryczalt);// na mieszkanie

                    System.out.println("Zapisywanie kwot ryczalt domyslny");
                    lastFlatCharges.setAccepted(true);

                }
                //zapisywanie ryczaltu
                flatChargesService.editFlatCharges(lastFlatCharges);

                List<SpUserApp> flatUsers = userService.getUserAppByFlat(tempFlat.getId());


                //wysylanie maila z rachunkiem ryczalt
                for (SpUserApp locator : flatUsers) {
                    System.out.println("wysylanie maila z rachunkiem RYCZALT "+tempFlat.getId()+" usr "+locator.getEmail());


                    mailService.sendSimpleMessage(locator.getEmail(), "Bill for "+lastFlatCharges.getData().getMonth()+" "+lastFlatCharges.getData().getYear(), "Hello "+locator.getFirstName()+" "+locator.getLastName()+"\r\nHere we are sending you bill with a lump sum for:"+lastFlatCharges.getData().getMonth()+"_"+lastFlatCharges.getData().getYear()+" : "+"http://localhost:8080/rachunekr_"+locator.getId()+"_"+lastFlatCharges.getId());

                }



            }

        }



    }

    @Scheduled(cron = "0 2/5 * * * ?")//TODO CO GODZINE MINUTE PRZED PEŁNA //@Scheduled(cron = "0 0/2 * * * ?")// co 10 s // co druga minuta parzysta
    public void mailSendReminder(){// przypomnienie o uzupełnieniu odczytow jesli odczyty nie zaakceptowane
        System.out.println("Wyzwolenie co 2 min mail przypomnienie odczyty");

        List<SpFlat> flatList = flatService.listFlats();

        for (SpFlat tempFlat : flatList) {
            SpFlatCharges lastFlatCharges = flatChargesService.getLastFlatChargesFromFlat(tempFlat.getId());
            System.out.println("DATA: " + lastFlatCharges.getData().getYear() + " _ " + lastFlatCharges.getData().getMonth());

            if (!lastFlatCharges.isAccepted()) {//todo Wysłać przypomnienie na maila o uzupełnienieniu odczytów
                List<SpUserApp> flatUsers = userService.getUserAppByFlat(tempFlat.getId());


                //wysylanie pRZYPOMNIENIA
                for (SpUserApp locator : flatUsers) {
                    mailService.sendSimpleMessage(locator.getEmail(), "Reminder "+lastFlatCharges.getData().getMonth()+" "+lastFlatCharges.getData().getYear(), "Hello "+locator.getFirstName()+" "+locator.getLastName()+"\r\nWe would like to remind You to fill readings for current period: "+lastFlatCharges.getData().getMonth()+"_"+lastFlatCharges.getData().getYear()+", flat: "+"Ul. "+tempFlat.getBuilding().getStreet()+" nr. "+tempFlat.getBuilding().getBuildingNumber()+"/"+tempFlat.getFlatNumber()+", " +tempFlat.getBuilding().getPostalCode()+", "+tempFlat.getBuilding().getCity());

                }


            }
        }


    }


    @Scheduled(cron = "*/30 * * * * *")// co 30 s //todo (cron = "1 0 0 1 * ? *") domyslnie ma to być co miesiac i bedzie wtedy generowana nowa krotka z miesiacem i odczytami
    public void newMonthRowGen(){
        System.out.println("Wyzwolenie co 30 s - tworzenie nowej krotki ");

        List<SpFlat> flatsList = flatService.listFlats();



        for (SpFlat sFlat : flatsList) {
            SpFlatCharges lastFlatCharges = null;
            for (SpFlatCharges tempFlatCharges : sFlat.getFlatCharges()) {// wpis z ostatniego miesiaca
                if(lastFlatCharges==null){
                    lastFlatCharges=tempFlatCharges;
                }else if(lastFlatCharges.getData().getTime()<tempFlatCharges.getData().getTime()){
                    lastFlatCharges = tempFlatCharges;
                }
            }


            LocalDate currentdate = LocalDate.now();

            if(lastFlatCharges.getData().getYear()<currentdate.getYear() || lastFlatCharges.getData().getMonth()!=currentdate.getMonthValue()){
                //tworzymy odczyty dla nowego miesiaca
                System.out.println("tworzymy odczyty dla nowego miesiaca "+sFlat.getFlatNumber()+" "+lastFlatCharges.getData().getMonth());

                SpFlatCharges nFlatCharges = new SpFlatCharges();
                nFlatCharges.setFlat(sFlat);
                Date date = new Date();
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
                nFlatCharges.setFunduszRemontowy_stawka(SpBuildingsControl.cfunduszRemontowy_stawka);
                nFlatCharges.setGaz_stawka(SpBuildingsControl.cgaz_stawka);
                nFlatCharges.setOgrzewanie_stawka(SpBuildingsControl.cogrzewanie_stawka);
                nFlatCharges.setPrad_stawka(SpBuildingsControl.cprad_stawka);
                nFlatCharges.setScieki_stawka(SpBuildingsControl.cscieki_stawka);
                nFlatCharges.setWoda_zimna_stawka(SpBuildingsControl.cwoda_zimna_stawka);
                nFlatCharges.setWoda_ciepla_stawka(SpBuildingsControl.cwoda_ciepla_stawka);
                //todo stawki


                System.out.println(date.getYear()+" miesiac: "+date.getMonth()+" dzien "+date.getDate());

                nFlatCharges.setData(date);

                //List<SpFlatCharges> oldCharges = sFlat.getFlatCharges();
                //oldCharges.add(nFlatCharges);
                sFlat.addFlatCharges(nFlatCharges);

                flatChargesService.addFlatCharges(nFlatCharges);
                flatService.editFlat(sFlat);


            }else{
                //nie tworzymy, nadal aktualny miesiac
                System.out.println("nie tworzymy, nadal aktualny miesiac "+sFlat.getFlatNumber()+" "+lastFlatCharges.getData().getMonth());

            }












        }









    }
}
