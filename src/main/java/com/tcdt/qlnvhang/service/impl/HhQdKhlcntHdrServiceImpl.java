package com.tcdt.qlnvhang.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntDsgthauDtlCtietReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntDsgthauReq;
import com.tcdt.qlnvhang.table.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
	private HhQdKhlcntDtlRepository hhQdKhlcntDtlRepository;

	@Autowired
	private HhQdKhlcntDsgthauRepository hhQdKhlcntDsgthauRepository;

	@Autowired
	private HhQdKhlcntDsgthauCtietRepository hhQdKhlcntDsgthauCtietRepository;

	@Autowired
	private HhDxKhLcntThopHdrRepository hhDxKhLcntThopHdrRepository;

	@Autowired
	private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;


	@Override
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

		List<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
		if (!checkSoQd.isEmpty()) {
			throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
		}

		Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(objReq.getIdThHdr());
		if (!qOptional.isPresent()){
			throw new Exception("Không tìm thấy tổng hợp kế hoạch lựa chọn nhà thầu");
		}

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();
		if (objReq.getHthucLcnt() == null || !mapDmuc.containsKey(objReq.getHthucLcnt())){
			throw new Exception("Hình thức lựa chọn nhà thầu không phù hợp");
		}

		if (objReq.getPthucLcnt() == null || !mapDmuc.containsKey(objReq.getPthucLcnt())){
			throw new Exception("Phương thức đấu thầu không phù hợp");
		}

		if (objReq.getLoaiHdong() == null || !mapDmuc.containsKey(objReq.getLoaiHdong())){
			throw new Exception("Loại hợp đồng không phù hợp");
		}

		if (objReq.getNguonVon() == null || !mapDmuc.containsKey(objReq.getNguonVon())){
			throw new Exception("Nguồn vốn không phù hợp");
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
		dataMap.setIdThHdr(qOptional.get().getId());
		dataMap.setLastest(objReq.getLastest());

		hhQdKhlcntHdrRepository.save(dataMap);

		// Update trạng thái tổng hợp dxkhclnt
		hhDxKhLcntThopHdrRepository.updateTrangThai(dataMap.getIdThHdr(), Contains.DADUTHAO_QD);

		for (HhQdKhlcntDtlReq dx : objReq.getDsDeXuat()){
			HhQdKhlcntDtl qd = ObjectMapperUtils.map(dx, HhQdKhlcntDtl.class);
			qd.setId(null);
			qd.setIdQdHdr(dataMap.getId());
			hhQdKhlcntDtlRepository.save(qd);
			for (HhQdKhlcntDsgthauReq gtList : dx.getDsGoiThau()){
				HhQdKhlcntDsgthau gt = ObjectMapperUtils.map(gtList, HhQdKhlcntDsgthau.class);
				gt.setId(null);
				gt.setIdQdDtl(qd.getId());
				gt.setThanhTien(gt.getDonGia().multiply(gt.getSoLuong()));
				gt.setTrangThai(Contains.CHUA_QUYET_DINH);
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
		hhQdKhlcntDtlRepository.save(qdDtl);

		if (objReq.getDsGoiThau() != null && objReq.getDsGoiThau().size() > 0) {
			for (HhQdKhlcntDsgthauReq dsgThau : objReq.getDsGoiThau()){
				HhQdKhlcntDsgthau gThau = ObjectMapperUtils.map(dsgThau, HhQdKhlcntDsgthau.class);
				gThau.setId(null);
				gThau.setIdQdDtl(qdDtl.getId());
				gThau.setTrangThai(Contains.CHUA_QUYET_DINH);
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

		if (!qOptional.get().getSoQd().equals(objReq.getSoQd())) {
			List<HhQdKhlcntHdr> checkSoQd = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQd());
			if (!checkSoQd.isEmpty()) {
				throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
			}
		}

		Optional<HhDxKhLcntThopHdr> qOptionalTh = hhDxKhLcntThopHdrRepository.findById(objReq.getIdThHdr());
		if (!qOptionalTh.isPresent()){
			throw new Exception("Không tìm thấy tổng hợp kế hoạch lựa chọn nhà thầu");
		}

		// Lay danh muc dung chung
		Map<String, String> mapDmuc = getMapCategory();
		if (objReq.getHthucLcnt() == null || !mapDmuc.containsKey(objReq.getHthucLcnt()))
			throw new Exception("Hình thức lựa chọn nhà thầu không phù hợp");

		if (objReq.getPthucLcnt() == null || !mapDmuc.containsKey(objReq.getPthucLcnt()))
			throw new Exception("Phương thức đấu thầu không phù hợp");

		if (objReq.getLoaiHdong() == null || !mapDmuc.containsKey(objReq.getLoaiHdong()))
			throw new Exception("Loại hợp đồng không phù hợp");

		if (objReq.getNguonVon() == null || !mapDmuc.containsKey(objReq.getNguonVon()))
			throw new Exception("Nguồn vốn không phù hợp");

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
		dataDB.setIdThHdr(qOptional.get().getId());

		hhQdKhlcntHdrRepository.save(dataDB);

		hhQdKhlcntDtlRepository.deleteAllByIdQdHdr(dataDB.getId());
		for (HhQdKhlcntDtlReq dx : objReq.getDsDeXuat()){
			HhQdKhlcntDtl qd = ObjectMapperUtils.map(dx, HhQdKhlcntDtl.class);
			hhQdKhlcntDsgthauRepository.deleteByIdQdDtl(qd.getId());
			qd.setId(null);
			qd.setIdQdHdr(dataDB.getId());
			hhQdKhlcntDtlRepository.save(qd);
			for (HhQdKhlcntDsgthauReq gtList : dx.getDsGoiThau()){
				HhQdKhlcntDsgthau gt = ObjectMapperUtils.map(gtList, HhQdKhlcntDsgthau.class);
				hhQdKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gt.getId());
				gt.setId(null);
				gt.setIdQdDtl(qd.getId());
				gt.setThanhTien(gt.getDonGia().multiply(gt.getSoLuong()));
				gt.setTrangThai(Contains.CHUA_QUYET_DINH);
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
		hhQdKhlcntDtlRepository.save(qdDtl);

		if (objReq.getDsGoiThau() != null && objReq.getDsGoiThau().size() > 0) {
			for (HhQdKhlcntDsgthauReq dsgThau : objReq.getDsGoiThau()){
				HhQdKhlcntDsgthau gThau = ObjectMapperUtils.map(dsgThau, HhQdKhlcntDsgthau.class);
				gThau.setId(null);
				gThau.setIdQdDtl(qdDtl.getId());
				gThau.setTrangThai(Contains.CHUA_QUYET_DINH);
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

		qOptional.get().setTenVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
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
				dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
				dsg.setTenLoaiHdong(hashMapLoaiHdong.get(dsg.getLoaiHdong()));
				dsg.setTenNguonVon(hashMapNguonVon.get(dsg.getNguonVon()));
				dsg.setTenPthucLcnt(hashMapPthucDthau.get(dsg.getPthucLcnt()));
				dsg.setTenHthucLcnt(hashMapHtLcnt.get(dsg.getHthucLcnt()));
				dsg.setChildren(listGtCtiet);
				hhQdKhlcntDsgthauList.add(dsg);
			};
			dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
			dtl.setDsGoiThau(hhQdKhlcntDsgthauList);
			hhQdKhlcntDtlList.add(dtl);
		}

		qOptional.get().setHhQdKhlcntDtlList(hhQdKhlcntDtlList);

		return qOptional.get();
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
		dataRes.setTenVthh(hashMapDmHh.get(dataRes.getLoaiVthh()));
		dataRes.setTenCloaiVthh(hashMapDmHh.get(dataRes.getCloaiVthh()));

		List<HhQdKhlcntDsgthauCtiet> dsgThauCtiet = hhQdKhlcntDsgthauCtietRepository.findByIdGoiThau(gThau.get().getId());
		dsgThauCtiet.forEach(item -> {
			item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
			item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
		});

		dataRes.setChildren(dsgThauCtiet);

		Optional<HhQdKhlcntDtl> dataDtl = hhQdKhlcntDtlRepository.findById(dataRes.getIdQdDtl());
		Optional<HhQdKhlcntHdr> dataHdr = hhQdKhlcntHdrRepository.findById(dataDtl.get().getIdQdHdr());

		dataHdr.get().setTenVthh(hashMapDmHh.get(dataHdr.get().getLoaiVthh()));
		dataHdr.get().setTenCloaiVthh(hashMapDmHh.get(dataHdr.get().getCloaiVthh()));

		dataDtl.get().setHhQdKhlcntHdr(dataHdr.get());
		dataDtl.get().setTenDvi(mapDmucDvi.get(dataDtl.get().getMaDvi()));


		dataRes.setHhQdKhlcntDtl(dataDtl.get());
		return gThau.get();
	}

	@Override
	@Transactional
	public HhQdKhlcntHdr approve(StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId())){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findById(Long.valueOf(stReq.getId()));
		if (!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		if(optional.get().getLoaiVthh().startsWith("02")){
			return this.approveVatTu(stReq,optional.get());
		}else{
			return this.approveLT(stReq,optional.get());
		}
	}

	private HhQdKhlcntHdr approveVatTu(StatusReq stReq,HhQdKhlcntHdr dataDB) throws Exception {
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
				hhDxuatKhLcntHdrRepository.updateStatus(dataDB.getIdTrHdr() , Contains.DABANHANH_QD);
			}else{
				throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
			}
			this.cloneProject(dataDB.getId(),true);
		}
		HhQdKhlcntHdr createCheck = hhQdKhlcntHdrRepository.save(dataDB);
		return createCheck;
	}

	private HhQdKhlcntHdr approveLT(StatusReq stReq,HhQdKhlcntHdr dataDB) throws Exception{
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
			Optional<HhDxKhLcntThopHdr> qOptional = hhDxKhLcntThopHdrRepository.findById(dataDB.getIdThHdr());
			if(qOptional.isPresent()){
				if(qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
					throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
				}
				hhDxKhLcntThopHdrRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
			}else{
				throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
			}
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

		if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
				&& !optional.get().getTrangThai().equals(Contains.TUCHOI_LDV))
			throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");

		hhQdKhlcntHdrRepository.delete(optional.get());

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
	public Page<HhQdKhlcntHdr> getAllPage(HhQdKhlcntSearchReq req,HttpServletResponse response) throws Exception {
		int page = req.getPaggingReq().getPage();
		int limit = req.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		Page<HhQdKhlcntHdr> pageContet = hhQdKhlcntHdrRepository.selectPage(req.getNamKhoach(),req.getLoaiVthh(), req.getSoQd(),req.getTrichYeu(), convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()), req.getTrangThai(), req.getLastest(),pageable);
		for (HhQdKhlcntHdr f : pageContet.getContent()) {
			f.setTenVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
			f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
			List<HhQdKhlcntDtl> detail = (hhQdKhlcntDtlRepository.findAllByIdQdHdr(f.getId()));
			for (HhQdKhlcntDtl data : detail) {
				f.setSoGthau(data.getSoGthau());
				f.setTongTien(data.getTongTien());
			}
			continue;
		}
		return pageContet;
	}


	@Override
	public List<HhQdKhlcntHdr> getAll(HhQdKhlcntSearchReq req) throws Exception {
		List<HhQdKhlcntHdr> listData =  hhQdKhlcntHdrRepository.selectAll(req.getNamKhoach(),req.getLoaiVthh(),req.getCloaiVthh(), req.getSoQd(), convertDateToString(req.getTuNgayQd()),convertDateToString(req.getDenNgayQd()),req.getTrangThai());
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		listData.forEach(f -> {
			f.setTenVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
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
		Page<HhQdKhlcntHdr> page = this.getAllPage(searchReq,response);
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
			objs[5] = qd.getMaTrHdr();
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
		Page<HhQdKhlcntHdr> page = this.getAllPage(searchReq,response);
		List<HhQdKhlcntHdr> data = page.getContent();

		// Tao form excel
		String title = "Danh sách QĐ phê duyệt KHLCNT";
		String[] rowsName = new String[] { "STT", "Số quyết định", "Ngày QĐ", "Trích yếu", "Năm kế hoạch",
				"Loại hàng hóa", "Chủng loại hàng hóa" };


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
		Page<HhQdKhlcntHdr> page = this.getAllPage(searchReq,response);
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
