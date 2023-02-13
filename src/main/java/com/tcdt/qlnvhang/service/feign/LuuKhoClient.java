package com.tcdt.qlnvhang.service.feign;

import com.tcdt.qlnvhang.request.feign.KeHoachRequest;
import com.tcdt.qlnvhang.table.PhieuNhapXuatHistory;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "qlnv-luukho")
public interface LuuKhoClient {

	@PostMapping("/hang-trong-kho/synchronizeData")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> synchronizeData(@RequestBody PhieuNhapXuatHistory objReq);

}
