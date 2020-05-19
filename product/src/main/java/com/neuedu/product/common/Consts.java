package com.neuedu.product.common;

public class Consts {


    public static final Integer NORMAR_USER=1;
    public static final String CURRENT_USER="USER";


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
        UPDATE_STOCK_FAIL(108,"扣库存失败"),

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


}
