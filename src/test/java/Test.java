import com.lsl.utils.check.CheckValueUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: TODO
 * @Author: lsl
 * @Data:2022-12-09 17:43
 * @Projectname: Kathryn
 * @Filename: Test
 */
public class Test {

    public static void main(String[] args) {

        String s = patternId("<@upper id = \" type \"  parameter=\"value\">");
        String idValue = getIdValue(s);
        System.out.println(idValue);


    }

    public static String patternId(String source){
        Pattern pattern = Pattern.compile("id\\s*=\\s*\"\\s*\\w*\\s*\"");
        Matcher matcher = pattern.matcher(source);
        matcher.find();
        return matcher.group();
    }

    public static String getIdValue(String source){
        if (CheckValueUtil.notBlank(source) && source.contains("=")) {
            String[] split = source.split("=");
            return split[1].replaceAll("\"", "").trim();
        }
        return null;
    }
}
