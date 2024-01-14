package com.github.ullaspprabhakar.fjord.framework.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.ullaspprabhakar.fjord.framework.common.service.GenericCRUDService;

import java.util.List;
import java.util.Optional;

public abstract class GenericCRUDController<DTO, ID> {

    private final GenericCRUDService<DTO, ID> crudService;

    public GenericCRUDController(GenericCRUDService<DTO, ID> crudService) {
        this.crudService = crudService;
    }

    @GetMapping
    public List<DTO> getAll() {
        return crudService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTO> getById(@PathVariable ID id) {
        Optional<DTO> dto = crudService.getById(id);
        return dto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DTO> save(@RequestBody DTO dto) {
        DTO savedDTO = crudService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTO> update(@PathVariable ID id, @RequestBody DTO dto) {
        DTO updatedDTO = crudService.update(id, dto);
        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        crudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
