package com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnCongTrinhNghienCuu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhCnCongTrinhNghienCuuRepository extends JpaRepository<KhCnCongTrinhNghienCuu,Long> {


    @Query(value = "select * from KH_CN_CONG_TRINH_NGHIEN_CUU KHCN " +
            " where (:maDeTai IS NULL OR LOWER(KHCN.MA_DE_TAI) LIKE LOWER(CONCAT(CONCAT('%',:maDeTai),'%' ) ) ) " +
            "AND (:tenDeTai IS NULL OR LOWER(KHCN.TEN_DE_TAI) LIKE LOWER(CONCAT(CONCAT('%',:tenDeTai),'%' ) ) )" +
            "AND (:capDeTai IS NULL OR KHCN.CAP_DE_TAI= :capDeTai)" +
            "AND (:trangThai IS NULL OR KHCN.TRANG_THAI = :trangThai)" +
            "AND (:maDvi IS NULL OR LOWER(DX.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<KhCnCongTrinhNghienCuu> searchPage(String maDeTai, String tenDeTai, String capDeTai, String trangThai,String maDvi,  Pageable pageable);

    Optional<KhCnCongTrinhNghienCuu> findAllByMaDeTai(String maDetai);

    List<KhCnCongTrinhNghienCuu> findAllByIdIn(List<Long> ids);

}
