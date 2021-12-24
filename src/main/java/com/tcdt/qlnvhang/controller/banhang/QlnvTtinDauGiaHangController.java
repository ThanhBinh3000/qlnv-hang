package com.tcdt.qlnvhang.controller.banhang;

import java.util.ArrayList;
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
import com.tcdt.qlnvhang.repository.QlnvQdMuaHangHdrRepository;
import com.tcdt.qlnvhang.repository.QlnvTtinDauGiaHangRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvTtinDauGiaHangDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvTtinDauGiaHangHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvTtinDauGiaHangSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.TtinDauGiaHangSpecification;
import com.tcdt.qlnvhang.table.QlnvQdMuaHangHdr;
import com.tcdt.qlnvhang.table.QlnvTtinDauGiaHangDtl;
import com.tcdt.qlnvhang.table.QlnvTtinDauGiaHangDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvTtinDauGiaHangHdr;
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
@RequestMapping(value = PathContains.QL_LCDVBH + PathContains.QL_TTIN_DGIA_HANG)
@Api(tags = "Quản lý thông tin đấu giá mua hàng DTQG")
public class QlnvTtinDauGiaHangController extends BaseController {
	@Autowired
	private QlnvTtinDauGiaHangRepository ttinDauGiaHangRepository;

	@Autowired
	private QlnvQdMuaHangHdrRepository qlnvQdMuaHangHdrRepository;

	@ApiOperation(value = "Tạo mới thông tin đấu giá mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest request,
			@Valid @RequestBody QlnvTtinDauGiaHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			QlnvTtinDauGiaHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvTtinDauGiaHangHdr.class);

			Optional<QlnvQdMuaHangHdr> opCheck = qlnvQdMuaHangHdrRepository
					.findBySoQdinhAndTrangThai(objReq.getSoQdKhoach(), Contains.DUYET);
			if (!opCheck.isPresent())
				throw new Exception("Quyết định kế hoạch mua hàng số "+ objReq.getSoQdKhoach()+" không tồn tại");
			
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setNgayTao(getDateTimeNow());

			if (objReq.getDetail() != null) {
				List<QlnvTtinDauGiaHangDtlCtiet> detailChild;
				List<QlnvTtinDauGiaHangDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvTtinDauGiaHangDtlReq dtlReq : dtlReqList) {
					QlnvTtinDauGiaHangDtl detail = ObjectMapperUtils.map(dtlReq, QlnvTtinDauGiaHangDtl.class);
					detailChild = new ArrayList<QlnvTtinDauGiaHangDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvTtinDauGiaHangDtlCtiet.class);
					detail.setChildren(detailChild);
					dataMap.addChild(detail);
				}
			}

			QlnvTtinDauGiaHangHdr createCheck = ttinDauGiaHangRepository.save(dataMap);
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

	@ApiOperation(value = "Xoá thông tin thông tin đấu giá mua hàng DTQG", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<QlnvTtinDauGiaHangHdr> qOptional = ttinDauGiaHangRepository.findById(idSearchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			ttinDauGiaHangRepository.delete(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu thông tin đấu giá mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvTtinDauGiaHangSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvTtinDauGiaHangHdr> dataPage = ttinDauGiaHangRepository.findAll(TtinDauGiaHangSpecification.buildSearchQuery(objReq),pageable);

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

	@ApiOperation(value = "Cập nhật thông tin đấu giá mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody QlnvTtinDauGiaHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {

			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdMuaHangHdr> opCheck = qlnvQdMuaHangHdrRepository
					.findBySoQdinhAndTrangThai(objReq.getSoQdKhoach(), Contains.DUYET);
			if (!opCheck.isPresent())
				throw new Exception("Quyết định kế hoạch mua hàng không tồn tại");

			Optional<QlnvTtinDauGiaHangHdr> qOptional = ttinDauGiaHangRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			QlnvTtinDauGiaHangHdr dataDB = qOptional.get();
			QlnvTtinDauGiaHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvTtinDauGiaHangHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));

			if (objReq.getDetail() != null) {
				List<QlnvTtinDauGiaHangDtlCtiet> detailChild;
				for (QlnvTtinDauGiaHangDtlReq dtlReq : objReq.getDetail()) {
					QlnvTtinDauGiaHangDtl detail = ObjectMapperUtils.map(dtlReq, QlnvTtinDauGiaHangDtl.class);
					detailChild = new ArrayList<QlnvTtinDauGiaHangDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvTtinDauGiaHangDtlCtiet.class);
					detail.setChildren(detailChild);
					dataDB.addChild(detail);
				}
			}
			QlnvTtinDauGiaHangHdr createCheck = ttinDauGiaHangRepository.save(dataDB);
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

	@ApiOperation(value = "Lấy chi tiết thông tin đấu giá mua hàng DTQG", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Đề xuất kế hoạch mua trực tiếp", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvTtinDauGiaHangHdr> qOptional = ttinDauGiaHangRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 thông tin đấu giá mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest request, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvTtinDauGiaHangHdr> qHoach = ttinDauGiaHangRepository.findById(Long.valueOf(stReq.getId()));
			if (!qHoach.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			String status = stReq.getTrangThai();
			switch (status) {
			case Contains.CHO_DUYET:
				qHoach.get().setNguoiGuiDuyet(getUserName(request));
				qHoach.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.DUYET:
				qHoach.get().setNguoiPduyet(getUserName(request));
				qHoach.get().setNgayPduyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI:
				qHoach.get().setLdoTuchoi(stReq.getLyDo());
				break;
			default:
				break;
			}

			qHoach.get().setTrangThai(stReq.getTrangThai());
			ttinDauGiaHangRepository.save(qHoach.get());

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