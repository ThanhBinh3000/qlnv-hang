package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkTongHopRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkThXuatHangKdmRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkThXuatHangKdmHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkThXuatHangKdmRepository extends JpaRepository<XhXkThXuatHangKdmHdr, Long> {
    @Query("SELECT distinct c FROM XhXkThXuatHangKdmHdr c left join c.tongHopDtl h WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.maDanhSach} IS NULL OR LOWER(c.maDanhSach) LIKE CONCAT('%',LOWER(:#{#param.maDanhSach}),'%')) " +
            "AND (:#{#param.maCuc} IS NULL OR substring(h.maDiaDiem,0,6) = :#{#param.maCuc}) " +
            "AND (:#{#param.maChiCuc} IS NULL OR substring(h.maDiaDiem,0,8) = :#{#param.maChiCuc}) " +
            "AND ((:#{#param.ngayTaoTu}  IS NULL OR c.ngayTao >= :#{#param.ngayTaoTu})" +
            "AND  (:#{#param.ngayTaoDen}  IS NULL OR c.ngayTao <= :#{#param.ngayTaoDen})) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkThXuatHangKdmHdr> searchPage(@Param("param") XhXkThXuatHangKdmRequest param, Pageable pageable);


    void deleteAllByIdIn(List<Long> listId);

    List<XhXkThXuatHangKdmHdr> findByIdIn(List<Long> ids);

    Optional<XhXkThXuatHangKdmHdr> findByMaDanhSach(String maDs);
}
