package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface XhDxKhBanDauGiaDtlRepository extends JpaRepository<XhDxKhBanDauGiaDtl,Long> {

    List<XhDxKhBanDauGiaDtl> findAllByIdHdr(Long ids);

    List<XhDxKhBanDauGiaDtl> findAllByIdHdrIn(List<Long> ids);
}
