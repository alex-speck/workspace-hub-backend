package com.projetofullstack.workspace_hub.infrastructure.config;

import com.projetofullstack.workspace_hub.application.dto.response.UsuarioLogado;
import com.projetofullstack.workspace_hub.infrastructure.exceptions.InvalidSessionException;
import com.projetofullstack.workspace_hub.application.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Liberar métodos, faz quase a mesma coisa que o Spring Security
        if(path.equals("/auth/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || request.getMethod().startsWith("OPTIONS")
                || path.startsWith("/webjars")
                || path.startsWith("/auth/cadastro")
                || path.startsWith("/usuarios/admin")
                || path.startsWith("/auth/recuperar-senha")
                || path.startsWith("/auth/alterar-senha")
                || path.startsWith("/reservas/cancelar/")
                || path.startsWith("/reservas/detalhes-cancelamento/")
        ){
            filterChain.doFilter(request, response);
            return;
        }


        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")) {

            String token = header.replace("Bearer ", "");

            try{

                var usuarioLogado = tokenService.validarToken(token);

                UsernamePasswordAuthenticationToken usuario = new UsernamePasswordAuthenticationToken(
                        new UsuarioLogado(usuarioLogado),
                        null,
                        usuarioLogado.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(usuario);
            }catch (Exception ex){
                throw new InvalidSessionException("Token expirado ou inválido");
            }

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token não informado ou inválido");
            return;
        }

        filterChain.doFilter(request, response);

    }

}
