package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbancbkho;

import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoRes;
import com.tcdt.qlnvhang.service.BaseService;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Set;

public interface NhBienBanChuanBiKhoService extends BaseService<NhBienBanChuanBiKho,NhBienBanChuanBiKhoReq,Long> {

}
