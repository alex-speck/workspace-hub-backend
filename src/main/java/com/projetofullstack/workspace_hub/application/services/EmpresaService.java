package com.projetofullstack.workspace_hub.application.services;

import com.projetofullstack.workspace_hub.application.dto.request.RegistroEmpresaRequest;
import com.projetofullstack.workspace_hub.domain.entities.Empresa;
import com.projetofullstack.workspace_hub.domain.entities.Usuario;
import com.projetofullstack.workspace_hub.domain.repository.EmpresaRepository;
import com.projetofullstack.workspace_hub.domain.valueobjects.CNPJ;
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

        if(empresaRepository.existsEmpresaByCnpj(new CNPJ(request.cnpj()))){
            throw new IllegalArgumentException("CNPJ já cadastrado");
        }

        Empresa empresa = new Empresa(request);

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
    }


}
