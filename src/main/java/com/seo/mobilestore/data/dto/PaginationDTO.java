package com.seo.mobilestore.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationDTO implements Serializable {
    private List<?> contents;
    private boolean isFirst;
    private boolean isLast;
    private long totalPages;
    private long totalItems;
    private long limit;
    private int no;
}

