package com.tcdt.qlnvhang.controller.dchuyenhang;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
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

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvDxkhDChuyenHangHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvDxkhDChuyenHangDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvDxkhDChuyenHangHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvDxkhDChuyenHangSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.DXuatKHDChuyenHangSpecification;
import com.tcdt.qlnvhang.table.QlnvDxkhDChuyenHangDtl;
import com.tcdt.qlnvhang.table.QlnvDxkhDChuyenHangHdr;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = PathContains.QL_DCHANG + PathContains.QL_DXKH_DCHANG)
@Api(tags = "Quản lý đề xuất kế hoạch điều chuyển hàng DTQG")
public class QlnvDxkhDChuyenHangController extends BaseController {
	@Autowired
	private QlnvDxkhDChuyenHangHdrRepository qlnvDxkhDChuyenHangHdrRepository;

	@ApiOperation(value = "Tạo mới Đề xuất kế hoạch điều chuyển hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest request,
			@Valid @RequestBody QlnvDxkhDChuyenHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			List<QlnvDxkhDChuyenHangDtlReq> dtlReqList = objReq.getDetailListReq();

			QlnvDxkhDChuyenHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvDxkhDChuyenHangHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setMaDvi(getDvi(request).getMaDvi());
			// add detail
			List<QlnvDxkhDChuyenHangDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvDxkhDChuyenHangDtl.class);
			dataMap.setChildren(dtls);
			// save db
			QlnvDxkhDChuyenHangHdr createCheck = qlnvDxkhDChuyenHangHdrRepository.save(dataMap);
			// return client
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

	@ApiOperation(value = "Xoá thông tin Đề xuất kế hoạch điều chuyển hàng DTQG", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<QlnvDxkhDChuyenHangHdr> qOptional = qlnvDxkhDChuyenHangHdrRepository.findById(idSearchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			qlnvDxkhDChuyenHangHdrRepository.delete(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu Đề xuất kế hoạch điều chuyển hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvDxkhDChuyenHangSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

			Page<QlnvDxkhDChuyenHangHdr> dataPage = qlnvDxkhDChuyenHangHdrRepository
					.findAll(DXuatKHDChuyenHangSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "Cập nhật Đề xuất kế hoạch điều chuyển hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody QlnvDxkhDChuyenHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvDxkhDChuyenHangHdr> QlnvDxkhMuaTtHdr = qlnvDxkhDChuyenHangHdrRepository
					.findById(Long.valueOf(objReq.getId()));
			if (!QlnvDxkhMuaTtHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			QlnvDxkhDChuyenHangHdr dataDB = QlnvDxkhMuaTtHdr.get();

			List<QlnvDxkhDChuyenHangDtlReq> dtlReqList = objReq.getDetailListReq();
			QlnvDxkhDChuyenHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvDxkhDChuyenHangHdr.class);

			updateObjectToObject(dataDB, dataMap);
			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));

			List<QlnvDxkhDChuyenHangDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvDxkhDChuyenHangDtl.class);
			dataDB.setChildren(dtls);

			qlnvDxkhDChuyenHangHdrRepository.save(dataDB);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết thông tin Đề xuất kế hoạch điều chuyển hàng DTQG", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Đề xuất kế hoạch mua trực tiếp", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvDxkhDChuyenHangHdr> qOptional = qlnvDxkhDChuyenHangHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 Đề xuất kế hoạch điều chuyển hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest request, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvDxkhDChuyenHangHdr> qHoach = qlnvDxkhDChuyenHangHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!qHoach.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			String status = stReq.getTrangThai();
			// Lay thong tin don vi quan ly
			QlnvDmDonvi objDvi = getDvi(request);
			if (ObjectUtils.isEmpty(objDvi) || StringUtils.isEmpty(objDvi.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");

			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC) && objDvi.getTrangThai().equals(Contains.TCUC_DUYET)) {
				throw new UnsupportedOperationException("Người sử dụng không phải cấp tổng cục để phê duyệt");
			}
			if (!objDvi.getCapDvi().equals(Contains.CAP_CUC) && objDvi.getTrangThai().equals(Contains.CUC_DUYET)) {
				throw new UnsupportedOperationException("Người sử dụng không phải cấp cục để phê duyệt");
			}
			if (!objDvi.getCapDvi().equals(Contains.CAP_CHI_CUC) && objDvi.getTrangThai().equals(Contains.CCUC_DUYET)) {
				throw new UnsupportedOperationException("Người sử dụng không phải cấp chi cục để phê duyệt");
			}
			switch (status) {
			case Contains.CHO_DUYET:
				qHoach.get().setNguoiGuiDuyet(getUserName(request));
				qHoach.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.CCUC_DUYET:
				qHoach.get().setNguoiCcucPduyet(getUserName(request));
				qHoach.get().setNgayCcucPduyet(getDateTimeNow());
				break;
			case Contains.CUC_DUYET:
				qHoach.get().setNguoiCucPduyet(getUserName(request));
				qHoach.get().setNgayCucPduyet(getDateTimeNow());
				break;
			case Contains.TCUC_DUYET:
				qHoach.get().setNguoiTcucPduyet(getUserName(request));
				qHoach.get().setNgayTcucPduyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI:
				qHoach.get().setLdoTuchoi(stReq.getLyDo());
				break;
			default:
				break;
			}

			qHoach.get().setTrangThai(stReq.getTrangThai());
			qlnvDxkhDChuyenHangHdrRepository.save(qHoach.get());
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