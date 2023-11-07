package com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.phieuktracluong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrReq;
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
public class XhPhieuKtraCluongBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhPhieuKtraCluongBttHdrRepository xhPhieuKtraCluongBttHdrRepository;
    @Autowired
    private XhPhieuKtraCluongBttDtlRepository xhPhieuKtraCluongBttDtlRepository;
    @Autowired
    private XhBbLayMauBttHdrRepository xhBbLayMauBttHdrRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhPhieuKtraCluongBttHdr> searchPage(CustomUserDetails currentUser, XhPhieuKtraCluongBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDC);
            req.setMaDviCon(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhPhieuKtraCluongBttHdr> search = xhPhieuKtraCluongBttHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhPhieuKtraCluongBttHdr create(CustomUserDetails currentUser, XhPhieuKtraCluongBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoPhieuKiemNghiem()) && xhPhieuKtraCluongBttHdrRepository.existsBySoPhieuKiemNghiem(req.getSoPhieuKiemNghiem())) {
            throw new Exception("Số phiếu kiểm nghiệm chất lượng " + req.getSoPhieuKiemNghiem() + " đã tồn tại");
        }
        XhPhieuKtraCluongBttHdr data = new XhPhieuKtraCluongBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdKtvBaoQuan(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoPhieuKiemNghiem().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhPhieuKtraCluongBttHdr created = xhPhieuKtraCluongBttHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhPhieuKtraCluongBttHdrReq req, Long idHdr) {
        xhPhieuKtraCluongBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhPhieuKtraCluongBttDtlReq dtlReq : req.getChildren()) {
            XhPhieuKtraCluongBttDtl dtl = new XhPhieuKtraCluongBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setId(null);
            dtl.setIdHdr(idHdr);
            xhPhieuKtraCluongBttDtlRepository.save(dtl);
        }
    }

    @Transactional
    public XhPhieuKtraCluongBttHdr update(CustomUserDetails currentUser, XhPhieuKtraCluongBttHdrReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhPhieuKtraCluongBttHdr data = xhPhieuKtraCluongBttHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhPhieuKtraCluongBttHdrRepository.existsBySoPhieuKiemNghiemAndIdNot(req.getSoPhieuKiemNghiem(), req.getId())) {
            throw new Exception("Số phiếu kiểm nghiệm chất lượng " + req.getSoPhieuKiemNghiem() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "idKtvBaoQuan");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhPhieuKtraCluongBttHdr update = xhPhieuKtraCluongBttHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhPhieuKtraCluongBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuKtraCluongBttHdr> list = xhPhieuKtraCluongBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhPhieuKtraCluongBttHdr> allById = xhPhieuKtraCluongBttHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapHinhThucBaoQuan = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapHinhThucBaoQuan(mapHinhThucBaoQuan);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNhapXuat(mapKieuNhapXuat);
            data.setTrangThai(data.getTrangThai());
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
            if (data.getIdTphongKtvBaoQuan() != null) {
                userInfoRepository.findById(data.getIdTphongKtvBaoQuan()).ifPresent(userInfo -> {
                    data.setTenTphongKtvBaoQuan(userInfo.getFullName());
                });
            }
            if (data.getNguoiPduyetId() != null) {
                userInfoRepository.findById(data.getNguoiPduyetId()).ifPresent(userInfo -> {
                    data.setTenThuTruongDonVi(userInfo.getFullName());
                });
            }
            List<XhPhieuKtraCluongBttDtl> listDtl = xhPhieuKtraCluongBttDtlRepository.findAllByIdHdr(data.getId());
            data.setChildren(listDtl);
        });
        return allById;
    }

    public XhPhieuKtraCluongBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuKtraCluongBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhPhieuKtraCluongBttHdr data = xhPhieuKtraCluongBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với phiếu kiểm nghiệm chất lượng ở trạng thái bản nháp hoặc từ chối");
        }
        xhPhieuKtraCluongBttDtlRepository.deleteAllByIdHdr(data.getId());
        xhPhieuKtraCluongBttHdrRepository.delete(data);
    }

    public XhPhieuKtraCluongBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhPhieuKtraCluongBttHdr data = xhPhieuKtraCluongBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(currentUser.getUser().getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setIdTphongKtvBaoQuan(currentUser.getUser().getId());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhPhieuKtraCluongBttHdr created = xhPhieuKtraCluongBttHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhPhieuKtraCluongBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhPhieuKtraCluongBttHdr> page = this.searchPage(currentUser, req);
        List<XhPhieuKtraCluongBttHdr> data = page.getContent();
        String title = "Danh sách phiếu kiểm nghiệm chất lượng";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số phiếu KNCL", "Ngày kiểm nghiệm", "Số BB LM/BGM", "Ngày lấy mẫu",
                "Số BB tịnh kho", "Ngày lập BB tịnh kho", "Trạng thái"};
        String fileName = "dạm-sach-phieu-kiem-nghiem-chat-luong.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhPhieuKtraCluongBttHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNamKh();
            objs[3] = hdr.getNgayKyQdNv();
            objs[4] = hdr.getTenDiemKho();
            objs[5] = hdr.getTenLoKho();
            objs[6] = hdr.getSoPhieuKiemNghiem();
            objs[7] = hdr.getNgayKiemNghiemMau();
            objs[8] = hdr.getSoBbLayMau();
            objs[9] = hdr.getNgayLayMau();
            objs[10] = hdr.getSoBbTinhKho();
            objs[11] = hdr.getNgayLapTinhKho();
            objs[12] = hdr.getTenTrangThai();
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
            FileInputStream inputStream = new FileInputStream(baseReportFolder + "bantructiep/Phiếu kiểm nghiệm chất lượng.docx");
            XhPhieuKtraCluongBttHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            xhBbLayMauBttHdrRepository.findById(detail.getIdBbLayMau())
                    .ifPresent(layMau -> detail.setSoLuongHangbaoQuan(layMau.getSoLuongKiemTra()));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}