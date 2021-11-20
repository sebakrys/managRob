package pl.skrys.service;

import pl.skrys.app.UserRole;

import java.util.List;

public interface UserRoleService {

    void addUserRole(UserRole userRole);
    List<UserRole> listUserRole();
    UserRole getUserRole(long id);
    UserRole getUserRoleByName(String role);
}
