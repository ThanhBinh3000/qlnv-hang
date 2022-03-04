package com.tcdt.qlnvhang.controller.dexuat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.HhDxKhLcntThopHdrRepository;
import com.tcdt.qlnvhang.repository.HhPaKhlcntHdrRepository;
import com.tcdt.qlnvhang.request.object.HhPaKhlcntDtlReq;
import com.tcdt.qlnvhang.request.object.HhPaKhlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhPaKhlcntSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.HhPaKhlcntSpecification;
import com.tcdt.qlnvhang.table.FileDKemJoinPaKhlcntHdr;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.HhPaKhlcntDsgthau;
import com.tcdt.qlnvhang.table.HhPaKhlcntDtl;
import com.tcdt.qlnvhang.table.HhPaKhlcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import com.tcdt.qlnvhang.util.PathContains;
import com.tcdt.qlnvhang.util.UnitScaler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PathContains.DX_KH + PathContains.PA_LCNT_GAO)
@Api(tags = "Phương án kế hoạch lựa chọn nhà thầu")
public class HhPaKhlcntHdrController extends BaseController {

	@Autowired
	private HhPaKhlcntHdrRepository hhPaKhlcntHdrRepository;

	@Autowired
	private HhDxKhLcntThopHdrRepository hhDxKhLcntThopHdrRepository;

	@ApiOperation(value = "Tạo mới phương án đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,
			@Valid @RequestBody HhPaKhlcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
				throw new Exception("Loại vật tư hàng hóa không phù hợp");

			Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(objReq.getIdThHdr());
			if (!qOptional.isPresent())
				throw new Exception("Tổng hợp đề xuất kế hoạch lựa chọn nhà thầu không tồn tại");

			// Lay danh muc dung chung
			Map<String, String> mapDmuc = getMapCategory();
			if (objReq.getHthucLcnt() == null || !mapDmuc.containsKey(objReq.getHthucLcnt()))
				throw new Exception("Hình thức lựa chọn nhà thầu không phù hợp");

			if (objReq.getPthucLcnt() == null || !mapDmuc.containsKey(objReq.getPthucLcnt()))
				throw new Exception("Phương thức đấu thầu không phù hợp");

			if (objReq.getLoaiHdong() == null || !mapDmuc.containsKey(objReq.getLoaiHdong()))
				throw new Exception("Loại hợp đồng không phù hợp");

			if (objReq.getNguonVon() == null || !mapDmuc.containsKey(objReq.getNguonVon()))
				throw new Exception("Nguồn vốn không phù hợp");

			// Add danh sach file dinh kem o Master
			List<FileDKemJoinPaKhlcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinPaKhlcntHdr>();
			if (objReq.getFileDinhKems() != null) {
				fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinPaKhlcntHdr.class);
				fileDinhKemList.forEach(f -> {
					f.setDataType(HhPaKhlcntHdr.TABLE_NAME);
					f.setCreateDate(new Date());
				});
			}

			HhPaKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhPaKhlcntHdr.class);

			dataMap.setNguoiTao(getUserName(request));
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setChildren(fileDinhKemList);

			if (objReq.getDetail() != null) {
				List<HhPaKhlcntDsgthau> detailChild;
				List<HhPaKhlcntDtlReq> dtlReqList = objReq.getDetail();
				for (HhPaKhlcntDtlReq dtlReq : dtlReqList) {
					HhPaKhlcntDtl detail = ObjectMapperUtils.map(dtlReq, HhPaKhlcntDtl.class);
					detailChild = new ArrayList<HhPaKhlcntDsgthau>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhPaKhlcntDsgthau.class);
					UnitScaler.reverseFormatList(detailChild, Contains.DVT_TAN);
					detail.setChildren(detailChild);
					dataMap.addChild1(detail);
				}
			}

			HhPaKhlcntHdr createCheck = handlingCreate(Contains.ACTIVE, dataMap);

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

	@ApiOperation(value = "Cập nhật phương án đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody HhPaKhlcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {

			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<HhPaKhlcntHdr> qOptional = hhPaKhlcntHdrRepository.findById(objReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			// Lay danh muc dung chung
			Map<String, String> mapDmuc = getMapCategory();
			if (objReq.getHthucLcnt() == null || !mapDmuc.containsKey(objReq.getHthucLcnt()))
				throw new Exception("Hình thức lựa chọn nhà thầu không phù hợp");

			if (objReq.getPthucLcnt() == null || !mapDmuc.containsKey(objReq.getPthucLcnt()))
				throw new Exception("Phương thức đấu thầu không phù hợp");

			if (objReq.getLoaiHdong() == null || !mapDmuc.containsKey(objReq.getLoaiHdong()))
				throw new Exception("Loại hợp đồng không phù hợp");

			if (objReq.getNguonVon() == null || !mapDmuc.containsKey(objReq.getNguonVon()))
				throw new Exception("Nguồn vốn không phù hợp");

			// Add danh sach file dinh kem o Master
			List<FileDKemJoinPaKhlcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinPaKhlcntHdr>();
			if (objReq.getFileDinhKems() != null) {
				fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinPaKhlcntHdr.class);
				fileDinhKemList.forEach(f -> {
					f.setDataType(HhPaKhlcntHdr.TABLE_NAME);
					f.setCreateDate(new Date());
				});
			}

			objReq.setIdThHdr(null);
			HhPaKhlcntHdr dataDB = qOptional.get();
			HhPaKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhPaKhlcntHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setChildren(fileDinhKemList);

			if (objReq.getDetail() != null) {
				List<HhPaKhlcntDsgthau> detailChild;
				List<HhPaKhlcntDtlReq> dtlReqList = objReq.getDetail();
				for (HhPaKhlcntDtlReq dtlReq : dtlReqList) {
					HhPaKhlcntDtl detail = ObjectMapperUtils.map(dtlReq, HhPaKhlcntDtl.class);
					detailChild = new ArrayList<HhPaKhlcntDsgthau>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhPaKhlcntDsgthau.class);
					UnitScaler.reverseFormatList(detailChild, Contains.DVT_TAN);
					detail.setChildren(detailChild);
					dataDB.addChild1(detail);
				}
			}

			HhPaKhlcntHdr createCheck = hhPaKhlcntHdrRepository.save(dataDB);

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

	@ApiOperation(value = "Tra cứu phương án đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody HhPaKhlcntSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<HhPaKhlcntHdr> dataPage = hhPaKhlcntHdrRepository
					.findAll(HhPaKhlcntSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "Lấy chi tiết phương án lựa chọn nhà thầu", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID phương án kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			Optional<HhPaKhlcntHdr> qOptional = hhPaKhlcntHdrRepository.findById(Long.parseLong(ids));

			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			// Quy doi don vi kg = tan
			List<HhPaKhlcntDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren1(), HhPaKhlcntDtl.class);
			for (HhPaKhlcntDtl dtl : dtls2) {
				UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
			}

			resp.setData(qOptional.get());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@Transactional
	synchronized HhPaKhlcntHdr handlingCreate(String trangThai, HhPaKhlcntHdr hThopHdr) throws Exception {
		HhPaKhlcntHdr createCheck = hhPaKhlcntHdrRepository.save(hThopHdr);
		if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
			hhDxKhLcntThopHdrRepository.updateTongHop(hThopHdr.getIdThHdr(), trangThai);
		}
		return createCheck;
	}

}
