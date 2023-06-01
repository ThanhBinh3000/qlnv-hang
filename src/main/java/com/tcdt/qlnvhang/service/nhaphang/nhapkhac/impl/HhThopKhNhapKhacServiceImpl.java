package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhThopKhNhapKhac;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhThopKhNhapKhacRepository;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacSearch;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhThopKhNhapKhacService;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HhThopKhNhapKhacServiceImpl extends BaseServiceImpl implements HhThopKhNhapKhacService {
    @Autowired
    private HhThopKhNhapKhacRepository hhThopKhNhapKhacRepository;
    @Autowired
    private HhDxuatKhNhapKhacHdrRepository hhDxuatKhNhapKhacHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<HhThopKhNhapKhac> timKiem(HhThopKhNhapKhacSearch req) {
        req.setTuNgayThStr(convertFullDateToString(req.getTuNgayTh()));
        req.setDenNgayThStr(convertFullDateToString(req.getDenNgayTh()));
        req.setTuNgayKyStr(convertFullDateToString(req.getTuNgayKy()));
        req.setDenNgayKyStr(convertFullDateToString(req.getDenNgayKy()));
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhThopKhNhapKhac> data = hhThopKhNhapKhacRepository.search(req, pageable);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
        });
        return null;
    }

    @Override
    public List<HhDxuatKhNhapKhacHdr> layDsDxuatChuaTongHop(HhThopKhNhapKhacSearch req) {
        if (req.getNamKhoach() != null && req.getLoaiHinhNx() != null && req.getLoaiVthh() != null) {
            List<String> trangThai = new ArrayList<>();
            trangThai.add(Contains.DA_DUYET_CBV);
            trangThai.add(Contains.DA_TAO_CBV);
            List<HhDxuatKhNhapKhacHdr> data = hhDxuatKhNhapKhacHdrRepository.findAllByNamKhoachAndLoaiHinhNxAndLoaiVthhAndTrangThaiInAndTrangThaiTh(
                    req.getNamKhoach(), req.getLoaiHinhNx(), req.getLoaiVthh(), trangThai, Contains.CHUATONGHOP);
            Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
            data.forEach(f -> {
                f.setTenDvi(mapDmucDvi.get(f.getMaDviDxuat()));
            });
            return data;
            }
        return null;
    }

    @Override
    @Transactional
    public HhThopKhNhapKhac themMoi(HhThopKhNhapKhacReq req) throws Exception {
        HhThopKhNhapKhac dataMap = new ModelMapper().map(req, HhThopKhNhapKhac.class);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setTrangThai(Contains.DUTHAO);
        HhThopKhNhapKhac created = hhThopKhNhapKhacRepository.save(dataMap);
        luuFile(req, created);
        luuChiTiet(req, created);
        return null;
    }

    private void luuFile(HhThopKhNhapKhacReq req, HhThopKhNhapKhac created) {
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(HhThopKhNhapKhac.TABLE_NAME));
        if (!DataUtils.isNullObject(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HhThopKhNhapKhac.TABLE_NAME);
        }
    }
    private void luuChiTiet(HhThopKhNhapKhacReq req, HhThopKhNhapKhac created) {
        BigDecimal tongSl = BigDecimal.ZERO;
        if (req.getDetails() != null && !req.getDetails().isEmpty()) {
            for (HhDxuatKhNhapKhacHdrReq item: req.getDetails()) {
                Optional<HhDxuatKhNhapKhacHdr> dx = hhDxuatKhNhapKhacHdrRepository.findById(item.getId());
                if(dx.isPresent()) {
                    dx.get().setThopId(created.getId());
                    dx.get().setTrangThaiTh(created.getTrangThai());
                    tongSl = tongSl.add(dx.get().getTongSlNhap());
                    created.setDvt(dx.get().getDvt());
                    hhDxuatKhNhapKhacHdrRepository.save(dx.get());
                }
            }
        }
        created.setTongSlNhap(tongSl);
    }
}
