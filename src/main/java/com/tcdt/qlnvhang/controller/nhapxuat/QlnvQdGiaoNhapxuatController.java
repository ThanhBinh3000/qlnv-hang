package com.tcdt.qlnvhang.controller.nhapxuat;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.tcdt.qlnvhang.repository.QlnvQdGiaoNhapxuatHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvQdGiaoNhapxuatDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdGiaoNhapxuatHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdGiaoNhapxuatSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvQdGiaoNhapxuatSpecification;
import com.tcdt.qlnvhang.table.QlnvQdGiaoNhapxuatDtl;
import com.tcdt.qlnvhang.table.QlnvQdGiaoNhapxuatHdr;
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
@RequestMapping(value = PathContains.QL_NXUAT_HANG + PathContains.QL_QD_NXUAT_HANG)
@Api(tags = "Quản lý quyết định giao nhập/xuất hàng")
public class QlnvQdGiaoNhapxuatController extends BaseController {
	@Autowired
	private QlnvQdGiaoNhapxuatHdrRepository qlnvQdGiaoNhapxuatHdrRepository;

	@ApiOperation(value = "Tạo mới quyết định giao nhập/xuất hàng", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest req,
			@Valid @RequestBody QlnvQdGiaoNhapxuatHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getSoHdong()))
				throw new Exception("Không tìm thấy số hợp đồng");

			Optional<QlnvQdGiaoNhapxuatHdr> qOptional = qlnvQdGiaoNhapxuatHdrRepository
					.findBySoHdong(objReq.getSoHdong());
			if (qOptional.isPresent())
				throw new Exception("Số hợp đồng đã tồn tại");

			// Add thong tin hdr
			QlnvQdGiaoNhapxuatHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdGiaoNhapxuatHdr.class);

			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(req));
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setLoaiHdong(objReq.getLoaiHdong().equals(Contains.QD_NHAP) ? Contains.QD_NHAP : Contains.QD_XUAT);
			// Add thong tin detail
			List<QlnvQdGiaoNhapxuatDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdGiaoNhapxuatDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvQdGiaoNhapxuatDtl.class);
			dataMap.setChildren(dtls);

			QlnvQdGiaoNhapxuatHdr createCheck = qlnvQdGiaoNhapxuatHdrRepository.save(dataMap);

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

	@ApiOperation(value = "Cập nhật quyết định giao nhập/xuất hàng", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest req,
			@Valid @RequestBody QlnvQdGiaoNhapxuatHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdGiaoNhapxuatHdr> qOptional = qlnvQdGiaoNhapxuatHdrRepository
					.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			objReq.setSoHdong(null);
			QlnvQdGiaoNhapxuatHdr dataDB = qOptional.get();
			QlnvQdGiaoNhapxuatHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdGiaoNhapxuatHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(req));

			// Add thong tin detail
			List<QlnvQdGiaoNhapxuatDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdGiaoNhapxuatDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvQdGiaoNhapxuatDtl.class);
			dataDB.setChildren(dtls);

			QlnvQdGiaoNhapxuatHdr createCheck = qlnvQdGiaoNhapxuatHdrRepository.save(dataDB);

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

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định giao nhập/xuất hàng", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID quyết định kế hoạch mua trực tiếp", example = "54", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdGiaoNhapxuatHdr> qOptional = qlnvQdGiaoNhapxuatHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 quyết định giao nhập/xuất hàng", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest req, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdGiaoNhapxuatHdr> qHoach = qlnvQdGiaoNhapxuatHdrRepository
					.findById(Long.valueOf(stReq.getId()));
			if (!qHoach.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			String status = stReq.getTrangThai();
			switch (status) {
			case Contains.CHO_DUYET:
				qHoach.get().setNguoiGuiDuyet(getUserName(req));
				qHoach.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.DUYET:
				qHoach.get().setNguoiPduyet(getUserName(req));
				qHoach.get().setNgayPduyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI:
				qHoach.get().setLdoTuchoi(stReq.getLyDo());
				break;
			default:
				break;
			}

			qHoach.get().setTrangThai(status);
			qlnvQdGiaoNhapxuatHdrRepository.save(qHoach.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu quyết định giao nhập/xuất hàng", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvQdGiaoNhapxuatSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

			Page<QlnvQdGiaoNhapxuatHdr> dataPage = qlnvQdGiaoNhapxuatHdrRepository
					.findAll(QlnvQdGiaoNhapxuatSpecification.buildSearchQuery(objReq), pageable);

			resp.setData(dataPage);
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