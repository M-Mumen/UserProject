package com.example.backend.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;

}
