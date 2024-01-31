package com.example.beautybook.repository;

import com.example.beautybook.model.WorkPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkPeriodRepository extends JpaRepository<WorkPeriod, Long> {
}
