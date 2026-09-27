package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;import org.springframework.data.domain.Pageable;import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.StoreProductMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.StoreProductRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreProductResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreProductEntity;
import rs.ac.ni.pmf.rwa.estore.repository.ProductRepository;
import rs.ac.ni.pmf.rwa.estore.repository.StoreProductRepository;
import rs.ac.ni.pmf.rwa.estore.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreProductService {

    private final StoreProductRepository storeProductRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final StoreProductMapper storeProductMapper;

    public Page<StoreProductResponse> getAll(Pageable  pageable) {
        return storeProductRepository.findAll(pageable).map(storeProductMapper::toResponse);
    }

    public StoreProductResponse getById(Long id) {
        StoreProductEntity storeProductEntity = storeProductRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return storeProductMapper.toResponse(storeProductEntity);
    }

    public List<StoreProductResponse> getByStore(Long storeId) {
        return storeProductRepository.findByStoreId(storeId).stream().map(storeProductMapper::toResponse).toList();
    }

    public List<StoreProductResponse> getByProduct(Long productId) {

        List<StoreProductResponse> storeProductResponse = storeProductRepository.findByProductId(productId).stream().map(storeProductMapper::toResponse).toList();

        return storeProductResponse;
        //return storeProductRepository.findByProductId(productId).stream().map(storeProductMapper::toResponse).toList();
    }
/*
    public StoreProductResponse create(StoreProductRequest storeProductRequest) {

        StoreEntity store = storeRepository.findById(storeProductRequest.getStore().getId())
                .orElseThrow(() -> new RuntimeException("Store with id " + storeProductRequest.getStore().getId() + " not found"));
        ProductEntity product = productRepository.findById(storeProductRequest.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product with id " + storeProductRequest.getProduct().getId() + " not found"));


        final StoreProductEntity storeProductEntity = storeProductMapper.toEntity(storeProductRequest);
        return storeProductMapper.toResponse(storeProductRepository.save(storeProductEntity));
    }*/

    //TODO id iz requesta?
    public StoreProductResponse updatePriceAndStock(StoreProductRequest storeProductRequest) {
        StoreProductEntity storeProduct = storeProductRepository.findById(storeProductRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("StoreProduct with id " + storeProductRequest.getId() + " not found"));

        storeProduct.setPrice(storeProductRequest.getPrice());
        storeProduct.setStock(storeProductRequest.getStock());
        return storeProductMapper.toResponse(storeProductRepository.save(storeProduct));
    }

    public void delete(Long id) {
        StoreProductEntity storeProduct = storeProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StoreProduct with id " + id + " not found"));
        storeProductRepository.delete(storeProduct);
    }
}
