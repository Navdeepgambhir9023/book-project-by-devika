package com.example.bookworm.controller;

import com.example.bookworm.model.Book;
import com.example.bookworm.model.CartItem;
import com.example.bookworm.model.User;
import com.example.bookworm.repository.BookRepository;
import com.example.bookworm.repository.CartItemRepository;
import com.example.bookworm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/cart")
    public String getCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Book book = bookRepository.findById(bookId).orElse(null);
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setUser(user);
        cartItem.setQuantity(1);
        cartItemRepository.save(cartItem);
        return "redirect:/cart";
    }
}
