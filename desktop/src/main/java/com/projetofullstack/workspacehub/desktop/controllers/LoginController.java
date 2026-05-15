package com.projetofullstack.workspacehub.desktop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private void onLoginButtonClick() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Login");
        alert.setHeaderText("");
        alert.setContentText("Logado com o usuario: " + txtUsuario.getText());

        alert.showAndWait();

    }
}
