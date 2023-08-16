package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtDeXuatHdrReq;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhCtvtDeXuatHdrRepository extends JpaRepository<XhCtvtDeXuatHdr, Long> {

  @Query("SELECT distinct c FROM XhCtvtDeXuatHdr c left join c.deXuatPhuongAn h WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
      "AND (:#{#param.maDviDx} IS NULL OR h.maDvi LIKE CONCAT(:#{#param.maDviDx},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soDx} IS NULL OR LOWER(c.soDx) LIKE CONCAT('%',LOWER(:#{#param.soDx}),'%')) " +
      "AND ((:#{#param.ngayDxTu}  IS NULL OR c.ngayDx >= :#{#param.ngayDxTu})" +
      "AND (:#{#param.ngayDxDen}  IS NULL OR c.ngayDx <= :#{#param.ngayDxDen}) ) " +
      "AND ((:#{#param.ngayKetThucTu}  IS NULL OR c.ngayKetThuc >= :#{#param.ngayKetThucTu})" +
      "AND (:#{#param.ngayKetThucDen}  IS NULL OR c.ngayKetThuc <= :#{#param.ngayKetThucDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "AND (:#{#param.trangThaiList == null || #param.trangThaiList.isEmpty() } = true OR c.trangThai IN :#{#param.trangThaiList}) " +
      "AND (:#{#param.idQdPdList.isEmpty() } = true OR c.idQdPd IN :#{#param.idQdPdList}) " +
      "AND (:#{#param.idQdPdNull } = false OR (:#{#param.idQdPdNull } = true AND c.idQdPd IS NULL AND c.idThop IS NULL)) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhCtvtDeXuatHdr> search(@Param("param") SearchXhCtvtDeXuatHdrReq param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  List<XhCtvtDeXuatHdr> findByIdIn(List<Long> ids);

  List<XhCtvtDeXuatHdr> findAllByIdIn(List<Long> listId);

  Optional<XhCtvtDeXuatHdr> findFirstBySoDx(String soDx);

  @Query("SELECT c FROM XhCtvtDeXuatHdr c WHERE " +
      "(:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) AND " +
      "(:#{#param.type} IS NULL OR c.type = :#{#param.type}) AND " +
      "(:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) AND " +
      "c.idThop IS NULL AND " +
      "c.idQdPd IS NULL AND " +
      "(:#{#param.loaiVthh} IS NULL OR c.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) AND " +
      "(:#{#param.loaiNhapXuat} IS NULL OR c.loaiNhapXuat LIKE CONCAT(:#{#param.loaiNhapXuat},'%')) AND " +
      "(:#{#param.trangThaiList == null || #param.trangThaiList.isEmpty()} = true OR c.trangThai IN :#{#param.trangThaiList}) " +
      "ORDER BY c.ngaySua DESC, c.ngayTao DESC, c.id DESC"
  )
  List<XhCtvtDeXuatHdr> listTongHop(@Param("param") SearchXhCtvtDeXuatHdrReq param);

  List<XhCtvtDeXuatHdr> findAllByIdThop(Long id);
}
