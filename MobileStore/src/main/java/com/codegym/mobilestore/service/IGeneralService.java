package com.codegym.mobilestore.service;

public interface IGeneralService<T> {
    T save(T t);
//    T update(T t);
    T delete(T t);
    Iterable<T> findAll();
}
