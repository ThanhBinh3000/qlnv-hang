package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.HhDxKhlcntDsgthau;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaPhanLo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface XhDxKhBanDauGiaPhanLoRepository extends JpaRepository<XhDxKhBanDauGiaPhanLo,Long> {

    void deleteAllByIdDxKhbdg(Long idDxKhbdg);

    List<XhDxKhBanDauGiaPhanLo> findByIdDxKhbdg(Long idDxKhbdg);

    @Transactional
    void deleteAllByIdDxKhbdgIn(List<Long> idDxKhbdg);


}
