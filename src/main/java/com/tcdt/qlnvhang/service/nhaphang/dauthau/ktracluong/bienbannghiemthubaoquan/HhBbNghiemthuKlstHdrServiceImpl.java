package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbannghiemthubaoquan;

import java.time.LocalDate;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.tcdt.qlnvhang.entities.FileDKemJoinKeLot;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.tcdt.qlnvhang.repository.HhBbNghiemthuKlstRepository;
import com.tcdt.qlnvhang.request.object.HhBbNghiemthuKlstHdrReq;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@Log4j2
public class HhBbNghiemthuKlstHdrServiceImpl extends BaseServiceImpl implements HhBbNghiemthuKlstHdrService {

    @Autowired
    private HhBbNghiemthuKlstRepository hhBbNghiemthuKlstRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private KtNganLoRepository ktNganLoRepository;

    @Autowired
    private HhHopDongRepository hhHopDongRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private HttpServletRequest req;

    @Override
    public Page<HhBbNghiemthuKlstHdr> searchPage(HhBbNghiemthuKlstHdrReq req) {
        return null;
    }

    @Override
    public List<HhBbNghiemthuKlstHdr> searchAll(HhBbNghiemthuKlstHdrReq req) {
        return null;
    }

    @Override
    public HhBbNghiemthuKlstHdr create(HhBbNghiemthuKlstHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<HhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(req.getIdQdGiaoNvNh());
        if (!qdNxOptional.isPresent())
            throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");

        // Add danh sach file dinh kem o Master
        List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
        if (req.getFileDinhKems() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(req.getFileDinhKems(), FileDKemJoinKeLot.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }
        HhBbNghiemthuKlstHdr dataMap = new HhBbNghiemthuKlstHdr();
        BeanUtils.copyProperties(req, dataMap);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        dataMap.setNguoiTaoId(getUser().getId());
        dataMap.setChildren1(fileDinhKemList);

        // Add thong tin chung
//		List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(req.getDetail(), HhBbNghiemthuKlstDtl.class);
//		dataMap.setChildren(dtls1);

        dataMap.setNam(qdNxOptional.get().getNamNhap());
        dataMap.setMaDvi(userInfo.getDvql());
        dataMap.setCapDvi(userInfo.getCapDvi());

        dataMap.setNam(LocalDate.now().getYear());
        dataMap.setId(Long.parseLong(req.getSoBbNtBq().split("/")[0]));

        hhBbNghiemthuKlstRepository.save(dataMap);
        return dataMap;
    }

    @Override
    public HhBbNghiemthuKlstHdr update(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu cần sửa");
		}


        Optional<HhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getIdQdGiaoNvNh());
        if (!qdNxOptional.isPresent()){
			throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");
		}

        // Add danh sach file dinh kem o Master
        List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
        if (objReq.getFileDinhKems() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKeLot.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }

        HhBbNghiemthuKlstHdr dataDTB = qOptional.get();
        BeanUtils.copyProperties(objReq,dataDTB,"id");

        dataDTB.setNgaySua(getDateTimeNow());
        dataDTB.setNguoiSuaId(getUser().getId());;
        dataDTB.setChildren1(fileDinhKemList);

        // Add thong tin chung
//        List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail(), HhBbNghiemthuKlstDtl.class);
//        dataDTB.setChildren(dtls1);

        dataDTB.setNam(qdNxOptional.get().getNamNhap());
        dataDTB.setMaDvi(userInfo.getDvql());
        dataDTB.setCapDvi(userInfo.getCapDvi());
        hhBbNghiemthuKlstRepository.save(dataDTB);

		return dataDTB;
    }

    @Override
    public HhBbNghiemthuKlstHdr detail(Long id) throws Exception {

        UserInfo userInfo = UserUtils.getUserInfo();

        if (ObjectUtils.isEmpty(userInfo)) {
            throw new Exception("401 Author");
        }

        if (ObjectUtils.isEmpty(id)) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(id);

        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        HhBbNghiemthuKlstHdr hhBbNghiemthuKlstHdr = qOptional.get();

        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        hhBbNghiemthuKlstHdr.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(hhBbNghiemthuKlstHdr.getTrangThai()));
        hhBbNghiemthuKlstHdr.setTenDvi(listDanhMucDvi.get(hhBbNghiemthuKlstHdr.getMaDvi()));
        hhBbNghiemthuKlstHdr.setTenNguoiTao(ObjectUtils.isEmpty(hhBbNghiemthuKlstHdr.getNguoiTaoId()) ? null : userInfoRepository.findById(hhBbNghiemthuKlstHdr.getNguoiTaoId()).get().getFullName());
        hhBbNghiemthuKlstHdr.setTenKeToan(ObjectUtils.isEmpty(hhBbNghiemthuKlstHdr.getIdKeToan()) ? null : userInfoRepository.findById(hhBbNghiemthuKlstHdr.getIdKeToan()).get().getFullName());
        hhBbNghiemthuKlstHdr.setTenKyThuatVien(ObjectUtils.isEmpty(hhBbNghiemthuKlstHdr.getIdKyThuatVien()) ? null : userInfoRepository.findById(hhBbNghiemthuKlstHdr.getIdKyThuatVien()).get().getFullName());
        hhBbNghiemthuKlstHdr.setTenNguoiPduyet(ObjectUtils.isEmpty(hhBbNghiemthuKlstHdr.getNguoiPduyetId()) ? null : userInfoRepository.findById(hhBbNghiemthuKlstHdr.getNguoiPduyetId()).get().getFullName());
        return hhBbNghiemthuKlstHdr;
    }

    @Override
    public HhBbNghiemthuKlstHdr approve(HhBbNghiemthuKlstHdrReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public boolean export(HhBbNghiemthuKlstHdrReq req) throws Exception {
        return false;
    }

//	@Override
//	public HhBbNghiemthuKlstHdr create(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
//			throw new Exception("Loại vật tư hàng hóa không phù hợp");
//		this.validateSoBb(null, objReq);
//
//		Optional<HhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getQdgnvnxId());
//		if (!qdNxOptional.isPresent())
//			throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");
//
//		// Add danh sach file dinh kem o Master
//		List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
//		if (objReq.getFileDinhKems() != null) {
//			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKeLot.class);
//			fileDinhKemList.forEach(f -> {
//				f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
//				f.setCreateDate(new Date());
//			});
//		}
//
//		HhBbNghiemthuKlstHdr dataMap = new ModelMapper().map(objReq, HhBbNghiemthuKlstHdr.class);
//		dataMap.setNgayTao(getDateTimeNow());
//		dataMap.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//		dataMap.setNguoiTao(getUser().getUsername());
//		dataMap.setChildren1(fileDinhKemList);
//
//		// Add thong tin chung
//		List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail(), HhBbNghiemthuKlstDtl.class);
//		dataMap.setChildren(dtls1);
//
//		dataMap.setNam(qdNxOptional.get().getNamNhap());
//		dataMap.setMaDvi(userInfo.getDvql());
//		dataMap.setCapDvi(userInfo.getCapDvi());
//
//		dataMap.setSo(getSo());
//		dataMap.setNam(LocalDate.now().getYear());
//		dataMap.setSoBb(String.format("%s/%s/%s-%s", dataMap.getSo(), dataMap.getNam(), "BBNTBQ", userInfo.getMaPbb()));
//
//		hhBbNghiemthuKlstRepository.save(dataMap);
//
//		return this.buildResponse(dataMap);
//
//	}
//
//	private void validateSoBb(HhBbNghiemthuKlstHdr update, HhBbNghiemthuKlstHdrReq req) throws Exception {
//		String soBB = req.getSoBb();
//		if (!StringUtils.hasText(soBB))
//			return;
//
//		if (update == null || (StringUtils.hasText(update.getSoBb()) && !update.getSoBb().equalsIgnoreCase(soBB))) {
//			Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findFirstBySoBb(soBB);
//			Long updateId = Optional.ofNullable(update).map(HhBbNghiemthuKlstHdr::getId).orElse(null);
//			if (optional.isPresent() && !optional.get().getId().equals(updateId))
//				throw new Exception("Số biên bản " + soBB + " đã tồn tại");
//		}
//	}
//
//	@Override
//	public HhBbNghiemthuKlstHdr update(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (StringUtils.isEmpty(objReq.getId()))
//			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
//
//		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(objReq.getId()));
//		if (!qOptional.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu cần sửa");
//
//		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
//			throw new Exception("Loại vật tư hàng hóa không phù hợp");
//
//		this.validateSoBb(qOptional.get(), objReq);
//
//		Optional<HhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getQdgnvnxId());
//		if (!qdNxOptional.isPresent())
//			throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");
//
//		// Add danh sach file dinh kem o Master
//		List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
//		if (objReq.getFileDinhKems() != null) {
//			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKeLot.class);
//			fileDinhKemList.forEach(f -> {
//				f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
//				f.setCreateDate(new Date());
//			});
//		}
//
//		HhBbNghiemthuKlstHdr dataDTB = qOptional.get();
//		HhBbNghiemthuKlstHdr dataMap = ObjectMapperUtils.map(objReq, HhBbNghiemthuKlstHdr.class);
//		dataMap.setSo(dataDTB.getSo());
//		dataMap.setNam(dataDTB.getNam());
//
//		updateObjectToObject(dataDTB, dataMap);
//
//		dataDTB.setNgaySua(getDateTimeNow());
//		dataDTB.setNguoiSua(getUser().getUsername());
//		dataDTB.setChildren1(fileDinhKemList);
//
//		// Add thong tin chung
//		List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail(), HhBbNghiemthuKlstDtl.class);
//		dataDTB.setChildren(dtls1);
//
//		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
//		dataDTB.setNam(qdNxOptional.get().getNamNhap());
//		dataDTB.setMaDvi(userInfo.getDvql());
//		dataDTB.setCapDvi(userInfo.getCapDvi());
//		hhBbNghiemthuKlstRepository.save(dataDTB);
//
//		return this.buildResponse(dataDTB);
//
//	}
//
//	@Override
//	public HhBbNghiemthuKlstHdr detail(String ids) throws Exception {
//
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//
//		if (StringUtils.isEmpty(ids))
//			throw new UnsupportedOperationException("Không tồn tại bản ghi");
//
//		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.parseLong(ids));
//
//		if (!qOptional.isPresent())
//			throw new UnsupportedOperationException("Không tồn tại bản ghi");
//
//		// Quy doi don vi kg = tan
//		List<HhBbNghiemthuKlstDtl> dtls = ObjectMapperUtils.mapAll(qOptional.get().getChildren(),
//				HhBbNghiemthuKlstDtl.class);
//		UnitScaler.formatList(dtls, Contains.DVT_TAN);
//
//		return this.buildResponse(qOptional.get());
//	}
//
//	@Override
//	public Page<HhBbNghiemthuKlstHdr> colection(HhBbNghiemthuKlstSearchReq objReq, HttpServletRequest req)
//			throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		int page = objReq.getPaggingReq().getPage();
//		int limit = objReq.getPaggingReq().getLimit();
//		Pageable pageable = PageRequest.of(page, limit);
//		this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//
//		Page<HhBbNghiemthuKlstHdr> qhKho = hhBbNghiemthuKlstRepository
//				.findAll(HhBbNghiemthuKlstSpecification.buildSearchQuery(objReq), pageable);
//
//		List<HhBbNghiemthuKlstHdr> data = qhKho.getContent();
//		if (CollectionUtils.isEmpty(data))
//			return qhKho;
//
//		// Quyet dinh giao nhiem vu nhap hang
//		Set<Long> qdNhapIds = data.stream()
//				.map(HhBbNghiemthuKlstHdr::getQdgnvnxId)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toSet());
//		Map<Long, HhQdGiaoNvuNhapxuatHdr> mapQdNhap = new HashMap<>();
//		if (!CollectionUtils.isEmpty(qdNhapIds)) {
//			mapQdNhap = hhQdGiaoNvuNhapxuatRepository.findByIdIn(qdNhapIds)
//					.stream().collect(Collectors.toMap(HhQdGiaoNvuNhapxuatHdr::getId, Function.identity()));
//		}
//
//		// Ngan lo
//		Set<String> maNganLos = data.stream()
//				.map(HhBbNghiemthuKlstHdr::getMaNganLo)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toSet());
//		Map<String, KtNganLo> mapNganLo = new HashMap<>();
//		if (!CollectionUtils.isEmpty(maNganLos)) {
//			mapNganLo = ktNganLoRepository.findByMaNganloIn(maNganLos)
//					.stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));
//		}
//
//		Set<Long> hopDongIds = data.stream()
//				.map(HhBbNghiemthuKlstHdr::getHopDongId)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toSet());
//
//		Map<Long, HhHopDongHdr> mapHopDong = new HashMap<>();
//		if (!CollectionUtils.isEmpty(hopDongIds)) {
//			mapHopDong = hhHopDongRepository.findByIdIn(hopDongIds)
//					.stream().collect(Collectors.toMap(HhHopDongHdr::getId, Function.identity()));
//		}
//
//		for (HhBbNghiemthuKlstHdr hdr : data) {
//			HhQdGiaoNvuNhapxuatHdr qdNhap = hdr.getQdgnvnxId() != null ? mapQdNhap.get(hdr.getQdgnvnxId()) : null;
//			KtNganLo nganLo = StringUtils.hasText(hdr.getMaNganLo()) ? mapNganLo.get(hdr.getMaNganLo()) : null;
//			HhHopDongHdr hopDong = hdr.getHopDongId() != null ? mapHopDong.get(hdr.getHopDongId()) : null;
//			this.buildResponseForList(hdr, qdNhap, nganLo, hopDong);
//		}
//
//		return qhKho;
//	}
//
//	@Override
//	public boolean approve(StatusReq stReq) throws Exception {
//
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (StringUtils.isEmpty(stReq.getId()))
//			throw new Exception("Không tìm thấy dữ liệu");
//
//		Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(stReq.getId()));
//		if (!optional.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu");
//
//		HhBbNghiemthuKlstHdr bb = optional.get();
//
//		String trangThai = bb.getTrangThai();
//		if (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId());
//			bb.setNguoiGuiDuyet(userInfo.getUsername());
//			bb.setNgayGuiDuyet(getDateTimeNow());
//
//		} else if (NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId().equals(trangThai))
//				return false;
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId());
//			bb.setNguoiGuiDuyet(userInfo.getUsername());
//			bb.setNgayGuiDuyet(getDateTimeNow());
//		} else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//			bb.setNguoiGuiDuyet(userInfo.getUsername());
//			bb.setNgayGuiDuyet(getDateTimeNow());
//		} else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//			bb.setNguoiPduyet(userInfo.getUsername());
//			bb.setNgayPduyet(getDateTimeNow());
//		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId());
//			bb.setNguoiPduyet(userInfo.getUsername());
//			bb.setNgayPduyet(getDateTimeNow());
//			bb.setLdoTuchoi(stReq.getLyDo());
//		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId());
//			bb.setNguoiPduyet(userInfo.getUsername());
//			bb.setNgayPduyet(getDateTimeNow());
//			bb.setLdoTuchoi(stReq.getLyDo());
//		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//			bb.setNguoiPduyet(userInfo.getUsername());
//			bb.setNgayPduyet(getDateTimeNow());
//			bb.setLdoTuchoi(stReq.getLyDo());
//		} else {
//			throw new Exception("Bad request.");
//		}
//
//		hhBbNghiemthuKlstRepository.save(bb);
//		return true;
//	}
//
//	@Override
//	public void delete(IdSearchReq idSearchReq) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (StringUtils.isEmpty(idSearchReq.getId()))
//			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
//		Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findById(idSearchReq.getId());
//
//		if (!optional.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu cần xoá");
//
//		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
//				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
//			throw new Exception("Chỉ thực hiện xóa với biên bản ở trạng thái bản nháp hoặc từ chối");
//
//		hhBbNghiemthuKlstRepository.delete(optional.get());
//	}
//
//	@Override
//	public boolean exportToExcel(HhBbNghiemthuKlstSearchReq objReq, HttpServletResponse response) throws Exception {
//		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//		List<HhBbNghiemthuKlstHdr> list = this.colection(objReq, null).get().collect(Collectors.toList());
//
//		if (CollectionUtils.isEmpty(list))
//			return true;
//
//		String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY_NGHIEM_THU, DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO,
//				CHI_PHI_THUC_HIEN_TRONG_NAM, CHI_PHI_THUC_HIEN_NAM_TRUOC, TONG_GIA_TRI, TRANG_THAI};
//		String filename = "Danh_sach_bien_ban_nghiem_thu_bao_quan_lan_dau.xlsx";
//
//		List<Object[]> dataList = new ArrayList<Object[]>();
//		Object[] objs = null;
//
//		try {
//			for (int i = 0; i < list.size(); i++) {
//				HhBbNghiemthuKlstHdr item = list.get(i);
//				objs = new Object[rowsName.length];
//				objs[0] = i;
//				objs[1] = item.getSoBb();
//				objs[2] = item.getSoQuyetDinhNhap();
//				objs[3] = convertDateToString(item.getNgayNghiemThu());
//				objs[4] = item.getTenDiemKho();
//				objs[5] = item.getTenNhaKho();
//				objs[6] = item.getTenNganKho();
//				objs[7] = item.getTenNganLo();
//				objs[8] = Optional.ofNullable(item.getChiPhiThucHienTrongNam()).orElse(BigDecimal.valueOf(0D));
//				objs[9] = Optional.ofNullable(item.getChiPhiThucHienNamTruoc()).orElse(BigDecimal.valueOf(0D));
//				objs[10] = item.getTongGiaTri();
//				objs[11] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//				dataList.add(objs);
//			}
//
//			ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_NGHIEM_THU_BAO_QUAN_LAN_DAU_NHAP, filename, rowsName, dataList, response);
//			ex.export();
//		} catch (Exception e) {
//			log.error("Error export", e);
//			return false;
//		}
//		return true;
//	}
//
//	private void buildResponseForList(HhBbNghiemthuKlstHdr bb, HhQdGiaoNvuNhapxuatHdr qdNhap,
//									  KtNganLo ktNganLo, HhHopDongHdr hopDong) {
//		this.baseResponse(bb);
//		if (qdNhap != null) {
//			bb.setSoQuyetDinhNhap(qdNhap.getSoQd());
//		}
//		this.thongTinNganLo(bb, ktNganLo);
//		if (hopDong != null) {
//			bb.setSoHopDong(hopDong.getSoHd());
//		}
//
//	}
//
//	private HhBbNghiemthuKlstHdr buildResponse(HhBbNghiemthuKlstHdr bb) throws Exception {
//
//		this.baseResponse(bb);
//		if (bb.getQdgnvnxId() != null) {
//			Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(bb.getQdgnvnxId());
//			if (!qdNhap.isPresent()) {
//				throw new Exception("Không tìm thấy quyết định nhập");
//			}
//			bb.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//		}
//
//		if (bb.getHopDongId() != null) {
//			Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(bb.getHopDongId());
//			if (!hopDong.isPresent()) {
//				throw new Exception("Không tìm thấy hợp đồng");
//			}
//			bb.setSoHopDong(hopDong.get().getSoHd());
//		}
//
//		QlnvDmDonvi donvi = getDviByMa(bb.getMaDvi(), req);
//		bb.setMaDvi(donvi.getMaDvi());
//		bb.setTenDvi(donvi.getTenDvi());
//		bb.setMaQhns(donvi.getMaQhns());
//
//		if (!StringUtils.hasText(bb.getMaNganLo()))
//			return bb;
//
//		KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(bb.getMaNganLo());
//		this.thongTinNganLo(bb, nganLo);
//		return bb;
//	}
//
//	private void baseResponse(HhBbNghiemthuKlstHdr bb) {
//		bb.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(bb.getTrangThai()));
//		bb.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(bb.getTrangThai()));
//		BigDecimal chiPhiTn = bb.getChildren().stream()
//				.map(HhBbNghiemthuKlstDtl::getThanhTienTn)
//				.filter(Objects::nonNull)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//		BigDecimal chiPhiNt = bb.getChildren().stream()
//				.map(HhBbNghiemthuKlstDtl::getThanhTienQt)
//				.filter(Objects::nonNull)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//		BigDecimal tongGiaTri = bb.getChildren().stream()
//				.map(HhBbNghiemthuKlstDtl::getTongGtri)
//				.filter(Objects::nonNull)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//		bb.setChiPhiThucHienTrongNam(chiPhiTn);
//		bb.setChiPhiThucHienNamTruoc(chiPhiNt);
//		bb.setTongGiaTri(tongGiaTri);
//		bb.setTongGiaTriBangChu(MoneyConvert.doctienBangChu(tongGiaTri.toString(), null));
//	}
//
//	private void thongTinNganLo(HhBbNghiemthuKlstHdr bb, KtNganLo nganLo) {
//		if (nganLo != null) {
//			bb.setTenNganLo(nganLo.getTenNganlo());
//			KtNganKho nganKho = nganLo.getParent();
//			if (nganKho == null)
//				return;
//
//			bb.setTenNganKho(nganKho.getTenNgankho());
//			bb.setMaNganKho(nganKho.getMaNgankho());
//			KtNhaKho nhaKho = nganKho.getParent();
//			if (nhaKho == null)
//				return;
//
//			bb.setTenNhaKho(nhaKho.getTenNhakho());
//			bb.setMaNhaKho(nhaKho.getMaNhakho());
//			KtDiemKho diemKho = nhaKho.getParent();
//			if (diemKho == null)
//				return;
//
//			bb.setTenDiemKho(diemKho.getTenDiemkho());
//			bb.setMaDiemKho(diemKho.getMaDiemkho());
//		}
//	}
//
//	@Transactional
//	@Override
//	public boolean deleteMultiple(DeleteReq req) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (CollectionUtils.isEmpty(req.getIds()))
//			return false;
//		hhBbNghiemthuKlstRepository.deleteByIdIn(req.getIds());
//		return true;
//	}
//
//	@Override
//	public Integer getSo() throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		Integer so = hhBbNghiemthuKlstRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//		so = Optional.ofNullable(so).orElse(0);
//		so = so + 1;
//		return so;
//	}
//
//	@Override
//	public BaseNhapHangCount count(Set<String> maDvis) {
//		HhBbNghiemthuKlstSearchReq countReq = new HhBbNghiemthuKlstSearchReq();
//		countReq.setMaDvis(maDvis);
//		BaseNhapHangCount count = new BaseNhapHangCount();
//
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_THOC);
//		count.setThoc((int) hhBbNghiemthuKlstRepository.count(HhBbNghiemthuKlstSpecification.buildSearchQuery(countReq)));
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_GAO);
//		count.setGao((int) hhBbNghiemthuKlstRepository.count(HhBbNghiemthuKlstSpecification.buildSearchQuery(countReq)));
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_MUOI);
//		count.setMuoi((int) hhBbNghiemthuKlstRepository.count(HhBbNghiemthuKlstSpecification.buildSearchQuery(countReq)));
//		return count;
//	}
}
