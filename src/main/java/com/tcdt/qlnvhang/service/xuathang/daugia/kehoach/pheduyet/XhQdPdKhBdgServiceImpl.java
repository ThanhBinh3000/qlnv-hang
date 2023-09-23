package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgReq;
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
public class XhQdPdKhBdgServiceImpl extends BaseServiceImpl {
    @Autowired
    private XhQdPdKhBdgRepository xhQdPdKhBdgRepository;
    @Autowired
    private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;
    @Autowired
    private XhQdPdKhBdgPlRepository xhQdPdKhBdgPlRepository;
    @Autowired
    private XhQdPdKhBdgPlDtlRepository xhQdPdKhBdgPlDtlRepository;
    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;
    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;
    @Autowired
    private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;
    @Autowired
    private XhQdDchinhKhBdgHdrRepository xhQdDchinhKhBdgHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhQdPdKhBdg> searchPage(CustomUserDetails currentUser, XhQdPdKhBdgReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setMaCuc(dvql);
            req.setTrangThai(Contains.BAN_HANH);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdPdKhBdg> search = xhQdPdKhBdgRepository.searchPage(req, pageable);
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
    public XhQdPdKhBdg create(CustomUserDetails currentUser, XhQdPdKhBdgReq req) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(req.getSoQdPd())) {
            throw new Exception("Bad request.");
        }
        if (xhQdPdKhBdgRepository.existsBySoQdPd(req.getSoQdPd())) {
            throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
        }
        XhThopDxKhBdg dataTh = null;
        XhDxKhBanDauGia dataDx = null;
        String phanLoai = req.getPhanLoai();
        if ("TH".equals(phanLoai)) {
            dataTh = xhThopDxKhBdgRepository.findById(req.getIdThHdr())
                    .orElseThrow(() -> new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá"));
        } else {
            dataDx = xhDxKhBanDauGiaRepository.findById(req.getIdTrHdr())
                    .orElseThrow(() -> new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá"));
        }
        XhQdPdKhBdg data = new XhQdPdKhBdg();
        BeanUtils.copyProperties(req, data);
        data.setLastest(false);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setTrangThai(Contains.DANG_NHAP_DU_LIEU);
        XhQdPdKhBdg created = xhQdPdKhBdgRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getCanCuPhapLy())) {
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), XhQdPdKhBdg.TABLE_NAME);
            created.setCanCuPhapLy(canCuPhapLy);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
            created.setFileDinhKem(fileDinhKem);
        }
        if ("TH".equals(phanLoai)) {
            dataTh.setIdQdPd(data.getId());
            dataTh.setSoQdPd(data.getSoQdPd());
            dataTh.setTrangThai(Contains.DADUTHAO_QD);
            xhThopDxKhBdgRepository.save(dataTh);
        } else {
            dataDx.setIdSoQdPd(data.getId());
            dataDx.setSoQdPd(data.getSoQdPd());
            dataDx.setNgayKyQd(data.getNgayKyQd());
            dataDx.setTrangThaiTh(Contains.DADUTHAO_QD);
            xhDxKhBanDauGiaRepository.save(dataDx);
        }
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhQdPdKhBdgReq req, Long idHdr) {
        xhQdPdKhBdgDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdPdKhBdgDtlReq dtlReq : req.getChildren()) {
            XhQdPdKhBdgDtl dtl = new XhQdPdKhBdgDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            dtl.setNam(req.getNam());
            dtl.setSoQdPd(req.getSoQdPd());
            dtl.setIsDieuChinh(false);
            dtl.setLastest(false);
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
    public XhQdPdKhBdg update(CustomUserDetails currentUser, XhQdPdKhBdgReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBdg data = xhQdPdKhBdgRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu cần sửa"));
        if (xhQdPdKhBdgRepository.existsBySoQdPdAndIdNot(req.getSoQdPd(), req.getId())) {
            throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
        }
        BeanUtils.copyProperties(req, data, "id", "maDvi", "lastest");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(currentUser.getUser().getId());
        XhQdPdKhBdg updated = xhQdPdKhBdgRepository.save(data);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBdg.TABLE_NAME);
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
        fileDinhKemService.delete(updated.getId(), tableNames);
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), updated.getId(), XhQdPdKhBdg.TABLE_NAME);
        updated.setCanCuPhapLy(canCuPhapLy);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), updated.getId(), XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
        updated.setFileDinhKem(fileDinhKem);
        if ("TH".equals(updated.getPhanLoai())) {
            xhThopDxKhBdgRepository.findById(updated.getIdThHdr()).ifPresent(optionalTh -> {
                optionalTh.setIdQdPd(updated.getId());
                optionalTh.setSoQdPd(updated.getSoQdPd());
                xhThopDxKhBdgRepository.save(optionalTh);
            });
        } else {
            xhDxKhBanDauGiaRepository.findById(updated.getIdTrHdr()).ifPresent(optionalDx -> {
                optionalDx.setIdSoQdPd(data.getId());
                optionalDx.setSoQdPd(data.getSoQdPd());
                optionalDx.setNgayKyQd(data.getNgayKyQd());
                xhDxKhBanDauGiaRepository.save(optionalDx);
            });
        }
        this.saveDetail(req, updated.getId());
        return updated;
    }

    public List<XhQdPdKhBdg> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBdg> list = xhQdPdKhBdgRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapPhuongThucTt = getListDanhMucChung("PHUONG_THUC_TT");
        for (XhQdPdKhBdg data : list) {
            List<XhQdPdKhBdgDtl> qdDtl = xhQdPdKhBdgDtlRepository.findAllByIdHdr(data.getId());
            for (XhQdPdKhBdgDtl dataDtl : qdDtl) {
                List<XhQdPdKhBdgPl> qdPhanLo = xhQdPdKhBdgPlRepository.findAllByIdDtl(dataDtl.getId());
                for (XhQdPdKhBdgPl dataPhanLo : qdPhanLo) {
                    List<XhQdPdKhBdgPlDtl> qdPhanLoDtl = xhQdPdKhBdgPlDtlRepository.findAllByIdPhanLo(dataPhanLo.getId());
                    for (XhQdPdKhBdgPlDtl dataPhanLoDtl : qdPhanLoDtl) {
                        dataPhanLoDtl.setTenDiemKho(StringUtils.isEmpty(dataPhanLoDtl.getMaDiemKho()) ? null : mapDmucDvi.get(dataPhanLoDtl.getMaDiemKho()));
                        dataPhanLoDtl.setTenNhaKho(StringUtils.isEmpty(dataPhanLoDtl.getMaNhaKho()) ? null : mapDmucDvi.get(dataPhanLoDtl.getMaNhaKho()));
                        dataPhanLoDtl.setTenNganKho(StringUtils.isEmpty(dataPhanLoDtl.getMaNganKho()) ? null : mapDmucDvi.get(dataPhanLoDtl.getMaNganKho()));
                        dataPhanLoDtl.setTenLoKho(StringUtils.isEmpty(dataPhanLoDtl.getMaLoKho()) ? null : mapDmucDvi.get(dataPhanLoDtl.getMaLoKho()));
                        dataPhanLoDtl.setTenLoaiVthh(StringUtils.isEmpty(dataPhanLoDtl.getLoaiVthh()) ? null : mapVthh.get(dataPhanLoDtl.getLoaiVthh()));
                        dataPhanLoDtl.setTenCloaiVthh(StringUtils.isEmpty(dataPhanLoDtl.getCloaiVthh()) ? null : mapVthh.get(dataPhanLoDtl.getCloaiVthh()));
                    }
                    dataPhanLo.setTenDvi(StringUtils.isEmpty(dataPhanLo.getMaDvi()) ? null : mapDmucDvi.get(dataPhanLo.getMaDvi()));
                    dataPhanLo.setChildren(qdPhanLoDtl);
                }
                dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi()) ? null : mapDmucDvi.get(dataDtl.getMaDvi()));
                dataDtl.setTenLoaiVthh(StringUtils.isEmpty(dataDtl.getLoaiVthh()) ? null : mapVthh.get(dataDtl.getLoaiVthh()));
                dataDtl.setTenCloaiVthh(StringUtils.isEmpty(dataDtl.getCloaiVthh()) ? null : mapVthh.get(dataDtl.getCloaiVthh()));
                dataDtl.setTenPthucTtoan(StringUtils.isEmpty(dataDtl.getPthucTtoan()) ? null : mapPhuongThucTt.get(dataDtl.getPthucTtoan()));
                dataDtl.setChildren(qdPhanLo);
            }
            data.setMapVthh(mapVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBdg.TABLE_NAME));
            data.setCanCuPhapLy(canCuPhapLy);
            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH"));
            data.setFileDinhKem(fileDinhKem);
            data.setChildren(qdDtl.stream().filter(isDieuChinh -> !isDieuChinh.getIsDieuChinh()).collect(Collectors.toList()));
        }
        return list;
    }

    public XhQdPdKhBdg detail(Long id) throws Exception {
        if (id == null) throw new Exception("Tham số không hợp lệ.");
        List<XhQdPdKhBdg> details = detail(Collections.singletonList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        XhQdPdKhBdg data = xhQdPdKhBdgRepository.findById(idSearchReq.getId())
                .orElseThrow(() -> new Exception("Bản ghi không tồn tại"));
        if (!data.getTrangThai().equals(Contains.DANG_NHAP_DU_LIEU)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái đang nhập dữ liệu");
        }
        List<XhQdPdKhBdgDtl> dtl = xhQdPdKhBdgDtlRepository.findAllByIdHdr(data.getId());
        dtl.forEach(dataDtl -> {
            List<XhQdPdKhBdgPl> phanLo = xhQdPdKhBdgPlRepository.findAllByIdDtl(dataDtl.getId());
            phanLo.forEach(dataPhanLo -> xhQdPdKhBdgPlDtlRepository.deleteAllByIdPhanLo(dataPhanLo.getId()));
            xhQdPdKhBdgPlRepository.deleteAllByIdDtl(dataDtl.getId());
        });
        xhQdPdKhBdgDtlRepository.deleteAllByIdHdr(data.getId());
        xhQdPdKhBdgRepository.delete(data);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBdg.TABLE_NAME);
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
        fileDinhKemService.delete(data.getId(), tableNames);
        if (data.getPhanLoai().equals("TH")) {
            xhThopDxKhBdgRepository.findById(data.getIdThHdr()).ifPresent(tongHop -> {
                tongHop.setIdQdPd(null);
                tongHop.setSoQdPd(null);
                tongHop.setTrangThai(Contains.CHUATAO_QD);
                xhThopDxKhBdgRepository.save(tongHop);
            });
        } else {
            xhDxKhBanDauGiaRepository.findById(data.getIdTrHdr()).ifPresent(deXuat -> {
                deXuat.setIdSoQdPd(null);
                deXuat.setSoQdPd(null);
                deXuat.setNgayKyQd(null);
                deXuat.setTrangThaiTh(Contains.CHUATONGHOP);
                xhDxKhBanDauGiaRepository.save(deXuat);
            });
        }
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdPdKhBdg> list = xhQdPdKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Không tìm thấy bản ghi để xóa");
        }
        for (XhQdPdKhBdg hdr : list) {
            if (!hdr.getTrangThai().equals(Contains.DANG_NHAP_DU_LIEU)) {
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái đang nhập dữ liệu");
            }
        }
        List<Long> idQdHdr = list.stream().map(XhQdPdKhBdg::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgDtl> listDtl = xhQdPdKhBdgDtlRepository.findByIdHdrIn(idQdHdr);
        List<Long> idDtl = listDtl.stream().map(XhQdPdKhBdgDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPl> listPhanLo = xhQdPdKhBdgPlRepository.findByIdDtlIn(idDtl);
        List<Long> idPhanLo = listPhanLo.stream().map(XhQdPdKhBdgPl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPlDtl> listPhanLoDtl = xhQdPdKhBdgPlDtlRepository.findByIdPhanLoIn(idPhanLo);
        xhQdPdKhBdgPlDtlRepository.deleteAll(listPhanLoDtl);
        xhQdPdKhBdgPlRepository.deleteAll(listPhanLo);
        xhQdPdKhBdgDtlRepository.deleteAll(listDtl);
        xhQdPdKhBdgRepository.deleteAll(list);
        Set<String> tableNames = new HashSet<>();
        tableNames.add(XhQdPdKhBdg.TABLE_NAME);
        tableNames.add(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), tableNames);
        List<Long> listIdThHdr = list.stream().map(XhQdPdKhBdg::getIdThHdr).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(listIdThHdr)) {
            List<XhThopDxKhBdg> tongHopHdr = xhThopDxKhBdgRepository.findByIdIn(listIdThHdr);
            tongHopHdr.forEach(item -> {
                item.setIdQdPd(null);
                item.setSoQdPd(null);
                item.setTrangThai(Contains.CHUATAO_QD);
            });
            xhThopDxKhBdgRepository.saveAll(tongHopHdr);
        }
        List<Long> listIdTrHdr = list.stream().map(XhQdPdKhBdg::getIdTrHdr).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(listIdTrHdr)) {
            List<XhDxKhBanDauGia> deXuatHdr = xhDxKhBanDauGiaRepository.findByIdIn(listIdTrHdr);
            deXuatHdr.forEach(item -> {
                item.setIdSoQdPd(null);
                item.setSoQdPd(null);
                item.setNgayKyQd(null);
                item.setTrangThaiTh(Contains.CHUATONGHOP);
            });
            xhDxKhBanDauGiaRepository.saveAll(deXuatHdr);
        }
    }

    public XhQdPdKhBdg approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        XhQdPdKhBdg data = xhQdPdKhBdgRepository.findById(Long.valueOf(statusReq.getId()))
                .orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DANG_NHAP_DU_LIEU:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(statusReq.getTrangThai());
        if (statusReq.getTrangThai().equals(Contains.BAN_HANH)) {
            handleBanHanhStatus(data);
            this.cloneProject(data.getId());
        }
        return xhQdPdKhBdgRepository.save(data);
    }

    private void handleBanHanhStatus(XhQdPdKhBdg data) throws Exception {
        if (data.getPhanLoai().equals("TH")) {
            XhThopDxKhBdg optionalTh = xhThopDxKhBdgRepository.findById(data.getIdThHdr())
                    .orElseThrow(() -> new Exception("Tổng hợp đề xuất kế hoạch không tồn tại"));
            if (optionalTh.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Tổng hợp đề xuất kế hoạch đã được quyết định");
            }
            optionalTh.setTrangThai(Contains.DABANHANH_QD);
            List<XhDxKhBanDauGia> listDx = xhDxKhBanDauGiaRepository.findAllByIdThop(optionalTh.getId());
            listDx.forEach(dataDx -> {
                dataDx.setIdSoQdPd(data.getId());
                dataDx.setSoQdPd(data.getSoQdPd());
                dataDx.setNgayKyQd(data.getNgayKyQd());
                dataDx.setTrangThaiTh(Contains.DABANHANH_QD);
            });
            xhDxKhBanDauGiaRepository.saveAll(listDx);
            xhThopDxKhBdgRepository.save(optionalTh);
        } else {
            XhDxKhBanDauGia optionalDx = xhDxKhBanDauGiaRepository.findById(data.getIdTrHdr())
                    .orElseThrow(() -> new Exception("Đề xuất kế hoạch không tồn tại"));
            if (optionalDx.getTrangThai().equals(Contains.DABANHANH_QD)) {
                throw new Exception("Đề xuất kế hoạch này đã được quyết định");
            }
            optionalDx.setTrangThaiTh(Contains.DABANHANH_QD);
            xhDxKhBanDauGiaRepository.save(optionalDx);
        }
    }

    private void cloneProject(Long idClone) throws Exception {
        XhQdPdKhBdg hdr = this.detail(idClone);
        XhQdPdKhBdg hdrClone = new XhQdPdKhBdg();
        BeanUtils.copyProperties(hdr, hdrClone);
        hdrClone.setId(null);
        hdrClone.setLastest(true);
        hdrClone.setIdGoc(hdr.getId());
        xhQdPdKhBdgRepository.save(hdrClone);
        for (XhQdPdKhBdgDtl dx : hdr.getChildren()) {
            XhQdPdKhBdgDtl dxClone = new XhQdPdKhBdgDtl();
            BeanUtils.copyProperties(dx, dxClone);
            dxClone.setId(null);
            dxClone.setLastest(true);
            dxClone.setIdHdr(hdrClone.getId());
            xhQdPdKhBdgDtlRepository.save(dxClone);
            for (XhQdPdKhBdgPl pl : dxClone.getChildren()) {
                XhQdPdKhBdgPl plClone = new XhQdPdKhBdgPl();
                BeanUtils.copyProperties(pl, plClone);
                plClone.setId(null);
                plClone.setIdDtl(dxClone.getId());
                xhQdPdKhBdgPlRepository.save(plClone);
                for (XhQdPdKhBdgPlDtl plDtl : pl.getChildren()) {
                    XhQdPdKhBdgPlDtl plDtlClone = new XhQdPdKhBdgPlDtl();
                    BeanUtils.copyProperties(plDtl, plDtlClone);
                    plDtlClone.setId(null);
                    plDtlClone.setIdPhanLo(plClone.getId());
                    xhQdPdKhBdgPlDtlRepository.save(plDtlClone);
                }
            }
        }
    }

    public void export(CustomUserDetails currentUser, XhQdPdKhBdgReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBdg> page = this.searchPage(currentUser, req);
        List<XhQdPdKhBdg> data = page.getContent();
        String title = " Danh sách quyết định phê duyệt kế hoạch mua trưc tiếp";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KH BĐG", "ngày ký QĐ", "Trích yếu", "Số KH/ Tờ trình", "Mã tổng hợp", "Loại hàng hóa", "Chủng loại hàng hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Trạng Thái"};
        String fileName = "danh-sach-dx-pd-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBdg pduyet = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = pduyet.getNam();
            objs[2] = pduyet.getSoQdPd();
            objs[3] = pduyet.getNgayKyQd();
            objs[4] = pduyet.getTrangThai();
            objs[5] = pduyet.getSoTrHdr();
            objs[6] = pduyet.getIdThHdr();
            objs[7] = pduyet.getTenCloaiVthh();
            objs[8] = pduyet.getSlDviTsan();
            objs[9] = null;
            objs[10] = pduyet.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public Page<XhQdPdKhBdgDtl> searchPageDtl(CustomUserDetails currentUser, XhQdPdKhBdgDtlReq req) throws Exception {
        String dvql = currentUser.getDvql();
        Integer lastest = 1;
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql.substring(0, 4));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql);
            req.setLastest(lastest);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdPdKhBdgDtl> search = xhQdPdKhBdgDtlRepository.searchDtl(req, pageable);
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        search.getContent().forEach(data -> {
            try {
                mapDmucDvi.computeIfPresent(data.getMaDvi(), (key, objDonVi) -> {
                    data.setTenDvi(objDonVi.get("tenDvi").toString());
                    return objDonVi;
                });
                mapDmucVthh.computeIfPresent(data.getLoaiVthh(), (key, value) -> {
                    data.setTenLoaiVthh(value);
                    return value;
                });
                mapDmucVthh.computeIfPresent(data.getCloaiVthh(), (key, value) -> {
                    data.setTenCloaiVthh(value);
                    return value;
                });
                if (data.getTrangThai() != null) {
                    data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
                }
                if (data.getSoQdDc() != null) {
                    data.setXhQdDchinhKhBdgHdr(xhQdDchinhKhBdgHdrRepository.findById(data.getIdHdr()).get());
                } else {
                    data.setXhQdPdKhBdg(xhQdPdKhBdgRepository.findById(data.getIdHdr()).get());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    public List<XhQdPdKhBdgDtl> detailDtl(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        List<XhQdPdKhBdgDtl> allByIdDtl = xhQdPdKhBdgDtlRepository.findAllById(ids);
        if (DataUtils.isNullOrEmpty(allByIdDtl)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapPhuongThucTt = getListDanhMucChung("PHUONG_THUC_TT");
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        allByIdDtl.forEach(dataDtl -> {
            dataDtl.setTenDvi(mapDmucDvi.getOrDefault(dataDtl.getMaDvi(), null));
            dataDtl.setTenLoaiVthh(mapVthh.getOrDefault(dataDtl.getLoaiVthh(), null));
            dataDtl.setTenCloaiVthh(mapVthh.getOrDefault(dataDtl.getCloaiVthh(), null));
            dataDtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dataDtl.getTrangThai()));
            dataDtl.setTenPthucTtoan(mapPhuongThucTt.getOrDefault(dataDtl.getPthucTtoan(), null));
            List<XhQdPdKhBdgPl> phanLo = xhQdPdKhBdgPlRepository.findAllByIdDtl(dataDtl.getId());
            phanLo.forEach(dataPhanLo -> {
                dataPhanLo.setTenDvi(mapDmucDvi.getOrDefault(dataPhanLo.getMaDvi(), null));
                List<XhQdPdKhBdgPlDtl> phanLoDtl = xhQdPdKhBdgPlDtlRepository.findAllByIdPhanLo(dataPhanLo.getId());
                phanLoDtl.forEach(dataPhanLoDtl -> {
                    dataPhanLoDtl.setTenDiemKho(mapDmucDvi.getOrDefault(dataPhanLoDtl.getMaDiemKho(), null));
                    dataPhanLoDtl.setTenNhaKho(mapDmucDvi.getOrDefault(dataPhanLoDtl.getMaNhaKho(), null));
                    dataPhanLoDtl.setTenNganKho(mapDmucDvi.getOrDefault(dataPhanLoDtl.getMaNganKho(), null));
                    dataPhanLoDtl.setTenLoKho(mapDmucDvi.getOrDefault(dataPhanLoDtl.getMaLoKho(), null));
                });
                dataPhanLo.setChildren(phanLoDtl);
            });
            List<XhTcTtinBdgHdr> xhTcTtinBdgHdrs = xhTcTtinBdgHdrRepository.findByIdQdPdDtlOrderByLanDauGia(dataDtl.getId());
            dataDtl.setListTtinDg(xhTcTtinBdgHdrs);
            dataDtl.setChildren(phanLo);
            if (dataDtl.getSoQdDc() != null) {
                XhQdDchinhKhBdgHdr dieuChinh = xhQdDchinhKhBdgHdrRepository.findById(dataDtl.getIdHdr()).orElse(null);
                if (dieuChinh != null) {
                    dieuChinh.setMapVthh(mapVthh);
                    dieuChinh.setMapKieuNx(mapKieuNx);
                    dieuChinh.setMapLoaiHinhNx(mapLoaiHinhNx);
                    dataDtl.setXhQdDchinhKhBdgHdr(dieuChinh);
                }
            } else {
                XhQdPdKhBdg quyetDinh = xhQdPdKhBdgRepository.findById(dataDtl.getIdHdr()).orElse(null);
                if (quyetDinh != null) {
                    quyetDinh.setMapVthh(mapVthh);
                    quyetDinh.setMapKieuNx(mapKieuNx);
                    quyetDinh.setMapLoaiHinhNx(mapLoaiHinhNx);
                    dataDtl.setXhQdPdKhBdg(quyetDinh);
                }
            }
        });
        return allByIdDtl;
    }

    public XhQdPdKhBdgDtl approveDtl(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null || StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Bad request.");
        }
        Long dtlId = Long.valueOf(statusReq.getId());
        Optional<XhQdPdKhBdgDtl> optional = xhQdPdKhBdgDtlRepository.findById(dtlId);
        XhQdPdKhBdgDtl dataDtl = optional.orElseThrow(() -> new Exception("Không tìm thấy dữ liệu"));
        String status = statusReq.getTrangThai() + dataDtl.getTrangThai();
        if (!status.equals(Contains.DA_HOAN_THANH + Contains.DANG_THUC_HIEN)) {
            throw new Exception("Phê duyệt không thành công");
        }
        dataDtl.setTrangThai(statusReq.getTrangThai());
        return xhQdPdKhBdgDtlRepository.save(dataDtl);
    }

    public void exportDtl(CustomUserDetails currentUser, XhQdPdKhBdgDtlReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBdgDtl> page = this.searchPageDtl(currentUser, req);
        List<XhQdPdKhBdgDtl> dataDtl = page.getContent();
        String title = "Danh sách quản lý thông tin bán đấu giá";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KHBĐG", "Số QĐ ĐC KHBĐG",
                "Số QĐ PD KQBĐG", "Số KH/đề xuất", "Ngày QĐ PD KQBĐG",
                "Tổng số ĐV tài sản", "Số ĐV tài sản ĐG thành công", "Số ĐV tài sản ĐG không thành công",
                "Thời hạn giao nhận hàng", "Chủng loại hàng hóa", "Trạng thái thực hiện", "Kết quả đấu giá"};
        String fileName = "danh-sach-quan-ly-thong-tin-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < dataDtl.size(); i++) {
            XhQdPdKhBdgDtl dtl = dataDtl.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dtl.getNam();
            objs[2] = dtl.getSoQdPd();
            objs[3] = dtl.getSoQdDc();
            objs[4] = dtl.getSoQdPdKqBdg();
            objs[5] = dtl.getSoDxuat();
            objs[6] = dtl.getNgayKyQdPdKqBdg();
            objs[7] = dtl.getSlDviTsan();
            objs[8] = dtl.getSoDviTsanThanhCong();
            objs[9] = dtl.getSoDviTsanKhongThanh();
            objs[10] = null;
            objs[11] = dtl.getTenCloaiVthh();
            objs[12] = dtl.getTenTrangThai();
            objs[13] = dtl.getKetQuaDauGia();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try (FileInputStream inputStream = new FileInputStream(baseReportFolder + "/bandaugia/Quyết định phê duyệt kế hoạch bán đấu giá.docx")) {
            XhQdPdKhBdg detail = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException | XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}
