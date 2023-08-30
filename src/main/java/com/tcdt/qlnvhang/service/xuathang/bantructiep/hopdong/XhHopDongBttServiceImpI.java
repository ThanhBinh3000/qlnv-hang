package com.tcdt.qlnvhang.service.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
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

    public Page<XhHopDongBttHdr> searchPage(CustomUserDetails currentUser, XhHopDongBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setTrangThai(Contains.DAKY);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 6));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql.substring(0, 8));
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhHopDongBttHdr> search = xhHopDongBttHdrRepository.searchPage(req, pageable);
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
    public XhHopDongBttHdr create(CustomUserDetails currentUser, XhHopDongBttHdrReq req) throws Exception {
        String capDvi = (currentUser.getUser().getCapDvi());
        if (Contains.CAP_CUC.equals(capDvi)) {
            return createCuc(currentUser, req);
        } else {
            return createChiCuc(currentUser, req);
        }
    }

    @Transactional
    public XhHopDongBttHdr createCuc(CustomUserDetails currentUser, XhHopDongBttHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!StringUtils.isEmpty(req.getSoHd())) {
            Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findBySoHd(req.getSoHd());
            if (DataUtils.isNullObject(req.getIdHd())) {
                if (optional.isPresent()) throw new Exception("Số hợp đồng " + req.getSoHd() + " đã tồn tại");
            }
        }
        if (!StringUtils.isEmpty(req.getSoPhuLuc())) {
            Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findBySoPhuLuc(req.getSoPhuLuc());
            if (!DataUtils.isNullObject(req.getIdHd())) {
                if (optional.isPresent()) throw new Exception("Số phụ lục " + req.getSoPhuLuc() + " đã tồn tại");
            }
        }
        if (!DataUtils.isNullObject(req.getSoQdKq())) {
            Optional<XhKqBttHdr> checkQdKq = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
            if (!checkQdKq.isPresent()) {
                throw new Exception("Số quyết định phê duyệt kết quả bán trực tiếp " + req.getSoQdKq() + "Không tồn tại");
            } else {
                checkQdKq.get().setTrangThaiHd(Contains.DANG_THUC_HIEN);
                xhKqBttHdrRepository.save(checkQdKq.get());
            }
        }
        XhHopDongBttHdr data = new XhHopDongBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        if (!ObjectUtils.isEmpty(req.getListMaDviTsan())) {
            data.setMaDviTsan(String.join(",", req.getListMaDviTsan()));
        }
        XhHopDongBttHdr created = xhHopDongBttHdrRepository.save(data);
        if (!DataUtils.isNullObject(created.getIdQdKq())) {
            Optional<XhKqBttHdr> ketQua = xhKqBttHdrRepository.findById(created.getIdQdKq());
            if (ketQua.isPresent()) {
                Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyCuc(created.getIdQdKq());
                ketQua.get().setSlHdChuaKy(slHdChuaKy);
                BigDecimal slXuatBanKyHdong = xhHopDongBttHdrRepository.countSlXuatBanKyHdongCuc(created.getIdQdKq());
                if (slXuatBanKyHdong != null) {
                    ketQua.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                    ketQua.get().setTongSlChuaKyHdong(ketQua.get().getTongSoLuong().subtract(ketQua.get().getTongSlDaKyHdong()));
                } else {
                    slXuatBanKyHdong = BigDecimal.ZERO;
                    ketQua.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                    ketQua.get().setTongSlChuaKyHdong(ketQua.get().getTongSoLuong().subtract(ketQua.get().getTongSlDaKyHdong()));
                }
                xhKqBttHdrRepository.save(ketQua.get());
            }
        }
        this.saveDetailCuc(req, created.getId());
        return created;
    }

    void saveDetailCuc(XhHopDongBttHdrReq req, Long idHdr) {
        if (DataUtils.isNullObject(req.getIdHd())) {
            xhHopDongBttDtlRepository.deleteAllByIdHdr(idHdr);
            for (XhHopDongBttDtlReq dtlReq : req.getChildren()) {
                XhHopDongBttDtl dtl = new XhHopDongBttDtl();
                BeanUtils.copyProperties(dtlReq, dtl, "id");
                dtl.setIdHdr(idHdr);
                XhHopDongBttDtl create = xhHopDongBttDtlRepository.save(dtl);
                // Bắt đầu phụ Lục
                List<XhHopDongBttDtl> phuLucDtl = xhHopDongBttDtlRepository.findAllByIdHdDtl(dtlReq.getId());
                if (!DataUtils.isNullOrEmpty(phuLucDtl)) {
                    phuLucDtl.forEach(s -> {
                        s.setIdHdDtl(create.getId());
                    });
                    xhHopDongBttDtlRepository.saveAll(phuLucDtl);
                }
                // Kết thúc phụ lục
                xhHopDongBttDviRepository.deleteAllByIdDtl(dtlReq.getId());
                for (XhHopDongBttDviReq dviReq : dtlReq.getChildren()) {
                    XhHopDongBttDvi dvi = new XhHopDongBttDvi();
                    BeanUtils.copyProperties(dviReq, dvi, "id");
                    dvi.setId(null);
                    dvi.setIdDtl(dtl.getId());
                    xhHopDongBttDviRepository.save(dvi);
                }
            }
        } else {
            // Phụ lục Hợp đồng
            xhHopDongBttDtlRepository.deleteAllByIdHdr(idHdr);
            for (XhHopDongBttDtlReq phuLucReq : req.getPhuLucDtl()) {
                XhHopDongBttDtl phuLuc = new XhHopDongBttDtl();
                BeanUtils.copyProperties(phuLucReq, phuLuc, "id");
                phuLuc.setId(null);
                phuLuc.setIdHdr(idHdr);
                xhHopDongBttDtlRepository.save(phuLuc);
            }
        }
    }

    @Transactional
    public XhHopDongBttHdr createChiCuc(CustomUserDetails currentUser, XhHopDongBttHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (DataUtils.isNullObject(req.getIdHd())) {
            if (!StringUtils.isEmpty(req.getSoHd())) {
                Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findBySoHd(req.getSoHd());
                if (optional.isPresent()) throw new Exception("Số hợp đồng " + req.getSoHd() + " đã tồn tại");
            }
        } else {
            if (!StringUtils.isEmpty(req.getSoPhuLuc())) {
                Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findBySoPhuLuc(req.getSoPhuLuc());
                if (optional.isPresent()) throw new Exception("Số phụ lục " + req.getSoPhuLuc() + " đã tồn tại");
            }
        }
        if (!DataUtils.isNullObject(req.getIdChaoGia())) {
            Optional<XhQdPdKhBttDtl> checkChaoGia = xhQdPdKhBttDtlRepository.findById(req.getIdChaoGia());
            if (!checkChaoGia.isPresent()) {
                throw new Exception(" Tổ chức triển khai hế hoạch bán trực tiếp không tồn tại ");
            } else {
                checkChaoGia.get().setTrangThaiHd(Contains.DANG_THUC_HIEN);
                xhQdPdKhBttDtlRepository.save(checkChaoGia.get());
            }
        }
        XhHopDongBttHdr data = new XhHopDongBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        if (!ObjectUtils.isEmpty(req.getListMaDviTsan())) {
            data.setMaDviTsan(String.join(",", req.getListMaDviTsan()));
        }
        XhHopDongBttHdr created = xhHopDongBttHdrRepository.save(data);
        if (!DataUtils.isNullObject(created.getIdChaoGia())) {
            Optional<XhQdPdKhBttDtl> chaoGia = xhQdPdKhBttDtlRepository.findById(created.getIdChaoGia());
            if (chaoGia.isPresent()) {
                Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyChiCuc(created.getIdChaoGia());
                chaoGia.get().setSlHdChuaKy(slHdChuaKy);
                BigDecimal slXuatBanKyHdong = xhHopDongBttHdrRepository.countSlXuatBanKyHdongChiCuc(created.getIdChaoGia());
                if (slXuatBanKyHdong != null) {
                    chaoGia.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                    chaoGia.get().setTongSlChuaKyHdong(chaoGia.get().getTongSoLuong().subtract(chaoGia.get().getTongSlDaKyHdong()));
                } else {
                    slXuatBanKyHdong = BigDecimal.ZERO;
                    chaoGia.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                    chaoGia.get().setTongSlChuaKyHdong(chaoGia.get().getTongSoLuong().subtract(chaoGia.get().getTongSlDaKyHdong()));
                }
                xhQdPdKhBttDtlRepository.save(chaoGia.get());
            }
        }
        this.saveDetailChiCuc(req, created.getId());
        return created;
    }

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
        if (currentUser == null) throw new Exception("Bad request.");
        String capDvi = (currentUser.getUser().getCapDvi());
        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        if (DataUtils.isNullObject(req.getIdHd())) {
            if (!StringUtils.isEmpty(req.getSoHd())) {
                Optional<XhHopDongBttHdr> bySoHd = xhHopDongBttHdrRepository.findBySoHd(req.getSoHd());
                if (bySoHd.isPresent()) {
                    if (!bySoHd.get().getId().equals(req.getId())) throw new Exception("Số hợp đồng đã tồn tại");
                }
            }
        } else {
            if (!StringUtils.isEmpty(req.getSoPhuLuc())) {
                Optional<XhHopDongBttHdr> bySoPhuLuc = xhHopDongBttHdrRepository.findBySoPhuLuc(req.getSoPhuLuc());
                if (bySoPhuLuc.isPresent()) {
                    if (!bySoPhuLuc.get().getId().equals(req.getId())) throw new Exception("Số phụ lục đã tồn tại");
                }
            }
        }
        XhHopDongBttHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi", "trangThaiXh");
        if (!ObjectUtils.isEmpty(req.getListMaDviTsan())) {
            data.setMaDviTsan(String.join(",", req.getListMaDviTsan()));
        }
        XhHopDongBttHdr update = xhHopDongBttHdrRepository.save(data);
        if (Contains.CAP_CUC.equals(capDvi)) {
            if (!DataUtils.isNullObject(update.getIdQdKq())) {
                Optional<XhKqBttHdr> ketQua = xhKqBttHdrRepository.findById(update.getIdQdKq());
                if (ketQua.isPresent()) {
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyCuc(update.getIdQdKq());
                    ketQua.get().setSlHdChuaKy(slHdChuaKy);
                    BigDecimal slXuatBanKyHdong = xhHopDongBttHdrRepository.countSlXuatBanKyHdongCuc(update.getIdQdKq());
                    if (slXuatBanKyHdong != null) {
                        ketQua.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        ketQua.get().setTongSlChuaKyHdong(ketQua.get().getTongSoLuong().subtract(ketQua.get().getTongSlDaKyHdong()));
                    } else {
                        slXuatBanKyHdong = BigDecimal.ZERO;
                        ketQua.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        ketQua.get().setTongSlChuaKyHdong(ketQua.get().getTongSoLuong().subtract(ketQua.get().getTongSlDaKyHdong()));
                    }
                    xhKqBttHdrRepository.save(ketQua.get());
                }
            }
            this.saveDetailCuc(req, update.getId());
        } else {
            if (!DataUtils.isNullObject(update.getIdChaoGia())) {
                Optional<XhQdPdKhBttDtl> chaoGia = xhQdPdKhBttDtlRepository.findById(update.getIdChaoGia());
                if (chaoGia.isPresent()) {
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyChiCuc(update.getIdChaoGia());
                    chaoGia.get().setSlHdChuaKy(slHdChuaKy);
                    BigDecimal slXuatBanKyHdong = xhHopDongBttHdrRepository.countSlXuatBanKyHdongChiCuc(update.getIdChaoGia());
                    if (slXuatBanKyHdong != null) {
                        chaoGia.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        chaoGia.get().setTongSlChuaKyHdong(chaoGia.get().getTongSoLuong().subtract(chaoGia.get().getTongSlDaKyHdong()));
                    } else {
                        slXuatBanKyHdong = BigDecimal.ZERO;
                        chaoGia.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        chaoGia.get().setTongSlChuaKyHdong(chaoGia.get().getTongSoLuong().subtract(chaoGia.get().getTongSlDaKyHdong()));
                    }
                    xhQdPdKhBttDtlRepository.save(chaoGia.get());
                }
            }
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
            String capDvi = (userInfo.getCapDvi());
            if (Contains.CAP_CUC.equals(capDvi)) {
                detailCuc(data);
            } else {
                detailChiCuc(data);
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
                dataDvi.setTenDiemKho(StringUtils.isEmpty(dataDvi.getMaDiemKho()) ? null : mapDmucDvi.get(dataDvi.getMaDiemKho()));
                dataDvi.setTenNhaKho(StringUtils.isEmpty(dataDvi.getMaNhaKho()) ? null : mapDmucDvi.get(dataDvi.getMaNhaKho()));
                dataDvi.setTenNganKho(StringUtils.isEmpty(dataDvi.getMaNganKho()) ? null : mapDmucDvi.get(dataDvi.getMaNganKho()));
                dataDvi.setTenLoKho(StringUtils.isEmpty(dataDvi.getMaLoKho()) ? null : mapDmucDvi.get(dataDvi.getMaLoKho()));
            });
            dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi()) ? null : mapDmucDvi.get(dataDtl.getMaDvi()));
            dataDtl.setChildren(listDvi);
            // Hiển thị tên đơn vị và địa chỉ đơn vị hợp đồng cho phụ lục
            if (!DataUtils.isNullObject(data.getIdHd())) {
                Optional<XhHopDongBttDtl> optionalPhuLuc = xhHopDongBttDtlRepository.findById(dataDtl.getIdHdDtl());
                if (!DataUtils.isNullObject(optionalPhuLuc)) {
                    dataDtl.setTenDviHd(mapDmucDvi.get(optionalPhuLuc.get().getMaDvi()));
                    dataDtl.setDiaChiHd(optionalPhuLuc.get().getDiaChi());
                }
            }
        }
        // Hiển thị phụ lục
        List<XhHopDongBttHdr> listPhuLuc = xhHopDongBttHdrRepository.findAllByIdHd(data.getId());
        for (XhHopDongBttHdr dataPhuLuc : listPhuLuc) {
            List<XhHopDongBttDtl> listPhuLucDtl = xhHopDongBttDtlRepository.findAllByIdHdr(dataPhuLuc.getId());
            listPhuLucDtl.forEach(dataPhuLucDtl -> {
                dataPhuLucDtl.setTenDvi(StringUtils.isEmpty(dataPhuLucDtl.getMaDvi()) ? null : mapDmucDvi.get(dataPhuLucDtl.getMaDvi()));
            });
            dataPhuLuc.setTrangThai(dataPhuLuc.getTrangThai());
            dataPhuLuc.setPhuLucDtl(listPhuLucDtl);
        }
        data.setPhuLuc(listPhuLuc);
        data.setChildren(listDtl);
    }

    void detailChiCuc(XhHopDongBttHdr data) throws Exception {
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        List<XhHopDongBttDvi> listDvi = xhHopDongBttDviRepository.findAllByIdHdr(data.getId());
        listDvi.forEach(dataDvi -> {
            dataDvi.setTenDiemKho(StringUtils.isEmpty(dataDvi.getMaDiemKho()) ? null : mapDmucDvi.get(dataDvi.getMaDiemKho()));
            dataDvi.setTenNhaKho(StringUtils.isEmpty(dataDvi.getMaNhaKho()) ? null : mapDmucDvi.get(dataDvi.getMaNhaKho()));
            dataDvi.setTenNganKho(StringUtils.isEmpty(dataDvi.getMaNganKho()) ? null : mapDmucDvi.get(dataDvi.getMaNganKho()));
            dataDvi.setTenLoKho(StringUtils.isEmpty(dataDvi.getMaLoKho()) ? null : mapDmucDvi.get(dataDvi.getMaLoKho()));
        });
        data.setXhHopDongBttDviList(listDvi);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhHopDongBttHdr data = optional.get();
        if (!data.getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ được xóa với bản ghi là dự thảo");
        }
        UserInfo userInfo = SecurityContextService.getUser();
        String capDvi = (userInfo.getCapDvi());
        if (Contains.CAP_CUC.equals(capDvi)) {
            List<XhHopDongBttDtl> listDtl = xhHopDongBttDtlRepository.findAllByIdHdr(data.getId());
            for (XhHopDongBttDtl dtl : listDtl) {
                xhHopDongBttDviRepository.deleteAllByIdDtl(dtl.getId());
            }
            xhHopDongBttDtlRepository.deleteAllByIdHdr(data.getId());
        } else {
            xhHopDongBttDviRepository.deleteAllByIdHdr(data.getId());
        }
        xhHopDongBttHdrRepository.delete(data);
        if (Contains.CAP_CUC.equals(capDvi)) {
            if (!DataUtils.isNullObject(data.getIdQdKq())) {
                Optional<XhKqBttHdr> ketQua = xhKqBttHdrRepository.findById(data.getIdQdKq());
                if (ketQua.isPresent()) {
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyCuc(data.getIdQdKq());
                    ketQua.get().setSlHdChuaKy(slHdChuaKy);
                    BigDecimal slXuatBanKyHdong = xhHopDongBttHdrRepository.countSlXuatBanKyHdongCuc(data.getIdQdKq());
                    if (slXuatBanKyHdong != null) {
                        ketQua.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        ketQua.get().setTongSlChuaKyHdong(ketQua.get().getTongSoLuong().subtract(ketQua.get().getTongSlDaKyHdong()));
                    } else {
                        slXuatBanKyHdong = BigDecimal.ZERO;
                        ketQua.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        ketQua.get().setTongSlChuaKyHdong(ketQua.get().getTongSoLuong().subtract(ketQua.get().getTongSlDaKyHdong()));
                    }
                    xhKqBttHdrRepository.save(ketQua.get());
                }
            }
        } else {
            if (!DataUtils.isNullObject(data.getIdChaoGia())) {
                Optional<XhQdPdKhBttDtl> chaoGia = xhQdPdKhBttDtlRepository.findById(data.getIdChaoGia());
                if (chaoGia.isPresent()) {
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyChiCuc(data.getIdChaoGia());
                    chaoGia.get().setSlHdChuaKy(slHdChuaKy);
                    BigDecimal slXuatBanKyHdong = xhHopDongBttHdrRepository.countSlXuatBanKyHdongChiCuc(data.getIdChaoGia());
                    if (slXuatBanKyHdong != null) {
                        chaoGia.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        chaoGia.get().setTongSlChuaKyHdong(chaoGia.get().getTongSoLuong().subtract(chaoGia.get().getTongSlDaKyHdong()));
                    } else {
                        slXuatBanKyHdong = BigDecimal.ZERO;
                        chaoGia.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        chaoGia.get().setTongSlChuaKyHdong(chaoGia.get().getTongSoLuong().subtract(chaoGia.get().getTongSlDaKyHdong()));
                    }
                    xhQdPdKhBttDtlRepository.save(chaoGia.get());
                }
            }
        }
    }

    public XhHopDongBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhHopDongBttHdr data = optional.get();
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
        String capDvi = (currentUser.getUser().getCapDvi());
        if (Contains.CAP_CUC.equals(capDvi)) {
            if (!DataUtils.isNullObject(create.getIdQdKq())) {
                Optional<XhKqBttHdr> ketQua = xhKqBttHdrRepository.findById(create.getIdQdKq());
                if (ketQua.isPresent()) {
                    Integer slHdongDaKy = xhHopDongBttHdrRepository.countSlHopDongDaKyCuc(create.getIdQdKq());
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyCuc(create.getIdQdKq());
                    ketQua.get().setSlHdDaKy(slHdongDaKy);
                    ketQua.get().setSlHdChuaKy(slHdChuaKy);
                    BigDecimal slXuatBanKyHdong = xhHopDongBttHdrRepository.countSlXuatBanKyHdongCuc(create.getIdQdKq());
                    if (slXuatBanKyHdong != null) {
                        ketQua.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        ketQua.get().setTongSlChuaKyHdong(ketQua.get().getTongSoLuong().subtract(ketQua.get().getTongSlDaKyHdong()));
                    } else {
                        slXuatBanKyHdong = BigDecimal.ZERO;
                        ketQua.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        ketQua.get().setTongSlChuaKyHdong(ketQua.get().getTongSoLuong().subtract(ketQua.get().getTongSlDaKyHdong()));
                    }
                    xhKqBttHdrRepository.save(ketQua.get());
                }
            }
        } else {
            if (!DataUtils.isNullObject(create.getIdChaoGia())) {
                Optional<XhQdPdKhBttDtl> chaoGia = xhQdPdKhBttDtlRepository.findById(create.getIdChaoGia());
                if (chaoGia.isPresent()) {
                    Integer slHdongDaKy = xhHopDongBttHdrRepository.countSlHopDongDaKyChiCuc(create.getIdChaoGia());
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyChiCuc(create.getIdChaoGia());
                    chaoGia.get().setSlHdDaKy(slHdongDaKy);
                    chaoGia.get().setSlHdChuaKy(slHdChuaKy);

                    BigDecimal slXuatBanKyHdong = xhHopDongBttHdrRepository.countSlXuatBanKyHdongChiCuc(create.getIdChaoGia());
                    if (slXuatBanKyHdong != null) {
                        chaoGia.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        chaoGia.get().setTongSlChuaKyHdong(chaoGia.get().getTongSoLuong().subtract(chaoGia.get().getTongSlDaKyHdong()));
                    } else {
                        slXuatBanKyHdong = BigDecimal.ZERO;
                        chaoGia.get().setTongSlDaKyHdong(slXuatBanKyHdong);
                        chaoGia.get().setTongSlChuaKyHdong(chaoGia.get().getTongSoLuong().subtract(chaoGia.get().getTongSlDaKyHdong()));
                    }
                    xhQdPdKhBttDtlRepository.save(chaoGia.get());
                }
            }
        }
        return create;
    }
}
