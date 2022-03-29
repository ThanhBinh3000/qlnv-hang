package com.tcdt.qlnvhang.service.quanlyphieunhapkholuongthuc;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLt;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLt;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtReq;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtSearchReq;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtRes;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
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
public class QlPhieuNhapKhoLtServiceImpl implements QlPhieuNhapKhoLtService {

    @Autowired
    private QlPhieuNhapKhoLtRepository qlPhieuNhapKhoLtRepository;

    @Autowired
    private QlPhieuNhapKhoHangHoaLtRepository qlPhieuNhapKhoHangHoaLtRepository;
    @Autowired
    private QlnvDmVattuRepository qlnvDmVattuRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QlPhieuNhapKhoLtRes create(QlPhieuNhapKhoLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        QlPhieuNhapKhoLt phieu = new QlPhieuNhapKhoLt();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgayTao(LocalDate.now());
        phieu.setNguoiTaoId(userInfo.getId());
        phieu.setTrangThai(QdPheDuyetKqlcntVtStatus.MOI_TAO.getId());
        phieu.setMaDonVi(userInfo.getDvql());
        qlPhieuNhapKhoLtRepository.save(phieu);

        List<QlPhieuNhapKhoHangHoaLtReq> hangHoaReqs = req.getHangHoaList();
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = this.saveListHangHoa(phieu.getId(), hangHoaReqs, new HashMap<>());
        phieu.setHangHoaList(hangHoaList);

        return this.buildResponse(phieu);
    }

    private List<QlPhieuNhapKhoHangHoaLt> saveListHangHoa(Long phieuNhapKhoId, List<QlPhieuNhapKhoHangHoaLtReq> hangHoaReqs, Map<Long, QlPhieuNhapKhoHangHoaLt> mapHangHoa) throws Exception {
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = new ArrayList<>();
        Set<Long> vatTuIds = hangHoaReqs.stream().map(QlPhieuNhapKhoHangHoaLtReq::getVatTuId).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = Sets.newHashSet(qlnvDmVattuRepository.findAllById(vatTuIds));
        for (QlPhieuNhapKhoHangHoaLtReq req : hangHoaReqs) {
            Long id = req.getId();
            QlPhieuNhapKhoHangHoaLt hangHoa = new QlPhieuNhapKhoHangHoaLt();
            QlnvDmVattu vatTu = qlnvDmVattus.stream().filter(v -> v.getId().equals(req.getVatTuId())).findFirst().orElse(null);
            if (vatTu == null)
                throw new Exception("Hàng Hóa không tồn tại.");

            if (id != null) {
                hangHoa = mapHangHoa.get(id);
                if (hangHoa == null)
                    throw new Exception("Chi tiết hàng Hóa không tồn tại.");
                mapHangHoa.remove(id);
            }

            BeanUtils.copyProperties(req, hangHoa, "id");
            hangHoa.setQlPhieuNhapKhoLtId(phieuNhapKhoId);
            hangHoa.setVatTuId(req.getVatTuId());
            hangHoaList.add(hangHoa);
        }

        if (!CollectionUtils.isEmpty(hangHoaList))
            qlPhieuNhapKhoHangHoaLtRepository.saveAll(hangHoaList);

        return hangHoaList;
    }

    private QlPhieuNhapKhoLtRes buildResponse(QlPhieuNhapKhoLt phieu) {
        QlPhieuNhapKhoLtRes response = new QlPhieuNhapKhoLtRes();
        BeanUtils.copyProperties(phieu, response);
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = phieu.getHangHoaList();
        for (QlPhieuNhapKhoHangHoaLt hangHoa : hangHoaList) {
            QlPhieuNhapKhoHangHoaLtRes hangHoaRes = new QlPhieuNhapKhoHangHoaLtRes();
            BeanUtils.copyProperties(hangHoa, hangHoaRes);
            response.getHangHoaRes().add(hangHoaRes);
        }
        return response;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QlPhieuNhapKhoLtRes update(QlPhieuNhapKhoLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlPhieuNhapKhoLt> optionalQd = qlPhieuNhapKhoLtRepository.findById(req.getId());
        if (!optionalQd.isPresent())
            throw new Exception("Phiếu nhập kho không tồn tại.");

        QlPhieuNhapKhoLt phieu = optionalQd.get();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgaySua(LocalDate.now());
        phieu.setNguoiSuaId(userInfo.getId());
        qlPhieuNhapKhoLtRepository.save(phieu);

        Map<Long, QlPhieuNhapKhoHangHoaLt> mapHangHoa = qlPhieuNhapKhoHangHoaLtRepository.findAllByQlPhieuNhapKhoLtId(phieu.getId())
                .stream().collect(Collectors.toMap(QlPhieuNhapKhoHangHoaLt::getId, Function.identity()));

        List<QlPhieuNhapKhoHangHoaLtReq> hangHoaReqs = req.getHangHoaList();
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = this.saveListHangHoa(phieu.getId(), hangHoaReqs, mapHangHoa);
        phieu.setHangHoaList(hangHoaList);

        return this.buildResponse(phieu);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlPhieuNhapKhoLt> optional = qlPhieuNhapKhoLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QlPhieuNhapKhoLt phieu = optional.get();
        if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(phieu.getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }

        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = qlPhieuNhapKhoHangHoaLtRepository.findAllByQlPhieuNhapKhoLtId(phieu.getId());
        if (!CollectionUtils.isEmpty(hangHoaList)) {
            qlPhieuNhapKhoHangHoaLtRepository.deleteAll(hangHoaList);
        }

        qlPhieuNhapKhoLtRepository.delete(phieu);
        return true;
    }

    @Override
    public QlPhieuNhapKhoLtRes detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlPhieuNhapKhoLt> optional = qlPhieuNhapKhoLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QlPhieuNhapKhoLt phieu = optional.get();
        phieu.setHangHoaList(qlPhieuNhapKhoHangHoaLtRepository.findAllByQlPhieuNhapKhoLtId(phieu.getId()));
        return this.buildResponse(phieu);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<QlPhieuNhapKhoLt> optional = qlPhieuNhapKhoLtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QlPhieuNhapKhoLt phieu = optional.get();
        return this.updateStatus(req, phieu, userInfo);
    }

    public boolean updateStatus(StatusReq req, QlPhieuNhapKhoLt phieu, UserInfo userInfo) throws Exception {
        String trangThai = phieu.getTrangThai();
        if (QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.MOI_TAO.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId());
            phieu.setNguoiGuiDuyetId(userInfo.getId());
            phieu.setNgayGuiDuyet(LocalDate.now());

        } else if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;
            phieu.setTrangThai(QdPheDuyetKqlcntVtStatus.DA_DUYET.getId());
            phieu.setNguoiPheDuyetId(userInfo.getId());
            phieu.setNgayPheDuyet(LocalDate.now());
        } else if (QdPheDuyetKqlcntVtStatus.TU_CHOI.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(QdPheDuyetKqlcntVtStatus.TU_CHOI.getId());
            phieu.setNguoiPheDuyetId(userInfo.getId());
            phieu.setNgayPheDuyet(LocalDate.now());
            phieu.setLyDoTuChoi(req.getLyDo());
        } else {
            throw new Exception("Bad request.");
        }

        qlPhieuNhapKhoLtRepository.save(phieu);
        return true;
    }

    @Override
    public Page<QlPhieuNhapKhoLtRes> search(QlPhieuNhapKhoLtSearchReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        return qlPhieuNhapKhoLtRepository.search(req);
    }
}
