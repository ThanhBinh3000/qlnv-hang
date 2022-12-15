package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgDtl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HhQdPheduyetKhMttDxRepository extends JpaRepository<HhQdPheduyetKhMttDx, Long> {
    List<HhQdPheduyetKhMttDx> findAllByIdQdHdrIn (List<Long> ids);
    void deleteAllByIdQdHdr(Long idQdHdr);

    List<HhQdPheduyetKhMttDx> findAllByIdQdHdr (Long idQdHdr);





  @Query(value = " SELECT DTL.* FROM HH_QD_PHE_DUYET_KHMTT_DX DTL " +
          " LEFT JOIN HH_CTIET_TTIN_CHAO_GIA DD ON DTL.ID=DD.ID_SO_QD_PDUYET_CGIA"+
          " LEFT JOIN HH_QD_PHE_DUYET_KHMTT_HDR HDR ON HDR.ID = DTL.ID_QD_HDR " +
          " WHERE (:namKh IS NULL OR HDR.NAM_KH = TO_NUMBER(:namKh)) " +
          " AND (:ngayCgiaTu IS NULL OR HDR.NGAY_HLUC >=  TO_DATE(:ngayCgiaTu,'yyyy-MM-dd')) " +
          " AND (:ngayCgiadDen IS NULL OR HDR.NGAY_HLUC <= TO_DATE(:ngayCgiadDen,'yyyy-MM-dd'))" +
          " AND (:loaiVthh IS NULL OR HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
          " AND (:maDvi IS NULL OR DTL.MA_DVI = :maDvi)" +
          " AND HDR.TRANG_THAI = :trangThai " +
          " AND (:trangThaiTkhai IS NULL OR DTL.TRANG_THAI_TKHAI = :trangThaiTkhai )" +
          " AND (:ctyCgia IS NULL OR LOWER(DD.CANHAN_TOCHUC) LIKE LOWER(CONCAT(CONCAT('%', :ctyCgia),'%'))) "+
          " AND (:pthucMuatt IS NULL OR DTL.PTHUC_MUATT =:pthucMuatt)"+
          " AND HDR.LASTEST = 1 ",nativeQuery = true )
  Page<HhQdPheduyetKhMttDx> selectPage(Integer namKh , String ngayCgiaTu, String ngayCgiadDen, String loaiVthh, String maDvi, String trangThai,String trangThaiTkhai, String ctyCgia, String pthucMuatt, Pageable pageable);

}
