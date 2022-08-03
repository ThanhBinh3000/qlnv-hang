package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;
import com.tcdt.qlnvhang.entities.FileDKemJoinHhDchinhDxKhLcntHdr;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.DchinhDxKhLcntDtlReq;
import com.tcdt.qlnvhang.request.object.DchinhDxKhLcntHdrReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntDsgthauDtlCtietReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntDsgthauReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDChinhSearchReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.UnitScaler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

import static com.tcdt.qlnvhang.service.SecurityContextService.getUser;
import static com.tcdt.qlnvhang.service.impl.BaseServiceImpl.convertDateToString;
import static com.tcdt.qlnvhang.service.impl.BaseServiceImpl.getDateTimeNow;

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
	private HhDxuatKhLcntHdrService hhDxuatKhLcntHdrService;

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
		Optional<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (!checkSoQd.isPresent()){
			throw new Exception("Không tìm thấy số đề xuất để điều chỉnh kế hoạch lựa chọn nhà thầu");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinHhDchinhDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinHhDchinhDxKhLcntHdr>();
		if (objReq.getFileDinhKem() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKem(), FileDKemJoinHhDchinhDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDchinhDxKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDchinhDxKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataMap);

		HhDchinhDxKhLcntDtl qdDtl = new HhDchinhDxKhLcntDtl();
		qdDtl.setId(null);
		qdDtl.setIdDxDcHdr(dataMap.getId());
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
		return dataMap;
	}

	private HhDchinhDxKhLcntHdr saveLuongThuc (DchinhDxKhLcntHdrReq objReq) throws Exception {
		Optional<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (!checkSoQd.isPresent()){
			throw new Exception("Không tìm thấy số đề xuất để điều chỉnh kế hoạch lựa chọn nhà thầu");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
		if (objReq.getFileDinhKem() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKem(), FileDKemJoinDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDchinhDxKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDchinhDxKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
//		dataMap.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataMap);

		for(DchinhDxKhLcntDtlReq dCxDtlReq : objReq.getDetail()){
			HhDchinhDxKhLcntDtl dCxDtl =  new ModelMapper().map(dCxDtlReq, HhDchinhDxKhLcntDtl.class);
			dCxDtl.setIdHdr(dataMap.getId());
			dtlRepository.save(dCxDtl);
		}
		return dataMap;
	}

	private HhDchinhDxKhLcntHdr updateVatTu (DchinhDxKhLcntHdrReq objReq) throws Exception {

//		if (StringUtils.isEmpty(objReq.getId()))
//			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
//		Optional<HhQdKhlcntHdr> HhQdKhlcntHdr = qlnvQdLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
//
//		if (!HhQdKhlcntHdr.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu cần sửa");
//
//		HhQdKhlcntHdr dataDTB = HhQdKhlcntHdr.get();
//
//		if (!Contains.QUYET_DINH_DC.equalsIgnoreCase(dataDTB.getLoaiQd()))
//			throw new Exception("Không tìm thấy dữ liệu cần sửa");
//
////			String soQdinhGoc = dataDTB.getSoQdinhGoc();
////			String soQdGiaoCtkh = dataDTB.getSoQdGiaoCtkh();
////			String nguonvon = dataDTB.getNguonvon();
//
//		HhQdKhlcntHdr dataMap = new ModelMapper().map(objReq, HhQdKhlcntHdr.class);
//
//		updateObjectToObject(dataDTB, dataMap);
//		dataDTB.setNgaySua(getDateTimeNow());
//		dataDTB.setNguoiSua(getUserName(request));
////			dataDTB.setSoQdinhGoc(soQdinhGoc);
////			dataDTB.setSoQdGiaoCtkh(soQdGiaoCtkh);
////			dataDTB.setNguonvon(nguonvon);
////			dataDTB.setLoaiDieuChinh(Contains.QUYET_DINH_DC);
//
//		List<QlnvQdLcntDtlReq> dtlReqList = objReq.getDetail();
//		List<HhDchinhDxKhLcntDtl> details = new ArrayList<>();
//		if (dtlReqList != null) {
//			List<HhDchinhDxKhLcntDtlCtiet> detailChild;
//			for (QlnvQdLcntDtlReq dtlReq : dtlReqList) {
//				List<QlnvQdLcntDtlCtietReq> cTietReq = dtlReq.getDetail();
//				HhDchinhDxKhLcntDtl detail = ObjectMapperUtils.map(dtlReq, HhDchinhDxKhLcntDtl.class);
//				detailChild = new ArrayList<HhDchinhDxKhLcntDtlCtiet>();
//				if (cTietReq != null)
//					detailChild = ObjectMapperUtils.mapAll(cTietReq, HhDchinhDxKhLcntDtlCtiet.class);
////					detail.setDetailList(detailChild);
//				details.add(detail);
//			}
//			dataMap.setDetailList(details);
//		}
//
//		qlnvQdLcntHdrRepository.save(dataDTB);


		Optional<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (!checkSoQd.isPresent()){
			throw new Exception("Không tìm thấy số đề xuất để điều chỉnh kế hoạch lựa chọn nhà thầu");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinHhDchinhDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinHhDchinhDxKhLcntHdr>();
		if (objReq.getFileDinhKem() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKem(), FileDKemJoinHhDchinhDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDchinhDxKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDchinhDxKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
		dataMap.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataMap);

		HhDchinhDxKhLcntDtl qdDtl = new HhDchinhDxKhLcntDtl();
		qdDtl.setId(null);
		qdDtl.setIdDxDcHdr(dataMap.getId());
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
		return dataMap;
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

	private HhDchinhDxKhLcntHdr updateLuongThuc (DchinhDxKhLcntHdrReq objReq) throws Exception {
		Optional<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (!checkSoQd.isPresent()){
			throw new Exception("Không tìm thấy số đề xuất để điều chỉnh kế hoạch lựa chọn nhà thầu");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
		if (objReq.getFileDinhKem() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKem(), FileDKemJoinDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDchinhDxKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDchinhDxKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
//		dataMap.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataMap);

		for(DchinhDxKhLcntDtlReq dCxDtlReq : objReq.getDetail()){
			HhDchinhDxKhLcntDtl dCxDtl =  new ModelMapper().map(dCxDtlReq, HhDchinhDxKhLcntDtl.class);
			dCxDtl.setIdHdr(dataMap.getId());
			dtlRepository.save(dCxDtl);
		}
		return dataMap;
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