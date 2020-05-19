package com.neuedu.product.service;

import com.neuedu.product.common.ServerResponse;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

public interface ICategoryService {
     ServerResponse add_category( Integer parentid,String categoryname);

     /**
      * 获取平级子类别
      * */
     ServerResponse getCategory(Integer categoryId);

     /**
      * 获取当前分类id及递归子节点categoryId
      * */
     ServerResponse get_deep_category(Integer categoryId);


     }
