package br.com.taguinho.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.taguinho.api.model.Tutor;
import br.com.taguinho.api.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TutorService {

  @Autowired
  private TutorRepository tutorRepository;

  public Tutor createTutor(Tutor Tutor) {
    return tutorRepository.save(Tutor);
  }

  public List<Tutor> getAllTutors() {
    return tutorRepository.findAll();
  }

  public Optional<Tutor> getTutorById(Long id) {
    return tutorRepository.findById(id);
  }

  public Tutor updateTutor(Long id, Map<String, Object> updates) {
    Tutor tutorToUpdate = tutorRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException());

    updates.forEach((key, value) -> {
      switch (key) {
        case "fullname" -> tutorToUpdate.setFullname((String) value);
        case "email" -> tutorToUpdate.setEmail((String) value);
        case "active" -> tutorToUpdate.setActive((Boolean) value);
        default -> System.out.println("Invalid field: " + key);
      }
    });

    tutorToUpdate.setUpdatedAt(LocalDateTime.now());
    return tutorRepository.save(tutorToUpdate);
  }

}
