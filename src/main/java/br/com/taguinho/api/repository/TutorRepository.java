package br.com.taguinho.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.taguinho.api.model.Tutor;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

  public List<Tutor> findByActiveTrue();

}
