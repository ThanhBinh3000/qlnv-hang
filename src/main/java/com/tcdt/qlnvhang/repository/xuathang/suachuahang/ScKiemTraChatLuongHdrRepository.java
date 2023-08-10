package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScKiemTraChatLuongReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.suachua.ScKiemTraChatLuongDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query(value = "SELECT qd FROM ScKiemTraChatLuongHdr qd " +
            " WHERE 1 = 1 " +
            " AND (qd.idPhieuXuatKho IN :#{#param.ids}) " +
            " AND (:#{#param.soPhieuKtcl} IS NULL OR qd.soPhieuKtcl LIKE CONCAT(:#{#param.soPhieuKtcl},'%')) " +
            " AND (:#{#param.trangThai} IS NULL OR qd.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.maDviSr} IS NULL OR qd.maDvi = :#{#param.maDviSr}) " +
            " AND ((:#{#param.ngayTu} IS NULL OR qd.ngayKiemDinh >= :#{#param.ngayTu}) AND (:#{#param.ngayDen}  IS NULL OR qd.ngayKiemDinh <= :#{#param.ngayDen})) "
    )
    List<ScKiemTraChatLuongHdr> findAllByIdPhieuXuatKho(@Param("param") ScKiemTraChatLuongReq req);

    List<ScKiemTraChatLuongHdr> findAllByIdQdXh(Long idQdXh);

    @Query(value = "SELECT qd FROM ScKiemTraChatLuongHdr qd " +
            " WHERE 1 = 1 " +
            " AND (:#{#param.idQdXh} IS NULL OR qd.idQdXh = :#{#param.idQdXh}) " +
            " AND (:#{#param.trangThai} IS NULL OR qd.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.maDviSr} IS NULL OR qd.maDvi = :#{#param.maDviSr}) " )
    List<ScKiemTraChatLuongHdr> searchListTaoQuyetDinhNhapHang(@Param("param") ScKiemTraChatLuongReq req);
}
