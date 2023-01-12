<#-- demo的hql语句 -->
<@Select id="types" parameter="value">
    select
        <#-- 字段 -->
       viewId as viewId,
       viewName as viewName,
        viewType as viewType,
        viewCategory as viewCategory,
        isPublish as isPublish,
        createdBy as createdBy,
        createdTime as createdTime
    from
        <#-- 表名 -->
        View
    where
        <#-- 国际惯例 -->
         1=1
    <#if value?? && value?size gt 0>
         <#if value.viewName?? >
             and  viewName like'${value.viewName}%'
         </#if>

        <#if value.createdBy?? >
            and createdBy = '${value.createdBy}'
        </#if>

        <#if  value.viewType?? && value.viewType?size gt 0>
            and  viewType in (
            <#list value.viewType as viewType>
                '${viewType}' <#if viewType_has_next>,</#if>
            </#list>
            )
        </#if>
    </#if>
</@Select>

<@Upper id="type"  parameter="value">
    222
<#-- All kind of FTL is allowed here -->
    <#if value?? && value? size gt 0>
        <#list value.color as color>
            ${color}
        </#list>
    </#if>
    two
</@Upper>
wombat