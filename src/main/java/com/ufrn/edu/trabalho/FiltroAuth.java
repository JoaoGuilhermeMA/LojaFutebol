package com.ufrn.edu.trabalho;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

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

        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/registrar") || requestURI.endsWith("/logar")|| requestURI.endsWith("/")) {

            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpSession session = request.getSession(false);

        if (session == null){
            response.sendRedirect("/logar");
            return;
        } else {
            Boolean logado = (Boolean) session.getAttribute("logado");
            if (logado == null){
                session.invalidate();
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
