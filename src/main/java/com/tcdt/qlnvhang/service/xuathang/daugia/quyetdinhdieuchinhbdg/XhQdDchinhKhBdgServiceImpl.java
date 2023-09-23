package com.tcdt.qlnvhang.service.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtl;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
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
public class XhQdDchinhKhBdgServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdDchinhKhBdgHdrRepository xhQdDchinhKhBdgHdrRepository;
    @Autowired
    private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;
    @Autowired
    private XhQdPdKhBdgPlRepository xhQdPdKhBdgPlRepository;
    @Autowired
    private XhQdPdKhBdgPlDtlRepository xhQdPdKhBdgPlDtlRepository;

    public Page<XhQdDchinhKhBdgHdr> searchPage(CustomUserDetails currentUser, XhQdDchinhKhBdgReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setTrangThai(Contains.BAN_HANH);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdDchinhKhBdgHdr> search = xhQdDchinhKhBdgHdrRepository.searchPage(req, pageable);
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
    public XhQdDchinhKhBdgHdr create(CustomUserDetails currentUser, XhQdDchinhKhBdgReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoQdDc()) && xhQdDchinhKhBdgHdrRepository.existsBySoQdDc(req.getSoQdDc())) {
            throw new Exception("Số quyết định điều chỉnh " + req.getSoQdDc() + " đã tồn tại");
        }
        XhQdDchinhKhBdgHdr data = new XhQdDchinhKhBdgHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DA_LAP);
        Long idQdPd = req.getIdQdPd();
        long occurrenceCount = xhQdDchinhKhBdgHdrRepository.countByidQdPd(idQdPd);
        data.setLanDieuChinh(Integer.valueOf((int) (occurrenceCount + 1)));
        XhQdDchinhKhBdgHdr created = xhQdDchinhKhBdgHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhQdDchinhKhBdgReq req, Long idHdr) {
        xhQdPdKhBdgDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdPdKhBdgDtlReq dtlReq : req.getChildren()) {
            XhQdPdKhBdgDtl dtl = new XhQdPdKhBdgDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            dtl.setNam(req.getNam());
            dtl.setSoQdPd(req.getSoQdPd());
            dtl.setIsDieuChinh(false);
            dtl.setLastest(true);
            dtl.setSoQdDc(req.getSoQdDc());
            dtl.setTrangThai(Contains.CHUA_THUC_HIEN);
            xhQdPdKhBdgDtlRepository.save(dtl);
            xhQdPdKhBdgPlRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhQdPdKhBdgPlReq plReq : dtlReq.getChildren()) {
                XhQdPdKhBdgPl pl = new XhQdPdKhBdgPl();
                BeanUtils.copyProperties(plReq, pl, "id");
                pl.setIdDtl(dtl.getId());
                xhQdPdKhBdgPlRepository.save(pl);
                xhQdPdKhBdgPlDtlRepository.deleteAllByIdPhanLo(plReq.getId());
                for (XhQdPdKhBdgPlDtlReq plDtlReq : plReq.getChildren()) {
                    XhQdPdKhBdgPlDtl plDtl = new XhQdPdKhBdgPlDtl();
                    BeanUtils.copyProperties(plDtlReq, plDtl, "id");
                    plDtl.setIdPhanLo(pl.getId());
                    xhQdPdKhBdgPlDtlRepository.save(plDtl);
                }
            }
        }
    }

    @Transactional
    public XhQdDchinhKhBdgHdr update(CustomUserDetails currentUser, XhQdDchinhKhBdgReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdDchinhKhBdgHdr data = xhQdDchinhKhBdgHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhQdDchinhKhBdgHdrRepository.existsBySoQdDcAndIdNot(req.getSoQdDc(), req.getId())) {
            throw new Exception("Số quyết điều chỉnh " + req.getSoQdDc() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhQdDchinhKhBdgHdr update = xhQdDchinhKhBdgHdrRepository.save(data);
        this.saveDetail(req, update.getId());
        return update;
    }

    public List<XhQdDchinhKhBdgHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdDchinhKhBdgHdr> list = xhQdDchinhKhBdgHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhQdDchinhKhBdgHdr> allById = xhQdDchinhKhBdgHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhQdDchinhKhBdgHdr data : list) {
            List<XhQdPdKhBdgDtl> listDtl = xhQdPdKhBdgDtlRepository.findAllByIdHdr(data.getId());
            for (XhQdPdKhBdgDtl dataDtl : listDtl) {
                List<XhQdPdKhBdgPl> listPhanLo = xhQdPdKhBdgPlRepository.findAllByIdDtl(dataDtl.getId());
                for (XhQdPdKhBdgPl dataPhanLo : listPhanLo) {
                    List<XhQdPdKhBdgPlDtl> listPhanLoDtl = xhQdPdKhBdgPlDtlRepository.findAllByIdPhanLo(dataPhanLo.getId());
                    listPhanLoDtl.forEach(dataPhanLoDtl -> {
                        dataPhanLoDtl.setTenDiemKho(mapDmucDvi.getOrDefault(dataPhanLoDtl.getMaDiemKho(), null));
                        dataPhanLoDtl.setTenNhaKho(mapDmucDvi.getOrDefault(dataPhanLoDtl.getMaNhaKho(), null));
                        dataPhanLoDtl.setTenNganKho(mapDmucDvi.getOrDefault(dataPhanLoDtl.getMaNganKho(), null));
                        dataPhanLoDtl.setTenLoKho(mapDmucDvi.getOrDefault(dataPhanLoDtl.getMaLoKho(), null));
                        dataPhanLoDtl.setTenLoaiVthh(mapVthh.getOrDefault(dataPhanLoDtl.getLoaiVthh(), null));
                        dataPhanLoDtl.setTenCloaiVthh(mapVthh.getOrDefault(dataPhanLoDtl.getCloaiVthh(), null));
                    });
                    dataPhanLo.setTenDvi(mapDmucDvi.getOrDefault(dataPhanLo.getMaDvi(), null));
                    dataPhanLo.setChildren(listPhanLoDtl);
                }
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                dataDtl.setTenLoaiVthh(mapDmucDvi.getOrDefault(dataDtl.getLoaiVthh(), null));
                dataDtl.setTenCloaiVthh(mapDmucDvi.getOrDefault(dataDtl.getCloaiVthh(), null));
                dataDtl.setChildren(listPhanLo);
            }
            data.setMapVthh(mapVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNhapXuat);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDtl.stream().filter(isDieuChinh -> isDieuChinh.getIsDieuChinh()).collect(Collectors.toList()));
        }
        return list;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhQdDchinhKhBdgHdr data = xhQdDchinhKhBdgHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        if (!data.getTrangThai().equals(Contains.DA_LAP)
                && !data.getTrangThai().equals(Contains.TUCHOI_LDV)
                && !data.getTrangThai().equals(Contains.TUCHOI_LDTC)) {
            throw new Exception("Chỉ thực hiện xóa với điều chỉnh ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdPdKhBdgDtl> listDtl = xhQdPdKhBdgDtlRepository.findAllByIdHdr(data.getId());
        listDtl.forEach(dataDtl -> {
            List<XhQdPdKhBdgPl> listPhanLo = xhQdPdKhBdgPlRepository.findAllByIdDtl(dataDtl.getId());
            listPhanLo.forEach(dataPhanLo -> xhQdPdKhBdgPlDtlRepository.deleteAllByIdPhanLo(dataPhanLo.getId()));
            xhQdPdKhBdgPlRepository.deleteAllByIdDtl(dataDtl.getId());
        });
        xhQdPdKhBdgDtlRepository.deleteAllByIdHdr(data.getId());
        xhQdDchinhKhBdgHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdDchinhKhBdgHdr> list = xhQdDchinhKhBdgHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Không tìm thấy bản ghi để xóa");
        }
        for (XhQdDchinhKhBdgHdr hdr : list) {
            if (!hdr.getTrangThai().equals(Contains.DA_LAP)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDV)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDTC)) {
                throw new Exception("Chỉ thực hiện xóa với điều chỉnh ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> idHdr = list.stream().map(XhQdDchinhKhBdgHdr::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgDtl> listDtl = xhQdPdKhBdgDtlRepository.findByIdHdrIn(idHdr);
        List<Long> idDtl = listDtl.stream().map(XhQdPdKhBdgDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPl> listPhanLo = xhQdPdKhBdgPlRepository.findByIdDtlIn(idDtl);
        List<Long> idPhanLo = listPhanLo.stream().map(XhQdPdKhBdgPl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPlDtl> listPhanLoDtl = xhQdPdKhBdgPlDtlRepository.findByIdPhanLoIn(idPhanLo);
        xhQdPdKhBdgPlDtlRepository.deleteAll(listPhanLoDtl);
        xhQdPdKhBdgPlRepository.deleteAll(listPhanLo);
        xhQdPdKhBdgDtlRepository.deleteAll(listDtl);
        xhQdDchinhKhBdgHdrRepository.deleteAll(list);
    }

    public XhQdDchinhKhBdgHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdDchinhKhBdgHdr data = xhQdDchinhKhBdgHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
            List<XhQdPdKhBdgDtl> list = xhQdPdKhBdgDtlRepository.findAllByIdHdr(data.getId());
            list.forEach(dataDtl -> dataDtl.setIsDieuChinh(true));
            xhQdPdKhBdgDtlRepository.saveAll(list);
        }
        XhQdDchinhKhBdgHdr created = xhQdDchinhKhBdgHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhQdDchinhKhBdgReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdDchinhKhBdgHdr> page = this.searchPage(currentUser, req);
        List<XhQdDchinhKhBdgHdr> data = page.getContent();
        String title = "Danh sách quyết định điều chỉnh quyết định kết quả bán đấu giá";
        String[] rowsName = new String[]{"STT", "Năm KH", "Số QĐ điều chỉnh KH BDG", "Ngày ký QĐ điều chỉnh", "Số QĐ cần điều chỉnh",
                "Số QĐ công văn/tờ trình", "Trích yếu", "Chủng loại hàng", "Số ĐV tài sản", "Số HĐ đã ký",
                "Thời hạn giao nhận", "Trạng thái"};
        String fileName = "danh-sach-quyet-dinh-dieu-chinh-quyet-dinh-ket-qua-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            XhQdDchinhKhBdgHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNam();
            objs[2] = hdr.getSoQdDc();
            objs[3] = hdr.getNgayKyDc();
            objs[4] = hdr.getSoQdPd();
            objs[5] = hdr.getSoCongVan();
            objs[6] = hdr.getTrichYeu();
            objs[7] = hdr.getTenCloaiVthh();
            objs[8] = hdr.getSlDviTsan();
            objs[9] = hdr.getSlHdongDaKy();
            objs[10] = hdr.getThoiHanGiaoNhan();
            objs[11] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
