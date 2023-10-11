package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLo;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhDxKhBanDauGiaServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;
    @Autowired
    private XhDxKhBanDauGiaDtlRepository xhDxKhBanDauGiaDtlRepository;
    @Autowired
    private XhDxKhBanDauGiaPhanLoRepository xhDxKhBanDauGiaPhanLoRepository;

    public Page<XhDxKhBanDauGia> searchPage(CustomUserDetails currentUser, XhDxKhBanDauGiaReq req) throws Exception {
        String dvql = currentUser.getDvql();
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql.substring(0, 4));
        } else if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhDxKhBanDauGia> search = xhDxKhBanDauGiaRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
            data.setTrangThaiTh(data.getTrangThaiTh());
        });
        return search;
    }

    @Transactional
    public XhDxKhBanDauGia create(CustomUserDetails currentUser, XhDxKhBanDauGiaReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoDxuat()) && xhDxKhBanDauGiaRepository.existsBySoDxuat(req.getSoDxuat())) {
            throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
        }
        XhDxKhBanDauGia data = new XhDxKhBanDauGia();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        //data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiTh(Contains.CHUATONGHOP);
        int slDviTsan = data.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanDauGiaPhanLo::getMaDviTsan)
                .collect(Collectors.toSet())
                .size();
        data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));
        XhDxKhBanDauGia created = xhDxKhBanDauGiaRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhDxKhBanDauGiaReq req, Long idHdr) {
        xhDxKhBanDauGiaDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhDxKhBanDauGiaDtl dataDtlReq : req.getChildren()) {
            XhDxKhBanDauGiaDtl dataDtl = new XhDxKhBanDauGiaDtl();
            BeanUtils.copyProperties(dataDtlReq, dataDtl, "id");
            dataDtl.setIdHdr(idHdr);
            xhDxKhBanDauGiaDtlRepository.save(dataDtl);
            xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDtl(dataDtlReq.getId());
            for (XhDxKhBanDauGiaPhanLo dataDtlPhanLoReq : dataDtlReq.getChildren()) {
                XhDxKhBanDauGiaPhanLo dataDtlPhanLo = new XhDxKhBanDauGiaPhanLo();
                BeanUtils.copyProperties(dataDtlPhanLoReq, dataDtlPhanLo, "id");
                dataDtlPhanLo.setIdDtl(dataDtl.getId());
                xhDxKhBanDauGiaPhanLoRepository.save(dataDtlPhanLo);
            }
        }
    }

    @Transactional
    public XhDxKhBanDauGia update(CustomUserDetails currentUser, XhDxKhBanDauGiaReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanDauGia data = xhDxKhBanDauGiaRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhDxKhBanDauGiaRepository.existsBySoDxuatAndIdNot(req.getSoDxuat(), req.getId())) {
            throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiTh");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        int slDviTsan = data.getChildren().stream()
                .flatMap(item -> item.getChildren().stream())
                .map(XhDxKhBanDauGiaPhanLo::getMaDviTsan)
                .collect(Collectors.toSet())
                .size();
        data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));
        XhDxKhBanDauGia updated = xhDxKhBanDauGiaRepository.save(data);
        this.saveDetail(req, updated.getId());
        return updated;
    }

    public List<XhDxKhBanDauGia> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhDxKhBanDauGia> list = xhDxKhBanDauGiaRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapPhuongThucTt = getListDanhMucChung("PHUONG_THUC_TT");
        List<XhDxKhBanDauGia> allById = xhDxKhBanDauGiaRepository.findAllById(ids);
        for (XhDxKhBanDauGia data : allById) {
            List<XhDxKhBanDauGiaDtl> dauGiaDtl = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(data.getId());
            for (XhDxKhBanDauGiaDtl dataDtl : dauGiaDtl) {
                List<XhDxKhBanDauGiaPhanLo> dauGiaPhanLo = xhDxKhBanDauGiaPhanLoRepository.findAllByIdDtl(dataDtl.getId());
                dauGiaPhanLo.forEach(dataPhanLo -> {
                    dataPhanLo.setTenDiemKho(mapDmucDvi.getOrDefault(dataPhanLo.getMaDiemKho(), null));
                    dataPhanLo.setTenNhaKho(mapDmucDvi.getOrDefault(dataPhanLo.getMaNhaKho(), null));
                    dataPhanLo.setTenNganKho(mapDmucDvi.getOrDefault(dataPhanLo.getMaNganKho(), null));
                    dataPhanLo.setTenLoKho(mapDmucDvi.getOrDefault(dataPhanLo.getMaLoKho(), null));
                    dataPhanLo.setTenLoaiVthh(mapVthh.getOrDefault(dataPhanLo.getLoaiVthh(), null));
                    dataPhanLo.setTenCloaiVthh(mapVthh.getOrDefault(dataPhanLo.getCloaiVthh(), null));
                });
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                dataDtl.setChildren(dauGiaPhanLo);
            }
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setMapPhuongThucTt(mapPhuongThucTt);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(dauGiaDtl);
        }
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhDxKhBanDauGia data = xhDxKhBanDauGiaRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC, Contains.TU_CHOI_CBV);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhDxKhBanDauGiaDtl> dauGiaDtl = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(data.getId());
        for (XhDxKhBanDauGiaDtl dtl : dauGiaDtl) {
            xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDtl(dtl.getId());
        }
        xhDxKhBanDauGiaDtlRepository.deleteAllByIdHdr(data.getId());
        xhDxKhBanDauGiaRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhDxKhBanDauGia> list = xhDxKhBanDauGiaRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = list.stream().allMatch(hdr ->
                hdr.getTrangThai().equals(Contains.DUTHAO) ||
                        hdr.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                        hdr.getTrangThai().equals(Contains.TUCHOI_LDC) ||
                        hdr.getTrangThai().equals(Contains.TU_CHOI_CBV));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với đề xuất ở trạng thái bản nháp hoặc từ chối.");
        }
        List<Long> idHdr = list.stream().map(XhDxKhBanDauGia::getId).collect(Collectors.toList());
        List<XhDxKhBanDauGiaDtl> listDtl = xhDxKhBanDauGiaDtlRepository.findByIdHdrIn(idHdr);
        List<Long> idDtl = listDtl.stream().map(XhDxKhBanDauGiaDtl::getId).collect(Collectors.toList());
        List<XhDxKhBanDauGiaPhanLo> listPhanLo = xhDxKhBanDauGiaPhanLoRepository.findByIdDtlIn(idDtl);
        xhDxKhBanDauGiaPhanLoRepository.deleteAll(listPhanLo);
        xhDxKhBanDauGiaDtlRepository.deleteAll(listDtl);
        xhDxKhBanDauGiaRepository.deleteAll(list);
    }

    public XhDxKhBanDauGia approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhDxKhBanDauGia data = xhDxKhBanDauGiaRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
            case Contains.CHODUYET_TP + Contains.TU_CHOI_CBV:
                data.setNguoiGuiDuyetId(currentUser.getUser().getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.TU_CHOI_CBV + Contains.DADUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
            case Contains.DA_DUYET_CBV + Contains.DADUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhDxKhBanDauGia created = xhDxKhBanDauGiaRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhDxKhBanDauGiaReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhDxKhBanDauGia> page = this.searchPage(currentUser, req);
        List<XhDxKhBanDauGia> data = page.getContent();
        String title = "Danh sách đề xuất kế hoạch bán đấu giá";
        String[] rowsName = new String[]{"STT", "Năm KH", "Số KH/tờ trình", "Ngày lập KH", "Ngày duyệt KH",
                "Số QĐ duyệt KH bán ĐG", "Ngày ký QĐ", "Trích yếu", "Loại hàng hóa", "Chủng loại hàng hóa",
                "Số ĐV tài sản", "SL HĐ đã ký", "Số QĐ giao chỉ tiêu", "Trạng thái"};
        String fileName = "danh-sach-dx-kh-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhDxKhBanDauGia hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoDxuat();
            objs[3] = hdr.getNgayTao();
            objs[4] = hdr.getNgayPduyet();
            objs[5] = hdr.getSoQdPd();
            objs[6] = hdr.getNgayKyQd();
            objs[7] = hdr.getTrichYeu();
            objs[8] = hdr.getTenLoaiVthh();
            objs[9] = hdr.getTenCloaiVthh();
            objs[10] = hdr.getSlDviTsan();
            objs[11] = null;
            objs[12] = hdr.getSoQdCtieu();
            objs[13] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) {
        return xhDxKhBanDauGiaRepository.countSLDalenKh(req.getYear(), req.getLoaiVthh(), req.getMaDvi(), req.getLastest());
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body) {
        try {
            String tenBaoCao = DataUtils.safeToString(body.get("tenBaoCao"));
            Long id = DataUtils.safeToLong(body.get("id"));
            ReportTemplateRequest reportTemplateRequest = new ReportTemplateRequest();
            reportTemplateRequest.setFileName(tenBaoCao);
            ReportTemplate model = findByTenFile(reportTemplateRequest);
            byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
            List<XhDxKhBanDauGia> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
        } catch (IOException | XDocReportException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}