package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.skrys.app.SpUserApp;
import pl.skrys.app.UserRole;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("myUserAppDetailsService")
public class MyUserAppDetailsService implements UserDetailsService {

    private SpUserService userAppService;

    @Autowired
    public MyUserAppDetailsService(SpUserService userAppService){
        this.userAppService = userAppService;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        List<SpUserApp> tmpList = userAppService.findByEmail(email); // logowanie przez email
        SpUserApp userApp = tmpList.get(0);
//        SpUserApp userApp = userAppService.findByPesel(pesel); // logowanie przez pesel
        List<GrantedAuthority> authorities = buildUserAuthority(userApp.getUserRole());
        return buildUserForAuthentication(userApp, authorities);
    }

    private User buildUserForAuthentication(SpUserApp userApp, List<GrantedAuthority> authorities){
        return new User(userApp.getPesel(), userApp.getPassword(), userApp.isEnabled(),
                true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority (Set<UserRole> userAppRoles){
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        for (UserRole userAppRole : userAppRoles){
            setAuths.add(new SimpleGrantedAuthority(userAppRole.getRole()));
        }
        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);
        return result;
    }

}
