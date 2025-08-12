package com.fitPartner.service;

import com.fitPartner.dto.ShoeResponse;
import com.fitPartner.entity.shoe.Review;
import com.fitPartner.entity.shoe.Shoe;
import com.fitPartner.repository.ShoesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoeService {
    private final ShoesRepository shoesRepository;


    public List<ShoeResponse> getAllShoe() {
        return shoesRepository.findAll ()
                .stream ().
                map (ShoeResponse::new)
                .collect (Collectors.toList ());
    }

    public ShoeResponse getById(Long id) {
        Shoe shoe = shoesRepository.findById (id)
                .orElseThrow (() -> new RuntimeException ("Shoe not found with id " + id));
        return new ShoeResponse (shoe);
    }

    public List<ShoeResponse> filterShoes(String name, String brand, String price, Integer discount, Double weight,
                                          String category, String gender, String material, Integer rating,
                                          String color, Integer size) {

        List<ShoeResponse> all = getAllShoe ();

        return all.stream ()
                .filter (s -> name == null || name.isBlank () || s.getName ().toLowerCase ().contains (name.toLowerCase ()))
                .filter (s -> brand == null || brand.isBlank () || s.getBrand ().equalsIgnoreCase (brand))
                .filter (s -> {
                    if (price == null || price.isBlank ()) return true;
                    double p = s.getPriceAfterDiscount ();
                    if (price.equals ("10000+")) return p > 10000;
                    try {
                        String[] range = price.split ("-");
                        double min = Double.parseDouble (range[0]);
                        double max = Double.parseDouble (range[1]);
                        return p >= min && p <= max;
                    } catch (Exception e) {
                        return true; // ignore bad format
                    }
                })
                .filter (s -> discount == null || s.getDiscountParentage () >= discount)
                .filter (s -> weight == null || s.getWeight () <= weight)
                .filter (s -> category == null || category.isBlank () || s.getCategory ().equalsIgnoreCase (category))
                .filter (s -> gender == null || gender.isBlank () || s.getGender ().equalsIgnoreCase (gender))
                .filter (s -> material == null || material.isBlank () || s.getMaterial ().equalsIgnoreCase (material))
                .filter (s -> {
                    if (rating == null) return true;
                    if (s.getRatings () == null || s.getRatings ().isEmpty ()) return false;
                    double avg = s.getRatings ().stream ().mapToInt (r -> r).average ().orElse (0);
                    return avg >= rating;
                })
                .filter (s -> color == null || color.isEmpty () || s.getColors ().stream ().anyMatch (c -> c.equalsIgnoreCase (color)))
                .filter (s -> size == null || s.getSizes ().contains (size))
                .collect (Collectors.toList ());
    }

    public List<ShoeResponse> findByGender(String gender) {
        List<Shoe> shoes = shoesRepository.findByGender (gender);
        return shoes
                .stream ().
                map (ShoeResponse::new)
                .collect (Collectors.toList ());
    }

    public List<ShoeResponse> getSameCategoryShoes(String brand, String category, String gender, String material, Long id) {
        List<Shoe> shoes = shoesRepository.findTop10ByBrandOrCategoryOrGenderOrMaterial (brand, category, gender, material);
        System.out.println ( " SHOELIS SIE => " + shoes.size ());
        shoes.removeIf (shoe -> Objects.equals (shoe.getId (), id));
        System.out.println ( " SHOELIS SIE => " + shoes.size ());
        return shoes
                .stream ().
                map (ShoeResponse::new)
                .collect (Collectors.toList ());
    }



    public List<ShoeResponse> getShoesByName(String name) {
        List<Shoe>  shoes =  shoesRepository.findByNameContainingIgnoreCase (name);
        return shoes
                .stream ().
                map (ShoeResponse::new)
                .collect (Collectors.toList ());
    }


    public List<ShoeResponse> getTop7OppositeGenderShoes(String Gender){
        List<Shoe> shoes = shoesRepository.findTop7ByGender(Gender.equals("Men") ? "Women" : "Men");
        System.out.println ( " SHOELIS SIE => " + shoes.size ());
        return shoes
                .stream ()
                .map (ShoeResponse::new)
                .collect (Collectors.toList ());
    }
}
