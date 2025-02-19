package org.example.miniproject.repositories.generic;

public interface ModifyingRepository<T> {
    void create(T dto);
}
