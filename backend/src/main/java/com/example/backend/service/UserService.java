package com.example.backend.service;

import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.model.User;
import com.example.backend.model.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserDTO userDTO) throws UserAlreadyExistsException;

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(Long id, UserDTO userDTO) throws UserAlreadyExistsException;

    void deleteUser(Long id);
}
