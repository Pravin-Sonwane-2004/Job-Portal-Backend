// package com.pravin.job_portal_backend.service;
// package com.pravin.job_portal_backend.service.service_implementation;

// import com.pravin.job_portal_backend.entity.JobAlert;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import com.pravin.job_portal_backend.repository.JobAlertRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class JobAlertService {
//     @Autowired
//     private JobAlertRepository jobAlertRepository;

//     public List<JobAlert> getAlertsByJOB_SEEKER(JOB_SEEKER JOB_SEEKER) {
//         return jobAlertRepository.findByJOB_SEEKER(JOB_SEEKER);
//     }

//     public JobAlert createAlert(JOB_SEEKER JOB_SEEKER, String criteria) {
//         return jobAlertRepository.save(new JobAlert(JOB_SEEKER, criteria));
//     }

//     public void deleteAlert(Long alertId) {
//         jobAlertRepository.deleteById(alertId);
//     }
// }
