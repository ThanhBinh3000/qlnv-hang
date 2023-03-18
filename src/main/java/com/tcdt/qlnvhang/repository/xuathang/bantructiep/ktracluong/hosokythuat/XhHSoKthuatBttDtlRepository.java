package com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.hosokythuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhHSoKthuatBttDtlRepository extends JpaRepository<XhHSoKthuatBttDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhHSoKthuatBttDtl> findAllByIdHdr(Long idHdr);
}
