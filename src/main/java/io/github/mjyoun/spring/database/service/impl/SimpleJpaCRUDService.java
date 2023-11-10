package io.github.mjyoun.spring.database.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.mjyoun.core.data.Result;
import io.github.mjyoun.spring.data.ListResultConverter;
import io.github.mjyoun.spring.data.PageResultConverter;
import io.github.mjyoun.spring.database.service.JpaCRUDService;

/**
 * JPA를 사용한 기본적인 CRUD를 지원하는 서비스
 * 
 * @author MJ Youn
 * @since 2023. 11. 08.
 */
public class SimpleJpaCRUDService<Entity, ID, DTO, Repository extends JpaRepository<Entity, ID> & JpaSpecificationExecutor<Entity>>
        implements JpaCRUDService<Entity, ID, DTO, Repository> {

    /** logger */
    private final Logger logger;
    /** repository */
    private final Repository repository;
    /** object mapper */
    private final ObjectMapper objectMapper;

    /** entity class */
    private final Class<Entity> entityClass;
    /** dto class */
    private final Class<DTO> dtoClass;

    /**
     * (non-javadoc)
     * 
     * @param logger
     *            logger
     * @param entityClass
     *            entity class
     * @param dtoClass
     *            dto class
     * @param repository
     *            implements of {@link JpaRepository} and {@link JpaSpecificationExecutor}
     * @param objectMapper
     *            {@link ObjectMapper}
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    protected SimpleJpaCRUDService(Logger logger, //
            Class<Entity> entityClass, Class<DTO> dtoClass, //
            Repository repository, //
            ObjectMapper objectMapper) {
        this.logger = logger;

        this.entityClass = entityClass;
        this.dtoClass = dtoClass;

        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * @see JpaCRUDService#findAll(Specification, Sort)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    public Result<List<DTO>> findAll(Specification<Entity> spec, @NotNull Sort sort) {
        logger.debug("[{}] find all [spec: {}, sort: {}]", this.entityClass.getName(), spec, sort);

        List<Entity> entities = null;

        if (spec == null) {
            entities = this.repository.findAll(sort);
        } else {
            entities = this.repository.findAll(spec, sort);
        }

        return ListResultConverter.of(entities) //
                .map(dtoClass, this::convertEntity2DTO) //
                .get();
    }

    /**
     * @see JpaCRUDService#findAll(Sort)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    public Result<List<DTO>> findAll(@NotNull Sort sort) {
        return this.findAll(null, sort);
    };

    /**
     * @see JpaCRUDService#retrieve(Specification, Pageable)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    public Result<Page<DTO>> retrieve(Specification<Entity> spec, @NotNull Pageable pageable) {
        logger.debug("[{}] retrieve [spec: {}, pageable: {}]", this.entityClass.getName(), spec, pageable);

        Page<Entity> entities = null;

        if (spec == null) {
            entities = this.repository.findAll(pageable);
        } else {
            entities = this.repository.findAll(spec, pageable);
        }

        return PageResultConverter.of(entities) //
                .map(dtoClass, this::convertEntity2DTO) //
                .get();
    }

    /**
     * @see JpaCRUDService#retrieve(Pageable)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    public Result<Page<DTO>> retrieve(@NotNull Pageable pageable) {
        return this.retrieve(null, pageable);
    }

    /**
     * @see JpaCRUDService#findById(Object)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    public Result<DTO> findById(@NotNull ID id) {
        Result<DTO> result = null;

        logger.debug("[{}] find by id [id: {}]", this.entityClass.getName(), id);
        Optional<Entity> entityOp = this.repository.findById(id);

        if (!entityOp.isPresent()) {
            logger.debug("[{}] 검색 결과가 존재하지 않습니다. [id: {}]", this.entityClass.getName(), id);
            result = Result.ok(null);
        } else {
            result = Result.ok(this.convertEntity2DTO(entityOp.get()));
        }

        return result;
    }

    /**
     * @see JpaCRUDService#findOne(Specification)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    public Result<DTO> findOne(@NotNull Specification<Entity> spec) {
        Result<DTO> result = null;

        logger.debug("[{}] find one [spec: {}]", this.entityClass.getName(), spec);
        Optional<Entity> entityOp = this.repository.findOne(spec);

        if (!entityOp.isPresent()) {
            logger.debug("[{}] 검색 결과가 존재하지 않습니다. [spec: {}]", this.entityClass.getName(), spec);
            result = Result.ok(null);
        } else {
            result = Result.ok(this.convertEntity2DTO(entityOp.get()));
        }

        return result;
    }

    /**
     * @see JpaCRUDService#save(Object)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    @Transactional
    public Result<Boolean> save(@NotNull DTO dto) {
        logger.debug("[{}] save", this.entityClass.getName());
        Entity entity = this.convertDTO2Entity(dto);

        this.repository.save(entity);

        return Result.ok(true);
    }

    /**
     * @see JpaCRUDService#delete(Object)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    @Transactional
    public Result<Boolean> delete(@NotNull ID id) {
        logger.debug("[{}] delete by id [id: {}]", this.entityClass.getName(), id);
        this.repository.deleteById(id);

        return Result.ok(true);
    }

    /**
     * @see JpaCRUDService#deleteAll(Object[])
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    @Transactional
    public Result<Boolean> deleteAll(@NotNull ID[] ids) {
        logger.debug("[{}] delete all by ids [ids: {}]", this.entityClass.getName(), ids);

        List<ID> _ids = Arrays.asList(ids);
        this.repository.deleteAllByIdInBatch(_ids);

        return Result.ok(true);
    }

    /**
     * @see JpaCRUDService#convertEntity2DTO(Object)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    public DTO convertEntity2DTO(@NotNull Entity entity) {
        return this.objectMapper.convertValue(entity, dtoClass);
    }

    /**
     * @see JpaCRUDService#convertDTO2Entity(Object)
     * 
     * @author MJ Youn
     * @since 2023. 11. 10.
     */
    @Override
    public Entity convertDTO2Entity(@NotNull DTO dto) {
        return this.objectMapper.convertValue(dto, entityClass);
    }

}
