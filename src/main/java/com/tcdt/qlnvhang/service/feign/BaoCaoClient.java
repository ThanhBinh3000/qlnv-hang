package com.tcdt.qlnvhang.service.feign;

import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "qlnv-report")
public interface BaoCaoClient {

	@PostMapping("/report-template/findByTenFile")
	@Headers({ "Accept: application/json; charset=utf-8","Content-Type: application/x-www-form-urlencoded"  })
	public ResponseEntity<String> findByTenFile(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
												@RequestBody ReportTemplateRequest objReq);

}
