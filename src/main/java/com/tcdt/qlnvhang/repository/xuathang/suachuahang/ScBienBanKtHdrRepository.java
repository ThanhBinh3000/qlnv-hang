package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBienBanKtHdr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScBienBanKtHdrRepository extends JpaRepository<ScBienBanKtHdr, Long> {

    List<ScBienBanKtHdr> findAllByIdQdNh(Long idQdNh);
}
