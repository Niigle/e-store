package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.StoreMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.StoreRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreProductResponse;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.CategoryEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreProductEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;
import rs.ac.ni.pmf.rwa.estore.repository.CategoryRepository;
import rs.ac.ni.pmf.rwa.estore.repository.StoreRepository;
import rs.ac.ni.pmf.rwa.estore.repository.UserRepository;
import rs.ac.ni.pmf.rwa.estore.repository.specification.StoreProductSpecifications;
import rs.ac.ni.pmf.rwa.estore.repository.specification.StoreSpecifications;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoreMapper storeMapper;
    private final CategoryRepository categoryRepository;

    public Page<StoreResponse> getAllStores(Pageable pageable) {
        return storeRepository.findAll(pageable).map(storeMapper::toResponse);
    }

    public List<StoreResponse> getActiveStores() {
        return storeRepository.findByIsActive(true).stream().map(storeMapper::toResponse).toList();
    }

    public StoreResponse getStoreById(Long id) {
        return storeRepository.findById(id).map(storeMapper::toResponse).orElseThrow(()-> new ResourceNotFoundException("Store not found with id: " + id));
    }

    /*public StoreResponse createStore(StoreEntity storeRequest, Long managerId) {
        UserEntity manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Menager with id " + managerId + " not found"));
        storeRequest.setManagerId(manager);
        return storeMapper.toResponse(storeRepository.save(storeRequest));
    }*/

    public StoreResponse createStore(StoreRequest request) {

        assert request.getManagerId() != null;
        UserEntity manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager with id " + request.getManagerId() + " not found"));

        assert request.getCategoryId() != null;
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + request.getCategoryId() + " not found"));

        StoreEntity store = storeMapper.toEntity(request);
        store.setManager(manager);
        store.setCategory(category);

        StoreResponse storeResponse = storeMapper.toResponse(storeRepository.save(store));

        log.info("Store with id: {}created and name {}", store.getId(), store.getName());
        return storeResponse;
    }

    /*public StoreResponse updateStore(Long id, StoreRequest storeRequest) {
        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store with id " + id + " not found"));

        store.setName(storeRequest.getName());
        store.setAddress(storeRequest.getAddress());
        //store.setCategoryId(storeRequest.getCategoryId());
        store.setPhone(storeRequest.getPhone());
        store.setIsActive(storeRequest.getIsActive());

        return storeMapper.toResponse(storeRepository.save(store));
    }*/

    public StoreResponse updateStore(Long id, StoreRequest request) {

        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store with id " + id + " not found"));

        assert request.getCategoryId() != null;
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + request.getCategoryId() + " not found"));

        store.setName(request.getName());
        store.setAddress(request.getAddress());
        store.setPhone(request.getPhone());
        store.setCategory(category);

        if (request.getIsActive() != null) store.setIsActive(request.getIsActive());

        StoreResponse storeResponse = storeMapper.toResponse(storeRepository.save(store));

        log.info("Store with id: {}updated and name {}", store.getId(), store.getName());
        return storeResponse;
    }

    public StoreResponse setActiveStatus(Long id, boolean active) {
        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store with id " + id + " not found"));

        store.setIsActive(active ? true : false);

        StoreResponse storeResponse = storeMapper.toResponse(storeRepository.save(store));

        log.info("Store with id: {} and name {} activated", store.getId(), store.getName());
        return storeResponse;
    }

    public void deleteStore(Long id) {

        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store with id " + id + " not found"));

        storeRepository.delete(store);

        log.info("Store with id: {} and name {} deleted", store.getId(), store.getName());
    }

    public Page<StoreResponse> searchStores(String name, String address, Boolean isActive, Pageable pageable) {

        Specification<StoreEntity> spec = Specification
                .where(StoreSpecifications.hasName(name))
                .and(StoreSpecifications.hasAddress(address))
                .and(StoreSpecifications.hasActiveStatus(isActive));

        Page<StoreEntity> stores = storeRepository.findAll(spec, pageable);
        return stores.map(storeMapper::toResponse);
    }
}
