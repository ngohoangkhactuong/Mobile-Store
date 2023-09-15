package com.seo.mobilestore.service;


import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.review.ReviewDTO;

public interface ReviewService {
    MessageResponse create(ReviewDTO reviewDTO);

    ReviewDTO update(long reviewID , ReviewDTO reviewDTO);

    boolean deleteById(long id);

    PaginationDTO getById(long id, int no, int limit);

    PaginationDTO getAllPagination(int no, int limit);


}
