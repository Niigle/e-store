package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;
import rs.ac.ni.pmf.rwa.estore.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<ProductEntity> getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    public ProductEntity updateProduct(Long id, ProductEntity productDetails) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proizvod sa id " + id + " nije pronađen"));

        product.setName(productDetails.getName());
        product.setType(productDetails.getType());
        product.setDescription(productDetails.getDescription());
        product.setBarcode(productDetails.getBarcode());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proizvod sa id " + id + " nije pronađen"));
        productRepository.delete(product);
    }

}
