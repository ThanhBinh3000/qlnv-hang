package com.tcdt.qlnvhang.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.repository.HhDxuatKhLcntCcxdgDtlRepository;
import com.tcdt.qlnvhang.repository.HhDxKhlcntDsgthauCtietRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntCcxdg;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;
import com.tcdt.qlnvhang.repository.HhDxuatKhLcntDsgtDtlRepository;
import com.tcdt.qlnvhang.repository.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.secification.HhDxuatKhLcntSpecification;
import com.tcdt.qlnvhang.service.HhDxuatKhLcntHdrService;

@Service
public class HhDxuatKhLcntHdrServiceImpl extends BaseServiceImpl implements HhDxuatKhLcntHdrService {

	@Autowired
	HttpServletRequest request;

	@Autowired
	private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

	@Autowired
	private HhDxuatKhLcntDsgtDtlRepository hhDxuatKhLcntDsgtDtlRepository;

	@Autowired
	private HhDxKhlcntDsgthauCtietRepository hhDxKhlcntDsgthauCtietRepository;

	@Autowired
	private HhDxuatKhLcntCcxdgDtlRepository hhDxuatKhLcntCcxdgDtlRepository;


	Long shgtNext = new Long(0);

	@Override
	@Transactional
	public HhDxuatKhLcntHdr create(HhDxuatKhLcntHdrReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh())){
			throw new Exception("Loại vật tư hàng hóa không phù hợp");
		}

		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
		if (qOptional.isPresent()){
			throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
		}
		// Add danh sach file dinh kem o Master
		List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
		if (objReq.getFileDinhKemReq() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}
		HhDxuatKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setFileDinhKems(fileDinhKemList);
		dataMap.setLastest(false);
		hhDxuatKhLcntHdrRepository.save(dataMap);

		// Lưu danh sách gói thầu
		for (HhDxuatKhLcntDsgtDtlReq gt : objReq.getDsGtReq()){
			HhDxKhlcntDsgthau data = new ModelMapper().map(gt, HhDxKhlcntDsgthau.class);
			data.setIdDxKhlcnt(dataMap.getId());
			BigDecimal thanhTien = data.getDonGia().multiply(data.getSoLuong());
			data.setThanhTien(thanhTien);
			hhDxuatKhLcntDsgtDtlRepository.save(data);
			// Lưu chi tiết danh sách gói thaauff ( địa điểm nhập )
			for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gt.getChildren()){
				HhDxKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDxKhlcntDsgthauCtiet.class);
				dataDdNhap.setIdGoiThau(data.getId());
				dataDdNhap.setThanhTien(dataDdNhap.getDonGia().multiply(dataDdNhap.getSoLuong()));
				hhDxKhlcntDsgthauCtietRepository.save(dataDdNhap);
			}
		}
		// Lưu quyết định căn cứ
		for (HhDxuatKhLcntCcxdgDtlReq cc : objReq.getCcXdgReq()){
			HhDxuatKhLcntCcxdgDtl data = ObjectMapperUtils.map(cc, HhDxuatKhLcntCcxdgDtl.class);
			List<FileDKemJoinDxKhLcntCcxdg> detailChild = new ArrayList<>();
			if (data.getChildren() != null) {
				detailChild = ObjectMapperUtils.mapAll(data.getChildren(), FileDKemJoinDxKhLcntCcxdg.class);
				detailChild.forEach(f -> {
					f.setDataType(HhDxuatKhLcntCcxdgDtl.TABLE_NAME);
					f.setCreateDate(new Date());
				});
			}
			data.setChildren(detailChild);
			data.setIdDxKhlcnt(dataMap.getId());

			hhDxuatKhLcntCcxdgDtlRepository.save(data);
		}
		return dataMap;
	}

	@Override
	@Transactional
	public HhDxuatKhLcntHdr update(HhDxuatKhLcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		Optional<HhDxuatKhLcntHdr> deXuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
		if (deXuat.isPresent()){
			if(!deXuat.get().getId().equals(objReq.getId())){
				throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
			}
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
		if (objReq.getFileDinhKemReq() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDxuatKhLcntHdr dataDTB = qOptional.get();
		HhDxuatKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDxuatKhLcntHdr.class);

		updateObjectToObject(dataDTB, dataMap);

		dataDTB.setNgaySua(getDateTimeNow());
		dataDTB.setNguoiSua(getUser().getUsername());
		dataDTB.setFileDinhKems(fileDinhKemList);

		hhDxuatKhLcntHdrRepository.save(dataDTB);

		// Xóa tất cả các gói thầu cũ và lưu mới
		hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcnt(dataMap.getId());
		for (HhDxuatKhLcntDsgtDtlReq gt : objReq.getDsGtReq()){
			HhDxKhlcntDsgthau data = new ModelMapper().map(gt, HhDxKhlcntDsgthau.class);
			data.setId(null);
			data.setIdDxKhlcnt(dataDTB.getId());
			BigDecimal thanhTien = data.getDonGia().multiply(data.getSoLuong());
			data.setThanhTien(thanhTien);
			hhDxuatKhLcntDsgtDtlRepository.save(data);
			hhDxKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(data.getId());
			// Lưu chi tiết danh sách gói thaauff ( địa điểm nhập );
			for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gt.getChildren()){
				HhDxKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDxKhlcntDsgthauCtiet.class);
				dataDdNhap.setId(null);
				dataDdNhap.setIdGoiThau(data.getId());
				hhDxKhlcntDsgthauCtietRepository.save(dataDdNhap);
			}
		}

		// Xóa tât cả các căn cứ xác định giá cũ và lưu mới
		hhDxuatKhLcntCcxdgDtlRepository.deleteAllByIdDxKhlcnt(dataDTB.getId());
		for (HhDxuatKhLcntCcxdgDtlReq cc : objReq.getCcXdgReq()){
			HhDxuatKhLcntCcxdgDtl data = ObjectMapperUtils.map(cc, HhDxuatKhLcntCcxdgDtl.class);
			List<FileDKemJoinDxKhLcntCcxdg> detailChild = new ArrayList<>();
			if (data.getChildren() != null) {
				detailChild = ObjectMapperUtils.mapAll(data.getChildren(), FileDKemJoinDxKhLcntCcxdg.class);
				detailChild.forEach(f -> {
					f.setDataType(HhDxuatKhLcntCcxdgDtl.TABLE_NAME);
					f.setCreateDate(new Date());
				});
			}
			data.setChildren(detailChild);
			data.setIdDxKhlcnt(dataDTB.getId());

			hhDxuatKhLcntCcxdgDtlRepository.save(data);
		}

		return dataDTB;
	}

	@Override
	public HhDxuatKhLcntHdr detail(Long ids) throws Exception {

		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(ids);

		if (!qOptional.isPresent()){
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		}

		Map<String,String> mapVthh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

		qOptional.get().setTenVthh( StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
		qOptional.get().setTenCloaiVthh( StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null :mapVthh.get(qOptional.get().getCloaiVthh()));
		qOptional.get().setTenVtu( StringUtils.isEmpty(qOptional.get().getMaVtu()) ? null :mapVthh.get(qOptional.get().getMaVtu()));
		qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
		qOptional.get().setCcXdgDtlList(hhDxuatKhLcntCcxdgDtlRepository.findByIdDxKhlcnt(qOptional.get().getId()));


		List<HhDxKhlcntDsgthau> dsGthauList = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(qOptional.get().getId());
		for(HhDxKhlcntDsgthau dsG : dsGthauList){
			dsG.setTenDvi(mapDmucDvi.get(dsG.getMaDvi()));
			List<HhDxKhlcntDsgthauCtiet> listDdNhap = hhDxKhlcntDsgthauCtietRepository.findByIdGoiThau(dsG.getId());
			listDdNhap.forEach( f -> {
				f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
				f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : mapDmucDvi.get(f.getMaDiemKho()));
			});
			dsG.setChildren(listDdNhap);
		}
		qOptional.get().setDsGtDtlList(dsGthauList);
		return qOptional.get();
	}

	@Override
	public Page<HhDxuatKhLcntHdr> colection(HhDxuatKhLcntSearchReq objReq, HttpServletRequest req) throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit);

		Page<HhDxuatKhLcntHdr> qhKho = hhDxuatKhLcntHdrRepository
				.findAll(HhDxuatKhLcntSpecification.buildSearchQuery(objReq), pageable);

		// Lay danh muc dung chung
		Map<String, String> mapDmucDvi = getMapTenDvi();
		for (HhDxuatKhLcntHdr hdr : qhKho.getContent()) {
			hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
		}

		return qhKho;
	}

	@Override
	public HhDxuatKhLcntHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhDxuatKhLcntHdr> optional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu");

		String status = stReq.getTrangThai() + optional.get().getTrangThai();
		switch (status) {
			case Contains.CHO_DUYET + Contains.TU_CHOI:
			case Contains.CHO_DUYET + Contains.MOI_TAO:
			case Contains.CHO_DUYET + Contains.LANHDAO_TU_CHOI:
				optional.get().setNguoiGuiDuyet(getUser().getUsername());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI + Contains.CHO_DUYET:
			case Contains.TU_CHOI + Contains.TPHONG_DUYET:
			case Contains.LANHDAO_TU_CHOI + Contains.TPHONG_DUYET:
				optional.get().setNguoiPduyet(getUser().getUsername());
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.TPHONG_DUYET + Contains.CHO_DUYET:
			case Contains.DUYET + Contains.TPHONG_DUYET:
				optional.get().setNguoiPduyet(getUser().getUsername());
				optional.get().setNgayPduyet(getDateTimeNow());
				break;
		default:
			throw new Exception("Phê duyệt không thành công");
		}

		optional.get().setTrangThai(stReq.getTrangThai());
		if (stReq.getTrangThai().equals(Contains.DUYET)) {
			optional.get().setLastest(true);
		}
		return hhDxuatKhLcntHdrRepository.save(optional.get());
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
		Optional<HhDxuatKhLcntHdr> optional = hhDxuatKhLcntHdrRepository.findById(idSearchReq.getId());

		if (!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu cần xoá");
		}

		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI) && !optional.get().getTrangThai().equals(Contains.TU_CHOI)){
			throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
		}

		for(HhDxKhlcntDsgthau dsgThau :  hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(idSearchReq.getId())){
			hhDxKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(dsgThau.getId());
		}
		hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcnt(idSearchReq.getId());
		for(HhDxuatKhLcntCcxdgDtl cCu : hhDxuatKhLcntCcxdgDtlRepository.findByIdDxKhlcnt(idSearchReq.getId())){
			hhDxuatKhLcntCcxdgDtlRepository.deleteAllByIdDxKhlcnt(cCu.getId());
		}
		hhDxuatKhLcntHdrRepository.delete(optional.get());
	}

	@Override
	public void exportToExcel(IdSearchReq searchReq, HttpServletResponse response) throws Exception {
		// Tao form excel
		String title = "Danh sách gói thầu";
		String[] rowsName = new String[] { "STT", "Gói thầu", "Số lượng (tấn)", "Địa điểm nhập kho",
				"Đơn giá (đồng/kg)", "Thành tiền (đồng)", "Bằng chữ" };
//		List<HhDxuatKhLcntDsgtDtl> dsgtDtls = hhDxuatKhLcntDsgtDtlRepository.findByIdHdr(searchReq.getId());

		List<HhDxKhlcntDsgthau> dsgtDtls = null;

		if (dsgtDtls.isEmpty())
			throw new UnsupportedOperationException("Không tìm thấy dữ liệu");

//		String filename = "Dexuat_Danhsachgoithau_" + dsgtDtls.get(0).getParent().getSoDxuat() + ".xlsx";
		String filename = "todo";
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < dsgtDtls.size(); i++) {
			HhDxKhlcntDsgthau dsgtDtl = dsgtDtls.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = dsgtDtl.getGoiThau();
			objs[2] = dsgtDtl.getSoLuong().multiply(Contains.getDVTinh(Contains.DVT_KG))
					.divide(Contains.getDVTinh(Contains.DVT_TAN)).setScale(0, RoundingMode.HALF_UP);
//			objs[3] = dsgtDtl.getDiaDiemNhap();
			objs[4] = dsgtDtl.getDonGia();
			objs[5] = dsgtDtl.getThanhTien();
			objs[6] = MoneyConvert.doctienBangChu(dsgtDtl.getThanhTien().toString(), "");
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public void exportDsKhlcnt(HhDxuatKhLcntSearchReq searchReq, HttpServletResponse response) throws Exception {
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		searchReq.setPaggingReq(paggingReq);
		Page<HhDxuatKhLcntHdr> page = this.colection(searchReq, null);
		List<HhDxuatKhLcntHdr> data = page.getContent();

		String title = "Danh sách kế hoạch đề xuất lựa chọn nhà thầu";
		String[] rowsName = new String[] { "STT", "Số tờ trình", "Ngày đề xuất", "Trích yếu","Số QĐ giao chỉ tiêu","Năm kế hoạch","Hàng hóa","Chủng loại hàng hóa","Trạng thái của đề xuất" };
		String filename = "Danh_sach_ke_hoach_de_xuat_lua_chon_nha_thau.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhDxuatKhLcntHdr dx = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = dx.getSoDxuat();
			objs[2] = dx.getNgayGuiDuyet();
			objs[3] = dx.getTrichYeu();
			objs[4] = dx.getSoQd();
			objs[5] = dx.getNamKhoach();
			objs[6] = dx.getLoaiVthh();
			objs[7] = dx.getCloaiVthh();
			objs[8] = dx.getTrangThai();
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public Page<HhDxuatKhLcntHdr> timKiem(HttpServletRequest request,HhDxuatKhLcntSearchReq req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
		Page<HhDxuatKhLcntHdr> page = hhDxuatKhLcntHdrRepository.select(req.getNamKh(),req.getSoTr(),req.getSoQd(),convertDateToString(req.getTuNgayKy()),convertDateToString(req.getDenNgayKy()),req.getLoaiVthh(),req.getTrichYeu(),req.getTrangThai(), pageable);
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String,String> mapVthh = getListDanhMucHangHoa();
		page.getContent().forEach(f -> {
			f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
			f.setTenVthh( StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh( StringUtils.isEmpty(f.getCloaiVthh()) ? null :mapVthh.get(f.getCloaiVthh()));
			f.setMaVtu( StringUtils.isEmpty(f.getMaVtu()) ? null :mapVthh.get(f.getMaVtu()));
		});
		return page;
	}

	@Override
	@Transactional
	public HhDxuatKhLcntHdr createVatTu(HhDxuatKhLcntHdrReq objReq) throws Exception {

		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
		if (qOptional.isPresent()){
			throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
		if (objReq.getFileDinhKemReq() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDxuatKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setFileDinhKems(fileDinhKemList);
		dataMap.setLastest(false);

		hhDxuatKhLcntHdrRepository.save(dataMap);
		// Lưu danh sách gói thầu
		for (HhDxuatKhLcntDsgtDtlReq gt : objReq.getDsGtReq()){
			HhDxKhlcntDsgthau data = new ModelMapper().map(gt, HhDxKhlcntDsgthau.class);
			data.setIdDxKhlcnt(dataMap.getId());
			BigDecimal thanhTien = data.getDonGia().multiply(data.getSoLuong());
			data.setThanhTien(thanhTien);
			hhDxuatKhLcntDsgtDtlRepository.save(data);
			// Lưu chi tiết danh sách gói thaauff ( địa điểm nhập )
			for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gt.getChildren()){
				HhDxKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDxKhlcntDsgthauCtiet.class);
				dataDdNhap.setIdGoiThau(data.getId());
//				dataDdNhap.setThanhTien(dataDdNhap.getDonGia().multiply(dataDdNhap.getSoLuong()));
				hhDxKhlcntDsgthauCtietRepository.save(dataDdNhap);
			}
		}

		// Add danh sach goi thau
//		List<HhDxuatKhLcntDsgtDtl> dtls2 = ObjectMapperUtils.mapAll(objReq.getChildren2(), HhDxuatKhLcntDsgtDtl.class);
//		UnitScaler.reverseFormatList(dtls2, Contains.DVT_TAN);

//		dataMap.setChildren2(dtls2);
//

//
//		for (int i = 0; i < dataMap.getChildren2().size(); i++){
//			HhDxuatKhLcntDsgtDtlReq dsgThau = objReq.getChildren2().get(i);
//			HhDxuatKhLcntDsgtDtl dsgThauSaved =  dataMap.getChildren2().get(i);
//			for (int j = 0; j < dsgThau.getDanhSachDiaDiemNhap().size(); j++){
//				HhDxuatKhLcntVtuDtlCtiet ddNhap = ObjectMapperUtils.map(dsgThau.getDanhSachDiaDiemNhap().get(j), HhDxuatKhLcntVtuDtlCtiet.class);
//				ddNhap.setIdGoiThau(dsgThauSaved.getId());
//				hhDxuatKhLcntVtuDtlCtietRepository.save(ddNhap);
//			}
//		}
		return dataMap;
	}

	@Override
	@Transactional
	public HhDxuatKhLcntHdr updateVatTu(HhDxuatKhLcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		Optional<HhDxuatKhLcntHdr> deXuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
		if (deXuat.isPresent()){
			if(!deXuat.get().getId().equals(objReq.getId())){
				throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
			}
		}

		List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
		if (objReq.getFileDinhKemReq() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDxuatKhLcntHdr dataDTB = qOptional.get();
		HhDxuatKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDxuatKhLcntHdr.class);

		updateObjectToObject(dataDTB, dataMap);
//
		dataDTB.setNgaySua(getDateTimeNow());
		dataDTB.setNguoiSua(getUser().getUsername());
		dataDTB.setFileDinhKems(fileDinhKemList);

		hhDxuatKhLcntHdrRepository.save(dataDTB);
		hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcnt(dataDTB.getId());
		// Lưu danh sách gói thầu
		for (HhDxuatKhLcntDsgtDtlReq gt : objReq.getDsGtReq()){
			HhDxKhlcntDsgthau data = new ModelMapper().map(gt, HhDxKhlcntDsgthau.class);
			hhDxKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gt.getId());
			data.setId(null);
			data.setIdDxKhlcnt(dataDTB.getId());
			BigDecimal thanhTien = data.getDonGia().multiply(data.getSoLuong());
			data.setThanhTien(thanhTien);
			hhDxuatKhLcntDsgtDtlRepository.save(data);
			// Lưu chi tiết danh sách gói thaauff ( địa điểm nhập )
			for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gt.getChildren()){
				HhDxKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDxKhlcntDsgthauCtiet.class);
				dataDdNhap.setId(null);
				dataDdNhap.setIdGoiThau(data.getId());
				hhDxKhlcntDsgthauCtietRepository.save(dataDdNhap);
			}
		}
		return dataDTB;
	}

	@Override
	public HhDxuatKhLcntHdr detailVatTu(String ids) throws Exception {
		if (StringUtils.isEmpty(ids)){
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		}
		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.parseLong(ids));

		if (!qOptional.isPresent()){
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		}

		Map<String,String> mapVthh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

		qOptional.get().setTenVthh( StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
		qOptional.get().setTenCloaiVthh( StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null :mapVthh.get(qOptional.get().getCloaiVthh()));
		qOptional.get().setTenVtu( StringUtils.isEmpty(qOptional.get().getMaVtu()) ? null :mapVthh.get(qOptional.get().getMaVtu()));
		qOptional.get().setTenDvi(StringUtils.isEmpty(qOptional.get().getMaDvi())? null : mapDmucDvi.get(qOptional.get().getMaDvi()));

		// Quy doi don vi kg = tan

//		UnitScaler.formatList(dtls2, Contains.DVT_TAN);


		List<HhDxKhlcntDsgthau> dsGthauList = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(qOptional.get().getId());
		for(HhDxKhlcntDsgthau dsG : dsGthauList){
			dsG.setTenDvi(mapDmucDvi.get(dsG.getMaDvi()));
			dsG.setTenHthucLcnt( StringUtils.isEmpty(dsG.getHthucLcnt()) ? null : hashMapHtLcnt.get(dsG.getHthucLcnt()));
			dsG.setTenPthucLcnt( StringUtils.isEmpty(dsG.getPthucLcnt()) ? null :hashMapPthucDthau.get(dsG.getPthucLcnt()));
			dsG.setTenLoaiHdong( StringUtils.isEmpty(dsG.getLoaiHdong()) ? null :hashMapLoaiHdong.get(dsG.getLoaiHdong()));
			dsG.setTenNguonVon( StringUtils.isEmpty(dsG.getNguonVon()) ? null :hashMapNguonVon.get(dsG.getNguonVon()));
			List<HhDxKhlcntDsgthauCtiet> listDdNhap = hhDxKhlcntDsgthauCtietRepository.findByIdGoiThau(dsG.getId());
			for(int i = 0;i < listDdNhap.size();i++){
				listDdNhap.get(i).setTenDvi(StringUtils.isEmpty(listDdNhap.get(i).getMaDvi())? null : mapDmucDvi.get(listDdNhap.get(i).getMaDvi()));
				listDdNhap.get(i).setTenDiemKho(StringUtils.isEmpty(listDdNhap.get(i).getMaDiemKho())? null : mapDmucDvi.get(listDdNhap.get(i).getMaDiemKho()));
			}
			dsG.setChildren(listDdNhap);
		}
		qOptional.get().setDsGtDtlList(dsGthauList);

		return qOptional.get();
	}

	@Override
	public HhDxuatKhLcntHdr approveVatTu(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		Optional<HhDxuatKhLcntHdr> optional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu");

		String status = stReq.getTrangThai() + optional.get().getTrangThai();
		switch (status) {
			case Contains.CHO_DUYET + Contains.TU_CHOI:
			case Contains.CHO_DUYET + Contains.MOI_TAO:
				optional.get().setNguoiGuiDuyet(getUser().getUsername());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI + Contains.CHO_DUYET:
				optional.get().setNguoiPduyet(getUser().getUsername());
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.DUYET + Contains.CHO_DUYET:
				optional.get().setNguoiPduyet(getUser().getUsername());
				optional.get().setNgayPduyet(getDateTimeNow());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}

		optional.get().setTrangThai(stReq.getTrangThai());
		if (stReq.getTrangThai().equals(Contains.DUYET)) {
			optional.get().setLastest(true);
		}
		return hhDxuatKhLcntHdrRepository.save(optional.get());
	}
}
