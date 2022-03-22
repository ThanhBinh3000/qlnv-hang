package com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdmvtThongTinChung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QlhdmvtThongTinChungRepository extends JpaRepository<QlhdmvtThongTinChung, Long>, JpaSpecificationExecutor<QlhdmvtThongTinChung> {

}