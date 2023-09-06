package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.common.enumeration.ERole;
import com.seo.mobilestore.data.dto.RoleDTO;
import com.seo.mobilestore.data.entity.Role;
import com.seo.mobilestore.data.mapper.RoleMapper;
import com.seo.mobilestore.data.repository.RoleRepository;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Method generate role
     */
    @PostConstruct
    public void init() {
        List<Role> roleToSave = new ArrayList<>();
        for (ERole eRole : ERole.values()) {
            if (!roleRepository.existsByName(eRole.toString())) {
                Role role = new Role();
                role.setName(eRole.toString());
                roleToSave.add(role);
            }
        }
        if (!roleToSave.isEmpty()) {
            roleRepository.saveAll(roleToSave);
        }
    }

    @Override
    public RoleDTO findByName(String name) {
        return roleMapper.toDTO(
                roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("Role name", name))));
    }
}
