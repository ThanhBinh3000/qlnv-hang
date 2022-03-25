package com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdMuaVatTu;
import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdmvtPhuLucHopDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QlhdmvtPhuLucHopDongRepository extends JpaRepository<QlhdmvtPhuLucHopDong, Long>, JpaSpecificationExecutor<QlhdMuaVatTu> {

}