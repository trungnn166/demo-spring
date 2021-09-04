package com.nnt.demo.services;

import com.nnt.demo.entities.Role;


import java.util.Optional;

public interface RoleService extends BaseService<Role> {
    Optional<Role> findByCode(String code);
}
