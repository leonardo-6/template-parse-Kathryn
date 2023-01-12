package com.lsl.aspect;

import com.lsl.exception.MyFreemarkerException;
import com.lsl.utils.check.CheckValueUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Hibernate 执行时的切面操作
 * @Description: 处理session 和 事务
 * @Author: lsl
 * @Data:2023-01-11 17:35
 * @Projectname: Kathryn
 * @Filename: HibernateAspect
 */
@Component
@Aspect
@Slf4j
public class HibernateAspect {

    @Autowired
    @Qualifier(value="initHibernate")
    private SessionFactory sessionFactory;

    @Pointcut("@annotation(com.lsl.annotation.HibernateSession)")
    public void methodPointcut() {}

    @Pointcut("execution(public * com.lsl.persistence.hibernate.*.*(..)) && " +
            "! this(com.lsl.persistence.hibernate.base.AbstractHibernate)")
    public void classPointCut() {}

    @Pointcut(" this(com.lsl.persistence.hibernate.base.AbstractHibernate) ")
    public void classExceptSelectPointCut(){}

    @Around("classExceptSelectPointCut()")
    public Object adviceExceptSelect(ProceedingJoinPoint joinPoint) {

        // 0. 从工厂获取到session
        Session session = sessionFactory.openSession();

        // 1. 开始 事务
        Transaction tx = session.beginTransaction();

        Object proceed = null;
        try {
            // 1. 字段注入session
            setField(joinPoint,session);

            // 2. 执行被代理的方法
             proceed = joinPoint.proceed();

        } catch (Throwable e) {
            log.error("错误类名: {},方法为：{}", joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName());
            e.printStackTrace();
        }finally {

            // 3. 关闭事务
            tx.commit();

            // 4. 关闭 工厂的session
            session.close();

            return proceed;
        }
    }

    /****
     * 这里还可以省略pointcut()方法，使用下面的写法
     * " 注解作用的方法名: " --- joinPoint.getSignature().getName()
     * " 所在类的简单类名: " --- joinPoint.getSignature().getDeclaringType().getSimpleName()
     * " 目标方法的声明类型: " --- Modifier.toString(joinPoint.getSignature().getModifiers() -- public之类
     * "--- 日志的内容为" + hibernateSession.value() + "
      */
    @Around(" methodPointcut() || classPointCut()")
    public void advice(ProceedingJoinPoint joinPoint) {

        // 0. 从工厂获取到session
        Session session = sessionFactory.openSession();

        try {
            // 1. 字段注入session
            setField(joinPoint,session);

            // 2. 执行被代理的方法
            joinPoint.proceed();

        } catch (Throwable e) {
            log.error("错误类名: {},方法为：{}", joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName());
            e.printStackTrace();
        }finally {
            // 3. 关闭 工厂的session
            session.close();
        }
    }

    /*****
     * 1. 字段注入值
     * @param joinPoint 切点
     * @param session 会话
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private void setField(ProceedingJoinPoint joinPoint,Session session) throws IllegalAccessException, MyFreemarkerException, ClassNotFoundException {

        // 1. 从切点获取到目标对象
        Object target = joinPoint.getTarget();

        // 2. 获取对应的字节码
        Class<?> aClass = target.getClass();

        // 3. 获取成员变量 -- class对象.getField(value)

        Field field = null;
        for (Field f : aClass.getDeclaredFields()) {
            if(Session.class.isAssignableFrom(Class.forName(f.getGenericType().getTypeName()))){
                field = f;
            }
        }
        if (!CheckValueUtil.notBlank(field)) {
            throw new MyFreemarkerException("字段 注入属性失败！");
        }
        // 4. 暴力破解
        field.setAccessible(true);

        // 5. 给获取到的field赋值 -- Field.set(实例,value)  ---- 对应模板
        field.set(target, session);
    }


}
