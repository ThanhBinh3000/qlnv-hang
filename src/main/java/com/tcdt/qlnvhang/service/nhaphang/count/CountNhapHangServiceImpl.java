package com.tcdt.qlnvhang.service.nhaphang.count;

import com.tcdt.qlnvhang.request.CountReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbannghiemthubaoquan.HhBbNghiemthuKlstHdrService;
import com.tcdt.qlnvhang.service.nhaphang.bbanlaymau.BienBanBanGiaoMauService;
import com.tcdt.qlnvhang.service.nhaphang.bbanlaymau.BienBanLayMauService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.phieukiemnghiemcl.PhieuKnghiemCluongHangService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangke.NhBangKeVtService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbancbkho.NhBienBanChuanBiKhoService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHangService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbanketthucnhapkho.NhBbKtNhapKhoVtService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbaogiaonhan.NhBbGiaoNhanVtService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.hosokythuat.NhHoSoKyThuatService;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CountNhapHangServiceImpl extends BaseServiceImpl implements CountNhapHangService {

    private final NhPhieuNhapKhoService nhPhieuNhapKhoService;
    private final HhBbNghiemthuKlstHdrService hhBbNghiemthuKlstHdrService;
    private final BienBanLayMauService bienBanLayMauService;
    private final BienBanBanGiaoMauService bienBanBanGiaoMauService;
    private final PhieuKnghiemCluongHangService phieuKnghiemCluongHangService;
    private final NhHoSoKyThuatService hoSoKyThuatService;
    private final NhBienBanChuanBiKhoService bienBanChuanBiKhoService;
    private final NhBangKeCanHangService bangKeCanHangLtService;
    private final NhPhieuNhapKhoTamGuiService phieuNhapKhoTamGuiService;
    private final NhBienBanGuiHangService bienBanGuiHangService;
    private final NhBangKeVtService nhBangKeVtService;
    private final NhBbKtNhapKhoVtService nhBbKtNhapKhoVtService;
    private final NhBbGiaoNhanVtService bbGiaoNhanVtService;

    @Override
    public BaseNhapHangCount countKtcl(CountReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        List<BaseNhapHangCount> counts = new ArrayList<>();

        // Kiem tra chat luong
//        counts.add(qlpktclhPhieuKtChatLuongService.count(req.getMaDvis()));
//        counts.add(hhBbNghiemthuKlstHdrService.count(req.getMaDvis()));
//        counts.add(hoSoKyThuatService.count(req.getMaDvis()));
//        counts.add(bienBanChuanBiKhoService.count(req.getMaDvis()));

        BaseNhapHangCount count = new BaseNhapHangCount();
        count.setThoc(counts.stream().map(BaseNhapHangCount::getThoc).mapToInt(Integer::intValue).sum());
        count.setGao(counts.stream().map(BaseNhapHangCount::getGao).mapToInt(Integer::intValue).sum());
        count.setMuoi(counts.stream().map(BaseNhapHangCount::getMuoi).mapToInt(Integer::intValue).sum());
        count.setVatTu(counts.stream().map(BaseNhapHangCount::getVatTu).mapToInt(Integer::intValue).sum());
        count.setTatCa(count.getThoc() + count.getGao() + count.getVatTu());
        return count;
    }

    @Override
    public BaseNhapHangCount countNhapKho(CountReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        List<BaseNhapHangCount> counts = new ArrayList<>();

//        counts.add(nhPhieuNhapKhoService.count(req.getMaDvis()));
//        counts.add(bangKeCanHangLtService.count(req.getMaDvis()));
//        counts.add(bienBanNhapDayKhoLtService.count(req.getMaDvis()));
//        counts.add(phieuNhapKhoTamGuiService.count(req.getMaDvis()));
//        counts.add(bienBanGuiHangService.count(req.getMaDvis()));
//        counts.add(nhBangKeVtService.count(req.getMaDvis()));
//        counts.add(nhBbKtNhapKhoVtService.count(req.getMaDvis()));
//        counts.add(bbGiaoNhanVtService.count(req.getMaDvis()));

        BaseNhapHangCount count = new BaseNhapHangCount();
        count.setThoc(counts.stream().map(BaseNhapHangCount::getThoc).mapToInt(Integer::intValue).sum());
        count.setGao(counts.stream().map(BaseNhapHangCount::getGao).mapToInt(Integer::intValue).sum());
        count.setMuoi(counts.stream().map(BaseNhapHangCount::getMuoi).mapToInt(Integer::intValue).sum());
        count.setVatTu(counts.stream().map(BaseNhapHangCount::getVatTu).mapToInt(Integer::intValue).sum());
        count.setTatCa(count.getThoc() + count.getGao() + count.getVatTu());
        return count;
    }
}
