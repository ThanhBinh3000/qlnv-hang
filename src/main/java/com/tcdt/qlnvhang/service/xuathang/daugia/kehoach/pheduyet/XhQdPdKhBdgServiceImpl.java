package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet.XhQdPdKhBdgReq;
import com.tcdt.qlnvhang.service.feign.KeHoachService;
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
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    private KeHoachService keHoachService;

    @Autowired
    private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;

    @Override
    public Page<XhQdPdKhBdg> searchPage(XhQdPdKhBdgReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdPdKhBdg> data = xhQdPdKhBdgRepository.searchPage(req.getNam(), req.getSoQdPd(), req.getTrichYeu(), Contains.convertDateToString(req.getNgayKyQdTu()), Contains.convertDateToString(req.getNgayKyQdDen()), req.getSoTrHdr(), req.getLoaiVthh(), req.getTrangThai(), req.getLastest(), req.getMaDvi(), pageable);

        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");

        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
            List<XhQdPdKhBdgDtl> listDtl = xhQdPdKhBdgDtlRepository.findAllByIdQdHdr(f.getId());
            listDtl.forEach(x -> {
                x.setTenDvi(listDanhMucDvi.get(x.getMaDvi()));
            });
            f.setChildren(listDtl);
        });
        return data;
    }

    @Override
    public XhQdPdKhBdg create(XhQdPdKhBdgReq req) throws Exception {

        if (!StringUtils.isEmpty(req.getSoQdPd())) {
            List<XhQdPdKhBdg> checkSoQd = xhQdPdKhBdgRepository.findBySoQdPd(req.getSoQdPd());
            if (!checkSoQd.isEmpty()) {
                throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
            }
        }
        XhThopDxKhBdg dataTh = new XhThopDxKhBdg();
        XhDxKhBanDauGia dataDx = new XhDxKhBanDauGia();
        if (req.getPhanLoai().equals("TH")) {
            Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(req.getIdThHdr());
            if (!qOptionalTh.isPresent()) {
                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
            }
            dataTh = qOptionalTh.get();
        } else {
            Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(req.getIdTrHdr());
            if (!byId.isPresent()) {
                throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
            }
            dataDx = byId.get();
        }

        XhQdPdKhBdg dataMap = new XhQdPdKhBdg();
        BeanUtils.copyProperties(req, dataMap, "id");
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNguoiTaoId(getUser().getId());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setLastest(false);
        dataMap.setMaDvi(getUser().getDvql());
        XhQdPdKhBdg created = xhQdPdKhBdgRepository.save(dataMap);

        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(req.getFileDinhKem()), created.getId(), XhQdPdKhBdg.TABLE_NAME);
            created.setFileDinhKem(fileDinhKem.get(0));
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdPdKhBdg.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        // Update trạng thái tổng hợp dxkhclnt
        if (req.getPhanLoai().equals("TH")) {
            dataTh.setTrangThai(Contains.DADUTHAO_QD);
            dataTh.setSoQdPd(dataMap.getSoQdPd());
            dataTh.setIdQdPd(dataMap.getId());
            xhThopDxKhBdgRepository.save(dataTh);
        } else {
            dataDx.setTrangThaiTh(Contains.DADUTHAO_QD);
            dataDx.setSoQdPd(dataMap.getSoQdPd());
            dataDx.setIdSoQdPd(dataMap.getId());
            xhDxKhBanDauGiaRepository.save(dataDx);
        }
        saveDetail(req, dataMap.getId());
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
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhQdPdKhBdg> qOptional = xhQdPdKhBdgRepository.findById(req.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if (!StringUtils.isEmpty(req.getSoQdPd())) {
            if (!req.getSoQdPd().equals(qOptional.get().getSoQdPd())) {
                List<XhQdPdKhBdg> checkSoQd = xhQdPdKhBdgRepository.findBySoQdPd(req.getSoQdPd());
                if (!checkSoQd.isEmpty()) {
                    throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
                }
            }
        }
        XhQdPdKhBdg dataDB = qOptional.get();
        if (req.getPhanLoai().equals("TH")) {
            Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(req.getIdThHdr());
            XhThopDxKhBdg dataTh = qOptionalTh.get();
            if (!qOptionalTh.isPresent()) {
                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
            }else {
                dataTh.setSoQdPd(req.getSoQdPd());
                dataTh.setIdQdPd(dataDB.getId());
                xhThopDxKhBdgRepository.save(dataTh);
            }
        } else {
            Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(req.getIdTrHdr());
            XhDxKhBanDauGia dataTr = byId.get();
            if (!byId.isPresent()) {
                throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
            }else {
                dataTr.setSoQdPd(req.getSoQdPd());
                dataTr.setIdSoQdPd(dataDB.getId());
                xhDxKhBanDauGiaRepository.save(dataTr);
            }
        }

        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSuaId(getUser().getId());
        XhQdPdKhBdg created = xhQdPdKhBdgRepository.save(dataDB);

        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), created.getId(), XhQdPdKhBdg.TABLE_NAME);
            dataDB.setFileDinhKem(fileDinhKem.get(0));
        }

        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdPdKhBdg.TABLE_NAME);
        dataDB.setFileDinhKems(fileDinhKems);

        this.saveDetail(req, dataDB.getId());
        return dataDB;
    }

    @Override
    public XhQdPdKhBdg detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id))
            throw new Exception("Không tồn tại bản ghi");

        Optional<XhQdPdKhBdg> qOptional = xhQdPdKhBdgRepository.findById(id);

        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

        XhQdPdKhBdg data = qOptional.get();

        data.setTenLoaiVthh(hashMapDmHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapDmHh.get(data.getCloaiVthh()));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBdg.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        data.setFileDinhKems(fileDinhKem);

        List<XhQdPdKhBdgDtl> xhQdPdKhBdgDtlList = new ArrayList<>();
        for (XhQdPdKhBdgDtl dtl : xhQdPdKhBdgDtlRepository.findAllByIdQdHdr(id)) {
            List<XhQdPdKhBdgPl> xhQdPdKhBdgPlList = new ArrayList<>();
            for (XhQdPdKhBdgPl dsg : xhQdPdKhBdgPlRepository.findByIdQdDtl(dtl.getId())) {
                List<XhQdPdKhBdgPlDtl> xhQdPdKhBdgPlDtlList = xhQdPdKhBdgPlDtlRepository.findByIdPhanLo(dsg.getId());
                xhQdPdKhBdgPlDtlList.forEach(f -> {
                    f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
                    f.setTenNhaKho(mapDmucDvi.get(f.getMaNhaKho()));
                    f.setTenNganKho(mapDmucDvi.get(f.getMaNganKho()));
                    f.setTenLoKho(mapDmucDvi.get(f.getMaLoKho()));
                });
                dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
                dsg.setChildren(xhQdPdKhBdgPlDtlList);
                xhQdPdKhBdgPlList.add(dsg);
            };
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
            dtl.setChildren(xhQdPdKhBdgPlList);
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            xhQdPdKhBdgDtlList.add(dtl);
        }
        data.setChildren(xhQdPdKhBdgDtlList);
        return data;
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
                dataDB.setNgayPduyet(getDateTimeNow());
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
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xóa");

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
            bdgDx.setIdSoQdPd(null);
            bdgDx.setSoQdPd(null);
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
            objs[2] = pduyet.getSoQdPd();
            objs[3] = pduyet.getNgayKyQd();
            objs[4] = pduyet.getTrichYeu();
            objs[5] = pduyet.getSoTrHdr();
            objs[6] = pduyet.getIdThHdr();
            objs[7] = pduyet.getTenLoaiVthh();
            objs[8] = pduyet.getTenCloaiVthh();
            objs[11] = pduyet.getTenTrangThai();
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
        if (ObjectUtils.isEmpty(ids)) {
            throw new Exception("Không tồn tại bản ghi");
        }
        Optional<XhQdPdKhBdgDtl> qOptional = xhQdPdKhBdgDtlRepository.findById(ids);
        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        XhQdPdKhBdgDtl data = qOptional.get();

        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        List<XhQdPdKhBdgPl> xhQdPdKhBdgPlList = new ArrayList<>();
        for (XhQdPdKhBdgPl dsg : xhQdPdKhBdgPlRepository.findByIdQdDtl(data.getId())) {
            List<XhQdPdKhBdgPlDtl> xhQdPdKhBdgPlDtlList = xhQdPdKhBdgPlDtlRepository.findByIdPhanLo(dsg.getId());
            xhQdPdKhBdgPlDtlList.forEach(f -> {
                f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
                f.setTenNhaKho(mapDmucDvi.get(f.getMaNhaKho()));
                f.setTenNganKho(mapDmucDvi.get(f.getMaNganKho()));
                f.setTenLoKho(mapDmucDvi.get(f.getMaLoKho()));
            });
            dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
            dsg.setChildren(xhQdPdKhBdgPlDtlList);
            xhQdPdKhBdgPlList.add(dsg);
        };
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        data.setChildren(xhQdPdKhBdgPlList);
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

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

    //
//
//  public XhQdPdKhBdg approve(StatusReq stReq) throws Exception {
//    if (StringUtils.isEmpty(stReq.getId())) {
//      throw new Exception("Không tìm thấy dữ liệu");
//    }
//    XhQdPdKhBdg dataDB = detail(String.valueOf(stReq.getId()));
//    String status = stReq.getTrangThai() + dataDB.getTrangThai();
//    switch (status) {
//      case Contains.BAN_HANH + Contains.DUTHAO:
//        dataDB.setNgayPduyet(getDateTimeNow());
//        dataDB.setNguoiPduyetId(getUser().getId());
//        break;
//      default:
//        throw new Exception("Phê duyệt không thành công");
//    }
//    dataDB.setTrangThai(stReq.getTrangThai());
//
//    if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
//      if (dataDB.getPhanLoai().equals("TH")) {
//        Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(dataDB.getIdThHdr());
//        if (qOptional.isPresent()) {
//          if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
//            throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
//          }
//          xhThopDxKhBdgRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
//        } else {
//          throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
//        }
//      } else {
//        Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
//        if (qOptional.isPresent()) {
//          if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
//            throw new Exception("Đề xuất này đã được quyết định");
//          }
//          // Update trạng thái tờ trình
//          xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
//        } else {
//          throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
//        }
//      }
//      this.cloneProject(dataDB.getId());
//      this.cloneForToChucBdg(dataDB);
//    }
//    XhQdPdKhBdg createCheck = xhQdPdKhBdgRepository.save(dataDB);
//    return createCheck;
//
//  }
//
//  @Transactional(rollbackOn = Exception.class)
//  XhQdPdKhBdg approveVatTu(StatusReq stReq, XhQdPdKhBdg dataDB) throws Exception {
//    String status = stReq.getTrangThai() + dataDB.getTrangThai();
//    switch (status) {
//      case Contains.CHODUYET_LDV + Contains.DUTHAO:
//      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
//        dataDB.setNgayGuiDuyet(getDateTimeNow());
//      case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
//        dataDB.setNgayPduyet(getDateTimeNow());
//      case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
//      case Contains.BAN_HANH + Contains.DADUYET_LDV:
//        dataDB.setNgayPduyet(getDateTimeNow());
//        break;
//      default:
//        throw new Exception("Phê duyệt không thành công");
//    }
//    dataDB.setTrangThai(stReq.getTrangThai());
//    if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
//      Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
//      if (qOptional.isPresent()) {
//        if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
//          throw new Exception("Đề xuất này đã được quyết định");
//        }
//        // Update trạng thái tờ trình
//        xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
//      } else {
//        throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
//      }
//      this.cloneProject(dataDB.getId());
//      this.cloneForToChucBdg(dataDB);
//    }
//    XhQdPdKhBdg createCheck = xhQdPdKhBdgRepository.save(dataDB);
//    return createCheck;
//  }
//
//  @Transactional(rollbackOn = Exception.class)
//  XhQdPdKhBdg approveLT(StatusReq stReq, XhQdPdKhBdg dataDB) throws Exception {
//    String status = stReq.getTrangThai() + dataDB.getTrangThai();
//    switch (status) {
//      case Contains.BAN_HANH + Contains.DUTHAO:
//        dataDB.setNgayPduyet(getDateTimeNow());
//        break;
//      default:
//        throw new Exception("Phê duyệt không thành công");
//    }
//    dataDB.setTrangThai(stReq.getTrangThai());
//    if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
//      if (dataDB.getPhanLoai().equals("TH")) {
//        Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(dataDB.getIdThHdr());
//        if (qOptional.isPresent()) {
//          if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
//            throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
//          }
//          xhThopDxKhBdgRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
//        } else {
//          throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
//        }
//      } else {
//        Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
//        if (qOptional.isPresent()) {
//          if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
//            throw new Exception("Đề xuất này đã được quyết định");
//          }
//          // Update trạng thái tờ trình
//          xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
//        } else {
//          throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
//        }
//      }
////            this.validateData(dataDB);
//      this.cloneProject(dataDB.getId());
//      this.cloneForToChucBdg(dataDB);
//    }
//    XhQdPdKhBdg createCheck = xhQdPdKhBdgRepository.save(dataDB);
//    return createCheck;
//  }
//
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
//
//  public void export(XhQdPdKhBdgSearchReq req, HttpServletResponse response) throws Exception {

//  }
//
//  private void cloneForToChucBdg(XhQdPdKhBdg data) throws Exception {
//    List<XhTcTtinBdgHdr> listXhTcTtinBdgHdr = new ArrayList<>();
//    data.getChildren().forEach(s -> {
//      XhTcTtinBdgHdr hdr = new XhTcTtinBdgHdr();
//      hdr.setMaDvi(data.getMaDvi());
//      hdr.setLoaiVthh(data.getLoaiVthh());
//      hdr.setCloaiVthh(data.getCloaiVthh());
//      hdr.setIdQdPdKh(data.getId());
//      hdr.setSoQdPdKh(data.getSoQdPd());
//      hdr.setIdQdDcKh(null);
//      hdr.setSoQdDcKh(null);
//      hdr.setIdQdPdKq(null);
//      hdr.setSoQdPdKq(null);
//      hdr.setIdKhDx(data.getIdTrHdr());
//      hdr.setSoKhDx(data.getSoTrHdr());
//      hdr.setNgayQdPdKqBdg(null);
//      hdr.setThoiHanGiaoNhan(s.getTgianGnhan());
//      hdr.setThoiHanThanhToan(s.getTgianTtoan());
//      hdr.setPhuongThucThanhToan(s.getPthucTtoan());
//      hdr.setPhuongThucGiaoNhan(s.getPthucGnhan());
//      hdr.setTrangThai(TrangThaiAllEnum.CHUA_CAP_NHAT.getId());
//      hdr.setMaDviThucHien(s.getMaDvi());
//      hdr.setTongTienGiaKhoiDiem(DataUtils.safeToLong(s.getTongTienKdienDonGia()));
//      hdr.setTongTienDatTruoc(DataUtils.safeToLong(s.getKhoanTienDatTruoc()));
//      hdr.setTongTienDatTruocDuocDuyet(DataUtils.safeToLong(s.getTongTienDatTruocDonGia()));
//      hdr.setTongSoLuong(DataUtils.safeToLong(s.getTongSoLuong()));
//      hdr.setPhanTramDatTruoc(DataUtils.safeToInt(s.getKhoanTienDatTruoc()));
//      if (!DataUtils.isNullObject(s.getTgianDkienTu()) && !DataUtils.isNullObject(s.getTgianDkienDen())) {
//        LocalDate localDateTu = Instant.ofEpochMilli(s.getTgianDkienTu().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate localDateDen = Instant.ofEpochMilli(s.getTgianDkienDen().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
//        hdr.setThoiGianToChucTu(localDateTu);
//        hdr.setThoiGianToChucDen(localDateDen);
//      }
//      hdr.setSoDviTsan(DataUtils.safeToInt(s.getSoDviTsan()));
//      hdr.setSoDviTsanThanhCong(0);
//      hdr.setSoDviTsanKhongThanh(0);
//      listXhTcTtinBdgHdr.add(hdr);
//    });
//    xhTcTtinBdgHdrRepository.saveAll(listXhTcTtinBdgHdr);
//  }
}
