package com.tcdt.qlnvhang.controller.xuatkhac;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvDxkhXuatKhacHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvDxkhXuatKhacDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvDxkhXuatKhacHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvDxkhXuatKhacSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvDxkhXuatKhacSpecification;
import com.tcdt.qlnvhang.table.QlnvDxkhXuatKhacDtl;
import com.tcdt.qlnvhang.table.QlnvDxkhXuatKhacHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.PathContains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = PathContains.QL_DXUAT_KH_XUATKHAC)
@Api(tags = "Quản lý đề xuất kế hoạch xuất khác")
public class QlnvDxkhXuatKhacHdrController extends BaseController {
	@Autowired
	private QlnvDxkhXuatKhacHdrRepository qlnvDxkhXuatKhacHdrRepository;
	
	@ApiOperation(value = "Tạo mới Đề xuất kế hoạch xuất khác", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest request,
			@Valid @RequestBody QlnvDxkhXuatKhacHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			List<QlnvDxkhXuatKhacDtlReq> dtlReqList = objReq.getDetailListReq();

			QlnvDxkhXuatKhacHdr dataMap = ObjectMapperUtils.map(objReq, QlnvDxkhXuatKhacHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setMaDvi(getDvi(request).getMaDvi());

			List<QlnvDxkhXuatKhacDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvDxkhXuatKhacDtl.class);
			dataMap.setChildren(dtls);

			QlnvDxkhXuatKhacHdr createCheck = qlnvDxkhXuatKhacHdrRepository.save(dataMap);

			resp.setData(createCheck);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Xoá thông tin Đề xuất kế hoạch xuất khác", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<QlnvDxkhXuatKhacHdr> qOptional = qlnvDxkhXuatKhacHdrRepository.findById(idSearchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			qlnvDxkhXuatKhacHdrRepository.delete(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu Đề xuất kế hoạch xuất khác", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvDxkhXuatKhacSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

			Page<QlnvDxkhXuatKhacHdr> dataPage = qlnvDxkhXuatKhacHdrRepository.findAll(QlnvDxkhXuatKhacSpecification.buildSearchQuery(objReq), pageable);

			resp.setData(dataPage);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (

		Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật Đề xuất kế hoạch xuất khác", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody QlnvDxkhXuatKhacHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvDxkhXuatKhacHdr> qlnvDxkhXuatKhacHdr = qlnvDxkhXuatKhacHdrRepository
					.findById(Long.valueOf(objReq.getId()));
			if (!qlnvDxkhXuatKhacHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			QlnvDxkhXuatKhacHdr dataDB = qlnvDxkhXuatKhacHdr.get();

			List<QlnvDxkhXuatKhacDtlReq> dtlReqList = objReq.getDetailListReq();
			QlnvDxkhXuatKhacHdr dataMap = ObjectMapperUtils.map(objReq, QlnvDxkhXuatKhacHdr.class);

			updateObjectToObject(dataDB, dataMap);
			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));

			List<QlnvDxkhXuatKhacDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvDxkhXuatKhacDtl.class);
			dataDB.setChildren(dtls);

			qlnvDxkhXuatKhacHdrRepository.save(dataDB);

			resp.setData(dataDB);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết thông tin Đề xuất kế hoạch xuất khác", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Đề xuất kế hoạch xuất khác", example = "1", required = true) @PathVariable("id") String id) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(id))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvDxkhXuatKhacHdr> qOptional = qlnvDxkhXuatKhacHdrRepository.findById(Long.parseLong(id));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 Đề xuất kế hoạch xuất khác", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest request, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvDxkhXuatKhacHdr> qOptional = qlnvDxkhXuatKhacHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			String status = stReq.getTrangThai();
			switch (status) {
			case Contains.CHO_DUYET:
				qOptional.get().setNguoiGuiDuyet(getUserName(request));
				qOptional.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.DUYET:
				qOptional.get().setNguoiPduyet(getUserName(request));
				qOptional.get().setNgayPduyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI:
				qOptional.get().setLdoTuchoi(stReq.getLyDo());
				break;
			default:
				break;
			}

			qOptional.get().setTrangThai(stReq.getTrangThai());
			qlnvDxkhXuatKhacHdrRepository.save(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}
