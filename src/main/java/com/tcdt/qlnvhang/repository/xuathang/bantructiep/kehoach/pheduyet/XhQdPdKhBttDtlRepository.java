package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.SearchXhTcTtinBttReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttDtlRepository extends JpaRepository<XhQdPdKhBttDtl , Long> {


//    @Query(value = " SELECT DTL.* FROM XH_QD_PD_KH_BTT_DTL DTL " +
//            " LEFT JOIN XH_TCTTIN_BTT CG ON DTL.ID=CG.ID_DTL"+
//            " LEFT JOIN XH_QD_PD_KH_BTT_HDR HDR ON HDR.ID = DTL.ID_QD_HDR " +
//            " WHERE (:namKh IS NULL OR HDR.NAM_KH = TO_NUMBER(:namKh)) " +
//            " AND (:ngayCgiaTu IS NULL OR CG.NGAY_CHAO_GIA >=  TO_DATE(:ngayCgiaTu,'yyyy-MM-dd')) " +
//            " AND (:ngayCgiadDen IS NULL OR CG.NGAY_CHAO_GIA <= TO_DATE(:ngayCgiadDen,'yyyy-MM-dd'))" +
//            " AND (:loaiVthh IS NULL OR HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
//            " AND (:maDvi IS NULL OR DTL.MA_DVI = :maDvi)" +
//            " AND (:trangThai IS NULL OR DTL.TRANG_THAI = :trangThai )" +
//            " AND (:tochucCanhan IS NULL OR LOWER(CG.TOCHUC_CANHAN) LIKE LOWER(CONCAT(CONCAT('%', :tochucCanhan),'%'))) "+
//            " AND HDR.LASTEST = 1 ",nativeQuery = true )
//    Page<XhQdPdKhBttDtl> selectPage(Integer namKh , String ngayCgiaTu, String ngayCgiadDen, String loaiVthh, String maDvi, String trangThai, String tochucCanhan, Pageable pageable);

    @Query("SELECT dtl FROM XhQdPdKhBttDtl dtl " +
            " left join XhTcTtinBtt btt on btt.idDtl = dtl.id " +
            " left join XhQdPdKhBttHdr hdr on hdr.id = dtl.idQdHdr WHERE 1=1 " +
            "AND (:#{#param.namKh} IS NULL OR hdr.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.ngayCgiaTu} IS NULL OR btt.ngayChaoGia >= :#{#param.ngayCgiaTu}) " +
            "AND (:#{#param.ngayCgiadDen} IS NULL OR btt.ngayChaoGia <= :#{#param.ngayCgiadDen}) " +
            "AND (:#{#param.maDvi} IS NULL OR dtl.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.trangThai} IS NULL OR dtl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.tochucCanhan} IS NULL OR LOWER(btt.tochucCanhan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tochucCanhan}),'%'))) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(hdr.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(hdr.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) "
    )
    Page<XhQdPdKhBttDtl> search(@Param("param") SearchXhTcTtinBttReq param, Pageable pageable);


    List<XhQdPdKhBttDtl> findAllByIdQdHdr (Long idQdHdr);

    void deleteAllByIdQdHdr(Long idQdHdr);
}
