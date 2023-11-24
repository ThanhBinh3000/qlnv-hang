package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbHaoDoiDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbHaoDoiDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdrReq;
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
public class XhDgBbHaoDoiService extends BaseServiceImpl {

    @Autowired
    private XhDgBbHaoDoiHdrRepository xhDgBbHaoDoiHdrRepository;
    @Autowired
    private XhDgBbHaoDoiDtlRepository xhDgBbHaoDoiDtlRepository;
    @Autowired
    private XhPhieuKnghiemCluongRepository xhPhieuKnghiemCluongRepository;
    @Autowired
    private XhBbLayMauRepository xhBbLayMauRepository;
    @Autowired
    private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhDgBbHaoDoiHdr> searchPage(CustomUserDetails currentUser, XhDgBbHaoDoiHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDCC);
            req.setMaDviCha(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhDgBbHaoDoiHdr> search = xhDgBbHaoDoiHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            try {
                data.setMapVthh(mapDmucVthh);
                data.setMapDmucDvi(mapDmucDvi);
                data.setTrangThai(data.getTrangThai());
                List<XhDgBbHaoDoiDtl> listDtl = xhDgBbHaoDoiDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Transactional
    public XhDgBbHaoDoiHdr create(CustomUserDetails currentUser, XhDgBbHaoDoiHdrReq req) throws Exception {
        System.out.println("Giá trị của biến number là: " + req);
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoBbHaoDoi()) && xhDgBbHaoDoiHdrRepository.existsBySoBbHaoDoi(req.getSoBbHaoDoi())) {
            throw new Exception("Số biên bản hao dôi " + req.getSoBbHaoDoi() + " đã tồn tại");
        }
        XhDgBbHaoDoiHdr data = new XhDgBbHaoDoiHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdThuKho(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoBbHaoDoi().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhDgBbHaoDoiHdr created = xhDgBbHaoDoiHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhDgBbHaoDoiHdrReq req, Long idHdr) {
        xhDgBbHaoDoiDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhDgBbHaoDoiDtl dtlReq : req.getChildren()) {
            XhDgBbHaoDoiDtl dataDtl = new XhDgBbHaoDoiDtl();
            BeanUtils.copyProperties(dtlReq, dataDtl, "id");
            dataDtl.setId(null);
            dataDtl.setIdHdr(idHdr);
            xhDgBbHaoDoiDtlRepository.save(dataDtl);
        }
    }

    @Transactional
    public XhDgBbHaoDoiHdr update(CustomUserDetails currentUser, XhDgBbHaoDoiHdrReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDgBbHaoDoiHdr data = xhDgBbHaoDoiHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDgBbHaoDoiHdrRepository.existsBySoBbHaoDoiAndIdNot(req.getSoBbHaoDoi(), req.getId())) {
            throw new Exception("Số biên bản tịnh kho " + req.getSoBbHaoDoi() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "idThuKho");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhDgBbHaoDoiHdr update = xhDgBbHaoDoiHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }


    public List<XhDgBbHaoDoiHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBbHaoDoiHdr> list = xhDgBbHaoDoiHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhDgBbHaoDoiHdr> allById = xhDgBbHaoDoiHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        allById.forEach(data -> {
            List<XhDgBbHaoDoiDtl> listDtl = xhDgBbHaoDoiDtlRepository.findAllByIdHdr(data.getId());
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

    public XhDgBbHaoDoiHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDgBbHaoDoiHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhDgBbHaoDoiHdr data = xhDgBbHaoDoiHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_KTVBQ, Contains.TUCHOI_KT, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với biên bản hao dôi ở trạng thái bản nháp hoặc từ chối");
        }
        xhDgBbHaoDoiDtlRepository.deleteAllByIdHdr(data.getId());
        xhDgBbHaoDoiHdrRepository.delete(data);
    }


    public XhDgBbHaoDoiHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDgBbHaoDoiHdr data = xhDgBbHaoDoiHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
        XhDgBbHaoDoiHdr created = xhDgBbHaoDoiHdrRepository.save(data);
        if (statusReq.getTrangThai().equals(Contains.DADUYET_LDCC)) {
            this.updateKiemNghiemAndLayMauAndQuyetDinh(created);
        }
        return created;
    }

    private void updateKiemNghiemAndLayMauAndQuyetDinh(XhDgBbHaoDoiHdr created) {
        xhPhieuKnghiemCluongRepository.findById(created.getIdPhieuKiemNghiem()).ifPresent(kiemNghiem -> {
            xhBbLayMauRepository.findById(kiemNghiem.getIdBbLayMau()).ifPresent(layMau -> {
                layMau.setIdHaoDoi(created.getId());
                layMau.setSoBbHaoDoi(created.getSoBbHaoDoi());
                xhBbLayMauRepository.save(layMau);
            });
        });
    }

    public void export(CustomUserDetails currentUser, XhDgBbHaoDoiHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDgBbHaoDoiHdr> page = this.searchPage(currentUser, req);
        List<XhDgBbHaoDoiHdr> data = page.getContent();
        String title = "Danh sách biên bản hao dôi";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số BB hao dôi", "Ngày lập BB hao dôi", "Số BB tịnh kho", "Số phiếu KNCL", "Số bảng kê",
                "Số phiếu xuất kho", "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-hao-doi.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhDgBbHaoDoiHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNam();
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

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templatePath = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "bandaugia/" + templatePath;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhDgBbHaoDoiHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}