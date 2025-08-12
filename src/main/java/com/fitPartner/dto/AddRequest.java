package com.fitPartner.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddRequest {
    private Long shoeId;
    private int quantity; // use only in cart
}
