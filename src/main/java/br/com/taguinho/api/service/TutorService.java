package br.com.taguinho.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

  public TutorDTO createTutor(Tutor tutor) {
    tutor.setCreatedAt(Objects.requireNonNullElse(tutor.getCreatedAt(), LocalDateTime.now()));
    tutor.setActive(true);

    Tutor savedTutor = tutorRepository.save(tutor);
    return TutorDTO.mapToDTO(savedTutor);
  }

  public List<TutorDTO> getAllActiveTutors() {
    List<Tutor> tutorsActive = tutorRepository.findByActiveTrue();

    return tutorsActive.stream()
        .map(TutorDTO::mapToDTO)
        .toList();
  }

  public TutorDTO getTutorById(Long id) {
    Tutor tutor = tutorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException());

    return TutorDTO.mapToDTO(tutor);
  }

  public void updateTutor(Long id, Map<String, Object> updates) {
    Tutor tutor = tutorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException());

    if (updates.get("fullname") != null)
      tutor.setFullname((String) updates.get("fullname"));

    if (updates.get("phone") != null)
      tutor.setPhone((String) updates.get("phone"));

    tutor.setUpdatedAt(LocalDateTime.now());
  }
}
