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
					"  AND (:trangThai IS NULL OR KHLCNT.TRANG_THAI = :trangThai) ", nativeQuery = true)
	Page<HhDxuatKhLcntHdr> select(String namKh, String soTr,String soQd, String ngayKyTu,String ngayKyDen,String loaiVthh,String trangThai, Pageable pageable);

	@Query(value = " SELECT KHLCNT.* \n" +
			"FROM HH_DX_KHLCNT_HDR KHLCNT \n" +
			"LEFT JOIN HH_DX_KHLCNT_LT_DTL KHLCNT_DETAIL ON KHLCNT.ID_HDR = KHLCNT_DETAIL.ID \n" +
			" WHERE KHLCNT.LOAI_VTHH = :loaiVthh \n" +
			" AND KHLCNT.NAM_KHOACH = :namKh \n" +
			" AND KHLCNT_DETAIL.HTHUC_LCNT = :hthucLcnt \n" +
			" AND KHLCNT_DETAIL.PTHUC_LCNT = :pthucLcnt \n" +
			" AND KHLCNT_DETAIL.LOAI_HDONG = :loatHdong \n" +
			" AND KHLCNT_DETAIL.NGUON_VON = :nguonVon " +
			" AND KHLCNT.TRANG_THAI = '11' ", nativeQuery = true)
	List<HhDxuatKhLcntHdr> listTongHop(String loaiVthh,String namKh, String hthucLcnt,String pthucLcnt, String loatHdong,String nguonVon);
}
