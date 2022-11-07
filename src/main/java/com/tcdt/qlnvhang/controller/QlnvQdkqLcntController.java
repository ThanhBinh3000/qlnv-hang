package com.tcdt.qlnvhang.controller;

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
import com.tcdt.qlnvhang.repository.QlnvQdkqLcntRepository;
import com.tcdt.qlnvhang.repository.QlnvTtinDthauHhoaHdrRepository;
import com.tcdt.qlnvhang.repository.QlnvTtinDthauVtuHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvQdkqLcntReq;
import com.tcdt.qlnvhang.request.search.QlnvQdkqLcntSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvQdkqLcntSpecification;
import com.tcdt.qlnvhang.table.QlnvQdkqLcnt;
import com.tcdt.qlnvhang.table.QlnvTtinDthauHhoaHdr;
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
@RequestMapping(value = PathContains.QL_QDKQ_LCNT)
@Api(tags = "Quản lý QĐ phê duyệt KQLCNT")
public class QlnvQdkqLcntController extends BaseController {

	@Autowired
	private QlnvQdkqLcntRepository qlnvQdkqLcntRepository;
	
	@Autowired
	private QlnvTtinDthauVtuHdrRepository qlnvTtinDthauVtuHdrRepository;
	
	@Autowired
	private QlnvTtinDthauHhoaHdrRepository qlnvTtinDthauHhoaHdrRepository;
	
	@ApiOperation(value = "Tạo mới Quyết định phê duyệt KQLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,@Valid @RequestBody QlnvQdkqLcntReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			
			if (ObjectUtils.isEmpty(objReq.getIdTtinDthau())) {
				throw new UnsupportedOperationException("Không tồn tại bản ghi thông tin đấu thầu");
			}
			
			// Lay thong tin don vi quan ly
			QlnvDmDonvi objDvi = getDvi(request);
			if (ObjectUtils.isEmpty(objDvi) || StringUtils.isEmpty(objDvi.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");

			String maDviNopthau = null;
			if (objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
				objReq.setLoaiHanghoa(Contains.VAT_TU);
				Optional<QlnvTtinDthauVtuHdr> dthauVtu = qlnvTtinDthauVtuHdrRepository.findById(objReq.getIdTtinDthau());
				if (!dthauVtu.isPresent()) 
					throw new UnsupportedOperationException("Không tồn tại bản ghi thông tin đấu thầu");
				maDviNopthau = dthauVtu.get().getMaDvi();
			} else {
				objReq.setLoaiHanghoa(Contains.LUONG_THUC_MUOI);
				Optional<QlnvTtinDthauHhoaHdr> dthauHhoa = qlnvTtinDthauHhoaHdrRepository.findById(objReq.getIdTtinDthau());
				if (!dthauHhoa.isPresent())
					throw new UnsupportedOperationException("Không tồn tại bản ghi thông tin đấu thầu");
				maDviNopthau = dthauHhoa.get().getMaDvi();
			}
				
			if (StringUtils.isEmpty(maDviNopthau))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");
			
			QlnvDmDonvi dviNopthau = getDviByMa(request, maDviNopthau);
			if (ObjectUtils.isEmpty(dviNopthau) || StringUtils.isEmpty(dviNopthau.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");
						
			objReq.setIdDviNopthau(dviNopthau.getId());
			QlnvQdkqLcnt dataMap = new ModelMapper().map(objReq, QlnvQdkqLcnt.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			qlnvQdkqLcntRepository.save(dataMap);
			
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
	
	@ApiOperation(value = "Xoá thông tin Quyết định phê duyệt KQLCNT", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@ApiParam(value = "ID kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdkqLcnt> optional = qlnvQdkqLcntRepository.findById(Long.parseLong(ids));
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");
			qlnvQdkqLcntRepository.delete(optional.get());
			
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

	@ApiOperation(value = "Tra cứu Quyết định phê duyệt KQLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvQdkqLcntSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
			Page<QlnvQdkqLcnt> qdkq = qlnvQdkqLcntRepository.findAll(QlnvQdkqLcntSpecification.buildSearchQuery(objReq), pageable);

			resp.setData(qdkq);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật Quyết định phê duyệt KQLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,@Valid @RequestBody QlnvQdkqLcntReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
			Optional<QlnvQdkqLcnt> optional = qlnvQdkqLcntRepository.findById(Long.valueOf(objReq.getId()));
			
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			
			if (ObjectUtils.isEmpty(objReq.getIdTtinDthau())) {
				throw new UnsupportedOperationException("Không tồn tại bản ghi thông tin đấu thầu");
			}
			
			// Lay thong tin don vi quan ly
			QlnvDmDonvi objDvi = getDvi(request);
			if (ObjectUtils.isEmpty(objDvi) || StringUtils.isEmpty(objDvi.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");

			String maDviNopthau = null;
			if (objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
				objReq.setLoaiHanghoa(Contains.VAT_TU);
				Optional<QlnvTtinDthauVtuHdr> dthauVtu = qlnvTtinDthauVtuHdrRepository.findById(objReq.getIdTtinDthau());
				if (!dthauVtu.isPresent()) 
					throw new UnsupportedOperationException("Không tồn tại bản ghi thông tin đấu thầu");
				maDviNopthau = dthauVtu.get().getMaDvi();
			} else {
				objReq.setLoaiHanghoa(Contains.LUONG_THUC_MUOI);
				Optional<QlnvTtinDthauHhoaHdr> dthauHhoa = qlnvTtinDthauHhoaHdrRepository.findById(objReq.getIdTtinDthau());
				if (!dthauHhoa.isPresent())
					throw new UnsupportedOperationException("Không tồn tại bản ghi thông tin đấu thầu");
				maDviNopthau = dthauHhoa.get().getMaDvi();
			}
			
			
			if (StringUtils.isEmpty(maDviNopthau))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");
			
			QlnvDmDonvi dviNopthau = getDviByMa(request, maDviNopthau);
			if (ObjectUtils.isEmpty(dviNopthau) || StringUtils.isEmpty(dviNopthau.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");
			
			objReq.setIdDviNopthau(dviNopthau.getId());
			QlnvQdkqLcnt dataDTB = optional.get();
			QlnvQdkqLcnt dataMap = new ModelMapper().map(objReq, QlnvQdkqLcnt.class);
			
			updateObjectToObject(dataDTB, dataMap);
			dataDTB.setNgaySua(getDateTimeNow());
			dataDTB.setNguoiSua(getUserName(request));
			
			qlnvQdkqLcntRepository.save(dataDTB);
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
	
	@ApiOperation(value = "Lấy chi tiết thông tin Quyết định phê duyệt KQLCNT", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Quyết định phê duyệt KQLCNT", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdkqLcnt> qOptional = qlnvQdkqLcntRepository.findById(Long.parseLong(ids));
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
	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 Quyết định phê duyệt KQLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(HttpServletRequest request,@Valid  @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdkqLcnt> qdLcnt = qlnvQdkqLcntRepository.findById(Long.valueOf(stReq.getId()));
			if (!qdLcnt.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");
			qdLcnt.get().setTrangThai(stReq.getTrangThai());
			String status = stReq.getTrangThai();
			Calendar cal = Calendar.getInstance();
			switch(status) {
				case Contains.CHO_DUYET:
					qdLcnt.get().setNguoiGuiDuyet(getUserName(request));
					qdLcnt.get().setNgayGuiDuyet(cal.getTime());
					break;
				case Contains.DUYET:
					qdLcnt.get().setNguoiPduyet(getUserName(request));
					qdLcnt.get().setNgayPduyet(cal.getTime());
					break;
				case Contains.TU_CHOI:
					qdLcnt.get().setLdoTuchoi(stReq.getLyDo());
					break;
				default:
					break;
			}
			qlnvQdkqLcntRepository.save(qdLcnt.get());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}
