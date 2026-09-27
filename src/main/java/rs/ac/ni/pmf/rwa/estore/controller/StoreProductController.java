package rs.ac.ni.pmf.rwa.estore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.StoreProductRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreProductResponse;
import rs.ac.ni.pmf.rwa.estore.service.StoreProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/store-products")
@RequiredArgsConstructor
public class StoreProductController {

    private final StoreProductService storeProductService;

    @GetMapping
    public Page<StoreProductResponse> getAll(@PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        return storeProductService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public StoreProductResponse getById(@PathVariable Long id) {
        return storeProductService.getById(id);
    }

    @GetMapping("/store/{storeId}")
    public List<StoreProductResponse> getByStore(@PathVariable Long storeId) {
        return storeProductService.getByStore(storeId);
    }

    @GetMapping("/product/{productId}")
    public List<StoreProductResponse> getByProduct(@PathVariable Long productId) {
        return storeProductService.getByProduct(productId);
    }
/*
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreProductResponse create(@RequestBody StoreProductRequest storeProductRequest) {

        return storeProductService.create(storeProductRequest);
    }*/

    @PutMapping("/{id}")
    public StoreProductResponse updatePriceAndStock(@RequestBody StoreProductRequest storeProductRequest) {
        return storeProductService.updatePriceAndStock(storeProductRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        storeProductService.delete(id);
    }
}
