package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbannhapdaykho;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNxDdiem;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.nhapkho.NhBbNhapDayKhoPreview;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtReq;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.service.HhQdGiaoNvuNhapxuatService;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class NhBienBanNhapDayKhoServiceImpl extends BaseServiceImpl implements NhBienBanNhapDayKhoService {

    @Autowired
    private NhBbNhapDayKhoRepository nhBbNhapDayKhoRepository;

    @Autowired
    private NhBbNhapDayKhoCtRepository nhBbNhapDayKhoCtRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatService hhQdGiaoNvuNhapxuatService;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Override
    public Page<NhBbNhapDayKho> searchPage(QlBienBanNhapDayKhoLtReq req) {
        return null;
    }

    @Override
    public NhBbNhapDayKho create(QlBienBanNhapDayKhoLtReq req) throws Exception {
        if (req == null) {
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        NhBbNhapDayKho item = new NhBbNhapDayKho();
        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.valueOf(req.getSoBienBanNhapDayKho().split("/")[0]));
        nhBbNhapDayKhoRepository.save(item);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), NhBbNhapDayKho.TABLE_NAME);
        }
        List<NhBbNhapDayKhoCt> chiTiets = saveListChiTiet(item.getId(), req.getChiTiets());
        item.setChiTiets(chiTiets);

        return item;
    }

    @Transactional
    public List<NhBbNhapDayKhoCt> saveListChiTiet(Long parentId, List<QlBienBanNdkCtLtReq> chiTietReqs) throws Exception {
        nhBbNhapDayKhoCtRepository.deleteByIdBbNhapDayKho(parentId);
        List<NhBbNhapDayKhoCt> chiTiets = new ArrayList<>();
        for (QlBienBanNdkCtLtReq req : chiTietReqs) {
            NhBbNhapDayKhoCt chiTiet = new NhBbNhapDayKhoCt();
            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setIdBbNhapDayKho(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets)) {
            nhBbNhapDayKhoCtRepository.saveAll(chiTiets);
        }

        return chiTiets;
    }

    @Override
    public NhBbNhapDayKho update(QlBienBanNhapDayKhoLtReq req) throws Exception {
        if (req == null) {
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        Optional<NhBbNhapDayKho> optional = nhBbNhapDayKhoRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản không tồn tại.");

        NhBbNhapDayKho item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        nhBbNhapDayKhoRepository.save(item);
        fileDinhKemService.delete(item.getId(), Lists.newArrayList(NhBbNhapDayKho.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), NhBbNhapDayKho.TABLE_NAME);
        }
        List<NhBbNhapDayKhoCt> chiTiets = saveListChiTiet(item.getId(), req.getChiTiets());
        item.setChiTiets(chiTiets);

        return item;
    }

    @Override
    public NhBbNhapDayKho detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NhBbNhapDayKho> optional = nhBbNhapDayKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản không tồn tại.");

        NhBbNhapDayKho item = optional.get();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setChiTiets(nhBbNhapDayKhoCtRepository.findAllByIdBbNhapDayKho(item.getId()));
        item.setTenNguoiTao(ObjectUtils.isEmpty(item.getNguoiTaoId()) ? null : userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
        item.setTenKeToan(ObjectUtils.isEmpty(item.getIdKeToan()) ? null : userInfoRepository.findById(item.getIdKeToan()).get().getFullName());
        item.setTenKyThuatVien(ObjectUtils.isEmpty(item.getIdKyThuatVien()) ? null : userInfoRepository.findById(item.getIdKyThuatVien()).get().getFullName());
        item.setTenNguoiPduyet(ObjectUtils.isEmpty(item.getNguoiPduyetId()) ? null : userInfoRepository.findById(item.getNguoiPduyetId()).get().getFullName());
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(item.getId(), Collections.singletonList(NhBbNhapDayKho.TABLE_NAME));
        item.setFileDinhKems(fileDinhKem);
        return item;
    }

    @Override
    public NhBbNhapDayKho approve(QlBienBanNhapDayKhoLtReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<NhBbNhapDayKho> optional = nhBbNhapDayKhoRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản không tồn tại.");

        NhBbNhapDayKho bienBan = optional.get();

        String trangThai = req.getTrangThai() + bienBan.getTrangThai();
        if(
            (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId()+NhapXuatHangTrangThaiEnum.DUTHAO.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId()+NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId()+NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId()+NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId()).equals(trangThai)
        ){
            bienBan.setNguoiGuiDuyetId(userInfo.getId());
            bienBan.setNgayGuiDuyet(new Date());
        } else if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId()+NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId()+NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId()).equals(trangThai)
        ) {
            bienBan.setIdKyThuatVien(userInfo.getId());
            bienBan.setNgayGuiDuyet(new Date());
            bienBan.setLyDoTuChoi(req.getLyDoTuChoi());
        } else if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId()+NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId()+NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId()).equals(trangThai)
        ) {
            bienBan.setIdKeToan(userInfo.getId());
            bienBan.setNgayGuiDuyet(new Date());
            bienBan.setLyDoTuChoi(req.getLyDoTuChoi());
        } else if (
            (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId()+NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId()+NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId()).equals(trangThai)
        ) {
            bienBan.setNguoiPduyetId(userInfo.getId());
            bienBan.setNgayPduyet(new Date());
            bienBan.setLyDoTuChoi(req.getLyDoTuChoi());
        }else{
            throw new Exception("Phê duyệt không thành công");
        }
        bienBan.setTrangThai(req.getTrangThai());
        nhBbNhapDayKhoRepository.save(bienBan);
        return bienBan;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<NhBbNhapDayKho> optional = nhBbNhapDayKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản không tồn tại.");
        fileDinhKemService.delete(optional.get().getId(), Lists.newArrayList(NhBbNhapDayKho.TABLE_NAME));
        nhBbNhapDayKhoRepository.delete(optional.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(QlBienBanNhapDayKhoLtReq req, HttpServletResponse response) throws Exception {
//        return false;
    }


//    @Override
//    public ReportTemplateResponse preview(QlBienBanNhapDayKhoLtReq req) throws Exception {
//        NhBbNhapDayKho bbNhapDayKho = detail(req.getId());
//        if (bbNhapDayKho == null) {
//            throw new Exception("Biên bản không tồn tại.");
//        }
//        NhBbNhapDayKhoPreview object = new NhBbNhapDayKhoPreview();
//        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
//        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
//        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
//    }
public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
        String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
        String fileTemplate = "nhapdauthau/nhapkho/" + fileName;
        FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
        NhBbNhapDayKho detail  = this.detail(DataUtils.safeToLong(body.get("id")));
        NhQdGiaoNvuNhapxuatHdr qd = hhQdGiaoNvuNhapxuatService.detail(String.valueOf(detail.getIdQdGiaoNvNh()));
        NhQdGiaoNvuNxDdiem diaDiem = null;
        if (qd != null && qd.getDtlList() != null) {
            for (NhQdGiaoNvuNhapxuatDtl dtl : qd.getDtlList()) {
                if (diaDiem==null && dtl.getMaDvi() != null && dtl.getMaDvi().equals(detail.getMaDvi())) {
                    diaDiem = dtl.getChildren().stream().filter(item -> item.getId().longValue() == detail.getIdDdiemGiaoNvNh().longValue()).findFirst().orElse(null);
                }
            }
        }
        return docxToPdfConverter.convertDocxToPdf(inputStream, detail ,qd, diaDiem);
    } catch (IOException e) {
        e.printStackTrace();
    } catch (XDocReportException e) {
        e.printStackTrace();
    }
    return null;
}

    @Override
    public void exportToExcel(HhQdNhapxuatSearchReq req, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        req.setMaDvi(userInfo.getDvql());
        Page<NhQdGiaoNvuNhapxuatHdr> page = hhQdGiaoNvuNhapxuatService.searchPage(req);
        List<NhQdGiaoNvuNhapxuatHdr> data = page.getContent();

        String title = "Danh sách biên bản nhập đầy kho";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm kế hoạch", "Thời hạn NH trước ngày", "Số BB NĐK", "Ngày bắt đầu nhập", "Ngày kết thúc nhập",
                "Điểm kho", "Nhà kho", "Ngăn kho", "Lô kho", "Số phiếu KT CL", "Số phiếu NK", "Số bảng kê", "Ngày nhập kho", "Trạng thái"};
        String filename = "Danh_sach_bien_ban_nhap_day_kho.xlsx";
        if (req.getLoaiVthh().startsWith("02")) {
            title = "Danh sách biên bản kết thúc nhập kho";
            rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm kế hoạch","Thời hạn NH trước ngày", "Số BB kết thúc NK", "Ngày bắt đầu nhập", "Ngày kết thúc nhập",
                    "Điểm kho", "Nhà kho", "Ngăn kho", "Lô kho", "Số phiếu KT CL", "Số phiếu NK", "Số bảng kê", "Ngày nhập kho", "Trạng thái"};
            filename = "Danh_sach_bien_ban_ket_thuc_nhap_kho.xlsx";
        }


        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        Object[] objsb = null;
        Object[] objsc = null;
        for (int i = 0; i < data.size(); i++) {
            NhQdGiaoNvuNhapxuatHdr qd = data.get(i);
            if (userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
                qd.setDetail(qd.getDtlList().stream().filter(item -> item.getMaDvi().equals(userInfo.getDvql())).collect(Collectors.toList()).get(0));
            } else {
                List<NhBbNhapDayKho> dataDd = new ArrayList<>();
                for (NhQdGiaoNvuNhapxuatDtl nhQdGiaoNvuNhapxuatDtl : qd.getDtlList()) {
                    dataDd.addAll(nhQdGiaoNvuNhapxuatDtl.getListBienBanNhapDayKho());
                }
                qd.setDetail(new NhQdGiaoNvuNhapxuatDtl());
                qd.getDetail().setListBienBanNhapDayKho(dataDd);
            }
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
            objs[2] = qd.getNamNhap();
            objs[3] = convertDate(qd.getTgianNkho());
            dataList.add(objs);
            for (int j = 0; j < qd.getDtlList().get(0).getListBienBanNhapDayKho().size(); j++) {
                objsb = new Object[rowsName.length];
                objsb[4] = qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getSoBienBanNhapDayKho();
                objsb[5] = convertDate(qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getNgayBatDauNhap());
                objsb[6] = convertDate(qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getNgayKetThucNhap());
                objsb[7] = qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getTenDiemKho();
                objsb[8] = qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getTenNhaKho();
                objsb[9] = qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getTenNganKho();
                objsb[10] = qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getTenLoKho();
                objsb[15] = qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getTenTrangThai();
                if (qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getChiTiets() != null) {
                    for (int k = 0; k < qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getChiTiets().size(); k++) {
                        objsc[11] = qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getChiTiets().get(k).getSoPhieuKtraCl();
                        objsc[12] = qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getChiTiets().get(k).getSoPhieuNhapKho();
                        objsc[13] = qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getChiTiets().get(k).getSoBangKe();
                        objsc[14] = convertDate(qd.getDtlList().get(0).getListBienBanNhapDayKho().get(j).getChiTiets().get(k).getNgayNhap());
                        dataList.add(objsc);
                    }
                }
                dataList.add(objsb);
            }
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
