package com.tcdt.qlnvhang.controller.dexuat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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
import com.tcdt.qlnvhang.repository.HhDthau2Repository;
import com.tcdt.qlnvhang.repository.HhDthauRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDthauGthauReq;
import com.tcdt.qlnvhang.request.object.HhDthauHsoKthuatReq;
import com.tcdt.qlnvhang.request.object.HhDthauKquaLcntReq;
import com.tcdt.qlnvhang.request.object.HhDthauReq;
import com.tcdt.qlnvhang.request.object.HhDthauTthaoHdongReq;
import com.tcdt.qlnvhang.request.search.HhDthauSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.HhDthau2Specification;
import com.tcdt.qlnvhang.table.FileDKemJoinGoiThau;
import com.tcdt.qlnvhang.table.FileDKemJoinHsoKthuat;
import com.tcdt.qlnvhang.table.FileDKemJoinKquaLcnt;
import com.tcdt.qlnvhang.table.FileDKemJoinTthaoHdong;
import com.tcdt.qlnvhang.table.HhDthau;
import com.tcdt.qlnvhang.table.HhDthau2;
import com.tcdt.qlnvhang.table.HhDthauGthau;
import com.tcdt.qlnvhang.table.HhDthauHsoKthuat;
import com.tcdt.qlnvhang.table.HhDthauHsoTchinh;
import com.tcdt.qlnvhang.table.HhDthauKquaLcnt;
import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;
import com.tcdt.qlnvhang.table.HhDthauTthaoHdong;
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
@RequestMapping(value = PathContains.DX_KH + PathContains.TTIN_DTHAU)
@Api(tags = "Thông tin đấu thầu gạo")
public class HhDauThauController extends BaseController {

	@Autowired
	private HhDthauRepository hhDthauRepository;

	@Autowired
	private HhDthau2Repository hhDthau2Repository;

	@ApiOperation(value = "Tạo mới thông tin đấu thầu gạo", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request, @Valid @RequestBody HhDthauReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
				throw new Exception("Loại vật tư hàng hóa không phù hợp");

			HhDthau dataMap = ObjectMapperUtils.map(objReq, HhDthau.class);

			dataMap.setNguoiTao(getUserName(request));
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);

			// Them thong tin chi tiet thong tin dau thau
			if (objReq.getDetail() != null) {
				List<HhDthauNthauDuthau> detailChild = new ArrayList<HhDthauNthauDuthau>();
				List<HhDthauHsoKthuat> detailChild1 = new ArrayList<HhDthauHsoKthuat>();
				List<HhDthauHsoTchinh> detailChild2 = new ArrayList<HhDthauHsoTchinh>();
				List<HhDthauTthaoHdong> detailChild3 = new ArrayList<HhDthauTthaoHdong>();
				List<HhDthauKquaLcnt> detailChild4 = new ArrayList<HhDthauKquaLcnt>();
				List<FileDKemJoinGoiThau> detailChild5 = new ArrayList<FileDKemJoinGoiThau>();

				// Thong tin goi thau
				List<HhDthauGthauReq> dtlReqList = objReq.getDetail();
				for (HhDthauGthauReq dtlReq : dtlReqList) {
					HhDthauGthau detail = ObjectMapperUtils.map(dtlReq, HhDthauGthau.class);
					detailChild = new ArrayList<HhDthauNthauDuthau>();
					detailChild1 = new ArrayList<HhDthauHsoKthuat>();
					detailChild2 = new ArrayList<HhDthauHsoTchinh>();
					detailChild3 = new ArrayList<HhDthauTthaoHdong>();
					detailChild4 = new ArrayList<HhDthauKquaLcnt>();
					detailChild5 = new ArrayList<FileDKemJoinGoiThau>();

					// Thong tin danh sach cac nha thau du thau
					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhDthauNthauDuthau.class);

					// Thong tin danh sach ho so ky thuat
					if (dtlReq.getDetail1() != null) {
						for (HhDthauHsoKthuatReq hsKthuatReq : dtlReq.getDetail1()) {
							HhDthauHsoKthuat hsKthuat = ObjectMapperUtils.map(hsKthuatReq, HhDthauHsoKthuat.class);
							List<FileDKemJoinHsoKthuat> fileDkHsKts = ObjectMapperUtils
									.mapAll(hsKthuatReq.getFileDinhKems(), FileDKemJoinHsoKthuat.class);
							fileDkHsKts.forEach(f -> {
								f.setDataType(HhDthauHsoKthuat.TABLE_NAME);
								f.setCreateDate(new Date());
							});
							hsKthuat.setChildren(fileDkHsKts);
							detailChild1.add(hsKthuat);
						}
					}

					// Thong tin danh sach ho so tai chinh
					if (dtlReq.getDetail2() != null)
						detailChild2 = ObjectMapperUtils.mapAll(dtlReq.getDetail2(), HhDthauHsoTchinh.class);

					// Thong tin thuong thao hop dong
					if (dtlReq.getDetail3() != null) {
						for (HhDthauTthaoHdongReq hsTtHdReq : dtlReq.getDetail3()) {
							HhDthauTthaoHdong hsTtHd = ObjectMapperUtils.map(hsTtHdReq, HhDthauTthaoHdong.class);
							List<FileDKemJoinTthaoHdong> fileDkTtHds = ObjectMapperUtils
									.mapAll(hsTtHdReq.getFileDinhKems(), FileDKemJoinTthaoHdong.class);
							fileDkTtHds.forEach(f -> {
								f.setDataType(HhDthauTthaoHdong.TABLE_NAME);
								f.setCreateDate(new Date());
							});
							hsTtHd.setChildren(fileDkTtHds);
							detailChild3.add(hsTtHd);
						}
					}

					// Thong tin ket qua lua chon nha thau
					if (dtlReq.getDetail4() != null) {
						for (HhDthauKquaLcntReq kqLcntReq : dtlReq.getDetail4()) {
							HhDthauKquaLcnt kqLcnt = ObjectMapperUtils.map(kqLcntReq, HhDthauKquaLcnt.class);
							List<FileDKemJoinKquaLcnt> fileDkKqLcnts = ObjectMapperUtils
									.mapAll(kqLcntReq.getFileDinhKems(), FileDKemJoinKquaLcnt.class);
							fileDkKqLcnts.forEach(f -> {
								f.setDataType(HhDthauKquaLcnt.TABLE_NAME);
								f.setCreateDate(new Date());
							});
							kqLcnt.setChildren(fileDkKqLcnts);
							detailChild4.add(kqLcnt);
						}
					}

					// File dinh kem cua goi thau
					if (dtlReq.getFileDinhKems() != null) {
						detailChild5 = ObjectMapperUtils.mapAll(dtlReq.getFileDinhKems(), FileDKemJoinGoiThau.class);
						detailChild5.forEach(f -> {
							f.setDataType(HhDthauGthau.TABLE_NAME);
							f.setCreateDate(new Date());
						});
					}

					detail.setChildren(detailChild);
					detail.setChildren1(detailChild1);
					detail.setChildren2(detailChild2);
					detail.setChildren3(detailChild3);
					detail.setChildren4(detailChild4);
					detail.setChildren5(detailChild5);
					dataMap.addChild(detail);
				}
			}

			UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
			HhDthau createCheck = hhDthauRepository.save(dataMap);

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

	@ApiOperation(value = "Cập nhật thông tin đấu thầu gạo", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request, @Valid @RequestBody HhDthauReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {

			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<HhDthau> qOptional = hhDthauRepository.findById(objReq.getId());
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			HhDthau dataDB = qOptional.get();
			HhDthau dataMap = ObjectMapperUtils.map(objReq, HhDthau.class);

			updateObjectToObject(dataDB, dataMap);

			dataDB.setNgaySua(getDateTimeNow());
			dataDB.setNguoiSua(getUserName(request));

			if (objReq.getDetail() != null) {
				List<HhDthauNthauDuthau> detailChild = new ArrayList<HhDthauNthauDuthau>();
				List<HhDthauHsoKthuat> detailChild1 = new ArrayList<HhDthauHsoKthuat>();
				List<HhDthauHsoTchinh> detailChild2 = new ArrayList<HhDthauHsoTchinh>();
				List<HhDthauTthaoHdong> detailChild3 = new ArrayList<HhDthauTthaoHdong>();
				List<HhDthauKquaLcnt> detailChild4 = new ArrayList<HhDthauKquaLcnt>();
				List<FileDKemJoinGoiThau> detailChild5 = new ArrayList<FileDKemJoinGoiThau>();

				List<HhDthauGthauReq> dtlReqList = objReq.getDetail();
				for (HhDthauGthauReq dtlReq : dtlReqList) {
					HhDthauGthau detail = ObjectMapperUtils.map(dtlReq, HhDthauGthau.class);
					detailChild = new ArrayList<HhDthauNthauDuthau>();
					detailChild1 = new ArrayList<HhDthauHsoKthuat>();
					detailChild2 = new ArrayList<HhDthauHsoTchinh>();
					detailChild3 = new ArrayList<HhDthauTthaoHdong>();
					detailChild4 = new ArrayList<HhDthauKquaLcnt>();
					detailChild5 = new ArrayList<FileDKemJoinGoiThau>();

					if (dtlReq.getDetail() != null)
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getDetail(), HhDthauNthauDuthau.class);

					if (dtlReq.getDetail1() != null) {
						for (HhDthauHsoKthuatReq hsKthuatReq : dtlReq.getDetail1()) {
							HhDthauHsoKthuat hsKthuat = ObjectMapperUtils.map(hsKthuatReq, HhDthauHsoKthuat.class);
							List<FileDKemJoinHsoKthuat> fileDkHsKts = ObjectMapperUtils
									.mapAll(hsKthuatReq.getFileDinhKems(), FileDKemJoinHsoKthuat.class);
							fileDkHsKts.forEach(f -> {
								f.setDataType(HhDthauHsoKthuat.TABLE_NAME);
								f.setCreateDate(new Date());
							});
							hsKthuat.setChildren(fileDkHsKts);
							detailChild1.add(hsKthuat);
						}
					}

					if (dtlReq.getDetail2() != null)
						detailChild2 = ObjectMapperUtils.mapAll(dtlReq.getDetail2(), HhDthauHsoTchinh.class);

					if (dtlReq.getDetail3() != null) {
						for (HhDthauTthaoHdongReq hsTtHdReq : dtlReq.getDetail3()) {
							HhDthauTthaoHdong hsTtHd = ObjectMapperUtils.map(hsTtHdReq, HhDthauTthaoHdong.class);
							List<FileDKemJoinTthaoHdong> fileDkTtHds = ObjectMapperUtils
									.mapAll(hsTtHdReq.getFileDinhKems(), FileDKemJoinTthaoHdong.class);
							fileDkTtHds.forEach(f -> {
								f.setDataType(HhDthauTthaoHdong.TABLE_NAME);
								f.setCreateDate(new Date());
							});
							hsTtHd.setChildren(fileDkTtHds);
							detailChild3.add(hsTtHd);
						}
					}

					if (dtlReq.getDetail4() != null) {
						for (HhDthauKquaLcntReq kqLcntReq : dtlReq.getDetail4()) {
							HhDthauKquaLcnt kqLcnt = ObjectMapperUtils.map(kqLcntReq, HhDthauKquaLcnt.class);
							List<FileDKemJoinKquaLcnt> fileDkKqLcnts = ObjectMapperUtils
									.mapAll(kqLcntReq.getFileDinhKems(), FileDKemJoinKquaLcnt.class);
							fileDkKqLcnts.forEach(f -> {
								f.setDataType(HhDthauKquaLcnt.TABLE_NAME);
								f.setCreateDate(new Date());
							});
							kqLcnt.setChildren(fileDkKqLcnts);
							detailChild4.add(kqLcnt);
						}
					}

					if (dtlReq.getFileDinhKems() != null) {
						detailChild5 = ObjectMapperUtils.mapAll(dtlReq.getFileDinhKems(), FileDKemJoinGoiThau.class);
						detailChild5.forEach(f -> {
							f.setDataType(HhDthauGthau.TABLE_NAME);
							f.setCreateDate(new Date());
						});
					}

					detail.setChildren(detailChild);
					detail.setChildren1(detailChild1);
					detail.setChildren2(detailChild2);
					detail.setChildren3(detailChild3);
					detail.setChildren4(detailChild4);
					detail.setChildren5(detailChild5);
					dataDB.addChild(detail);
				}
			}

			UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
			HhDthau createCheck = hhDthauRepository.save(dataDB);

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

	@ApiOperation(value = "Lấy chi tiết thông tin đấu thầu gạo", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID thông tin đấu thầu gạo", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			Optional<HhDthau> qOptional = hhDthauRepository.findById(Long.parseLong(ids));

			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			// Quy doi don vi kg = tan
			List<HhDthauGthau> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), HhDthauGthau.class);
			for (HhDthauGthau dtl : dtls2) {
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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03 thông tin đấu thầu gạo", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<HhDthau> optional = hhDthauRepository.findById(Long.valueOf(stReq.getId()));
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
			hhDthauRepository.save(optional.get());

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá thông tin đấu thầu gạo", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<HhDthau> optional = hhDthauRepository.findById(idSearchReq.getId());
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
					&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
				throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");

			hhDthauRepository.delete(optional.get());

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
			@Valid @RequestBody HhDthauSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

			Page<HhDthau2> dataPage = hhDthau2Repository.findAll(HhDthau2Specification.buildSearchQuery(objReq),
					pageable);

			for (HhDthau2 hdr : dataPage.getContent()) {
				hdr.setTenDvi(getDviByMa(request, hdr.getMaDvi()).getTenDvi());
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

}
