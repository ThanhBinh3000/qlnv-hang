package com.tcdt.qlnvhang.service.xuathang.xuatkhac;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.XhXkDanhSachRepository;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkDanhSachRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.XhXkDanhSachHdr;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class XhXkDanhSachService extends BaseServiceImpl {
    @Autowired
    private XhXkDanhSachRepository xhXkDanhSachRepository;

    public Page<XhXkDanhSachHdr> searchPage(CustomUserDetails currentUser, XhXkDanhSachRequest req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setDvql(currentUser.getDvql());
        Page<XhXkDanhSachHdr> search = xhXkDanhSachRepository.searchPage(req, pageable);
        //set label
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
            s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
        });
        return search;
    }

    public List<XhXkDanhSachHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhXkDanhSachHdr> optional = xhXkDanhSachRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<XhXkDanhSachHdr> allById = xhXkDanhSachRepository.findAllById(ids);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        allById.forEach(data -> {
            data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));
            data.setMapDmucDvi(mapDmucDvi);
            data.setMapVthh(mapVthh);
        });
        return allById;
    }
}
