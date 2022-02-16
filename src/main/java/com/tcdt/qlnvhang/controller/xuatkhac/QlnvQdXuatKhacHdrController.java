package com.tcdt.qlnvhang.controller.xuatkhac;

import java.util.ArrayList;
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
import com.tcdt.qlnvhang.repository.QlnvQdXuatKhacHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvQdXuatKhacDtlCtietReq;
import com.tcdt.qlnvhang.request.object.QlnvQdXuatKhacDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdXuatKhacHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdXuatKhacSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvQdXuatKhacSpecification;
import com.tcdt.qlnvhang.table.QlnvQdXuatKhacDtl;
import com.tcdt.qlnvhang.table.QlnvQdXuatKhacDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdXuatKhacHdr;
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
@RequestMapping(value = PathContains.QL_QD_DXUAT_KH_XUATKHAC)
@Api(tags = "Quản lý quyết định đề xuất kế hoạch xuất khác")
public class QlnvQdXuatKhacHdrController extends BaseController {
	@Autowired
	private QlnvQdXuatKhacHdrRepository qlnvQdXuatKhacHdrRepository;

	@ApiOperation(value = "Tạo mới quyết định Đề xuất kế hoạch xuất khác", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest request,
			@Valid @RequestBody QlnvQdXuatKhacHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			QlnvQdXuatKhacHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdXuatKhacHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));

			List<QlnvQdXuatKhacDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdXuatKhacDtl> details;
			if (dtlReqList != null) {
				details = new ArrayList<QlnvQdXuatKhacDtl>();
				List<QlnvQdXuatKhacDtlCtiet> detailChild;
				for (QlnvQdXuatKhacDtlReq dtlReq : dtlReqList) {
					List<QlnvQdXuatKhacDtlCtietReq> cTietReq = dtlReq.getDetail();
					QlnvQdXuatKhacDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdXuatKhacDtl.class);
					detailChild = new ArrayList<QlnvQdXuatKhacDtlCtiet>();
					if (cTietReq != null)
						detailChild = ObjectMapperUtils.mapAll(cTietReq, QlnvQdXuatKhacDtlCtiet.class);
					detail.setChildren(detailChild);
					details.add(detail);
				}
				dataMap.setChildren(details);
			}

			qlnvQdXuatKhacHdrRepository.save(dataMap);

			resp.setData(dataMap);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá thông tin quyết định Đề xuất kế hoạch xuất khác", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdXuatKhacHdr> qOptional = qlnvQdXuatKhacHdrRepository.findById(idSearchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			qlnvQdXuatKhacHdrRepository.delete(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu quyết định Đề xuất kế hoạch xuất khác", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvQdXuatKhacSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvQdXuatKhacHdr> dataPage = qlnvQdXuatKhacHdrRepository
					.findAll(QlnvQdXuatKhacSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "Cập nhật quyết định Đề xuất kế hoạch xuất khác", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody QlnvQdXuatKhacHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdXuatKhacHdr> qOptional = qlnvQdXuatKhacHdrRepository.findById(Long.valueOf(objReq.getId()));

			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			QlnvQdXuatKhacHdr dataDB = qOptional.get();
			QlnvQdXuatKhacHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdXuatKhacHdr.class);

			updateObjectToObject(dataDB, dataMap);
			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));

			List<QlnvQdXuatKhacDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdXuatKhacDtl> details;
			if (dtlReqList != null) {
				details = new ArrayList<QlnvQdXuatKhacDtl>();
				List<QlnvQdXuatKhacDtlCtiet> detailChild;
				for (QlnvQdXuatKhacDtlReq dtlReq : dtlReqList) {
					List<QlnvQdXuatKhacDtlCtietReq> cTietReq = dtlReq.getDetail();
					QlnvQdXuatKhacDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdXuatKhacDtl.class);
					detailChild = new ArrayList<QlnvQdXuatKhacDtlCtiet>();
					if (cTietReq != null)
						detailChild = ObjectMapperUtils.mapAll(cTietReq, QlnvQdXuatKhacDtlCtiet.class);
					detail.setChildren(detailChild);
					details.add(detail);
				}
				dataDB.setChildren(details);
			}
			qlnvQdXuatKhacHdrRepository.save(dataDB);

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

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định Đề xuất kế hoạch xuất khác", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Đề xuất kế hoạch xuất khác", example = "1", required = true) @PathVariable("id") String id) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(id))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdXuatKhacHdr> qOptional = qlnvQdXuatKhacHdrRepository.findById(Long.parseLong(id));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 quyết định Đề xuất kế hoạch xuất khác", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest request, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdXuatKhacHdr> qOptional = qlnvQdXuatKhacHdrRepository.findById(Long.valueOf(stReq.getId()));
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
			qlnvQdXuatKhacHdrRepository.save(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu quyết định Đề xuất kế hoạch xuất khác dành cho cấp cục", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU
			+ PathContains.URL_CAP_CUC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colectionChild(HttpServletRequest request,
			@Valid @RequestBody QlnvQdXuatKhacSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			// Lay thong tin don vi quan ly
			QlnvDmDonvi objDvi = getDvi(request);
			if (ObjectUtils.isEmpty(objDvi) || StringUtils.isEmpty(objDvi.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");

			// Add them dk loc trong child
			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC))
				objReq.setMaDvi(objDvi.getMaDvi());

			Page<QlnvQdXuatKhacHdr> qlnvQdTlthHdr = qlnvQdXuatKhacHdrRepository
					.findAll(QlnvQdXuatKhacSpecification.buildSearchChildQuery(objReq), pageable);

			resp.setData(qlnvQdTlthHdr);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định Đề xuất kế hoạch xuất khác dành cho cấp cục", response = List.class)
	@PostMapping(value = PathContains.URL_CHI_TIET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detailChild(@Valid @RequestBody IdSearchReq objReq,
			HttpServletRequest request) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			// Lay thong tin don vi quan ly
			QlnvDmDonvi objDvi = getDvi(request);
			if (ObjectUtils.isEmpty(objDvi) || StringUtils.isEmpty(objDvi.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");

			// Add them dk loc trong child
			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
				objReq.setMaDvi(objDvi.getMaDvi());
			} else if (objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC) && !StringUtils.isEmpty(objReq.getMaDvi())) {
				objReq.setMaDvi(objDvi.getMaDvi());
			}

			List<QlnvQdXuatKhacHdr> qOptional = qlnvQdXuatKhacHdrRepository
					.findAll(QlnvQdXuatKhacSpecification.buildFindByIdQuery(objReq));

			if (qOptional.isEmpty())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			resp.setData(qOptional.get(0));
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
