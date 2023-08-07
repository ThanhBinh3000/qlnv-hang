package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface XhDxKhBanDauGiaDtlRepository extends JpaRepository<XhDxKhBanDauGiaDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhDxKhBanDauGiaDtl> findAllByIdHdr(Long idHdr);

    List<XhDxKhBanDauGiaDtl> findByIdHdrIn(List<Long> listId);
}
