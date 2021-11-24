package com.tcdt.qlnvhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = { "com.tcdt.qlnvhang.entities","com.tcdt.qlnvhang.table" })
public class QlnvHangApplication {
	public static void main(String[] args) {
		SpringApplication.run(QlnvHangApplication.class, args);
	}

}
