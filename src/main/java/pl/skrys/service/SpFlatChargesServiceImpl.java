package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.skrys.app.SpFlat;
import pl.skrys.app.SpFlatCharges;
import pl.skrys.dao.SpFlatChargesRepository;
import pl.skrys.dao.SpFlatRepository;

import java.util.List;

@Service("flatChargesService")
public class SpFlatChargesServiceImpl implements SpFlatChargesService{


    private SpFlatChargesRepository spFlatChargesRepository;

    @Autowired
    public SpFlatChargesServiceImpl(SpFlatChargesRepository spFlatChargesRepository) {
        this.spFlatChargesRepository = spFlatChargesRepository;
    }

    @Transactional
    public List<SpFlatCharges> getLast12AcceptedFlatChargesByFlat(long flatId) {
        return spFlatChargesRepository.findTop12ByFlatIdAndAcceptedOrderByDataDesc(flatId, true);
    }


    @Transactional
    public Long getCountOfFlatChargesAcceptedFromFlat(long flatId){
        return spFlatChargesRepository.getNumberOfAcceptedFlatCharges(flatId);
    }

    @Transactional
    public SpFlatCharges getLastFlatChargesFromFlat(long flatId){
        return spFlatChargesRepository.findFirstByFlatIdOrderByDataDesc(flatId);
    }


    @Transactional
    public void addFlatCharges(SpFlatCharges flatCharges) {

        spFlatChargesRepository.save(flatCharges);
    }

    @Transactional
    public void editFlatCharges(SpFlatCharges flatCharges) {

        spFlatChargesRepository.save(flatCharges);
    }

    @Transactional
    public List<SpFlatCharges> listFlatCharges() {
        return spFlatChargesRepository.findAll();
    }

    @Transactional
    public SpFlatCharges getFlatCharges(long id) {
        return spFlatChargesRepository.findById(id);
    }

    @Transactional
    public void removeFlatCharges(long id) {
        spFlatChargesRepository.delete(id);
    }




}
