package com.tcdt.qlnvhang.repository.kiemtrachatluong;

import com.tcdt.qlnvhang.request.kiemtrachatluong.SearchNhHoSoBienBan;
import com.tcdt.qlnvhang.table.kiemtrachatluong.NhHoSoBienBan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhHoSoBienBanRepository extends JpaRepository<NhHoSoBienBan,Long> {

    Optional<NhHoSoBienBan> findAllBySoBienBan(String soBienBan);

    List<NhHoSoBienBan> findAllByIdIn(List<Long> ids);


}
