package com.tcdt.qlnvhang.service.feign;

import com.tcdt.qlnvhang.request.feign.TrangThaiHtReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.table.PhieuNhapXuatHistory;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "qlnv-kho")
public interface KhoClient {
	@PostMapping("/mlk/info-mlk")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<BaseResponse> infoMlk(@RequestBody TrangThaiHtReq objReq);
}
