package com.seo.mobilestore.data.mapper;


import com.seo.mobilestore.data.dto.promotion.PromotionCreationDTO;
import com.seo.mobilestore.data.dto.promotion.PromotionDTO;
import com.seo.mobilestore.data.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StatusMapper.class})
public interface PromotionMapper {

    @Mapping(source = "discountCodeDTO", target = "discountCode")
    @Mapping(source = "totalPurchaseDTO", target = "totalPurchase")
    @Mapping(source = "discountDTO", target = "discount")
    @Mapping(source = "maxGetDTO", target = "maxGet")
    @Mapping(source = "expireDateDTO", target = "expireDate")
    Promotion toEntity(PromotionCreationDTO promotionCreationDTO);

    @Mapping(source = "discountCode", target = "discountCodeDTO")
    @Mapping(source = "totalPurchase", target = "totalPurchaseDTO")
    @Mapping(source = "discount", target = "discountDTO")
    @Mapping(source = "maxGet", target = "maxGetDTO")
    @Mapping(source = "expireDate", target = "expireDateDTO")
    @Mapping(source = "campagn", target = "campagnDTO")
    @Mapping(source = "status", target = "statusDTO")
    PromotionDTO toDTO(Promotion promotion);
}
