package com.tcdt.qlnvhang.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
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

import com.tcdt.qlnvhang.entities.QlnvDmDonviEntity;
import com.tcdt.qlnvhang.repository.QlnvDmDonviEntityRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.QlnvTtinDthauHhoaHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.QlnvDmDonviSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.QlnvTtinDthauHhoaDtl1Req;
import com.tcdt.qlnvhang.request.object.QlnvTtinDthauHhoaDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvTtinDthauHhoaHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntAggReq;
import com.tcdt.qlnvhang.request.search.QlnvTtinDthauHhoaHdrSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.table.QlnvTtinDthauHhoaDtl;
import com.tcdt.qlnvhang.table.QlnvTtinDthauHhoaDtl1;
import com.tcdt.qlnvhang.table.QlnvTtinDthauHhoaHdr;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.PaginationSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ttin-dthau-hhoa")
@Api(tags = "Thông tin đấu thầu hàng hóa")
public class QlnvTtinDthauHhoaHdrController extends BaseController {
	@Autowired
	private QlnvTtinDthauHhoaHdrRepository QlnvTtinDthauHhoaHdrRepository;
	
	
	@Autowired
	private QlnvDmDonviEntityRepository qDmDonviEntityRepository;
	
	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;
	
	
	@ApiOperation(value = "Tạo mới Thông tin đấu thầu hàng hóa", response = List.class)
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,@Valid @RequestBody QlnvTtinDthauHhoaHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		Calendar cal = Calendar.getInstance();
		try {
			List<QlnvTtinDthauHhoaDtlReq> dtlReqList = objReq.getDtlList();
			objReq.setDtlList(null);
			QlnvTtinDthauHhoaHdr dataMap = new ModelMapper().map(objReq, QlnvTtinDthauHhoaHdr.class);
			dataMap.setNgayTao(cal.getTime());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
				for (QlnvTtinDthauHhoaDtlReq dtlReq : dtlReqList) {
					QlnvTtinDthauHhoaDtl detail = new ModelMapper().map(dtlReq, QlnvTtinDthauHhoaDtl.class);
					List<QlnvTtinDthauHhoaDtl1Req> dtlCtietReqList = dtlReq.getDtl1List();
					//dtlReq.setDtl1List(null);
						for (QlnvTtinDthauHhoaDtl1Req dtlCtReq : dtlCtietReqList) {
							QlnvTtinDthauHhoaDtl1 detailCtiet = new ModelMapper().map(dtlCtReq, QlnvTtinDthauHhoaDtl1.class);
							detail.addDetail1(detailCtiet);
						}
					dataMap.addDetail(detail);
				}
				QlnvTtinDthauHhoaHdrRepository.save(dataMap);
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
	
	@ApiOperation(value = "Xoá thông tin Thông tin đấu thầu hàng hóa", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/delete/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@ApiParam(value = "ID Thông tin đấu thầu hàng hóa", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvTtinDthauHhoaHdr> QlnvTtinDthauHhoaHdr = QlnvTtinDthauHhoaHdrRepository.findById(Long.parseLong(ids));
			if (!QlnvTtinDthauHhoaHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");
			QlnvTtinDthauHhoaHdrRepository.delete(QlnvTtinDthauHhoaHdr.get());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu Thông tin đấu thầu hàng hóa", response = List.class)
	@PostMapping(value = "/findList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvTtinDthauHhoaHdrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

			Page<QlnvTtinDthauHhoaHdr> qhKho = QlnvTtinDthauHhoaHdrRepository.selectParams(objReq.getSoQdKh(),objReq.getMaHhoa(), pageable);

			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setData(qhKho);
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật Thông tin đấu thầu hàng hóa", response = List.class)
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,@Valid @RequestBody QlnvTtinDthauHhoaHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
			Calendar cal = Calendar.getInstance();
			Optional<QlnvTtinDthauHhoaHdr> QlnvTtinDthauHhoaHdr = QlnvTtinDthauHhoaHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!QlnvTtinDthauHhoaHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			QlnvTtinDthauHhoaHdr dataDTB = QlnvTtinDthauHhoaHdr.get();
			List<QlnvTtinDthauHhoaDtlReq> dtlReqList = objReq.getDtlList();
			objReq.setDtlList(null);
			QlnvTtinDthauHhoaHdr dataMap = new ModelMapper().map(objReq, QlnvTtinDthauHhoaHdr.class);
			updateObjectToObject(dataDTB, dataMap);
			dataDTB.setNgaySua(cal.getTime());
			dataDTB.setNguoiSua(getUserName(request));
			List<QlnvTtinDthauHhoaDtl> dtlList = dataDTB.getDetailList();
			for (QlnvTtinDthauHhoaDtl dtl : dtlList) { 
				List<QlnvTtinDthauHhoaDtl1> dtlCtietList = dtl.getDetail1List();
				for (QlnvTtinDthauHhoaDtl1 dtlCtiet : dtlCtietList) {
					dtl.removeDetail1(dtlCtiet);
				}
				
				dataDTB.removeDetail(dtl);
			}
			for (QlnvTtinDthauHhoaDtlReq dtlReq : dtlReqList) {
				QlnvTtinDthauHhoaDtl detail = new ModelMapper().map(dtlReq, QlnvTtinDthauHhoaDtl.class);
				List<QlnvTtinDthauHhoaDtl1Req> dtlCtietReqList = dtlReq.getDtl1List();
				//dtlReq.setDtl1List(null);
				for (QlnvTtinDthauHhoaDtl1Req dtlCtReq : dtlCtietReqList) {
					QlnvTtinDthauHhoaDtl1 detailCtiet = new ModelMapper().map(dtlCtReq, QlnvTtinDthauHhoaDtl1.class);
					detail.addDetail1(detailCtiet);
				}
				dataDTB.addDetail(detail);
				//detailList.add(detail);
			}
			QlnvTtinDthauHhoaHdrRepository.save(dataDTB);
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
	
	@ApiOperation(value = "Lấy chi tiết thông tin Thông tin đấu thầu hàng hóa", response = List.class)
	@GetMapping(value = "/chi-tiet/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Thông tin đấu thầu hàng hóa", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvTtinDthauHhoaHdr> qOptional = QlnvTtinDthauHhoaHdrRepository.findById(Long.parseLong(ids));
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
	
	@ApiOperation(value = "Lấy danh sách đơn vị cấp Chi cục", response = List.class)
	@PostMapping(value = "/ds-donvi-ccuc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colectionCcuc(@Valid @RequestBody StrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			List<QlnvDmDonvi> data = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(objReq.getStr(), Contains.HOAT_DONG);
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