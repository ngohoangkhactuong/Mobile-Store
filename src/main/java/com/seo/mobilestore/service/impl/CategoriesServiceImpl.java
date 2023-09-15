package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.categories.CategoriesDTO;
import com.seo.mobilestore.data.entity.Categories;
import com.seo.mobilestore.data.mapper.product.CategoriesMapper;
import com.seo.mobilestore.data.repository.CategoriesRepository;
import com.seo.mobilestore.exception.ConflictException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private CategoriesMapper categoriesMapper;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Override
    public CategoriesDTO create(CategoriesDTO categoriesDTO) {
        Categories categories = categoriesMapper.toEntity(categoriesDTO);
        this.categoriesRepository.save(categories);

        return categoriesMapper.toDTO(categories);
    }

    @Override
    public CategoriesDTO update(long id , CategoriesDTO categoriesDTO){
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("Not found" , id)));

        Map<String, Object> errors = new HashMap<>();

        if (categoriesRepository.existsByName(categoriesDTO.getName())) {
            errors.put("name", categoriesDTO.getName());
        }

        if (!errors.isEmpty()) {
            throw new ConflictException(Collections.singletonMap("categoriesDTO", errors));
        }

        categories.setName(categoriesDTO.getName());

        this.categoriesRepository.save(categories);

        return categoriesMapper.toDTO(categories);
    }

    @Override
    public Boolean delete(long id){
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("Not found" , id)));

        boolean deleteStatus = false;
        categories.setStatus(deleteStatus);
        this.categoriesRepository.save(categories);

        return true;
    }


    public List<CategoriesDTO> getAll() {
        return categoriesRepository.findByStatusIsTrue().stream().map(u -> categoriesMapper.toDTO(u)).collect(Collectors.toList());
    }
    @Override
    public CategoriesDTO getById(long id){
        Categories categories = categoriesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        return categoriesMapper.toDTO(categories);
    }

    @Override
    public PaginationDTO getAllPagination(int no, int limit) {
        Page<CategoriesDTO> page = this.categoriesRepository.findAll(
                PageRequest.of(no, limit)).map(item -> categoriesMapper.toDTO(item));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }

}
