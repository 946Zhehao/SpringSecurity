package com.demo.springsecurity.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//配置类
//启动SpringSecurity
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //设计一个权限
        //首页所有人可以访问，功能页只有有权限的人才能访问
        http.authorizeRequests()
                //设置路径 “/” 为所有人都能访问
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");

        //没有权限设置跳转到登录页，如果不加loginPage，会使用它内置的登录页;
        http.formLogin().loginPage("/toLogin").usernameParameter("username").passwordParameter("password");

        // 开启注销，注销过跳到首页
        http.logout().logoutSuccessUrl("/");

        //关闭crsf（跨站域请求伪造）功能
        http.csrf().disable();

        //开启 记住我 功能 cookie，默认会保存两周  ，自定义接受前端的参数
        http.rememberMe().rememberMeParameter("rememberMe");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //                                   设置编码方式
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                //设置用户权限，正常情况下去数据库中读，密码需要设置编码方式
                .withUser("LZH").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2", "vip3")
                .and()
                .withUser("LeanMice").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1", "vip2", "vip3");

    }
}
