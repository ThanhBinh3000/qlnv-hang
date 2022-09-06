package com.tcdt.qlnvhang.service.xuathang.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuat;
import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuatCt;
import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKho;
import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bbtinhkho.XhBienBanTinhKhoCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho.XhPhieuXuatKhoCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho.XhPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuXuatKhoSearchReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatCtReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkho.XhPhieuXuatKhoCtReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkho.XhPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.xuathang.phieuxuatkho.XhPhieuXuatKhoCtRes;
import com.tcdt.qlnvhang.response.xuathang.phieuxuatkho.XhPhieuXuatKhoRes;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.UserUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.modelmapper.ModelMapper;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhPhieuXuatKhoServiceImpl implements XhPhieuXuatKhoService {

    @Autowired
    XhPhieuXuatKhoRepository xuatKhoRepo;

    @Autowired
    XhPhieuXuatKhoCtRepository xuatKhoCtRepo;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public XhPhieuXuatKhoRes create(XhPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        XhPhieuXuatKho item = new XhPhieuXuatKho();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        xuatKhoRepo.save(item);

        List<XhPhieuXuatKhoCt> ds = req.getDs().stream()
                .map(d -> {
                    d.setPxuatKhoId(item.getId());
                    return d;
                })
                .map(d -> {
                    XhPhieuXuatKhoCt xuatKhoCt = new XhPhieuXuatKhoCt();
                    BeanUtils.copyProperties(d, xuatKhoCt, "id");
                    return xuatKhoCt;
                })
                .collect(Collectors.toList());

        xuatKhoCtRepo.saveAll(ds);
        XhPhieuXuatKhoRes result = new XhPhieuXuatKhoRes();
        BeanUtils.copyProperties(item, result, "id");

        List<XhPhieuXuatKhoCtRes> dsRes = ds
                .stream()
                .map(user -> new ModelMapper().map(user, XhPhieuXuatKhoCtRes.class))
                .collect(Collectors.toList());

        result.setDs(dsRes);

        return result;

    }


    @Override
    public XhPhieuXuatKhoRes update(XhPhieuXuatKhoReq xhPhieuXuatKhoReq) throws Exception {
        return null;
    }

    @Override
    public XhPhieuXuatKhoRes detail(Long id) throws Exception {
        XhPhieuXuatKho dsHangTieuHuy = xuatKhoRepo.findById(id).get();
        if (dsHangTieuHuy == null)
            throw new Exception("Không tìm thấy dữ liệu.");

        XhPhieuXuatKhoRes item = new XhPhieuXuatKhoRes();
        BeanUtils.copyProperties(dsHangTieuHuy, item, "id");
        item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
        // chưa tìm ngăn kho lô kho các giá trị tên
        List<XhPhieuXuatKhoCt> lstDs = xuatKhoCtRepo.findByPxuatKhoIdIn(Collections.singleton(id));

        List<XhPhieuXuatKhoCtRes> dsRes = lstDs
                .stream()
                .map(user -> new ModelMapper().map(user, XhPhieuXuatKhoCtRes.class))
                .collect(Collectors.toList());


        item.setDs(dsRes);

        return item;
    }
    @Override
    public boolean delete(Long id) throws Exception {
        Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findById(id);
        if (!optional.isPresent())
            throw new Exception("Phiếu xuất kho không tồn tại.");

        XhPhieuXuatKho item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa phiếu xuất kho đã đã duyệt");
        }
        xuatKhoCtRepo.deleteAllByPxuatKhoId(item.getId());
        xuatKhoRepo.delete(item);
        return true;
    }

    @Override
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        xuatKhoCtRepo.deleteByPxuatKhoIdIn(req.getIds());
        xuatKhoRepo.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatus(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Phiếu xuất kho không tồn tại.");

        XhPhieuXuatKho item = optional.get();
        String trangThai = item.getTrangThai();
        if (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId());
            item.setNguoiGuiDuyetId(userInfo.getId());
            item.setNgayGuiDuyet(LocalDate.now());

        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());

        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());

        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());
            item.setLyDoTuChoi(stReq.getLyDo());

        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());
            item.setLyDoTuChoi(stReq.getLyDo());

        } else {
            throw new Exception("Bad request.");
        }

        return true;
    }

    @Override
    public Page<XhPhieuXuatKhoRes> search(XhPhieuXuatKhoSearchReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<XhPhieuXuatKhoRes> list = xuatKhoRepo.search(req);

        Page<XhPhieuXuatKhoRes> page = new PageImpl<>(list, pageable, list.size());

        return page;
    }

    @Override
    public boolean exportToExcel(XhPhieuXuatKhoSearchReq objReq, HttpServletResponse response) throws Exception {
        return false;
    }
}
