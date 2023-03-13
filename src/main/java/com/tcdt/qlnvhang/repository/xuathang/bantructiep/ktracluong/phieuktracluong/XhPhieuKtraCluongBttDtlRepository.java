package com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhPhieuKtraCluongBttDtlRepository extends JpaRepository<XhPhieuKtraCluongBttDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhPhieuKtraCluongBttDtl> findAllByIdHdr (Long idHdr);

}
