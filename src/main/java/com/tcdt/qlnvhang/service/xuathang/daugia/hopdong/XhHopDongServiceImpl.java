package com.tcdt.qlnvhang.service.xuathang.daugia.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongDdiemNhapKho;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongDdiemNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhDdiemNhapKhoReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhHopDongDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhHopDongHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.docx4j.wml.P;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhHopDongServiceImpl extends BaseServiceImpl {
    @Autowired
    private XhHopDongHdrRepository xhHopDongHdrRepository;

    @Autowired
    private XhHopDongDtlRepository xhHopDongDtlRepository;

    @Autowired
    private XhHopDongDdiemNhapKhoRepository xhHopDongDdiemNhapKhoRepository;

    @Autowired
    private XhKqBdgHdrRepository xhKqBdgHdrRepository;

    public Page<XhHopDongHdr> searchPage(CustomUserDetails currentUser, XhHopDongHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setTrangThai(Contains.DAKY);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhHopDongHdr> search = xhHopDongHdrRepository.searchPage(req, pageable);
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
    public XhHopDongHdr create(CustomUserDetails currentUser, XhHopDongHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoHopDong()) && DataUtils.isNullObject(req.getIdHopDong()) && xhHopDongHdrRepository.existsBySoHopDong(req.getSoHopDong())) {
            throw new Exception("Số hợp đồng " + req.getSoHopDong() + " đã tồn tại");
        }
        if (!StringUtils.isEmpty(req.getSoPhuLuc()) && !DataUtils.isNullObject(req.getIdHopDong()) && xhHopDongHdrRepository.existsBySoPhuLuc(req.getSoPhuLuc())) {
            throw new Exception("Số phụ lục " + req.getSoPhuLuc() + " đã tồn tại");
        }
        if (!DataUtils.isNullObject(req.getIdQdKq())) {
            xhKqBdgHdrRepository.findById(req.getIdQdKq()).map(checkKetQua -> {
                checkKetQua.setTrangThaiHd(Contains.DANG_THUC_HIEN);
                return xhKqBdgHdrRepository.save(checkKetQua);
            }).orElseThrow(() -> new Exception("Số quyết định phê duyệt kết quả bán đấu giá không tồn tại"));
        }
        XhHopDongHdr data = new XhHopDongHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        List<String> maDviTsanList = req.getListMaDviTsan();
        data.setMaDviTsan(maDviTsanList.isEmpty() ? null : String.join(",", maDviTsanList));
        XhHopDongHdr created = xhHopDongHdrRepository.save(data);
        saveDetail(req, created.getId());
        return created;
    }


    void saveDetail(XhHopDongHdrReq req, Long idHdr) {
        xhHopDongDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhHopDongDtlReq dtlReq : req.getChildren()) {
            XhHopDongDtl dtl = new XhHopDongDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            dtl.setTyPe(req.getIdHopDong() != null ? true : false);
            xhHopDongDtlRepository.save(dtl);
            if (req.getIdHopDong() == null) {
                xhHopDongDdiemNhapKhoRepository.deleteAllByIdDtl((dtlReq.getId()));
                for (XhDdiemNhapKhoReq nhapKhoReq : dtlReq.getChildren()) {
                    XhHopDongDdiemNhapKho nhapKho = new XhHopDongDdiemNhapKho();
                    BeanUtils.copyProperties(nhapKhoReq, nhapKho, "id");
                    nhapKho.setId(null);
                    nhapKho.setIdDtl(dtl.getId());
                    xhHopDongDdiemNhapKhoRepository.save(nhapKho);
                }
            }
        }
    }

    @Transactional
    public XhHopDongHdr update(CustomUserDetails currentUser, XhHopDongHdrReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        XhHopDongHdr data = xhHopDongHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));

        if (DataUtils.isNullObject(req.getIdHopDong()) && !StringUtils.isEmpty(req.getSoHopDong())) {
            boolean soHongDongExists = xhHopDongHdrRepository.existsBySoHopDongAndIdNot(req.getSoHopDong(), req.getId());
            if (soHongDongExists) {
                throw new Exception("Số hợp đồng " + req.getSoHopDong() + " đã tồn tại");
            }
        } else if (!StringUtils.isEmpty(req.getSoPhuLuc())) {
            boolean soPhuLucExists = xhHopDongHdrRepository.existsBySoPhuLucAndIdNot(req.getSoPhuLuc(), req.getId());
            if (soPhuLucExists) {
                throw new Exception("Số phụ lục " + req.getSoPhuLuc() + " đã tồn tại");
            }
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiXh");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setMaDviTsan(req.getListMaDviTsan() != null ? String.join(",", req.getListMaDviTsan()) : null);
        XhHopDongHdr update = xhHopDongHdrRepository.save(data);
        saveDetail(req, update.getId());
        return update;
    }

    public List<XhHopDongHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhHopDongHdr> list = xhHopDongHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhHopDongHdr> allById = xhHopDongHdrRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNhapXuat = getListDanhMucChung("KIEU_NHAP_XUAT");
        for (XhHopDongHdr data : allById) {
            List<XhHopDongDtl> listDtl = xhHopDongDtlRepository.findAllByIdHdr(data.getId());
            for (XhHopDongDtl dataDtl : listDtl) {
                List<XhHopDongDdiemNhapKho> listNhapKho = xhHopDongDdiemNhapKhoRepository.findAllByIdDtl(dataDtl.getId());
                listNhapKho.forEach(dataNhapKho -> {
                    dataNhapKho.setTenDiemKho(mapDmucDvi.getOrDefault(dataNhapKho.getMaDiemKho(), null));
                    dataNhapKho.setTenNhaKho(mapDmucDvi.getOrDefault(dataNhapKho.getMaNhaKho(), null));
                    dataNhapKho.setTenNganKho(mapDmucDvi.getOrDefault(dataNhapKho.getMaNganKho(), null));
                    dataNhapKho.setTenLoKho(mapDmucDvi.getOrDefault(dataNhapKho.getMaLoKho(), null));
                });
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                dataDtl.setChildren(listNhapKho);
            }
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNhapXuat(mapKieuNhapXuat);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(listDtl);
            if (!DataUtils.isNullObject(data.getMaDviTsan())) {
                data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
            }
            List<XhHopDongHdr> listPhuLuc = xhHopDongHdrRepository.findAllByIdHopDong(data.getId());
            if (listPhuLuc != null && !listPhuLuc.isEmpty()) {
                data.setPhuLuc(listPhuLuc);
            }
        }
        return allById;
    }

    public XhHopDongHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhHopDongHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhHopDongHdr data = xhHopDongHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));

        if (!data.getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ xóa bản ghi ở trạng thái dự thảo");
        }
        List<XhHopDongDtl> listDtl = xhHopDongDtlRepository.findAllByIdHdr(data.getId());
        for (XhHopDongDtl dataDtl : listDtl) {
            xhHopDongDdiemNhapKhoRepository.deleteAllByIdDtl(dataDtl.getId());
        }
        xhHopDongDtlRepository.deleteAllByIdHdr(data.getId());
        xhHopDongHdrRepository.delete(data);
    }

    public XhHopDongHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhHopDongHdr data = xhHopDongHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.DAKY + Contains.DUTHAO:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        XhHopDongHdr created = xhHopDongHdrRepository.save(data);
        Optional<XhKqBdgHdr> optionalKq = xhKqBdgHdrRepository.findById(data.getId());
        optionalKq.ifPresent(kq -> {
            Integer slHdongDaKy = xhHopDongHdrRepository.countSlHopDongDaKy(data.getIdQdKq(), data.getTrangThai());
            kq.setSlHopDongDaKy(slHdongDaKy);
            xhKqBdgHdrRepository.save(kq);
        });
        return created;
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        try {
            String reportFilePath = baseReportFolder + "bandaugia/Thông tin hợp đồng bán.docx";
            FileInputStream inputStream = new FileInputStream(reportFilePath);
            XhHopDongHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException | XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}
