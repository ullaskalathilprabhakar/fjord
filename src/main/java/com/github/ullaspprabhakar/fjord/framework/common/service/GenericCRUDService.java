package com.github.ullaspprabhakar.fjord.framework.common.service;

import java.util.List;
import java.util.Optional;

public interface GenericCRUDService<DTO, ID> {

    List<DTO> getAll();

    Optional<DTO> getById(ID id);

    DTO save(DTO dto);

    DTO update(ID id, DTO dto);

    void delete(ID id);
}
