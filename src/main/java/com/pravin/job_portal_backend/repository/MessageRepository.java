package com.pravin.job_portal_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.Message;
import com.pravin.job_portal_backend.entity.User;

public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findBySenderOrderBySentAtDesc(User sender);

  List<Message> findByReceiverOrderBySentAtDesc(User receiver);

  List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderBySentAtAsc(
      User sender, User receiver, User receiver2, User sender2);
}
