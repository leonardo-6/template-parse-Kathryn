package com.lsl.test.infrastructure.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 组件 视图 对应表结构 持久层
 * describe 视图表 基础设施PO
 * @author LuShuL 12304
 * @date 2022/11/3 1:08
 * @version V1.0
 */
@Entity(name = "View")
@Table(name = "COMPO_VIEW")
@Data
public class ViewPO implements Serializable {

    /***
     * 视图ID
     */
    @Id
    @Column(name = "VIEW_ID")
    private String viewId;

    /***
     * 视图名称
     */
    @Basic
    @Column(name = "VIEW_NAME")
    private String viewName;

    /***
     * 视图分类
     */
    @Basic
    @Column(name = "VIEW_TYPE")
    private String viewType;

    /***
     * 视图类别
     */
    @Basic
    @Column(name = "VIEW_CATEGORY")
    private String viewCategory;

    /***
     * 是否发布
     */
    @Basic
    @Column(name = "IS_PUBLISH")
    private String isPublish;

    /***
     * 创建人
     */
    @Basic
    @Column(name = "CREATED_BY")
    private String createdBy;

    /***
     * 创建时间
     */
    @Basic
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    /***
     * 更新人
     */
    @Basic
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    /***
     * 更新时间
     */
    @Basic
    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    /***
     * 是否删除 0:未删除 1:删除
     */
    @Basic
    @Column(name = "IS_DELETED")
    private String isDeleted;
}
