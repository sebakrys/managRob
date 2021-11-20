package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.skrys.app.SpBuilding;
import pl.skrys.app.SpFlat;
import pl.skrys.dao.SpBuildingRepository;
import pl.skrys.dao.SpFlatChargesRepository;
import pl.skrys.dao.SpFlatRepository;

import java.util.List;

@Service("flatService")
public class SpFlatServiceImpl implements SpFlatService{

    private SpFlatRepository spFlatRepository;
    private SpBuildingRepository buildingRepository;
    private SpBuildingService buildingService;
    private SpFlatChargesService flatChargesService;


    @Autowired
    public SpFlatServiceImpl(SpFlatRepository spFlatRepository, SpBuildingRepository buildingRepository, SpBuildingService buildingService, SpFlatChargesService flatChargesService) {
        this.spFlatRepository = spFlatRepository;
        this.buildingRepository = buildingRepository;
        this.buildingService = buildingService;
        this.flatChargesService = flatChargesService;
    }






    @Transactional
    public void addFlat(SpFlat flat) {

        spFlatRepository.save(flat);
    }

    @Transactional
    public void editFlat(SpFlat flat) {

        spFlatRepository.save(flat);
    }

    @Transactional
    public List<SpFlat> listFlats() {
        return spFlatRepository.findAll();
    }

    @Transactional
    public SpFlat getFlat(long id) {
        return spFlatRepository.findById(id);
    }

    @Transactional
    public void removeFlat(long id) {
        System.out.println("Usuwanie mieszkania service "+id);

        //this.parent.dismissChild(this); //SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
        //this.parent = null;




        SpFlat tempFlat = getFlat(id);
        SpBuilding tempBuilding = buildingService.getBuilding(tempFlat.getBuilding().getId());


        //todo getFlat(id).setBuilding(null);
        //todo tempBuilding.removeFlat(tempFlat);
        tempFlat.getBuilding().removeFlat(tempFlat);
        spFlatRepository.delete(tempFlat);

        System.out.println("mieszkania powinno byc usuniete service "+id);
    }

}
