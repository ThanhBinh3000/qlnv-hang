package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPlDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBdgPlDtlRepository extends JpaRepository<XhQdPdKhBdgPlDtl, Long> {

    void deleteAllByIdPhanLo(Long idPhanLo);

    List<XhQdPdKhBdgPlDtl> findAllByIdPhanLo(Long idPhanLo);

    List<XhQdPdKhBdgPlDtl> findByIdPhanLoIn(List<Long> listId);
}
