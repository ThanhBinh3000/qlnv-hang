package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBNTBQHdrReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauReq;
import com.tcdt.qlnvhang.request.object.dauthauvattu.DTGoiThauDiaDiemNhapVTReq;
import com.tcdt.qlnvhang.response.dauthauvattu.DTGoiThauDiaDiemNhapVTRes;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DcnbBBNTBQHdrService extends BaseService<DcnbBBNTBQHdr, DcnbBBNTBQHdrReq,Long> {

}