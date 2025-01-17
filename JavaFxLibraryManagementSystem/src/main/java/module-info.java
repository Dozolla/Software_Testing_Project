module com.example.javafxlibrarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.javafxlibrarymanagementsystem to javafx.fxml;
    opens com.example.javafxlibrarymanagementsystem.model to javafx.base;
    exports com.example.javafxlibrarymanagementsystem;

}