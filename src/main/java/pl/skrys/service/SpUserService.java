package pl.skrys.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import pl.skrys.app.SpBuilding;
import pl.skrys.app.SpFlat;
import pl.skrys.app.SpUserApp;

import java.util.List;

public interface SpUserService {

    //@Secured("ROLE_ADMIN")
    public void addUser(SpUserApp user);
    public void activateUserApp(SpUserApp userApp, boolean enabled);
    public List<SpUserApp> getUserAppByBuilding(long BuildingId);
    List<SpUserApp> getUserAppByFlat(long flatId);
    void addUserBuildings(SpUserApp user);
    void removeUserBuilding(SpUserApp user, SpBuilding building);
    void addUserFlat(SpUserApp user);
    void removeUserFlat(SpUserApp user, SpFlat flat);

    @PreAuthorize("hasRole('ROLE_ADMIN') OR (#spUserApp.pesel == principal.username)")//TODO
    public void editUserApp(SpUserApp user);
    public void editUserAppWoPassword(SpUserApp user);
    void editUserAppRole(SpUserApp user);
    public void editPassword(SpUserApp user);
    public List<SpUserApp> listUserApp();

    @Secured("ROLE_ADMIN")
    public void removeUserApp(long id);
    public SpUserApp getUserApp(long id);



    SpUserApp findByPesel(String pesel);
    List<SpUserApp> findByTel(String tele);


    List<SpUserApp> findByEmail(String email);
}
