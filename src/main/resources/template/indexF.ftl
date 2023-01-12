<!doctype html>
<html lang="zh-cn">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport content="width-device-width, initial-scale=1, shrink-to-fit=no">
    <script src="$(indexDTO.contextPath)/resources/a.js" type="text/javascript"></script>
    <title>${indexDTO.getTitle()}</title>
</head>

<body>
        从request中获取contextPath:${rc.contextPath} <br/>
        从session中获取信息:${website} <br/>
        <#--import是freemark的语法,基Findex.ftl所在目录开始寻spring.ftL-->
        <#import "/spring.ftl" as spring>
        国际化信息:<@spring.message code="1001"></@spring.message><br/>
        取出共享变量:${ctx};
</body>
</html>