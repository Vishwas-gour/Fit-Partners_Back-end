package com.fitPartner.entity.shoe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int sizeNo;
    @ManyToMany(mappedBy = "sizes")
    private List<Shoe> shoes;

    public Size(int sizeNo) {
        this.sizeNo = sizeNo;
    }
    public Size() {
    }
}
