package pl.skrys.app;

public class SearchedObject {
    String nazwaProjektu;
    String nazwaStacji;
    String nazwaRobota;
    long numerIdRobota;

    public SearchedObject(String nazwaProjektu, String nazwaStacji, String nazwaRobota, long numerIdRobota) {
        this.nazwaProjektu = nazwaProjektu;
        this.nazwaStacji = nazwaStacji;
        this.nazwaRobota = nazwaRobota;
        this.numerIdRobota = numerIdRobota;
    }

    public String getNazwaProjektu() {
        return nazwaProjektu;
    }

    public void setNazwaProjektu(String nazwaProjektu) {
        this.nazwaProjektu = nazwaProjektu;
    }

    public String getNazwaStacji() {
        return nazwaStacji;
    }

    public void setNazwaStacji(String nazwaStacji) {
        this.nazwaStacji = nazwaStacji;
    }

    public String getNazwaRobota() {
        return nazwaRobota;
    }

    public void setNazwaRobota(String nazwaRobota) {
        this.nazwaRobota = nazwaRobota;
    }

    public long getNumerIdRobota() {
        return numerIdRobota;
    }

    public void setNumerIdRobota(int numerIdRobota) {
        this.numerIdRobota = numerIdRobota;
    }
}
