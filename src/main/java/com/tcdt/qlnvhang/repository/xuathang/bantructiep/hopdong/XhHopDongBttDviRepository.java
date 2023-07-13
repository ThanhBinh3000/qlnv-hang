package com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDvi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface XhHopDongBttDviRepository extends JpaRepository<XhHopDongBttDvi, Long> {

    @Transactional
    void deleteAllByIdDtl(Long idDtl);

    List<XhHopDongBttDvi> findAllByIdDtl (Long idDtl);

    @Transactional
    void deleteAllByIdHdr(Long idHdr);

    @Transactional
    List<XhHopDongBttDvi> findAllByIdHdr(Long idHdr);

}
