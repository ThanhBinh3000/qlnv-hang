package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.entities.FileDKemJoinHhDchinhDxKhLcntHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDChinhSearchReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.Contains;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
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
	private HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

	@Autowired
	private HhQdKhlcntHdrService hhQdKhlcntHdrService;

	@Autowired
	private HhQdKhlcntDtlRepository hhQdKhlcntDtlRepository;

	@Autowired
	private HhQdKhlcntDsgthauRepository hhQdKhlcntDsgthauRepository;

	@Autowired
	private HhQdKhlcntDsgthauCtietRepository hhQdKhlcntDsgthauCtietRepository;

	public Page<HhDchinhDxKhLcntHdr> getAllPage(QlnvQdLcntHdrDChinhSearchReq objReq) throws Exception {
		int page = objReq.getPaggingReq().getPage();
		int limit = objReq.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
		Page<HhDchinhDxKhLcntHdr> data = hdrRepository.selectPage(objReq.getNamKh(),objReq.getSoQdinh(), objReq.getTrichYeu(),
				convertDateToString(objReq.getTuNgayQd()),
				convertDateToString(objReq.getDenNgayQd()),
				pageable);
		List<Long> ids = data.getContent().stream().map(HhDchinhDxKhLcntHdr::getId).collect(Collectors.toList());
		List<Object[]> listGthau = dtlRepository.countAllByDcHdr(ids);
		Map<String,String> soGthau = new HashMap<>();
		for (Object[] it: listGthau) {
			soGthau.put(it[0].toString(),it[1].toString());
		}
		for (HhDchinhDxKhLcntHdr hdr:data.getContent()) {
			hdr.setSoGoiThau(Long.parseLong(soGthau.get(hdr.getId().toString())));
			hdr.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(hdr.getTrangThai()));
		}
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
		List<FileDKemJoinHhDchinhDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinHhDchinhDxKhLcntHdr>();
		if (objReq.getFileDinhKem() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKem(), FileDKemJoinHhDchinhDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType("HH_DC_DX_LCNT_HDR");
				f.setCreateDate(new Date());
			});
		}

		HhDchinhDxKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDchinhDxKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.DUTHAO);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataMap);
		this.saveCtietVatTu(dataMap.getId(),objReq);
		return dataMap;
	}

	private HhDchinhDxKhLcntHdr saveLuongThuc (DchinhDxKhLcntHdrReq objReq) throws Exception {

		this.validateCreate(objReq);

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinHhDchinhDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinHhDchinhDxKhLcntHdr>();
		if (objReq.getFileDinhKem() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKem(), FileDKemJoinHhDchinhDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType("HH_DC_DX_LCNT_HDR");
				f.setCreateDate(new Date());
			});
		}

		HhDchinhDxKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDchinhDxKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.DUTHAO);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataMap);
		this.saveCtietLT(dataMap.getId(),objReq);
		return dataMap;
	}

	private void validateCreate (DchinhDxKhLcntHdrReq objReq) throws Exception {
		List<HhQdKhlcntHdr> checkSoQdGoc = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (checkSoQdGoc.isEmpty()){
			throw new Exception("Không tìm thấy số đề xuất để điều chỉnh kế hoạch lựa chọn nhà thầu");
		}

		Optional<HhDchinhDxKhLcntHdr> checkSoQd = hdrRepository.findBySoQd(objReq.getSoQd());
		if (checkSoQd.isPresent()){
			throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
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

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinHhDchinhDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinHhDchinhDxKhLcntHdr>();
		if (objReq.getFileDinhKem() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKem(), FileDKemJoinHhDchinhDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType("HH_DC_DX_LCNT_HDR");
				f.setCreateDate(new Date());
			});
		}

		HhDchinhDxKhLcntHdr dataDB = hdrData;
		HhDchinhDxKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDchinhDxKhLcntHdr.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());
		dataDB.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataDB);
		this.deleteDataOld(objReq,false);
		this.saveCtietVatTu(dataMap.getId(),objReq);
		return dataMap;
	}

	private HhDchinhDxKhLcntHdr updateLuongThuc (DchinhDxKhLcntHdrReq objReq) throws Exception {

		HhDchinhDxKhLcntHdr hdrData = this.validateUpdate(objReq);

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinHhDchinhDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinHhDchinhDxKhLcntHdr>();
		if (objReq.getFileDinhKem() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKem(), FileDKemJoinHhDchinhDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType("HH_DC_DX_LCNT_HDR");
				f.setCreateDate(new Date());
			});
		}

		HhDchinhDxKhLcntHdr dataDB = hdrData;
		HhDchinhDxKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDchinhDxKhLcntHdr.class);

		updateObjectToObject(dataDB, dataMap);

		dataDB.setNgaySua(getDateTimeNow());
		dataDB.setNguoiSua(getUser().getUsername());
		dataDB.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataDB);
		this.deleteDataOld(objReq,true);
		this.saveCtietLT(dataMap.getId(),objReq);
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

		if (!hdrData.get().getSoQd().equals(objReq.getSoQd())) {
			Optional<HhDchinhDxKhLcntHdr> checkSoQd = hdrRepository.findBySoQd(objReq.getSoQd());
			if (checkSoQd.isPresent()){
				throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
			}
		}

		return hdrData.get();
	}

	public void deleteDataOld(DchinhDxKhLcntHdrReq objReq, Boolean isLuongThuc){
		dtlRepository.deleteAllByIdDxDcHdr(objReq.getId());
		if(isLuongThuc){
			for (HhQdKhlcntDtlReq dx : objReq.getDsDeXuat()) {
				if(!ObjectUtils.isEmpty(dx.getId()) ){
					gThauRepository.deleteAllByIdDcDxDtl(dx.getId());
				}
				for (HhQdKhlcntDsgthauReq gt : dx.getDsGoiThau()) {
					if(!ObjectUtils.isEmpty(gt.getId()) ){
						gThauCietRepository.deleteAllByIdGoiThau(gt.getId());
					}
				}
			}
		}else{
			for (HhQdKhlcntDsgthauReq dx : objReq.getDsGoiThau()) {
				gThauRepository.deleteById(dx.getId());
				if(!ObjectUtils.isEmpty(dx.getId()) ){
					gThauCietRepository.deleteAllByIdGoiThau(dx.getId());
				}
			}
		}
	}

	public void saveCtietLT(Long idHdr , DchinhDxKhLcntHdrReq objReq){
		for (HhQdKhlcntDtlReq dx : objReq.getDsDeXuat()){
			HhDchinhDxKhLcntDtl qd = ObjectMapperUtils.map(dx, HhDchinhDxKhLcntDtl.class);
			qd.setId(null);
			qd.setIdDxDcHdr(idHdr);
			dtlRepository.save(qd);
			for (HhQdKhlcntDsgthauReq gtList : dx.getDsGoiThau()){
				HhDchinhDxKhLcntDsgthau gt = ObjectMapperUtils.map(gtList, HhDchinhDxKhLcntDsgthau.class);
				gt.setId(null);
				gt.setIdDcDxDtl(qd.getId());
				gt.setThanhTien(gt.getDonGia().multiply(gt.getSoLuong()));
				gt.setTrangThai(Contains.CHUATAO_QD);
				gThauRepository.save(gt);
				for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gtList.getChildren()){
					HhDchinhDxKhLcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDchinhDxKhLcntDsgthauCtiet.class);
					dataDdNhap.setId(null);
					dataDdNhap.setIdGoiThau(gt.getId());
					dataDdNhap.setThanhTien(dataDdNhap.getDonGia().multiply(dataDdNhap.getSoLuong()));
					gThauCietRepository.save(dataDdNhap);
				}
			}
		}
	}

	public void saveCtietVatTu(Long idHdr,DchinhDxKhLcntHdrReq objReq){
		HhDchinhDxKhLcntDtl qdDtl = new HhDchinhDxKhLcntDtl();
		qdDtl.setId(null);
		qdDtl.setIdDxDcHdr(idHdr);
		qdDtl.setMaDvi(getUser().getDvql());
		dtlRepository.save(qdDtl);

		if (objReq.getDsGoiThau() != null && objReq.getDsGoiThau().size() > 0) {
			for (HhQdKhlcntDsgthauReq dsgThau : objReq.getDsGoiThau()){
				HhDchinhDxKhLcntDsgthau gThau = ObjectMapperUtils.map(dsgThau, HhDchinhDxKhLcntDsgthau.class);
				gThau.setId(null);
				gThau.setIdDcDxDtl(qdDtl.getId());
				gThau.setTrangThai(Contains.CHUATAO_QD);
				gThauRepository.save(gThau);
				for (HhDxuatKhLcntDsgthauDtlCtietReq dsDdNhap : dsgThau.getChildren()){
					HhDchinhDxKhLcntDsgthauCtiet ddNhap = ObjectMapperUtils.map(dsDdNhap, HhDchinhDxKhLcntDsgthauCtiet.class);
					ddNhap.setId(null);
					ddNhap.setIdGoiThau(gThau.getId());
					gThauCietRepository.save(ddNhap);
				}
			}
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public HhDchinhDxKhLcntHdr approve (StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");
		HhDchinhDxKhLcntHdr qdLcnt = this.detail(stReq.getId().toString());

		qdLcnt.setTrangThai(stReq.getTrangThai());
		String status = stReq.getTrangThai() + qdLcnt.getTrangThai();
		switch(status) {
			case Contains.CHODUYET_LDV + Contains.DUTHAO:
			case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
				qdLcnt.setNguoiGuiDuyet(getUser().getUsername());
				qdLcnt.setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
				qdLcnt.setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
				qdLcnt.setNguoiPduyet(getUser().getUsername());
				qdLcnt.setNgayPduyet(getDateTimeNow());
			case Contains.BAN_HANH + Contains.DADUYET_LDV:
				qdLcnt.setNguoiPduyet(getUser().getUsername());
				qdLcnt.setNgayPduyet(getDateTimeNow());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}
		if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
			this.updateDataQdGoc(qdLcnt);
		}
		return hdrRepository.save(qdLcnt);
	}

	private void updateDataQdGoc(HhDchinhDxKhLcntHdr qdDieuChinh) throws Exception {
		HhQdKhlcntHdr hdr = hhQdKhlcntHdrService.detail(qdDieuChinh.getIdQdGoc().toString());
		BeanUtils.copyProperties(qdDieuChinh,hdr,"id","soQd");
		hhQdKhlcntHdrRepository.save(hdr);
		hhQdKhlcntDtlRepository.deleteAllByIdQdHdr(hdr.getId());
		//Is vật tư
		if(hdr.getLoaiVthh().startsWith("02")){
			HhQdKhlcntDtl qdDtl = new HhQdKhlcntDtl();
			qdDtl.setIdQdHdr(hdr.getId());
			qdDtl.setMaDvi(getUser().getDvql());
			hhQdKhlcntDtlRepository.save(qdDtl);
			if (qdDieuChinh.getHhQdKhlcntDtlList().get(0).getDsGoiThau() != null && qdDieuChinh.getHhQdKhlcntDtlList().get(0).getDsGoiThau().size() > 0) {
				for (HhDchinhDxKhLcntDsgthau dsgThau : qdDieuChinh.getHhQdKhlcntDtlList().get(0).getDsGoiThau()){
					HhQdKhlcntDsgthau gThau = new HhQdKhlcntDsgthau();
					BeanUtils.copyProperties(dsgThau,gThau,"id");
					gThau.setIdQdDtl(qdDtl.getId());
					gThau.setTrangThai(Contains.CHUATAO_QD);
					hhQdKhlcntDsgthauRepository.save(gThau);
					for (HhDchinhDxKhLcntDsgthauCtiet dsDdNhap : dsgThau.getChildren()){
						HhQdKhlcntDsgthauCtiet ddNhap = new HhQdKhlcntDsgthauCtiet();
						BeanUtils.copyProperties(dsDdNhap,ddNhap,"id");
						ddNhap.setIdGoiThau(gThau.getId());
						hhQdKhlcntDsgthauCtietRepository.save(ddNhap);
					}
				}
			}
		}else{
			for(HhDchinhDxKhLcntDtl dtl : qdDieuChinh.getHhQdKhlcntDtlList()){
				HhQdKhlcntDtl qdDtl = new HhQdKhlcntDtl();
				BeanUtils.copyProperties(dtl,qdDtl,"id");
				qdDtl.setIdQdHdr(hdr.getId());
				hhQdKhlcntDtlRepository.save(qdDtl);
				if (dtl.getDsGoiThau() != null && dtl.getDsGoiThau().size() > 0) {
					for (HhDchinhDxKhLcntDsgthau dsgThau : dtl.getDsGoiThau()){
						HhQdKhlcntDsgthau gThau = new HhQdKhlcntDsgthau();
						BeanUtils.copyProperties(dsgThau,gThau,"id");
						gThau.setIdQdDtl(qdDtl.getId());
						gThau.setTrangThai(Contains.CHUATAO_QD);
						hhQdKhlcntDsgthauRepository.save(gThau);
						for (HhDchinhDxKhLcntDsgthauCtiet dsDdNhap : dsgThau.getChildren()){
							HhQdKhlcntDsgthauCtiet ddNhap = new HhQdKhlcntDsgthauCtiet();
							BeanUtils.copyProperties(dsDdNhap,ddNhap,"id");
							ddNhap.setIdGoiThau(gThau.getId());
							hhQdKhlcntDsgthauCtietRepository.save(ddNhap);
						}
					}
				}
			}
		}
	}

	public HhDchinhDxKhLcntHdr  detail(String ids){
		if (StringUtils.isEmpty(ids))
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Optional<HhDchinhDxKhLcntHdr> qOptional = hdrRepository.findById(Long.parseLong(ids));
		System.out.println(qOptional);
		if (!qOptional.isPresent())
			throw new UnsupportedOperationException("Không tồn tại bản ghi");

		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
		Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
		Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
		Map<String,String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
		Map<String,String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

		qOptional.get().setTenVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
		qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));

		List<HhDchinhDxKhLcntDtl> dtlList = new ArrayList<>();
		for(HhDchinhDxKhLcntDtl dtl : dtlRepository.findAllByIdDxDcHdr(Long.parseLong(ids))){
			List<HhDchinhDxKhLcntDsgthau> gThauList = new ArrayList<>();
			for(HhDchinhDxKhLcntDsgthau gThau : gThauRepository.findAllByIdDcDxDtl(dtl.getId())){
				List<HhDchinhDxKhLcntDsgthauCtiet> gthauCtietList = gThauCietRepository.findAllByIdGoiThau(gThau.getId());
				gthauCtietList.forEach(f -> {
					f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
					f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
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
			dtl.setDsGoiThau(gThauList);
			dtlList.add(dtl);
		}

		qOptional.get().setHhQdKhlcntDtlList(dtlList);

		return qOptional.get();
	}

	public  void export(QlnvQdLcntHdrDChinhSearchReq objReq, HttpServletResponse response) throws Exception{
		PaggingReq paggingReq = new PaggingReq();
		paggingReq.setPage(0);
		paggingReq.setLimit(Integer.MAX_VALUE);
		objReq.setPaggingReq(paggingReq);
		Page<HhDchinhDxKhLcntHdr> page=this.getAllPage(objReq);
		List<HhDchinhDxKhLcntHdr> data=page.getContent();

		String title="Danh sách quyết định điều chỉnh kế hoạch lựa chọn nhà thầu";
		String[] rowsName=new String[]{"STT","Số quyết định điều chỉnh KH LCNT","Ngày ký","Trích yếu","Số QĐ duyệt KHLCNT","Năm kế hoạch","Loại hàng hóa","Trạng thái"};
		String fileName="danh-sach-qd-dieu-chinh-khlcnt.xlsx";
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objs=null;
		for (int i=0;i<data.size();i++){
			HhDchinhDxKhLcntHdr dx=data.get(i);
			objs=new Object[rowsName.length];
			objs[0]=i;
			objs[1]=dx.getSoQd();
			objs[2]=dx.getNgayQd();
			objs[3]=dx.getTrichYeu();
			objs[4]=dx.getSoQdGoc();
			objs[5]=dx.getNamKh();
			objs[6]=dx.getLoaiVthh();
			objs[6]=dx.getTenTrangThai();
			dataList.add(objs);
		}
		ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
		ex.export();
	}




}