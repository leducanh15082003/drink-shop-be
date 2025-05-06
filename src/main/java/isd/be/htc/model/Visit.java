package isd.be.htc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "visit")
@Getter
@Setter
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visited_at", nullable = false)
    private LocalDateTime visitedAt;

    public Visit (LocalDateTime visitedAt){
        this.visitedAt = visitedAt;
    }
}
