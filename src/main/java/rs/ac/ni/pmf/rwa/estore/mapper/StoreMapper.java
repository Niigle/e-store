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
                //.categoryId(storeEntity.getCategoryId())
                .address(storeEntity.getAddress())
                .phone(storeEntity.getPhone())
                .createdOn(storeEntity.getCreatedOn())
                .modifiedOn(storeEntity.getModifiedOn())
                .isActive(storeEntity.getIsActive())
                .build();
    }

    public StoreEntity toEntity(final StoreRequest storeRequest) {

        return StoreEntity.builder()
                .name(storeRequest.getName())
                //.categoryId(storeRequest.getCategoryId())
                .address(storeRequest.getAddress())
                .phone(storeRequest.getPhone())
                .build();

    }
}
