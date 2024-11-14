package org.tinkoff.orderservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.orderservice.dto.OrderDto;
import org.tinkoff.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/rest/orders")
@RequiredArgsConstructor
public class OrderResource {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getList() {
        return orderService.getList();
    }

    @GetMapping("/{id}")
    public OrderDto getOne(@PathVariable Integer id) {
        return orderService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<OrderDto> getMany(@RequestParam List<Integer> ids) {
        return orderService.getMany(ids);
    }

    @PostMapping
    public OrderDto create(@RequestBody OrderDto dto) {
        return orderService.create(dto);
    }

    @PatchMapping("/{id}")
    public OrderDto patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) {
        return orderService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) {
        return orderService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public OrderDto delete(@PathVariable Integer id) {
        return orderService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        orderService.deleteMany(ids);
    }
}
