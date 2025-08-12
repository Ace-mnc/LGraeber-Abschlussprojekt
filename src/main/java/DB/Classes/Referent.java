package DB.Classes;

public class Referent {
    private int id;
    private String nachname;
    private String vorname;
    private String eMail;

    public Referent(int id, String nachname, String vorname, String eMail){
        this.id = id;
        this.nachname = nachname;
        this.vorname = vorname;
        this.eMail = eMail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }
}
