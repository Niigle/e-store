package rs.ac.ni.pmf.rwa.estore.controller;

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
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public Page<StoreResponse> getAllStores(@PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        return storeService.getAllStores(pageable);
    }

    @GetMapping("/active")
    public List<StoreResponse> getActiveStores() {
        return storeService.getActiveStores();
    }

    @GetMapping("/{id}")
    public StoreResponse getStoreById(@PathVariable Long id) {
        return storeService.getStoreById(id);
    }

    //TODO storeRequest
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse createStore(@RequestBody @Valid StoreRequest storeRequest
            /*@RequestBody StoreEntity store,
            @RequestParam Long managerId*/) {
        return storeService.createStore(storeRequest);
    }

    //TODO
    @PutMapping("/{id}")
    public StoreResponse updateStore(@PathVariable Long id, @RequestBody @Valid final StoreRequest storeRequest) {

        return storeService.updateStore(id, storeRequest);
    }

    @PutMapping("/{id}/active")
    public StoreResponse setActiveStatus(@PathVariable Long id, @RequestParam boolean active) {

        return storeService.setActiveStatus(id, active);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStore(@PathVariable Long id) {

        storeService.deleteStore(id);
    }

    @GetMapping("/search")
    public Page<StoreResponse> searchStoreProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        return storeService.searchStores(name, address, isActive, pageable);
    }

}
