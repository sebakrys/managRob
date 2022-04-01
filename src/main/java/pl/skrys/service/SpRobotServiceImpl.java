package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.skrys.app.SpStation;
import pl.skrys.app.SpRobot;
import pl.skrys.dao.SpStationRepository;
import pl.skrys.dao.SpRobotRepository;

import java.util.List;

@Service("robotService")
public class SpRobotServiceImpl implements SpRobotService{

    private SpRobotRepository spRobotRepository;
    private SpStationRepository stationRepository;
    private SpStationService stationService;
    private SpRobotStatusService robotStatusService;


    @Autowired
    public SpRobotServiceImpl(SpRobotRepository spRobotRepository, SpStationRepository stationRepository, SpStationService stationService, SpRobotStatusService robotStatusService) {
        this.spRobotRepository = spRobotRepository;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.robotStatusService = robotStatusService;
    }






    @Transactional
    public void addRobot(SpRobot robot) {

        spRobotRepository.save(robot);
    }

    @Transactional
    public void editRobot(SpRobot robot) {

        spRobotRepository.save(robot);
    }

    @Transactional
    public List<SpRobot> listRobots() {
        return spRobotRepository.findAll();
    }

    @Override
    public List<SpRobot> getRobotsByName(String name) {
        return spRobotRepository.findByRobotNumberContainingIgnoreCase(name);
    }

    @Transactional
    public SpRobot getRobot(long id) {
        return spRobotRepository.findById(id);
    }

    @Override
    public SpRobot getSpRobot(String robotNumber) {
        return spRobotRepository.findByRobotNumber(robotNumber);
    }

    @Transactional
    public void removeRobot(long id) {
        System.out.println("Usuwanie mieszkania service "+id);

        //this.parent.dismissChild(this); //SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
        //this.parent = null;




        SpRobot tempRobot = getRobot(id);
        SpStation tempStation = stationService.getStation(tempRobot.getStation().getId());


        //todo getRobot(id).setStation(null);
        //todo tempStation.removeRobot(tempRobot);
        tempRobot.getStation().removeRobot(tempRobot);
        spRobotRepository.delete(tempRobot);

        System.out.println("mieszkania powinno byc usuniete service "+id);
    }

}
