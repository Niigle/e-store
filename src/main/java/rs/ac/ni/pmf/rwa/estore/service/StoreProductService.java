package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
import rs.ac.ni.pmf.rwa.estore.repository.specification.StoreProductSpecifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreProductService {

    private final StoreProductRepository storeProductRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final StoreProductMapper storeProductMapper;

    private static final Logger log = LoggerFactory.getLogger(StoreProductService.class);

    //TODO
    @Cacheable(value = "storeProductsAll", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<StoreProductResponse> getAll(Pageable  pageable) {
        return storeProductRepository.findAll(pageable).map(storeProductMapper::toResponse);
    }

    @Cacheable(value = "storeProductsById", key = "#id")
    public StoreProductResponse getById(Long id) {
        StoreProductEntity storeProductEntity = storeProductRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return storeProductMapper.toResponse(storeProductEntity);
    }

    @Cacheable(value = "storeProductsByStore", key = "#storeId")
    public List<StoreProductResponse> getByStore(Long storeId) {
        return storeProductRepository.findByStoreId(storeId).stream().map(storeProductMapper::toResponse).toList();
    }

    @Cacheable(value = "storeProductsByProduct", key = "#productId")
    public List<StoreProductResponse> getByProduct(Long productId) {

        List<StoreProductResponse> storeProductResponse = storeProductRepository.findByProductId(productId).stream().map(storeProductMapper::toResponse).toList();

        return storeProductResponse;
        //return storeProductRepository.findByProductId(productId).stream().map(storeProductMapper::toResponse).toList();
    }

    @Caching(evict = {
            @CacheEvict(value = "storeProductsAll", allEntries = true),
            @CacheEvict(value = "storeProductsByStore", allEntries = true),
            @CacheEvict(value = "storeProductsByProduct", allEntries = true)
    })
    public StoreProductResponse create(Long storeId, Long productId, StoreProductRequest storeProductRequest) {

        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));


        final StoreProductEntity storeProductEntity = storeProductMapper.toEntity(storeProductRequest);
        storeProductEntity.setProduct(product);
        storeProductEntity.setStore(store);

        StoreProductResponse storeProductResponse = storeProductMapper.toResponse(storeProductRepository.save(storeProductEntity));
        log.info("Store product ti id: {} created", storeProductEntity.getId());

        return storeProductResponse;
    }

    //TODO id iz requesta?
    @Caching(
            put = @CachePut(value = "storeProductsById", key = "#id"),
            evict = {
                    @CacheEvict(value = "storeProductsAll", allEntries = true),
                    @CacheEvict(value = "storeProductsByStore", allEntries = true),
                    @CacheEvict(value = "storeProductsByProduct", allEntries = true)
            }
    )
    public StoreProductResponse updatePriceAndStock(Long id, StoreProductRequest storeProductRequest) {

        StoreProductEntity storeProduct = storeProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StoreProduct with id " + id + " not found"));

        if (storeProductRequest.getName() != null) {
            storeProduct.setName(storeProductRequest.getName());
        }
        if (storeProductRequest.getPrice() != null) {
            storeProduct.setPrice(storeProductRequest.getPrice());
        }
        if (storeProductRequest.getStock() != null) {
            storeProduct.setStock(storeProductRequest.getStock());
        }

        StoreProductResponse storeProductResponse = storeProductMapper.toResponse(storeProductRepository.save(storeProduct));
        log.info("Update price and stock for store's product with id {} , from store {} and is product {}",
                id, storeProduct.getStore().getId(), storeProduct.getProduct().getId());

        return storeProductResponse;
    }

    @Caching(evict = {
            @CacheEvict(value = "storeProductsById", key = "#id"),
            @CacheEvict(value = "storeProductsAll", allEntries = true),
            @CacheEvict(value = "storeProductsByStore", allEntries = true),
            @CacheEvict(value = "storeProductsByProduct", allEntries = true)
    })
    public void delete(Long id) {

        StoreProductEntity storeProduct = storeProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StoreProduct with id " + id + " not found"));

        storeProductRepository.delete(storeProduct);
        log.info("Delete store's product with id {}", id);
    }


    public Page<StoreProductResponse> searchStoreProducts(String name, BigDecimal minPrice, BigDecimal maxPrice,
                                                          Integer minStock, Integer maxStock, Pageable pageable) {
        Specification<StoreProductEntity> spec = Specification
                .where(StoreProductSpecifications.hasName(name))
                .and(StoreProductSpecifications.priceBetween(minPrice, maxPrice))
                .and(StoreProductSpecifications.stockBetween(minStock, maxStock));

        Page<StoreProductEntity> storeProducts = storeProductRepository.findAll(spec, pageable);
        return storeProducts.map(storeProductMapper::toResponse);
    }
}
