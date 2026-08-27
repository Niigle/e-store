package rs.ac.ni.pmf.rwa.estore.mapper;

import org.springframework.stereotype.Component;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.StoreProductRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreProductResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreProductEntity;

@Component
public class StoreProductMapper {

    public StoreProductResponse toResponse(final StoreProductEntity storeProductEntity) {

        return StoreProductResponse.builder()
                .id(storeProductEntity.getId())
                .name(storeProductEntity.getName())
                .price(storeProductEntity.getPrice())
                .stock(storeProductEntity.getStock())
                .build();
    }

    public StoreProductEntity toEntity(final StoreProductRequest storeProductRequest) {

        return StoreProductEntity.builder()
                .name(storeProductRequest.getName())
                .price(storeProductRequest.getPrice())
                .stock(storeProductRequest.getStock())
                .build();

    }

}
