package com.auhentication.userdemo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class LoginRequest {

    @NotEmpty(message = "EmailId cannot be empty")
    private String emailId;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
