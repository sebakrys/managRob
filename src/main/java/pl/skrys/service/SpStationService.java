package pl.skrys.service;

import pl.skrys.app.SpStation;

import java.util.List;

public interface SpStationService {

    //List<SpUserApp> listManagers(SpStation station);todo
    void addStation(SpStation station);
    void editStation(SpStation station);
    List<SpStation> listStations();
    List<SpStation> listStationsByProject(long id);
    SpStation getStation(long id);
    SpStation getStationByName(String name);
    void removeStation(long id);
}
