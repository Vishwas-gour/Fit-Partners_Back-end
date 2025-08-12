package com.fitPartner.controller;


import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;


@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final LocalContainerEntityManagerFactoryBean entityManagerFactory2;
    @Value("${razorpay.key_id}")
    private  String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    private  final OrderController orderController;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> request) {
        double amountToPay = Double.parseDouble (request.get ("amountToPay"));
        String currency = request.get ("currency");


        try {
            RazorpayClient client = new RazorpayClient (razorpayKeyId, razorpayKeySecret);
            JSONObject options = new JSONObject ();
            options.put ("receipt", "order_rcptid_" + System.currentTimeMillis ());
            options.put ("amount", amountToPay * 100);
            options.put ("currency", currency);
            Order order = client.orders.create (options);
            String orderId = order.get ("id");

            return ResponseEntity.ok (Map.of (
                    "orderId", orderId,
                    "razorpayKey", razorpayKeyId
            ));
        } catch (Exception e) {
            e.printStackTrace ();
            return ResponseEntity.badRequest ().body ("Error creating Razorpay order");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> request, Principal principal) {

        String razorpayPaymentId = request.get ("razorpayPaymentId");
        String razorpayOrderId = request.get ("razorpayOrderId");
        String razorpaySignature = request.get ("razorpaySignature");
        System.out.println ( "verifyPayment->" +  principal.getName ());

        try {
            JSONObject attributes = new JSONObject ();
            attributes.put ("razorpay_payment_id", razorpayPaymentId);
            attributes.put ("razorpay_order_id", razorpayOrderId);
            attributes.put ("razorpay_signature", razorpaySignature);
            Utils.verifyPaymentSignature (attributes, razorpayKeySecret);
            orderController.placeOrder(principal, razorpayPaymentId);

            return ResponseEntity.ok ("Payment verified successfully!");

        } catch (RazorpayException e) {
            e.printStackTrace ();
            return ResponseEntity.badRequest ().body ("Invalid payment signature");
        }
    }

}

