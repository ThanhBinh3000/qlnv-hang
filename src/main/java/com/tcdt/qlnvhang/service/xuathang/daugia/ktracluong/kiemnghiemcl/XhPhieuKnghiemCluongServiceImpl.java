package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluongCt;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongCtReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
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
import java.util.stream.Collectors;

@Service
public class XhPhieuKnghiemCluongServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhPhieuKnghiemCluongRepository xhPhieuKnghiemCluongRepository;
    @Autowired
    private XhPhieuKnghiemCluongCtRepository xhPhieuKnghiemCluongCtRepository;
    @Autowired
    private XhBbLayMauRepository xhBbLayMauRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhPhieuKnghiemCluong> searchPage(CustomUserDetails currentUser, XhPhieuKnghiemCluongReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDC);
            req.setMaDviCon(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhPhieuKnghiemCluong> search = xhPhieuKnghiemCluongRepository.searchPage(req, pageable);
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
    public XhPhieuKnghiemCluong create(CustomUserDetails currentUser, XhPhieuKnghiemCluongReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoPhieuKiemNghiem()) && xhPhieuKnghiemCluongRepository.existsBySoPhieuKiemNghiem(req.getSoPhieuKiemNghiem())) {
            throw new Exception("Số phiếu kiểm nghiệm chất lượng " + req.getSoPhieuKiemNghiem() + " đã tồn tại");
        }
        XhPhieuKnghiemCluong data = new XhPhieuKnghiemCluong();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdNguoiKiemNghiem(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoPhieuKiemNghiem().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhPhieuKnghiemCluong created = xhPhieuKnghiemCluongRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhPhieuKnghiemCluongReq req, Long idHdr) {
        xhPhieuKnghiemCluongCtRepository.deleteAllByIdHdr(idHdr);
        for (XhPhieuKnghiemCluongCtReq dtlReq : req.getChildren()) {
            XhPhieuKnghiemCluongCt dtl = new XhPhieuKnghiemCluongCt();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setId(null);
            dtl.setIdHdr(idHdr);
            xhPhieuKnghiemCluongCtRepository.save(dtl);
        }
    }

    @Transactional
    public XhPhieuKnghiemCluong update(CustomUserDetails currentUser, XhPhieuKnghiemCluongReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhPhieuKnghiemCluong data = xhPhieuKnghiemCluongRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhPhieuKnghiemCluongRepository.existsBySoPhieuKiemNghiemAndIdNot(req.getSoPhieuKiemNghiem(), req.getId())) {
            throw new Exception("Số phiếu kiểm nghiệm chất lượng " + req.getSoPhieuKiemNghiem() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "idNguoiKiemNghiem");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhPhieuKnghiemCluong update = xhPhieuKnghiemCluongRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhPhieuKnghiemCluong> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuKnghiemCluong> list = xhPhieuKnghiemCluongRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhPhieuKnghiemCluong> allById = xhPhieuKnghiemCluongRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapHinhThucBaoQuan = getListDanhMucChung("HINH_THUC_BAO_QUAN");
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapHinhThucBaoQuan(mapHinhThucBaoQuan);
            data.setTrangThai(data.getTrangThai());
            if (data.getIdThuKho() != null) {
                userInfoRepository.findById(data.getIdThuKho()).ifPresent(userInfo -> {
                    data.setTenThuKho(userInfo.getFullName());
                });
            }
            if (data.getIdNguoiKiemNghiem() != null) {
                userInfoRepository.findById(data.getIdNguoiKiemNghiem()).ifPresent(userInfo -> {
                    data.setTenNguoiKiemNghiem(userInfo.getFullName());
                });
            }
            if (data.getIdTruongPhongKtvbq() != null) {
                userInfoRepository.findById(data.getIdTruongPhongKtvbq()).ifPresent(userInfo -> {
                    data.setTenTruongPhongKtvbq(userInfo.getFullName());
                });
            }
            if (data.getIdLanhDaoCuc() != null) {
                userInfoRepository.findById(data.getIdLanhDaoCuc()).ifPresent(userInfo -> {
                    data.setTenLanhDaoCuc(userInfo.getFullName());
                });
            }
            List<XhPhieuKnghiemCluongCt> listDtl = xhPhieuKnghiemCluongCtRepository.findAllByIdHdr(data.getId());
            data.setChildren(listDtl);
        });
        return allById;
    }

    public XhPhieuKnghiemCluong detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhPhieuKnghiemCluong> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhPhieuKnghiemCluong data = xhPhieuKnghiemCluongRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với phiếu kiểm nghiệm chất lượng ở trạng thái bản nháp hoặc từ chối");
        }
        xhPhieuKnghiemCluongCtRepository.deleteAllByIdHdr(data.getId());
        xhPhieuKnghiemCluongRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhPhieuKnghiemCluong> list = xhPhieuKnghiemCluongRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = list.stream().allMatch(hdr -> hdr.getTrangThai().equals(Contains.DUTHAO) ||
                hdr.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                hdr.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với phiếu kiểm nghiệm chất lượng ở trạng thái bản nháp hoặc từ chối");
        }
        List<Long> idHdr = list.stream().map(XhPhieuKnghiemCluong::getId).collect(Collectors.toList());
        List<XhPhieuKnghiemCluongCt> listDtl = xhPhieuKnghiemCluongCtRepository.findByIdHdrIn(idHdr);
        xhPhieuKnghiemCluongCtRepository.deleteAll(listDtl);
        xhPhieuKnghiemCluongRepository.deleteAll(list);
    }

    public XhPhieuKnghiemCluong approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhPhieuKnghiemCluong data = xhPhieuKnghiemCluongRepository.findById(Long.valueOf(statusReq.getId()))
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
                data.setIdTruongPhongKtvbq(currentUser.getUser().getId());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setIdLanhDaoCuc(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhPhieuKnghiemCluong created = xhPhieuKnghiemCluongRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhPhieuKnghiemCluongReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhPhieuKnghiemCluong> page = this.searchPage(currentUser, req);
        List<XhPhieuKnghiemCluong> data = page.getContent();
        String title = "Danh sách phiếu kiểm nghiệm chất lượng";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH", "Năm KH", "Thời hạn XH", "Điểm kho",
                "Ngăn/Lô kho", "Số phiếu KNCL", "Ngày kiểm nghiệm", "Số BB LM/BGM", "Ngày lấy mẫu",
                "Số BB tịnh kho", "Ngày lập BB tịnh kho", "Trạng thái"};
        String fileName = "dạm-sach-phieu-kiem-nghiem-chat-luong.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhPhieuKnghiemCluong hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNam();
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
            String templatePath = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "bandaugia/" + templatePath;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhPhieuKnghiemCluong detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}