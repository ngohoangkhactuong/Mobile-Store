package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriProductCreationDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriProductDTO;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.entity.Seri;
import com.seo.mobilestore.data.mapper.product.SeriMapper;
import com.seo.mobilestore.data.repository.ColorRepository;
import com.seo.mobilestore.data.repository.ProductRepository;
import com.seo.mobilestore.data.repository.SeriRepository;
import com.seo.mobilestore.data.repository.UserRepository;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.SeriService;
import com.seo.mobilestore.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class SeriServiceImpl implements SeriService {

    @Autowired
    private SeriMapper seriMapper;
    @Autowired
    private SeriRepository seriRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<SeriDTO> createProductSeri(Product product, List<SeriDTO> seriDTOs) {

        List<SeriDTO> seriDTOsResult = new ArrayList<>();
        seriDTOs.forEach(seriDTO -> {

            Seri seri = seriMapper.toEntity(seriDTO);
            seri.setProduct(product);
            seri.setColor(colorRepository.findByName(seriDTO.getColor_name()));
            seriDTOsResult.add(seriMapper.toDTO(seriRepository.save(seri)));
        });
        return seriDTOsResult;
    }

    @Override
    public List<SeriProductCreationDTO> createProductSeriCreation(Product product, List<SeriProductCreationDTO> seriDTOs) {

        List<SeriProductCreationDTO> seriDTOsResult = new ArrayList<>();
        seriDTOs.forEach(seriProductCreationDTO -> {
            Seri seri = seriMapper.toSeriCreationEntity(seriProductCreationDTO);
            seri.setProduct(product);
            seri.setColor(colorRepository.findByName(seriProductCreationDTO.getColor()));
            seriDTOsResult.add(seriMapper.toSeriProductCreationDTO(seriRepository.save(seri)));
        });
        return seriDTOsResult;
    }

    @Override
    public MessageResponse create(SeriProductDTO seriDTO) {
        Seri seri = seriMapper.toSeriEntity(seriDTO);

        if (seriRepository.existsByName(seri.getName()))

            throw new InternalServerErrorException(
                    String.format("Exists seri named %s", seri.getName()));

        Product product = productRepository.findById(seriDTO.getProduct_id()).orElseThrow();

        seri.setProduct(product);
        seriRepository.save(seri);

        return new MessageResponse(HttpServletResponse.SC_OK, null, null);
    }
    @Override
    public List<SeriProductCreationDTO> updateProductSeri(Product product, List<SeriProductCreationDTO> seriDTOs) {

        List<SeriProductCreationDTO> seriDTOsResult = new ArrayList<>();
        List<Seri> seris = seriRepository.findByProductId(product.getId());
        List<SeriProductCreationDTO> seriDTOsPrepare = new ArrayList<>();

        seris.forEach(seri -> {
            seriDTOsPrepare.add(seriMapper.toSeriProductCreationDTO(seri));
            if (!seriDTOs.contains(seriMapper.toSeriProductCreationDTO(seri))) {
                // Update status = false (0) for seri when consumer delete this seri
                // seri.setStatus(false);
                seriRepository.delete(seri);
            } else {
                seriDTOs.forEach(seriDTO -> {
                    // if seri existed in database, it will be updated then save it
                    if (Objects.equals(seriDTO.getName(), seri.getName())) {
                        Seri seriMapped = seriMapper.toSeriCreationEntity(seriDTO);
                        seriMapped.setProduct(product);
                        seriMapped.setColor(colorRepository.findByName(seriDTO.getColor()));
                        seriDTOsResult.add(seriMapper.toSeriProductCreationDTO(seriRepository.save(seriMapped)));
                    }
                });
            }
        });
        // a foreach to check a seriDTO in list seriDTO have the new seri, yes or no?
        // if existing new seri then save this seri into database
        seriDTOs.forEach(seriDTO -> {
            if (!seriDTOsPrepare.contains(seriDTO)) {
                Seri seriMapped = seriMapper.toSeriCreationEntity(seriDTO);
                seriMapped.setProduct(product);
                seriMapped.setColor(colorRepository.findByName(seriDTO.getColor()));
                Seri seriSaved = seriRepository.save(seriMapped);
                seriDTOsResult.add(seriMapper.toSeriProductCreationDTO(seriSaved));
            }
        });

        return seriDTOsResult;
    }

    @Override
    public SeriProductDTO update(long seriID, SeriProductDTO seriProductDTO) {

        //find seri by seriID .
        Seri oldSeri = seriRepository.findById(seriID).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", seriID))
        );

        Seri updatedSeri = seriMapper.toSeriEntity(seriProductDTO);
        updatedSeri.setId(oldSeri.getId());
        updatedSeri.setStatus(true);

        seriRepository.save(updatedSeri);

        return seriMapper.toSeriProductDTO(updatedSeri);
    }

    public boolean deletedByID(long id) {

        Seri seri = seriRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id))
        );

        // check if seri status is true -> delete
        if (!seri.isStatus()) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        seri.setStatus(false);
        this.seriRepository.save(seri);

        return true;
    }

    @Override
    public SeriDTO getById(long id) {
        Seri seri = seriRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        return seriMapper.toDTO(seri);
    }

    @Override
    public PaginationDTO getAllPagination(int no, int limit) {
        Page<SeriDTO> page = this.seriRepository.findAll(
                PageRequest.of(no, limit)).map(item -> seriMapper.toDTO(item));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }

}

