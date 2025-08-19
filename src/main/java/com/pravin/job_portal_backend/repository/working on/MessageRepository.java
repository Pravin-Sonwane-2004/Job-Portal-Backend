// package com.pravin.job_portal_backend.repository;

// import com.pravin.job_portal_backend.entity.Message;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public interface MessageRepository extends JpaRepository<Message, Long> {
//     List<Message> findBySender(JOB_SEEKER sender);
//     List<Message> findByReceiver(JOB_SEEKER receiver);
//     List<Message> findBySenderAndReceiver(JOB_SEEKER sender, JOB_SEEKER receiver);
// }
