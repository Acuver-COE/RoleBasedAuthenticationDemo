package com.auhentication.userdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
@Document(collection = "users")
public class User {
    @Id
    private String acuverId;

    private String name;

    private String emailId;

    private String password;

    private String confirmPassword;

    private List<Role> roles;

}
