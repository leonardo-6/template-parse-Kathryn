foo
<@Upper id="types" parameter="value">
<#-- All kind of FTL is allowed here -->
    <#if value?? && value? size gt 0>
        <#list value.color as color>
            ${color} + ${color}
        </#list>
    </#if>
   this is way
</@Upper>

<@Upper id="type"  parameter="value">
    222
<#-- All kind of FTL is allowed here -->
    <#if color?? >
          ${color}
    </#if>

    <#if value?? && value? size gt 0>
        <#list value.color as color>
            ${color}
        </#list>
    </#if>
    two
</@Upper>
wombat