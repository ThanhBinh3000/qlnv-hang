package com.tcdt.qlnvhang.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
	@Bean
	public Docket api1() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.tcdt.qlnvhang.controller"))
				.paths(PathSelectors.regex("/.*")).build().securitySchemes(Lists.newArrayList(apiKey()))
				.securityContexts(Lists.newArrayList(securityContext())).apiInfo(apiEndPointsInfo())
				.groupName("Quản lý hàng");
	}
	
	@Bean
	public Docket api2() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.tcdt.qlnvhang.controller.hopdong"))
				.paths(PathSelectors.regex("/.*")).build().securitySchemes(Lists.newArrayList(apiKey()))
				.securityContexts(Lists.newArrayList(securityContext())).apiInfo(apiEndPointsInfo())
				.groupName("Quản lý hợp đồng xuất/nhập hàng");
	}
	
	@Bean
	public Docket api3() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.tcdt.qlnvhang.controller.nhapxuat"))
				.paths(PathSelectors.regex("/.*")).build().securitySchemes(Lists.newArrayList(apiKey()))
				.securityContexts(Lists.newArrayList(securityContext())).apiInfo(apiEndPointsInfo())
				.groupName("Quản lý nhập/xuất hàng");
	}
	
	@Bean
	public Docket api7() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.tcdt.qlnvhang.controller.banhang"))
				.paths(PathSelectors.regex("/.*")).build().securitySchemes(Lists.newArrayList(apiKey()))
				.securityContexts(Lists.newArrayList(securityContext())).apiInfo(apiEndPointsInfo())
				.groupName("Quản lý lựa chọn đơn vị mua hàng");
	}

	@Bean
	public Docket api4() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.tcdt.qlnvhang.controller.dchuyenhang"))
				.paths(PathSelectors.regex("/.*")).build().securitySchemes(Lists.newArrayList(apiKey()))
				.securityContexts(Lists.newArrayList(securityContext())).apiInfo(apiEndPointsInfo())
				.groupName("Quản lý điều chuyển hàng");
	}
	@Bean
	public Docket api5() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.tcdt.qlnvhang.controller.bonganh"))
				.paths(PathSelectors.regex("/.*")).build().securitySchemes(Lists.newArrayList(apiKey()))
				.securityContexts(Lists.newArrayList(securityContext())).apiInfo(apiEndPointsInfo())
				.groupName("Quản lý nhập xuất hàng Bộ ngành");
	}
	@Bean
	public Docket api6() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.tcdt.qlnvhang.controller.pangia"))
				.paths(PathSelectors.regex("/.*")).build().securitySchemes(Lists.newArrayList(apiKey()))
				.securityContexts(Lists.newArrayList(securityContext())).apiInfo(apiEndPointsInfo())
				.groupName("Quản lý phương án giá mua, bán hàng DTQG");
	}
	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("TCDT REST API").description("Quản lý API hệ thống Quản lý nghiệp vụ - TCDT")
				.version("1.0.0").build();
	}

	@Bean
	SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}
}