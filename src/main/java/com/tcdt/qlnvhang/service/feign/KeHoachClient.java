package com.tcdt.qlnvhang.service.feign;

import com.tcdt.qlnvhang.request.feign.KeHoachRequest;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "qlnv-khoach")
public interface KeHoachClient {

	@PostMapping("/chi-tieu-ke-hoach-nam/chi-tiet-kehoach-donvi")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> getDetailByCode(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody KeHoachRequest objReq);

}
