package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NhBangKeCanHangRepository extends BaseRepository<NhBangKeCanHang, Long> {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhBangKeCanHang> findFirstBySoBangKe(String soBangKe);

    List<NhBangKeCanHang> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    List<NhBangKeCanHang> findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    NhBangKeCanHang findBySoPhieuNhapKho(String soPhieuNhapKho);

}
