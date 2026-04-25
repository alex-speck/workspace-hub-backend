package com.projetofullstack.workspace_hub.model.dto.response;

import com.projetofullstack.workspace_hub.model.entities.Usuario;
import com.projetofullstack.workspace_hub.model.enums.StatusUsuario;

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
