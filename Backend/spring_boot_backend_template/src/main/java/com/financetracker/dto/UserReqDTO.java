package com.financetracker.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserReqDTO {
    private String username;
    
    @Email(message = "invalid email format !")
    private String email;
    private String password;
}
