package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttDviDtlRepository extends JpaRepository<XhQdPdKhBttDviDtl, Long> {

    void deleteAllByIdDvi(Long idDvi);

    List<XhQdPdKhBttDviDtl> findAllByIdDvi(Long idDvi);

    List<XhQdPdKhBttDviDtl> findByIdDviIn(List<Long> listId);

}
