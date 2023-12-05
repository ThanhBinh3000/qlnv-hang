package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReposytory;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReq;
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
public class XhPhieuXkhoBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhPhieuXkhoBttReposytory xhPhieuXkhoBttReposytory;
    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhPhieuXkhoBtt> searchPage(CustomUserDetails currentUser, XhPhieuXkhoBttReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDCC);
            req.setMaDviCha(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhPhieuXkhoBtt> search = xhPhieuXkhoBttReposytory.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapHinhThucBaoQuan = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapHinhThucBaoQuan(mapHinhThucBaoQuan);
            data.setTrangThai(data.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhPhieuXkhoBtt create(CustomUserDetails currentUser, XhPhieuXkhoBttReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoPhieuXuatKho()) && xhPhieuXkhoBttReposytory.existsBySoPhieuXuatKho(req.getSoPhieuXuatKho())) {
            throw new Exception("Số phiếu xuất kho " + req.getSoPhieuXuatKho() + " đã tồn tại");
        }
        XhPhieuXkhoBtt data = new XhPhieuXkhoBtt();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdThuKho(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoPhieuXuatKho().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhPhieuXkhoBtt created = xhPhieuXkhoBttReposytory.save(data);
        if (created.getIdQdNv() != null) {
            xhQdNvXhBttHdrRepository.findById(created.getIdQdNv()).ifPresent(quyetDinh -> {
                quyetDinh.setTrangThaiXh(Contains.DANG_THUC_HIEN);
                xhQdNvXhBttHdrRepository.save(quyetDinh);
            });
        }
        return created;
    }

    @Transactional
    public XhPhieuXkhoBtt update(CustomUserDetails currentUser, XhPhieuXkhoBttReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhPhieuXkhoBtt data = xhPhieuXkhoBttReposytory.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhPhieuXkhoBttReposytory.existsBySoPhieuXuatKhoAndIdNot(req.getSoPhieuXuatKho(), req.getId())) {
            throw new Exception("Số phiếu xuất kho " + req.getSoPhieuXuatKho() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "idThuKho");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhPhieuXkhoBtt update = xhPhieuXkhoBttReposytory.save(data);
        return update;
    }

    public List<XhPhieuXkhoBtt> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuXkhoBtt> list = xhPhieuXkhoBttReposytory.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhPhieuXkhoBtt> allById = xhPhieuXkhoBttReposytory.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapHinhThucBaoQuan = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNhapXuat(mapKieuNhapXuat);
            data.setMapHinhThucBaoQuan(mapHinhThucBaoQuan);
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
            if (data.getIdLanhDaoChiCuc() != null) {
                userInfoRepository.findById(data.getIdLanhDaoChiCuc()).ifPresent(userInfo -> {
                    data.setTenLanhDaoChiCuc(userInfo.getFullName());
                });
            }
        });
        return allById;
    }

    public XhPhieuXkhoBtt detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuXkhoBtt> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhPhieuXkhoBtt data = xhPhieuXkhoBttReposytory.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với phiếu xuất kho ở trạng thái bản nháp hoặc từ chối");
        }
        if (data.getIdQdNv() != null) {
            xhQdNvXhBttHdrRepository.findById(data.getIdQdNv()).ifPresent(quyetDinh -> {
                quyetDinh.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
                xhQdNvXhBttHdrRepository.save(quyetDinh);
            });
        }
        xhPhieuXkhoBttReposytory.delete(data);
    }

    public XhPhieuXkhoBtt approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhPhieuXkhoBtt data = xhPhieuXkhoBttReposytory.findById(Long.valueOf(statusReq.getId()))
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
        XhPhieuXkhoBtt created = xhPhieuXkhoBttReposytory.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhPhieuXkhoBttReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhPhieuXkhoBtt> page = this.searchPage(currentUser, req);
        List<XhPhieuXkhoBtt> data = page.getContent();
        String title = "Danh sách phiếu xuất kho bán trực tiếp hàng DTQG";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Lô kho", "Số phiếu KNCL", "Ngày giám định", "Số phiếu xuất kho", "Số bảng kê",
                "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-phieu-xuat-kho-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhPhieuXkhoBtt hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNamKh();
            objs[3] = hdr.getTgianGiaoNhan();
            objs[4] = hdr.getTenDiemKho();
            objs[5] = hdr.getTenNganLoKho();
            objs[6] = hdr.getSoPhieuKiemNghiem();
            objs[7] = hdr.getNgayKiemNghiemMau();
            objs[8] = hdr.getSoPhieuXuatKho();
            objs[9] = hdr.getSoBangKeHang();
            objs[10] = hdr.getNgayLapPhieu();
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
            String fileTemplate = "bantructiep/" + templatePath;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhPhieuXkhoBtt detail = this.detail(DataUtils.safeToLong(body.get("id")));
            Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
            xhQdNvXhBttHdrRepository.findById(detail.getIdQdNv()).ifPresent(quyetDinh -> {
                detail.setMaDviCha(quyetDinh.getMaDvi());
                if (mapDmucDvi.containsKey((detail.getMaDviCha()))) {
                    Map<String, Object> objDonVi = mapDmucDvi.get(detail.getMaDviCha());
                    detail.setTenDviCha(objDonVi.get("tenDvi").toString());
                }
            });
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}