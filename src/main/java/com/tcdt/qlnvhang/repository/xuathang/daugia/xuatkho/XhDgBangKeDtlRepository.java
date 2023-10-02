package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDgBangKeDtlRepository extends JpaRepository<XhDgBangKeDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhDgBangKeDtl> findAllByIdHdr(Long idHdr);

    List<XhDgBangKeDtl> findByIdHdrIn(List<Long> listId);
}
