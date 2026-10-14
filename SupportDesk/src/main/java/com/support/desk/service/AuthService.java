package com.support.desk.service;

import com.support.desk.dto.JwtRequest;
import com.support.desk.dto.JwtResponse;
import com.support.desk.dto.UserRegistrationDTO;
import com.support.desk.model.Role;
import com.support.desk.model.User;
import com.support.desk.repository.RoleRepository;
import com.support.desk.repository.UserRepository;
import com.support.desk.security.JwtAuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.support.desk.model.Role;
import com.support.desk.repository.RoleRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtAuthenticationHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JwtResponse login(JwtRequest jwtRequest) {
        this.doAuthenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtHelper.generateToken(userDetails);
        return JwtResponse.builder().jwtToken(token).build();
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password!");
        }
    }

    @Transactional
    public void registerCustomer(UserRegistrationDTO dto) {
        registerUserWithRole(dto, "ROLE_CUSTOMER");
    }

    @Transactional
    public void registerEmployee(UserRegistrationDTO dto) {
        registerUserWithRole(dto, "ROLE_EMPLOYEE");
    }

    @Transactional
    public void registerAdmin(UserRegistrationDTO dto) {
        registerUserWithRole(dto, "ROLE_ADMIN");
    }

    private void registerUserWithRole(UserRegistrationDTO dto, String roleName) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + dto.getUsername());
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());

        if (dto.getEmployeeCode() != null) {
            user.setEmployeeCode(dto.getEmployeeCode());
        }
        if (dto.getDepartment() != null) {
            user.setDepartment(dto.getDepartment());
        }

        Role role = roleRepository.findByRoleName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        user.getRoles().add(role);
        userRepository.save(user);
    }
}