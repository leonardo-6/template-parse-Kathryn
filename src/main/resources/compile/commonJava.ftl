<@Java id="package" parameter="value">
    package %s;

</@Java>

<@Java id="importBaseTage" parameter="value">
    import %s;
</@Java>

<@Java id="importCheckUtil" parameter="value">
    import %s;
</@Java>

<@Java id="importCommon" parameter="value">
    import java.util.HashMap;
    import java.util.Map;
    import org.springframework.stereotype.Component;
</@Java>

<@Java id="className" parameter="value">

    @Component
    public class %s extends BaseTag {
    public %s() { super(%s.class.getName()); }

</@Java>

<@Java id="classMethod" parameter="value">

    public Object %s(Map params) {
        if (!CheckValueUtil.notBlank(params)) {
            Map< String, Object> map = new HashMap<>();
            return  map;
        }
        return params;
    }

</@Java>

<@Java id="classEnd" parameter="value">

    }
</@Java>

