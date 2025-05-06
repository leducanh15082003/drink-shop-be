package isd.be.htc.config;

import isd.be.htc.model.Visit;
import isd.be.htc.repository.VisitRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class VisitInterceptor implements HandlerInterceptor {
    private final VisitRepository visitRepository;
    public VisitInterceptor(VisitRepository visitRepository){
        this.visitRepository = visitRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("GET".equals(request.getMethod()) && "/".equals(request.getRequestURI())) {
            visitRepository.save(new Visit(LocalDateTime.now()));
        }
        return true;
    }
}
