package com.example.bookworm.controller;

import com.example.bookworm.model.CartItem;
import com.example.bookworm.model.Order;
import com.example.bookworm.model.User;
import com.example.bookworm.repository.CartItemRepository;
import com.example.bookworm.repository.OrderRepository;
import com.example.bookworm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/checkout")
    public String getCheckout(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        model.addAttribute("cartItems", cartItems);
        return "checkout";
    }

    @PostMapping("/order")
    public String placeOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        Order order = new Order();
        order.setUser(user);
        order.setCartItems(cartItems);
        orderRepository.save(order);

        // Clear the cart
        cartItemRepository.deleteAll(cartItems);

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "orders";
    }
}
