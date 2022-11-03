package com.tcdt.qlnvhang.controller;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcdt.qlnvhang.entities.QlnvDmDonviEntity;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvDmDonviEntityRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.QlnvTtinDthauVtuHdrRepository;
import com.tcdt.qlnvhang.request.QlnvDmDonviSearchReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.QlnvTtinDthauVtuDtl1Req;
import com.tcdt.qlnvhang.request.object.QlnvTtinDthauVtuDtl2Req;
import com.tcdt.qlnvhang.request.object.QlnvTtinDthauVtuDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvTtinDthauVtuHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvTtinDthauVtuHdrSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvTtinDthauVtuSpecification;
import com.tcdt.qlnvhang.table.QlnvTtinDthauVtuDtl;
import com.tcdt.qlnvhang.table.QlnvTtinDthauVtuDtl1;
import com.tcdt.qlnvhang.table.QlnvTtinDthauVtuDtl2;
import com.tcdt.qlnvhang.table.QlnvTtinDthauVtuHdr;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.QL_TTIN_DTHAU_VTU)
@Api(tags = "Thông tin đấu thầu vật tư")
public class QlnvTtinDthauVtuHdrController extends BaseController {
	@Autowired
	private QlnvTtinDthauVtuHdrRepository QlnvTtinDthauVtuHdrRepository;
	
	
	@Autowired
	private QlnvDmDonviEntityRepository qDmDonviEntityRepository;
	
	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;
	
	
	@ApiOperation(value = "Tạo mới Thông tin đấu thầu vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,@Valid @RequestBody QlnvTtinDthauVtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			List<QlnvTtinDthauVtuDtlReq> dtlReqList = objReq.getDtlList();
			objReq.setDtlList(null);
			QlnvTtinDthauVtuHdr dataMap = new ModelMapper().map(objReq, QlnvTtinDthauVtuHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
				for (QlnvTtinDthauVtuDtlReq dtlReq : dtlReqList) {
					QlnvTtinDthauVtuDtl detail = new ModelMapper().map(dtlReq, QlnvTtinDthauVtuDtl.class);
					List<QlnvTtinDthauVtuDtl1Req> dtlCtietReqList = dtlReq.getDtl1List();
					//dtlReq.setDtl1List(null);
						for (QlnvTtinDthauVtuDtl1Req dtlCtReq : dtlCtietReqList) {
							QlnvTtinDthauVtuDtl1 detailCtiet = new ModelMapper().map(dtlCtReq, QlnvTtinDthauVtuDtl1.class);
							detail.addDetail1(detailCtiet);
						}
					List<QlnvTtinDthauVtuDtl2Req> dtl2CtietReqList = dtlReq.getDtl2List();
					//dtlReq.setDtl1List(null);
						for (QlnvTtinDthauVtuDtl2Req dtlCtReq : dtl2CtietReqList) {
							QlnvTtinDthauVtuDtl2 detailCtiet = new ModelMapper().map(dtlCtReq, QlnvTtinDthauVtuDtl2.class);
							detail.addDetail2(detailCtiet);
						}
					dataMap.addDetail(detail);
				}
				QlnvTtinDthauVtuHdrRepository.save(dataMap);
				
			resp.setData(dataMap);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Xoá thông tin Thông tin đấu thầu vật tư", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@ApiParam(value = "ID Thông tin đấu thầu vật tư", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvTtinDthauVtuHdr> QlnvTtinDthauVtuHdr = QlnvTtinDthauVtuHdrRepository.findById(Long.parseLong(ids));
			if (!QlnvTtinDthauVtuHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");
			QlnvTtinDthauVtuHdrRepository.delete(QlnvTtinDthauVtuHdr.get());
			
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu Thông tin đấu thầu vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvTtinDthauVtuHdrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

			Page<QlnvTtinDthauVtuHdr> qhKho = QlnvTtinDthauVtuHdrRepository.findAll(QlnvTtinDthauVtuSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "Cập nhật Thông tin đấu thầu vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,@Valid @RequestBody QlnvTtinDthauVtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
			Optional<QlnvTtinDthauVtuHdr> QlnvTtinDthauVtuHdr = QlnvTtinDthauVtuHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!QlnvTtinDthauVtuHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			QlnvTtinDthauVtuHdr dataDTB = QlnvTtinDthauVtuHdr.get();
			List<QlnvTtinDthauVtuDtlReq> dtlReqList = objReq.getDtlList();
			objReq.setDtlList(null);
			QlnvTtinDthauVtuHdr dataMap = new ModelMapper().map(objReq, QlnvTtinDthauVtuHdr.class);
			updateObjectToObject(dataDTB, dataMap);
			dataDTB.setNgaySua(getDateTimeNow());
			dataDTB.setNguoiSua(getUserName(request));
			List<QlnvTtinDthauVtuDtl> dtlList = dataDTB.getDetailList();
			for (QlnvTtinDthauVtuDtl dtl : dtlList) { 
				List<QlnvTtinDthauVtuDtl1> dtlCtietList = dtl.getDetail1List();
				for (QlnvTtinDthauVtuDtl1 dtlCtiet : dtlCtietList) {
					dtl.removeDetail1(dtlCtiet);
				}
				
				List<QlnvTtinDthauVtuDtl2> dtl2CtietList = dtl.getDetail2List();
				for (QlnvTtinDthauVtuDtl2 dtlCtiet : dtl2CtietList) {
					dtl.removeDetail2(dtlCtiet);
				}
				
				dataDTB.removeDetail(dtl);
			}
			for (QlnvTtinDthauVtuDtlReq dtlReq : dtlReqList) {
				QlnvTtinDthauVtuDtl detail = new ModelMapper().map(dtlReq, QlnvTtinDthauVtuDtl.class);
				List<QlnvTtinDthauVtuDtl1Req> dtlCtietReqList = dtlReq.getDtl1List();
				//dtlReq.setDtl1List(null);
				for (QlnvTtinDthauVtuDtl1Req dtlCtReq : dtlCtietReqList) {
					QlnvTtinDthauVtuDtl1 detailCtiet = new ModelMapper().map(dtlCtReq, QlnvTtinDthauVtuDtl1.class);
					detail.addDetail1(detailCtiet);
				}
				List<QlnvTtinDthauVtuDtl2Req> dtl2CtietReqList = dtlReq.getDtl2List();
				//dtlReq.setDtl1List(null);
				for (QlnvTtinDthauVtuDtl2Req dtlCtReq : dtl2CtietReqList) {
					QlnvTtinDthauVtuDtl2 detailCtiet = new ModelMapper().map(dtlCtReq, QlnvTtinDthauVtuDtl2.class);
					detail.addDetail2(detailCtiet);
				}
				dataDTB.addDetail(detail);
				//detailList.add(detail);
			}
			QlnvTtinDthauVtuHdrRepository.save(dataDTB);
			resp.setData(dataDTB);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Lấy chi tiết thông tin Thông tin đấu thầu vật tư", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Thông tin đấu thầu vật tư", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvTtinDthauVtuHdr> qOptional = QlnvTtinDthauVtuHdrRepository.findById(Long.parseLong(ids));
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
	@ApiOperation(value = "Lấy danh sách đơn vị", response = List.class)
	@PostMapping(value = "/ds-donvi", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(@Valid @RequestBody QlnvDmDonviSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
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