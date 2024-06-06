package com.westwebsystems.budgets.config;

import com.westwebsystems.budgets.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<EndUser, Long> {
}