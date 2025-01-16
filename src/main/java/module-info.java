module de.die_gfi.projektunterrichtsprotokolle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.apache.poi.ooxml;
    requires org.checkerframework.checker.qual;


    opens de.die_gfi.projektunterrichtsprotokolle to javafx.fxml;
    exports de.die_gfi.projektunterrichtsprotokolle;
    exports DB.Access;
    exports DB.Classes;
    exports Document;
}