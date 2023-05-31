package com.tcdt.qlnvhang.service.nhaphang.dauthau.dieuchinh;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.FileDKemJoinHhDchinhDxKhLcntHdr;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtietRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDChinhSearchReq;
import com.tcdt.qlnvhang.service.HhQdKhlcntHdrService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
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
	private FileDinhKemService fileDinhKemService;

	public Page<HhDchinhDxKhLcntHdr> getAllPage(QlnvQdLcntHdrDChinhSearchReq objReq) throws Exception {
		int page = objReq.getPaggingReq().getPage();
		int limit = objReq.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
		Page<HhDchinhDxKhLcntHdr> data = hdrRepository.selectPage(objReq.getNam(),objReq.getSoQdDc(), objReq.getTrichYeu(),objReq.getLoaiVthh(),
				convertDateToString(objReq.getTuNgayQd()),
				convertDateToString(objReq.getDenNgayQd()),
				pageable);
		Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
		data.getContent().forEach(f->{
			f.setTenLoaiVthh(hashMapDmHh.get(f.getLoaiVthh()));
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
		dataMap.setNam(LocalDate.now().getYear());
		dataMap.setTrangThai(Contains.DUTHAO);
		dataMap.setNguoiTaoId(getUser().getId());
		dataMap.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataMap);
		this.saveDetail(dataMap.getId(),objReq);
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
		dataMap.setNguoiTaoId(getUser().getId());
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

		Optional<HhDchinhDxKhLcntHdr> checkSoQd = hdrRepository.findBySoQdDc(objReq.getSoQdDc());
		if (checkSoQd.isPresent()){
			throw new Exception("Số quyết định " + objReq.getSoQdDc() + " đã tồn tại");
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
		dataDB.setNguoiSuaId(getUser().getId());
		dataDB.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataDB);
		this.deleteDataOld(objReq);
		this.saveDetail(dataMap.getId(),objReq);
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
		dataDB.setNguoiSuaId(getUser().getId());
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

		List<HhQdKhlcntHdr> checkSoQdGoc = hhQdKhlcntHdrRepository.findBySoQd(objReq.getSoQdGoc());
		if (checkSoQdGoc.isEmpty()){
			throw new Exception("Không tìm thấy số đề xuất để điều chỉnh kế hoạch lựa chọn nhà thầu");
		}

		if (!hdrData.get().getSoQdDc().equals(objReq.getSoQdDc())) {
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

	public void saveCtietLT(Long idHdr , DchinhDxKhLcntHdrReq objReq){
		for (HhQdKhlcntDtlReq dx : objReq.getChildren()){
			HhDchinhDxKhLcntDtl qd = ObjectMapperUtils.map(dx, HhDchinhDxKhLcntDtl.class);
			qd.setId(null);
			qd.setIdDxDcHdr(idHdr);
			dtlRepository.save(qd);
			for (HhQdKhlcntDsgthauReq gtList : dx.getChildren()){
				HhDchinhDxKhLcntDsgthau gt = ObjectMapperUtils.map(gtList, HhDchinhDxKhLcntDsgthau.class);
				gt.setId(null);
				gt.setIdDcDxDtl(qd.getId());
//				gt.setThanhTien(gt.getDonGia().multiply(gt.getSoLuong()));
				gt.setTrangThai(Contains.CHUATAO_QD);
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
	public void saveDetail(Long idHdr,DchinhDxKhLcntHdrReq objReq){
		for (HhQdKhlcntDtlReq dx : objReq.getChildren()){
			HhDchinhDxKhLcntDtl qd = ObjectMapperUtils.map(dx, HhDchinhDxKhLcntDtl.class);
			qd.setId(null);
			qd.setIdDxDcHdr(idHdr);
			dtlRepository.save(qd);
			for (HhQdKhlcntDsgthauReq gtList : objReq.getLoaiVthh().startsWith("02") ? dx.getChildren() :  dx.getDsGoiThau()){
				HhDchinhDxKhLcntDsgthau gt = ObjectMapperUtils.map(gtList, HhDchinhDxKhLcntDsgthau.class);
				gt.setId(null);
				gt.setIdDcDxDtl(qd.getId());
				gt.setTrangThai(Contains.CHUATAO_QD);
				gThauRepository.save(gt);
				for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gtList.getChildren()){
					HhDchinhDxKhLcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDchinhDxKhLcntDsgthauCtiet.class);
					dataDdNhap.setId(null);
					dataDdNhap.setIdGoiThau(gt.getId());
//					dataDdNhap.setThanhTien(dataDdNhap.getDonGia().multiply(dataDdNhap.getSoLuong()));
					gThauCietRepository.save(dataDdNhap);
				}
			}
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public HhDchinhDxKhLcntHdr approve (StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");
//		HhDchinhDxKhLcntHdr qdLcnt = this.detail(stReq.getId().toString());
		Optional<HhDchinhDxKhLcntHdr>optional=hdrRepository.findById(stReq.getId());
		if (!optional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}
//		qdLcnt.setTrangThai(stReq.getTrangThai());
		String status = stReq.getTrangThai() + optional.get().getTrangThai();
		if ((Contains.BAN_HANH + Contains.DUTHAO).equals(status)) {
			optional.get().setNguoiPduyetId(getUser().getId());
			optional.get().setNgayPduyet(getDateTimeNow());
		} else {
			throw new Exception("Phê duyệt không thành công");
		}
		optional.get().setTrangThai(stReq.getTrangThai());
		hdrRepository.save(optional.get());
		if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
//			this.updateDataQdGoc(optional.get());
		}

		return optional.get();
	}

	private void updateDataQdGoc(HhDchinhDxKhLcntHdr qdDieuChinh) throws Exception {
		HhQdKhlcntHdr hdr = hhQdKhlcntHdrService.detail(qdDieuChinh.getIdQdGoc().toString());
		HhDchinhDxKhLcntHdr dchinh = detail(qdDieuChinh.getId().toString());
		BeanUtils.copyProperties(dchinh,hdr,"id","soQd");
		hhQdKhlcntHdrRepository.save(hdr);
		hhQdKhlcntDtlRepository.deleteAllByIdQdHdr(hdr.getId());
		//Is vật tư
		if(hdr.getLoaiVthh().startsWith("02")){
			HhQdKhlcntDtl qdDtl = new HhQdKhlcntDtl();
			qdDtl.setIdQdHdr(hdr.getId());
			qdDtl.setMaDvi(getUser().getDvql());
			hhQdKhlcntDtlRepository.save(qdDtl);
			if (dchinh.getChildren().get(0).getChildren() != null && dchinh.getChildren().get(0).getChildren().size() > 0) {
				for (HhDchinhDxKhLcntDsgthau dsgThau : dchinh.getChildren().get(0).getChildren()){
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
			for(HhDchinhDxKhLcntDtl dtl : dchinh.getChildren()){
				HhQdKhlcntDtl qdDtl = new HhQdKhlcntDtl();
				BeanUtils.copyProperties(dtl,qdDtl,"id");
				qdDtl.setIdQdHdr(hdr.getId());
				hhQdKhlcntDtlRepository.save(qdDtl);
				if (dtl.getChildren() != null && dtl.getChildren().size() > 0) {
					for (HhDchinhDxKhLcntDsgthau dsgThau : dtl.getChildren()){
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

	public HhDchinhDxKhLcntHdr detail(String ids){
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

		qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
		qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));
		qOptional.get().setTenTrangThai(TrangThaiAllEnum.getLabelById(qOptional.get().getTrangThai()));
		List<HhDchinhDxKhLcntDtl> dtlList = new ArrayList<>();
		for(HhDchinhDxKhLcntDtl dtl : dtlRepository.findAllByIdDxDcHdr(Long.parseLong(ids))){
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
		if (!optional.get().getTrangThai().equals(Contains.DUTHAO)){
			throw new Exception("Chỉ cho phép xóa bản ghi ở trạng thái dự thảo");
		}
		List<HhDchinhDxKhLcntDtl> listDtl=dtlRepository.findAllByIdDxDcHdr(optional.get().getId());
		List<Long> idListDtl=listDtl.stream().map(HhDchinhDxKhLcntDtl::getId).collect(Collectors.toList());
		List<HhDchinhDxKhLcntDsgthau>  listGthau = gThauRepository.findAllByIdDcDxDtlIn(idListDtl);
		List<Long> idListGthau=listGthau.stream().map(HhDchinhDxKhLcntDsgthau::getId).collect(Collectors.toList());
		List<HhDchinhDxKhLcntDsgthauCtiet> listGthauCtiet=gThauCietRepository.findAllByIdGoiThauIn(idListGthau);
		dtlRepository.deleteAll(listDtl);
		gThauRepository.deleteAll(listGthau);
		gThauCietRepository.deleteAll(listGthauCtiet);
		fileDinhKemService.delete(optional.get().getId(),  Lists.newArrayList("HH_DC_DX_LCNT_HDR"));
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
	public HhDchinhDxKhLcntHdr findByIdQdGoc(Long idQdGoc) throws Exception {
		Optional<HhDchinhDxKhLcntHdr> data = hdrRepository.findByIdQdGoc(idQdGoc);
		return data.get();
	}



}