package com.support.desk.controller;

import com.support.desk.dto.JwtRequest;
import com.support.desk.dto.JwtResponse;
import com.support.desk.dto.UserRegistrationDTO;
import com.support.desk.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        return new ResponseEntity<>(authService.login(jwtRequest), HttpStatus.OK);
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody UserRegistrationDTO dto) {
        authService.registerCustomer(dto);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(@RequestBody UserRegistrationDTO dto) {
        authService.registerEmployee(dto);
        return ResponseEntity.ok("Employee registered successfully!");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRegistrationDTO dto) {
        authService.registerAdmin(dto);
        return ResponseEntity.ok("Admin registered successfully!");
    }
}