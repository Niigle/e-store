package rs.ac.ni.pmf.rwa.estore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.AddToOrderRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.OrderResponse;
import rs.ac.ni.pmf.rwa.estore.service.OrderService;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse addToOrder(@RequestBody /*@Valid*/ AddToOrderRequest request) {

        return orderService.addToOrder(request);
    }

    @GetMapping("/history/{userId}")
    public Page<OrderResponse> getOrderHistory(@PathVariable Long userId, @PageableDefault(page = 0, size = 10, sort = "createdOn") Pageable pageable) {

        return orderService.getOrderHistory(userId, pageable);
    }

    @GetMapping("/{userId}")
    public OrderResponse getOrder(@PathVariable Long userId) {

        return orderService.getOrder(userId);
    }

    @DeleteMapping("/item/{orderItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable Long orderItemId) {

        orderService.removeItem(orderItemId);
    }

    @PostMapping("/checkout/{userId}")
    public OrderResponse checkout(@PathVariable Long userId) {

        return orderService.checkout(userId);
    }
}
