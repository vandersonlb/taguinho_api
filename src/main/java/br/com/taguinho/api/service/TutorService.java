package br.com.taguinho.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.taguinho.api.model.Tutor;
import br.com.taguinho.api.model.TutorDTO;
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

  public List<TutorDTO> getAllActiveTutors() {
    List<Tutor> tutorsActive = tutorRepository.findByActiveTrue();

    return tutorsActive.stream()
        .map(res -> new TutorDTO(res.getId(), res.getFullname(), res.getEmail(), res.getPhone(), res.getRole(), res.getActive()))
        .collect(Collectors.toList());
  }

  public TutorDTO getTutorById(Long id) {
    Tutor tutor = tutorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException());

    return new TutorDTO(tutor.getId(), tutor.getFullname(), tutor.getEmail(), tutor.getPhone(), tutor.getRole(), tutor.getActive());
  }

  public void updateTutor(Long id, Map<String, Object> updates) {
    Tutor tutorToUpdate = tutorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException());

    updates.forEach((key, value) -> {
      switch (key) {
        case "fullname" -> tutorToUpdate.setFullname((String) value);
        case "phone" -> tutorToUpdate.setPhone((String) value);
      }
    });

    tutorToUpdate.setUpdatedAt(LocalDateTime.now());
  }
}
