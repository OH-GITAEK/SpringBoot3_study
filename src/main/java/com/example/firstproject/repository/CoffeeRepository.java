package com.example.firstproject.repository;

import com.example.firstproject.entity.Coffee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CoffeeRepository extends CrudRepository<Coffee,Long> {
    @Override
    List<Coffee> findAll();
}
