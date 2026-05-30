package com.projetofullstack.workspace_hub.infrastructure.providers;

import com.projetofullstack.workspace_hub.application.dto.response.UsuarioLogado;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioLogadoProvider {

    public static UsuarioLogado getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated()) {
            return (UsuarioLogado) auth.getPrincipal();
        }

        return null;
    }

}
