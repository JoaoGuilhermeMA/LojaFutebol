package com.ufrn.edu.trabalho;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class FiltroAuth implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        HttpServletRequest request = ((HttpServletRequest) servletRequest);

        // Verifica se a URL atual corresponde à página de registro ou de login
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/registrar") || requestURI.endsWith("/logar")) {
            // Se corresponder, permite que a solicitação continue sem aplicar o filtro
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpSession session = request.getSession(false);

        if (session == null){
            response.sendRedirect("index.html?msg=Você precisa logar antes");
        } else {
            Boolean logado = (Boolean) session.getAttribute("logado");
            if (!logado || logado == null){
                response.sendRedirect("index.html?msg=Você precisa logar antes");
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
