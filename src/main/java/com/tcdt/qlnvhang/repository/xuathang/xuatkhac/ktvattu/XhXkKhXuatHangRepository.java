package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkTongHopRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkKhXuatHangRepository extends JpaRepository<XhXkKhXuatHang, Long> {
    @Query("SELECT distinct c FROM XhXkKhXuatHang c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
            "AND (:#{#param.capDvi} IS NULL OR c.capDvi = :#{#param.capDvi}) " +
            "AND ((:#{#param.ngayKeHoachTu}  IS NULL OR c.ngayKeHoach >= :#{#param.ngayKeHoachTu})" +
            "AND  (:#{#param.ngayKeHoachDen}  IS NULL OR c.ngayKeHoach <= :#{#param.ngayKeHoachDen})) " +
            "AND ((:#{#param.ngayDuyetKeHoachTu}  IS NULL OR c.ngayDuyetKeHoach >= :#{#param.ngayDuyetKeHoachTu})" +
            "AND  (:#{#param.ngayDuyetKeHoachDen}  IS NULL OR c.ngayDuyetKeHoach <= :#{#param.ngayDuyetKeHoachDen})) " +
            "AND ((:#{#param.ngayDuyetBtcTu}  IS NULL OR c.ngayDuyetBtc >= :#{#param.ngayDuyetBtcTu})" +
            "AND  (:#{#param.ngayDuyetBtcDen}  IS NULL OR c.ngayDuyetBtc <= :#{#param.ngayDuyetBtcDen})) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkKhXuatHang> searchPage(@Param("param") XhXkKhXuatHangRequest param, Pageable pageable);

    @Query(value = "SELECT" +
        "    CASE" +
        "        WHEN LENGTH(:ma) = 14 THEN kl.nam_nhap" +
        "        WHEN LENGTH(:ma) = 16 THEN kl.nam_nhap" +
        "        ELSE NULL " +
        "    END AS nam_nhap_result " +
        " FROM" +
        "    xh_xk_kh_xuathang_dtl yt" +
        " LEFT JOIN" +
        "    kt_ngan_kho kk ON yt.ma_dia_diem = kk.ma_ngankho" +
        " LEFT JOIN" +
        "    kt_ngan_lo kl ON yt.ma_dia_diem = kl.ma_nganlo where yt.id = :id " ,nativeQuery = true)
    Integer getNamNhap(String ma, Long id);

    void deleteAllByIdIn(List<Long> listId);

    List<XhXkKhXuatHang> findByIdIn(List<Long> ids);

    List<XhXkKhXuatHang> findByIdCanCuIn(List<Long> ids);

    Optional<XhXkKhXuatHang> findBySoToTrinh(String soToTrinh);

    //Search kế hoạch để tổng hợp
    @Query("SELECT distinct c FROM XhXkKhXuatHang c WHERE 1=1 " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.capDvi} IS NULL OR c.capDvi = :#{#param.capDvi}) " +
            "AND (c.idCanCu is null) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    List<XhXkKhXuatHang> searchListTh(@Param("param") XhXkKhXuatHangRequest param);
}
