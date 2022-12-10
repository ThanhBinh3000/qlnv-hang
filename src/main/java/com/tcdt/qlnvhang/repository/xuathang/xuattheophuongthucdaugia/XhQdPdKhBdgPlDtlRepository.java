package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgPlDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBdgPlDtlRepository extends JpaRepository<XhQdPdKhBdgPlDtl,Long> {
    List<XhQdPdKhBdgPlDtl> findByIdPhanLo(Long idPhanLo);
    List<XhQdPdKhBdgPlDtl> findByIdQdHdr(Long id);


    void deleteAllByIdPhanLo(Long idPhanLo);
}
