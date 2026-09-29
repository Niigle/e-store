package rs.ac.ni.pmf.rwa.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.StoreProductRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreProductResponse;
import rs.ac.ni.pmf.rwa.estore.service.StoreProductService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/v1/store-products")
@RequiredArgsConstructor
@Tag(name = "Store products", description = "Pregled i upravljanje proizvodima u prodavnicama (cena i zalihe)")
public class StoreProductController {

    private final StoreProductService storeProductService;

    @GetMapping
    @Operation(summary = "Get all store products", description = "Returns a page of all products per store. Default: page=0, size=10, sort=name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized user (missing or bad JWT token)", content = @Content)
    })
    public Page<StoreProductResponse> getAll(@PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        return storeProductService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get store product by ID", description = "Returns one store product for id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store product found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized user", content = @Content),
            @ApiResponse(responseCode = "404", description = "Store product for provided id not found", content = @Content)
    })
    public StoreProductResponse getById(@PathVariable Long id) {
        return storeProductService.getById(id);
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get products by store", description = "Returns all product for provided store.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized user", content = @Content),
            @ApiResponse(responseCode = "404", description = "Store not found", content = @Content)
    })
    public List<StoreProductResponse> getByStore(@PathVariable Long storeId) {
        return storeProductService.getByStore(storeId);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get store products by product", description = "Returns all products from all store for provided product id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized user", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public List<StoreProductResponse> getByProduct(@PathVariable Long productId) {
        return storeProductService.getByProduct(productId);
    }

    @PostMapping("/stores/{storeId}/products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public StoreProductResponse create(@PathVariable Long storeId, @PathVariable Long productId, @RequestBody @Valid StoreProductRequest storeProductRequest) {

        return storeProductService.create(storeId, productId, storeProductRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update price and stock", description = "Updates price and stock for product in a store.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update successful"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found for provided id", content = @Content)
    })
    public StoreProductResponse updatePriceAndStock(@PathVariable final Long id, @RequestBody @Valid StoreProductRequest storeProductRequest) {

        return storeProductService.updatePriceAndStock(id, storeProductRequest);
    }

    @Operation(summary = "Delete store product", description = "Removes products from the store for provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found for provided id", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        storeProductService.delete(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search store products", description = "Search store product by name, price range or stock range. Filters are optional in any combination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search successful"),
            @ApiResponse(responseCode = "400", description = "Bad search criteria", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user", content = @Content)
    })
    public Page<StoreProductResponse> searchStoreProducts(
            @Parameter(description = "Part of a product name", example = "milk")
            @RequestParam(required = false) String name,
            @Parameter(description = "Minimum price", example = "100.00")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price", example = "500.00")
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Minimal amount at stock", example = "1")
            @RequestParam(required = false) Integer minStock,
            @Parameter(description = "Maximum amount at stock", example = "1")
            @RequestParam(required = false) Integer maxStock,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        return storeProductService.searchStoreProducts(name, minPrice, maxPrice, minStock, maxStock, pageable);
    }
}
