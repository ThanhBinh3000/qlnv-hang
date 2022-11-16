package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.HhDxKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;


public interface XhDxKhBanDauGiaDtlRepository extends JpaRepository<XhDxKhBanDauGiaDtl,Long> {

    void deleteAllByIdPhanLo(Long idPhanLo);

    List<XhDxKhBanDauGiaDtl> findByIdPhanLo(Long idPhanLo);

    @Transactional
    void deleteAllByIdPhanLoIn(List<Long> idPhanLoList);
}
