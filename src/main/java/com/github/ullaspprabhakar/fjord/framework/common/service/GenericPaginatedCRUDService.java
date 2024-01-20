package com.github.ullaspprabhakar.fjord.framework.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface GenericPaginatedCRUDService<DTO, ID,NUM ,SIZE> {

    List<DTO> getAll();

    Page<DTO> getAll(NUM page,  SIZE size );

    Page<DTO> getAll(NUM page,  SIZE size ,FilterMarker filter);

    Slice<DTO> getAllSlice(NUM page,  SIZE size );

    Slice<DTO> getAllSlice(NUM page,  SIZE size ,FilterMarker filter);

    Optional<DTO> getById(ID id);

    DTO save(DTO dto);

    DTO update(ID id, DTO dto);

    void delete(ID id);
}
