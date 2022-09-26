package com.tcdt.qlnvhang.service.feign;

import com.tcdt.qlnvhang.request.QlnvDmDonviSearchReq;
import com.tcdt.qlnvhang.request.object.HhDmDviLquanSearchReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.tcdt.qlnvhang.request.BaseRequest;

import feign.Headers;

@FeignClient(name = "qlnv-category")
public interface CategoryServiceProxy {

	@GetMapping("/dmuc-donvi/chi-tiet/{ids}")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> getDetail(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable("ids") String ids);

	@PostMapping("/dmuc-donvi/chi-tiet")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> getDetailByCode(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody BaseRequest objReq);

	@GetMapping("/dmuc-chung/danh-sach/{loai}")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> getDanhMucChung(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable("loai") String loai);

	@PostMapping("/dmuc-donvi/tat-ca")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> getDanhMucDvi(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,@RequestBody QlnvDmDonviSearchReq objReq);

	@PostMapping("/dmuc-dvi-lquan/tat-ca")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> getDanhMucDviLquan(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody HhDmDviLquanSearchReq objReq);

	@GetMapping("/dm-hang/danh-sach/dvql")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> getDanhMucHangHoa(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader);

}
