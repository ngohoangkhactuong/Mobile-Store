package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.common.enumeration.ENum;
import com.seo.mobilestore.common.enumeration.EPaymentMethod;
import com.seo.mobilestore.common.enumeration.Estatus;
import com.seo.mobilestore.data.dto.order.OrderCreationDTO;
import com.seo.mobilestore.data.dto.order.OrderDTO;
import com.seo.mobilestore.data.dto.order.OrderDetailDTO;
import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import com.seo.mobilestore.data.entity.*;
import com.seo.mobilestore.data.mapper.OrderMapper;
import com.seo.mobilestore.data.repository.*;
import com.seo.mobilestore.exception.CannotDeleteException;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.exception.ValidationException;
import com.seo.mobilestore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private PromotionRepository  promotionRepository;
    @Autowired
    private StatusRepository  statusRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private MemoryRepository memoryRepository;
    @Autowired
    private SeriRepository seriRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderDetailRepository  orderDetailRepository;
    @Autowired
    private MessageSource messageSource;

    @Override
    public OrderDetailDTO create(OrderCreationDTO orderCreationDTO) {

        /*----------------------------- Xử lý Order -------------------------------------*/
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("email", email)));

        BigDecimal total = BigDecimal.valueOf(0);
        for(ProductOrderDTO productOrderDTO : orderCreationDTO.getOrderProductDTOList()){
            BigDecimal price = productOrderDTO.getPrice();
            total = total.add(price);
        }

        Promotion  promotion = null;
        long promotion_id = orderCreationDTO.getId_promotion();
        if(promotion_id != 0){
            promotion = promotionRepository.findById(promotion_id).orElseThrow(() ->
                    new ResourceNotFoundException(Collections.singletonMap("Not found",promotion_id)));
            total = calculateDiscountedTotal(
                    total, promotion.getDiscount(), promotion.getTotalPurchase(), promotion.getMaxGet());
        }

        Orders order = orderMapper.toEnity(orderCreationDTO);
        order.setTotal(total);
        order.setPromotion(promotion);
        order.setUser(user);

        String payment_method = orderCreationDTO.getPayment_method();;
        PaymentMethod paymentMethod = paymentMethodRepository.findByName(payment_method).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("Not found",payment_method)));
        order.setPaymentMethod(paymentMethod);


        String status_name = orderCreationDTO.getStatusDTO().getName();
        Status status =  statusRepository.findByName(status_name).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("Not found",status_name)));
        order.setStatus(status);
        order.setReceiveDate(java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(0)));
        order.setPaymentStatus(!orderCreationDTO.getPayment_method().equals(EPaymentMethod.Cash.toString()));

        this.ordersRepository.save(order);
        /* ---------------------------------------------------------------------------------------------------- */

        OrderDTO orderDTO = orderMapper.toDTO(order);

        /*------------------------------------ Xử lý OrderDetail ---------------------------------------------*/
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrderDTO(orderDTO);
        orderDetailDTO.setOrderProductDTOList(orderCreationDTO.getOrderProductDTOList());
        orderDetailDTO.setQuantity(orderCreationDTO.getOrderProductDTOList().size());

        for(ProductOrderDTO productOrderDTO : orderCreationDTO.getOrderProductDTOList()){
            String color_name = productOrderDTO.getColor();
            String memory_name = productOrderDTO.getMemory();
            String seri_name = productOrderDTO.getSeri();

            OrderDetails orderDetails = orderMapper.toDetailEnity(orderDetailDTO);
            orderDetails.setQuantity(1);

            Color color = colorRepository.findColorByNameAndProductId(
                    color_name , productOrderDTO.getId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(Collections.singletonMap("Not found",color_name)));
            orderDetails.setColor(color);

            Memory memory = memoryRepository.findMemoryByNameAndProductId(
                    memory_name , productOrderDTO.getId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(Collections.singletonMap("Not found",memory_name)));
            orderDetails.setMemory(memory);

            Seri seri = seriRepository.findSeriByNameAndProductId(
                    seri_name , productOrderDTO.getId()
            ).orElseThrow((()->
                    new ResourceNotFoundException(Collections.singletonMap("Not found" , seri_name))
                    ));
            orderDetails.setSeri(seri);

            orderDetails.setAddress(addressRepository.findAddressById(orderCreationDTO.getId_address())
                    .orElse(addressRepository.findDefaultAddress(user.getId())));

            this.orderDetailRepository.save(orderDetails);
        }

        return orderDetailDTO;
    }


    /**
     * function calculate total price after apply discount
     * */
    @Override
    public BigDecimal calculateDiscountedTotal(BigDecimal total, int discountPercentage, BigDecimal totalPurchase, BigDecimal maxDiscountAmount) {
        if (total.compareTo(totalPurchase) > 0) {
            BigDecimal discountDecimal = BigDecimal.valueOf(discountPercentage).divide(BigDecimal.valueOf(100));

            BigDecimal discountValue = total.multiply(discountDecimal);

            if (discountValue.compareTo(maxDiscountAmount) > 0) {
                discountValue = maxDiscountAmount;
            }

            total = total.subtract(discountValue);
        }

        return total;
    }

    public boolean checkValidIdColor(List<Color> list, long id) {

        for (Color color : list) {

            if (color.getId() == id)
                return true;
        }

        return false;
    }

    public boolean checkValidIdMemory(List<Memory> list, long id) {

        for (Memory memory : list) {

            if (memory.getId() == id)
                return true;
        }

        return false;
    }

    /*
    * Delete order by admin
    **/
    public Boolean deleteOrder(long id) {
        int idStatus = 5; //idStatus = 5 => name = Deleted

        Orders order = ordersRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        // idStatus = 5 => Order deleted => data not found
        if (order.getStatus().getId() == idStatus) {

            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        Status status = statusRepository.findById(idStatus).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));


        order.setStatus(status);

        ordersRepository.saveAndFlush(order);

        return true;

    }

    /*
    * Delete order by user id
    * */

    /*
     *  Show detail order by order id
    **/

    /*
     *  Show order of user
     **/

    /*
     *  Show all order (role admin)
     **/

    /*
    *  Update order
    **/



    /*
     *  Delete order of customer
     **/
    @Override
    public Boolean deleteOrderByCustomer(long orderId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen", null, null)));

        int idStatus = 5; //idStatus = 5 => name = Deleted

        Orders order = ordersRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", orderId)));

        // idStatus = 5 => Order deleted => data not found
        if (order.getStatus().getId() == idStatus) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", orderId));
        }

        //Check user has this order, yes or no?
        if (!order.getUser().equals(user)) {
            throw new CannotDeleteException(String.format("Order Id: %s", orderId));
        }

        Status status = statusRepository.findById(idStatus).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", idStatus)));

        //Only cash payment method is allowed to delete
        long paymentMethodId = order.getPaymentMethod().getId();

        if (paymentMethodId != EPaymentMethod.cashPaymentMethod) {
            throw new CannotDeleteException(String.format("Payment Method: %s", order.getPaymentMethod().getName()));
        }

        order.setStatus(status);

        ordersRepository.save(order);

        return true;
    }


    /*
    * Cancel order  overdue
    **/
    public Boolean cancelOrder(long idOrder) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen", null, null)));


        Orders order = ordersRepository.findById(idOrder).orElseThrow(
                () -> new ValidationException(Collections.singletonMap("id order",
                        idOrder)));

        if (!order.getUser().equals(user)) {
            throw new BadCredentialsException("Can't cancel");
        }

        Date curDate = java.sql.Timestamp.valueOf(LocalDateTime.now());
        if (curDate.after(order.getReceiveDate()))
            throw new BadCredentialsException("overdue");
        Status status = statusRepository.findByName(Estatus.Cancel.toString())
                .orElseThrow(() -> new ValidationException(Collections.singletonMap("id order",
                        Estatus.Cancel.toString())));
        order.setStatus(status);

        ordersRepository.save(order);

        return true;
    }

}
