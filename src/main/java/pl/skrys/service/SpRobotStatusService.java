package pl.skrys.service;

import org.springframework.data.domain.Pageable;
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
    List<SpRobotStatus>  getRobotStatusFromRobotStronicowane(long robotId, Pageable pageable);
    long gerNumberOfPagesRobotStatusFromRobot(long robotId, int pageSize);
    Long getCountOfRobotStatusAcceptedFromRobot(long robotId);
    List<SpRobotStatus> getLast12AcceptedRobotStatusByRobot(long robotId);
}
