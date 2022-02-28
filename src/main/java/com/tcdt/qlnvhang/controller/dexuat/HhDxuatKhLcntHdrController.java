package com.tcdt.qlnvhang.controller.dexuat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.tcdt.qlnvhang.repository.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntCcxdgDtlReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.HhDxuatKhLcntSpecification;
import com.tcdt.qlnvhang.table.FileDKemJoinDxKhLcntCcxdg;
import com.tcdt.qlnvhang.table.FileDKemJoinDxKhLcntHdr;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntCcxdgDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntDsgtDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntGaoDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.util.Contains;
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
@RequestMapping(value = PathContains.DX_KH + PathContains.DX_KH_LCNT_GAO)
@Api(tags = "Đề xuất kế hoạch lựa chọn nhà thầu Gạo")
public class HhDxuatKhLcntHdrController extends BaseController {

	@Autowired
	private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

	@ApiOperation(value = "Tạo mới đề xuất kế hoạch lựa chọn nhà thầu Gạo", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(@Valid HttpServletRequest request,
			@RequestBody HhDxuatKhLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
				throw new Exception("Loại vật tư hàng hóa không phù hợp");

			Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
			if (!qOptional.isPresent())
				throw new Exception("Số đề xuất " + objReq.getSoDxuat() + "đã tồn tại");

			// Add danh sach file dinh kem o Master
			List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
			if (objReq.getFileDinhKems() != null) {
				fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinDxKhLcntHdr.class);
				fileDinhKemList.forEach(f -> {
					f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
					f.setCreateDate(new Date());
				});
			}

			HhDxuatKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhLcntHdr.class);
			dataMap.setNgayTao(getDateTimeNow());
			dataMap.setTrangThai(Contains.TAO_MOI);
			dataMap.setNguoiTao(getUserName(request));
			dataMap.setChildren(fileDinhKemList);

			// Add thong tin chung
			List<HhDxuatKhLcntGaoDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail1(), HhDxuatKhLcntGaoDtl.class);
			dataMap.setChildren1(dtls1);
			// Add danh sach goi thau
			List<HhDxuatKhLcntDsgtDtl> dtls2 = ObjectMapperUtils.mapAll(objReq.getDetail2(),
					HhDxuatKhLcntDsgtDtl.class);
			dataMap.setChildren2(dtls2);
			// Add danh sach can cu xac dinh gia
			if (objReq.getDetail3() != null) {
				List<FileDKemJoinDxKhLcntCcxdg> detailChild;
				List<HhDxuatKhLcntCcxdgDtlReq> dtlReqList = objReq.getDetail3();
				for (HhDxuatKhLcntCcxdgDtlReq dtlReq : dtlReqList) {
					HhDxuatKhLcntCcxdgDtl detail = ObjectMapperUtils.map(dtlReq, HhDxuatKhLcntCcxdgDtl.class);
					detailChild = new ArrayList<FileDKemJoinDxKhLcntCcxdg>();
					if (dtlReq.getFileDinhKems() != null) {
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getFileDinhKems(),
								FileDKemJoinDxKhLcntCcxdg.class);
						detailChild.forEach(f -> {
							f.setDataType(HhDxuatKhLcntCcxdgDtl.TABLE_NAME);
							f.setCreateDate(new Date());
						});
					}
					detail.setChildren(detailChild);
					dataMap.addChild3(detail);
				}
			}

			HhDxuatKhLcntHdr createCheck = hhDxuatKhLcntHdrRepository.save(dataMap);

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

	@ApiOperation(value = "Cập nhật đề xuất kế hoạch lựa chọn nhà thầu Gạo", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(@Valid HttpServletRequest request,
			@RequestBody HhDxuatKhLcntHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
				throw new Exception("Loại vật tư hàng hóa không phù hợp");

			// Add danh sach file dinh kem o Master
			List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
			if (objReq.getFileDinhKems() != null) {
				fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinDxKhLcntHdr.class);
				fileDinhKemList.forEach(f -> {
					f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
					f.setCreateDate(new Date());
				});
			}

			HhDxuatKhLcntHdr dataDTB = qOptional.get();
			HhDxuatKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDxuatKhLcntHdr.class);

			updateObjectToObject(dataDTB, dataMap);

			dataDTB.setNgaySua(getDateTimeNow());
			dataDTB.setNguoiSua(getUserName(request));
			dataDTB.setChildren(fileDinhKemList);

			// Add thong tin chung
			List<HhDxuatKhLcntGaoDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail1(), HhDxuatKhLcntGaoDtl.class);
			dataDTB.setChildren1(dtls1);
			// Add danh sach goi thau
			List<HhDxuatKhLcntDsgtDtl> dtls2 = ObjectMapperUtils.mapAll(objReq.getDetail2(),
					HhDxuatKhLcntDsgtDtl.class);
			dataDTB.setChildren2(dtls2);
			// Add danh sach can cu xac dinh gia
			if (objReq.getDetail3() != null) {
				List<FileDKemJoinDxKhLcntCcxdg> detailChild;
				List<HhDxuatKhLcntCcxdgDtlReq> dtlReqList = objReq.getDetail3();
				for (HhDxuatKhLcntCcxdgDtlReq dtlReq : dtlReqList) {
					HhDxuatKhLcntCcxdgDtl detail = ObjectMapperUtils.map(dtlReq, HhDxuatKhLcntCcxdgDtl.class);
					detailChild = new ArrayList<FileDKemJoinDxKhLcntCcxdg>();
					if (dtlReq.getFileDinhKems() != null) {
						detailChild = ObjectMapperUtils.mapAll(dtlReq.getFileDinhKems(),
								FileDKemJoinDxKhLcntCcxdg.class);
						detailChild.forEach(f -> {
							f.setDataType(HhDxuatKhLcntCcxdgDtl.TABLE_NAME);
							f.setCreateDate(new Date());
						});
					}
					detail.setChildren(detailChild);
					dataDTB.addChild3(detail);
				}
			}

			HhDxuatKhLcntHdr createCheck = hhDxuatKhLcntHdrRepository.save(dataDTB);

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

	@ApiOperation(value = "Tra cứu đề xuất kế hoạch lựa chọn nhà thầu Gạo", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@RequestBody HhDxuatKhLcntSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit);

			Page<HhDxuatKhLcntHdr> qhKho = hhDxuatKhLcntHdrRepository
					.findAll(HhDxuatKhLcntSpecification.buildSearchQuery(objReq), pageable);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(qhKho);
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy chi tiết đề xuất kế hoạch lựa chọn nhà thầu Gạo", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID đề xuất kế hoạch lựa chọn nhà thầu Gạo", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.parseLong(ids));

			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

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

	@ApiOperation(value = "Xoá đề xuất kế hoạch lựa chọn nhà thầu Gạo", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
			Optional<HhDxuatKhLcntHdr> optional = hhDxuatKhLcntHdrRepository.findById(idSearchReq.getId());

			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			if (!optional.get().getTrangThai().equals(Contains.TAO_MOI))
				throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp");

			hhDxuatKhLcntHdrRepository.delete(optional.get());

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

	@ApiOperation(value = "Trình duyệt-01/Duyệt-02/Từ chối-03/Xoá-04 đề xuất kế hoạch lựa chọn nhà thầu Gạo", response = List.class)
	@PostMapping(value = PathContains.URL_PHE_DUYET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> updateStatus(@Valid HttpServletRequest req, @RequestBody StatusReq stReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(stReq.getId()))
				throw new Exception("Không tìm thấy dữ liệu");

			Optional<HhDxuatKhLcntHdr> optional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(stReq.getId()));
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
			hhDxuatKhLcntHdrRepository.save(optional.get());

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
}
