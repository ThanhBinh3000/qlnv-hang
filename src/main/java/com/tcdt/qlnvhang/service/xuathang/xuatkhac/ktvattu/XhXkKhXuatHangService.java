package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkDanhSachRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkTongHopRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkKhXuatHangService extends BaseServiceImpl {
    @Autowired
    private XhXkKhXuatHangRepository xhXkKhXuatHangRepository;

    public Page<XhXkKhXuatHang> searchPage(CustomUserDetails currentUser, XhXkKhXuatHangRequest req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setDvql(currentUser.getDvql());
        Page<XhXkKhXuatHang> search = xhXkKhXuatHangRepository.searchPage(req, pageable);
        //set label
//        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
//        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
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
        data.getListXhXkKhXuatHangDtl().forEach(s -> s.setXhXkKhXuatHang(data));
        XhXkKhXuatHang created = xhXkKhXuatHangRepository.save(data);
        created = xhXkKhXuatHangRepository.save(created);
        return created;

    }

    public XhXkKhXuatHang detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkKhXuatHang> optional = xhXkKhXuatHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkKhXuatHang model = optional.get();
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
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
            objs[6] = dx.getSoDvTaiSan();
            objs[7] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
