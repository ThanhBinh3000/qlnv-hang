package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTcTtinBttRepository extends JpaRepository<XhTcTtinBtt, Long> {

    void deleteAllByIdDviDtl(Long idDviDtl);

    List<XhTcTtinBtt> findAllByIdDviDtl(Long idDviDtl);

    List<XhTcTtinBtt> findByIdDviDtlIn(List<Long> listId);

    List<XhTcTtinBtt> findAllByOrderByTochucCanhan();
}