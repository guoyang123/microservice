package com.neuedu.product.dao;

import com.neuedu.product.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IProductDao {

    List<Product> findProducsByCategoryIdsAndkeyword(@Param("keyword") String keyword,
                                                     @Param("categoryIds") List<Integer> categoryIds);


    Product findProductDetailById(@Param("productId")Integer productId);

    int updateProductStock(@Param("stock") Integer stock,@Param("productId") Integer productId);
}
