package rs.ac.ni.pmf.rwa.estore.controller;

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
    public List<ProductResponse> getAllProducts() {

        return productService.getAllProducts();
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
    public ProductResponse createProduct(@RequestBody ProductRequest  productRequest) {

        return productService.createProduct(productRequest);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@RequestBody ProductRequest  productRequest) {

        return productService.updateProduct(productRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
    }

}
