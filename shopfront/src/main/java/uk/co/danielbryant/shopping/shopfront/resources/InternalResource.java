package uk.co.danielbryant.shopping.shopfront.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.danielbryant.shopping.shopfront.model.CircuitBreaker;
import uk.co.danielbryant.shopping.shopfront.repo.CircuitBreakerRepo;

import java.util.List;

@RestController
@RequestMapping("/internal")
public class InternalResource {

    @Autowired
    private CircuitBreakerRepo circuitBreakerRepo;

    @RequestMapping("/circuit-breakers")
    public List<CircuitBreaker> getCircuitBreakers() {
        return circuitBreakerRepo.getAll();
    }
}
