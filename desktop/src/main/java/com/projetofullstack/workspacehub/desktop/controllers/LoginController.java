package com.projetofullstack.workspacehub.desktop.controllers;

import com.projetofullstack.workspacehub.desktop.WorkspaceApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private void onLoginButtonClick(ActionEvent event) throws IOException {

        if (txtUsuario.getText().equals("admin") && txtSenha.getText().equals("satorugojo")) {
            showMessage("Login", "Logado com sucesso", Alert.AlertType.INFORMATION);

            FXMLLoader loader = new FXMLLoader(WorkspaceApplication.class.getResource("menu-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } else {
            showMessage("Erro", "Usuario ou senha incorretos!", Alert.AlertType.ERROR);
        }


    }


    private void showMessage(String titulo, String message, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
