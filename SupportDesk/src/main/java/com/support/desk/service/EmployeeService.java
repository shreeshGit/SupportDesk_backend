package com.support.desk.service;

import com.support.desk.dto.UserRegistrationDTO;
import com.support.desk.model.User;
import com.support.desk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injected to encrypt passwords

    @Transactional
    public User registerEmployee(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        
        // Encrypt the plain text password before saving
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        
        user.setEmail(registrationDTO.getEmail());
        user.setFullName(registrationDTO.getFullName());
        user.setPhoneNumber(registrationDTO.getPhoneNumber());
        user.setAge(registrationDTO.getAge());
        user.setGender(registrationDTO.getGender());
        user.setEmployeeCode(registrationDTO.getEmployeeCode());
        user.setDepartment(registrationDTO.getDepartment());
        return userRepository.save(user);
    }
}