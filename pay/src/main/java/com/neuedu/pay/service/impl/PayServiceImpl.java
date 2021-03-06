package com.neuedu.pay.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.alipay.demo.trade.DemoHbRunner;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.*;
import com.alipay.demo.trade.model.hb.*;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.Utils;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.neuedu.pay.common.Consts;
import com.neuedu.pay.common.ServerResponse;
import com.neuedu.pay.dao.IPayDao;
import com.neuedu.pay.pojo.PayInfo;
import com.neuedu.pay.service.IOrderService;
import com.neuedu.pay.service.IPayService;
import com.neuedu.pay.vo.OrderItemVO;
import com.neuedu.pay.vo.OrderVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by liuyangkly on 15/8/9.
 * 简单main函数，用于测试当面付api
 * sdk和demo的意见和问题反馈请联系：liuyang.kly@alipay.com
 */
@Service
public class PayServiceImpl  implements IPayService {
    private static Log                  log = LogFactory.getLog(PayServiceImpl.class);

    // 支付宝当面付2.0服务
    private static AlipayTradeService   tradeService;

    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService   tradeWithHBService;

    // 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
    private static AlipayMonitorService monitorService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
            .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
            .setFormat("json").build();
    }


    @Autowired
    IOrderService orderService;
    @Autowired
    IPayDao payDao;
    // 测试当面付2.0生成支付二维码
    @Override
    public ServerResponse pay(Long orderNo) {

        //step1:根据订单号查询订单信息->调用订单服务
         ServerResponse<OrderVO> serverResponse=orderService.getOrderDetail(orderNo);





         if(!serverResponse.isSuccess()){
             return serverResponse;
         }

        OrderVO orderVO= serverResponse.getData();



        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(orderVO.getOrderNo());

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "xxx平台订单信息";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = orderVO.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        List<OrderItemVO> orderItemVOList=orderVO.getOrderItemVOList();
        Integer count=0;

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();


        for(OrderItemVO orderItemVO:orderItemVOList){
           count+= orderItemVO.getQuantity();

            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods1 = GoodsDetail.newInstance(String.valueOf(orderItemVO.getProductId()),
                    orderItemVO.getProductName(), orderItemVO.getCurrentUnitPrice().longValue(), orderItemVO.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods1);
        }
        String body = "购买商品"+count+"件共"+orderVO.getPayment().toString()+"元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";



        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
            .setTimeoutExpress(timeoutExpress)
                //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
             .setNotifyUrl("http://z8babq.natappfree.cc/pay/callback.do")

            .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                String filePath = String.format("D:\\ftpfile\\qr-%s.png",
                    response.getOutTradeNo());
                log.info("filePath:" + filePath);
                 ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                break;

            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return  null;
    }

    @Override
    public String orderLogic(Map<String, String> params) {

        //step1: 拿到订单号并查询订单状态
        Long orderNo=Long.parseLong(params.get("out_trade_no"));
        ServerResponse serverResponse=orderService.getOrderDetail(orderNo);
        if(!serverResponse.isSuccess()){
            return "failure";
        }
        OrderVO orderVO=(OrderVO) serverResponse.getData();

         Integer status=orderVO.getStatus();

         if(status> Consts.OrderStatusEnum.ORDER_NOPAY.getStatus()){
             //订单不需要处理
             return "success";
         }

         //获取订单在支付宝的支付状态
        String orderPayStatus=params.get("trade_status");

         Integer orderStatus=Consts.OrderPayStatusEnum.getOrderStatus(orderPayStatus);

         String paymentTime=params.get("gmt_payment");

         //更新订单信息
        ServerResponse serverResponse1= orderService.updateOrder(orderNo,orderStatus,paymentTime);

       if(!serverResponse1.isSuccess()){
           return "failure";
       }

       //将支付信息插入到支付表

        String trade_no=params.get("trade_no");

        PayInfo payInfo=new PayInfo();
        payInfo.setOrderNo(orderNo);
        payInfo.setPayPlatform(Consts.PAY_ZFBAO);
        payInfo.setPlatformNumber(trade_no);
        payInfo.setUserId(orderVO.getUserId());
        payInfo.setPlatformStatus(orderPayStatus);

        int payCount=payDao.insert(payInfo);
        if(payCount==0){
            return "failure";
        }

        return "success";
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

}
