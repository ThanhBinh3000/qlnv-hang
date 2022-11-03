package com.tcdt.qlnvhang.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvDxkhMuaTtDtlRepository;
import com.tcdt.qlnvhang.repository.QlnvDxkhMuaTtHdrRepository;
import com.tcdt.qlnvhang.repository.QlnvQdMuattHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.QlnvQdMuattDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdMuattHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvDxkhMuaTtThopSearchReq;
import com.tcdt.qlnvhang.request.search.QlnvQdMuattSearchAdjustReq;
import com.tcdt.qlnvhang.request.search.QlnvQdMuattSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvDxkhMuaTtDtlSpecification;
import com.tcdt.qlnvhang.secification.QlnvQdMuattHdrSpecification;
import com.tcdt.qlnvhang.table.QlnvDxkhMuaTtDtl;
import com.tcdt.qlnvhang.table.QlnvQdMuattDtl;
import com.tcdt.qlnvhang.table.QlnvQdMuattDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdMuattHdr;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = PathContains.QL_NHA_CCAP + PathContains.QL_QD_MUATT)
@Api(tags = "Quản lý quyết định mua trực tiếp")
public class QlnvQdMuattController extends BaseController {
	@Autowired
	private QlnvQdMuattHdrRepository qdMuaHangHdrRepository;

	@Autowired
	private QlnvDxkhMuaTtHdrRepository qlnvDxkhMuaTtHdrRepository;

	@Autowired
	private QlnvDxkhMuaTtDtlRepository qlnvDxkhMuaTtDtlRepository;

	@ApiOperation(value = "Tổng hợp thông đề xuất để làm quyết định", response = List.class)
	@PostMapping(value = PathContains.URL_THOP_DATA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> sumarryData(HttpServletRequest request,
			@Valid @RequestBody QlnvDxkhMuaTtThopSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			String maDvi = objReq.getMaDvi();
			if (StringUtils.isEmpty(maDvi))
				maDvi = getDvql(request);

			List<QlnvDxkhMuaTtDtl> data = qlnvDxkhMuaTtDtlRepository
					.findAll(QlnvDxkhMuaTtDtlSpecification.buildTHopQuery(objReq));

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

	@ApiOperation(value = "Tạo mới quyết định mua trực tiếp", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest req, @Valid @RequestBody QlnvQdMuattHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			QlnvQdMuattHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdMuattHdr.class);

			dataMap.setNgayQd(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(req));
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setSoQdinh(getUUID(Contains.QUYET_DINH));
			dataMap.setLoaiDchinh(Contains.QD_GOC);

			if (objReq.getDetail() != null) {
				List<QlnvQdMuattDtlCtiet> detailChild;
				List<QlnvQdMuattDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdMuattDtlReq dtlReq : dtlReqList) {
					QlnvQdMuattDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdMuattDtl.class);
					detailChild = new ArrayList<QlnvQdMuattDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdMuattDtlCtiet.class);
					detail.setChildren(detailChild);
					dataMap.addChild(detail);
				}
			}

			QlnvQdMuattHdr createCheck = qdMuaHangHdrRepository.save(dataMap);
			if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
				List<String> soDxuatList = createCheck.getChildren().stream().map(QlnvQdMuattDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxkhMuaTtHdrRepository.updateTongHop(soDxuatList, Contains.TONG_HOP);
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

	@ApiOperation(value = "Cập nhật quyết định mua trực tiếp", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest req, @Valid @RequestBody QlnvQdMuattHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdMuattHdr> qOptional = qdMuaHangHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			if (qOptional.get().getChildren().size() > 0) {
				List<String> soDxuatList = qOptional.get().getChildren().stream().map(QlnvQdMuattDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxkhMuaTtHdrRepository.updateTongHop(soDxuatList, Contains.DUYET);
			}

			objReq.setSoQdinh(null);

			QlnvQdMuattHdr dataDB = qOptional.get();
			QlnvQdMuattHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdMuattHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(req));

			if (objReq.getDetail() != null) {
				List<QlnvQdMuattDtlCtiet> detailChild;
				List<QlnvQdMuattDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdMuattDtlReq dtlReq : dtlReqList) {
					QlnvQdMuattDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdMuattDtl.class);
					detailChild = new ArrayList<QlnvQdMuattDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdMuattDtlCtiet.class);
					detail.setChildren(detailChild);
					dataDB.addChild(detail);
				}
			}

			QlnvQdMuattHdr createCheck = qdMuaHangHdrRepository.save(dataDB);
			if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
				List<String> soDxuatList = createCheck.getChildren().stream().map(QlnvQdMuattDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxkhMuaTtHdrRepository.updateTongHop(soDxuatList, Contains.TONG_HOP);
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

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định mua trực tiếp", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID quyết định kế hoạch mua trực tiếp", example = "54", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdMuattHdr> qOptional = qdMuaHangHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 quyết định mua trực tiếp", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest req, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdMuattHdr> qHoach = qdMuaHangHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!qHoach.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			String status = stReq.getTrangThai() + qHoach.get().getTrangThai();
			switch (status) {
			case Contains.CHO_DUYET + Contains.MOI_TAO:
				qHoach.get().setNguoiGuiDuyet(getUserName(req));
				qHoach.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI + Contains.CHO_DUYET:
				qHoach.get().setNguoiPduyet(getUserName(req));
				qHoach.get().setNgayPduyet(getDateTimeNow());
				qHoach.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.DUYET + Contains.CHO_DUYET:
				qHoach.get().setNguoiPduyet(getUserName(req));
				qHoach.get().setNgayPduyet(getDateTimeNow());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
			}

			qHoach.get().setTrangThai(stReq.getTrangThai());
			qdMuaHangHdrRepository.save(qHoach.get());

			if (status.equals(Contains.TU_CHOI + Contains.CHO_DUYET) && qHoach.get().getChildren().size() > 0) {
				List<String> soDxuatList = qHoach.get().getChildren().stream().map(QlnvQdMuattDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxkhMuaTtHdrRepository.updateTongHop(soDxuatList, Contains.DUYET);
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

	@ApiOperation(value = "Tra cứu quyết định mua trực tiếp", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvQdMuattSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

			Page<QlnvQdMuattHdr> dataPage = qdMuaHangHdrRepository
					.findAll(QlnvQdMuattHdrSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "In phụ lục kế hoạch được duyệt", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void export(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response, HttpServletRequest req)
			throws Exception {
		String template = "/reports/PL_QD_MUA_TT.docx";
		try {
			if (StringUtils.isEmpty(searchReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdMuattHdr> qHoach = qdMuaHangHdrRepository.findById(searchReq.getId());
			if (!qHoach.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=PL_QD_MUA_TT_" + getDateTimeNow() + ".docx");

			// Add parameter to table
			List<QlnvQdMuattDtl> detail = new ArrayList<QlnvQdMuattDtl>();
			if (qHoach.get().getChildren() != null)
				detail = qHoach.get().getChildren();

			List<QlnvQdMuattDtlCtiet> detailCtiets = new ArrayList<QlnvQdMuattDtlCtiet>();
			if (detail.size() > 0)
				detailCtiets = detail.get(0).getChildren();

			List<Map<String, Object>> lstMapDetail = null;
			if (detailCtiets.size() > 0) {
				lstMapDetail = new ArrayList<Map<String, Object>>();
				Map<String, Object> detailMap;
				for (int i = 0; i < detailCtiets.size(); i++) {
					detailMap = Maps.<String, Object>buildMap().put("stt", i + 1)
							.put("donvi",
									StringUtils.isEmpty(detailCtiets.get(i).getMaDvi()) ? ""
											: detailCtiets.get(i).getMaDvi())
							.put("diadiem", "").put("soluong", detailCtiets.get(i).getSoDuyet())
							.put("dongia", detailCtiets.get(i).getDonGia())
							.put("thanhtien", detailCtiets.get(i).getTongTien()).get();
					lstMapDetail.add(detailMap);
				}
			}

			// Add gia tri bien string
			HashMap<String, String> mappings = new HashMap<String, String>();
			mappings.put("param1", "(1) …");
			mappings.put("param2", qHoach.get().getMaHanghoa());
			mappings.put("param3", getDvi(req).getTenDvi());
			mappings.put("param4", qHoach.get().getSoQdinh());

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

	@ApiOperation(value = "Tra cứu quyết định điều chỉnh phê duyệt kế hoạch mua trực tiếp", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU
			+ PathContains.URL_DIEU_CHINH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colectionAdjust(HttpServletRequest request,
			@Valid @RequestBody QlnvQdMuattSearchAdjustReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

			Page<QlnvQdMuattHdr> dataPage = qdMuaHangHdrRepository
					.findAll(QlnvQdMuattHdrSpecification.buildSearchAdjQuery(objReq), pageable);

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

	@ApiOperation(value = "Tạo mới quyết định điều chỉnh phê duyệt kế hoạch mua trực tiếp", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI
			+ PathContains.URL_DIEU_CHINH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> createAdjust(HttpServletRequest req,
			@Valid @RequestBody QlnvQdMuattHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			// Check quyet dinh goc co ton tai
			if (StringUtils.isEmpty(objReq.getSoQdinhGoc()))
				throw new Exception("Không tìm thấy số quyết định gốc");

			if (objReq.getLoaiDchinh() == null || !Contains.mappingLoaiDc.containsKey(objReq.getLoaiDchinh()))
				throw new Exception("Loại điều chỉnh không phù hợp");

			Optional<QlnvQdMuattHdr> opCheck = qdMuaHangHdrRepository.findBySoQdinhAndTrangThai(objReq.getSoQdinhGoc(),
					Contains.DUYET);
			if (!opCheck.isPresent())
				throw new Exception("Quyết định gốc không tồn tại hoặc đã bị điều chỉnh");

			QlnvQdMuattHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdMuattHdr.class);

			dataMap.setNgayQd(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(req));
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setSoQdinh(getUUID(Contains.QUYET_DINH_DC));

			if (objReq.getDetail() != null) {
				List<QlnvQdMuattDtlCtiet> detailChild;
				List<QlnvQdMuattDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdMuattDtlReq dtlReq : dtlReqList) {
					QlnvQdMuattDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdMuattDtl.class);
					detailChild = new ArrayList<QlnvQdMuattDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdMuattDtlCtiet.class);
					detail.setChildren(detailChild);
					dataMap.addChild(detail);
				}
			}

			QlnvQdMuattHdr createCheck = qdMuaHangHdrRepository.save(dataMap);

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

	@ApiOperation(value = "Cập nhật quyết định điều chỉnh phê duyệt kế hoạch mua trực tiếp", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT
			+ PathContains.URL_DIEU_CHINH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateAdjust(HttpServletRequest req,
			@Valid @RequestBody QlnvQdMuattHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			// Check quyet dinh goc co ton tai
			if (StringUtils.isEmpty(objReq.getSoQdinhGoc()))
				throw new Exception("Không tìm thấy số quyết định gốc");

			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			if (objReq.getLoaiDchinh() == null || !Contains.mappingLoaiDc.containsKey(objReq.getLoaiDchinh()))
				throw new Exception("Loại điều chỉnh không phù hợp");

			Optional<QlnvQdMuattHdr> opCheck = qdMuaHangHdrRepository.findBySoQdinhAndTrangThai(objReq.getSoQdinhGoc(),
					Contains.DUYET);
			if (!opCheck.isPresent())
				throw new Exception("Quyết định gốc không tồn tại hoặc đã bị điều chỉnh");

			Optional<QlnvQdMuattHdr> qOptional = qdMuaHangHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			objReq.setSoQdinh(null);

			QlnvQdMuattHdr dataDB = qOptional.get();
			QlnvQdMuattHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdMuattHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(req));

			if (objReq.getDetail() != null) {
				List<QlnvQdMuattDtlCtiet> detailChild;
				List<QlnvQdMuattDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdMuattDtlReq dtlReq : dtlReqList) {
					QlnvQdMuattDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdMuattDtl.class);
					detailChild = new ArrayList<QlnvQdMuattDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdMuattDtlCtiet.class);
					detail.setChildren(detailChild);
					dataDB.addChild(detail);
				}
			}

			QlnvQdMuattHdr createCheck = qdMuaHangHdrRepository.save(dataDB);

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

	@ApiOperation(value = "Xem chi tiết thông tin quyết định gốc mua trực tiếp", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET_GOC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detailFather(HttpServletRequest request,
			@Valid @RequestBody StrSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getStr()))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			QlnvQdMuattHdr objHdr = qdMuaHangHdrRepository.findBySoQdinh(objReq.getStr());
			if (ObjectUtils.isEmpty(objHdr))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			resp.setData(objHdr);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu quyết định mua trực tiếp dành cho cấp cục", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU
			+ PathContains.URL_CAP_CUC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colectionChild(HttpServletRequest request,
			@Valid @RequestBody QlnvQdMuattSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

			// Lay thong tin don vi quan ly
			QlnvDmDonvi objDvi = getDvi(request);
			if (ObjectUtils.isEmpty(objDvi) || StringUtils.isEmpty(objDvi.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");

			// Add them dk loc trong child
			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC))
				objReq.setMaDvi(objDvi.getMaDvi());

			Map<String, String> mappingLoaiDc = new HashMap<>();
			if (!StringUtils.isEmpty(objReq.getLoaiDchinh())
					&& Contains.getLoaiDc(objReq.getLoaiDchinh()).equals(Contains.QD_GOC))
				mappingLoaiDc.put(Contains.QD_GOC, "");
			else
				mappingLoaiDc = Contains.mappingLoaiDc;

			List<String> listLoaiDc = new ArrayList<String>(mappingLoaiDc.keySet());

			Page<QlnvQdMuattHdr> dataPage = qdMuaHangHdrRepository
					.findAll(QlnvQdMuattHdrSpecification.buildSearchChildQuery(objReq, listLoaiDc), pageable);

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

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định mua trực tiếp dành cho cấp cục", response = List.class)
	@PostMapping(value = PathContains.URL_CHI_TIET
			+ PathContains.URL_CAP_CUC, produces = MediaType.APPLICATION_JSON_VALUE)
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
			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC))
				objReq.setMaDvi(objDvi.getMaDvi());

			List<QlnvQdMuattHdr> lMuattHdrs = qdMuaHangHdrRepository
					.findAll(QlnvQdMuattHdrSpecification.buildFindByIdQuery(objReq));

			if (lMuattHdrs.isEmpty())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			resp.setData(lMuattHdrs.get(0));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "In phụ lục kế hoạch được duyệt dành cho cấp cục", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT
			+ PathContains.URL_CAP_CUC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportChild(@Valid @RequestBody IdSearchReq objReq, HttpServletResponse response,
			HttpServletRequest req) throws Exception {
		String template = "/reports/PL_QD_MUA_TT.docx";
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			// Lay thong tin don vi quan ly
			QlnvDmDonvi objDvi = getDvi(req);
			if (ObjectUtils.isEmpty(objDvi) || StringUtils.isEmpty(objDvi.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");

			// Add them dk loc trong child
			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC))
				objReq.setMaDvi(objDvi.getMaDvi());

			List<QlnvQdMuattHdr> lMuattHdrs = qdMuaHangHdrRepository
					.findAll(QlnvQdMuattHdrSpecification.buildFindByIdQuery(objReq));

			if (lMuattHdrs.isEmpty())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			QlnvQdMuattHdr objHdr = lMuattHdrs.get(0);

			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=PL_QD_MUA_TT_" + getDateTimeNow() + ".docx");

			// Add parameter to table
			List<QlnvQdMuattDtl> detail = new ArrayList<QlnvQdMuattDtl>();
			if (objHdr.getChildren() != null)
				detail = objHdr.getChildren();

			List<QlnvQdMuattDtlCtiet> detailCtiets = new ArrayList<QlnvQdMuattDtlCtiet>();
			if (detail.size() > 0)
				detailCtiets = detail.get(0).getChildren();

			List<Map<String, Object>> lstMapDetail = null;
			if (detailCtiets.size() > 0) {
				lstMapDetail = new ArrayList<Map<String, Object>>();
				Map<String, Object> detailMap;
				for (int i = 0; i < detailCtiets.size(); i++) {
					detailMap = Maps.<String, Object>buildMap().put("stt", i + 1)
							.put("donvi",
									StringUtils.isEmpty(detailCtiets.get(i).getMaDvi()) ? ""
											: detailCtiets.get(i).getMaDvi())
							.put("diadiem", "").put("soluong", detailCtiets.get(i).getSoDuyet())
							.put("dongia", detailCtiets.get(i).getDonGia())
							.put("thanhtien", detailCtiets.get(i).getTongTien()).get();
					lstMapDetail.add(detailMap);
				}
			}

			// Add gia tri bien string
			HashMap<String, String> mappings = new HashMap<String, String>();
			mappings.put("param1", "(1) …");
			mappings.put("param2", objHdr.getMaHanghoa());
			mappings.put("param3", objDvi.getTenDvi());
			mappings.put("param4", objHdr.getSoQdinh());

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
}