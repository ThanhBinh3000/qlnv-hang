package com.tcdt.qlnvhang.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcdt.qlnvhang.entities.QlnvDmDonviEntity;
import com.tcdt.qlnvhang.repository.QlnvDmDonviEntityRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.QlnvKhoachLcntHdrRepository;
import com.tcdt.qlnvhang.repository.QlnvQdLcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.QlnvDmDonviSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntDtlCtietReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdLcntHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntAggReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.table.QlnvKhoachLcntDtl;
import com.tcdt.qlnvhang.table.QlnvKhoachLcntHdr;
import com.tcdt.qlnvhang.table.QlnvQdLcntDtl;
import com.tcdt.qlnvhang.table.QlnvQdLcntDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdLcntHdr;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.PaginationSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/qd-lcnt")
@Api(tags = "Quyết định phê duyệt KHLCNT")
public class QlnvQdLcntHdrController extends BaseController {
	@Autowired
	private QlnvQdLcntHdrRepository QlnvQdLcntHdrRepository;
	
	@Autowired
	private QlnvKhoachLcntHdrRepository qlnvKhoachLcntHdrRepository;
	
	@Autowired
	private QlnvDmDonviEntityRepository qDmDonviEntityRepository;
	
	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;
	
	
	@ApiOperation(value = "Tạo mới Quyết định phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		Calendar cal = Calendar.getInstance();
		try {
			List<QlnvQdLcntDtlReq> dtlReqList = objReq.getDetail();
			objReq.setDetail(null);
			QlnvQdLcntHdr dataMap = new ModelMapper().map(objReq, QlnvQdLcntHdr.class);
			dataMap.setNgayTao(cal.getTime());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setLoaiQd(Contains.GOC);
				for (QlnvQdLcntDtlReq dtlReq : dtlReqList) {
					QlnvQdLcntDtl detail = new ModelMapper().map(dtlReq, QlnvQdLcntDtl.class);
					List<QlnvQdLcntDtlCtietReq> dtlCtietReqList = dtlReq.getDetail();
					dtlReq.setDetail(null);
						for (QlnvQdLcntDtlCtietReq dtlCtReq : dtlCtietReqList) {
							QlnvQdLcntDtlCtiet detailCtiet = new ModelMapper().map(dtlCtReq, QlnvQdLcntDtlCtiet.class);
							detail.addDetail(detailCtiet);
						}
					dataMap.addDetail(detail);
				}
				QlnvQdLcntHdrRepository.save(dataMap);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Tổng hợp đề xuất KHLCNT", response = List.class)
	@PostMapping(value = "/agg", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insertThop(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntAggReq objReq) {
		BaseResponse resp = new BaseResponse();
		Calendar cal = Calendar.getInstance();
		try {
			List<QlnvKhoachLcntHdr> khoachList = qlnvKhoachLcntHdrRepository.getTHop(objReq.getLoaiHanghoa(), objReq.getNguonvon(), objReq.getHanghoa(), objReq.getSoQdGiaoCtkh(), Contains.DUYET);
			QlnvQdLcntHdrReq hdrReq = new QlnvQdLcntHdrReq();
			List<QlnvQdLcntDtlReq> dtlReqList = new ArrayList<QlnvQdLcntDtlReq>();
			for (QlnvKhoachLcntHdr qlnvKhoachLcntHdr : khoachList) {
				QlnvQdLcntDtlReq dtlReq = new QlnvQdLcntDtlReq();
				List<QlnvQdLcntDtlCtietReq> detail = new ArrayList<QlnvQdLcntDtlCtietReq>();
				for (QlnvKhoachLcntDtl dtl : qlnvKhoachLcntHdr.getDetailList()) {
					QlnvQdLcntDtlCtietReq ctietReq = new QlnvQdLcntDtlCtietReq();
					ctietReq.setDiaChi(dtl.getDiaChi());
					ctietReq.setDonGia(dtl.getGiaDkien());
					ctietReq.setDviTinh(dtl.getDviTinh());
					ctietReq.setMaDvi(dtl.getMaDvi());
					ctietReq.setSoDxuat(dtl.getSoLuong());
					ctietReq.setSoDuyet(dtl.getSoLuong());
					ctietReq.setThuesuat(new BigDecimal(0));
					ctietReq.setThanhtien(dtl.getSoLuong().multiply(dtl.getSoLuong()));
					ctietReq.setTongtien(dtl.getSoLuong().multiply(dtl.getSoLuong()));
					detail.add(ctietReq);
				}
				dtlReq.setDetail(detail);
				dtlReq.setMaDvi(qlnvKhoachLcntHdr.getMaDvi());
				dtlReq.setSoDxuat(qlnvKhoachLcntHdr.getSoDx());
				dtlReqList.add(dtlReq);
				
			}
			hdrReq.setDetail(dtlReqList);
			hdrReq.setLoaiHanghoa(objReq.getLoaiHanghoa());
			hdrReq.setNguonvon(objReq.getNguonvon());
			hdrReq.setLoaiQd(Contains.MOI_TAO);
			hdrReq.setSoQdGiaoCtkh(objReq.getSoQdGiaoCtkh());
			hdrReq.setMaHanghoa(objReq.getHanghoa());
			
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setData(hdrReq);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá thông tin Quyết định phê duyệt KHLCNT", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/delete/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@ApiParam(value = "ID kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdLcntHdr> QlnvQdLcntHdr = QlnvQdLcntHdrRepository.findById(Long.parseLong(ids));
			if (!QlnvQdLcntHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");
			QlnvQdLcntHdrRepository.delete(QlnvQdLcntHdr.get());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu Quyết định phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = "/findList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvQdLcntHdrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvQdLcntHdr> qhKho = QlnvQdLcntHdrRepository.selectParams(objReq.getSoQdinh(),objReq.getSoQdGiaoCtkh(),objReq.getTuNgayQd(),objReq.getDenNgayQd(),objReq.getMaHanghoa(),objReq.getLoaiHanghoa(),null,null, pageable);

			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setData(qhKho);
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật Quyết định phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
			Calendar cal = Calendar.getInstance();
			Optional<QlnvQdLcntHdr> QlnvQdLcntHdr = QlnvQdLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!QlnvQdLcntHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			QlnvQdLcntHdr dataDTB = QlnvQdLcntHdr.get();
			List<QlnvQdLcntDtlReq> dtlReqList = objReq.getDetail();
			objReq.setDetail(null);
			QlnvQdLcntHdr dataMap = new ModelMapper().map(objReq, QlnvQdLcntHdr.class);
			updateObjectToObject(dataDTB, dataMap);
			dataDTB.setNgaySua(cal.getTime());
			dataDTB.setNguoiSua(getUserName(request));
			List<QlnvQdLcntDtl> dtlList = dataDTB.getDetailList();
			for (QlnvQdLcntDtl dtl : dtlList) { 
				List<QlnvQdLcntDtlCtiet> dtlCtietList = dtl.getDetailList();
				for (QlnvQdLcntDtlCtiet dtlCtiet : dtlCtietList) {
					dtl.removeDetail(dtlCtiet);
				}
				
				dataDTB.removeDetail(dtl);
			}
			for (QlnvQdLcntDtlReq dtlReq : dtlReqList) {
				QlnvQdLcntDtl detail = new ModelMapper().map(dtlReq, QlnvQdLcntDtl.class);
				List<QlnvQdLcntDtlCtietReq> dtlCtietReqList = dtlReq.getDetail();
				dtlReq.setDetail(null);
					for (QlnvQdLcntDtlCtietReq dtlCtReq : dtlCtietReqList) {
						QlnvQdLcntDtlCtiet detailCtiet = new ModelMapper().map(dtlCtReq, QlnvQdLcntDtlCtiet.class);
						detail.addDetail(detailCtiet);
					}
				dataDTB.addDetail(detail);
				//detailList.add(detail);
			}
			QlnvQdLcntHdrRepository.save(dataDTB);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Lấy chi tiết thông tin Quyết định phê duyệt KHLCNT", response = List.class)
	@GetMapping(value = "/chi-tiet/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Quyết định phê duyệt KHLCNT", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdLcntHdr> qOptional = QlnvQdLcntHdrRepository.findById(Long.parseLong(ids));
			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			resp.setData(qOptional);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 Quyết định phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(HttpServletRequest request,@Valid  @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdLcntHdr> qHoach = QlnvQdLcntHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!qHoach.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");
			qHoach.get().setTrangThai(stReq.getTrangThai());
			String status = stReq.getTrangThai();
			Calendar cal = Calendar.getInstance();
			switch(status) {
				case Contains.CHO_DUYET:
					qHoach.get().setNguoiGuiDuyet(getUserName(request));
					qHoach.get().setNgayGuiDuyet(cal.getTime());
					break;
				case Contains.DUYET:
					qHoach.get().setNguoiPduyet(getUserName(request));
					qHoach.get().setNgayPduyet(cal.getTime());
					break;
				case Contains.TU_CHOI:
					qHoach.get().setLdoTuchoi(stReq.getLyDo());
					break;
				default:
					break;
			}
			QlnvQdLcntHdrRepository.save(qHoach.get());
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Lấy danh sách đơn vị", response = List.class)
	@PostMapping(value = "/ds-donvi", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(@Valid @RequestBody QlnvDmDonviSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
			Page<QlnvDmDonviEntity> data = qDmDonviEntityRepository.selectParams(objReq.getMaDvi(), objReq.getTenDvi(),
					objReq.getTrangThai(), objReq.getMaTinh(), objReq.getMaQuan(), objReq.getMaPhuong(),
					objReq.getCapDvi(), objReq.getKieuDvi(), objReq.getLoaiDvi(), pageable);
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Kiểm tra đơn vị theo mã", response = List.class)
	@PostMapping(value = "/find-ma-dv", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detailbycode(@Valid @RequestBody StrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getStr()))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			QlnvDmDonvi qOptional = qlnvDmDonviRepository.findByMaDvi(objReq.getStr());
			if (qOptional == null)
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			resp.setData(qOptional);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "Lấy danh sách đơn vị cấp Chi cục", response = List.class)
	@PostMapping(value = "/ds-donvi-ccuc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colectionCcuc(@Valid @RequestBody StrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			List<QlnvDmDonvi> data = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(objReq.getStr(), Contains.HOAT_DONG);
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}