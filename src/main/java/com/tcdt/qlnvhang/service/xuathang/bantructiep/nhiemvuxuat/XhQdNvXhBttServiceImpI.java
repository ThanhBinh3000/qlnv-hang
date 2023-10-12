package com.tcdt.qlnvhang.service.xuathang.bantructiep.nhiemvuxuat;

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
        String userCapDvi = currentUser.getUser().getCapDvi();
        if (userCapDvi.equals(Contains.CAP_TONG_CUC)) {
            req.setTrangThai(Contains.BAN_HANH);
        } else if (userCapDvi.equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        } else if (userCapDvi.equals(Contains.CAP_CHI_CUC)) {
            req.setTrangThai(Contains.BAN_HANH);
            req.setMaDviCon(dvql);
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
                data.setTrangThaiXh(data.getTrangThaiXh());
                List<XhQdNvXhBttDtl> listDtl = xhQdNvXhBttDtlRepository.findAllByIdHdr(data.getId());
                data.setChildren(listDtl != null && !listDtl.isEmpty() ? listDtl : Collections.emptyList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Transactional
    public XhQdNvXhBttHdr create(CustomUserDetails currentUser, XhQdNvXhBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoQdNv()) && xhQdNvXhBttHdrRepository.existsBySoQdNv(req.getSoQdNv())) {
            throw new Exception("Số quyết định nhiệm vụ " + req.getSoQdNv() + " đã tồn tại");
        }
        if (req.getPhanLoai().equals("CG")) {
            xhHopDongBttHdrRepository.findById(req.getIdHopDong())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin chào giá"));
        } else {
            xhQdPdKhBttDtlRepository.findById(req.getIdChaoGia())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin ủy quyền/bán lẻ"));
        }
        XhQdNvXhBttHdr data = new XhQdNvXhBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiXh(Contains.DANG_THUC_HIEN);
        if (data.getPhanLoai().equals("UQBL")) {
            List<String> maDviTsanList = req.getListMaDviTsan();
            data.setMaDviTsan(maDviTsanList.isEmpty() ? null : String.join(",", maDviTsanList));
        }
        XhQdNvXhBttHdr created = xhQdNvXhBttHdrRepository.save(data);
        this.saveDetail(req, created.getId());
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
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhQdNvXhBttHdr data = xhQdNvXhBttHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhQdNvXhBttHdrRepository.existsBySoQdNvAndIdNot(req.getSoQdNv(), req.getId())) {
            throw new Exception("Số quyết định nhiệm vụ " + req.getSoQdNv() + " đã tồn tại");
        }
        if (data.getPhanLoai().equals("CG")) {
            xhHopDongBttHdrRepository.findById(data.getIdHopDong())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin chào giá"));
        } else {
            xhQdPdKhBttDtlRepository.findById(data.getIdChaoGia())
                    .orElseThrow(() -> new Exception("Không tìm thấy thông tin ủy quyền/bán lẻ"));
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiXh");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        if (data.getPhanLoai().equals("UQBL")) {
            List<String> maDviTsanList = req.getListMaDviTsan();
            data.setMaDviTsan(maDviTsanList.isEmpty() ? null : String.join(",", maDviTsanList));
        }
        XhQdNvXhBttHdr updated = xhQdNvXhBttHdrRepository.save(data);
        this.saveDetail(req, updated.getId());
        return updated;
    }

    public List<XhQdNvXhBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdNvXhBttHdr> list = xhQdNvXhBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhQdNvXhBttHdr> allById = xhQdNvXhBttHdrRepository.findAllById(ids);
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
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdNvXhBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhQdNvXhBttHdr data = xhQdNvXhBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO, Contains.TU_CHOI_TP, Contains.TUCHOI_LDC);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhQdNvXhBttDtl> listDtl = xhQdNvXhBttDtlRepository.findAllByIdHdr(data.getId());
        for (XhQdNvXhBttDtl dtl : listDtl) {
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(dtl.getId());
        }
        xhQdNvXhBttDtlRepository.deleteAllByIdHdr(data.getId());
        xhQdNvXhBttHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdNvXhBttHdr> list = xhQdNvXhBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        boolean isValidToDelete = list.stream().allMatch(hdr -> hdr.getTrangThai().equals(Contains.DUTHAO) ||
                hdr.getTrangThai().equals(Contains.TU_CHOI_TP) ||
                hdr.getTrangThai().equals(Contains.TUCHOI_LDC));
        if (!isValidToDelete) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<Long> idHdr = list.stream().map(XhQdNvXhBttHdr::getId).collect(Collectors.toList());
        List<XhQdNvXhBttDtl> listDtl = xhQdNvXhBttDtlRepository.findByIdHdrIn(idHdr);
        List<Long> idDtl = listDtl.stream().map(XhQdNvXhBttDtl::getId).collect(Collectors.toList());
        List<XhQdNvXhBttDvi> listDvi = xhQdNvXhBttDviRepository.findByIdDtlIn(idDtl);
        xhQdNvXhBttDviRepository.deleteAll(listDvi);
        xhQdNvXhBttDtlRepository.deleteAll(listDtl);
        xhQdNvXhBttHdrRepository.deleteAll(list);
    }

    @Transactional
    public XhQdNvXhBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdNvXhBttHdr data = xhQdNvXhBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
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
        data.setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            this.updateContractWithQdNv(data);
        }
        XhQdNvXhBttHdr created = xhQdNvXhBttHdrRepository.save(data);
        return created;
    }

    private void updateContractWithQdNv(XhQdNvXhBttHdr data) {
        if ("CG".equals(data.getPhanLoai())) {
            xhHopDongBttHdrRepository.findById(data.getIdHopDong()).ifPresent(hopDong -> {
                hopDong.setIdQdNv(data.getId());
                hopDong.setSoQdNv(data.getSoQdNv());
                xhHopDongBttHdrRepository.save(hopDong);
            });
        } else {
            xhQdPdKhBttDtlRepository.findById(data.getIdChaoGia()).ifPresent(chaoGia -> {
                chaoGia.setIdQdNv(data.getId());
                chaoGia.setSoQdNv(data.getSoQdNv());
                xhQdPdKhBttDtlRepository.save(chaoGia);
            });
        }
    }

    public void export(CustomUserDetails currentUser, XhQdNvXhBttHdrReq req, HttpServletResponse response) throws Exception {
        req.getPaggingReq().setPage(0);
        req.getPaggingReq().setLimit(Integer.MAX_VALUE);
        Page<XhQdNvXhBttHdr> page = this.searchPage(currentUser, req);
        List<XhQdNvXhBttHdr> data = page.getContent();
        String title = "Danh sách quyết định giao nhiệm vụ xuất hàng";
        String[] rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định",
                "Số hợp đồng", "Loại hàng hóa", "Chủng loại hành hóa",
                "Thời gian giao nhận hàng", "Trích yếu quyết định", "Số BB tịnh kho",
                "Số BB hao dôi", "Trạng thái QĐ", "Trạng thái XH"};
        String fileName = "danh-sach-dx-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (int i = 0; i < data.size(); i++) {
            XhQdNvXhBttHdr hdr = data.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoQdNv();
            objs[3] = hdr.getNgayKyQdNv();
            objs[4] = hdr.getSoHopDong();
            objs[5] = hdr.getTenLoaiVthh();
            objs[6] = hdr.getTenCloaiVthh();
            objs[7] = hdr.getTgianGiaoNhan();
            objs[8] = hdr.getTrichYeu();
            objs[9] = hdr.getSoTinhKho();
            objs[10] = hdr.getSoHaoDoi();
            objs[11] = hdr.getTenTrangThai();
            objs[12] = hdr.getTenTrangThaiXh();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}