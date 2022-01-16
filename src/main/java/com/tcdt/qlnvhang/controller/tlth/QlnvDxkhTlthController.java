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
import com.tcdt.qlnvhang.repository.QlnvDxkhTlthHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.QlnvDxkhTlthDtlReq;
import com.tcdt.qlnvhang.request.object.QlnvDxkhTlthHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvDxkhTlthSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.QlnvDxkhTlthSpecification;
import com.tcdt.qlnvhang.table.QlnvDxkhTlthDtl;
import com.tcdt.qlnvhang.table.QlnvDxkhTlthHdr;
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
@RequestMapping(value = PathContains.QL_XTLTH)
@Api(tags = "Quản lý đề xuất thanh lý, tiêu hủy hàng DTQG")
public class QlnvDxkhTlthController extends BaseController {

	@Autowired
	private QlnvDxkhTlthHdrRepository qlnvDxkhTlthHdrRepository;

	@ApiOperation(value = "Tạo mới đề xuất thanh lý, tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> create(HttpServletRequest request,@Valid @RequestBody QlnvDxkhTlthHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			QlnvDxkhTlthHdr dataMap = new ModelMapper().map(objReq, QlnvDxkhTlthHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setMaDvi(getDvi(request).getMaDvi());
			
			// add detail
			List<QlnvDxkhTlthDtlReq> dtlReqList = objReq.getDetailListReq();
			List<QlnvDxkhTlthDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvDxkhTlthDtl.class);
			dataMap.setChildren(dtls);
						
			qlnvDxkhTlthHdrRepository.save(dataMap);

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

	@ApiOperation(value = "Xóa đề xuất thanh lý, tiêu hủy", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<QlnvDxkhTlthHdr> qOptional = qlnvDxkhTlthHdrRepository.findById(idSearchReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			qlnvDxkhTlthHdrRepository.delete(qOptional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu đề xuất thanh lý, tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@RequestBody QlnvDxkhTlthSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<QlnvDxkhTlthHdr> qlnvDxkhTlthHdr = qlnvDxkhTlthHdrRepository.findAll(QlnvDxkhTlthSpecification.buildSearchQuery(objReq), pageable);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(qlnvDxkhTlthHdr);
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Sửa đề xuất thanh lý, tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
											   @Valid @RequestBody QlnvDxkhTlthHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<QlnvDxkhTlthHdr> qlnvDxkhTlthHdr = qlnvDxkhTlthHdrRepository
					.findById(Long.valueOf(objReq.getId()));
			if (!qlnvDxkhTlthHdr.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			QlnvDxkhTlthHdr dataDB = qlnvDxkhTlthHdr.get();
			QlnvDxkhTlthHdr dataMap = ObjectMapperUtils.map(objReq, QlnvDxkhTlthHdr.class);
			updateObjectToObject(dataDB, dataMap);
			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));
			
			List<QlnvDxkhTlthDtlReq> dtlReqList = objReq.getDetailListReq();
			List<QlnvDxkhTlthDtl> dtls = ObjectMapperUtils.mapAll(dtlReqList, QlnvDxkhTlthDtl.class);
			dataDB.setChildren(dtls);
			qlnvDxkhTlthHdrRepository.save(dataDB);

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

	@ApiOperation(value = "Lấy chi tiết thông tin đề xuất thanh lý, tiêu hủy", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID đề xuất thanh lý, tiêu hủy", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");
			Optional<QlnvDxkhTlthHdr> qOptional = qlnvDxkhTlthHdrRepository.findById(Long.parseLong(ids));
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 đề xuất thanh lý, tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> approve(HttpServletRequest req, @Valid @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvDxkhTlthHdr> optional = qlnvDxkhTlthHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			QlnvDxkhTlthHdr qlnvDxkhTlthHdr = optional.get();
			
			String status = stReq.getTrangThai();
			// Lay thong tin don vi quan ly
			QlnvDmDonvi objDvi = getDvi(req);
			if (ObjectUtils.isEmpty(objDvi) || StringUtils.isEmpty(objDvi.getCapDvi()))
				throw new UnsupportedOperationException("Không lấy được thông tin đơn vị");

			if (!objDvi.getCapDvi().equals(Contains.CAP_TONG_CUC) && objDvi.getTrangThai().equals(Contains.TCUC_DUYET)) {
				throw new UnsupportedOperationException("Người sử dụng không phải cấp tổng cục để phê duyệt");
			}
			if (!objDvi.getCapDvi().equals(Contains.CAP_CUC) && objDvi.getTrangThai().equals(Contains.CUC_DUYET)) {
				throw new UnsupportedOperationException("Người sử dụng không phải cấp cục để phê duyệt");
			}
			if (!objDvi.getCapDvi().equals(Contains.CAP_CHI_CUC) && objDvi.getTrangThai().equals(Contains.CCUC_DUYET)) {
				throw new UnsupportedOperationException("Người sử dụng không phải cấp chi cục để phê duyệt");
			}
			
			switch (status) {
			case Contains.CHO_DUYET:
				qlnvDxkhTlthHdr.setNguoiGuiDuyet(getUserName(req));
				qlnvDxkhTlthHdr.setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.CCUC_DUYET:
				qlnvDxkhTlthHdr.setNguoiCcucPduyet(getUserName(req));
				qlnvDxkhTlthHdr.setNgayCcucPduyet(getDateTimeNow());
				break;
			case Contains.CUC_DUYET:
				qlnvDxkhTlthHdr.setNguoiCucPduyet(getUserName(req));
				qlnvDxkhTlthHdr.setNgayCucPduyet(getDateTimeNow());
				break;
			case Contains.TCUC_DUYET:
				qlnvDxkhTlthHdr.setNguoiTcucPduyet(getUserName(req));
				qlnvDxkhTlthHdr.setNgayTcucPduyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI:
				qlnvDxkhTlthHdr.setLdoTuchoi(stReq.getLyDo());
				break;
			default:
				break;
			}

			qlnvDxkhTlthHdr.setTrangThai(stReq.getTrangThai());
			qlnvDxkhTlthHdrRepository.save(qlnvDxkhTlthHdr);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	@ApiOperation(value = "In đề xuất thanh lý, tiêu hủy", response = List.class)
	@PostMapping(value = PathContains.URL_KET_XUAT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void export(@Valid @RequestBody IdSearchReq searchReq, HttpServletResponse response, HttpServletRequest req)
			throws Exception {
		String template = "/reports/DX_TLTH.docx";
		try {
			if (StringUtils.isEmpty(searchReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<QlnvDxkhTlthHdr> optional = qlnvDxkhTlthHdrRepository.findById(Long.valueOf(searchReq.getId()));

			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			ServletOutputStream dataOutput = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.addHeader("content-disposition",
					"attachment;filename=DX_TLTH_" + getDateTimeNow() + ".docx");

			QlnvDxkhTlthHdr qlnvDxkhTlthHdr = optional.get();
			
			// Add parameter to table
			List<QlnvDxkhTlthDtl> detailList = Optional.ofNullable(qlnvDxkhTlthHdr.getChildren()).orElse(new ArrayList<>());

			List<Map<String, Object>> lstMapDetail = null;
			if (detailList.size() > 0) {
				lstMapDetail = new ArrayList<Map<String, Object>>();
				Map<String, Object> detailMap;
				for (int i = 0; i < detailList.size(); i++) {
					QlnvDxkhTlthDtl tlthDtl = detailList.get(i);
					detailMap = Maps.<String, Object>buildMap().put("stt", i + 1)
							.put("donvitinh", tlthDtl.getDviTinh())
							.put("soluong", tlthDtl.getSoLuong())
							.put("ghichu", tlthDtl.getGhiChu())
							.get();
					lstMapDetail.add(detailMap);
				}
			}

			HashMap<String, String> mappings = new HashMap<String, String>();
			mappings.put("param1", Contains.mappingLoaiDx.get(qlnvDxkhTlthHdr.getLhinhXuat()));
			mappings.put("param2", qlnvDxkhTlthHdr.getTenHhoa());
			mappings.put("param3", getDvi(req).getTenDvi());

			// save the docs
			Doc4jUtils.generateDoc(template, mappings, lstMapDetail, dataOutput);
			dataOutput.flush();
			dataOutput.close();
		} catch (Exception e) {
			log.error("In đề xuất thanh lý, tiêu hủy", e);
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
