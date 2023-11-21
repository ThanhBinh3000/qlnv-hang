package com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPlo;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPloRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrServiceImpl;
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
public class XhKqBdgHdrServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhKqBdgHdrRepository xhKqBdgHdrRepository;
    @Autowired
    private XhHopDongHdrRepository xhHopDongHdrRepository;
    @Autowired
    private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;
    @Autowired
    private XhTcTtinBdgHdrServiceImpl xhTcTtinBdgHdrServiceImpl;
    @Autowired
    private XhTcTtinBdgDtlRepository xhTcTtinBdgDtlRepository;
    @Autowired
    private XhTcTtinBdgPloRepository xhTcTtinBdgPloRepository;

    public Page<XhKqBdgHdr> searchPage(CustomUserDetails currentUser, XhKqBdgHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhKqBdgHdr> search = xhKqBdgHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapHinhThucDauGia = getListDanhMucChung("HINH_THUC_DG");
        Map<String, String> mapPhuongThucDauGia = getListDanhMucChung("PHUONG_THUC_DG");
        search = search.map(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapHinhThucDauGia(mapHinhThucDauGia);
            data.setMapPhuongThucDauGia(mapPhuongThucDauGia);
            data.setTrangThai(data.getTrangThai());
            data.setTrangThaiHd(data.getTrangThaiHd());
            data.setTrangThaiXh(data.getTrangThaiXh());
            return data;
        });
        return search;
    }

    @Transactional
    public XhKqBdgHdr create(CustomUserDetails currentUser, XhKqBdgHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoQdKq()) && xhKqBdgHdrRepository.existsBySoQdKq(req.getSoQdKq())) {
            throw new Exception("Số quyết định kết quả " + req.getSoQdKq() + " đã tồn tại");
        }
        XhKqBdgHdr data = new XhKqBdgHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        Optional<XhKqBdgHdr> byMaThongBao = xhKqBdgHdrRepository.findByMaThongBao(req.getMaThongBao());
        byMaThongBao.ifPresent(result -> {
            throw new RuntimeException("Mã thông báo này đã được quyết định kết quả bán đấu giá, xin vui lòng chọn mã thông báo khác");
        });
        return xhKqBdgHdrRepository.save(data);
    }

    @Transactional
    public XhKqBdgHdr update(CustomUserDetails currentUser, XhKqBdgHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhKqBdgHdr> optional = xhKqBdgHdrRepository.findById(req.getId());
        XhKqBdgHdr data = optional.orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhKqBdgHdrRepository.existsBySoQdKqAndIdNot(req.getSoQdKq(), req.getId())) {
            throw new Exception("Số quyết định kết quả " + req.getSoQdKq() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiHd", "trangThaiXh");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        return xhKqBdgHdrRepository.save(data);
    }

    public List<XhKqBdgHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhKqBdgHdr> list = xhKqBdgHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhKqBdgHdr> allById = xhKqBdgHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapDmucVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNhapXuat(mapKieuNhapXuat);
            data.setTrangThai(data.getTrangThai());
            List<XhHopDongHdr> listHd = xhHopDongHdrRepository.findAllByIdQdKq(data.getId());
            listHd.forEach(dataHd -> {
                dataHd.setTrangThai(dataHd.getTrangThai());
                dataHd.setTrangThaiXh(dataHd.getTrangThaiXh());
            });
            data.setListHopDong(listHd);
        });
        return allById;
    }

    public XhKqBdgHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhKqBdgHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhKqBdgHdr> optional = xhKqBdgHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
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
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
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
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        Optional<XhKqBdgHdr> optional = xhKqBdgHdrRepository.findById(Long.valueOf(statusReq.getId()));
        XhKqBdgHdr data = optional.orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        if (statusReq.getTrangThai().equals(Contains.DA_HOAN_THANH)
                && data.getTrangThaiHd().equals(Contains.DANG_THUC_HIEN)) {
            data.setTrangThaiHd(statusReq.getTrangThai());
        } else {
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
                xhQdPdKhBdgDtlRepository.findById(data.getIdQdPdDtl()).ifPresent(dauGiaDtl -> {
                    dauGiaDtl.setIdQdKq(data.getId());
                    dauGiaDtl.setSoQdKq(data.getSoQdKq());
                    dauGiaDtl.setNgayKyQdKq(data.getNgayKy());
                    xhQdPdKhBdgDtlRepository.save(dauGiaDtl);
                });
            }
        }
        return xhKqBdgHdrRepository.save(data);
    }

    public void export(CustomUserDetails currentUser, XhKqBdgHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhKqBdgHdr> page = this.searchPage(currentUser, req);
        List<XhKqBdgHdr> data = page.getContent();
        String title = "Danh sách quyết định phê duyệt kết quả đấu giá";
        String[] rowsName = {"STT", "Năm Kế hoạch", "Số QĐ PD KQ BĐG", "Ngày ký", "Trích yếu",
                "Ngày tổ chức BĐG", "Số QĐ PD KH BĐG", "Mã thông báo BĐG", "Hình thức đấu giá", "Phương thức đấu giá",
                "Số TB đấu giá không thành", "Số biên bản đấu giá", "Trạng thái"};
        String fileName = "danh-sach-quyet-dinh-phe-duyet-ket-qua-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhKqBdgHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNam();
            objs[2] = hdr.getSoQdKq();
            objs[3] = hdr.getNgayKy();
            objs[4] = hdr.getTrichYeu();
            objs[5] = hdr.getNgayTao();
            objs[6] = hdr.getSoQdPd();
            objs[7] = hdr.getMaThongBao();
            objs[8] = hdr.getHinhThucDauGia();
            objs[9] = hdr.getPhuongThucDauGia();
            objs[10] = hdr.getSoTbKhongThanh();
            objs[11] = hdr.getSoBienBan();
            objs[12] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public void exportHd(CustomUserDetails currentUser, XhKqBdgHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhKqBdgHdr> page = this.searchPage(currentUser, req);
        List<XhKqBdgHdr> data = page.getContent();
        String title = "QUẢN LÝ KÝ HỢP ĐỒNG BÁN HÀNG DTQG THEO PHƯƠNG THỨC BÁN ĐẤU GIÁ";
        String[] rowsName = new String[]{"STT", "Năm Kế hoạch", "QĐ PD KHBĐG", "QĐ PD KQBĐG", "Tổng sô ĐV tài sản",
                "Số ĐVTS ĐG thành công", "SL HĐ đã ký", "Thời hạn thanh toán", "Chủng loại hành hóa", "ĐV thực hiện",
                "Tổng SL xuất bán", "Thành tiền(đ)", "Trạng thái HĐ", "Trạng thái XH"};
        String fileName = "quan-ly-ky-hop-dong-ban-hang-dtqg-theo-phuong-thuc-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (int i = 0; i < data.size(); i++) {
            XhKqBdgHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNam();
            objs[2] = hdr.getSoQdPd();
            objs[3] = hdr.getSoQdKq();
            objs[4] = hdr.getTongDviTsan();
            objs[5] = hdr.getSlDviTsanThanhCong();
            objs[6] = hdr.getSlHopDongDaKy();
            objs[7] = hdr.getThoiHanThanhToan();
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

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templatePath = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "bandaugia/" + templatePath;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhKqBdgHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            XhTcTtinBdgHdr thongTin = null;
            if (detail.getIdThongBao() != null) {
                thongTin = xhTcTtinBdgHdrServiceImpl.detail(detail.getIdThongBao());
                if (thongTin != null) {
                    thongTin.setTenDvi(thongTin.getTenDvi().toUpperCase());
                    thongTin.setTenCloaiVthh(thongTin.getTenCloaiVthh().toUpperCase());
                    List<XhTcTtinBdgDtl> thongTinDtl = xhTcTtinBdgDtlRepository.findAllByIdHdr(thongTin.getId());
                    for (XhTcTtinBdgDtl dataThongTinDtl : thongTinDtl) {
                        List<XhTcTtinBdgPlo> thongTinLo = xhTcTtinBdgPloRepository.findAllByIdDtl(dataThongTinDtl.getId());
                        List<XhTcTtinBdgPlo> filteredLo = thongTinLo.stream().filter(type -> type.getToChucCaNhan() != null).collect(Collectors.toList());
                        dataThongTinDtl.setChildren(filteredLo);
                    }
                    List<XhTcTtinBdgDtl> filteredThongTinDtl = thongTinDtl.stream().filter(type -> type.getChildren() != null && !type.getChildren().isEmpty()).collect(Collectors.toList());
                    thongTin.setChildren(filteredThongTinDtl);
                }
            }
            if (thongTin != null) {
                return docxToPdfConverter.convertDocxToPdf(inputStream, thongTin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}