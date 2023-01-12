select ${express}
from ${modelName}
    <#if joinList??>
        <#list joinList as joinObj>
            ${joinObj.type} ${joinObj.modelName} on ${joinObj.criteria}
        </#list>
        </#if>
    <#if whereList??>
where 1=1
    <#list whereList as whereObj>
    ${whereObj.type} ${whereObj.criteria}
    </#list>
    </#if>
    <#if groupList??>
group by
        <#list groupList as criteria>
    ${criteria}<#if criteria_has_next>, </#if>
        </#list>
        </#if>
        <#if orderList??>
order by
        <#list orderList as criteria>
    ${criteria}<#if criteria_has_next>, </#if>
        </#list>
        </#if>