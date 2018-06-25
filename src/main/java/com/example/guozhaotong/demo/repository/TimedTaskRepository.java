package com.example.guozhaotong.demo.repository;

import com.example.guozhaotong.demo.entity.TimedTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimedTaskRepository extends JpaRepository<TimedTask, Long> {
    List<TimedTask> findByStatus(int starus);
}
