package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.ProductMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.ProductRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.ProductResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;
import rs.ac.ni.pmf.rwa.estore.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    //TODO zameni entity
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll().stream().map(productMapper::toResponse).toList();
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

    public ProductResponse updateProduct(ProductRequest  productRequest) {

        ProductEntity existing = productRepository.findById(productRequest.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productRequest.getId()));

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

}
