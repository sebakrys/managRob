package pl.skrys.service;

import pl.skrys.app.SpRobotCharges;

import java.util.List;

public interface SpRobotChargesService {


    void addRobotCharges(SpRobotCharges robotCharges);
    void editRobotCharges(SpRobotCharges robotCharges);
    List<SpRobotCharges> listRobotCharges();
    SpRobotCharges getRobotCharges(long id);
    void removeRobotCharges(long id);

    SpRobotCharges getLastRobotChargesFromRobot(long robotId);
    Long getCountOfRobotChargesAcceptedFromRobot(long robotId);
    List<SpRobotCharges> getLast12AcceptedRobotChargesByRobot(long robotId);
}
