package com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhKqBdgHdrServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhKqBdgHdrRepository xhKqBdgHdrRepository;
    @Autowired
    private XhHopDongHdrRepository xhHopDongHdrRepository;
    @Autowired
    private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;

    public Page<XhKqBdgHdr> searchPage(CustomUserDetails currentUser, XhKqBdgHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setTrangThai(Contains.BAN_HANH);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhKqBdgHdr> search = xhKqBdgHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapHinhThucDg = getListDanhMucChung("HINH_THUC_DG");
        Map<String, String> mapPhuongThucDg = getListDanhMucChung("PHUONG_THUC_DG");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapHinhThucDg(mapHinhThucDg);
            data.setMapPthucDauGia(mapPhuongThucDg);
            data.setTrangThai(data.getTrangThai());
            data.setTrangThaiHd(data.getTrangThaiHd());
            data.setTrangThaiXh(data.getTrangThaiXh());
        });
        return search;
    }

    @Transactional
    public XhKqBdgHdr create(CustomUserDetails currentUser, XhKqBdgHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!StringUtils.isEmpty(req.getSoQdKq())) {
            Optional<XhKqBdgHdr> optional = xhKqBdgHdrRepository.findBySoQdKq(req.getSoQdKq());
            if (optional.isPresent()) throw new Exception("Số quyết định kết quả " + req.getSoQdKq() + " đã tồn tại");
        }
        XhKqBdgHdr data = new XhKqBdgHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        Optional<XhKqBdgHdr> byMaThongBao = xhKqBdgHdrRepository.findByMaThongBao(req.getMaThongBao());
        if (!ObjectUtils.isEmpty(byMaThongBao)) {
            throw new Exception("Mã thông báo này đã được quyết định kết quả bán đấu giá, xin vui lòng chọn mã thông báo khác");
        }
        XhKqBdgHdr created = xhKqBdgHdrRepository.save(data);
        return created;
    }

    @Transactional
    public XhKqBdgHdr update(CustomUserDetails currentUser, XhKqBdgHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhKqBdgHdr> optional = xhKqBdgHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        Optional<XhKqBdgHdr> soQdKq = xhKqBdgHdrRepository.findBySoQdKq(req.getSoQdKq());
        if (soQdKq.isPresent()) {
            if (!soQdKq.get().getId().equals(req.getId())) {
                throw new Exception("Số quyết định kết quả đã tồn tại");
            }
        }
        XhKqBdgHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiHd", "trangThaiXh");
        XhKqBdgHdr updated = xhKqBdgHdrRepository.save(data);
        return updated;
    }

    public List<XhKqBdgHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhKqBdgHdr> list = xhKqBdgHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhKqBdgHdr> allById = xhKqBdgHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapDmucVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
            data.setListHopDong(xhHopDongHdrRepository.findAllBySoQdKq(data.getSoQdKq()));
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhKqBdgHdr> optional = xhKqBdgHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhKqBdgHdr data = optional.get();
        if (!data.getTrangThai().equals(Contains.DUTHAO)
                && !data.getTrangThai().equals(Contains.TU_CHOI_TP)
                && !data.getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        xhKqBdgHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhKqBdgHdr> list = xhKqBdgHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        for (XhKqBdgHdr hdr : list) {
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                    && !hdr.getTrangThai().equals(Contains.TU_CHOI_TP)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDC)) {
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        xhKqBdgHdrRepository.deleteAll(list);
    }

    public XhKqBdgHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhKqBdgHdr> optional = xhKqBdgHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhKqBdgHdr data = optional.get();
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
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            Optional<XhQdPdKhBdgDtl> dauGiaDtl = xhQdPdKhBdgDtlRepository.findById(data.getIdPdKhDtl());
            if (dauGiaDtl.isPresent()) {
                dauGiaDtl.get().setSoQdPdKqBdg(data.getSoQdKq());
                dauGiaDtl.get().setNgayKyQdPdKqBdg(data.getNgayKy());
                dauGiaDtl.get().setIdQdPdKqBdg(data.getId());
                xhQdPdKhBdgDtlRepository.save(dauGiaDtl.get());
            }
        }
        XhKqBdgHdr created = xhKqBdgHdrRepository.save(data);
        return created;
    }


    public void export(CustomUserDetails currentUser, XhKqBdgHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhKqBdgHdr> page = this.searchPage(currentUser, req);
        List<XhKqBdgHdr> data = page.getContent();
        String title = "Danh sách quyết định phê duyệt kết quả đấu giá";
        String[] rowsName = new String[]{"STT", "Năm Kế hoạch", "Số QĐ PD KQ BĐG", "Ngày ký", "Trích yếu",
                "Ngày tổ chức BĐG", "Số QĐ PD KH BĐG", "Mã thông báo BĐG", "Hình thức đấu giá", "Phướng thức đấu giá",
                "Số TB đấu giá không thành", "Số biên bản đấu giá", "Trạng thái"};
        String fileName = "danh-sach-quyet-dinh-phe-duyet-ket-qua-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhKqBdgHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNam();
            objs[2] = hdr.getSoQdKq();
            objs[3] = hdr.getNgayKy();
            objs[4] = hdr.getTrichYeu();
            objs[5] = hdr.getNgayTao();
            objs[6] = hdr.getSoQdPd();
            objs[7] = hdr.getMaThongBao();
            objs[8] = hdr.getHinhThucDauGia();
            objs[9] = hdr.getPthucDauGia();
            objs[10] = hdr.getSoTbKhongThanh();
            objs[11] = hdr.getSoBienBan();
            objs[12] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public void exportHd(CustomUserDetails currentUser, XhKqBdgHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhKqBdgHdr> page = this.searchPage(currentUser, req);
        List<XhKqBdgHdr> data = page.getContent();
        String title = "QUẢN LÝ KÝ HỢP ĐỒNG BÁN HÀNG DTQG THEO PHƯƠNG THỨC BÁN ĐẤU GIÁ";
        String[] rowsName = new String[]{"STT", "Năm Kế hoạch", "QĐ PD KHBĐG", "QĐ PD KQBĐG", "Tổng sô ĐV tài sản",
                "Số ĐVTS ĐG thành công", "SL HĐ đã ký", "Thời hạn thanh toán", "Chủng loại hành hóa", "ĐV thực hiện",
                "Tổng SL xuất bán", "Thành tiền(đ)", "Trạng thái HĐ", "Trạng thái XH"};
        String fileName = "quan-ly-ky-hop-dong-ban-hang-dtqg-theo-phuong-thuc-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhKqBdgHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNam();
            objs[2] = hdr.getSoQdPd();
            objs[3] = hdr.getSoQdKq();
            objs[4] = hdr.getTongDvts();
            objs[5] = hdr.getSoDvtsDgTc();
            objs[6] = hdr.getSlHdDaKy();
            objs[7] = hdr.getThoiHanTt();
            objs[8] = hdr.getTenDvi();
            objs[10] = hdr.getTongSlXuat();
            objs[11] = hdr.getThanhTien();
            objs[12] = hdr.getTenTrangThaiHd();
            objs[13] = hdr.getTenTrangThaiXh();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            ReportTemplateRequest reportTemplateRequest = new ReportTemplateRequest();
            reportTemplateRequest.setFileName(DataUtils.safeToString(body.get("tenBaoCao")));
            ReportTemplate model = findByTenFile(reportTemplateRequest);
            byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
            List<XhKqBdgHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}

