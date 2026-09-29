package org.example.tarea1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MonumentRepository
        extends JpaRepository<Monument, Long> {

}