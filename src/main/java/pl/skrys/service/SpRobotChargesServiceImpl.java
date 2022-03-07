package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.skrys.app.SpRobotCharges;
import pl.skrys.dao.SpRobotChargesRepository;

import java.util.List;

@Service("robotChargesService")
public class SpRobotChargesServiceImpl implements SpRobotChargesService{


    private SpRobotChargesRepository spRobotChargesRepository;

    @Autowired
    public SpRobotChargesServiceImpl(SpRobotChargesRepository spRobotChargesRepository) {
        this.spRobotChargesRepository = spRobotChargesRepository;
    }

    @Transactional
    public List<SpRobotCharges> getLast12AcceptedRobotChargesByRobot(long robotId) {
        return spRobotChargesRepository.findTop12ByRobotIdAndAcceptedOrderByDataDesc(robotId, true);
    }


    @Transactional
    public Long getCountOfRobotChargesAcceptedFromRobot(long robotId){
        return spRobotChargesRepository.getNumberOfAcceptedRobotCharges(robotId);
    }

    @Transactional
    public SpRobotCharges getLastRobotChargesFromRobot(long robotId){
        return spRobotChargesRepository.findFirstByRobotIdOrderByDataDesc(robotId);
    }


    @Transactional
    public void addRobotCharges(SpRobotCharges robotCharges) {

        spRobotChargesRepository.save(robotCharges);
    }

    @Transactional
    public void editRobotCharges(SpRobotCharges robotCharges) {

        spRobotChargesRepository.save(robotCharges);
    }

    @Transactional
    public List<SpRobotCharges> listRobotCharges() {
        return spRobotChargesRepository.findAll();
    }

    @Transactional
    public SpRobotCharges getRobotCharges(long id) {
        return spRobotChargesRepository.findById(id);
    }

    @Transactional
    public void removeRobotCharges(long id) {
        spRobotChargesRepository.delete(id);
    }




}
