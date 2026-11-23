package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}