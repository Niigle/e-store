package rs.ac.ni.pmf.rwa.estore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.StoreMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.StoreRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.StoreResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.CategoryEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;
import rs.ac.ni.pmf.rwa.estore.repository.CategoryRepository;
import rs.ac.ni.pmf.rwa.estore.repository.StoreRepository;
import rs.ac.ni.pmf.rwa.estore.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest
{

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Spy
    private StoreMapper storeMapper;

    @InjectMocks
    private StoreService storeService;

    @Test
    void getAllStoresReturnsMappedPage()
    {
        final Pageable pageable = PageRequest.of(0, 10);
        final Page<StoreEntity> page = new PageImpl<>(
                List.of(createStoreEntity(1L, "A Store", true), createStoreEntity(2L, "B Store", false)), pageable, 2);
        when(storeRepository.findAll(pageable)).thenReturn(page);

        final Page<StoreResponse> result = storeService.getAllStores(pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting(StoreResponse::getName)
                .containsExactly("A Store", "B Store");
    }

    @Test
    void getActiveStoresReturnsOnlyActive()
    {
        when(storeRepository.findByIsActive(true)).thenReturn(List.of(createStoreEntity(1L, "Active", true)));

        final List<StoreResponse> result = storeService.getActiveStores();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Active");
        assertThat(result.get(0).getIsActive()).isTrue();
    }

    @Test
    void getActiveStoresReturnsEmptyWhenNoActive()
    {
        when(storeRepository.findByIsActive(true)).thenReturn(List.of());

        assertThat(storeService.getActiveStores()).isEmpty();
    }

    @Test
    void getStoreByIdReturnsStoreWhenExists()
    {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(createStoreEntity(1L, "Book Store", true)));

        final StoreResponse result = storeService.getStoreById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Book Store");
        assertThat(result.getAddress()).isEqualTo("Adresa 1");
    }

    @Test
    void getStoreByIdThrowsWhenNotExists()
    {
        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.getStoreById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createStoreSetsManagerAndCategoryAndSaves()
    {
        final UserEntity manager = UserEntity.builder().id(1L).build();
        final CategoryEntity category = CategoryEntity.builder().id(2L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(storeRepository.save(any(StoreEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        final StoreResponse response = storeService.createStore(createStoreRequest(2L, 1L, null));

        final ArgumentCaptor<StoreEntity> captor = ArgumentCaptor.forClass(StoreEntity.class);
        verify(storeRepository).save(captor.capture());
        final StoreEntity savedEntity = captor.getValue();

        assertThat(savedEntity.getManager()).isSameAs(manager);
        assertThat(savedEntity.getCategory()).isSameAs(category);
        assertThat(savedEntity.getName()).isEqualTo("Tech Shop");

        assertThat(response.getName()).isEqualTo("Tech Shop");
        assertThat(response.getAddress()).isEqualTo("Nikole Pasica 1");
        assertThat(response.getPhone()).isEqualTo("018123456");
        assertThat(response.getIsActive()).isTrue();
    }

    @Test
    void createStoreThrowsWhenManagerNotExists()
    {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.createStore(createStoreRequest(2L, 1L, true)))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(storeRepository, never()).save(any());
    }

    @Test
    void createStoreThrowsWhenCategoryNotExists()
    {
        when(userRepository.findById(1L)).thenReturn(Optional.of(UserEntity.builder().id(1L).build()));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.createStore(createStoreRequest(2L, 1L, true)))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(storeRepository, never()).save(any());
    }

    @Test
    void updateStoreUpdatesFieldsAndCategory()
    {
        final StoreEntity storeEntity = createStoreEntity(1L, "Old Name", true);
        final CategoryEntity category = CategoryEntity.builder().id(2L).build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(storeEntity));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(storeRepository.save(storeEntity)).thenReturn(storeEntity);

        final StoreResponse response = storeService.updateStore(1L, createStoreRequest(2L, 1L, false));

        assertThat(storeEntity.getName()).isEqualTo("Tech Shop");
        assertThat(storeEntity.getAddress()).isEqualTo("Nikole Pasica 1");
        assertThat(storeEntity.getPhone()).isEqualTo("018123456");
        assertThat(storeEntity.getCategory()).isSameAs(category);
        assertThat(storeEntity.getIsActive()).isFalse();

        assertThat(response.getName()).isEqualTo("Tech Shop");
        assertThat(response.getIsActive()).isFalse();
    }

    @Test
    void updateStoreKeepsIsActiveWhenNull()
    {
        final StoreEntity storeEntity = createStoreEntity(1L, "Old Name", true);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(storeEntity));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(CategoryEntity.builder().id(2L).build()));
        when(storeRepository.save(storeEntity)).thenReturn(storeEntity);

        storeService.updateStore(1L, createStoreRequest(2L, 1L, null));

        assertThat(storeEntity.getIsActive()).isTrue();
    }

    @Test
    void updateStoreThrowsWhenStoreNotExists()
    {
        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.updateStore(99L, createStoreRequest(2L, 1L, true)))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(storeRepository, never()).save(any());
    }

    @Test
    void updateStoreThrowsWhenCategoryNotExists()
    {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(createStoreEntity(1L, "Old", true)));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.updateStore(1L, createStoreRequest(2L, 1L, true)))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(storeRepository, never()).save(any());
    }

    @Test
    void setActiveStatusUpdatesFlag()
    {
        final StoreEntity storeEntity = createStoreEntity(1L, "Toggle", true);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(storeEntity));
        when(storeRepository.save(storeEntity)).thenReturn(storeEntity);

        final StoreResponse response = storeService.setActiveStatus(1L, false);

        assertThat(storeEntity.getIsActive()).isFalse();
        assertThat(response.getIsActive()).isFalse();
    }

    @Test
    void setActiveStatusThrowsWhenNotExists()
    {
        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.setActiveStatus(99L, true))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(storeRepository, never()).save(any());
    }

    @Test
    void deleteStoreDeletesWhenExists()
    {
        final StoreEntity storeEntity = createStoreEntity(1L, "To Delete", true);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(storeEntity));

        storeService.deleteStore(1L);

        verify(storeRepository).delete(storeEntity);
    }

    @Test
    void deleteStoreThrowsWhenNotExists()
    {
        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.deleteStore(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(storeRepository, never()).delete(any(StoreEntity.class));
    }

    private StoreEntity createStoreEntity(final Long id, final String name, final boolean active)
    {
        return StoreEntity.builder()
                .id(id)
                .name(name)
                .address("Adresa " + id)
                .phone("0" + id)
                .isActive(active)
                .build();
    }

    private StoreRequest createStoreRequest(final Long categoryId, final Long managerId, final Boolean active)
    {
        return StoreRequest.builder()
                .name("Tech Shop")
                .address("Nikole Pasica 1")
                .phone("018123456")
                .categoryId(categoryId)
                .managerId(managerId)
                .isActive(active)
                .build();
    }
}