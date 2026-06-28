package com.learning.main.repository.services;

import com.learning.main.model.Team;
import com.learning.main.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    public List<Team> findTeamsByCity(String city) {
        return teamRepository.findByCity(city);
    }

    public Optional<Team> findTeamByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName);
    }
    
    public boolean existsTeamByTeamName(String teamName) { // New method for existence check
        return teamRepository.findByTeamName(teamName).isPresent();
    }
    // Removed: public Optional<Team> findTeamByName(String name)
    // Removed: public List<Team> findTeamsByCoach(String coach)
    // Removed: public boolean existsTeamByName(String name)
}