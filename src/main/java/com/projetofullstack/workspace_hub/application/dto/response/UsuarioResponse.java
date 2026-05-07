package com.projetofullstack.workspace_hub.application.dto.response;

import com.projetofullstack.workspace_hub.domain.entities.Usuario;
import com.projetofullstack.workspace_hub.domain.enums.StatusUsuario;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        StatusUsuario status
) {
    public UsuarioResponse(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getStatus());
    }
}
