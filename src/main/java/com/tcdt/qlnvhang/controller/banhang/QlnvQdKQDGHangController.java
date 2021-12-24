package com.tcdt.qlnvhang.controller.banhang;

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
import com.tcdt.qlnvhang.repository.QlnvQdKQDGHangRepository;
import com.tcdt.qlnvhang.repository.QlnvTtinDauGiaHangDtlRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvQdKQDGHangHdrReq;
import com.tcdt.qlnvhang.request.object.QlnvTtinDauGiaHangHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdKQDGHangSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdKQDGThopSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QdinhKQDauGiaSpecification;
import com.tcdt.qlnvhang.table.QlnvQdKQDGHangDtl;
import com.tcdt.qlnvhang.table.QlnvQdKQDGHangHdr;
import com.tcdt.qlnvhang.table.QlnvTtinDauGiaHangDtl;
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
@RequestMapping(value = PathContains.QL_LCDVBH + PathContains.QL_QDKQ_DGIA_HANG)
@Api(tags = "Quản lý quyết định kết quả đấu giá mua hàng DTQG")
public class QlnvQdKQDGHangController extends BaseController {
	@Autowired
	private QlnvQdKQDGHangRepository qdKQDGHangRepository;

	@Autowired
	private QlnvTtinDauGiaHangDtlRepository qlnvTtinDauGiaHangDtlRepository;

	@ApiOperation(value = "Tổng hợp thông tin chào giá để làm quyết định", response = List.class)
	@PostMapping(value = PathContains.URL_THOP_DATA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> tongHopData(HttpServletRequest request,
			@Valid @RequestBody QlnvQdKQDGThopSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			String maDvi = objReq.getMaDvi();
			if (StringUtils.isEmpty(maDvi)) {
				maDvi = getDvql(request);
			}
			List<QlnvTtinDauGiaHangDtl> data = qlnvTtinDauGiaHangDtlRepository
					.findAll(QdinhKQDauGiaSpecification.buildTHopQuery(objReq));
//					qlnvTtinDauGiaHangDtlRepository.selectParams(maDvi,
//					Contains.DUYET, objReq.getMaHhoa(), objReq.getLanDauGia());			
			resp.setData(data);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tạo mới quyết định kết quả đấu giá mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest request,
			@Valid @RequestBody QlnvQdKQDGHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			QlnvQdKQDGHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdKQDGHangHdr.class);
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setNgayTao(getDateTimeNow());
			if (objReq.getDetail() != null) {
				List<QlnvQdKQDGHangDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetail(), QlnvQdKQDGHangDtl.class);
				dataMap.setChildren(dtls);
			}			
			QlnvQdKQDGHangHdr createCheck = qdKQDGHangRepository.save(dataMap);
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

	@ApiOperation(value = "Xoá thông tin quyết định kết quả đấu giá mua hàng DTQG", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdKQDGHangHdr> qOptional = qdKQDGHangRepository.findById(idSearchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			qdKQDGHangRepository.delete(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu thông tin quyết định kết quả đấu giá mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvQdKQDGHangSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

//			Page<QlnvQdKQDGHangHdr> dataPage = qdKQDGHangRepository.selectParams(objReq.getMaDvi(),
//					objReq.getTrangThai(), objReq.getTuNgay(), objReq.getDenNgay(), objReq.getMaHhoa(),
//					objReq.getSoQdinh(), pageable);

			Page<QlnvQdKQDGHangHdr> dataPage = qdKQDGHangRepository
					.findAll(QdinhKQDauGiaSpecification.buildSearchQuery(objReq), pageable);

			resp.setData(dataPage);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật thông tin quyết định kết quả đấu giá mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody QlnvTtinDauGiaHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {

			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdKQDGHangHdr> qOptional = qdKQDGHangRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			QlnvQdKQDGHangHdr dataDB = qOptional.get();
			QlnvQdKQDGHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdKQDGHangHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));
			if (objReq.getDetail() != null) {
				List<QlnvQdKQDGHangDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetail(), QlnvQdKQDGHangDtl.class);
				dataMap.setChildren(dtls);
			}
			QlnvQdKQDGHangHdr createCheck = qdKQDGHangRepository.save(dataDB);
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

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định kết quả đấu giá mua hàng DTQG", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Đề xuất kế hoạch mua trực tiếp", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdKQDGHangHdr> qOptional = qdKQDGHangRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 quyết định kết quả đấu giá mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest request, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdKQDGHangHdr> qHoach = qdKQDGHangRepository.findById(Long.valueOf(stReq.getId()));
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
			qdKQDGHangRepository.save(qHoach.get());

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