package com.example.backend.service;

import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.model.UserDTO;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) throws UserAlreadyExistsException {
        User user = fromDTO(userDTO);
        try {
            User savedUser = userRepository.save(user);
            return convertToDTO(savedUser);
        }
        catch(DataIntegrityViolationException ex) {
            throw new UserAlreadyExistsException(user.getUserName() + " already exists");
        }

    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(Math.toIntExact(id))
                .map(this::convertToDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(Math.toIntExact(id)).map(user -> {
            user.setUserName(userDTO.getUserName());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());

            try {
                User savedUser = userRepository.save(user);
                return convertToDTO(savedUser);
            } catch (DataIntegrityViolationException e) {
                throw new UserAlreadyExistsException("User Name " + user.getUserName() + " already taken");
            }

        }).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(Math.toIntExact(id))) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id.intValue());
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUserName(), user.getFirstName(),
                user.getLastName(), user.getEmail());
    }

    private User fromDTO(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUserName(), userDTO.getFirstName(),
                userDTO.getLastName(), userDTO.getEmail());
    }
}
