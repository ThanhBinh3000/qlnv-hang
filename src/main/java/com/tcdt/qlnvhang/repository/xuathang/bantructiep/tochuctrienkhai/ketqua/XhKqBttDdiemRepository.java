package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDdiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface XhKqBttDdiemRepository extends JpaRepository<XhKqBttDdiem, Long> {

    void deleteAllByIdDtl(Long idDtl);

    @Transactional
    List<XhKqBttDdiem> findAllByIdDtl(Long idDtl);

}
