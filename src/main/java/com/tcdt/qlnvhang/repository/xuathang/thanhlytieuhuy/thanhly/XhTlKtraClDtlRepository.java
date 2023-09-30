package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBienBanTkDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBienBanTkHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlKtraClDtlRepository extends JpaRepository<XhTlKtraClDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhTlKtraClDtl> findAllByIdHdrOrderByThuTuHt (Long id);
}