package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScKiemTraChatLuongDtlRepository extends JpaRepository<ScKiemTraChatLuongDtl,Long> {
    List<ScKiemTraChatLuongDtl> findAllByIdHdr(Long id);

    void deleteAllByIdHdr(Long idHdr);

}
