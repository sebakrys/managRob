package pl.skrys.app;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "robot_charges")
public class SpRobotCharges {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name="robot_id", nullable=false)
    private SpRobot robot;

    private double tz;
    private double vel_pot;
    private double vel_prod;

    private boolean pot;
    private boolean prod;

    private String comment;

    private Date data;

    @OneToOne
    private SpUserApp robotyk;

    private boolean accepted;

    public SpRobot getRobot() {
        return robot;
    }

    public void setRobot(SpRobot robot) {
        this.robot = robot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTz() {
        return tz;
    }

    public void setTz(double tz) {
        this.tz = tz;
    }

    public double getVel_pot() {
        return vel_pot;
    }

    public void setVel_pot(double vel_pot) {
        this.vel_pot = vel_pot;
    }

    public double getVel_prod() {
        return vel_prod;
    }

    public void setVel_prod(double vel_prod) {
        this.vel_prod = vel_prod;
    }

    public boolean isPot() {
        return pot;
    }

    public void setPot(boolean pot) {
        this.pot = pot;
    }

    public boolean isProd() {
        return prod;
    }

    public void setProd(boolean prod) {
        this.prod = prod;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public SpUserApp getRobotyk() {
        return robotyk;
    }

    public void setRobotyk(SpUserApp robotyk) {
        this.robotyk = robotyk;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
