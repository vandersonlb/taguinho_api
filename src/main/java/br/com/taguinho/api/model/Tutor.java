package br.com.taguinho.api.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.taguinho.api.enums.UserRole;
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
public class Tutor implements UserDetails {

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
  private UserRole role;

  @Column
  private Boolean active;

  @Column
  private LocalDateTime createdAt;

  @Column
  private LocalDateTime updatedAt;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return switch (this.role) {
      case MASTER -> List.of(new SimpleGrantedAuthority("ROLE_MASTER"), new SimpleGrantedAuthority("ROLE_USER"));
      case USER -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
      default -> throw new IllegalStateException();
    };
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}
