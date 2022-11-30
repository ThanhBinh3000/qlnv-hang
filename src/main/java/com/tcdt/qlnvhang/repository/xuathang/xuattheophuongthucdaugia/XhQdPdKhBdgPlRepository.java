package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgPl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhQdPdKhBdgPlRepository extends JpaRepository<XhQdPdKhBdgPl,Long> {


    List<XhQdPdKhBdgPl> findAllByIdQdDtlIn (List<Long> ids);
     List<XhQdPdKhBdgPl> findByIdQdDtl(Long idQdDtl);

    void deleteByIdQdDtl(Long idQdDtl);
}
