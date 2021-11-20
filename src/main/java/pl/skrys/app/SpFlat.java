package pl.skrys.app;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "flat")
public class SpFlat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;


    @ManyToOne
    @JoinColumn(name="building_id", nullable=false)
    private SpBuilding building;


    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SpFlatCharges> flatCharges;

    private String flatNumber;
    //private String city;
    //private String street;
    //private String postalCode;


    public List<SpFlatCharges> getFlatCharges() {
        return flatCharges;
    }

    public void addFlatCharges(SpFlatCharges flatCharge) {
        flatCharges.add(flatCharge);
        flatCharge.setFlat(this);
    }

    public void removeFlatCharges(SpFlatCharges flatCharge) {
        flatCharges.remove(flatCharge);
        flatCharge.setFlat(null);
    }


    public void setFlatCharges(List<SpFlatCharges> flatCharges) {
        this.flatCharges = flatCharges;
        this.flatCharges.forEach(spFlatCharges -> spFlatCharges.setFlat(this));
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SpBuilding getBuilding() {
        return building;
    }

    public void setBuilding(SpBuilding building) {
        this.building = building;
    }



    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
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
