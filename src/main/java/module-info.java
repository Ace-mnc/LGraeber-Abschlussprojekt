module de.die_gfi.projektunterrichtsprotokolle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens de.die_gfi.projektunterrichtsprotokolle to javafx.fxml;
    exports de.die_gfi.projektunterrichtsprotokolle;
}