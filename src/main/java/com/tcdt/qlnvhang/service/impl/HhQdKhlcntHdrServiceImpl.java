package com.tcdt.qlnvhang.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntDsgthauDtlCtietReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntDsgthauReq;
import com.tcdt.qlnvhang.service.feign.KeHoachService;
import com.tcdt.qlnvhang.table.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinQdKhlcntHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntDtlReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.service.HhQdKhlcntHdrService;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;

@Service
public class HhQdKhlcntHdrServiceImpl extends BaseServiceImpl implements HhQdKhlcntHdrService {

	@Autowired
	private HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

	@Autowired
	private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

	@Autowired
	private HhQdKhlcntDtlRepository hhQdKhlcntDtlRepository;

	@Autowired
	private HhQdKhlcntDsgthauRepository hhQdKhlcntDsgthauRepository;

	@Autowired
	private HhQdKhlcntDsgthauCtietRepository hhQdKhlcntDsgthauCtietRepository;

	@Autowired
	private HhDxKhLcntThopHdrRepository hhDxKhLcntThopHdrRepository;

	@Autowired
	private KeHoachService keHoachService;

	@Override
	@Transactional
	public HhQdKhlcntHdr create(HhQdKhlcntHdrReq objReq) throws Exception {
		// Vật tư
		if(objReq.getLoaiVthh().startsWith("02")){
			return createVatTu(objReq);
		}else{
		// Lương thực
			return createLuongThuc(objReq);
		}
	}

	public HhQdKhlcntHdr createLuongThuc(HhQdKhlcntHdrReq objReq) throws Exception {
		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
			throw new Exception("Loại vật tư hàng hóa không phù hợp");

		if(!StringUtils.isEmpty(objReq.getSoQd())){
			List<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
			if (!checkSoQd.isEmpty()) {
				throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
			}
		}

		if(objReq.getPhanLoai().equals("TH")){
			Optional<HhDxKhLcntThopHdr> qOptionalTh = hhDxKhLcntThopHdrRepository.findById(objReq.getIdThHdr());
			if (!qOptionalTh.isPresent()){
				throw new Exception("Không tìm thấy tổng hợp kế hoạch lựa chọn nhà thầu");
			}
		}else{
			Optional<HhDxuatKhLcntHdr> byId = hhDxuatKhLcntHdrRepository.findById(objReq.getIdTrHdr());
			if(!byId.isPresent()){
				throw new Exception("Không tìm thấy đề xuất kế hoạch lựa chọn nhà thầu");
			}
		}
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
		dataMap.setTrangThai(Contains.DUTHAO);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setChildren(fileDinhKemList);
		dataMap.setLastest(objReq.getLastest());
		hhQdKhlcntHdrRepository.save(dataMap);

		// Update trạng thái tổng hợp dxkhclnt
		if(objReq.getPhanLoai().equals("TH")){
			hhDxKhLcntThopHdrRepository.updateTrangThai(dataMap.getIdThHdr(), Contains.DADUTHAO_QD);
		}else{
			hhDxuatKhLcntHdrRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);
		}

		for (HhQdKhlcntDtlReq dx : objReq.getDsDeXuat()){
			HhQdKhlcntDtl qd = ObjectMapperUtils.map(dx, HhQdKhlcntDtl.class);
			qd.setId(null);
			qd.setIdQdHdr(dataMap.getId());
			qd.setTrangThai(Contains.CHUACAPNHAT);
			hhQdKhlcntDtlRepository.save(qd);
			for (HhQdKhlcntDsgthauReq gtList : ObjectUtils.isEmpty(dx.getDsGoiThau()) ? dx.getDsGtDtlList() : dx.getDsGoiThau()){
				HhQdKhlcntDsgthau gt = ObjectMapperUtils.map(gtList, HhQdKhlcntDsgthau.class);
				gt.setId(null);
				gt.setIdQdDtl(qd.getId());
				gt.setThanhTien(gt.getDonGia().multiply(gt.getSoLuong()));
				gt.setTrangThai(Contains.CHUACAPNHAT);
				hhQdKhlcntDsgthauRepository.save(gt);
				for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gtList.getChildren()){
					HhQdKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhQdKhlcntDsgthauCtiet.class);
					dataDdNhap.setId(null);
					dataDdNhap.setIdGoiThau(gt.getId());
					dataDdNhap.setThanhTien(dataDdNhap.getDonGia().multiply(dataDdNhap.getSoLuong()));
					hhQdKhlcntDsgthauCtietRepository.save(dataDdNhap);
				}
			}
		}
		return dataMap;
	}

	private HhQdKhlcntHdr createVatTu(HhQdKhlcntHdrReq objReq) throws Exception {
		List<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
		if (!checkSoQd.isEmpty()) {
			throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
		}

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
		dataMap.setTrangThai(Contains.DUTHAO);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setChildren(fileDinhKemList);
		dataMap.setLastest(objReq.getLastest());
		hhQdKhlcntHdrRepository.save(dataMap);

		HhQdKhlcntDtl qdDtl = new HhQdKhlcntDtl();
		qdDtl.setId(null);
		qdDtl.setIdQdHdr(dataMap.getId());
		qdDtl.setMaDvi(getUser().getDvql());
		qdDtl.setTrangThai(Contains.CHUACAPNHAT);

		hhQdKhlcntDtlRepository.save(qdDtl);

		// Update trạng thái tờ trình
		hhDxuatKhLcntHdrRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);

//		 .updateTrangThai(dataMap.getIdThHdr(), Contains.DADUTHAO_QD);

		if (objReq.getDsGoiThau() != null && objReq.getDsGoiThau().size() > 0) {
			for (HhQdKhlcntDsgthauReq dsgThau : objReq.getDsGoiThau()){
				HhQdKhlcntDsgthau gThau = ObjectMapperUtils.map(dsgThau, HhQdKhlcntDsgthau.class);
				gThau.setId(null);
				gThau.setIdQdDtl(qdDtl.getId());
				gThau.setTrangThai(Contains.CHUACAPNHAT);
				hhQdKhlcntDsgthauRepository.save(gThau);
				for (HhDxuatKhLcntDsgthauDtlCtietReq dsDdNhap : dsgThau.getChildren()){
					HhQdKhlcntDsgthauCtiet ddNhap = ObjectMapperUtils.map(dsDdNhap, HhQdKhlcntDsgthauCtiet.class);
					ddNhap.setId(null);
					ddNhap.setIdGoiThau(gThau.getId());
					hhQdKhlcntDsgthauCtietRepository.save(ddNhap);
				}
			}
		}
		return dataMap;
	}

	public void validateData(HhQdKhlcntHdr objHdr) throws Exception {
		for(HhQdKhlcntDtl dtl : objHdr.getHhQdKhlcntDtlList()){
			for(HhQdKhlcntDsgthau dsgthau : dtl.getDsGoiThau()){
				BigDecimal aLong = hhDxuatKhLcntHdrRepository.countSLDalenKh(objHdr.getNamKhoach(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
				BigDecimal soLuongTotal = aLong.add(dsgthau.getSoLuong());
				BigDecimal nhap = keHoachService.getChiTieuNhapXuat(objHdr.getNamKhoach(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(), "NHAP");
				if(soLuongTotal.compareTo(nhap) > 0){
					throw new Exception(dsgthau.getTenDvi() + " đã nhập quá số lượng chi tiêu, vui lòng nhập lại");
				}
			}
		}
	}

	@Override
	@Transactional
	public HhQdKhlcntHdr update(HhQdKhlcntHdrReq objReq) throws Exception {
		// Vật tư
		if(objReq.getLoaiVthh().startsWith("02")){
			return updateVatTu(objReq);
		}else{
			// Lương thực
			return updateLuongThuc(objReq);
		}
	}

	@Transactional
	public HhQdKhlcntHdr updateLuongThuc(HhQdKhlcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId())){
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
		}

		Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(objReq.getId());
		if (!qOptional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu cần sửa");
		}

		if(!StringUtils.isEmpty(objReq.getSoQd())){
			if (!objReq.getSoQd().equals(qOptional.get().getSoQd())) {
				List<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
				if (!checkSoQd.isEmpty()) {
					throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
				}
			}
		}

		if(objReq.getPhanLoai().equals("TH")){
			Optional<HhDxKhLcntThopHdr> qOptionalTh = hhDxKhLcntThopHdrRepository.findById(objReq.getIdThHdr());
			if (!qOptionalTh.isPresent()){
				throw new Exception("Không tìm thấy tổng hợp kế hoạch lựa chọn nhà thầu");
			}
		}else{
			Optional<HhDxuatKhLcntHdr> byId = hhDxuatKhLcntHdrRepository.findById(objReq.getIdTrHdr());
			if(!byId.isPresent()){
				throw new Exception("Không tìm thấy đề xuất kế hoạch lựa chọn nhà thầu");
			}
		}

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
		dataDB.setNguoiSua(getUser().getUsername());
		dataDB.setChildren(fileDinhKemList);

		hhQdKhlcntHdrRepository.save(dataDB);

		hhQdKhlcntDtlRepository.deleteAllByIdQdHdr(dataDB.getId());
		for (HhQdKhlcntDtlReq dx : objReq.getDsDeXuat()){
			HhQdKhlcntDtl qd = ObjectMapperUtils.map(dx, HhQdKhlcntDtl.class);
			hhQdKhlcntDsgthauRepository.deleteByIdQdDtl(qd.getId());
			qd.setId(null);
			qd.setIdQdHdr(dataDB.getId());
			qd.setTrangThai(Contains.CHUACAPNHAT);
			hhQdKhlcntDtlRepository.save(qd);
			for (HhQdKhlcntDsgthauReq gtList : ObjectUtils.isEmpty(dx.getDsGoiThau()) ? dx.getDsGtDtlList() : dx.getDsGoiThau()){
				HhQdKhlcntDsgthau gt = ObjectMapperUtils.map(gtList, HhQdKhlcntDsgthau.class);
				hhQdKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gt.getId());
				gt.setId(null);
				gt.setIdQdDtl(qd.getId());
				gt.setThanhTien(gt.getDonGia().multiply(gt.getSoLuong()));
				gt.setTrangThai(Contains.CHUACAPNHAT);
				hhQdKhlcntDsgthauRepository.save(gt);
				for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gtList.getChildren()){
					HhQdKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhQdKhlcntDsgthauCtiet.class);
					dataDdNhap.setId(null);
					dataDdNhap.setIdGoiThau(gt.getId());
					dataDdNhap.setThanhTien(dataDdNhap.getDonGia().multiply(dataDdNhap.getSoLuong()));
					hhQdKhlcntDsgthauCtietRepository.save(dataDdNhap);
				}
			}
		}

		return dataDB;
	}

	@Transactional
	public HhQdKhlcntHdr updateVatTu(HhQdKhlcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId())){
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
		}

		Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(objReq.getId());
		if (!qOptional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu cần sửa");
		}

		if (!qOptional.get().getSoQd().equals(objReq.getSoQd())) {
			List<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
			if (!checkSoQd.isEmpty()) {
				throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
			}
		}

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
		dataDB.setNguoiSua(getUser().getUsername());
		dataDB.setChildren(fileDinhKemList);

		hhQdKhlcntHdrRepository.save(dataDB);

		hhQdKhlcntDtlRepository.deleteAllByIdQdHdr(dataMap.getId());
		HhQdKhlcntDtl qdDtl = new HhQdKhlcntDtl();
		qdDtl.setId(null);
		qdDtl.setIdQdHdr(dataDB.getId());
		qdDtl.setMaDvi(getUser().getDvql());
		qdDtl.setTrangThai(Contains.CHUACAPNHAT);
		hhQdKhlcntDtlRepository.save(qdDtl);

		if (objReq.getDsGoiThau() != null && objReq.getDsGoiThau().size() > 0) {
			for (HhQdKhlcntDsgthauReq dsgThau : objReq.getDsGoiThau()){
				HhQdKhlcntDsgthau gThau = ObjectMapperUtils.map(dsgThau, HhQdKhlcntDsgthau.class);
				gThau.setId(null);
				gThau.setIdQdDtl(qdDtl.getId());
				gThau.setTrangThai(Contains.CHUACAPNHAT);
				hhQdKhlcntDsgthauRepository.save(gThau);
				for (HhDxuatKhLcntDsgthauDtlCtietReq dsDdNhap : dsgThau.getChildren()){
					HhQdKhlcntDsgthauCtiet ddNhap = ObjectMapperUtils.map(dsDdNhap, HhQdKhlcntDsgthauCtiet.class);
					ddNhap.setId(null);
					ddNhap.setIdGoiThau(gThau.getId());
					hhQdKhlcntDsgthauCtietRepository.save(ddNhap);
				}
			}
		}
		return dataDB;
	}

	@Override
	public HhQdKhlcntHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(Long.parseLong(ids));


		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

		qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
		qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));

		List<HhQdKhlcntDtl> hhQdKhlcntDtlList = new ArrayList<>();
		for(HhQdKhlcntDtl dtl : hhQdKhlcntDtlRepository.findAllByIdQdHdr(Long.parseLong(ids))){
			List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauList = new ArrayList<>();
			for(HhQdKhlcntDsgthau dsg : hhQdKhlcntDsgthauRepository.findByIdQdDtl(dtl.getId())){
				List<HhQdKhlcntDsgthauCtiet> listGtCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(dsg.getId());
				listGtCtiet.forEach(f -> {
					f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
					f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
				});
				dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
				dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
				dsg.setTenLoaiHdong(hashMapLoaiHdong.get(dsg.getLoaiHdong()));
				dsg.setTenNguonVon(hashMapNguonVon.get(dsg.getNguonVon()));
				dsg.setTenPthucLcnt(hashMapPthucDthau.get(dsg.getPthucLcnt()));
				dsg.setTenHthucLcnt(hashMapHtLcnt.get(dsg.getHthucLcnt()));
				dsg.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThai()));
				dsg.setChildren(listGtCtiet);
				hhQdKhlcntDsgthauList.add(dsg);
			};
			dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
			dtl.setDsGoiThau(hhQdKhlcntDsgthauList);
			dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));

			hhQdKhlcntDtlList.add(dtl);
		}

		qOptional.get().setHhQdKhlcntDtlList(hhQdKhlcntDtlList);
		qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));

		return qOptional.get();
	}

	@Override
	public HhQdKhlcntDtl detailDtl(Long ids) throws Exception {
		Optional<HhQdKhlcntDtl> byId = hhQdKhlcntDtlRepository.findById(ids);

		if(!byId.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		};

		HhQdKhlcntDtl dtl = byId.get();

		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String,String> hashMapDvi = getListDanhMucDvi(null,null,"01");
		// Set Hdr
		HhQdKhlcntHdr hhQdKhlcntHdr = hhQdKhlcntHdrRepository.findById(dtl.getIdQdHdr()).get();
		hhQdKhlcntHdr.setTenLoaiHdong(hashMapLoaiHdong.get(hhQdKhlcntHdr.getLoaiHdong()));
		hhQdKhlcntHdr.setTenNguonVon(hashMapNguonVon.get(hhQdKhlcntHdr.getNguonVon()));
		hhQdKhlcntHdr.setTenPthucLcnt(hashMapPthucDthau.get(hhQdKhlcntHdr.getPthucLcnt()));
		hhQdKhlcntHdr.setTenHthucLcnt(hashMapHtLcnt.get(hhQdKhlcntHdr.getHthucLcnt()));
		hhQdKhlcntHdr.setTenCloaiVthh(hashMapDmHh.get(hhQdKhlcntHdr.getCloaiVthh()));
		hhQdKhlcntHdr.setTenLoaiVthh(hashMapDmHh.get(hhQdKhlcntHdr.getLoaiVthh()));
		dtl.setHhQdKhlcntHdr(hhQdKhlcntHdr);

		List<HhQdKhlcntDsgthau> byIdQdDtl = hhQdKhlcntDsgthauRepository.findByIdQdDtl(dtl.getId());

		for(HhQdKhlcntDsgthau dsg : byIdQdDtl){
			List<HhQdKhlcntDsgthauCtiet> listGtCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(dsg.getId());
			listGtCtiet.forEach(f -> {
				f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
				f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
			});
			dsg.setTenDvi(hashMapDvi.get(dsg.getMaDvi()));
			dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
			dsg.setTenLoaiHdong(hashMapLoaiHdong.get(dsg.getLoaiHdong()));
			dsg.setTenNguonVon(hashMapNguonVon.get(dsg.getNguonVon()));
			dsg.setTenPthucLcnt(hashMapPthucDthau.get(dsg.getPthucLcnt()));
			dsg.setTenHthucLcnt(hashMapHtLcnt.get(dsg.getHthucLcnt()));
			dsg.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThai()));
			dsg.setChildren(listGtCtiet);
		};
		dtl.setDsGoiThau(byIdQdDtl);


		long countThanhCong = byIdQdDtl.stream().filter(x -> x.getTrangThai().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId())).count();
		long countThatBai = byIdQdDtl.stream().filter(x -> x.getTrangThai().equals(NhapXuatHangTrangThaiEnum.THAT_BAI.getId())).count();
		dtl.setSoGthauTrung(countThanhCong);
		dtl.setSoGthauTruot(countThatBai);
		dtl.setDxuatKhLcntHdr(hhDxuatKhLcntHdrRepository.findBySoDxuat(dtl.getSoDxuat()).get());
		dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(dtl.getTrangThai()));
		dtl.setTenDvi(hashMapDvi.get(dtl.getMaDvi()));
		return dtl;
	}

	@Override
	public HhQdKhlcntDsgthau detailGoiThau(String ids) throws Exception {
		Optional<HhQdKhlcntDsgthau> gThau = hhQdKhlcntDsgthauRepository.findById(Long.parseLong(ids));

		if (!gThau.isPresent()) {
			throw new Exception("Gói thầu không tồn tại");
		}
		Map<String, String> mapDmucDvi = getMapTenDvi();
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();

		HhQdKhlcntDsgthau dataRes = gThau.get();
		dataRes.setTenDvi(mapDmucDvi.get(dataRes.getMaDvi()));
		dataRes.setTenLoaiVthh(hashMapDmHh.get(dataRes.getLoaiVthh()));
		dataRes.setTenCloaiVthh(hashMapDmHh.get(dataRes.getCloaiVthh()));

		List<HhQdKhlcntDsgthauCtiet> dsgThauCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(gThau.get().getId());
		dsgThauCtiet.forEach(item -> {
			item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
			item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
		});

		dataRes.setChildren(dsgThauCtiet);

		Optional<HhQdKhlcntDtl> dataDtl = hhQdKhlcntDtlRepository.findById(dataRes.getIdQdDtl());
		Optional<HhQdKhlcntHdr> dataHdr = hhQdKhlcntHdrRepository.findById(dataDtl.get().getIdQdHdr());

		dataHdr.get().setTenLoaiVthh(hashMapDmHh.get(dataHdr.get().getLoaiVthh()));
		dataHdr.get().setTenCloaiVthh(hashMapDmHh.get(dataHdr.get().getCloaiVthh()));

		dataDtl.get().setHhQdKhlcntHdr(dataHdr.get());
		dataDtl.get().setTenDvi(mapDmucDvi.get(dataDtl.get().getMaDvi()));


		dataRes.setHhQdKhlcntDtl(dataDtl.get());
		dataHdr.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dataHdr.get().getTrangThai()));
		return gThau.get();
	}

	@Override
	public HhQdKhlcntHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId())){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		HhQdKhlcntHdr detail = detail(String.valueOf(stReq.getId()));
		if(detail.getLoaiVthh().startsWith("02")){
			return this.approveVatTu(stReq,detail);
		}else{
			return this.approveLT(stReq,detail);
		}
	}

	@Transactional(rollbackOn = Exception.class)
	HhQdKhlcntHdr approveVatTu(StatusReq stReq,HhQdKhlcntHdr dataDB) throws Exception {
		String status = stReq.getTrangThai() + dataDB.getTrangThai();
		switch (status) {
			case Contains.CHODUYET_LDV + Contains.DUTHAO:
			case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
				dataDB.setNguoiGuiDuyet(getUser().getUsername());
				dataDB.setNgayGuiDuyet(getDateTimeNow());
			case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
				dataDB.setNguoiPduyet(getUser().getUsername());
				dataDB.setNgayPduyet(getDateTimeNow());
				dataDB.setLdoTuchoi(stReq.getLyDo());
			case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
			case Contains.BAN_HANH + Contains.DADUYET_LDV:
				dataDB.setNguoiPduyet(getUser().getUsername());
				dataDB.setNgayPduyet(getDateTimeNow());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}
		dataDB.setTrangThai(stReq.getTrangThai());
		if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
			Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(dataDB.getIdTrHdr());
			if(qOptional.isPresent()){
				if(qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
					throw new Exception("Đề xuất này đã được quyết định");
				}
				// Update trạng thái tờ trình
				hhDxuatKhLcntHdrRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
			}else{
				throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
			}
			this.cloneProject(dataDB.getId(),true);
		}
		HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(dataDB);
		return createCheck;
	}

	@Transactional(rollbackOn = Exception.class)
	HhQdKhlcntHdr approveLT(StatusReq stReq, HhQdKhlcntHdr dataDB) throws Exception{
		String status = stReq.getTrangThai() + dataDB.getTrangThai();
		switch (status) {
			case Contains.BAN_HANH + Contains.DUTHAO:
				dataDB.setNguoiPduyet(getUser().getUsername());
				dataDB.setNgayPduyet(getDateTimeNow());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}
		dataDB.setTrangThai(stReq.getTrangThai());
		if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
			if(dataDB.getPhanLoai().equals("TH")){
				Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(dataDB.getIdThHdr());
				if(qOptional.isPresent()){
					if(qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
						throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
					}
					hhDxKhLcntThopHdrRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
				}else{
					throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
				}
			}else{
				Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(dataDB.getIdTrHdr());
				if(qOptional.isPresent()){
					if(qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
						throw new Exception("Đề xuất này đã được quyết định");
					}
					// Update trạng thái tờ trình
					hhDxuatKhLcntHdrRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
				}else{
					throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
				}
			}
			this.validateData(dataDB);
			this.cloneProject(dataDB.getId(),false);
		}
		HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(dataDB);
		return createCheck;
	}

	private void cloneProject(Long idClone,Boolean isVatTu) throws Exception {
		HhQdKhlcntHdr hdr = this.detail(idClone.toString());
		HhQdKhlcntHdr hdrClone = new HhQdKhlcntHdr();
		BeanUtils.copyProperties(hdr, hdrClone);
		hdrClone.setId(null);
		hdrClone.setLastest(true);
		hhQdKhlcntHdrRepository.save(hdrClone);

		if(isVatTu){
			HhQdKhlcntDtl qdDtl = new HhQdKhlcntDtl();
			qdDtl.setId(null);
			qdDtl.setIdQdHdr(hdrClone.getId());
			qdDtl.setMaDvi(getUser().getDvql());
			qdDtl.setTrangThai(NhapXuatHangTrangThaiEnum.CHUACAPNHAT.getId());
			hhQdKhlcntDtlRepository.save(qdDtl);
			List<HhQdKhlcntDsgthau> dsGoiThauClone = hdr.getHhQdKhlcntDtlList().get(0).getDsGoiThau();
			for (HhQdKhlcntDsgthau gthau : dsGoiThauClone){
				HhQdKhlcntDsgthau gThauClone = new HhQdKhlcntDsgthau();
				BeanUtils.copyProperties(gthau, gThauClone);
				gThauClone.setId(null);
				gThauClone.setIdQdDtl(qdDtl.getId());
				hhQdKhlcntDsgthauRepository.save(gThauClone);
				for (HhQdKhlcntDsgthauCtiet dsDdNhap : gThauClone.getChildren()){
					HhQdKhlcntDsgthauCtiet dsDdNhapClone = new HhQdKhlcntDsgthauCtiet();
					BeanUtils.copyProperties(dsDdNhap, dsDdNhapClone);
					dsDdNhapClone.setId(null);
					dsDdNhapClone.setIdGoiThau(gThauClone.getId());
					hhQdKhlcntDsgthauCtietRepository.save(dsDdNhapClone);
				}
			}
		}else{
			for (HhQdKhlcntDtl dx : hdr.getHhQdKhlcntDtlList()){
				HhQdKhlcntDtl dxClone = new HhQdKhlcntDtl();
				BeanUtils.copyProperties(dx, dxClone);
				dxClone.setId(null);
				dxClone.setIdQdHdr(hdrClone.getId());
				hhQdKhlcntDtlRepository.save(dxClone);
				for (HhQdKhlcntDsgthau gthau : dx.getDsGoiThau()){
					HhQdKhlcntDsgthau gThauClone = new HhQdKhlcntDsgthau();
					BeanUtils.copyProperties(gthau, gThauClone);
					gThauClone.setId(null);
					gThauClone.setIdQdDtl(dxClone.getId());
					hhQdKhlcntDsgthauRepository.save(gThauClone);
					for (HhQdKhlcntDsgthauCtiet dsDdNhap : gThauClone.getChildren()){
						HhQdKhlcntDsgthauCtiet dsDdNhapClone = new HhQdKhlcntDsgthauCtiet();
						BeanUtils.copyProperties(dsDdNhap, dsDdNhapClone);
						dsDdNhapClone.setId(null);
						dsDdNhapClone.setIdGoiThau(gThauClone.getId());
						hhQdKhlcntDsgthauCtietRepository.save(dsDdNhapClone);
					}
				}
			}
		}
	}

	@Override
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.DUTHAO) && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDV)){
			throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
		}
		/**
		 * XÓA DETAIL
		 */
		List<HhQdKhlcntDtl> hhQdKhlcntDtl = hhQdKhlcntDtlRepository.findAllByIdQdHdr(optional.get().getId());
		if(!CollectionUtils.isEmpty(hhQdKhlcntDtl)){
			hhQdKhlcntDtlRepository.deleteAll(hhQdKhlcntDtl);
		}
//		hhQdKhlcntDtlRepository.deleteAllByIdQdHdr(optional.get().getId());
		//Xóa header
		hhQdKhlcntHdrRepository.delete(optional.get());
		// Update trạng thái tổng hợp dxkhclnt
		if(optional.get().getPhanLoai().equals("TH")){
			hhDxKhLcntThopHdrRepository.updateTrangThai(optional.get().getIdThHdr(), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
		}else{
			hhDxuatKhLcntHdrRepository.updateStatusInList(Arrays.asList(optional.get().getSoTrHdr()), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
		}
	}

	@Override
	public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getIdList()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		List<HhQdKhlcntHdr> listHdr = hhQdKhlcntHdrRepository.findAllByIdIn(idSearchReq.getIdList());
		List<Long> idList=listHdr.stream().map(HhQdKhlcntHdr::getId).collect(Collectors.toList());
		List<HhQdKhlcntDtl> hhQdKhlcntDtl = hhQdKhlcntDtlRepository.findAllByIdQdHdrIn(idList);

		hhQdKhlcntDtlRepository.deleteAll(hhQdKhlcntDtl);
		hhQdKhlcntHdrRepository.deleteAll(listHdr);
	}

	@Override
	public HhQdKhlcntHdr detailNumber(String soQd) throws Exception {
		if (StringUtils.isEmpty(soQd))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
//
//		Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findBySoQd(soQd);
//
//		if (!qOptional.isPresent())
//			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		// Quy doi don vi kg = tan
//		List<HhQdKhlcntDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren1(), HhQdKhlcntDtl.class);
//		for (HhQdKhlcntDtl dtl : dtls2) {
//			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
//		}
		return null;
	}

	@Override
	public Page<HhQdKhlcntHdr> colection(HhQdKhlcntSearchReq objReq, HttpServletResponse response) throws Exception {
		int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

		Page<HhQdKhlcntHdr> dataPage = null;

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();

		for (HhQdKhlcntHdr hdr : dataPage.getContent()) {
			hdr.setHthucLcnt(mapDmuc.get(hdr.getHthucLcnt()));
			hdr.setPthucLcnt(mapDmuc.get(hdr.getPthucLcnt()));
			hdr.setLoaiHdong(mapDmuc.get(hdr.getLoaiHdong()));
			hdr.setNguonVon(mapDmuc.get(hdr.getNguonVon()));
		}

		return dataPage;
	}

	@Override
	public Page<HhQdKhlcntHdr> getAllPage(HhQdKhlcntSearchReq req) throws Exception {
		int page = req.getPaggingReq().getPage();
		int limit = req.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Page<HhQdKhlcntHdr> data = hhQdKhlcntHdrRepository.selectPage(req.getNamKhoach(), req.getLoaiVthh(), req.getSoQd(), req.getTrichYeu(),
				convertDateToString(req.getTuNgayQd()),
				convertDateToString(req.getDenNgayQd()),
				req.getTrangThai(), req.getLastest(),
				req.getMaDvi(),
				req.getTrangThaiDtl(),
				pageable);
		List<Long> ids = data.getContent().stream().map(HhQdKhlcntHdr::getId).collect(Collectors.toList());
		List<Object[]> listGthau = hhQdKhlcntDtlRepository.countAllBySoGthau(ids);
		List<Object[]> listSum = hhQdKhlcntDtlRepository.sumTongTienByIdHdr(ids);
		Map<String,String> hashMapSum = new HashMap<>();
		for (Object[] it: listSum) {
			hashMapSum.put(it[0].toString(),it[1].toString());
		}
		Map<String,String> soGthau = new HashMap<>();
		for (HhQdKhlcntHdr f : data.getContent()) {
			f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
			if(f.getLoaiVthh().startsWith("02")){
				HhQdKhlcntDtl detail = hhQdKhlcntDtlRepository.findByIdQdHdr(f.getId());
				f.setTongTien(hhQdKhlcntDsgthauRepository.sumTotalPriceByIdQdDtl(detail.getId()));
			}else{
				f.setTongTien(StringUtils.isEmpty(hashMapSum.get(f.getId().toString())) ? BigDecimal.valueOf(0) : BigDecimal.valueOf(Long.parseLong(hashMapSum.get(f.getId().toString()))));
			}

			f.setNamKhoach(f.getNamKhoach());
		}
		for (Object[] it: listGthau) {
			soGthau.put(it[0].toString(),it[1].toString());
		}
		for (HhQdKhlcntHdr qd:data.getContent()) {
			qd.setSoGthau(StringUtils.isEmpty(soGthau.get(qd.getId().toString())) ? 0 : Long.parseLong(soGthau.get(qd.getId().toString())));
			qd.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qd.getTrangThai()));
		}
		return data;
	}


	@Override
	public List<HhQdKhlcntHdr> getAll(HhQdKhlcntSearchReq req) throws Exception {
		List<HhQdKhlcntHdr> listData =  hhQdKhlcntHdrRepository.selectAll(req.getNamKhoach(),req.getLoaiVthh(),req.getCloaiVthh(), req.getSoQd(), convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()),req.getTrangThai());
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		listData.forEach(f -> {
			f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
		});
		return listData;
	}
	@Override
	public void exportList(HhQdKhlcntSearchReq searchReq, HttpServletResponse response) throws Exception {

		String cDvi = getUser().getCapDvi();
		if(Contains.CAP_TONG_CUC.equals(cDvi)){
			this.exportToExcelTongCuc(searchReq,response);
		}else{
			this.exportToExcelCuc(searchReq,response);
		}
	}
	public void exportToExcelTongCuc(HhQdKhlcntSearchReq searchReq, HttpServletResponse response) throws Exception {
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		searchReq.setPaggingReq(paggingReq);
		Page<HhQdKhlcntHdr> page = this.getAllPage(searchReq);
		List<HhQdKhlcntHdr> data = page.getContent();

		// Tao form excel
		String title = "Danh sách QĐ phê duyệt KHLCNT";
		String[] rowsName = new String[] { "STT", "Số quyết định", "Ngày QĐ", "Trích yếu","Mã tổng hợp","Mã tờ trình", "Năm kế hoạch",
				"Loại hàng hóa", "Chủng loại hàng hóa","Trạng thái" };


		String filename = "Quyet_dinh_ke_hoach_lcnt-tong-cuc.xlsx";

		// Lay danh muc dung chung
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhQdKhlcntHdr qd = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = qd.getSoQd();
			objs[2] = qd.getNgayQd();
			objs[3] = qd.getTrichYeu();
			objs[4] = qd.getIdThHdr();
			objs[5] = qd.getSoTrHdr();
			objs[6] = qd.getNamKhoach();
			objs[7] = qd.getLoaiVthh();
			objs[8] = qd.getCloaiVthh();
			objs[9] = qd.getTrangThai();
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}

	public void exportToExcelCuc(HhQdKhlcntSearchReq searchReq, HttpServletResponse response) throws Exception {

		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		searchReq.setPaggingReq(paggingReq);
		Page<HhQdKhlcntHdr> page = this.getAllPage(searchReq);
		List<HhQdKhlcntHdr> data = page.getContent();

		// Tao form excel
		String title = "Danh sách QĐ phê duyệt KHLCNT";
		String[] rowsName = new String[] { "STT", "Số quyết định", "Ngày quyết định", "Trích yếu", "Năm kế hoạch",
				"Loại hàng hóa", "Chủng loại hàng hóa","Số gói thầu","Tổng tiền(đồng)" };


		String filename = "Quyet_dinh_ke_hoach_lcnt-cuc.xlsx";

		// Lay danh muc dung chung
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhQdKhlcntHdr qd = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = qd.getSoQd();
			objs[2] = qd.getNgayQd();
			objs[3] = qd.getTrichYeu();
			objs[4] = qd.getNamKhoach();
			objs[5] = qd.getLoaiVthh();
			objs[6] = qd.getCloaiVthh();
			objs[7] = qd.getSoGthau();
			objs[8] = qd.getTongTien();
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}
	@Override
	public void exportToExcel(HhQdKhlcntSearchReq searchReq, HttpServletResponse response) throws Exception {

		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		searchReq.setPaggingReq(paggingReq);
		Page<HhQdKhlcntHdr> page = this.getAllPage(searchReq);
		List<HhQdKhlcntHdr> data = page.getContent();

		// Tao form excel
		String title = "Danh sách QĐ phê duyệt KHLCNT";
		String[] rowsName = new String[] { "STT", "Số quyết định", "Ngày QĐ", "Trích yếu", "Năm kế hoạch",
				"Loại hàng hóa", "Chủng loại hàng hóa" };


		String filename = "Quyet_dinh_ke_hoach_lcnt.xlsx";

		// Lay danh muc dung chung
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhQdKhlcntHdr qd = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = qd.getSoQd();
			objs[2] = qd.getNgayQd();
			objs[3] = qd.getTrichYeu();
			objs[4] = qd.getNamKhoach();
			objs[5] = qd.getLoaiVthh();
			objs[6] = qd.getCloaiVthh();
			dataList.add(objs);
		}

		ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
		ex.export();
	}


}
