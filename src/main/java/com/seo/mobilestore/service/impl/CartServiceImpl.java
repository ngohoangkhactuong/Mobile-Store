package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.cart.CartCreationDTO;
import com.seo.mobilestore.data.dto.product.cart.CartDTO;
import com.seo.mobilestore.data.dto.product.cart.CartDetailDTO;
import com.seo.mobilestore.data.dto.product.cart.ShowCartDTO;
import com.seo.mobilestore.data.entity.*;
import com.seo.mobilestore.data.mapper.CartMapper;
import com.seo.mobilestore.data.mapper.UserMapper;
import com.seo.mobilestore.data.repository.*;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MemoryRepository memoryRepository;
    @Autowired
    private SeriRepository seriRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;


    @Override
    public CartDetailDTO create(CartCreationDTO cartCreationDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        Cart cart = cartMapper.toEntity(cartCreationDTO);
        cart.setUser(user);

        CartDTO cartDTO = cartMapper.toDTO(cartRepository.save(cart));
// ---------------------------------------------------------------------------------------------------------------------
        CartDetailDTO cartDetailDTO = new CartDetailDTO();
        cartDetailDTO.setCartDTO(cartDTO);
        cartDetailDTO.setOrderProductDTOList(cartCreationDTO.getOrderProductDTOList());
        cartDetailDTO.setQuantity(cartCreationDTO.getOrderProductDTOList().size());

        for(int i = 0; i < cartCreationDTO.getOrderProductDTOList().size() ; i++){

            String memory = cartCreationDTO.getOrderProductDTOList().get(i).getMemory();
            String seri = cartCreationDTO.getOrderProductDTOList().get(i).getSeri();

            long product_id = cartCreationDTO.getOrderProductDTOList().get(i).getId();

            CartDetails cartDetails = new CartDetails();
            cartDetails.setId(cartDetailDTO.getId());
            cartDetails.setCart(cart);
            cartDetails.setQuantity((int) cartCreationDTO.getOrderProductDTOList().get(i).getQuantity());

            cartDetails.setMemory(
                    memoryRepository.findMemoryByNameAndProductId(
                            memory , product_id
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(Collections.singletonMap("Not found",memory)))
            );

            Seri SERI = seriRepository.findSeriByNameAndProductId(
                    seri , product_id
            ).orElseThrow((()->
                    new ResourceNotFoundException(Collections.singletonMap("Not found" , seri))
            ));
            cartDetails.setSeri(SERI);

            Color color = colorRepository.findByName(SERI.getColor().getName());
            cartDetails.setColor(color);

            cartDetailRepository.save(cartDetails);
        }

        return cartDetailDTO;
    }

    @Override
    public ShowCartDTO update(long cart_id , CartCreationDTO cartCreationDTO){
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByEmail(email).orElseThrow(
//                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
//                        null, null)));
//
//        // find cart by input cart id
//        Cart cart = cartRepository.findById(cart_id).orElseThrow(() ->
//                new ResourceNotFoundException(Collections.singletonMap("Not found", cart_id)));
//
//        // check if user of cart is valid
//        if(user.getId() != cart.getUser().getId()){
//            throw new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
//                    null, null));
//        }
//
//
//        UserDTO userDTO = userMapper.toDTO(user);
//
//        ShowCartDTO showCartDTO = new ShowCartDTO();
//        showCartDTO.setOrderProductDTOList(cartDTO.getOrderProductDTOList());
//        showCartDTO.setUserDTO(userDTO);
//
//        for(ProductOrderDTO productOrderDTO : cartDTO.getOrderProductDTOList()){
//
//            cart.setUser(user);
//            cart.setProduct(productRepository.findById(productOrderDTO.getId()).orElseThrow(() ->
//                    new ResourceNotFoundException(Collections.singletonMap("Not found", productOrderDTO.getId()))));
//            cart.setQuantity(productOrderDTO.getQuantity());
//
//            this.cartRepository.save(cart);
//        }
//
//        return showCartDTO;
        return null;
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
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByEmail(email).orElseThrow(
//                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
//                        null, null)));
//
//        Page<ShowCartDTO> page = this.cartRepository.findByUserId(user.getId(),
//                PageRequest.of(no, limit)).map(item -> cartMapper.toShowDTO(item));
//
//        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
//                page.getTotalPages(),
//                page.getTotalElements(), page.getSize(),
//                page.getNumber());
        return null;
    }
}
