package com.projetofullstack.workspace_hub.model.dto.response;

import com.projetofullstack.workspace_hub.model.entities.Empresa;
import com.projetofullstack.workspace_hub.model.entities.Usuario;

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
