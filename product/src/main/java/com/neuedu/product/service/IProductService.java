package com.neuedu.product.service;

import com.neuedu.product.common.ServerResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface IProductService {

    public ServerResponse list( Integer categoryId,
                               String keyword,
                               Integer pageNum,
                               Integer pageSize,
                               String orderby);
    public ServerResponse findProductDetail( Integer productId);

    /**
     * 更新库存
     * */
    public ServerResponse updateProductStock(Integer produtId,Integer updatecount);

    }
