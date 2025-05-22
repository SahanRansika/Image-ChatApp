module org.example.demochat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;


    opens org.example.demochat to javafx.fxml;
    exports org.example.demochat;
    exports org.example.demochat.controller;
    opens org.example.demochat.controller to javafx.fxml;
}