package com.fitPartner.controller;

import com.fitPartner.dto.ShoeRequest;
import com.fitPartner.service.AdminShoeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin/addShoe")
public class AdminShoeAddController {
    ShoeRequest shoeRequest = new ShoeRequest ();
    private final AdminShoeService adminShoeService;

    public AdminShoeAddController(AdminShoeService adminShoeService) {
        this.adminShoeService = adminShoeService;
    }

    @PostMapping("/addStep1")
    public ResponseEntity<?> step1(@RequestBody Map<String, Object> data) {

        shoeRequest.setName ((String) data.get ("name"));
        shoeRequest.setBrand ((String) data.get ("brand"));
        shoeRequest.setStock (Integer.parseInt (data.get ("stock").toString ()));
        shoeRequest.setDescription ((String) data.get ("description"));
        shoeRequest.setWeight (Double.parseDouble (data.get ("weight").toString ()));

        double salePrice = Double.parseDouble(data.get("salePrice").toString());
        double costPrice = Double.parseDouble(data.get("costPrice").toString());
        double discount = Double.parseDouble(data.get("discountParentage").toString());
        double priceAfterDiscount = salePrice - (salePrice * discount / 100);
        shoeRequest.setId (data.get ("id") == null? null : Long.parseLong (data.get ("id").toString ()));
        shoeRequest.setSalePrice(salePrice);
        shoeRequest.setCostPrice(costPrice);
        shoeRequest.setDiscountParentage(discount);
        shoeRequest.setPriceAfterDiscount(priceAfterDiscount);

        return ResponseEntity.ok ("Step 1 saved");
    }

    @PostMapping("/addStep2")
    public ResponseEntity<?> step2(@RequestBody Map<String, String> data) {
        shoeRequest.setCategory (data.get ("category"));
        shoeRequest.setGender (data.get ("gender"));
        shoeRequest.setMaterial (data.get ("material"));
        return ResponseEntity.ok ("Step 2 saved");
    }

    @PostMapping("/addStep3")
    public ResponseEntity<?> step3(@RequestBody List<Integer> sizes) {
        shoeRequest.setSizes (sizes);
        return ResponseEntity.ok ("Step 3 saved");
    }

    @PostMapping("/addStep4")
    public ResponseEntity<?> step4(@RequestBody List<String> colors) {
        shoeRequest.setColors (colors);
        return ResponseEntity.ok ("Step 4 saved");
    }

    @PostMapping("/addStep5")
    public ResponseEntity<?> step5(@RequestParam("images") List<MultipartFile> images) {
        shoeRequest.setImages (images);
        return adminShoeService.addShoe (shoeRequest);
    }

}

@CrossOrigin
@RestController
@RequestMapping("/admin")
class AdminShoeDeleteController {
    private final AdminShoeService adminShoeService;
    public AdminShoeDeleteController(AdminShoeService adminShoeService) {
        this.adminShoeService = adminShoeService;
    }

    @PutMapping("/shoe/availability/{id}")
    public ResponseEntity<?> disable(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean available = body.get("available");
        return adminShoeService.updateAvailability (id, available);
    }
}
