package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface XhCtvtTongHopDtlRepository extends JpaRepository<XhCtvtTongHopDtl, Long> {
    List<XhCtvtTongHopDtl> findAllByXhCtvtTongHopHdrId(Long idHdr);
    List<XhCtvtTongHopDtl> findAllByXhCtvtTongHopHdrIdIn(List<Long> idHdr);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<XhCtvtTongHopDtl> ids);
}
