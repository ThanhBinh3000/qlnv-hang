package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuKtcl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhNkPhieuKtclRepository extends JpaRepository<HhNkPhieuKtcl, Long> {
    HhNkPhieuKtcl findByIdQdGiaoNvNhAndMaLoKhoAndMaNganKhoAndSoBbNtBq(Long idQdGiaoNvNh, String maLoKho, String maNganKho, String soBbNtBq);
}
