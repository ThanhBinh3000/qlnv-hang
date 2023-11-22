package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttServiceImpl;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhQdPdKhBttServicelmpl extends BaseServiceImpl {

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;
    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;
    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;
    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;
    @Autowired
    private XhThopDxKhBttRepository xhThopDxKhBttRepository;
    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;
    @Autowired
    private XhTcTtinBttServiceImpl xhTcTtinBttServiceImpl;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhQdPdKhBttHdr> searchPage(CustomUserDetails currentUser, XhQdPdKhBttHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setMaCuc(dvql);
            req.setTrangThai(Contains.BAN_HANH);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdPdKhBttHdr> search = xhQdPdKhBttHdrRepository.searchPage(req, pageable);
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
    public XhQdPdKhBttHdr create(CustomUserDetails currentUser, XhQdPdKhBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if ("QDKH".equals(req.getType()) && !StringUtils.isEmpty(req.getSoQdPd()) && xhQdPdKhBttHdrRepository.existsBySoQdPd(req.getSoQdPd())) {
            throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
        }
        if ("QDDC".equals(req.getType()) && !StringUtils.isEmpty(req.getSoQdDc()) && xhQdPdKhBttHdrRepository.existsBySoQdDc(req.getSoQdDc())) {
            throw new Exception("Số quyết định điều chỉnh " + req.getSoQdDc() + " đã tồn tại");
        }
        XhQdPdKhBttHdr data = new XhQdPdKhBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setLastest(false);
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        if ("QDKH".equals(data.getType())) {
            data.setTrangThai(Contains.DU_THAO);
            data.setLanDieuChinh(Integer.valueOf(0));
        }
        if ("QDDC".equals(data.getType())) {
            data.setTrangThai(Contains.DA_LAP);
            long occurrenceCount = xhQdPdKhBttHdrRepository.countBySoQdPdAndType(data.getSoQdPd(), data.getType());
            data.setLanDieuChinh(Integer.valueOf((int) (occurrenceCount + 1)));
        }
        XhQdPdKhBttHdr created = xhQdPdKhBttHdrRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getCanCuPhapLy())) {
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), XhQdPdKhBttHdr.TABLE_NAME);
            created.setCanCuPhapLy(canCuPhapLy);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
            created.setFileDinhKem(fileDinhKem);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKemDc())) {
            List<FileDinhKem> fileDinhKemDc = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemDc(), created.getId(), XhQdPdKhBttHdr.TABLE_NAME + "_DIEU_CHINH");
            created.setFileDinhKemDc(fileDinhKemDc);
        }
        if ("QDKH".equals(data.getType()) && "TH".equals(data.getPhanLoai())) {
            xhThopDxKhBttRepository.findById(created.getIdThHdr()).ifPresent(tongHop -> {
                tongHop.setIdSoQdPd(data.getId());
                tongHop.setSoQdPd(data.getSoQdPd());
                tongHop.setTrangThai(Contains.DADUTHAO_QD);
                xhThopDxKhBttRepository.save(tongHop);
            });
        }
        this.saveDetail(req, created.getId(), false);
        return created;
    }

    void saveDetail(XhQdPdKhBttHdrReq req, Long idHdr, Boolean isCheckRequired) {
        if (isCheckRequired) {
            xhQdPdKhBttDtlRepository.deleteAllByIdHdr(idHdr);
        }
        for (XhQdPdKhBttDtlReq dtlReq : req.getChildren()) {
            XhQdPdKhBttDtl dtl = new XhQdPdKhBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            dtl.setNamKh(req.getNamKh());
            dtl.setSoQdDc(req.getSoQdDc());
            dtl.setSoQdPd(req.getSoQdPd());
            dtl.setTrangThai(Contains.CHUA_THUC_HIEN);
            dtl.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
            dtl.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
            xhQdPdKhBttDtlRepository.save(dtl);
            if (isCheckRequired) {
                xhQdPdKhBttDviRepository.deleteAllByIdDtl(dtlReq.getId());
            }
            for (XhQdPdKhBttDviReq dviReq : dtlReq.getChildren()) {
                XhQdPdKhBttDvi dvi = new XhQdPdKhBttDvi();
                BeanUtils.copyProperties(dviReq, dvi, "id");
                dvi.setIdDtl(dtl.getId());
                dvi.setIsKetQua(false);
                xhQdPdKhBttDviRepository.save(dvi);
                if (isCheckRequired) {
                    xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dviReq.getId());
                }
                for (XhQdPdKhBttDviDtlReq dviDtlReq : dviReq.getChildren()) {
                    XhQdPdKhBttDviDtl dviDtl = new XhQdPdKhBttDviDtl();
                    BeanUtils.copyProperties(dviDtlReq, dviDtl, "id");
                    dviDtl.setIdDvi(dvi.getId());
                    xhQdPdKhBttDviDtlRepository.save(dviDtl);
                }
            }
        }
    }

    @Transactional
    public XhQdPdKhBttHdr update(CustomUserDetails currentUser, XhQdPdKhBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if ("QDKH".equals(req.getType()) && !StringUtils.isEmpty(req.getSoQdPd()) && xhQdPdKhBttHdrRepository.existsBySoQdPdAndIdNot(req.getSoQdPd(), req.getId())) {
            throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
        }
        if ("QDDC".equals(req.getType()) && !StringUtils.isEmpty(req.getSoQdDc()) && xhQdPdKhBttHdrRepository.existsBySoQdDcAndIdNot(req.getSoQdDc(), req.getId())) {
            throw new Exception("Số quyết định điều chỉnh " + req.getSoQdDc() + " đã tồn tại");
        }
        XhQdPdKhBttHdr data = xhQdPdKhBttHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        BeanUtils.copyProperties(req, data, "id", "maDvi", "lastest", "lanDieuChinh");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhQdPdKhBttHdr updated = xhQdPdKhBttHdrRepository.save(data);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME);
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_DIEU_CHINH");
        fileDinhKemService.delete(updated.getId(), tableNames);
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), updated.getId(), XhQdPdKhBttHdr.TABLE_NAME);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), updated.getId(), XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        List<FileDinhKem> fileDinhKemDc = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemDc(), updated.getId(), XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        updated.setCanCuPhapLy(canCuPhapLy);
        updated.setFileDinhKem(fileDinhKem);
        updated.setFileDinhKemDc(fileDinhKemDc);
        if ("QDKH".equals(updated.getType()) && "TH".equals(updated.getPhanLoai())) {
            xhThopDxKhBttRepository.findById(updated.getIdThHdr()).ifPresent(tongHop -> {
                tongHop.setIdSoQdPd(updated.getId());
                tongHop.setSoQdPd(updated.getSoQdPd());
                tongHop.setTrangThai(Contains.DADUTHAO_QD);
                xhThopDxKhBttRepository.save(tongHop);
            });
        }
        this.saveDetail(req, updated.getId(), true);
        return updated;
    }

    public List<XhQdPdKhBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBttHdr> list = xhQdPdKhBttHdrRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapPhuongThucTt = getListDanhMucChung("PHUONG_THUC_TT");
        for (XhQdPdKhBttHdr data : list) {
            List<XhQdPdKhBttDtl> listDtl = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
            for (XhQdPdKhBttDtl dataDtl : listDtl) {
                BigDecimal giaDuocDuyet = BigDecimal.ZERO;
                List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdDtl(dataDtl.getId());
                for (XhQdPdKhBttDvi dataDvi : listDvi) {
                    List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                    for (XhQdPdKhBttDviDtl dataDviDtl : listDviDtl) {
                        dataDviDtl.setTenDiemKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaDiemKho(), null));
                        dataDviDtl.setTenNhaKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaNhaKho(), null));
                        dataDviDtl.setTenNganKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaNganKho(), null));
                        dataDviDtl.setTenLoKho(mapDmucDvi.getOrDefault(dataDviDtl.getMaLoKho(), null));
                        dataDviDtl.setTenLoaiVthh(mapVthh.getOrDefault(dataDviDtl.getLoaiVthh(), null));
                        dataDviDtl.setTenCloaiVthh(mapVthh.getOrDefault(dataDviDtl.getCloaiVthh(), null));
                        if (dataDviDtl.getDonGiaDuocDuyet().equals(BigDecimal.ZERO)) {
                            Long longNamKh = data.getNamKh() != null ? data.getNamKh().longValue() : null;
                            if (dataDviDtl.getLoaiVthh() != null && longNamKh != null && dataDviDtl.getLoaiVthh().startsWith(Contains.LOAI_VTHH_VATTU)) {
                                giaDuocDuyet = xhQdPdKhBttDviDtlRepository.getGiaDuocDuyetVatTu(dataDviDtl.getCloaiVthh(), dataDviDtl.getLoaiVthh(), longNamKh);
                            } else if (dataDviDtl.getCloaiVthh() != null && dataDviDtl.getLoaiVthh() != null && longNamKh != null && dataDvi.getMaDvi() != null) {
                                giaDuocDuyet = xhQdPdKhBttDviDtlRepository.getGiaDuocDuyetLuongThuc(dataDviDtl.getCloaiVthh(), dataDviDtl.getLoaiVthh(), longNamKh, dataDvi.getMaDvi());
                            }
                            Optional<BigDecimal> giaDuocDuyetOptional = Optional.ofNullable(giaDuocDuyet);
                            dataDviDtl.setDonGiaDuocDuyet(giaDuocDuyet);
                            dataDviDtl.setThanhTienDuocDuyet(dataDviDtl.getSoLuongDeXuat().multiply(giaDuocDuyetOptional.orElse(BigDecimal.ZERO)));
                        }
                    }
                    dataDvi.setTenDvi(mapDmucDvi.getOrDefault(dataDvi.getMaDvi(), null));
                    if (!giaDuocDuyet.equals(BigDecimal.ZERO)) {
                        BigDecimal sumThanhTienDuocDuyet = listDviDtl.stream().map(XhQdPdKhBttDviDtl::getThanhTienDuocDuyet).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                        dataDvi.setTienDuocDuyet(sumThanhTienDuocDuyet);
                    }
                    dataDvi.setChildren(listDviDtl);
                }
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                dataDtl.setTenLoaiVthh(mapVthh.getOrDefault(dataDtl.getLoaiVthh(), null));
                dataDtl.setTenCloaiVthh(mapVthh.getOrDefault(dataDtl.getCloaiVthh(), null));
                dataDtl.setTenPthucTtoan(mapPhuongThucTt.getOrDefault(dataDtl.getPthucTtoan(), null));
                if (!giaDuocDuyet.equals(BigDecimal.ZERO)) {
                    BigDecimal sumThanhTienDuocDuyet = listDvi.stream().map(XhQdPdKhBttDvi::getTienDuocDuyet).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                    dataDtl.setThanhTienDuocDuyet(sumThanhTienDuocDuyet);
                }
                dataDtl.setChildren(listDvi.stream().filter(item -> !item.getIsKetQua()).collect(Collectors.toList()));
            }
            data.setMapVthh(mapVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
            BigDecimal sumSoLuong = listDtl.stream().map(XhQdPdKhBttDtl::getTongSoLuong).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal sumTien = listDtl.stream().map(XhQdPdKhBttDtl::getThanhTien).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            data.setTongSoLuongCuc(sumSoLuong);
            data.setTongTienCuc(sumTien);
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttHdr.TABLE_NAME));
            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH"));
            List<FileDinhKem> fileDinhKemDc = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH"));
            data.setCanCuPhapLy(canCuPhapLy);
            data.setFileDinhKem(fileDinhKem);
            data.setFileDinhKemDc(fileDinhKemDc);
            data.setChildren(listDtl);
        }
        return list;
    }

    public XhQdPdKhBttHdr detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhQdPdKhBttHdr data = xhQdPdKhBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        if ("QDKH".equals(data.getType()) && !data.getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
        }
        if ("QDDC".equals(data.getType()) && !data.getTrangThai().equals(Contains.DA_LAP)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định điều chỉnh ở trạng thái đã lập");
        }
        List<XhQdPdKhBttDtl> dtlList = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
        dtlList.forEach(dataDtl -> {
            List<XhQdPdKhBttDvi> dviList = xhQdPdKhBttDviRepository.findAllByIdDtl(dataDtl.getId());
            dviList.forEach(dvi -> xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dvi.getId()));
            xhQdPdKhBttDviRepository.deleteAllByIdDtl(dataDtl.getId());
        });
        xhQdPdKhBttDtlRepository.deleteAllByIdHdr(data.getId());
        xhQdPdKhBttHdrRepository.delete(data);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME);
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        fileDinhKemService.delete(data.getId(), tableNames);
        if ("QDKH".equals(data.getType()) && "TH".equals(data.getPhanLoai())) {
            xhThopDxKhBttRepository.findById(data.getIdThHdr()).ifPresent(tongHop -> {
                tongHop.setIdSoQdPd(null);
                tongHop.setSoQdPd(null);
                tongHop.setTrangThai(Contains.CHUATAO_QD);
                xhThopDxKhBttRepository.save(tongHop);
            });
        }
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdPdKhBttHdr> list = xhQdPdKhBttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        for (XhQdPdKhBttHdr hdr : list) {
            if ("QDKH".equals(hdr.getType()) && !hdr.getTrangThai().equals(Contains.DUTHAO)) {
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
            }
            if ("QDDC".equals(hdr.getType()) && !hdr.getTrangThai().equals(Contains.DA_LAP)) {
                throw new Exception("Chỉ thực hiện xóa với quyết định điều chỉnh ở trạng thái đã lập");
            }
        }
        List<Long> idHdr = list.stream().map(XhQdPdKhBttHdr::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDtl> listDtl = xhQdPdKhBttDtlRepository.findByIdHdrIn(idHdr);
        List<Long> idDtl = listDtl.stream().map(XhQdPdKhBttDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findByIdDtlIn(idDtl);
        List<Long> idDvi = listDvi.stream().map(XhQdPdKhBttDvi::getId).collect(Collectors.toList());
        List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findByIdDviIn(idDvi);
        xhQdPdKhBttDviDtlRepository.deleteAll(listDviDtl);
        xhQdPdKhBttDviRepository.deleteAll(listDvi);
        xhQdPdKhBttDtlRepository.deleteAll(listDtl);
        xhQdPdKhBttHdrRepository.deleteAll(list);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME);
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_DIEU_CHINH");
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), tableNames);
        List<String> listType = list.stream().map(XhQdPdKhBttHdr::getType).collect(Collectors.toList());
        if (listType.contains("QDKH")) {
            List<Long> listIdThHdr = list.stream().map(XhQdPdKhBttHdr::getIdThHdr).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(listIdThHdr)) {
                List<XhThopDxKhBttHdr> tongHop = xhThopDxKhBttRepository.findByIdIn(listIdThHdr);
                tongHop.forEach(item -> {
                    item.setIdSoQdPd(null);
                    item.setSoQdPd(null);
                    item.setTrangThai(Contains.CHUATAO_QD);
                });
                xhThopDxKhBttRepository.saveAll(tongHop);
            }
        }
    }

    public XhQdPdKhBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBttHdr data = xhQdPdKhBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        if ("QDKH".equals(data.getType())) {
            this.handleQDKHApproval(currentUser, statusReq, data, status);
        } else if ("QDDC".equals(data.getType())) {
            this.handleQDDCApproval(currentUser, statusReq, data, status);
        } else {
            throw new Exception("Loại dữ liệu không được hỗ trợ");
        }
        data.setTrangThai(statusReq.getTrangThai());
        return xhQdPdKhBttHdrRepository.save(data);
    }

    private void handleQDKHApproval(CustomUserDetails currentUser, StatusReq statusReq, XhQdPdKhBttHdr data, String status) throws Exception {
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            data.setLastest(true);
            this.handleBanHanhSpecifics(data);
        }
    }

    private void handleBanHanhSpecifics(XhQdPdKhBttHdr data) throws Exception {
        if ("TH".equals(data.getPhanLoai())) {
            XhThopDxKhBttHdr tongHop = xhThopDxKhBttRepository.findById(data.getIdThHdr())
                    .orElseThrow(() -> new Exception("Tổng hợp đề xuất kế hoạch không tồn tại"));
            if (tongHop.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Tổng hợp đề xuất kế hoạch đã được quyết định");
            }
            tongHop.setTrangThai(Contains.DABANHANH_QD);
            List<XhDxKhBanTrucTiepHdr> listDeXuat = xhDxKhBanTrucTiepHdrRepository.findAllByIdThop(tongHop.getId());
            listDeXuat.forEach(deXuat -> {
                deXuat.setIdSoQdPd(data.getId());
                deXuat.setSoQdPd(data.getSoQdPd());
                deXuat.setNgayKyQd(data.getNgayKyQd());
                deXuat.setTrangThaiTh(Contains.DABANHANH_QD);
            });
            xhDxKhBanTrucTiepHdrRepository.saveAll(listDeXuat);
            xhThopDxKhBttRepository.save(tongHop);
        }
        if ("TTr".equals(data.getPhanLoai())) {
            XhDxKhBanTrucTiepHdr deXuat = xhDxKhBanTrucTiepHdrRepository.findById(data.getIdTrHdr())
                    .orElseThrow(() -> new Exception("Đề xuất kế hoạch không tồn tại"));
            if (deXuat.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Đề xuất kế hoạch này đã được quyết định");
            }
            deXuat.setTrangThaiTh(Contains.DABANHANH_QD);
            xhDxKhBanTrucTiepHdrRepository.save(deXuat);
        }
    }

    private void handleQDDCApproval(CustomUserDetails currentUser, StatusReq statusReq, XhQdPdKhBttHdr data, String status) throws Exception {
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
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            data.setLastest(true);
            xhQdPdKhBttHdrRepository.findById(data.getIdGoc()).ifPresent(quyetDinhDc -> {
                quyetDinhDc.setLastest(false);
                xhQdPdKhBttHdrRepository.save(quyetDinhDc);
            });
        }
    }

    public void export(CustomUserDetails currentUser, XhQdPdKhBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBttHdr> page = this.searchPage(currentUser, req);
        List<XhQdPdKhBttHdr> data = page.getContent();
        String title = " Danh sách quyết định phê duyệt kế hoạch bán trưc tiếp";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KH BTT", "Ngày ký QĐ", "Trích yếu", "Số KH/Tờ trình", "Mã tổng hợp", "Loại hàng hóa", "Chủng loại hành hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Trạng thái"};
        String fileName = "danh-sach-dx-pd-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoQdPd();
            objs[3] = hdr.getNgayKyQd();
            objs[4] = hdr.getTrichYeu();
            objs[5] = hdr.getSoTrHdr();
            objs[6] = hdr.getIdThHdr();
            objs[7] = hdr.getTenLoaiVthh();
            objs[8] = hdr.getTenCloaiVthh();
            objs[9] = hdr.getSlDviTsan();
            objs[10] = hdr.getSlHdongDaKy();
            objs[11] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body, CustomUserDetails currentUser) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        String capDvi = currentUser.getUser().getCapDvi();
        boolean isTongCuc = Contains.CAP_TONG_CUC.equals(capDvi);
        try {
            String templatePath = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "bantructiep/" + templatePath;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            XhQdPdKhBttHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            if (isTongCuc) {
                return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
            } else {
                XhQdPdKhBttDtl detailDtl = null;
                if (detail.getId() != null) {
                    List<XhQdPdKhBttDtl> filteredChildren = detail.getChildren().stream()
                            .filter(type -> type.getMaDvi().equals(currentUser.getDvql()))
                            .collect(Collectors.toList());
                    if (!filteredChildren.isEmpty()) {
                        detailDtl = xhTcTtinBttServiceImpl.detail(filteredChildren.get(0).getId());
                    }
                }
                if (detailDtl != null) {
                    return docxToPdfConverter.convertDocxToPdf(inputStream, detailDtl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private void cloneProject(Long idClone) throws Exception {
//        XhQdPdKhBttHdr hdr = this.detail(idClone);
//        XhQdPdKhBttHdr hdrClone = new XhQdPdKhBttHdr();
//        BeanUtils.copyProperties(hdr, hdrClone);
//        hdrClone.setId(null);
//        hdrClone.setLastest(true);
//        hdrClone.setIdGoc(hdr.getId());
//        xhQdPdKhBttHdrRepository.save(hdrClone);
//        for (XhQdPdKhBttDtl dtl : hdr.getChildren()) {
//            XhQdPdKhBttDtl dtlClone = new XhQdPdKhBttDtl();
//            BeanUtils.copyProperties(dtl, dtlClone);
//            dtlClone.setId(null);
//            dtlClone.setLastest(true);
//            dtlClone.setIdHdr(hdrClone.getId());
//            xhQdPdKhBttDtlRepository.save(dtlClone);
//            for (XhQdPdKhBttDvi dvi : dtlClone.getChildren()) {
//                XhQdPdKhBttDvi dviClone = new XhQdPdKhBttDvi();
//                BeanUtils.copyProperties(dvi, dviClone);
//                dviClone.setId(null);
//                dviClone.setIdDtl(dtlClone.getId());
//                xhQdPdKhBttDviRepository.save(dviClone);
//                for (XhQdPdKhBttDviDtl dviDtl : dvi.getChildren()) {
//                    XhQdPdKhBttDviDtl dviDtlClone = new XhQdPdKhBttDviDtl();
//                    BeanUtils.copyProperties(dviDtl, dviDtlClone);
//                    dviDtlClone.setId(null);
//                    dviDtlClone.setIdDvi(dviClone.getId());
//                    xhQdPdKhBttDviDtlRepository.save(dviDtlClone);
//                }
//            }
//        }
//    }
}