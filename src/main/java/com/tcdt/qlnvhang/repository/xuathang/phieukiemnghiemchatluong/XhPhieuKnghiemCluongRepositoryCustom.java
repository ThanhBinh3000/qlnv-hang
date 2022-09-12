package com.tcdt.qlnvhang.repository.xuathang.phieukiemnghiemchatluong;

import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuKnghiemCluongSearchReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface XhPhieuKnghiemCluongRepositoryCustom {
    List<Object[]> search(XhPhieuKnghiemCluongSearchReq req);

    int count(XhPhieuKnghiemCluongSearchReq req);
}
