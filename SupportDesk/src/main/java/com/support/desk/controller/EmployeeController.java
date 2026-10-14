package com.support.desk.controller;

import com.support.desk.dto.TicketDTO;
import com.support.desk.dto.TicketDetailsUpdateDTO;
import com.support.desk.dto.TicketEmpDTO;
import com.support.desk.dto.UserRegistrationDTO;
import com.support.desk.service.EmployeeService;
import com.support.desk.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@PreAuthorize("hasRole('EMPLOYEE')")
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(@Valid @RequestBody UserRegistrationDTO registrationDto) {
        employeeService.registerEmployee(registrationDto);
        return ResponseEntity.ok().body("Employee registered successfully!");
    }

    @GetMapping("/tickets/{userId}")
    public ResponseEntity<List<TicketEmpDTO>> getAssignedTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByAgent(userId));
    }

    @PutMapping("/ticket/update")
    public String updateTicket(@RequestBody TicketDetailsUpdateDTO ticketDetailsUpdateDTO) {
        return ticketService.updateTicket(ticketDetailsUpdateDTO);
    }
}