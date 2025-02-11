package br.com.taguinho.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.taguinho.api.model.Tutor;
import br.com.taguinho.api.model.TutorDTO;
import br.com.taguinho.api.service.TokenService;
import br.com.taguinho.api.service.TutorService;
import jakarta.validation.Valid;

@Controller
public class AuthController {

  @Autowired
  private TutorService tutorService;

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestBody Tutor tutor) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(tutor.getEmail(), tutor.getPassword());
    Authentication auth = authManager.authenticate(usernamePassword);

    String token = tokenService.generateToken((Tutor) auth.getPrincipal());

    return ResponseEntity.ok(token);
  }

  @PostMapping("/register")
  public ResponseEntity<Void> registrer(@Valid @RequestBody Tutor tutor) {
    String encryptedPassword = passwordEncoder.encode(tutor.getPassword());
    tutor.setPassword(encryptedPassword);

    TutorDTO createdTutor = tutorService.createTutor(tutor);
    URI location = URI.create("/tutors/" + createdTutor.getId());

    return ResponseEntity.created(location).build();
  }
}
