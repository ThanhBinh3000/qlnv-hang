package com.tcdt.qlnvhang.service.impl;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.*;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.tochuctrienkhai.QdPdHsmt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.*;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.tochuctrienkhai.QdPdHsmtRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.service.feign.KeHoachService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinQdKhlcntHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.service.HhQdKhlcntHdrService;

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
	private HhQdKhlcntDsgthauCtietVtRepository hhQdKhlcntDsgthauCtietVtRepository;

	@Autowired
	private HhDxKhLcntThopHdrRepository hhDxKhLcntThopHdrRepository;

	@Autowired
	private KeHoachService keHoachService;

	@Autowired
	private FileDinhKemService fileDinhKemService;

	@Autowired
	private HhDchinhDxKhLcntHdrRepository hhDchinhDxKhLcntHdrRepository;

	@Autowired
	private HhDchinhDxKhLcntDsgthauRepository gThauRepository;

	@Autowired
	private HhDchinhDxKhLcntDsgthauCtietRepository gThauCietRepository;

	@Autowired
	private HhDchinhDxKhLcntDsgthauCtietVtRepository gThauCietVtRepository;

	@Autowired
	private QdPdHsmtRepository qdPdHsmtRepository;

	@Autowired
	private HhQdPduyetKqlcntDtlRepository hhQdPduyetKqlcntDtlRepository;

	@Autowired
	private HhHopDongRepository hhHopDongRepository;
	@Autowired
	private HhQdPduyetKqlcntHdrRepository hhQdPduyetKqlcntHdrRepository;
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

	@Transactional
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

		HhQdKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdKhlcntHdr.class);
		if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), HhQdKhlcntHdr.TABLE_NAME);
		}
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.DANG_NHAP_DU_LIEU);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setLastest(objReq.getLastest());
		dataMap.setMaDvi(getUser().getDvql());
		hhQdKhlcntHdrRepository.save(dataMap);

		// Update trạng thái tổng hợp dxkhclnt
		if(objReq.getPhanLoai().equals("TH")){
			hhDxKhLcntThopHdrRepository.updateTrangThai(dataMap.getIdThHdr(), Contains.DADUTHAO_QD);
		}else{
			hhDxuatKhLcntHdrRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);
		}

		saveDetail(objReq,dataMap);
		return dataMap;
	}

	@Transactional
	HhQdKhlcntHdr createVatTu(HhQdKhlcntHdrReq objReq) throws Exception {
		List<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
		if (!checkSoQd.isEmpty()) {
			throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
		}
		HhQdKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdKhlcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.DANG_NHAP_DU_LIEU);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setMaDvi(getUser().getDvql());
		dataMap.setDieuChinh(Boolean.FALSE);
		hhQdKhlcntHdrRepository.save(dataMap);
		if (!DataUtils.isNullOrEmpty(objReq.getListCcPhapLy())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getListCcPhapLy(), dataMap.getId(), HhQdKhlcntHdr.TABLE_NAME + "_CAN_CU");
		}
		if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), HhQdKhlcntHdr.TABLE_NAME);
		}

		// Update trạng thái tờ trình
		hhDxuatKhLcntHdrRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);

		saveDetailVt(objReq,dataMap);

		return dataMap;
	}

	@Transactional
	void saveDetail(HhQdKhlcntHdrReq objReq,HhQdKhlcntHdr dataMap){
		hhQdKhlcntDtlRepository.deleteAllByIdQdHdr(dataMap.getId());
		for (HhQdKhlcntDtlReq dx : objReq.getChildren()){
			HhQdKhlcntDtl qd = ObjectMapperUtils.map(dx, HhQdKhlcntDtl.class);
			hhQdKhlcntDsgthauRepository.deleteByIdQdDtl(qd.getId());
			qd.setId(null);
			qd.setIdQdHdr(dataMap.getId());
			qd.setTrangThai(Contains.CHUACAPNHAT);
			Optional<HhDxuatKhLcntHdr> hhDxuatKhLcntHdr = hhDxuatKhLcntHdrRepository.findById(dx.getIdDxHdr());
			if (hhDxuatKhLcntHdr.isPresent()) {
				if (dx.getTgianBdauTchuc() == null) {
					qd.setTgianBdauTchuc(hhDxuatKhLcntHdr.get().getTgianBdauTchuc());
				}
				if (dx.getTgianMthau() == null) {
					qd.setTgianMthau(hhDxuatKhLcntHdr.get().getTgianMthau());
				}
				if (dx.getTgianDthau() == null) {
					qd.setTgianDthau(hhDxuatKhLcntHdr.get().getTgianDthau());
				}
				if (dx.getTgianNhang() == null) {
					qd.setTgianNhang(hhDxuatKhLcntHdr.get().getTgianNhang());
				}
			}
			hhQdKhlcntDtlRepository.save(qd);
			for (HhQdKhlcntDsgthauReq gtList : ObjectUtils.isEmpty(dx.getDsGoiThau()) ? dx.getChildren() : dx.getDsGoiThau()){
					HhQdKhlcntDsgthau gt = ObjectMapperUtils.map(gtList, HhQdKhlcntDsgthau.class);
				hhQdKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gt.getId());
				gt.setId(null);
				gt.setIdQdDtl(qd.getId());
				gt.setTrangThai(Contains.CHUACAPNHAT);
				gt.setSoLuongTheoChiTieu(gtList.getSoLuongTheoChiTieu());
				gt.setSoLuongDaMua(gtList.getSoLuongDaMua());
				hhQdKhlcntDsgthauRepository.save(gt);
				for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gtList.getChildren()){
					HhQdKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhQdKhlcntDsgthauCtiet.class);
					dataDdNhap.setId(null);
					dataDdNhap.setIdGoiThau(gt.getId());
					dataDdNhap.setDonGia(ddNhap.getDonGiaTamTinh() != null ? ddNhap.getDonGiaTamTinh() : ddNhap.getDonGia());
					dataDdNhap.setSoLuongTheoChiTieu(ddNhap.getSoLuongTheoChiTieu());
					dataDdNhap.setSoLuongDaMua(ddNhap.getSoLuongDaMua());
					hhQdKhlcntDsgthauCtietRepository.save(dataDdNhap);
					hhQdKhlcntDsgthauCtietVtRepository.deleteAllByIdGoiThauCtiet(ddNhap.getId());
					for(HhDxuatKhLcntDsgthauDtlCtietVtReq ctietReq : ddNhap.getChildren()){
						HhQdKhlcntDsgthauCtietVt ctietVt = new ModelMapper().map(ctietReq,HhQdKhlcntDsgthauCtietVt.class);
						ctietVt.setId(null);
						ctietVt.setIdGoiThauCtiet(dataDdNhap.getId());
						ctietVt.setDiaDiemNhap(ctietReq.getDiaDiemNhap());
						hhQdKhlcntDsgthauCtietVtRepository.save(ctietVt);
					}
				}
			}
		}
	}

	@Transactional
	void saveDetailVt(HhQdKhlcntHdrReq objReq, HhQdKhlcntHdr dataMap) {
		hhQdKhlcntDsgthauRepository.deleteByIdQdHdr(dataMap.getId());
		for (HhQdKhlcntDsgthauReq gtList : objReq.getDsGoiThau()) {
			HhQdKhlcntDsgthau gt = ObjectMapperUtils.map(gtList, HhQdKhlcntDsgthau.class);
			hhQdKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gt.getId());
			gt.setId(null);
			gt.setIdQdHdr(dataMap.getId());
			gt.setSoLuongTheoChiTieu(gtList.getSoLuongTheoChiTieu());
			gt.setSoLuongDaMua(gtList.getSoLuongDaMua());
			hhQdKhlcntDsgthauRepository.save(gt);
			for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gtList.getChildren()) {
				HhQdKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhQdKhlcntDsgthauCtiet.class);
				dataDdNhap.setId(null);
				dataDdNhap.setIdGoiThau(gt.getId());
				dataDdNhap.setDonGia(ddNhap.getDonGiaTamTinh() != null ? ddNhap.getDonGiaTamTinh() : ddNhap.getDonGia());
				dataDdNhap.setSoLuongTheoChiTieu(ddNhap.getSoLuongTheoChiTieu());
				dataDdNhap.setSoLuongDaMua(ddNhap.getSoLuongDaMua());
				hhQdKhlcntDsgthauCtietRepository.save(dataDdNhap);
				hhQdKhlcntDsgthauCtietVtRepository.deleteAllByIdGoiThauCtiet(ddNhap.getId());
				for (HhDxuatKhLcntDsgthauDtlCtietVtReq ctietReq : ddNhap.getChildren()) {
					HhQdKhlcntDsgthauCtietVt ctietVt = new ModelMapper().map(ctietReq, HhQdKhlcntDsgthauCtietVt.class);
					ctietVt.setId(null);
					ctietVt.setIdGoiThauCtiet(dataDdNhap.getId());
					ctietVt.setDiaDiemNhap(ctietReq.getDiaDiemNhap());
					hhQdKhlcntDsgthauCtietVtRepository.save(ctietVt);
				}
			}
		}
	}

	@Transactional(rollbackFor = {Exception.class, Throwable.class})
	public void validateData(HhQdKhlcntHdr objHdr) throws Exception {
		for(HhQdKhlcntDtl dtl : objHdr.getChildren()){
			for(HhQdKhlcntDsgthau dsgthau : dtl.getChildren()){
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



		HhQdKhlcntHdr dataDB = qOptional.get();
		HhQdKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdKhlcntHdr.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());
		fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(HhQdKhlcntHdr.TABLE_NAME));
		if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataDB.getId(), HhQdKhlcntHdr.TABLE_NAME);
		}

		hhQdKhlcntHdrRepository.save(dataDB);

		this.saveDetail(objReq,dataDB);

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

		if(!StringUtils.isEmpty(objReq.getSoQd())){
			if (!objReq.getSoQd().equals(qOptional.get().getSoQd())) {
				List<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
				if (!checkSoQd.isEmpty()) {
					throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
				}
			}
		}
		HhQdKhlcntHdr dataDB = qOptional.get();
		HhQdKhlcntHdr dataMap = ObjectMapperUtils.map(objReq, HhQdKhlcntHdr.class);
		updateObjectToObject(dataDB, dataMap);
		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());
		hhQdKhlcntHdrRepository.save(dataDB);
		fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(HhQdKhlcntHdr.TABLE_NAME + "_CAN_CU"));
		if (!DataUtils.isNullOrEmpty(objReq.getListCcPhapLy())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getListCcPhapLy(), dataMap.getId(), HhQdKhlcntHdr.TABLE_NAME + "_CAN_CU");
		}
		fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(HhQdKhlcntHdr.TABLE_NAME));
		if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), HhQdKhlcntHdr.TABLE_NAME);
		}
		this.saveDetailVt(objReq,dataDB);
		return dataDB;
	}

	@Override
	public HhQdKhlcntHdr detail(String ids) throws Exception {
		if (StringUtils.isEmpty(ids)) {
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		}
		Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(Long.parseLong(ids));


		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		if (qOptional.get().getLoaiVthh().startsWith("02")) {
			List<FileDinhKem> canCu = fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhQdKhlcntHdr.TABLE_NAME + "_CAN_CU"));
			qOptional.get().setListCcPhapLy(canCu);
			List<FileDinhKem> fileDinhKems = fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhQdKhlcntHdr.TABLE_NAME));
			qOptional.get().setFileDinhKems(fileDinhKems);
			detailVt(qOptional.get());
		} else {
			Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
			Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
			Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
			Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
			Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
			Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
//			Map<String,String> hashMapLoaiNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
//			Map<String,String> hashMapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");

			qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
			qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));
			qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
			List<FileDinhKem> fileDinhKems = fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhQdKhlcntHdr.TABLE_NAME));
			qOptional.get().setFileDinhKems(fileDinhKems);
			List<HhQdKhlcntDtl> hhQdKhlcntDtlList = new ArrayList<>();
//			Optional<HhDxuatKhLcntHdr> hhDxuatKhLcntHdr = Optional.empty();
//			List<HhDxuatKhLcntHdr> listHhDxuatKhLcntHdr = new ArrayList<>();
//			Optional<HhDxKhLcntThopHdr> hhDxKhLcntThopHdr = Optional.empty();
//			if(qOptional.get().getIdTrHdr() != null){
//				hhDxuatKhLcntHdr = hhDxuatKhLcntHdrRepository.findById(qOptional.get().getIdTrHdr());
//			}else{
//				listHhDxuatKhLcntHdr = hhDxuatKhLcntHdrRepository.getAllByIdThopHrd(qOptional.get().getIdThHdr());
//			}
			List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauData = new ArrayList<>();
			Long countSlGThau = 0L;
			for(HhQdKhlcntDtl dtl : hhQdKhlcntDtlRepository.findAllByIdQdHdr(Long.parseLong(ids))){
				List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauList = new ArrayList<>();
//				if(hhDxuatKhLcntHdr.isPresent()){
//					dtl.setDxuatKhLcntHdr(hhDxuatKhLcntHdr.get());
//				}
//				if(!listHhDxuatKhLcntHdr.isEmpty()){
//					listHhDxuatKhLcntHdr.forEach(item ->{
//						dtl.setDxuatKhLcntHdr(item);
//					});
//				}
				Optional<HhDxuatKhLcntHdr> hhDxuatKhLcntHdr = hhDxuatKhLcntHdrRepository.findById(dtl.getIdDxHdr());
				if (hhDxuatKhLcntHdr.isPresent()) {
					hhDxuatKhLcntHdr.get().setTenDvi(mapDmucDvi.get(hhDxuatKhLcntHdr.get().getMaDvi()));
					dtl.setDxuatKhLcntHdr(hhDxuatKhLcntHdr.get());
				}
				hhQdKhlcntDsgthauData = hhQdKhlcntDsgthauRepository.findByIdQdDtlOrderByGoiThauAsc(dtl.getId());
				Set<Long> goiThauSet = new HashSet<>();
				int count = 0;
				for(HhQdKhlcntDsgthau dsg : hhQdKhlcntDsgthauData){
					Long idQdDtl = dsg.getIdQdDtl();
					if (!goiThauSet.contains(idQdDtl)) {
						goiThauSet.add(idQdDtl);
						count++;
					}
					List<HhQdKhlcntDsgthauCtiet> listGtCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(dsg.getId());
					listGtCtiet.forEach(f -> {
						f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
						f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
						List<HhQdKhlcntDsgthauCtietVt> byIdGoiThauCtiet = hhQdKhlcntDsgthauCtietVtRepository.findByIdGoiThauCtiet(f.getId());
						byIdGoiThauCtiet.forEach( x -> {
							x.setTenDvi(mapDmucDvi.get(x.getMaDvi()));
						});
						f.setChildren(byIdGoiThauCtiet);
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
				countSlGThau += count;
				dtl.setTenCloaiVthh(hashMapDmHh.get(dtl.getCloaiVthh()));
				dtl.setTenLoaiVthh(hashMapDmHh.get(dtl.getLoaiVthh()));
				dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
				dtl.setChildren(hhQdKhlcntDsgthauList);
				dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));


				hhQdKhlcntDtlList.add(dtl);
			}
			long countThanhCong = hhQdKhlcntDtlList.stream()
					.flatMap(x -> x.getChildren().stream())
					.filter(y -> y.getTrangThai().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId()))
					.map(y -> y.getIdQdDtl())
					.distinct()
					.count();
			qOptional.get().setSoGthau(countSlGThau);
			qOptional.get().setSoGthauTrung(countThanhCong);
			qOptional.get().setTenLoaiHdong(hashMapLoaiHdong.get(qOptional.get().getLoaiHdong()));
//			if(hhDxuatKhLcntHdr.isPresent()){
//				qOptional.get().setTenLoaiHinhNx(hashMapLoaiNx.get(hhDxuatKhLcntHdr.get().getLoaiHinhNx()));
//				qOptional.get().setTenKieuNx(hashMapKieuNx.get(hhDxuatKhLcntHdr.get().getKieuNx()));
//			}
//		if(!listHhDxuatKhLcntHdr.isEmpty()){
//			listHhDxuatKhLcntHdr.forEach(item ->{
//				dtl.setDxuatKhLcntHdr(item);
//			});
//		}
			qOptional.get().setChildren(hhQdKhlcntDtlList);
			qOptional.get().setTenHthucLcnt(hashMapHtLcnt.get(qOptional.get().getHthucLcnt()));
			qOptional.get().setTenPthucLcnt(hashMapPthucDthau.get(qOptional.get().getPthucLcnt()));
			qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
			qOptional.get().setTenTrangThaiDt(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThaiDt()));
			qOptional.get().setTenNguonVon(hashMapNguonVon.get(qOptional.get().getNguonVon()));

		}
		return qOptional.get();
	}

	private void detailVt (HhQdKhlcntHdr data) {
		Map<String,String> hashMapLoaiNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
		Map<String,String> hashMapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
		data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : hashMapDmHh.get(data.getCloaiVthh()));
		data.setTenHthucLcnt(hashMapHtLcnt.get(data.getHthucLcnt()));
		data.setTenPthucLcnt(hashMapPthucDthau.get(data.getPthucLcnt()));
		data.setTenLoaiHdong(hashMapLoaiHdong.get(data.getLoaiHdong()));
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
		List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauData = hhQdKhlcntDsgthauRepository.findByIdQdHdrOrderByGoiThauAsc(data.getId());
		Long countGoiTrung = 0L;
		for(HhQdKhlcntDsgthau dsg : hhQdKhlcntDsgthauData){
			List<HhQdKhlcntDsgthauCtiet> listGtCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(dsg.getId());
			listGtCtiet.forEach(f -> {
				f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
				f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
				List<HhQdKhlcntDsgthauCtietVt> byIdGoiThauCtiet = hhQdKhlcntDsgthauCtietVtRepository.findByIdGoiThauCtiet(f.getId());
				byIdGoiThauCtiet.forEach( x -> {
					x.setTenDvi(mapDmucDvi.get(x.getMaDvi()));
				});
				f.setChildren(byIdGoiThauCtiet);
			});
			dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
			dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
			dsg.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThai()));
			dsg.setChildren(listGtCtiet);
			dsg.setKqlcntDtl(hhQdPduyetKqlcntDtlRepository.findByIdGoiThauAndType(dsg.getId(), "GOC"));
			if (dsg.getTrangThaiDt().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId())) {
				countGoiTrung += 1L;
			}
		}
		data.setDsGthau(hhQdKhlcntDsgthauData);
		data.setSoGthau((long)hhQdKhlcntDsgthauData.size());
		data.setSoGthauTrung(countGoiTrung);
		data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
		data.setTenTrangThaiDt(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiDt()));
		if (data.getIdTrHdr() != null) {
			Optional<HhDxuatKhLcntHdr> dxuatKhLcntHdr = hhDxuatKhLcntHdrRepository.findById(data.getIdTrHdr());
			if (dxuatKhLcntHdr.isPresent()) {
				dxuatKhLcntHdr.get().setTenLoaiHinhNx(hashMapLoaiNx.get(dxuatKhLcntHdr.get().getLoaiHinhNx()));
				dxuatKhLcntHdr.get().setTenKieuNx(hashMapKieuNx.get(dxuatKhLcntHdr.get().getKieuNx()));
				dxuatKhLcntHdr.get().setTenNguonVon(hashMapNguonVon.get(dxuatKhLcntHdr.get().getNguonVon()));
				data.setDxKhlcntHdr(dxuatKhLcntHdr.get());
			}
		}
		if (data.getDieuChinh().equals(Boolean.TRUE)) {
			Optional<HhDchinhDxKhLcntHdr> dchinhDxKhLcntHdr = hhDchinhDxKhLcntHdrRepository.findByIdQdGocAndLastest(data.getId(), Boolean.TRUE);
			if (dchinhDxKhLcntHdr.isPresent()) {
				List<HhDchinhDxKhLcntDsgthau> gThauList = gThauRepository.findAllByIdDcDxHdr(dchinhDxKhLcntHdr.get().getId());
				for(HhDchinhDxKhLcntDsgthau gThau : gThauList){
					List<HhDchinhDxKhLcntDsgthauCtiet> gthauCtietList = gThauCietRepository.findAllByIdGoiThau(gThau.getId());
					gthauCtietList.forEach(f -> {
						f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
						f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
						List<HhDchinhDxKhLcntDsgthauCtietVt> gthauCtietVtList = gThauCietVtRepository.findAllByIdGoiThauCtiet(f.getId());
						gthauCtietVtList.forEach(ct ->{
							ct.setTenDvi(mapDmucDvi.get(ct.getMaDvi()));
						});
						f.setChildren(gthauCtietVtList);
					});
					gThau.setTenTrangThaiDt(NhapXuatHangTrangThaiEnum.getTenById(gThau.getTrangThaiDt()));
					gThau.setTenCloaiVthh(hashMapDmHh.get(gThau.getCloaiVthh()));
					gThau.setChildren(gthauCtietList);
					gThau.setKqlcntDtl(hhQdPduyetKqlcntDtlRepository.findByIdGoiThauAndType(gThau.getId(), "DC"));
				}
				dchinhDxKhLcntHdr.get().setDsGthau(gThauList);
				data.setDchinhDxKhLcntHdr(dchinhDxKhLcntHdr.get());
				data.setSoQdDc(dchinhDxKhLcntHdr.get().getSoQdDc());
			}
		}
		Optional<QdPdHsmt> qOptional = qdPdHsmtRepository.findByIdQdPdKhlcnt(data.getId());
		qOptional.ifPresent(data::setQdPdHsmt);
	}

	@Override
	public HhQdKhlcntDtl detailDtl(Long ids) throws Exception {
		Optional<HhQdKhlcntDtl> byId = hhQdKhlcntDtlRepository.findById(ids);
		Optional<HhQdKhlcntHdr> hhQdKhlcntHdr = Optional.empty();
		if(!byId.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		};

		HhQdKhlcntDtl dtl = byId.get();

		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
		Map<String,String> hashMapLoaiNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
		Map<String,String> hashMapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String,String> hashMapDvi = getListDanhMucDvi(null,null,"01");
		// Set Hdr
		hhQdKhlcntHdr = hhQdKhlcntHdrRepository.findById(dtl.getIdQdHdr());
		HhQdPduyetKqlcntHdr hhQdPduyetKqlcntHdr = new HhQdPduyetKqlcntHdr();
		if(hhQdKhlcntHdr.isPresent()) {
			hhQdKhlcntHdr.get().setTenLoaiHdong(hashMapLoaiHdong.get(hhQdKhlcntHdr.get().getLoaiHdong()));
			hhQdKhlcntHdr.get().setTenNguonVon(hashMapNguonVon.get(hhQdKhlcntHdr.get().getNguonVon()));
			hhQdKhlcntHdr.get().setTenPthucLcnt(hashMapPthucDthau.get(hhQdKhlcntHdr.get().getPthucLcnt()));
			hhQdKhlcntHdr.get().setTenHthucLcnt(hashMapHtLcnt.get(hhQdKhlcntHdr.get().getHthucLcnt()));
			hhQdKhlcntHdr.get().setTenCloaiVthh(hashMapDmHh.get(hhQdKhlcntHdr.get().getCloaiVthh()));
			hhQdKhlcntHdr.get().setTenLoaiVthh(hashMapDmHh.get(hhQdKhlcntHdr.get().getLoaiVthh()));
			dtl.setHhQdKhlcntHdr(hhQdKhlcntHdr.get());
			Optional<HhQdPduyetKqlcntHdr> hhQdPduyetKqlcntHdrOptional = hhQdPduyetKqlcntHdrRepository.findByIdQdPdKhlcnt(hhQdKhlcntHdr.get().getId());
			if (hhQdPduyetKqlcntHdrOptional.isPresent()) {
				hhQdPduyetKqlcntHdr = hhQdPduyetKqlcntHdrOptional.get();
			}
		}
		List<HhQdKhlcntDsgthau> byIdQdDtl = hhQdKhlcntDsgthauRepository.findByIdQdDtl(dtl.getId());
		Long countSlGThau = 0L;
		for (HhQdKhlcntDsgthau x : byIdQdDtl )  {
			x.setTenDvi(hashMapDvi.get(x.getMaDvi()));
			x.setFileDinhKems(fileDinhKemService.search(x.getId(), Collections.singleton("HH_QD_KHLCNT_DSGTHAU")));
			List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauList = new ArrayList<>();
			Set<Long> goiThauSet = new HashSet<>();
			int count = 0;
			for(HhQdKhlcntDsgthau dsg : hhQdKhlcntDsgthauRepository.findByIdQdDtl(dtl.getId())){
				Long idQdDtl = dsg.getIdQdDtl();
				if (!goiThauSet.contains(idQdDtl)) {
					goiThauSet.add(idQdDtl);
					count++;
				}
				List<HhQdKhlcntDsgthauCtiet> listGtCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(dsg.getId());
				listGtCtiet.forEach(f -> {
					f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
					f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
					List<HhQdKhlcntDsgthauCtietVt> byIdGoiThauCtiet = hhQdKhlcntDsgthauCtietVtRepository.findByIdGoiThauCtiet(f.getId());
					byIdGoiThauCtiet.forEach( z -> {
						z.setTenDvi(hashMapDvi.get(z.getMaDvi()));
					});
					f.setChildren(byIdGoiThauCtiet);
				});
				dsg.setTenDvi(hashMapDvi.get(dsg.getMaDvi()));
				dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
				dsg.setTenLoaiHdong(hashMapLoaiHdong.get(dsg.getLoaiHdong()));
				dsg.setTenNguonVon(hashMapNguonVon.get(dsg.getNguonVon()));
				dsg.setTenPthucLcnt(hashMapPthucDthau.get(dsg.getPthucLcnt()));
				dsg.setTenHthucLcnt(hashMapHtLcnt.get(dsg.getHthucLcnt()));
				dsg.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThai()));
				dsg.setTenTrangThaiDt(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThaiDt()));
				dsg.setChildren(listGtCtiet);
				if (hhQdPduyetKqlcntHdr.getId() != null) {
					dsg.setKqlcntDtl(hhQdPduyetKqlcntDtlRepository.findByIdGoiThauAndIdQdPdHdr(dsg.getId(), hhQdPduyetKqlcntHdr.getId()));
				}
				hhQdKhlcntDsgthauList.add(dsg);
			};
			countSlGThau += count;
		};
		dtl.setChildren(byIdQdDtl);
		dtl.setSoGthau(countSlGThau);
		long countThanhCong = byIdQdDtl.stream().filter(x -> x.getTrangThai().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId())).map(y -> y.getId())
				.distinct().count();
		long countThatBai = byIdQdDtl.stream().filter(x -> x.getTrangThai().equals(NhapXuatHangTrangThaiEnum.THAT_BAI.getId())).map(y -> y.getId())
				.distinct().count();
		dtl.setSoGthauTrung(countThanhCong);
		dtl.setSoGthauTruot(countThatBai);
		Optional<HhDxuatKhLcntHdr> bySoDxuat;
		if(!StringUtils.isEmpty(dtl.getSoDxuat())){
			bySoDxuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(dtl.getSoDxuat());
		}else{
			bySoDxuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(hhQdKhlcntHdr.get().getSoTrHdr());
		}
		if (bySoDxuat.isPresent()) {
			bySoDxuat.get().setTenLoaiHinhNx(hashMapLoaiNx.get(bySoDxuat.get().getLoaiHinhNx()));
			bySoDxuat.get().setTenKieuNx(hashMapKieuNx.get(bySoDxuat.get().getKieuNx()));
			bySoDxuat.get().setTenLoaiHdong(hashMapLoaiHdong.get(bySoDxuat.get().getLoaiHdong()));
			bySoDxuat.get().setTenNguonVon(hashMapNguonVon.get(bySoDxuat.get().getNguonVon()));
			bySoDxuat.get().setTenPthucLcnt(hashMapPthucDthau.get(bySoDxuat.get().getPthucLcnt()));
			bySoDxuat.get().setTenHthucLcnt(hashMapHtLcnt.get(bySoDxuat.get().getHthucLcnt()));
			bySoDxuat.get().setTenCloaiVthh(hashMapDmHh.get(bySoDxuat.get().getCloaiVthh()));
			bySoDxuat.get().setTenLoaiVthh(hashMapDmHh.get(bySoDxuat.get().getLoaiVthh()));
		}
		bySoDxuat.ifPresent(dtl::setDxuatKhLcntHdr);
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
	@Transactional(rollbackFor = {Exception.class, Throwable.class})
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

	@Transactional(rollbackFor = {Exception.class, Throwable.class})
	HhQdKhlcntHdr approveVatTu(StatusReq stReq,HhQdKhlcntHdr dataDB) throws Exception {
		String status = stReq.getTrangThai() + dataDB.getTrangThai();
		switch (status) {
			case Contains.BAN_HANH + Contains.DANG_NHAP_DU_LIEU:
				dataDB.setNguoiPduyet(getUser().getUsername());
				dataDB.setNgayPduyet(getDateTimeNow());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}
		dataDB.setTrangThai(stReq.getTrangThai());
		dataDB.setTrangThaiDt(TrangThaiAllEnum.CHUA_CAP_NHAT.getId());
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
//			this.cloneProject(dataDB.getId());
		}
		HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(dataDB);
		return createCheck;
	}

	@Transactional(rollbackFor = {Exception.class, Throwable.class})
	HhQdKhlcntHdr approveLT(StatusReq stReq, HhQdKhlcntHdr dataDB) throws Exception{
		String status = stReq.getTrangThai() + dataDB.getTrangThai();
		switch (status) {
			case Contains.BAN_HANH + Contains.DANG_NHAP_DU_LIEU:
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
					qOptional.get().setTrangThai(Contains.DABANHANH_QD);
					hhDxKhLcntThopHdrRepository.save(qOptional.get());
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
//			this.validateData(dataDB);
			this.cloneProject(dataDB.getId());
		}
		HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(dataDB);
		return createCheck;
	}

	private void cloneProject(Long idClone) throws Exception {
		HhQdKhlcntHdr hdr = this.detail(idClone.toString());
		HhQdKhlcntHdr hdrClone = new HhQdKhlcntHdr();
		BeanUtils.copyProperties(hdr, hdrClone);
		hdrClone.setId(null);
		hdrClone.setLastest(true);
		hdrClone.setIdGoc(hdr.getId());
		hhQdKhlcntHdrRepository.save(hdrClone);
		for (HhQdKhlcntDtl dx : hdr.getChildren()){
			HhQdKhlcntDtl dxClone = new HhQdKhlcntDtl();
			BeanUtils.copyProperties(dx, dxClone);
			dxClone.setId(null);
			dxClone.setIdQdHdr(hdrClone.getId());
			hhQdKhlcntDtlRepository.save(dxClone);
			for (HhQdKhlcntDsgthau gthau : dx.getChildren()){
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
					for(HhQdKhlcntDsgthauCtietVt ctietVt : dsDdNhap.getChildren()){
						HhQdKhlcntDsgthauCtietVt ctietVtClone = new HhQdKhlcntDsgthauCtietVt();
						BeanUtils.copyProperties(ctietVt, ctietVtClone);
						ctietVtClone.setId(null);
						ctietVtClone.setIdGoiThauCtiet(dsDdNhapClone.getId());
						hhQdKhlcntDsgthauCtietVtRepository.save(ctietVtClone);
					}
				}
			}
		}
	}

	@Override
	@Transactional
	public void delete(IdSearchReq idSearchReq) throws Exception {
		if (StringUtils.isEmpty(idSearchReq.getId()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

		Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findById(idSearchReq.getId());
		if (!optional.isPresent())
			throw new Exception("Không tìm thấy dữ liệu cần xoá");

		if (!optional.get().getTrangThai().equals(Contains.DANG_NHAP_DU_LIEU) && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDV)){
			throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
		}
		/**
		 * XÓA DETAIL
		 */
		if (optional.get().getLoaiVthh().startsWith("02")) {
			List<HhQdKhlcntDsgthau> byIdQdDtl = hhQdKhlcntDsgthauRepository.findByIdQdHdr(optional.get().getId());
			for (HhQdKhlcntDsgthau gThau :byIdQdDtl) {
				hhQdKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gThau.getId());
			}
			hhQdKhlcntDsgthauRepository.deleteByIdQdHdr(optional.get().getId());
		} else {
			List<HhQdKhlcntDtl> hhQdKhlcntDtl = hhQdKhlcntDtlRepository.findAllByIdQdHdr(optional.get().getId());
			if(!CollectionUtils.isEmpty(hhQdKhlcntDtl)){
				for (HhQdKhlcntDtl dtl:hhQdKhlcntDtl) {
					List<HhQdKhlcntDsgthau> byIdQdDtl = hhQdKhlcntDsgthauRepository.findByIdQdDtl(dtl.getId());
					for (HhQdKhlcntDsgthau gThau :byIdQdDtl) {
						hhQdKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gThau.getId());
					}
					hhQdKhlcntDsgthauRepository.deleteByIdQdDtl(dtl.getId());
				}
				hhQdKhlcntDtlRepository.deleteAll(hhQdKhlcntDtl);
			}
		}
		// Update trạng thái tổng hợp dxkhclnt
		if(optional.get().getPhanLoai().equals("TH")){
			hhDxKhLcntThopHdrRepository.updateTrangThai(optional.get().getIdThHdr(), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
		}else{
			hhDxuatKhLcntHdrRepository.updateStatusInList(Arrays.asList(optional.get().getSoTrHdr()), NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
		}
		//Xóa header
		hhQdKhlcntHdrRepository.delete(optional.get());
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
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

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
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Page<HhQdKhlcntHdr> data = hhQdKhlcntHdrRepository.selectPage(req.getNamKhoach(), req.getLoaiVthh(), req.getSoQd(), req.getTrichYeu(),
				convertDateToString(req.getTuNgayQd()),
				convertDateToString(req.getDenNgayQd()),
				req.getTrangThai(), req.getLastest(),
				req.getMaDvi(),
				req.getTrangThaiDtl(),
				req.getTrangThaiDt(),
				pageable);
		if(req.getLoaiVthh().startsWith("02")){
			for (HhQdKhlcntHdr f : data.getContent()) {
				if(f.getIdTrHdr() == null){
					List<HhQdKhlcntDtl> hhQdKhlcntDtl = hhQdKhlcntDtlRepository.findAllByIdQdHdr(f.getId());
					hhQdKhlcntDtl.forEach(item ->{
						f.setSoTrHdr(item.getSoDxuat());
					});
				}
				f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
				f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
				f.setNamKhoach(f.getNamKhoach());
				f.setTenPthucLcnt(hashMapPthucDthau.get(f.getPthucLcnt()));
				f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
				List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauData = hhQdKhlcntDsgthauRepository.findByIdQdHdr(f.getId());
				f.setSoGthau((long) hhQdKhlcntDsgthauData.size());
				int soGthauTrung = 0;
				int soHdDaKy = 0;
				if (f.getDieuChinh().equals(Boolean.TRUE)) {
					Optional<HhDchinhDxKhLcntHdr> dchinhDxKhLcntHdr = hhDchinhDxKhLcntHdrRepository.findByIdQdGocAndLastest(f.getId(), Boolean.TRUE);
					if (dchinhDxKhLcntHdr.isPresent()) {
						List<HhDchinhDxKhLcntDsgthau> gThauList = gThauRepository.findAllByIdDcDxHdr(dchinhDxKhLcntHdr.get().getId());
						for (HhDchinhDxKhLcntDsgthau gthauDc : gThauList) {
							if (gthauDc.getTrangThaiDt() != null && gthauDc.getTrangThaiDt().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId())) {
								soGthauTrung += 1;
							}
							Optional<HhHopDongHdr> hhHopDongHdr = hhHopDongRepository.findBySoQdKqLcntAndIdGoiThau(f.getSoQdPdKqLcnt(), gthauDc.getId());
							if (hhHopDongHdr.isPresent() && !hhHopDongHdr.get().getTrangThai().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId())) {
								soHdDaKy += 1;
							}
						}
					}
				} else {
					for (HhQdKhlcntDsgthau gthau : hhQdKhlcntDsgthauData) {
						if (gthau.getTrangThaiDt() != null && gthau.getTrangThaiDt().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId())) {
							soGthauTrung += 1;
						}
						Optional<HhHopDongHdr> hhHopDongHdr = hhHopDongRepository.findBySoQdKqLcntAndIdGoiThau(f.getSoQdPdKqLcnt(), gthau.getId());
						if (hhHopDongHdr.isPresent() && !hhHopDongHdr.get().getTrangThai().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId())) {
							soHdDaKy += 1;
						}
					}
				}
				f.setSoGthauTrung((long) soGthauTrung);
				f.setSoHdDaKy((long) soHdDaKy);
			}
		} else {
			List<Long> ids = data.getContent().stream().map(req.getLastest() == 1 ? HhQdKhlcntHdr::getIdGoc : HhQdKhlcntHdr::getId).collect(Collectors.toList());
			List<Object[]> listGthau = hhQdKhlcntDtlRepository.countAllBySoGthau(ids);
			List<Object[]> listGthau2 = hhQdKhlcntDtlRepository.countAllBySoGthauStatus(ids,NhapXuatHangTrangThaiEnum.THANH_CONG.getId());
			List<Object[]> listGthau3 = hhQdKhlcntDtlRepository.countAllBySoGthauStatus(ids,NhapXuatHangTrangThaiEnum.THAT_BAI.getId());
			List<Object[]> listSum = hhQdKhlcntDtlRepository.sumTongTienByIdHdr(ids);
			Map<String,String> hashMapSum = new HashMap<>();
			for (Object[] it: listSum) {
				hashMapSum.put(it[0].toString(),it[1].toString());
			}
			Map<String,String> soGthau = new HashMap<>();
			Map<String,String> soGthau2 = new HashMap<>();
			Map<String,String> soGthau3 = new HashMap<>();
			for (HhQdKhlcntHdr f : data.getContent()) {
				if(f.getIdTrHdr() == null){
					List<HhQdKhlcntDtl> hhQdKhlcntDtl = hhQdKhlcntDtlRepository.findAllByIdQdHdr(f.getId());
					hhQdKhlcntDtl.forEach(item ->{
						f.setSoTrHdr(item.getSoDxuat());
					});
				}
				f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
				f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
				f.setTongTien(StringUtils.isEmpty(hashMapSum.get(f.getId().toString())) ? BigDecimal.valueOf(0) : BigDecimal.valueOf(Long.parseLong(hashMapSum.get(f.getId().toString()))));
				f.setNamKhoach(f.getNamKhoach());
			}
			for (Object[] it: listGthau) {
				soGthau.put(it[0].toString(),it[1].toString());
			}
			for (Object[] it: listGthau2) {
				soGthau2.put(it[0].toString(),it[1].toString());
			}
			for (Object[] it: listGthau3) {
				soGthau3.put(it[0].toString(),it[1].toString());
			}
			for (HhQdKhlcntHdr qd:data.getContent()) {
				qd.setTenPthucLcnt(hashMapPthucDthau.get(qd.getPthucLcnt()));
				qd.setSoGthau(StringUtils.isEmpty(soGthau.get((req.getLastest() == 1 ? qd.getIdGoc() : qd.getId()).toString())) ? 0 : Long.parseLong(soGthau.get((req.getLastest() == 1 ? qd.getIdGoc() : qd.getId()).toString())));
				qd.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qd.getTrangThai()));
				qd.setSoGthauTrung(StringUtils.isEmpty(soGthau2.get((req.getLastest() == 1 ? qd.getIdGoc() : qd.getId()).toString())) ? 0 : Long.parseLong(soGthau2.get((req.getLastest() == 1 ? qd.getIdGoc() : qd.getId()).toString())));
				qd.setSoGthauTruot(StringUtils.isEmpty(soGthau3.get((req.getLastest() == 1 ? qd.getIdGoc() : qd.getId()).toString())) ? 0 : Long.parseLong(soGthau3.get((req.getLastest() == 1 ? qd.getIdGoc() : qd.getId()).toString())));
				if(!ObjectUtils.isEmpty(qd.getIdTrHdr())){
					Optional<HhDxuatKhLcntHdr> byId = hhDxuatKhLcntHdrRepository.findById(qd.getIdTrHdr());
					if(byId.isPresent()){
						qd.setTgianNhang(byId.get().getTgianNhang());
					}
				}
			}
		}
		return data;
	}


	@Override
	public List<HhQdKhlcntHdr> getAll(HhQdKhlcntSearchReq req) throws Exception {
		List<HhQdKhlcntHdr> listData =  hhQdKhlcntHdrRepository.selectAll(req.getNamKhoach(),req.getLoaiVthh(),req.getCloaiVthh(), req.getSoQd(), convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()),req.getTrangThai());
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		if (req.getLoaiVthh() != null && req.getLoaiVthh().startsWith("02")) {
			listData.forEach(qd -> {
				qd.setTenLoaiVthh(StringUtils.isEmpty(qd.getLoaiVthh()) ? null : hashMapDmHh.get(qd.getLoaiVthh()));
				detailVt(qd);
			});
		} else {
			listData.forEach(f -> {
				f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
				f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
			});
		}
		return listData;
	}

	@Override
	public ReportTemplateResponse preview(HhQdKhlcntHdrReq objReq) throws Exception {
		Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(objReq.getId());
		if (!qOptional.isPresent()) {
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		}
		ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
		byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
		HhQdKhlcntPreview object = new HhQdKhlcntPreview();
		if (objReq.getLoaiVthh().startsWith("02")) {
			Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
			Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
			Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
			Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
			object.setHthucLcnt(hashMapHtLcnt.get(qOptional.get().getHthucLcnt()));
			object.setTgianThienHd(qOptional.get().getTgianThienHd() + " ngày (" + qOptional.get().getDienGiai() + ")");
			if (qOptional.get().getIdTrHdr() != null) {
				Optional<HhDxuatKhLcntHdr> dxuatKhLcntHdr = hhDxuatKhLcntHdrRepository.findById(qOptional.get().getIdTrHdr());
				if (dxuatKhLcntHdr.isPresent()) {
					object.setNguonVon(hashMapNguonVon.get(dxuatKhLcntHdr.get().getNguonVon()));
					object.setTgianBdauTchuc("Quý " + dxuatKhLcntHdr.get().getQuy() + "/" + dxuatKhLcntHdr.get().getNamKhoach());
				}
			}
			List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauData = hhQdKhlcntDsgthauRepository.findByIdQdHdr(qOptional.get().getId());
			for(HhQdKhlcntDsgthau dsg : hhQdKhlcntDsgthauData){
				List<HhQdKhlcntDsgthauCtiet> listGtCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(dsg.getId());
				listGtCtiet.forEach(f -> {
					f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
				});
				dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
				dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
				dsg.setChildren(listGtCtiet);
			}
			object.setDsGthau(hhQdKhlcntDsgthauData);
		} else {
			Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
			Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
			List<HhQdKhlcntDtl> hhQdKhlcntDtlList = new ArrayList<>();
			List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauData = new ArrayList<>();
			for(HhQdKhlcntDtl dtl : hhQdKhlcntDtlRepository.findAllByIdQdHdr(objReq.getId())){
				dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
				hhQdKhlcntDsgthauData = hhQdKhlcntDsgthauRepository.findByIdQdDtl(dtl.getId());
				for(HhQdKhlcntDsgthau dsg : hhQdKhlcntDsgthauData){
					List<HhQdKhlcntDsgthauCtiet> listGtCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(dsg.getId());
					listGtCtiet.forEach(chiCuc -> chiCuc.setTenDvi(mapDmucDvi.get(chiCuc.getMaDvi())));
					if (dsg.getDonGiaTamTinh() != null && dsg.getSoLuong() != null) {
						dsg.setThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(dsg.getDonGiaTamTinh().multiply(dsg.getSoLuong())));
					}
					dsg.setChildren(listGtCtiet);
				}
				dtl.setChildren(hhQdKhlcntDsgthauData);
				dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
				hhQdKhlcntDtlList.add(dtl);
			}
			object.setQdKhlcntDtls(hhQdKhlcntDtlList);
			object.setNamKhoach(qOptional.get().getNamKhoach().toString());
			object.setTenCloaiVthh(hashMapDmHh.get(qOptional.get().getCloaiVthh()).toUpperCase());
		}
		return docxToPdfConverter.convertDocxToPdf(inputStream, object);
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
		String[] rowsName = new String[] { "STT", "Năm kế hoạch", "Số quyết định", "Ngày quyết định","Trình yếu","Số KH/tờ trình", "Mã tổng hợp",
				"Loại hàng hóa", "Chủng loại hàng hóa", "Tổng số gói thầu", "Số gói thầu đã trúng", "SL HĐ đã ký", "Thời hạn nhập kho", "Thời gian thực hiện dự án", "Trạng thái" };


		String filename = "Quyet_dinh_ke_hoach_lcnt-tong-cuc.xlsx";
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();

		// Lay danh muc dung chung
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs = null;
		for (int i = 0; i < data.size(); i++) {
			HhQdKhlcntHdr qd = data.get(i);
			objs = new Object[rowsName.length];
			objs[0] = i;
			objs[1] = qd.getNamKhoach();
			objs[2] = qd.getSoQd();
			objs[3] = qd.getNgayQd();
			objs[4] = qd.getTrichYeu();
			objs[5] = qd.getSoTrHdr();
			objs[6] = qd.getIdThHdr();
			objs[7] = StringUtils.isEmpty(qd.getLoaiVthh()) ? null : hashMapDmHh.get(qd.getLoaiVthh());
			objs[8] = StringUtils.isEmpty(qd.getCloaiVthh()) ? null : hashMapDmHh.get(qd.getCloaiVthh());
			objs[9] = qd.getSoGthau();
			objs[10] = qd.getSoGthauTrung();
			objs[11] = null;
			objs[12] = qd.getTgianNhang();
			objs[13] = qd.getTgianThien();
			objs[14] = NhapXuatHangTrangThaiEnum.getTenById(qd.getTrangThai());
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
			objs[5] = qd.getTenLoaiVthh();
			objs[6] = qd.getTenCloaiVthh();
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
