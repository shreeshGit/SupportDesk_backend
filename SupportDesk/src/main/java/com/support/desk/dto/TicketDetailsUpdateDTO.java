package com.support.desk.dto;

import com.support.desk.model.TicketPriority;
import com.support.desk.model.TicketStatus;

public class TicketDetailsUpdateDTO {

    private Long ticketId;
    private TicketStatus status;
    private TicketPriority priority;
    private String content;

    public TicketDetailsUpdateDTO() {}

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}