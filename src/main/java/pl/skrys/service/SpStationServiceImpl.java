package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.skrys.app.SpStation;
import pl.skrys.dao.SpStationRepository;
import pl.skrys.dao.SpUserRepository;

import java.util.List;

@Service("stationService")
public class SpStationServiceImpl implements SpStationService{


    private SpStationRepository spStationRepository;
    private SpUserRepository spUserService;

    @Autowired
    public SpStationServiceImpl(SpStationRepository spStationRepository, SpUserRepository spUserService) {
        this.spStationRepository = spStationRepository;
        this.spUserService = spUserService;
    }





    @Transactional
    public void addStation(SpStation station) {

        spStationRepository.save(station);
    }

    @Transactional
    public void editStation(SpStation station) {

        spStationRepository.save(station);
    }

    @Transactional
    public List<SpStation> listStations() {
        return spStationRepository.findAll();
    }

    @Transactional
    public SpStation getStation(long id) {
        return spStationRepository.findById(id);
    }

    @Transactional
    public SpStation getStationByName(String name) {
        return spStationRepository.findByNazwa(name);
    }

    @Transactional
    public void removeStation(long id) {
        spStationRepository.delete(id);
    }

}
