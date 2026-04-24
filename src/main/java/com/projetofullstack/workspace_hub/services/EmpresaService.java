package com.projetofullstack.workspace_hub.services;

import com.projetofullstack.workspace_hub.model.dto.request.RegistroEmpresaRequest;
import com.projetofullstack.workspace_hub.model.entities.Empresa;
import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void cadastrarEmpresa(RegistroEmpresaRequest request){
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(request.razaoSocial());
        empresa.setNomeFantasia(request.nomeFantasia());
        empresa.setCnpj(request.cnpj());
        empresa.setEmail(request.email());
        empresa.setTelefone(request.telefone());

        Usuario usuarioPadrao = new Usuario();
        usuarioPadrao.se
    }

}
