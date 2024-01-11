package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkDanhSachRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkTongHopRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatCuc;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatHangDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtPhieuKnClHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangDtl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkKhXuatHangService extends BaseServiceImpl {
    @Autowired
    private XhXkKhXuatHangRepository xhXkKhXuatHangRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkKhXuatHang> searchPage(CustomUserDetails currentUser, XhXkKhXuatHangRequest req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setDvql(currentUser.getDvql());
        Page<XhXkKhXuatHang> search = xhXkKhXuatHangRepository.searchPage(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
            s.setSoDviTaiSan(s.getXhXkKhXuatHangDtl().size());
            s.getXhXkKhXuatHangDtl().forEach(item -> {
                item.setMapVthh(mapVthh);
                item.setMapDmucDvi(mapDmucDvi);
//                item.setNamNhap(xhXkKhXuatHangRepository.getNamNhap(item.getMaDiaDiem(), item.getId()));
            });
        });
        return search;
    }

    @Transactional
    public XhXkKhXuatHang save(CustomUserDetails currentUser, XhXkKhXuatHangRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!DataUtils.isNullObject(objReq.getSoToTrinh())) {
            Optional<XhXkKhXuatHang> optional = xhXkKhXuatHangRepository.findBySoToTrinh(objReq.getSoToTrinh());
            if (optional.isPresent()) {
                throw new Exception("Số kế hoạch/tờ trình đã tồn tại");
            }
        }
        XhXkKhXuatHang data = new XhXkKhXuatHang();
        BeanUtils.copyProperties(objReq, data);
//        data.setMaDvi(currentUser.getUser().getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.getXhXkKhXuatHangDtl().forEach(s -> s.setXhXkKhXuatHang(data));
        XhXkKhXuatHang created = xhXkKhXuatHangRepository.save(data);
        //Nếu là quyết định của Tổng cục tạo, cập nhật lại trạng thái bản ghi tổng hợp thành đã tạo kế hoạch
        if (objReq.getCapDvi() == 1) {
            Optional<XhXkKhXuatHang> recordTh = xhXkKhXuatHangRepository.findById(objReq.getIdCanCu());
            if (recordTh.isPresent()) {
                recordTh.get().setTrangThai(TrangThaiAllEnum.DADUTHAO_KH.getId());
                xhXkKhXuatHangRepository.save(recordTh.get());
            }
        }
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkKhXuatHang.TABLE_NAME);
        return detail(created.getId());
    }

    @Transactional
    public XhXkKhXuatHang saveTongHop(CustomUserDetails currentUser, XhXkKhXuatHangRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        XhXkKhXuatHang data = new XhXkKhXuatHang();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDvql());
        data.setTrangThai(TrangThaiAllEnum.CHUATAO_KH.getId());
        data.getXhXkKhXuatHangDtl().forEach(s -> s.setXhXkKhXuatHang(data));
        XhXkKhXuatHang created = xhXkKhXuatHangRepository.save(data);
        Long idTh = created.getId();
        //Set id TH cho bản ghi kế hoạch
        List<XhXkKhXuatHang> listKh = xhXkKhXuatHangRepository.findByIdIn(objReq.getListIdKeHoachs());
        if (!listKh.isEmpty()) {
            listKh.forEach(item -> {
                item.setIdCanCu(idTh);
            });
        }
        return detail(created.getId());
    }

    @Transactional()
    public XhXkKhXuatHang update(CustomUserDetails currentUser, XhXkKhXuatHangRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkKhXuatHang> optional = xhXkKhXuatHangRepository.findById(objReq.getId());
        if (!optional.isPresent()) throw new Exception("Đề xuất không tồn tại!");

        if (objReq.getSoToTrinh().contains("/") && !ObjectUtils.isEmpty(objReq.getSoToTrinh().split("/")[0])) {
            Optional<XhXkKhXuatHang> optionalBySoTt = xhXkKhXuatHangRepository.findBySoToTrinh(objReq.getSoToTrinh());
            if (optionalBySoTt.isPresent() && optionalBySoTt.get().getId() != objReq.getId()) {
                if (!optionalBySoTt.isPresent()) throw new Exception("Số tờ trình/kế hoạch đã tồn tại!");
            }
        }
        XhXkKhXuatHang dx = optional.get();
        dx.getXhXkKhXuatHangDtl().forEach(e -> e.setXhXkKhXuatHang(null));
        BeanUtils.copyProperties(objReq, dx);
        dx.getXhXkKhXuatHangDtl().forEach(e -> e.setXhXkKhXuatHang(dx));
        dx.setXhXkKhXuatHangDtl(objReq.getXhXkKhXuatHangDtl());
        XhXkKhXuatHang created = xhXkKhXuatHangRepository.save(dx);
        fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkKhXuatHang.TABLE_NAME));
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkKhXuatHang.TABLE_NAME);
        return detail(created.getId());
    }


    @Transactional()
    public XhXkKhXuatHang updateTongHop(CustomUserDetails currentUser, XhXkKhXuatHangRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkKhXuatHang> optional = xhXkKhXuatHangRepository.findById(objReq.getId());
        if (!optional.isPresent()) throw new Exception("Tổng hợp kế hoạch không tồn tại!");
        XhXkKhXuatHang dx = optional.get();
        dx.getXhXkKhXuatHangDtl().forEach(e -> e.setXhXkKhXuatHang(null));
        BeanUtils.copyProperties(objReq, dx);
        dx.getXhXkKhXuatHangDtl().forEach(e -> e.setXhXkKhXuatHang(dx));
        dx.setXhXkKhXuatHangDtl(objReq.getXhXkKhXuatHangDtl());
        XhXkKhXuatHang created = xhXkKhXuatHangRepository.save(dx);
        return detail(created.getId());
    }

    @Transactional()
    public XhXkKhXuatHang detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkKhXuatHang> optional = xhXkKhXuatHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkKhXuatHang model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkKhXuatHang.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.getXhXkKhXuatHangDtl().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
            s.setNamNhap(xhXkKhXuatHangRepository.getNamNhap(s.getMaDiaDiem(), s.getId()));
        });
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setSoDviTaiSan(model.getXhXkKhXuatHangDtl().size());
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }

    @Transactional()
    public XhXkKhXuatHang detailTongHop(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkKhXuatHang> optional = xhXkKhXuatHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkKhXuatHang model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        model.getXhXkKhXuatHangDtl().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
        });
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setSoDviTaiSan(model.getXhXkKhXuatHangDtl().size());
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        Map<String, Long> mapCount = new HashMap<>();
        ArrayList<XhXkTongHopKhXuatCuc> listSumDtlByCuc = new ArrayList<>();
        //get List đề xuất kế hoạch của Cục đc bản ghi này tổng hợp nên.
        List<XhXkKhXuatHang> listKeHoachs = xhXkKhXuatHangRepository.findByIdCanCuIn(Arrays.asList(model.getId()));
        List<String> soTotrinhs = listKeHoachs.stream().map(XhXkKhXuatHang::getSoToTrinh).collect(Collectors.toList());
        List<Long> idKeHoachs = listKeHoachs.stream().map(XhXkKhXuatHang::getId).collect(Collectors.toList());
        model.setListIdKeHoachs(idKeHoachs);
        model.setListSoKeHoachs(soTotrinhs);
        mapCount = model.getXhXkKhXuatHangDtl().stream()
                .collect(Collectors.groupingBy(XhXkKhXuatHangDtl::getTenCuc, Collectors.counting()));
        if (!mapCount.isEmpty()) {
            for (Map.Entry<String, Long> entry : mapCount.entrySet()) {
                XhXkTongHopKhXuatCuc item = new XhXkTongHopKhXuatCuc();
                item.setTenDvi(entry.getKey());
                item.setSoDviTaiSan(entry.getValue());
                item.setTenTrangThai(TrangThaiAllEnum.DA_DUYET_LDC.getTen());
                item.setTrangThai(TrangThaiAllEnum.DA_DUYET_LDC.getId());
                listSumDtlByCuc.add(item);
            }
        }
        model.setListDxCuc(listSumDtlByCuc);
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkKhXuatHang> optional = xhXkKhXuatHangRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkKhXuatHang data = optional.get();
        //Kiểm tra nếu là xóa bản ghi kế hoạch của Tổng CỤc thì update bản ghi Tổng hợp về trạng thái chưa tạo KH
        if (data.getCapDvi() == 1 && data.getLoai().equals("00")) {
            Optional<XhXkKhXuatHang> itemTh = xhXkKhXuatHangRepository.findById(data.getIdCanCu());
            if (itemTh.isPresent()) {
                itemTh.get().setTrangThai(TrangThaiAllEnum.CHUATAO_KH.getId());
                xhXkKhXuatHangRepository.save(itemTh.get());
            }
        }
        xhXkKhXuatHangRepository.delete(data);
    }

    @Transactional
    public void deleteTongHop(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkKhXuatHang> optional = xhXkKhXuatHangRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkKhXuatHang data = optional.get();
        //Update lại idTh cho bản ghi đề xuất của cục
        List<XhXkKhXuatHang> listKh = xhXkKhXuatHangRepository.findByIdCanCuIn(Arrays.asList(data.getId()));
        if (!listKh.isEmpty()) {
            listKh.forEach(item -> {
                item.setIdCanCu(null);
            });
            xhXkKhXuatHangRepository.saveAll(listKh);
        }
        xhXkKhXuatHangRepository.delete(data);
    }


    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhXkKhXuatHang> list = xhXkKhXuatHangRepository.findByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        xhXkKhXuatHangRepository.deleteAll(list);
    }

    @Transient
    public void deleteMultiTongHop(IdSearchReq idSearchReq) throws Exception {
        List<XhXkKhXuatHang> list = xhXkKhXuatHangRepository.findByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<XhXkKhXuatHang> listKh = xhXkKhXuatHangRepository.findByIdCanCuIn(idSearchReq.getIdList());
        if (!listKh.isEmpty()) {
            listKh.forEach(item -> {
                item.setIdCanCu(null);
            });
            xhXkKhXuatHangRepository.saveAll(listKh);
        }
        xhXkKhXuatHangRepository.deleteAll(list);
    }


    public XhXkKhXuatHang pheDuyet(CustomUserDetails currentUser, StatusReq req) throws Exception {
        Optional<XhXkKhXuatHang> dx = xhXkKhXuatHangRepository.findById(req.getId());
        if (!dx.isPresent()) {
            throw new Exception("Không tồn tại bản ghi");
        }
        XhXkKhXuatHang xhXkKhXuatHang = dx.get();
        String status = xhXkKhXuatHang.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.DU_THAO + Contains.CHO_DUYET_TP:
            case Contains.TU_CHOI_TP + Contains.CHO_DUYET_TP:
            case Contains.TU_CHOI_LDC + Contains.CHO_DUYET_TP:
                break;
            case Contains.CHO_DUYET_TP + Contains.TU_CHOI_TP:
            case Contains.CHO_DUYET_LDC + Contains.CHO_DUYET_TP:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
            case Contains.CHO_DUYET_BTC + Contains.TU_CHOI_BTC:
            case Contains.CHODUYET_LDTC + Contains.TUCHOI_LDTC:
                xhXkKhXuatHang.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHO_DUYET_LDC + Contains.DA_DUYET_LDC:
            case Contains.CHO_DUYET_TP + Contains.CHO_DUYET_LDC:
            case Contains.CHODUYET_LDV + Contains.CHODUYET_LDTC:
            case Contains.CHODUYET_LDTC + Contains.CHO_DUYET_BTC:
            case Contains.CHO_DUYET_BTC + Contains.DA_DUYET_BTC:
            case Contains.DU_THAO + Contains.CHODUYET_LDV:
                xhXkKhXuatHang.setNguoiDuyetId(currentUser.getUser().getId());
                xhXkKhXuatHang.setNgayDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công.");
        }
        xhXkKhXuatHang.setTrangThai(req.getTrangThai());
        //Check nếu là bản ghi kế hoạch của TỔng cục thì update trang thái bản ghi TH thành đã gửi duyệt KH
        if (xhXkKhXuatHang.getCapDvi() == 1 && xhXkKhXuatHang.getLoai().equals("00")) {
            Optional<XhXkKhXuatHang> itemTh = xhXkKhXuatHangRepository.findById(xhXkKhXuatHang.getIdCanCu());
            if (itemTh.isPresent() && !itemTh.get().getTrangThai().equals(TrangThaiAllEnum.DAGUIDUYET_KH.getId())) {
                itemTh.get().setTrangThai(TrangThaiAllEnum.DAGUIDUYET_KH.getId());
                xhXkKhXuatHangRepository.save(itemTh.get());
            }
        }
        XhXkKhXuatHang model = xhXkKhXuatHangRepository.save(xhXkKhXuatHang);
        return detail(model.getId());
    }


    public void export(CustomUserDetails currentUser, XhXkKhXuatHangRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        List<XhXkKhXuatHang> data = this.searchPage(currentUser, objReq).getContent();
        String title, fileName = "";
        String[] rowsName;
        Object[] objs;
        List<Object[]> dataList = new ArrayList<>();
        if (objReq.getLoai().equals("00")) {
            title = "Danh sách kế hoạch VT, TB có thời hạn lưu kho lớn hơn 12 tháng của Cục DTNN KV";
            fileName = "ds-ke-hoach-vt-tb-co-thoi-han-luu-kho-lon-hon-12-thang-cua-cuc-dtnn-kv.xlsx";
            rowsName = new String[]{"STT", "Năm kế hoạch", "Số KH/Tờ trình", "Ngày lập KH", "Ngày duyệt KH", "Trích yếu", "Số ĐV tài sản", "Trạng thái"};
            for (int i = 0; i < data.size(); i++) {
                XhXkKhXuatHang dx = data.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = dx.getNamKeHoach();
                objs[2] = dx.getSoToTrinh();
                objs[3] = dx.getNgayKeHoach();
                objs[4] = dx.getNgayDuyetKeHoach();
                objs[5] = dx.getTrichYeu();
                objs[6] = dx.getSoDviTaiSan();
                objs[7] = dx.getTenTrangThai();
                dataList.add(objs);
            }
        } else {
            title = "Danh sách tổng hợp kế hoạch xuất vật tư, thiết bị có thời hạn lưu kho lớn hơn 12";
            fileName = "ds-tong-hop-ke-hoach-vt-tb-co-thoi-han-luu-kho-lon-hon-12-thang.xlsx";
            rowsName = new String[]{"STT", "Mã tổng hợp", "Thời gian tổng hợp", "Nội dung tổng hợp", "Năm kế hoạch", "Trạng thái"};
            for (int i = 0; i < data.size(); i++) {
                XhXkKhXuatHang dx = data.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = dx.getId();
                objs[2] = dx.getThoiGianTh();
                objs[3] = dx.getNoiDungTh();
                objs[4] = dx.getNamKeHoach();
                objs[5] = dx.getTenTrangThai();
                dataList.add(objs);
            }
        }

        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }


    public XhXkTongHopKhXuatHangDTO searchListForTh(XhXkKhXuatHangRequest req) throws Exception {
        XhXkTongHopKhXuatHangDTO resp = new XhXkTongHopKhXuatHangDTO();
        List<XhXkKhXuatHang> listKeHoachs = xhXkKhXuatHangRepository.searchListTh(req);
        if (listKeHoachs.isEmpty() || listKeHoachs.size() == 0)
            throw new Exception("Không tìm thấy dữ liệu kế hoạch của cục");

        ArrayList<XhXkKhXuatHangDtl> listSumAllDtl = new ArrayList<>();
        ArrayList<XhXkTongHopKhXuatCuc> listSumDtlByCuc = new ArrayList<>();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, Long> mapCount = new HashMap<>();
        List<String> soTotrinhs = listKeHoachs.stream().map(XhXkKhXuatHang::getSoToTrinh).collect(Collectors.toList());
        List<Long> idKeHoachs = listKeHoachs.stream().map(XhXkKhXuatHang::getId).collect(Collectors.toList());
        resp.setListIdKeHoachs(idKeHoachs);
        resp.setListSoKeHoachs(soTotrinhs);
        listKeHoachs.forEach(s -> {
            s.getXhXkKhXuatHangDtl().forEach(it -> {
                it.setMapDmucDvi(mapDmucDvi);
                it.setMapVthh(mapVthh);
            });
            listSumAllDtl.addAll(s.getXhXkKhXuatHangDtl());
        });
        mapCount = listSumAllDtl.stream()
                .collect(Collectors.groupingBy(XhXkKhXuatHangDtl::getTenCuc, Collectors.counting()));
        if (!mapCount.isEmpty()) {
            for (Map.Entry<String, Long> entry : mapCount.entrySet()) {
                XhXkTongHopKhXuatCuc item = new XhXkTongHopKhXuatCuc();
                item.setTenDvi(entry.getKey());
                item.setSoDviTaiSan(entry.getValue());
                item.setTenTrangThai(TrangThaiAllEnum.DA_DUYET_LDC.getTen());
                item.setTrangThai(TrangThaiAllEnum.DA_DUYET_LDC.getId());
                listSumDtlByCuc.add(item);
            }
        }
        resp.setXhXkKhXuatHangDtl(listSumAllDtl);
        resp.setListDxCuc(listSumDtlByCuc);
        return resp;
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            ReportTemplateRequest reportTemplateRequest = new ReportTemplateRequest();
            reportTemplateRequest.setFileName(DataUtils.safeToString(body.get("tenBaoCao")));
            ReportTemplate model = findByTenFile(reportTemplateRequest);
            byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
//            FileInputStream inputStream = new FileInputStream("src/main/resources/reports/xuatcuutrovientro/Phiếu kiểm nghiệm chất lượng.docx");
            XhXkKhXuatHang  detail = this.detailTongHop(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }
}
