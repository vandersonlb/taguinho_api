package br.com.taguinho.api.model;

import br.com.taguinho.api.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TutorDTO {

  private Long id;
  private String fullname;
  private String email;
  private String phone;
  private UserRole role;
  private Boolean active;

  public static TutorDTO mapToDTO(Tutor tutor) {
    return new TutorDTO(
        tutor.getId(),
        tutor.getFullname(),
        tutor.getEmail(),
        tutor.getPhone(),
        tutor.getRole(),
        tutor.getActive());
  }
}
