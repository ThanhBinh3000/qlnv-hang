package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDxKhBanTrucTiepDtlRepository extends JpaRepository<XhDxKhBanTrucTiepDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhDxKhBanTrucTiepDtl> findAllByIdHdr(Long idHdr);

    List<XhDxKhBanTrucTiepDtl> findByIdHdrIn(List<Long> listId);
}
