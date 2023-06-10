package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.pheduyet;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhQdPdKhBdgServiceImpl extends BaseServiceImpl implements XhQdPdKhBdgService {

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
    private XhThopDxKhBdgDtlRepository xhThopDxKhBdgDtlRepository;

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;

    @Override
    public Page<XhQdPdKhBdg> searchPage(XhQdPdKhBdgReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdPdKhBdg> data = xhQdPdKhBdgRepository.searchPage(
                req,
                pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:mapDmucVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:mapDmucVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            List<XhQdPdKhBdgDtl> listDtl = xhQdPdKhBdgDtlRepository.findAllByIdQdHdr(f.getId());
            listDtl.forEach(x -> {
                x.setTenDvi(mapDmucVthh.get(x.getMaDvi()));
            });
            f.setChildren(listDtl);
        });
        return data;
    }

    @Override
    public XhQdPdKhBdg create(XhQdPdKhBdgReq req) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (!StringUtils.isEmpty(req.getSoQdPd())) {
            List<XhQdPdKhBdg> checkSoQd = xhQdPdKhBdgRepository.findBySoQdPd(req.getSoQdPd());
            if (!checkSoQd.isEmpty()) throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
        }

        XhThopDxKhBdg dataTh = new XhThopDxKhBdg();
        XhDxKhBanDauGia dataDx = new XhDxKhBanDauGia();
        if (req.getPhanLoai().equals("TH")) {
            Optional<XhThopDxKhBdg> optionalTh = xhThopDxKhBdgRepository.findById(req.getIdThHdr());
            if (!optionalTh.isPresent()) {
                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
            }
            dataTh = optionalTh.get();
        } else {
            Optional<XhDxKhBanDauGia> optionalDX = xhDxKhBanDauGiaRepository.findById(req.getIdTrHdr());
            if (!optionalDX.isPresent()) {
                throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
            }
            dataDx = optionalDX.get();
        }

        XhQdPdKhBdg data = new XhQdPdKhBdg();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(getUser().getId());
        data.setTrangThai(Contains.DUTHAO);
        data.setLastest(false);
        data.setMaDvi(userInfo.getDvql());

        XhQdPdKhBdg created = xhQdPdKhBdgRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdPdKhBdg.TABLE_NAME+ "_BAN_HANH");
            created.setFileDinhKem(fileDinhKem);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdPdKhBdg.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        // Update trạng thái tổng hợp và đề xuất
        if (req.getPhanLoai().equals("TH")) {
            dataTh.setTrangThai(Contains.DADUTHAO_QD);
            dataTh.setSoQdPd(created.getSoQdPd());
            dataTh.setIdQdPd(created.getId());
            xhThopDxKhBdgRepository.save(dataTh);
        } else {
            dataDx.setTrangThaiTh(Contains.DADUTHAO_QD);
            xhDxKhBanDauGiaRepository.save(dataDx);
        }
        saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhQdPdKhBdgReq req, Long idHdr) {
        xhQdPdKhBdgDtlRepository.deleteAllByIdQdHdr(idHdr);
        for (XhQdPdKhBdgDtlReq dtl : req.getChildren()) {
            XhQdPdKhBdgDtl qd = new XhQdPdKhBdgDtl();
            BeanUtils.copyProperties(dtl, qd, "id");
            qd.setIdQdHdr(idHdr);
            qd.setLoaiVthh(req.getLoaiVthh());
            qd.setCloaiVthh(req.getCloaiVthh());
            qd.setTrangThai(Contains.CHUACAPNHAT);
            xhQdPdKhBdgDtlRepository.save(qd);
            xhQdPdKhBdgPlRepository.deleteByIdQdDtl(dtl.getId());
            for (XhQdPdKhBdgPlReq plReq : dtl.getChildren()) {
                XhQdPdKhBdgPl pl = new XhQdPdKhBdgPl();
                BeanUtils.copyProperties(plReq, pl, "id");
                pl.setIdQdDtl(qd.getId());
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

    @Override
    public XhQdPdKhBdg update(XhQdPdKhBdgReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhQdPdKhBdg> optional = xhQdPdKhBdgRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Quyết định phê duyệt kế hoạch bán đấu giá không tồn tại");

        if (!StringUtils.isEmpty(req.getSoQdPd())) {
            if (!req.getSoQdPd().equals(optional.get().getSoQdPd())) {
                List<XhQdPdKhBdg> checkSoQd = xhQdPdKhBdgRepository.findBySoQdPd(req.getSoQdPd());
                if (!checkSoQd.isEmpty()) throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
            }
        }

        XhQdPdKhBdg data = optional.get();
        if (req.getPhanLoai().equals("TH")) {
            Optional<XhThopDxKhBdg> optionalTh = xhThopDxKhBdgRepository.findById(req.getIdThHdr());
            XhThopDxKhBdg dataTh = optionalTh.get();
            if (!optionalTh.isPresent()) {
                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
            }else {
                dataTh.setSoQdPd(req.getSoQdPd());
                dataTh.setIdQdPd(data.getId());
                xhThopDxKhBdgRepository.save(dataTh);
            }
        }
//        else {
//            Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(req.getIdTrHdr());
//            XhDxKhBanDauGia dataTr = byId.get();
//            if (!byId.isPresent()) {
//                throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
//            }else {
//                dataTr.setSoQdPd(req.getSoQdPd());
//                dataTr.setIdSoQdPd(dataDB.getId());
//                xhDxKhBanDauGiaRepository.save(dataTr);
//            }
//        }

        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(getUser().getId());

        XhQdPdKhBdg created = xhQdPdKhBdgRepository.save(data);
        fileDinhKemService.delete(created.getId(), Collections.singleton(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH"));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH");
        created.setFileDinhKem(fileDinhKem);

        fileDinhKemService.delete(created.getId(), Collections.singleton(XhQdPdKhBdg.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdPdKhBdg.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);

        this.saveDetail(req, created.getId());
        return created;
    }

    @Override
    public XhQdPdKhBdg detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhQdPdKhBdg> optional = xhQdPdKhBdgRepository.findById(id);
        if (!optional.isPresent()) throw new UnsupportedOperationException("Quyết định phê duyệt kế hoạch bán đấu giá không tồn tại");

        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        XhQdPdKhBdg data = optional.get();

        List<XhQdPdKhBdgDtl> dataDtlList = new ArrayList<>();
        for (XhQdPdKhBdgDtl dataDtl : xhQdPdKhBdgDtlRepository.findAllByIdQdHdr(id)) {
            List<XhQdPdKhBdgPl> dataDtlPloList = new ArrayList<>();
            for (XhQdPdKhBdgPl dataDtlPlo : xhQdPdKhBdgPlRepository.findByIdQdDtl(dataDtl.getId())) {
                List<XhQdPdKhBdgPlDtl> xhQdPdKhBdgPlDtlList = xhQdPdKhBdgPlDtlRepository.findByIdPhanLo(dataDtlPlo.getId());
                xhQdPdKhBdgPlDtlList.forEach(f -> {
                    f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())?null:mapDmucDvi.get(f.getMaDiemKho()));
                    f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())?null:mapDmucDvi.get(f.getMaNhaKho()));
                    f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())?null:mapDmucDvi.get(f.getMaNganKho()));
                    f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:mapDmucDvi.get(f.getMaLoKho()));
                    f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:mapDmucVthh.get(f.getLoaiVthh()));
                    f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:mapDmucVthh.get(f.getCloaiVthh()));
                    this.donGiaDuocDuyet(data, dataDtl, f);
                });
                dataDtlPlo.setTenDvi(StringUtils.isEmpty(dataDtlPlo.getMaDvi())?null:mapDmucDvi.get(dataDtlPlo.getMaDvi()));
                dataDtlPlo.setChildren(xhQdPdKhBdgPlDtlList);
                dataDtlPloList.add(dataDtlPlo);
            };
            dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi())?null:mapDmucDvi.get(dataDtl.getMaDvi()));
            dataDtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dataDtl.getTrangThai()));
            dataDtl.setChildren(dataDtlPloList);
            dataDtlList.add(dataDtl);
        }
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:mapDmucDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:mapDmucVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:mapDmucVthh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBdg.TABLE_NAME+ "_BAN_HANH"));
        data.setFileDinhKem(fileDinhKem);

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBdg.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);

        data.setChildren(dataDtlList);
        return data;
    }

    void donGiaDuocDuyet(XhQdPdKhBdg data, XhQdPdKhBdgDtl dtl, XhQdPdKhBdgPlDtl f) {
        BigDecimal donGiaDuocDuyet = BigDecimal.ZERO;
        if(data.getLoaiVthh().startsWith("02")){
            donGiaDuocDuyet = xhQdPdKhBdgDtlRepository.getDonGiaVatVt(data.getCloaiVthh(), data.getNam());
            if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(dtl.getTongSoLuong()) && !StringUtils.isEmpty(dtl.getKhoanTienDatTruoc())){
                BigDecimal tongKhoanTienDtTheoDgiaDd = dtl.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(dtl.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                f.setDonGiaDuocDuyet(donGiaDuocDuyet);
                dtl.setTongKhoanTienDtTheoDgiaDd(tongKhoanTienDtTheoDgiaDd);
            }
        }else {
            donGiaDuocDuyet = xhQdPdKhBdgDtlRepository.getDonGiaVatLt(data.getCloaiVthh(), dtl.getMaDvi(), data.getNam());
            if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(dtl.getTongSoLuong()) && !StringUtils.isEmpty(dtl.getKhoanTienDatTruoc())){
                BigDecimal tongKhoanTienDtTheoDgiaDd = dtl.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(dtl.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                f.setDonGiaDuocDuyet(donGiaDuocDuyet);
                dtl.setTongKhoanTienDtTheoDgiaDd(tongKhoanTienDtTheoDgiaDd);
            }
        }
    }

    @Override
    public XhQdPdKhBdg approve(XhQdPdKhBdgReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhQdPdKhBdg dataDB = detail(req.getId());
        String status = req.getTrangThai() + dataDB.getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                dataDB.setNgayPduyet(LocalDate.now());
                dataDB.setNguoiPduyetId(getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        dataDB.setTrangThai(req.getTrangThai());
        if (req.getTrangThai().equals(Contains.BAN_HANH)) {
            if (dataDB.getPhanLoai().equals("TH")) {
                Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(dataDB.getIdThHdr());
                if (qOptional.isPresent()) {
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
                    }
                    xhThopDxKhBdgRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
                    List<XhThopDxKhBdgDtl> dtlList = xhThopDxKhBdgDtlRepository.findByIdThopHdr(dataDB.getIdThHdr());
                    for (XhThopDxKhBdgDtl dtl : dtlList){
                        Optional<XhDxKhBanDauGia> optionalDx = xhDxKhBanDauGiaRepository.findById(dtl.getIdDxHdr());
                        if (optionalDx.isPresent()){
                            optionalDx.get().setSoQdPd(dataDB.getSoQdPd());
                            optionalDx.get().setIdSoQdPd(dataDB.getId());
                            optionalDx.get().setNgayKyQd(dataDB.getNgayPduyet());
                            xhDxKhBanDauGiaRepository.save(optionalDx.get());
                        }
                    }
                } else {
                    throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
                }

            } else {
                Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
                if (qOptional.isPresent()) {
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Đề xuất này đã được quyết định");
                    }
                    qOptional.get().setTrangThaiTh(Contains.DABANHANH_QD);
                    qOptional.get().setSoQdPd(dataDB.getSoQdPd());
                    qOptional.get().setIdSoQdPd(dataDB.getId());
                    qOptional.get().setNgayKyQd(dataDB.getNgayPduyet());
                    xhDxKhBanDauGiaRepository.save(qOptional.get());
                } else {
                    throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
                }
            }
            this.cloneProject(dataDB.getId());
        }
        XhQdPdKhBdg createCheck = xhQdPdKhBdgRepository.save(dataDB);
        return createCheck;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");
        }
        Optional<XhQdPdKhBdg> optional = xhQdPdKhBdgRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
        }
        List<XhQdPdKhBdgDtl> xhQdPdKhBdgDtl = xhQdPdKhBdgDtlRepository.findAllByIdQdHdr(optional.get().getId());
        if (!CollectionUtils.isEmpty(xhQdPdKhBdgDtl)) {
            for (XhQdPdKhBdgDtl dtl : xhQdPdKhBdgDtl) {
                List<XhQdPdKhBdgPl> byIdQdDtl = xhQdPdKhBdgPlRepository.findByIdQdDtl(dtl.getId());
                for (XhQdPdKhBdgPl phanLo : byIdQdDtl) {
                    xhQdPdKhBdgPlDtlRepository.deleteAllByIdPhanLo(phanLo.getId());
                }
                xhQdPdKhBdgPlRepository.deleteByIdQdDtl(dtl.getId());
            }
            xhQdPdKhBdgDtlRepository.deleteAll(xhQdPdKhBdgDtl);
        }
        xhQdPdKhBdgRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhQdPdKhBdg.TABLE_NAME));
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhQdPdKhBdg.TABLE_NAME + "_BAN_HANH"));
        if (optional.get().getPhanLoai().equals("TH")) {
            Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(optional.get().getIdThHdr());
            XhThopDxKhBdg bdgTh = qOptionalTh.get();
            bdgTh.setIdQdPd(null);
            bdgTh.setSoQdPd(null);
            bdgTh.setTrangThai(NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
            xhThopDxKhBdgRepository.save(qOptionalTh.get());
//            xhThopDxKhBdgRepository.updateTrangThai(optional.get().getIdThHdr(), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
        } else {
            Optional<XhDxKhBanDauGia> qOptionalTr = xhDxKhBanDauGiaRepository.findById(optional.get().getIdTrHdr());
            XhDxKhBanDauGia bdgDx = qOptionalTr.get();
//            bdgDx.setIdSoQdPd(null);
//            bdgDx.setSoQdPd(null);
            bdgDx.setTrangThaiTh(NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
            xhDxKhBanDauGiaRepository.save(qOptionalTr.get());
//            xhDxKhBanDauGiaRepository.updateStatusTh(optional.get().getIdTrHdr(), NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
        }
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)) {
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }
        List<XhQdPdKhBdg> listHdr = xhQdPdKhBdgRepository.findAllByIdIn(listMulti);
        if (listHdr.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (XhQdPdKhBdg bdg : listHdr) {
            if (!bdg.getTrangThai().equals(Contains.DUTHAO)) {
                throw new Exception("Chỉ thực hiện xóa bản nghi ở trạng thái dự thảo");
            } else {
                this.delete(bdg.getId());
            }
        }
    }

    @Override
    public void export(XhQdPdKhBdgReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBdg> page = this.searchPage(req);
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

//    public void validateData(XhQdPdKhBdg objHdr) throws Exception {
//        for (XhQdPdKhBdgDtl dtl : objHdr.getChildren()) {
//            for (XhQdPdKhBdgPl dsgthau : dtl.getChildren()) {
//                BigDecimal aLong = xhDxKhBanDauGiaRepository.countSLDalenKh(objHdr.getNam(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(), NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
//                BigDecimal soLuongTotal = aLong.add(dsgthau.getSoLuong());
//                BigDecimal nhap = keHoachService.getChiTieuNhapXuat(objHdr.getNam(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(), "NHAP");
//                if (soLuongTotal.compareTo(nhap) > 0) {
//                    throw new Exception(dsgthau.getTenDvi() + "Đã nhập quá số lượng chỉ tiêu vui lòng nhập lại");
//                }
//            }
//        }
//    }

    public XhQdPdKhBdgDtl detailDtl(Long ids) throws Exception {
        if (ObjectUtils.isEmpty(ids)) throw new Exception("Không tồn tại bản ghi");

        Optional<XhQdPdKhBdgDtl> optional = xhQdPdKhBdgDtlRepository.findById(ids);
        if (!optional.isPresent()) throw new UnsupportedOperationException("Không tồn tại bản ghi");

        XhQdPdKhBdgDtl data = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        XhQdPdKhBdg hdr = xhQdPdKhBdgRepository.findById(data.getIdQdHdr()).get();
        data.setXhQdPdKhBdg(hdr);
        List<XhQdPdKhBdgPl> xhQdPdKhBdgPlList = new ArrayList<>();
        for (XhQdPdKhBdgPl dsg : xhQdPdKhBdgPlRepository.findByIdQdDtl(data.getId())) {
            List<XhQdPdKhBdgPlDtl> xhQdPdKhBdgPlDtlList = xhQdPdKhBdgPlDtlRepository.findByIdPhanLo(dsg.getId());
            xhQdPdKhBdgPlDtlList.forEach(f -> {
                f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())?null:mapDmucDvi.get(f.getMaDiemKho()));
                f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())?null:mapDmucDvi.get(f.getMaNhaKho()));
                f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())?null:mapDmucDvi.get(f.getMaNganKho()));
                f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:mapDmucDvi.get(f.getMaLoKho()));
                this.donGiaDuocDuyet(hdr, data, f);
            });
            dsg.setTenDvi(StringUtils.isEmpty(dsg.getMaDvi())?null:mapDmucDvi.get(dsg.getMaDvi()));
            dsg.setChildren(xhQdPdKhBdgPlDtlList);
            xhQdPdKhBdgPlList.add(dsg);
        };
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:mapDmucDvi.get(data.getMaDvi()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setChildren(xhQdPdKhBdgPlList);

        List<XhTcTtinBdgHdr> byIdQdPdDtl = xhTcTtinBdgHdrRepository.findByIdQdPdDtlOrderByLanDauGia(ids);
        data.setListTtinDg(byIdQdPdDtl);
        return data;
    }

      public XhQdPdKhBdgDtl approveDtl(XhQdPdKhBdgReq stReq) throws Exception {
          if (ObjectUtils.isEmpty(stReq.getId())) {
              throw new Exception("Không tồn tại bản ghi");
          }
          Optional<XhQdPdKhBdgDtl> qOptional = xhQdPdKhBdgDtlRepository.findById(stReq.getId());
          if (!qOptional.isPresent()) {
              throw new UnsupportedOperationException("Không tồn tại bản ghi");
          }
          XhQdPdKhBdgDtl data = qOptional.get();
          String status = stReq.getTrangThai() + data.getTrangThai();
          switch (status) {
              case Contains.HOANTHANHCAPNHAT + Contains.DANGCAPNHAT:
                  break;
              default:
                  throw new Exception("Phê duyệt không thành công");
          }
          data.setTrangThai(stReq.getTrangThai());
          xhQdPdKhBdgDtlRepository.save(data);
          return data;
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
            dxClone.setIdQdHdr(hdrClone.getId());
            xhQdPdKhBdgDtlRepository.save(dxClone);
            for (XhQdPdKhBdgPl pl : dxClone.getChildren()) {
                XhQdPdKhBdgPl plClone = new XhQdPdKhBdgPl();
                BeanUtils.copyProperties(pl, plClone);
                plClone.setId(null);
                plClone.setIdQdDtl(dxClone.getId());
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
}
