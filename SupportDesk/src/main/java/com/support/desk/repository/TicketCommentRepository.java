package com.support.desk.repository;

import com.support.desk.model.Ticket;
import com.support.desk.model.TicketComment;
import com.support.desk.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    List<TicketComment> findByTicketOrderByCreatedAtAsc(Ticket ticket);

	Optional<User> findByTicketTicketId(Long ticketId);
}