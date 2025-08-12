package de.die_gfi.projektunterrichtsprotokolle;

import DB.Access.DBConnection;
import DB.Access.DBProperty;
import DB.Access.IDBConnection;
import DB.Classes.Masznahme;
import DB.Classes.Referent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ProtocolMsnEdit implements Initializable {

    private final IDBConnection dbcon = new DBConnection().openConnection(DBProperty.readProperties());

    public ListView listViewMsn;
    public Button btnNew;
    public Button btnDel;
    public Button btnClose;
    public TextField tBoxMsnName;
    public TextField tBoxADatum;
    public TextField tBoxEDatum;
    public Button btnApply;
    public TextField tBoxSemL;
    public TextField tBoxANr;
    public TextField tBoxUOrt;

    HashMap<String, Masznahme> mapMsn = new HashMap<>();
    int maxId = 0;

    ProtocolController pc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbcon.openConnection(DBProperty.readProperties());
        importAllData();

        fillMsnList();
    }

    private void importAllData() {
        ResultSet rs;
        try {
            PreparedStatement stmt = this.dbcon.getConnection().prepareStatement("Select msnid, Msnname, SemL, Anr, Unterrichtsort, ADatum, EDatum FROM Masznahmen");
            stmt.execute();
            rs = stmt.getResultSet();
            while (rs.next()) {

                Masznahme msn =new Masznahme(
                        rs.getInt("Msnid"),
                        rs.getString("MsnName"),
                        rs.getString("SemL"),
                        rs.getInt("ANr"),
                        rs.getString("Unterrichtsort"),
                        rs.getString("ADatum"),
                        rs.getString("EDatum")
                );

                mapMsn.put(msn.getName(), msn);
                maxId++;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void fillMsnList(){
        ObservableList<String> msns = FXCollections.observableArrayList();
        for (var v : mapMsn.entrySet()) {
            Masznahme m = v.getValue();
            msns.add(m.getName());
        }

        listViewMsn.setItems(msns);
    }

    public void onListRefClicked() {
        String chosenCustomer = listViewMsn.getSelectionModel().getSelectedItem().toString();
        System.out.println(listViewMsn.getSelectionModel().getSelectedItem().toString());
        tBoxMsnName.setText(mapMsn.get(chosenCustomer).getName());
        tBoxMsnName.setDisable(false);
        tBoxADatum.setText(mapMsn.get(chosenCustomer).getAnfangsDatum());
        tBoxADatum.setDisable(false);
        tBoxEDatum.setText(mapMsn.get(chosenCustomer).getEndDatum());
        tBoxEDatum.setDisable(false);
        tBoxSemL.setText(mapMsn.get(chosenCustomer).getSeminarleitung());
        tBoxSemL.setDisable(false);
        tBoxANr.setText(""+mapMsn.get(chosenCustomer).getAuftragsnr());
        tBoxANr.setDisable(false);
        tBoxUOrt.setText(mapMsn.get(chosenCustomer).getUnterrichtsort());
        tBoxUOrt.setDisable(false);
        btnDel.setDisable(false);
    }

    public void onBtnNewPressed() {
        try {

            int effId = maxId + 1;

            Masznahme newMsn = new Masznahme(effId, "Neu-" + effId, "", 0,"",LocalDate.now().toString(),LocalDate.now().toString());
            String sql = "INSERT INTO Masznahmen (Msnname, SemL, Anr, UnterrichtsOrt, ADatum, EDatum) VALUES ('"+
                    newMsn.getName() + "', '" + newMsn.getSeminarleitung() + "', '" + newMsn.getAuftragsnr() + "', '" +
                    newMsn.getUnterrichtsort() + "', '" + newMsn.getAnfangsDatum() + "', '" + newMsn.getEndDatum() + "');";
            runSql(sql);

            ResultSet rs = runSql("Select msnId From Masznahmen WHERE msnName = '" + newMsn.getName() + "';");
            while(rs.next()) {
                newMsn.setId(rs.getInt("msnid"));
                System.out.println(rs.getInt("msnid"));
            }
            mapMsn.put("Neu-" + effId, newMsn);
            fillMsnList();
            listViewMsn.getSelectionModel().select("Neu-" + effId);
            maxId++;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet runSql(String sql){
        System.out.println(sql);
        try {
            PreparedStatement stmt = this.dbcon.getConnection().prepareStatement(sql);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onBtnDelPressed() {
        String sql = "DELETE FROM Masznahmen WHERE msnID ='"+ mapMsn.get(listViewMsn.getSelectionModel().getSelectedItem().toString()).getId()+"';";
        //delete entry
        tBoxMsnName.setDisable(true);
        tBoxMsnName.setText("");
        tBoxADatum.setDisable(true);
        tBoxADatum.setText("");
        tBoxEDatum.setDisable(true);
        tBoxEDatum.setText("");
        btnDel.setDisable(true);
        tBoxSemL.setText("");
        tBoxSemL.setDisable(true);
        tBoxANr.setText("");
        tBoxANr.setDisable(true);
        tBoxUOrt.setText("");
        tBoxUOrt.setDisable(true);
        mapMsn.remove(listViewMsn.getSelectionModel().getSelectedItem().toString());
        runSql(sql);
        fillMsnList();
    }

    public void onBtnClosePressed() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        pc.setChoiceBox(pc.cBoxMsn1, "msnName", "Masznahmen", pc.lblSemL1, "Seminarleitung 1");
        pc.setChoiceBox(pc.cBoxMsn2, "msnName", "Masznahmen", pc.lblSemL2, "Seminarleitung 2");
        stage.close();
    }

    public void onBtnApplyPressed() {
        String sql = "UPDATE Masznahmen SET ";
        String msnName = tBoxMsnName.getText();
        String aDatum = tBoxADatum.getText();
        String eDatum = tBoxEDatum.getText();
        String semL = tBoxSemL.getText();
        String aNR = tBoxANr.getText();
        String uOrt = tBoxUOrt.getText();
        Masznahme msn = mapMsn.get(listViewMsn.getSelectionModel().getSelectedItem().toString());
        sql+="msnName = '"+msnName+"', ADatum = '"+aDatum+"', EDatum = '"+eDatum+"', semL = '"+semL+"', aNR = '"+aNR+"', unterrichtsort = '"+uOrt+"' ";
        msn.setName(msnName);
        msn.setAnfangsDatum(aDatum);
        msn.setEndDatum(eDatum);
        msn.setSeminarleitung(semL);
        msn.setAuftragsnr(Integer.parseInt(aNR));
        msn.setUnterrichtsort(uOrt);

        sql+="WHERE msnid ='"+msn.getId()+"';";
        runSql(sql);
        mapMsn.remove(listViewMsn.getSelectionModel().getSelectedItem().toString());
        mapMsn.put(msn.getName(),msn);
        fillMsnList();
        listViewMsn.getSelectionModel().select(msn.getName());
        System.out.println(sql);
        btnApply.setDisable(true);
    }

    public void onRefEdited() {
        btnApply.setDisable(false);
    }
}
