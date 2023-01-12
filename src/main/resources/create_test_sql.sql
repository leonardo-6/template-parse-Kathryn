-- Create table
create table COMPO_VIEW
(
    view_id       VARCHAR2(32) not null,
    view_name     VARCHAR2(32) not null,
    view_type     VARCHAR2(32),
    view_category VARCHAR2(64),
    is_publish    CHAR(1),
    created_by    VARCHAR2(32) not null,
    created_time  DATE not null,
    updated_by    VARCHAR2(32),
    updated_time  DATE,
    is_deleted    CHAR(1) default '0' not null
)
 tablespace EHRVIEW
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table
comment on table COMPO_VIEW
  is '视图表';
-- Add comments to the columns
comment on column COMPO_VIEW.view_id
  is '视图ID';
comment on column COMPO_VIEW.view_name
  is '视图名称';
comment on column COMPO_VIEW.view_type
  is '视图分类';
comment on column COMPO_VIEW.view_category
  is '视图类别(0-默认 1-自定义)';
comment on column COMPO_VIEW.is_publish
  is '是否发布';
comment on column COMPO_VIEW.created_by
  is '创建人';
comment on column COMPO_VIEW.created_time
  is '创建时间';
comment on column COMPO_VIEW.updated_by
  is '更新人';
comment on column COMPO_VIEW.updated_time
  is '更新时间';
comment on column COMPO_VIEW.is_deleted
  is '是否删除 0:未删除 1:删除';
-- Create/Recreate primary, unique and foreign key constraints
alter table COMPO_VIEW
    add constraint COMPO_VIEW_KEY primary key (VIEW_ID)
    using index
  tablespace EHRVIEW
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 128K
    next 1M
    minextents 1
    maxextents unlimited
  );


insert into COMPO_VIEW (VIEW_ID, VIEW_NAME, VIEW_TYPE, VIEW_CATEGORY, IS_PUBLISH, CREATED_BY, CREATED_TIME, UPDATED_BY, UPDATED_TIME, IS_DELETED)
values ('1018244837010049', '测试3', '10', '0', '1', '12314', to_date('17-11-2022 14:08:50', 'dd-mm-yyyy hh24:mi:ss'), '12314', to_date('17-11-2022 14:11:12', 'dd-mm-yyyy hh24:mi:ss'), '0');

insert into COMPO_VIEW (VIEW_ID, VIEW_NAME, VIEW_TYPE, VIEW_CATEGORY, IS_PUBLISH, CREATED_BY, CREATED_TIME, UPDATED_BY, UPDATED_TIME, IS_DELETED)
values ('1018244837010051', '测试4', '8', null, null, '12314', to_date('11-01-2023 16:53:10', 'dd-mm-yyyy hh24:mi:ss'), null, null, '0');

insert into COMPO_VIEW (VIEW_ID, VIEW_NAME, VIEW_TYPE, VIEW_CATEGORY, IS_PUBLISH, CREATED_BY, CREATED_TIME, UPDATED_BY, UPDATED_TIME, IS_DELETED)
values ('1018244837010048', '测试2', '8', '0', '1', '12314', to_date('17-11-2022 14:08:50', 'dd-mm-yyyy hh24:mi:ss'), '12314', to_date('17-11-2022 14:11:12', 'dd-mm-yyyy hh24:mi:ss'), '0');

