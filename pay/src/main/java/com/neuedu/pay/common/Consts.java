package com.neuedu.pay.common;

public class Consts {


    public static final Integer NORMAR_USER=1;
    public static final String CURRENT_USER="USER";

    public static final Integer CHECKED=1;

    public static final Integer PAY_ZFBAO=1;


    public static   enum ResponseEnum{

        REGISTER_PARAM_NOT_EMPTY(1,"参数不能为空"),
        REGISTER_PARAM_USERNAME_NOT_EMPTY(2,"用户名不能为空"),
        REGISTER_PARAM_PASSWORD_NOT_EMPTY(3,"密码不能为空"),
        REGISTER_PARAM_EMAIL_NOT_EMPTY(4,"邮箱不能为空"),
        REGISTER_PARAM_QUESTION_NOT_EMPTY(5,"密保问题不能为空"),
        REGISTER_PARAM_ANSWER_NOT_EMPTY(6,"答案不能为空"),
        REGISTER_PARAM_PHONE_NOT_EMPTY(7,"手机号不能为空"),

        REGISTER_PARAM_USERNAME_EXISTS(8,"用户名存在"),
        REGISTER_FAIL(9,"注册失败"),
        REGISTER_PARAM_USERNAME_NOT_EXISTS(10,"用户名不存在"),
        REGISTER_PARAM_PASSWORD_ERROR(11,"密码错误"),

        QUESTION_QUERY_FAIL(12,"密保问题查询失败"),
        ANSWER_FAIL(13,"回答问题失败"),
        PASSWORD_UPDATE_FAIL(14,"密码修改失败"),
        REGISTER_PARAM_FORGETTOKEN_NOT_EMPTY(15,"重置密码token不能为空"),
        REGISTER_PARAM_FORGETTOKEN_TIMEOUT(16,"重置密码token不存在或者已过期"),
        REGISTER_PARAM_FORGETTOKEN_ERROR(17,"重置密码token错误"),


        NO_LOGIN(100,"未登录"),
        NO_PRIORITY(105,"无权限"),


        PARENTID_NEED(101,"父类id必须"),
        CATEGORYNAME_NEED(102,"类别名称必须"),
        CATEGORY_ADD_FAIL(103,"类别添加失败"),
        SEARCH_NEED_PARAM(106,"参数不能为空"),

        PRODUCT_DELETE(107,"商品被下架或者删除"),


        CART_UPDATE_FAIL(201,"购物车商品更新失败"),
        CART_ADD_FAIL(202,"购物车商品添加失败"),
        CART_CHECK_FAIL(203,"更改商品选择状态失败"),

        ORDER_INSERT_FAIL(301,"订单插入失败"),
        ORDERITEM_INSERT_FAIL(302,"订单明细插入失败"),
        NO_EXISTS_TIMEOUT_ORDER(303,"没有超时未支付订单"),
        NO_EXISTS_ORDER(305,"订单不存在"),
        NO_UNPAY_ORDER(306,"不是未支付订单"),
        ORDERNO_NOT_EMPYT(307,"订单号不能为空"),
        ;
        private int  status;
        private String msg;
         ResponseEnum(int status,String msg){
            this.status=status;
            this.msg=msg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static  enum OrderStatusEnum{

        /**
         * 订单状态：0-已取消 10-未付款 20-已付款 30-已发货 40-交易成功 50-交易关闭
         * */

        ORDER_CANCEL(0,"已取消"),
        ORDER_NOPAY(10,"未付款"),
        ORDER_PAYED(20,"已付款"),
        ORDER_SEND(30,"已发货"),
        ORDER_SUCCESS(40,"交易成功"),
        ORDER_CLOSE(50,"交易关闭"),


        ;

        private Integer status;
        private String desc;

        OrderStatusEnum(Integer status,String desc){
            this.status=status;
            this.desc=desc;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


    public static  enum OrderPayStatusEnum{

        /**
         * 订单状态：0-已取消 10-未付款 20-已付款 30-已发货 40-交易成功 50-交易关闭
         * */

        ORDER_UNPAY(10,"WAIT_BUYER_PAY"),
        ORDER_CLOSE(50,"TRADE_CLOSED"),
        ORDER_PAYED(20,"TRADE_SUCCESS"),
        ORDER_FINISH(40,"TRADE_FINISHED"),

        ;

        private Integer status;
        private String desc;

        OrderPayStatusEnum(Integer status,String desc){
            this.status=status;
            this.desc=desc;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static Integer getOrderStatus(String desc){

            for(OrderPayStatusEnum orderPayStatusEnum:values()){
                if(desc.equals(orderPayStatusEnum.getDesc())){
                    return orderPayStatusEnum.getStatus();
                }
            }
            return 0;
        }

    }

}
