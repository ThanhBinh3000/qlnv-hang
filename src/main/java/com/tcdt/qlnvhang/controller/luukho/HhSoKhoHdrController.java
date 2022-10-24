package com.tcdt.qlnvhang.controller.luukho;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.tcdt.qlnvhang.table.HhSoKhoHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.TokenAuthenticationService;
import com.tcdt.qlnvhang.repository.HhSoKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.sokho.HhSoKhoHdrReq;
import com.tcdt.qlnvhang.request.search.sokho.HHSoKhoHdrSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/business/so-kho")
@Api(tags = "Quản lý thông tin sổ kho")
public class HhSoKhoHdrController extends BaseController {

	@Autowired
	private HhSoKhoHdrRepository qlnvSoKhoRepository;

	@ApiOperation(value = "Tạo mới Thông tin sổ kho", response = List.class)
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,@Valid @RequestBody HhSoKhoHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		Calendar cal = Calendar.getInstance();
		try {
			UserInfo userInfo = UserUtils.getUserInfo();
			HhSoKhoHdr dataMap = new ModelMapper().map(objReq, HhSoKhoHdr.class);
			qlnvSoKhoRepository.save(dataMap);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá thông tin Thông tin sổ kho", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
			Long qhoachId = idSearchReq.getId();
			Optional<HhSoKhoHdr> qlnvSoKho = qlnvSoKhoRepository.findById(qhoachId);
			if (!qlnvSoKho.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");
			qlnvSoKhoRepository.deleteById(qhoachId);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu Thông tin sổ kho", response = List.class)
	@PostMapping(value = "/findList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@RequestBody HHSoKhoHdrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<HhSoKhoHdr> qhKho = qlnvSoKhoRepository.selectParams(objReq.getMaDvi(), objReq.getTenHhoa(),
					objReq.getMaHhoa(),objReq.getMaNgan(),objReq.getMaLo(), objReq.getTuNgayMoSo(), objReq.getDenNgayMoSo(), pageable);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(qhKho);
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật Thông tin sổ kho", response = List.class)
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request, @Valid @RequestBody HhSoKhoHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			UserInfo userInfo = UserUtils.getUserInfo();
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
			Calendar cal = Calendar.getInstance();
			Optional<HhSoKhoHdr> QlnvTtinHdongHdr = qlnvSoKhoRepository.findById(Long.valueOf(objReq.getId()));
			if (!QlnvTtinHdongHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			HhSoKhoHdr dataDTB = QlnvTtinHdongHdr.get();
			HhSoKhoHdr dataMap = new ModelMapper().map(objReq, HhSoKhoHdr.class);
			updateObjectToObject(dataDTB, dataMap);
			qlnvSoKhoRepository.save(dataDTB);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết thông tin Thông tin sổ kho", response = List.class)
	@GetMapping(value = "/chi-tiet/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Thông tin hợp đồng", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<HhSoKhoHdr> qOptional = qlnvSoKhoRepository.findById(Long.parseLong(ids));
			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			resp.setData(qOptional);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "In sổ kho", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void export(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response, HttpServletRequest req)
			throws Exception {		
		try {
			if (StringUtils.isEmpty(searchReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<HhSoKhoHdr> qOptional = qlnvSoKhoRepository.findById(searchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=SO_KHO_" + getDateTimeNow() + ".docx");

			// Add gia tri bien header
			HashMap<String, String> mappings = new HashMap<String, String>();			
			QlnvDmDonvi dvi = getDvi(req);
			mappings.put("param1", dvi.getTenDvi());
			mappings.put("param2", dvi.getDiaChi());
			mappings.put("param3", qOptional.get().getThuKho());
			mappings.put("param4", "");
			mappings.put("param5", qOptional.get().getMaHhoa());
			mappings.put("param6", qOptional.get().getDviTinh());
			mappings.put("param7", "");
			mappings.put("param8", convertDateToString(qOptional.get().getNgayMoSo()));
			mappings.put("param9", convertDateToString(qOptional.get().getNgayMoSo()));
			
			TestObject obj = new TestObject(1, "22/12/2021", "123","1234","dien giai", "20/12/2021", "10000","2000000","2222");
			List<TestObject> lstObject = new ArrayList<TestObject>();
			lstObject.add(obj);
			
			List<Map<String, Object>> lstMap = new ArrayList<Map<String,Object>>();
			lstObject.stream().forEach(o -> {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("stt", o.getStt());
				map.put("ngay", o.getNgay());
				map.put("ctu_nhap", o.getCtu_nhap());
				map.put("ctu_xuat", o.getCtu_xuat());
				map.put("diengiai", o.getDiengiai());
				map.put("ngay_nhapxuat", o.getNgay_nhapxuat());
				map.put("nhap", o.getNhap());
				map.put("xuat", o.getXuat());
				map.put("ton", o.getTon());
				lstMap.add(map);
			});
			
			Doc4jUtils.generateDoc(Contains.TEMPLATE_SO_KHO, mappings, null, dataOutput);
			
			dataOutput.flush();
			dataOutput.close();
		} catch (Exception e) {
			log.error("In sổ kho", e);
			final Map<String, Object> body = new HashMap<>();
			body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			body.put("msg", e.getMessage());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), body);
		}
	}
	
	@Data
	@AllArgsConstructor
	public class TestObject {
		Integer stt;
		String ngay;
		String ctu_nhap;
		String ctu_xuat;
		String diengiai;
		String ngay_nhapxuat;
		String nhap;
		String xuat;
		String ton;
	}
}
