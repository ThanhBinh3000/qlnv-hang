package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface XhKqBttDtlRepository extends JpaRepository<XhKqBttDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    @Transactional
    List<XhKqBttDtl> findAllByIdHdr(Long idHdr);


}
