package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhPhieuKnghiemCluongRepository extends BaseRepository<XhPhieuKnghiemCluong, Long> {


    @Query("SELECT CL from XhPhieuKnghiemCluong CL " +
            "LEFT JOIN XhBbLayMau LM ON LM.id = CL.idBbLayMau WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR CL.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(CL.soQdGiaoNvXh) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdGiaoNvXh}),'%' ) ) )" +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(CL.soPhieu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soPhieu}),'%' ) ) )" +
            "AND (:#{#param.ngayKnghiemTu} IS NULL OR CL.ngayKnghiem >= :#{#param.ngayKnghiemTu}) " +
            "AND (:#{#param.ngayKnghiemDen} IS NULL OR CL.ngayKnghiem <= :#{#param.ngayKnghiemDen}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(CL.soBbLayMau) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBbLayMau}),'%' ) ) )" +
            "AND (:#{#param.loaiVthh} IS NULL OR CL.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR CL.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maChiCuc} IS NULL OR LM.maDvi = :#{#param.maChiCuc})" +
            "AND (:#{#param.maDvi} IS NULL OR CL.maDvi = :#{#param.maDvi})")
    Page<XhPhieuKnghiemCluong> searchPage(@Param("param") XhPhieuKnghiemCluongReq param, Pageable pageable);

    List<XhPhieuKnghiemCluong> findByIdIn(List<Long> idList);


}
