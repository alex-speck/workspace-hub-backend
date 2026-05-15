package com.projetofullstack.workspacehub.desktop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class UsuarioController {

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtSenha;
    @FXML
    private TextField txtConfirmarSenha;

    @FXML
    private void onCriarButtonClicked() {
        if (!txtSenha.getText().equals(txtConfirmarSenha.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("");
            alert.setContentText("As senhas não conhecidem!");
        }
    }


}
