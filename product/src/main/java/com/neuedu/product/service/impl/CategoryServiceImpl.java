package com.neuedu.product.service.impl;

import com.neuedu.product.common.Consts;
import com.neuedu.product.common.ServerResponse;
import com.neuedu.product.dao.ICategoryDao;
import com.neuedu.product.pojo.Category;
import com.neuedu.product.service.ICategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    ICategoryDao categoryDao;
    @Override
    public ServerResponse add_category(Integer parentid, String categoryname) {

        if(parentid==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.PARENTID_NEED.getStatus(),Consts.ResponseEnum.PARENTID_NEED.getMsg());
        }
        if(StringUtils.isBlank(categoryname)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.CATEGORYNAME_NEED.getStatus(),Consts.ResponseEnum.CATEGORYNAME_NEED.getMsg());
        }

        //添加类别
        int count=categoryDao.addCategory(parentid, categoryname);
        if(count==0){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.CATEGORY_ADD_FAIL.getStatus(),Consts.ResponseEnum.CATEGORY_ADD_FAIL.getMsg());
        }


        return ServerResponse.createServerResponseBySuccess();
    }

    @Override
    public ServerResponse getCategory(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.PARENTID_NEED.getStatus(),Consts.ResponseEnum.PARENTID_NEED.getMsg());
        }

        List<Category> categoryList=categoryDao.getSubCategorysById(categoryId);

        return ServerResponse.createServerResponseBySuccess(categoryList);
    }

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {


        Set<Category> categorySet=new HashSet<>();
        categorySet=findChildCategory(categorySet,categoryId);
        ServerResponse serverResponse=ServerResponse.createServerResponseBySuccess(categorySet);

        return serverResponse;
    }


    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){


        Category category=categoryDao.selectByPrimaryKey(categoryId);

        if(category!=null){
            categorySet.add(category);
        }

        List<Category> categoryList=categoryDao.getSubCategorysById(categoryId);
        for(Category category1:categoryList){
            findChildCategory(categorySet,category1.getId());
        }


        return categorySet;
    }

}
