module com.projetofullstack.workspacehub.desktop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.projetofullstack.workspacehub.desktop to javafx.fxml;
    exports com.projetofullstack.workspacehub.desktop;
    exports com.projetofullstack.workspacehub.desktop.controllers;
    opens com.projetofullstack.workspacehub.desktop.controllers to javafx.fxml;
}