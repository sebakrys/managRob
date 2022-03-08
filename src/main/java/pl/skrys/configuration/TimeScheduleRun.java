package pl.skrys.configuration;
/*
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpRobotStatus;
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
    private SpRobotStatusService robotStatusService;
    private SpUserRepository userRepository;
    private SpMailService mailService;
    private SpUserValidator validator  = new SpUserValidator();

    public TimeScheduleRun(SpUserService userService, UserRoleService userRoleService, SpStationService stationService, SpRobotService robotService, SpRobotStatusService robotStatusService, SpUserRepository userRepository, SpMailService mailService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.stationService = stationService;
        this.robotService = robotService;
        this.robotStatusService = robotStatusService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }


    @Scheduled(cron = "0 0/5 * * * ?")//@Scheduled(cron = "0 1/2 * * * ?")////todo co godzine //todo co druga minute nieparzysta /wysylanie co 2 minuty @Scheduled(cron = "0 1/2 * * * ?")
    public void mailSendPayment(){//todo wysłanie rachunku, jesli odczyty nie zaakceptowana uzupełnić ryczałatem
        System.out.println("Wyzwolenie co 2 min mail rachunek");

        List<SpRobot> robotList = robotService.listRobots();

        for (SpRobot tempRobot : robotList) {
            SpRobotStatus lastRobotStatus =  robotStatusService.getLastRobotStatusFromRobot(tempRobot.getId());
            System.out.println("DATA: "+lastRobotStatus.getData().getYear()+" _ "+lastRobotStatus.getData().getMonth());

            if(lastRobotStatus.isAccepted()){//todo zaakceptowne odczyty, wysłać tylko rachunek
                //wysylanie maila z rachunkiem
                System.out.println("wysylanie maila z rachunkiem "+tempRobot.getId());

                List<SpUserApp> robotUsers = userService.getUserAppByRobot(tempRobot.getId());

                //wysylanie maila z rachunkiem
                for (SpUserApp robprog : robotUsers) {
                    System.out.println("wysylanie maila z rachunkiem "+tempRobot.getId()+" usr "+robprog.getEmail());
                    mailService.sendSimpleMessage(robprog.getEmail(), "Bill for "+lastRobotStatus.getData().getMonth()+" "+lastRobotStatus.getData().getYear(), "Hello "+robprog.getFirstName()+" "+robprog.getLastName()+"\r\nHere we are sending you bill for this month: "+"http://localhost:8080/rachuneko_"+robprog.getId()+"_"+lastRobotStatus.getId());

                }



            }else{// RYCZALT, odczyty nie zakceptowane, wygenerowac ryczałt

                System.out.println("przed getCountOfRobotStatusAcceptedFromRobot");
                if(robotStatusService.getCountOfRobotStatusAcceptedFromRobot(tempRobot.getId())>0){//jest conajmniej jeden zaakceptowany wpis

                    System.out.println("przed getLast12AcceptedRobotStatusByRobot");
                    List<SpRobotStatus> last12RobotStatus = robotStatusService.getLast12AcceptedRobotStatusByRobot(tempRobot.getId());


                    double pradSr = 0;
                    double gazSr = 0;
                    double woda_cieplaSr = 0;
                    double woda_zimnaSr = 0;
                    double sciekiSr = 0;
                    double ogrzewanieSr = 0;
                    double funduszRemontowySr = 0;

                    for (SpRobotStatus fc1z12 : last12RobotStatus) {
                        pradSr+=fc1z12.getPrad();
                        gazSr+=fc1z12.getGaz();
                        woda_cieplaSr+=fc1z12.getWoda_ciepla();
                        woda_zimnaSr+=fc1z12.getWoda_zimna();
                        sciekiSr+=fc1z12.getScieki();
                        ogrzewanieSr+=fc1z12.getOgrzewanie();
                        funduszRemontowySr+=fc1z12.getFunduszRemontowy();
                    }
                    pradSr/=last12RobotStatus.size();
                    gazSr/=last12RobotStatus.size();
                    woda_cieplaSr/=last12RobotStatus.size();
                    woda_zimnaSr/=last12RobotStatus.size();
                    sciekiSr/=last12RobotStatus.size();
                    ogrzewanieSr/=last12RobotStatus.size();
                    funduszRemontowySr/=last12RobotStatus.size();

                    lastRobotStatus.setPrad(pradSr);
                    lastRobotStatus.setGaz(gazSr);
                    lastRobotStatus.setWoda_ciepla(woda_cieplaSr);
                    lastRobotStatus.setWoda_zimna(woda_zimnaSr);
                    lastRobotStatus.setScieki(sciekiSr);
                    lastRobotStatus.setOgrzewanie(ogrzewanieSr);
                    lastRobotStatus.setFunduszRemontowy(funduszRemontowySr);

                    System.out.println("Zapisywanie kwot ryczalt srednia");
                    lastRobotStatus.setAccepted(true);

                }else{//brak zaakceptowanych wpisów
                    //ryczalt * liczba mieszkancow

                    int liczLokatorow = userService.getUserAppByRobot(tempRobot.getId()).size();

                    lastRobotStatus.setPrad(liczLokatorow*SpStationsControl.prad_ryczalt);
                    lastRobotStatus.setGaz(liczLokatorow*SpStationsControl.gaz_ryczalt);
                    lastRobotStatus.setWoda_ciepla(liczLokatorow*SpStationsControl.woda_ciepla_ryczalt);
                    lastRobotStatus.setWoda_zimna(liczLokatorow*SpStationsControl.woda_zimna_ryczalt);
                    lastRobotStatus.setScieki(liczLokatorow*SpStationsControl.scieki_ryczalt);
                    lastRobotStatus.setOgrzewanie(liczLokatorow*SpStationsControl.ogrzewanie_ryczalt);
                    lastRobotStatus.setFunduszRemontowy(SpStationsControl.funduszRemontowy_ryczalt);// na mieszkanie

                    System.out.println("Zapisywanie kwot ryczalt domyslny");
                    lastRobotStatus.setAccepted(true);

                }
                //zapisywanie ryczaltu
                robotStatusService.editRobotStatus(lastRobotStatus);

                List<SpUserApp> robotUsers = userService.getUserAppByRobot(tempRobot.getId());


                //wysylanie maila z rachunkiem ryczalt
                for (SpUserApp robprog : robotUsers) {
                    System.out.println("wysylanie maila z rachunkiem RYCZALT "+tempRobot.getId()+" usr "+robprog.getEmail());


                    mailService.sendSimpleMessage(robprog.getEmail(), "Bill for "+lastRobotStatus.getData().getMonth()+" "+lastRobotStatus.getData().getYear(), "Hello "+robprog.getFirstName()+" "+robprog.getLastName()+"\r\nHere we are sending you bill with a lump sum for:"+lastRobotStatus.getData().getMonth()+"_"+lastRobotStatus.getData().getYear()+" : "+"http://localhost:8080/rachunekr_"+robprog.getId()+"_"+lastRobotStatus.getId());

                }



            }

        }



    }

    @Scheduled(cron = "0 2/5 * * * ?")//TODO CO GODZINE MINUTE PRZED PEŁNA //@Scheduled(cron = "0 0/2 * * * ?")// co 10 s // co druga minuta parzysta
    public void mailSendReminder(){// przypomnienie o uzupełnieniu odczytow jesli odczyty nie zaakceptowane
        System.out.println("Wyzwolenie co 2 min mail przypomnienie odczyty");

        List<SpRobot> robotList = robotService.listRobots();

        for (SpRobot tempRobot : robotList) {
            SpRobotStatus lastRobotStatus = robotStatusService.getLastRobotStatusFromRobot(tempRobot.getId());
            System.out.println("DATA: " + lastRobotStatus.getData().getYear() + " _ " + lastRobotStatus.getData().getMonth());

            if (!lastRobotStatus.isAccepted()) {//todo Wysłać przypomnienie na maila o uzupełnienieniu odczytów
                List<SpUserApp> robotUsers = userService.getUserAppByRobot(tempRobot.getId());


                //wysylanie pRZYPOMNIENIA
                for (SpUserApp robprog : robotUsers) {
                    mailService.sendSimpleMessage(robprog.getEmail(), "Reminder "+lastRobotStatus.getData().getMonth()+" "+lastRobotStatus.getData().getYear(), "Hello "+robprog.getFirstName()+" "+robprog.getLastName()+"\r\nWe would like to remind You to fill readings for current period: "+lastRobotStatus.getData().getMonth()+"_"+lastRobotStatus.getData().getYear()+", robot: "+"Ul. "+tempRobot.getStation().getStreet()+" nr. "+tempRobot.getStation().getStationNumber()+"/"+tempRobot.getRobotNumber()+", " +tempRobot.getStation().getPostalCode()+", "+tempRobot.getStation().getCity());

                }


            }
        }


    }*/


    //@Scheduled(cron = "*/30 * * * * *")// co 30 s //todo (cron = "1 0 0 1 * ? *") domyslnie ma to być co miesiac i bedzie wtedy generowana nowa krotka z miesiacem i odczytami
    /*public void newMonthRowGen(){
        System.out.println("Wyzwolenie co 30 s - tworzenie nowej krotki ");

        List<SpRobot> robotsList = robotService.listRobots();



        for (SpRobot sRobot : robotsList) {
            SpRobotStatus lastRobotStatus = null;
            for (SpRobotStatus tempRobotStatus : sRobot.getRobotStatus()) {// wpis z ostatniego miesiaca
                if(lastRobotStatus==null){
                    lastRobotStatus=tempRobotStatus;
                }else if(lastRobotStatus.getData().getTime()<tempRobotStatus.getData().getTime()){
                    lastRobotStatus = tempRobotStatus;
                }
            }


            LocalDate currentdate = LocalDate.now();

            if(lastRobotStatus.getData().getYear()<currentdate.getYear() || lastRobotStatus.getData().getMonth()!=currentdate.getMonthValue()){
                //tworzymy odczyty dla nowego miesiaca
                System.out.println("tworzymy odczyty dla nowego miesiaca "+sRobot.getRobotNumber()+" "+lastRobotStatus.getData().getMonth());

                SpRobotStatus nRobotStatus = new SpRobotStatus();
                nRobotStatus.setRobot(sRobot);
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
                nRobotStatus.setFunduszRemontowy_stawka(SpStationsControl.cfunduszRemontowy_stawka);
                nRobotStatus.setGaz_stawka(SpStationsControl.cgaz_stawka);
                nRobotStatus.setOgrzewanie_stawka(SpStationsControl.cogrzewanie_stawka);
                nRobotStatus.setPrad_stawka(SpStationsControl.cprad_stawka);
                nRobotStatus.setScieki_stawka(SpStationsControl.cscieki_stawka);
                nRobotStatus.setWoda_zimna_stawka(SpStationsControl.cwoda_zimna_stawka);
                nRobotStatus.setWoda_ciepla_stawka(SpStationsControl.cwoda_ciepla_stawka);
                //todo stawki


                System.out.println(date.getYear()+" miesiac: "+date.getMonth()+" dzien "+date.getDate());

                nRobotStatus.setData(date);

                //List<SpRobotStatus> oldStatus = sRobot.getRobotStatus();
                //oldStatus.add(nRobotStatus);
                sRobot.addRobotStatus(nRobotStatus);

                robotStatusService.addRobotStatus(nRobotStatus);
                robotService.editRobot(sRobot);


            }else{
                //nie tworzymy, nadal aktualny miesiac
                System.out.println("nie tworzymy, nadal aktualny miesiac "+sRobot.getRobotNumber()+" "+lastRobotStatus.getData().getMonth());

            }












        }









    }
}
*/