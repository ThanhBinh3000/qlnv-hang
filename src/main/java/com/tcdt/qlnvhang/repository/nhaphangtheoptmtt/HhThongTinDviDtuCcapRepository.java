package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhThongTinDviDtuCcap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhThongTinDviDtuCcapRepository extends JpaRepository<HhThongTinDviDtuCcap,Long> {
    List<HhThongTinDviDtuCcap> findAllByIdHdr(Long idHdr);

    List<HhThongTinDviDtuCcap> findAllByIdHdrInAndTypeIn(List<Long> idHdr,List<String> type);
    List<HhThongTinDviDtuCcap> findAllByIdHdrAndType(Long idHdr,String type);
    List<HhThongTinDviDtuCcap> findAllByIdHdrIn(List<Long> idHdr);
}
