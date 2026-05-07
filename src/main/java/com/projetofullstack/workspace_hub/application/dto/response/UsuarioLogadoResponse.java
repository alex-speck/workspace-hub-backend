package com.projetofullstack.workspace_hub.application.dto.response;

import com.projetofullstack.workspace_hub.domain.entities.Empresa;
import com.projetofullstack.workspace_hub.domain.entities.Usuario;

public record UsuarioLogadoResponse(
        String nome,
        String email,
        String role,
        EmpresaResponse empresa
) {
    public UsuarioLogadoResponse(Usuario usuario, Empresa empresa) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getRole(), new EmpresaResponse(empresa));
    }
}
