package com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QlhdmvtDiaDiemNhapVtRepository extends JpaRepository<QlhdmvtDiaDiemNhapVt, String>, JpaSpecificationExecutor<QlhdmvtDiaDiemNhapVt> {

}