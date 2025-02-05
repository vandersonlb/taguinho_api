package br.com.taguinho.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Tutor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotNull
  @Size(min = 3, max = 150)
  private String fullname;

  @Column(unique = true)
  @NotNull
  @Email
  @Size(min = 10, max = 150)
  private String email;

  @Column
  @Size(min = 9, max = 11)
  private String phone;

  @Column
  @NotNull
  @Size(min = 8, max = 100)
  private String password;

  @Column
  @NotNull
  private Boolean active = true;

  @Column
  @NotNull
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column
  private LocalDateTime updatedAt;

}
