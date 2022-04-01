package pl.skrys.service;

import pl.skrys.app.SpRobot;

import java.util.List;

public interface SpRobotService {

    void addRobot(SpRobot robot);
    void editRobot(SpRobot robot);
    List<SpRobot> listRobots();
    List<SpRobot> getRobotsByName(String name);
    SpRobot getRobot(long id);
    SpRobot getSpRobot(String robotNumber);
    void removeRobot(long id);
}
