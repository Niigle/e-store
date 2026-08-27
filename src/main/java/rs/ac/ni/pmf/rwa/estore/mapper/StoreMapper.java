package rs.ac.ni.pmf.rwa.estore.mapper;

import org.springframework.stereotype.Component;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.StoreRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;

@Component
public class StoreMapper {

    public StoreResponse toResponse(final StoreEntity storeEntity) {

        return StoreResponse.builder()
                .id(storeEntity.getId())
                .name(storeEntity.getName())
                .type(storeEntity.getType())
                .address(storeEntity.getAddress())
                .phone(storeEntity.getPhone())
                .createdAt(storeEntity.getCreatedAt())
                .modifiedOn(storeEntity.getModifiedOn())
                .isActive(storeEntity.getIsActive())
                .build();
    }

    public StoreEntity toEntity(final StoreRequest storeRequest) {

        return StoreEntity.builder()
                .name(storeRequest.getName())
                .type(storeRequest.getType())
                .address(storeRequest.getAddress())
                .phone(storeRequest.getPhone())
                .build();

    }
}
