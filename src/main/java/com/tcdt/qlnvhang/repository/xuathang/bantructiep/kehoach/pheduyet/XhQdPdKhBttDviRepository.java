package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttDviRepository extends JpaRepository<XhQdPdKhBttDvi, Long> {

    void deleteAllByIdDtl(Long idDtl);

    List<XhQdPdKhBttDvi> findAllByIdDtl(Long idDtl);

    List<XhQdPdKhBttDvi> findByIdDtlIn(List<Long> listId);

    void deleteAllByIdQdKqHdr(Long idQdKqHdr);

    List<XhQdPdKhBttDvi> findAllByIdQdKqHdr(Long idQdKqHdr);

    List<XhQdPdKhBttDvi> findByIdQdKqHdrIn(List<Long> listId);
}
