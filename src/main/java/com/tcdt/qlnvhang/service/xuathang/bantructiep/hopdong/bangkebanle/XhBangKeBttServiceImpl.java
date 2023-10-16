package com.tcdt.qlnvhang.service.xuathang.bantructiep.hopdong.bangkebanle;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.bangkebanle.XhBangKeBtt;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.bangkebanle.XhBangKeBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.bangkebanle.XhBangKeBttReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhBangKeBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhBangKeBttRepository xhBangKeBttRepository;

    public Page<XhBangKeBtt> searchPage(CustomUserDetails currentUser, XhBangKeBttReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC) || currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhBangKeBtt> search = xhBangKeBttRepository.searchPage(req, pageable);
        Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        search.getContent().forEach(data -> {
            data.setMapVthh(mapDmucVthh);
            data.setMapDmucDvi(mapDmucDvi);
        });
        return search;
    }

    @Transactional
    public XhBangKeBtt create(CustomUserDetails currentUser, XhBangKeBttReq req) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        XhBangKeBtt data = new XhBangKeBtt();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        XhBangKeBtt created = xhBangKeBttRepository.save(data);
        return created;
    }

    public List<XhBangKeBtt> detail(List<Long> ids) throws Exception {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("Tham số không hợp lệ.");
        }
        List<XhBangKeBtt> list = xhBangKeBttRepository.findByIdIn(ids);
        if (list.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        list.forEach(data -> {
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
        });
        return list;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhBangKeBtt> optional = xhBangKeBttRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        xhBangKeBttRepository.delete(optional.get());
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<Long> idList = idSearchReq.getIdList();
        if (idList == null || idList.isEmpty()) {
            throw new Exception("Danh sách ID trống hoặc không hợp lệ.");
        }
        List<XhBangKeBtt> list = xhBangKeBttRepository.findAllByIdIn(idList);
        if (list.isEmpty()) {
            throw new Exception("Không tìm thấy bản ghi nào để xóa.");
        }
        xhBangKeBttRepository.deleteAll(list);
    }

    public void export(CustomUserDetails currentUser, XhBangKeBttReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhBangKeBtt> page = this.searchPage(currentUser, req);
        List<XhBangKeBtt> data = page.getContent();
        String title = "Danh sách bảng kê bán lẻ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số bảng kê",
                "Số quyết định", "Tên người mua", "Địa chỉ", "Số CMT/CCCD",
                "Loại hàng hóa", "Chủng loại hàng hóa", "Số lượng",
                "Đơn giá", "Thanh tiền"};
        String filename = "danh-sach-dx-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<>();
        data.stream().forEach(hdr -> {
            Object[] objs = new Object[rowsName.length];
            objs[0] = data.indexOf(hdr) + 1;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoBangKe();
            objs[3] = hdr.getSoQdNv();
            objs[4] = hdr.getTenBenMua();
            objs[5] = hdr.getDiaChi();
            objs[6] = hdr.getCmtBenMua();
            objs[7] = hdr.getTenLoaiVthh();
            objs[8] = hdr.getTenCloaiVthh();
            objs[9] = hdr.getSoLuong();
            objs[10] = hdr.getDonGia();
            objs[11] = hdr.getThanhTien();
            dataList.add(objs);
        });
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}