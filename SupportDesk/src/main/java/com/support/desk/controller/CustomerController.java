package com.support.desk.controller;

import com.support.desk.dto.TicketDTO;
import com.support.desk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/raise/issue")
    public ResponseEntity<TicketDTO> raiseIssue(@RequestBody TicketDTO ticketDTO, @RequestParam Long userId) {
        TicketDTO ticket = ticketService.createTicket(ticketDTO, userId);
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getCustomerTickets(@RequestParam Long userId) {
        List<TicketDTO> tickets = ticketService.findByCustomerUserId(userId);
        return ResponseEntity.ok(tickets);
    }
}