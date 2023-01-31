package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;


public interface XhDxKhBanDauGiaDtlRepository extends JpaRepository<XhDxKhBanDauGiaDtl,Long> {

    void deleteAllByIdHdr(Long idHdt);

    List<XhDxKhBanDauGiaDtl> findAllByIdHdr(Long idHdt);

    @Transactional
    void deleteAllByIdHdrIn(List<Long> idHdtList);
}
