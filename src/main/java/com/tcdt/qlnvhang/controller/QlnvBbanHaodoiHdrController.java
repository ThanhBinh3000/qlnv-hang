package com.tcdt.qlnvhang.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvBbanHaodoiHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvBbanHaodoiDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvBbanHaodoiHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvBbanHaodoiSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvBbanHaodoiSpecification;
import com.tcdt.qlnvhang.table.QlnvBbanHaodoiDtl;
import com.tcdt.qlnvhang.table.QlnvBbanHaodoiHdr;
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
@RequestMapping(value = PathContains.QL_XUAT_HANG + PathContains.BIEN_BAN_HAO_DOI)
@Api(tags = "Quản lý biên bản xác định hao dôi")
public class QlnvBbanHaodoiHdrController extends BaseController {
	@Autowired
	private QlnvBbanHaodoiHdrRepository qlnvBbanHaodoiHdrRepository;

	@ApiOperation(value = "Tạo mới biên bản xác định hao dôi", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest request,
			@Valid @RequestBody QlnvBbanHaodoiHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			QlnvBbanHaodoiHdr dataMap = ObjectMapperUtils.map(objReq, QlnvBbanHaodoiHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setMaDvi(getDvi(request).getMaDvi());

			// add detail
			List<QlnvBbanHaodoiDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvBbanHaodoiDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvBbanHaodoiDtl.class);
			dataMap.setDetailList(dtls);

			qlnvBbanHaodoiHdrRepository.save(dataMap);

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

	@ApiOperation(value = "Xoá biên bản xác định hao dôi", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<QlnvBbanHaodoiHdr> qOptional = qlnvBbanHaodoiHdrRepository.findById(idSearchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			qlnvBbanHaodoiHdrRepository.delete(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu biên bản xác định hao dôi", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvBbanHaodoiSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvBbanHaodoiHdr> dataPage = qlnvBbanHaodoiHdrRepository
					.findAll(QlnvBbanHaodoiSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "Cập nhật biên bản xác định hao dôi", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody QlnvBbanHaodoiHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvBbanHaodoiHdr> optional = qlnvBbanHaodoiHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			QlnvBbanHaodoiHdr dataDB = optional.get();
			QlnvBbanHaodoiHdr dataMap = ObjectMapperUtils.map(objReq, QlnvBbanHaodoiHdr.class);

			updateObjectToObject(dataDB, dataMap);
			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));

			List<QlnvBbanHaodoiDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvBbanHaodoiDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvBbanHaodoiDtl.class);
			dataDB.setDetailList(dtls);
			qlnvBbanHaodoiHdrRepository.save(dataDB);

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

	@ApiOperation(value = "Lấy chi tiết thông tin biên bản xác định hao dôi", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID Đề xuất kế hoạch mua trực tiếp", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvBbanHaodoiHdr> qOptional = qlnvBbanHaodoiHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 biên bản xác định hao dôi", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest req, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvBbanHaodoiHdr> optional = qlnvBbanHaodoiHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			String status = stReq.getTrangThai() + optional.get().getTrangThai();
			switch (status) {
			case Contains.CHO_DUYET:
				optional.get().setNguoiGuiDuyet(getUserName(req));
				optional.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.DUYET:
				optional.get().setNguoiPduyet(getUserName(req));
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.TU_CHOI:
				optional.get().setNguoiPduyet(getUserName(req));
				optional.get().setNgayPduyet(getDateTimeNow());
				break;
			default:
				break;
			}

			optional.get().setTrangThai(stReq.getTrangThai());
			qlnvBbanHaodoiHdrRepository.save(optional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "In phụ lục biên bản xác định hao dôi", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void export(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response, HttpServletRequest req)
			throws Exception {
		String template = "/reports/BB_HAO_DOI.docx";
		try {
			if (StringUtils.isEmpty(searchReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvBbanHaodoiHdr> optional = qlnvBbanHaodoiHdrRepository.findById(searchReq.getId());
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			QlnvBbanHaodoiHdr bb = optional.get();

			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=BB_HAO_DOI_" + getDateTimeNow() + ".docx");

			// Add gia tri bien header
			HashMap<String, String> mappings = new HashMap<String, String>();
			QlnvDmDonvi dvi = getDvi(req);
			mappings.put("param1", dvi.getTenDvi());
			mappings.put("param2", "");
			mappings.put("param3", bb.getSoBban());
			mappings.put("param4", bb.getTenHhoa());
			mappings.put("param5", bb.getMaKho());
			mappings.put("param6", bb.getMaNgan());
			mappings.put("param7", dvi.getTenDvi());
			mappings.put("param8", bb.getSoBban());
			mappings.put("param9", getDateText(bb.getNgayLap()));
			mappings.put("param10", getDateText(bb.getNgayLap()));
			mappings.put("param11", bb.getDiaDiem());
			mappings.put("param12", bb.getThuTruong());
			mappings.put("param13", bb.getKeToan());
			mappings.put("param14", bb.getKthuatVien());
			mappings.put("param15", bb.getThuKho());
			mappings.put("param16", bb.getSluongNhap().toString());
			mappings.put("param17", bb.getDviTinh());
			mappings.put("param18", getDateText(bb.getNgayKthucNhap()));
			mappings.put("param19", bb.getSluongXuat().toString());
			mappings.put("param20", getDateText(bb.getNgayKthucXuat()));

			mappings.put("param21", bb.getSluongHaoTte().toString());
			mappings.put("param22", "");
			mappings.put("param23", bb.getSluongHaoTly().toString());
			mappings.put("param24", "");
			
			mappings.put("param25", bb.getSluongHaoTrendm().toString());
			mappings.put("param26", "");
			mappings.put("param27", bb.getSluongHaoDuoidm().toString());
			mappings.put("param28", "");
			mappings.put("param29", bb.getNguyenNhan());
			mappings.put("param30", bb.getKienNghi());

			// Add parameter to table
			List<QlnvBbanHaodoiDtl> detailList = Optional.ofNullable(bb.getDetailList()).orElse(new ArrayList<>());
			List<Map<String, Object>> lstMapDetail = null;
			if (detailList.size() > 0) {
				lstMapDetail = new ArrayList<Map<String, Object>>();
				Map<String, Object> detailMap;
				for (int i = 0; i < detailList.size(); i++) {
					QlnvBbanHaodoiDtl dtl = detailList.get(i);
					detailMap = Maps.<String, Object>buildMap().put("stt", i + 1)
							.put("tuthoigianbaoquan", getDateText(dtl.getTuNgayBquan()))
							.put("denthoigianbaoquan", getDateText(dtl.getDenNgayBquan()))
							.put("sluongbaoquan", dtl.getSoLuong())
							.put("dinhmuchaohut", dtl.getDmucHao())
							.put("sluonghaotheodm", dtl.getSluongHao())
							.get();
					lstMapDetail.add(detailMap);
				}
			}
						
			// save the docs
			Doc4jUtils.generateDoc(template, mappings, lstMapDetail, dataOutput);
			dataOutput.flush();
			dataOutput.close();
		} catch (Exception e) {
			log.error("In phụ lục biên bản xác định hao dôi", e);
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
