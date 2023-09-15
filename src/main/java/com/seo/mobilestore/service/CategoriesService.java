package com.seo.mobilestore.service;

import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.categories.CategoriesDTO;

public interface CategoriesService {
    CategoriesDTO create(CategoriesDTO categoriesDTO);

    CategoriesDTO update(long id , CategoriesDTO categoriesDTO);

    Boolean delete(long id);

    CategoriesDTO getById(long id);

    PaginationDTO getAllPagination(int no, int limit);

}
