package br.com.taguinho.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.taguinho.api.model.TutorDTO;
import br.com.taguinho.api.service.TutorService;

@RestController
@RequestMapping("/tutors")
public class TutorController {

  @Autowired
  private TutorService tutorService;

  @GetMapping
  public ResponseEntity<List<TutorDTO>> getAllActive() {
    List<TutorDTO> tutorsActive = tutorService.getAllActiveTutors();
    return ResponseEntity.ok(tutorsActive);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TutorDTO> getOne(@PathVariable Long id) {
    TutorDTO tutor = tutorService.getTutorById(id);
    return ResponseEntity.ok(tutor);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
    tutorService.updateTutor(id, updates);
    return ResponseEntity.noContent().build();
  }
}
