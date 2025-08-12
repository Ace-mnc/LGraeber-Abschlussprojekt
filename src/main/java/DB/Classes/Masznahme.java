package DB.Classes;

public class Masznahme {
    private int id;
    private String name;
    private String seminarleitung;
    private int auftragsnr;
    private String unterrichtsort;
    private String anfangsDatum;
    private String endDatum;


    public String getEndDatum() {
        return endDatum;
    }

    public void setEndDatum(String endDatum) {
        this.endDatum = endDatum;
    }

    public Masznahme(int id,String name,String seminarleitung,int auftragsnr,String unterrichtsort,String anfangsDatum, String endDatum){
        this.id = id;
        this.name = name;
        this.seminarleitung = seminarleitung;
        this.auftragsnr = auftragsnr;
        this.unterrichtsort = unterrichtsort;
        this.anfangsDatum = anfangsDatum;
        this.endDatum = endDatum;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeminarleitung() {
        return seminarleitung;
    }

    public void setSeminarleitung(String seminarleitung) {
        this.seminarleitung = seminarleitung;
    }

    public int getAuftragsnr() {
        return auftragsnr;
    }

    public void setAuftragsnr(int auftragsnr) {
        this.auftragsnr = auftragsnr;
    }

    public String getUnterrichtsort() {
        return unterrichtsort;
    }

    public void setUnterrichtsort(String unterrichtsort) {
        this.unterrichtsort = unterrichtsort;
    }

    public String getAnfangsDatum(){
        return anfangsDatum;
    }

    public void setAnfangsDatum(String anfangsDatum){
        this.anfangsDatum = anfangsDatum;
    }
}
