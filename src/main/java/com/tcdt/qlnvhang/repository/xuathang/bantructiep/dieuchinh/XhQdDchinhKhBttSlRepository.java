package com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdDchinhKhBttSlRepository extends JpaRepository<XhQdDchinhKhBttSl, Long> {

    void deleteAllByIdDtl(Long idDtl);

    List<XhQdDchinhKhBttSl> findAllByIdDtl(Long idDtl);

    List<XhQdDchinhKhBttSl> findByIdDtlIn(List<Long> listId);
}
