package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface QlnvDmVattuRepository extends CrudRepository<QlnvDmVattu, Long> {
    Set<QlnvDmVattu> findByMaIn(Collection<String> maVatTus);

    QlnvDmVattu findByMa(String maVatTu);

    @Query(value = "SELECT * from DM_VATTU where TRANG_THAI = '01' AND IS_LOAI_KHOI_DM = :loaiKhoiDm and " +
            "ma not in (select ma from xh_xk_ds_hang_dtqg_dtl dtl,xh_xk_ds_hang_dtqg_hdr hdr where hdr.id = dtl.id_hdr and hdr.loai = '01')", nativeQuery = true)
    List<QlnvDmVattu> listHangDtqg(Integer loaiKhoiDm);
}
