module com.example.COMP2042_CW_hcycl3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens FX to javafx.fxml;
    exports FX;
    exports FX.Controller;
    opens FX.Controller to javafx.fxml;
}