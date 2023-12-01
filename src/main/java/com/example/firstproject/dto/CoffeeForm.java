package com.example.firstproject.dto;

import com.example.firstproject.entity.Coffee;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class CoffeeForm {
    public Long id;
    public String name;
    public String price;

    public Coffee toEntity() {
        return new Coffee(id,name,price);
    }
}
