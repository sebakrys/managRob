package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.skrys.app.*;
import pl.skrys.dao.SpUserRepository;
import pl.skrys.dao.UserRoleRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SpUserServiceImpl implements SpUserService {

    private SpUserRepository spUserRepository;
    private UserRoleRepository userRoleRepository;
    private SpMailService spMailService;

    @Autowired
    public SpUserServiceImpl(SpUserRepository spUserRepository, UserRoleRepository userAppRoleRepository, SpMailService spMailService){
        this.spUserRepository = spUserRepository;
        this.userRoleRepository = userAppRoleRepository;
        this.spMailService = spMailService;
    }


    /*@Autowired
    public UserAppServiceImpl(UserAppRepository userAppRepository){
        this.userAppRepository = userAppRepository;
    }*/

    @Transactional
    public void addUser(SpUserApp spUserApp) {
        System.out.println("addUserApp SPUserServiceIMPL");
        spUserApp.getUserRole().add(userRoleRepository.findByRole("ROLE_ROBPROG"));//domyślna rola podczas tworzenia



        //TODO TOKEN





        spUserApp.setPassword(hashPassword(spUserApp.getPassword()));
        System.out.println("przeszło addUserApp SPUserServiceIMPL");
        spUserRepository.save(spUserApp);
        System.out.println("próba wyslania maila: "+spUserApp.getEmail());


        //wczesniej tu było wysyłanie maila


    }

    @Transactional
    public void editUserApp(SpUserApp userApp) {
        //userApp.getUserAppRole().add(userAppRoleRepository.findByRole("ROLE_ROBPROG"));//TODO domyślna rola podczas edycji
        userApp.setPassword(hashPassword(userApp.getPassword()));
        spUserRepository.save(userApp);
    }

    @Override
    public void editUserAppWoPassword(SpUserApp user) {
        //user.setPassword(hashPassword(user.getPassword()));

        SpUserApp oldUser = getUserApp(user.getId());

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_ROBPROG"));

        System.out.println("stare hasło"+oldUser.getPassword());//todo usunac
        System.out.println("nowe hasło"+user.getPassword());

        user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        user.setRobot(oldUser.getRobot());//przepsanie mieszkań
        user.setStations(oldUser.getStations());//przepisane stacji
        user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        user.setConfirmPassword(oldUser.getPassword());

        System.out.println("zapisywane hasło"+user.getPassword());//todo usunac
        spUserRepository.save(user);
    }

    @Override
    public void editUserAppRole(SpUserApp user) {
        //user.setPassword(hashPassword(user.getPassword()));

        SpUserApp oldUser = getUserApp(user.getId());

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_ROBPROG"));

        System.out.println("stare hasło"+oldUser.getPassword());//todo usunac
        System.out.println("nowe hasło"+user.getPassword());

        user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        user.setRobot(oldUser.getRobot());//przepsanie mieszkań
        user.setStations(oldUser.getStations());//przepisane stacji
        user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        user.setConfirmPassword(oldUser.getPassword());

        System.out.println("zapisywane hasło"+user.getPassword());//todo usunac
        spUserRepository.save(user);
    }

    public void addUserProjects(SpUserApp user){
        //user.setPassword(hashPassword(user.getPassword()));

        SpUserApp oldUser = getUserApp(user.getId());

        Set<Project> oldProjects = oldUser.getProjects();
        oldProjects.addAll(user.getProjects());

        oldUser.setProjects(oldProjects);

        user = oldUser;

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_ROBPROG"));



        //user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        //user.setRobot(oldUser.getRobot());//przepsanie mieszkań
        //user.setStations(oldUser.getStations());//przepisane stacji
        //user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        //user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        //user.setConfirmPassword(oldUser.getPassword());


        spUserRepository.save(user);
    }

    public void addUserStations(SpUserApp user) {//todo
        //user.setPassword(hashPassword(user.getPassword()));

        SpUserApp oldUser = getUserApp(user.getId());

        Set<SpStation> oldStations = oldUser.getStations();
        oldStations.addAll(user.getStations());

        oldUser.setStations(oldStations);

        user = oldUser;

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_ROBPROG"));



        //user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        //user.setRobot(oldUser.getRobot());//przepsanie mieszkań
        //user.setStations(oldUser.getStations());//przepisane stacji
        //user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        //user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        //user.setConfirmPassword(oldUser.getPassword());


        spUserRepository.save(user);
    }

    public void removeUserProject(SpUserApp user, Project project){
        //user.setPassword(hashPassword(user.getPassword()));

        System.out.println("Usuwanie "+user.getPesel());

        SpUserApp oldUser = getUserApp(user.getId());

        Set<Project> oldProjects = oldUser.getProjects();
        //SpStation tempBuilToDel = new SpStation();

        System.out.println("Przed usunieciem: "+oldProjects);
        for (Project tempProject : oldProjects) {
            if(tempProject.getId()==project.getId()){
                //tempBuilToDel = tempBuild
                boolean suc = oldProjects.remove(tempProject);
                System.out.println("suc: "+suc+" "+tempProject);
                break;
            }
        }

        System.out.println("Po usunieciu: "+oldProjects);
        //oldStations.addAll(user.getStations());

        oldUser.setProjects(oldProjects);

        user = oldUser;

        spUserRepository.save(user);
    }

    public void removeUserStation(SpUserApp user, SpStation station) {
        //user.setPassword(hashPassword(user.getPassword()));

        System.out.println("Usuwanie "+user.getPesel());

        SpUserApp oldUser = getUserApp(user.getId());

        Set<SpStation> oldStations = oldUser.getStations();
        //SpStation tempBuilToDel = new SpStation();

        System.out.println("Przed usunieciem: "+oldStations);
        for (SpStation tempBuild : oldStations) {
            if(tempBuild.getId()==station.getId()){
                //tempBuilToDel = tempBuild
                boolean suc = oldStations.remove(tempBuild);
                System.out.println("suc: "+suc+" "+tempBuild);
                break;
            }
        }



        System.out.println("Po usunieciu: "+oldStations);
        //oldStations.addAll(user.getStations());

        oldUser.setStations(oldStations);

        user = oldUser;

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_ROBPROG"));



        //user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        //user.setRobot(oldUser.getRobot());//przepsanie mieszkań
        //user.setStations(oldUser.getStations());//przepisane stacji
        //user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        //user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        //user.setConfirmPassword(oldUser.getPassword());


        spUserRepository.save(user);
    }




    public void addUserRobot(SpUserApp user) {//todo
        //user.setPassword(hashPassword(user.getPassword()));

        SpUserApp oldUser = getUserApp(user.getId());

        Set<SpRobot> oldRobot = oldUser.getRobot();
        oldRobot.addAll(user.getRobot());

        oldUser.setRobot(oldRobot);

        user = oldUser;

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_ROBPROG"));



        //user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        //user.setRobot(oldUser.getRobot());//przepsanie mieszkań
        //user.setStations(oldUser.getStations());//przepisane stacji
        //user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        //user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        //user.setConfirmPassword(oldUser.getPassword());


        spUserRepository.save(user);
    }




    public void removeUserRobot(SpUserApp user, SpRobot robot) {
        //user.setPassword(hashPassword(user.getPassword()));

        System.out.println("Usuwanie "+user.getPesel());

        SpUserApp oldUser = getUserApp(user.getId());

        Set<SpRobot> oldRobots = oldUser.getRobot();
        //SpStation tempBuilToDel = new SpStation();

        System.out.println("Przed usunieciem: "+oldRobots);
        for (SpRobot tempRobot : oldRobots) {
            if(tempRobot.getId()==robot.getId()){
                //tempBuilToDel = tempBuild
                boolean suc = oldRobots.remove(tempRobot);
                System.out.println("suc: "+suc+" "+tempRobot);
                break;
            }
        }



        System.out.println("Po usunieciu: "+oldRobots);
        //oldStations.addAll(user.getStations());

        oldUser.setRobot(oldRobots);

        user = oldUser;

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_ROBPROG"));



        //user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        //user.setRobot(oldUser.getRobot());//przepsanie mieszkań
        //user.setStations(oldUser.getStations());//przepisane stacji
        //user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        //user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        //user.setConfirmPassword(oldUser.getPassword());


        spUserRepository.save(user);
    }



    @Override
    public void editPassword(SpUserApp user) {
        String nPass = hashPassword(user.getPassword());

        SpUserApp oldUser = getUserApp(user.getId());

        user = oldUser;
        user.setPassword(nPass);
        spUserRepository.save(user);
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }



    @Transactional
    public List<SpUserApp> listUserApp() {
        return spUserRepository.findAll();
    }

    @Transactional
    public void removeUserApp(long id) {
        spUserRepository.delete(id);
    }

    @Transactional
    public SpUserApp getUserApp(long id) {
        return spUserRepository.findById(id);
    }

    @Transactional
    public List<SpUserApp> getUserAppByStation(long StationId) {
        return spUserRepository.findByStationId(StationId);
    }

    @Transactional
    public List<SpUserApp> getUserAppByProject(long ProjectId){
        return spUserRepository.findByProjectId(ProjectId);
    }


    @Transactional
    public List<SpUserApp> getUserAppByRobot(long robotId) {
        return spUserRepository.findByByRobotId(robotId);
    }


    @Transactional
    public void activateUserApp(SpUserApp userApp, boolean enabled) {
        System.out.println("ACTIVATEuser "+userApp.getPesel()+" "+enabled);
        userApp.setEnabled(enabled);//TODO ACTIVATE
        spUserRepository.save(userApp);
    }


    @Override
    public SpUserApp findByPesel(String pesel) {
        return spUserRepository.findByPesel(pesel);
    }

    @Override
    public List<SpUserApp> findByTel(String tele) {
        return spUserRepository.findByTelephone(tele);
    }

    @Override
    public List<SpUserApp> findByEmail(String email) {
        return spUserRepository.findByEmail(email);
    }


}
