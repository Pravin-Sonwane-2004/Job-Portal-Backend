package com.pravin.job_portal_backend.repository;

import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // Find jobs by company
    List<Job> findByCompanyId(Long companyId);

    // // Find jobs by status
    // List<Job> findByStatus(JobStatus status);

    // // Custom search across title, location, and category
    // @Query("""
    //         SELECT j FROM Job j
    //         WHERE (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
    //                OR LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%'))
    //                OR LOWER(j.category) LIKE LOWER(CONCAT('%', :keyword, '%')))
    //           AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
    //         """)
    // List<Job> searchJobs(@Param("keyword") String keyword,
    //         @Param("location") String location);

    // // ✅ Fixed filtering query with pageable
    // @Query("""
    //         SELECT j FROM Job j
    //         WHERE (:jobTitle IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :jobTitle, '%')))
    //           AND (:jobLocation IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :jobLocation, '%')))
    //           AND (:minSalary IS NULL OR j.salary >= :minSalary)
    //           AND (:maxSalary IS NULL OR j.salary <= :maxSalary)
    //           AND j.jobStatus = com.pravin.job_portal_backend.enums.JobStatus.ACTIVE
    //         """)
    // Page<Job> filterJobs(@Param("jobTitle") String jobTitle,
    //         @Param("jobLocation") String jobLocation,
    //         @Param("minSalary") Double minSalary,
    //         @Param("maxSalary") Double maxSalary,
    //         Pageable pageable);
}
