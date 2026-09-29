package rs.ac.ni.pmf.rwa.estore.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.config.RabbitConfig;
import rs.ac.ni.pmf.rwa.estore.model.dto.event.OrderCompletedEvent;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishOrderCompleted(Long orderId, String userEmail) {

        OrderCompletedEvent event = OrderCompletedEvent.builder().orderId(orderId).userEmail(userEmail).build();

        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_QUEUE, event);
    }
}
