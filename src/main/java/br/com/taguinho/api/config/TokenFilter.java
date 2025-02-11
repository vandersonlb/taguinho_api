package br.com.taguinho.api.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.taguinho.api.repository.TutorRepository;
import br.com.taguinho.api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class TokenFilter extends OncePerRequestFilter {

  @Autowired
  private TokenService tokenService;

  @Autowired
  private TutorRepository tutorRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = request.getHeader("Authorization");
    if (token != null) {
      try {
        token = token.replace("Bearer ", "");
        String login = tokenService.validateToken(token);

        if (login.isEmpty()) throw new JWTVerificationException("Token inválido.");
        UserDetails user = tutorRepository.findByEmail(login);
        var usernamePassword = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePassword);

      } catch (JWTVerificationException ex) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
      }
    }
    filterChain.doFilter(request, response);
  }
}
