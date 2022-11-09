package com.tcdt.qlnvhang.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhDxuatKhLcntCcxdgDtlRepository;
import com.tcdt.qlnvhang.repository.HhDxKhlcntDsgthauCtietRepository;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
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
import org.springframework.util.ObjectUtils;
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
		if(!StringUtils.isEmpty(objReq.getSoDxuat())){
			Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
			if (qOptional.isPresent()){
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
		HhDxuatKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.DUTHAO);
		dataMap.setTrangThaiTh(Contains.CHUATONGHOP);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setFileDinhKems(fileDinhKemList);
		this.validateData(dataMap,dataMap.getTrangThai());
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

	public void validateData(HhDxuatKhLcntHdr objHdr,String trangThai) throws Exception {
		if(trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())){
			HhDxuatKhLcntHdr dXuat = hhDxuatKhLcntHdrRepository.findAllByLoaiVthhAndCloaiVthhAndNamKhoachAndMaDviAndTrangThaiNot(objHdr.getLoaiVthh(), objHdr.getCloaiVthh(), objHdr.getNamKhoach(), objHdr.getMaDvi(),NhapXuatHangTrangThaiEnum.DUTHAO.getId());
			if(!ObjectUtils.isEmpty(dXuat) && !dXuat.getId().equals(objHdr.getId())){
				throw new Exception("Chủng loại hàng hóa đã được tạo và gửi duyệt, xin vui lòng chọn lại chủng loại hàng hóa khác");
			}
		}
		if(trangThai.equals(NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId())) {
			for(HhDxKhlcntDsgthau chiCuc : objHdr.getDsGtDtlList()){
				BigDecimal aLong = hhDxuatKhLcntHdrRepository.countSLDalenKh(objHdr.getNamKhoach(), objHdr.getLoaiVthh(), chiCuc.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
				BigDecimal soLuongTotal = aLong.add(chiCuc.getSoLuong());
				if(soLuongTotal.compareTo(chiCuc.getSoLuongChiTieu()) > 0){
					throw new Exception(chiCuc.getTenDvi() + " đã nhập quá số lượng chi tiêu, vui lòng nhập lại");
				}
			}
		}
	}

	@Override
	@Transactional
	public HhDxuatKhLcntHdr update(HhDxuatKhLcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId()))
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

		Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
		if (!qOptional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần sửa");

		if(!StringUtils.isEmpty(objReq.getSoDxuat())) {
			Optional<HhDxuatKhLcntHdr> deXuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
			if (deXuat.isPresent()){
				if(!deXuat.get().getId().equals(objReq.getId())){
					throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
				}
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

		qOptional.get().setTenLoaiVthh( StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
		qOptional.get().setTenCloaiVthh( StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null :mapVthh.get(qOptional.get().getCloaiVthh()));
		qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
		qOptional.get().setCcXdgDtlList(hhDxuatKhLcntCcxdgDtlRepository.findByIdDxKhlcnt(qOptional.get().getId()));
		qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));

		List<HhDxKhlcntDsgthau> dsGthauList = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(qOptional.get().getId());
		for(HhDxKhlcntDsgthau dsG : dsGthauList){
			dsG.setTenDvi(mapDmucDvi.get(dsG.getMaDvi()));
			dsG.setTenCloaiVthh(mapVthh.get(dsG.getCloaiVthh()));

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
	@Transactional
	public HhDxuatKhLcntHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");

		HhDxuatKhLcntHdr optional = this.detail(stReq.getId());

		if(optional.getLoaiVthh().startsWith("02")){
			String status = stReq.getTrangThai() + optional.getTrangThai();
			switch (status) {
				case Contains.CHODUYET_LDV + Contains.DUTHAO:
				case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
					optional.setNguoiGuiDuyet(getUser().getUsername());
					optional.setNgayGuiDuyet(getDateTimeNow());
					break;
				case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
					optional.setNguoiPduyet(getUser().getUsername());
					optional.setNgayPduyet(getDateTimeNow());
					optional.setLdoTuchoi(stReq.getLyDo());
					break;
				case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
					optional.setNguoiPduyet(getUser().getUsername());
					optional.setNgayPduyet(getDateTimeNow());
					break;
				default:
					throw new Exception("Phê duyệt không thành công");
			}
		}else{
			String status = stReq.getTrangThai() + optional.getTrangThai();
			switch (status) {
				case Contains.CHODUYET_TP + Contains.DUTHAO:
				case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
				case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
					this.validateData(optional,Contains.CHODUYET_TP);
					optional.setNguoiGuiDuyet(getUser().getUsername());
					optional.setNgayGuiDuyet(getDateTimeNow());
				case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
				case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
					optional.setNguoiPduyet(getUser().getUsername());
					optional.setNgayPduyet(getDateTimeNow());
					optional.setLdoTuchoi(stReq.getLyDo());
					break;
				case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
				case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
					this.validateData(optional,stReq.getTrangThai());
					optional.setNguoiPduyet(getUser().getUsername());
					optional.setNgayPduyet(getDateTimeNow());
					break;
				default:
					throw new Exception("Phê duyệt không thành công");
			}
		}

		optional.setTrangThai(stReq.getTrangThai());
		return hhDxuatKhLcntHdrRepository.save(optional);
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
		Optional<HhDxuatKhLcntHdr> optional = hhDxuatKhLcntHdrRepository.findById(idSearchReq.getId());

		if (!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu cần xoá");
		}
		if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
				&& !optional.get().getTrangThai().equals(Contains.TUCHOI_LDV)
				&& !optional.get().getTrangThai().equals(Contains.TUCHOI_TP)
				&& !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
			throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
		}
		List<HhDxKhlcntDsgthau> goiThau = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(idSearchReq.getId());
		if(goiThau != null && goiThau.size() > 0){
			for (HhDxKhlcntDsgthau ct : goiThau) {
				List<HhDxKhlcntDsgthauCtiet> dsCtiet=hhDxKhlcntDsgthauCtietRepository.findByIdGoiThau(ct.getId());
				hhDxKhlcntDsgthauCtietRepository.deleteAll(dsCtiet);
			}
			hhDxuatKhLcntDsgtDtlRepository.deleteAll(goiThau);
		}
		hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcnt(idSearchReq.getId());
		List<HhDxuatKhLcntCcxdgDtl> cc=hhDxuatKhLcntCcxdgDtlRepository.findByIdDxKhlcnt(idSearchReq.getId());
		if (cc != null && cc.size() > 0){
			hhDxuatKhLcntCcxdgDtlRepository.deleteAll(cc);
		}
		hhDxuatKhLcntHdrRepository.delete(optional.get());
	}

	@Override
	public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getIdList()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
		List<HhDxuatKhLcntHdr> listDx = hhDxuatKhLcntHdrRepository.findByIdIn(idSearchReq.getIdList());
		if (listDx.isEmpty()){
			throw new Exception("Không tìm thấy dữ liệu cần xoá");
		}
		for (HhDxuatKhLcntHdr dx:listDx) {
			if (!dx.getTrangThai().equals(Contains.DUTHAO)
					&& !dx.getTrangThai().equals(Contains.TUCHOI_LDV)
					&& !dx.getTrangThai().equals(Contains.TUCHOI_TP)
					&& !dx.getTrangThai().equals(Contains.TUCHOI_LDC)){
				throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
			}
			List<HhDxKhlcntDsgthau> goiThau = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(dx.getId());
			List<Long> listGoiThau = goiThau.stream().map(HhDxKhlcntDsgthau::getId).collect(Collectors.toList());
			hhDxKhlcntDsgthauCtietRepository.deleteAllByIdGoiThauIn(listGoiThau);
		}
		hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcntIn(idSearchReq.getIdList());
		hhDxuatKhLcntCcxdgDtlRepository.deleteAllByIdDxKhlcntIn(idSearchReq.getIdList());
		hhDxuatKhLcntHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());

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
	public void exportDsKhlcnt( HhDxuatKhLcntSearchReq req,HttpServletResponse response) throws Exception {
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		req.setPaggingReq(paggingReq);
		Page<HhDxuatKhLcntHdr> page = this.timKiem(req);
		List<HhDxuatKhLcntHdr> data = page.getContent();

		String title = "Danh sách kế hoạch đề xuất lựa chọn nhà thầu";
		String[] rowsName = new String[] { "STT", "Số tờ trình", "Ngày đề xuất", "Trích yếu","Số QĐ giao chỉ tiêu","Năm kế hoạch","Hàng hóa","Chủng loại hàng hóa","Số gói thầu","Trạng thái của đề xuất" };
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
			objs[8] = dx.getSoGoiThau();
			objs[9] = dx.getTrangThai();
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}
	@Override
	public void exportList(HhDxuatKhLcntSearchReq objReq,HttpServletResponse response) throws Exception {

		String cDvi = getUser().getCapDvi();
		if(Contains.CAP_TONG_CUC.equals(cDvi)){
			this.exportToExcelTongCuc(objReq,response);
		}else{
			this.exportToExcelCuc(objReq,response);
		}
	}
	public void exportToExcelTongCuc(HhDxuatKhLcntSearchReq objReq,HttpServletResponse response) throws Exception {
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		objReq.setPaggingReq(paggingReq);
		Page<HhDxuatKhLcntHdr> page = this.timKiem(objReq);
		List<HhDxuatKhLcntHdr> data = page.getContent();

		String title = "Danh sách kế hoạch đề xuất lựa chọn nhà thầu";
		String[] rowsName = new String[] { "STT", "Số tờ trình", "Đơn vị", "Ngày đề xuất","Trích yếu","Số QĐ giao chỉ tiêu","Năm kế hoạch","Hàng hóa","Số gói thầu","Trạng thái" };
		String filename = "Danh_sach_ke_hoach_de_xuat_lua_chon_nha_thau_tong_cuc.xlsx";

		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhDxuatKhLcntHdr dx = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = dx.getSoDxuat();
			objs[2] = dx.getTenDvi();
			objs[3] = dx.getNgayGuiDuyet();
			objs[4] = dx.getTrichYeu();
			objs[5] = dx.getSoQd();
			objs[6] = dx.getNamKhoach();
			objs[7] = dx.getTenLoaiVthh();
			objs[8] = dx.getSoGoiThau();
			objs[9] = dx.getTenTrangThai();
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	public void exportToExcelCuc(HhDxuatKhLcntSearchReq objReq,HttpServletResponse response) throws Exception {

		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		objReq.setPaggingReq(paggingReq);
		Page<HhDxuatKhLcntHdr> page = this.timKiem(objReq);
		List<HhDxuatKhLcntHdr> data = page.getContent();

		String title = "Danh sách kế hoạch đề xuất lựa chọn nhà thầu";
		String[] rowsName = new String[] { "STT", "Số tờ trình", "Ngày ký", "Trích yếu","Số QĐ giao chỉ tiêu","Năm kế hoạch","Hàng hóa","Chủng loại hàng hóa","Số gói thầu","Trạng thái" };
		String filename = "Danh_sach_ke_hoach_de_xuat_lua_chon_nha_thau_cuc.xlsx";

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
			objs[8] = dx.getSoGoiThau();
			objs[9] = dx.getTenTrangThai();
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	@Override
	public Page<HhDxuatKhLcntHdr> timKiem(HhDxuatKhLcntSearchReq req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
		Page<HhDxuatKhLcntHdr> page = hhDxuatKhLcntHdrRepository.select(req.getNamKh(),req.getSoTr(),req.getSoQd(),convertDateToString(req.getTuNgayKy()),convertDateToString(req.getDenNgayKy()),req.getLoaiVthh(),req.getTrichYeu(),req.getTrangThai(),req.getTrangThaiTh(),req.getMaDvi(), pageable);
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String,String> mapVthh = getListDanhMucHangHoa();
		List<Long> ids = page.getContent().stream().map(HhDxuatKhLcntHdr::getId).collect(Collectors.toList());
		List<Object[]> listGthau = hhDxuatKhLcntHdrRepository.getQdPdKhLcnt(ids,NhapXuatHangTrangThaiEnum.THANH_CONG.getId());
		Map<Long,Map<String,String>> hhDxKhlcnt = new HashMap<>();
		for (Object[] it: listGthau) {
			Map<String,String> dataQd = new HashMap<>();
			dataQd.put("idQdPdKqLcnt",it[1].toString());
			dataQd.put("soQdPdKqLcnt",it[2].toString());
			dataQd.put("soGthauTrung",it[3].toString());
			hhDxKhlcnt.put(Long.parseLong(it[0].toString()),dataQd);
		}
		page.getContent().forEach(f -> {
			f.setSoGoiThau ( hhDxuatKhLcntDsgtDtlRepository.countByIdDxKhlcnt(f.getId()));
			f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
			f.setTenLoaiVthh( StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh( StringUtils.isEmpty(f.getCloaiVthh()) ? null :mapVthh.get(f.getCloaiVthh()));
			f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
			f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
			Map<String, String> stringStringMap = hhDxKhlcnt.get(f.getId());
			if(!ObjectUtils.isEmpty(stringStringMap)){
				f.setIdQdPdKqLcnt(Long.parseLong(stringStringMap.get("idQdPdKqLcnt")));
				f.setSoQdPdKqLcnt(stringStringMap.get("soQdPdKqLcnt"));
				f.setSoGthauTrung(Integer.valueOf(stringStringMap.get("soGthauTrung")));
			}
		});
		return page;
	}

	@Override
	@Transactional
	public HhDxuatKhLcntHdr createVatTu(HhDxuatKhLcntHdrReq objReq) throws Exception {

		if(!StringUtils.isEmpty(objReq.getSoDxuat())) {
			Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
			if (qOptional.isPresent()){
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

		HhDxuatKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setFileDinhKems(fileDinhKemList);
		dataMap.setTrangThaiTh(Contains.CHUATAO_QD);

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

		if(!StringUtils.isEmpty(objReq.getSoDxuat())){
			Optional<HhDxuatKhLcntHdr> deXuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
			if (deXuat.isPresent()){
				if(!deXuat.get().getId().equals(objReq.getId())){
					throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
				}
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

		qOptional.get().setTenLoaiVthh( StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
		qOptional.get().setTenCloaiVthh( StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null :mapVthh.get(qOptional.get().getCloaiVthh()));
		qOptional.get().setTenDvi(StringUtils.isEmpty(qOptional.get().getMaDvi())? null : mapDmucDvi.get(qOptional.get().getMaDvi()));
		qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));

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
	public Page<HhDxuatKhLcntHdr> selectDropdown(HhDxuatKhLcntSearchReq req) {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
		Page<HhDxuatKhLcntHdr> page = hhDxuatKhLcntHdrRepository.selectDropdown(req.getNamKh(),req.getMaDvi(),req.getListTrangThai(),req.getListTrangThaiTh(),pageable);
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String,String> mapVthh = getListDanhMucHangHoa();
		page.getContent().forEach(f -> {
			f.setSoGoiThau ( hhDxuatKhLcntDsgtDtlRepository.countByIdDxKhlcnt(f.getId()));
			f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
			f.setTenLoaiVthh( StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh( StringUtils.isEmpty(f.getCloaiVthh()) ? null :mapVthh.get(f.getCloaiVthh()));
			f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
			f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
		});
		return page;
	}

	@Override
	public BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) throws Exception {
		return hhDxuatKhLcntHdrRepository.countSLDalenKh(req.getYear(),req.getLoaiVthh(), req.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
	}
}
