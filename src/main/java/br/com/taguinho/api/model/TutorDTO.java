package br.com.taguinho.api.model;

import br.com.taguinho.api.enums.UserRole;

public record TutorDTO(Long id, String fullname, String email, String phone, UserRole role, Boolean active) {

}
