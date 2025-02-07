package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenTongCucDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenTongCucHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface THKeHoachDieuChuyenTongCucHdrRepository extends JpaRepository<THKeHoachDieuChuyenTongCucHdr,Long> {
    List<THKeHoachDieuChuyenTongCucHdr> findByMaTongHop(String maTongHop);
    @Query(value = "SELECT distinct hdr FROM THKeHoachDieuChuyenTongCucHdr hdr WHERE 1=1 " +
            "AND (:#{#param.maDVi} IS NULL OR hdr.maDVi LIKE CONCAT(:#{#param.maDVi},'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR hdr.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.id} IS NULL OR LOWER(hdr.id) LIKE CONCAT('%',LOWER(:#{#param.id}),'%')) " +
            "AND (:#{#param.loaiDieuChuyen} IS NULL OR hdr.loaiDieuChuyen = :#{#param.loaiDieuChuyen})"+
            "AND ((:#{#param.tuNgay}  IS NULL OR hdr.ngayTongHop >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR hdr.ngayTongHop <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(hdr.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) "+
            "ORDER BY hdr.id desc, hdr.ngaySua desc, hdr.ngayTao desc, hdr.namKeHoach desc"
    )
    Page<THKeHoachDieuChuyenTongCucHdr> search(@Param("param") TongHopKeHoachDieuChuyenSearch req, Pageable pageable);

    List<THKeHoachDieuChuyenTongCucHdr> findByIdIn(List<Long> ids);
    List<THKeHoachDieuChuyenTongCucDtl> findAllByIdIn(List<Long> listId);

    @Query(value = "SELECT distinct hdr FROM THKeHoachDieuChuyenTongCucHdr hdr WHERE 1=1 " +
            "AND (:#{#param.loaiDieuChuyen} IS NULL OR hdr.loaiDieuChuyen = :#{#param.loaiDieuChuyen}) "+
            "AND (:#{#param.id} IS NULL OR LOWER(hdr.id) LIKE CONCAT('%',LOWER(:#{#param.id}),'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR hdr.namKeHoach = :#{#param.namKeHoach}) " +
            "AND hdr.id not in (select distinct qdtc.idThop from DcnbQuyetDinhDcTcHdr qdtc where 1 =1 AND qdtc.idThop is not null AND (:#{#param.id} IS NULL OR qdtc.id != :#{#param.qdtcId} ))" +
            "ORDER BY hdr.maTongHop desc")
    List<THKeHoachDieuChuyenTongCucHdr> filterMaTongHop(@Param("param") TongHopKeHoachDieuChuyenSearch param);

}
