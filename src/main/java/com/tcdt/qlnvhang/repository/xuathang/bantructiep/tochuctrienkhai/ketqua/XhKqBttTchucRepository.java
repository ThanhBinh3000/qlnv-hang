package com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttTchuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface XhKqBttTchucRepository extends JpaRepository<XhKqBttTchuc, Long> {

    void deleteAllByIdDdiem(Long idDdiem);

    @Transactional
    List<XhKqBttTchuc> findAllByIdDdiem(Long idDdiem);

}
