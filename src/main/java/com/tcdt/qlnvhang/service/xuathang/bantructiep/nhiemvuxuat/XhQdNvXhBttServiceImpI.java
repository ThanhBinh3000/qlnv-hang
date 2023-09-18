package com.tcdt.qlnvhang.service.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class XhQdNvXhBttServiceImpI extends BaseServiceImpl {

    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;
    @Autowired
    private XhQdNvXhBttDtlRepository xhQdNvXhBttDtlRepository;
    @Autowired
    private XhQdNvXhBttDviRepository xhQdNvXhBttDviRepository;
    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;

    public Page<XhQdNvXhBttHdr> searchPage(CustomUserDetails currentUser, XhQdNvXhBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setTrangThai(Contains.BAN_HANH);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdNvXhBttHdr> search = xhQdNvXhBttHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            try {
                data.setMapVthh(mapDmucVthh);
                data.setMapDmucDvi(mapDmucDvi);
                data.setTrangThai(data.getTrangThai());
                data.setChildren(xhQdNvXhBttDtlRepository.findAllByIdHdr(data.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Transactional
    public XhQdNvXhBttHdr create(CustomUserDetails currentUser, XhQdNvXhBttHdrReq req) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(req.getSoQdNv())) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoQdNv()) && xhQdNvXhBttHdrRepository.existsBySoQdNv(req.getSoQdNv())) {
            throw new Exception("Số quyết định nhiệm vụ " + req.getSoQdNv() + " đã tồn tại");
        }
        XhHopDongBttHdr hopDong = null;
        XhQdPdKhBttDtl chaoGia = null;
        if (req.getPhanLoai().equals("CG")) {
            hopDong = xhHopDongBttHdrRepository.findById(req.getIdHd())
                    .orElseThrow(() -> new Exception("Không tìm thấy hợp đồng bán trực tiếp"));
        } else {
            chaoGia = xhQdPdKhBttDtlRepository.findById(req.getIdChaoGia())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin ủy quyền/bán lẻ"));
        }
        XhQdNvXhBttHdr data = new XhQdNvXhBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiXh(Contains.DANG_THUC_HIEN);
        List<String> maDviTsanList = req.getListMaDviTsan();
        if (!ObjectUtils.isEmpty(maDviTsanList)) {
            data.setMaDviTsan(String.join(",", maDviTsanList));
        }
        XhQdNvXhBttHdr created = xhQdNvXhBttHdrRepository.save(data);
        if (req.getPhanLoai().equals("CG")) {
            hopDong.setTrangThaiXh(Contains.DANG_THUC_HIEN);
            xhHopDongBttHdrRepository.save(hopDong);
        } else {
            chaoGia.setTrangThaiXh(Contains.DANG_THUC_HIEN);
            xhQdPdKhBttDtlRepository.save(chaoGia);
        }
        saveDetail(req, created.getId());
        return created;
    }

    public void saveDetail(XhQdNvXhBttHdrReq req, Long idHdr) {
        xhQdNvXhBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdNvXhBttDtlReq dtlReq : req.getChildren()) {
            XhQdNvXhBttDtl dtl = new XhQdNvXhBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhQdNvXhBttDtlRepository.save(dtl);
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhQdNvXhBttDviReq dviReq : dtlReq.getChildren()) {
                XhQdNvXhBttDvi dvi = new XhQdNvXhBttDvi();
                BeanUtils.copyProperties(dviReq, dvi, "id");
                dvi.setId(null);
                dvi.setIdDtl(dtl.getId());
                xhQdNvXhBttDviRepository.save(dvi);
            }
        }
    }

    @Transactional
    public XhQdNvXhBttHdr update(CustomUserDetails currentUser, XhQdNvXhBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhQdNvXhBttHdr> optional = xhQdNvXhBttHdrRepository.findById(req.getId());
        XhQdNvXhBttHdr data = optional.orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhQdNvXhBttHdrRepository.existsBySoQdNvAndIdNot(req.getSoQdNv(), req.getId())){
            throw new Exception("Số quyết định giao nhiệm vụ " + req.getSoQdNv() + " đã tồn tại");
        }
        if (req.getPhanLoai().equals("CG")) {
            xhHopDongBttHdrRepository.findById(req.getIdHd())
                    .orElseThrow(() -> new Exception("Không tìm thấy hợp đồng bán trực tiếp"));
        } else {
            xhQdPdKhBttDtlRepository.findById(req.getIdChaoGia())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin ủy quyền/bán lẻ"));
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiXh");
        List<String> maDviTsanList = req.getListMaDviTsan();
        if (!ObjectUtils.isEmpty(maDviTsanList)) {
            data.setMaDviTsan(String.join(",", maDviTsanList));
        }
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhQdNvXhBttHdr updated = xhQdNvXhBttHdrRepository.save(data);
        this.saveDetail(req, updated.getId());
        return updated;
    }

    public List<XhQdNvXhBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhQdNvXhBttHdr> allById = xhQdNvXhBttHdrRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(allById)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhQdNvXhBttHdr data : allById) {
            List<XhQdNvXhBttDtl> listDtl = xhQdNvXhBttDtlRepository.findAllByIdHdr(data.getId());
            for (XhQdNvXhBttDtl dataDtl : listDtl) {
                List<XhQdNvXhBttDvi> listDvi = xhQdNvXhBttDviRepository.findAllByIdDtl(dataDtl.getId());
                listDvi.forEach(dataDvi -> {
                    dataDvi.setTenDiemKho(mapDmucDvi.getOrDefault(dataDvi.getMaDiemKho(), null));
                    dataDvi.setTenNhaKho(mapDmucDvi.getOrDefault(dataDvi.getMaNhaKho(), null));
                    dataDvi.setTenNganKho(mapDmucDvi.getOrDefault(dataDvi.getMaNganKho(), null));
                    dataDvi.setTenLoKho(mapDmucDvi.getOrDefault(dataDvi.getMaLoKho(), null));
                });
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                dataDtl.setChildren(listDvi);
            }
            if (!DataUtils.isNullObject(data.getMaDviTsan())) {
                data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
            }
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDtl);
        }
        return allById;
    }

    public XhQdNvXhBttHdr detail(Long id) throws Exception {
        if (id == null) throw new Exception("Tham số không hợp lệ.");
        List<XhQdNvXhBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhQdNvXhBttHdr> optional = xhQdNvXhBttHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhQdNvXhBttHdr data = optional.get();
        if (!data.getTrangThai().equals(Contains.DUTHAO)
                && !data.getTrangThai().equals(Contains.TU_CHOI_TP)
                && !data.getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định giao nhiệm vụ ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdNvXhBttDtl> listDtl = xhQdNvXhBttDtlRepository.findAllByIdHdr(data.getId());
        for (XhQdNvXhBttDtl dtl : listDtl) {
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(dtl.getId());
        }
        xhQdNvXhBttDtlRepository.deleteAllByIdHdr(data.getId());
        xhQdNvXhBttHdrRepository.delete(data);
        if (data.getPhanLoai().equals("CG")) {
            Optional<XhHopDongBttHdr> hopDong = xhHopDongBttHdrRepository.findById(data.getIdHd());
            if (hopDong.isPresent()) {
                hopDong.get().setTrangThaiXh(Contains.CHUA_THUC_HIEN);
                xhHopDongBttHdrRepository.save(hopDong.get());
            }
        } else {
            Optional<XhQdPdKhBttDtl> chaoGia = xhQdPdKhBttDtlRepository.findById(data.getIdChaoGia());
            if (chaoGia.isPresent()) {
                chaoGia.get().setTrangThaiXh(Contains.CHUA_THUC_HIEN);
                xhQdPdKhBttDtlRepository.save(chaoGia.get());
            }
        }
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdNvXhBttHdr> list = xhQdNvXhBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        for (XhQdNvXhBttHdr hdr : list) {
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                    && !hdr.getTrangThai().equals(Contains.TU_CHOI_TP)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDC)) {
                throw new Exception("Chỉ thực hiện xóa với quyết định giao nhiệm vụ ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> idHdr = list.stream().map(XhQdNvXhBttHdr::getId).collect(Collectors.toList());
        List<XhQdNvXhBttDtl> listDtl = xhQdNvXhBttDtlRepository.findByIdHdrIn(idHdr);
        List<Long> idDtl = listDtl.stream().map(XhQdNvXhBttDtl::getId).collect(Collectors.toList());
        List<XhQdNvXhBttDvi> listDvi = xhQdNvXhBttDviRepository.findByIdDtlIn(idDtl);
        xhQdNvXhBttDviRepository.deleteAll(listDvi);
        xhQdNvXhBttDtlRepository.deleteAll(listDtl);
        xhQdNvXhBttHdrRepository.deleteAll(list);
        List<Long> idHopDong = list.stream().map(XhQdNvXhBttHdr::getIdHd).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(idHopDong)) {
            List<XhHopDongBttHdr> hopDong = xhHopDongBttHdrRepository.findByIdIn(idHopDong);
            hopDong.stream().map(item -> {
                item.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
                return item;
            }).collect(Collectors.toList());
        }
        List<Long> idChaoGia = list.stream().map(XhQdNvXhBttHdr::getIdChaoGia).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(idChaoGia)) {
            List<XhQdPdKhBttDtl> chaoGia = xhQdPdKhBttDtlRepository.findByIdIn(idChaoGia);
            chaoGia.stream().map(item -> {
                item.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
                return item;
            }).collect(Collectors.toList());
        }
    }

    @Transactional
    public XhQdNvXhBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        Long id = Long.valueOf(statusReq.getId());
        XhQdNvXhBttHdr data = xhQdNvXhBttHdrRepository.findById(id)
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String newStatus = statusReq.getTrangThai();
        String currentStatus = data.getTrangThai();
        switch (newStatus + currentStatus) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(currentUser.getUser().getId());
                data.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(newStatus);
        if (newStatus.equals(Contains.BAN_HANH)) {
            if (data.getPhanLoai().equals("CG")) {
                Optional<XhHopDongBttHdr> hopDong = xhHopDongBttHdrRepository.findById(data.getIdHd());
                if (hopDong.isPresent()) {
                    if (hopDong.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Hợp đồng bán trực tiếp đã được giao quyết định nhiệm vụ");
                    }
                    hopDong.get().setIdQdNv(data.getId());
                    hopDong.get().setSoQdNv(data.getSoQdNv());
                    hopDong.get().setTrangThaiXh(Contains.DA_HOAN_THANH);
                    xhHopDongBttHdrRepository.save(hopDong.get());
                }
            } else {
                Optional<XhQdPdKhBttDtl> chaoGia = xhQdPdKhBttDtlRepository.findById(data.getIdChaoGia());
                if (chaoGia.isPresent()) {
                    if (chaoGia.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Thông tin ủy quyền/bán lẻ đã được giao nhiệm vụ");
                    }
                    chaoGia.get().setIdQdNv(data.getId());
                    chaoGia.get().setSoQdNv(data.getSoQdNv());
                    chaoGia.get().setTrangThaiXh(Contains.DA_HOAN_THANH);
                    xhQdPdKhBttDtlRepository.save(chaoGia.get());
                }
            }
        }
        return xhQdNvXhBttHdrRepository.save(data);
    }

    public void export(CustomUserDetails currentUser, XhQdNvXhBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdNvXhBttHdr> page = this.searchPage(currentUser, req);
        List<XhQdNvXhBttHdr> data = page.getContent();
        String title = "Danh sách quyết định giao nhiệm vụ xuất hàng";
        String[] rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định",
                "Số hợp đồng", "Loại hàng hóa", "Chủng loại hành hóa",
                "Thời gian giao nhận hàng", "Trích yếu quyết định", "Số BB tịnh kho",
                "Số BB hao dôi", "Trạng thái QĐ", "Trạng thái XH"};
        String fileName = "danh-sach-dx-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = new Object[rowsName.length];
        for (int i = 0; i < data.size(); i++) {
            XhQdNvXhBttHdr hdr = data.get(i);
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = null;
            objs[3] = hdr.getNgayTao();
            objs[4] = hdr.getSoHd();
            objs[5] = hdr.getTenLoaiVthh();
            objs[6] = hdr.getTenCloaiVthh();
            objs[7] = hdr.getTgianGnhan();
            objs[8] = hdr.getTrichYeu();
            objs[9] = null;
            objs[10] = null;
            objs[11] = hdr.getTenTrangThai();
            objs[12] = hdr.getTenTrangThaiXh();
            dataList.add(Arrays.copyOf(objs, objs.length));
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
