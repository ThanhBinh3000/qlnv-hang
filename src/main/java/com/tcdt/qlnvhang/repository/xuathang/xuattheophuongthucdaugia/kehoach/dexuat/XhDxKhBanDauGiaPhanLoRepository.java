package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.kehoach.dexuat;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface XhDxKhBanDauGiaPhanLoRepository extends JpaRepository<XhDxKhBanDauGiaPhanLo,Long> {

    void deleteAllByIdDtl(Long idDtl);

    List<XhDxKhBanDauGiaPhanLo> findByIdDtl(Long idDtl);

    @Transactional
    void deleteAllByIdDtlIn(List<Long> idDtl);


}
