package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBdgDtlRepository extends JpaRepository<XhQdPdKhBdgDtl,Long> {
    List<XhQdPdKhBdgDtl> findAllByIdQdPd(Long ids);

    List<XhQdPdKhBdgDtl> findAllByIdQdPdIn(List<Long> ids);
}
