package pl.skrys.service;

import pl.skrys.app.SpFlatCharges;

import java.util.List;

public interface SpFlatChargesService {


    void addFlatCharges(SpFlatCharges flatCharges);
    void editFlatCharges(SpFlatCharges flatCharges);
    List<SpFlatCharges> listFlatCharges();
    SpFlatCharges getFlatCharges(long id);
    void removeFlatCharges(long id);

    SpFlatCharges getLastFlatChargesFromFlat(long flatId);
    Long getCountOfFlatChargesAcceptedFromFlat(long flatId);
    List<SpFlatCharges> getLast12AcceptedFlatChargesByFlat(long flatId);
}
