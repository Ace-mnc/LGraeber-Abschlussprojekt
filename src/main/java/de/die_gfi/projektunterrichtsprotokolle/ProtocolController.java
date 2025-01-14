package de.die_gfi.projektunterrichtsprotokolle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProtocolController  {
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


//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        dbcon.openConnection(Property.readProperties());
//        setChoiceBox(choiceSem1, "Name", "Masznahmen", sem1, "Seminarleitung 1");
//        setChoiceBox(choiceSem2, "Name", "Masznahmen", sem2, "Seminarleitung 2");
//        setChoiceBox(choiceReferent, "Nachname", "referenten", null, null);
//        System.out.println(choiceReferent.getItems());
//        textFieldZielordner.setPromptText(DEFAULTFOLDER);
//        fileNameChanger();
//    }
//
//    private void setChoiceBox(ChoiceBox<String> cb, String slc, String tbl, Label lbl, String lbT) {
//        List<String> rsl = new ArrayList<>();
//        rsl.add("Abwählen");
//        ResultSet rs;
//        try {
//            PreparedStatement stmt = this.dbcon.getConnection().prepareStatement("SELECT " + slc + " FROM " + tbl);
//            stmt.execute();
//            rs = stmt.getResultSet();
//            while (rs.next()) {
//                rsl.add(rs.getString(1));
//            }
//
//        } catch (SQLException e) {
//            System.out.println(e);
//        }

//        cb.setItems(FXCollections.observableList(rsl));
//        if (lbl != null) {
//            lbl.setText("<" + lbT + ">");
//        }
//    }

    public void refSelected() {
    }

    public void msn1Selected() {
    }

    public void msn2Selected() {
    }

    public void uOrtTyped() {
    }

    public void dateVonPicked() {
    }

    public void dateBisPicked() {

    }

    public void onCheckboxFriday() {
    }

    public void onDateTyped() {
    }

    public void onTargetUpdated() {
    }

    public void onFolderSelect() {
    }

    public void onZipChecked() {
    }

    public void onOpenChecked() {
    }

    public void onOpenClicked() {
    }

    public void onSortChecked() {
    }

    public void onCopyChecked() {
    }

    public void onGenerate() {
    }

    public void onFinish() {
        Stage stage = (Stage) btnFinish.getScene().getWindow();
        stage.close();
    }

    public void editRef() {
        System.out.println("RefEdit");
    }

    public void editMsn() {
        System.out.println("MsnEdit");
    }
}