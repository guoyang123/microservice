package com.neuedu.product.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.product.common.Consts;
import com.neuedu.product.common.DateUtils;
import com.neuedu.product.common.ServerResponse;
import com.neuedu.product.dao.IProductDao;
import com.neuedu.product.pojo.Category;
import com.neuedu.product.pojo.Product;
import com.neuedu.product.service.ICategoryService;
import com.neuedu.product.service.IProductService;
import com.neuedu.product.vo.ProductDetailVO;
import com.neuedu.product.vo.ProductListVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    IProductDao productDao;

    @Autowired
    ICategoryService categoryService;
    @Override
    public ServerResponse list(Integer categoryId, String keyword,
                               Integer pageNum, Integer pageSize,
                               String orderby) {


 // select * from neuedu_product where categoryId in(1,2,3) and keyword=%keyword% order by xx limit (pageNum-1)*pageSize,pageSize

        String _keyword=null;
        if(categoryId==null){
            if(StringUtils.isBlank(keyword)){
                return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.SEARCH_NEED_PARAM.getStatus(),Consts.ResponseEnum.SEARCH_NEED_PARAM.getMsg());
            }else{
                _keyword="%"+keyword+"%";

            }
        }

        List<Integer> categoryIds=null;
        if(categoryId!=null){
            ServerResponse<Set<Category>> serverResponse=categoryService.get_deep_category(categoryId);
            if(!serverResponse.isSuccess()){
                return serverResponse;
            }

            Set<Category> categorySet=serverResponse.getData();
            categoryIds=new ArrayList<>();

            if(categorySet!=null){
                Iterator<Category> iterator=categorySet.iterator();
                while(iterator.hasNext()){
                    Category category=iterator.next();
                    categoryIds.add(category.getId());
                }
            }
        }


        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(orderby)){
           String[] orders= orderby.split("_");
            PageHelper.orderBy(orders[0]+" "+orders[1]);
        }
        // orderby  desc_price
        List<Product> productList=productDao.findProducsByCategoryIdsAndkeyword(_keyword,categoryIds);
        PageInfo pageInfo=new PageInfo(productList);

        List<ProductListVO> productListVOList=new ArrayList<>();
        for(Product product:productList){
            ProductListVO productListVO=product2productListVO(product);
            productListVOList.add(productListVO);
        }

        pageInfo.setList(productListVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse findProductDetail(Integer productId) {
        if(productId==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.SEARCH_NEED_PARAM.getStatus(),Consts.ResponseEnum.SEARCH_NEED_PARAM.getMsg());
        }
        Product product=productDao.findProductDetailById(productId);

        if(product==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.PRODUCT_DELETE.getStatus(),Consts.ResponseEnum.PRODUCT_DELETE.getMsg());
        }
        //vo



        return ServerResponse.createServerResponseBySuccess(product2ProductDetailVO(product));
    }

    @Override
    public ServerResponse updateProductStock(Integer productId, Integer updatecount) {
        if(productId==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.SEARCH_NEED_PARAM.getStatus(),Consts.ResponseEnum.SEARCH_NEED_PARAM.getMsg());
        }
        if(updatecount==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.SEARCH_NEED_PARAM.getStatus(),Consts.ResponseEnum.SEARCH_NEED_PARAM.getMsg());
        }

        Product product=productDao.findProductDetailById(productId);
        if(product==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.PRODUCT_DELETE.getStatus(),Consts.ResponseEnum.PRODUCT_DELETE.getMsg());
        }

         int stock=product.getStock();
         if(stock>updatecount){
             updatecount=stock-updatecount;
         }else{
             updatecount=0;
         }
         int count=productDao.updateProductStock(updatecount,productId);
         if(count==0){
             return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.UPDATE_STOCK_FAIL.getStatus(),Consts.ResponseEnum.UPDATE_STOCK_FAIL.getMsg());
         }

        return ServerResponse.createServerResponseBySuccess();
    }

    private ProductDetailVO product2ProductDetailVO(Product product){

        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.date2Str(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setName(product.getName());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setUpdateTime(DateUtils.date2Str(product.getUpdateTime()));
        productDetailVO.setSubtitle(product.getSubtitle());

        return productDetailVO;
    }
    private ProductListVO product2productListVO(Product product){

        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }

}
