package com.tcdt.qlnvhang.service.xuathang.phieukiemnghiemchatluong;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuKnghiemCluongSearchReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.xuathang.phieuknghiemcluonghang.XhPhieuKnghiemCluongRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Set;

public interface XhPhieuKnghiemCluongService {
    XhPhieuKnghiemCluongRes create(XhPhieuKnghiemCluongReq req) throws Exception;

    XhPhieuKnghiemCluongRes update(XhPhieuKnghiemCluongReq req) throws Exception;

    XhPhieuKnghiemCluongRes detail(Long id) throws Exception;

    boolean delete(Long id) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    boolean updateStatus(StatusReq req) throws Exception;

    Page<XhPhieuKnghiemCluongRes> search(XhPhieuKnghiemCluongSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(XhPhieuKnghiemCluongSearchReq objReq, HttpServletResponse response) throws Exception;

    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
}
