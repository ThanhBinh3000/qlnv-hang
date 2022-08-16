package com.tcdt.qlnvhang.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import com.tcdt.qlnvhang.enums.*;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.HhQdGiaoNvuNhapxuatDtlLoaiNx;
import com.tcdt.qlnvhang.enums.HhQdGiaoNvuNhapxuatHdrLoaiQd;
import com.tcdt.qlnvhang.repository.HhDviThuchienQdinhRepository;
import com.tcdt.qlnvhang.repository.HhHopDongDdiemNhapKhoRepository;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatDtl1Repository;
import com.tcdt.qlnvhang.request.*;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdNhapxuat;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.request.object.HhQdGiaoNvuNhapxuatHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.secification.HhQdGiaoNvuNhapxuatSpecification;
import com.tcdt.qlnvhang.service.HhQdGiaoNvuNhapxuatService;

@Service
public class HhQdGiaoNvuNhapxuatServiceImpl extends BaseServiceImpl implements HhQdGiaoNvuNhapxuatService {
	@Autowired
	private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

	@Autowired
	private HhDviThuchienQdinhRepository hhDviThuchienQdinhRepository;

	@Autowired
	private HhHopDongRepository hhHopDongRepository;

	@Autowired
	private HhQdGiaoNvuNhapxuatDtl1Repository hhQdGiaoNvuNhapxuatDtl1Repository;

	@Autowired
	private HhHopDongDdiemNhapKhoRepository hhHopDongDdiemNhapKhoRepository;

	@Autowired
	private HttpServletRequest req;

	@Override
	public HhQdGiaoNvuNhapxuatHdr create(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception {

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");

		this.validateSoQd(null, objReq);

		Map<String, QlnvDmDonvi> mapDmucDvi = getMapDvi();
		QlnvDmDonvi qlnvDmDonvi = mapDmucDvi.get(userInfo.getDvql());
		if (qlnvDmDonvi == null)
			throw new Exception("Bad request.");

		HhQdGiaoNvuNhapxuatHdr dataMap = new HhQdGiaoNvuNhapxuatHdr();
		BeanUtils.copyProperties(objReq, dataMap, "id");

		dataMap.setNguoiTao(userInfo.getUsername());
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(DanhMucTrangThaiDcEnum.DU_THAO.getId());
		dataMap.setMaDvi(userInfo.getDvql());
		dataMap.setCapDvi(userInfo.getCapDvi());
		// add thong tin chi tiet
		List<HhQdGiaoNvuNhapxuatDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetail(), HhQdGiaoNvuNhapxuatDtl.class);
		for (HhQdGiaoNvuNhapxuatDtl dtl : dtls) {
			dtl.setTenLoaiNx(HhQdGiaoNvuNhapxuatDtlLoaiNx.getTenById(dtl.getLoaiNx()));
		}
		dataMap.setChildren(dtls);

		List<Long> hopDongIds = objReq.getHopDongIds();
		List<HhQdGiaoNvuNhapxuatDtl1> dtls1 = new ArrayList<HhQdGiaoNvuNhapxuatDtl1>();
		if (!CollectionUtils.isEmpty(hopDongIds)) {
			List<HhHopDongHdr> hhHopDongHdrs = hhHopDongRepository.findAllById(hopDongIds);
			hhHopDongHdrs.forEach(hopDong -> {
				HhQdGiaoNvuNhapxuatDtl1 dtl1 = new HhQdGiaoNvuNhapxuatDtl1();
				dtl1.setParent(dataMap);
				dtl1.setHopDong(hopDong);
				dtls1.add(dtl1);
			}) ;
		}
		dataMap.setChildren1(dtls1);

		// File dinh kem cua goi thau
		List<FileDKemJoinQdNhapxuat> dtls2 = new ArrayList<FileDKemJoinQdNhapxuat>();
		if (objReq.getFileDinhKems() != null) {
			dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdNhapxuat.class);
			dtls2.forEach(f -> {
				f.setDataType(HhQdGiaoNvuNhapxuatHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataMap.setChildren2(dtls2);

		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
		hhQdGiaoNvuNhapxuatRepository.save(dataMap);

		dataMap.setTenTrangThai(DanhMucTrangThaiDcEnum.getTenById(dataMap.getTrangThai()));
		dataMap.setTrangThaiDuyet(DanhMucTrangThaiDcEnum.getTrangThaiDuyetById(dataMap.getTrangThai()));
		this.setTenDvi(dataMap);
		return dataMap;
	}

	@Override
	public HhQdGiaoNvuNhapxuatHdr update(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");

		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhQdGiaoNvuNhapxuatHdr> qOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getId());
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		this.validateSoQd(qOptional.get(), objReq);

		HhQdGiaoNvuNhapxuatHdr dataDB = qOptional.get();
		BeanUtils.copyProperties(objReq, dataDB, "id");

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(userInfo.getUsername());
		dataDB.setId(dataDB.getId());
		// add thong tin chi tiet
		List<HhQdGiaoNvuNhapxuatDtl> dtls = ObjectMapperUtils.mapAll(objReq.getDetail(), HhQdGiaoNvuNhapxuatDtl.class);
		for (HhQdGiaoNvuNhapxuatDtl dtl : dtls) {
			dtl.setTenLoaiNx(HhQdGiaoNvuNhapxuatDtlLoaiNx.getTenById(dtl.getLoaiNx()));
		}
		dataDB.setChildren(dtls);

		List<Long> hopDongIds = objReq.getHopDongIds();
		List<HhQdGiaoNvuNhapxuatDtl1> dtls1 = new ArrayList<HhQdGiaoNvuNhapxuatDtl1>();
		if (!CollectionUtils.isEmpty(hopDongIds)) {
			List<HhHopDongHdr> hhHopDongHdrs = hhHopDongRepository.findAllById(hopDongIds);
			hhHopDongHdrs.forEach(hopDong -> {
				HhQdGiaoNvuNhapxuatDtl1 dtl1 = new HhQdGiaoNvuNhapxuatDtl1();
				dtl1.setParent(dataDB);
				dtl1.setHopDong(hopDong);
				dtls1.add(dtl1);
			}) ;
		}
		dataDB.setChildren1(dtls1);

		// File dinh kem cua goi thau
		List<FileDKemJoinQdNhapxuat> dtls2 = new ArrayList<FileDKemJoinQdNhapxuat>();
		if (objReq.getFileDinhKems() != null) {
			dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdNhapxuat.class);
			dtls2.forEach(f -> {
				f.setDataType(HhQdGiaoNvuNhapxuatHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		dataDB.setChildren2(dtls2);

		hhQdGiaoNvuNhapxuatRepository.save(dataDB);
		dataDB.setTenTrangThai(DanhMucTrangThaiDcEnum.getTenById(dataDB.getTrangThai()));
		dataDB.setTrangThaiDuyet(DanhMucTrangThaiDcEnum.getTrangThaiDuyetById(dataDB.getTrangThai()));
		this.setTenDvi(dataDB);
		return dataDB;
	}

	private void setTenDvi(HhQdGiaoNvuNhapxuatHdr data) {
		Map<String, String> mapDmucDvi = getMapTenDvi();
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
		List<HhQdGiaoNvuNhapxuatDtl> children = data.getChildren();
		children.forEach(c -> {
			String a = mapDmucDvi.get(c.getMaDvi());
			c.setTenDvi(mapDmucDvi.get(c.getMaDvi()));
		});
	}

	@Override
	public HhQdGiaoNvuNhapxuatHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhQdGiaoNvuNhapxuatHdr> qOptional = hhQdGiaoNvuNhapxuatRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		HhQdGiaoNvuNhapxuatHdr data = qOptional.get();

		data.setTenLoaiQd(HhQdGiaoNvuNhapxuatHdrLoaiQd.getTenById(data.getLoaiQd()));
		Map<String, String> mapDmucDvi = getMapTenDvi();
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));

		data.setTenTrangThai(DanhMucTrangThaiDcEnum.getTenById(data.getTrangThai()));
		data.setTrangThaiDuyet(DanhMucTrangThaiDcEnum.getTrangThaiDuyetById(data.getTrangThai()));
		this.setTenDvi(data);
		Set<Long> hopDongIds = data.getChildren1().stream().map(HhQdGiaoNvuNhapxuatDtl1::getHopDong)
				.filter(Objects::nonNull)
				.map(HhHopDongHdr::getId)
				.collect(Collectors.toSet());


		if (!CollectionUtils.isEmpty(hopDongIds)) {
			Map<Long, List<HhHopDongDdiemNhapKho>> mapDiaDiaNhapKho = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdrIn(hopDongIds)
					.stream().collect(Collectors.groupingBy(HhHopDongDdiemNhapKho::getIdHdongHdr));

			for (HhQdGiaoNvuNhapxuatDtl1 dtl1 : data.getChildren1()) {
				dtl1.getHopDong().setHhDdiemNhapKhoList(mapDiaDiaNhapKho.get(dtl1.getHopDong().getId()));
			}
		}

		return data;
	}

	@Override
	public Page<HhQdGiaoNvuNhapxuatHdr> colection(HhQdNhapxuatSearchReq objReq, HttpServletRequest req)
			throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

		Page<HhQdGiaoNvuNhapxuatHdr> dataPage = hhQdGiaoNvuNhapxuatRepository
				.findAll(HhQdGiaoNvuNhapxuatSpecification.buildSearchQuery(objReq), pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmucDvi = getMapTenDvi();
		for (HhQdGiaoNvuNhapxuatHdr hdr : dataPage.getContent()) {
			hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
			hdr.setTenLoaiQd(HhQdGiaoNvuNhapxuatHdrLoaiQd.getTenById(hdr.getLoaiQd()));
		}
		return dataPage;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public boolean updateStatus(StatusReq stReq) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null)
			throw new Exception("Bad request.");

		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");

		Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu.");

		HhQdGiaoNvuNhapxuatHdr item = optional.get();

		String trangThai = item.getTrangThai();

		if (Contains.LOAI_VTHH_VATTU.equals(item.getLoaiVthh())) {
			if (DanhMucTrangThaiDcEnum.CHO_DUYET_TP.getId().equals(stReq.getTrangThai())) {
				if (!DanhMucTrangThaiDcEnum.DU_THAO.getId().equals(trangThai))
					return false;

				item.setTrangThai(DanhMucTrangThaiDcEnum.BAN_HANH.getId());
				item.setNguoiPduyet(userInfo.getUsername());
				item.setNgayPduyet(getDateTimeNow());

			} else if (DanhMucTrangThaiDcEnum.CHO_DUYET_LDC.getId().equals(stReq.getTrangThai())) {
				if (!DanhMucTrangThaiDcEnum.CHO_DUYET_TP.getId().equals(trangThai))
					return false;

				item.setTrangThai(DanhMucTrangThaiDcEnum.CHO_DUYET_LDC.getId());
				item.setNguoiPduyet(userInfo.getUsername());
				item.setNgayPduyet(getDateTimeNow());

			} else if (DanhMucTrangThaiDcEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
				if (!DanhMucTrangThaiDcEnum.CHO_DUYET_LDC.getId().equals(trangThai))
					return false;

				item.setTrangThai(DanhMucTrangThaiDcEnum.BAN_HANH.getId());
				item.setNguoiPduyet(userInfo.getUsername());
				item.setNgayPduyet(getDateTimeNow());

			} else if (DanhMucTrangThaiDcEnum.TU_CHOI_TP.getId().equals(stReq.getTrangThai())) {
				if (!DanhMucTrangThaiDcEnum.CHO_DUYET_TP.getId().equals(trangThai))
					return false;

				item.setTrangThai(DanhMucTrangThaiDcEnum.TU_CHOI_TP.getId());
				item.setNguoiPduyet(userInfo.getUsername());
				item.setNgayPduyet(getDateTimeNow());

			} else if (DanhMucTrangThaiDcEnum.TU_CHOI_LDC.getId().equals(stReq.getTrangThai())) {
				if (!DanhMucTrangThaiDcEnum.CHO_DUYET_LDC.getId().equals(trangThai))
					return false;

				item.setTrangThai(DanhMucTrangThaiDcEnum.TU_CHOI_LDC.getId());
				item.setNguoiPduyet(userInfo.getUsername());
				item.setNgayPduyet(getDateTimeNow());

			} else {
				throw new Exception("Bad request.");
			}
		} else {
			if (DanhMucTrangThaiDcEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
				if (!DanhMucTrangThaiDcEnum.DU_THAO.getId().equals(trangThai))
					return false;

				item.setTrangThai(DanhMucTrangThaiDcEnum.BAN_HANH.getId());
				item.setNguoiPduyet(userInfo.getUsername());
				item.setNgayPduyet(getDateTimeNow());

			} else {
				throw new Exception("Bad request.");
			}
		}

		hhQdGiaoNvuNhapxuatRepository.save(item);
		return true;
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		if (!Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi()))
			throw new Exception("Bad request.");
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
			throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");

		hhQdGiaoNvuNhapxuatRepository.delete(optional.get());

	}

	@Override
	public HhQdGiaoNvuNhapxuatHdr findBySoHd(StrSearchReq strSearchReq) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HhDviThuchienQdinh dviThQdDetail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhDviThuchienQdinh> qOptional = hhDviThuchienQdinhRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		HhDviThuchienQdinh data = qOptional.get();
		Map<String, String> mapDmucDvi = getMapTenDvi();
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
		return data;
	}

	@Override
	public List<Map<String, String>> listLoaiNx() {
		List<Map<String, String>> response = new ArrayList<>();
		for (HhQdGiaoNvuNhapxuatDtlLoaiNx loaiNx : HhQdGiaoNvuNhapxuatDtlLoaiNx.values()) {
			Map<String, String> map = new HashMap<>();
			map.put("ma", loaiNx.getId());
			map.put("ten", loaiNx.getTen());
			response.add(map);
		}
		return response;
	}

	@Override
	public void exportDsQdGNvNx(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		searchReq.setPaggingReq(paggingReq);
		searchReq.setMaDvi(userInfo.getDvql());
		Page<HhQdGiaoNvuNhapxuatHdr> page = this.timKiem(searchReq);
		List<HhQdGiaoNvuNhapxuatHdr> data = page.getContent();

		String title = "Danh sách quyết định giao nhiệm vụ nhập xuất";
		String[] rowsName = new String[] { "STT", "Số QĐ", "Ngày QĐ", "Năm Nhập",
				"Trích Yếu Quyết Định", "Trạng thái"};
		String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_nhap_xuat.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhQdGiaoNvuNhapxuatHdr qd = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = qd.getSoQd();
			objs[2] = convertDateToString(qd.getNgayQdinh());
			objs[3] = qd.getNamNhap();
			objs[4] = qd.getTrichYeu();
			objs[5] = DanhMucTrangThaiDcEnum.getTenById(qd.getTrangThai());
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public Page<HhQdGiaoNvuNhapxuatHdr> timKiem(HhQdNhapxuatSearchReq req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		UserInfo userInfo = UserUtils.getUserInfo();
		String dvql = userInfo.getDvql();
		req.setMaDvi(dvql);
		String capDvi = userInfo.getCapDvi();
		if (!Contains.CAP_CHI_CUC.equalsIgnoreCase(userInfo.getCapDvi()) && !Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi())) {
			return new PageImpl<>(new ArrayList<>(), pageable, 0);
		}

		List<HhQdGiaoNvuNhapxuatHdr> qds = new ArrayList<>();
		List<Object[]> data = hhQdGiaoNvuNhapxuatRepository.search(req, capDvi);
		Set<Long> qdIds = data.stream().map(o -> (Long) o[0]).collect(Collectors.toSet());
		Map<Long, List<HhQdGiaoNvuNhapxuatDtl1>> dtl1Map = hhQdGiaoNvuNhapxuatDtl1Repository.findByParentIdIn(qdIds)
				.stream().collect(Collectors.groupingBy(HhQdGiaoNvuNhapxuatDtl1::getParentId));

		for (Object[] o : data) {
			HhQdGiaoNvuNhapxuatHdr qd = new HhQdGiaoNvuNhapxuatHdr();
			Long qdId = (Long) o[0];
			String soQdNhap = o[1] != null ? (String) o[1] : null;
			Date ngayQdinh = o[2] != null ? (Date) o[2] : null;
			Integer namNhap = o[3] != null ? (Integer) o[3] : null;
			String trichYeu = o[4] != null ? (String) o[4] : null;
			String trangThai = o[5] != null ? (String) o[5] : null;
			qd.setId(qdId);
			qd.setSoQd(soQdNhap);
			qd.setNgayQdinh(ngayQdinh);
			qd.setNamNhap(namNhap);
			qd.setTrichYeu(trichYeu);
			qd.setTrangThai(trangThai);
			qd.setTenTrangThai(DanhMucTrangThaiDcEnum.getTenById(trangThai));
			qd.setTrangThaiDuyet(DanhMucTrangThaiDcEnum.getTrangThaiDuyetById(trangThai));
			if (!CollectionUtils.isEmpty(dtl1Map.get(qd.getId()))) {
				qd.setChildren1(dtl1Map.get(qd.getId()));
			}
			qds.add(qd);
		}
		return new PageImpl<>(qds, pageable, hhQdGiaoNvuNhapxuatRepository.count(req, capDvi));
	}

	@Override
	public BaseNhapHangCount count() throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		String dvql = userInfo.getDvql();
		BaseNhapHangCount count = new BaseNhapHangCount();

		if (Contains.CAP_CUC.equalsIgnoreCase(userInfo.getCapDvi())) {
			count.setTatCa(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, null));
			count.setThoc(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, Contains.LOAI_VTHH_THOC));
			count.setGao(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, Contains.LOAI_VTHH_GAO));
			count.setVatTu(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, Contains.LOAI_VTHH_VATTU));
			count.setMuoi(hhQdGiaoNvuNhapxuatRepository.countQdCuc(dvql, Contains.LOAI_VTHH_MUOI));
		} else if (Contains.CAP_CHI_CUC.equalsIgnoreCase(userInfo.getCapDvi())){
			count.setTatCa(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, null));
			count.setThoc(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, Contains.LOAI_VTHH_THOC));
			count.setGao(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, Contains.LOAI_VTHH_GAO));
			count.setVatTu(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, Contains.LOAI_VTHH_VATTU));
			count.setMuoi(hhQdGiaoNvuNhapxuatRepository.countQdChiCuc(dvql, Contains.LOAI_VTHH_MUOI));
		}

		return count;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean deleteMultiple(DeleteReq req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();

		if (CollectionUtils.isEmpty(req.getIds()))
			return false;

		hhQdGiaoNvuNhapxuatRepository.deleteByIdIn(req.getIds());
		return true;
	}

	private void validateSoQd(HhQdGiaoNvuNhapxuatHdr update, HhQdGiaoNvuNhapxuatHdrReq req) throws Exception {
		String soQd = req.getSoQd();
		if (!StringUtils.hasText(soQd))
			return;
		if (update == null || (StringUtils.hasText(update.getSoQd()) && !update.getSoQd().equalsIgnoreCase(soQd))) {
			Optional<HhQdGiaoNvuNhapxuatHdr> optional = hhQdGiaoNvuNhapxuatRepository.findFirstBySoQd(soQd);
			Long updateId = Optional.ofNullable(update).map(HhQdGiaoNvuNhapxuatHdr::getId).orElse(null);
			if (optional.isPresent() && !optional.get().getId().equals(updateId))
				throw new Exception("Số quyết định " + soQd + " đã tồn tại");
		}
	}


}
