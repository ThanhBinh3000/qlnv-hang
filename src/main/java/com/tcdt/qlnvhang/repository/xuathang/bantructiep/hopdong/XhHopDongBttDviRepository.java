package com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDvi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhHopDongBttDviRepository extends JpaRepository<XhHopDongBttDvi, Long> {

    void deleteAllByIdDtl(Long idDtl);

    List<XhHopDongBttDvi> findAllByIdDtl(Long idDtl);

    // Cấp Chi Cục
    void deleteAllByIdHdr(Long idHdr);

    List<XhHopDongBttDvi> findAllByIdHdr(Long idHdr);
}
