package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.kehoach.pheduyet.XhQdPdKhBdgPl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBdgPlRepository extends JpaRepository<XhQdPdKhBdgPl,Long> {


    List<XhQdPdKhBdgPl> findAllByIdQdDtlIn (List<Long> ids);
     List<XhQdPdKhBdgPl> findByIdQdDtl(Long idQdDtl);

    void deleteByIdQdDtl(Long idQdDtl);
}
