package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.ProductMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.ProductRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.ProductResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;
import rs.ac.ni.pmf.rwa.estore.repository.ProductRepository;
import rs.ac.ni.pmf.rwa.estore.repository.specification.ProductSpecifications;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable).map(productMapper::toResponse);
    }

    public ProductResponse getProductById(Long id) {

        final ProductEntity product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id: " + id));

        return productMapper.toResponse(product);
    }

    public ProductResponse getProductByBarcode(String barcode) {

        final ProductEntity product = productRepository.findByBarcode(barcode).orElseThrow(()->new ResourceNotFoundException("Product not found with barcode: " + barcode));

        return productMapper.toResponse(product);
    }

    public ProductResponse createProduct(ProductRequest product) {

        final ProductEntity productEntity = productMapper.toEntity(product);

        return productMapper.toResponse(productRepository.save(productEntity));
    }

    public ProductResponse updateProduct(Long id, ProductRequest  productRequest) {

        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existing.setName(productRequest.getName());
        existing.setType(productRequest.getType());
        existing.setDescription(productRequest.getDescription());
        existing.setBarcode(productRequest.getBarcode());

        return productMapper.toResponse(productRepository.save(existing));
    }

    public void deleteProduct(Long id) {

        final ProductEntity existing = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(existing);
    }

    public Page<ProductResponse> searchProducts(String name, String type, Pageable pageable) {
        Specification<ProductEntity> spec = Specification
                .where(ProductSpecifications.hasName(name))
                .and(ProductSpecifications.hasType(type));

        Page<ProductEntity> products = productRepository.findAll(spec, pageable);
        return products.map(productMapper::toResponse);
    }

}
