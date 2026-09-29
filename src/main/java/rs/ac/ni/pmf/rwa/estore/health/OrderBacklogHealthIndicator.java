package rs.ac.ni.pmf.rwa.estore.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import rs.ac.ni.pmf.rwa.estore.repository.OrderRepository;

@Component
@RequiredArgsConstructor
public class OrderBacklogHealthIndicator implements HealthIndicator {

    private final OrderRepository orderRepository;

    @Override
    public Health health() {
        long stuckOrders = orderRepository.countByStatus("IN_PROGRESS");

        if (stuckOrders > 100) {
            return Health.down()
                    .withDetail("stuckOrders", stuckOrders)
                    .withDetail("reason", "Too many stuck orders!")
                    .build();
        }

        return Health.up()
                .withDetail("stuckOrders", stuckOrders)
                .build();
    }

}
