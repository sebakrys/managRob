package pl.skrys.app;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;


    private String country;
    private String city;
    private String standard;

    private String nazwa;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private List<SpStation> stacje;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }


    public List<SpStation> getStacje() {
        return stacje;
    }

    public void setStacje(List<SpStation> stacje) {
        this.stacje = stacje;
    }
}
