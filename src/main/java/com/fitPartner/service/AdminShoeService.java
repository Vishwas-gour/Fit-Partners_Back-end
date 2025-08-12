package com.fitPartner.service;

import com.cloudinary.Cloudinary;
import com.fitPartner.dto.ShoeRequest;
import com.fitPartner.entity.shoe.Color;
import com.fitPartner.entity.shoe.Image;
import com.fitPartner.entity.shoe.Shoe;
import com.fitPartner.entity.shoe.Size;
import com.fitPartner.repository.ColorRepository;
import com.fitPartner.repository.ShoesRepository;
import com.fitPartner.repository.SizeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminShoeService {
    private final Cloudinary cloudinary;
    private final ShoesRepository shoesRepository;
    private final ColorRepository colorRepo;
    private final SizeRepository sizeRepo;


    public ResponseEntity<?> addShoe(ShoeRequest shoeRequest) {
        try {
            Shoe shoe;
            System.out.println (shoeRequest.getId ());

            if (shoeRequest.getId () != null) {
                Optional<Shoe> existing = shoesRepository.findById (shoeRequest.getId ());
                if (existing.isEmpty ()) return ResponseEntity.badRequest ().body ("Invalid Shoe ID");
                shoe = existing.get ();
            } else {
                shoe = new Shoe ();
            }

            shoe.setName (shoeRequest.getName ());
            shoe.setBrand (shoeRequest.getBrand ());
            shoe.setStock (shoeRequest.getStock ());
            shoe.setDescription (shoeRequest.getDescription ());
            shoe.setWeight (shoeRequest.getWeight ());

            shoe.setCostPrice (shoeRequest.getCostPrice ());
            shoe.setSalePrice (shoeRequest.getSalePrice ());
            shoe.setDiscountParentage (shoeRequest.getDiscountParentage ());
            shoe.setPriceAfterDiscount (shoeRequest.getPriceAfterDiscount ());

            shoe.setCategory (shoeRequest.getCategory ());
            shoe.setGender (shoeRequest.getGender ());
            shoe.setMaterial (shoeRequest.getMaterial ());

            // COLORS
            List<Color> colors = new ArrayList<> ();
            for (String colorName : shoeRequest.getColors ()) {
                Color color = colorRepo.findByColorName (colorName);
                if (color == null) color = colorRepo.save (new Color (colorName));
                colors.add (color);
            }
            shoe.setColors (colors);

            // SIZES
            List<Size> sizes = new ArrayList<> ();
            for (Integer sizeNo : shoeRequest.getSizes ()) {
                Size size = sizeRepo.findBySizeNo (sizeNo);
                if (size == null) size = sizeRepo.save (new Size (sizeNo));
                sizes.add (size);
            }
            shoe.setSizes (sizes);

            // IMAGES
            if (shoe.getImages () == null) {
                shoe.setImages (new ArrayList<> ());
            } else {
                shoe.getImages ().clear (); // only clear if updating
            }

            for (MultipartFile file : shoeRequest.getImages ()) {
                Map uploadResult = cloudinary.uploader ().upload (file.getBytes (), Map.of ("folder", "Fit-Partners"));
                String url = uploadResult.get ("secure_url").toString ();

                Image image = new Image ();
                image.setImgUrl (url);
                image.setShoe (shoe); // bind image to shoe
                shoe.getImages ().add (image); // add directly
            }

            shoesRepository.save (shoe);
            return ResponseEntity.ok ("Product saved");
        } catch (Exception e) {
            e.printStackTrace ();
            return ResponseEntity.status (500).body ("Error: " + e.getMessage ());
        }
    }



    public ResponseEntity<?> updateAvailability(Long id, boolean available) {

        Optional<Shoe> optional = shoesRepository.findById(id);
        if (optional.isPresent()) {
            Shoe shoe = optional.get();
            shoe.setAvailable(available);
            shoesRepository.save(shoe);
            return ResponseEntity.ok("Product " + (available ? "enabled" : "disabled"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

}
