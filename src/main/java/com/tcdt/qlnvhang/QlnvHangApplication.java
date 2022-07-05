package com.tcdt.qlnvhang;

import com.tcdt.qlnvhang.repository.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = { "com.tcdt.qlnvhang.entities","com.tcdt.qlnvhang.table" })
@EnableFeignClients(basePackages = "com.tcdt.qlnvhang.service.feign")
@EnableJpaRepositories(basePackages = "com.tcdt.qlnvhang.repository", repositoryBaseClass = BaseRepositoryImpl.class)
public class QlnvHangApplication {
	public static void main(String[] args) {
		SpringApplication.run(QlnvHangApplication.class, args);
	}

}
