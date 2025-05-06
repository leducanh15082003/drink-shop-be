package isd.be.htc.controller;

import isd.be.htc.model.Visit;
import isd.be.htc.repository.VisitRepository;
import isd.be.htc.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/visits")
public class VisitController {
    @Autowired
    private VisitRepository visitRepository;

    @PostMapping
    public ResponseEntity<Void> recordVisit() {
        visitRepository.save(new Visit(LocalDateTime.now()));
        return ResponseEntity.ok().build();
    }
}
