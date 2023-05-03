package com.greengoldfish.facade.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserToUpdateDTO implements Serializable {

    @NotBlank
    private String email;

    private String password;

    private String firstName;

    private String lastName;
}
