package cz.cvut.fel.ear.projekt.dto;

import cz.cvut.fel.ear.projekt.model.Role;

import java.io.Serializable;

public record UserDto(Long id, String username, Role role) {}
