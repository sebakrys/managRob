package pl.skrys.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import pl.skrys.app.Project;
import pl.skrys.app.SpStation;
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpUserApp;

import java.util.List;

public interface SpUserService {

    //@Secured("ROLE_ADMIN")
    public void addUser(SpUserApp user);
    public void activateUserApp(SpUserApp userApp, boolean enabled);
    public List<SpUserApp> getUserAppByStation(long StationId);
    public List<SpUserApp> getUserAppByProject(long ProjectId);
    List<SpUserApp> getUserAppByRobot(long robotId);
    void addUserStations(SpUserApp user);
    void addUserProjects(SpUserApp user);

    void removeUserStation(SpUserApp user, SpStation station);
    void removeUserProject(SpUserApp user, Project project);
    void addUserRobot(SpUserApp user);
    void removeUserRobot(SpUserApp user, SpRobot robot);

    @PreAuthorize("hasRole('ROLE_ADMIN') OR (#spUserApp.email == principal.username)")//TODO
    public void editUserApp(SpUserApp user);
    public void editUserAppWoPassword(SpUserApp user);
    void editUserAppRole(SpUserApp user);
    public void editPassword(SpUserApp user);
    public List<SpUserApp> listUserApp();

    @Secured("ROLE_ADMIN")
    public void removeUserApp(long id);
    public SpUserApp getUserApp(long id);

    //SpUserApp findByPesel(String pesel);
    List<SpUserApp> findByTel(String tele);


    SpUserApp findByEmail(String email);

    boolean isThisProjectManager(SpUserApp userApp, Long projectId);
    boolean hasRoleAdmin(SpUserApp userApp);
    boolean hasRoleManager(SpUserApp userApp);
    boolean hasRoleRobProg(SpUserApp userApp);
}
