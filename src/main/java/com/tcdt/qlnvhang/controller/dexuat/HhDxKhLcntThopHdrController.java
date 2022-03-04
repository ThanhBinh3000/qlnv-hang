package com.tcdt.qlnvhang.controller.dexuat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

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
import com.tcdt.qlnvhang.repository.HhDxKhLcntThopHdrRepository;
import com.tcdt.qlnvhang.repository.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.HhDxKhLcntThopHdrReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntDsChuaThReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntTChiThopReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntThopSearchReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.secification.HhDxKhLcntThopSpecification;
import com.tcdt.qlnvhang.secification.HhDxuatKhLcntSpecification;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntDsgtDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntGaoDtl;
import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;
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
@RequestMapping(value = PathContains.DX_KH + PathContains.URL_THOP_DATA)
@Api(tags = "Tổng hợp đề xuất kế hoạch lựa chọn nhà thầu")
public class HhDxKhLcntThopHdrController extends BaseController {

	@Autowired
	private HhDxKhLcntThopHdrRepository hhDxKhLcntThopHdrRepository;

	@Autowired
	private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

	@ApiOperation(value = "Tổng hợp đề xuất kế hoạch lựa chọn nhà thầu Gạo", response = List.class)
	@PostMapping(value = "dx-cuc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> sumarryData(HttpServletRequest request,
			@Valid @RequestBody HhDxKhLcntTChiThopReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			List<HhDxuatKhLcntHdr> dxuatList = hhDxuatKhLcntHdrRepository
					.findAll(HhDxuatKhLcntSpecification.buildTHopQuery(objReq));

			if (dxuatList.isEmpty())
				throw new Exception("Không tìm thấy dữ liệu để tổng hợp");

			// Set thong tin hdr tong hop
			HhDxKhLcntThopHdr thopHdr = new HhDxKhLcntThopHdr();

			// Lay danh muc dung chung
			Map<String, String> mapDmuc = getMapCategory();

			thopHdr.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());
			thopHdr.setLoaiVthh(Contains.getLoaiVthh(dxuatList.get(0).getLoaiVthh()));
			thopHdr.setHthucLcnt(mapDmuc.get(dxuatList.get(0).getChildren1().get(0).getHthucLcnt()));
			thopHdr.setPthucLcnt(mapDmuc.get(dxuatList.get(0).getChildren1().get(0).getPthucLcnt()));
			thopHdr.setLoaiHdong(mapDmuc.get(dxuatList.get(0).getChildren1().get(0).getLoaiHdong()));
			thopHdr.setNguonVon(mapDmuc.get(dxuatList.get(0).getChildren1().get(0).getNguonVon()));

			// Add thong tin list dtl
			List<HhDxKhLcntThopDtl> thopDtls = new ArrayList<HhDxKhLcntThopDtl>();
			for (HhDxuatKhLcntHdr dxuat : dxuatList) {
				HhDxKhLcntThopDtl thopDtl = new HhDxKhLcntThopDtl();
				// Set ngay min va ngay max o detail Gao
				List<HhDxuatKhLcntGaoDtl> dtlsGao = dxuat.getChildren1();
				if (dtlsGao.isEmpty())
					continue;

				for (HhDxuatKhLcntGaoDtl dxuatGao : dtlsGao) {
					if (StringUtils.isEmpty(thopHdr.getTuTgianTbao())
							|| thopHdr.getTuTgianTbao().compareTo(dxuatGao.getTgianTbao()) > 0)
						thopHdr.setTuTgianTbao(dxuatGao.getTgianTbao());
					if (StringUtils.isEmpty(thopHdr.getDenTgianTbao())
							|| thopHdr.getDenTgianTbao().compareTo(dxuatGao.getTgianTbao()) < 0)
						thopHdr.setDenTgianTbao(dxuatGao.getTgianTbao());

					if (StringUtils.isEmpty(thopHdr.getTuTgianMthau())
							|| thopHdr.getTuTgianMthau().compareTo(dxuatGao.getTgianMoThau()) > 0)
						thopHdr.setTuTgianMthau(dxuatGao.getTgianMoThau());
					if (StringUtils.isEmpty(thopHdr.getDenTgianDthau())
							|| thopHdr.getDenTgianMthau().compareTo(dxuatGao.getTgianMoThau()) < 0)
						thopHdr.setDenTgianMthau(dxuatGao.getTgianMoThau());

					if (StringUtils.isEmpty(thopHdr.getTuTgianDthau())
							|| thopHdr.getTuTgianDthau().compareTo(dxuatGao.getTgianDongThau()) > 0)
						thopHdr.setTuTgianDthau(dxuatGao.getTgianDongThau());
					if (StringUtils.isEmpty(thopHdr.getDenTgianDthau())
							|| thopHdr.getDenTgianDthau().compareTo(dxuatGao.getTgianDongThau()) < 0)
						thopHdr.setDenTgianDthau(dxuatGao.getTgianDongThau());

					if (StringUtils.isEmpty(thopHdr.getTuTgianNhang())
							|| thopHdr.getTuTgianNhang().compareTo(dxuatGao.getTgianNhapHang()) > 0)
						thopHdr.setTuTgianNhang(dxuatGao.getTgianNhapHang());
					if (StringUtils.isEmpty(thopHdr.getDenTgianNhang())
							|| thopHdr.getDenTgianNhang().compareTo(dxuatGao.getTgianNhapHang()) < 0)
						thopHdr.setDenTgianNhang(dxuatGao.getTgianNhapHang());
				}

				// Set thong tin chung lay tu de xuat
				thopDtl.setIdDxHdr(dxuat.getId());
				thopDtl.setMaDvi(dxuat.getMaDvi());
				thopDtl.setTenDvi(getDviByMa(request, dxuat.getMaDvi()).getTenDvi());
				thopDtl.setSoDxuat(dxuat.getSoDxuat());
				thopDtl.setNgayDxuat(dxuat.getNgayKy());
				thopDtl.setTenDuAn(dtlsGao.get(0).getTenDuAn());

				// Add danh sach goi thau
				List<HhDxuatKhLcntDsgtDtl> dtlsGThau = dxuat.getChildren2();
				BigDecimal soLuong = BigDecimal.ZERO;
				BigDecimal tongTien = BigDecimal.ZERO;
				int soGthau = dtlsGThau.size();
				for (HhDxuatKhLcntDsgtDtl gthauDtl : dtlsGThau) {
					soLuong = soLuong.add(gthauDtl.getSoLuong());
					tongTien = tongTien.add(gthauDtl.getThanhTien());
				}
				thopDtl.setSoLuong(soLuong);
				thopDtl.setTongTien(tongTien);
				thopDtl.setSoGthau(Long.valueOf(soGthau));
				thopDtl.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());

				thopDtls.add(thopDtl);
			}

			// Quy doi don vi do luong khoi luong
			UnitScaler.reverseFormatList(thopDtls, Contains.DVT_TAN);
			thopHdr.setChildren(thopDtls);

			resp.setData(thopHdr);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tạo mới tổng hợp đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TAO_MOI, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseResponse> insert(HttpServletRequest request,
			@Valid @RequestBody HhDxKhLcntThopHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
				throw new Exception("Loại vật tư hàng hóa không phù hợp");

			List<HhDxuatKhLcntHdr> dxuatList = hhDxuatKhLcntHdrRepository
					.findAll(HhDxuatKhLcntSpecification.buildTHopQuery(objReq));

			if (dxuatList.isEmpty())
				throw new Exception("Không tìm thấy dữ liệu để tổng hợp");

			// Set thong tin hdr tong hop
			HhDxKhLcntThopHdr thopHdr = new HhDxKhLcntThopHdr();

			thopHdr.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());
			thopHdr.setLoaiVthh(dxuatList.get(0).getLoaiVthh());
			thopHdr.setHthucLcnt(dxuatList.get(0).getChildren1().get(0).getHthucLcnt());
			thopHdr.setPthucLcnt(dxuatList.get(0).getChildren1().get(0).getPthucLcnt());
			thopHdr.setLoaiHdong(dxuatList.get(0).getChildren1().get(0).getLoaiHdong());
			thopHdr.setNguonVon(dxuatList.get(0).getChildren1().get(0).getNguonVon());

			thopHdr.setNgayTao(getDateTimeNow());
			thopHdr.setNguoiTao(getUserName(request));
			thopHdr.setVeViec(objReq.getVeViec());

			// Add thong tin list dtl
			List<HhDxKhLcntThopDtl> thopDtls = new ArrayList<HhDxKhLcntThopDtl>();
			for (HhDxuatKhLcntHdr dxuat : dxuatList) {
				HhDxKhLcntThopDtl thopDtl = new HhDxKhLcntThopDtl();
				// Set ngay min va ngay max o detail Gao
				List<HhDxuatKhLcntGaoDtl> dtlsGao = dxuat.getChildren1();
				if (dtlsGao.isEmpty())
					continue;

				for (HhDxuatKhLcntGaoDtl dxuatGao : dtlsGao) {
					if (StringUtils.isEmpty(thopHdr.getTuTgianTbao())
							|| thopHdr.getTuTgianTbao().compareTo(dxuatGao.getTgianTbao()) > 0)
						thopHdr.setTuTgianTbao(dxuatGao.getTgianTbao());
					if (StringUtils.isEmpty(thopHdr.getDenTgianTbao())
							|| thopHdr.getDenTgianTbao().compareTo(dxuatGao.getTgianTbao()) < 0)
						thopHdr.setDenTgianTbao(dxuatGao.getTgianTbao());

					if (StringUtils.isEmpty(thopHdr.getTuTgianMthau())
							|| thopHdr.getTuTgianMthau().compareTo(dxuatGao.getTgianMoThau()) > 0)
						thopHdr.setTuTgianMthau(dxuatGao.getTgianMoThau());
					if (StringUtils.isEmpty(thopHdr.getDenTgianDthau())
							|| thopHdr.getDenTgianMthau().compareTo(dxuatGao.getTgianMoThau()) < 0)
						thopHdr.setDenTgianMthau(dxuatGao.getTgianMoThau());

					if (StringUtils.isEmpty(thopHdr.getTuTgianDthau())
							|| thopHdr.getTuTgianDthau().compareTo(dxuatGao.getTgianDongThau()) > 0)
						thopHdr.setTuTgianDthau(dxuatGao.getTgianDongThau());
					if (StringUtils.isEmpty(thopHdr.getDenTgianDthau())
							|| thopHdr.getDenTgianDthau().compareTo(dxuatGao.getTgianDongThau()) < 0)
						thopHdr.setDenTgianDthau(dxuatGao.getTgianDongThau());

					if (StringUtils.isEmpty(thopHdr.getTuTgianNhang())
							|| thopHdr.getTuTgianNhang().compareTo(dxuatGao.getTgianNhapHang()) > 0)
						thopHdr.setTuTgianNhang(dxuatGao.getTgianNhapHang());
					if (StringUtils.isEmpty(thopHdr.getDenTgianNhang())
							|| thopHdr.getDenTgianNhang().compareTo(dxuatGao.getTgianNhapHang()) < 0)
						thopHdr.setDenTgianNhang(dxuatGao.getTgianNhapHang());
				}

				// Set thong tin chung lay tu de xuat
				thopDtl.setIdDxHdr(dxuat.getId());
				thopDtl.setMaDvi(dxuat.getMaDvi());
				thopDtl.setTenDvi(getDviByMa(request, dxuat.getMaDvi()).getTenDvi());
				thopDtl.setSoDxuat(dxuat.getSoDxuat());
				thopDtl.setNgayDxuat(dxuat.getNgayKy());
				thopDtl.setTenDuAn(dtlsGao.get(0).getTenDuAn());

				// Add danh sach goi thau
				List<HhDxuatKhLcntDsgtDtl> dtlsGThau = dxuat.getChildren2();
				BigDecimal soLuong = BigDecimal.ZERO;
				BigDecimal tongTien = BigDecimal.ZERO;
				int soGthau = dtlsGThau.size();
				for (HhDxuatKhLcntDsgtDtl gthauDtl : dtlsGThau) {
					soLuong = soLuong.add(gthauDtl.getSoLuong());
					tongTien = tongTien.add(gthauDtl.getThanhTien());
				}
				thopDtl.setSoLuong(soLuong);
				thopDtl.setTongTien(tongTien);
				thopDtl.setSoGthau(Long.valueOf(soGthau));
				thopDtl.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());

				thopDtls.add(thopDtl);
			}
			thopHdr.setChildren(thopDtls);

			handlingCreate(Contains.TONG_HOP, thopHdr);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật tổng hợp đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_CAP_NHAT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> update(HttpServletRequest request,
			@Valid @RequestBody HhDxKhLcntThopHdrReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(objReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

			Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(Long.valueOf(objReq.getId()));
			if (!qOptional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần sửa");

			if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
				throw new Exception("Loại vật tư hàng hóa không phù hợp");

			HhDxKhLcntThopHdr dataDTB = qOptional.get();
			HhDxKhLcntThopHdr dataMap = ObjectMapperUtils.map(objReq, HhDxKhLcntThopHdr.class);

			updateObjectToObject(dataDTB, dataMap);

			HhDxKhLcntThopHdr createCheck = hhDxKhLcntThopHdrRepository.save(dataDTB);

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

	@ApiOperation(value = "Tra cứu danh sách tổng hợp đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> selectAll(@RequestBody HhDxKhLcntThopSearchReq objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit);

			Page<HhDxKhLcntThopHdr> qhKho = hhDxKhLcntThopHdrRepository
					.findAll(HhDxKhLcntThopSpecification.buildSearchQuery(objReq), pageable);

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

	@ApiOperation(value = "Lấy chi tiết đề xuất kế hoạch lựa chọn nhà thầu", response = List.class)
	@GetMapping(value = PathContains.URL_CHI_TIET + "/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> detail(
			@ApiParam(value = "ID đề xuất kế hoạch lựa chọn nhà thầu Gạo", example = "1", required = true) @PathVariable("ids") String ids) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(ids))
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(Long.parseLong(ids));

			if (!qOptional.isPresent())
				throw new UnsupportedOperationException("Không tồn tại bản ghi");

			HhDxKhLcntThopHdr hdrThop = qOptional.get();

			// Lay danh muc dung chung
			Map<String, String> mapDmuc = getMapCategory();

			hdrThop.setLoaiVthh(Contains.getLoaiVthh(hdrThop.getLoaiVthh()));
			hdrThop.setHthucLcnt(mapDmuc.get(hdrThop.getHthucLcnt()));
			hdrThop.setPthucLcnt(mapDmuc.get(hdrThop.getPthucLcnt()));
			hdrThop.setLoaiHdong(mapDmuc.get(hdrThop.getLoaiHdong()));
			hdrThop.setNguonVon(mapDmuc.get(hdrThop.getNguonVon()));

			// Quy doi don vi kg = tan
			List<HhDxKhLcntThopDtl> dtls = ObjectMapperUtils.mapAll(qOptional.get().getChildren(),
					HhDxKhLcntThopDtl.class);
			UnitScaler.formatList(dtls, Contains.DVT_TAN);
			qOptional.get().setChildren(dtls);

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

	@ApiOperation(value = "Xoá đề xuất kế hoạch lựa chọn nhà thầu", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = PathContains.URL_XOA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> delete(@Valid @RequestBody IdSearchReq idSearchReq) {
		BaseResponse resp = new BaseResponse();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

			Optional<HhDxKhLcntThopHdr> optional = hhDxKhLcntThopHdrRepository.findById(idSearchReq.getId());
			if (!optional.isPresent())
				throw new Exception("Không tìm thấy dữ liệu cần xoá");

			if (optional.get().getPhuongAn().equals(Contains.ACTIVE))
				throw new Exception("Tổng hợp đã được lập phương án trình Tổng cục, không được phép xóa");

			handlingDelete(Contains.DUYET, optional.get());

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

	@ApiOperation(value = "Danh sách đề xuất kế hoạch LCNT chưa/đã tổng hợp", response = List.class)
	@PostMapping(value = PathContains.URL_TRA_CUU + "ds-chua-th", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> findByStatus(@RequestBody @Valid HhDxKhLcntDsChuaThReq objReq,
			HttpServletRequest request) {
		BaseResponse resp = new BaseResponse();
		try {
			List<HhDxuatKhLcntHdr> dxuatList = hhDxuatKhLcntHdrRepository
					.findAll(HhDxuatKhLcntSpecification.buildDsChuaTh(objReq));

			if (dxuatList.isEmpty())
				throw new Exception("Không tìm thấy dữ liệu");

			// Add thong tin list dtl
			List<HhDxKhLcntThopDtl> thopDtls = new ArrayList<HhDxKhLcntThopDtl>();
			for (HhDxuatKhLcntHdr dxuat : dxuatList) {
				HhDxKhLcntThopDtl thopDtl = new HhDxKhLcntThopDtl();
				// Set ngay min va ngay max o detail Gao
				List<HhDxuatKhLcntGaoDtl> dtlsGao = dxuat.getChildren1();
				if (dtlsGao.isEmpty())
					continue;

				// Set thong tin chung lay tu de xuat
				thopDtl.setIdDxHdr(dxuat.getId());
				thopDtl.setMaDvi(dxuat.getMaDvi());
				thopDtl.setTenDvi(getDviByMa(request, dxuat.getMaDvi()).getTenDvi());
				thopDtl.setSoDxuat(dxuat.getSoDxuat());
				thopDtl.setNgayDxuat(dxuat.getNgayKy());
				thopDtl.setTenDuAn(dtlsGao.get(0).getTenDuAn());

				// Add danh sach goi thau
				List<HhDxuatKhLcntDsgtDtl> dtlsGThau = dxuat.getChildren2();
				BigDecimal soLuong = BigDecimal.ZERO;
				BigDecimal tongTien = BigDecimal.ZERO;
				int soGthau = dtlsGThau.size();
				for (HhDxuatKhLcntDsgtDtl gthauDtl : dtlsGThau) {
					soLuong = soLuong.add(gthauDtl.getSoLuong());
					tongTien = tongTien.add(gthauDtl.getThanhTien());
				}
				thopDtl.setSoLuong(soLuong);
				thopDtl.setTongTien(tongTien);
				thopDtl.setSoGthau(Long.valueOf(soGthau));
				thopDtl.setNamKhoach(dxuatList.get(0).getNamKhoach().toString());

				thopDtls.add(thopDtl);
			}

			// Quy doi don vi do luong khoi luong
			UnitScaler.reverseFormatList(thopDtls, Contains.DVT_TAN);

			resp.setData(thopDtls);
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
	synchronized void handlingCreate(String trangThai, HhDxKhLcntThopHdr hThopHdr) throws Exception {
		HhDxKhLcntThopHdr createCheck = hhDxKhLcntThopHdrRepository.save(hThopHdr);
		if (createCheck.getId() > 0 && createCheck.getChildren().size() > 0) {
			List<String> soDxuatList = createCheck.getChildren().stream().map(HhDxKhLcntThopDtl::getSoDxuat)
					.collect(Collectors.toList());
			hhDxuatKhLcntHdrRepository.updateTongHop(soDxuatList, trangThai);
		}
	}

	@Transactional
	synchronized void handlingDelete(String trangThai, HhDxKhLcntThopHdr hThopHdr) throws Exception {
		hhDxKhLcntThopHdrRepository.delete(hThopHdr);
		List<String> soDxuatList = hThopHdr.getChildren().stream().map(HhDxKhLcntThopDtl::getSoDxuat)
				.collect(Collectors.toList());
		hhDxuatKhLcntHdrRepository.updateTongHop(soDxuatList, trangThai);
	}
}
