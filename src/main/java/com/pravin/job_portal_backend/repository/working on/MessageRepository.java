// package com.pravin.job_portal_backend.repository;

// import com.pravin.job_portal_backend.entity.Message;
// import com.pravin.job_portal_backend.entity.User;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public interface MessageRepository extends JpaRepository<Message, Long> {
//     List<Message> findBySender(User sender);
//     List<Message> findByReceiver(User receiver);
//     List<Message> findBySenderAndReceiver(User sender, User receiver);
// }
