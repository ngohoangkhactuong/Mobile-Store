package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.cart.*;
import com.seo.mobilestore.data.dto.product.ShowProductOrderDTO;
import com.seo.mobilestore.data.entity.*;
import com.seo.mobilestore.data.mapper.CartMapper;
import com.seo.mobilestore.data.repository.*;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private MemoryRepository memoryRepository;
    @Autowired
    private SeriRepository seriRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private EntityManager entityManager;


    @Override
    public CartDetailDTO create(CartCreationDTO cartCreationDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        Cart cart = cartMapper.toEntity(cartCreationDTO);
        cart.setUser(user);

        CartDTO cartDTO = cartMapper.toDTO(cartRepository.save(cart));
        cartDTO.setId(cart.getId());

// ---------------------------------------------------------------------------------------------------------------------
        CartDetailDTO cartDetailDTO = new CartDetailDTO();
        cartDetailDTO.setCartDTO(cartDTO);
        cartDetailDTO.setOrderProductDTOList(cartCreationDTO.getOrderProductDTOList());
        cartDetailDTO.setQuantity(cartCreationDTO.getOrderProductDTOList().size());

        for(int i = 0; i < cartCreationDTO.getOrderProductDTOList().size() ; i++){

            String memory = cartCreationDTO.getOrderProductDTOList().get(i).getMemory();
            String seri = cartCreationDTO.getOrderProductDTOList().get(i).getSeri();

            String product_name = cartCreationDTO.getOrderProductDTOList().get(i).getName();

            CartDetails cartDetails = cartMapper.toDetailEntity(cartDetailDTO);
            cartDetails.setQuantity((int)cartCreationDTO.getOrderProductDTOList().get(i).getQuantity());

            cartDetails.setMemory(
                    memoryRepository.findMemoryByNameAndProductName(
                            memory , product_name
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(Collections.singletonMap("Not found",memory)))
            );

            Seri SERI = seriRepository.findSeriByNameAndProductName(
                    seri , product_name
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
    public CartDetailDTO update(long cart_id , CartUpdateDTO cartUpdateDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));
        long userID = user.getId();

        // find cart by input cart id
        Cart updateCart = cartRepository.findById(cart_id).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("Not found", cart_id)));

        // check if user of cart is valid
        if(userID != updateCart.getUser().getId()){
            throw new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                    null, null));
        }

        updateCart.setId(cart_id);
        updateCart.setUser(user);

        CartDTO updateCartDTO = cartMapper.toDTO(cartRepository.save(updateCart));

        List<CartDetails> oldCartDetails = cartDetailRepository.findAllByCartId(cart_id);
        long id = oldCartDetails.get(0).getId();
        if(!oldCartDetails.isEmpty()){
            oldCartDetails.forEach(oldCartDetail -> {
                cartDetailRepository.deleteById(oldCartDetail.getId());
            });
        }

        String tableName = "cart_details";
        resetAutoIncrement(tableName , id);

        CartDetailDTO newCartDetailDTO = new CartDetailDTO();
        newCartDetailDTO.setCartDTO(updateCartDTO);
        newCartDetailDTO.setOrderProductDTOList(cartUpdateDTO.getOrderProductDTOList());
        newCartDetailDTO.setQuantity(cartUpdateDTO.getOrderProductDTOList().size());

        for(int i = 0; i < cartUpdateDTO.getOrderProductDTOList().size() ; i++){

            String memory = cartUpdateDTO.getOrderProductDTOList().get(i).getMemory();
            String seri = cartUpdateDTO.getOrderProductDTOList().get(i).getSeri();

            String product_name = cartUpdateDTO.getOrderProductDTOList().get(i).getName();

            CartDetails cartDetails = cartMapper.toDetailEntity(newCartDetailDTO);
            cartDetails.setQuantity((int)cartUpdateDTO.getOrderProductDTOList().get(i).getQuantity());

            cartDetails.setMemory(
                    memoryRepository.findMemoryByNameAndProductName(
                            memory , product_name
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(Collections.singletonMap("Not found",memory)))
            );

            Seri SERI = seriRepository.findSeriByNameAndProductName(
                    seri , product_name
            ).orElseThrow((()->
                    new ResourceNotFoundException(Collections.singletonMap("Not found" , seri))
            ));
            cartDetails.setSeri(SERI);

            Color color = colorRepository.findByName(SERI.getColor().getName());
            cartDetails.setColor(color);

            cartDetailRepository.save(cartDetails);
        }

        return newCartDetailDTO;
    }

    @Override
    public Boolean delete(long product_id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        Cart cart = cartRepository.findCartByUserId(user.getId());
        if(user != cart.getUser()){
            throw new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                    null, null));
        }

        List<CartDetails> cartDetailsList = cartDetailRepository.findAllByCartId(cart.getId());
        for(CartDetails cartDetails : cartDetailsList){
            if(cartDetails.getSeri().getProduct().getId() == product_id){
                cartDetailRepository.delete(cartDetails);
            }
        }

        return true;
    }


    @Override
    public Boolean clearCart(long cart_id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        Cart cart = cartRepository.findCartByUserId(user.getId());
        if(user != cart.getUser()){
            throw new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                    null, null));
        }

        List<CartDetails> cartDetailsList = cartDetailRepository.findAllByCartId(cart.getId());
        for(CartDetails cartDetails : cartDetailsList){
            if(cartDetails.getCart().getId() == cart_id){
                cartDetailRepository.delete(cartDetails);
            }
        }
        cartRepository.delete(cart);

        return true;
    }
    @Override
    public PaginationDTO getAllPagination(int no, int limit){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        Page<Cart> page = cartRepository.findCartByUserId(user.getId(), PageRequest.of(no, limit));

        List<ShowCartDTO> showCartDTOList = page.getContent()
                .stream().map(this::mapToShowCartDTO)
                .collect(Collectors.toList());

        return new PaginationDTO(showCartDTOList, page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }

    private void resetAutoIncrement(String tableName, long nextId) {
        String query = "ALTER TABLE " + tableName + " AUTO_INCREMENT = " + nextId;
        entityManager.createNativeQuery(query).executeUpdate();
    }

    private ShowCartDTO mapToShowCartDTO(Cart cart){
        ShowCartDTO showCartDTO = new ShowCartDTO();
        ShowCartDetailDTO showCartDetailDTO = getCartDetailDTO(cart.getId());

        showCartDTO = cartMapper.toShowDTO(cart);

        showCartDTO.setProductOrderDTO(showCartDetailDTO.getOrderProductDTOList());

        return showCartDTO;
    }

    public ShowCartDetailDTO getCartDetailDTO(long cart_id){
        int defaultNum = 0;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        List<CartDetails> cartDetailsList = new ArrayList<>();
        cartDetailsList = cartDetailRepository.findAllByCartId(cart_id);

        ShowCartDetailDTO showCartDetailDTO = new ShowCartDetailDTO();
        if(cartDetailsList.isEmpty()){
            throw new ResourceNotFoundException(Collections.singletonMap("id", cart_id));
        }

        List<ShowProductOrderDTO> productOrderDTOList = cartDetailsList.stream()
                .map(this::mapToProductOrderDTO)
                .collect(Collectors.toList());

        showCartDetailDTO.setId(cart_id);
        showCartDetailDTO.setQuantity(cartDetailsList.size());
        showCartDetailDTO.setCartDTO(cartMapper.toDTO(cartDetailsList.get(defaultNum).getCart()));
        showCartDetailDTO.setOrderProductDTOList(productOrderDTOList);

        return showCartDetailDTO;
    }

    private ShowProductOrderDTO mapToProductOrderDTO(CartDetails cartDetails) {
        Product product = cartDetails.getSeri().getProduct();
        ShowProductOrderDTO productOrderDTO = new ShowProductOrderDTO();

        productOrderDTO.setSeri(cartDetails.getSeri().getName());
        productOrderDTO.setMemory(cartDetails.getMemory().getName());
        productOrderDTO.setQuantity(cartDetails.getQuantity());
        productOrderDTO.setId(product.getId());
        productOrderDTO.setName(product.getName());
        productOrderDTO.setPrice(product.getPrice());
        productOrderDTO.setDescription(product.getDescription());
        productOrderDTO.setImage(product.getImages().get(0).getName());

        return productOrderDTO;
    }

}
