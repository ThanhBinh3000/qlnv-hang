package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.HhDxuatKhLcntHdr;

public interface HhDxuatKhLcntHdrRepository extends BaseRepository<HhDxuatKhLcntHdr, Long> {

	Optional<HhDxuatKhLcntHdr> findBySoDxuat(String soDxuat);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_DX_KHLCNT_HDR SET TRANG_THAI=:trangThai WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
	void updateTongHop(List<String> soDxuatList, String trangThai);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_DX_KHLCNT_HDR SET TRANG_THAI=:trangThai WHERE ID = :id ", nativeQuery = true)
	void updateStatus(Long id, String trangThai);

	@Query(value = "select shgt.nextval from dual", nativeQuery = true)
	Long getIdShgt();

	@Query(
			value = "SELECT * " +
					"FROM HH_DX_KHLCNT_HDR KHLCNT " +
					" WHERE (:namKh IS NULL OR KHLCNT.NAM_KHOACH = TO_NUMBER(:namKh)) " +
					"  AND (:soTr IS NULL OR LOWER(KHLCNT.SO_DXUAT) LIKE LOWER(CONCAT(CONCAT('%', :soTr),'%'))) " +
					"  AND (:soQd IS NULL OR LOWER(KHLCNT.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%')))" +
					"  AND (:ngayKyTu IS NULL OR KHLCNT.NGAY_KY >= TO_DATE(:ngayKyTu, 'yyyy-MM-dd'))" +
					"  AND (:ngayKyDen IS NULL OR KHLCNT.NGAY_KY <= TO_DATE(:ngayKyDen, 'yyyy-MM-dd'))" +
					"  AND (:loaiVthh IS NULL OR KHLCNT.LOAI_VTHH = :loaiVthh) " +
					"  AND (:trichYeu IS NULL OR LOWER(KHLCNT.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%')))" +
					"  AND (:trangThai IS NULL OR KHLCNT.TRANG_THAI = :trangThai) ", nativeQuery = true)
	Page<HhDxuatKhLcntHdr> select(String namKh, String soTr,String soQd, String ngayKyTu,String ngayKyDen,String loaiVthh,String trichYeu,String trangThai, Pageable pageable);

	@Query(value = " SELECT KHLCNT.* \n" +
			"FROM HH_DX_KHLCNT_HDR KHLCNT \n" +
			" WHERE KHLCNT.LOAI_VTHH = :loaiVthh \n" +
			" AND KHLCNT.CLOAI_VTHH = :cloaiVthh \n" +
			" AND KHLCNT.NAM_KHOACH = :namKh \n" +
			" AND KHLCNT.HTHUC_LCNT = :hthucLcnt \n" +
			" AND KHLCNT.PTHUC_LCNT = :pthucLcnt \n" +
			" AND KHLCNT.LOAI_HDONG = :loaiHdong \n" +
			" AND KHLCNT.NGUON_VON = :nguonVon " +
			" AND KHLCNT.TRANG_THAI = '02' ", nativeQuery = true)
	List<HhDxuatKhLcntHdr> listTongHop(String loaiVthh,String cloaiVthh,String namKh, String hthucLcnt,String pthucLcnt, String loaiHdong,String nguonVon);
}
