package com.example.guozhaotong.demo.repository;

import com.example.guozhaotong.demo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
