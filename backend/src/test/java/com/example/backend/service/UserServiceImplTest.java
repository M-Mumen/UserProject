package com.example.backend.service;

import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.model.UserDTO;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("Ironman");
        user.setFirstName("Tony");
        user.setLastName(("Stark"));
        user.setEmail("ironman@avengers.com");
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO createdUser = userService.createUser(convertToDTO(user));

        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));

        UserDTO foundUser = userService.getUserById(1L);

        assertEquals(foundUser.getUserName(), user.getUserName());
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn((List<User>) Collections.singletonList(user));

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updatedUser = userService.updateUser(1L, convertToDTO(user));

        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void testUpdateUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, convertToDTO(user)));
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
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
