package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkho;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangke.NhBangKeVt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNxDdiem;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhBbNghiemthuKlstRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatDtlRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNxDdiemRepository;
import com.tcdt.qlnvhang.repository.vattu.bangke.NhBangKeVtRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.nhapkho.NhPhieuNhapKhoPreview;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCtReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.object.sokho.LkPhieuNhapKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.phieukiemtracl.NhPhieuKtChatLuongService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbLayMauHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatDtlRepository hhQdGiaoNvuNhapxuatDtlRepository;

    @Autowired
    private HhQdGiaoNvuNxDdiemRepository hhQdGiaoNvuNxDdiemRepository;

    @Autowired
    private HhBbNghiemthuKlstRepository hhBbNghiemthuKlstRepository;

    @Autowired
    private NhPhieuKtChatLuongService nhPhieuKtChatLuongService;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private NhBangKeVtRepository bangKeVtRepository;

    @Override
    public Page<NhQdGiaoNvuNhapxuatHdr> timKiem(NhPhieuNhapKhoReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<NhQdGiaoNvuNhapxuatHdr> data = null;
        req.setMaDvi(userInfo.getDvql());
        if (userInfo.getCapDvi().equalsIgnoreCase(Contains.CAP_CHI_CUC)) {
            data = hhQdGiaoNvuNhapxuatRepository.selectPnkChiCuc(req, pageable);
        } else {
            data = hhQdGiaoNvuNhapxuatRepository.selectPnkCuc(req, pageable);
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            List<NhQdGiaoNvuNhapxuatDtl> nhQdGiaoNvuNhapxuatDtl = hhQdGiaoNvuNhapxuatDtlRepository.findAllByIdHdr(f.getId());
            for (NhQdGiaoNvuNhapxuatDtl dtl : nhQdGiaoNvuNhapxuatDtl) {
                dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
                List<NhQdGiaoNvuNxDdiem> allByIdCt = hhQdGiaoNvuNxDdiemRepository.findAllByIdCt(dtl.getId());
                allByIdCt.forEach(item->{
                    item.setTenCuc(mapDmucDvi.get(item.getMaCuc()));
                    item.setTenChiCuc(mapDmucDvi.get(item.getMaChiCuc()));
                    item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
                    item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
                    item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
                    item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
                    item.setListPhieuNhapKho(findAllByIdDdiemGiaoNvNh(item.getId()));
                    List<HhBbNghiemthuKlstHdr> hhBbNghiemthuKlstHdrList = hhBbNghiemthuKlstRepository.findByIdDdiemGiaoNvNh(item.getId());
                    hhBbNghiemthuKlstHdrList.forEach(i -> {
                        i.setTenThuKho(ObjectUtils.isEmpty(i.getIdThuKho()) ? null : userInfoRepository.findById(i.getIdThuKho()).get().getFullName());
                    });
                    item.setListBbNtbqld(hhBbNghiemthuKlstHdrList);
                    item.setListPhieuKtraCl(nhPhieuKtChatLuongService.findAllByIdDdiemGiaoNvNh(item.getId()));
                });
                dtl.setChildren(allByIdCt);
            }
            f.setDtlList(nhQdGiaoNvuNhapxuatDtl);
        });
        return data;
    }

    @Override
    public Page<NhPhieuNhapKho> searchPage(NhPhieuNhapKhoReq req) throws Exception {
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
        saveFile(req, phieu);
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
        saveFile(req, phieu);
        this.saveCtiet(phieu.getId(),req);
        return phieu;
    }

    @Transactional()
    public void saveCtiet(Long idHdr, NhPhieuNhapKhoReq req){
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

    private void saveFile (NhPhieuNhapKhoReq req, NhPhieuNhapKho phieu) {
        fileDinhKemService.delete(phieu.getId(),  Lists.newArrayList(NhPhieuNhapKho.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), phieu.getId(), NhPhieuNhapKho.TABLE_NAME);
        }
        fileDinhKemService.delete(phieu.getId(),  Lists.newArrayList(NhPhieuNhapKho.TABLE_NAME + "_CHUNG_TU"));
        if (!DataUtils.isNullObject(req.getChungTuKemTheo())) {
            fileDinhKemService.saveListFileDinhKem(req.getChungTuKemTheo(), phieu.getId(), NhPhieuNhapKho.TABLE_NAME + "_CHUNG_TU");
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
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Collections.singletonList(NhPhieuNhapKho.TABLE_NAME));
        data.setFileDinhKems(fileDinhKem);
        List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singletonList(NhPhieuNhapKho.TABLE_NAME + "_CHUNG_TU"));
        data.setChungTuKemTheo(canCu);
        NhBangKeCanHang bySoPhieuNhapKho = nhBangKeCanHangRepository.findBySoPhieuNhapKho(data.getSoPhieuNhapKho());
        if(!ObjectUtils.isEmpty(bySoPhieuNhapKho)){
            data.setBangKeCanHang(bySoPhieuNhapKho);
        }
        NhBangKeVt bangKeVt = bangKeVtRepository.findBySoPhieuNhapKho(data.getSoPhieuNhapKho());
        if(!ObjectUtils.isEmpty(bangKeVt)){
            data.setBangKeVt(bangKeVt);
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

    @Override
//    public ReportTemplateResponse preview(NhPhieuNhapKhoReq req) throws Exception {
//        NhPhieuNhapKho nhPhieuNhapKho = detail(req.getId());
//        if (nhPhieuNhapKho == null) {
//            throw new Exception("Phiếu nhập kho không tồn tại.");
//        }
//        NhPhieuNhapKhoPreview object = new NhPhieuNhapKhoPreview();
//        if (req.getLoaiVthh().startsWith("02")) {
//
//        } else {
//            for (NhPhieuNhapKhoCt nhPhieuNhapKhoCt : nhPhieuNhapKho.getHangHoaList()) {
//                nhPhieuNhapKhoCt.setThanhTien(nhPhieuNhapKhoCt.getDonGia().multiply(nhPhieuNhapKhoCt.getSoLuongThucNhap()));
//            }
//            BeanUtils.copyProperties(nhPhieuNhapKho, object);
//        }
//        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
//        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
//        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
//    }
    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "nhapdauthau/" + fileName;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            NhPhieuNhapKho detail  = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
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
    @Override
    public void exportToExcel(NhPhieuNhapKhoReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<NhQdGiaoNvuNhapxuatHdr> page = timKiem(objReq);
        List<NhQdGiaoNvuNhapxuatHdr> data = page.getContent();
        data.forEach(item -> {
            if (userInfo.getCapDvi().equalsIgnoreCase(Contains.CAP_CHI_CUC)) {
                item.setDetail(item.getDtlList().stream().filter(i -> i.getMaDvi().equals(userInfo.getDvql())).findFirst().get());
            } else {
                NhQdGiaoNvuNhapxuatDtl dtl = new NhQdGiaoNvuNhapxuatDtl();
                List<NhQdGiaoNvuNxDdiem> children = new ArrayList<>();
                item.getDtlList().forEach(i -> {
                    children.addAll(i.getChildren());
                });
                dtl.setChildren(children);
                item.setDetail(dtl);
            }
        });
        data.forEach(item -> {
            item.getDetail().getChildren().forEach(diaDiem -> {
                List<String> soBbNtbqld = new ArrayList<>();
                diaDiem.getListBbNtbqld().forEach(z -> {
                    soBbNtbqld.add(z.getSoBbNtBq());
                });
                diaDiem.setSoBbNtbqld(String.join(", ", soBbNtbqld));
                diaDiem.getListPhieuNhapKho().forEach(k -> {
                    k.setPhieuKiemTraCl(diaDiem.getListPhieuKtraCl().stream().filter(y -> y.getSoPhieu().equals(k.getSoPhieuKtraCl())).collect(Collectors.toList()).get(0));
                });
            });
        });
        String filename = "Danh_sach_phieu_nhap_kho.xlsx";
        String title = "Danh sách phiếu nhập kho";
        String[] rowsName;
        if (objReq.getLoaiVthh().startsWith("02")) {
            rowsName = new String[]{"STT", "Số kế hoạch/ tờ trình", "Năm kế hoạch", "Ngày lập KH", "Ngày duyệt KH", "Số QĐ giao chỉ tiêu", "Loại hàng hóa",
                    "Tổng số gói thầu", "Số gói thầu đã trúng", "SL HĐ đã ký", "Số QĐ duyệt KHLCNT", "Thời hạn thực hiện dự án", "Trạng thái đề xuất"};
        } else {
            rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm KH", "Thời hạn NH", "Điểm kho", "Ngăn/Lô kho", "BB NTBQLĐ",
                    "Số phiếu nhập kho", "Ngày nhập kho", "Số phiếu KTCL", "Ngày giám định", "Trạng thái"};
        }
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        Object[] objsb = null;
        Object[] objsc = null;
        for (int i = 0; i < data.size(); i++) {
            NhQdGiaoNvuNhapxuatHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
            objs[2] = qd.getNamNhap();
            objs[3] = convertDate(qd.getTgianNkho());
            dataList.add(objs);
            for (int j = 0; j < qd.getDetail().getChildren().size(); j++) {
                objsb = new Object[rowsName.length];
                objsb[4] = qd.getDetail().getChildren().get(j).getTenDiemKho();
                objsb[5] = qd.getDetail().getChildren().get(j).getTenLoKho() != null ?
                        qd.getDetail().getChildren().get(j).getTenLoKho() + " - " + qd.getDetail().getChildren().get(j).getTenNganKho()
                        : qd.getDetail().getChildren().get(j).getTenNganKho();
                if (!objReq.getLoaiVthh().startsWith("02")) {
                    objsb[6] = qd.getDetail().getChildren().get(j).getSoBbNtbqld();
                }
                dataList.add(objsb);
                for (int k = 0; k < qd.getDetail().getChildren().get(j).getListPhieuNhapKho().size(); k++) {
                    objsc = new Object[rowsName.length];
                    objsc[7] = qd.getDetail().getChildren().get(j).getListPhieuNhapKho().get(k).getSoPhieuNhapKho();
                    objsc[8] = convertDate(qd.getDetail().getChildren().get(j).getListPhieuNhapKho().get(k).getNgayTao());
                    if (objReq.getLoaiVthh().startsWith("02")) {
                        objsc[9] = qd.getDetail().getChildren().get(j).getListPhieuNhapKho().get(k).getSoBienBanGuiHang();
                        objsc[10] = qd.getDetail().getChildren().get(j).getBienBanGuiHang().getNgayTao();
                    } else {
                        objsc[9] = qd.getDetail().getChildren().get(j).getListPhieuNhapKho().get(k).getSoPhieuKtraCl();
                        DateTimeFormatter df = DateTimeFormatter.ofPattern(Contains.FORMAT_DATE);
                        objsc[10] = df.format(qd.getDetail().getChildren().get(j).getListPhieuNhapKho().get(k).getPhieuKiemTraCl().getNgayGdinh());
                    }
                    objsc[11] = qd.getDetail().getChildren().get(j).getListPhieuNhapKho().get(k).getTenTrangThai();
                    dataList.add(objsc);
                }
            }
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
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
