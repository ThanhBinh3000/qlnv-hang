package com.tcdt.qlnvhang.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
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

import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvQdLcntVtuHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntVtuDtlCtietReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntVtuDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntVtuHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntVtuHdrSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvQdLcntVtuSpecification;
import com.tcdt.qlnvhang.table.QlnvQdLcntVtuDtl;
import com.tcdt.qlnvhang.table.QlnvQdLcntVtuDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdLcntVtuHdr;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
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
@RequestMapping(value = PathContains.QL_QD_LCNT_VTU)
@Api(tags = "Quyết định phê duyệt KHLCNT vật tư")
public class QlnvQdLcntVtuHdrController extends BaseController {
	
	@Autowired
	private QlnvQdLcntVtuHdrRepository qlnvQdLcntVtuHdrRepository;
	
	@ApiOperation(value = "Tạo mới Quyết định phê duyệt KHLCNT vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntVtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			
			QlnvQdLcntVtuHdr dataMap = new ModelMapper().map(objReq, QlnvQdLcntVtuHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setLoaiQd(Contains.QUYET_DINH);
				
			List<QlnvQdLcntVtuDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdLcntVtuDtl> details = new ArrayList<>();
			if (dtlReqList != null) {
				List<QlnvQdLcntVtuDtlCtiet> detailChild;
				for (QlnvQdLcntVtuDtlReq dtlReq : dtlReqList) {
					List<QlnvQdLcntVtuDtlCtietReq> cTietReq = dtlReq.getDetail();
					QlnvQdLcntVtuDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdLcntVtuDtl.class);
					detailChild = new ArrayList<QlnvQdLcntVtuDtlCtiet>();
					if (cTietReq != null)
						detailChild = ObjectMapperUtils.mapAll(cTietReq, QlnvQdLcntVtuDtlCtiet.class);
					detail.setDetailList(detailChild);
					details.add(detail);
				}
				dataMap.setDetailList(details);
			}
			qlnvQdLcntVtuHdrRepository.save(dataMap);
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
	
	
	@ApiOperation(value = "Xoá thông tin Quyết định phê duyệt KHLCNT vật tư", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@ApiParam(value = "ID kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdLcntVtuHdr> optional = qlnvQdLcntVtuHdrRepository.findById(Long.parseLong(ids));
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");
			qlnvQdLcntVtuHdrRepository.delete(optional.get());
			
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
 
	@ApiOperation(value = "Tra cứu Quyết định phê duyệt KHLCNT vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvQdLcntVtuHdrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
			objReq.setLoaiQd(Contains.QUYET_DINH);
			Page<QlnvQdLcntVtuHdr> qdLcnt = qlnvQdLcntVtuHdrRepository.findAll(QlnvQdLcntVtuSpecification.buildSearchQuery(objReq), pageable);

			resp.setData(qdLcnt);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật Quyết định phê duyệt KHLCNT vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntVtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
			Optional<QlnvQdLcntVtuHdr> optional = qlnvQdLcntVtuHdrRepository.findById(Long.valueOf(objReq.getId()));
			
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			
			QlnvQdLcntVtuHdr dataDTB = optional.get();
			QlnvQdLcntVtuHdr dataMap = new ModelMapper().map(objReq, QlnvQdLcntVtuHdr.class);
			
			updateObjectToObject(dataDTB, dataMap);
			dataDTB.setNgaySua(getDateTimeNow());
			dataDTB.setNguoiSua(getUserName(request));
			dataDTB.setLoaiQd(Contains.QUYET_DINH);
			
			List<QlnvQdLcntVtuDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdLcntVtuDtl> details = new ArrayList<>();
			if (dtlReqList != null) {
				List<QlnvQdLcntVtuDtlCtiet> detailChild;
				for (QlnvQdLcntVtuDtlReq dtlReq : dtlReqList) {
					List<QlnvQdLcntVtuDtlCtietReq> cTietReq = dtlReq.getDetail();
					QlnvQdLcntVtuDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdLcntVtuDtl.class);
					detailChild = new ArrayList<QlnvQdLcntVtuDtlCtiet>();
					if (cTietReq != null)
						detailChild = ObjectMapperUtils.mapAll(cTietReq, QlnvQdLcntVtuDtlCtiet.class);
					detail.setDetailList(detailChild);
					details.add(detail);
				}
				dataMap.setDetailList(details);
			}
			
			qlnvQdLcntVtuHdrRepository.save(dataDTB);
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
	
	@ApiOperation(value = "Lấy chi tiết thông tin Quyết định phê duyệt KHLCNT vật tư", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Quyết định phê duyệt KHLCNT", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdLcntVtuHdr> qOptional = qlnvQdLcntVtuHdrRepository.findById(Long.parseLong(ids));
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
	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 Quyết định phê duyệt KHLCNT vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(HttpServletRequest request,@Valid  @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdLcntVtuHdr> qOptional = qlnvQdLcntVtuHdrRepository.findById(stReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");
			
			qOptional.get().setTrangThai(stReq.getTrangThai());
			String status = stReq.getTrangThai();
			Calendar cal = Calendar.getInstance();
			switch(status) {
				case Contains.CHO_DUYET:
					qOptional.get().setNguoiGuiDuyet(getUserName(request));
					qOptional.get().setNgayGuiDuyet(cal.getTime());
					break;
				case Contains.DUYET:
					qOptional.get().setNguoiPduyet(getUserName(request));
					qOptional.get().setNgayPduyet(cal.getTime());
					break;
				case Contains.TU_CHOI:
					qOptional.get().setLdoTuchoi(stReq.getLyDo());
					break;
				default:
					break;
			}
			qlnvQdLcntVtuHdrRepository.save(qOptional.get());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Tra cứu Quyết định phê duyệt KHLCNT vật tư dành cho cấp cục", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU
			+ PathContains.URL_CAP_CUC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colectionChild(HttpServletRequest request,
			@Valid @RequestBody QlnvQdLcntVtuHdrSearchReq objReq) {
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

			objReq.setLoaiQd(Contains.QUYET_DINH);
			Page<QlnvQdLcntVtuHdr> qlnvQdTlthHdr = qlnvQdLcntVtuHdrRepository.findAll(QlnvQdLcntVtuSpecification.buildSearchChildQuery(objReq), pageable);

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
	
	@ApiOperation(value = "Lấy chi tiết thông tin Quyết định phê duyệt KHLCNT vật tư dành cho cấp cục", response = List.class)
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

			List<QlnvQdLcntVtuHdr> qOptional = qlnvQdLcntVtuHdrRepository.findAll(QlnvQdLcntVtuSpecification.buildFindByIdQuery(objReq));

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
