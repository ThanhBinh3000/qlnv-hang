package com.tcdt.qlnvhang.controller.tlth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.tcdt.qlnvhang.repository.QlnvQdTlthHdrRepository;
import com.tcdt.qlnvhang.request.object.QlnvQdTlthHdrReq;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvQdTlthDtlReq;
import com.tcdt.qlnvhang.request.search.QlnvQdTlthSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvQdTlthSpecification;
import com.tcdt.qlnvhang.table.QlnvQdTlthDtl;
import com.tcdt.qlnvhang.table.QlnvQdTlthDtlCtiet;
import com.tcdt.qlnvhang.table.QlnvQdTlthHdr;
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
@RequestMapping(value = PathContains.QL_QD_TLTH)
@Api(tags = "Quản lý quyết định thanh lý, tiêu hủy")
public class QlnvQdTlthController extends BaseController {

	@Autowired
	private QlnvQdTlthHdrRepository qlnvQdTlthHdrRepository;

	@ApiOperation(value = "Tạo mới quyết định thanh lý, tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest request,@Valid @RequestBody QlnvQdTlthHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			
			QlnvQdTlthHdr dataMap = new ModelMapper().map(objReq, QlnvQdTlthHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setSoQdinh(getUUID(Contains.QUYET_DINH));
			// add detail
			List<QlnvQdTlthDtlReq> dtlReqList = objReq.getDetailListReq();
			if (dtlReqList != null) {
				List<QlnvQdTlthDtlCtiet> detailChild;
				for (QlnvQdTlthDtlReq dtlReq : dtlReqList) {
					QlnvQdTlthDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdTlthDtl.class);
					detailChild = new ArrayList<QlnvQdTlthDtlCtiet>();
					if (dtlReq.getDetailListReq() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetailListReq(), QlnvQdTlthDtlCtiet.class);
					detail.setChildren(detailChild);
					dataMap.addChild(detail);
				}
			}

			qlnvQdTlthHdrRepository.save(dataMap);

			resp.setData(dataMap);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xóa quyết định thanh lý, tiêu hủy", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdTlthHdr> qOptional = qlnvQdTlthHdrRepository.findById(idSearchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			qlnvQdTlthHdrRepository.delete(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu quyết định thanh lý, tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@RequestBody QlnvQdTlthSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvQdTlthHdr> qlnvQdTlthHdr = qlnvQdTlthHdrRepository.findAll(QlnvQdTlthSpecification.buildSearchQuery(objReq), pageable);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(qlnvQdTlthHdr);
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Sửa quyết định thanh lý, tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
											   @Valid @RequestBody QlnvQdTlthHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvQdTlthHdr> qlnvQdTlthHdr = qlnvQdTlthHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qlnvQdTlthHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			QlnvQdTlthHdr dataDB = qlnvQdTlthHdr.get();
			QlnvQdTlthHdr dataMap = ObjectMapperUtils.map(objReq, QlnvQdTlthHdr.class);
			updateObjectToObject(dataDB, dataMap);
			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));
			
			List<QlnvQdTlthDtlReq> dtlReqList = objReq.getDetailListReq();
			if (dtlReqList != null) {
				List<QlnvQdTlthDtlCtiet> detailChild;
				for (QlnvQdTlthDtlReq dtlReq : dtlReqList) {
					QlnvQdTlthDtl detail = ObjectMapperUtils.map(dtlReq, QlnvQdTlthDtl.class);
					detailChild = new ArrayList<QlnvQdTlthDtlCtiet>();
					if (dtlReq.getDetailListReq() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetailListReq(), QlnvQdTlthDtlCtiet.class);
					detail.setChildren(detailChild);
					dataDB.addChild(detail);
				}
			}

			
			qlnvQdTlthHdrRepository.save(dataDB);

			resp.setData(dataDB);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết thông tin quyết định thanh lý, tiêu hủy", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID đề xuất thanh lý, tiêu hủy", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvQdTlthHdr> qOptional = qlnvQdTlthHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 quyết định thanh lý, tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest req, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdTlthHdr> optional = qlnvQdTlthHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			QlnvQdTlthHdr qlnvQdTlthHdr = optional.get();
			String status = stReq.getTrangThai();
			
			switch (status) {
			case Contains.CHO_DUYET:
				qlnvQdTlthHdr.setNguoiGuiDuyet(getUserName(req));
				qlnvQdTlthHdr.setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.DUYET:
				qlnvQdTlthHdr.setNguoiPduyet(getUserName(req));
				qlnvQdTlthHdr.setNgayPduyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI:
				qlnvQdTlthHdr.setLdoTuchoi(stReq.getLyDo());
				break;
			default:
				break;
			}
			

			qlnvQdTlthHdr.setTrangThai(stReq.getTrangThai());
			qlnvQdTlthHdrRepository.save(qlnvQdTlthHdr);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	
	@ApiOperation(value = "In quyết định thanh lý tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void export(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response, HttpServletRequest req)
			throws Exception {
		String template = "/reports/QD_TLTH.docx";
		try {
			if (StringUtils.isEmpty(searchReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvQdTlthHdr> optional = qlnvQdTlthHdrRepository.findById(searchReq.getId());
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			QlnvQdTlthHdr quyetDinh = optional.get();
			
			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=QD_TLTH_" + getDateTimeNow() + ".docx");

			// Add parameter to table
			List<QlnvQdTlthDtl> detail = Optional.ofNullable(quyetDinh.getChildren()).orElse(new ArrayList<>());

			List<QlnvQdTlthDtlCtiet> detailCtiets = new ArrayList<QlnvQdTlthDtlCtiet>();
			if (detail.size() > 0)
				detailCtiets = detail.get(0).getChildren();

			List<Map<String, Object>> lstMapDetail = null;
			if (detailCtiets.size() > 0) {
				lstMapDetail = new ArrayList<Map<String, Object>>();
				Map<String, Object> detailMap;
				for (int i = 0; i < detailCtiets.size(); i++) {
					QlnvQdTlthDtlCtiet cTiet = detailCtiets.get(i);
					detailMap = Maps.<String, Object>buildMap().put("stt", i + 1)
							.put("soluongdxuat", cTiet.getSoDxuat())
							.put("soluongduyet", cTiet.getSoDuyet())
							.put("donvitinh", cTiet.getDviTinh())
							.get();
					lstMapDetail.add(detailMap);
				}
			}

			HashMap<String, String> mappings = new HashMap<String, String>();
			mappings.put("param1", Contains.mappingLoaiDx.get(quyetDinh.getLhinhXuat()));
			mappings.put("param2", quyetDinh.getMaHhoa());
			mappings.put("param3", getDvi(req).getTenDvi());


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
	
	@ApiOperation(value = "Tra cứu quyết định thanh lý tiêu hủy dành cho cấp cục", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU
			+ PathContains.URL_CAP_CUC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colectionChild(HttpServletRequest request,
			@Valid @RequestBody QlnvQdTlthSearchReq objReq) {
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

			Page<QlnvQdTlthHdr> qlnvQdTlthHdr = qlnvQdTlthHdrRepository.findAll(QlnvQdTlthSpecification.buildSearchChildQuery(objReq), pageable);

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
	
	@ApiOperation(value = "Lấy chi tiết thông tin quyết định thanh lý tiêu hủy dành cho cấp cục", response = List.class)
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

			List<QlnvQdTlthHdr> qOptional = qlnvQdTlthHdrRepository.findAll(QlnvQdTlthSpecification.buildFindByIdQuery(objReq));

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
	
	@ApiOperation(value = "In quyết định thanh lý tiêu hủy dành cho cấp cục", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT + PathContains.URL_CAP_CUC, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void exportChild(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response, HttpServletRequest req)
			throws Exception {
		String template = "/reports/QD_TLTH.docx";
		try {
			if (StringUtils.isEmpty(searchReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			// Lay thong tin don vi quan ly
			QlnvDmDonvi objDvi = getDvi(req);
			if (ObjectUtils.isEmpty(objDvi) || StringUtils.isEmpty(objDvi.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");

			// Add them dk loc trong child
			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC))
				searchReq.setMaDvi(objDvi.getMaDvi());
						
			List<QlnvQdTlthHdr> qdTlths = qlnvQdTlthHdrRepository.findAll(QlnvQdTlthSpecification.buildFindByIdQuery(searchReq));
			if (qdTlths.isEmpty())
				throw new Exception("Không tìm thấy dữ liệu");

			QlnvQdTlthHdr quyetDinh = qdTlths.get(0);
			
			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=QD_TLTH_" + getDateTimeNow() + ".docx");

			// Add parameter to table
			List<QlnvQdTlthDtl> detail = Optional.ofNullable(quyetDinh.getChildren()).orElse(new ArrayList<>());

			List<QlnvQdTlthDtlCtiet> detailCtiets = new ArrayList<QlnvQdTlthDtlCtiet>();
			if (detail.size() > 0)
				detailCtiets = detail.get(0).getChildren();

			List<Map<String, Object>> lstMapDetail = null;
			if (detailCtiets.size() > 0) {
				lstMapDetail = new ArrayList<Map<String, Object>>();
				Map<String, Object> detailMap;
				for (int i = 0; i < detailCtiets.size(); i++) {
					QlnvQdTlthDtlCtiet cTiet = detailCtiets.get(i);
					detailMap = Maps.<String, Object>buildMap().put("stt", i + 1)
							.put("soluongdxuat", cTiet.getSoDxuat())
							.put("soluongduyet", cTiet.getSoDuyet())
							.put("donvitinh", cTiet.getDviTinh())
							.get();
					lstMapDetail.add(detailMap);
				}
			}

			HashMap<String, String> mappings = new HashMap<String, String>();
			mappings.put("param1", Contains.mappingLoaiDx.get(quyetDinh.getLhinhXuat()));
			mappings.put("param2", quyetDinh.getMaHhoa());
			mappings.put("param3", getDvi(req).getTenDvi());


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
