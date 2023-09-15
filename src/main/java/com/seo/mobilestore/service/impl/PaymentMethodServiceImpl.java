package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.common.enumeration.ENum;
import com.seo.mobilestore.common.enumeration.EPaymentMethod;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.paymentMethod.PaymentMethodDTO;
import com.seo.mobilestore.data.entity.PaymentMethod;
import com.seo.mobilestore.data.mapper.paymentMethod.PaymentMethodMapper;
import com.seo.mobilestore.data.repository.PaymentMethodRepository;
import com.seo.mobilestore.data.repository.UserRepository;
import com.seo.mobilestore.exception.ConflictException;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;


@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;

    public List<PaymentMethodDTO> getAll() {
        return paymentMethodRepository.findByStatusIsTrue().stream().map(u -> paymentMethodMapper.toDTO(u)).collect(Collectors.toList());
    }

    public PaymentMethodDTO getById(long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByIdPagement(id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        return paymentMethodMapper.toDTO(paymentMethod);
    }

    @Override
    public PaginationDTO getAllPagination(int no, int limit) {
        Page<PaymentMethodDTO> page = this.paymentMethodRepository.findAllPagment(
                PageRequest.of(no, limit)).map(item -> paymentMethodMapper.toDTO(item));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }


    @PostConstruct
    public void init() {
        List<PaymentMethod> paymentMethodToSave = new ArrayList<>();
        for (EPaymentMethod ePaymentMethod : EPaymentMethod.values()) {

            if (!paymentMethodRepository.existsByName(ePaymentMethod.toString())) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setName(ePaymentMethod.toString());
                paymentMethodToSave.add(paymentMethod);
            }
        }

        if (!paymentMethodToSave.isEmpty()) {
            paymentMethodRepository.saveAll(paymentMethodToSave);
        }
    }


    public MessageResponse create(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = paymentMethodMapper.toEnity(paymentMethodDTO);

        if (paymentMethodRepository.existsByName(paymentMethod.getName()))
            throw new InternalServerErrorException(
                    String.format("Exists payment method named %s", paymentMethod.getName()));

        paymentMethodRepository.save(paymentMethod);

        return new MessageResponse(HttpServletResponse.SC_OK, null, null);
    }

    @Override
    public PaymentMethodDTO update(long id, PaymentMethodDTO paymentMethodDTO) {

        PaymentMethod oldPaymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        Map<String, Object> errors = new HashMap<>();

        //Check if exits name
        if (paymentMethodRepository.existsByName(paymentMethodDTO.getName())) {
            errors.put("name", paymentMethodDTO.getName());
        }

        //throw conflict exception
        if (errors.size() > ENum.ZERO.getValue()) {
            throw new ConflictException(Collections.singletonMap("paymentMethod", errors));
        }

        PaymentMethod updatePaymentMethod = this.paymentMethodMapper.toEnity(paymentMethodDTO);
        updatePaymentMethod.setId(oldPaymentMethod.getId());

        return this.paymentMethodMapper.toDTO(paymentMethodRepository.save(updatePaymentMethod));
    }


    @Override
    public boolean deleteById(long id) {

        PaymentMethod oldPaymentMethod = this.paymentMethodRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        //Check manufacture deleted
        if (!oldPaymentMethod.isStatus()) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        oldPaymentMethod.setStatus(false);
        this.paymentMethodRepository.save(oldPaymentMethod);

        return true;
    }
}
