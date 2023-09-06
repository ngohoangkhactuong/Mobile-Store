package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.RoleDTO;

public interface RoleService {

    RoleDTO findByName(String name);
}
