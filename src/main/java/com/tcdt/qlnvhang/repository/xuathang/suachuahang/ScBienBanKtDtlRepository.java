package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBienBanKtDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBienBanKtHdr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScBienBanKtDtlRepository extends JpaRepository<ScBienBanKtDtl, Long> {

    List<ScBienBanKtDtl> findByIdHdr(Long idHdr);

    void deleteAllByIdHdr(Long idHdr);


}
