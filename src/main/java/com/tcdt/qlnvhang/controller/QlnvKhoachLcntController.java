package com.tcdt.qlnvhang.controller;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.activation.UnsupportedDataTypeException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.entities.DiemKhoEntity;
import com.tcdt.qlnvhang.entities.QlnvDmDonviEntity;
import com.tcdt.qlnvhang.entities.UserInfoEntity;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.QlnvDiemKhoEntityRepository;
import com.tcdt.qlnvhang.repository.QlnvKhoachLcntRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviEntityRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.request.object.QlnvKhoachLcntReq;
import com.tcdt.qlnvhang.request.object.TTinGoiThau;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.QlnvKhoachLcntSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.request.QlnvDmDonviSearchReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.table.QlnvKhoachLcnt;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.UserInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/ke-hoach-lcnt")
@Slf4j
@Api(tags = "Kế hoạch lựa chọn nhà thầu")
public class QlnvKhoachLcntController extends BaseController {

	@Autowired
	private QlnvKhoachLcntRepository qlnvKhoachLcntRepository;
	
	@Autowired
	private QlnvDmDonviEntityRepository qDmDonviEntityRepository;
	
	@Autowired
	private QlnvDiemKhoEntityRepository diemKhoEntityRepository;
	
	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;

	@ApiOperation(value = "Lấy chi tiết kế hoạch lựa chọn nhà thầu", response = List.class)
	@GetMapping(value = "/chi-tiet/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvKhoachLcnt> qOptional = qlnvKhoachLcntRepository.findById(Long.parseLong(ids));
			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			resp.setData(qOptional);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy danh sách kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = "/findList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(@Valid @RequestBody QlnvKhoachLcntSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
//			if (!GenericValidator.isDate(objReq.getTuNgayLap(), "dd/mm/yyyy", true))
//				throw new UnsupportedDataTypeException("Trường ngày không đúng định dạng");
//			if (!GenericValidator.isDate(objReq.getDenNgayLap(), "dd/mm/yyyy", true))
//				throw new UnsupportedDataTypeException("Trường ngày không đúng định dạng");

			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvKhoachLcnt> data = qlnvKhoachLcntRepository.selectParams(objReq.getMaDvi(), objReq.getLoaiHanghoa(),
					objReq.getTrangThai(), objReq.getNguoiTao(), objReq.getTuNgayLap(), objReq.getDenNgayLap(),
					pageable);
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Thêm mới kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody QlnvKhoachLcntReq objReq, HttpServletRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			TTinGoiThau doc = objReq.getDoc();
			objReq.setDoc(null);
			QlnvKhoachLcnt dataMap = new ModelMapper().map(objReq, QlnvKhoachLcnt.class);
			String username= getUserName(req);
			UserInfo u =  userInfoRepository.findByUsername(username);
			Optional<QlnvDmDonvi> dvi =   qlnvDmDonviRepository.findById(u.getDvql());
			dataMap.setMaDvi(dvi.get().getMaDvi());
			dataMap.setDoc(new Gson().toJson(doc));
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(req));
			dataMap.setNgayTao(new Date());

			QlnvKhoachLcnt createCheck = qlnvKhoachLcntRepository.save(dataMap);

			resp.setData(createCheck);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Sửa kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> edit(@Valid @RequestBody QlnvKhoachLcntReq objReq, HttpServletRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			Optional<QlnvKhoachLcnt> qOptional = qlnvKhoachLcntRepository.findById(objReq.getId());

			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Kế hoạch lựa chọn nhà thầu không tồn tại");
			TTinGoiThau doc = objReq.getDoc();
			objReq.setDoc(null);
			QlnvKhoachLcnt dataDTB = qOptional.get();
			QlnvKhoachLcnt dataMap = new ModelMapper().map(objReq, QlnvKhoachLcnt.class);

			updateObjectToObject(dataDTB, dataMap);

			dataDTB.setDoc(new Gson().toJson(doc));
			dataDTB.setNguoiSua(getUserName(req));
			dataDTB.setNgaySua(new Date());

			QlnvKhoachLcnt createCheck = qlnvKhoachLcntRepository.save(dataDTB);

			resp.setData(createCheck);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class, notes = "01: chờ duyệt; 02: Duyệt; 03: Từ chối")
	@PostMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest request, @RequestBody StatusReq stReq,
			HttpServletRequest req) {
		BaseResponse resp = new BaseResponse();
		try {
			if (stReq.getId() == null)
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvKhoachLcnt> approveCheck = qlnvKhoachLcntRepository.findById(stReq.getId());
			if (!approveCheck.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			String status = stReq.getTrangThai();
			switch (status) {
			case Contains.CHO_DUYET:
				approveCheck.get().setNguoiGuiDuyet(getUserName(req));
				approveCheck.get().setNgayGuiDuyet(new Date());
				break;
			case Contains.DUYET:
				approveCheck.get().setNguoiPduyet(getUserName(req));
				approveCheck.get().setNgayPduyet(new Date());
				break;
			case Contains.TU_CHOI:
				approveCheck.get().setLdoTuchoi(stReq.getLyDo());
				break;
			default:
				break;
			}

			approveCheck.get().setTrangThai(stReq.getTrangThai());
			qlnvKhoachLcntRepository.save(approveCheck.get());

			resp.setData(approveCheck);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xóa kế hoạch lựa chọn nhà thầu", response = List.class)
	@GetMapping(value = "/delete/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(
			@ApiParam(value = "ID kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvKhoachLcnt> qOptional = qlnvKhoachLcntRepository.findById(Long.parseLong(ids));
			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			qlnvKhoachLcntRepository.delete(qOptional.get());

			resp.setData(qOptional);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Lấy danh sách đơn vị", response = List.class)
	@PostMapping(value = "/ds-donvi", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(@Valid @RequestBody QlnvDmDonviSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
			Page<QlnvDmDonviEntity> data = qDmDonviEntityRepository.selectParams(objReq.getMaDvi(), objReq.getTenDvi(),
					objReq.getTrangThai(), objReq.getMaTinh(), objReq.getMaQuan(), objReq.getMaPhuong(),
					objReq.getCapDvi(), objReq.getKieuDvi(), objReq.getLoaiDvi(), pageable);
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Kiểm tra đơn vị theo mã", response = List.class)
	@PostMapping(value = "/find-ma-dv", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detailbycode(@Valid @RequestBody StrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getStr()))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			QlnvDmDonvi qOptional = qlnvDmDonviRepository.findByMaDvi(objReq.getStr());
			if (qOptional == null)
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			resp.setData(qOptional);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Lấy danh sách điểm kho theo đơn vị", response = List.class)
	@PostMapping(value = "/ds-diem-kho", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colectionDkho(@Valid @RequestBody StrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			List<DiemKhoEntity> data = diemKhoEntityRepository.selectParams(objReq.getStr());
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}