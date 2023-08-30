package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
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
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
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
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setTrangThai(Contains.BAN_HANH);
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
        if (currentUser == null) throw new Exception("Bad request.");
        if (!StringUtils.isEmpty(req.getSoQdKq())) {
            Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
            if (optional.isPresent()) throw new Exception("Số quyết định " + req.getSoQdKq() + " đã tồn tại");
        }
        XhKqBttHdr data = new XhKqBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        XhKqBttHdr created = xhKqBttHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhKqBttHdrReq req, Long idQdKqHdr) {
        xhQdPdKhBttDviRepository.deleteAllByIdQdKqHdr(idQdKqHdr);
        for (XhQdPdKhBttDviReq dviReq : req.getChildren()) {
            XhQdPdKhBttDvi dvi = new XhQdPdKhBttDvi();
            BeanUtils.copyProperties(dviReq, dvi, "id");
            dvi.setIdQdKqHdr(idQdKqHdr);
            dvi.setTypeQdKq(true);
            xhQdPdKhBttDviRepository.save(dvi);
            xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dviReq.getId());
            for (XhQdPdKhBttDviDtlReq dviDtlReq : dviReq.getChildren()) {
                XhQdPdKhBttDviDtl dviDtl = new XhQdPdKhBttDviDtl();
                BeanUtils.copyProperties(dviDtlReq, dviDtl, "id");
                dviDtl.setIdDvi(dvi.getId());
                dviDtl.setTypeQdKq(true);
                xhQdPdKhBttDviDtlRepository.save(dviDtl);
                xhTcTtinBttRepository.deleteAllByIdDviDtl(dviDtlReq.getId());
                for (XhTcTtinBttReq tTinReq : dviDtlReq.getChildren()) {
                    XhTcTtinBtt tTtin = new XhTcTtinBtt();
                    BeanUtils.copyProperties(tTinReq, tTtin, "id");
                    tTtin.setIdDviDtl(dviDtl.getId());
                    tTtin.setTypeQdKq(true);
                    XhTcTtinBtt create = xhTcTtinBttRepository.save(tTtin);
                    if (!DataUtils.isNullObject(tTinReq.getFileDinhKems())) {
                        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(tTinReq.getFileDinhKems()), create.getId(), XhTcTtinBtt.TABLE_NAME);
                        create.setFileDinhKems(fileDinhKem.get(0));
                    }
                }
            }
        }
    }

    @Transactional
    public XhKqBttHdr update(CustomUserDetails currentUser, XhKqBttHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        Optional<XhKqBttHdr> bySoQdKq = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
        if (bySoQdKq.isPresent()) {
            if (!bySoQdKq.get().getId().equals(req.getId())) throw new Exception("số quyết định đã tồn tại");
        }
        XhKqBttHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiHd", "trangThaiXh");
        XhKqBttHdr updated = xhKqBttHdrRepository.save(data);
        this.saveDetail(req, updated.getId());
        return updated;
    }

    public List<XhKqBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhKqBttHdr> list = xhKqBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhKqBttHdr> allById = xhKqBttHdrRepository.findAllById(ids);
        for (XhKqBttHdr data : allById) {
            List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdQdKqHdr(data.getId());
            for (XhQdPdKhBttDvi dataDvi : listDvi) {
                List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                for (XhQdPdKhBttDviDtl dviDtl : listDviDtl) {
                    List<XhTcTtinBtt> listTtin = xhTcTtinBttRepository.findAllByIdDviDtl(dviDtl.getId());
                    listTtin.forEach(dataTtin -> {
                        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(dataTtin.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
                        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
                            dataTtin.setFileDinhKems(fileDinhKem.get(0));
                        }
                    });
                    dviDtl.setTenDiemKho(StringUtils.isEmpty(dviDtl.getMaDiemKho()) ? null : mapDmucDvi.get(dviDtl.getMaDiemKho()));
                    dviDtl.setTenNhaKho(StringUtils.isEmpty(dviDtl.getMaNhaKho()) ? null : mapDmucDvi.get(dviDtl.getMaNhaKho()));
                    dviDtl.setTenNganKho(StringUtils.isEmpty(dviDtl.getMaNganKho()) ? null : mapDmucDvi.get(dviDtl.getMaNganKho()));
                    dviDtl.setTenLoKho(StringUtils.isEmpty(dviDtl.getMaLoKho()) ? null : mapDmucDvi.get(dviDtl.getMaLoKho()));
                    dviDtl.setTenLoaiVthh(StringUtils.isEmpty(dviDtl.getLoaiVthh()) ? null : mapDmucVthh.get(dviDtl.getLoaiVthh()));
                    dviDtl.setTenCloaiVthh(StringUtils.isEmpty(dviDtl.getCloaiVthh()) ? null : mapDmucVthh.get(dviDtl.getCloaiVthh()));
                    dviDtl.setChildren(listTtin);
                }
                dataDvi.setTenDvi(StringUtils.isEmpty(dataDvi.getMaDvi()) ? null : mapDmucDvi.get(dataDvi.getMaDvi()));
                dataDvi.setChildren(listDviDtl);
            }
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapDmucVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDvi);
            List<XhHopDongBttHdr> listHd = xhHopDongBttHdrRepository.findAllByIdQdKq(data.getId());
            listHd.forEach(dataHd ->{
                dataHd.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(dataHd.getTrangThai()));
                dataHd.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dataHd.getTrangThaiXh()));
            });
            data.setListHopDongBtt(listHd);
        }
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhKqBttHdr data = optional.get();
        if (!data.getTrangThai().equals(Contains.DUTHAO)
                && !data.getTrangThai().equals(Contains.TU_CHOI_TP)
                && !data.getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdPdKhBttDvi> dviList = xhQdPdKhBttDviRepository.findAllByIdQdKqHdr(data.getId());
        for (XhQdPdKhBttDvi dvi : dviList) {
            List<XhQdPdKhBttDviDtl> dviDtlList = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dvi.getId());
            for (XhQdPdKhBttDviDtl dviDtl : dviDtlList) {
                xhTcTtinBttRepository.deleteAllByIdDviDtl(dviDtl.getId());
                fileDinhKemService.delete(data.getId(), Collections.singleton(XhTcTtinBtt.TABLE_NAME));
            }
            xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dvi.getId());
        }
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
        if (currentUser == null) throw new Exception("Bad request.");
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhKqBttHdr data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        if (statusReq.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId())
                && data.getTrangThaiHd().equals(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId())) {
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
                Optional<XhQdPdKhBttDtl> optionalDtl = xhQdPdKhBttDtlRepository.findById(data.getIdChaoGia());
                optionalDtl.get().setIdQdKq(data.getId());
                optionalDtl.get().setSoQdKq(data.getSoQdKq());
                xhQdPdKhBttDtlRepository.save(optionalDtl.get());
            }
        }
        XhKqBttHdr created = xhKqBttHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhKqBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhKqBttHdr> page = this.searchPage(currentUser, req);
        List<XhKqBttHdr> data = page.getContent();
        String title = " Danh sách quyết định phê duyệt kết quả chào giá";
        String[] rowsName = new String[]{"STT", "Số QĐ PDKQ chào giá", "Ngày ký QĐ",
                "Đơn vị", "Số QĐ PDKH bán trực tiếp", "Loại hàng hóa",
                "Chủng loại hành hóa", "Trạng thái"};
        String fileName = "danh-sach-dx-pd-kq-chao-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhKqBttHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
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
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhKqBttHdr> page = this.searchPage(currentUser, req);
        List<XhKqBttHdr> data = page.getContent();
        String title = "Quản lý ký hợp đồng bán hàng DTQG theo phương thức bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Năm KH", "QĐ PD KHBTT",
                "QĐ PD KQ chào giá", "Sl HĐ cần ký", "Sl HĐ đã ký",
                "Thời hạn xuất kho", "Loại hàng háo", "Chủng loại hàng hóa", "Tổng giá trị hợp đồng", "Trạng thái HĐ", "Trạng thái XH"};
        String fileName = "danh-sach-dx-pd-kq-chao-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhKqBttHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoQdPd();
            objs[3] = hdr.getSoQdKq();
            objs[4] = hdr.getSlHdChuaKy();
            objs[5] = hdr.getSlHdDaKy();
            objs[6] = hdr.getNgayMkho();
            objs[8] = hdr.getTenLoaiVthh();
            objs[9] = hdr.getTenCloaiVthh();
            objs[10] = null;
            objs[11] = hdr.getTenTrangThaiHd();
            objs[12] = hdr.getTenTrangThaiXh();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public XhTcTtinBtt detailToChuc(Long id) throws Exception {
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(id)) {
            throw new Exception("Không tồn tại bản ghi.");
        }
        Optional<XhTcTtinBtt> byIdTc = xhTcTtinBttRepository.findById(id);
        if (!byIdTc.isPresent()) {
            throw new UnsupportedOperationException("Bản ghi không tồn tại.");
        }
        XhTcTtinBtt tchuc = byIdTc.get();
//        XhQdPdKhBttDviDtl dviDtl = xhQdPdKhBttDviDtlRepository.findById(tchuc.getIdDviDtl()).get();
//        tchuc.setXhQdPdKhBttDviDtl(dviDtl);
        return tchuc;
    }
}
