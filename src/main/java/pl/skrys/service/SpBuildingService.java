package pl.skrys.service;

import pl.skrys.app.SpBuilding;
import pl.skrys.app.SpUserApp;

import java.util.List;

public interface SpBuildingService {

    //List<SpUserApp> listZarzadcy(SpBuilding building);todo
    void addBuilding(SpBuilding building);
    void editBuilding(SpBuilding building);
    List<SpBuilding> listBuildings();
    SpBuilding getBuilding(long id);
    SpBuilding getBuildingByName(String name);
    void removeBuilding(long id);
}
