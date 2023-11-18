package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.ProductDTO;
import com.seo.mobilestore.data.dto.product.cart.CartDTO;
import com.seo.mobilestore.data.dto.product.cart.ShowCartDTO;
import com.seo.mobilestore.data.entity.Cart;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.entity.User;
import com.seo.mobilestore.data.mapper.CartMapper;
import com.seo.mobilestore.data.repository.CartRepository;
import com.seo.mobilestore.data.repository.ProductRepository;
import com.seo.mobilestore.data.repository.UserRepository;
import com.seo.mobilestore.exception.AccessDeniedException;
import com.seo.mobilestore.exception.ConflictException;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;


    @Override
    public ShowCartDTO create(CartDTO cartDTO){

        Map<String, Object> errors = new HashMap<>();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        Cart cartToCheck = cartRepository.findByProductId(cartDTO.getProduct_id());
        if(!(cartToCheck == null)){
            errors.put("product id", cartDTO.getProduct_id());
        }

        if (!errors.isEmpty()) {
            throw new ConflictException(Collections.singletonMap("CartDTO", errors));
        }

        Cart cart = cartMapper.toEntity(cartDTO);
        cart.setUser(user);
        cart.setProduct(productRepository.findById(cartDTO.getProduct_id()).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("Not found", cartDTO.getProduct_id()))));
        cart.setQuantity(cartDTO.getQuantity());

        ShowCartDTO showCartDTO = cartMapper.toShowDTO(this.cartRepository.save(cart));
        showCartDTO.setQuantity(cart.getQuantity());
        return showCartDTO;
    }

    @Override
    public ShowCartDTO update(long cart_id , CartDTO cartDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        // find cart by input cart id
        Cart old_cart = cartRepository.findById(cart_id).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("Not found", cart_id)));

        // check if user of cart is valid
        if(user.getId() != old_cart.getUser().getId()){
            throw new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                    null, null));
        }

        Cart cart = cartMapper.toEntity(cartDTO);
        cart.setId(old_cart.getId());
        cart.setUser(user);

        ShowCartDTO showCartDTO = cartMapper.toShowDTO(this.cartRepository.save(cart));
        showCartDTO.setQuantity(cart.getQuantity());

        return showCartDTO;
    }

    @Override
    public Boolean delete(long cart_id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        Cart cart = cartRepository.findById(cart_id).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("Not found", cart_id)));

        if(user != cart.getUser()){
            throw new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                    null, null));
        }

        this.cartRepository.delete(cart);
        return true;
    }

    public PaginationDTO getAllPagination(int no, int limit){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        Page<ShowCartDTO> page = this.cartRepository.findByUserId(user.getId(),
                PageRequest.of(no, limit)).map(item -> cartMapper.toShowDTO(item));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }
}
