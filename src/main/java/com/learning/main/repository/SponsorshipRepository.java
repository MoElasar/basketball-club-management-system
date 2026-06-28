package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Sponsor;
import com.learning.main.model.Sponsorship;
import com.learning.main.model.Team;

import java.time.LocalDate;
import java.util.List;


public interface SponsorshipRepository extends JpaRepository<Sponsorship, Long> {
    List<Sponsorship> findByTeam(Team team);
    List<Sponsorship> findBySponsor(Sponsor sponsor);
    List<Sponsorship> findByStartDateAfter(LocalDate date);
}