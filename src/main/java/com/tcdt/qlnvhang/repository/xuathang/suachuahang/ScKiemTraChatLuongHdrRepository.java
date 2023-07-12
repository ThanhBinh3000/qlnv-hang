package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScKiemTraChatLuongReq;
import com.tcdt.qlnvhang.response.suachua.ScKiemTraChatLuongDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScKiemTraChatLuongHdrRepository extends JpaRepository<ScKiemTraChatLuongHdr, Long> {
//    @Query(value = "SELECT new com.tcdt.qlnvhang.response.suachua.ScKiemTraChatLuongDTO( " +
//            "hdr.id, qd.id, qd.soQd, hdr.nam, qd.thoiHanXuat,hdr.maDiemKho, hdr.tenDiemKho, hdr.maLoKho,hdr.tenLoKho, " +
//            "hdr.soPhieuKdcl,hdr.ngayKiemDinh, hdr.trangThai " +
//            ") FROM ScQuyetDinhXuatHang qd " +
//            "LEFT JOIN ScKiemTraChatLuongHdr hdr on hdr.soQdGiaoNvId = qd.id " +
//            "LEFT JOIN ScKiemTraChatLuongDtl dtl on dtl.hdrId = hdr.id " +
//            "WHERE 1 = 1 " +
//            "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
//            "AND (:#{#param.soQdGiaoNv} IS NULL OR qd.soQd = :#{#param.soQdGiaoNv}) " +
//            "AND (:#{#param.soPhieuKdcl} IS NULL OR hdr.soPhieuKdcl = :#{#param.soPhieuKdcl}) ")
//    Page<ScKiemTraChatLuongDTO> searchPage(@Param("param") ScKiemTraChatLuongReq req, Pageable pageable);


//    Optional<ScKiemTraChatLuongHdr> findBySoPhieuKdcl(String soPhieuKdcl);
}
