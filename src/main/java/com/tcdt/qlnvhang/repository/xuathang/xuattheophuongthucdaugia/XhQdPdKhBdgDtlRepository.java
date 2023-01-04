package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBdgDtlRepository extends JpaRepository<XhQdPdKhBdgDtl,Long> {
    List<XhQdPdKhBdgDtl> findAllByIdQdHdrIn (List<Long> ids);
    List<XhQdPdKhBdgDtl> findAllByIdQdHdr (Long idQdHdr);

    void deleteAllByIdQdHdr(Long idQdHdr);
}
