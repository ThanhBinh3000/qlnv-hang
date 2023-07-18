package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.response.suachua.ScPhieuNhapKhoDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScPhieuNhapKhoHdrRepository extends JpaRepository<ScPhieuNhapKhoHdr, Long> {

    Optional<ScPhieuNhapKhoHdr> findBySoPhieuNhapKho(String soPhieuNhapKho);

    List<ScPhieuNhapKhoHdr> findAllByIdScDanhSachHdrAndIdQdNh(Long idScDanhSachHdr,Long idQdNh);

    @Query(value = "SELECT qd FROM ScPhieuNhapKhoHdr qd " +
            " LEFT JOIN ScBangKeNhapVtHdr bk on qd.id = bk.idPhieuNhapKho " +
            " WHERE 1 = 1 " +
            " AND bk.id is null " +
            " AND (:#{#param.trangThai} IS NULL OR qd.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.idQdNh} IS NULL OR qd.idQdNh = :#{#param.idQdNh}) " )
    List<ScPhieuNhapKhoHdr> searchListTaoBangKe(@Param("param") ScPhieuNhapKhoReq req);



}
