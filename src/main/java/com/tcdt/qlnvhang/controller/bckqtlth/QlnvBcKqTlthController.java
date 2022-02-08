package com.tcdt.qlnvhang.controller.bckqtlth;

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
import com.tcdt.qlnvhang.repository.QlnvBcKqTlthRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvBcKqTlthReq;
import com.tcdt.qlnvhang.request.search.QlnvBcKqTlthSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvBcKqTlthSpecification;
import com.tcdt.qlnvhang.table.QlnvBcKqTlth;
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
@RequestMapping(value = PathContains.BC_KQ_TLTH)
@Api(tags = "Báo cáo kết quả Thanh lý, Tiêu hủy hàng DTQG")
public class QlnvBcKqTlthController extends BaseController{
	@Autowired
	private QlnvBcKqTlthRepository qlnvBcKqTlthRepository;
	
	@ApiOperation(value = "Tạo báo cáo Thanh lý, tiêu hủy hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest request,
			@Valid @RequestBody QlnvBcKqTlthReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			QlnvBcKqTlth dataMap = ObjectMapperUtils.map(objReq, QlnvBcKqTlth.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setMaDvi(getDvi(request).getMaDvi());
			
			// save
			QlnvBcKqTlth createCheck = qlnvBcKqTlthRepository.save(dataMap);
			
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
	
	@ApiOperation(value = "Xoá báo cáo Thanh lý, tiêu hủy hàng DTQG", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<QlnvBcKqTlth> qOptional = qlnvBcKqTlthRepository.findById(idSearchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			qlnvBcKqTlthRepository.delete(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Tra cứu báo cáo Thanh lý, tiêu hủy hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvBcKqTlthSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvBcKqTlth> dataPage = qlnvBcKqTlthRepository
					.findAll(QlnvBcKqTlthSpecification.buildSearchQuery(objReq), pageable);

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
	
	@ApiOperation(value = "Cập nhật Báo cáo Thanh lý, tiêu hủy hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody QlnvBcKqTlthReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvBcKqTlth> qlnvBcKqTlth = qlnvBcKqTlthRepository
					.findById(Long.valueOf(objReq.getId()));
			if (!qlnvBcKqTlth.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			QlnvBcKqTlth dataDB = qlnvBcKqTlth.get();
			QlnvBcKqTlth dataMap = ObjectMapperUtils.map(objReq, QlnvBcKqTlth.class);

			updateObjectToObject(dataDB, dataMap);
			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));

			
			resp.setData(qlnvBcKqTlthRepository.save(dataDB));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Lấy chi tiết thông tin Báo cáo Thanh lý, tiêu hủy hàng DTQG", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Báo cáo Thanh lý, tiêu hủy hàng DTQG", example = "1", required = true) @PathVariable("id") String id) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(id))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvBcKqTlth> qlnvBcKqTlth = qlnvBcKqTlthRepository.findById(Long.parseLong(id));
			if (!qlnvBcKqTlth.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			resp.setData(qlnvBcKqTlth);
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

			Optional<QlnvBcKqTlth> oQlnvBcKqTlth = qlnvBcKqTlthRepository.findById(Long.valueOf(stReq.getId()));
			if (!oQlnvBcKqTlth.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			QlnvBcKqTlth qlnvBcKqTlth = oQlnvBcKqTlth.get();
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
				qlnvBcKqTlth.setNguoiGuiDuyet(getUserName(request));
				qlnvBcKqTlth.setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TPHONG_DUYET:
				qlnvBcKqTlth.setTphongPduyet(getUserName(request));
				qlnvBcKqTlth.setNgayTphongPduyet(getDateTimeNow());
				break;
			case Contains.LANHDAO_DUYET:
				qlnvBcKqTlth.setLanhdaoPduyet(getUserName(request));
				qlnvBcKqTlth.setNgayLanhdaoPduyet(getDateTimeNow());
				break;
			
			default:
				break;
			}

			qlnvBcKqTlth.setTrangThai(stReq.getTrangThai());
			qlnvBcKqTlthRepository.save(qlnvBcKqTlth);
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
