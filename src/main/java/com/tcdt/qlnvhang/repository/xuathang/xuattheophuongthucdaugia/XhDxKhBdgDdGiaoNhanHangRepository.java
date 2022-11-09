package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBdgDdGiaoNhanHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhDxKhBdgDdGiaoNhanHangRepository extends JpaRepository<XhDxKhBdgDdGiaoNhanHang,Long> {

    List<XhDxKhBdgDdGiaoNhanHang> findAllByIdHdr(Long ids);

    List<XhDxKhBdgDdGiaoNhanHang> findAllByIdHdrIn(List<Long> ids);
}
