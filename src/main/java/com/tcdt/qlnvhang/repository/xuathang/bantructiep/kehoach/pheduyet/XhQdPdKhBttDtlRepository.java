package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttDtlRepository extends JpaRepository<XhQdPdKhBttDtl , Long> {


    @Query(value = " SELECT DTL.* FROM XH_QD_PD_KH_BTT_DTL DTL " +
            " LEFT JOIN XH_TCTTIN_BTT CG ON DTL.ID=CG.ID_DTL"+
            " LEFT JOIN XH_QD_PD_KH_BTT_HDR HDR ON HDR.ID = DTL.ID_QD_HDR " +
            " WHERE (:namKh IS NULL OR HDR.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:ngayCgiaTu IS NULL OR CG.NGAY_CHAO_GIA >=  TO_DATE(:ngayCgiaTu,'yyyy-MM-dd')) " +
            " AND (:ngayCgiadDen IS NULL OR CG.NGAY_CHAO_GIA <= TO_DATE(:ngayCgiadDen,'yyyy-MM-dd'))" +
            " AND (:loaiVthh IS NULL OR HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
            " AND (:maDvi IS NULL OR DTL.MA_DVI = :maDvi)" +
            " AND (:trangThai IS NULL OR DTL.TRANG_THAI = :trangThai )" +
            " AND (:toChucCaNhan IS NULL OR LOWER(CG.TOCHUC_CANHAN) LIKE LOWER(CONCAT(CONCAT('%', :toChucCaNhan),'%'))) "+
            " AND HDR.LASTEST = 1 ",nativeQuery = true )
    Page<XhQdPdKhBttDtl> selectPage(Integer namKh , String ngayCgiaTu, String ngayCgiadDen, String loaiVthh, String maDvi, String trangThai, String toChucCaNhan, Pageable pageable);


    List<XhQdPdKhBttDtl> findAllByIdQdHdr (Long idQdHdr);

    void deleteAllByIdQdHdr(Long idQdHdr);
}
