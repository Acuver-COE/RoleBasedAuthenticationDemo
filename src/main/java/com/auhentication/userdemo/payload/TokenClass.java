package com.auhentication.userdemo.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Slf4j
@Document(collection = "tokenstore")
public class TokenClass {
    private String token;
    @Id
    private String userName;
    private Date expireDate;
}

