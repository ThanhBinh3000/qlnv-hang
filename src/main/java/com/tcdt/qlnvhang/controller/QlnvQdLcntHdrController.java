package com.tcdt.qlnvhang.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.entities.QlnvDmDonviEntity;
import com.tcdt.qlnvhang.enums.EnumResponse;
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
import com.tcdt.qlnvhang.secification.QlnvQdLcntSpecification;
import com.tcdt.qlnvhang.table.QlnvKhoachLcntDtl;
import com.tcdt.qlnvhang.table.QlnvKhoachLcntHdr;
import com.tcdt.qlnvhang.table.QlnvQdLcntDtl;
import com.tcdt.qlnvhang.table.QlnvQdLcntDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdLcntHdr;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.Doc4jUtils;
import com.tcdt.qlnvhang.util.Maps;
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
@RequestMapping(value = PathContains.QL_QD_LCNT)
@Api(tags = "Quyết định phê duyệt KHLCNT")
public class QlnvQdLcntHdrController extends BaseController {
	@Autowired
	private QlnvQdLcntHdrRepository qlnvQdLcntHdrRepository;
	
	@Autowired
	private QlnvKhoachLcntHdrRepository qlnvKhoachLcntHdrRepository;
	
	@Autowired
	private QlnvDmDonviEntityRepository qDmDonviEntityRepository;
	
	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;
	
	
	@ApiOperation(value = "Tạo mới Quyết định phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			
			QlnvQdLcntHdr dataMap = new ModelMapper().map(objReq, QlnvQdLcntHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setLoaiQd(Contains.QUYET_DINH);
				
			List<QlnvQdLcntDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdLcntDtl> details = new ArrayList<>();
			if (dtlReqList != null) {
				List<QlnvQdLcntDtlCtiet> detailChild;
				for (QlnvQdLcntDtlReq dtlReq : dtlReqList) {
					List<QlnvQdLcntDtlCtietReq> cTietReq = dtlReq.getDetail();
					QlnvQdLcntDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdLcntDtl.class);
					detailChild = new ArrayList<QlnvQdLcntDtlCtiet>();
					if (cTietReq != null)
						detailChild = ObjectMapperUtils.mapAll(cTietReq, QlnvQdLcntDtlCtiet.class);
					detail.setDetailList(detailChild);
					details.add(detail);
				}
				dataMap.setDetailList(details);
			}
			qlnvQdLcntHdrRepository.save(dataMap);
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
	
	@ApiOperation(value = "Tổng hợp đề xuất KHLCNT", response = List.class)
	@PostMapping(value = "/agg", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insertThop(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntAggReq objReq) {
		BaseResponse resp = new BaseResponse();
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
	@PostMapping(value = PathContains.URL_XOA + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@ApiParam(value = "ID kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdLcntHdr> QlnvQdLcntHdr = qlnvQdLcntHdrRepository.findById(Long.parseLong(ids));
			if (!QlnvQdLcntHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");
			qlnvQdLcntHdrRepository.delete(QlnvQdLcntHdr.get());
			
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

	@ApiOperation(value = "Tra cứu Quyết định phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@Valid @RequestBody QlnvQdLcntHdrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
			objReq.setLoaiQd(Contains.QUYET_DINH);
			Page<QlnvQdLcntHdr> qdLcnt = qlnvQdLcntHdrRepository.findAll(QlnvQdLcntSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "Cập nhật Quyết định phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,@Valid @RequestBody QlnvQdLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
			Optional<QlnvQdLcntHdr> QlnvQdLcntHdr = qlnvQdLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
			
			if (!QlnvQdLcntHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			
			QlnvQdLcntHdr dataDTB = QlnvQdLcntHdr.get();
			QlnvQdLcntHdr dataMap = new ModelMapper().map(objReq, QlnvQdLcntHdr.class);
			
			updateObjectToObject(dataDTB, dataMap);
			dataDTB.setNgaySua(getDateTimeNow());
			dataDTB.setNguoiSua(getUserName(request));
			dataDTB.setLoaiQd(Contains.QUYET_DINH);
			List<QlnvQdLcntDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvQdLcntDtl> details = new ArrayList<>();
			if (dtlReqList != null) {
				List<QlnvQdLcntDtlCtiet> detailChild;
				for (QlnvQdLcntDtlReq dtlReq : dtlReqList) {
					List<QlnvQdLcntDtlCtietReq> cTietReq = dtlReq.getDetail();
					QlnvQdLcntDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdLcntDtl.class);
					detailChild = new ArrayList<QlnvQdLcntDtlCtiet>();
					if (cTietReq != null)
						detailChild = ObjectMapperUtils.mapAll(cTietReq, QlnvQdLcntDtlCtiet.class);
					detail.setDetailList(detailChild);
					details.add(detail);
				}
				dataMap.setDetailList(details);
			}
			
			qlnvQdLcntHdrRepository.save(dataDTB);
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
	
	@ApiOperation(value = "Lấy chi tiết thông tin Quyết định phê duyệt KHLCNT", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Quyết định phê duyệt KHLCNT", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdLcntHdr> qOptional = qlnvQdLcntHdrRepository.findById(Long.parseLong(ids));
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
	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 Quyết định phê duyệt KHLCNT", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(HttpServletRequest request,@Valid  @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdLcntHdr> qdLcnt = qlnvQdLcntHdrRepository.findById(Long.valueOf(stReq.getId()));
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
			qlnvQdLcntHdrRepository.save(qdLcnt.get());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "In phụ lục kế hoạch duyệt", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void export(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response, HttpServletRequest req)
			throws Exception {
		String template = "/reports/PL_QD_LCNT.docx";
		try {
			if (StringUtils.isEmpty(searchReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdLcntHdr> optional = qlnvQdLcntHdrRepository.findById(searchReq.getId());
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			QlnvQdLcntHdr qd = optional.get();
			
			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=PL_QD_LCNT_" + getDateTimeNow() + ".docx");

			// Add parameter to table
			List<QlnvQdLcntDtl> detail = new ArrayList<QlnvQdLcntDtl>();
			if (qd.getDetailList() != null)
				detail = qd.getDetailList();

			List<QlnvQdLcntDtlCtiet> detailCtiets = new ArrayList<QlnvQdLcntDtlCtiet>();
			if (detail.size() > 0)
				detailCtiets = detail.get(0).getDetailList();

			List<Map<String, Object>> lstMapDetail = null;
			if (detailCtiets.size() > 0) {
				lstMapDetail = new ArrayList<Map<String, Object>>();
				Map<String, Object> detailMap;
				for (int i = 0; i < detailCtiets.size(); i++) {
					QlnvQdLcntDtlCtiet cTiet = detailCtiets.get(i);
					detailMap = Maps.<String, Object>buildMap().put("stt", i + 1)
							.put("donvi", StringUtils.isEmpty(cTiet.getMaDvi()) ? "" : cTiet.getMaDvi())
							.put("diadiem", cTiet.getDiaChi())
							.put("soluong", cTiet.getSoDuyet())
							.put("dongia", cTiet.getDonGia())
							.put("thanhtien", cTiet.getThanhtien()).get();
					lstMapDetail.add(detailMap);
				}
			}

			// Add gia tri bien string
			HashMap<String, String> mappings = new HashMap<String, String>();
			mappings.put("param1", "(1) …");
			mappings.put("param2", qd.getMaHanghoa());
			mappings.put("param3", getDvi(req).getTenDvi());
			mappings.put("param4", qd.getSoQdinh());

			// save the docs
			Doc4jUtils.generateDoc(template, mappings, lstMapDetail, dataOutput);
			dataOutput.flush();
			dataOutput.close();
		} catch (Exception e) {
			log.error("In phụ lục kế hoạch được duyệt", e);
			final Map<String, Object> body = new HashMap<>();
			body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			body.put("msg", e.getMessage());

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");

			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), body);
		}
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