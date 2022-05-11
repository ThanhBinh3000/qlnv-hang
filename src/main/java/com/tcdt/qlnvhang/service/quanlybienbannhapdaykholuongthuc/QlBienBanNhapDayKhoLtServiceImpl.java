package com.tcdt.qlnvhang.service.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLt;
import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLt;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.enums.QlBangKeCanHangLtStatus;
import com.tcdt.qlnvhang.enums.QlBienBanNhapDayKhoLtStatus;
import com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtRepository;
import com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtReq;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtReq;
import com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtSearchReq;
import com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtRes;
import com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.donvi.QlnvDmDonViService;
import com.tcdt.qlnvhang.table.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tcdt.qlnvhang.service.impl.BaseServiceImpl.convertDateToString;

@Service
public class QlBienBanNhapDayKhoLtServiceImpl implements QlBienBanNhapDayKhoLtService {

    @Autowired
    private QlBienBanNhapDayKhoLtRepository qlBienBanNhapDayKhoLtRepository;

    @Autowired
    private QlBienBanNdkCtLtRepository qlBienBanNdkCtLtRepository;

    @Autowired
    private QlnvDmDonViService qlnvDmDonViService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QlBienBanNhapDayKhoLtRes create(QlBienBanNhapDayKhoLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        QlBienBanNhapDayKhoLt item = new QlBienBanNhapDayKhoLt();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(QlBienBanNhapDayKhoLtStatus.MOI_TAO.getId());
        item.setMaDonVi(userInfo.getDvql());
        item.setCapDonVi(qlnvDmDonViService.getCapDviByMa(userInfo.getDvql()));
        qlBienBanNhapDayKhoLtRepository.save(item);

        List<QlBienBanNdkCtLt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);
        return buildResponse(item);
    }

    private QlBienBanNhapDayKhoLtRes buildResponse(QlBienBanNhapDayKhoLt item) {
        QlBienBanNhapDayKhoLtRes response = new QlBienBanNhapDayKhoLtRes();
        BeanUtils.copyProperties(item, response);
        List<QlBienBanNdkCtLt> chiTiets = item.getChiTiets();
        for (QlBienBanNdkCtLt chiTiet : chiTiets) {
            QlBienBanNdkCtLtRes chiTietRes = new QlBienBanNdkCtLtRes();
            BeanUtils.copyProperties(chiTiet, chiTietRes);
            response.getChiTiets().add(chiTietRes);
        }
        return response;
    }

    private List<QlBienBanNdkCtLt> saveListChiTiet(Long parentId, List<QlBienBanNdkCtLtReq> chiTietReqs, Map<Long, QlBienBanNdkCtLt> mapChiTiet) throws Exception {
        List<QlBienBanNdkCtLt> chiTiets = new ArrayList<>();
        for (QlBienBanNdkCtLtReq req : chiTietReqs) {
            Long id = req.getId();
            QlBienBanNdkCtLt chiTiet = new QlBienBanNdkCtLt();

            if (id != null) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Biên bản chi tiết hàng Hóa không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setQlBienBanNdkLtId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            qlBienBanNdkCtLtRepository.saveAll(chiTiets);

        return chiTiets;
    }
    @Override
    @Transactional(rollbackOn = Exception.class)
    public QlBienBanNhapDayKhoLtRes update(QlBienBanNhapDayKhoLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBienBanNhapDayKhoLt> optional = qlBienBanNhapDayKhoLtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản không tồn tại.");

        QlBienBanNhapDayKhoLt item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        qlBienBanNhapDayKhoLtRepository.save(item);

        Map<Long, QlBienBanNdkCtLt> map = qlBienBanNdkCtLtRepository.findAllByQlBienBanNdkLtIdOrderBySttAsc(item.getId())
                .stream().collect(Collectors.toMap(QlBienBanNdkCtLt::getId, Function.identity()));

        List<QlBienBanNdkCtLtReq> chiTietReqs = req.getChiTiets();
        List<QlBienBanNdkCtLt> chiTiets = this.saveListChiTiet(item.getId(), chiTietReqs, map);
        item.setChiTiets(chiTiets);

        return this.buildResponse(item);
    }

    @Override
    public QlBienBanNhapDayKhoLtRes detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBienBanNhapDayKhoLt> optional = qlBienBanNhapDayKhoLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản không tồn tại.");

        QlBienBanNhapDayKhoLt item = optional.get();
        item.setChiTiets(qlBienBanNdkCtLtRepository.findAllByQlBienBanNdkLtIdOrderBySttAsc(item.getId()));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBienBanNhapDayKhoLt> optional = qlBienBanNhapDayKhoLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        QlBienBanNhapDayKhoLt item = optional.get();
        if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa bảng kê đã duyệt");
        }

        List<QlBienBanNdkCtLt> chiTiets = qlBienBanNdkCtLtRepository.findAllByQlBienBanNdkLtIdOrderBySttAsc(item.getId());
        if (!CollectionUtils.isEmpty(chiTiets)) {
            qlBienBanNdkCtLtRepository.deleteAll(chiTiets);
        }

        qlBienBanNhapDayKhoLtRepository.delete(item);
        return true;
    }

    @Override
    public Page<QlBienBanNhapDayKhoLtRes> search(QlBienBanNhapDayKhoLtSearchReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        req.setMaDonVi(userInfo.getDvql());
        return qlBienBanNhapDayKhoLtRepository.search(req);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<QlBienBanNhapDayKhoLt> optional = qlBienBanNhapDayKhoLtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản không tồn tại.");

        QlBienBanNhapDayKhoLt bangKe = optional.get();
        return this.updateStatus(req, bangKe, userInfo);
    }

    @Override
    public Page<QlBienBanNhapDayKhoLt> timKiem(QlBienBanNhapDayKhoLtSearchReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
        return qlBienBanNhapDayKhoLtRepository.select(req.getSoBienBan(),convertDateToString(req.getNgayBatDauNhap()),convertDateToString(req.getNgayKetThucNhap()),convertDateToString(req.getNgayNhapDayKhoTu()),convertDateToString(req.getNgayNhapDayKhoDen()),req.getMaDiemKho(),req.getMaNhaKho(),req.getMaKhoNganLo(),req.getKyThuatVien(), pageable);
    }

    public boolean updateStatus(StatusReq req, QlBienBanNhapDayKhoLt bangKe, UserInfo userInfo) throws Exception {
        String trangThai = bangKe.getTrangThai();
        if (QlBienBanNhapDayKhoLtStatus.CHO_DUYET.getId().equals(req.getTrangThai())) {
            if (!QlBienBanNhapDayKhoLtStatus.MOI_TAO.getId().equals(trangThai))
                return false;

            bangKe.setTrangThai(QlBienBanNhapDayKhoLtStatus.CHO_DUYET.getId());
            bangKe.setNguoiGuiDuyetId(userInfo.getId());
            bangKe.setNgayGuiDuyet(LocalDate.now());

        } else if (QlBienBanNhapDayKhoLtStatus.DA_DUYET.getId().equals(req.getTrangThai())) {
            if (!QlBienBanNhapDayKhoLtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;
            bangKe.setTrangThai(QlBienBanNhapDayKhoLtStatus.DA_DUYET.getId());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
        } else if (QlBienBanNhapDayKhoLtStatus.TU_CHOI.getId().equals(req.getTrangThai())) {
            if (!QlBienBanNhapDayKhoLtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;

            bangKe.setTrangThai(QlBangKeCanHangLtStatus.TU_CHOI.getId());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
            bangKe.setLyDoTuChoi(req.getLyDo());
        } else {
            throw new Exception("Bad request.");
        }

        qlBienBanNhapDayKhoLtRepository.save(bangKe);
        return true;
    }
}
