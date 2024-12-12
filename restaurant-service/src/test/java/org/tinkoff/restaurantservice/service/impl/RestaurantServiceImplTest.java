package org.tinkoff.restaurantservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.restaurantservice.dto.RestaurantDto;
import org.tinkoff.restaurantservice.dto.mapper.RestaurantMapper;
import org.tinkoff.restaurantservice.entity.Restaurant;
import org.tinkoff.restaurantservice.repository.RestaurantRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getList_returnsAllRestaurants() {
        Restaurant restaurant1 = new Restaurant();
        Restaurant restaurant2 = new Restaurant();
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));
        when(restaurantMapper.toDto(restaurant1)).thenReturn(new RestaurantDto());
        when(restaurantMapper.toDto(restaurant2)).thenReturn(new RestaurantDto());

        List<RestaurantDto> result = restaurantService.getList();

        assertThat(result).hasSize(2);
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void getOne_returnsRestaurantIfFound() {
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toDto(restaurant)).thenReturn(new RestaurantDto());

        RestaurantDto result = restaurantService.getOne(1L);

        assertThat(result).isNotNull();
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void getOne_throwsExceptionIfNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.getOne(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Entity with id `1` not found");
    }

    @Test
    void patch_updatesRestaurantIfFound() throws IOException {
        Restaurant restaurant = new Restaurant();
        RestaurantDto restaurantDto = new RestaurantDto();
        JsonNode patchNode = mock(JsonNode.class);
        ObjectReader objectReader = mock(ObjectReader.class);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantDto);
        when(objectMapper.readerForUpdating(restaurantDto)).thenReturn(objectReader);
        when(objectReader.readValue(patchNode)).thenReturn(restaurantDto);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantDto);

        RestaurantDto result = restaurantService.patch(1L, patchNode);

        assertThat(result).isEqualTo(restaurantDto);
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    void patch_throwsExceptionIfNotFound() {
        JsonNode patchNode = mock(JsonNode.class);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.patch(1L, patchNode))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Entity with id `1` not found");
    }

    @Test
    void patchMany_updatesMultipleRestaurants() throws IOException {
        Restaurant restaurant1 = mock(Restaurant.class);
        Restaurant restaurant2 = mock(Restaurant.class);
        RestaurantDto restaurantDto1 = new RestaurantDto();
        RestaurantDto restaurantDto2 = new RestaurantDto();
        JsonNode patchNode = mock(JsonNode.class);
        ObjectReader objectReader1 = mock(ObjectReader.class);
        ObjectReader objectReader2 = mock(ObjectReader.class);

        when(restaurantRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(restaurant1, restaurant2));
        when(restaurantMapper.toDto(restaurant1)).thenReturn(restaurantDto1);
        when(restaurantMapper.toDto(restaurant2)).thenReturn(restaurantDto2);
        when(objectMapper.readerForUpdating(restaurantDto1)).thenReturn(objectReader1);
        when(objectMapper.readerForUpdating(restaurantDto2)).thenReturn(objectReader2);
        when(objectReader1.readValue(patchNode)).thenReturn(restaurantDto1);
        when(objectReader2.readValue(patchNode)).thenReturn(restaurantDto2);
        when(restaurantRepository.saveAll(List.of(restaurant1, restaurant2))).thenReturn(List.of(restaurant1, restaurant2));
        when(restaurant1.getId()).thenReturn(1L);
        when(restaurant2.getId()).thenReturn(2L);

        List<Long> result = restaurantService.patchMany(List.of(1L, 2L), patchNode);

        assertThat(result).containsExactlyInAnyOrder(1L, 2L);
        verify(restaurantRepository, times(1)).saveAll(List.of(restaurant1, restaurant2));
    }

    @Test
    void deleteMany_deletesMultipleRestaurants() {
        List<Long> ids = List.of(1L, 2L);

        restaurantService.deleteMany(ids);

        verify(restaurantRepository, times(1)).deleteAllById(ids);
    }
}
