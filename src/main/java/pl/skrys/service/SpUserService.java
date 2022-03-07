package pl.skrys.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import pl.skrys.app.SpStation;
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpUserApp;

import java.util.List;

public interface SpUserService {

    //@Secured("ROLE_ADMIN")
    public void addUser(SpUserApp user);
    public void activateUserApp(SpUserApp userApp, boolean enabled);
    public List<SpUserApp> getUserAppByStation(long StationId);
    List<SpUserApp> getUserAppByRobot(long robotId);
    void addUserStations(SpUserApp user);
    void removeUserStation(SpUserApp user, SpStation station);
    void addUserRobot(SpUserApp user);
    void removeUserRobot(SpUserApp user, SpRobot robot);

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
