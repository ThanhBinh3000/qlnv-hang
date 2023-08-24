package com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdDchinhKhBttSlDtlRepository extends JpaRepository<XhQdDchinhKhBttSlDtl, Long> {

    void deleteAllByIdSl(Long idSl);

    List<XhQdDchinhKhBttSlDtl> findAllByIdSl(Long idSl);

    List<XhQdDchinhKhBttSlDtl> findByIdSlIn(List<Long> listId);
}
