package com.tcdt.qlnvhang.service.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
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
import java.util.stream.Collectors;

@Service
public class XhQdDchinhKhBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdDchinhKhBttHdrRepository xhQdDchinhKhBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;
    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;

    public Page<XhQdDchinhKhBttHdr> searchPage(CustomUserDetails currentUser, XhQdDchinhKhBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdDchinhKhBttHdr> search = xhQdDchinhKhBttHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            try {
                data.setMapVthh(mapDmucVthh);
                data.setMapDmucDvi(mapDmucDvi);
                data.setTrangThai(data.getTrangThai());
                List<XhQdPdKhBttDtl> listDtl = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Transactional
    public XhQdDchinhKhBttHdr create(CustomUserDetails currentUser, XhQdDchinhKhBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoQdDc()) && xhQdDchinhKhBttHdrRepository.existsBySoQdDc(req.getSoQdDc())) {
            throw new Exception("Số quyết định điều chỉnh " + req.getSoQdDc() + " đã tồn tại");
        }
        XhQdDchinhKhBttHdr data = new XhQdDchinhKhBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DA_LAP);
        Long idQdPd = req.getIdQdPd();
        long occurrenceCount = xhQdDchinhKhBttHdrRepository.countByidQdPd(idQdPd);
        data.setLanDieuChinh(Integer.valueOf((int) (occurrenceCount + 1)));
        XhQdDchinhKhBttHdr created = xhQdDchinhKhBttHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }


    void saveDetail(XhQdDchinhKhBttHdrReq req, Long idHdr) {
        xhQdPdKhBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdPdKhBttDtlReq dtlReq : req.getChildren()) {
            XhQdPdKhBttDtl dtl = new XhQdPdKhBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            dtl.setNamKh(req.getNamKh());
            dtl.setSoQdPd(req.getSoQdPd());
            dtl.setSoQdDc(req.getSoQdDc());
            dtl.setLoaiHinhNx(req.getLoaiHinhNx());
            dtl.setKieuNx(req.getKieuNx());
            dtl.setLoaiVthh(req.getLoaiVthh());
            dtl.setCloaiVthh(req.getCloaiVthh());
            dtl.setMoTaHangHoa(req.getMoTaHangHoa());
            dtl.setIsDieuChinh(false);
            dtl.setLastest(true);
            dtl.setTrangThai(Contains.CHUA_THUC_HIEN);
            dtl.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
            dtl.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
            dtl.setLanDieuChinh(req.getLanDieuChinh());
            dtl.setPthucBanTrucTiep(null);
            dtl.setNgayNhanCgia(null);
            dtl.setIdQdKq(null);
            dtl.setSoQdKq(null);
            dtl.setDiaDiemChaoGia(null);
            dtl.setNgayMkho(null);
            dtl.setNgayKthuc(null);
            dtl.setGhiChuChaoGia(null);
            dtl.setThoiHanBan(null);
            dtl.setIdQdNv(null);
            dtl.setSoQdNv(null);
            dtl.setSlHdDaKy(null);
            dtl.setSlHdChuaKy(null);
            xhQdPdKhBttDtlRepository.save(dtl);
            xhQdPdKhBttDviRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhQdPdKhBttDviReq dviReq : dtlReq.getChildren()) {
                String type = "QdDc";
                XhQdPdKhBttDvi dvi = new XhQdPdKhBttDvi();
                BeanUtils.copyProperties(dviReq, dvi, "id");
                dvi.setIdDtl(dtl.getId());
                dvi.setType(type);
                xhQdPdKhBttDviRepository.save(dvi);
                xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dviReq.getId());
                for (XhQdPdKhBttDviDtlReq dviDtlReq : dviReq.getChildren()) {
                    XhQdPdKhBttDviDtl dviDtl = new XhQdPdKhBttDviDtl();
                    BeanUtils.copyProperties(dviDtlReq, dviDtl, "id");
                    dviDtl.setIdDvi(dvi.getId());
                    dviDtl.setType(type);
                    xhQdPdKhBttDviDtlRepository.save(dviDtl);
                }
            }
        }
    }

    @Transactional
    public XhQdDchinhKhBttHdr update(CustomUserDetails currentUser, XhQdDchinhKhBttHdrReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdDchinhKhBttHdr data = xhQdDchinhKhBttHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhQdDchinhKhBttHdrRepository.existsBySoQdDcAndIdNot(req.getSoQdDc(), req.getId())) {
            throw new Exception("Số quyết điều chỉnh " + req.getSoQdDc() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhQdDchinhKhBttHdr update = xhQdDchinhKhBttHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhQdDchinhKhBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdDchinhKhBttHdr> list = xhQdDchinhKhBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhQdDchinhKhBttHdr> allById = xhQdDchinhKhBttHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhQdDchinhKhBttHdr data : list) {
            List<XhQdPdKhBttDtl> listDtl = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
            for (XhQdPdKhBttDtl dataDtl : listDtl) {
                List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdDtl(dataDtl.getId());
                for (XhQdPdKhBttDvi dataDvi : listDvi) {
                    List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                    listDviDtl.forEach(dataDviDtl -> {
                        dataDviDtl.setTenDiemKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaDiemKho(), null));
                        dataDviDtl.setTenNhaKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaNhaKho(), null));
                        dataDviDtl.setTenNganKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaNganKho(), null));
                        dataDviDtl.setTenLoKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaLoKho(), null));
                        dataDviDtl.setTenLoaiVthh(mapVthh.getOrDefault(dataDviDtl.getLoaiVthh(), null));
                        dataDviDtl.setTenCloaiVthh(mapVthh.getOrDefault(dataDviDtl.getCloaiVthh(), null));
                    });
                    dataDvi.setTenDvi(mapDmucDvi.getOrDefault(dataDvi.getMaDvi(), null));
                    dataDvi.setChildren(listDviDtl.stream().filter(type -> "QdDc".equals(type.getType())).collect(Collectors.toList()));
                }
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                dataDtl.setTenLoaiVthh(mapDmucDvi.getOrDefault(dataDtl.getLoaiVthh(), null));
                dataDtl.setTenCloaiVthh(mapDmucDvi.getOrDefault(dataDtl.getCloaiVthh(), null));
                dataDtl.setChildren(listDvi.stream().filter(type -> "QdDc".equals(type.getType())).collect(Collectors.toList()));
            }
            data.setMapVthh(mapVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNhapXuat);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDtl);
        }
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhQdDchinhKhBttHdr data = xhQdDchinhKhBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DA_LAP, Contains.TUCHOI_LDV, Contains.TUCHOI_LDTC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với điều chỉnh ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdPdKhBttDtl> listDtl = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
        listDtl.forEach(dataDtl -> {
            List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdDtl(dataDtl.getId());
            listDvi.forEach(dataDvi -> xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dataDvi.getId()));
            xhQdPdKhBttDviRepository.deleteAllByIdDtl(dataDtl.getId());
        });
        xhQdPdKhBttDtlRepository.deleteAllByIdHdr(data.getId());
        xhQdDchinhKhBttHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdDchinhKhBttHdr> list = xhQdDchinhKhBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = list.stream().allMatch(hdr -> hdr.getTrangThai().equals(Contains.DA_LAP) ||
                hdr.getTrangThai().equals(Contains.TUCHOI_LDV) ||
                hdr.getTrangThai().equals(Contains.TUCHOI_LDTC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với điều chỉnh ở trạng thái bản nháp hoặc từ chối");
        }
        List<Long> idHdr = list.stream().map(XhQdDchinhKhBttHdr::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDtl> listDtl = xhQdPdKhBttDtlRepository.findByIdHdrIn(idHdr);
        List<Long> idDtl = listDtl.stream().map(XhQdPdKhBttDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findByIdDtlIn(idDtl);
        List<Long> idDvi = listDvi.stream().map(XhQdPdKhBttDvi::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findByIdDviIn(idDvi);
        xhQdPdKhBttDviDtlRepository.deleteAll(listDviDtl);
        xhQdPdKhBttDviRepository.deleteAll(listDvi);
        xhQdPdKhBttDtlRepository.deleteAll(listDtl);
        xhQdDchinhKhBttHdrRepository.deleteAll(list);
    }


    public XhQdDchinhKhBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdDchinhKhBttHdr data = xhQdDchinhKhBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDV + Contains.DA_LAP:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC:
                data.setNguoiGuiDuyetId(currentUser.getUser().getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
            case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDTC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDTC + Contains.CHODUYET_LDV:
            case Contains.BAN_HANH + Contains.CHODUYET_LDTC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            List<XhQdPdKhBttDtl> list = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
            list.forEach(dataDtl -> dataDtl.setIsDieuChinh(true));
            xhQdPdKhBttDtlRepository.saveAll(list);
        }
        XhQdDchinhKhBttHdr created = xhQdDchinhKhBttHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhQdDchinhKhBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdDchinhKhBttHdr> page = this.searchPage(currentUser, req);
        List<XhQdDchinhKhBttHdr> data = page.getContent();
        String title = "Danh sách quyết định điều chỉnh quyết định kết quả bán đấu giá";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ điều chỉnh KH bán trực tiếp", "Ngày ký QĐ điều chỉnh", "Số quyết định gốc",
                "Trích yếu", "Loại hàng hóa", "Chủng loại hàng", "Trạng thái"};
        String fileName = "danh-sach-quyet-dinh-dieu-chinh-quyet-dinh-ket-qua-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhQdDchinhKhBttHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoQdDc();
            objs[3] = hdr.getNgayKyDc();
            objs[4] = hdr.getSoQdPd();
            objs[5] = hdr.getTrichYeu();
            objs[6] = hdr.getTenLoaiVthh();
            objs[7] = hdr.getTenCloaiVthh();
            objs[8] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
