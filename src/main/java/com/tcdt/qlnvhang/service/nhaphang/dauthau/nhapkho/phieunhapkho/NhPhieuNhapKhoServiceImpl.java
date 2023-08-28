package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuongRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt1Repository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntPreview;
import com.tcdt.qlnvhang.request.object.NhPhieuNhapKhoPreview;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCtReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.object.sokho.LkPhieuNhapKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Log4j2
public class NhPhieuNhapKhoServiceImpl extends BaseServiceImpl implements NhPhieuNhapKhoService {

    @Autowired
    private NhPhieuNhapKhoRepository nhPhieuNhapKhoRepository;

    @Autowired
    private NhPhieuNhapKhoCtRepository nhPhieuNhapKhoCtRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private NhBangKeCanHangRepository nhBangKeCanHangRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<NhPhieuNhapKho> searchPage(NhPhieuNhapKhoReq req){
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<NhPhieuNhapKho> pages = nhPhieuNhapKhoRepository.selectPage(req.getSoPhieu(),req.getNam(), req.getMaDvi(),req.getLoaiVthh(),req.getCloaiVthh(),req.getMaDiemKho(),req.getMaNhaKho(),req.getMaNganKho(),req.getMaLoKho(),pageable);
        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        pages.getContent().forEach(x -> {
            x.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(x.getTrangThai()));
            x.setTenLoaiVthh(listDanhMucHangHoa.get(x.getLoaiVthh()));
            x.setTenCloaiVthh(listDanhMucHangHoa.get(x.getCloaiVthh()));
            x.setTenDvi(listDanhMucDvi.get(x.getMaDvi()));
            x.setTenDiemKho(listDanhMucDvi.get(x.getMaDiemKho()));
            x.setTenNhaKho(listDanhMucDvi.get(x.getMaNhaKho()));
            x.setTenNganKho(listDanhMucDvi.get(x.getMaNganKho()));
            x.setTenLoKho(listDanhMucDvi.get(x.getMaLoKho()));
        });
        return pages;
    }

    @Override
    @Transactional()
    public NhPhieuNhapKho create(NhPhieuNhapKhoReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }
        NhPhieuNhapKho phieu = new NhPhieuNhapKho();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgayTao(new Date());
        phieu.setNguoiTaoId(userInfo.getId());
        phieu.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        phieu.setMaDvi(userInfo.getDvql());
        phieu.setNam(LocalDate.now().getYear());
        phieu.setId(Long.valueOf(phieu.getSoPhieuNhapKho().split("/")[0]));
        nhPhieuNhapKhoRepository.save(phieu);
        this.saveCtiet(phieu.getId(),req);
        return phieu;
    }

    @Override
    @Transactional()
    public NhPhieuNhapKho update(NhPhieuNhapKhoReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhPhieuNhapKho> optionalQd = nhPhieuNhapKhoRepository.findById(req.getId());
        if (!optionalQd.isPresent())
            throw new Exception("Phiếu nhập kho không tồn tại.");

        NhPhieuNhapKho phieu = optionalQd.get();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgaySua(new Date());
        phieu.setNguoiSuaId(userInfo.getId());
        nhPhieuNhapKhoRepository.save(phieu);
        this.saveCtiet(phieu.getId(),req);
        return phieu;
    }

    @Transactional()
    void saveCtiet(Long idHdr , NhPhieuNhapKhoReq req){
        nhPhieuNhapKhoCtRepository.deleteAllByIdPhieuNkHdr(idHdr);
        if(!ObjectUtils.isEmpty(req.getHangHoaList())){
            for(NhPhieuNhapKhoCtReq obj : req.getHangHoaList()){
                NhPhieuNhapKhoCt data = new NhPhieuNhapKhoCt();
                BeanUtils.copyProperties(obj,data,"id");
                data.setIdPhieuNkHdr(idHdr);
                nhPhieuNhapKhoCtRepository.save(data);
            }
        }
    }

    @Override
    public NhPhieuNhapKho detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }

        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Quyết định không tồn tại.");
        }

        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi("", "", "01");

        NhPhieuNhapKho data = optional.get();
//        data.setKetQuaKiemTra(qlpktclhKetQuaKiemTraRepo.findAllByPhieuKtChatLuongId(data.getId()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenLoaiVthh(listDanhMucHangHoa.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(listDanhMucHangHoa.get(data.getCloaiVthh()));
        data.setTenDvi(listDanhMucDvi.get(data.getMaDvi()));
        data.setTenDiemKho(listDanhMucDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(listDanhMucDvi.get(data.getMaNhaKho()));
        data.setTenNganKho(listDanhMucDvi.get(data.getMaNganKho()));
        data.setTenLoKho(listDanhMucDvi.get(data.getMaLoKho()));
        data.setTenNguoiTao(ObjectUtils.isEmpty(data.getNguoiTaoId()) ? null : userInfoRepository.findById(data.getNguoiTaoId()).get().getFullName());
        data.setTenNguoiPduyet(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
        data.setHangHoaList(nhPhieuNhapKhoCtRepository.findAllByIdPhieuNkHdr(data.getId()));
        NhBangKeCanHang bySoPhieuNhapKho = nhBangKeCanHangRepository.findBySoPhieuNhapKho(data.getSoPhieuNhapKho());
        if(!ObjectUtils.isEmpty(bySoPhieuNhapKho)){
            data.setBangKeCanHang(bySoPhieuNhapKho);
        }
        return data;
    }

    @Override
    public NhPhieuNhapKho approve(NhPhieuNhapKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        NhPhieuNhapKho phieu = optional.get();

        String status = req.getTrangThai() + phieu.getTrangThai();
        this.validateApprove(phieu);
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                phieu.setNguoiGuiDuyetId(userInfo.getId());
                phieu.setNgayGuiDuyet(new Date());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(new Date());
                phieu.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        phieu.setTrangThai(req.getTrangThai());
        NhPhieuNhapKho rs = nhPhieuNhapKhoRepository.save(phieu);
        return rs;
    }


    void validateApprove(NhPhieuNhapKho phieu) throws Exception {
        if(phieu.getLoaiVthh().startsWith("02")){

        }else{
            NhBangKeCanHang bySoPhieuNhapKho = nhBangKeCanHangRepository.findBySoPhieuNhapKho(phieu.getSoPhieuNhapKho());
            if(ObjectUtils.isEmpty(bySoPhieuNhapKho)){
                throw new Exception("Phiếu nhập kho đang không có bảng kê cân hàng, xin vui lòng tạo bảo kê cân hàng cho phiếu nhập kho");
            }else{
                if(!bySoPhieuNhapKho.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId())){
                    throw new Exception("Bảng kê cân hàng của Phiếu nhập kho đang chưa được duyệt, xin vui lòng duyệt bảng kê cân hàng");
                }
            }
        }
    }

    @Override
    @Transactional()
    public void delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Quyết định không tồn tại.");
        }

        NhPhieuNhapKho phieu = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(phieu.getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }

        List<NhPhieuNhapKhoCt> hangHoaList = nhPhieuNhapKhoCtRepository.findAllByIdPhieuNkHdr(phieu.getId());
        if (!CollectionUtils.isEmpty(hangHoaList)) {
            nhPhieuNhapKhoCtRepository.deleteAll(hangHoaList);
        }

        nhPhieuNhapKhoRepository.delete(phieu);
        fileDinhKemService.delete(phieu.getId(), Collections.singleton(NhPhieuNhapKho.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(NhPhieuNhapKhoReq req, HttpServletResponse response) throws Exception {
//        return false;
    }

    @Override
    public List<NhPhieuNhapKho> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh) {
        return setDetailList(nhPhieuNhapKhoRepository.findAllByIdQdGiaoNvNh(idQdGiaoNvNh));
    }

    @Override
    public List<NhPhieuNhapKho> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh) {
        return setDetailList(nhPhieuNhapKhoRepository.findByIdDdiemGiaoNvNh(idDdiemGiaoNvNh));
    }

    @Override
    public Page<NhPhieuNhapKho> search(LkPhieuNhapKhoReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<NhPhieuNhapKho> pages = nhPhieuNhapKhoRepository.selectPageTheKho(req.getTrangThai(),req.getNam(), Contains.convertDateToString(req.getTuNgay()),Contains.convertDateToString(req.getDenNgay()), req.getMaDvi(),req.getLoaiVthh(),req.getCloaiVthh(),req.getMaDiemKho(),req.getMaNhaKho(),req.getMaNganKho(),req.getMaLoKho(),pageable);
        pages.getContent().forEach(x -> {
            x.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(x.getTrangThai()));
            x.setHangHoaList(nhPhieuNhapKhoCtRepository.findAllByIdPhieuNkHdr(x.getId()));
        });
        return pages;
    }

    List<NhPhieuNhapKho> setDetailList(List<NhPhieuNhapKho> list){
        list.forEach( item -> {
            List<NhPhieuNhapKhoCt> allByIdPhieuNkHdr = nhPhieuNhapKhoCtRepository.findAllByIdPhieuNkHdr(item.getId());
            if(!allByIdPhieuNkHdr.isEmpty()){
                item.setSoLuongNhapKho(allByIdPhieuNkHdr.get(0).getSoLuongThucNhap());
            }
            item.setBangKeCanHang(nhBangKeCanHangRepository.findBySoPhieuNhapKho(item.getSoPhieuNhapKho()));
        });
        return list;
    }

    @Override
    public ReportTemplateResponse preview(NhPhieuNhapKhoReq objReq) throws Exception {
        NhPhieuNhapKho qOptional = this.detail(objReq.getId());

        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        NhPhieuNhapKhoPreview object = new NhPhieuNhapKhoPreview();
        for (NhPhieuNhapKhoCt nhPhieuNhapKhoCt : qOptional.getHangHoaList()) {
            nhPhieuNhapKhoCt.setThanhTien(nhPhieuNhapKhoCt.getDonGia().multiply(nhPhieuNhapKhoCt.getSoLuongThucNhap()));
        }
        BeanUtils.copyProperties(qOptional, object);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }


//
//    @Override
//    public Page<NhPhieuNhapKhoRes> search(NhPhieuNhapKhoSearchReq req) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        return nhPhieuNhapKhoRepository.search(req);
//    }
//
//    private void thongTinNganLo(NhPhieuNhapKhoRes phieu, KtNganLo nganLo) {
//        if (nganLo != null) {
//            phieu.setTenNganLo(nganLo.getTenNganlo());
//            KtNganKho nganKho = nganLo.getParent();
//            if (nganKho == null)
//                return;
//
//            phieu.setTenNganKho(nganKho.getTenNgankho());
//            phieu.setMaNganKho(nganKho.getMaNgankho());
//            KtNhaKho nhaKho = nganKho.getParent();
//            if (nhaKho == null)
//                return;
//
//            phieu.setTenNhaKho(nhaKho.getTenNhakho());
//            phieu.setMaNhaKho(nhaKho.getMaNhakho());
//            KtDiemKho diemKho = nhaKho.getParent();
//            if (diemKho == null)
//                return;
//
//            phieu.setTenDiemKho(diemKho.getTenDiemkho());
//            phieu.setMaDiemKho(diemKho.getMaDiemkho());
//        }
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        NhPhieuNhapKhoSearchReq countReq = new NhPhieuNhapKhoSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        countReq.setLoaiVthh(Contains.LOAI_VTHH_THOC);
//        count.setThoc(nhPhieuNhapKhoRepository.count(countReq));
//        countReq.setLoaiVthh(Contains.LOAI_VTHH_GAO);
//        count.setGao(nhPhieuNhapKhoRepository.count(countReq));
//        countReq.setLoaiVthh(Contains.LOAI_VTHH_MUOI);
//        count.setMuoi(nhPhieuNhapKhoRepository.count(countReq));
//        countReq.setLoaiVthh(Contains.LOAI_VTHH_VATTU);
//        count.setVatTu(nhPhieuNhapKhoRepository.count(countReq));
//        return count;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        if (CollectionUtils.isEmpty(req.getIds()))
//            return false;
//
//
//        nhPhieuNhapKhoCtRepository.deleteByPhieuNkIdIn(req.getIds());
//        nhPhieuNhapKhoRepository.deleteByIdIn(req.getIds());
//        fileDinhKemService.deleteMultiple(req.getIds(), Collections.singleton(NhPhieuNhapKho.TABLE_NAME));
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(NhPhieuNhapKhoSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<NhPhieuNhapKhoRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_PHIEU, SO_QUYET_DINH_NHAP, NGAY_NHAP_KHO,
//                DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//        String filename = "Danh_sach_phieu_nhap_kho.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                NhPhieuNhapKhoRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoPhieu();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayNhapKho());
//                objs[4] = item.getTenDiemKho();
//                objs[5] = item.getTenNhaKho();
//                objs[6] = item.getTenNganKho();
//                objs[7] = item.getTenNganLo();
//                objs[8] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_PHIEU_NHAP_KHO, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//        return true;
//    }
//
//    private void validateSoPhieu(NhPhieuNhapKho update, NhPhieuNhapKhoReq req) throws Exception {
//        String so = req.getSoPhieu();
//        if (!StringUtils.hasText(so))
//            return;
//
//        if (update == null || (StringUtils.hasText(update.getSoPhieu()) && !update.getSoPhieu().equalsIgnoreCase(so))) {
//            Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findFirstBySoPhieu(so);
//            Long updateId = Optional.ofNullable(update).map(NhPhieuNhapKho::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số phiếu " + so + " đã tồn tại");
//        }
//    }
//
//    @Override
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = nhPhieuNhapKhoRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
}
