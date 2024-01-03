package com.tcdt.qlnvhang.service.nhaphang.dauthau.dieuchinh;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.HhSlNhapHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.*;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.HhSlNhapHangRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDChinhSearchReq;
import com.tcdt.qlnvhang.service.HhQdKhlcntHdrService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DchinhDxuatKhLcntService extends BaseServiceImpl  {

	@Autowired
	private HhDchinhDxKhLcntHdrRepository hdrRepository;

	@Autowired
	private HhDchinhDxKhLcntDtlRepository dtlRepository;

	@Autowired
	private HhDchinhDxKhLcntDsgthauRepository gThauRepository;

	@Autowired
	private HhDchinhDxKhLcntDsgthauCtietRepository gThauCietRepository;
	@Autowired
	private HhDchinhDxKhLcntDsgthauCtietVtRepository gThauCietVtRepository;

	@Autowired
	private HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

	@Autowired
	private HhQdKhlcntHdrService hhQdKhlcntHdrService;

	@Autowired
	private HhQdKhlcntDtlRepository hhQdKhlcntDtlRepository;

	@Autowired
	private HhQdKhlcntDsgthauRepository hhQdKhlcntDsgthauRepository;

	@Autowired
	private HhQdKhlcntDsgthauCtietRepository hhQdKhlcntDsgthauCtietRepository;

	@Autowired
	private HhQdKhlcntDsgthauCtietVtRepository hhQdKhlcntDsgthauCtietVtRepository;

	@Autowired
	private FileDinhKemService fileDinhKemService;

	@Autowired
	private HhSlNhapHangRepository hhSlNhapHangRepository;

	public Page<HhDchinhDxKhLcntHdr> getAllPage(QlnvQdLcntHdrDChinhSearchReq objReq) throws Exception {
		int page = objReq.getPaggingReq().getPage();
		int limit = objReq.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
		Page<HhDchinhDxKhLcntHdr> data = hdrRepository.selectPage(objReq.getNam(),objReq.getSoQdDc(), objReq.getTrichYeu(),objReq.getLoaiVthh(),
				convertDateToString(objReq.getTuNgayQd()),
				convertDateToString(objReq.getDenNgayQd()),
				pageable);
		List<Long> ids = data.getContent().stream().map(HhDchinhDxKhLcntHdr::getId).collect(Collectors.toList());
		List<Object[]> listGthau = hdrRepository.countAllBySoGthau(ids);
		List<Object[]> listGthau2 = hdrRepository.countAllBySoGthauStatus(ids, NhapXuatHangTrangThaiEnum.THANH_CONG.getId());
		Map<String,String> soGthau = new HashMap<>();
		Map<String,String> soGthau2 = new HashMap<>();
		for (Object[] it: listGthau) {
			soGthau.put(it[0].toString(),it[1].toString());
		}
		for (Object[] it: listGthau2) {
			soGthau2.put(it[0].toString(),it[1].toString());
		}
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		data.getContent().forEach(f->{
			f.setTenLoaiVthh(hashMapDmHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(hashMapDmHh.get(f.getCloaiVthh()));
			f.setGthauTrung(StringUtils.isEmpty(soGthau2.get(f.getId().toString())) ? 0 : Integer.parseInt(soGthau2.get(f.getId().toString())));
			f.setSoGoiThau(StringUtils.isEmpty(soGthau.get(f.getId().toString())) ? 0 : Long.parseLong(soGthau.get(f.getId().toString())));
		});
		return data;
	}

	@Transactional(rollbackOn = Exception.class)
	public HhDchinhDxKhLcntHdr save (DchinhDxKhLcntHdrReq objReq) throws Exception {
		if(objReq.getLoaiVthh().startsWith("02")){
			return saveVatTu(objReq);
		}else{
			// Lương thực
			return saveLuongThuc(objReq);
		}
	}

	private HhDchinhDxKhLcntHdr saveVatTu (DchinhDxKhLcntHdrReq objReq) throws Exception {

		this.validateCreate(objReq);

		// Add danh sach file dinh kem o Master

		HhDchinhDxKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDchinhDxKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
//		dataMap.setNam(LocalDate.now().getYear());
		dataMap.setTrangThai(Contains.DA_LAP);
		dataMap.setNguoiTaoId(getUser().getId());
		hdrRepository.save(dataMap);
		updateFile(objReq, dataMap);
		saveDetail(dataMap.getId(),objReq);
		return dataMap;
	}

	private HhDchinhDxKhLcntHdr saveLuongThuc (DchinhDxKhLcntHdrReq objReq) throws Exception {

		this.validateCreate(objReq);

		HhDchinhDxKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDchinhDxKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.DA_LAP);
		dataMap.setNguoiTaoId(getUser().getId());

		hdrRepository.save(dataMap);
		updateFile(objReq, dataMap);
		List<HhQdKhlcntDtl> hhQdKhlcntDtl =  hhQdKhlcntDtlRepository.findByIdQdHdr(dataMap.getIdQdGoc());
		hhQdKhlcntDtl.forEach(item -> {
			item.setIdDcDxHdr(dataMap.getId());
			hhQdKhlcntDtlRepository.save(item);
		});
		this.saveCtietLT(dataMap.getId(),objReq, true);
		dataMap.setChildren(dtlRepository.findAllByIdDxDcHdrOrderByMaDvi(dataMap.getId()));
		return dataMap;
	}

	private void validateCreate (DchinhDxKhLcntHdrReq objReq) throws Exception {
		List<HhQdKhlcntHdr> checkSoQdGoc = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (checkSoQdGoc.isEmpty()){
			throw new Exception("Không tìm thấy số đề xuất để điều chỉnh kế hoạch lựa chọn nhà thầu");
		}

		Optional<HhDchinhDxKhLcntHdr> checkSoQd = hdrRepository.findBySoQdDc(objReq.getSoQdDc());
		if (checkSoQd.isPresent()){
			throw new Exception("Số quyết định " + objReq.getSoQdDc() + " đã tồn tại");
		}
		List<HhDchinhDxKhLcntHdr> checkBanHanh = hdrRepository.findAllByIdQdGocAndTrangThaiNot(objReq.getIdQdGoc(), NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
		if (!checkBanHanh.isEmpty()) {
			throw new Exception("Số quyết định gốc " + objReq.getSoQdGoc() + " có quyết định điều chỉnh chưa được ban hành.");
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public HhDchinhDxKhLcntHdr update (DchinhDxKhLcntHdrReq objReq) throws Exception {
		if(objReq.getLoaiVthh().startsWith("02")){
			return updateVatTu(objReq);
		}else{
			// Lương thực
			return updateLuongThuc(objReq);
		}
	}

	private HhDchinhDxKhLcntHdr updateVatTu (DchinhDxKhLcntHdrReq objReq) throws Exception {
		HhDchinhDxKhLcntHdr hdrData = this.validateUpdate(objReq);
		HhDchinhDxKhLcntHdr dataDB = hdrData;
		HhDchinhDxKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDchinhDxKhLcntHdr.class);
		updateObjectToObject(dataDB, dataMap);
		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSuaId(getUser().getId());
		updateFile(objReq, dataMap);
		hdrRepository.save(dataDB);
//		this.deleteDataOld(objReq);
		this.saveDetail(dataMap.getId(),objReq);
		return dataMap;
	}

	private HhDchinhDxKhLcntHdr updateLuongThuc (DchinhDxKhLcntHdrReq objReq) throws Exception {
		HhDchinhDxKhLcntHdr hdrData = this.validateUpdate(objReq);
		HhDchinhDxKhLcntHdr dataDB = hdrData;
		HhDchinhDxKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDchinhDxKhLcntHdr.class);
		updateObjectToObject(dataDB, dataMap);
		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSuaId(getUser().getId());
		hdrRepository.save(dataDB);
		updateFile(objReq, dataMap);
		this.deleteDataOld(objReq);
		this.saveCtietLT(dataMap.getId(),objReq, false);
		return dataMap;
	}

	private HhDchinhDxKhLcntHdr validateUpdate (DchinhDxKhLcntHdrReq objReq) throws Exception {
		if (StringUtils.isEmpty(objReq.getId())){
			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
		}
		Optional<HhDchinhDxKhLcntHdr> hdrData = hdrRepository.findById(Long.valueOf(objReq.getId()));

		if (!hdrData.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu cần sửa");
		}

		List<HhQdKhlcntHdr> checkSoQdGoc = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (checkSoQdGoc.isEmpty()){
			throw new Exception("Không tìm thấy số đề xuất để điều chỉnh kế hoạch lựa chọn nhà thầu");
		}

		if (objReq.getSoQdDc() != null && hdrData.get().getSoQdDc() != null && !hdrData.get().getSoQdDc().equals(objReq.getSoQdDc())) {
			Optional<HhDchinhDxKhLcntHdr> checkSoQd = hdrRepository.findBySoQdDc(objReq.getSoQdDc());
			if (checkSoQd.isPresent()){
				throw new Exception("Số quyết định " + objReq.getSoQdDc() + " đã tồn tại");
			}
		}

		return hdrData.get();
	}

	public void deleteDataOld(DchinhDxKhLcntHdrReq objReq){
		dtlRepository.deleteAllByIdDxDcHdr(objReq.getId());
		for (HhQdKhlcntDtlReq dx : objReq.getChildren()) {
			if(!ObjectUtils.isEmpty(dx.getId()) ){
				gThauRepository.deleteAllByIdDcDxDtl(dx.getId());
			}
			for (HhQdKhlcntDsgthauReq gt : dx.getDsGoiThau()) {
				if(!ObjectUtils.isEmpty(gt.getId()) ){
					gThauCietRepository.deleteAllByIdGoiThau(gt.getId());
				}
			}
		}

	}

	public void saveCtietLT(Long idHdr , DchinhDxKhLcntHdrReq objReq, boolean isThemMoi){
		for (HhQdKhlcntDtlReq dx : objReq.getChildren()){
			HhDchinhDxKhLcntDtl qd = ObjectMapperUtils.map(dx, HhDchinhDxKhLcntDtl.class);
			if (isThemMoi) {
				qd.setIdHhQdKhlcntDtl(dx.getId());
			}
			qd.setId(null);
			qd.setIdDxDcHdr(idHdr);
			qd.setTchuanCluong(objReq.getTchuanCluong());
			qd.setHthucLcnt(objReq.getHthucLcnt());
			qd.setPthucLcnt(objReq.getPthucLcnt());
			qd.setLoaiHdong(objReq.getLoaiHdong());
			dtlRepository.save(qd);
			for (HhQdKhlcntDsgthauReq gtList : dx.getChildren()){
				HhDchinhDxKhLcntDsgthau gt = ObjectMapperUtils.map(gtList, HhDchinhDxKhLcntDsgthau.class);
				gt.setId(null);
				gt.setIdDcDxDtl(qd.getId());
//				gt.setThanhTien(gt.getDonGia().multiply(gt.getSoLuong()));
				gt.setTrangThai(Contains.CHUATAO_QD);
				gt.setIdGthauDx(gtList.getId());
				gThauRepository.save(gt);
				for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gtList.getChildren()){
					HhDchinhDxKhLcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDchinhDxKhLcntDsgthauCtiet.class);
					dataDdNhap.setId(null);
					dataDdNhap.setIdGoiThau(gt.getId());
//					dataDdNhap.setThanhTien(dataDdNhap.getDonGia().multiply(dataDdNhap.getSoLuong()));
					gThauCietRepository.save(dataDdNhap);
					for(HhDxuatKhLcntDsgthauDtlCtietVtReq dKho : ddNhap.getChildren()){
						HhDchinhDxKhLcntDsgthauCtietVt datadKho = new ModelMapper().map(dKho, HhDchinhDxKhLcntDsgthauCtietVt.class);
						datadKho.setId(null);
						datadKho.setIdGoiThauCtiet(dataDdNhap.getId());
						gThauCietVtRepository.save(datadKho);
					}
				}
			}
		}
	}

	@Transactional
	public void saveDetail(Long idHdr, DchinhDxKhLcntHdrReq objReq) {
		if (objReq.getDsGoiThau() != null) {
			gThauRepository.deleteAllByIdDcDxHdr(idHdr);
			for (HhQdKhlcntDsgthauReq gtList : objReq.getDsGoiThau()) {
				HhDchinhDxKhLcntDsgthau gt = ObjectMapperUtils.map(gtList, HhDchinhDxKhLcntDsgthau.class);
				gt.setId(null);
				gt.setIdDcDxHdr(idHdr);
//			gt.setTrangThai(Contains.CHUATAO_QD);
				gThauRepository.save(gt);
				gThauCietRepository.deleteAllByIdGoiThau(gt.getId());
				for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gtList.getChildren()) {
					HhDchinhDxKhLcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDchinhDxKhLcntDsgthauCtiet.class);
					dataDdNhap.setId(null);
					dataDdNhap.setIdGoiThau(gt.getId());
					gThauCietRepository.save(dataDdNhap);
				}
			}
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public HhDchinhDxKhLcntHdr approve (StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");
		Optional<HhDchinhDxKhLcntHdr>optional=hdrRepository.findById(stReq.getId());
		if (!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		String status = stReq.getTrangThai() + optional.get().getTrangThai();
		if (optional.get().getLoaiVthh().startsWith("02")) {
			if ((Contains.CHODUYET_LDV + Contains.DA_LAP).equals(status)) {
				optional.get().setNguoiGuiDuyetId(getUser().getId());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
			} else if ((Contains.CHODUYET_LDV + Contains.TUCHOI_LDV).equals(status)) {
				optional.get().setNguoiGuiDuyetId(getUser().getId());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
			} else if ((Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC).equals(status)) {
				optional.get().setNguoiGuiDuyetId(getUser().getId());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
			} else if ((Contains.DADUYET_LDV + Contains.CHODUYET_LDV).equals(status)) {
				optional.get().setNguoiPduyetId(getUser().getId());
				optional.get().setNgayPduyet(getDateTimeNow());
			} else if ((Contains.BAN_HANH + Contains.DADUYET_LDV).equals(status)) {
				Optional<HhDchinhDxKhLcntHdr> dchinhDxKhLcntHdr = hdrRepository.findByIdQdGocAndLastest(optional.get().getIdQdGoc(), Boolean.TRUE);
				if (dchinhDxKhLcntHdr.isPresent()) {
					dchinhDxKhLcntHdr.get().setLastest(Boolean.FALSE);
					hdrRepository.save(dchinhDxKhLcntHdr.get());
				}
				optional.get().setNguoiPduyetId(getUser().getId());
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLastest(Boolean.TRUE);
			} else if ((Contains.TUCHOI_LDV + Contains.CHODUYET_LDV).equals(status)) {
				optional.get().setNguoiPduyetId(getUser().getId());
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLyDoTuChoi(stReq.getLyDo());
			} else if ((Contains.TUCHOI_LDTC + Contains.DADUYET_LDV).equals(status)) {
				optional.get().setNguoiPduyetId(getUser().getId());
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLyDoTuChoi(stReq.getLyDo());
			} else {
				throw new Exception("Phê duyệt không thành công");
			}
		} else {
			if ((Contains.CHODUYET_LDV + Contains.DA_LAP).equals(status)) {
				optional.get().setNguoiGuiDuyetId(getUser().getId());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
			} else if ((Contains.CHODUYET_LDV + Contains.TUCHOI_LDV).equals(status)) {
				optional.get().setNguoiGuiDuyetId(getUser().getId());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
			} else if ((Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC).equals(status)) {
				optional.get().setNguoiGuiDuyetId(getUser().getId());
				optional.get().setNgayGuiDuyet(getDateTimeNow());
			} else if ((Contains.DADUYET_LDV + Contains.CHODUYET_LDV).equals(status)) {
				optional.get().setNguoiPduyetId(getUser().getId());
				optional.get().setNgayPduyet(getDateTimeNow());
			} else if ((Contains.BAN_HANH + Contains.DADUYET_LDV).equals(status)) {
				optional.get().setNguoiPduyetId(getUser().getId());
				optional.get().setNgayPduyet(getDateTimeNow());
			} else if ((Contains.TUCHOI_LDV + Contains.CHODUYET_LDV).equals(status)) {
				optional.get().setNguoiPduyetId(getUser().getId());
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLyDoTuChoi(stReq.getLyDo());
			} else if ((Contains.TUCHOI_LDTC + Contains.DADUYET_LDV).equals(status)) {
				optional.get().setNguoiPduyetId(getUser().getId());
				optional.get().setNgayPduyet(getDateTimeNow());
				optional.get().setLyDoTuChoi(stReq.getLyDo());
			} else {
				throw new Exception("Phê duyệt không thành công");
			}
		}
		optional.get().setTrangThai(stReq.getTrangThai());
		hdrRepository.save(optional.get());
		if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
			if (optional.get().getLoaiVthh().startsWith("02")) {
				capNhatSoLuongNhap(optional.get());
			}else {
				this.updateDataQdGoc(optional.get());
			}
		}
		return optional.get();
	}

	private void updateDataQdGoc(HhDchinhDxKhLcntHdr qdDieuChinh) throws Exception {
		HhQdKhlcntHdr hdr = hhQdKhlcntHdrService.detail(qdDieuChinh.getIdQdGoc().toString());
		HhDchinhDxKhLcntHdr dchinh = detail(qdDieuChinh.getId().toString());
//		BeanUtils.copyProperties(dchinh, hdr, "id", "soQd");
//		hhQdKhlcntHdrRepository.save(hdr);
//		hhQdKhlcntDtlRepository.deleteAllByIdQdHdr(hdr.getId());
		hhSlNhapHangRepository.deleteAllByIdQdKhlcnt(hdr.getId());
		for (HhQdKhlcntDtl qdDtl : hdr.getChildren()) {
			for (HhDchinhDxKhLcntDtl dtl : dchinh.getChildren()) {
				if (dtl.getIdHhQdKhlcntDtl().equals(qdDtl.getId())) {
					qdDtl.setTgianBdauTchuc(dtl.getTgianBdauTchuc());
					qdDtl.setTgianMthau(dtl.getTgianMthau());
					qdDtl.setTgianDthau(dtl.getTgianDthau());
					qdDtl.setTgianMthauTime(dtl.getTgianMthauTime());
					qdDtl.setTgianDthauTime(dtl.getTgianDthauTime());
					qdDtl.setTgianNhang(dtl.getTgianNhang());
					qdDtl.setTgianMoHoSo(dtl.getTgianMoHoSo());
					qdDtl.setTgianMoHoSoTime(dtl.getTgianMoHoSoTime());
					qdDtl.setGiaBanHoSo(dtl.getGiaBanHoSo());
					BigDecimal tongSl = BigDecimal.ZERO;
					if (dtl.getChildren() != null && dtl.getChildren().size() > 0) {
						hhQdKhlcntDsgthauRepository.deleteByIdQdDtl(qdDtl.getId());
						for (HhDchinhDxKhLcntDsgthau dsgThau : dtl.getChildren()) {
							HhQdKhlcntDsgthau gThau = new HhQdKhlcntDsgthau();
							BeanUtils.copyProperties(dsgThau, gThau, "id");
							gThau.setIdQdDtl(qdDtl.getId());
							hhQdKhlcntDsgthauRepository.save(gThau);
							hhQdKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gThau.getId());
							tongSl = tongSl.add(gThau.getSoLuong());
							for (HhDchinhDxKhLcntDsgthauCtiet dsDdNhap : dsgThau.getChildren()) {
								HhQdKhlcntDsgthauCtiet ddNhap = new HhQdKhlcntDsgthauCtiet();
								BeanUtils.copyProperties(dsDdNhap, ddNhap, "id");
								ddNhap.setIdGoiThau(gThau.getId());
								hhQdKhlcntDsgthauCtietRepository.save(ddNhap);
								for(HhDchinhDxKhLcntDsgthauCtietVt dKho : dsDdNhap.getChildren()){
									HhQdKhlcntDsgthauCtietVt ctietVt = new ModelMapper().map(dKho, HhQdKhlcntDsgthauCtietVt.class);
									ctietVt.setId(null);
									ctietVt.setIdGoiThauCtiet(dsDdNhap.getId());
									hhQdKhlcntDsgthauCtietVtRepository.save(ctietVt);
								}
							}
						}
					}
					qdDtl.setSoLuong(tongSl);
					hhQdKhlcntDtlRepository.save(qdDtl);
					HhSlNhapHang slNhapHang = HhSlNhapHang.builder()
							.loaiVthh(hdr.getLoaiVthh())
							.cloaiVthh(hdr.getCloaiVthh())
							.idQdKhlcnt(hdr.getId())
							.namKhoach(hdr.getNamKhoach())
							.soLuong(qdDtl.getSoLuong())
							.maDvi(qdDtl.getMaDvi())
							.kieuNhap("NHAP_DAU_THAU")
							.build();
					hhSlNhapHangRepository.save(slNhapHang);
				}
			}
		}
	}

	private void capNhatSoLuongNhap(HhDchinhDxKhLcntHdr data) {
		hhSlNhapHangRepository.deleteAllByIdQdKhlcnt(data.getIdQdGoc());
		List<HhDchinhDxKhLcntDsgthau> gThauList = gThauRepository.findAllByIdDcDxHdrOrderByGoiThau(data.getId());
		for(HhDchinhDxKhLcntDsgthau gThau : gThauList) {
			List<HhDchinhDxKhLcntDsgthauCtiet> gthauCtietList = gThauCietRepository.findAllByIdGoiThau(gThau.getId());
			for(HhDchinhDxKhLcntDsgthauCtiet ctiet : gthauCtietList) {
				HhSlNhapHang slNhapHang = HhSlNhapHang.builder()
						.loaiVthh(gThau.getLoaiVthh())
						.cloaiVthh(gThau.getCloaiVthh())
						.idQdKhlcnt(data.getIdQdGoc())
						.namKhoach(data.getNam())
						.soLuong(ctiet.getSoLuong())
						.maDvi(ctiet.getMaDvi())
						.kieuNhap("NHAP_DAU_THAU")
						.build();
				hhSlNhapHangRepository.save(slNhapHang);
			}
		}
	}

	public HhDchinhDxKhLcntHdr detail(String ids){
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhDchinhDxKhLcntHdr> qOptional = hdrRepository.findById(Long.parseLong(ids));
		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("HINH_THUC_HOP_DONG");

		qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
		qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));
		qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
		qOptional.get().setTenHthucLcnt(hashMapHtLcnt.get(qOptional.get().getHthucLcnt()));
		qOptional.get().setTenPthucLcnt(hashMapPthucDthau.get(qOptional.get().getPthucLcnt()));
		qOptional.get().setTenLoaiHdong(hashMapLoaiHdong.get(qOptional.get().getLoaiHdong()));
		qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
		if (qOptional.get().getLoaiVthh().startsWith("02")) {
			getDetailVt(qOptional.get());
		} else {
			getDetailLt(qOptional.get(), mapDmucDvi, hashMapDmHh, hashMapLoaiHdong, hashMapNguonVon, hashMapPthucDthau, hashMapHtLcnt);
		}
		return qOptional.get();
	}

	private void getDetailLt (HhDchinhDxKhLcntHdr data, Map<String, String> mapDmucDvi, Map<String,String> hashMapDmHh,
							  Map<String,String> hashMapLoaiHdong, Map<String,String> hashMapNguonVon, Map<String,String> hashMapPthucDthau,
							  Map<String,String> hashMapHtLcnt) {
		List<HhDchinhDxKhLcntDtl> dtlList = new ArrayList<>();
		for(HhDchinhDxKhLcntDtl dtl : dtlRepository.findAllByIdDxDcHdrOrderByMaDvi(data.getId())){
			List<HhDchinhDxKhLcntDsgthau> gThauList = new ArrayList<>();
			for(HhDchinhDxKhLcntDsgthau gThau : gThauRepository.findAllByIdDcDxDtlOrderByGoiThau(dtl.getId())){
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
				gThau.setTenCloaiVthh(hashMapDmHh.get(gThau.getCloaiVthh()));
				gThau.setTenLoaiHdong(hashMapLoaiHdong.get(gThau.getLoaiHdong()));
				gThau.setTenNguonVon(hashMapNguonVon.get(gThau.getNguonVon()));
				gThau.setTenPthucLcnt(hashMapPthucDthau.get(gThau.getPthucLcnt()));
				gThau.setTenHthucLcnt(hashMapHtLcnt.get(gThau.getHthucLcnt()));
				gThau.setChildren(gthauCtietList);
				gThauList.add(gThau);
			};
			dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
			dtl.setChildren(gThauList);
			dtlList.add(dtl);
		}
		data.setChildren(dtlList);
		List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singletonList("HH_DC_DX_LCNT_HDR"  + "_CAN_CU"));
		data.setListCcPhapLy(canCu);
		List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Collections.singletonList("HH_DC_DX_LCNT_HDR"));
		data.setFileDinhKems(fileDinhKems);
		List<FileDinhKem> fileDinhKemsTtr = fileDinhKemService.search(data.getId(), Collections.singletonList("HH_DC_DX_LCNT_HDR" + "_TTR"));
		data.setFileDinhKemsTtr(fileDinhKemsTtr);
	}

	private void getDetailVt(HhDchinhDxKhLcntHdr data) {
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
		data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : hashMapDmHh.get(data.getCloaiVthh()));
		data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
		List<HhDchinhDxKhLcntDsgthau> gThauList = gThauRepository.findAllByIdDcDxHdrOrderByGoiThau(data.getId());
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
			gThau.setTenCloaiVthh(hashMapDmHh.get(gThau.getCloaiVthh()));
			gThau.setChildren(gthauCtietList);
		}
		data.setDsGthau(gThauList);
		List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singletonList("HH_DC_DX_LCNT_HDR"  + "_CAN_CU"));
		data.setListCcPhapLy(canCu);
		List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Collections.singletonList("HH_DC_DX_LCNT_HDR"));
		data.setFileDinhKems(fileDinhKems);
		List<FileDinhKem> fileDinhKemsTtr = fileDinhKemService.search(data.getId(), Collections.singletonList("HH_DC_DX_LCNT_HDR" + "_TTR"));
		data.setFileDinhKemsTtr(fileDinhKemsTtr);
	}

	public  void export(QlnvQdLcntHdrDChinhSearchReq objReq, HttpServletResponse response) throws Exception{
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		objReq.setPaggingReq(paggingReq);
		Page<HhDchinhDxKhLcntHdr> page=this.getAllPage(objReq);
		List<HhDchinhDxKhLcntHdr> data=page.getContent();

		String title="Danh sách quyết định điều chỉnh kế hoạch lựa chọn nhà thầu";
		String[] rowsName=new String[]{"STT","Số quyết định điều chỉnh KH LCNT","Ngày ký","Trích yếu","Số QĐ duyệt KHLCNT","Năm kế hoạch","Loại hàng hóa","số gói thầu","Trạng thái"};
		String fileName="danh-sach-qd-dieu-chinh-khlcnt.xlsx";
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs=null;
		for (int i=0;i<data.size();i++){
			HhDchinhDxKhLcntHdr dx=data.get(i);
			objs=new Object[rowsName.length];
			objs[0]=i;
			objs[1]=dx.getSoQdDc();
			objs[2]=dx.getNgayQd();
			objs[3]=dx.getTrichYeu();
			objs[4]=dx.getSoQdGoc();
			objs[5]=dx.getNam();
			objs[6]=dx.getTenLoaiVthh();
			objs[7]=dx.getSoGoiThau();
			objs[8]=dx.getTenTrangThai();
			dataList.add(objs);
		}
		ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
		ex.export();
	}

	@Transient
	public void delete(IdSearchReq idSearchReq) throws Exception{
		Optional<HhDchinhDxKhLcntHdr> optional= hdrRepository.findById(idSearchReq.getId());
		if (!optional.isPresent()){
			throw new Exception("Bản ghi không tồn tại");
		}
		if (optional.get().getLoaiVthh().startsWith("02")) {
			if (!optional.get().getTrangThai().equals(Contains.DA_LAP)){
				throw new Exception("Chỉ cho phép xóa bản ghi ở trạng thái đã lập");
			}
			fileDinhKemService.delete(optional.get().getId(), Lists.newArrayList("HH_DC_DX_LCNT_HDR" + "_CAN_CU"));
			fileDinhKemService.delete(optional.get().getId(), Lists.newArrayList("HH_DC_DX_LCNT_HDR"));
			fileDinhKemService.delete(optional.get().getId(), Lists.newArrayList("HH_DC_DX_LCNT_HDR" + "_TTR"));
			List<HhDchinhDxKhLcntDsgthau> gThauList = gThauRepository.findAllByIdDcDxHdr(optional.get().getId());
			List<Long> idListGthau= gThauList.stream().map(HhDchinhDxKhLcntDsgthau::getId).collect(Collectors.toList());
			List<HhDchinhDxKhLcntDsgthauCtiet> listGthauCtiet=gThauCietRepository.findAllByIdGoiThauIn(idListGthau);
			List<Long> idListGthauCtiet=listGthauCtiet.stream().map(HhDchinhDxKhLcntDsgthauCtiet::getId).collect(Collectors.toList());
			List<HhDchinhDxKhLcntDsgthauCtietVt> listGthauCtietVt=gThauCietVtRepository.findAllByIdGoiThauCtietIn(idListGthauCtiet);
			gThauRepository.deleteAll(gThauList);
			gThauCietRepository.deleteAll(listGthauCtiet);
			gThauCietVtRepository.deleteAll(listGthauCtietVt);
		} else {
			if (!optional.get().getTrangThai().equals(Contains.DA_LAP)){
				throw new Exception("Chỉ cho phép xóa bản ghi ở trạng thái nháp");
			}
			List<HhQdKhlcntDtl> hhQdKhlcntDtl = hhQdKhlcntDtlRepository.findByIdQdHdr(optional.get().getIdQdGoc());
			hhQdKhlcntDtl.forEach(item -> {
				item.setIdDcDxHdr(null);
				hhQdKhlcntDtlRepository.save(item);
			});
			List<HhDchinhDxKhLcntDtl> listDtl=dtlRepository.findAllByIdDxDcHdr(optional.get().getId());
			List<Long> idListDtl=listDtl.stream().map(HhDchinhDxKhLcntDtl::getId).collect(Collectors.toList());
			List<HhDchinhDxKhLcntDsgthau>  listGthau = gThauRepository.findAllByIdDcDxDtlIn(idListDtl);
			List<Long> idListGthau=listGthau.stream().map(HhDchinhDxKhLcntDsgthau::getId).collect(Collectors.toList());
			List<HhDchinhDxKhLcntDsgthauCtiet> listGthauCtiet=gThauCietRepository.findAllByIdGoiThauIn(idListGthau);
			List<Long> idListGthauCtiet=listGthauCtiet.stream().map(HhDchinhDxKhLcntDsgthauCtiet::getId).collect(Collectors.toList());
			List<HhDchinhDxKhLcntDsgthauCtietVt> listGthauCtietVt=gThauCietVtRepository.findAllByIdGoiThauCtietIn(idListGthauCtiet);
			dtlRepository.deleteAll(listDtl);
			gThauRepository.deleteAll(listGthau);
			gThauCietRepository.deleteAll(listGthauCtiet);
			gThauCietVtRepository.deleteAll(listGthauCtietVt);
			fileDinhKemService.delete(optional.get().getId(),  Lists.newArrayList("HH_DC_DX_LCNT_HDR"));
		}
		hdrRepository.delete(optional.get());
	}

	@Transient
	public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
		if (StringUtils.isEmpty(idSearchReq.getIdList()))
			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
		List<HhDchinhDxKhLcntHdr> listDchinh= hdrRepository.findAllByIdIn(idSearchReq.getIdList());
		for (HhDchinhDxKhLcntHdr listDtls : listDchinh) {
			List<HhDchinhDxKhLcntDtl> listDtl = dtlRepository.findAllByIdDxDcHdr(listDtls.getId());
			List<Long> idListDtl = listDtl.stream().map(HhDchinhDxKhLcntDtl::getId).collect(Collectors.toList());
			List<HhDchinhDxKhLcntDsgthau> listGthau = gThauRepository.findAllByIdDcDxDtlIn(idListDtl);
			List<Long> idListGthau = listGthau.stream().map(HhDchinhDxKhLcntDsgthau::getId).collect(Collectors.toList());
			List<HhDchinhDxKhLcntDsgthauCtiet> listGthauCtiet = gThauCietRepository.findAllByIdGoiThauIn(idListGthau);
			dtlRepository.deleteAll(listDtl);
			gThauRepository.deleteAll(listGthau);
			gThauCietRepository.deleteAll(listGthauCtiet);
			fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList("HH_DC_DX_LCNT_HDR"));
		}
		hdrRepository.deleteAllByIdIn(idSearchReq.getIdList());

	}
	@Transient
	public HhDchinhDxKhLcntHdr findByIdQdGoc(Long idQdGoc) {
		Optional<HhDchinhDxKhLcntHdr> qOptional = hdrRepository.findTopByIdQdGocOrderByLanDieuChinhDesc(idQdGoc);
		System.out.println(qOptional);
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
		qOptional.get().setTenTrangThai(TrangThaiAllEnum.getLabelById(qOptional.get().getTrangThai()));
		List<HhDchinhDxKhLcntDtl> dtlList = new ArrayList<>();
		for(HhDchinhDxKhLcntDtl dtl : dtlRepository.findAllByIdDxDcHdrOrderByMaDvi(qOptional.get().getId())){
			List<HhDchinhDxKhLcntDsgthau> gThauList = new ArrayList<>();
			for(HhDchinhDxKhLcntDsgthau gThau : gThauRepository.findAllByIdDcDxDtl(dtl.getId())){
				List<HhDchinhDxKhLcntDsgthauCtiet> gthauCtietList = gThauCietRepository.findAllByIdGoiThau(gThau.getId());
				gthauCtietList.forEach(f -> {
					f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
					f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
					List<HhDchinhDxKhLcntDsgthauCtietVt> gthauCtietVtList = gThauCietVtRepository.findAllByIdGoiThauCtiet(f.getId());
					f.setChildren(gthauCtietVtList);
				});
				gThau.setTenCloaiVthh(hashMapDmHh.get(gThau.getCloaiVthh()));
				gThau.setTenLoaiHdong(hashMapLoaiHdong.get(gThau.getLoaiHdong()));
				gThau.setTenNguonVon(hashMapNguonVon.get(gThau.getNguonVon()));
				gThau.setTenPthucLcnt(hashMapPthucDthau.get(gThau.getPthucLcnt()));
				gThau.setTenHthucLcnt(hashMapHtLcnt.get(gThau.getHthucLcnt()));
				gThau.setChildren(gthauCtietList);
				gThauList.add(gThau);
			};
			dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
			dtl.setChildren(gThauList);
			dtlList.add(dtl);
		}

		qOptional.get().setChildren(dtlList);

		return qOptional.get();
	}

	private void updateFile (DchinhDxKhLcntHdrReq objReq, HhDchinhDxKhLcntHdr dataMap){
		fileDinhKemService.delete(objReq.getId(), Lists.newArrayList("HH_DC_DX_LCNT_HDR" + "_CAN_CU"));
		if (!DataUtils.isNullOrEmpty(objReq.getListCcPhapLy())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getListCcPhapLy(), dataMap.getId(), "HH_DC_DX_LCNT_HDR" + "_CAN_CU");
		}
		fileDinhKemService.delete(objReq.getId(), Lists.newArrayList("HH_DC_DX_LCNT_HDR"));
		if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), "HH_DC_DX_LCNT_HDR");
		}
		fileDinhKemService.delete(objReq.getId(), Lists.newArrayList("HH_DC_DX_LCNT_HDR" + "_TTR"));
		if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKemsTtr())) {
			fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemsTtr(), dataMap.getId(), "HH_DC_DX_LCNT_HDR" + "_TTR");
		}
	}

	public ReportTemplateResponse preview(DchinhDxKhLcntHdrReq objReq) throws Exception {
		if (objReq.getId() == null) {
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		}
		Optional<HhDchinhDxKhLcntHdr> qOptional = hdrRepository.findById(objReq.getId());
		if (!qOptional.isPresent()) {
			throw new UnsupportedOperationException("Không tồn tại bản ghi");
		}
		Optional<HhQdKhlcntHdr> qdKhlcntHdrOptional = hhQdKhlcntHdrRepository.findById(qOptional.get().getIdQdGoc());
		if (!qdKhlcntHdrOptional.isPresent()) {
			throw new UnsupportedOperationException("Không tồn tại bản ghi quyết định gốc");
		}
		ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
		byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		HhDchinhDxKhLcntPreview data = HhDchinhDxKhLcntPreview.builder()
				.namKhoach(qOptional.get().getNam())
				.tenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()).toUpperCase())
				.soQdDcKhlcnt(qOptional.get().getSoQdDc())
				.soQdPdKhlcnt(qOptional.get().getSoQdGoc())
				.ngayPdKhlcnt(convertDate(qdKhlcntHdrOptional.get().getNgayQd()))
				.ngayPdDcKhlcnt(convertDate(qOptional.get().getNgayQdDc()))
				.build();
		if (qOptional.get().getLoaiVthh().startsWith("02")) {

		} else {
			BigDecimal tongThanhTien = BigDecimal.ZERO;
			BigDecimal tongSoLuong = BigDecimal.ZERO;
			BigDecimal tongSoLuongDc = BigDecimal.ZERO;
			List<HhDchinhDxKhLcntDtlPreview> hhDchinhDxKhLcntDtlPreviews = new ArrayList<>();
			HhDchinhDxKhLcntHdr dcLanTruoc = new HhDchinhDxKhLcntHdr();
			if (qOptional.get().getLanDieuChinh() > 1) {
				Optional<HhDchinhDxKhLcntHdr> dcLanTruocOptional = hdrRepository.findByIdQdGocAndLanDieuChinh(objReq.getIdQdGoc(), objReq.getLanDieuChinh() - 1);
				if (!dcLanTruocOptional.isPresent()) {
					throw new UnsupportedOperationException("Không tồn tại bản ghi");
				}
				dcLanTruoc = dcLanTruocOptional.get();
			}
			for(HhDchinhDxKhLcntDtl dtl : dtlRepository.findAllByIdDxDcHdrOrderByMaDvi(qOptional.get().getId())){
				HhDchinhDxKhLcntDtlPreview hhDchinhDxKhLcntDtlPreview = HhDchinhDxKhLcntDtlPreview.builder()
						.tenDvi(mapDmucDvi.get(dtl.getMaDvi()))
						.tgianNhangDc(convertDate(dtl.getTgianNhang()))
						.build();
				HhQdKhlcntDtl qdKhlcntDtl = new HhQdKhlcntDtl();
				HhDchinhDxKhLcntDtl dcLanTruocDtl = new HhDchinhDxKhLcntDtl();
				if (qOptional.get().getLanDieuChinh() > 1) {
					dcLanTruocDtl = dtlRepository.findFirstByIdDxDcHdrAndMaDvi(dcLanTruoc.getId(), dtl.getMaDvi());
					if (dcLanTruocDtl != null) {
						hhDchinhDxKhLcntDtlPreview.setTgianNhang(convertDate(dcLanTruocDtl.getTgianNhang()));
					}
				} else {
					Optional<HhQdKhlcntDtl> qdKhlcntDtlOptional = hhQdKhlcntDtlRepository.findById(dtl.getIdHhQdKhlcntDtl());
					if (!qdKhlcntDtlOptional.isPresent()) {
						throw new UnsupportedOperationException("Không tồn tại bản ghi");
					}
					qdKhlcntDtl = qdKhlcntDtlOptional.get();
					hhDchinhDxKhLcntDtlPreview.setTgianNhang(convertDate(qdKhlcntDtl.getTgianNhang()));
				}
				List<HhDchinhDxKhLcntGthauPreview> children = new ArrayList<>();
				for(HhDchinhDxKhLcntDsgthau gThau : gThauRepository.findAllByIdDcDxDtlOrderByGoiThau(dtl.getId())){
					HhDchinhDxKhLcntGthauPreview hhDchinhDxKhLcntGthauPreview = HhDchinhDxKhLcntGthauPreview.builder()
							.gthau(gThau.getGoiThau())
							.soLuongDc(docxToPdfConverter.convertBigDecimalToStr(gThau.getSoLuong()))
							.build();
					tongSoLuongDc = tongSoLuongDc.add(gThau.getSoLuong());
					if (qOptional.get().getLanDieuChinh() > 1) {
						if (dcLanTruocDtl != null) {
							HhDchinhDxKhLcntDsgthau gthauDcLanTruoc = gThauRepository.findFirstByIdDcDxDtlAndGoiThau(dcLanTruocDtl.getId(), gThau.getGoiThau());
							if (gthauDcLanTruoc != null && gthauDcLanTruoc.getSoLuong() != null) {
								hhDchinhDxKhLcntGthauPreview.setSoLuong(docxToPdfConverter.convertBigDecimalToStr(gthauDcLanTruoc.getSoLuong()));
								tongSoLuong = tongSoLuong.add(gthauDcLanTruoc.getSoLuong());
							}
						}
					} else {
						HhQdKhlcntDsgthau hhQdKhlcntDsgthauData = hhQdKhlcntDsgthauRepository.findByGoiThauAndIdQdDtl(gThau.getGoiThau(), qdKhlcntDtl.getId());
						if (hhQdKhlcntDsgthauData != null && hhQdKhlcntDsgthauData.getSoLuong() != null) {
							hhDchinhDxKhLcntGthauPreview.setSoLuong(docxToPdfConverter.convertBigDecimalToStr(hhQdKhlcntDsgthauData.getSoLuong()));
							tongSoLuong = tongSoLuong.add(hhQdKhlcntDsgthauData.getSoLuong());
						}
					}
					List<HhDchinhDxKhLcntGthauCtietPreview> hhDchinhDxKhLcntGthauCtietPreviews = new ArrayList<>();
					for (HhDchinhDxKhLcntDsgthauCtiet ctiet : gThauCietRepository.findAllByIdGoiThau(gThau.getId())) {
						HhDchinhDxKhLcntGthauCtietPreview hhDchinhDxKhLcntGthauCtietPreview = HhDchinhDxKhLcntGthauCtietPreview.builder()
								.tenDvi(mapDmucDvi.get(ctiet.getMaDvi()))
								.donGia(docxToPdfConverter.convertBigDecimalToStr(ctiet.getDonGia()))
								.build();
						if (ctiet.getDonGia() != null && ctiet.getSoLuong() != null) {
							tongThanhTien = tongThanhTien.add(ctiet.getDonGia().multiply(ctiet.getSoLuong()));
							hhDchinhDxKhLcntGthauCtietPreview.setThanhTien(docxToPdfConverter.convertBigDecimalToStr(ctiet.getDonGia().multiply(ctiet.getSoLuong())));
						}
						hhDchinhDxKhLcntGthauCtietPreviews.add(hhDchinhDxKhLcntGthauCtietPreview);
					}
					hhDchinhDxKhLcntGthauPreview.setChildren(hhDchinhDxKhLcntGthauCtietPreviews);
					children.add(hhDchinhDxKhLcntGthauPreview);
				}
				hhDchinhDxKhLcntDtlPreview.setChildren(children);
				hhDchinhDxKhLcntDtlPreviews.add(hhDchinhDxKhLcntDtlPreview);
			}
			data.setChildren(hhDchinhDxKhLcntDtlPreviews);
			data.setTongSl(docxToPdfConverter.convertBigDecimalToStr(tongSoLuong));
			data.setTongSlDc(docxToPdfConverter.convertBigDecimalToStr(tongSoLuongDc));
			data.setTongThanhTien(docxToPdfConverter.convertBigDecimalToStr(tongThanhTien));
		}
		return docxToPdfConverter.convertDocxToPdf(inputStream, data);
	}

}