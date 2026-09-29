package rs.ac.ni.pmf.rwa.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Orders", description = "Order and shopping cart management API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Add item to order", description = "Add a product item to the user's active order or cart")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item successfully added to order",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product or user does not exist", content = @Content)
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse addToOrder(@RequestBody @Valid AddToOrderRequest request) {

        return orderService.addToOrder(request);
    }

    @Operation(summary = "Get user order history", description = "Paginated list of completed orders for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order history"),
            @ApiResponse(responseCode = "404", description = "User with the given ID does not exist", content = @Content)
    })
    @GetMapping("/history/{userId}")
    public Page<OrderResponse> getOrderHistory(
            @Parameter(description = "User ID", example = "1") @PathVariable Long userId,
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(page = 0, size = 10, sort = "createdOn") Pageable pageable) {

        return orderService.getOrderHistory(userId, pageable);
    }

    @Operation(summary = "Get active order", description = "Retrieve current active order (shopping cart) for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active order retrieved successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Active order or user not found", content = @Content)
    })
    @GetMapping("/{userId}")
    public OrderResponse getOrder(
            @Parameter(description = "User ID", example = "1") @PathVariable Long userId) {

        return orderService.getOrder(userId);
    }

    @Operation(summary = "Remove item from order", description = "Remove an order item by its ID from the cart")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item successfully removed from order"),
            @ApiResponse(responseCode = "404", description = "Order item with the given ID does not exist", content = @Content)
    })
    @DeleteMapping("/item/{orderItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(
            @Parameter(description = "Order Item ID", example = "1") @PathVariable Long orderItemId) {

        orderService.removeItem(orderItemId);
    }

    @Operation(summary = "Checkout order", description = "Finalize and place the active order for a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order checked out successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cart is empty or order cannot be processed", content = @Content),
            @ApiResponse(responseCode = "404", description = "Active order or user not found", content = @Content)
    })
    @PostMapping("/checkout/{userId}")
    public OrderResponse checkout(
            @Parameter(description = "User ID", example = "1") @PathVariable Long userId) {

        return orderService.checkout(userId);
    }
}