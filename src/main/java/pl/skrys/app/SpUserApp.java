package pl.skrys.app;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="sp_appuser")
public class SpUserApp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    private int version;

    @NotNull
    @Column(name = "firstName", nullable = false)
    @Size(min=2, max=30, message = "{error.size.2_30}")
    private String firstName;


    @NotNull
    @Size(min=2, max=30, message = "{error.size.2_30}")
    private String lastName;

    //todo @Column(unique = true)
    @NotNull
    private String email;

    //todo @Column(unique = true)
    @Size(min = 9, max = 9, message = "{error.size.tel}")
    private String telephone;




    @NotNull
    @Column(unique = true)
    @Size(min = 11, max = 11, message = "{error.size.pesel}")
    private String pesel;

    @NotNull
    //@Size(min = 6, message = "{error.size.pass}")//validacja wystepuje w innym miejscu
    private String password;

    @Transient
    private String confirmPassword;



    private boolean enabled;

    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Pesel pesel;
*/

    @OneToOne(cascade = CascadeType.ALL)
    private SpVerifyToken verifyToken;

    public SpVerifyToken getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(SpVerifyToken verifyToken) {
        this.verifyToken = verifyToken;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> userRole = new HashSet<UserRole>(0);

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<SpBuilding> buildings = new HashSet<SpBuilding>(0);

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<SpFlat> flat = new HashSet<SpFlat>(0);


/*
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        //checkPassword();
    }*/

/*
    private void checkPassword() {
        if(this.password == null || this.confirmPassword == null){
            return;
        }else if(!this.password.equals(confirmPassword)){
            this.confirmPassword = null;
        }
    }*/


    public Set<SpFlat> getFlat() {
        return flat;
    }

    public void setFlat(Set<SpFlat> flat) {
        this.flat = flat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        //checkPassword();
    }


    @Transient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Transient
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



    public Set<UserRole> getUserRole() {
        return userRole;
    }

    public void setUserRole(Set<UserRole> userRole) {
        this.userRole = userRole;
    }

    public Set<SpBuilding> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<SpBuilding> buildings) {
        this.buildings = buildings;
    }




}
