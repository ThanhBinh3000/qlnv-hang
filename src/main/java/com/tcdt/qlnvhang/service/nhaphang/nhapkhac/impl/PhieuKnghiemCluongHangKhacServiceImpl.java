package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.KquaKnghiem;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.phieuknghiemcl.KquaKnghiemKhac;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.phieuknghiemcl.PhieuKnghiemCluongHangKhac;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.KquaKnghiemKhacRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.PhieuKnghiemCluongHangKhacRepository;
import com.tcdt.qlnvhang.request.object.KquaKnghiemKhacReq;
import com.tcdt.qlnvhang.request.object.PhieuKnghiemCluongHangKhacReq;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.KquaKnghiemReq;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.phieukiemnghiemcl.PhieuKnghiemCluongHangService;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.PhieuKnghiemCluongHangKhacService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

@Service
@Log4j2
public class PhieuKnghiemCluongHangKhacServiceImpl extends BaseServiceImpl implements PhieuKnghiemCluongHangKhacService {

    @Autowired
    private PhieuKnghiemCluongHangKhacRepository phieuKnghiemCluongHangKhacRepository;

    @Autowired
    private KquaKnghiemKhacRepository kquaKnghiemKhacRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Page<PhieuKnghiemCluongHangKhac> searchPage(PhieuKnghiemCluongHangKhacReq req) {
        return null;
    }

    @Override
    public PhieuKnghiemCluongHangKhac create(PhieuKnghiemCluongHangKhacReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        PhieuKnghiemCluongHangKhac phieuKnclh = new PhieuKnghiemCluongHangKhac();
        BeanUtils.copyProperties(req, phieuKnclh, "id");
        phieuKnclh.setNguoiTaoId(userInfo.getId());
        phieuKnclh.setIdKyThuatVien(userInfo.getId());
        phieuKnclh.setNgayTao(new Date());
        phieuKnclh.setMaDvi(userInfo.getDvql());
        phieuKnclh.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        phieuKnclh.setNam(LocalDate.now().getYear());
        phieuKnclh.setId(Long.parseLong(phieuKnclh.getSoPhieuKiemNghiemCl().split("/")[0]));
        phieuKnghiemCluongHangKhacRepository.save(phieuKnclh);
        saveDetail(req, phieuKnclh.getId());
        return phieuKnclh;
    }

    @Override
    public PhieuKnghiemCluongHangKhac update(PhieuKnghiemCluongHangKhacReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        Optional<PhieuKnghiemCluongHangKhac> optional = phieuKnghiemCluongHangKhacRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu.");

        PhieuKnghiemCluongHangKhac phieuKnclh = optional.get();
        BeanUtils.copyProperties(req, phieuKnclh, "id");
        phieuKnclh.setNguoiSuaId(userInfo.getId());
        phieuKnclh.setNgaySua(new Date());

        phieuKnghiemCluongHangKhacRepository.save(phieuKnclh);
        saveDetail(req, phieuKnclh.getId());
        return phieuKnclh;
    }

    @Override
    public ReportTemplateResponse preview(PhieuKnghiemCluongHangSearchReq objReq) throws Exception {
        PhieuKnghiemCluongHangKhac optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }

    void saveDetail(PhieuKnghiemCluongHangKhacReq req, Long id) {
        kquaKnghiemKhacRepository.deleteByPhieuKnghiemId(id);

        for (KquaKnghiemKhacReq kquaReq : req.getKquaKnghiem()) {
            KquaKnghiemKhac kq = new KquaKnghiemKhac();
//            BeanUtils.copyProperties(kquaReq, kq, "id");
            kq.setChiSoNhap(kquaReq.getMucYeuCauNhap());
            kq.setPhuongPhap(kquaReq.getPhuongPhapXd());
            kq.setDanhGia(kquaReq.getDanhGia());
            kq.setTenTchuan(kquaReq.getTenChiTieu());
            kq.setMaTchuan(kquaReq.getMaChiTieu());
            kq.setPhieuKnghiemId(id);
            kquaKnghiemKhacRepository.save(kq);
        }
    }

    @Override
    public PhieuKnghiemCluongHangKhac detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<PhieuKnghiemCluongHangKhac> optional = phieuKnghiemCluongHangKhacRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu.");

        PhieuKnghiemCluongHangKhac item = optional.get();
        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        item.setTenLoaiVthh(listDanhMucHangHoa.get(item.getLoaiVthh()));
        item.setTenCloaiVthh(listDanhMucHangHoa.get(item.getCloaiVthh()));
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setTenDiemKho(listDanhMucDvi.get(item.getMaDiemKho()));
        item.setTenNhaKho(listDanhMucDvi.get(item.getMaNhaKho()));
        item.setTenNganKho(listDanhMucDvi.get(item.getMaNganKho()));
        item.setTenLoKho(listDanhMucDvi.get(item.getMaLoKho()));
        item.setTenKyThuatVien(ObjectUtils.isEmpty(item.getIdKyThuatVien()) ? null : userInfoRepository.findById(item.getIdKyThuatVien()).get().getFullName());
        item.setTenTruongPhong(ObjectUtils.isEmpty(item.getIdTruongPhong()) ? null : userInfoRepository.findById(item.getIdTruongPhong()).get().getFullName());

        item.setListKquaKngiem(kquaKnghiemKhacRepository.findByPhieuKnghiemId(id));

        return item;
    }

    @Override
    public PhieuKnghiemCluongHangKhac approve(PhieuKnghiemCluongHangKhacReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<PhieuKnghiemCluongHangKhac> optional = phieuKnghiemCluongHangKhacRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }

        PhieuKnghiemCluongHangKhac item = optional.get();
        String trangThai = req.getTrangThai() + item.getTrangThai();

        if (
			(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId() + NhapXuatHangTrangThaiEnum.DUTHAO.getId()).equals(trangThai) ||
			(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId()).equals(trangThai) ||
			(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId()).equals(trangThai)
        ) {
            item.setNguoiGuiDuyetId(userInfo.getId());
            item.setNgayGuiDuyet(new Date());
        } else if (
			(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()).equals(trangThai) ||
			(NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()).equals(trangThai)
        ) {
            item.setIdTruongPhong(userInfo.getId());
            item.setLyDoTuChoi(req.getLyDoTuChoi());
        } else if (
			(NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId()).equals(trangThai) ||
			(NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId()).equals(trangThai)
        ) {
            item.setNgayPduyet(new Date());
			item.setNguoiPduyetId(userInfo.getId());
			item.setLyDoTuChoi(req.getLyDoTuChoi());
		} else {
            throw new Exception("Phê duyệt không thành công");
        }
        item.setTrangThai(req.getTrangThai());
        phieuKnghiemCluongHangKhacRepository.save(item);
        return item;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<PhieuKnghiemCluongHangKhac> optional = phieuKnghiemCluongHangKhacRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu.");

        PhieuKnghiemCluongHangKhac phieu = optional.get();

        if (NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId().equals(phieu.getTrangThai())) {
            throw new Exception("Không thể xóa đề xuất điều chỉnh đã đã duyệt");
        }
        kquaKnghiemKhacRepository.deleteByPhieuKnghiemId(phieu.getId());
        phieuKnghiemCluongHangKhacRepository.delete(phieu);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(PhieuKnghiemCluongHangKhacReq req, HttpServletResponse response) throws Exception {
//        return false;
    }


//	@Override
//	public Page<PhieuKnghiemCluongHangRes> search(PhieuKnghiemCluongHangSearchReq req) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		String dvql = userInfo.getDvql();
//
//		QlnvDmDonvi donVi = getDviByMa(dvql, httpServletRequest);
//		QlnvDmDonvi donViCha = Optional.ofNullable(donVi).map(QlnvDmDonvi::getParent).orElse(null);
//
//		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),  req.getPaggingReq().getLimit());
//		List<Object[]> list = phieuKnghiemCluongHangKhacRepository.search(req, pageable);
//
//		Set<Long> ids = list.stream().filter(o -> o[0] != null)
//				.map(o -> ((PhieuKnghiemCluongHang) o[0]).getId())
//				.collect(Collectors.toSet());
//		Map<Long, Long> mapCount = kquaKnghiemService.countKqByPhieuKnghiemId(ids);
//
//		List<PhieuKnghiemCluongHangRes> responses = new ArrayList<>();
//		for (Object[] o : list) {
//			PhieuKnghiemCluongHangRes response = new PhieuKnghiemCluongHangRes();
//			PhieuKnghiemCluongHang item = (PhieuKnghiemCluongHang) o[0];
//			KtNganLo nganLo = o[1] != null ? (KtNganLo) o[1] : null;
//			Long qdNhapId = (Long) o[2];
//			String soQdNhap = (String) o[3];
//			Long bbBanGiaoId = (Long) o[4];
//			String soBienBanBanGiao = (String) o[5];
//			LocalDate ngayBanGiaoMau = (LocalDate) o[6];
//
//			BeanUtils.copyProperties(item, response);
//			response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//			response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//			this.thongTinNganLo(response, nganLo);
//			response.setQdgnvnxId(qdNhapId);
//			response.setSoQuyetDinhNhap(soQdNhap);
//			if (donVi != null) {
//				response.setMaDvi(donVi.getMaDvi());
//				response.setTenDvi(donVi.getTenDvi());
//			}
//			if (donViCha != null) {
//				response.setMaDviCha(donViCha.getMaDvi());
//				response.setTenDviCha(donViCha.getTenDvi());
//			}
//			response.setBbBanGiaoMauId(bbBanGiaoId);
//			response.setSoBbBanGiao(soBienBanBanGiao);
//			response.setNgayBanGiaoMau(ngayBanGiaoMau);
//			response.setSoLuongMauHangKt(mapCount.get(item.getId()));
//			responses.add(response);
//		}
//		return new PageImpl<>(responses, pageable, phieuKnghiemCluongHangKhacRepository.count(req));
//	}
//
//	@Override
//	@Transactional(rollbackOn = Exception.class)
//	public boolean deleteMultiple(DeleteReq req) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (CollectionUtils.isEmpty(req.getIds()))
//			return false;
//
//
//		kquaKnghiemService.deleteByPhieuKnghiemIdIn(req.getIds());
//		phieuKnghiemCluongHangKhacRepository.deleteByIdIn(req.getIds());
//		return true;
//	}
//
//	@Override
//	public boolean exportToExcel(PhieuKnghiemCluongHangSearchReq objReq, HttpServletResponse response) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//		List<PhieuKnghiemCluongHangRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//		if (CollectionUtils.isEmpty(list))
//			return true;
//
//		String[] rowsName = new String[] { STT, SO_PHIEU, SO_QUYET_DINH_NHAP, NGAY_BAN_GIAO_MAU, CUC_DU_TRU_NHA_NUOC_KHU_VUC,
//				DON_VI_TO_CHUC_THU_NGHIEM, SO_LUONG_MAU_HANG_KIEM_TRA, TRANG_THAI};
//		String filename = "Danh_sach_phieu_kiem_nghiem_chat_luong_hang.xlsx";
//
//		List<Object[]> dataList = new ArrayList<Object[]>();
//		Object[] objs = null;
//
//		try {
//			for (int i = 0; i < list.size(); i++) {
//				PhieuKnghiemCluongHangRes item = list.get(i);
//				objs = new Object[rowsName.length];
//				objs[0] = i;
//				objs[1] = item.getSoPhieu();
//				objs[2] = item.getSoQuyetDinhNhap();
//				objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayBanGiaoMau());
//				objs[4] = item.getTenDviCha();
//				objs[5] = item.getTenDvi();
//				objs[6] = item.getSoLuongMauHangKt();
//				objs[7] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//				dataList.add(objs);
//			}
//
//			ExportExcel ex = new ExportExcel(SHEET_PHIEU_KIEM_NGHIEM_CHAT_LUONG_HANG, filename, rowsName, dataList, response);
//			ex.export();
//		} catch (Exception e) {
//			log.error("Error export", e);
//			return false;
//		}
//
//		return true;
//	}

}
