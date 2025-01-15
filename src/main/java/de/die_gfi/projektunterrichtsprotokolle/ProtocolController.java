package de.die_gfi.projektunterrichtsprotokolle;

import DB.Access.DBConnection;
import DB.Access.DBProperty;
import DB.Classes.Masznahme;
import DB.IDBConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProtocolController implements Initializable{
    @FXML
    public ChoiceBox cBoxReferent;
    @FXML
    public ChoiceBox cBoxMsn1;
    @FXML
    public Label lblSemL1;
    @FXML
    public ChoiceBox cBoxMsn2;
    @FXML
    public Label lblSemL2;
    @FXML
    public TextField tBoxUOrt;
    @FXML
    public DatePicker dPickVon;
    @FXML
    public DatePicker dPickBis;
    @FXML
    public CheckBox checkBoxFriday;
    @FXML
    public TextField tBoxDates;
    @FXML
    public TextField tBoxTarget;
    @FXML
    public Button btnSelectFolder;
    @FXML
    public CheckBox checkBoxZip;
    @FXML
    public CheckBox checkBoxOpen;
    @FXML
    public Button btnOpenFolder;
    @FXML
    public CheckBox checkBoxCopyEmail;
    @FXML
    public CheckBox checkBoxSort;
    @FXML
    public Label lblFileName;
    @FXML
    public Button btnGenerate;
    @FXML
    public Button btnFinish;

    private final IDBConnection dbcon = new DBConnection().openConnection(DBProperty.readProperties());
    private static final String DEFAULTFOLDER = "C:\\Users\\Admin\\Documents\\baseFolderProtocol";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbcon.openConnection(DBProperty.readProperties());
        setChoiceBox(cBoxMsn1, "msnName", "Masznahme", lblSemL1, "Seminarleitung 1");
        setChoiceBox(cBoxMsn2, "msnName", "Masznahme", lblSemL2, "Seminarleitung 2");
        setChoiceBox(cBoxReferent, "Nachname", "referenten", null, null);
        System.out.println(cBoxReferent.getItems());
        tBoxTarget.setPromptText(DEFAULTFOLDER);
        //fileNameChanger();
    }

    private void setChoiceBox(ChoiceBox<String> cb, String slc, String tbl, Label lbl, String lbT) {
        List<String> rsl = new ArrayList<>();
        rsl.add("Abwählen");
        ResultSet rs;
        try {
            PreparedStatement stmt = this.dbcon.getConnection().prepareStatement("SELECT " + slc + " FROM " + tbl);
            stmt.execute();
            rs = stmt.getResultSet();
            while (rs.next()) {
                rsl.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        cb.setItems(FXCollections.observableList(rsl));
        if (lbl != null) {
            lbl.setText("<" + lbT + ">");
        }
    }

    @FXML
    public void refSelected() {
        abwaehlcheck(cBoxReferent);
    }

    @FXML
    public void msn1Selected() {
        abwaehlcheck(cBoxMsn1);
        try{
            if(cBoxMsn1.getSelectionModel().getSelectedItem().toString().equals("")){
                lblSemL1.setText("<Seminarleiter 1>");
                tBoxUOrt.setText("");
                cBoxMsn2.getSelectionModel().select("");
                cBoxMsn2.setDisable(true);
                return;
            }
            System.out.println(cBoxMsn1.getSelectionModel().getSelectedItem().toString());
            PreparedStatement stmt = this.dbcon.getConnection().prepareStatement("SELECT SemL,unterrichtsort FROM Masznahme WHERE msnName = '"+cBoxMsn1.getSelectionModel().getSelectedItem().toString()+"'");
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while(rs.next()) {
                lblSemL1.setText(rs.getString("semL"));
                tBoxUOrt.setText(rs.getString("unterrichtsort"));
            }
            cBoxMsn2.setDisable(false);
        } catch (Exception e) {
        }

    }

    @FXML
    public void msn2Selected() {
        abwaehlcheck(cBoxMsn2);
        try{
            if(cBoxMsn2.getSelectionModel().getSelectedItem().toString().equals("")){
                lblSemL2.setText("<Seminarleiter 2>");
                return;
            }
            System.out.println(cBoxMsn1.getSelectionModel().getSelectedItem().toString());
            PreparedStatement stmt = this.dbcon.getConnection().prepareStatement("SELECT SemL,unterrichtsort FROM Masznahme WHERE msnName = '"+cBoxMsn2.getSelectionModel().getSelectedItem().toString()+"'");
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while(rs.next()) {
                lblSemL2.setText(rs.getString("semL"));
            }
            cBoxMsn2.setDisable(false);
        } catch (Exception e) {
        }
    }

    private void abwaehlcheck(ChoiceBox<String> cb){
        if(cb.getSelectionModel().getSelectedItem().equals("Abwählen")){
            cb.getSelectionModel().select("");
        }
        updateFilename();
    }


    @FXML
    public void dateVonPicked() {
        datechecker(dPickVon);
        if(dPickBis.getValue()!=null&&!dPickBis.getValue().toString().isEmpty()){
            tBoxDates.setText(dateFiller());
        }
        updateFilename();
    }

    @FXML
    public void dateBisPicked() {
        datechecker(dPickBis);
        if(dPickVon.getValue()!=null&&!dPickVon.getValue().toString().isEmpty()){
            tBoxDates.setText(dateFiller());
        }
        updateFilename();
    }

    private void datechecker(DatePicker dp){
        String output = dp.getValue().toString();
        System.out.println(output);
    }

    private String dateFiller(){
        String dates = "";
        int dateCurrent = (int) dPickVon.getValue().toEpochDay();
        while(dateCurrent<(int) dPickBis.getValue().toEpochDay()){
            boolean firstDay = false;
            while(LocalDate.ofEpochDay(dateCurrent).getDayOfWeek()== DayOfWeek.SATURDAY || LocalDate.ofEpochDay(dateCurrent).getDayOfWeek()==  DayOfWeek.SUNDAY || (LocalDate.ofEpochDay(dateCurrent).getDayOfWeek()== DayOfWeek.FRIDAY && !checkBoxFriday.isSelected())){
                dateCurrent++;
                if(LocalDate.ofEpochDay(dateCurrent).getDayOfYear()==1) {
                    firstDay = true;
                }
            }
            dates+= LocalDate.ofEpochDay(dateCurrent).format(DateTimeFormatter.ofPattern("dd.MM."));
            if(dateCurrent==dPickVon.getValue().toEpochDay()) {
                dates += dPickVon.getValue().getYear();
            } else if(LocalDate.ofEpochDay(dateCurrent).getYear()!= dPickVon.getValue().getYear()&&LocalDate.ofEpochDay(dateCurrent).getDayOfYear()==1||firstDay||!dates.contains(",")){
                dates+= LocalDate.ofEpochDay(dateCurrent).getYear();
            }
            dates+= ", ";
            dateCurrent++;
        }
        dates+=dPickBis.getValue().format(DateTimeFormatter.ofPattern("dd.MM."));
        System.out.println(dates);
        return dates;
    }

    @FXML
    public void onCheckboxFriday() {
        if(!dPickVon.getValue().toString().isEmpty()&&!dPickBis.getValue().toString().isEmpty()) {
            tBoxDates.setText(dateFiller());
        }
        updateFilename();
    }

    private void updateFilename(){
        String filename = "";
        String date = "";
        String format = (checkBoxSort.isSelected()) ? "yyyyMMdd" : "dd.MM.yyyy";
        if(cBoxMsn1.getSelectionModel().getSelectedItem()==null){
            filename = "MSN";
        } else {
            filename = cBoxMsn1.getSelectionModel().getSelectedItem().toString();
        }
        if(tBoxDates.getText()==null||tBoxDates.getText().isEmpty()) {
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } else {
            date = LocalDate.parse(tBoxDates.getText().split(",")[0].trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy")).format(DateTimeFormatter.ofPattern(format));
        }
        filename+="_"+date+"_Protokoll.docx";
        lblFileName.setText(filename);
    }

    @FXML
    public void onTargetUpdated() {
    }

    @FXML
    public void onFolderSelect() {
    }

    @FXML
    public void onZipChecked() {
    }

    @FXML
    public void onOpenChecked() {
    }

    @FXML
    public void onOpenClicked() {
    }

    @FXML
    public void onSortChecked() {
        updateFilename();
    }

    @FXML
    public void onCopyChecked() {
    }

    @FXML
    public void onGenerate() {
    }

    @FXML
    public void onFinish() {
        Stage stage = (Stage) btnFinish.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void editRef() {
        System.out.println("RefEdit");
    }

    @FXML
    public void editMsn() {
        System.out.println("MsnEdit");
    }
}