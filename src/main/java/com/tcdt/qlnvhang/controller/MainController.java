package com.tcdt.qlnvhang.controller;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class MainController {

	@Value("${eureka.client.serviceUrl.defaultZone}")
	private String defaultZone;

	@Value("${spring.datasource.driver.class}")
	private String driverClassName;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String userName;

	@Value("${spring.datasource.password}")
	private String password;

	@Autowired
	private DataSource dataSource;

	@RequestMapping("/showConfig")
	@ResponseBody
	public String showConfig() {
		String configInfo = "eureka.client.serviceUrl.defaultZone= " + defaultZone //
				+ "<br/>spring.datasource.driver.class=" + driverClassName //
				+ "<br/>spring.datasource.url=" + url //
				+ "<br/>spring.datasource.username=" + userName //
				+ "<br/>spring.datasource.password=" + password;

		return configInfo;
	}

	@RequestMapping("/pingDataSource")
	@ResponseBody
	public String pingDataSource() {
		try {
			return this.dataSource.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

}