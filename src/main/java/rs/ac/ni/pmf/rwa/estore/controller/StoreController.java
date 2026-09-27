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
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse createStore(
            @RequestBody StoreEntity store,
            @RequestParam Long managerId) {
        return storeService.createStore(store, managerId);
    }

    //TODO
    @PutMapping("/{id}")
    public StoreResponse updateStore(@PathVariable Long id, @RequestBody @Valid final StoreRequest storeRequest) {
        try {
            return storeService.updateStore(id, storeRequest);
        } catch (RuntimeException e) {
            return null;//ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/active")
    public StoreResponse setActiveStatus(@PathVariable Long id, @RequestParam boolean active) {
        try {
            return storeService.setActiveStatus(id, active);
        } catch (RuntimeException e) {
            return null;//ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStore(@PathVariable Long id) {

        storeService.deleteStore(id);
    }

}
