package com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhHopDongBttDtlRepository extends JpaRepository<XhHopDongBttDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhHopDongBttDtl> findAllByIdHdr(Long idHdr);

    // Phụ Lục
    List<XhHopDongBttDtl> findAllByIdHdDtl(Long idHdDtl);
}
