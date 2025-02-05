package br.com.taguinho.api.enums;

public enum UserRole {

  MASTER("master"),
  USER("user");

  private String role;

  UserRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return this.role;
  }
}
