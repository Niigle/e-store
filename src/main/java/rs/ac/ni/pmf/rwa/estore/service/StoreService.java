package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;import org.springframework.data.domain.Pageable;import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.StoreMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.StoreRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;
import rs.ac.ni.pmf.rwa.estore.repository.StoreRepository;
import rs.ac.ni.pmf.rwa.estore.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoreMapper storeMapper;

    public Page<StoreResponse> getAllStores(Pageable pageable) {
        return storeRepository.findAll(pageable).map(storeMapper::toResponse);
    }

    public List<StoreResponse> getActiveStores() {
        return storeRepository.findByIsActive(1).stream().map(storeMapper::toResponse).toList();
    }

    public StoreResponse getStoreById(Long id) {
        return storeRepository.findById(id).map(storeMapper::toResponse).orElseThrow(()-> new ResourceNotFoundException("Store not found with id: " + id));
    }

    public StoreResponse createStore(StoreEntity storeRequest, Long managerId) {
        UserEntity manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Menager with id " + managerId + " not found"));
        storeRequest.setManagerId(manager);
        return storeMapper.toResponse(storeRepository.save(storeRequest));
    }

    public StoreResponse updateStore(Long id, StoreRequest storeRequest) {
        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store with id " + id + " not found"));

        store.setName(storeRequest.getName());
        store.setAddress(storeRequest.getAddress());
        //store.setCategoryId(storeRequest.getCategoryId());
        store.setPhone(storeRequest.getPhone());
        store.setIsActive(storeRequest.getIsActive());

        return storeMapper.toResponse(storeRepository.save(store));
    }

    public StoreResponse setActiveStatus(Long id, boolean active) {
        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store with id " + id + " not found"));

        store.setIsActive(active ? true : false);
        return storeMapper.toResponse(storeRepository.save(store));
    }

    public void deleteStore(Long id) {

        StoreEntity store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store with id " + id + " not found"));

        storeRepository.delete(store);
    }
}
