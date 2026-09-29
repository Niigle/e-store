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
import rs.ac.ni.pmf.rwa.estore.model.dto.request.StoreRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreResponse;
import rs.ac.ni.pmf.rwa.estore.service.StoreService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Tag(name = "Stores", description = "Store management API")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "Get all stores", description = "Paginated and sorted list of all stores")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of stores")
    @GetMapping
    public Page<StoreResponse> getAllStores(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        return storeService.getAllStores(pageable);
    }

    @Operation(summary = "Get active stores only", description = "List of all active stores")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of active stores")
    @GetMapping("/active")
    public List<StoreResponse> getActiveStores() {
        return storeService.getActiveStores();
    }

    @Operation(summary = "Get store by ID", description = "Retrieve a single store by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Store found",
                    content = @Content(schema = @Schema(implementation = StoreResponse.class))),
            @ApiResponse(responseCode = "404", description = "Store with the given ID does not exist", content = @Content)
    })
    @GetMapping("/{id}")
    public StoreResponse getStoreById(
            @Parameter(description = "Store ID", example = "1") @PathVariable Long id) {
        return storeService.getStoreById(id);
    }

    @Operation(summary = "Create a new store", description = "Create a store with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Store created successfully",
                    content = @Content(schema = @Schema(implementation = StoreResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Manager or category does not exist", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse createStore(@RequestBody @Valid StoreRequest storeRequest) {
        return storeService.createStore(storeRequest);
    }

    @Operation(summary = "Update an existing store", description = "Update store details by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Store updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Store with the given ID does not exist", content = @Content)
    })
    @PutMapping("/{id}")
    public StoreResponse updateStore(
            @Parameter(description = "Store ID", example = "1") @PathVariable Long id,
            @RequestBody @Valid final StoreRequest storeRequest) {
        return storeService.updateStore(id, storeRequest);
    }

    @Operation(summary = "Change store active status", description = "Enable or disable a store by updating its active status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Store with the given ID does not exist", content = @Content)
    })
    @PutMapping("/{id}/active")
    public StoreResponse setActiveStatus(
            @Parameter(description = "Store ID", example = "1") @PathVariable Long id,
            @Parameter(description = "New active status", example = "true") @RequestParam boolean active) {
        return storeService.setActiveStatus(id, active);
    }

    @Operation(summary = "Delete store", description = "Remove a store by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Store deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Store with the given ID does not exist", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStore(@Parameter(description = "Store ID", example = "1") @PathVariable Long id) {
        storeService.deleteStore(id);
    }

    @Operation(summary = "Search stores by multiple criteria",
            description = "All parameters are optional and combined using AND logic")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered list of stores")
    @GetMapping("/search")
    public Page<StoreResponse> searchStoreProducts(
            @Parameter(description = "Store name (partial match)") @RequestParam(required = false) String name,
            @Parameter(description = "Store address (partial match)") @RequestParam(required = false) String address,
            @Parameter(description = "Whether the store is active") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        return storeService.searchStores(name, address, isActive, pageable);
    }
}
