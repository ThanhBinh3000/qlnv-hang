package com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdDchinhKhBttSlRepository extends JpaRepository<XhQdDchinhKhBttSl, Long> {

    void deleteAllByIdDtl(Long idDtl);


    List<XhQdDchinhKhBttSl> findByIdDtl(Long idDtl);
}
