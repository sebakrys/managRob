package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.skrys.app.SpRobotStatus;
import pl.skrys.dao.SpRobotStatusRepository;

import java.util.List;

@Service("robotStatusService")
public class SpRobotStatusServiceImpl implements SpRobotStatusService{


    private SpRobotStatusRepository spRobotStatusRepository;

    @Autowired
    public SpRobotStatusServiceImpl(SpRobotStatusRepository spRobotStatusRepository) {
        this.spRobotStatusRepository = spRobotStatusRepository;
    }

    @Transactional
    public List<SpRobotStatus> getLast12AcceptedRobotStatusByRobot(long robotId) {
        return spRobotStatusRepository.findTop12ByRobotIdAndAcceptedOrderByDataDesc(robotId, true);
    }


    @Transactional
    public Long getCountOfRobotStatusAcceptedFromRobot(long robotId){
        return spRobotStatusRepository.getNumberOfAcceptedRobotStatus(robotId);
    }

    @Transactional
    public SpRobotStatus getLastRobotStatusFromRobot(long robotId){
        return spRobotStatusRepository.findFirstByRobotIdOrderByDataDesc(robotId);
    }

    @Transactional
    public List<SpRobotStatus>  getRobotStatusFromRobot(long robotId){
        return spRobotStatusRepository.findByRobotIdOrderByDataDesc(robotId);
    }


    @Transactional
    public void addRobotStatus(SpRobotStatus robotStatus) {

        spRobotStatusRepository.save(robotStatus);
    }

    @Transactional
    public void editRobotStatus(SpRobotStatus robotStatus) {

        spRobotStatusRepository.save(robotStatus);
    }

    @Transactional
    public List<SpRobotStatus> listRobotStatus() {
        return spRobotStatusRepository.findAll();
    }

    @Transactional
    public SpRobotStatus getRobotStatus(long id) {
        return spRobotStatusRepository.findById(id);
    }

    @Transactional
    public void removeRobotStatus(long id) {
        spRobotStatusRepository.delete(id);
    }




}
