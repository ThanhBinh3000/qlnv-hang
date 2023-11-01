package com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrReq;
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
public class XhBbLayMauBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhBbLayMauBttHdrRepository xhBbLayMauBttHdrRepository;
    @Autowired
    private XhBbLayMauBttDtlRepository xhBbLayMauBttDtlRepository;
    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhBbLayMauBttHdr> searchPage(CustomUserDetails currentUser, XhBbLayMauBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setTrangThai(Contains.DADUYET_LDCC);
            req.setMaDviCha(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhBbLayMauBttHdr> search = xhBbLayMauBttHdrRepository.searchPage(req, pageable);
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
    public XhBbLayMauBttHdr create(CustomUserDetails currentUser, XhBbLayMauBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoBbLayMau()) && xhBbLayMauBttHdrRepository.existsBySoBbLayMau(req.getSoBbLayMau())) {
            throw new Exception("Số biên bản lấy mẫu " + req.getSoBbLayMau() + " đã tồn tại");
        }
        XhBbLayMauBttHdr data = new XhBbLayMauBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdKtvBaoQuan(currentUser.getUser().getId());
        data.setId(Long.parseLong(data.getSoBbLayMau().split("/")[0]));
        data.setTrangThai(Contains.DU_THAO);
        XhBbLayMauBttHdr created = xhBbLayMauBttHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhBbLayMauBttHdrReq req, Long idHdr) {
        xhBbLayMauBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhBbLayMauBttDtlReq dtlReq : req.getChildren()) {
            XhBbLayMauBttDtl dtl = new XhBbLayMauBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setId(null);
            dtl.setIdHdr(idHdr);
            xhBbLayMauBttDtlRepository.save(dtl);
        }
    }

    @Transactional
    public XhBbLayMauBttHdr update(CustomUserDetails currentUser, XhBbLayMauBttHdrReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhBbLayMauBttHdr data = xhBbLayMauBttHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhBbLayMauBttHdrRepository.existsBySoBbLayMauAndIdNot(req.getSoBbLayMau(), req.getId())) {
            throw new Exception("Số biên bản lấy mẫu " + req.getSoBbLayMau() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhBbLayMauBttHdr update = xhBbLayMauBttHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhBbLayMauBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbLayMauBttHdr> list = xhBbLayMauBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhBbLayMauBttHdr> allById = xhBbLayMauBttHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhBbLayMauBttHdr data : allById) {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapDmucVthh);
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
            List<XhBbLayMauBttDtl> listDtl = xhBbLayMauBttDtlRepository.findAllByIdHdr(data.getId());
            data.setChildren(listDtl);
        }
        return allById;
    }

    public XhBbLayMauBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhBbLayMauBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhBbLayMauBttHdr data = xhBbLayMauBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TUCHOI_LDCC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với biên bản lấy mẫu ở trạng thái bản nháp hoặc từ chối");
        }
        xhBbLayMauBttDtlRepository.deleteAllByIdHdr(data.getId());
        xhBbLayMauBttHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhBbLayMauBttHdr> list = xhBbLayMauBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = list.stream().allMatch(hdr -> hdr.getTrangThai().equals(Contains.DUTHAO) ||
                hdr.getTrangThai().equals(Contains.TUCHOI_LDCC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với biên bản lấy mẫu ở trạng thái bản nháp hoặc từ chối.");
        }
        List<Long> idHdr = list.stream().map(XhBbLayMauBttHdr::getId).collect(Collectors.toList());
        List<XhBbLayMauBttDtl> listDtl = xhBbLayMauBttDtlRepository.findByIdHdrIn(idHdr);
        xhBbLayMauBttDtlRepository.deleteAll(listDtl);
        xhBbLayMauBttHdrRepository.deleteAll(list);
    }

    public XhBbLayMauBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhBbLayMauBttHdr data = xhBbLayMauBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
        XhBbLayMauBttHdr created = xhBbLayMauBttHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhBbLayMauBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhBbLayMauBttHdr> page = this.searchPage(currentUser, req);
        List<XhBbLayMauBttHdr> data = page.getContent();
        String title = "Danh sách biên bản lấy mẫu/bàn giao mẫu";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Số BB LM/BGM",
                "Ngày lấy mẫu", "Điểm kho", "Lô kho", "Số BB tịnh kho", "Ngày xuất dốc kho",
                "Số BB hao dôi", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-lay-mau/ban-giao-mau.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhBbLayMauBttHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdNv();
            objs[2] = hdr.getNamKh();
            objs[3] = hdr.getNgayKyQdNv();
            objs[4] = hdr.getSoBbLayMau();
            objs[5] = hdr.getNgayLayMau();
            objs[6] = hdr.getTenDiemKho();
            objs[7] = hdr.getTenNganLoKho();
            objs[8] = hdr.getSoBbTinhKho();
            objs[9] = hdr.getNgayXuatDocKho();
            objs[10] = hdr.getSoBbHaoDoi();
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
            String templatePath = baseReportFolder + "/bantructiep/";
            XhBbLayMauBttHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            if (detail.getLoaiVthh().startsWith("02")) {
                templatePath += "Biên bản lấy mẫu bàn giao mẫu vật tư.docx";
            } else {
                templatePath += "Biên bản lấy mẫu bàn giao mẫu lương thực.docx";
            }
            Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
            xhQdNvXhBttHdrRepository.findById(detail.getIdQdNv())
                    .ifPresent(quyetDinh -> {
                        if (detail.getPthucBanTrucTiep().equals("01")) {
                            detail.setTenBenMua(quyetDinh.getTenBenMua());
                        }
                        detail.setMaDviCha(quyetDinh.getMaDvi());
                        if (mapDmucDvi.containsKey((detail.getMaDviCha()))) {
                            Map<String, Object> objDonVi = mapDmucDvi.get(detail.getMaDviCha());
                            detail.setTenDviCha(objDonVi.get("tenDvi").toString());
                        }
                    });
            if (detail.getPthucBanTrucTiep().equals("02")) {
                xhHopDongBttHdrRepository.findById(detail.getIdHopDong())
                        .ifPresent(hopDong -> detail.setTenBenMua(hopDong.getTenBenMua()));
            }
            List<XhBbLayMauBttDtl> listDtl = xhBbLayMauBttDtlRepository.findAllByIdHdr(detail.getId());
            if (listDtl != null || !listDtl.isEmpty()) {
                List<XhBbLayMauBttDtl> filteredPhuongPhapLayMau = listDtl.stream().filter(type -> "PPLM".equals(type.getType()) && type.getChecked()).collect(Collectors.toList());
                List<XhBbLayMauBttDtl> filteredChiTieuKiemTra = listDtl.stream().filter(type -> "CTCL".equals(type.getType()) && type.getChecked()).collect(Collectors.toList());
                if (!filteredPhuongPhapLayMau.isEmpty()) {
                    detail.setPhuongPhapLayMau(filteredPhuongPhapLayMau.get(0).getTen());
                }
                if (!filteredChiTieuKiemTra.isEmpty()) {
                    detail.setChiTieuKiemTra(String.join(",", filteredChiTieuKiemTra.stream().map(XhBbLayMauBttDtl::getTen).collect(Collectors.toList())));
                }
                detail.setChildren(listDtl.stream().filter(type -> "NLQ".equals(type.getType())).collect(Collectors.toList()));
            }
            FileInputStream inputStream = new FileInputStream(templatePath);
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}