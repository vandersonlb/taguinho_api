package br.com.taguinho.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.taguinho.api.model.Tutor;
import br.com.taguinho.api.service.TutorService;

@RestController
@RequestMapping("/tutors")
public class TutorController {

  @Autowired
  private TutorService tutorService;

  @PostMapping
  public ResponseEntity<Tutor> create(@RequestBody Tutor tutor) {
    Tutor createdTutor = tutorService.createTutor(tutor);
    URI location = URI.create("/tutors/" + createdTutor.getId());
    return ResponseEntity.created(location).build();
  }

  @GetMapping
  public List<Tutor> getAll() {
    return tutorService.getAllTutors();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tutor> getOne(@PathVariable Long id) {
    return tutorService.getTutorById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Tutor> update(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
    return tutorService.getTutorById(id)
        .map(tutorToUpdate -> {
          Tutor tutorUpdated = tutorService.updateTutor(tutorToUpdate.getId(), updates);
          return ResponseEntity.ok(tutorUpdated);
        })
        .orElse(ResponseEntity.notFound().build());
  }
}