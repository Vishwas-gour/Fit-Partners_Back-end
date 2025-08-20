package com.fitPartner.controller;

import com.fitPartner.dto.OrderResponseDTO;
import com.fitPartner.entity.order.Orders;
import com.fitPartner.service.MyUserService;
import com.fitPartner.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;
    private final MyUserService userService;


    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(Principal principal, @RequestParam String razorpayPaymentId) {
        return ordersService.placeOrder (principal, razorpayPaymentId);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrders(Principal principal) {

        return ResponseEntity.ok (ordersService.getOrdersByUserEmail (principal.getName ()));
    }

    @GetMapping("orders/{email}")
    public ResponseEntity<List<OrderResponseDTO>> getOrderById(@PathVariable String email) {
        return ResponseEntity.ok (ordersService.getOrdersByUserEmail (email));
    }


    @GetMapping("/count")
    public ResponseEntity<Integer> countOrders(Principal principal) {
        return ResponseEntity.ok (ordersService.countOrders (principal.getName ()));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<OrderResponseDTO>> filterOrders(@RequestParam(required = false) String orderStatus,
                                                               @RequestParam(required = false) String orderTime) {
        if (orderTime.equals ("OLDER")) {
            orderTime = "2021";
        }
        return ResponseEntity.ok (ordersService.filterOrders (orderStatus, orderTime));
    }


    @GetMapping("/available-delivery-boy")
    public boolean getAvailableDeliveryBoyByAddress(Principal principal) {
        return userService.getAvailableDeliveryBoy (principal.getName ());
    }

    @GetMapping("/byStatus")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByStatus(@RequestParam String status) {
        return ResponseEntity.ok (ordersService.getOrdersByStatus (status));

    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok (ordersService.findAll ());
    }

    @GetMapping("/current/assignedTo")
    public ResponseEntity<?> getOrderAssignedTo(@RequestParam(required = false) String email,Principal principal) {

        System.out.println (email);
        String emailToUse = (email != null && !email.isBlank())? email : principal.getName();
        Long id = userService.findByEmail(emailToUse);
        return ordersService.getCurrentOrderByAssignedToId(id);
    }

    @GetMapping("/history/assignedTo")
    public ResponseEntity<?> getOrderHistoryAssignedTo(@RequestParam(required = false) String email,Principal principal) {
        String emailToUse = (email != null && !email.isBlank())? email : principal.getName();
        Long id = userService.findByEmail(emailToUse);
        return ordersService.getOrderHistoryByAssignedToId (id);

    }
    @PutMapping("/updateStatus/{id}/{newStatus}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @PathVariable String newStatus) {
        return ordersService.updateOrderStatus (id, newStatus);
    }
}