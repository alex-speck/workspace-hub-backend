package com.projetofullstack.workspace_hub.services;

import com.projetofullstack.workspace_hub.model.dto.request.RegistroEmpresaRequest;
import com.projetofullstack.workspace_hub.model.entities.Empresa;
import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void cadastrarEmpresa(RegistroEmpresaRequest request){

        if(empresaRepository.existsEmpresaByCnpj(request.cnpj())){
            throw new IllegalArgumentException("CNPJ já cadastrado");
        }

        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(request.razaoSocial());
        empresa.setNomeFantasia(request.nomeFantasia());
        empresa.setCnpj(request.cnpj());
        empresa.setEmail(request.email());
        empresa.setTelefone(request.telefone());

        Usuario usuarioPadrao = request.usuarioPadrao().toUsuario();
        usuarioPadrao.setRole("GESTOR");
        usuarioPadrao.setSenha(passwordEncoder.encode(usuarioPadrao.getSenha()));
        usuarioPadrao.setEmpresa(empresa);

        empresa.setUsuarios(new ArrayList<>(List.of(usuarioPadrao)));

        try {
            empresaRepository.save(empresa);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
