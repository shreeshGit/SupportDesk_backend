package com.support.desk.dto;

import com.support.desk.model.TicketComment;
import com.support.desk.model.TicketPriority;
import com.support.desk.model.TicketStatus;
import com.support.desk.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketEmpDTO {

    private Long id;
    private Long ticketId;
    private String title;
    private String description;
    private TicketPriority priority;
    private TicketStatus status;
    private String department;
    private Date creationTime;
    private Date resolutionTime;

    private User customer;
    private List<TicketCommentDTO> comments = new ArrayList<>();

    public TicketEmpDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date date) {
        this.creationTime = date;
    }

    public Date getResolutionTime() {
        return resolutionTime;
    }

    public void setResolutionTime(Date date) {
        this.resolutionTime = date;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public List<TicketCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<TicketCommentDTO> list) {
        this.comments = list;
    }
}