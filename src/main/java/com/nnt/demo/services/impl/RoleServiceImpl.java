package com.nnt.demo.services.impl;

import com.nnt.demo.entities.Role;
import com.nnt.demo.repositories.RoleRepository;
import com.nnt.demo.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public Iterable<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role save(Role role) {

        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
