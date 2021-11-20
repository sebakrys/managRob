package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.skrys.app.UserRole;
import pl.skrys.dao.UserRoleRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService{


    private UserRoleRepository userRoleRepository;


    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository){
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public void addUserRole(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    @Transactional
    public List<UserRole> listUserRole() {
        return userRoleRepository.findAll();
    }

    @Transactional
    public UserRole getUserRole(long id) {
        return userRoleRepository.getOne(id);
    }

    @Transactional
    public UserRole getUserRoleByName(String role){
        return userRoleRepository.findByRole(role);
    }


}
