module com.example.brickdestroyerfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens FX to javafx.fxml;
    exports FX;
}