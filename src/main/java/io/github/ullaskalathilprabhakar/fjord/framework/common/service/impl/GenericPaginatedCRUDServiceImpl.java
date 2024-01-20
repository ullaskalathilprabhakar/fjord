package io.github.ullaskalathilprabhakar.fjord.framework.common.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.ullaskalathilprabhakar.fjord.framework.common.service.FilterMarker;
import io.github.ullaskalathilprabhakar.fjord.framework.common.service.GenericPaginatedCRUDService;

public abstract class GenericPaginatedCRUDServiceImpl<ENTITY, DTO, ID,NUM,SIZE> implements GenericPaginatedCRUDService<DTO, ID,NUM,SIZE> {

    @Autowired
    private JpaRepository<ENTITY, ID> repository;

    @Autowired
    private ModelMapper modelMapper;

    private final Class<DTO> dtoClass;

    @SuppressWarnings("unchecked")
    public GenericPaginatedCRUDServiceImpl() {
        this.dtoClass = (Class<DTO>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
        this.modelMapper= new ModelMapper();

    }

    @Override
    public List<DTO> getAll() {
        List<ENTITY> entities = repository.findAll();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DTO> getById(ID id) {
        return repository.findById(id).map(this::convertToDTO);
    }

    @Override
    public DTO save(DTO dto) {
        ENTITY entity = convertToEntity(dto);
        ENTITY savedEntity = repository.save(entity);
        return convertToDTO(savedEntity);
    }

    @Override
    public DTO update(ID id, DTO dto) {
        if (repository.existsById(id)) {
            ENTITY entity = convertToEntity(dto);
            entity = repository.save(entity);
            return convertToDTO(entity);
        } else {
            throw new RuntimeException("Entity with id " + id + " not found");
        }
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    protected DTO convertToDTO(ENTITY entity) {
        return modelMapper.map(entity, dtoClass);
    }

    protected ENTITY convertToEntity(DTO dto) {
        return modelMapper.map(dto, getEntityClass());
    }

    @SuppressWarnings("unchecked")
    private Class<ENTITY> getEntityClass() {
        return (Class<ENTITY>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Page<DTO> getAll(NUM page, SIZE size) {
        PageRequest pageable = PageRequest.of((int)page, (int)size);
        Page<ENTITY> entityPage = repository.findAll(pageable);
        return entityPage.map(this::convertToDTO);
    }

	@Override
	public Page<DTO> getAll(NUM page, SIZE size, FilterMarker filter) {
        PageRequest pageable = PageRequest.of((int)page, (int)size);
        Page<ENTITY> entityPage = repository.findAll(pageable);
        return entityPage.map(this::convertToDTO);
	}

	@Override
	public Slice<DTO> getAllSlice(NUM page, SIZE size) {
	    PageRequest pageRequest = PageRequest.of((int)page, (int)size);
	    Slice<ENTITY> entitySlice = repository.findAll(pageRequest);

	    List<DTO> dtoList = entitySlice.getContent()
	            .stream()
	            .map(this::convertToDTO)
	            .collect(Collectors.toList());

	    return new SliceImpl<>(dtoList, pageRequest, entitySlice.hasNext());
	}

	@Override
	public Slice<DTO> getAllSlice(NUM page, SIZE size, FilterMarker filter) {
	    PageRequest pageRequest = PageRequest.of((int)page, (int)size);
	    Slice<ENTITY> entitySlice = repository.findAll(pageRequest);

	    List<DTO> dtoList = entitySlice.getContent()
	            .stream()
	            .map(this::convertToDTO)
	            .collect(Collectors.toList());

	    return new SliceImpl<>(dtoList, pageRequest, entitySlice.hasNext());
	}



}
