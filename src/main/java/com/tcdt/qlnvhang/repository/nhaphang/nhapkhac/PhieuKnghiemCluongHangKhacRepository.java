package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.phieuknghiemcl.PhieuKnghiemCluongHangKhac;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhieuKnghiemCluongHangKhacRepository extends BaseRepository<PhieuKnghiemCluongHangKhac, Long> {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    List<PhieuKnghiemCluongHangKhac> findBySoQdGiaoNvNhAndMaDvi(String idQdGiaoNvNh, String maDvi);

    Optional<PhieuKnghiemCluongHangKhac> findByMaLoKho(String maLoKho);


//    Optional<PhieuKnghiemCluongHang> findFirstBySoPhieu(String soPhieu);
}
