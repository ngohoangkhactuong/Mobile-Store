package com.seo.mobilestore.service.impl.product;

import com.seo.mobilestore.common.enumeration.ENum;
import com.seo.mobilestore.data.dto.ImageDTO;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.*;
import com.seo.mobilestore.data.dto.product.color.ColorDTO;
import com.seo.mobilestore.data.dto.product.memory.MemoryDTO;
import com.seo.mobilestore.data.dto.product.review.ReviewDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriDTO;
import com.seo.mobilestore.data.entity.Categories;
import com.seo.mobilestore.data.entity.Manufacturer;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.entity.Review;
import com.seo.mobilestore.data.mapper.product.*;
import com.seo.mobilestore.data.repository.*;
import com.seo.mobilestore.exception.CannotDeleteException;
import com.seo.mobilestore.exception.ConflictException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.*;
import com.seo.mobilestore.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
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
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageReponsetory;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoriesMapper categoriesMapper;
    @Autowired
    private ManufacturerMapper manufacturerMapper;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private ProductTechMapper productTechMapper;
    @Autowired
    private ReviewMapper reviewMapper;



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

    @Override
    @Transactional
    public ProductDTO update(long id, ProductDTO productDTO, List<MultipartFile> fileImages) {

        Map<String, Object> errors = new HashMap<>();

        //create a variable ImageDTO list to include image filename
        List<ImageDTO> imageDTOs = new ArrayList<>();

        //Check if the product is already in the database before updating
        Product product = productRepository.findById(id).orElseThrow(

                () -> new ResourceNotFoundException(Collections.singletonMap("ProductDTO", id)));

        //Check All file input is null or not null
        // If null then not update
        //If file name is null then add old image into imageDTO
        if (fileImages != null) {

            fileImages.forEach(file -> {
                String fileName = fileService.uploadFile(file);

                //Check file name of file input
                // If file name is null then add old image into imageDTO
                if (fileName == null) {
                    imageReponsetory.findByProductId(product.getId()).forEach(image -> {
                        imageDTOs.add(imageMapper.toDTO(image));
                    });
                }

                //file name is not null then add image into image DTO to update new image
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setName(fileName);
                imageDTOs.add(imageDTO);
            });
        } else {

            imageReponsetory.findByProductId(product.getId()).forEach(image -> {

                imageDTOs.add(imageMapper.toDTO(image));
            });
        }

        productDTO.setImageDTOs(imageDTOs);

        //check input of productDTO
        checkInputProductDTO(errors, productDTO);

        // Check if exists errors then throw object errors with message errors
        if (errors.size() > ENum.ZERO.getValue())

            throw new ConflictException(Collections.singletonMap("ProductDTO", errors));

        // create a variable Product will be updated into database
        Product productUpdated = productMapper.toEntity(productDTO);
        productUpdated.setId(product.getId());

        Categories categories = categoriesMapper.toEntity(productDTO.getCategoriesDTO());
        productUpdated.setCategory(categories);

        Manufacturer manufacturer = manufacturerMapper.toEntity(productDTO.getManufacturerDTO());
        productUpdated.setManufacturer(manufacturer);

        Product productUpdateResult;

        //Check if productDTO name is not equals product existed in database
        if (!product.getName().equals(productDTO.getName())) {

            // Check productDTO name with orther product in database
            if (productRepository.existsByName(productDTO.getName()))

                errors.put("product name existed", productDTO.getName());

            if (errors.size() > ENum.ZERO.getValue())

                throw new ConflictException(Collections.singletonMap("ProductDTO", errors));
            productUpdateResult = productRepository.save(productUpdated);

            return updateDTOsForProduct(productUpdateResult, productDTO);
        }

        if (product.getReviews() != null) {

            productUpdated.setReviews(product.getReviews());

        }

        // ProductDTO name equals Old Product name and unique
        productUpdateResult = productRepository.save(productUpdated);

        return updateDTOsForProduct(productUpdateResult, productDTO);
    }

    public ProductDTO updateDTOsForProduct(Product product, ProductDTO productDTO) {

        // update lists DTO of product
        List<ColorDTO> colorDTOs = colorService.updateProductColor(product, productDTO.getColorDTOs());
        List<ImageDTO> imageDTOs = imageService.updateProductImage(product, productDTO.getImageDTOs());
        List<MemoryDTO> memoryDTOs = memoryService.updateProductMemory(product, productDTO.getMemoryDTOs());
        List<SeriDTO> seriDTOs = seriService.updateProductSeri(product, productDTO.getSeriDTOs());
        List<ProductTechDTO> productTechDTOs = productTechService.updateProductTech(product, productDTO.getProductTechDTOs());

        ProductDTO productDTOResult = productMapper.toDTO(product);
        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        if (product.getReviews() != null) {

            for (Review review : product.getReviews()) {

                if (review.isStatus()) {

                    reviewDTOList.add(reviewMapper.toDTO(review));
                }
            }
        }
        productDTOResult.setReviewDTOs(reviewDTOList);

        // set lists DTO for productDTO view
        productDTOResult.setColorDTOs(colorDTOs);
        productDTOResult.setImageDTOs(imageDTOs);
        productDTOResult.setMemoryDTOs(memoryDTOs);
        productDTOResult.setSeriDTOs(seriDTOs);
        productDTOResult.setProductTechDTOs(productTechDTOs);


        return productDTOResult;
    }


    public PaginationDTO getAllPagination(int no, int limit){
        Page<ProductDTO> page = this.productRepository.findAll(
                PageRequest.of(no, limit)).map(item -> productMapper.toDTO(item));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }

    @Override
    public PaginationDTO findAllActiveProduct(int no, int limit) {

        Page<ProductDTO> page = productRepository.findAllActive(PageRequest.of(no, limit))
                .map(item -> {

                    ProductDTO productDTO = productMapper.toDTO(item);

                    List<ReviewDTO> reviewDTOS = new ArrayList<>();

                    item.getReviews().forEach(review -> {

                        if (review.isStatus()) {

                            reviewDTOS.add(reviewMapper.toDTO(review));
                        }
                    });

                    productDTO.setReviewDTOs(reviewDTOS);

                    List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                    item.getProductTechs().forEach(productTech -> {

                        productTechDTOs.add(productTechMapper.toDTO(productTech));
                    });

                    productDTO.setProductTechDTOs(productTechDTOs);

                    return productDTO;

                });

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());
    }

    public PaginationDTO searchProduct(String keyword, int no, int limit) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Page<ShowProductDTO> page;

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Role_Admin"))) {

            page = productRepository.findProductByKeywordAdmin(keyword, PageRequest.of(no, limit)).map(
                    product -> productMapper.toShowProductDTO(product));

        } else {

            page = productRepository.findProductByKeywordCustomer(keyword, PageRequest.of(no, limit)).map(
                    product -> productMapper.toShowProductDTO(product));
        }

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());

    }


    @Override
    public boolean deleteById(long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        if (!product.isStatus()) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        //Check if the promotion exists in any orders
        if (ordersRepository.existsByPromotionId(id)) {

            throw new CannotDeleteException(id);
        }

        product.setColors(null);
        product.setProductTechs(null);
        product.setImages(null);
        product.setSeries(null);
        product.setReviews(null);
        product.setStatus(false);
        product.setMemories(null);
        this.productRepository.save(product);

        return true;
    }


    /**
     * Method show new Product
     */
    @Override
    public List<ProductDTO>  findNewProduct() {

        List<ProductDTO> productDTOList = productRepository.findNewProductWithLimit()
                .stream().map(item -> {
                    ProductDTO productDTO = productMapper.toDTO(item);

                    List<ReviewDTO> reviewDTOS = new ArrayList<>();

                    item.getReviews().forEach(review -> {
                        if (review.isStatus()) {

                            reviewDTOS.add(reviewMapper.toDTO(review));
                        }
                    });

                    productDTO.setReviewDTOs(reviewDTOS);

                    List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                    item.getProductTechs().forEach(productTech -> {
                        productTechDTOs.add(productTechMapper.toDTO(productTech));
                    });

                    productDTO.setProductTechDTOs(productTechDTOs);

                    return productDTO;

                }).collect(Collectors.toList());

        return productDTOList;
    }

    /**
     * Method Show list product by category
     */
    @Override
    public PaginationDTO showListProductPagination(long categoryId, int no, int limit) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Page<ProductDTO> page;

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Role_Admin"))) {

            page = productRepository.showListProductPaginationAdmin(categoryId,
                            PageRequest.of(no, limit))
                    .map(item -> {

                        ProductDTO productDTO = productMapper.toDTO(item);

                        List<ReviewDTO> reviewDTOS = new ArrayList<>();

                        item.getReviews().forEach(review -> {

                            if (review.isStatus()) {

                                reviewDTOS.add(reviewMapper.toDTO(review));
                            }
                        });

                        productDTO.setReviewDTOs(reviewDTOS);

                        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                        item.getProductTechs().forEach(productTech -> {

                            productTechDTOs.add(productTechMapper.toDTO(productTech));
                        });

                        productDTO.setProductTechDTOs(productTechDTOs);

                        return productDTO;

                    });
        } else {
            page = productRepository.showListProductPaginationCustomer(categoryId,
                            PageRequest.of(no, limit))
                    .map(item -> {

                        ProductDTO productDTO = productMapper.toDTO(item);

                        List<ReviewDTO> reviewDTOS = new ArrayList<>();

                        item.getReviews().forEach(review -> {

                            if (review.isStatus()) {

                                reviewDTOS.add(reviewMapper.toDTO(review));
                            }
                        });

                        productDTO.setReviewDTOs(reviewDTOS);

                        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                        item.getProductTechs().forEach(productTech -> {
                            productTechDTOs.add(productTechMapper.toDTO(productTech));
                        });

                        productDTO.setProductTechDTOs(productTechDTOs);

                        return productDTO;

                    });
        }

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());
    }

    @Override
    public PaginationDTO showProductByCategoryAndManufacturer(long category_id, long manufacturer_id,
                                                              int no, int limit) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Page<ProductDTO> page;

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Role_Admin"))) {

            page = productRepository.showListProductByCategoryAndManufacturerAdmin(category_id,
                            manufacturer_id, PageRequest.of(no, limit))
                    .map(item -> {

                        ProductDTO productDTO = productMapper.toDTO(item);

                        List<ReviewDTO> reviewDTOS = new ArrayList<>();

                        item.getReviews().forEach(review -> {

                            if (review.isStatus()) {

                                reviewDTOS.add(reviewMapper.toDTO(review));
                            }
                        });
                        productDTO.setReviewDTOs(reviewDTOS);

                        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                        item.getProductTechs().forEach(productTech -> {
                            productTechDTOs.add(productTechMapper.toDTO(productTech));
                        });

                        productDTO.setProductTechDTOs(productTechDTOs);

                        return productDTO;
                    });


        } else {
            page = productRepository.showListProductByCategoryAndManufacturerCustomer(category_id,
                            manufacturer_id, PageRequest.of(no, limit))
                    .map(item -> {
                        ProductDTO productDTO = productMapper.toDTO(item);

                        List<ReviewDTO> reviewDTOS = new ArrayList<>();

                        item.getReviews().forEach(review -> {

                            if (review.isStatus()) {

                                reviewDTOS.add(reviewMapper.toDTO(review));
                            }
                        });
                        productDTO.setReviewDTOs(reviewDTOS);

                        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                        item.getProductTechs().forEach(productTech -> {
                            productTechDTOs.add(productTechMapper.toDTO(productTech));
                        });

                        productDTO.setProductTechDTOs(productTechDTOs);

                        return productDTO;

                    });
        }

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());
    }

    /**
     * Method related products by category and manufacturer (default = 6)
     * */

    @Override
    public List<ShowProductRelated> showRelatedProduct(long productId, int quantity) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ConflictException(Collections.singletonMap("id", productId))
        );

        long categoryId = product.getCategory().getId();
        long manufacturerId = product.getManufacturer().getId();

        Page<Product> products = productRepository.showListProductByCategoryAndManufacturerAdmin(
                categoryId, manufacturerId, PageRequest.of(0, quantity));

        List<ShowProductRelated> productsRelated = new ArrayList<>();

        for (Product prod : products.getContent()) {

            if (!prod.getName().equals(product.getName())) {

                productsRelated.add(setProductToShowProductRelatedDTO(prod)); //convert to dto
            }

        }


        return productsRelated;
    }


    public ShowProductRelated setProductToShowProductRelatedDTO(Product product) {

        ShowProductRelated showProductDTO = new ShowProductRelated();
        showProductDTO.setId(product.getId());
        showProductDTO.setName(product.getName());
        showProductDTO.setPrice(product.getPrice());
        showProductDTO.setDescription(product.getDescription());

        List<ImageDTO> list = new ArrayList<>();
        product.getImages().forEach(
                item -> {
                    ImageDTO imageDTO = imageMapper.toDTO(item);
                    list.add(imageDTO);
                }
        );
        showProductDTO.setImageDTOs(list);

        return showProductDTO;

    }

    /**
     * Method show detail product and increase view
     */
    public ProductDTO showDetailProduct(long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("product Id", id)));

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Role_Customer")) &
                !product.isStatus()) {

            throw new ResourceNotFoundException(Collections.singletonMap("id", product.getId()));
        }

        int views = product.getViews();
        final int plus = 1;
        product.setViews(views + plus);

        Product productSaved = productRepository.save(product);
        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

        productSaved.getProductTechs().forEach(productTech -> {

            productTechDTOs.add(productTechMapper.toDTO(productTech));
        });

        ProductDTO productDTO = this.productMapper.toDTO(product);

        productDTO.setProductTechDTOs(productTechDTOs);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();

        productSaved.getReviews().forEach(review -> {

            if (review.isStatus()) {

                reviewDTOS.add(reviewMapper.toDTO(review));

            }
        });

        int sum = 0;
        for (Review review : productSaved.getReviews()) {
            sum += review.getRating();
        }

        float star = 0;
        if ((productSaved.getReviews().size()) != 0) {
            star = ((float) sum ) / (productSaved.getReviews().size());

        }
        productDTO.setReviewDTOs(reviewDTOS);
        productDTO.setStar((float) Math.round(star * 10) / 10);

        return productDTO;
    }

    /**
     * method search products by many params
     */
    @Override
    public PaginationDTO showListProductFilter(FilterProductDTO filterProductDTO, int no, int limit) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Page<ProductDTO> page;


        int manufacturerId = filterProductDTO.getManufactureId();
        int categoryId = filterProductDTO.getCategoryId();
        String keyword = filterProductDTO.getKeyword();
        BigDecimal lowerPrice = filterProductDTO.getLowerPrice();
        BigDecimal higherPrice = filterProductDTO.getHigherPrice();

        if (higherPrice == null) {
            higherPrice = BigDecimal.valueOf(10000000);
        }


        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Role_Admin"))) {

            page = productRepository
                    .findProductsByFullFilterAdmin(manufacturerId, categoryId, keyword, lowerPrice, higherPrice, PageRequest.of(no, limit))

                    .map(item -> {
                        ProductDTO productDTO = productMapper.toDTO(item);

                        List<ReviewDTO> reviewDTOS = new ArrayList<>();
                        item.getReviews().forEach(review -> {

                            if (review.isStatus()) {

                                reviewDTOS.add(reviewMapper.toDTO(review));
                            }
                        });

                        productDTO.setReviewDTOs(reviewDTOS);

                        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                        item.getProductTechs().forEach(productTech -> {
                            productTechDTOs.add(productTechMapper.toDTO(productTech));
                        });

                        productDTO.setProductTechDTOs(productTechDTOs);

                        return productDTO;
                    });


        } else {
            page = productRepository

                    .findProductsByFullFilterCustomer(manufacturerId, categoryId, keyword, lowerPrice, higherPrice,
                            PageRequest.of(no, limit))

                    .map(item -> {
                        ProductDTO productDTO = productMapper.toDTO(item);

                        List<ReviewDTO> reviewDTOS = new ArrayList<>();

                        item.getReviews().forEach(review -> {

                            if (review.isStatus()) {

                                reviewDTOS.add(reviewMapper.toDTO(review));
                            }
                        });

                        productDTO.setReviewDTOs(reviewDTOS);

                        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                        item.getProductTechs().forEach(productTech -> {

                            productTechDTOs.add(productTechMapper.toDTO(productTech));
                        });

                        productDTO.setProductTechDTOs(productTechDTOs);

                        return productDTO;

                    });
        }

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());
    }


    public ProductDTO setProductTechDTOforProductDTO(Product product) {

        ProductDTO productDTO = productMapper.toDTO(product);
        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

        product.getProductTechs().forEach(productTech -> {

            ProductTechDTO productTechDTO = productTechMapper.toDTO(productTech);
            productTechDTOs.add(productTechDTO);
        });

        productDTO.setProductTechDTOs(productTechDTOs);

        return productDTO;

    }


    /**
     * Method show active product by category
     */
    @Override
    public PaginationDTO showActiveProductByCategory(long id, int no, int limit) {

        Categories categories = categoriesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id))
        );

        Page<ProductDTO> page = productRepository
                .findActiveProductByCategory(categories.getId(), PageRequest.of(no, limit))
                .map(item -> {

                    ProductDTO productDTO = productMapper.toDTO(item);

                    List<ReviewDTO> reviewDTOS = new ArrayList<>();

                    item.getReviews().forEach(review -> {

                        if (review.isStatus()) {

                            reviewDTOS.add(reviewMapper.toDTO(review));
                        }
                    });

                    productDTO.setReviewDTOs(reviewDTOS);

                    List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                    item.getProductTechs().forEach(productTech -> {

                        productTechDTOs.add(productTechMapper.toDTO(productTech));
                    });

                    productDTO.setProductTechDTOs(productTechDTOs);

                    return productDTO;

                });

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());
    }

    /*
     *  Method show active product by manufacturer
     * */
    @Override
    public PaginationDTO showActiveProductByManufacturer(long id, int no, int limit) {

        Manufacturer manufacturer = manufacturerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id))
        );

        Page<ProductDTO> page = productRepository
                .findActiveProductByManufacturer(manufacturer.getId(), PageRequest.of(no, limit))
                .map(item -> {

                    ProductDTO productDTO = productMapper.toDTO(item);

                    List<ReviewDTO> reviewDTOS = new ArrayList<>();

                    item.getReviews().forEach(review -> {

                        if (review.isStatus()) {

                            reviewDTOS.add(reviewMapper.toDTO(review));
                        }
                    });

                    productDTO.setReviewDTOs(reviewDTOS);

                    List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                    item.getProductTechs().forEach(productTech -> {

                        productTechDTOs.add(productTechMapper.toDTO(productTech));
                    });

                    productDTO.setProductTechDTOs(productTechDTOs);

                    return productDTO;

                });

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());

    }


    /**
     * Method filter product by price
     */
    @Override
    public PaginationDTO FilterProductsByPrice(BigDecimal lowerPrice, BigDecimal higherPrice, int no,
                                               int limit) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Page<ProductDTO> page;

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Role_Admin"))) {

            page = productRepository.FilterProductsByPriceAdmin(
                            lowerPrice, higherPrice, PageRequest.of(no, limit))
                    .map(item -> {

                        ProductDTO productDTO = productMapper.toDTO(item);

                        List<ReviewDTO> reviewDTOS = new ArrayList<>();

                        item.getReviews().forEach(review -> {

                            if (review.isStatus()) {

                                reviewDTOS.add(reviewMapper.toDTO(review));
                            }
                        });

                        productDTO.setReviewDTOs(reviewDTOS);

                        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                        item.getProductTechs().forEach(productTech -> {

                            productTechDTOs.add(productTechMapper.toDTO(productTech));
                        });

                        productDTO.setProductTechDTOs(productTechDTOs);

                        return productDTO;

                    });
        } else {
            page = productRepository.FilterProductsByPriceCustomer(
                            lowerPrice, higherPrice, PageRequest.of(no, limit))
                    .map(item -> {

                        ProductDTO productDTO = productMapper.toDTO(item);

                        List<ReviewDTO> reviewDTOS = new ArrayList<>();

                        item.getReviews().forEach(review -> {

                            if (review.isStatus()) {

                                reviewDTOS.add(reviewMapper.toDTO(review));
                            }
                        });

                        productDTO.setReviewDTOs(reviewDTOS);

                        List<ProductTechDTO> productTechDTOs = new ArrayList<>();

                        item.getProductTechs().forEach(productTech -> {

                            productTechDTOs.add(productTechMapper.toDTO(productTech));
                        });

                        productDTO.setProductTechDTOs(productTechDTOs);

                        return productDTO;

                    });
        }


        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());
    }

}
