package com.tcdt.qlnvhang.controller.dexuat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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

import com.tcdt.qlnvhang.controller.BaseController;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.repository.HhPaKhlcntHdrRepository;
import com.tcdt.qlnvhang.repository.HhQdKhlcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntDtlReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.HhQdKhlcntSpecification;
import com.tcdt.qlnvhang.table.FileDKemJoinQdKhlcntHdr;
import com.tcdt.qlnvhang.table.HhPaKhlcntHdr;
import com.tcdt.qlnvhang.table.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.table.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.table.HhQdKhlcntHdr;
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
@RequestMapping(value = PathContains.DX_KH + PathContains.QD_LCNT_GAO)
@Api(tags = "Quyết định phê duyệt kế hoạch lựa chọn nhà thầu")
public class HhQdKhlcntHdrController extends BaseController {

	@Autowired
	private HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

	@Autowired
	private HhPaKhlcntHdrRepository hhPaKhlcntHdrRepository;

	@ApiOperation(value = "Tạo mới Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,
			@Valid @RequestBody HhQdKhlcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
				throw new Exception("Loại vật tư hàng hóa không phù hợp");

			Optional<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
			if (checkSoQd.isPresent())
				throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");

			if (ObjectUtils.isNotEmpty(objReq.getIdPaHdr()) && objReq.getIdPaHdr() > 0) {
				Optional<HhPaKhlcntHdr> qOptional = hhPaKhlcntHdrRepository.findById(objReq.getIdPaHdr());
				if (!qOptional.isPresent())
					throw new Exception("Không tìm thấy phương án kế hoạch lựa chọn nhà thầu");
			}

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
			List<FileDKemJoinQdKhlcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinQdKhlcntHdr>();
			if (objReq.getFileDinhKems() != null) {
				fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdKhlcntHdr.class);
				fileDinhKemList.forEach(f -> {
					f.setDataType(HhQdKhlcntHdr.TABLE_NAME);
					f.setCreateDate(new Date());
				});
			}

			HhQdKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdKhlcntHdr.class);

			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setChildren(fileDinhKemList);

			if (objReq.getDetail() != null) {
				List<HhQdKhlcntDsgthau> detailChild;
				List<HhQdKhlcntDtlReq> dtlReqList = objReq.getDetail();
				for (HhQdKhlcntDtlReq dtlReq : dtlReqList) {
					HhQdKhlcntDtl detail = ObjectMapperUtils.map(dtlReq, HhQdKhlcntDtl.class);
					detailChild = new ArrayList<HhQdKhlcntDsgthau>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhQdKhlcntDsgthau.class);
					UnitScaler.reverseFormatList(detailChild, Contains.DVT_TAN);
					detail.setChildren(detailChild);
					dataMap.addChild1(detail);
				}
			}

			HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(dataMap);

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

	@ApiOperation(value = "Cập nhật Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody HhQdKhlcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {

			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(objReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			if (!qOptional.get().getSoQd().equals(objReq.getSoQd())) {
				Optional<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
				if (checkSoQd.isPresent())
					throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
			}

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
			List<FileDKemJoinQdKhlcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinQdKhlcntHdr>();
			if (objReq.getFileDinhKems() != null) {
				fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdKhlcntHdr.class);
				fileDinhKemList.forEach(f -> {
					f.setDataType(HhQdKhlcntHdr.TABLE_NAME);
					f.setCreateDate(new Date());
				});
			}

			HhQdKhlcntHdr dataDB = qOptional.get();
			HhQdKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdKhlcntHdr.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));
			dataDB.setChildren(fileDinhKemList);

			if (objReq.getDetail() != null) {
				List<HhQdKhlcntDsgthau> detailChild;
				List<HhQdKhlcntDtlReq> dtlReqList = objReq.getDetail();
				for (HhQdKhlcntDtlReq dtlReq : dtlReqList) {
					HhQdKhlcntDtl detail = ObjectMapperUtils.map(dtlReq, HhQdKhlcntDtl.class);
					detailChild = new ArrayList<HhQdKhlcntDsgthau>();
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhQdKhlcntDsgthau.class);
					UnitScaler.reverseFormatList(detailChild, Contains.DVT_TAN);
					detail.setChildren(detailChild);
					dataDB.addChild1(detail);
				}
			}

			HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(dataDB);

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

	@ApiOperation(value = "Tra cứu Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> colection(HttpServletRequest request,
			@Valid @RequestBody HhQdKhlcntSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<HhQdKhlcntHdr> dataPage = hhQdKhlcntHdrRepository
					.findAll(HhQdKhlcntSpecification.buildSearchQuery(objReq), pageable);

			// Lay danh muc dung chung
			Map<String, String> mapDmuc = getMapCategory();

			for (HhQdKhlcntHdr hdr : dataPage.getContent()) {
				hdr.setHthucLcnt(mapDmuc.get(hdr.getHthucLcnt()));
				hdr.setPthucLcnt(mapDmuc.get(hdr.getPthucLcnt()));
				hdr.setLoaiHdong(mapDmuc.get(hdr.getLoaiHdong()));
				hdr.setNguonVon(mapDmuc.get(hdr.getNguonVon()));
			}

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

	@ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID phương án kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(Long.parseLong(ids));

			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			// Quy doi don vi kg = tan
			List<HhQdKhlcntDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren1(), HhQdKhlcntDtl.class);
			for (HhQdKhlcntDtl dtl : dtls2) {
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 Quyết định phê duyệt kế hoạch lựa chọn nhà thầu Gạo", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findById(Long.valueOf(stReq.getId()));
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu");

			String status = stReq.getTrangThai() + optional.get().getTrangThai();
			switch (status) {
			case Contains.CHO_DUYET + Contains.MOI_TAO:
				optional.get().setNguoiGuiDuyet(getUserName(req));
				optional.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI + Contains.CHO_DUYET:
				optional.get().setNguoiPduyet(getUserName(req));
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.DUYET + Contains.CHO_DUYET:
				optional.get().setNguoiPduyet(getUserName(req));
				optional.get().setNgayPduyet(getDateTimeNow());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
			}

			optional.get().setTrangThai(stReq.getTrangThai());
			handlingApprove(Contains.ACTIVE, optional.get());

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

	@ApiOperation(value = "Xoá quyết định phê duyệt kế hoạch lựa chọn nhà thầu", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findById(idSearchReq.getId());
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
					&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
				throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");

			hhQdKhlcntHdrRepository.delete(optional.get());

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

	@ApiOperation(value = "Lấy chi tiết Quyết định phê duyệt kế hoạch lựa chọn nhà thầu theo số quyết định", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/so-qd" + "/{soQd}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detailNumber(
			@ApiParam(value = "Số quyết định phê duyệt kế hoạch lựa chọn nhà thầu", example = "1", required = true) @PathVariable("soQd") String soQd) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(soQd))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findBySoQd(soQd);

			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			// Quy doi don vi kg = tan
			List<HhQdKhlcntDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren1(), HhQdKhlcntDtl.class);
			for (HhQdKhlcntDtl dtl : dtls2) {
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
	synchronized HhQdKhlcntHdr handlingApprove(String trangThai, HhQdKhlcntHdr hThopHdr) throws Exception {
		HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(hThopHdr);
		if (createCheck.getIdPaHdr() > 0 && hThopHdr.getTrangThai().equals(Contains.DUYET)) {
			hhPaKhlcntHdrRepository.updateTongHop(hThopHdr.getIdPaHdr(), trangThai);
		}
		return createCheck;
	}

}
