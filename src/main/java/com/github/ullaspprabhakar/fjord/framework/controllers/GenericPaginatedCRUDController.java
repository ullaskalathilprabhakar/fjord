package com.github.ullaspprabhakar.fjord.framework.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.ullaspprabhakar.fjord.framework.common.service.GenericPaginatedCRUDService;

public abstract class GenericPaginatedCRUDController<DTO, ID,NUM, SIZE> {



    public abstract GenericPaginatedCRUDService<DTO, ID ,NUM, SIZE>  getService();

    public GenericPaginatedCRUDController(GenericPaginatedCRUDService<DTO, ID ,NUM, SIZE> crudService) {
    	 super();
    }

    @GetMapping
    public List<DTO> getAll() {
        return getService().getAll();
    }

    @GetMapping("/pages/{num}/size/{size}")
    public Page<DTO> getAllPages(@PathVariable NUM num,@PathVariable SIZE size) {
        return getService().getAll(num, size);
    }

    @GetMapping("/slices/{num}/size/{size}")
    public Slice<DTO> getAllSlices(@PathVariable NUM num,@PathVariable SIZE size) {
        return getService().getAllSlice(num, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTO> getById(@PathVariable ID id) {
        Optional<DTO> dto = getService().getById(id);
        return dto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DTO> save(@RequestBody DTO dto) {
        DTO savedDTO = getService().save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTO> update(@PathVariable ID id, @RequestBody DTO dto) {
        DTO updatedDTO = getService().update(id, dto);
        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
    	getService().delete(id);
        return ResponseEntity.noContent().build();
    }
}
