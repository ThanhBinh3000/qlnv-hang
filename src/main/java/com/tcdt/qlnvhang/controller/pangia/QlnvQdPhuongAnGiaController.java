package com.tcdt.qlnvhang.controller.pangia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.tcdt.qlnvhang.repository.QlnvDxPhuongAnGiaHdrRepository;
import com.tcdt.qlnvhang.repository.QlnvQdPhuongAnGiaHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvQdPhuongAnGiaDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdPhuongAnGiaHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdPhuongAnGiaSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QDinhPAnGiaSpecification;
import com.tcdt.qlnvhang.table.QlnvDxPhuongAnGiaHdr;
import com.tcdt.qlnvhang.table.QlnvQdPhuongAnGiaDtl;
import com.tcdt.qlnvhang.table.QlnvQdPhuongAnGiaDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdPhuongAnGiaHdr;
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
@RequestMapping(value = PathContains.QL_PANGIA + PathContains.QL_QD_PANGIA)
@Api(tags = "Quản lý quyết định phương án giá của Tổng cục ")
public class QlnvQdPhuongAnGiaController extends BaseController {
	@Autowired
	private QlnvQdPhuongAnGiaHdrRepository qdPAnGiaHdrRepository;

	@Autowired
	private QlnvDxPhuongAnGiaHdrRepository qlnvDxPhuongAnGiaHdrRepository;
	
	
	@ApiOperation(value = "Tổng hợp thông đề xuất để làm quyết định", response = List.class)
	@PostMapping(value = PathContains.URL_THOP_DATA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> sumarryData(HttpServletRequest request,
			@Valid @RequestBody QlnvQdPhuongAnGiaSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {			
			List<QlnvDxPhuongAnGiaHdr> data = qlnvDxPhuongAnGiaHdrRepository.findAll(QDinhPAnGiaSpecification.buildTHopQuery(objReq));
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

	@ApiOperation(value = "Tạo mới quyết định phương án giá của Tổng cục ", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest req, @Valid @RequestBody QlnvQdPhuongAnGiaHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			QlnvQdPhuongAnGiaHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdPhuongAnGiaHdr.class);

			dataMap.setNgayQd(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(req));
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setSoQdinh(getUUID(Contains.QUYET_DINH));

			if (objReq.getDetail() != null) {
				List<QlnvQdPhuongAnGiaDtlCtiet> detailChild;
				List<QlnvQdPhuongAnGiaDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdPhuongAnGiaDtlReq dtlReq : dtlReqList) {
					QlnvQdPhuongAnGiaDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdPhuongAnGiaDtl.class);
					detailChild = new ArrayList<QlnvQdPhuongAnGiaDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdPhuongAnGiaDtlCtiet.class);
					detail.setChildren(detailChild);
					dataMap.addChild(detail);
				}
			}

			QlnvQdPhuongAnGiaHdr createCheck = qdPAnGiaHdrRepository.save(dataMap);
			if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
				List<String> soDxuatList = createCheck.getChildren().stream().map(QlnvQdPhuongAnGiaDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxPhuongAnGiaHdrRepository.updateTongHop(soDxuatList, Contains.TONG_HOP);
			}
			resp.setData(createCheck);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật quyết định phương án giá của Tổng cục ", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest req, @Valid @RequestBody QlnvQdPhuongAnGiaHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdPhuongAnGiaHdr> qOptional = qdPAnGiaHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			if (qOptional.get().getChildren().size() > 0) {
				List<String> soDxuatList = qOptional.get().getChildren().stream().map(QlnvQdPhuongAnGiaDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxPhuongAnGiaHdrRepository.updateTongHop(soDxuatList, Contains.DUYET);
			}
			
			

			QlnvQdPhuongAnGiaHdr dataDB = qOptional.get();
			QlnvQdPhuongAnGiaHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdPhuongAnGiaHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(req));

			if (objReq.getDetail() != null) {
				List<QlnvQdPhuongAnGiaDtlCtiet> detailChild;
				List<QlnvQdPhuongAnGiaDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdPhuongAnGiaDtlReq dtlReq : dtlReqList) {
					QlnvQdPhuongAnGiaDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdPhuongAnGiaDtl.class);
					detailChild = new ArrayList<QlnvQdPhuongAnGiaDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdPhuongAnGiaDtlCtiet.class);
					detail.setChildren(detailChild);
					dataDB.addChild(detail);
				}
			}

			QlnvQdPhuongAnGiaHdr createCheck = qdPAnGiaHdrRepository.save(dataDB);

			if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
				List<String> soDxuatList = createCheck.getChildren().stream().map(QlnvQdPhuongAnGiaDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxPhuongAnGiaHdrRepository.updateTongHop(soDxuatList, Contains.TONG_HOP);
			}

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

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định phương án giá của Tổng cục ", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID quyết định kế hoạch mua hàng DTQG", example = "54", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdPhuongAnGiaHdr> qOptional = qdPAnGiaHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 quyết định phương án giá của Tổng cục ", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest req, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdPhuongAnGiaHdr> qHoach = qdPAnGiaHdrRepository.findById(Long.valueOf(stReq.getId()));
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

			qHoach.get().setTrangThai(stReq.getTrangThai());
			qdPAnGiaHdrRepository.save(qHoach.get());

			if (status.equals(Contains.TU_CHOI) && qHoach.get().getChildren().size() > 0) {
				List<String> soDxuatList = qHoach.get().getChildren().stream().map(QlnvQdPhuongAnGiaDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxPhuongAnGiaHdrRepository.updateTongHop(soDxuatList, Contains.DUYET);
			}

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu quyết định phương án giá của Tổng cục ", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvQdPhuongAnGiaSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

			Page<QlnvQdPhuongAnGiaHdr> dataPage = qdPAnGiaHdrRepository
					.findAll(QDinhPAnGiaSpecification.buildSearchQuery(objReq), pageable);

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
}
