package rs.ac.ni.pmf.rwa.estore.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.ProductRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.ProductResponse;
import rs.ac.ni.pmf.rwa.estore.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductResponse> getAllProducts(@PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {

        return productService.getProductById(id);
    }

    @GetMapping("/barcode/{barcode}")
    public ProductResponse getProductByBarcode(@PathVariable String barcode) {

        return productService.getProductByBarcode(barcode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest  productRequest) {

        return productService.createProduct(productRequest);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest  productRequest) {

        return productService.updateProduct(id, productRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
    }

    @GetMapping("/search")
    public Page<ProductResponse> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        return productService.searchProducts(name, type, pageable);
    }
}
