package uk.co.danielbryant.shopping.shopfront.repo;

import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import org.springframework.stereotype.Component;
import uk.co.danielbryant.shopping.shopfront.model.CircuitBreaker;

import java.util.List;
import java.util.Optional;

import static com.github.quiram.utils.Collections.map;
import static uk.co.danielbryant.shopping.shopfront.model.Constants.COMMAND_KEYS;

@Component
public class CircuitBreakerRepo {
    public List<CircuitBreaker> getAll() {
        return map(COMMAND_KEYS, (String key) -> {
            final HystrixCircuitBreaker circuitBreaker = HystrixCircuitBreaker.Factory.getInstance(HystrixCommandKey.Factory.asKey(key));
            return new CircuitBreaker(key, Optional.ofNullable(circuitBreaker).map(HystrixCircuitBreaker::isOpen).orElse(false));
        });
    }
}
