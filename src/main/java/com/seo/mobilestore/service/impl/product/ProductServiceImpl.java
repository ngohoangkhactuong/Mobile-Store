package com.seo.mobilestore.service.impl.product;

import com.seo.mobilestore.data.dto.ImageDTO;
import com.seo.mobilestore.data.dto.product.ProductDTO;
import com.seo.mobilestore.data.dto.product.ProductTechDTO;
import com.seo.mobilestore.data.dto.product.color.ColorDTO;
import com.seo.mobilestore.data.dto.product.memory.MemoryDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriDTO;
import com.seo.mobilestore.data.entity.Categories;
import com.seo.mobilestore.data.entity.Manufacturer;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.mapper.product.CategoriesMapper;
import com.seo.mobilestore.data.mapper.product.ManufacturerMapper;
import com.seo.mobilestore.data.mapper.product.ProductMapper;
import com.seo.mobilestore.data.repository.ProductRepository;
import com.seo.mobilestore.exception.ConflictException;
import com.seo.mobilestore.service.*;
import com.seo.mobilestore.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoriesMapper  categoriesMapper;
    @Autowired
    private ManufacturerMapper manufacturerMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private ColorService colorService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private MemoryService memoryService;
    @Autowired
    private ProductTechService productTechService;
    @Autowired
    private SeriService seriService;


    @Override
    @Transactional
    public ProductDTO create(ProductDTO productDTO, List<MultipartFile> fileImages) {

        Map<String, Object> errors = new HashMap<>();
        Product product = productMapper.toEntity(productDTO);

        List<ImageDTO> imageDTOs = new ArrayList<>();
        if (fileImages != null) {

            fileImages.forEach(file -> {

                String fileName = fileService.uploadFile(file);
                if (fileName == null) {

                    errors.put("Images not be null!", productDTO.getName());
                }

                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setName(fileName);
                imageDTOs.add(imageDTO);
            });

            productDTO.setImageDTOs(imageDTOs);

        } else {
            errors.put("Images not be null!", productDTO.getName());
        }

        if (productRepository.existsByName(productDTO.getName())){
            errors.put("product name", productDTO.getName());
        }

        checkInputProductDTO(errors, productDTO);

        Categories categories = categoriesMapper.toEntity(productDTO.getCategoriesDTO());
        product.setCategory(categories);

        Manufacturer manufacturer = manufacturerMapper.toEntity(productDTO.getManufacturerDTO());
        product.setManufacturer(manufacturer);

        Product new_product = productRepository.save(product);
        return createDTOsForProduct(new_product, productDTO);
    }

    public void checkInputProductDTO(Map<String, Object> errors, ProductDTO productDTO) {
        if (productDTO == null) {
            errors.put("productDTO", "ProductDTO should not be null!");
            return;  // Stop further checks if productDTO is null
        }

        checkNotNull(errors, productDTO.getCategoriesDTO(), "Categories");
        checkNotNull(errors, productDTO.getManufacturerDTO(), "Manufacturer");
        checkCollectionNotEmpty(errors, productDTO.getColorDTOs(), "Colors");
        checkCollectionNotEmpty(errors, productDTO.getMemoryDTOs(), "Memories");
        checkCollectionNotEmpty(errors, productDTO.getSeriDTOs(), "Series");
        checkCollectionNotEmpty(errors, productDTO.getProductTechDTOs(), "ProductTechs");

        if (!errors.isEmpty()) {
            throw new ConflictException(Collections.singletonMap("ProductDTO", errors));
        }
    }

    private void checkNotNull(Map<String, Object> errors, Object field, String fieldName) {
        if (field == null) {
            errors.put(fieldName, fieldName + " should not be null!");
        }
    }

    private void checkCollectionNotEmpty(Map<String, Object> errors, Collection<?> collection, String fieldName) {
        if (collection == null || collection.isEmpty()) {
            errors.put(fieldName, fieldName + " should not be null or empty!");
        }
    }


    public ProductDTO createDTOsForProduct(Product product, ProductDTO productDTO) {
        // create lists DTO of product
        List<ColorDTO> colorDTOs = colorService.createProductColor(product, productDTO.getColorDTOs());
        List<ImageDTO> imageDTOs = imageService.createProductImage(product, productDTO.getImageDTOs());
        List<MemoryDTO> memoryDTOs = memoryService.createProductMemory(product, productDTO.getMemoryDTOs());
        List<SeriDTO> seriDTOs = seriService.createProductSeri(product, productDTO.getSeriDTOs());
        List<ProductTechDTO> productTechDTOs = productTechService.createProductTech(product, productDTO.getProductTechDTOs());

        ProductDTO productDTOResult = productMapper.toDTO(product);
        // set DTOs information for productDTO view
        productDTOResult.setColorDTOs(colorDTOs);
        productDTOResult.setImageDTOs(imageDTOs);
        productDTOResult.setMemoryDTOs(memoryDTOs);
        productDTOResult.setSeriDTOs(seriDTOs);
        productDTOResult.setProductTechDTOs(productTechDTOs);

        return productDTOResult;
    }
}
