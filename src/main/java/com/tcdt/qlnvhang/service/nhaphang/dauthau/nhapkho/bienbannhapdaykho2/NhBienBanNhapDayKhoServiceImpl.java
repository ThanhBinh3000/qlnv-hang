package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbannhapdaykho2;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoRepository;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtReq;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Log4j2
public class NhBienBanNhapDayKhoServiceImpl extends BaseServiceImpl implements NhBienBanNhapDayKhoService {

    @Autowired
    private NhBbNhapDayKhoRepository nhBbNhapDayKhoRepository;

    @Autowired
    private NhBbNhapDayKhoCtRepository nhBbNhapDayKhoCtRepository;

    @Override
    public Page<NhBbNhapDayKho> searchPage(QlBienBanNhapDayKhoLtReq req) {
        return null;
    }

    @Override
    public List<NhBbNhapDayKho> searchAll(QlBienBanNhapDayKhoLtReq req) {
        return null;
    }

    @Override
    public NhBbNhapDayKho create(QlBienBanNhapDayKhoLtReq req) throws Exception {
        if (req == null) {
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        NhBbNhapDayKho item = new NhBbNhapDayKho();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.valueOf(req.getSoBienBanNhapDayKho().split("/")[0]));
        nhBbNhapDayKhoRepository.save(item);

        List<NhBbNhapDayKhoCt> chiTiets = saveListChiTiet(item.getId(), req.getChiTiets());
        item.setChiTiets(chiTiets);

        return item;
    }

    @Transactional
    List<NhBbNhapDayKhoCt> saveListChiTiet(Long parentId, List<QlBienBanNdkCtLtReq> chiTietReqs) throws Exception {
        nhBbNhapDayKhoCtRepository.deleteByIdBbNhapDayKho(parentId);
        List<NhBbNhapDayKhoCt> chiTiets = new ArrayList<>();
        for (QlBienBanNdkCtLtReq req : chiTietReqs) {
            NhBbNhapDayKhoCt chiTiet = new NhBbNhapDayKhoCt();
            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setIdBbNhapDayKho(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets)) {
            nhBbNhapDayKhoCtRepository.saveAll(chiTiets);
        }

        return chiTiets;
    }

    @Override
    public NhBbNhapDayKho update(QlBienBanNhapDayKhoLtReq req) throws Exception {
        if (req == null) {
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        Optional<NhBbNhapDayKho> optional = nhBbNhapDayKhoRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản không tồn tại.");

        NhBbNhapDayKho item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        nhBbNhapDayKhoRepository.save(item);

        List<NhBbNhapDayKhoCt> chiTiets = saveListChiTiet(item.getId(), req.getChiTiets());
        item.setChiTiets(chiTiets);

        return item;
    }

    @Override
    public NhBbNhapDayKho detail(Long id) throws Exception {
        return null;
    }

    @Override
    public NhBbNhapDayKho approve(QlBienBanNhapDayKhoLtReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public boolean export(QlBienBanNhapDayKhoLtReq req) throws Exception {
        return false;
    }


}
