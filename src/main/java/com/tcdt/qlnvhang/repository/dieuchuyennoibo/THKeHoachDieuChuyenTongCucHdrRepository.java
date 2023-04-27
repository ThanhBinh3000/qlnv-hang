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
            "AND (:#{#param.nam} IS NULL OR hdr.namKeHoach = :#{#param.nam}) " +
            "AND (:#{#param.maTongHop} IS NULL OR LOWER(hdr.maTongHop) LIKE CONCAT('%',LOWER(:#{#param.maTongHop}),'%')) " +
            "AND (:#{#param.loaiDieuChuyen} IS NULL OR LOWER(hdr.loaiDieuChuyen) LIKE CONCAT('%',LOWER(:#{#param.loaiDieuChuyen}),'%'))"+
            "AND (:#{#param.loaiHH} IS NULL OR LOWER(hdr.loaiHangHoa) LIKE CONCAT('%',LOWER(:#{#param.loaiHH}),'%'))"+
            "AND (:#{#param.chungLoaiHH} IS NULL OR LOWER(hdr.chungLoaiHangHoa) LIKE CONCAT('%',LOWER(:#{#param.chungLoaiHH}),'%'))"+
            "AND ((:#{#param.tuNgay}  IS NULL OR hdr.ngayTongHop >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR hdr.ngayTongHop <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(hdr.noiDung) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) "+
            "ORDER BY hdr.namKeHoach desc"
    )
    Page<THKeHoachDieuChuyenTongCucHdr> search(@Param("param") TongHopKeHoachDieuChuyenSearch req, Pageable pageable);

    List<THKeHoachDieuChuyenTongCucHdr> findByIdIn(List<Long> ids);
    List<THKeHoachDieuChuyenTongCucDtl> findAllByIdIn(List<Long> listId);

//    @Query(value = "SELECT distinct hdr.maTongHop FROM THKeHoachDieuChuyenTongCucHdr hdr WHERE 1=1 " +
//            "AND (:#{#param.maDVi} IS NULL OR hdr.maDVi LIKE CONCAT(:#{#param.maDVi},'%')) "+
//            "AND (:#{#param.loaiDieuChuyen} IS NULL OR LOWER(hdr.loaiDieuChuyen) LIKE CONCAT('%',LOWER(:#{#param.loaiDieuChuyen}),'%'))"+
//            "ORDER BY hdr.maTongHop desc")
//    List<THKeHoachDieuChuyenTongCucHdr> filterMaTongHop(@Param("param") TongHopKeHoachDieuChuyenSearch param);
//
//    @Query(value = "SELECT distinct hdr.soDeXuat FROM THKeHoachDieuChuyenTongCucHdr hdr WHERE 1=1 " +
//            "AND (:#{#param.maDVi} IS NULL OR hdr.maDVi LIKE CONCAT(:#{#param.maDVi},'%')) "+
//            "AND (:#{#param.loaiDieuChuyen} IS NULL OR LOWER(hdr.loaiDieuChuyen) LIKE CONCAT('%',LOWER(:#{#param.loaiDieuChuyen}),'%'))"+
//            "ORDER BY hdr.maTongHop desc")
//    List<THKeHoachDieuChuyenTongCucHdr> filterSoDeXuat(TongHopKeHoachDieuChuyenSearch req);
}
