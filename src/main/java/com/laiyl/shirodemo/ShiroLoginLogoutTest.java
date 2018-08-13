package com.laiyl.shirodemo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class ShiroLoginLogoutTest {

    @Test
    public void testHelloWorld() {

        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(true, subject.isAuthenticated());

        if  (subject.hasRole("role1")) {
            System.out.println("zhang has role1");
        }

        //subject.checkPermissions("system:update");
        subject.checkPermissions("find:view:2");

        subject.logout();

    }

    @Test
    public void testBase64() {
        String str = "hello";

        String salt = "123";

        String simpleHash = new SimpleHash("md5", str, salt).toString();

        System.out.println(simpleHash);
    }

    @Test
    public void testBit() {
        String permission = "+user1+2";
        String[] strs = permission.split("\\+");
        System.out.println(strs[0]);
        System.out.println(strs[1]);
        System.out.println(1 & 9);
    }

    @After
    public void after() {
        ThreadContext.unbindSubject();
    }


}
