package org.tinkoff.restaurantservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import org.tinkoff.restaurantservice.dto.DishDto;
import org.tinkoff.restaurantservice.dto.mapper.DishMapper;
import org.tinkoff.restaurantservice.entity.Dish;
import org.tinkoff.restaurantservice.repository.DishRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DishServiceImplTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishMapper dishMapper;

    @InjectMocks
    private DishServiceImpl dishService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getList_returnsAllDishes() {
        Dish dish1 = new Dish();
        Dish dish2 = new Dish();
        when(dishRepository.findAll()).thenReturn(List.of(dish1, dish2));
        when(dishMapper.toDto(dish1)).thenReturn(new DishDto());
        when(dishMapper.toDto(dish2)).thenReturn(new DishDto());

        List<DishDto> result = dishService.getList();

        assertThat(result).hasSize(2);
        verify(dishRepository, times(1)).findAll();
    }

    @Test
    void getOne_returnsDishIfFound() {
        Dish dish = new Dish();
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishMapper.toDto(dish)).thenReturn(new DishDto());

        DishDto result = dishService.getOne(1L);

        assertThat(result).isNotNull();
        verify(dishRepository, times(1)).findById(1L);
    }

    @Test
    void getOne_throwsExceptionIfNotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dishService.getOne(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Entity with id `1` not found");
    }

    @Test
    void create_savesAndReturnsDish() {
        DishDto dishDto = new DishDto();
        Dish dish = new Dish();
        when(dishMapper.toEntity(dishDto)).thenReturn(dish);
        when(dishRepository.save(dish)).thenReturn(dish);
        when(dishMapper.toDto(dish)).thenReturn(dishDto);

        DishDto result = dishService.create(dishDto);

        assertThat(result).isEqualTo(dishDto);
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void delete_deletesDishIfFound() {
        Dish dish = new Dish();
        DishDto dishDto = new DishDto();

        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishMapper.toDto(dish)).thenReturn(dishDto);

        DishDto result = dishService.delete(1L);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(dishDto);
        verify(dishRepository, times(1)).delete(dish);
    }

    @Test
    void delete_returnsNullIfNotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        DishDto result = dishService.delete(1L);

        assertThat(result).isNull();
        verify(dishRepository, never()).delete(any(Dish.class));
    }

    @Test
    void getManyByRestaurantId_returnsDishes() {
        Dish dish1 = new Dish();
        Dish dish2 = new Dish();
        when(dishRepository.findAllByRestaurantId(1L)).thenReturn(List.of(dish1, dish2));
        when(dishMapper.toDto(dish1)).thenReturn(new DishDto());
        when(dishMapper.toDto(dish2)).thenReturn(new DishDto());

        List<DishDto> result = dishService.getManyByRestaurantId(1L);

        assertThat(result).hasSize(2);
        verify(dishRepository, times(1)).findAllByRestaurantId(1L);
    }
}