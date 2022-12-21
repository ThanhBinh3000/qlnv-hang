package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhThongTinDviDtuCcap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HhThongTinDviDtuCcapRepository extends JpaRepository<HhThongTinDviDtuCcap,Long> {
    void deleteAllByIdHdr(Long idHdr);

    @Transactional
    void deleteAllByIdHdrIn(List<Long> idHdr);
    List<HhThongTinDviDtuCcap> findAllByIdHdr(Long idHdr);
}
