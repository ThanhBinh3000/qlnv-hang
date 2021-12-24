package com.tcdt.qlnvhang.controller.banhang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Filter;
import org.hibernate.Session;
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
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvDxkhBanHangRepository;
import com.tcdt.qlnvhang.repository.QlnvQdMuaHangHdr2Repository;
import com.tcdt.qlnvhang.repository.QlnvQdMuaHangHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.QlnvQdMuaHangDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvQdMuaHangHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdMuaHangSearchAdjustReq;
import com.tcdt.qlnvhang.request.search.QlnvQdMuaHangSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QDinhMuaHangSpecification;
import com.tcdt.qlnvhang.table.QlnvQdMuaHangDtl;
import com.tcdt.qlnvhang.table.QlnvQdMuaHangDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdMuaHangHdr;
import com.tcdt.qlnvhang.table.QlnvQdMuaHangHdr2;
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
@RequestMapping(value = PathContains.QL_LCDVBH + PathContains.QL_QD_BANHANG)
@Api(tags = "Quản lý quyết định mua hàng DTQG")
public class QlnvQdMuaHangController extends BaseController {
	@Autowired
	private QlnvQdMuaHangHdrRepository qdMuaHangHdrRepository;
	
	@Autowired
	private QlnvQdMuaHangHdr2Repository qdMuaHangHdr2Repository;

	@Autowired
	private QlnvDxkhBanHangRepository qlnvDxkhBanHangRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@ApiOperation(value = "Tạo mới quyết định mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest req, @Valid @RequestBody QlnvQdMuaHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			QlnvQdMuaHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdMuaHangHdr.class);

			dataMap.setNgayQdinh(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(req));
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setSoQdinh(getUUID(Contains.QUYET_DINH));
			dataMap.setLoaiDchinh(Contains.QD_GOC);

			if (objReq.getDetail() != null) {
				List<QlnvQdMuaHangDtlCtiet> detailChild;
				List<QlnvQdMuaHangDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdMuaHangDtlReq dtlReq : dtlReqList) {
					QlnvQdMuaHangDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdMuaHangDtl.class);
					detailChild = new ArrayList<QlnvQdMuaHangDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdMuaHangDtlCtiet.class);
					detail.setChildren(detailChild);
					dataMap.addChild(detail);
				}
			}

			QlnvQdMuaHangHdr createCheck = qdMuaHangHdrRepository.save(dataMap);
			if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
				List<String> soDxuatList = createCheck.getChildren().stream().map(QlnvQdMuaHangDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxkhBanHangRepository.updateTongHop(soDxuatList, Contains.TONG_HOP);
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

	@ApiOperation(value = "Cập nhật quyết định mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest req, @Valid @RequestBody QlnvQdMuaHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdMuaHangHdr> qOptional = qdMuaHangHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");
			
			if (qOptional.get().getChildren().size() > 0) {
				List<String> soDxuatList = qOptional.get().getChildren().stream().map(QlnvQdMuaHangDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxkhBanHangRepository.updateTongHop(soDxuatList, Contains.DUYET);
			}

			QlnvQdMuaHangHdr dataDB = qOptional.get();
			QlnvQdMuaHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdMuaHangHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(req));

			if (objReq.getDetail() != null) {
				List<QlnvQdMuaHangDtlCtiet> detailChild;
				List<QlnvQdMuaHangDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdMuaHangDtlReq dtlReq : dtlReqList) {
					QlnvQdMuaHangDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdMuaHangDtl.class);
					detailChild = new ArrayList<QlnvQdMuaHangDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdMuaHangDtlCtiet.class);
					detail.setChildren(detailChild);
					dataDB.addChild(detail);
				}
			}

			QlnvQdMuaHangHdr createCheck = qdMuaHangHdrRepository.save(dataDB);
			
			if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
				List<String> soDxuatList = createCheck.getChildren().stream().map(QlnvQdMuaHangDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxkhBanHangRepository.updateTongHop(soDxuatList, Contains.TONG_HOP);
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

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định mua hàng DTQG", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID quyết định kế hoạch mua hàng DTQG", example = "54", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdMuaHangHdr> qOptional = qdMuaHangHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 quyết định mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest req, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdMuaHangHdr> qHoach = qdMuaHangHdrRepository.findById(Long.valueOf(stReq.getId()));
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
			qdMuaHangHdrRepository.save(qHoach.get());
			
			if (status.equals(Contains.TU_CHOI) && qHoach.get().getChildren().size() > 0) {
				List<String> soDxuatList = qHoach.get().getChildren().stream().map(QlnvQdMuaHangDtl::getSoDxuat)
						.collect(Collectors.toList());
				qlnvDxkhBanHangRepository.updateTongHop(soDxuatList, Contains.DUYET);
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

	@ApiOperation(value = "Tra cứu quyết định mua hàng DTQG", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvQdMuaHangSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvQdMuaHangHdr> dataPage = qdMuaHangHdrRepository.findAll(QDinhMuaHangSpecification.buildSearchQuery(objReq), pageable);

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

			Optional<QlnvQdMuaHangHdr> qHoach = qdMuaHangHdrRepository.findById(searchReq.getId());
			if (!qHoach.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=PL_QD_MUA_TT_" + getDateTimeNow() + ".docx");

			// Add parameter to table
			List<QlnvQdMuaHangDtl> detail = new ArrayList<QlnvQdMuaHangDtl>();
			if (qHoach.get().getChildren() != null)
				detail = qHoach.get().getChildren();

			List<QlnvQdMuaHangDtlCtiet> detailCtiets = new ArrayList<QlnvQdMuaHangDtlCtiet>();
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
			@Valid @RequestBody QlnvQdMuaHangSearchAdjustReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

//			Page<QlnvQdMuaHangHdr> dataPage = qdMuaHangHdrRepository.selectParamsAdjust(objReq.getSoQdinh(),
//					objReq.getTrangThai(), objReq.getTuNgayQdinh(), objReq.getDenNgayQdinh(), objReq.getSoQdinhGoc(),
//					objReq.getSoQdKh(), objReq.getLoaiDchinh(), pageable);
			Page<QlnvQdMuaHangHdr> dataPage = qdMuaHangHdrRepository.findAll(QDinhMuaHangSpecification.buildSearchQueryAjust(objReq), pageable);

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

	@ApiOperation(value = "Tạo mới quyết định điều chỉnh phê duyệt kế hoạch mua trực tiếp", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI
			+ PathContains.URL_DIEU_CHINH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> createAdjust(HttpServletRequest req,
			@Valid @RequestBody QlnvQdMuaHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			// Check quyet dinh goc co ton tai
			if (StringUtils.isEmpty(objReq.getSoQdinhGoc()))
				throw new Exception("Không tìm thấy số quyết định gốc");

			if (objReq.getLoaiDchinh() == null || !Contains.mappingLoaiDc.containsKey(objReq.getLoaiDchinh()))
				throw new Exception("Loại điều chỉnh không phù hợp");

			Optional<QlnvQdMuaHangHdr> opCheck = qdMuaHangHdrRepository.findBySoQdinhAndTrangThai(objReq.getSoQdinhGoc(),
					Contains.DUYET);
			if (!opCheck.isPresent())
				throw new Exception("Quyết định gốc không tồn tại hoặc đã bị điều chỉnh");

			QlnvQdMuaHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdMuaHangHdr.class);

			dataMap.setNgayQdinh(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(req));
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setSoQdinh(getUUID(Contains.QUYET_DINH_DC));

			if (objReq.getDetail() != null) {
				List<QlnvQdMuaHangDtlCtiet> detailChild;
				List<QlnvQdMuaHangDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdMuaHangDtlReq dtlReq : dtlReqList) {
					QlnvQdMuaHangDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdMuaHangDtl.class);
					detailChild = new ArrayList<QlnvQdMuaHangDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdMuaHangDtlCtiet.class);
					detail.setChildren(detailChild);
					dataMap.addChild(detail);
				}
			}

			QlnvQdMuaHangHdr createCheck = qdMuaHangHdrRepository.save(dataMap);

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
			@Valid @RequestBody QlnvQdMuaHangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			// Check quyet dinh goc co ton tai
			if (StringUtils.isEmpty(objReq.getSoQdinhGoc()))
				throw new Exception("Không tìm thấy số quyết định gốc");

			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			if (objReq.getLoaiDchinh() == null || !Contains.mappingLoaiDc.containsKey(objReq.getLoaiDchinh()))
				throw new Exception("Loại điều chỉnh không phù hợp");

			Optional<QlnvQdMuaHangHdr> opCheck = qdMuaHangHdrRepository.findBySoQdinhAndTrangThai(objReq.getSoQdinhGoc(),
					Contains.DUYET);
			if (!opCheck.isPresent())
				throw new Exception("Quyết định gốc không tồn tại hoặc đã bị điều chỉnh");

			Optional<QlnvQdMuaHangHdr> qOptional = qdMuaHangHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			objReq.setSoQdinh(null);

			QlnvQdMuaHangHdr dataDB = qOptional.get();
			QlnvQdMuaHangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdMuaHangHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(req));

			if (objReq.getDetail() != null) {
				List<QlnvQdMuaHangDtlCtiet> detailChild;
				List<QlnvQdMuaHangDtlReq> dtlReqList = objReq.getDetail();
				for (QlnvQdMuaHangDtlReq dtlReq : dtlReqList) {
					QlnvQdMuaHangDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdMuaHangDtl.class);
					detailChild = new ArrayList<QlnvQdMuaHangDtlCtiet>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), QlnvQdMuaHangDtlCtiet.class);
					detail.setChildren(detailChild);
					dataDB.addChild(detail);
				}
			}

			QlnvQdMuaHangHdr createCheck = qdMuaHangHdrRepository.save(dataDB);

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
			QlnvQdMuaHangHdr objHdr = qdMuaHangHdrRepository.findBySoQdinh(objReq.getStr());
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
			@Valid @RequestBody QlnvQdMuaHangSearchReq objReq) {
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
			Session session = entityManager.unwrap(Session.class);
			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
				Filter filter = session.enableFilter("pFilter");
				filter.setParameter("maDvi", objDvi.getMaDvi());
			}

			if (objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC) && !StringUtils.isEmpty(objReq.getMaDvi())) {
				Filter filter = session.enableFilter("pFilter");
				filter.setParameter("maDvi", objReq.getMaDvi());
			}

			Map<String, String> mappingLoaiDc = new HashMap<>();
			if (!StringUtils.isEmpty(objReq.getLoaiDchinh())
					&& Contains.getLoaiDc(objReq.getLoaiDchinh()).equals(Contains.QD_GOC))
				mappingLoaiDc.put(Contains.QD_GOC, "");
			else
				mappingLoaiDc = Contains.mappingLoaiDc;

			List<String> listLoaiDc = new ArrayList<String>(mappingLoaiDc.keySet());

			Page<QlnvQdMuaHangHdr2> dataPage = qdMuaHangHdr2Repository.selectParams(objReq.getSoQdinh(),
					objReq.getTrangThai(), objReq.getTuNgayQdinh(), objReq.getDenNgayQdinh(), objReq.getMaHhoa(),
					objReq.getSoQdKh(), listLoaiDc, pageable);

			session.disableFilter("pFilter");

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
			Session session = entityManager.unwrap(Session.class);
			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
				Filter filter = session.enableFilter("pFilter");
				filter.setParameter("maDvi", objDvi.getMaDvi());
			}

			if (objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC) && !StringUtils.isEmpty(objReq.getMaDvi())) {
				Filter filter = session.enableFilter("pFilter");
				filter.setParameter("maDvi", objReq.getMaDvi());
			}

			Optional<QlnvQdMuaHangHdr2> qOptional = qdMuaHangHdr2Repository.findById(objReq.getId());

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

}
