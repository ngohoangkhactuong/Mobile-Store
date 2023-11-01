package com.seo.mobilestore.data.dto.order;

import com.seo.mobilestore.data.dto.address.AddressDTO;
import com.seo.mobilestore.data.dto.product.ShowProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowOrderDetailsDTO{

    private long id;
    private OrderDTO orderDTO;
    private int quantity;
    private AddressDTO address;
    private List<ShowProductOrderDTO> orderProductDTOList;
}
