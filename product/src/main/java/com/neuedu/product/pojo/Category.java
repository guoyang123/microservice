package com.neuedu.product.pojo;

import java.util.Date;

public class Category {

    private Integer id;//`       int(11)      not null  auto_increment comment '类别id',
    private Integer parent_id;//       int(11)      default null   comment '父类Id,当pareng_id=0,说明是根节点，一级类别',
    private String  name;//       varchar(50)      DEFAULT null   comment '类别名称',
    private int status;//       tinyint(1)      DEFAULT '1'  comment '类别状态1-正常，2-已废弃',
    private int sort_order;//       int(4)    DEFAULT null   comment '排序编号，同类展示顺序，数值相等则自然排序',
    private Date create_time;//      datetime      not null   comment '创建时间',
    private Date update_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
