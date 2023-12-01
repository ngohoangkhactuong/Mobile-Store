package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.common.enumeration.ENum;
import com.seo.mobilestore.common.enumeration.EPaymentMethod;
import com.seo.mobilestore.common.enumeration.Estatus;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.cart.CartDTO;
import com.seo.mobilestore.data.dto.order.*;
import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import com.seo.mobilestore.data.dto.product.ShowProductOrderDTO;
import com.seo.mobilestore.data.entity.*;
import com.seo.mobilestore.data.mapper.CartMapper;
import com.seo.mobilestore.data.mapper.OrderMapper;
import com.seo.mobilestore.data.mapper.address.AddressMapper;
import com.seo.mobilestore.data.repository.*;
import com.seo.mobilestore.exception.CannotDeleteException;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.exception.ValidationException;
import com.seo.mobilestore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private CartMapper cartMapper;

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

        String payment_method = orderCreationDTO.getPayment_method();
        PaymentMethod paymentMethod = paymentMethodRepository.findByName(payment_method).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("Not found",payment_method)));
        order.setPaymentMethod(paymentMethod);


        String status_name = orderCreationDTO.getStatusDTO().getName();
        Status status =  statusRepository.findByName(status_name).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("Not found",status_name)));
        order.setStatus(status);
        order.setReceiveDate(java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(0)));
        order.setPaymentStatus(!orderCreationDTO.getPayment_method().equals(EPaymentMethod.Cash.toString()));
        /* ---------------------------------------------------------------------------------------------------- */

        OrderDTO orderDTO = orderMapper.toDTO(ordersRepository.save(order));

        /*------------------------------------ Xử lý OrderDetail ---------------------------------------------*/
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrderDTO(orderDTO);
        orderDetailDTO.setOrderProductDTOList(orderCreationDTO.getOrderProductDTOList());
        orderDetailDTO.setQuantity(orderCreationDTO.getOrderProductDTOList().size());

        Cart cart = cartRepository.findCartByUserId(user.getId());
        List<CartDetails> cartDetailsList = cartDetailRepository.findAllByCartId(cart.getId());

        for(int i = 0; i < orderCreationDTO.getOrderProductDTOList().size(); i++){

            String memory_name = orderCreationDTO.getOrderProductDTOList().get(i).getMemory();
            String seri_name = orderCreationDTO.getOrderProductDTOList().get(i).getSeri();

            if(!Objects.equals(memory_name, cartDetailsList.get(i).getMemory().getName())){
                throw new ResourceNotFoundException(Collections.singletonMap("Memory of product not in cart",memory_name));
            }

            if(!Objects.equals(seri_name, cartDetailsList.get(i).getSeri().getName())){
                throw new ResourceNotFoundException(Collections.singletonMap("Seri of product not in cart" , seri_name));
            }

            String product_name = orderCreationDTO.getOrderProductDTOList().get(i).getName();

            OrderDetails orderDetails = orderMapper.toDetailEnity(orderDetailDTO);
            orderDetails.setQuantity((int)orderCreationDTO.getOrderProductDTOList().get(i).getQuantity());

            Memory memory = memoryRepository.findMemoryByNameAndProductName(
                    memory_name , product_name
            ).orElseThrow(() ->
                    new ResourceNotFoundException(Collections.singletonMap("Not found",memory_name)));
            orderDetails.setMemory(memory);

            Seri seri = seriRepository.findSeriByNameAndProductName(
                    seri_name , product_name
            ).orElseThrow((()->
                    new ResourceNotFoundException(Collections.singletonMap("Not found" , seri_name))
                    ));
            orderDetails.setSeri(seri);

            Color color = colorRepository.findByName(seri.getColor().getName());
            orderDetails.setColor(color);

            orderDetails.setAddress(addressRepository.findAddressById(orderCreationDTO.getId_address())
                    .orElse(addressRepository.findDefaultAddress(user.getId())));

            orderDetailRepository.save(orderDetails);
        }

        // delete product from cart after ordered
        cartDetailRepository.deleteAll(cartDetailsList);
        cartRepository.delete(cart);

        return orderDetailDTO;
    }


    /**
     * function calculate total price after apply discount
     * */
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
    public boolean deleteOrderByIdUser(long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        //Get current user's order
        Orders order = ordersRepository.findOrderByUserId(user.getId(), id);

        //Order don't exist
        if (order == null) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        //Order has been paid or transfer (Need to have payment_method name = "Cash" in database and paymentStatus is not NULL)
        if (order.isPaymentStatus() || !order.getPaymentMethod().getName().equals("Cash")) {
            throw new InternalServerErrorException(this.messageSource.getMessage("error.orderHasBeenPaid", null, null));
        }

        //Order is not ready (Need to have status name = "Default" in database)
        if (!order.getStatus().getName().equals("Active")) {
            throw new InternalServerErrorException(String.format("Your order is %s", order.getStatus().getName()));
        }

        //Set status "Cancel" for order
        order.setStatus(statusRepository.findByName("Cancel").get());

        this.ordersRepository.save(order);

        return true;

    }

    /*
     *  Show detail order by order id
    **/
    public ShowOrderDetailsDTO getOrderDetailsDTO(long orderId) {
        int defaultNum = 0;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        int roleAdmin = 1;
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        if (user.getRole().getId() == roleAdmin) {

            orderDetailsList = this.orderDetailRepository.findAllByOrderId(orderId);

            user = orderDetailsList.get(defaultNum).getOrders().getUser();

        } else {

            orderDetailsList = this.orderDetailRepository
                    .findAllByOrderIdAndUserId(orderId, user.getId());
        }

        ShowOrderDetailsDTO orderDetailsDTO = new ShowOrderDetailsDTO();

        if (orderDetailsList.isEmpty()) {

            throw new ResourceNotFoundException(Collections.singletonMap("id", orderId));
        }

        List<ShowProductOrderDTO> productOrderDTOList = orderDetailsList.stream()
                .map(this::mapToProductOrderDTO)
                .collect(Collectors.toList());

        orderDetailsDTO.setId(orderId);
        orderDetailsDTO.setOrderDTO(orderMapper.toDTO(orderDetailsList.get(defaultNum).getOrders()));
        orderDetailsDTO.setQuantity(orderDetailsList.size());
        orderDetailsDTO.setOrderProductDTOList(productOrderDTOList);

        user.getAddressList().stream()
                .filter(Address::isDefaults)
                .findFirst()
                .ifPresent(address -> orderDetailsDTO.setAddress(addressMapper.toDTO(address)));

        return orderDetailsDTO;
    }


    /*
     *  Show order of user
     **/
    public PaginationDTO showOrderByUser(int no, int limit) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        Page<Orders> page = this.ordersRepository.findAllByUserId(user.getId(), PageRequest.of(no, limit));

        List<ShowOrderDTO> showOrderDTOList = page.getContent().stream().map(this::mapToShowOrderDTO)
                .collect(Collectors.toList());


        for (ShowOrderDTO order : showOrderDTOList) {
            order.setQuantity(orderDetailRepository.CountByOrderId(order.getId()) - 1);
        }

        return new PaginationDTO(showOrderDTOList, page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());
    }


    /*
     *  Show all order (role admin)
     **/
    public PaginationDTO showAllOrderByAdmin(int no, int limit) {

        Page<Orders> page = this.ordersRepository.findAll(PageRequest.of(no, limit));

        List<ShowOrderDTO> showOrderDTOList = page.getContent().stream().map(this::mapToShowOrderDTO)
                .collect(Collectors.toList());

        return new PaginationDTO(showOrderDTOList, page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());
    }
    /*
    *  Update order
    **/
//    public OrderDetailDTO update(long order_id, OrderUpdateDTO orderUpdateDTO) {
//
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByEmail(email).orElseThrow(
//                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
//                        null, null)));
//
//        long userID = user.getId();
//
//        Orders oldOrder = ordersRepository.findById(order_id).orElseThrow(
//                () -> new ResourceNotFoundException(Collections.singletonMap("id", order_id))
//        );
//
//        // Check if the authenticated user is authorized to update this order
//        if (oldOrder.getUser().getId() != userID) {
//            throw new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
//                    null, null));
//        } else {
//
//            Orders updatedOrder = orderMapper.toOrderUpdateEntity(orderUpdateDTO);
//            updatedOrder.setId(oldOrder.getId());
//            updatedOrder.setUser(user);
//            Date date = oldOrder.getReceiveDate();
//            updatedOrder.setReceiveDate(date);
//
//            Promotion updatedPromotion = promotionRepository.findById(orderUpdateDTO.getIdPromotion()).orElseThrow(
//                    () -> new ResourceNotFoundException(Collections.singletonMap("Promotion ID",
//                            orderUpdateDTO.getIdPromotion()))
//            );
//            updatedOrder.setPromotion(updatedPromotion);
//            updatedOrder.setStatus(oldOrder.getStatus());
//
//            // Update order payment method
//            // Fetch the corresponding PaymentMethod entity using the payment method name from the orderUpdateDTO
//            if (!oldOrder.getPaymentMethod().getName().equals(EPaymentMethod.Cash.toString()) &&
//                    orderUpdateDTO.getPaymentMethodDTO().equals(EPaymentMethod.Cash.toString())) {
//                throw new IllegalStateException("Update not allowed for non-cash payment method.");
//            } else {
//                String payment_method = orderUpdateDTO.getPaymentMethodDTO();
//                updatedOrder.setPaymentMethod(paymentMethodRepository.
//                        findByName(payment_method).orElseThrow(
//                        () -> new ValidationException(
//                                Collections.singletonMap("payment method", payment_method)))
//                );
//            }
//
//            // Update order payment status based on payment method
//            // If the payment method is not "Cash", set the payment status to true (payment made), otherwise set it to false
//            updatedOrder.setPaymentStatus(!orderUpdateDTO.getPaymentMethodDTO().equals(EPaymentMethod.Cash.toString()));
//
//            BigDecimal total = new BigDecimal(ENum.ZERO.getValue());
//            for (ProductOrderDTO productOrderDTO : orderUpdateDTO.getOrderProductDTOList()) {
//                total = total.add(productOrderDTO.getPrice());
//            }
//            total = totalProduct(total, updatedPromotion.getDiscount(), updatedPromotion.getTotalPurchase(),
//                    updatedPromotion.getMaxGet());
//            updatedOrder.setTotal(total);
//
//            OrderDTO updatedOrderDTO = orderMapper.toDTO(ordersRepository.save(updatedOrder));
//
//            List<OrderDetails> oldOrderDetails = orderDetailRepository.findAllByOrderId(order_id);
//            long id = oldOrderDetails.get(0).getId();
//            if (!oldOrderDetails.isEmpty()) {
//                oldOrderDetails.forEach(oldOrderDetail -> {
//                    orderDetailRepository.deleteById(oldOrderDetail.getId());
//                });
//
//            }
//            String tableName = "order_details";
//            resetAutoIncrement(tableName, id);
//
//            OrderDetailDTO newOrderDetailDTO = new OrderDetailDTO();
//            newOrderDetailDTO.setOrderDTO(updatedOrderDTO);
//            newOrderDetailDTO.setQuantity(orderUpdateDTO.getOrderProductDTOList().size());
//            newOrderDetailDTO.setOrderProductDTOList(orderUpdateDTO.getOrderProductDTOList());
//
//            for (int i = 0; i < orderUpdateDTO.getOrderProductDTOList().size(); i++) {
//
//                String memory_name = orderUpdateDTO.getOrderProductDTOList().get(i).getMemory();
//                String seri_name = orderUpdateDTO.getOrderProductDTOList().get(i).getSeri();
//                String product_name = orderUpdateDTO.getOrderProductDTOList().get(i).getName();
//
//
//                OrderDetails orderDetails = orderMapper.toDetailEnity(newOrderDetailDTO);
//                orderDetails.setQuantity(ENum.ONE.getValue());
//
//                orderDetails.setAddress(addressRepository.findAddressById(orderUpdateDTO.getIdAddress())
//                        .orElse(addressRepository.findDefaultAddress(userID)));
//
//                Seri seri = seriRepository.findSeriByNameAndProductName(
//                        seri_name , product_name
//                ).orElseThrow((()->
//                        new ResourceNotFoundException(Collections.singletonMap("Not found" , seri_name))
//                ));
//                orderDetails.setSeri(seri);
//                orderDetails.setColor(colorRepository.findByName(seri.getColor().getName()));
//
//                orderDetails.setMemory(
//                       memoryRepository.findMemoryByNameAndProductName(
//                                memory_name , product_name
//                        ).orElseThrow(() ->
//                                new ResourceNotFoundException(Collections.singletonMap("Not found",memory_name)))
//                );
//
//                orderDetails.setAddress(addressRepository.findAddressById(
//                        orderUpdateDTO.getIdAddress()).orElse(addressRepository.findDefaultAddress(userID)));
//
//
//                orderDetailRepository.save(orderDetails);
//            }
//
//            return newOrderDetailDTO;
//        }
//    }

    private void resetAutoIncrement(String tableName, long nextId) {
        String query = "ALTER TABLE " + tableName + " AUTO_INCREMENT = " + nextId;
        entityManager.createNativeQuery(query).executeUpdate();
    }

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

    public ShowOrderDTO mapToShowOrderDTO(Orders order) {
//        int defaultNum = 0;
        ShowOrderDTO showOrderDTO = new ShowOrderDTO();
        ShowOrderDetailsDTO showOrderDetailsDTO = getOrderDetailsDTO(order.getId());

        showOrderDTO = this.orderMapper.toShowOrderDTO(order);
        showOrderDTO.setProductOrderDTO(showOrderDetailsDTO.getOrderProductDTOList());

        return showOrderDTO;
    }

    private ShowProductOrderDTO mapToProductOrderDTO(OrderDetails orderDetail) {
        int defaultNum = 0;
        Product product = orderDetail.getSeri().getProduct();
        ShowProductOrderDTO productOrderDTO = new ShowProductOrderDTO();

        productOrderDTO.setSeri(orderDetail.getSeri().getName());
        productOrderDTO.setMemory(orderDetail.getMemory().getName());
//        productOrderDTO.setColor(orderDetail.getSeri().getColor().getName());
        productOrderDTO.setId(product.getId());
        productOrderDTO.setName(product.getName());
        productOrderDTO.setPrice(product.getPrice());
        productOrderDTO.setDescription(product.getDescription());
        productOrderDTO.setImage(product.getImages().get(defaultNum).getName());
        productOrderDTO.setQuantity(orderDetail.getQuantity());

        return productOrderDTO;
    }

    public BigDecimal totalProduct(BigDecimal total, int discount, BigDecimal totalPurchase, BigDecimal maxGet) {

        if (total.compareTo(totalPurchase) > ENum.ZERO.getValue()) {

            BigDecimal discountBigDecimal = new BigDecimal(discount).divide(new BigDecimal(100));

            BigDecimal discountValue = total.multiply(discountBigDecimal);

            if (discountValue.compareTo(maxGet) > ENum.ZERO.getValue())
                discountValue = maxGet;

            total = total.subtract(discountValue);
        }

        return total;
    }
}
