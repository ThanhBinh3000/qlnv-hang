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
import com.tcdt.qlnvhang.common.AsyncProduct;
import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.QlnvBkeCanhangHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.TableInWord;
import com.tcdt.qlnvhang.request.object.QlnvBkeCanhangDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvBkeCanhangHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvBkeCanhangSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvBkeCanhangSpecification;
import com.tcdt.qlnvhang.table.QlnvBkeCanhangDtl;
import com.tcdt.qlnvhang.table.QlnvBkeCanhangHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.Doc4jUtils;
import com.tcdt.qlnvhang.util.Maps;
import com.tcdt.qlnvhang.util.MoneyConvert;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.PathContains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = PathContains.QL_NXUAT_HANG + PathContains.QL_BKE_CANHANG)
@Api(tags = "Quản lý bảng kê cân hàng")
public class QlnvBkeCanhangController extends BaseController {
	@Autowired
	private QlnvBkeCanhangHdrRepository qlnvBkeCanhangHdrRepository;

	@Autowired
	AsyncProduct asyncProduct;

	@ApiOperation(value = "Tạo mới bảng kê cân hàng", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest req,
			@Valid @RequestBody QlnvBkeCanhangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			// Add thong tin hdr
			QlnvBkeCanhangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvBkeCanhangHdr.class);

			dataMap.setSoBke(getUUID(Contains.BANG_KE));
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(req));
			dataMap.setNgayTao(getDateTimeNow());
			if (ObjectUtils.isNotEmpty(objReq.getNgayLap()))
				dataMap.setNgayLap(getDateTimeNow());
			dataMap.setLoaiBke(objReq.getLoaiBke().equals(Contains.BK_NHAP) ? Contains.BK_NHAP : Contains.BK_XUAT);
			// Add thong tin detail
			List<QlnvBkeCanhangDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvBkeCanhangDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvBkeCanhangDtl.class);
			dataMap.setChildren(dtls);

			QlnvBkeCanhangHdr createCheck = qlnvBkeCanhangHdrRepository.save(dataMap);

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

	@ApiOperation(value = "Cập nhật bảng kê cân hàng", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest req,
			@Valid @RequestBody QlnvBkeCanhangHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvBkeCanhangHdr> qOptional = qlnvBkeCanhangHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			objReq.setSoBke(null);
			QlnvBkeCanhangHdr dataDB = qOptional.get();
			QlnvBkeCanhangHdr dataMap = ObjectMapperUtils.map(objReq, QlnvBkeCanhangHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(req));
			if (ObjectUtils.isNotEmpty(objReq.getNgayLap()))
				dataMap.setNgayLap(getDateTimeNow());

			// Add thong tin detail
			List<QlnvBkeCanhangDtlReq> dtlReqList = objReq.getDetail();
			List<QlnvBkeCanhangDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvBkeCanhangDtl.class);
			dataDB.setChildren(dtls);

			QlnvBkeCanhangHdr createCheck = qlnvBkeCanhangHdrRepository.save(dataDB);

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

	@ApiOperation(value = "Lấy chi tiết thông tin bảng kê cân hàng", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID quyết định kế hoạch mua trực tiếp", example = "54", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvBkeCanhangHdr> qOptional = qlnvBkeCanhangHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 bảng kê cân hàng", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest req, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvBkeCanhangHdr> qHoach = qlnvBkeCanhangHdrRepository.findById(Long.valueOf(stReq.getId()));
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
			qlnvBkeCanhangHdrRepository.save(qHoach.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu bảng kê cân hàng", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody QlnvBkeCanhangSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvBkeCanhangHdr> dataPage = qlnvBkeCanhangHdrRepository
					.findAll(QlnvBkeCanhangSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "In bảng kê cân hàng", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void export(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response, HttpServletRequest req)
			throws Exception {
		String template = "/reports/BK_CAN_HANG.docx";
		try {
			if (StringUtils.isEmpty(searchReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvBkeCanhangHdr> qPhieu = qlnvBkeCanhangHdrRepository.findById(Long.valueOf(searchReq.getId()));

			if (!qPhieu.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition", "attachment;filename=BK_CAN_HANG_" + getDateTimeNow() + ".docx");

			// Add parameter to table
			List<QlnvBkeCanhangDtl> detailList = new ArrayList<QlnvBkeCanhangDtl>();
			if (qPhieu.get().getChildren() != null)
				detailList = qPhieu.get().getChildren();

			Map<TableInWord, List<Map<String, Object>>> tableMap = new HashMap<TableInWord, List<Map<String, Object>>>();
			List<Map<String, Object>> lstMapDetail = null;
			BigDecimal soLuong1 = BigDecimal.ZERO;
			BigDecimal trongLuong1 = BigDecimal.ZERO;
			BigDecimal soLuong2 = BigDecimal.ZERO;
			BigDecimal trongLuong2 = BigDecimal.ZERO;
			if (detailList.size() > 0) {
				lstMapDetail = new ArrayList<Map<String, Object>>();
				Map<String, Object> detailMap;
				for (int i = 1; i <= detailList.size(); i++) {
					if (i % 2 == 0) {
						Map<String, Object> detailMapNew = lstMapDetail.get(lstMapDetail.size() - 1);
						lstMapDetail.remove(lstMapDetail.size() - 1);
						detailMapNew.put("macan2", detailList.get(i - 1).getMaCan());
						detailMapNew.put("soluong2", detailList.get(i - 1).getSoLuong());
						detailMapNew.put("trongluong2", detailList.get(i - 1).getTrongLuong());

						lstMapDetail.add(detailMapNew);

						soLuong2 = Optional.ofNullable(detailList.get(i - 1).getSoLuong()).orElse(BigDecimal.ZERO)
								.add(soLuong2);
						trongLuong2 = Optional.ofNullable(detailList.get(i - 1).getTrongLuong()).orElse(BigDecimal.ZERO)
								.add(trongLuong2);
					} else {
						detailMap = Maps.<String, Object>buildMap().put("macan1", detailList.get(i - 1).getMaCan())
								.put("soluong1", detailList.get(i - 1).getSoLuong())
								.put("trongluong1", detailList.get(i - 1).getTrongLuong()).get();
						if (detailList.size() == i) {
							detailMap.put("macan2", "");
							detailMap.put("soluong2", "");
							detailMap.put("trongluong2", "");
						}

						lstMapDetail.add(detailMap);

						soLuong1 = Optional.ofNullable(detailList.get(i - 1).getSoLuong()).orElse(BigDecimal.ZERO)
								.add(soLuong1);
						trongLuong1 = Optional.ofNullable(detailList.get(i - 1).getTrongLuong()).orElse(BigDecimal.ZERO)
								.add(trongLuong1);
					}
				}
			}
			TableInWord tabWord = new TableInWord();
			tabWord.setTbNum(1);
			tabWord.setTbIndex(2);
			tableMap.put(tabWord, lstMapDetail);

			BigDecimal tongTluong = trongLuong1.add(trongLuong2);
			BigDecimal tongTLuongBbi = soLuong1.add(soLuong2).multiply(qPhieu.get().getTluongBbi());
			BigDecimal tongTluongHang = tongTluong.subtract(tongTLuongBbi);
			String dviTinh = qPhieu.get().getDviTinh();

			// Add gia tri bien string
			// TODO: Dang fix tam, sau nay co yeu cau cu the sua sau
			Map<String, String> mappings = Maps.<String, String>buildMap().put("param1", getDvi(req).getTenDvi())
					.put("param2", "").put("param3", qPhieu.get().getSoBke()).put("param4", getUserName(req))
					.put("param5", qPhieu.get().getMaKho()).put("param6", qPhieu.get().getSoKho())
					.put("param7", qPhieu.get().getMaNgan()).put("param8", qPhieu.get().getDiaChi())
					.put("param9", qPhieu.get().getMaHhoa()).put("param10", dviTinh)
					.put("param11", qPhieu.get().getTenNguoiGiao()).put("param12", qPhieu.get().getSoHdong())
					.put("param13", tongTluong.toString() + " " + dviTinh)
					.put("param14", tongTLuongBbi.toString() + " " + dviTinh)
					.put("param15", tongTluongHang.toString() + " " + dviTinh)
					.put("param16", MoneyConvert.docSoLuong(tongTluongHang.toString(), dviTinh))
					.put("param17", getDateText(getDateTimeNow())).put("param18", soLuong1.toString())
					.put("param19", trongLuong1.toString()).put("param20", soLuong2.toString())
					.put("param21", trongLuong2.toString()).get();

			// save the docs
			Doc4jUtils.generateDocMutipleTable(template, mappings, tableMap, dataOutput);
			dataOutput.flush();
			dataOutput.close();
		} catch (Exception e) {
			log.error("In bảng kê cân hàng", e);
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