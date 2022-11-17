package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhieuKnghiemCluongHangRepository extends BaseRepository<PhieuKnghiemCluongHang, Long> {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    List<PhieuKnghiemCluongHang> findBySoQdGiaoNvNhAndMaDvi(String idQdGiaoNvNh, String maDvi);


//    Optional<PhieuKnghiemCluongHang> findFirstBySoPhieu(String soPhieu);
}
