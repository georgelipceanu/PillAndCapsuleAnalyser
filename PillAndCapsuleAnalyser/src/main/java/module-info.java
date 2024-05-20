module org.example.pillandcapsuleanalyser {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmh.core;


    opens org.example.pillandcapsuleanalyser to javafx.fxml,jmh.core,jmh.generator.annprocess;
    exports org.example.pillandcapsuleanalyser;
    exports org.example.pillandcapsuleanalyser.jmh_generated to jmh.core;
    exports org.example.pillandcapsuleanalyser.controllers;
    opens org.example.pillandcapsuleanalyser.controllers to javafx.fxml,jmh.core,jmh.generator.annprocess;
    exports org.example.pillandcapsuleanalyser.models;
    opens org.example.pillandcapsuleanalyser.models to javafx.fxml,jmh.core,jmh.generator.annprocess;
}