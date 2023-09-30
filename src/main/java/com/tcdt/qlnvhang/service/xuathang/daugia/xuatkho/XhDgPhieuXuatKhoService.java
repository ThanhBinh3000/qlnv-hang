package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhServiceImpl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhDgPhieuXuatKhoService extends BaseServiceImpl {


    @Autowired
    private XhDgPhieuXuatKhoRepository xhDgPhieuXuatKhoRepository;
    @Autowired
    private XhQdGiaoNvXhServiceImpl xhQdGiaoNvXhService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhDgPhieuXuatKho> searchPage(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq req) throws Exception {
        req.setDvql(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhDgPhieuXuatKho> search = xhDgPhieuXuatKhoRepository.searchPage(req, pageable);
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
    public XhDgPhieuXuatKho create(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoPhieuXuatKho()) && xhDgPhieuXuatKhoRepository.existsBySoPhieuXuatKho(req.getSoPhieuXuatKho())) {
            throw new Exception("Số phiếu xuất kho " + req.getSoPhieuXuatKho() + " đã tồn tại");
        }
        XhDgPhieuXuatKho data = new XhDgPhieuXuatKho();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdThuKho(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoPhieuXuatKho().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhDgPhieuXuatKho created = xhDgPhieuXuatKhoRepository.save(data);
        return created;
    }

    @Transactional
    public XhDgPhieuXuatKho update(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgPhieuXuatKho data = xhDgPhieuXuatKhoRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDgPhieuXuatKhoRepository.existsBySoPhieuXuatKhoAndIdNot(req.getSoPhieuXuatKho(), req.getId())) {
            throw new Exception("Số phiếu xuất kho " + req.getSoPhieuXuatKho() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhDgPhieuXuatKho update = xhDgPhieuXuatKhoRepository.save(data);
        return update;
    }

    public List<XhDgPhieuXuatKho> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgPhieuXuatKho> list = xhDgPhieuXuatKhoRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhDgPhieuXuatKho> allById = xhDgPhieuXuatKhoRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
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
            if (data.getIdLanhDaoChiCuc() != null) {
                userInfoRepository.findById(data.getIdLanhDaoChiCuc()).ifPresent(userInfo -> {
                    data.setTenLanhDaoChiCuc(userInfo.getFullName());
                });
            }
        });
        return allById;
    }

    public XhDgPhieuXuatKho detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgPhieuXuatKho> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhDgPhieuXuatKho data = xhDgPhieuXuatKhoRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với phiếu xuất kho ở trạng thái bản nháp hoặc từ chối");
        }
        xhDgPhieuXuatKhoRepository.delete(data);
    }


    @Transient
    public XhDgPhieuXuatKho approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDgPhieuXuatKho data = xhDgPhieuXuatKhoRepository.findById(Long.valueOf(statusReq.getId()))
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
        XhDgPhieuXuatKho created = xhDgPhieuXuatKhoRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhDgPhieuXuatKhoReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDgPhieuXuatKho> page = this.searchPage(currentUser, req);
        List<XhDgPhieuXuatKho> data = page.getContent();
        String title = "Danh sách phiếu xuất kho";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Lô kho", "Số phiếu KNCL", "Ngày giám định", "Số phiếu xuất kho", "Số bảng kê",
                "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-lay-mau/ban-giao-mau.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhDgPhieuXuatKho hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNam();
            objs[3] = hdr.getNgayKyQdNv();
            objs[4] = hdr.getTenDiemKho();
            objs[5] = hdr.getTenLoKho();
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
}