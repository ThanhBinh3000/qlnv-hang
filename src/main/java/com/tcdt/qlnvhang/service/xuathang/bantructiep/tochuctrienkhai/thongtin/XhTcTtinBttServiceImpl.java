package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttRepository;
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
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;
    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;
    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhQdPdKhBttDtl> searchPage(CustomUserDetails currentUser, SearchXhTcTtinBttReq req) throws Exception {
        String dvql = currentUser.getDvql();
        Integer lastest = 1;
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setLastest(lastest);
            req.setTrangThaiHdr(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
            req.setLastest(lastest);
            req.setTrangThaiHdr(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setLastest(lastest);
            req.setTrangThaiHdr(Contains.BAN_HANH);
            req.setMaChiCuc(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdPdKhBttDtl> search = xhQdPdKhBttDtlRepository.searchPage(req, pageable);
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        search.getContent().forEach(data -> {
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
                data.setXhQdPdKhBttHdr(xhQdPdKhBttHdrRepository.findById(data.getIdHdr()).orElse(null));
                List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdDtl(data.getId());
                data.setChildren(listDvi != null && !listDvi.isEmpty() ? listDvi : Collections.emptyList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
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
        data.setGhiChuChaoGia(req.getGhiChuChaoGia());
        data.setTrangThai(Contains.DANG_THUC_HIEN);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            if (req.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)) {
                List<FileDinhKem> fileUyQuyen = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), data.getId(), XhQdPdKhBttDtl.TABLE_NAME + "_UY_QUYEN");
                data.setFileDinhKem(fileUyQuyen);
            } else if (req.getPthucBanTrucTiep().equals(Contains.BAN_LE)) {
                List<FileDinhKem> fileBanLe = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), data.getId(), XhQdPdKhBttDtl.TABLE_NAME + "_BAN_LE");
                data.setFileDinhKem(fileBanLe);
            }
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
                    XhTcTtinBtt chaoGia = new XhTcTtinBtt();
                    BeanUtils.copyProperties(chaoGiaReq, chaoGia, "id");
                    chaoGia.setId(null);
                    chaoGia.setIdQdPdDtl(data.getId());
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
            data.setXhQdPdKhBttHdr(xhQdPdKhBttHdrRepository.findById(data.getIdHdr()).orElse(null));
            data.setListHopDongBtt(xhHopDongBttHdrRepository.findAllByIdChaoGia(data.getId()));
            if (!DataUtils.isNullObject(data.getPthucBanTrucTiep())) {
                List<FileDinhKem> fileDinhKems = new ArrayList<>();
                if (data.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)) {
                    fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME + "_UY_QUYEN"));
                    data.setFileDinhKem(fileDinhKems);
                } else if (data.getPthucBanTrucTiep().equals(Contains.BAN_LE)) {
                    fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME + "_BAN_LE"));
                    data.setFileDinhKem(fileDinhKems);
                }
            }
            List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdDtl(data.getId());
            listDvi.forEach(dataDvi -> {
                List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                listDviDtl.forEach(dataDviDtl -> {
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
                    dataDviDtl.setChildren(toChuc);
                });
                dataDvi.setTenDvi(mapDmucDvi.getOrDefault(dataDvi.getMaDvi(), null));
                dataDvi.setChildren(listDviDtl);
            });
            data.setChildren(listDvi.stream().filter(item -> !item.getIsKetQua()).collect(Collectors.toList()));
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
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdPdKhBttDtl> page = this.searchPage(currentUser, req);
        List<XhQdPdKhBttDtl> data = page.getContent();
        boolean isVattuType = data.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        boolean isCapDvi = Contains.CAP_CHI_CUC.equals(currentUser.getUser().getCapDvi());
        if (isCapDvi) {
            this.exportChiCuc(response, data, isVattuType);
        } else {
            this.exportCuc(response, data, isVattuType);
        }
    }

    public void exportCuc(HttpServletResponse response, List<XhQdPdKhBttDtl> data, boolean isVattuType) throws Exception {
        String title = "Danh sách thông tin triển khai kế hoạch bán trực tiếp hàng DTQG";
        String[] rowsName;
        String[] commonRowsName = new String[]{"STT", "Số QĐ phê duyệt KH bán trực tiếp", "Số QĐ điều chỉnh KH bán trực tiếp", "Số đề xuất KH bán trực tiếp", "Phương thức bán trực tiếp", "Ngày nhận chào giá/Ngày ủy quyền", "Số QĐ PD KQ chào giá"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 3);
            vattuRowsName[7] = "Loại hàng DTQG";
            vattuRowsName[8] = "Chủng loại hàng DTQG";
            vattuRowsName[9] = "Trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 2);
            nonVattuRowsName[7] = "Chủng loại hàng DTQG";
            nonVattuRowsName[8] = "Trạng thái";
            rowsName = nonVattuRowsName;
        }
        Map<String, String> pthucBanTrucTiepMap = new HashMap<>();
        pthucBanTrucTiepMap.put(Contains.CHAO_GIA, "Chào giá");
        pthucBanTrucTiepMap.put(Contains.UY_QUYEN, "Ủy quyền");
        pthucBanTrucTiepMap.put(Contains.BAN_LE, "Bán lẻ");
        String fileName = "danh-sach-thong-tin-trien-khai-ke-hoach-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttDtl hdr = data.get(i);
            String pthucBanTrucTiepValue = pthucBanTrucTiepMap.get(hdr.getPthucBanTrucTiep());
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdPd();
            objs[2] = hdr.getSoQdDc();
            objs[3] = hdr.getSoDxuat();
            objs[4] = pthucBanTrucTiepValue;
            objs[5] = hdr.getNgayNhanCgia();
            objs[6] = hdr.getSoQdKq();
            if (isVattuType) {
                objs[7] = hdr.getTenLoaiVthh();
                objs[8] = hdr.getTenCloaiVthh();
                objs[9] = hdr.getTenTrangThai();
            } else {
                objs[7] = hdr.getTenCloaiVthh();
                objs[8] = hdr.getTenTrangThai();
            }
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public void exportChiCuc(HttpServletResponse response, List<XhQdPdKhBttDtl> data, boolean isVattuType) throws Exception {
        String title = "Danh sách quyết định phê duyệt kế hoạch bán trực tiếp được ủy quyền/bán lẻ hàng DTQG";
        String[] rowsName;
        String[] commonRowsName = new String[]{"STT", "Số quyết định", "Số kế hoạch", "Năm kế hoạch", "Ngày duyệt", "Ngày ủy quyền", "Trích yếu"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 5);
            vattuRowsName[7] = "Loại hàng DTQG";
            vattuRowsName[8] = "Chủng loại hàng DTQG";
            vattuRowsName[9] = "Số lượng";
            vattuRowsName[10] = "Phương thức bán trực tiếp";
            vattuRowsName[11] = "Trạng thái";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 4);
            nonVattuRowsName[7] = "Chủng loại hàng DTQG";
            nonVattuRowsName[8] = "Số lượng (kg)";
            nonVattuRowsName[9] = "Phương thức bán trực tiếp";
            nonVattuRowsName[10] = "Trạng thái";
            rowsName = nonVattuRowsName;
        }
        Map<String, String> pthucBanTrucTiepMap = new HashMap<>();
        pthucBanTrucTiepMap.put(Contains.UY_QUYEN, "Ủy quyền");
        pthucBanTrucTiepMap.put(Contains.BAN_LE, "Bán lẻ");
        String fileName = "danh-sach-quyet-dinh-phe-duyet-ke-hoach-ban-truc-tiep-duoc-uy-quyen/ban-le-hang-DTQG.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttDtl hdr = data.get(i);
            String pthucBanTrucTiepValue = pthucBanTrucTiepMap.get(hdr.getPthucBanTrucTiep());
            XhQdPdKhBttHdr xhQdPdKhBttHdr = hdr.getXhQdPdKhBttHdr();
            String tenTrangThai = null;
            String trichYeu = null;
            LocalDate ngayPheDuyet = null;
            if (xhQdPdKhBttHdr != null) {
                tenTrangThai = NhapXuatHangTrangThaiEnum.getTenById(xhQdPdKhBttHdr.getTrangThai());
                trichYeu = xhQdPdKhBttHdr.getTrichYeu();
                ngayPheDuyet = xhQdPdKhBttHdr.getNgayPduyet();
            }
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdPd();
            objs[2] = hdr.getSoDxuat();
            objs[3] = hdr.getNamKh();
            objs[4] = ngayPheDuyet;
            objs[5] = hdr.getNgayNhanCgia();
            objs[6] = trichYeu;
            if (isVattuType) {
                objs[7] = hdr.getTenLoaiVthh();
                objs[8] = hdr.getTenCloaiVthh();
                objs[9] = hdr.getTongSoLuong();
                objs[10] = pthucBanTrucTiepValue;
                objs[11] = tenTrangThai;
            } else {
                objs[7] = hdr.getTenCloaiVthh();
                objs[8] = hdr.getTongSoLuong();
                objs[9] = pthucBanTrucTiepValue;
                objs[10] = tenTrangThai;
            }
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public void exportHd(CustomUserDetails currentUser, SearchXhTcTtinBttReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdPdKhBttDtl> page = this.searchPage(currentUser, req);
        List<XhQdPdKhBttDtl> data = page.getContent();
        String title = "Danh sách hợp đồng bán trực tiếp hàng DTQG";
        String[] rowsName;
        boolean isVattuType = data.stream().anyMatch(item -> item.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU));
        String[] commonRowsName = new String[]{"STT", "Năm KH", "QĐ PD KHBTT", "SL HĐ cần ký", "SL HĐ đã ký", "Thời hạn xuất kho"};
        if (isVattuType) {
            String[] vattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 5);
            vattuRowsName[6] = "Loại hàng DTQG";
            vattuRowsName[7] = "Chủng loại hàng DTQG";
            vattuRowsName[8] = "Tổng giá trị hợp đồng";
            vattuRowsName[9] = "Trạng thái ký HĐ";
            vattuRowsName[10] = "trạng thái XH";
            rowsName = vattuRowsName;
        } else {
            String[] nonVattuRowsName = Arrays.copyOf(commonRowsName, commonRowsName.length + 4);
            nonVattuRowsName[6] = "Chủng loại hàng DTQG";
            nonVattuRowsName[7] = "Tổng giá trị hợp đồng";
            nonVattuRowsName[8] = "Trạng thái ký HĐ";
            nonVattuRowsName[9] = "trạng thái XH";
            rowsName = nonVattuRowsName;
        }
        String fileName = "danh-sach-hop-dong-ban-truc-tiep-hang-DTQG.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttDtl hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoQdPd();
            objs[3] = hdr.getSlHdChuaKy();
            objs[4] = hdr.getSlHdDaKy();
            objs[5] = hdr.getTgianDkienDen();
            if (isVattuType) {
                objs[6] = hdr.getTenLoaiVthh();
                objs[7] = hdr.getTenCloaiVthh();
                objs[8] = hdr.getThanhTienDuocDuyet();
                objs[9] = hdr.getTenTrangThaiHd();
                objs[10] = hdr.getTenTrangThaiXh();
            } else {
                objs[6] = hdr.getTenCloaiVthh();
                objs[7] = hdr.getThanhTienDuocDuyet();
                objs[8] = hdr.getTenTrangThaiHd();
                objs[9] = hdr.getTenTrangThaiXh();
            }
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}