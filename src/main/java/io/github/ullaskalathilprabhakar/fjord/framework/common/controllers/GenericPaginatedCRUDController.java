package io.github.ullaskalathilprabhakar.fjord.framework.common.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.github.ullaskalathilprabhakar.fjord.framework.common.service.GenericPaginatedCRUDService;
import jakarta.validation.Valid;

public abstract class GenericPaginatedCRUDController<DTO, ID,NUM, SIZE> {

	private static final Logger logger = LoggerFactory.getLogger(GenericPaginatedCRUDController.class);

    public abstract GenericPaginatedCRUDService<DTO, ID ,NUM, SIZE>  getService();

    public GenericPaginatedCRUDController() {
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
    public ResponseEntity<DTO> save(@Valid @RequestBody DTO dto) {
        DTO savedDTO = getService().save(dto);
        logger.info("Saved {} DTO: {}", dto.getClass().getSimpleName(), savedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTO> update(@PathVariable ID id, @Valid @RequestBody DTO dto) {
        DTO updatedDTO = getService().update(id, dto);
        logger.info("Updated {} DTO: {}", dto.getClass().getSimpleName(), updatedDTO);
        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
    	getService().delete(id);
    	logger.info("Deleted  DTO: {}",  id);
        return ResponseEntity.noContent().build();
    }
}
