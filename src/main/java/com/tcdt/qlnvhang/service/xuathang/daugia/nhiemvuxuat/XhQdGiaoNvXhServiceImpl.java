package com.tcdt.qlnvhang.service.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiem;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvXhDdiemReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatCtReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
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
public class XhQdGiaoNvXhServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
    @Autowired
    private XhQdGiaoNvXhDtlRepository xhQdGiaoNvXhDtlRepository;
    @Autowired
    private XhQdGiaoNvXhDdiemRepository xhQdGiaoNvXhDdiemRepository;
    @Autowired
    private XhHopDongHdrRepository xhHopDongHdrRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Page<XhQdGiaoNvXh> searchPage(CustomUserDetails currentUser, XhQdGiaoNvuXuatReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_TONG_CUC)) {
            req.setTrangThai(Contains.BAN_HANH);
        } else if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setTrangThai(Contains.BAN_HANH);
            req.setMaDviCon(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdGiaoNvXh> search = xhQdGiaoNvXhRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            try {
                data.setMapVthh(mapDmucVthh);
                data.setMapDmucDvi(mapDmucDvi);
                data.setTrangThai(data.getTrangThai());
                data.setTrangThaiXh(data.getTrangThaiXh());
                List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Transactional
    public XhQdGiaoNvXh create(CustomUserDetails currentUser, XhQdGiaoNvuXuatReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoQdNv()) && xhQdGiaoNvXhRepository.existsBySoQdNv(req.getSoQdNv())) {
            throw new Exception("Số quyết định nhiệm vụ " + req.getSoQdNv() + " đã tồn tại");
        }
        XhQdGiaoNvXh data = new XhQdGiaoNvXh();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setIdCanBoPhong(currentUser.getUser().getId());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        XhQdGiaoNvXh created = xhQdGiaoNvXhRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    public void saveDetail(XhQdGiaoNvuXuatReq req, Long idHdr) {
        xhQdGiaoNvXhDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdGiaoNvuXuatCtReq dtlReq : req.getChildren()) {
            XhQdGiaoNvXhDtl dtl = new XhQdGiaoNvXhDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            dtl.setTrangThai(Contains.CHUA_THUC_HIEN);
            xhQdGiaoNvXhDtlRepository.save(dtl);
            xhQdGiaoNvXhDdiemRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhQdGiaoNvXhDdiemReq ddiemReq : dtlReq.getChildren()) {
                XhQdGiaoNvXhDdiem ddiem = new XhQdGiaoNvXhDdiem();
                BeanUtils.copyProperties(ddiemReq, ddiem, "id");
                ddiem.setId(null);
                ddiem.setIdDtl(dtl.getId());
                xhQdGiaoNvXhDdiemRepository.save(ddiem);
            }
        }
    }

    @Transactional
    public XhQdGiaoNvXh update(CustomUserDetails currentUser, XhQdGiaoNvuXuatReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdGiaoNvXh data = xhQdGiaoNvXhRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhQdGiaoNvXhRepository.existsBySoQdNvAndIdNot(req.getSoQdNv(), req.getId())) {
            throw new Exception("Số quyết định nhiệm vụ " + req.getSoQdNv() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiXh");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhQdGiaoNvXh update = xhQdGiaoNvXhRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhQdGiaoNvXh> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdGiaoNvXh> list = xhQdGiaoNvXhRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhQdGiaoNvXh> allById = xhQdGiaoNvXhRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhQdGiaoNvXh data : allById) {
            List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdHdr(data.getId());
            for (XhQdGiaoNvXhDtl dataDtl : listDtl) {
                List<XhQdGiaoNvXhDdiem> listDdiem = xhQdGiaoNvXhDdiemRepository.findAllByIdDtl(dataDtl.getId());
                listDdiem.forEach(dataDdiem -> {
                    dataDdiem.setTenDiemKho(mapDmucDvi.getOrDefault(dataDdiem.getMaDiemKho(), null));
                    dataDdiem.setTenNhaKho(mapDmucDvi.getOrDefault(dataDdiem.getMaNhaKho(), null));
                    dataDdiem.setTenNganKho(mapDmucDvi.getOrDefault(dataDdiem.getMaNganKho(), null));
                    dataDdiem.setTenLoKho(mapDmucDvi.getOrDefault(dataDdiem.getMaLoKho(), null));
                });
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                dataDtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dataDtl.getTrangThai()));
                dataDtl.setChildren(listDdiem);
            }
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNhapXuat(mapKieuNhapXuat);
            data.setTrangThai(data.getTrangThai());
            if (data.getIdCanBoPhong() != null) {
                userInfoRepository.findById(data.getIdCanBoPhong()).ifPresent(userInfo -> {
                    data.setTenCanBoPhong(userInfo.getFullName());
                });
            }
            if (data.getIdTruongPhong() != null) {
                userInfoRepository.findById(data.getIdTruongPhong()).ifPresent(userInfo -> {
                    data.setTenTruongPhong(userInfo.getFullName());
                });
            }
            if (data.getIdLanhDaoCuc() != null) {
                userInfoRepository.findById(data.getIdLanhDaoCuc()).ifPresent(userInfo -> {
                    data.setTenLanhDaoCuc(userInfo.getFullName());
                });
            }
            data.setChildren(listDtl);
        }
        return allById;
    }

    public XhQdGiaoNvXh detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdGiaoNvXh> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhQdGiaoNvXh data = xhQdGiaoNvXhRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdHdr(data.getId());
        for (XhQdGiaoNvXhDtl dataDtl : listDtl) {
            xhQdGiaoNvXhDdiemRepository.deleteAllByIdDtl(dataDtl.getId());
        }
        xhQdGiaoNvXhDtlRepository.deleteAllByIdHdr(data.getId());
        xhQdGiaoNvXhRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdGiaoNvXh> list = xhQdGiaoNvXhRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = list.stream().allMatch(hdr -> hdr.getTrangThai().equals(Contains.DUTHAO) ||
                hdr.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                hdr.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<Long> idHdr = list.stream().map(XhQdGiaoNvXh::getId).collect(Collectors.toList());
        List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findByIdHdrIn(idHdr);
        List<Long> idDtl = listDtl.stream().map(XhQdGiaoNvXhDtl::getId).collect(Collectors.toList());
        List<XhQdGiaoNvXhDdiem> listDdiem = xhQdGiaoNvXhDdiemRepository.findByIdDtlIn(idDtl);
        xhQdGiaoNvXhDdiemRepository.deleteAll(listDdiem);
        xhQdGiaoNvXhDtlRepository.deleteAll(listDtl);
        xhQdGiaoNvXhRepository.deleteAll(list);
    }

    public XhQdGiaoNvXh approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdGiaoNvXh data = xhQdGiaoNvXhRepository.findById(Long.valueOf(statusReq.getId()))
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
                data.setIdTruongPhong(currentUser.getUser().getId());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setIdLanhDaoCuc(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            xhHopDongHdrRepository.findById(data.getIdHopDong()).ifPresent(hopDong -> {
                hopDong.setIdQdNv(data.getId());
                hopDong.setSoQdNv(data.getSoQdNv());
                xhHopDongHdrRepository.save(hopDong);
            });
        }
        XhQdGiaoNvXh created = xhQdGiaoNvXhRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhQdGiaoNvuXuatReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdGiaoNvXh> page = this.searchPage(currentUser, req);
        List<XhQdGiaoNvXh> data = page.getContent();
        String title = "Danh sách quyết định giao nhiệm vụ xuất hàng bán đấu giá";
        String[] rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Số hợp đồng",
                "Chủng loại hàng hóa", "Thời gian giao nhận hàng", "Trích yếu quyết định", "Số BB tịnh kho", "Số BB hao dôi",
                "Trạng thái QĐ", "Trạng thái XH"};
        String fileName = "danh-sach-quyet-dinh-nhiem-vu-xuat-hang-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhQdGiaoNvXh hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNam();
            objs[2] = hdr.getSoQdNv();
            objs[3] = hdr.getNgayKy();
            objs[4] = hdr.getSoHopDong();
            objs[5] = hdr.getTenCloaiVthh();
            objs[6] = hdr.getTgianGiaoHang();
            objs[7] = hdr.getTrichYeu();
            objs[8] = hdr.getBienBanTinhKho();
            objs[9] = hdr.getBienBanHaoDoi();
            objs[10] = hdr.getTenTrangThai();
            objs[11] = hdr.getTenTrangThaiXh();
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
            XhQdGiaoNvXh detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}