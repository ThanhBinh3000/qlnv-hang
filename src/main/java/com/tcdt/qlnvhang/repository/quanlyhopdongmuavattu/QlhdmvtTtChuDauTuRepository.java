package com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdmvtTtChuDauTu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QlhdmvtTtChuDauTuRepository extends JpaRepository<QlhdmvtTtChuDauTu, Long>, JpaSpecificationExecutor<QlhdmvtTtChuDauTu> {

}