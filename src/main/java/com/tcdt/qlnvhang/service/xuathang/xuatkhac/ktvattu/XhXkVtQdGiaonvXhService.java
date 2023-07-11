package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRequest;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatCuc;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatHangDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkVtQdGiaonvXhService extends BaseServiceImpl {
    @Autowired
    private XhXkVtQdGiaonvXhRepository xhXkVtQdGiaonvXhRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtQdGiaonvXhHdr> searchPage(CustomUserDetails currentUser, XhXkVtQdGiaonvXhRequest req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setDvql(currentUser.getDvql());
        Page<XhXkVtQdGiaonvXhHdr> search = xhXkVtQdGiaonvXhRepository.searchPage(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.setTenLoai(Contains.mapLoaiHinhXuat.get(s.getLoai()));
            s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
            s.getXhXkVtQdGiaonvXhDtl().forEach(item -> {
                item.setMapVthh(mapVthh);
                item.setMapDmucDvi(mapDmucDvi);
            });
        });
        return search;
    }

    @Transactional
    public XhXkVtQdGiaonvXhHdr save(CustomUserDetails currentUser, XhXkVtQdGiaonvXhRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!DataUtils.isNullObject(objReq.getSoQuyetDinh())) {
            Optional<XhXkVtQdGiaonvXhHdr> optional = xhXkVtQdGiaonvXhRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
            if (optional.isPresent()) {
                throw new Exception("Số quyết định đã tồn tại");
            }
        }
        XhXkVtQdGiaonvXhHdr data = new XhXkVtQdGiaonvXhHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        data.getXhXkVtQdGiaonvXhDtl().forEach(s -> {
            s.setXhXkVtQdGiaonvXhHdr(data);
            s.setId(null);
        });
        XhXkVtQdGiaonvXhHdr created = xhXkVtQdGiaonvXhRepository.save(data);
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtQdGiaonvXhHdr.TABLE_NAME);
        return detail(created.getId());
    }


    @Transactional()
    public XhXkVtQdGiaonvXhHdr update(CustomUserDetails currentUser, XhXkVtQdGiaonvXhRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkVtQdGiaonvXhHdr> optional = xhXkVtQdGiaonvXhRepository.findById(objReq.getId());
        if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

        if (objReq.getSoQuyetDinh().contains("/") && !ObjectUtils.isEmpty(objReq.getSoQuyetDinh().split("/")[0])) {
            Optional<XhXkVtQdGiaonvXhHdr> optionalBySoTt = xhXkVtQdGiaonvXhRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
            if (optionalBySoTt.isPresent() && optionalBySoTt.get().getId() != objReq.getId()) {
                if (!optionalBySoTt.isPresent()) throw new Exception("Số quyết định đã tồn tại!");
            }
        }
        XhXkVtQdGiaonvXhHdr dx = optional.get();
        dx.getXhXkVtQdGiaonvXhDtl().forEach(e -> e.setXhXkVtQdGiaonvXhHdr(null));
        BeanUtils.copyProperties(objReq, dx);
        dx.getXhXkVtQdGiaonvXhDtl().forEach(e -> e.setXhXkVtQdGiaonvXhHdr(dx));
        dx.setXhXkVtQdGiaonvXhDtl(objReq.getXhXkVtQdGiaonvXhDtl());
        XhXkVtQdGiaonvXhHdr created = xhXkVtQdGiaonvXhRepository.save(dx);
        fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtQdGiaonvXhHdr.TABLE_NAME));
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtQdGiaonvXhHdr.TABLE_NAME);
        return detail(created.getId());
    }

    @Transactional()
    public XhXkVtQdGiaonvXhHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtQdGiaonvXhHdr> optional = xhXkVtQdGiaonvXhRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtQdGiaonvXhHdr model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkKhXuatHang.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.getXhXkVtQdGiaonvXhDtl().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
        });
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }


    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtQdGiaonvXhHdr> optional = xhXkVtQdGiaonvXhRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkVtQdGiaonvXhHdr data = optional.get();
        fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtQdGiaonvXhHdr.TABLE_NAME));
        xhXkVtQdGiaonvXhRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhXkVtQdGiaonvXhHdr> list = xhXkVtQdGiaonvXhRepository.findByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Collections.singleton(XhXkVtQdGiaonvXhHdr.TABLE_NAME));
        xhXkVtQdGiaonvXhRepository.deleteAll(list);
    }


//    public XhXkKhXuatHang pheDuyet(CustomUserDetails currentUser, StatusReq req) throws Exception {
//        Optional<XhXkKhXuatHang> dx = xhXkKhXuatHangRepository.findById(req.getId());
//        if (!dx.isPresent()) {
//            throw new Exception("Không tồn tại bản ghi");
//        }
//        XhXkKhXuatHang xhXkKhXuatHang = dx.get();
//        String status = xhXkKhXuatHang.getTrangThai() + req.getTrangThai();
//        switch (status) {
//            case Contains.DU_THAO + Contains.CHO_DUYET_TP:
//            case Contains.TU_CHOI_TP + Contains.CHO_DUYET_TP:
//            case Contains.TU_CHOI_LDC + Contains.CHO_DUYET_TP:
//                break;
//            case Contains.CHO_DUYET_TP + Contains.TU_CHOI_TP:
//            case Contains.CHO_DUYET_LDC + Contains.CHO_DUYET_TP:
//            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
//            case Contains.CHO_DUYET_BTC + Contains.TU_CHOI_BTC:
//            case Contains.CHODUYET_LDTC + Contains.TUCHOI_LDTC:
//                xhXkKhXuatHang.setLyDoTuChoi(req.getLyDoTuChoi());
//                break;
//            case Contains.CHO_DUYET_LDC + Contains.DA_DUYET_LDC:
//            case Contains.CHO_DUYET_TP + Contains.CHO_DUYET_LDC:
//            case Contains.CHODUYET_LDV + Contains.CHODUYET_LDTC:
//            case Contains.CHODUYET_LDTC + Contains.CHO_DUYET_BTC:
//            case Contains.CHO_DUYET_BTC + Contains.DA_DUYET_BTC:
//            case Contains.DU_THAO + Contains.CHODUYET_LDV:
//                xhXkKhXuatHang.setNguoiDuyetId(currentUser.getUser().getId());
//                xhXkKhXuatHang.setNgayDuyet(LocalDate.now());
//                break;
//            default:
//                throw new Exception("Phê duyệt không thành công.");
//        }
//        xhXkKhXuatHang.setTrangThai(req.getTrangThai());
//        //Check nếu là bản ghi kế hoạch của TỔng cục thì update trang thái bản ghi TH thành đã gửi duyệt KH
//        if (xhXkKhXuatHang.getCapDvi() == 1 && xhXkKhXuatHang.getLoai().equals("00")) {
//            Optional<XhXkKhXuatHang> itemTh = xhXkKhXuatHangRepository.findById(xhXkKhXuatHang.getIdCanCu());
//            if (itemTh.isPresent() && !itemTh.get().getTrangThai().equals(TrangThaiAllEnum.DAGUIDUYET_KH.getId())) {
//                itemTh.get().setTrangThai(TrangThaiAllEnum.DAGUIDUYET_KH.getId());
//                xhXkKhXuatHangRepository.save(itemTh.get());
//            }
//        }
//        XhXkKhXuatHang model = xhXkKhXuatHangRepository.save(xhXkKhXuatHang);
//        return detail(model.getId());
//    }


//    public void export(CustomUserDetails currentUser, XhXkVtQdGiaonvXhRequest objReq, HttpServletResponse response) throws Exception {
//        PaggingReq paggingReq = new PaggingReq();
//        paggingReq.setPage(0);
//        paggingReq.setLimit(Integer.MAX_VALUE);
//        objReq.setPaggingReq(paggingReq);
//        List<XhXkVtQdGiaonvXhHdr> data = this.searchPage(currentUser, objReq).getContent();
//        String title, fileName = "";
//        String[] rowsName;
//        Object[] objs;
//        List<Object[]> dataList = new ArrayList<>();
//        if (objReq.getLoai().equals("00")) {
//            title = "Danh sách kế hoạch VT, TB có thời hạn lưu kho lớn hơn 12 tháng của Cục DTNN KV";
//            fileName = "ds-ke-hoach-vt-tb-co-thoi-han-luu-kho-lon-hon-12-thang-cua-cuc-dtnn-kv.xlsx";
//            rowsName = new String[]{"STT", "Năm kế hoạch", "Số KH/Tờ trình", "Ngày lập KH", "Ngày duyệt KH", "Trích yếu", "Số ĐV tài sản", "Trạng thái"};
//            for (int i = 0; i < data.size(); i++) {
//                XhXkVtQdGiaonvXhHdr dx = data.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = dx.getNamKeHoach();
//                objs[2] = dx.getSoToTrinh();
//                objs[3] = dx.getNgayKeHoach();
//                objs[4] = dx.getNgayDuyetKeHoach();
//                objs[5] = dx.getTrichYeu();
//                objs[6] = dx.getSoDviTaiSan();
//                objs[7] = dx.getTenTrangThai();
//                dataList.add(objs);
//            }
//        } else {
//            title = "Danh sách tổng hợp kế hoạch xuất vật tư, thiết bị có thời hạn lưu kho lớn hơn 12";
//            fileName = "ds-tong-hop-ke-hoach-vt-tb-co-thoi-han-luu-kho-lon-hon-12-thang.xlsx";
//            rowsName = new String[]{"STT", "Mã tổng hợp", "Thời gian tổng hợp", "Nội dung tổng hợp", "Năm kế hoạch", "Trạng thái"};
//            for (int i = 0; i < data.size(); i++) {
//                XhXkKhXuatHang dx = data.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = dx.getId();
//                objs[2] = dx.getThoiGianTh();
//                objs[3] = dx.getNoiDungTh();
//                objs[4] = dx.getNamKeHoach();
//                objs[5] = dx.getTenTrangThai();
//                dataList.add(objs);
//            }
//        }
//
//        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
//        ex.export();
//    }

}
