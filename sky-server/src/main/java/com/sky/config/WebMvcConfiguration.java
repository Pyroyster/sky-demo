package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docket() { // Docket是 Swagger 中的主要配置类，用于配置 Swagger 文档生成器
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build(); // ApiInfo 对象，它用于描述 API 文档的基本信息。
        return new Docket(DocumentationType.SWAGGER_2) // 用 Swagger 2 规范生成文档。
                .apiInfo(apiInfo) // 将前面创建的 ApiInfo 对象关联到 Docket，用来配置文档的基础信息。
                .select() // 定义 API 文档生成的选择器，返回一个 ApiSelectorBuilder
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller")) // 指定哪些类中的接口需要生成文档，只扫描 com.sky.controller 包下的接口。
                .paths(PathSelectors.any()) // 表示对所有路径的请求都生成文档
                .build(); // 调用 build() 方法生成 Docket 实例
    }

    /**
     * 设置静态资源映射，使得接口文档页面可以被访问到
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) { // ResourceHandlerRegistry 类型，允许将请求路径映射到具体的静态资源位置
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/"); // addResourceHandlers 是 Spring 提供的一个钩子方法，用于设置静态资源的处理逻辑。
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
