package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.kehoach.pheduyet.XhQdPdKhBdgPlDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBdgPlDtlRepository extends JpaRepository<XhQdPdKhBdgPlDtl,Long> {
    List<XhQdPdKhBdgPlDtl> findByIdPhanLo(Long idPhanLo);

    void deleteAllByIdPhanLo(Long idPhanLo);
}
