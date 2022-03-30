package com.tcdt.qlnvhang.service.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeChCtLt;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRepository;
import com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc.QlBangKeChCtLtRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeChCtLtReq;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRes;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeChCtLtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.table.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QlBangKeCanHangLtServiceImpl implements QlBangKeCanHangLtService {

    @Autowired
    private QlBangKeCanHangLtRepository qlBangKeCanHangLtRepository;

    @Autowired
    private QlBangKeChCtLtRepository qlBangKeChCtLtRepository;

    @Override
    public QlBangKeCanHangLtRes create(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        QlBangKeCanHangLt item = new QlBangKeCanHangLt();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(QdPheDuyetKqlcntVtStatus.MOI_TAO.getId());
        item.setMaDonVi(userInfo.getDvql());
        qlBangKeCanHangLtRepository.save(item);

        List<QlBangKeChCtLtReq> chiTietReqs = req.getChiTiets();
        List<QlBangKeChCtLt> chiTiets = this.saveListChiTiet(item.getId(), chiTietReqs, new HashMap<>());
        item.setChiTiets(chiTiets);

        return this.buildResponse(item);
    }

    private QlBangKeCanHangLtRes buildResponse(QlBangKeCanHangLt item) {
        QlBangKeCanHangLtRes response = new QlBangKeCanHangLtRes();
        BeanUtils.copyProperties(item, response);
        List<QlBangKeChCtLt> chiTiets = item.getChiTiets();
        for (QlBangKeChCtLt chiTiet : chiTiets) {
            QlBangKeChCtLtRes chiTietRes = new QlBangKeChCtLtRes();
            BeanUtils.copyProperties(chiTiet, chiTietRes);
            response.getChiTiets().add(chiTietRes);
        }
        return response;
    }

    private List<QlBangKeChCtLt> saveListChiTiet(Long parentId, List<QlBangKeChCtLtReq> chiTietReqs, Map<Long, QlBangKeChCtLt> mapChiTiet) throws Exception {
        List<QlBangKeChCtLt> chiTiets = new ArrayList<>();
        for (QlBangKeChCtLtReq req : chiTietReqs) {
            Long id = req.getId();
            QlBangKeChCtLt chiTiet = new QlBangKeChCtLt();

            if (id != null) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Bảng kê chi tiết hàng Hóa không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setQlBangKeCanHangLtId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            qlBangKeChCtLtRepository.saveAll(chiTiets);

        return chiTiets;
    }

    @Override
    public QlBangKeCanHangLtRes update(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        QlBangKeCanHangLt item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        qlBangKeCanHangLtRepository.save(item);

        Map<Long, QlBangKeChCtLt> map = qlBangKeChCtLtRepository.findAllByQlBangKeCanHangLtId(item.getId())
                .stream().collect(Collectors.toMap(QlBangKeChCtLt::getId, Function.identity()));

        List<QlBangKeChCtLtReq> chiTietReqs = req.getChiTiets();
        List<QlBangKeChCtLt> chiTiets = this.saveListChiTiet(item.getId(), chiTietReqs, map);
        item.setChiTiets(chiTiets);

        return this.buildResponse(item);
    }

    @Override
    public QlBangKeCanHangLtRes detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        QlBangKeCanHangLt item = optional.get();
        item.setChiTiets(qlBangKeChCtLtRepository.findAllByQlBangKeCanHangLtId(item.getId()));
        return this.buildResponse(item);
    }

    @Override
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        QlBangKeCanHangLt item = optional.get();
        if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa bảng kê đã duyệt");
        }

        List<QlBangKeChCtLt> chiTiets = qlBangKeChCtLtRepository.findAllByQlBangKeCanHangLtId(item.getId());
        if (!CollectionUtils.isEmpty(chiTiets)) {
            qlBangKeChCtLtRepository.deleteAll(chiTiets);
        }

        qlBangKeCanHangLtRepository.delete(item);
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QlBangKeCanHangLt bangKe = optional.get();
        return this.updateStatus(req, bangKe, userInfo);
    }

    public boolean updateStatus(StatusReq req, QlBangKeCanHangLt bangKe, UserInfo userInfo) throws Exception {
        String trangThai = bangKe.getTrangThai();
        if (QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.MOI_TAO.getId().equals(trangThai))
                return false;

            bangKe.setTrangThai(QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId());
            bangKe.setNguoiGuiDuyetId(userInfo.getId());
            bangKe.setNgayGuiDuyet(LocalDate.now());

        } else if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;
            bangKe.setTrangThai(QdPheDuyetKqlcntVtStatus.DA_DUYET.getId());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
        } else if (QdPheDuyetKqlcntVtStatus.TU_CHOI.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;

            bangKe.setTrangThai(QdPheDuyetKqlcntVtStatus.TU_CHOI.getId());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
            bangKe.setLyDoTuChoi(req.getLyDo());
        } else {
            throw new Exception("Bad request.");
        }

        qlBangKeCanHangLtRepository.save(bangKe);
        return true;
    }

    @Override
    public Page<QlBangKeCanHangLtRes> search(QlBangKeCanHangLtSearchReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        req.setMaDonVi(userInfo.getDvql());
        return qlBangKeCanHangLtRepository.search(req);
    }
}
