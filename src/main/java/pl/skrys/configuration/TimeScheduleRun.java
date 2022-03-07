package pl.skrys.configuration;
/*
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpRobotCharges;
import pl.skrys.app.SpUserApp;
import pl.skrys.controller.SpStationsControl;
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
    private SpStationService stationService;
    private SpRobotService robotService;
    private SpRobotChargesService robotChargesService;
    private SpUserRepository userRepository;
    private SpMailService mailService;
    private SpUserValidator validator  = new SpUserValidator();

    public TimeScheduleRun(SpUserService userService, UserRoleService userRoleService, SpStationService stationService, SpRobotService robotService, SpRobotChargesService robotChargesService, SpUserRepository userRepository, SpMailService mailService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.stationService = stationService;
        this.robotService = robotService;
        this.robotChargesService = robotChargesService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }


    @Scheduled(cron = "0 0/5 * * * ?")//@Scheduled(cron = "0 1/2 * * * ?")////todo co godzine //todo co druga minute nieparzysta /wysylanie co 2 minuty @Scheduled(cron = "0 1/2 * * * ?")
    public void mailSendPayment(){//todo wysłanie rachunku, jesli odczyty nie zaakceptowana uzupełnić ryczałatem
        System.out.println("Wyzwolenie co 2 min mail rachunek");

        List<SpRobot> robotList = robotService.listRobots();

        for (SpRobot tempRobot : robotList) {
            SpRobotCharges lastRobotCharges =  robotChargesService.getLastRobotChargesFromRobot(tempRobot.getId());
            System.out.println("DATA: "+lastRobotCharges.getData().getYear()+" _ "+lastRobotCharges.getData().getMonth());

            if(lastRobotCharges.isAccepted()){//todo zaakceptowne odczyty, wysłać tylko rachunek
                //wysylanie maila z rachunkiem
                System.out.println("wysylanie maila z rachunkiem "+tempRobot.getId());

                List<SpUserApp> robotUsers = userService.getUserAppByRobot(tempRobot.getId());

                //wysylanie maila z rachunkiem
                for (SpUserApp robprog : robotUsers) {
                    System.out.println("wysylanie maila z rachunkiem "+tempRobot.getId()+" usr "+robprog.getEmail());
                    mailService.sendSimpleMessage(robprog.getEmail(), "Bill for "+lastRobotCharges.getData().getMonth()+" "+lastRobotCharges.getData().getYear(), "Hello "+robprog.getFirstName()+" "+robprog.getLastName()+"\r\nHere we are sending you bill for this month: "+"http://localhost:8080/rachuneko_"+robprog.getId()+"_"+lastRobotCharges.getId());

                }



            }else{// RYCZALT, odczyty nie zakceptowane, wygenerowac ryczałt

                System.out.println("przed getCountOfRobotChargesAcceptedFromRobot");
                if(robotChargesService.getCountOfRobotChargesAcceptedFromRobot(tempRobot.getId())>0){//jest conajmniej jeden zaakceptowany wpis

                    System.out.println("przed getLast12AcceptedRobotChargesByRobot");
                    List<SpRobotCharges> last12RobotCharges = robotChargesService.getLast12AcceptedRobotChargesByRobot(tempRobot.getId());


                    double pradSr = 0;
                    double gazSr = 0;
                    double woda_cieplaSr = 0;
                    double woda_zimnaSr = 0;
                    double sciekiSr = 0;
                    double ogrzewanieSr = 0;
                    double funduszRemontowySr = 0;

                    for (SpRobotCharges fc1z12 : last12RobotCharges) {
                        pradSr+=fc1z12.getPrad();
                        gazSr+=fc1z12.getGaz();
                        woda_cieplaSr+=fc1z12.getWoda_ciepla();
                        woda_zimnaSr+=fc1z12.getWoda_zimna();
                        sciekiSr+=fc1z12.getScieki();
                        ogrzewanieSr+=fc1z12.getOgrzewanie();
                        funduszRemontowySr+=fc1z12.getFunduszRemontowy();
                    }
                    pradSr/=last12RobotCharges.size();
                    gazSr/=last12RobotCharges.size();
                    woda_cieplaSr/=last12RobotCharges.size();
                    woda_zimnaSr/=last12RobotCharges.size();
                    sciekiSr/=last12RobotCharges.size();
                    ogrzewanieSr/=last12RobotCharges.size();
                    funduszRemontowySr/=last12RobotCharges.size();

                    lastRobotCharges.setPrad(pradSr);
                    lastRobotCharges.setGaz(gazSr);
                    lastRobotCharges.setWoda_ciepla(woda_cieplaSr);
                    lastRobotCharges.setWoda_zimna(woda_zimnaSr);
                    lastRobotCharges.setScieki(sciekiSr);
                    lastRobotCharges.setOgrzewanie(ogrzewanieSr);
                    lastRobotCharges.setFunduszRemontowy(funduszRemontowySr);

                    System.out.println("Zapisywanie kwot ryczalt srednia");
                    lastRobotCharges.setAccepted(true);

                }else{//brak zaakceptowanych wpisów
                    //ryczalt * liczba mieszkancow

                    int liczLokatorow = userService.getUserAppByRobot(tempRobot.getId()).size();

                    lastRobotCharges.setPrad(liczLokatorow*SpStationsControl.prad_ryczalt);
                    lastRobotCharges.setGaz(liczLokatorow*SpStationsControl.gaz_ryczalt);
                    lastRobotCharges.setWoda_ciepla(liczLokatorow*SpStationsControl.woda_ciepla_ryczalt);
                    lastRobotCharges.setWoda_zimna(liczLokatorow*SpStationsControl.woda_zimna_ryczalt);
                    lastRobotCharges.setScieki(liczLokatorow*SpStationsControl.scieki_ryczalt);
                    lastRobotCharges.setOgrzewanie(liczLokatorow*SpStationsControl.ogrzewanie_ryczalt);
                    lastRobotCharges.setFunduszRemontowy(SpStationsControl.funduszRemontowy_ryczalt);// na mieszkanie

                    System.out.println("Zapisywanie kwot ryczalt domyslny");
                    lastRobotCharges.setAccepted(true);

                }
                //zapisywanie ryczaltu
                robotChargesService.editRobotCharges(lastRobotCharges);

                List<SpUserApp> robotUsers = userService.getUserAppByRobot(tempRobot.getId());


                //wysylanie maila z rachunkiem ryczalt
                for (SpUserApp robprog : robotUsers) {
                    System.out.println("wysylanie maila z rachunkiem RYCZALT "+tempRobot.getId()+" usr "+robprog.getEmail());


                    mailService.sendSimpleMessage(robprog.getEmail(), "Bill for "+lastRobotCharges.getData().getMonth()+" "+lastRobotCharges.getData().getYear(), "Hello "+robprog.getFirstName()+" "+robprog.getLastName()+"\r\nHere we are sending you bill with a lump sum for:"+lastRobotCharges.getData().getMonth()+"_"+lastRobotCharges.getData().getYear()+" : "+"http://localhost:8080/rachunekr_"+robprog.getId()+"_"+lastRobotCharges.getId());

                }



            }

        }



    }

    @Scheduled(cron = "0 2/5 * * * ?")//TODO CO GODZINE MINUTE PRZED PEŁNA //@Scheduled(cron = "0 0/2 * * * ?")// co 10 s // co druga minuta parzysta
    public void mailSendReminder(){// przypomnienie o uzupełnieniu odczytow jesli odczyty nie zaakceptowane
        System.out.println("Wyzwolenie co 2 min mail przypomnienie odczyty");

        List<SpRobot> robotList = robotService.listRobots();

        for (SpRobot tempRobot : robotList) {
            SpRobotCharges lastRobotCharges = robotChargesService.getLastRobotChargesFromRobot(tempRobot.getId());
            System.out.println("DATA: " + lastRobotCharges.getData().getYear() + " _ " + lastRobotCharges.getData().getMonth());

            if (!lastRobotCharges.isAccepted()) {//todo Wysłać przypomnienie na maila o uzupełnienieniu odczytów
                List<SpUserApp> robotUsers = userService.getUserAppByRobot(tempRobot.getId());


                //wysylanie pRZYPOMNIENIA
                for (SpUserApp robprog : robotUsers) {
                    mailService.sendSimpleMessage(robprog.getEmail(), "Reminder "+lastRobotCharges.getData().getMonth()+" "+lastRobotCharges.getData().getYear(), "Hello "+robprog.getFirstName()+" "+robprog.getLastName()+"\r\nWe would like to remind You to fill readings for current period: "+lastRobotCharges.getData().getMonth()+"_"+lastRobotCharges.getData().getYear()+", robot: "+"Ul. "+tempRobot.getStation().getStreet()+" nr. "+tempRobot.getStation().getStationNumber()+"/"+tempRobot.getRobotNumber()+", " +tempRobot.getStation().getPostalCode()+", "+tempRobot.getStation().getCity());

                }


            }
        }


    }*/


    //@Scheduled(cron = "*/30 * * * * *")// co 30 s //todo (cron = "1 0 0 1 * ? *") domyslnie ma to być co miesiac i bedzie wtedy generowana nowa krotka z miesiacem i odczytami
    /*public void newMonthRowGen(){
        System.out.println("Wyzwolenie co 30 s - tworzenie nowej krotki ");

        List<SpRobot> robotsList = robotService.listRobots();



        for (SpRobot sRobot : robotsList) {
            SpRobotCharges lastRobotCharges = null;
            for (SpRobotCharges tempRobotCharges : sRobot.getRobotCharges()) {// wpis z ostatniego miesiaca
                if(lastRobotCharges==null){
                    lastRobotCharges=tempRobotCharges;
                }else if(lastRobotCharges.getData().getTime()<tempRobotCharges.getData().getTime()){
                    lastRobotCharges = tempRobotCharges;
                }
            }


            LocalDate currentdate = LocalDate.now();

            if(lastRobotCharges.getData().getYear()<currentdate.getYear() || lastRobotCharges.getData().getMonth()!=currentdate.getMonthValue()){
                //tworzymy odczyty dla nowego miesiaca
                System.out.println("tworzymy odczyty dla nowego miesiaca "+sRobot.getRobotNumber()+" "+lastRobotCharges.getData().getMonth());

                SpRobotCharges nRobotCharges = new SpRobotCharges();
                nRobotCharges.setRobot(sRobot);
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
                nRobotCharges.setFunduszRemontowy_stawka(SpStationsControl.cfunduszRemontowy_stawka);
                nRobotCharges.setGaz_stawka(SpStationsControl.cgaz_stawka);
                nRobotCharges.setOgrzewanie_stawka(SpStationsControl.cogrzewanie_stawka);
                nRobotCharges.setPrad_stawka(SpStationsControl.cprad_stawka);
                nRobotCharges.setScieki_stawka(SpStationsControl.cscieki_stawka);
                nRobotCharges.setWoda_zimna_stawka(SpStationsControl.cwoda_zimna_stawka);
                nRobotCharges.setWoda_ciepla_stawka(SpStationsControl.cwoda_ciepla_stawka);
                //todo stawki


                System.out.println(date.getYear()+" miesiac: "+date.getMonth()+" dzien "+date.getDate());

                nRobotCharges.setData(date);

                //List<SpRobotCharges> oldCharges = sRobot.getRobotCharges();
                //oldCharges.add(nRobotCharges);
                sRobot.addRobotCharges(nRobotCharges);

                robotChargesService.addRobotCharges(nRobotCharges);
                robotService.editRobot(sRobot);


            }else{
                //nie tworzymy, nadal aktualny miesiac
                System.out.println("nie tworzymy, nadal aktualny miesiac "+sRobot.getRobotNumber()+" "+lastRobotCharges.getData().getMonth());

            }












        }









    }
}
*/