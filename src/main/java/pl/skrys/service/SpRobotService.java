package pl.skrys.service;

import pl.skrys.app.SpRobot;

import java.util.List;

public interface SpRobotService {

    void addRobot(SpRobot robot);
    void editRobot(SpRobot robot);
    List<SpRobot> listRobots();
    SpRobot getRobot(long id);
    void removeRobot(long id);
}