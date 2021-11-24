package com.tcdt.qlnvhang.controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.google.gson.Gson;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.jwt.TokenAuthenticationService;
import com.tcdt.qlnvhang.repository.QlnvKhLcntVtuHdrRepository;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.ReportPrint;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvKhLcntVtuDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvKhLcntVtuHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvKhLcntVtuHdrSearchReq;
import com.tcdt.qlnvhang.request.SimpleSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.table.QlnvKhLcntVtuHdr;
import com.tcdt.qlnvhang.table.QlnvKhLcntVtuDtl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperPrint;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ke-hoach-lcnt/vat-tu")
@Api(tags = "Kế hoạch lựa chọn nhà thầu cung cấp vật tư")
public class QlnvKhLcntVtuHdrController extends BaseController {
	@Autowired
	private QlnvKhLcntVtuHdrRepository qlnvKhLcntVtuHdrRepository;

	@ApiOperation(value = "Tạo mới kế hoạch lựa chọn nhà thầu cung cấp vật tư", response = List.class)
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(@Valid HttpServletRequest request, @RequestBody QlnvKhLcntVtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		Calendar cal = Calendar.getInstance();
		try {
			List<QlnvKhLcntVtuDtlReq> dtlReqList = objReq.getDetailList();
			objReq.setDetailList(null);
			QlnvKhLcntVtuHdr dataMap = new ModelMapper().map(objReq, QlnvKhLcntVtuHdr.class);
			Authentication authentication = TokenAuthenticationService.getAuthentication(request);
			dataMap.setNgayTao(cal.getTime());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(authentication.getName());
				for (QlnvKhLcntVtuDtlReq dtlReq : dtlReqList) {
					QlnvKhLcntVtuDtl detail = new ModelMapper().map(dtlReq, QlnvKhLcntVtuDtl.class);
					detail.setDoc(new Gson().toJson(dtlReq.getNdungGoiThau()));
					dataMap.addDetail(detail);
				}
				qlnvKhLcntVtuHdrRepository.save(dataMap);
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


	@ApiOperation(value = "Xoá thông tin kế hoạch lựa chọn nhà thầu cung cấp vật tư", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
			Long qhoachId = idSearchReq.getId();
			Optional<QlnvKhLcntVtuHdr> QlnvKhLcntVtuHdr = qlnvKhLcntVtuHdrRepository.findById(qhoachId);
			if (!QlnvKhLcntVtuHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");
			//QlnvKhLcntVtuDtlRepository.deleteByIdQhHdr(qhoachId);
			qlnvKhLcntVtuHdrRepository.deleteById(qhoachId);
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu kế hoạch lựa chọn nhà thầu cung cấp vật tư", response = List.class)
	@PostMapping(value = "/findList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@RequestBody QlnvKhLcntVtuHdrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvKhLcntVtuHdr> qhKho = qlnvKhLcntVtuHdrRepository.selectParams(objReq.getMaVtu(),
					objReq.getTrangThai(), objReq.getNguoiTao(), objReq.getTuNgayLap(), objReq.getDenNgayLap(),
					pageable);

			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setData(qhKho);
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật kế hoạch lựa chọn nhà thầu cung cấp vật tư", response = List.class)
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(@Valid HttpServletRequest request,@RequestBody QlnvKhLcntVtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
			Calendar cal = Calendar.getInstance();
			Optional<QlnvKhLcntVtuHdr> QlnvKhLcntVtuHdr = qlnvKhLcntVtuHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!QlnvKhLcntVtuHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			QlnvKhLcntVtuHdr dataDTB = QlnvKhLcntVtuHdr.get();
			List<QlnvKhLcntVtuDtlReq> dtlReqList = objReq.getDetailList();
			objReq.setDetailList(null);
			QlnvKhLcntVtuHdr dataMap = new ModelMapper().map(objReq, QlnvKhLcntVtuHdr.class);
			Authentication authentication = TokenAuthenticationService.getAuthentication(request);
			updateObjectToObject(dataDTB, dataMap);
			dataDTB.setNgaySua(cal.getTime());
			dataDTB.setNguoiSua(authentication.getName());
			List<QlnvKhLcntVtuDtl> dtlList = dataDTB.getDetailList();
			for (QlnvKhLcntVtuDtl dtl : dtlList) {
				dataDTB.removeDetail(dtl);
			}
			for (QlnvKhLcntVtuDtlReq dtlReq : dtlReqList) {
				QlnvKhLcntVtuDtl detail = new ModelMapper().map(dtlReq, QlnvKhLcntVtuDtl.class);
				detail.setDoc(new Gson().toJson(dtlReq.getNdungGoiThau()));
				dataDTB.addDetail(detail);
				//detailList.add(detail);
			}
			qlnvKhLcntVtuHdrRepository.save(dataDTB);
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
	@ApiOperation(value = "Lấy chi tiết thông tin kế hoạch lựa chọn nhà thầu cung cấp vật tư", response = List.class)
	@GetMapping(value = "/chi-tiet/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID kế hoạch lựa chọn nhà thầu cung cấp vật tư", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvKhLcntVtuHdr> qOptional = qlnvKhLcntVtuHdrRepository.findById(Long.parseLong(ids));
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
	@ApiOperation(value = "Vụ: Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 kế hoạch lựa chọn nhà thầu cung cấp vật tư", response = List.class)
	@PostMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest request, @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvKhLcntVtuHdr> qHoach = qlnvKhLcntVtuHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!qHoach.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");
			qHoach.get().setTrangThai(stReq.getTrangThai());
			String status = stReq.getTrangThai();
			Calendar cal = Calendar.getInstance();
			Authentication authentication = TokenAuthenticationService.getAuthentication(request);
			switch(status) {
				case Contains.CHO_DUYET:
					qHoach.get().setNguoiGuiDuyet(authentication.getName());
					qHoach.get().setNgayGuiDuyet(cal.getTime());
					break;
				case Contains.DUYET:
					qHoach.get().setNguoiPduyet(authentication.getName());
					qHoach.get().setNgayPduyet(cal.getTime());
					break;
				case Contains.TU_CHOI:
					qHoach.get().setLdoTuchoi(stReq.getLyDo());
					break;
				default:
					break;
			}
			qlnvKhLcntVtuHdrRepository.save(qHoach.get());
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}