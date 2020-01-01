package com.jjzy;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Author:   wj
 * Date:     2019/12/31 11:52
 * shiro 使用 .ini 文件
 */
public class TestAuthenticationApp {
    private static final transient Logger log = LoggerFactory.getLogger(TestAuthenticationApp.class);

    public static void main(String[] args) {
        String username = "one";
        String password = "123456";
        // 1，创建安全管理器的工厂对象
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // 2,使用工厂创建安全管理器
        SecurityManager securityManager = factory.getInstance();
        // 3,把当前的安全管理器绑定当到线的线程
        SecurityUtils.setSecurityManager(securityManager);
        // 4,使用SecurityUtils.getSubject得到主体对象
        Subject subject = SecurityUtils.getSubject();
        // 5，封装用户名和密码
        AuthenticationToken token = new UsernamePasswordToken(username, password);

        //6.获取认证
        try {
            subject.login(token);
            System.out.println("1");
            log.info("认证成功");
        } catch (AuthenticationException e) {
            System.out.println("11");
            log.info("认证失败");
        }
        //判断是否授权通过
        boolean authenticated = subject.isAuthenticated();
        System.out.println("是否授权" + authenticated);





        //7,1判断是否有角色
        boolean permitted = subject.hasRole("role1");
        log.info("授权单个:" + permitted);
        //7.2分别判断用户是否具有List中每个内容
        boolean[] roles = subject.hasRoles(Arrays.asList("role1", "role2", "role3"));
        log.info("多个授权");
        for (boolean role : roles) {
            System.out.println(role);
        }
        //判断当前用户是否拥有集合里的全部权限
        boolean allRoles = subject.hasAllRoles(Arrays.asList("role1", "role2", "role3"));
        System.out.println("是否拥有全部权限:" + allRoles);

        //8.1权限判断
        boolean a = subject.isPermitted("user:query");
        System.out.println("是否拥有查询权限" + a);
        boolean[] permitted1 = subject.isPermitted("user:create", "user:add");
        for (boolean p : permitted1) {
            System.out.println("是否拥有查询权限" + p);
        }
    }
}
