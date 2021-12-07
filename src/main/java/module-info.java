module com.example.brickdestroyerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens FX to javafx.fxml;
    exports FX;
    exports FX.Controller;
    opens FX.Controller to javafx.fxml;
}