package com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdMuaVatTu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QlhdMuaVatTuRepository extends JpaRepository<QlhdMuaVatTu, String>, JpaSpecificationExecutor<QlhdMuaVatTu> {

}