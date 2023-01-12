package com.lsl.utils.file;

import com.lsl.utils.check.CheckValueUtil;
import com.lsl.utils.strings.MyStrUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.lsl.common.constant.FreemarkerConstant.*;
import static com.lsl.common.enums.CharInfoEnum.SLASH;

/**
 * @className: FileUtils
 * @description: 文件操作工具类
 * @date: 2020/8/20
 * @author: cakin
 */
@Slf4j
public class FileUtils {
    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return String 扩展名
     */
    public static String getExtend(String filename) {
        return getExtend(filename, "");
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @param defExt   默认扩展名
     * @return String 扩展名
     */
    public static String getExtend(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');

            if ((i > 0) && (i < (filename.length() - 1))) {
                return (filename.substring(i + 1)).toLowerCase();
            }
        }
        return defExt.toLowerCase();
    }

    /**
     * 获取文件名称[不含扩展名]
     *
     * @param fileName 文件名全名
     * @return String 文件名
     */
    public static String getFilePrefix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
    }

    /**
     * 获取文件名称[不含扩展名]
     * 不去掉文件目录的空格
     *
     * @param fileName 文件名全名
     * @return String 文件名
     */
    public static String getFilePrefix2(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex);
    }

    /**
     * 文件复制
     *
     * @param inputFile 源文件
     * @param outputFile 目的文件
     */
    public static void copyFile(String inputFile, String outputFile) throws FileNotFoundException {
        File sFile = new File(inputFile);
        File tFile = new File(outputFile);
        FileInputStream fis = new FileInputStream(sFile);
        FileOutputStream fos = new FileOutputStream(tFile);
        int temp = 0;
        byte[] buf = new byte[10240];
        try {
            while ((temp = fis.read(buf)) != -1) {
                fos.write(buf, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 功能描述：判断文件是否为图片
     *
     * @author cakin
     * @date 2020/8/20
     * @param filename 文件名
     * @return boolean 文件是否为图片
     */
    public static boolean isPicture(String filename) {
        // 文件名称为空的场合
        if (StringUtils.isEmpty(filename)) {
            // 返回不合法
            return false;
        }
        // 获得文件后缀名
        String tmpName = getExtend(filename);
        // 声明图片后缀名数组
        String imgeArray[][] = {{"bmp", "0"}, {"dib", "1"},
                {"gif", "2"}, {"jfif", "3"}, {"jpe", "4"},
                {"jpeg", "5"}, {"jpg", "6"}, {"png", "7"},
                {"tif", "8"}, {"tiff", "9"}, {"ico", "10"}};
        // 遍历名称数组
        for (int i = 0; i < imgeArray.length; i++) {
            // 判断单个类型文件的场合
            if (imgeArray[i][0].equals(tmpName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 功能描述：判断文件是否为DWG
     *
     * @author cakin
     * @date 2020/8/20
     * @param filename 文件名
     * @return boolean 件是否为DWG
     */
    public static boolean isDwg(String filename) {
        // 文件名称为空的场合
        if (StringUtils.isEmpty(filename)) {
            // 返回不合法
            return false;
        }
        // 获得文件后缀名
        String tmpName = getExtend(filename);
        // 后缀名是否为 dwg
        if (tmpName.equals("dwg")) {
            return true;
        }
        return false;
    }

    /**
     * 删除指定的文件
     *
     * @param strFileName 指定绝对路径的文件名
     * @return boolean 是否删除成功
     */
    public static boolean delete(String strFileName) {
        File fileDelete = new File(strFileName);
        if (!fileDelete.exists() || !fileDelete.isFile()) {
            return false;
        }
        return fileDelete.delete();
    }

    /**
     * 功能描述：文件编码
     *
     * @author cakin
     * @date 2020/8/20
     * @param fileName 文件名
     * @return String 编码后的文件名
     * @description: 防止文件名中文乱码
     */
    public static String encodingFileName(String fileName) {
        String returnFileName = "";
        try {
            returnFileName = URLEncoder.encode(fileName, "UTF-8");
            returnFileName = StringUtils.replace(returnFileName, "+", "%20");
            if (returnFileName.length() > 150) {
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnFileName;
    }

    /**
     * 生成文件，并写入指定的内容
     *
     * @param inputFile 文件存储路径和名称
     * @param content   文件内容
     * @return boolean 生成文件是否成功
     */
    public static boolean createFile(String inputFile, String content) {
        try {
            File file = new File(inputFile);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            // 创建文件
            file.createNewFile();
            // 写入内容
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
            bw.close();
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static File createCompileFile(String inputFile, byte[] content) {
        try {
            File file = new File(inputFile);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            // 创建文件
            file.createNewFile();
            // 写入内容
            OutputStream os = new FileOutputStream(file);
            os.write(content, 0, content.length);
            os.flush();
            os.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /****
     * 创建新的 freemarker文件
     * @param fileName
     * @param content
     * @return
     */
    public static boolean createFreeMarkSqlFile(String resourceName,String sourcePath,String fileName, String content) {

        // 1. 创建文件 根据路径位置
        String resource = FileUtils.class.getClassLoader().getResource(resourceName).getPath();
        String inputFile = resource + sourcePath + TEMP_PATH +fileName.trim()+FREEMARKER_FILE_SUFFIX;
        File file = new File(inputFile);

        // 2. 如果存在 校验内容是否相同
        if (file.exists()) {
            String source = readToString(inputFile);
            if (CheckValueUtil.notBlank(source.trim())&& source.trim().equals(content.trim())) {
                log.info("已创建 且内容相同");
                return true;
            }
            delete(inputFile);
        }
        return createFile(inputFile,content);
    }

    /****
     * 2. 根据文件路径读取文件内容
     * @param filePath
     * @return
     */
    public static String readFileContent(String filePath) {

        // 转换成List<String>, 要注意java.lang.OutOfMemoryError: Java heap space
        List<String> lines = null;
        StringBuilder sb = new StringBuilder();
        try {
            if (isWindows()) {
                filePath = filePath.substring(1);
            }
            lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            for (String line : lines) {
                sb.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }

    public static boolean isLinux() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("LINUX") != -1;
    }

    /***
     * 传入文件资源下 文件夹名称 获取 下面的文件名和路径
     * @param SourceName
     * @return
     */
    public static Set<Map.Entry<String, String>>  getFileNames(String SourceName) {
        if (!CheckValueUtil.notBlank(FileUtils.class.getClassLoader().getResource(SourceName))) {
            return new HashSet<>();
        }
        String sourcePath = FileUtils.class.getClassLoader().getResource(SourceName).getPath();
        File file = new File(sourcePath);

        HashMap<String,String> map = new HashMap<>();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        getFile( file.listFiles(), map,sourcePath,SourceName);

        return entries;
    }

    /****
     * 将文件名 + 文件路径 存入 map集合
     * @param files 文件
     * @param map 集合
     */
    public static void getFile(File[] files, HashMap<String,String> map,String sourcePath,String SourceName){
        if (!CheckValueUtil.notBlank(files)) {
            return;
        }
        for(File f : files){
            if(f.isDirectory()){
                getFile( f.listFiles(), map,sourcePath,SourceName);
            } else {
                String fileName = removePrefix(sourcePath, f.getAbsolutePath(),SourceName);
                if (fileName.startsWith(TEMP_PATH.substring(1, TEMP_PATH.length()-1))){
                   continue;
                }
                log.info(" === 扫描的模板有：{}",fileName);
                map.put(fileName,f.getAbsolutePath());
            }
        }
    }

    /****
     *
     * @param source
     * @param target
     * @return
     */
    public static String removePrefix(String source,String target,String sourceName){
        if (isWindows()) {
            source =  source.substring(1).replaceAll("\\/","\\\\");
            source = source.substring(0,source.lastIndexOf(sourceName.replaceAll("\\/","\\\\")));
            return target.replace(source,"");
        }else if(isLinux()){
            source = source.substring(0,source.lastIndexOf(sourceName.replaceAll("\\/","\\\\")));
            return target.replace(source,"");
        }
        return source;
    }


    /**
     * 读取文件内容
     *
     * @param fileName 文件名
     * @return String 文件内容
     */
    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将文件转为base64字符串
     *
     * @param fileName 文件名
     * @return String 文件内容的base64字符串
     * @throws Exception
     */
    public static String readToBase64(String fileName) throws Exception {
        InputStream is = new FileInputStream(fileName);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(bytes);
    }

    /*****
     * 1. 动态编译 生成 java文件
     * @param beanName class名字
     * @param suffix 后缀
     * @param content 文件内容
     * @return 文件
     * @throws IOException
     */
    public static File createTempFileWithFileNameAndContent(String resourceName,String beanName,String suffix,byte[] content,String packageSuffix) throws IOException {
        String resource = FileUtils.class.getClassLoader().getResource(resourceName).getPath();
        String inputFile = resource+packageSuffix+SLASH.symbol()+beanName+suffix;
        File file = new File(inputFile);

        // 2. 如果存在 校验内容是否相同
        if (file.exists()) {
            String source = readToString(inputFile);
            if (CheckValueUtil.notBlank(source.trim())&& source.trim().equals(content.toString().trim())) {
                log.info("已创建 且内容相同");
                return file;
            }
            delete(inputFile);
        }
        return createCompileFile(inputFile,content);
    }

    /*public static void main(String[] args) {
        createTempFileWithFileNameAndContent("ss","java","");
    }*/

    public static String getMethodParamClass(String tempFilePath){
        if (!CheckValueUtil.notBlank(tempFilePath)) {
            return "";
        }

        if (tempFilePath.contains(MyStrUtils.spliceFirstSymbol(TEMP_PATH))){
            tempFilePath = tempFilePath.replace(MyStrUtils.spliceFirstSymbol(TEMP_PATH),"");
        }

        if (tempFilePath.contains("/")){
            tempFilePath= tempFilePath.substring(0,tempFilePath.lastIndexOf("/"));
        }

        if (tempFilePath.contains("/")){
            tempFilePath = tempFilePath.replaceAll("/",".");
        }

        if(tempFilePath.startsWith(".")){
            tempFilePath= tempFilePath.substring(1);
        }

        return "com.lsl."+ tempFilePath;
    }
}
