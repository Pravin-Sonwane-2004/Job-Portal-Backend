// package com.pravin.job_portal_backend.repository;

// import com.pravin.job_portal_backend.entity.Interview;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import com.pravin.job_portal_backend.entity.ApplyJob;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public interface InterviewRepository extends JpaRepository<Interview, Long> {
//     List<Interview> findByJOB_SEEKER(JOB_SEEKER JOB_SEEKER);
//     List<Interview> findByEmployer(JOB_SEEKER employer);
//     List<Interview> findByApplication(ApplyJob application);
// }
