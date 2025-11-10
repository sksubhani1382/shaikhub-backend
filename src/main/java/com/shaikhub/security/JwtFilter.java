package com.shaikhub.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    public JwtFilter(JwtUtil jwtUtil) { this.jwtUtil = jwtUtil; }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String path = request.getRequestURI();

        if (path.startsWith("/api/auth")) {
            chain.doFilter(req, res);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            ((HttpServletResponse) res).sendError(401, "Missing or invalid token");
            return;
        }

        String token = header.substring(7);
        if (!jwtUtil.validate(token)) {
            ((HttpServletResponse) res).sendError(401, "Invalid or expired token");
            return;
        }

        request.setAttribute("userEmail", jwtUtil.getSubject(token));
        chain.doFilter(req, res);
    }
}