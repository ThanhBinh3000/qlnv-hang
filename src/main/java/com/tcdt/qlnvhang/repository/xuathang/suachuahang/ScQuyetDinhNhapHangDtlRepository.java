package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHangDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScQuyetDinhNhapHangDtlRepository extends JpaRepository<ScQuyetDinhNhapHangDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<ScQuyetDinhNhapHangDtl> findAllByIdHdr(Long idHdr);


}
