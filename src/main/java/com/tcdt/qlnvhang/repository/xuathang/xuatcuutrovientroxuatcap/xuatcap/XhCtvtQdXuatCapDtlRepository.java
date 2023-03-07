package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface XhCtvtQdXuatCapDtlRepository extends JpaRepository<XhCtvtQdXuatCapDtl, Long> {
    List<XhCtvtQdXuatCapDtl> findByHdrId(Long hdrId);

    List<XhCtvtQdXuatCapDtl> findAllByHdrIdIn(List<Long> hdrId);

    @Transactional
    void deleteAllByHdrId(Long hdrId);


}
