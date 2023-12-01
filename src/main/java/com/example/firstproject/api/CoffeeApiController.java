package com.example.firstproject.api;

import com.example.firstproject.dto.CoffeeForm;
import com.example.firstproject.entity.Coffee;
import com.example.firstproject.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CoffeeApiController {
    @Autowired
    CoffeeRepository coffeeRepository;
    @GetMapping("/api/coffees")
    public List<Coffee> index(){
        return coffeeRepository.findAll();
    }

    @GetMapping("/api/coffees/{id}")
    public Coffee show(@PathVariable Long id){
        return coffeeRepository.findById(id).orElse(null);
    }

    @PostMapping("/api/coffees")
    public Coffee create(@RequestBody CoffeeForm dto){
        Coffee coffeeEntity = dto.toEntity();
        return coffeeRepository.save(coffeeEntity);
    }

    @PatchMapping("/api/coffees/{id}")
    public ResponseEntity<Coffee> update(@PathVariable Long id, @RequestBody CoffeeForm dto){
        Coffee coffee = dto.toEntity();
        log.info("id: {} Coffee: {}",id,coffee.toString());
        Coffee target = coffeeRepository.findById(id).orElse(null);
        if (target == null || id != coffee.getId()){
            log.info("잘못된 요청! id: {} Coffee: {}",id,coffee.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        target.patch(coffee);
        coffeeRepository.save(target);
        return ResponseEntity.status(HttpStatus.OK).body(coffee);
    }

    @DeleteMapping("/api/coffees/{id}")
    public ResponseEntity<Coffee> delete(@PathVariable Long id){
        Coffee target = coffeeRepository.findById(id).orElse(null);
        if (target == null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        coffeeRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
