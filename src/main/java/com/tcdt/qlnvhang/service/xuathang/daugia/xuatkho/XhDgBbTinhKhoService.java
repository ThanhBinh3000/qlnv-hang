package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbTinhKhoDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhDgBbTinhKhoService extends BaseServiceImpl {

    @Autowired
    private XhDgBbTinhKhoHdrRepository xhDgBbTinhKhoHdrRepository;
    @Autowired
    private XhDgBbTinhKhoDtlRepository xhDgBbTinhKhoDtlRepository;
    @Autowired
    private XhPhieuKnghiemCluongRepository xhPhieuKnghiemCluongRepository;
    @Autowired
    private XhBbLayMauRepository xhBbLayMauRepository;
    @Autowired
    private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
    @Autowired
    private XhQdGiaoNvXhDtlRepository xhQdGiaoNvXhDtlRepository;
    @Autowired
    private XhQdGiaoNvXhDdiemRepository xhQdGiaoNvXhDdiemRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhDgBbTinhKhoHdr> searchPage(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDCC);
            req.setMaDviCha(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhDgBbTinhKhoHdr> search = xhDgBbTinhKhoHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapHinhThucBaoQuan = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        search.getContent().forEach(data -> {
            try {
                data.setMapVthh(mapDmucVthh);
                data.setMapDmucDvi(mapDmucDvi);
                data.setMapHinhThucBaoQuan(mapHinhThucBaoQuan);
                data.setTrangThai(data.getTrangThai());
                List<XhDgBbTinhKhoDtl> listDtl = xhDgBbTinhKhoDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Transactional
    public XhDgBbTinhKhoHdr create(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoBbTinhKho()) && xhDgBbTinhKhoHdrRepository.existsBySoBbTinhKho(req.getSoBbTinhKho())) {
            throw new Exception("Số biên bản tịnh kho " + req.getSoBbTinhKho() + " đã tồn tại");
        }
        XhDgBbTinhKhoHdr data = new XhDgBbTinhKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdThuKho(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoBbTinhKho().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhDgBbTinhKhoHdr created = xhDgBbTinhKhoHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhDgBbTinhKhoHdrReq req, Long idHdr) {
        xhDgBbTinhKhoDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhDgBbTinhKhoDtl dtlReq : req.getChildren()) {
            XhDgBbTinhKhoDtl dataDtl = new XhDgBbTinhKhoDtl();
            BeanUtils.copyProperties(dtlReq, dataDtl, "id");
            dataDtl.setId(null);
            dataDtl.setIdHdr(idHdr);
            xhDgBbTinhKhoDtlRepository.save(dataDtl);
        }
    }

    @Transactional
    public XhDgBbTinhKhoHdr update(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgBbTinhKhoHdr data = xhDgBbTinhKhoHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDgBbTinhKhoHdrRepository.existsBySoBbTinhKhoAndIdNot(req.getSoBbTinhKho(), req.getId())) {
            throw new Exception("Số biên bản tịnh kho " + req.getSoBbTinhKho() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhDgBbTinhKhoHdr update = xhDgBbTinhKhoHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhDgBbTinhKhoHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBbTinhKhoHdr> list = xhDgBbTinhKhoHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhDgBbTinhKhoHdr> allById = xhDgBbTinhKhoHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapHinhThucBaoQuan = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        allById.forEach(data -> {
            List<XhDgBbTinhKhoDtl> listDtl = xhDgBbTinhKhoDtlRepository.findAllByIdHdr(data.getId());
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapHinhThucBaoQuan(mapHinhThucBaoQuan);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDtl);
            if (data.getIdThuKho() != null) {
                userInfoRepository.findById(data.getIdThuKho()).ifPresent(userInfo -> {
                    data.setTenThuKho(userInfo.getFullName());
                });
            }
            if (data.getIdKtvBaoQuan() != null) {
                userInfoRepository.findById(data.getIdKtvBaoQuan()).ifPresent(userInfo -> {
                    data.setTenKtvBaoQuan(userInfo.getFullName());
                });
            }
            if (data.getIdKeToan() != null) {
                userInfoRepository.findById(data.getIdKeToan()).ifPresent(userInfo -> {
                    data.setTenKeToan(userInfo.getFullName());
                });
            }
            if (data.getIdLanhDaoChiCuc() != null) {
                userInfoRepository.findById(data.getIdLanhDaoChiCuc()).ifPresent(userInfo -> {
                    data.setTenLanhDaoChiCuc(userInfo.getFullName());
                });
            }
        });
        return allById;
    }

    public XhDgBbTinhKhoHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBbTinhKhoHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhDgBbTinhKhoHdr data = xhDgBbTinhKhoHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_KTVBQ, Contains.TUCHOI_KT, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với biên bản tịnh kho ở trạng thái bản nháp hoặc từ chối");
        }
        xhDgBbTinhKhoDtlRepository.deleteAllByIdHdr(data.getId());
        xhDgBbTinhKhoHdrRepository.delete(data);
    }

    public XhDgBbTinhKhoHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDgBbTinhKhoHdr data = xhDgBbTinhKhoHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_KTVBQ + Contains.DUTHAO:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KT:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_LDCC:
                data.setNguoiGuiDuyetId(currentUser.getUser().getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_KTVBQ:
                data.setIdKtvBaoQuan(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.CHODUYET_KT:
                data.setIdKeToan(currentUser.getUser().getId());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setIdLanhDaoChiCuc(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhDgBbTinhKhoHdr created = xhDgBbTinhKhoHdrRepository.save(data);
        if (statusReq.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            this.updateKiemNghiemAndLayMauAndQuyetDinh(created);
        }
        return created;
    }

    private void updateKiemNghiemAndLayMauAndQuyetDinh(XhDgBbTinhKhoHdr created) {
        xhPhieuKnghiemCluongRepository.findById(created.getIdPhieuKiemNghiem()).ifPresent(kiemNghiem -> {
            kiemNghiem.setIdTinhKho(created.getId());
            kiemNghiem.setSoBbTinhKho(created.getSoBbTinhKho());
            kiemNghiem.setNgayLapTinhKho(created.getNgayLapBienBan());
            xhBbLayMauRepository.findById(kiemNghiem.getIdBbLayMau()).ifPresent(layMau -> {
                layMau.setIdTinhKho(created.getId());
                layMau.setSoBbTinhKho(created.getSoBbTinhKho());
                layMau.setNgayXuatDocKho(created.getNgayLapBienBan());
                xhBbLayMauRepository.save(layMau);
            });
            xhPhieuKnghiemCluongRepository.save(kiemNghiem);
        });
        xhQdGiaoNvXhDdiemRepository.findById(created.getIdKho()).ifPresent(quyetDinhDdiem -> {
            quyetDinhDdiem.setIdBbTinhKho(created.getId());
            quyetDinhDdiem.setSoBbTinhKho(created.getSoBbTinhKho());
            quyetDinhDdiem.setNgayLapBbTinhKho(created.getNgayLapBienBan());
            xhQdGiaoNvXhDdiemRepository.save(quyetDinhDdiem);
        });
    }

    public void export(CustomUserDetails currentUser, XhDgBbTinhKhoHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDgBbTinhKhoHdr> page = this.searchPage(currentUser, req);
        List<XhDgBbTinhKhoHdr> data = page.getContent();
        String title = "Danh sách biên bản tịnh kho";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số BB tịnh kho", "Ngày lập BB tịnh kho", "Số phiếu KNCL", "Số bảng kê",
                "Số phiếu xuất kho", "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-tinh-kho.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhDgBbTinhKhoHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNam();
            objs[3] = hdr.getNgayKyQdNv();
            objs[4] = hdr.getTenDiemKho();
            objs[5] = hdr.getTenNganLoKho();
            objs[6] = hdr.getSoBbTinhKho();
            objs[7] = hdr.getNgayLapBienBan();
            objs[8] = hdr.getSoPhieuKiemNghiem();
            Object[] finalObjs = objs;
            hdr.getChildren().forEach(dtl -> {
                finalObjs[9] = dtl.getSoBangKeHang();
                finalObjs[10] = dtl.getSoPhieuXuatKho();
                finalObjs[11] = dtl.getNgayXuatKho();
            });
            objs[12] = hdr.getTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templatePath = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "bandaugia/" + templatePath;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhDgBbTinhKhoHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}