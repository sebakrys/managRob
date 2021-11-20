package pl.skrys.service;

import pl.skrys.app.SpFlat;

import java.util.List;

public interface SpFlatService {

    void addFlat(SpFlat flat);
    void editFlat(SpFlat flat);
    List<SpFlat> listFlats();
    SpFlat getFlat(long id);
    void removeFlat(long id);
}
