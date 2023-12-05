package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBangKeDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBangKeHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBangKeReq;
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
public class XhDgBangKeService extends BaseServiceImpl {

    @Autowired
    private XhDgBangKeHdrRepository xhDgBangKeHdrRepository;
    @Autowired
    private XhDgBangKeDtlRepository xhDgBangKeDtlRepository;
    @Autowired
    private XhDgPhieuXuatKhoRepository xhDgPhieuXuatKhoRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhDgBangKeHdr> searchPage(CustomUserDetails currentUser, XhDgBangKeReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDCC);
            req.setMaDviCha(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhDgBangKeHdr> search = xhDgBangKeHdrRepository.searchPage(req, pageable);
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
    public XhDgBangKeHdr create(CustomUserDetails currentUser, XhDgBangKeReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoBangKeHang()) && xhDgBangKeHdrRepository.existsBySoBangKeHang(req.getSoBangKeHang())) {
            throw new Exception("Số bảng cân kê hàng " + req.getSoBangKeHang() + " đã tồn tại");
        }
        XhDgBangKeHdr data = new XhDgBangKeHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdThuKho(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoBangKeHang().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhDgBangKeHdr created = xhDgBangKeHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhDgBangKeReq req, Long idHdr) {
        xhDgBangKeDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhDgBangKeDtl dataDtlReq : req.getChildren()) {
            XhDgBangKeDtl dataDtl = new XhDgBangKeDtl();
            BeanUtils.copyProperties(dataDtlReq, dataDtl, "id");
            dataDtl.setId(null);
            dataDtl.setIdHdr(idHdr);
            xhDgBangKeDtlRepository.save(dataDtl);
        }
    }

    @Transactional
    public XhDgBangKeHdr update(CustomUserDetails currentUser, XhDgBangKeReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgBangKeHdr data = xhDgBangKeHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDgBangKeHdrRepository.existsBySoBangKeHangAndIdNot(req.getSoBangKeHang(), req.getId())) {
            throw new Exception("Số phiếu xuất kho " + req.getSoPhieuXuatKho() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "idThuKho");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhDgBangKeHdr update = xhDgBangKeHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhDgBangKeHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBangKeHdr> list = xhDgBangKeHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhDgBangKeHdr> allById = xhDgBangKeHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        allById.forEach(data -> {
            List<XhDgBangKeDtl> listDtl = xhDgBangKeDtlRepository.findAllByIdHdr(data.getId());
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNhapXuat(mapKieuNhapXuat);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDtl);
            if (data.getIdThuKho() != null) {
                userInfoRepository.findById(data.getIdThuKho()).ifPresent(userInfo -> {
                    data.setTenThuKho(userInfo.getFullName());
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

    public XhDgBangKeHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBangKeHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhDgBangKeHdr data = xhDgBangKeHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với bảng kê cân hàng ở trạng thái bản nháp hoặc từ chối");
        }
        xhDgBangKeDtlRepository.deleteAllByIdHdr(data.getId());
        xhDgBangKeHdrRepository.delete(data);
    }


    public XhDgBangKeHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDgBangKeHdr data = xhDgBangKeHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                data.setNguoiGuiDuyetId(currentUser.getUser().getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
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
        if (statusReq.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            xhDgPhieuXuatKhoRepository.findById(data.getIdPhieuXuatKho()).ifPresent(xuatKho -> {
                xuatKho.setIdBangKeHang(data.getId());
                xuatKho.setSoBangKeHang(data.getSoBangKeHang());
                xhDgPhieuXuatKhoRepository.save(xuatKho);
            });
        }
        XhDgBangKeHdr created = xhDgBangKeHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhDgBangKeReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDgBangKeHdr> page = this.searchPage(currentUser, req);
        List<XhDgBangKeHdr> data = page.getContent();
        String title = "Danh sách bảng kê cân hàng DTQG";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH trước ngày", "Điểm kho",
                "Lô kho", "Số phiếu KNCL", "Ngày giám định", "Số bảng kê", "Số phiếu xuất kho",
                "Ngày tạo bảng kê", "Trạng thái"};
        String fileName = "danh-sach-bang-ke-can-hang-DTQG.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhDgBangKeHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNam();
            objs[3] = hdr.getThoiGianGiaoNhan();
            objs[4] = hdr.getTenDiemKho();
            objs[5] = hdr.getTenNganLoKho();
            objs[6] = hdr.getSoPhieuKiemNghiem();
            objs[7] = hdr.getNgayKiemNghiemMau();
            objs[8] = hdr.getSoBangKeHang();
            objs[9] = hdr.getSoPhieuXuatKho();
            objs[10] = hdr.getNgayLapBangKe();
            objs[11] = hdr.getTenTrangThai();
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
            XhDgBangKeHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}