package com.fitPartner.service;

import com.fitPartner.dto.CartItemResponse;
import com.fitPartner.dto.OrderItemDTO;
import com.fitPartner.dto.OrderResponseDTO;
import com.fitPartner.entity.order.OrderItem;
import com.fitPartner.entity.order.Orders;
import com.fitPartner.entity.user.Address;
import com.fitPartner.entity.user.MyUser;
import com.fitPartner.repository.MyUserRepository;
import com.fitPartner.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final MyUserService userService;
    private final OrdersRepository ordersRepository;
    private final CartItemService cartItemsService;
    private final MyUserRepository myUserRepository;

    public ResponseEntity<?> placeOrder(Principal principal, String razorpayPaymentId) {

        // DeliveryBoy selection logic
        String pincode =  myUserRepository.findByEmail (principal.getName ()).orElseThrow ().getAddress ().getPincode ();

        List<MyUser> employees = userService.findByRoleAndAddress_Pincode ("ROLE_EMPLOYEE", pincode);
        int empNo = (int) (Math.random () * employees.size ());
        System.out.println (empNo);

        List<CartItemResponse> cartItems = cartItemsService.getCart (principal).getBody ();
        if (cartItems.isEmpty ()) return ResponseEntity.badRequest ().body ("Cart is empty");
        System.out.println ("3 placeOrder service->" + principal.getName ());

        Orders order = new Orders ();
        order.setUserEmail (principal.getName ());
        order.setOrderStatus ("PLACED");
        order.setPaymentId (razorpayPaymentId);
        order.setTotalAmount (cartItems.stream ().mapToDouble (CartItemResponse::getCartPrice).sum ());
        order.setPaymentMethod ("Razorpay");

        List<OrderItem> items = cartItems.stream ()
                .map (cartItem -> new OrderItem (cartItem, order))
                .collect (Collectors.toList ());

        if (!employees.isEmpty ()) {
            order.setAssignedTo (employees.get (empNo));
        }

        order.setItems (items);
        ordersRepository.save (order);
        return ResponseEntity.ok ("Order created successfully");
    }


    public List<OrderResponseDTO> getOrdersByUserEmail(String email) {
        return ordersRepository.findByUserEmail (email).stream ()
                .sorted ((o1, o2) -> o2.getCreatedAt ().compareTo (o1.getCreatedAt ()))
                .map (OrderResponseDTO::new)
                .collect (Collectors.toList ());
    }


    public ResponseEntity<String> cancelOrder(Long id) {
        Orders order = ordersRepository.findById (id).orElseThrow ();
        if (!order.getOrderStatus ().equals ("DELIVERED") && !order.getOrderStatus ().equals ("RETURNED")) {
            order.setOrderStatus ("CANCELLED");
            ordersRepository.save (order);
            return ResponseEntity.ok ("Order cancelled successfully");
        } else {
            return ResponseEntity.badRequest ().body ("Order cannot be cancelled");
        }
    }

    public Integer countOrders(String name) {
        return ordersRepository.countByUserEmail (name);
    }

    public List<OrderResponseDTO> filterOrders(String orderStatus, String orderTime) {
        return ordersRepository.findAll ().stream ()
                .filter (order ->
                        (orderStatus.isEmpty () || order.getOrderStatus ().equals (orderStatus)) &&
                                (orderTime.isEmpty () || order.getCreatedAt ().getYear () == Integer.parseInt (orderTime)))
                .map (OrderResponseDTO::new)
                .collect (Collectors.toList ());
    }

    public List<OrderResponseDTO> getOrdersByStatus(String status) {
        return ordersRepository.findAll ().stream ()
                .filter (order -> order.getOrderStatus ().equals (status))
                .map (orders -> new OrderResponseDTO (orders))
                .collect (Collectors.toList ());
    }

    public List<OrderResponseDTO> findAll() {
        return ordersRepository.findAll ().stream ()
                .map (orders -> new OrderResponseDTO (orders))
                .collect (Collectors.toList ());
    }

    public ResponseEntity<OrderResponseDTO> getOrderById(Long id) {
        return null;
    }

    public ResponseEntity<?> getCurrentOrderByAssignedToId(Long id) {
        List<Orders> allOrders = ordersRepository.findByAssignedTo_Id (id);
        return ResponseEntity.ok (allOrders.stream ()
                        .filter (orders -> orders.getOrderStatus ().equals ("PLACED")
                                          || orders.getOrderStatus ().equals ("ON_THE_WAY")
                                        || orders.getOrderStatus ().equals ("RETURNING"))
                .map (OrderResponseDTO::new)
                .collect (Collectors.toList ()));
    }
    public ResponseEntity<?> getOrderHistoryByAssignedToId(Long id) {
        List<Orders> allOrders = ordersRepository.findByAssignedTo_Id (id);
        return ResponseEntity.ok (allOrders.stream ()
                        .filter (orders -> orders.getOrderStatus ().equals ("DELIVERED") || orders.getOrderStatus ().equals ("RETURNED"))
                .map (OrderResponseDTO::new)
                .collect (Collectors.toList ()));
    }

    public ResponseEntity<String> updateOrderStatus(Long id, String status) {
        Orders order = ordersRepository.findById (id).orElseThrow ();
        order.setOrderStatus (status);
        if(status.equals("DELIVERED") || status.equals("RETURNED")){
            order.setUpdatedAt (LocalDateTime.now ());
        }
        ordersRepository.save (order);
        return ResponseEntity.ok ("Order status updated successfully");
    }


    public ResponseEntity<String> returningOrder(Long id) {
        Orders order = ordersRepository.findById (id).orElseThrow ();
        if (order.getOrderStatus ().equals ("DELIVERED")) {
            order.setOrderStatus ("RETURNING");
            ordersRepository.save (order);
            return ResponseEntity.ok ("Order returned successfully");
        } else {
            return ResponseEntity.badRequest ().body ("Order cannot be returned");
        }
    }
}
