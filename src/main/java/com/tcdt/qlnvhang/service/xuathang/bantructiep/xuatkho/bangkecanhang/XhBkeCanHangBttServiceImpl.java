package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bangcankehang.XhBkeCanHangBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bangcankehang.XhBkeCanHangBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReposytory;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdrReq;
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
public class XhBkeCanHangBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhBkeCanHangBttHdrRepository xhBkeCanHangBttHdrRepository;
    @Autowired
    private XhBkeCanHangBttDtlRepository xhBkeCanHangBttDtlRepository;
    @Autowired
    private XhPhieuXkhoBttReposytory xhPhieuXkhoBttReposytory;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhBkeCanHangBttHdr> searchPage(CustomUserDetails currentUser, XhBkeCanHangBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDCC);
            req.setMaDviCha(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhBkeCanHangBttHdr> search = xhBkeCanHangBttHdrRepository.searchPage(req, pageable);
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
    public XhBkeCanHangBttHdr create(CustomUserDetails currentUser, XhBkeCanHangBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoBangKeHang()) && xhBkeCanHangBttHdrRepository.existsBySoBangKeHang(req.getSoBangKeHang())) {
            throw new Exception("Số bảng cân kê hàng " + req.getSoBangKeHang() + " đã tồn tại");
        }
        XhBkeCanHangBttHdr data = new XhBkeCanHangBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdThuKho(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoBangKeHang().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhBkeCanHangBttHdr created = xhBkeCanHangBttHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhBkeCanHangBttHdrReq req, Long idHdr) {
        xhBkeCanHangBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhBkeCanHangBttDtlReq dataDtlReq : req.getChildren()) {
            XhBkeCanHangBttDtl dataDtl = new XhBkeCanHangBttDtl();
            BeanUtils.copyProperties(dataDtlReq, dataDtl, "id");
            dataDtl.setId(null);
            dataDtl.setIdHdr(idHdr);
            xhBkeCanHangBttDtlRepository.save(dataDtl);
        }
    }

    @Transactional
    public XhBkeCanHangBttHdr update(CustomUserDetails currentUser, XhBkeCanHangBttHdrReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhBkeCanHangBttHdr data = xhBkeCanHangBttHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhBkeCanHangBttHdrRepository.existsBySoBangKeHangAndIdNot(req.getSoBangKeHang(), req.getId())) {
            throw new Exception("Số phiếu xuất kho " + req.getSoPhieuXuatKho() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "idThuKho");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhBkeCanHangBttHdr update = xhBkeCanHangBttHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhBkeCanHangBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBkeCanHangBttHdr> list = xhBkeCanHangBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhBkeCanHangBttHdr> allById = xhBkeCanHangBttHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        allById.forEach(data -> {
            List<XhBkeCanHangBttDtl> listDtl = xhBkeCanHangBttDtlRepository.findAllByIdHdr(data.getId());
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

    public XhBkeCanHangBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBkeCanHangBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhBkeCanHangBttHdr data = xhBkeCanHangBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với bảng kê cân hàng ở trạng thái bản nháp hoặc từ chối");
        }
        xhBkeCanHangBttDtlRepository.deleteAllByIdHdr(data.getId());
        xhBkeCanHangBttHdrRepository.delete(data);
    }

    public XhBkeCanHangBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhBkeCanHangBttHdr data = xhBkeCanHangBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
            xhPhieuXkhoBttReposytory.findById(data.getIdPhieuXuatKho()).ifPresent(xuatKho -> {
                xuatKho.setIdBangKeHang(data.getId());
                xuatKho.setSoBangKeHang(data.getSoBangKeHang());
                xhPhieuXkhoBttReposytory.save(xuatKho);
            });
        }
        XhBkeCanHangBttHdr created = xhBkeCanHangBttHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhBkeCanHangBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhBkeCanHangBttHdr> page = this.searchPage(currentUser, req);
        List<XhBkeCanHangBttHdr> data = page.getContent();
        String title = "Danh sách bảng kê cân hàng";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH trước ngày", "Điểm kho",
                "Lô kho", "Số phiếu KNCL", "Ngày giám định", "Số bảng kê", "Số phiếu xuất kho",
                "Ngày tạo bảng kê", "Trạng thái"};
        String fileName = "danh-sach-bang-ke-can-hang.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhBkeCanHangBttHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNamKh();
            objs[3] = hdr.getNgayKyQdNv();
            objs[4] = hdr.getTenDiemKho();
            objs[5] = hdr.getTenLoKho();
            objs[6] = hdr.getSoPhieuKiemNghiem();
            objs[7] = hdr.getNgayKiemNghiemMau();
            objs[8] = hdr.getSoBangKeHang();
            objs[9] = hdr.getSoPhieuXuatKho();
            objs[10] = hdr.getNgayLapBangKe();
            objs[11] = hdr.getTrangThai();
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
            XhBkeCanHangBttHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}