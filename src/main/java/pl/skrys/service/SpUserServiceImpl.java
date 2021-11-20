package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.skrys.app.SpBuilding;
import pl.skrys.app.SpFlat;
import pl.skrys.app.SpUserApp;
import pl.skrys.app.SpVerifyToken;
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
        spUserApp.getUserRole().add(userRoleRepository.findByRole("ROLE_MIESZKANIEC"));//domyślna rola podczas tworzenia



        //TODO TOKEN





        spUserApp.setPassword(hashPassword(spUserApp.getPassword()));
        System.out.println("przeszło addUserApp SPUserServiceIMPL");
        spUserRepository.save(spUserApp);
        System.out.println("próba wyslania maila: "+spUserApp.getEmail());


        //wczesniej tu było wysyłanie maila


    }

    @Transactional
    public void editUserApp(SpUserApp userApp) {
        //userApp.getUserAppRole().add(userAppRoleRepository.findByRole("ROLE_MIESZKANIEC"));//TODO domyślna rola podczas edycji
        userApp.setPassword(hashPassword(userApp.getPassword()));
        spUserRepository.save(userApp);
    }

    @Override
    public void editUserAppWoPassword(SpUserApp user) {
        //user.setPassword(hashPassword(user.getPassword()));

        SpUserApp oldUser = getUserApp(user.getId());

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_MIESZKANIEC"));

        System.out.println("stare hasło"+oldUser.getPassword());//todo usunac
        System.out.println("nowe hasło"+user.getPassword());

        user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        user.setFlat(oldUser.getFlat());//przepsanie mieszkań
        user.setBuildings(oldUser.getBuildings());//przepisane budynków
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

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_MIESZKANIEC"));

        System.out.println("stare hasło"+oldUser.getPassword());//todo usunac
        System.out.println("nowe hasło"+user.getPassword());

        user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        user.setFlat(oldUser.getFlat());//przepsanie mieszkań
        user.setBuildings(oldUser.getBuildings());//przepisane budynków
        user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        user.setConfirmPassword(oldUser.getPassword());

        System.out.println("zapisywane hasło"+user.getPassword());//todo usunac
        spUserRepository.save(user);
    }


    public void addUserBuildings(SpUserApp user) {//todo
        //user.setPassword(hashPassword(user.getPassword()));

        SpUserApp oldUser = getUserApp(user.getId());

        Set<SpBuilding> oldBuildings = oldUser.getBuildings();
        oldBuildings.addAll(user.getBuildings());

        oldUser.setBuildings(oldBuildings);

        user = oldUser;

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_MIESZKANIEC"));



        //user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        //user.setFlat(oldUser.getFlat());//przepsanie mieszkań
        //user.setBuildings(oldUser.getBuildings());//przepisane budynków
        //user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        //user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        //user.setConfirmPassword(oldUser.getPassword());


        spUserRepository.save(user);
    }

    public void removeUserBuilding(SpUserApp user, SpBuilding building) {
        //user.setPassword(hashPassword(user.getPassword()));

        System.out.println("Usuwanie "+user.getPesel());

        SpUserApp oldUser = getUserApp(user.getId());

        Set<SpBuilding> oldBuildings = oldUser.getBuildings();
        //SpBuilding tempBuilToDel = new SpBuilding();

        System.out.println("Przed usunieciem: "+oldBuildings);
        for (SpBuilding tempBuild : oldBuildings) {
            if(tempBuild.getId()==building.getId()){
                //tempBuilToDel = tempBuild
                boolean suc = oldBuildings.remove(tempBuild);
                System.out.println("suc: "+suc+" "+tempBuild);
                break;
            }
        }



        System.out.println("Po usunieciu: "+oldBuildings);
        //oldBuildings.addAll(user.getBuildings());

        oldUser.setBuildings(oldBuildings);

        user = oldUser;

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_MIESZKANIEC"));



        //user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        //user.setFlat(oldUser.getFlat());//przepsanie mieszkań
        //user.setBuildings(oldUser.getBuildings());//przepisane budynków
        //user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        //user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        //user.setConfirmPassword(oldUser.getPassword());


        spUserRepository.save(user);
    }


    public void addUserFlat(SpUserApp user) {//todo
        //user.setPassword(hashPassword(user.getPassword()));

        SpUserApp oldUser = getUserApp(user.getId());

        Set<SpFlat> oldFlat = oldUser.getFlat();
        oldFlat.addAll(user.getFlat());

        oldUser.setFlat(oldFlat);

        user = oldUser;

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_MIESZKANIEC"));



        //user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        //user.setFlat(oldUser.getFlat());//przepsanie mieszkań
        //user.setBuildings(oldUser.getBuildings());//przepisane budynków
        //user.setVerifyToken(oldUser.getVerifyToken());//przepisane tokena
        //user.getUserRole().addAll(oldUser.getUserRole());//przepisane starej roli
        //user.setPassword(oldUser.getPassword());//przepisywanie hasłą takie jak było wczesniej
        //user.setConfirmPassword(oldUser.getPassword());


        spUserRepository.save(user);
    }




    public void removeUserFlat(SpUserApp user, SpFlat flat) {
        //user.setPassword(hashPassword(user.getPassword()));

        System.out.println("Usuwanie "+user.getPesel());

        SpUserApp oldUser = getUserApp(user.getId());

        Set<SpFlat> oldFlats = oldUser.getFlat();
        //SpBuilding tempBuilToDel = new SpBuilding();

        System.out.println("Przed usunieciem: "+oldFlats);
        for (SpFlat tempFlat : oldFlats) {
            if(tempFlat.getId()==flat.getId()){
                //tempBuilToDel = tempBuild
                boolean suc = oldFlats.remove(tempFlat);
                System.out.println("suc: "+suc+" "+tempFlat);
                break;
            }
        }



        System.out.println("Po usunieciu: "+oldFlats);
        //oldBuildings.addAll(user.getBuildings());

        oldUser.setFlat(oldFlats);

        user = oldUser;

        //user.getUserRole().add(userRoleRepository.findByRole("ROLE_MIESZKANIEC"));



        //user.setEnabled(oldUser.isEnabled());//przepisanie aktywacji konta
        //user.setFlat(oldUser.getFlat());//przepsanie mieszkań
        //user.setBuildings(oldUser.getBuildings());//przepisane budynków
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
    public List<SpUserApp> getUserAppByBuilding(long BuildingId) {
        return spUserRepository.findByBuildingId(BuildingId);
    }

    @Transactional
    public List<SpUserApp> getUserAppByFlat(long flatId) {
        return spUserRepository.findByByFlatId(flatId);
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
