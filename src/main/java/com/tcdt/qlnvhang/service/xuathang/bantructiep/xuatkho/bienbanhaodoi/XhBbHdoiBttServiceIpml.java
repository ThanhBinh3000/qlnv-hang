package com.tcdt.qlnvhang.service.xuathang.bantructiep.xuatkho.bienbanhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi.XhBbHdoiBttHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhBbHdoiBttServiceIpml extends BaseServiceImpl {

    @Autowired
    private XhBbHdoiBttHdrRepository xhBbHdoiBttHdrRepository;
    @Autowired
    private XhBbHdoiBttDtlRepository xhBbHdoiBttDtlRepository;
    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;
    @Autowired
    private XhBbLayMauBttHdrRepository xhBbLayMauBttHdrRepository;
    @Autowired
    private XhPhieuKtraCluongBttHdrRepository xhPhieuKtraCluongBttHdrRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhBbHdoiBttHdr> searchPage(CustomUserDetails currentUser, XhBbHdoiBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDCC);
            req.setMaDviCha(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhBbHdoiBttHdr> search = xhBbHdoiBttHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            try {
                data.setMapVthh(mapDmucVthh);
                data.setMapDmucDvi(mapDmucDvi);
                data.setTrangThai(data.getTrangThai());
                List<XhBbHdoiBttDtl> listDtl = xhBbHdoiBttDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Transactional
    public XhBbHdoiBttHdr create(CustomUserDetails currentUser, XhBbHdoiBttHdrReq req) throws Exception {
        System.out.println("Giá trị của biến number là: " + req);
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoBbHaoDoi()) && xhBbHdoiBttHdrRepository.existsBySoBbHaoDoi(req.getSoBbHaoDoi())) {
            throw new Exception("Số biên bản hao dôi " + req.getSoBbHaoDoi() + " đã tồn tại");
        }
        XhBbHdoiBttHdr data = new XhBbHdoiBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdThuKho(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoBbHaoDoi().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhBbHdoiBttHdr created = xhBbHdoiBttHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhBbHdoiBttHdrReq req, Long idHdr) {
        xhBbHdoiBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhBbHdoiBttDtlReq dtlReq : req.getChildren()) {
            XhBbHdoiBttDtl dataDtl = new XhBbHdoiBttDtl();
            BeanUtils.copyProperties(dtlReq, dataDtl, "id");
            dataDtl.setId(null);
            dataDtl.setIdHdr(idHdr);
            xhBbHdoiBttDtlRepository.save(dataDtl);
        }
    }

    @Transactional
    public XhBbHdoiBttHdr update(CustomUserDetails currentUser, XhBbHdoiBttHdrReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhBbHdoiBttHdr data = xhBbHdoiBttHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhBbHdoiBttHdrRepository.existsBySoBbHaoDoiAndIdNot(req.getSoBbHaoDoi(), req.getId())) {
            throw new Exception("Số biên bản tịnh kho " + req.getSoBbHaoDoi() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhBbHdoiBttHdr update = xhBbHdoiBttHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhBbHdoiBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbHdoiBttHdr> list = xhBbHdoiBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhBbHdoiBttHdr> allById = xhBbHdoiBttHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        allById.forEach(data -> {
            List<XhBbHdoiBttDtl> listDtl = xhBbHdoiBttDtlRepository.findAllByIdHdr(data.getId());
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
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

    public XhBbHdoiBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbHdoiBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhBbHdoiBttHdr data = xhBbHdoiBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_KTVBQ, Contains.TUCHOI_KT, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với biên bản hao dôi ở trạng thái bản nháp hoặc từ chối");
        }
        xhBbHdoiBttDtlRepository.deleteAllByIdHdr(data.getId());
        xhBbHdoiBttHdrRepository.delete(data);
    }

    public XhBbHdoiBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhBbHdoiBttHdr data = xhBbHdoiBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
        XhBbHdoiBttHdr created = xhBbHdoiBttHdrRepository.save(data);
        if (statusReq.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            this.updateKiemNghiemAndLayMauAndQuyetDinh(created);
        }
        return created;
    }

    private void updateKiemNghiemAndLayMauAndQuyetDinh(XhBbHdoiBttHdr created) {
        xhQdNvXhBttHdrRepository.findById(created.getIdQdNv()).ifPresent(quyetDinh -> {
            quyetDinh.setIdHaoDoi(created.getId());
            quyetDinh.setSoHaoDoi(created.getSoBbHaoDoi());
            xhQdNvXhBttHdrRepository.save(quyetDinh);
        });
        xhPhieuKtraCluongBttHdrRepository.findById(created.getIdPhieuKiemNghiem()).ifPresent(kiemNghiem -> {
            xhBbLayMauBttHdrRepository.findById(kiemNghiem.getIdBbLayMau()).ifPresent(layMau -> {
                layMau.setIdHaoDoi(created.getId());
                layMau.setSoBbHaoDoi(created.getSoBbHaoDoi());
                xhBbLayMauBttHdrRepository.save(layMau);
            });
        });
    }

    public void export(CustomUserDetails currentUser, XhBbHdoiBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhBbHdoiBttHdr> page = this.searchPage(currentUser, req);
        List<XhBbHdoiBttHdr> data = page.getContent();
        String title = "Danh sách biên bản hao dôi";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số BB hao dôi", "Ngày lập BB hao dôi", "Số BB tịnh kho", "Số phiếu KNCL", "Số bảng kê",
                "Số phiếu xuất kho", "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-hao-doi.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhBbHdoiBttHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNamKh();
            objs[3] = hdr.getNgayKyQdNv();
            objs[4] = hdr.getTenDiemKho();
            objs[5] = hdr.getTenNganLoKho();
            objs[6] = hdr.getSoBbHaoDoi();
            objs[7] = hdr.getNgayLapBienBan();
            objs[8] = hdr.getSoBbTinhKho();
            objs[9] = hdr.getSoPhieuKiemNghiem();
            Object[] finalObjs = objs;
            hdr.getChildren().forEach(dtl -> {
                finalObjs[10] = dtl.getSoBangKeHang();
                finalObjs[11] = dtl.getSoPhieuXuatKho();
                finalObjs[12] = dtl.getNgayXuatKho();
            });
            objs[13] = hdr.getTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}