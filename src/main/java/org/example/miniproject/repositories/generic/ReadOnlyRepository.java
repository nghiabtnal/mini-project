package org.example.miniproject.repositories.generic;

import java.util.Optional;

public interface ReadOnlyRepository<T, U> {
    Optional<T> get(U id);
}
