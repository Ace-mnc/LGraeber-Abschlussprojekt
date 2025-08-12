package de.die_gfi.projektunterrichtsprotokolle;

import DB.Access.DBConnection;
import DB.Access.DBProperty;
import DB.Access.IDBConnection;
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
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ProtocolRefEdit implements Initializable {
    private final IDBConnection dbcon =
            new DBConnection().openConnection(DBProperty.readProperties());

    public ListView listViewRef;
    public Button btnNew;
    public Button btnDel;
    public Button btnClose;
    public TextField tBoxNName;
    public TextField tBoxVName;
    public TextField tBoxEMail;
    public Button btnApply;

    ProtocolController pc;

    HashMap<String,Referent> mapRef = new HashMap<>();
    int maxId = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbcon.openConnection(DBProperty.readProperties());
        importAllData();

        fillRefList();
    }

    private void importAllData() {
        ResultSet rs;
        try {
            PreparedStatement stmt = this.dbcon.getConnection().prepareStatement("Select id, Nachname, Vorname, Email FROM Referenten");
            stmt.execute();
            rs = stmt.getResultSet();
            while (rs.next()) {

                Referent ref =new Referent(
                        rs.getInt("id"),
                        rs.getString("nachname"),
                        rs.getString("Vorname"),
                        rs.getString("Email")
                );

                mapRef.put(ref.getNachname(), ref);
                maxId++;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void fillRefList(){
        ObservableList<String> refs = FXCollections.observableArrayList();
        for (var v : mapRef.entrySet()) {
            Referent r = v.getValue();
            refs.add(r.getNachname());
        }

        listViewRef.setItems(refs);
    }

    public void onListRefClicked() {

        System.out.println(listViewRef.getSelectionModel().getSelectedItem().toString());
        tBoxNName.setText(mapRef.get(listViewRef.getSelectionModel().getSelectedItem().toString()).getNachname());
        tBoxNName.setDisable(false);
        tBoxVName.setText(mapRef.get(listViewRef.getSelectionModel().getSelectedItem().toString()).getVorname());
        tBoxVName.setDisable(false);
        tBoxEMail.setText(mapRef.get(listViewRef.getSelectionModel().getSelectedItem().toString()).getEMail());
        tBoxEMail.setDisable(false);
        btnDel.setDisable(false);
    }

    public void onBtnNewPressed() {
        try {
            String sql = "INSERT INTO Referenten (Nachname, Vorname, eMail) VALUES ('";
            int effId = maxId + 1;

            Referent newRef = new Referent(effId, "Neu-" + effId, "", "");
            sql += newRef.getNachname() + "', '" + newRef.getVorname() + "', '" + newRef.getEMail() + "');";
            runSql(sql);

            String getId = "Select Id From Referenten WHERE Nachname = '" + newRef.getNachname() + "';";
            ResultSet rs;
            rs = runSql(getId);
            while(rs.next()) {
                newRef.setId(rs.getInt("id"));
                System.out.println(rs.getInt("id"));
            }
            mapRef.put("Neu-" + effId, newRef);
            fillRefList();
            listViewRef.getSelectionModel().select("Neu-" + effId);
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
        String sql = "DELETE FROM Referenten WHERE id ='"+mapRef.get(listViewRef.getSelectionModel().getSelectedItem().toString()).getId()+"';";
        //delete entry
        tBoxNName.setDisable(true);
        tBoxNName.setText("");
        tBoxVName.setDisable(true);
        tBoxVName.setText("");
        tBoxEMail.setDisable(true);
        tBoxEMail.setText("");
        btnDel.setDisable(true);
        mapRef.remove(listViewRef.getSelectionModel().getSelectedItem().toString());
        runSql(sql);
        fillRefList();
    }

    public void onBtnClosePressed() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        pc.setChoiceBox(pc.cBoxReferent, "Nachname", "referenten", null, null);
        stage.close();
    }

    public void onBtnApplyPressed() {
        String sql = "UPDATE Referenten SET ";
        String nachname = tBoxNName.getText();
        String vorname = tBoxVName.getText();
        String email =tBoxEMail.getText();
        Referent ref = mapRef.get(listViewRef.getSelectionModel().getSelectedItem().toString());
        sql+="Nachname = '"+nachname+"', ";
        ref.setNachname(nachname);
        sql+="Vorname = '"+vorname+"',";
        ref.setVorname(vorname);
        sql+="EMail = '"+email+"'";
        ref.setEMail(email);
        sql+="WHERE id ='"+ref.getId()+"';";
        runSql(sql);
        mapRef.remove(listViewRef.getSelectionModel().getSelectedItem().toString());
        mapRef.put(ref.getNachname(),ref);
        fillRefList();
        listViewRef.getSelectionModel().select(ref.getNachname());
        System.out.println(sql);
        btnApply.setDisable(true);
    }

    public void onRefEdited() {
        btnApply.setDisable(false);
    }
}
