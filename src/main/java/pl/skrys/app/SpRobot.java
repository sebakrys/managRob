package pl.skrys.app;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "robot")
public class SpRobot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;


    @ManyToOne
    @JoinColumn(name="station_id", nullable=false)
    private SpStation station;


    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SpRobotCharges> robotCharges;

    private String robotNumber;
    //private String city;
    //private String street;
    //private String postalCode;


    public List<SpRobotCharges> getRobotCharges() {
        return robotCharges;
    }

    public void addRobotCharges(SpRobotCharges robotCharge) {
        robotCharges.add(robotCharge);
        robotCharge.setRobot(this);
    }

    public void removeRobotCharges(SpRobotCharges robotCharge) {
        robotCharges.remove(robotCharge);
        robotCharge.setRobot(null);
    }


    public void setRobotCharges(List<SpRobotCharges> robotCharges) {
        this.robotCharges = robotCharges;
        this.robotCharges.forEach(spRobotCharges -> spRobotCharges.setRobot(this));
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SpStation getStation() {
        return station;
    }

    public void setStation(SpStation station) {
        this.station = station;
    }



    public String getRobotNumber() {
        return robotNumber;
    }

    public void setRobotNumber(String robotNumber) {
        this.robotNumber = robotNumber;
    }

    /*
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


    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }*/
}
