package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.skrys.app.SpBuilding;
import pl.skrys.app.SpUserApp;
import pl.skrys.app.SpVerifyToken;
import pl.skrys.dao.SpBuildingRepository;
import pl.skrys.dao.SpUserRepository;
import pl.skrys.dao.SpVerifyTokenRepository;

import java.util.List;

@Service("buildingService")
public class SpBuildingServiceImpl implements SpBuildingService{


    private SpBuildingRepository spBuildingRepository;
    private SpUserRepository spUserService;

    @Autowired
    public SpBuildingServiceImpl(SpBuildingRepository spBuildingRepository, SpUserRepository spUserService) {
        this.spBuildingRepository = spBuildingRepository;
        this.spUserService = spUserService;
    }





    @Transactional
    public void addBuilding(SpBuilding building) {

        spBuildingRepository.save(building);
    }

    @Transactional
    public void editBuilding(SpBuilding building) {

        spBuildingRepository.save(building);
    }

    @Transactional
    public List<SpBuilding> listBuildings() {
        return spBuildingRepository.findAll();
    }

    @Transactional
    public SpBuilding getBuilding(long id) {
        return spBuildingRepository.findById(id);
    }

    @Transactional
    public SpBuilding getBuildingByName(String name) {
        return spBuildingRepository.findByNazwa(name);
    }

    @Transactional
    public void removeBuilding(long id) {
        spBuildingRepository.delete(id);
    }

}
