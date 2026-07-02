package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.application.dto.events.EnviarEmailEvent;
import com.projetofullstack.workspace_hub.application.dto.request.RegistroEmpresaDesktopRequest;
import com.projetofullstack.workspace_hub.application.dto.request.RegistroEmpresaRequest;
import com.projetofullstack.workspace_hub.application.dto.response.BuscarCnpjResponse;
import com.projetofullstack.workspace_hub.domain.entities.Empresa;
import com.projetofullstack.workspace_hub.domain.entities.Usuario;
import com.projetofullstack.workspace_hub.domain.enums.EmailTypes;
import com.projetofullstack.workspace_hub.domain.repository.EmpresaRepository;
import com.projetofullstack.workspace_hub.domain.valueobjects.CNPJ;
import com.projetofullstack.workspace_hub.infrastructure.dto.BrasilApiResponse;
import com.projetofullstack.workspace_hub.infrastructure.external.BrasilApiClient;
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

    @Autowired
    private BrasilApiClient brasilApiClient;

    @Transactional
    public void cadastrarEmpresa(RegistroEmpresaRequest request){

        if(empresaRepository.existsEmpresaByCnpj(new CNPJ(request.cnpj()))){
            throw new IllegalArgumentException("CNPJ já cadastrado");
        }

        Empresa empresa = new Empresa(request);

        // busca dados da empresa, se der 404 significa que não existe empresa com esse CNPJ
        // e lança exceção
        buscarDadosEmpresa(empresa.getCnpj().toFormattedString());

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

    public void cadastrarEmpresaDesktop(RegistroEmpresaDesktopRequest request){
        BuscarCnpjResponse dadosEmpresa = buscarDadosEmpresa(request.cnpj());

        var empresa = new Empresa(request, dadosEmpresa);
        empresa.criarUsuarioPadrao(request.email(), passwordEncoder.encode(request.senha()));
        empresaRepository.save(empresa);
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

    private BuscarCnpjResponse buscarDadosEmpresa(String cnpj){
        BrasilApiResponse response = brasilApiClient.buscarEmpresaPorCNPJ(cnpj);

        return new BuscarCnpjResponse(response.getRazao_social(), response.getNome_fantasia());
    }


}
