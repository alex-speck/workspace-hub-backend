package com.projetofullstack.workspacehub.desktop.controllers;

import com.projetofullstack.workspacehub.desktop.WorkspaceApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
    private TextField txtCep;
    @FXML
    private TextArea txtEndereco;

    @FXML
    private void onCriarButtonClicked(ActionEvent event) throws IOException {
        if (!txtSenha.getText().equals(txtConfirmarSenha.getText())){
            showMessage("Erro", "As senhas não conhecidem", Alert.AlertType.ERROR);
        } else if (txtEndereco.getText().isEmpty()){
            showMessage("Erro", "Deve conter um endereço!", Alert.AlertType.ERROR);
        } else {
            URL url = new URL("http://localhost:8080/usuarios/admin");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");

            conn.setDoOutput(true);

            String json = "{\n" +
                    "  \"nome\": \""+ txtNome.getText() +"\",\n" +
                    "  \"email\": \""+ txtEmail.getText() +"\",\n" +
                    "  \"senha\": \""+ txtSenha.getText() +"\"\n" +
                    "  \"secretKey\": \"oyuq87y2e987y2e49yahwduhi721yeolid\" \n" +
                    "  \"endereco\": " + txtEndereco.getText() + " \n" +
                    "}";

            try(OutputStream os = conn.getOutputStream()){
                os.write(json.getBytes());
            }

            var code = conn.getResponseCode();
            var body = conn.getResponseMessage();

            System.out.println(body);

            if(code == 201) {
                showMessage("Sucesso!", "Sucesso ao salvar usuario", Alert.AlertType.INFORMATION);

                FXMLLoader loader = new FXMLLoader(WorkspaceApplication.class.getResource("menu-view.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } else {
                showMessage("Erro", "Erro ao salvar!", Alert.AlertType.ERROR);
            }

            conn.disconnect();
        }

    }

    @FXML
    private void onVoltarButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(WorkspaceApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void onBuscarEnderecoButtonClick() throws Exception {
        URL url = new URL("https://viacep.com.br/ws/" + txtCep.getText() + "/json/");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);

        var code = conn.getResponseCode();

        if (code >= 200 && code < 300) {
            StringBuilder response = new StringBuilder();

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
            }

            txtEndereco.setText(response.toString());
            System.out.println(response);
        }

        conn.disconnect();

    }

    private void showMessage(String titulo, String message, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


}
