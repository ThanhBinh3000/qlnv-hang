package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class XhKqBttHdrServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhKqBttHdrRepository xhKqBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;
    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;
    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;


    public Page<XhKqBttHdr> searchPage(CustomUserDetails currentUser, XhKqBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setTrangThai(Contains.DA_DUYET_LDC);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhKqBttHdr> search = xhKqBttHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
            data.setTrangThaiHd(data.getTrangThaiHd());
            data.setTrangThaiXh(data.getTrangThaiXh());
        });
        return search;
    }

    @Transactional
    public XhKqBttHdr create(CustomUserDetails currentUser, XhKqBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoQdKq()) && xhKqBttHdrRepository.existsBySoQdKq(req.getSoQdKq())) {
            throw new Exception("Số quyết định kết quả " + req.getSoQdKq() + " đã tồn tại");
        }
        XhKqBttHdr data = new XhKqBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        XhKqBttHdr created = xhKqBttHdrRepository.save(data);
        this.saveDetail(req, created.getId(), false);
        return created;
    }

    void saveDetail(XhKqBttHdrReq req, Long idHdr, Boolean isCheckRequired) {
        if (isCheckRequired) {
            xhQdPdKhBttDviRepository.deleteAllByIdQdKqHdr(idHdr);
        }
        for (XhQdPdKhBttDviReq dviReq : req.getChildren().stream().filter(item -> item.getIsKetQua()).collect(Collectors.toList())) {
            XhQdPdKhBttDvi dvi = new XhQdPdKhBttDvi();
            BeanUtils.copyProperties(dviReq, dvi, "id");
            dvi.setId(null);
            dvi.setIdQdKqHdr(idHdr);
            dvi.setIsKetQua(true);
            XhQdPdKhBttDvi saveDvi = xhQdPdKhBttDviRepository.save(dvi);
            if (isCheckRequired) {
                xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dviReq.getId());
            }
            for (XhQdPdKhBttDviDtlReq dviDtlReq : dviReq.getChildren()) {
                XhQdPdKhBttDviDtl dviDtl = new XhQdPdKhBttDviDtl();
                BeanUtils.copyProperties(dviDtlReq, dviDtl, "id");
                dviDtl.setId(null);
                dviDtl.setIdDvi(saveDvi.getId());
                XhQdPdKhBttDviDtl saveDviDtl = xhQdPdKhBttDviDtlRepository.save(dviDtl);
                if (isCheckRequired) {
                    xhTcTtinBttRepository.deleteAllByIdDviDtl(dviDtlReq.getId());
                }
                for (XhTcTtinBttReq tTinReq : dviDtlReq.getChildren()) {
                    XhTcTtinBtt tTtin = new XhTcTtinBtt();
                    BeanUtils.copyProperties(tTinReq, tTtin, "id");
                    tTtin.setId(null);
                    tTtin.setIdDviDtl(saveDviDtl.getId());
                    XhTcTtinBtt saveTtin = xhTcTtinBttRepository.save(tTtin);
                    if (!DataUtils.isNullObject(tTinReq.getFileDinhKems())) {
                        tTinReq.getFileDinhKems().setId(null);
                        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(
                                Collections.singletonList(tTinReq.getFileDinhKems()), saveTtin.getId(), XhTcTtinBtt.TABLE_NAME);
                        saveTtin.setFileDinhKems(fileDinhKem.get(0));
                    }
                }
            }
        }
    }

    @Transactional
    public XhKqBttHdr update(CustomUserDetails currentUser, XhKqBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(req.getId());
        XhKqBttHdr data = optional.orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (!StringUtils.isEmpty(req.getSoQdKq()) && xhKqBttHdrRepository.existsBySoQdKqAndIdNot(req.getSoQdKq(), req.getId())) {
            throw new Exception("Số quyết định kết quả " + req.getSoQdKq() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiHd", "trangThaiXh");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhKqBttHdr updated = xhKqBttHdrRepository.save(data);
        this.saveDetail(req, updated.getId(), true);
        return updated;
    }

    public List<XhKqBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhKqBttHdr> allById = xhKqBttHdrRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(allById)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhKqBttHdr data : allById) {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapDmucVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
            List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdQdKqHdr(data.getId());
            for (XhQdPdKhBttDvi dataDvi : listDvi) {
                dataDvi.setTenDvi(StringUtils.isEmpty(dataDvi.getMaDvi()) ? null : mapDmucDvi.get(dataDvi.getMaDvi()));
                List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                for (XhQdPdKhBttDviDtl dviDtl : listDviDtl) {
                    dviDtl.setTenDiemKho(StringUtils.isEmpty(dviDtl.getMaDiemKho()) ? null : mapDmucDvi.get(dviDtl.getMaDiemKho()));
                    dviDtl.setTenNhaKho(StringUtils.isEmpty(dviDtl.getMaNhaKho()) ? null : mapDmucDvi.get(dviDtl.getMaNhaKho()));
                    dviDtl.setTenNganKho(StringUtils.isEmpty(dviDtl.getMaNganKho()) ? null : mapDmucDvi.get(dviDtl.getMaNganKho()));
                    dviDtl.setTenLoKho(StringUtils.isEmpty(dviDtl.getMaLoKho()) ? null : mapDmucDvi.get(dviDtl.getMaLoKho()));
                    dviDtl.setTenLoaiVthh(StringUtils.isEmpty(dviDtl.getLoaiVthh()) ? null : mapDmucVthh.get(dviDtl.getLoaiVthh()));
                    dviDtl.setTenCloaiVthh(StringUtils.isEmpty(dviDtl.getCloaiVthh()) ? null : mapDmucVthh.get(dviDtl.getCloaiVthh()));
                    dataDvi.setDonGiaDuocDuyet(dviDtl.getDonGiaDuocDuyet());
                    List<XhTcTtinBtt> listTtin = xhTcTtinBttRepository.findAllByIdDviDtl(dviDtl.getId());
                    listTtin.forEach(dataTtin -> {
                        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(dataTtin.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
                        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
                            dataTtin.setFileDinhKems(fileDinhKem.get(0));
                        }
                    });
                    dviDtl.setChildren(listTtin);
                }
                dataDvi.setChildren(listDviDtl);
            }
            data.setChildren(listDvi.stream().filter(item -> item.getIsKetQua()).collect(Collectors.toList()));
            List<XhHopDongBttHdr> listHd = xhHopDongBttHdrRepository.findAllByIdQdKq(data.getId());
            listHd.forEach(dataHd -> {
                dataHd.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(dataHd.getTrangThai()));
                dataHd.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dataHd.getTrangThaiXh()));
            });
            data.setListHopDongBtt(listHd);
        }
        return allById;
    }

    public XhKqBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhKqBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(idSearchReq.getId());
        XhKqBttHdr data = optional.orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        String trangThai = data.getTrangThai();
        if (!trangThai.equals(Contains.DUTHAO)
                && !trangThai.equals(Contains.TU_CHOI_TP)
                && !trangThai.equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdPdKhBttDvi> dviList = xhQdPdKhBttDviRepository.findAllByIdQdKqHdr(data.getId());
        dviList.forEach(dvi -> {
            List<XhQdPdKhBttDviDtl> dviDtlList = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dvi.getId());
            dviDtlList.forEach(dviDtl -> {
                xhTcTtinBttRepository.deleteAllByIdDviDtl(dviDtl.getId());
                fileDinhKemService.delete(data.getId(), Collections.singleton(XhTcTtinBtt.TABLE_NAME));
            });
            xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dvi.getId());
        });
        xhQdPdKhBttDviRepository.deleteAllByIdQdKqHdr(data.getId());
        xhKqBttHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhKqBttHdr> list = xhKqBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        for (XhKqBttHdr hdr : list) {
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                    && !hdr.getTrangThai().equals(Contains.TU_CHOI_TP)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDC)) {
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> idHdr = list.stream().map(XhKqBttHdr::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findByIdQdKqHdrIn(idHdr);
        List<Long> idDvi = listDvi.stream().map(XhQdPdKhBttDvi::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findByIdDviIn(idDvi);
        List<Long> idDviDtl = listDviDtl.stream().map(XhQdPdKhBttDviDtl::getId).collect(Collectors.toList());
        List<XhTcTtinBtt> listTtin = xhTcTtinBttRepository.findByIdDviDtlIn(idDviDtl);
        List<Long> idTtin = listTtin.stream().map(XhTcTtinBtt::getId).collect(Collectors.toList());
        xhTcTtinBttRepository.deleteAll(listTtin);
        fileDinhKemService.deleteMultiple(idTtin, Collections.singleton(XhTcTtinBtt.TABLE_NAME));
        xhQdPdKhBttDviDtlRepository.deleteAll(listDviDtl);
        xhQdPdKhBttDviRepository.deleteAll(listDvi);
        xhKqBttHdrRepository.deleteAll(list);
    }

    public XhKqBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(Long.valueOf(statusReq.getId()));
        XhKqBttHdr data = optional.orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
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
                case Contains.DA_DUYET_LDC + Contains.CHODUYET_LDC:
                    data.setNguoiPduyetId(currentUser.getUser().getId());
                    data.setNgayPduyet(LocalDate.now());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            data.setTrangThai(statusReq.getTrangThai());
            if (statusReq.getTrangThai().equals(Contains.DA_DUYET_LDC)) {
                xhQdPdKhBttDtlRepository.findById(data.getIdChaoGia()).ifPresent(dtl -> {
                    dtl.setIdQdKq(data.getId());
                    dtl.setSoQdKq(data.getSoQdKq());
                    xhQdPdKhBttDtlRepository.save(dtl);
                });
            }
        }
        XhKqBttHdr created = xhKqBttHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhKqBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhKqBttHdr> page = this.searchPage(currentUser, req);
        List<XhKqBttHdr> data = page.getContent();
        String title = "Danh sách quyết định phê duyệt kết quả chào giá";
        String[] rowsName = {"STT", "Số QĐ PDKQ chào giá", "Ngày ký QĐ",
                "Đơn vị", "Số QĐ PDKH bán trực tiếp", "Loại hàng hóa",
                "Chủng loại hành hóa", "Trạng thái"};
        String fileName = "danh-sach-dx-pd-kq-chao-gia.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhKqBttHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = hdr.getSoQdKq();
            objs[2] = hdr.getNgayKy();
            objs[3] = hdr.getTenDvi();
            objs[4] = hdr.getSoQdPd();
            objs[5] = hdr.getTenLoaiVthh();
            objs[6] = hdr.getTenCloaiVthh();
            objs[7] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public void exportHdong(CustomUserDetails currentUser, XhKqBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhKqBttHdr> page = this.searchPage(currentUser, req);
        List<XhKqBttHdr> data = page.getContent();
        String title = "Quản lý ký hợp đồng bán hàng DTQG theo phương thức bán trực tiếp";
        String[] rowsName = {"STT", "Năm KH", "QĐ PD KHBTT",
                "QĐ PD KQ chào giá", "Sl HĐ cần ký", "Sl HĐ đã ký",
                "Thời hạn xuất kho", "Loại hàng hóa", "Chủng loại hàng hóa", "Tổng giá trị hợp đồng", "Trạng thái HĐ", "Trạng thái XH"};
        String fileName = "danh-sach-dx-pd-kq-chao-gia.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhKqBttHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoQdPd();
            objs[3] = hdr.getSoQdKq();
            objs[4] = hdr.getSlHdChuaKy();
            objs[5] = hdr.getSlHdDaKy();
            objs[6] = hdr.getNgayMkho();
            objs[7] = hdr.getTenLoaiVthh();
            objs[8] = hdr.getTenCloaiVthh();
            objs[9] = hdr.getTongGiaTriHdong();
            objs[10] = hdr.getTenTrangThaiHd();
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
            String fileTemplate = "bantructiep/" + templatePath;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhKqBttHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdQdKqHdr(detail.getId());
            listDvi.forEach(dataDvi -> {
                List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                listDviDtl.forEach(dataDviDtl -> {
                    List<XhTcTtinBtt> listTtin  = xhTcTtinBttRepository.findAllByIdDviDtl(dataDviDtl.getId());
                    dataDviDtl.setChildren(listTtin.stream().filter(item -> item.getLuaChon()).collect(Collectors.toList()));
                });
            });
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}