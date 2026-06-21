module com.tuktuk.tuktukspareparts {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tuktuk.tuktukspareparts to javafx.fxml;
    exports com.tuktuk.tuktukspareparts;
}