package io.github.mjyoun.spring.database.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import io.github.mjyoun.core.data.Result;

/**
 * JPA를 사용한 간단한 CRUD 기능을 구현한 Service
 * 
 * @author MJ Youn
 * @since 2023. 11. 08.
 */
public interface JpaCRUDService<Entity, ID, DTO, Repository extends JpaRepository<Entity, ID> & JpaSpecificationExecutor<Entity>> {

    /**
     * 전체 조회
     * 
     * @param spec
     *            검색 조건. nullable
     * @param sort
     *            정렬 조건
     * @return 전체 조회 결과
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    public Result<List<DTO>> findAll(Specification<Entity> spec, @NotNull Sort sort);

    /**
     * 전체 조회
     * 
     * @param sort
     *            정렬 조건
     * @return 전체 조회 결과
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    public Result<List<DTO>> findAll(@NotNull Sort sort);

    /**
     * 페이지 정보 조회
     * 
     * @param spec
     *            검색 조건. nullable
     * @param pageable
     *            페이지네이션 정보
     * @return 조회 결과
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    public Result<Page<DTO>> retrieve(Specification<Entity> spec, @NotNull Pageable pageable);

    /**
     * 페이지 정보 조회
     * 
     * @param pageable
     *            페이지네이션 정보
     * @return 조회 결과
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    public Result<Page<DTO>> retrieve(@NotNull Pageable pageable);

    /**
     * 아이디로 단일 항목 조회
     * 
     * @param id
     *            아이디
     * @return 조회 결과
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    public Result<DTO> findById(@NotNull ID id);

    /**
     * 검색 조건으로 단일 항목 조회
     * 
     * @param spec
     *            검색 조건
     * @return 조회 결과
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    public Result<DTO> findOne(@NotNull Specification<Entity> spec);

    /**
     * 등록 / 수정
     * 
     * @param dto
     *            등록/수정 할 값
     * @return 등록/수정 결과
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    @Transactional
    public Result<Boolean> save(@NotNull DTO dto);

    /**
     * 아이디로 삭제
     * 
     * @param id
     *            아이디
     * @return 삭제 결과
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    @Transactional
    public Result<Boolean> delete(@NotNull ID id);

    /**
     * 아이디 목록으로 삭제
     * 
     * @param ids
     *            아이디 목록
     * @return 삭제 결과
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    @Transactional
    public Result<Boolean> deleteAll(@NotNull ID[] ids);

    /**
     * Convert Entity to DTO
     * 
     * @param entity
     *            entity info
     * @return DTO
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    public DTO convertEntity2DTO(@NotNull Entity entity);

    /**
     * Convert DTO to Entity
     * 
     * @param dto
     *            dto info
     * @return Entity
     * 
     * @author MJ Youn
     * @since 2023. 11. 08.
     */
    public Entity convertDTO2Entity(@NotNull DTO dto);

}
