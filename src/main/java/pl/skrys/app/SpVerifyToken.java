package pl.skrys.app;

import javax.persistence.*;

@Entity
@Table(name = "sp_vertoken")
public class SpVerifyToken {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    @OneToOne(mappedBy = "verifyToken")
    private SpUserApp userApp;

    String verifyToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SpUserApp getUserApp() {
        return userApp;
    }

    public void setUserApp(SpUserApp userApp) {
        this.userApp = userApp;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }
}
