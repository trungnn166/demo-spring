package com.nnt.demo.services;

import java.util.Optional;

public interface BaseService<T> {
    Iterable<T> findAll();
    Optional<T> findById(Long id);
    T save(T t);
    T update(T t);
    void delete(Long id);
}
