package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.suachua.ScPhieuXuatKhoDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScPhieuXuatKhoHdrRepository extends JpaRepository<ScPhieuXuatKhoHdr, Long> {
    Optional<ScPhieuXuatKhoHdr> findBySoPhieuXuatKho(String soPhieuXuatKho);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.suachua.ScPhieuXuatKhoDTO(" +
            "pxkh.id , qd.id, qd.soQd, qd.nam, qd.thoiHanXuat,pxkh.maDiemKho, pxkh.tenDiemKho, pxkh.maLoKho, pxkh.tenLoKho," +
            "pxkh.soPhieuXuatKho, pxkh.ngayXuatKho, pxkh.soPhieuKiemDinhChatLuong, pxkh.ngayKyQdGiaoNv, pxkh.trangThai " +
            ") FROM ScQuyetDinhXuatHang qd " +
            "LEFT JOIN ScPhieuXuatKhoHdr pxkh on pxkh.soQdGiaoNv = qd.soQd " +
            "LEFT JOIN ScPhieuXuatKhoDtl pxkd on pxkd.hdrId = pxkh.id WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam})" +
            "AND (:#{#param.soQdGiaoNv} IS NULL OR qd.soQd = :#{#param.soQdGiaoNv})" +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR pxkh.soPhieuXuatKho = :#{#param.soPhieuXuatKho})")
    Page<ScPhieuXuatKhoDTO> searchPage(@Param("param") ScPhieuXuatKhoReq req, Pageable pageable);
}
