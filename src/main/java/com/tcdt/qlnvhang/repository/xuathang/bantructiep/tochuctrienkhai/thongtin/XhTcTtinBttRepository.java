package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTcTtinBttRepository extends JpaRepository<XhTcTtinBtt, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhTcTtinBtt> findAllByIdHdr(Long idHdr);
}
