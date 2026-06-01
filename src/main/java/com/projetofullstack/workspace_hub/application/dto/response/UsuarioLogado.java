package com.projetofullstack.workspace_hub.application.dto.response;

import com.projetofullstack.workspace_hub.domain.entities.Usuario;

public record UsuarioLogado(
        Long id,
        String nome,
        String role,
        String email,
        Long empresaId
) {
    public UsuarioLogado(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getRole(), usuario.getEmail(), usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null);
    }
}
