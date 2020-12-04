package com.auhentication.userdemo.payload;

import com.auhentication.userdemo.config.StringConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class SignUpRequest {

    @NotEmpty(message = "Acuver id cannot be empty")
    private String acuverId;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "EmailId cannot be empty")
    @Pattern(regexp= StringConstants.regex,
            message="EmailId is not valid")
    private String emailId;

    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp=StringConstants.passwordregx,
            message="password length must be 8")
    private String password;

    @NotEmpty(message = "Confirm password cannot be empty")
    private String confirmPassword;
}
