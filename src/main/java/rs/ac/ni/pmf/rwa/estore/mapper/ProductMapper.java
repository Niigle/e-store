package rs.ac.ni.pmf.rwa.estore.mapper;

import org.springframework.stereotype.Component;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.ProductRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.ProductResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;

@Component
public class ProductMapper {

    public ProductResponse toResponse(final ProductEntity  productEntity) {

        return ProductResponse.builder()
                .id(productEntity.getId())
                .type(productEntity.getType())
                .name(productEntity.getName())
                .barcode(productEntity.getBarcode())
                .description(productEntity.getDescription())
                .build();
    }

    public ProductEntity toEntity(final ProductRequest productRequest) {

        return ProductEntity.builder()
                .id(productRequest.getId())
                .type(productRequest.getType())
                .name(productRequest.getName())
                .barcode(productRequest.getBarcode())
                .description(productRequest.getDescription())
                .build();
    }

}
