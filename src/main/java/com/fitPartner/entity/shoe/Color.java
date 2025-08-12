package com.fitPartner.entity.shoe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String colorName;

    @ManyToMany(mappedBy = "colors")
    private List<Shoe> shoes;



    public Color(String colorName) {
        this.colorName = colorName;
    }

    public Color() {

    }
}
