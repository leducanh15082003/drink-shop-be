package isd.be.htc.repository;

import isd.be.htc.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    long countByVisitedAtBetween(LocalDateTime start, LocalDateTime end);
}
