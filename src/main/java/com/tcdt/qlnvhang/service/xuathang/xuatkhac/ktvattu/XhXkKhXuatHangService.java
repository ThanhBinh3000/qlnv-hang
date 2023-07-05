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
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
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
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
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
        search.getContent().forEach(s -> {
            s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
            s.setSoDviTaiSan(s.getXhXkKhXuatHangDtl().size());
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
        data.setMaDvi(currentUser.getUser().getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.getXhXkKhXuatHangDtl().forEach(s -> s.setXhXkKhXuatHang(data));
        XhXkKhXuatHang created = xhXkKhXuatHangRepository.save(data);
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkKhXuatHang.TABLE_NAME);
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
        });
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setSoDviTaiSan(model.getXhXkKhXuatHangDtl().size());
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }


    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkKhXuatHang> optional = xhXkKhXuatHangRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkKhXuatHang data = optional.get();
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
                xhXkKhXuatHang.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHO_DUYET_LDC + Contains.DA_DUYET_LDC:
            case Contains.CHO_DUYET_TP + Contains.CHO_DUYET_LDC:
                xhXkKhXuatHang.setNguoiDuyetId(currentUser.getUser().getId());
                xhXkKhXuatHang.setNgayDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công.");
        }
        xhXkKhXuatHang.setTrangThai(req.getTrangThai());
        XhXkKhXuatHang model = xhXkKhXuatHangRepository.save(xhXkKhXuatHang);
        return detail(model.getId());
    }


    public void export(CustomUserDetails currentUser, XhXkKhXuatHangRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        List<XhXkKhXuatHang> data = this.searchPage(currentUser, objReq).getContent();

        String title = "Danh sách kế hoạch VT, TB có thời hạn lưu kho lớn hơn 12 tháng của Cục DTNN KV";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số KH/Tờ trình", "Ngày lập KH", "Ngày duyệt KH", "Trích yếu", "Số ĐV tài sản", "Trạng thái"};
        String fileName = "ds-ke-hoach-vt-tb-co-thoi-han-luu-kho-lon-hon-12-thang-cua-cuc-dtnn-kv.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs;
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
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
