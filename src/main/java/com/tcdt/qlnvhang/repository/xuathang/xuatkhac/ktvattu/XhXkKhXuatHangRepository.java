package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkTongHopRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkKhXuatHangRepository extends JpaRepository<XhXkKhXuatHang, Long> {
    @Query("SELECT distinct c FROM XhXkKhXuatHang c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND ((:#{#param.ngayKeHoachTu}  IS NULL OR c.ngayKeHoach >= :#{#param.ngayKeHoachTu})" +
            "AND  (:#{#param.ngayKeHoachDen}  IS NULL OR c.ngayKeHoach <= :#{#param.ngayKeHoachDen})) " +
            "AND ((:#{#param.ngayDuyetKeHoachTu}  IS NULL OR c.ngayDuyetKeHoach >= :#{#param.ngayDuyetKeHoachTu})" +
            "AND  (:#{#param.ngayDuyetKeHoachDen}  IS NULL OR c.ngayDuyetKeHoach <= :#{#param.ngayDuyetKeHoachDen})) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkKhXuatHang> searchPage(@Param("param") XhXkKhXuatHangRequest param, Pageable pageable);


    void deleteAllByIdIn(List<Long> listId);

    List<XhXkKhXuatHang> findByIdIn(List<Long> ids);

    Optional<XhXkKhXuatHang> findBySoToTrinh(String soToTrinh);
}
