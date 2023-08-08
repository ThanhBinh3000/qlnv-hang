package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkDsHangDtqgRepository extends JpaRepository<XhXkDsHangDtqgHdr, Long> {
    @Query("SELECT distinct c FROM XhXkDsHangDtqgHdr c WHERE 1=1 " +
            "AND (:#{#param.loai} IS NULL OR c.loai  = :#{#param.loai}) " +
            "AND (:#{#param.maDs} IS NULL OR c.maDs LIKE CONCAT(:#{#param.maDs},'%')) " +
            "AND (:#{#param.tenDs} IS NULL OR c.tenDs LIKE CONCAT(:#{#param.tenDs},'%')) " +
            "AND ((:#{#param.ngayCapNhatTu}  IS NULL OR c.ngayTao >= :#{#param.ngayCapNhatTu})" +
            "AND  (:#{#param.ngayCapNhatDen}  IS NULL OR c.ngayTao <= :#{#param.ngayCapNhatDen})) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkDsHangDtqgHdr> searchPage(@Param("param") XhXkDsHangDtqgRequest param, Pageable pageable);

    void deleteAllByIdIn(List<Long> listId);

    List<XhXkDsHangDtqgHdr> findByIdIn(List<Long> ids);


}
