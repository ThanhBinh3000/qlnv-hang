package com.tcdt.qlnvhang.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.response.dauthauvattu.ThongTinDauThauRes;
import com.tcdt.qlnvhang.table.HhDthau;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HhDthauRepository extends CrudRepository<HhDthau, Long> {

	Optional<HhDthau> findBySoQd(String soQd);
	@Query("SELECT new com.tcdt.qlnvhang.response.dauthauvattu.ThongTinDauThauRes(HDR.id,DTL.id,DSG.id,GT.idNhaThau,DSG.goiThau,DTL.maDvi,HDR.soQd,HDR.ngayQd,HDR.trichYeu,HDR.loaiVthh,COALESCE(HDR.cloaiVthh,DSG.cloaiVthh) ,DSG.thanhTien,DSG.trangThai,GT.nguonVon,GT.tgianThienHd,GT.loaiHdong,GT.vat,GT.soLuong,GT.donGiaTrcVat) " +
			" FROM HhQdKhlcntDsgthau DSG " +
			" LEFT JOIN HhQdKhlcntDtl DTL ON DTL.id = DSG.idQdDtl " +
			" LEFT JOIN HhQdKhlcntHdr HDR ON HDR.id = DTL.idQdHdr " +
			" LEFT JOIN HhDthauGthau GT ON GT.idGoiThau = DSG.id " +
			" WHERE HDR.trangThai = '11' " +
			" AND (?1 is null or HDR.namKhoach = ?1 )" +
			" AND (?2 is null or  lower(HDR.loaiVthh) like lower(concat(?2,'%' ) ) )" +
			" AND (?3 is null or lower(HDR.soQd) like lower(concat(concat('%',?3),'%') ) )" +
			" AND (?4 is null or  DTL.maDvi = ?4 )" +
//			" AND (?5 is null or HDR.ngayQd >= ?5 ) " +
//			" AND (?6 is null or HDR.ngayQd <= ?6 )" +
			" AND (?5 is null or  lower(HDR.trichYeu)like lower(concat(concat('%',?5),'%' ) ) )")
	Page<ThongTinDauThauRes> cusTomQuerySearch(Long namKh, String loaiVthh, String soQd, String maDvi, String trichYeu,Pageable pageable);

	@Query("SELECT new com.tcdt.qlnvhang.response.dauthauvattu.ThongTinDauThauRes(HDR.id,DTL.id,DSG.id,GT.idNhaThau,DSG.goiThau,DTL.maDvi,HDR.soQd,HDR.ngayQd,HDR.trichYeu,HDR.loaiVthh,COALESCE(HDR.cloaiVthh,DSG.cloaiVthh),DSG.thanhTien,DSG.trangThai,GT.nguonVon,GT.tgianThienHd,GT.loaiHdong,GT.vat,GT.soLuong,GT.donGiaTrcVat) " +
			" FROM HhQdKhlcntDsgthau DSG " +
			" LEFT JOIN HhQdKhlcntDtl DTL ON DTL.id = DSG.idQdDtl " +
			" LEFT JOIN HhQdKhlcntHdr HDR ON HDR.id = DTL.idQdHdr " +
			" LEFT JOIN HhDthauGthau GT ON GT.idGoiThau = DSG.id " +
			" WHERE HDR.trangThai = '11' " +
			" AND (?1 is null or HDR.namKhoach = ?1 )" +
			" AND (?2 is null or  HDR.loaiVthh = ?2 )" +
			" AND (?3 is null or  HDR.cloaiVthh = ?3 )" +
			" AND (?4 is null or  HDR.soQd = ?4 )" +
			" AND (?5 is null or  DTL.maDvi = ?5 )" +
			" AND (?6 is null or  DSG.trangThai = ?6 )")
	List<ThongTinDauThauRes> cusTomQuerySearch(Long namKh, String loaiVthh, String cloaiVthh,String soQd, String maDvi,String trangThai);

}
