package pl.skrys.service;

import pl.skrys.app.SpRobotStatus;

import java.util.List;

public interface SpRobotStatusService {


    void addRobotStatus(SpRobotStatus robotStatus);
    void editRobotStatus(SpRobotStatus robotStatus);
    List<SpRobotStatus> listRobotStatus();
    SpRobotStatus getRobotStatus(long id);
    void removeRobotStatus(long id);

    SpRobotStatus getLastRobotStatusFromRobot(long robotId);
    List<SpRobotStatus>  getRobotStatusFromRobot(long robotId);
    Long getCountOfRobotStatusAcceptedFromRobot(long robotId);
    List<SpRobotStatus> getLast12AcceptedRobotStatusByRobot(long robotId);
}
