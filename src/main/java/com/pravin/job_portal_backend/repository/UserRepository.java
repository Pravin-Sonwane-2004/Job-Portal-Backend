package com.pravin.job_portal_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.enums.Role;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  List<User> findByRole(Role role);
  List<User> findByCompany(Company company);
  long countByCompany(Company company);
  long countByRole(Role role);
  void deleteByEmail(String email);
}
