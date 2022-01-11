package com.tcdt.qlnvhang.controller.nhapxuat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvBkeNhapvtuHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.TableInWord;
import com.tcdt.qlnvhang.request.object.QlnvBkeNhapvtuDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvBkeNhapvtuHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvBkeNhapvtuSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvBkeNhapvtuSpecification;
import com.tcdt.qlnvhang.table.QlnvBkeNhapvtuDtl;
import com.tcdt.qlnvhang.table.QlnvBkeNhapvtuHdr;
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
@RequestMapping(value = PathContains.QL_NXUAT_HANG + PathContains.QL_BKE_NHAPVTU)
@Api(tags = "Quản lý bảng kê nhập vật tư")
public class QlnvBkeNhapvtuController extends BaseController {
	@Autowired
	private QlnvBkeNhapvtuHdrRepository qlnvBkeNhapvtuHdrRepository;

	@ApiOperation(value = "Tạo mới bảng kê nhập vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest req,
			@Valid @RequestBody QlnvBkeNhapvtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			// Add thong tin hdr
			QlnvBkeNhapvtuHdr dataMap = ObjectMapperUtils.map(objReq, QlnvBkeNhapvtuHdr.class);

			dataMap.setSoBke(getUUID(Contains.BANG_KE));
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(req));
			dataMap.setNgayTao(getDateTimeNow());
			if (ObjectUtils.isNotEmpty(objReq.getNgayLap()))
				dataMap.setNgayLap(getDateTimeNow());
			dataMap.setLoaiBke(objReq.getLoaiBke().equals(Contains.BK_NHAP) ? Contains.BK_NHAP : Contains.BK_XUAT);
			// Add thong tin detail
			List<QlnvBkeNhapvtuDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvBkeNhapvtuDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvBkeNhapvtuDtl.class);
			dataMap.setChildren(dtls);

			QlnvBkeNhapvtuHdr createCheck = qlnvBkeNhapvtuHdrRepository.save(dataMap);

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

	@ApiOperation(value = "Cập nhật bảng kê nhập vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest req,
			@Valid @RequestBody QlnvBkeNhapvtuHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvBkeNhapvtuHdr> qOptional = qlnvBkeNhapvtuHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			objReq.setSoBke(null);
			QlnvBkeNhapvtuHdr dataDB = qOptional.get();
			QlnvBkeNhapvtuHdr dataMap = ObjectMapperUtils.map(objReq, QlnvBkeNhapvtuHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(req));
			if (ObjectUtils.isNotEmpty(objReq.getNgayLap()))
				dataMap.setNgayLap(getDateTimeNow());

			// Add thong tin detail
			List<QlnvBkeNhapvtuDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvBkeNhapvtuDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvBkeNhapvtuDtl.class);
			dataDB.setChildren(dtls);

			QlnvBkeNhapvtuHdr createCheck = qlnvBkeNhapvtuHdrRepository.save(dataDB);

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

	@ApiOperation(value = "Lấy chi tiết thông tin bảng kê nhập vật tư", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID quyết định kế hoạch mua trực tiếp", example = "54", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvBkeNhapvtuHdr> qOptional = qlnvBkeNhapvtuHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 bảng kê nhập vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest req, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvBkeNhapvtuHdr> qHoach = qlnvBkeNhapvtuHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!qHoach.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			String status = stReq.getTrangThai() + qHoach.get().getTrangThai();
			switch (status) {
			case Contains.CHO_DUYET + Contains.MOI_TAO:
				qHoach.get().setNguoiGuiDuyet(getUserName(req));
				qHoach.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI + Contains.CHO_DUYET:
				qHoach.get().setTkPduyet(getUserName(req));
				qHoach.get().setNgayTkPduyet(getDateTimeNow());
				qHoach.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.TU_CHOI + Contains.TK_DUYET:
				qHoach.get().setKtvPduyet(getUserName(req));
				qHoach.get().setNgayKtvPduyet(getDateTimeNow());
				qHoach.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.TU_CHOI + Contains.KTV_DUYET:
				qHoach.get().setKttPduyet(getUserName(req));
				qHoach.get().setNgayKttPduyet(getDateTimeNow());
				qHoach.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.TU_CHOI + Contains.KTT_DUYET:
				qHoach.get().setNguoiPduyet(getUserName(req));
				qHoach.get().setNgayPduyet(getDateTimeNow());
				qHoach.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.TK_DUYET + Contains.CHO_DUYET:
				qHoach.get().setTkPduyet(getUserName(req));
				qHoach.get().setNgayTkPduyet(getDateTimeNow());
				break;
			case Contains.KTV_DUYET + Contains.TK_DUYET:
				qHoach.get().setKtvPduyet(getUserName(req));
				qHoach.get().setNgayKtvPduyet(getDateTimeNow());
				break;
			case Contains.KTT_DUYET + Contains.KTV_DUYET:
				qHoach.get().setKttPduyet(getUserName(req));
				qHoach.get().setNgayKttPduyet(getDateTimeNow());
				break;
			case Contains.DUYET + Contains.KTT_DUYET:
				qHoach.get().setNguoiPduyet(getUserName(req));
				qHoach.get().setNgayPduyet(getDateTimeNow());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
			}

			qHoach.get().setTrangThai(stReq.getTrangThai());
			qlnvBkeNhapvtuHdrRepository.save(qHoach.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu bảng kê nhập vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvBkeNhapvtuSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvBkeNhapvtuHdr> dataPage = qlnvBkeNhapvtuHdrRepository
					.findAll(QlnvBkeNhapvtuSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "In bảng kê nhập vật tư", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void export(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response, HttpServletRequest req)
			throws Exception {
		String template = "/reports/BK_NHAP_VTU.docx";
		try {
			if (StringUtils.isEmpty(searchReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvBkeNhapvtuHdr> qPhieu = qlnvBkeNhapvtuHdrRepository.findById(Long.valueOf(searchReq.getId()));

			if (!qPhieu.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=BK_NHAP_VTU_" + getDateTimeNow() + ".docx");

			// Add parameter to table
			List<QlnvBkeNhapvtuDtl> detailList = new ArrayList<QlnvBkeNhapvtuDtl>();
			if (qPhieu.get().getChildren() != null)
				detailList = qPhieu.get().getChildren();

			Map<TableInWord, List<Map<String, Object>>> tableMap = new HashMap<TableInWord, List<Map<String, Object>>>();
			List<Map<String, Object>> lstMapDetail = null;
			BigDecimal soLuong = BigDecimal.ZERO;
			if (detailList.size() > 0) {
				lstMapDetail = new ArrayList<Map<String, Object>>();
				Map<String, Object> detailMap;
				for (int i = 0; i < detailList.size(); i++) {
					detailMap = Maps.<String, Object>buildMap().put("stt", i + 1)
							.put("soluong", detailList.get(i).getSoLuong())
							.put("soserial", detailList.get(i).getSoSerial()).get();
					lstMapDetail.add(detailMap);
					soLuong = Optional.ofNullable(detailList.get(i - 1).getSoLuong()).orElse(BigDecimal.ZERO)
							.add(soLuong);
				}
			}
			TableInWord tabWord = new TableInWord();
			tabWord.setTbNum(1);
			tabWord.setTbIndex(1);
			tableMap.put(tabWord, lstMapDetail);

			// Add gia tri bien string
			// TODO: Dang fix tam, sau nay co yeu cau cu the sua sau
			Map<String, String> mappings = Maps.<String, String>buildMap().put("param1", getDvi(req).getTenDvi())
					.put("param2", "").put("param3", qPhieu.get().getSoBke()).put("param4", getUserName(req))
					.put("param5",
							qPhieu.get().getMaKho() + "-" + qPhieu.get().getMaNgan() + "-" + qPhieu.get().getMaLo())
					.put("param6", qPhieu.get().getSoKho()).put("param7", "").put("param8", qPhieu.get().getMaHhoa())
					.put("param9", qPhieu.get().getTenNguoiGiao()).put("param10", qPhieu.get().getSoHdong())
					.put("param11", getDateText(qPhieu.get().getNgayGiao())).put("param12", soLuong.toString()).get();

			// save the docs
			Doc4jUtils.generateDocMutipleTable(template, mappings, tableMap, dataOutput);
			dataOutput.flush();
			dataOutput.close();
		} catch (Exception e) {
			log.error("In bảng kê nhập vật tư", e);
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