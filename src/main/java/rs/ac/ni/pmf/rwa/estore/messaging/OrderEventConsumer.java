package rs.ac.ni.pmf.rwa.estore.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rs.ac.ni.pmf.rwa.estore.config.RabbitConfig;
import rs.ac.ni.pmf.rwa.estore.model.dto.event.OrderCompletedEvent;

@Component
public class OrderEventConsumer {

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void handleOrderCompleted(OrderCompletedEvent event) {

        System.out.println("Processing order #" + event.getOrderId() +
                ", sending email to " + event.getUserEmail());
    }

}
