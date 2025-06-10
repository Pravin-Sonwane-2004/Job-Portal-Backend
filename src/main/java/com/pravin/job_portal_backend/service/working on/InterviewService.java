// package com.pravin.job_portal_backend.service;
// package com.pravin.job_portal_backend.service.service_implementation;

// import com.pravin.job_portal_backend.entity.Interview;
// import com.pravin.job_portal_backend.entity.User;
// import com.pravin.job_portal_backend.entity.ApplyJob;
// import com.pravin.job_portal_backend.repository.InterviewRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;

// @Service
// public class InterviewService {
//     @Autowired
//     private InterviewRepository interviewRepository;

//     public List<Interview> getInterviewsByUser(User user) {
//         return interviewRepository.findByUser(user);
//     }

//     public List<Interview> getInterviewsByEmployer(User employer) {
//         return interviewRepository.findByEmployer(employer);
//     }

//     public List<Interview> getInterviewsByApplication(ApplyJob application) {
//         return interviewRepository.findByApplication(application);
//     }

//     public Interview scheduleInterview(ApplyJob application, User employer, User user, LocalDateTime scheduledTime, String status) {
//         return interviewRepository.save(new Interview(application, employer, user, scheduledTime, status));
//     }

//     public void updateInterviewStatus(Long interviewId, String status) {
//         Optional<Interview> interviewOpt = interviewRepository.findById(interviewId);
//         interviewOpt.ifPresent(interview -> {
//             interview.setStatus(status);
//             interviewRepository.save(interview);
//         });
//     }

//     public void deleteInterview(Long interviewId) {
//         interviewRepository.deleteById(interviewId);
//     }
// }
