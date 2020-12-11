package com.auhentication.userdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
@Document(collection = "roles")
public class Role {
    @Id
    private String id;
    Rolevalues name;
    private String tenantId;
}
