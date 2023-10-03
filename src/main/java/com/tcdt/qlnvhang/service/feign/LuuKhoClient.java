package com.tcdt.qlnvhang.service.feign;

import com.tcdt.qlnvhang.request.feign.TrangThaiHtReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.table.PhieuNhapXuatHistory;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "qlnv-luukho")
public interface LuuKhoClient {

	@PostMapping("/hang-trong-kho/synchronizeData")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public String synchronizeData(@RequestBody PhieuNhapXuatHistory objReq);

	@PostMapping("/hang-trong-kho/trang-thai-ht")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<BaseResponse> trangThaiHt(@RequestBody TrangThaiHtReq objReq);
}
