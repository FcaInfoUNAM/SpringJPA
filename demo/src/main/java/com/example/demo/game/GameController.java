package com.example.demo.game;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Game;
import com.example.demo.repo.GameRepository;

@RestController
public class GameController {
    private final GameRepository repo;

    public GameController(GameRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/games")
    public List<Game> findAllGames() {
        return repo.findAll();
    }

    @PostMapping("/games")
    public Game addGame(@RequestBody Game game) {
        return repo.save(game);
    }
}