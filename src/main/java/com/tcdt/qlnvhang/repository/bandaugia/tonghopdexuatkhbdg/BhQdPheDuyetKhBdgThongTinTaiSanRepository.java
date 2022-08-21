package com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg;


import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BhQdPheDuyetKhBdgThongTinTaiSanRepository extends BaseRepository<BhQdPheDuyetKhBdgThongTinTaiSan, Long> {
	void deleteAllByIdIn(Collection<Long> ids);

	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByBhDgKehoachId(Long id);

	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByBhDgKehoachIdIn(List<Long> id);

	void deleteAllByBhDgKehoachIdIn(List<Long> ids);

	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByBbBanDauGiaIdIn(Collection<Long> bbBdgIds);

	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByThongBaoBdgIdIn(Collection<Long> tbBdgIds);

	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByQdPheDuyetKqbdgIdIn(Collection<Long> qdPdKqBdgIds);

	void deleteAllByQdPheDuyetKhbdgChiTietIdIn(Collection<Long> chiTietIdList);

	List<BhQdPheDuyetKhBdgThongTinTaiSan> findByQdPheDuyetKhbdgChiTietIdIn(Collection<Long> qdPdKhBdgCtIds);

	@Query("select item from BhQdPheDuyetKhBdgThongTinTaiSan item " +
			"inner join BhQdPheDuyetKhbdgCt ct on ct.id = item.qdPheDuyetKhbdgChiTietId " +
			"inner join KeHoachBanDauGia kh on kh.id = ct.bhDgKeHoachId " +
			"where kh.maDv = :maDonVi and ct.quyetDinhPheDuyetId = :qdPdKhBdgId ")
	List<BhQdPheDuyetKhBdgThongTinTaiSan> findTaiSanBdgCuc(@Param("qdPdKhBdgId") Long qdPdKhBdgId, @Param("maDonVi") String maDonVi);

}
