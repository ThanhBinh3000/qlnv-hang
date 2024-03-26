package com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaReq;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface XhTcTtinBdgHdrRepository extends JpaRepository<XhTcTtinBdgHdr, Long> {

    @Query("SELECT TT FROM XhTcTtinBdgHdr TT " +
            "WHERE (:#{#param.dvql} IS NULL OR TT.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR TT.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdPd} IS NULL OR LOWER(TT.soQdPd) LIKE LOWER(CONCAT('%', :#{#param.soQdPd}, '%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR TT.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trangThai} IS NULL OR TT.trangThai = :#{#param.trangThai}) " +
            "ORDER BY TT.nam DESC, TT.ngaySua DESC, TT.ngayTao DESC, TT.id DESC")
    Page<XhTcTtinBdgHdr> searchPage(@Param("param") ThongTinDauGiaReq param, Pageable pageable);

    List<XhTcTtinBdgHdr> findAllBySoQdPd(String soQdPd);

    List<XhTcTtinBdgHdr> findByIdQdPdDtlOrderByLanDauGia(Long idQdPdDtl);

    List<XhTcTtinBdgHdr> findByIdIn(List<Long> ids);
}