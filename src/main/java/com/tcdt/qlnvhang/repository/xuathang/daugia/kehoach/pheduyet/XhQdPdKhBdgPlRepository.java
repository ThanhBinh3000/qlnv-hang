package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgPl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBdgPlRepository extends JpaRepository<XhQdPdKhBdgPl,Long> {

    void deleteAllByIdQdDtl(Long idQdDtl);

    List<XhQdPdKhBdgPl> findAllByIdQdDtl(Long idQdDtl);
}
