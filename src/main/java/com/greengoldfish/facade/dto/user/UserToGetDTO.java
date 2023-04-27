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
public class UserToGetDTO implements Serializable {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private Boolean activated;

    private String activationKey;

    private String resetKey;
}
