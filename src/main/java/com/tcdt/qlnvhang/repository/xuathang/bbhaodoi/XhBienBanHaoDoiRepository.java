package com.tcdt.qlnvhang.repository.xuathang.bbhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bbhaodoi.XhBienBanHaoDoi;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhBienBanHaoDoiRepository extends BaseRepository<XhBienBanHaoDoi, Long>, XhBienBanHaoDoiRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    @Query(value = "select max(id) from XhBienBanHaoDoi")
    Long getMaxId();

    @Query(value = "SELECT * from KT_TRANGTHAI_HIENTHOI where MA_DON_VI = ?1 AND MA_VTHH = ?2",nativeQuery = true)
    List<Object[]> getHangTrongKho(String maLoKho, String maChungLoaiHang);
}
