package com.tcdt.qlnvhang.repository.xuathang.kiemtrachatluong;

import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.SearchHoSoKyThuatReq;
import com.tcdt.qlnvhang.response.xuathang.NhHoSoKyThuatDTO;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.hosokythuat.XhHoSoKyThuatHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface XhHoSoKyThuatRepository extends JpaRepository<XhHoSoKyThuatHdr, Long> {

  @Query("SELECT new com.tcdt.qlnvhang.response.xuathang.NhHoSoKyThuatDTO(" +
      "hs.id, hs.idQdGiaoNvNh , hs.soQdGiaoNvNh , hs.soBbLayMau , hs.soHd , hs.maDvi , hs.soHoSoKyThuat , hs.nam , hs.idBbLayMauXuat , hs.kqKiemTra , 'DT' , bb.maDiemKho , bb.maNhaKho , bb.maNganKho , bb.maLoKho, hs.ngayTao,xh.kqKiemTra,xh.soBbKtNgoaiQuan,xh.soBbKtVanHanh,xh.soBbKtHskt) " +
      "FROM NhHoSoKyThuat hs join BienBanLayMau bb on hs.soBbLayMau = bb.soBienBan " +
      "left join XhHoSoKyThuatHdr xh on xh.idHsktNh = hs.id AND xh.type = :#{#param.type} WHERE 1=1 " +
      "AND (hs.soBbLayMau = bb.soBienBan)" +
      "AND (:#{#param.dvql} IS NULL OR hs.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.soHoSoKyThuat} IS NULL OR hs.soHoSoKyThuat LIKE CONCAT('%',LOWER(:#{#param.soHoSoKyThuat}),'%')) "
      /* "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
       "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
       "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
       "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
       "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
       "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
       "AND (:#{#param.soBbXuatDocKho} IS NULL OR LOWER(c.soBbXuatDocKho) LIKE CONCAT('%',LOWER(:#{#param.soBbXuatDocKho}),'%')) " +
       "AND ((:#{#param.ngayKnTu}  IS NULL OR c.ngayLayMau >= :#{#param.ngayKnTu})" +
       "AND (:#{#param.ngayKnDen}  IS NULL OR c.ngayLayMau <= :#{#param.ngayKnDen}) ) " +
       "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +*/
//      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"

  )
  Page<NhHoSoKyThuatDTO> search(@Param("param") SearchHoSoKyThuatReq param, Pageable pageable);

  XhHoSoKyThuatHdr findByMaDiaDiem(String maDiaDiem);

  XhHoSoKyThuatHdr findByIdHsktNhAndType(Long id, String type);
}
