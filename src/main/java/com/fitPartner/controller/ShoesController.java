package com.fitPartner.controller;

import com.fitPartner.dto.ShoeResponse;
import com.fitPartner.service.ShoeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoes")
@CrossOrigin
public class ShoesController {
    private final ShoeService shoeService;

    public ShoesController(ShoeService shoeService) {
        this.shoeService = shoeService;
    }

    @GetMapping("/details/{id}")
    public ShoeResponse getShoeById(@PathVariable Long id) {
        return shoeService.getById (id);
    }

    @GetMapping({"/allShoes", "/{gender}"})
    public Page<ShoeResponse> getShoesByGender(
            @PathVariable(required = false) String gender,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (gender == null || gender.isEmpty()) {
            return shoeService.getAllShoes(pageable);
        } else {
            return shoeService.findByGender(gender, pageable);
        }
    }


    @GetMapping("/filter")
    public Page<ShoeResponse> filterShoes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) Integer discount,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer size,
            Pageable pageable) {

        return shoeService.filterShoes(name, brand, price, discount, weight,
                category, gender, material, rating, color, size, pageable);
    }


    @GetMapping("/sameCategoryShoes")
    public ResponseEntity<List<ShoeResponse>> getSameCategoryShoes(@RequestParam(required = false) String brand,
                                                                   @RequestParam(required = false) String category,
                                                                   @RequestParam(required = false) String gender,
                                                                   @RequestParam(required = false) String material,
                                                                   @RequestParam(required = false) Long id

    ) {
        return ResponseEntity.ok (shoeService.getSameCategoryShoes (brand, category, gender, material, id));

    }

    @GetMapping("/byName")
    public ResponseEntity<List<ShoeResponse>> getShoesByNameOrBrand(@RequestParam String name) {
        System.out.println (name);
        List<ShoeResponse> shoes = shoeService.getShoesByName (name);
        return ResponseEntity.ok (shoes);
    }

    @GetMapping("/oppositeGenderShoes")
    public ResponseEntity<List<ShoeResponse>> getOppositeGenderShoes(@RequestParam String gender) {
        System.out.println ("gender="+gender);
        List<ShoeResponse> shoes = shoeService.getTop7OppositeGenderShoes (gender);
        System.out.println (shoes.size () + " shoes found");
        return ResponseEntity.ok (shoes);
    }

}
