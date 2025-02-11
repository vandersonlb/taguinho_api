package br.com.taguinho.api.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

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
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = request.getHeader("Authorization");

    if (token != null) {
      token = token.replace("Bearer ", "");
      String login = tokenService.validateToken(token);
      UserDetails user = tutorRepository.findByEmail(login);

      UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(user, null,
          user.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(usernamePassword);
    }
    filterChain.doFilter(request, response);
  }
}
