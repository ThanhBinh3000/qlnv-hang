package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.SearchXhTcTtinBttReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class XhTcTtinBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;
    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;
    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private XhQdDchinhKhBttHdrRepository xhQdDchinhKhBttHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhQdPdKhBttDtl> searchPage(CustomUserDetails currentUser, SearchXhTcTtinBttReq req) throws Exception {
        String dvql = currentUser.getDvql();
        Integer lastest = 1;
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setLastest(lastest);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
            req.setLastest(lastest);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setLastest(lastest);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdPdKhBttDtl> search = xhQdPdKhBttDtlRepository.searchPage(req, pageable);
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, XhQdPdKhBttDtl> recordMap = new HashMap<>();
        search.getContent().forEach(data -> {
            if (recordMap.containsKey(data.getSoDxuat())) {
                XhQdPdKhBttDtl existingRecord = recordMap.get(data.getSoDxuat());
                if (data.getIsDieuChinh() && data.getLanDieuChinh() > existingRecord.getLanDieuChinh()) {
                    recordMap.put(data.getSoDxuat(), data);
                } else if (!existingRecord.getIsDieuChinh()) {
                    recordMap.put(data.getSoDxuat(), data);
                }
            } else {
                recordMap.put(data.getSoDxuat(), data);
            }
        });
        List<XhQdPdKhBttDtl> filteredRecords = new ArrayList<>(recordMap.values());
        filteredRecords.forEach(data -> {
            try {
                mapDmucDvi.computeIfPresent(data.getMaDvi(), (key, objDonVi) -> {
                    data.setTenDvi(objDonVi.get("tenDvi").toString());
                    return objDonVi;
                });
                mapDmucVthh.computeIfPresent(data.getLoaiVthh(), (key, value) -> {
                    data.setTenLoaiVthh(value);
                    return value;
                });
                mapDmucVthh.computeIfPresent(data.getCloaiVthh(), (key, value) -> {
                    data.setTenCloaiVthh(value);
                    return value;
                });
                if (data.getTrangThai() != null) {
                    data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
                }
                if (data.getTrangThaiHd() != null) {
                    data.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiHd()));
                }
                if (data.getTrangThaiXh() != null) {
                    data.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiXh()));
                }
                if (data.getSoQdDc() != null) {
                    data.setXhQdDchinhKhBttHdr(xhQdDchinhKhBttHdrRepository.findById(data.getIdHdr()).orElse(null));
                } else {
                    data.setXhQdPdKhBttHdr(xhQdPdKhBttHdrRepository.findById(data.getIdHdr()).orElse(null));
                }
                List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdDtl(data.getId());
                data.setChildren(listDvi != null && !listDvi.isEmpty() ? listDvi : Collections.emptyList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return new PageImpl<>(filteredRecords, pageable, search.getTotalElements());
    }

    @Transactional
    public XhQdPdKhBttDtl create(CustomUserDetails currentUser, XhQdPdKhBttDtlReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBttDtl data = xhQdPdKhBttDtlRepository.findById(req.getIdDtl())
                .orElseThrow(() -> new Exception("Bản Ghi không tồn tại"));
        data.setPthucBanTrucTiep(req.getPthucBanTrucTiep());
        data.setDiaDiemChaoGia(req.getDiaDiemChaoGia());
        data.setNgayMkho(req.getNgayMkho());
        data.setNgayKthuc(req.getNgayKthuc());
        data.setGhiChuChaoGia(req.getGhiChuChaoGia());
        data.setThoiHanBan(req.getThoiHanBan());
        data.setTongGiaTriHdong(req.getTongGiaTriHdong());
        data.setTrangThai(Contains.DANG_THUC_HIEN);
        if (req.getPthucBanTrucTiep().equals(Contains.UY_QUYEN) && !DataUtils.isNullOrEmpty(req.getFileUyQuyen())) {
            List<FileDinhKem> fileUyQuyen = fileDinhKemService.saveListFileDinhKem(req.getFileUyQuyen(), data.getId(), XhQdPdKhBttDtl.TABLE_NAME + "_UY_QUYEN");
            data.setFileUyQuyen(fileUyQuyen);
        }
        if (req.getPthucBanTrucTiep().equals(Contains.BAN_LE) && !DataUtils.isNullOrEmpty(req.getFileBanLe())) {
            List<FileDinhKem> fileBanLe = fileDinhKemService.saveListFileDinhKem(req.getFileBanLe(), data.getId(), XhQdPdKhBttDtl.TABLE_NAME + "_BAN_LE");
            data.setFileBanLe(fileBanLe);
        }
        if (data.getPthucBanTrucTiep().equals(Contains.CHAO_GIA)) {
            this.processChaoGiaData(req, data);
        }
        XhQdPdKhBttDtl created = xhQdPdKhBttDtlRepository.save(data);
        return created;
    }

    private void processChaoGiaData(XhQdPdKhBttDtlReq req, XhQdPdKhBttDtl data) {
        for (XhQdPdKhBttDviReq dviReq : req.getChildren()) {
            for (XhQdPdKhBttDviDtlReq dviDtlReq : dviReq.getChildren()) {
                xhTcTtinBttRepository.deleteAllByIdDviDtl(dviDtlReq.getId());
                for (XhTcTtinBttReq chaoGiaReq : dviDtlReq.getChildren()) {
                    String type = "tcTt";
                    XhTcTtinBtt chaoGia = new XhTcTtinBtt();
                    BeanUtils.copyProperties(chaoGiaReq, chaoGia, "id");
                    chaoGia.setId(null);
                    chaoGia.setIdQdPdDtl(data.getId());
                    chaoGia.setType(type);
                    XhTcTtinBtt create = xhTcTtinBttRepository.save(chaoGia);
                    fileDinhKemService.delete(create.getId(), Collections.singleton(XhTcTtinBtt.TABLE_NAME));
                    if (!DataUtils.isNullObject(chaoGiaReq.getFileDinhKems())) {
                        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(chaoGiaReq.getFileDinhKems()), create.getId(), XhTcTtinBtt.TABLE_NAME);
                        chaoGia.setFileDinhKems(fileDinhKems.get(0));
                    }
                }
            }
        }
    }

    public List<XhQdPdKhBttDtl> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBttDtl> list = xhQdPdKhBttDtlRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        list.forEach(data -> {
            data.setTenDvi(mapDmucDvi.getOrDefault(data.getMaDvi(), null));
            data.setTenLoaiHinhNx(mapLoaiHinhNx.getOrDefault(data.getLoaiHinhNx(), null));
            data.setTenKieuNx(mapKieuNx.getOrDefault(data.getKieuNx(), null));
            data.setTenLoaiVthh(mapDmucVthh.getOrDefault(data.getLoaiVthh(), null));
            data.setTenCloaiVthh(mapDmucVthh.getOrDefault(data.getCloaiVthh(), null));
            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
            data.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiHd()));
            data.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiXh()));
            if (data.getSoQdDc() != null) {
                data.setXhQdDchinhKhBttHdr(xhQdDchinhKhBttHdrRepository.findById(data.getIdHdr()).orElse(null));
            } else {
                data.setXhQdPdKhBttHdr(xhQdPdKhBttHdrRepository.findById(data.getIdHdr()).orElse(null));
            }
            data.setListHopDongBtt(xhHopDongBttHdrRepository.findAllByIdChaoGia(data.getId()));
            if (!DataUtils.isNullObject(data.getPthucBanTrucTiep())) {
                List<FileDinhKem> fileDinhKems = new ArrayList<>();
                if (data.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)) {
                    fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME + "_UY_QUYEN"));
                    data.setFileUyQuyen(fileDinhKems);
                } else if (data.getPthucBanTrucTiep().equals(Contains.BAN_LE)) {
                    fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME + "_BAN_LE"));
                    data.setFileBanLe(fileDinhKems);
                }
            }
            List<XhQdPdKhBttDvi> dvi = xhQdPdKhBttDviRepository.findAllByIdDtl(data.getId());
            dvi.forEach(dataDvi -> {
                List<XhQdPdKhBttDviDtl> dviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                dviDtl.forEach(dataDviDtl -> {
                    List<XhTcTtinBtt> toChuc = xhTcTtinBttRepository.findAllByIdDviDtl(dataDviDtl.getId());
                    toChuc.forEach(dataToChuc -> {
                        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(dataToChuc.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
                        if (!DataUtils.isNullOrEmpty(fileDinhKems)) {
                            dataToChuc.setFileDinhKems(fileDinhKems.get(0));
                        }
                    });
                    dataDviDtl.setTenDiemKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaDiemKho(), null));
                    dataDviDtl.setTenNhaKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaNhaKho(), null));
                    dataDviDtl.setTenNganKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaNganKho(), null));
                    dataDviDtl.setTenLoKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaLoKho(), null));
                    dataDviDtl.setTenLoaiVthh(mapDmucVthh.getOrDefault(dataDviDtl.getLoaiVthh(), null));
                    dataDviDtl.setTenCloaiVthh(mapDmucVthh.getOrDefault(dataDviDtl.getCloaiVthh(), null));
                    dataDviDtl.setChildren(toChuc.stream().filter(type -> "tcTt".equals(type.getType())).collect(Collectors.toList()));
                });
                dataDvi.setTenDvi(mapDmucDvi.getOrDefault(dataDvi.getMaDvi(), null));
                if (data.getSoQdDc() != null) {
                    dataDvi.setChildren(dviDtl.stream().filter(type -> "QdDc".equals(type.getType())).collect(Collectors.toList()));
                } else {
                    dataDvi.setChildren(dviDtl.stream().filter(type -> "QdKh".equals(type.getType())).collect(Collectors.toList()));
                }
            });
            if (data.getSoQdDc() != null) {
                data.setChildren(dvi.stream().filter(type -> "QdDc".equals(type.getType())).collect(Collectors.toList()));
            } else {
                data.setChildren(dvi.stream().filter(type -> "QdKh".equals(type.getType())).collect(Collectors.toList()));
            }
        });
        return list;
    }

    public XhQdPdKhBttDtl detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBttDtl> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    public XhQdPdKhBttDtl approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        Long recordId = Long.valueOf(statusReq.getId());
        Optional<XhQdPdKhBttDtl> optional = xhQdPdKhBttDtlRepository.findById(recordId);
        XhQdPdKhBttDtl data = optional.orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        if (statusReq.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId())
                && data.getTrangThaiHd().equals(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId())) {
            data.setTrangThaiHd(statusReq.getTrangThai());
        } else {
            switch (status) {
                case Contains.DA_HOAN_THANH + Contains.DANG_THUC_HIEN:
                    data.setNgayNhanCgia(LocalDate.now());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            data.setTrangThai(statusReq.getTrangThai());
        }
        return xhQdPdKhBttDtlRepository.save(data);
    }

    public void export(CustomUserDetails currentUser, SearchXhTcTtinBttReq req, HttpServletResponse response) throws Exception {
        String capDvi = (currentUser.getUser().getCapDvi());
        if (Contains.CAP_CHI_CUC.equals(capDvi)) {
            exportChiCuc(currentUser, req, response);
            return;
        }
        exportCuc(currentUser, req, response);

    }

    void exportCuc(CustomUserDetails currentUser, SearchXhTcTtinBttReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBttDtl> page = this.searchPage(currentUser, req);
        List<XhQdPdKhBttDtl> data = page.getContent();
        String title = "Danh sách thông tin triển khai kế hoạch bán trực tiếp";
        String[] rowsName = {"STT", "Số QĐ phê duyệt KH bán trực tiếp",
                "Số đề xuất KH bán trực tiếp", "Phương thức bán trực tiếp", "Ngày nhận chào giá/Ngày ủy quyền",
                "Số QĐ PD KQ chào giá", "Loại hàng hóa", "Chủng loại hàng hóa", "Trạng thái"};
        String fileName = "Danh-sach-thong-tin-trien-khai-ke-hoach-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttDtl dtl = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dtl.getSoQdPd();
            objs[2] = dtl.getSoDxuat();
            objs[3] = dtl.getPthucBanTrucTiep();
            objs[4] = dtl.getNgayNhanCgia();
            objs[5] = dtl.getSoQdKq();
            objs[6] = dtl.getTenLoaiVthh();
            objs[7] = dtl.getTenCloaiVthh();
            objs[8] = dtl.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    void exportChiCuc(CustomUserDetails currentUser, SearchXhTcTtinBttReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBttDtl> page = this.searchPage(currentUser, req);
        List<XhQdPdKhBttDtl> data = page.getContent();
        String title = "Danh sách quyết định phê duyệt bán trực tiếp ủy quyền/bán lẻ";
        String[] rowsName = {"STT", "Số quyết định",
                "Số kế hoạch", "Năm kế hoạch", "Ngày duyệt",
                "Ngày ủy quyền", "Trích yếu", "Loại hàng hóa", "Chủng loại hàng hóa", "Số lượng", "Phương thức bán trực tiếp", "Trạng thái"};
        String fileName = "Danh-sach-quyet-dinh-phe-duyet-bán-truc-tiep-uy-quyen-ban-le.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttDtl dtl = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dtl.getSoQdPd();
            objs[2] = dtl.getSoDxuat();
            objs[3] = dtl.getNamKh();
            objs[4] = dtl.getNgayPduyet();
            objs[5] = dtl.getNgayNhanCgia();
            objs[6] = dtl.getTrichYeu();
            objs[7] = dtl.getTenLoaiVthh();
            objs[8] = dtl.getTenCloaiVthh();
            objs[9] = dtl.getTongSoLuong();
            objs[10] = dtl.getPthucBanTrucTiep();
            objs[11] = dtl.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public void exportHd(CustomUserDetails currentUser, SearchXhTcTtinBttReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBttDtl> page = this.searchPage(currentUser, req);
        List<XhQdPdKhBttDtl> data = page.getContent();
        String title = "Danh sách hợp đồng bán trực tiếp";
        String[] rowsName = {"STT", "Năm KH",
                "QĐ PD KHBTT", "SL HĐ cần ký", "SL HĐ đã ký",
                "Thời hạn xuất kho", "Loại hàng hóa", "Chủng loại hàng hóa", "Tổng giá trị hợp đồng",
                "Trạng thái hợp đồng", "Trạng thái xuất hàng"};
        String fileName = "Danh-sach-hop-dong-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttDtl dtl = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dtl.getNamKh();
            objs[2] = dtl.getSoQdPd();
            objs[3] = dtl.getSlHdChuaKy();
            objs[4] = dtl.getSlHdDaKy();
            objs[5] = dtl.getNgayMkho();
            objs[6] = dtl.getTenLoaiVthh();
            objs[7] = dtl.getTenCloaiVthh();
            objs[8] = dtl.getTongGiaTriHdong();
            objs[9] = dtl.getTenTrangThaiHd();
            objs[10] = dtl.getTenTrangThaiXh();
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
            XhQdPdKhBttDtl detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}