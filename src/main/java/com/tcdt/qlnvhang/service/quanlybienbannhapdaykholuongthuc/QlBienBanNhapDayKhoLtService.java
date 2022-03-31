package com.tcdt.qlnvhang.service.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtReq;
import com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtSearchReq;
import com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtRes;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;

public interface QlBienBanNhapDayKhoLtService {
    QlBienBanNhapDayKhoLtRes create(QlBienBanNhapDayKhoLtReq req) throws Exception;
    QlBienBanNhapDayKhoLtRes update(QlBienBanNhapDayKhoLtReq req) throws Exception;
    QlBienBanNhapDayKhoLtRes detail(Long id) throws Exception;
    boolean delete(Long id) throws Exception;
    Page<QlBienBanNhapDayKhoLtRes> search(QlBienBanNhapDayKhoLtSearchReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;
}
