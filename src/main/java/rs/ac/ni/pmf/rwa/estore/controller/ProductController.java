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
import rs.ac.ni.pmf.rwa.estore.model.dto.request.ProductRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.ProductResponse;
import rs.ac.ni.pmf.rwa.estore.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Paginated and sorted list of all products")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products")
    @GetMapping
    public Page<ProductResponse> getAllProducts(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        return productService.getAllProducts(pageable);
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a single product by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product with the given ID does not exist", content = @Content)
    })
    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id) {

        return productService.getProductById(id);
    }

    @Operation(summary = "Get product by barcode", description = "Retrieve a single product by its unique barcode")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product with the given barcode does not exist", content = @Content)
    })
    @GetMapping("/barcode/{barcode}")
    public ProductResponse getProductByBarcode(
            @Parameter(description = "Product barcode", example = "8600123456789") @PathVariable String barcode) {

        return productService.getProductByBarcode(barcode);
    }

    @Operation(summary = "Create a new product", description = "Create a product with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category or associated resource does not exist", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest productRequest) {

        return productService.createProduct(productRequest);
    }

    @Operation(summary = "Update an existing product", description = "Update product details by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product with the given ID does not exist", content = @Content)
    })
    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest) {

        return productService.updateProduct(id, productRequest);
    }

    @Operation(summary = "Delete product", description = "Remove a product by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product with the given ID does not exist", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id) {

        productService.deleteProduct(id);
    }

    @Operation(summary = "Search products by multiple criteria",
            description = "All parameters are optional and combined using AND logic")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered list of products")
    @GetMapping("/search")
    public Page<ProductResponse> searchProducts(
            @Parameter(description = "Product name (partial match)") @RequestParam(required = false) String name,
            @Parameter(description = "Product type (partial match)") @RequestParam(required = false) String type,
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        return productService.searchProducts(name, type, pageable);
    }
}