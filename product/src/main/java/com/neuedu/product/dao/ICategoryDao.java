package com.neuedu.product.dao;

import com.neuedu.product.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICategoryDao {

    int addCategory(@Param("parentId")Integer parentId,@Param("categoryName")String categoryName);


    List<Category> getSubCategorysById(@Param("categoryId") Integer categoryId);

    Category selectByPrimaryKey(@Param("id") Integer categoryId);
}
