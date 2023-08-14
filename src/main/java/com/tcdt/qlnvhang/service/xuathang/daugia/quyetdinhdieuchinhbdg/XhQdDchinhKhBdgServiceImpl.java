package com.tcdt.qlnvhang.service.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtl;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgPlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgPlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
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
public class XhQdDchinhKhBdgServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdDchinhKhBdgHdrRepository xhQdDchinhKhBdgHdrRepository;

    @Autowired
    private XhQdDchinhKhBdgDtlRepository xhQdDchinhKhBdgDtlRepository;

    @Autowired
    private XhQdDchinhKhBdgPlRepository xhQdDchinhKhBdgPlRepository;

    @Autowired
    private XhQdDchinhKhBdgPlDtlRepository xhQdDchinhKhBdgPlDtlRepository;


    public Page<XhQdDchinhKhBdgHdr> searchPage(CustomUserDetails currentUser, XhQdDchinhKhBdgReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
            req.setTrangThai(Contains.BAN_HANH);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhQdDchinhKhBdgHdr> search = xhQdDchinhKhBdgHdrRepository.searchPage(req, pageable);
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
    public XhQdDchinhKhBdgHdr create(CustomUserDetails currentUser, XhQdDchinhKhBdgReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!StringUtils.isEmpty(req.getSoQdDc())) {
            Optional<XhQdDchinhKhBdgHdr> optional = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(req.getSoQdDc());
            if (optional.isPresent())
                throw new Exception("Số quyết định điều chỉnh " + req.getSoQdDc() + " đã tồn tại");
        }
        XhQdDchinhKhBdgHdr data = new XhQdDchinhKhBdgHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DU_THAO);
        XhQdDchinhKhBdgHdr created = xhQdDchinhKhBdgHdrRepository.save(data);
        this.saveDetail(req, created.getId());
        return created;
    }

    void saveDetail(XhQdDchinhKhBdgReq req, Long idHdr) {
        xhQdDchinhKhBdgDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdDchinhKhBdgDtlReq dtlReq : req.getChildren()) {
            XhQdDchinhKhBdgDtl dtl = new XhQdDchinhKhBdgDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhQdDchinhKhBdgDtlRepository.save(dtl);
            xhQdDchinhKhBdgPlRepository.deleteAllByIdDcDtl(dtlReq.getId());
            for (XhQdDchinhKhBdgPlReq plReq : dtlReq.getChildren()) {
                XhQdDchinhKhBdgPl pl = new XhQdDchinhKhBdgPl();
                BeanUtils.copyProperties(plReq, pl, "id");
                pl.setIdDcDtl(dtl.getId());
                xhQdDchinhKhBdgPlRepository.save(pl);
                xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(plReq.getId());
                for (XhQdDchinhKhBdgPlDtlReq plDtlReq : plReq.getChildren()) {
                    XhQdDchinhKhBdgPlDtl plDtl = new XhQdDchinhKhBdgPlDtl();
                    BeanUtils.copyProperties(plDtlReq, plDtl, "id");
                    plDtl.setIdPhanLo(pl.getId());
                    xhQdDchinhKhBdgPlDtlRepository.save(plDtl);
                }
            }
        }
    }

    @Transactional
    public XhQdDchinhKhBdgHdr update(CustomUserDetails currentUser, XhQdDchinhKhBdgReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhQdDchinhKhBdgHdr> optional = xhQdDchinhKhBdgHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        Optional<XhQdDchinhKhBdgHdr> soQdDc = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(req.getSoQdDc());
        if (soQdDc.isPresent()) {
            if (!soQdDc.get().getId().equals(req.getId())) {
                throw new Exception("số quyết định điều chỉnh đã tồn tại");
            }
        }
        XhQdDchinhKhBdgHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        XhQdDchinhKhBdgHdr updated = xhQdDchinhKhBdgHdrRepository.save(data);
        this.saveDetail(req, updated.getId());
        return updated;
    }

    public List<XhQdDchinhKhBdgHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhQdDchinhKhBdgHdr> list = xhQdDchinhKhBdgHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        List<XhQdDchinhKhBdgHdr> allById = xhQdDchinhKhBdgHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<XhQdDchinhKhBdgDtl> dtlList = xhQdDchinhKhBdgDtlRepository.findAllByIdHdr(data.getId());
            dtlList.forEach(dataDtl -> {
                List<XhQdDchinhKhBdgPl> plList = xhQdDchinhKhBdgPlRepository.findAllByIdDcDtl(dataDtl.getId());
                plList.forEach(dataPl -> {
                    List<XhQdDchinhKhBdgPlDtl> plDtlList = xhQdDchinhKhBdgPlDtlRepository.findAllByIdPhanLo(dataPl.getId());
                    plDtlList.forEach(dataPlDtl -> {
                        dataPlDtl.setTenDiemKho(StringUtils.isEmpty(dataPlDtl.getMaDiemKho()) ? null : mapDmucDvi.get(dataPlDtl.getMaDiemKho()));
                        dataPlDtl.setTenNhaKho(StringUtils.isEmpty(dataPlDtl.getMaNhaKho()) ? null : mapDmucDvi.get(dataPlDtl.getMaNhaKho()));
                        dataPlDtl.setTenNganKho(StringUtils.isEmpty(dataPlDtl.getMaNganKho()) ? null : mapDmucDvi.get(dataPlDtl.getMaNganKho()));
                        dataPlDtl.setTenLoKho(StringUtils.isEmpty(dataPlDtl.getMaLoKho()) ? null : mapDmucDvi.get(dataPlDtl.getMaLoKho()));
                        dataPlDtl.setTenLoaiVthh(StringUtils.isEmpty(dataPlDtl.getLoaiVthh()) ? null : mapVthh.get(dataPlDtl.getLoaiVthh()));
                        dataPlDtl.setTenCloaiVthh(StringUtils.isEmpty(dataPlDtl.getCloaiVthh()) ? null : mapVthh.get(dataPlDtl.getCloaiVthh()));
                    });
                    dataPl.setTenDvi(StringUtils.isEmpty(dataPl.getMaDvi()) ? null : mapDmucDvi.get(dataPl.getMaDvi()));
                    dataPl.setChildren(plDtlList);
                });
                dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi()) ? null : mapDmucDvi.get(dataDtl.getMaDvi()));
                dataDtl.setTenLoaiVthh(StringUtils.isEmpty(dataDtl.getLoaiVthh()) ? null : mapVthh.get(dataDtl.getLoaiVthh()));
                dataDtl.setTenCloaiVthh(StringUtils.isEmpty(dataDtl.getCloaiVthh()) ? null : mapVthh.get(dataDtl.getCloaiVthh()));
                dataDtl.setChildren(plList);
            });
            data.setMapVthh(mapVthh);
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapLoaiHinhNx(mapLoaiHinhNx);
            data.setMapKieuNx(mapKieuNx);
            data.setTrangThai(data.getTrangThai());
            data.setChildren(dtlList);
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhQdDchinhKhBdgHdr> optional = xhQdDchinhKhBdgHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhQdDchinhKhBdgHdr data = optional.get();
        if (!data.getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa bản ghi ở trạng thái dự thảo");
        }
        List<XhQdDchinhKhBdgDtl> listDtl = xhQdDchinhKhBdgDtlRepository.findAllByIdHdr(data.getId());
        for (XhQdDchinhKhBdgDtl dataDtl : listDtl) {
            List<XhQdDchinhKhBdgPl> plList = xhQdDchinhKhBdgPlRepository.findAllByIdDcDtl(dataDtl.getId());
            for (XhQdDchinhKhBdgPl dataPl : plList) {
                xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(dataPl.getId());
            }
            xhQdDchinhKhBdgPlRepository.deleteAllByIdDcDtl(dataDtl.getId());
        }
        xhQdDchinhKhBdgDtlRepository.deleteAllByIdHdr(data.getId());
        xhQdDchinhKhBdgHdrRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdDchinhKhBdgHdr> list = xhQdDchinhKhBdgHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        for (XhQdDchinhKhBdgHdr xhQdDchinhKhBdgHdr : list) {
            if (!xhQdDchinhKhBdgHdr.getTrangThai().equals(Contains.DUTHAO)) {
                throw new Exception("Chỉ thực hiện xóa bản ghi ở trạng thái dự thảo");
            }
        }
        List<Long> idHdr = list.stream().map(XhQdDchinhKhBdgHdr::getId).collect(Collectors.toList());
        List<XhQdDchinhKhBdgDtl> listDtl = xhQdDchinhKhBdgDtlRepository.findByIdHdrIn(idHdr);
        listDtl.forEach(dataDtl -> {
            List<XhQdDchinhKhBdgPl> listPhanLo = xhQdDchinhKhBdgPlRepository.findAllByIdDcDtl(dataDtl.getId());
            listPhanLo.forEach(dataPhanLo -> {
                List<XhQdDchinhKhBdgPlDtl> listPhanLoDtl = xhQdDchinhKhBdgPlDtlRepository.findAllByIdPhanLo(dataPhanLo.getId());
                xhQdDchinhKhBdgPlDtlRepository.deleteAll(listPhanLoDtl);
            });
            xhQdDchinhKhBdgPlRepository.deleteAll(listPhanLo);
        });
        xhQdDchinhKhBdgDtlRepository.deleteAll(listDtl);
        xhQdDchinhKhBdgHdrRepository.deleteAll(list);
    }

    public XhQdDchinhKhBdgHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhQdDchinhKhBdgHdr> optional = xhQdDchinhKhBdgHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        XhQdDchinhKhBdgHdr data = optional.get();
        String status = statusReq.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                data.setNguoiPduyetId(currentUser.getUser().getId());
                data.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        XhQdDchinhKhBdgHdr created = xhQdDchinhKhBdgHdrRepository.save(data);
        return created;
    }

    public void export(CustomUserDetails currentUser, XhQdDchinhKhBdgReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdDchinhKhBdgHdr> page = this.searchPage(currentUser, req);
        List<XhQdDchinhKhBdgHdr> data = page.getContent();
        String title = " Danh sách quyết định điều chỉnh bán đấu giá ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ điều chỉnh KH BĐG",
                "Ngày ký QĐ điều chỉnh", "Số QĐ gốc", "Trích yếu", "Chủng loại hàng hóa",
                "Số ĐV tài sản", "SL HĐ đã ký", "Thời gian giao nhận", "Trạng thái"};
        String fileName = "Danh-sach-quyet-dinh-dieu-chinh-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdDchinhKhBdgHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNam();
            objs[2] = hdr.getSoQdDc();
            objs[3] = hdr.getNgayKyDc();
            objs[4] = hdr.getSoQdGoc();
            objs[5] = hdr.getTrichYeu();
            objs[6] = hdr.getTenCloaiVthh();
            objs[7] = hdr.getSlDviTsan();
            objs[8] = null;
            objs[9] = hdr.getThoiHanGiaoNhanHang();
            objs[10] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }


//    @Override
//    public Page<XhQdDchinhKhBdgHdr> searchPage(XhQdDchinhKhBdgReq req) throws Exception {
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
//                req.getPaggingReq().getLimit(), Sort.by("id").descending());
//        Page<XhQdDchinhKhBdgHdr> data = xhQdDchinhKhBdgHdrRepository.searchPage(
//                req,
//                pageable);
//        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
//        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
//        data.getContent().forEach(f ->{
//            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
//            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
//            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
//        });
//        return data;
//    }
//
//    @Override
//    public XhQdDchinhKhBdgHdr create(XhQdDchinhKhBdgReq req) throws Exception {
//        if (!StringUtils.isEmpty(req.getSoQdDc())){
//            List<XhQdDchinhKhBdgHdr> checkSoQdDc = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(req.getSoQdDc());
//            if (!checkSoQdDc.isEmpty()){
//                throw new Exception("Số quyết định điều chỉnh" + req.getSoQdDc() + "đã tồn tại");
//            }
//        }
//        XhQdDchinhKhBdgHdr dataMap = new XhQdDchinhKhBdgHdr();
//        BeanUtils.copyProperties(req, dataMap, "id");
//        dataMap.setNgayTao(LocalDate.now());
//        dataMap.setNguoiTaoId(getUser().getId());
//        dataMap.setTrangThai(Contains.DUTHAO);
//        dataMap.setMaDvi(getUser().getDvql());
//        XhQdDchinhKhBdgHdr created = xhQdDchinhKhBdgHdrRepository.save(dataMap);
//        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
//            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdDchinhKhBdgHdr.TABLE_NAME+ "_BAN_HANH");
//            created.setFileDinhKem(fileDinhKem);
//        }
//        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdDchinhKhBdgHdr.TABLE_NAME);
//            created.setFileDinhKems(fileDinhKems);
//        }
//        saveDetail(req, dataMap.getId());
//        return dataMap;
//    }
//
//    void saveDetail(XhQdDchinhKhBdgReq req, Long idHdr){
//        xhQdDchinhKhBdgDtlRepository.deleteAllByIdHdr(idHdr);
//        for (XhQdDchinhKhBdgDtlReq dtlReq : req.getChildren()){
//            XhQdDchinhKhBdgDtl dtl = new XhQdDchinhKhBdgDtl();
//            BeanUtils.copyProperties(dtlReq, dtl, "id");
//            dtl.setIdHdr(idHdr);
//            dtl.setIdQdGoc(req.getIdQdGoc());
//            xhQdDchinhKhBdgDtlRepository.save(dtl);
//            xhQdDchinhKhBdgPlRepository.deleteAllByIdDcDtl(dtlReq.getId());
//            for (XhQdDchinhKhBdgPlReq plReq : dtlReq.getChildren()){
//                XhQdDchinhKhBdgPl pl = new XhQdDchinhKhBdgPl();
//                BeanUtils.copyProperties(plReq, pl, "id");
//                pl.setIdDcDtl(dtl.getId());
//                xhQdDchinhKhBdgPlRepository.save(pl);
//                xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(plReq.getId());
//                for (XhQdDchinhKhBdgPlDtlReq plDtlReq : plReq.getChildren()){
//                    XhQdDchinhKhBdgPlDtl plDtl = new XhQdDchinhKhBdgPlDtl();
//                    BeanUtils.copyProperties(plDtlReq, plDtl, "id");
//                    plDtl.setId(null);
//                    plDtl.setIdPhanLo(pl.getId());
//                    xhQdDchinhKhBdgPlDtlRepository.save(plDtl);
//                }
//            }
//        }
//    }
//
//    @Override
//    public XhQdDchinhKhBdgHdr update(XhQdDchinhKhBdgReq req) throws Exception {
//        if (StringUtils.isEmpty(req.getId())) {
//            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
//        }
//
//        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(req.getId());
//        if (!qOptional.isPresent()){
//            throw new Exception("Không tìm thấy dữ liệu cần sửa");
//        }
//
//        XhQdDchinhKhBdgHdr dataDB = qOptional.get();
//        BeanUtils.copyProperties(req, dataDB, "id");
//        dataDB.setNgaySua(LocalDate.now());
//        dataDB.setNguoiSuaId(getUser().getId());
//        XhQdDchinhKhBdgHdr created = xhQdDchinhKhBdgHdrRepository.save(dataDB);
//
//        fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhQdDchinhKhBdgHdr.TABLE_NAME + "_BAN_HANH"));
//        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdDchinhKhBdgHdr.TABLE_NAME + "_BAN_HANH");
//        created.setFileDinhKem(fileDinhKem);
//
//        fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhQdDchinhKhBdgHdr.TABLE_NAME));
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdDchinhKhBdgHdr.TABLE_NAME);
//        created.setFileDinhKems(fileDinhKems);
//
//        this.saveDetail(req, dataDB.getId());
//        return dataDB;
//    }
//
//    @Override
//    public XhQdDchinhKhBdgHdr detail(Long id) throws Exception {
//        if (StringUtils.isEmpty(id))
//            throw new Exception("Không tồn tại bản ghi");
//
//        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(id);
//
//        if (!qOptional.isPresent()){
//            throw new UnsupportedOperationException("Không tồn tại bản ghi");
//        }
//
//        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
//        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
//
//        XhQdDchinhKhBdgHdr data = qOptional.get();
//
//        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
//        data.setTenLoaiVthh(hashMapDmHh.get(data.getLoaiVthh()));
//        data.setTenCloaiVthh(hashMapDmHh.get(data.getCloaiVthh()));
//        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
//
//        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdDchinhKhBdgHdr.TABLE_NAME+ "_BAN_HANH"));
//        data.setFileDinhKem(fileDinhKem);
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdDchinhKhBdgHdr.TABLE_NAME));
//        data.setFileDinhKems(fileDinhKems);
//
//        List<XhQdDchinhKhBdgDtl> xhQdDchinhKhBdgDtlList = new ArrayList<>();
//        for (XhQdDchinhKhBdgDtl dtl : xhQdDchinhKhBdgDtlRepository.findAllByIdHdr(id)){
//            List<XhQdDchinhKhBdgPl> xhQdDchinhKhBdgPlList = new ArrayList<>();
//            for (XhQdDchinhKhBdgPl pl : xhQdDchinhKhBdgPlRepository.findAllByIdDcDtl(dtl.getId())){
//                List<XhQdDchinhKhBdgPlDtl> xhQdDchinhKhBdgPlDtlList = xhQdDchinhKhBdgPlDtlRepository.findAllByIdPhanLo(pl.getId());
//                xhQdDchinhKhBdgPlDtlList.forEach(f ->{
//                    f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())? null : mapDmucDvi.get(f.getMaDiemKho()));
//                    f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())? null : mapDmucDvi.get(f.getMaNhaKho()));
//                    f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())? null : mapDmucDvi.get(f.getMaNganKho()));
//                    f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())? null : mapDmucDvi.get(f.getMaLoKho()));
//                });
//                pl.setTenDvi(StringUtils.isEmpty(pl.getMaDvi())? null : mapDmucDvi.get(pl.getMaDvi()));
//                pl.setChildren(xhQdDchinhKhBdgPlDtlList);
//                xhQdDchinhKhBdgPlList.add(pl);
//            };
//            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
//            dtl.setChildren(xhQdDchinhKhBdgPlList);
//            xhQdDchinhKhBdgDtlList.add(dtl);
//        }
//        data.setChildren(xhQdDchinhKhBdgDtlList);
//        return data;
//    }
//
//    @Override
//    public XhQdDchinhKhBdgHdr approve(XhQdDchinhKhBdgReq req) throws Exception {
//        if (StringUtils.isEmpty(req.getId())){
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        XhQdDchinhKhBdgHdr detail = detail(req.getId());
//        String status = req.getTrangThai() + detail.getTrangThai();
//        switch (status){
//            case Contains.CHODUYET_LDV + Contains.DUTHAO:
//            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
//                detail.setNguoiGuiDuyetId(getUser().getId());
//                detail.setNgayGuiDuyet(LocalDate.now());
//                break;
//            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
//                detail.setNguoiPduyetId(getUser().getId());
//                detail.setNgayPduyet(LocalDate.now());
//                detail.setLyDoTuChoi(req.getLyDoTuChoi());
//                break;
//            case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
//            case Contains.BAN_HANH + Contains.DADUYET_LDV:
//                detail.setNguoiPduyetId(getUser().getId());
//                detail.setNgayPduyet(LocalDate.now());
//                break;
//            default:
//                throw new Exception("Phê duyệt không thành công");
//        }
//        detail.setTrangThai(req.getTrangThai());
//        XhQdDchinhKhBdgHdr createCheck = xhQdDchinhKhBdgHdrRepository.save(detail);
//        return createCheck;
//    }
//
//    @Override
//    public void delete(Long id) throws Exception {
//        if (StringUtils.isEmpty(id)) {
//            throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");
//        }
//
//        Optional<XhQdDchinhKhBdgHdr> optional = xhQdDchinhKhBdgHdrRepository.findById(id);
//        if (!optional.isPresent()){
//            throw new Exception("Không tìm thấy dữ liệu cần xóa");
//        }
//
//        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
//            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
//        }
//        XhQdDchinhKhBdgHdr hdr = optional.get();
//
//        List<XhQdDchinhKhBdgDtl> xhQdDchinhKhBdgDtlList = xhQdDchinhKhBdgDtlRepository.findAllByIdHdr(hdr.getId());
//        if (!CollectionUtils.isEmpty(xhQdDchinhKhBdgDtlList)){
//            for (XhQdDchinhKhBdgDtl dtl : xhQdDchinhKhBdgDtlList){
//                List<XhQdDchinhKhBdgPl> xhQdDchinhKhBdgPlList = xhQdDchinhKhBdgPlRepository.findAllByIdDcDtl(dtl.getId());
//                for (XhQdDchinhKhBdgPl pl : xhQdDchinhKhBdgPlList){
//                    xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(pl.getId());
//                }
//                xhQdDchinhKhBdgPlRepository.deleteAllByIdDcDtl(dtl.getId());
//            }
//            xhQdDchinhKhBdgDtlRepository.deleteAll(xhQdDchinhKhBdgDtlList);
//        }
//        xhQdDchinhKhBdgHdrRepository.delete(hdr);
//        fileDinhKemService.delete(hdr.getId(), Collections.singleton(XhQdDchinhKhBdgHdr.TABLE_NAME));
//        fileDinhKemService.delete(hdr.getId(), Collections.singleton(XhQdDchinhKhBdgHdr.TABLE_NAME + "_BAN_HANH"));
//
//    }
//
//    @Override
//    public void deleteMulti(List<Long> listMulti) throws Exception {
//        if (StringUtils.isEmpty(listMulti)) {
//            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
//        }
//
//        List<XhQdDchinhKhBdgHdr> list = xhQdDchinhKhBdgHdrRepository.findAllByIdIn(listMulti);
//        if (list.isEmpty()){
//            throw new Exception("Không tìm thấy dữ liệu cần xóa");
//        }
//
//        for (XhQdDchinhKhBdgHdr hdr : list){
//            if (!hdr.getTrangThai().equals(Contains.DUTHAO)){
//                throw new Exception("Chỉ thực hiện xóa bản nghi ở trạng thái dự thảo");
//            }else {
//                this.delete(hdr.getId());
//            }
//        }
//    }
//
//    @Override
//    public void export(XhQdDchinhKhBdgReq req, HttpServletResponse response) throws Exception {
//        PaggingReq paggingReq = new PaggingReq();
//        paggingReq.setPage(0);
//        paggingReq.setLimit(Integer.MAX_VALUE);
//        req.setPaggingReq(paggingReq);
//        Page<XhQdDchinhKhBdgHdr> page = this.searchPage(req);
//        List<XhQdDchinhKhBdgHdr> data = page.getContent();
//        String title = " Danh sách quyết định điều chỉnh bán đấu giá ";
//        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ điều chỉnh KH BĐG", "Ngày ký QĐ điều chỉnh", "Số QĐ gốc", "Trích yếu", "Chủng loại hàng hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Thời gian giao nhận", "Trạng thái"};
//        String fileName = "Danh-sach-quyet-dinh-dieu-chinh-ban-dau-gia.xlsx";
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//        for (int i = 0; i < data.size(); i++) {
//            XhQdDchinhKhBdgHdr hdr = data.get(i);
//            objs = new Object[rowsName.length];
//            objs[0] = i;
//            objs[1] = hdr.getNam();
//            objs[2] = hdr.getSoQdDc();
//            objs[3] = hdr.getNgayKyDc();
//            objs[4] = hdr.getSoQdGoc();
//            objs[5] = hdr.getTrichYeu();
//            objs[6] = hdr.getTenCloaiVthh();
//            objs[7] = hdr.getSlDviTsan();
//            objs[8] = null;
//            objs[9] = null;
//            objs[10] = hdr.getTenTrangThai();
//            dataList.add(objs);
//        }
//        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
//        ex.export();
//    }
}
