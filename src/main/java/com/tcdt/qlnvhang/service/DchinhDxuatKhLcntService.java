package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;
import com.tcdt.qlnvhang.entities.FileDKemJoinHhDchinhDxKhLcntHdr;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDChinhSearchReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

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

	public Page<HhDchinhDxKhLcntHdr> getAllPage(QlnvQdLcntHdrDChinhSearchReq objReq) throws Exception {
		int page = objReq.getPaggingReq().getPage();
		int limit = objReq.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
		return hdrRepository.selectPage(objReq.getNamKh(),objReq.getSoQdinh(), objReq.getTrichYeu(), convertDateToString(objReq.getTuNgayQd()),convertDateToString(objReq.getDenNgayQd()), pageable);
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
		dataMap.setTrangThai(Contains.TAO_MOI);
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
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataMap);
		this.saveCtietLT(dataMap.getId(),objReq);
		return dataMap;
	}

	private void validateCreate (DchinhDxKhLcntHdrReq objReq) throws Exception {
		Optional<HhQdKhlcntHdr> checkSoQdGoc = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (!checkSoQdGoc.isPresent()){
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
		this.deleteDataOld(objReq);
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
		this.deleteDataOld(objReq);
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

		Optional<HhQdKhlcntHdr> checkSoQdGoc = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (!checkSoQdGoc.isPresent()){
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

	public void deleteDataOld(DchinhDxKhLcntHdrReq objReq){
		dtlRepository.deleteAllByIdDxDcHdr(objReq.getId());
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
				gt.setTrangThai(Contains.CHUA_QUYET_DINH);
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
				gThau.setTrangThai(Contains.CHUA_QUYET_DINH);
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
		Optional<HhDchinhDxKhLcntHdr> qdLcnt = hdrRepository.findById(Long.valueOf(stReq.getId()));
		if (!qdLcnt.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		qdLcnt.get().setTrangThai(stReq.getTrangThai());
		String status = stReq.getTrangThai() + qdLcnt.get().getTrangThai();
		switch(status) {
			case Contains.CHO_DUYET + Contains.MOI_TAO:
			case Contains.CHO_DUYET + Contains.TU_CHOI:
				qdLcnt.get().setNguoiGuiDuyet(getUser().getUsername());
				qdLcnt.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI + Contains.CHO_DUYET:
				qdLcnt.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.DUYET + Contains.CHO_DUYET:
				qdLcnt.get().setNguoiPduyet(getUser().getUsername());
				qdLcnt.get().setNgayPduyet(getDateTimeNow());
			case Contains.BAN_HANH + Contains.DUYET:
				qdLcnt.get().setNguoiPduyet(getUser().getUsername());
				qdLcnt.get().setNgayPduyet(getDateTimeNow());
				break;
			default:
				break;
		}
//		if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
//			Optional<HhDxuatKhLcntHdr> dXuat = hhQdKhlcntHdrRepository.findBySoDxuat(qdLcnt.get().getSoQdinhGoc());
//			dXuat.get().setLastest(false);
//			hhDxuatKhLcntHdrRepository.save(dXuat.get());
//			HhDxuatKhLcntHdr dXuatNew = new ModelMapper().map(qdLcnt.get(), HhDxuatKhLcntHdr.class);
//			dXuatNew.setLastest(true);
//			dXuatNew.setId(null);
//			hhDxuatKhLcntHdrRepository.save(dXuatNew);
//		}
		return hdrRepository.save(qdLcnt.get());
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



}