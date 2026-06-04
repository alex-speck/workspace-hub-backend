package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.application.dto.events.EnviarEmailEvent;
import com.projetofullstack.workspace_hub.application.dto.request.RegistroEmpresaRequest;
import com.projetofullstack.workspace_hub.domain.entities.Empresa;
import com.projetofullstack.workspace_hub.domain.entities.Usuario;
import com.projetofullstack.workspace_hub.domain.enums.EmailTypes;
import com.projetofullstack.workspace_hub.domain.repository.EmpresaRepository;
import com.projetofullstack.workspace_hub.domain.valueobjects.CNPJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void cadastrarEmpresa(RegistroEmpresaRequest request){

        if(empresaRepository.existsEmpresaByCnpj(new CNPJ(request.cnpj()))){
            throw new IllegalArgumentException("CNPJ já cadastrado");
        }

        Empresa empresa = new Empresa(request);

        if (!verificarCNPJ(empresa.getCnpj().getValor())){
            throw new IllegalArgumentException("Não existe empresa com esse CNPJ");
        }

        Usuario usuarioPadrao = new Usuario(request.usuarioPadrao(), empresa);
        usuarioPadrao.setRole("GESTOR");
        usuarioPadrao.setSenha(passwordEncoder.encode(usuarioPadrao.getSenha()));
        usuarioPadrao.setEmpresa(empresa);

        empresa.setUsuarios(new ArrayList<>(List.of(usuarioPadrao)));

        try {
            empresaRepository.save(empresa);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //envia email de boas vindas
        publicarEventoEmail(empresa);
    }

    private void publicarEventoEmail(Empresa empresa){
        applicationEventPublisher.publishEvent(new EnviarEmailEvent(
                empresa.getEmail(),
                "Bem Vindos ao WorkSpace Hub!",
                EmailTypes.CADASTRO,
                Map.of(
                        "nomeEmpresa", empresa.getRazaoSocial(),
                        "cnpj", empresa.getCnpj().toFormattedString(),
                        "dataCadastro", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        "linkAcesso", "http://localhost:3000/login"
                )
        ));
    }

    private boolean verificarCNPJ(String cnpj) {
        try {
            URL url = new URL("https://brasilapi.com.br/api/cnpj/v1/" + cnpj);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);


            var code = conn.getResponseCode();
            conn.disconnect();

            return code == 200;
        } catch (IOException e) {
            return false;
        }

    }


}
