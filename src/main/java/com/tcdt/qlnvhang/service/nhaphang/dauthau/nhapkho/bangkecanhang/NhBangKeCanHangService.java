package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.service.BaseService;

import java.util.List;

public interface NhBangKeCanHangService extends BaseService<NhBangKeCanHang,QlBangKeCanHangLtReq,Long> {

    List<NhBangKeCanHang> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    List<NhBangKeCanHang> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

//    QlBangKeCanHangLtRes create(QlBangKeCanHangLtReq req) throws Exception;
//
//    QlBangKeCanHangLtRes update(QlBangKeCanHangLtReq req) throws Exception;
//
//    QlBangKeCanHangLtRes detail(Long id) throws Exception;
//
//    boolean delete(Long id) throws Exception;
//
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean updateStatusQd(StatusReq req) throws Exception;
//
//    Page<QlBangKeCanHangLtRes> search(QlBangKeCanHangLtSearchReq req) throws Exception;
//
//    BaseNhapHangCount count() throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
//    boolean exportToExcel(QlBangKeCanHangLtSearchReq objReq, HttpServletResponse response) throws Exception;
//
//    Integer getSo() throws Exception;
//
//    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
}
