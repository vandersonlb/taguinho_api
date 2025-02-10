package br.com.taguinho.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.com.taguinho.api.repository.TutorRepository;

@Service
public class AuthService implements UserDetailsService {

  @Autowired
  private TutorRepository tutorRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return tutorRepository.findByEmail(username);
  }
}
