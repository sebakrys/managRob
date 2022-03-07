package pl.skrys.app;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "station")
public class SpStation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;




    private String nazwa;

    private String city;
    private String street;
    private String stationNumber;
    private String postalCode;


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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(String stationNumber) {
        this.stationNumber = stationNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
