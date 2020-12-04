package com.auhentication.userdemo.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Slf4j
public class ApiResponse {
    private int status;
    private String message;
}
