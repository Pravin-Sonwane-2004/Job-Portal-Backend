// package com.pravin.job_portal_backend.service;
// package com.pravin.job_portal_backend.service.service_implementation;

// import com.pravin.job_portal_backend.entity.JobAlert;
// import com.pravin.job_portal_backend.entity.User;
// import com.pravin.job_portal_backend.repository.JobAlertRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class JobAlertService {
//     @Autowired
//     private JobAlertRepository jobAlertRepository;

//     public List<JobAlert> getAlertsByUser(User user) {
//         return jobAlertRepository.findByUser(user);
//     }

//     public JobAlert createAlert(User user, String criteria) {
//         return jobAlertRepository.save(new JobAlert(user, criteria));
//     }

//     public void deleteAlert(Long alertId) {
//         jobAlertRepository.deleteById(alertId);
//     }
// }
