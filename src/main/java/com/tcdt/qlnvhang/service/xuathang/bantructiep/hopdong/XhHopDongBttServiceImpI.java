package com.tcdt.qlnvhang.service.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
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
public class XhHopDongBttServiceImpI extends BaseServiceImpl {

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;
    @Autowired
    private XhHopDongBttDtlRepository xhHopDongBttDtlRepository;
    @Autowired
    private XhHopDongBttDviRepository xhHopDongBttDviRepository;
    @Autowired
    private XhKqBttHdrRepository xhKqBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;

    public Page<XhHopDongBttHdr> searchPage(CustomUserDetails currentUser, XhHopDongBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setTrangThai(Contains.DAKY);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhHopDongBttHdr> search = xhHopDongBttHdrRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
            if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
                List<XhHopDongBttDvi> listDiaDiemKho = xhHopDongBttDviRepository.findAllByIdHdr(data.getId());
                for (XhHopDongBttDvi dataDvi : listDiaDiemKho) {
                    dataDvi.setTenDiemKho(mapDmucDvi.getOrDefault(dataDvi.getMaDiemKho(), null));
                    dataDvi.setTenNhaKho(mapDmucDvi.getOrDefault(dataDvi.getMaNhaKho(), null));
                    dataDvi.setTenNganKho(mapDmucDvi.getOrDefault(dataDvi.getMaNganKho(), null));
                    dataDvi.setTenLoKho(mapDmucDvi.getOrDefault(dataDvi.getMaLoKho(), null));
                }
                data.setXhHopDongBttDviList(listDiaDiemKho);
            }
        });
        return search;
    }

    @Transactional
    public XhHopDongBttHdr create(CustomUserDetails currentUser, XhHopDongBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        String capDvi = currentUser.getUser().getCapDvi();
        boolean isCapCuc = Contains.CAP_CUC.equals(capDvi);
        if (!StringUtils.isEmpty(req.getSoHopDong()) && DataUtils.isNullObject(req.getIdHd()) && xhHopDongBttHdrRepository.existsBySoHopDong(req.getSoHopDong())) {
            throw new Exception("Số hợp đồng " + req.getSoHopDong() + " đã tồn tại");
        }
        if (!StringUtils.isEmpty(req.getSoPhuLuc()) && !DataUtils.isNullObject(req.getIdHd()) && xhHopDongBttHdrRepository.existsBySoPhuLuc(req.getSoPhuLuc())) {
            throw new Exception("Số phụ lục " + req.getSoPhuLuc() + " đã tồn tại");
        }
        if (isCapCuc) {
            this.updateRelatedEntityStatusCuc(req.getIdQdKq(), Contains.DANG_THUC_HIEN);
        } else {
            this.updateRelatedEntityStatusChiCuc(req.getIdChaoGia(), Contains.DANG_THUC_HIEN);
        }
        XhHopDongBttHdr data = new XhHopDongBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        List<String> maDviTsanList = req.getListMaDviTsan();
        data.setMaDviTsan(maDviTsanList.isEmpty() ? null : String.join(",", maDviTsanList));
        XhHopDongBttHdr created = xhHopDongBttHdrRepository.save(data);
        if (isCapCuc) {
            this.updateRelatedEntityHdChuaKyCuc(created.getIdQdKq());
            this.saveDetailCuc(req, created.getId());
        } else {
            this.updateRelatedEntityHdChuaKyChiCuc(created.getIdChaoGia());
            this.saveDetailChiCuc(req, created.getId());
        }
        return created;
    }

    private void updateRelatedEntityStatusCuc(Long id, String trangThai) throws Exception {
        if (!DataUtils.isNullObject(id)) {
            xhKqBttHdrRepository.findById(id).map(checkData -> {
                checkData.setTrangThaiHd(trangThai);
                return xhKqBttHdrRepository.save(checkData);
            }).orElseThrow(() -> new Exception("Số quyết định phê duyệt kết quả bán trực tiếp không tồn tại"));
        }
    }

    private void updateRelatedEntityStatusChiCuc(Long id, String trangThai) throws Exception {
        if (!DataUtils.isNullObject(id)) {
            xhQdPdKhBttDtlRepository.findById(id).map(checkData -> {
                checkData.setTrangThaiHd(trangThai);
                return xhQdPdKhBttDtlRepository.save(checkData);
            }).orElseThrow(() -> new Exception("Tổ chức triển khai hế hoạch bán trực tiếp không tồn tại"));
        }
    }

    private void updateRelatedEntityHdChuaKyCuc(Long id) {
        if (!DataUtils.isNullObject(id)) {
            xhKqBttHdrRepository.findById(id).ifPresent(relatedEntity -> {
                Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyCuc(id);
                relatedEntity.setSlHdChuaKy(slHdChuaKy);
                xhKqBttHdrRepository.save(relatedEntity);
            });
        }
    }

    private void updateRelatedEntityHdChuaKyChiCuc(Long id) {
        if (!DataUtils.isNullObject(id)) {
            xhQdPdKhBttDtlRepository.findById(id).ifPresent(relatedEntity -> {
                Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyChiCuc(id);
                relatedEntity.setSlHdChuaKy(slHdChuaKy);
                xhQdPdKhBttDtlRepository.save(relatedEntity);
            });
        }
    }

    void saveDetailCuc(XhHopDongBttHdrReq req, Long idHdr) {
        if (DataUtils.isNullObject(req.getIdHd())) {
            this.deleteAndSaveContractDetails(req, idHdr);
        } else {
            this.deleteAndSaveAppendixDetails(req, idHdr);
        }
    }

    //Thêm Hợp đồng detail ở cấp Cục
    private void deleteAndSaveContractDetails(XhHopDongBttHdrReq req, Long idHdr) {
        xhHopDongBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhHopDongBttDtlReq dtlReq : req.getChildren()) {
            XhHopDongBttDtl dtl = new XhHopDongBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            XhHopDongBttDtl create = xhHopDongBttDtlRepository.save(dtl);
            this.processAppendixDetails(dtlReq, create);
            xhHopDongBttDviRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhHopDongBttDviReq dviReq : dtlReq.getChildren()) {
                XhHopDongBttDvi dvi = new XhHopDongBttDvi();
                BeanUtils.copyProperties(dviReq, dvi, "id");
                dvi.setIdDtl(dtl.getId());
                xhHopDongBttDviRepository.save(dvi);
            }
        }
    }

    //Hiển thị thông tin phụ lục để thêm vào detail phụ lục
    private void processAppendixDetails(XhHopDongBttDtlReq parentDtlReq, XhHopDongBttDtl create) {
        List<XhHopDongBttDtl> phuLucDtl = xhHopDongBttDtlRepository.findAllByIdHdDtl(parentDtlReq.getId());
        if (!DataUtils.isNullOrEmpty(phuLucDtl)) {
            phuLucDtl.forEach(s -> s.setIdHdDtl(create.getId()));
            xhHopDongBttDtlRepository.saveAll(phuLucDtl);
        }
    }

    //Thêm thông tin phụ lục detail
    private void deleteAndSaveAppendixDetails(XhHopDongBttHdrReq req, Long idHdr) {
        xhHopDongBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhHopDongBttDtlReq phuLucReq : req.getPhuLucDtl()) {
            XhHopDongBttDtl phuLuc = new XhHopDongBttDtl();
            BeanUtils.copyProperties(phuLucReq, phuLuc, "id");
            phuLuc.setIdHdr(idHdr);
            xhHopDongBttDtlRepository.save(phuLuc);
        }
    }

    //Thêm Hợp đồng detail ở cấp Chi cục
    void saveDetailChiCuc(XhHopDongBttHdrReq req, Long idHdr) {
        xhHopDongBttDviRepository.deleteAllByIdHdr(idHdr);
        for (XhHopDongBttDviReq dviReq : req.getXhHopDongBttDviList()) {
            XhHopDongBttDvi dvi = new XhHopDongBttDvi();
            BeanUtils.copyProperties(dviReq, dvi, "id");
            dvi.setIdHdr(idHdr);
            xhHopDongBttDviRepository.save(dvi);
        }
    }

    @Transactional
    public XhHopDongBttHdr update(CustomUserDetails currentUser, XhHopDongBttHdrReq req) throws Exception {
        if (currentUser == null || req == null || req.getId() == null) {
            throw new Exception("Bad request.");
        }
        String capDvi = currentUser.getUser().getCapDvi();
        boolean isCapCuc = Contains.CAP_CUC.equals(capDvi);
        XhHopDongBttHdr data = xhHopDongBttHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));

        if (DataUtils.isNullObject(req.getIdHd()) && !StringUtils.isEmpty(req.getSoHopDong())) {
            boolean soHdExists = xhHopDongBttHdrRepository.existsBySoHopDongAndIdNot(req.getSoHopDong(), req.getId());
            if (soHdExists) {
                throw new Exception("Số hợp đồng đã tồn tại");
            }
        } else if (!StringUtils.isEmpty(req.getSoPhuLuc())) {
            boolean soPhuLucExists = xhHopDongBttHdrRepository.existsBySoPhuLucAndIdNot(req.getSoPhuLuc(), req.getId());
            if (soPhuLucExists) {
                throw new Exception("Số phụ lục đã tồn tại");
            }
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiXh");
        data.setMaDviTsan(req.getListMaDviTsan() != null ? String.join(",", req.getListMaDviTsan()) : null);
        XhHopDongBttHdr update = xhHopDongBttHdrRepository.save(data);
        if (isCapCuc) {
            this.saveDetailCuc(req, update.getId());
        } else {
            this.saveDetailChiCuc(req, update.getId());
        }
        return update;
    }


    public List<XhHopDongBttHdr> detail(List<Long> ids) throws Exception {
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhHopDongBttHdr> list = xhHopDongBttHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        List<XhHopDongBttHdr> allById = xhHopDongBttHdrRepository.findAllById(ids);
        for (XhHopDongBttHdr data : allById) {
            UserInfo userInfo = SecurityContextService.getUser();
            String capDvi = userInfo.getCapDvi();
            boolean isCapCuc = Contains.CAP_CUC.equals(capDvi);
            if (isCapCuc) {
                this.detailCuc(data);
            } else {
                this.detailChiCuc(data);
            }
            if (!DataUtils.isNullObject(data.getMaDviTsan())) {
                data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
            }
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
        }
        return allById;
    }

    void detailCuc(XhHopDongBttHdr data) throws Exception {
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        List<XhHopDongBttDtl> listDtl = xhHopDongBttDtlRepository.findAllByIdHdr(data.getId());
        for (XhHopDongBttDtl dataDtl : listDtl) {
            List<XhHopDongBttDvi> listDvi = xhHopDongBttDviRepository.findAllByIdDtl(dataDtl.getId());
            listDvi.forEach(dataDvi -> {
                dataDvi.setTenDiemKho(mapDmucDvi.getOrDefault(dataDvi.getMaDiemKho(), null));
                dataDvi.setTenNhaKho(mapDmucDvi.getOrDefault(dataDvi.getMaNhaKho(), null));
                dataDvi.setTenNganKho(mapDmucDvi.getOrDefault(dataDvi.getMaNganKho(), null));
                dataDvi.setTenLoKho(mapDmucDvi.getOrDefault(dataDvi.getMaLoKho(), null));
            });

            dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
            dataDtl.setChildren(listDvi);
            if (!DataUtils.isNullObject(data.getIdHd())) {
                this.setPhuLucDetails(dataDtl, mapDmucDvi);
            }
        }
        this.detailPhuLuc(data, mapDmucDvi);
        data.setChildren(listDtl);
    }

    private void setPhuLucDetails(XhHopDongBttDtl dataDtl, Map<String, String> mapDmucDvi) {
        Optional<XhHopDongBttDtl> optionalPhuLuc = xhHopDongBttDtlRepository.findById(dataDtl.getIdHdDtl());
        if (optionalPhuLuc.isPresent()) {
            XhHopDongBttDtl phuLuc = optionalPhuLuc.get();
            dataDtl.setTenDviHd(mapDmucDvi.getOrDefault(phuLuc.getMaDvi(), null));
            dataDtl.setDiaChiHd(phuLuc.getDiaChi());
        }
    }

    private void detailPhuLuc(XhHopDongBttHdr data, Map<String, String> mapDmucDvi) {
        List<XhHopDongBttHdr> list = xhHopDongBttHdrRepository.findAllByIdHd(data.getId());
        for (XhHopDongBttHdr dataPhuLuc : list) {
            List<XhHopDongBttDtl> listDtl = xhHopDongBttDtlRepository.findAllByIdHdr(dataPhuLuc.getId());
            for (XhHopDongBttDtl dataDtl : listDtl) {
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
            }
            dataPhuLuc.setTrangThai(dataPhuLuc.getTrangThai());
            dataPhuLuc.setPhuLucDtl(listDtl);
        }
        data.setPhuLuc(list);
    }


    void detailChiCuc(XhHopDongBttHdr data) throws Exception {
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        List<XhHopDongBttDvi> listDvi = xhHopDongBttDviRepository.findAllByIdHdr(data.getId());
        listDvi.forEach(dataDvi -> {
            dataDvi.setTenDiemKho(mapDmucDvi.getOrDefault(dataDvi.getMaDiemKho(), null));
            dataDvi.setTenNhaKho(mapDmucDvi.getOrDefault(dataDvi.getMaNhaKho(), null));
            dataDvi.setTenNganKho(mapDmucDvi.getOrDefault(dataDvi.getMaNganKho(), null));
            dataDvi.setTenLoKho(mapDmucDvi.getOrDefault(dataDvi.getMaLoKho(), null));
        });
        data.setXhHopDongBttDviList(listDvi);
    }

    public XhHopDongBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhHopDongBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhHopDongBttHdr data = xhHopDongBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        List<String> allowedStatus = Arrays.asList(Contains.DUTHAO);
        if (!allowedStatus.contains(data.getTrangThai())) {
            throw new Exception("Chỉ được xóa với hợp đồng ở trạng thái là dự thảo");
        }
        UserInfo userInfo = SecurityContextService.getUser();
        String capDvi = userInfo.getCapDvi();
        boolean isCapCuc = Contains.CAP_CUC.equals(capDvi);
        if (isCapCuc) {
            List<XhHopDongBttDtl> listDtl = xhHopDongBttDtlRepository.findAllByIdHdr(data.getId());
            for (XhHopDongBttDtl dtl : listDtl) {
                xhHopDongBttDviRepository.deleteAllByIdDtl(dtl.getId());
            }
            xhHopDongBttDtlRepository.deleteAllByIdHdr(data.getId());
        } else {
            xhHopDongBttDviRepository.deleteAllByIdHdr(data.getId());
        }
        xhHopDongBttHdrRepository.delete(data);
        if (isCapCuc) {
            this.updateRelatedEntityHdChuaKyCuc(data.getIdQdKq());
        } else {
            this.updateRelatedEntityHdChuaKyChiCuc(data.getIdChaoGia());
        }
    }

    @Transactional
    public XhHopDongBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhHopDongBttHdr data = xhHopDongBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
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
        XhHopDongBttHdr create = xhHopDongBttHdrRepository.save(data);
        UserInfo userInfo = SecurityContextService.getUser();
        String capDvi = userInfo.getCapDvi();
        boolean isCapCuc = Contains.CAP_CUC.equals(capDvi);
        if (isCapCuc) {
            if (!DataUtils.isNullObject(create.getIdQdKq())) {
                this.updateRelatedEntityHdDaKyCuc(create.getIdQdKq());
                this.updateRelatedEntityHdChuaKyCuc(data.getIdQdKq());
            }
        } else {
            if (!DataUtils.isNullObject(create.getIdChaoGia())) {
                this.updateRelatedEntityHdDaKyChiCuc(create.getIdChaoGia());
                this.updateRelatedEntityHdChuaKyChiCuc(data.getIdChaoGia());
            }
            if (create.getIdQdNv() != null && create.getSoHopDong() != null) {
                this.updateRelatedEntitySoHopDong(create);
            }
        }
        return create;
    }

    private void updateRelatedEntityHdDaKyCuc(Long id) {
        if (!DataUtils.isNullObject(id)) {
            xhKqBttHdrRepository.findById(id).ifPresent(relatedEntity -> {
                Integer slHdongDaKy = xhHopDongBttHdrRepository.countSlHopDongDaKyCuc(id);
                relatedEntity.setSlHdDaKy(slHdongDaKy);
                xhKqBttHdrRepository.save(relatedEntity);
            });
        }
    }

    private void updateRelatedEntityHdDaKyChiCuc(Long id) {
        if (!DataUtils.isNullObject(id)) {
            xhQdPdKhBttDtlRepository.findById(id).ifPresent(relatedEntity -> {
                Integer slHdongDaKy = xhHopDongBttHdrRepository.countSlHopDongDaKyChiCuc(id);
                relatedEntity.setSlHdDaKy(slHdongDaKy);
                xhQdPdKhBttDtlRepository.save(relatedEntity);
            });
        }
    }

    private void updateRelatedEntitySoHopDong(XhHopDongBttHdr data) {
        xhQdNvXhBttHdrRepository.findById(data.getIdQdNv()).ifPresent(nhiemVu -> {
            String soHopDong = nhiemVu.getSoHopDong() != null ? (", " + data.getSoHopDong()) : data.getSoHopDong();
            xhQdNvXhBttHdrRepository.updateSoHopDong(soHopDong, data.getIdQdNv());
        });
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        try {
            String templatePath = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "bantructiep/" + templatePath;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhHopDongBttHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}