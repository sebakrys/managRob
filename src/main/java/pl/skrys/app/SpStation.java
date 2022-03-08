package pl.skrys.app;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "station")
public class SpStation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;


    private String hala;
    private String linia;
    private String sterownik;

    private String nazwa;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "station")
    private List<SpRobot> robot;


    public void removeRobot(SpRobot robot) {
        this.robot.remove(robot);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<SpRobot> getRobot() {
        return robot;
    }

    public void setRobot(List<SpRobot> robot) {
        this.robot = robot;
        this.robot.forEach(sprobot -> sprobot.setStation(this));
    }

    public String getHala() {
        return hala;
    }

    public void setHala(String hala) {
        this.hala = hala;
    }

    public String getSterownik() {
        return sterownik;
    }

    public void setSterownik(String sterownik) {
        this.sterownik = sterownik;
    }

    public String getLinia() {
        return linia;
    }

    public void setLinia(String linia) {
        this.linia = linia;
    }
}
