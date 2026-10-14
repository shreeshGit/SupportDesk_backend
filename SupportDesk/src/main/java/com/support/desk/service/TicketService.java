package com.support.desk.service;

import com.support.desk.dto.*;
import com.support.desk.exception.TicketNotFoundException;
import com.support.desk.exception.UserNotFoundException;
import com.support.desk.model.*;
import com.support.desk.repository.TicketCommentRepository;
import com.support.desk.repository.TicketRepository;
import com.support.desk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketCommentRepository ticketCommentRepository;

    @Transactional
    public TicketDTO createTicket(TicketDTO ticketDTO, Long userId) {
        User customer = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Ticket ticket = new Ticket();
        ticket.setTicketId(generateFourDigitNumber());
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setPriority(TicketPriority.LOW);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCustomer(customer);
        ticket.setCreationTime(LocalDateTime.now());
        ticket.setResolutionTime(LocalDateTime.now().plusHours(48));

        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDTO(savedTicket);
    }

    @Transactional
    public TicketDTO assignTicket(Long ticketId, Long agentId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + ticketId));

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new UserNotFoundException("Agent not found with id: " + agentId));

        ticket.setAssignedAgent(agent);
        ticket.setDepartment(agent.getDepartment());

        Ticket updatedTicket = ticketRepository.save(ticket);
        return convertToDTO(updatedTicket);
    }

    @Transactional
    public String updateTicket(TicketDetailsUpdateDTO ticketDetailsUpdateDTO) {
        Ticket ticket = ticketRepository.findByTicketId(ticketDetailsUpdateDTO.getTicketId());
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket not found with id: " + ticketDetailsUpdateDTO.getTicketId());
        }

        if (!ticket.getStatus().equals(TicketStatus.RESOLVED)) {
            if (ticketDetailsUpdateDTO.getStatus() != null && ticketDetailsUpdateDTO.getStatus() != ticket.getStatus()) {
                ticket.setStatus(ticketDetailsUpdateDTO.getStatus());
                if (ticket.getStatus().equals(TicketStatus.RESOLVED)) {
                    ticket.setResolutionTime(LocalDateTime.now());
                }
            }

            if (ticketDetailsUpdateDTO.getContent() != null && !ticketDetailsUpdateDTO.getContent().isEmpty()) {
                TicketComment ticketComment = new TicketComment();
                ticketComment.setTicket(ticket);
                ticketComment.setUser(ticket.getCustomer());
                ticketComment.setContent(ticketDetailsUpdateDTO.getContent());
                ticketComment.setCreatedAt(LocalDateTime.now());
                ticket.getComments().add(ticketComment);
            }

            if (ticketDetailsUpdateDTO.getPriority() != null && ticketDetailsUpdateDTO.getPriority() != ticket.getPriority()) {
                ticket.setPriority(ticketDetailsUpdateDTO.getPriority());
            }

            ticketRepository.save(ticket);
            return "Ticket details updated successfully";
        } else {
            return "The Ticket with id " + ticket.getTicketId() + " is already Resolved.";
        }
    }

    public List<TicketDTO> findByCustomerUserId(Long userId) {
        User customer = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<Ticket> tickets = ticketRepository.findByCustomer(customer);
        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("No tickets found for user with id: " + userId);
        }

        return tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TicketEmpDTO> getTicketsByAgent(Long userId) {
        User agent = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<Ticket> tickets = ticketRepository.findByAssignedAgent(agent);
        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("No tickets found assigned to agent with id: " + userId);
        }

        return tickets.stream().map(this::convertToEmpDTO).collect(Collectors.toList());
    }

    public List<TicketDTO> getTicketsByStatus(TicketStatus status) {
        List<Ticket> tickets = ticketRepository.findByStatus(status);
        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("No tickets found with status: " + status);
        }
        return tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TicketDTO> getTicketsByDepartment(String department) {
        List<Ticket> tickets = ticketRepository.findByDepartment(department);
        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("No tickets found in department: " + department);
        }
        return tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Long getTotalActiveTicketCount() {
        return (long) ticketRepository.findByStatus(TicketStatus.OPEN).size();
    }

    public List<TicketCommentDTO> getCommentsByTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findByTicketId(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket not found with id: " + ticketId);
        }
        List<TicketComment> comments = ticketCommentRepository.findByTicketOrderByCreatedAtAsc(ticket);
        return comments.stream().map(this::convertToCommentDTO).collect(Collectors.toList());
    }

    // Helper Methods
    private Long generateFourDigitNumber() {
        Random random = new Random();
        return 1000L + random.nextInt(9000);
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setTicketId(ticket.getTicketId());
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPriority(ticket.getPriority());
        dto.setCreationTime(ticket.getCreationTime());
        dto.setResolutionTime(ticket.getResolutionTime());
        return dto;
    }

    private TicketEmpDTO convertToEmpDTO(Ticket ticket) {
        TicketEmpDTO dto = new TicketEmpDTO();
        dto.setTicketId(ticket.getTicketId());
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPriority(ticket.getPriority());
        return dto;
    }

    private TicketCommentDTO convertToCommentDTO(TicketComment comment) {
        TicketCommentDTO dto = new TicketCommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        if (comment.getCreatedAt() != null) {
            dto.setCreatedAt(java.sql.Timestamp.valueOf(comment.getCreatedAt()));
        }
        if (comment.getUser() != null) {
            dto.setUsername(comment.getUser().getUsername());
        }
        return dto;
    }
}