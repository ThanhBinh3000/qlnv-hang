package com.tcdt.qlnvhang.repository.kiemtrachatluong;

import com.tcdt.qlnvhang.table.kiemtrachatluong.NhHoSoBienBanCt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhHoSoBienBanCtRepository extends JpaRepository<NhHoSoBienBanCt,Long> {
    List<NhHoSoBienBanCt> findAllByIdHoSoBienBan(Long ids);
    List<NhHoSoBienBanCt> findAllByIdHoSoBienBanIn(List<Long> ids);
}
