package com.lsl.common.enums;


import com.lsl.exception.ResultCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 异常信息 枚举类
 * describe 枚举 工具
 * @author LuShuL 12304
 * @date 2022/5/3 1:08
 * @version V1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionInfoEnum implements ResultCodeInterface {

    // 数据操作错误定义
    SUCCESS(200, "成功!"),
    UNSUCCESS(400, "失败"),
    SIGNATURE_NOT_MATCH(401, "请求的数字签名不匹配!"),
    BODY_NOT_MATCH(402, "请求的数据格式不符!"),
    REQUEST_NOT_MATCH(403,"请求不合法"),
    NOT_FOUND(404, "未找到该资源!"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
    SERVER_BUSY(503, "服务器正忙，请稍后再试!"),

    //数据地图常见异常
    INPUT_PARAM_ERROR(505, "Exception on input：入参非法异常!"),
    INPUT_PARAM_KEY_ERROR(506, "Exception on input：入参的 主键值 未获取到对应信息,请重试!"),
    DATASOURCE_PARAM_ERROR(511, "Exception on input：数据源信息入参缺失!"),
    DATASOURCE_CONNECT_ERROR(512, "Exception on connect：数据源信连接异常!"),
    DATASOURCE_NOT_FOUND_ERROR(513, "DataSource Type Not FOUND：未找到对应的数据源类型!"),
    DATASOURCE_CLOSE_ERROR(514, "DataSource close ERROR：数据源连接未正常关闭释放!"),

    VERIFY_EXISTS(1002, "刷新过于频繁，请稍后再试"),
    SIGN_CACHE_NOT_FOUND(1002, "验证码不存在"),
    SIGN_CACHE_NO_EQUAL(1003, "验证码错误"),

    // 1. 定义错误信息
    CODE(0,"code"),
    MSG(1,"msg"),
    WRONG_NUM(-1,"WRONG_NUM"),
    // 2. 后台信息的错误
    CONNECT_ERROR(501,"连接异常----Connect Error ---"),
    CLASS_NOT_FOUND(502,"未找到当前类----CLASS NOT FOUND----"),
    OBJECT_NOT_SUPPORT(503,"当前对象不支持！"),
    FAILED_GET_USER(504,"获取 当前登录用户 信息失败！"),
    INPUT_VALUE_BLANK(505,"传入值为空！"),
    ARRAY_TABLE_BLANK(506,"无可申请的表！"),
    TABLE_AND_SQL_NOT_EXIST(507,"当前传入的表名和sql不存在！"),
    SHELL_COMMAND_ERROR(508,"执行远程shell操作 存在异常！"),
    SHELL_COMMAND_RETURN_NULL(509,"执行远程shell操作 返回信息信息为空！"),
    SHELL_COMMAND_UNKNOWN_ERROR(510,"执行远程shell操作 字段处理后信息为空的未知异常！"),
    SQL_EXCEPTION(511,"执行远程sql操作 发生了异常！"),
    // 3. 表信息问题
    ALREADY_INITIATE_AUTHORIZE(451,"用户已经授权 或 正在申请 对 %s 表的权限,无需发起申请！"),
    TABLE_NOT_EXIST(421,"表名不存在！请检查输入的表名或sql语法是否规范！"),
    TABLE_LIST_NOT_EXIST(422,"表信息列表不存在！"),
    DB_ID_NOT_EXIST(423,"当前表信息未包含库ID！"),
    DB_NAME_NOT_EXIST(424,"当前库信息未包含库名！"),
    USER_NOT_NEED_APPLY(425,"当前登录用户角色 无需发起申请！"),
    USER_NOT_AUTHORIZE(426,"当前登录用户 没有对该表的查询权限！请发起申请 或 预览其他表！"),
    MYSQL_OR_MYSQL_IS_BLANK(430,"Oracle或Mysql的数据源实例不能为空！"),
    DATASOURCE_TYPE_IS_BLANK(431,"当前数据源类型不能为空！"),
    DATASOURCE_IS_BLANK(432,"当数据源类型为Oracle和Mysql时,数据源实例 不能为空！"),
    DATASET_IS_LEAF(433,"当前无节点无子节点!"),

    // 4. 用户信息获取异常
    GET_USER_INFO_ERROR(521,"get userInfo Exception: 获取当前登录用户信息 错误！请联系管理员"),
    ROLE_NEED_NOT_APPLY(441,"user need not to apply : 当前用户权限无需发起申请！"),
    ROLE_UNABLE_OPERATE(442,"user unable to operate : 当前用户权限无法进行操作！"),

    NO_DATA(522,"No data : 当前数据库无数据！");


    /***
     * 数据源类型id
     */
    private Integer code;
    /***
     * 数据源类型名称
     */
    private String msg;


    /***
     *
     * @param code 代号
     * @return 名称
     */
    public static String getNameByCode(Integer code){
        ExceptionInfoEnum[] values = values();
        for(ExceptionInfoEnum msgEnum : values){
            if(msgEnum.code.equals(code)){
                return msgEnum.name();
            }
        }
        return null ;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
