package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
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
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setTrangThai(data.getTrangThai());
        });
        return search;
    }

    @Transactional
    public XhQdPdKhBttHdr create(CustomUserDetails currentUser, XhQdPdKhBttHdrReq req) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(req.getSoQdPd())) {
            throw new Exception("Bad request.");
        }
        if (xhQdPdKhBttHdrRepository.existsBySoQdPd(req.getSoQdPd())) {
            throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
        }
        XhThopDxKhBttHdr dataTh = null;
        XhDxKhBanTrucTiepHdr dataDx = null;
        String phanLoai = req.getPhanLoai();
        if ("TH".equals(phanLoai)) {
            dataTh = xhThopDxKhBttRepository.findById(req.getIdThHdr())
                    .orElseThrow(() -> new Exception("Không tìm thấy tổng hợp kế hoạch bán trực tiếp"));
        } else {
            dataDx = xhDxKhBanTrucTiepHdrRepository.findById(req.getIdTrHdr())
                    .orElseThrow(() -> new Exception("Không tìm thấy đề xuất kế hoạch bán trực tiếp"));
        }
        XhQdPdKhBttHdr data = new XhQdPdKhBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setLastest(false);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DU_THAO);
        XhQdPdKhBttHdr created = xhQdPdKhBttHdrRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getCanCuPhapLy())) {
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), XhQdPdKhBttHdr.TABLE_NAME);
            created.setCanCuPhapLy(canCuPhapLy);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
            created.setFileDinhKem(fileDinhKem);
        }
        if ("TH".equals(phanLoai)) {
            dataTh.setIdSoQdPd(data.getId());
            dataTh.setSoQdPd(data.getSoQdPd());
            dataTh.setTrangThai(Contains.DADUTHAO_QD);
            xhThopDxKhBttRepository.save(dataTh);
        } else {
            dataDx.setIdSoQdPd(data.getId());
            dataDx.setSoQdPd(data.getSoQdPd());
            dataDx.setNgayKyQd(data.getNgayKyQd());
            dataDx.setTrangThaiTh(Contains.DADUTHAO_QD);
            xhDxKhBanTrucTiepHdrRepository.save(dataDx);
        }
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhQdPdKhBttHdrReq req, Long idHdr) {
        xhQdPdKhBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdPdKhBttDtlReq dtlReq : req.getChildren()) {
            XhQdPdKhBttDtl dtl = new XhQdPdKhBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            dtl.setNamKh(req.getNamKh());
            dtl.setSoQdPd(req.getSoQdPd());
            dtl.setLoaiHinhNx(req.getLoaiHinhNx());
            dtl.setKieuNx(req.getKieuNx());
            dtl.setLoaiVthh(req.getLoaiVthh());
            dtl.setCloaiVthh(req.getCloaiVthh());
            dtl.setMoTaHangHoa(req.getMoTaHangHoa());
            dtl.setIsDieuChinh(false);
            dtl.setLastest(false);
            dtl.setTrangThai(Contains.CHUA_THUC_HIEN);
            dtl.setTrangThaiHd(Contains.CHUA_THUC_HIEN);
            dtl.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
            xhQdPdKhBttDtlRepository.save(dtl);
            xhQdPdKhBttDviRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhQdPdKhBttDviReq dviReq : dtlReq.getChildren()) {
                String type = "QdKh";
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
    public XhQdPdKhBttHdr update(CustomUserDetails currentUser, XhQdPdKhBttHdrReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBttHdr data = xhQdPdKhBttHdrRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhQdPdKhBttHdrRepository.existsBySoQdPdAndIdNot(req.getSoQdPd(), req.getId())) {
            throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "lastest");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhQdPdKhBttHdr updated = xhQdPdKhBttHdrRepository.save(data);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME);
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        fileDinhKemService.delete(updated.getId(), tableNames);
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), updated.getId(), XhQdPdKhBttHdr.TABLE_NAME);
        updated.setCanCuPhapLy(canCuPhapLy);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), updated.getId(), XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        updated.setFileDinhKem(fileDinhKem);
        if ("TH".equals(updated.getPhanLoai())) {
            xhThopDxKhBttRepository.findById(updated.getIdThHdr()).ifPresent(optionalTh -> {
                optionalTh.setIdSoQdPd(updated.getId());
                optionalTh.setSoQdPd(updated.getSoQdPd());
                xhThopDxKhBttRepository.save(optionalTh);
            });
        } else {
            xhDxKhBanTrucTiepHdrRepository.findById(updated.getIdTrHdr()).ifPresent(optionalDx -> {
                optionalDx.setIdSoQdPd(data.getId());
                optionalDx.setSoQdPd(data.getSoQdPd());
                optionalDx.setNgayKyQd(data.getNgayKyQd());
                xhDxKhBanTrucTiepHdrRepository.save(optionalDx);
            });
        }
        this.saveDetail(req, updated.getId());
        return updated;
    }

    public List<XhQdPdKhBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBttHdr> allById = xhQdPdKhBttHdrRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(allById)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapPhuongThucTt = getListDanhMucChung("PHUONG_THUC_TT");
        for (XhQdPdKhBttHdr data : allById) {
            List<XhQdPdKhBttDtl> listDtl = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
            for (XhQdPdKhBttDtl dataDtl : listDtl) {
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
                    }
                    dataDvi.setTenDvi(mapDmucDvi.getOrDefault(dataDvi.getMaDvi(), null));
                    dataDvi.setChildren(listDviDtl.stream().filter(type -> "QdKh".equals(type.getType())).collect(Collectors.toList()));
                }
                dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
                dataDtl.setTenLoaiVthh(mapVthh.getOrDefault(dataDtl.getLoaiVthh(), null));
                dataDtl.setTenCloaiVthh(mapVthh.getOrDefault(dataDtl.getCloaiVthh(), null));
                dataDtl.setTenPthucTtoan(mapPhuongThucTt.getOrDefault(dataDtl.getPthucTtoan(), null));
                dataDtl.setChildren(listDvi.stream().filter(type -> "QdKh".equals(type.getType())).collect(Collectors.toList()));
            }
            data.setMapVthh(mapVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttHdr.TABLE_NAME));
            data.setCanCuPhapLy(canCuPhapLy);
            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH"));
            data.setFileDinhKem(fileDinhKem);
            data.setChildren(listDtl.stream().filter(isDieuChinh -> !isDieuChinh.getIsDieuChinh()).collect(Collectors.toList()));
        }
        return allById;
    }

    public XhQdPdKhBttHdr detail(Long id) throws Exception {
        if (id == null) throw new Exception("Tham số không hợp lệ.");
        List<XhQdPdKhBttHdr> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhQdPdKhBttHdr data = xhQdPdKhBttHdrRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        if (!data.getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
        }
        List<XhQdPdKhBttDtl> dtlList = xhQdPdKhBttDtlRepository.findAllByIdHdr(data.getId());
        dtlList.forEach(dtl -> {
            List<XhQdPdKhBttDvi> dviList = xhQdPdKhBttDviRepository.findAllByIdDtl(dtl.getId());
            dviList.forEach(dvi -> {
                xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dvi.getId());
            });
            xhQdPdKhBttDviRepository.deleteAll(dviList);
        });
        xhQdPdKhBttDtlRepository.deleteAll(dtlList);
        xhQdPdKhBttHdrRepository.delete(data);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME);
        tableNames.add(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        fileDinhKemService.delete(data.getId(), tableNames);
        if (data.getPhanLoai().equals("TH")) {
            xhThopDxKhBttRepository.findById(data.getIdThHdr()).ifPresent(tongHop -> {
                tongHop.setIdSoQdPd(null);
                tongHop.setSoQdPd(null);
                tongHop.setTrangThai(Contains.CHUATAO_QD);
                xhThopDxKhBttRepository.save(tongHop);
            });
        } else {
            xhDxKhBanTrucTiepHdrRepository.findById(data.getIdTrHdr()).ifPresent(deXuat -> {
                deXuat.setIdSoQdPd(null);
                deXuat.setSoQdPd(null);
                deXuat.setNgayKyQd(null);
                deXuat.setTrangThaiTh(Contains.CHUATONGHOP);
                xhDxKhBanTrucTiepHdrRepository.save(deXuat);
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
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)) {
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
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
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), tableNames);
        List<Long> listIdThHdr = list.stream().map(XhQdPdKhBttHdr::getIdThHdr).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(listIdThHdr)) {
            List<XhThopDxKhBttHdr> tongHopHdr = xhThopDxKhBttRepository.findByIdIn(listIdThHdr);
            tongHopHdr.forEach(item -> {
                item.setIdSoQdPd(null);
                item.setSoQdPd(null);
                item.setTrangThai(Contains.CHUATAO_QD);
            });
            xhThopDxKhBttRepository.saveAll(tongHopHdr);
        }
        List<Long> listIdTrHdr = list.stream().map(XhQdPdKhBttHdr::getIdTrHdr).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(listIdTrHdr)) {
            List<XhDxKhBanTrucTiepHdr> deXuatHdr = xhDxKhBanTrucTiepHdrRepository.findByIdIn(listIdTrHdr);
            deXuatHdr.forEach(item -> {
                item.setIdSoQdPd(null);
                item.setSoQdPd(null);
                item.setNgayKyQd(null);
                item.setTrangThaiTh(Contains.CHUATONGHOP);
            });
            xhDxKhBanTrucTiepHdrRepository.saveAll(deXuatHdr);
        }
    }

    public XhQdPdKhBttHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBttHdr data = xhQdPdKhBttHdrRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            handleBanHanhSpecifics(data);
            this.cloneProject(data.getId());
        }
        return xhQdPdKhBttHdrRepository.save(data);
    }

    private void handleBanHanhSpecifics(XhQdPdKhBttHdr data) throws Exception {
        if (data.getPhanLoai().equals("TH")) {
            XhThopDxKhBttHdr optionalTh = xhThopDxKhBttRepository.findById(data.getIdThHdr())
                    .orElseThrow(() -> new Exception("Tổng hợp đề xuất kế hoạch không tồn tại"));
            if (optionalTh.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Tổng hợp đề xuất kế hoạch đã được quyết định");
            }
            optionalTh.setTrangThai(Contains.DABANHANH_QD);
            List<XhDxKhBanTrucTiepHdr> listDx = xhDxKhBanTrucTiepHdrRepository.findAllByIdThop(optionalTh.getId());
            listDx.forEach(dataDx -> {
                dataDx.setIdSoQdPd(data.getId());
                dataDx.setSoQdPd(data.getSoQdPd());
                dataDx.setNgayKyQd(data.getNgayKyQd());
                dataDx.setTrangThaiTh(Contains.DABANHANH_QD);
            });
            xhDxKhBanTrucTiepHdrRepository.saveAll(listDx);
            xhThopDxKhBttRepository.save(optionalTh);
        } else {
            XhDxKhBanTrucTiepHdr optionalDx = xhDxKhBanTrucTiepHdrRepository.findById(data.getIdTrHdr())
                    .orElseThrow(() -> new Exception("Đề xuất kế hoạch không tồn tại"));
            if (optionalDx.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Đề xuất kế hoạch này đã được quyết định");
            }
            optionalDx.setTrangThaiTh(Contains.DABANHANH_QD);
            xhDxKhBanTrucTiepHdrRepository.save(optionalDx);
        }
    }

    private void cloneProject(Long idClone) throws Exception {
        XhQdPdKhBttHdr hdr = this.detail(idClone);
        XhQdPdKhBttHdr hdrClone = new XhQdPdKhBttHdr();
        BeanUtils.copyProperties(hdr, hdrClone);
        hdrClone.setId(null);
        hdrClone.setLastest(true);
        hdrClone.setIdGoc(hdr.getId());
        xhQdPdKhBttHdrRepository.save(hdrClone);
        for (XhQdPdKhBttDtl dtl : hdr.getChildren()) {
            XhQdPdKhBttDtl dtlClone = new XhQdPdKhBttDtl();
            BeanUtils.copyProperties(dtl, dtlClone);
            dtlClone.setId(null);
            dtlClone.setLastest(true);
            dtlClone.setIdHdr(hdrClone.getId());
            xhQdPdKhBttDtlRepository.save(dtlClone);
            for (XhQdPdKhBttDvi dvi : dtlClone.getChildren()) {
                XhQdPdKhBttDvi dviClone = new XhQdPdKhBttDvi();
                BeanUtils.copyProperties(dvi, dviClone);
                dviClone.setId(null);
                dviClone.setIdDtl(dtlClone.getId());
                xhQdPdKhBttDviRepository.save(dviClone);
                for (XhQdPdKhBttDviDtl dviDtl : dvi.getChildren()) {
                    XhQdPdKhBttDviDtl dviDtlClone = new XhQdPdKhBttDviDtl();
                    BeanUtils.copyProperties(dviDtl, dviDtlClone);
                    dviDtlClone.setId(null);
                    dviDtlClone.setIdDvi(dviClone.getId());
                    xhQdPdKhBttDviDtlRepository.save(dviDtlClone);
                }
            }
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
        String templatePath;
        if (Contains.CAP_TONG_CUC.equals(capDvi)) {
            templatePath = baseReportFolder + "/bantructiep/Quyết định phê duyệt kế hoạch bán trực tiếp Tổng Cục.docx";
        } else {
            templatePath = baseReportFolder + "/bantructiep/Quyết định phê duyệt kế hoạch bán trực tiếp Cục.docx";
        }
        try {
            FileInputStream inputStream = new FileInputStream(templatePath);
            XhQdPdKhBttHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
            List<XhQdPdKhBttDtl> listDtl = xhQdPdKhBttDtlRepository.findAllByIdHdr(detail.getId());
            listDtl.forEach(dataDtl -> {
                List<XhQdPdKhBttDvi> listDvi = xhQdPdKhBttDviRepository.findAllByIdDtl(dataDtl.getId());
                listDvi.forEach(dataDvi -> {
                    List<XhQdPdKhBttDviDtl> listDviDtl = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
                    dataDvi.setDonGiaDuocDuyet(listDviDtl.get(0).getDonGiaDuocDuyet());
                });
            });
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            throw new Exception("Error reading template file.", e);
        } catch (XDocReportException e) {
            throw new Exception("Error converting template to PDF.", e);
        }
    }
}
