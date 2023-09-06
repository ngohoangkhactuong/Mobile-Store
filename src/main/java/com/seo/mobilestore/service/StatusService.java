package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.StatusDTO;

public interface StatusService {
    StatusDTO findByName(String name);
}
