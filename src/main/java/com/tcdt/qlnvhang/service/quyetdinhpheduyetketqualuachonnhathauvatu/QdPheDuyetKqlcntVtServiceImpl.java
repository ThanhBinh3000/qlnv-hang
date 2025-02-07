package com.tcdt.qlnvhang.service.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGoiThauVt;
import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGtDdnVt;
import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVt;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGoiThauVtRepository;
import com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGtDdnVtRepository;
import com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGoiThauVtReq;
import com.tcdt.qlnvhang.request.object.quyetdinhpheduyetketqualuachonnhathauvatu.QdKqlcntGtDdnVtReq;
import com.tcdt.qlnvhang.request.object.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtReq;
import com.tcdt.qlnvhang.request.search.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtSearchReq;
import com.tcdt.qlnvhang.response.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.donvi.QlnvDmDonViService;
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
public class QdPheDuyetKqlcntVtServiceImpl implements QdPheDuyetKqlcntVtService {

    @Autowired
    private QdPheDuyetKqlcntVtRepository qdPheDuyetKqlcntVtRepo;

    @Autowired
    private QdKqlcntGoiThauVtRepository qdKqlcntGoiThauVtRepo;

    @Autowired
    private QdKqlcntGtDdnVtRepository qdKqlcntGtDdnVtRepo;

    @Autowired
    private QdKqlcntGoiThauVtService qdKqlcntGoiThauVtService;

    @Autowired
    private QlnvDmVattuRepository qlnvDmVattuRepository;

    @Autowired
    private QlnvDmDonViService qlnvDmDonViService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QdPheDuyetKqlcntVtRes create(QdPheDuyetKqlcntVtReq req) throws Exception {

        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        QlnvDmVattu vattu = qlnvDmVattuRepository.findByMa(req.getMaVatTu());
        if (vattu == null)
            throw new Exception("Vật tư không tồn tại.");

        QdPheDuyetKqlcntVt qd = new QdPheDuyetKqlcntVt();
        BeanUtils.copyProperties(req, qd, "id");
        qd.setTrangThai(QdPheDuyetKqlcntVtStatus.MOI_TAO.getId());
        qd.setMaDonVi(userInfo.getDvql());
        qd.setCapDonVi(qlnvDmDonViService.getCapDviByMa(userInfo.getDvql()));
        qdPheDuyetKqlcntVtRepo.save(qd);

        List<QdKqlcntGoiThauVt> goiThauList = new ArrayList<>();
        List<QdKqlcntGoiThauVtReq> goiThauReqs = req.getGoiThauReqs();
        for (QdKqlcntGoiThauVtReq goiThauReq : goiThauReqs) {
            QdKqlcntGoiThauVt goiThau = this.saveGoiThauVt(qd.getId(), goiThauReq, new HashMap<>());
            List<QdKqlcntGtDdnVtReq> ddnReqs = goiThauReq.getDdnReqs();
            this.saveListDdnVt(goiThau.getId(), ddnReqs, new HashMap<>());
            goiThauList.add(goiThau);
        }

        qd.setGoiThauList(goiThauList);
        return this.buildResponse(qd, vattu);
    }

    private QdKqlcntGoiThauVt saveGoiThauVt(Long qdId, QdKqlcntGoiThauVtReq goiThauReq, Map<Long, QdKqlcntGoiThauVt> mapGoiThau) throws Exception {
        Long id = goiThauReq.getId();
        QdKqlcntGoiThauVt goiThau = new QdKqlcntGoiThauVt();

        if (id != null && id > 0) {
            goiThau = mapGoiThau.get(id);
            if (goiThau == null)
                throw new Exception("Gói thầu không tồn tại.");
            mapGoiThau.remove(id);
        }

        BeanUtils.copyProperties(goiThauReq, goiThau, "id");
        goiThau.setQdPdKhlcntId(qdId);
        mapGoiThau.remove(id);
        return qdKqlcntGoiThauVtRepo.save(goiThau);
    }

    private List<QdKqlcntGtDdnVt> saveListDdnVt(Long goiThauId, List<QdKqlcntGtDdnVtReq> ddnReqs, Map<Long, QdKqlcntGtDdnVt> mapDdn) throws Exception {
        List<QdKqlcntGtDdnVt> ddnList = new ArrayList<>();

        for (QdKqlcntGtDdnVtReq req : ddnReqs) {
            Long id = req.getId();
            QdKqlcntGtDdnVt ddn = new QdKqlcntGtDdnVt();

            if (id != null) {
                ddn = mapDdn.get(id);
                if (ddn == null)
                    throw new Exception("Không tồn tại địa điểm nhập");
                mapDdn.remove(id);
            }

            BeanUtils.copyProperties(req, ddn, "id");
            ddn.setGoiThauId(goiThauId);
            ddnList.add(ddn);
        }

        if (!CollectionUtils.isEmpty(ddnList))
            qdKqlcntGtDdnVtRepo.saveAll(ddnList);

        return ddnList;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QdPheDuyetKqlcntVtRes update(QdPheDuyetKqlcntVtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QdPheDuyetKqlcntVt> optionalQd = qdPheDuyetKqlcntVtRepo.findById(req.getId());
        if (!optionalQd.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QlnvDmVattu vattu = qlnvDmVattuRepository.findByMa(req.getMaVatTu());
        if (vattu == null)
            throw new Exception("Vật tư không tồn tại.");

        QdPheDuyetKqlcntVt qd = optionalQd.get();
        BeanUtils.copyProperties(req, qd, "id");
        qdPheDuyetKqlcntVtRepo.save(qd);

        Map<Long, QdKqlcntGoiThauVt> mapGoiThau = qdKqlcntGoiThauVtRepo.findAllByQdPdKhlcntId(qd.getId())
                .stream().collect(Collectors.toMap(QdKqlcntGoiThauVt::getId, Function.identity()));

        Map<Long, QdKqlcntGtDdnVt> mapDdn = new HashMap<>();
        Set<Long> goiThauIds = mapGoiThau.keySet();
        if (!CollectionUtils.isEmpty(goiThauIds)) {
            List<QdKqlcntGtDdnVt> ddns = qdKqlcntGtDdnVtRepo.findByGoiThauIdIn(goiThauIds);
            mapDdn = ddns.stream().collect(Collectors.toMap(QdKqlcntGtDdnVt::getId, Function.identity()));
        }

        List<QdKqlcntGoiThauVtReq> goiThauReqs = req.getGoiThauReqs();
        for (QdKqlcntGoiThauVtReq goiThauReq : goiThauReqs) {
            QdKqlcntGoiThauVt goiThau = this.saveGoiThauVt(qd.getId(), goiThauReq, mapGoiThau);
            List<QdKqlcntGtDdnVtReq> ddnReqs = goiThauReq.getDdnReqs();
            this.saveListDdnVt(goiThau.getId(), ddnReqs, mapDdn);
        }

        if (!CollectionUtils.isEmpty(mapGoiThau.values()))
            qdKqlcntGoiThauVtRepo.deleteAll(mapGoiThau.values());

        if (!CollectionUtils.isEmpty(mapDdn.values()))
            qdKqlcntGtDdnVtRepo.deleteAll(mapDdn.values());
        return this.buildResponse(qd, vattu);
    }

    private QdPheDuyetKqlcntVtRes buildResponse(QdPheDuyetKqlcntVt qd, QlnvDmVattu vattu) {
        QdPheDuyetKqlcntVtRes response = new QdPheDuyetKqlcntVtRes();
        BeanUtils.copyProperties(qd, response);
        response.setVatTuId(vattu.getId());
        response.setMaVatTu(vattu.getMa());
        response.setTenVatTu(vattu.getTen());
        List<QdKqlcntGoiThauVt> goiThauList = qd.getGoiThauList();
        for (QdKqlcntGoiThauVt goiThau : goiThauList) {
            response.getGoiThaus().add(qdKqlcntGoiThauVtService.buildResponseForList(goiThau));
        }
        return response;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QdPheDuyetKqlcntVt> optionalQd = qdPheDuyetKqlcntVtRepo.findById(id);
        if (!optionalQd.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QdPheDuyetKqlcntVt qd = optionalQd.get();
        if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(qd.getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }

        List<QdKqlcntGoiThauVt> goiThauList = qdKqlcntGoiThauVtRepo.findAllByQdPdKhlcntId(qd.getId());
        if (!CollectionUtils.isEmpty(goiThauList)) {
            Set<Long> goiThauIds = goiThauList.stream().map(QdKqlcntGoiThauVt::getId).collect(Collectors.toSet());
            List<QdKqlcntGtDdnVt> ddnList = qdKqlcntGtDdnVtRepo.findByGoiThauIdIn(goiThauIds);
            if (CollectionUtils.isEmpty(ddnList)) {
                qdKqlcntGtDdnVtRepo.deleteAll(ddnList);
            }
            qdKqlcntGoiThauVtRepo.deleteAll(goiThauList);
        }

        qdPheDuyetKqlcntVtRepo.delete(qd);
        return true;
    }

    @Override
    public Page<QdPheDuyetKqlcntVtRes> search(QdPheDuyetKqlcntVtSearchReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        return qdPheDuyetKqlcntVtRepo.search(req);
    }

    @Override
    public Object detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QdPheDuyetKqlcntVt> optionalQd = qdPheDuyetKqlcntVtRepo.findById(id);
        if (!optionalQd.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QdPheDuyetKqlcntVt qd = optionalQd.get();
        QlnvDmVattu vattu = qlnvDmVattuRepository.findByMa(qd.getMaVatTu());
        if (vattu == null)
            throw new Exception("Vật tư không tồn tại.");

        qd.setGoiThauList(qdKqlcntGoiThauVtRepo.findAllByQdPdKhlcntId(qd.getId()));

        return this.buildResponse(qd, vattu);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<QdPheDuyetKqlcntVt> optionalQd = qdPheDuyetKqlcntVtRepo.findById(req.getId());
        if (!optionalQd.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QdPheDuyetKqlcntVt qd = optionalQd.get();
        return this.updateStatus(req, qd, userInfo);
    }

    public boolean updateStatus(StatusReq req, QdPheDuyetKqlcntVt qd, UserInfo userInfo) throws Exception {
        String trangThai = qd.getTrangThai();
        if (QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.MOI_TAO.getId().equals(trangThai))
                return false;

            qd.setTrangThai(QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId());
            qd.setNguoiGuiDuyetId(userInfo.getId());
            qd.setNgayGuiDuyet(LocalDate.now());

        } else if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;
            qd.setTrangThai(QdPheDuyetKqlcntVtStatus.DA_DUYET.getId());
            qd.setNguoiPheDuyetId(userInfo.getId());
            qd.setNgayPheDuyet(LocalDate.now());
        } else if (QdPheDuyetKqlcntVtStatus.TU_CHOI.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;

            qd.setTrangThai(QdPheDuyetKqlcntVtStatus.TU_CHOI.getId());
            qd.setNguoiPheDuyetId(userInfo.getId());
            qd.setNgayPheDuyet(LocalDate.now());
            qd.setLyDoTuChoi(req.getLyDo());
        } else {
            throw new Exception("Bad request.");
        }

        qdPheDuyetKqlcntVtRepo.save(qd);
        return true;
    }

    @Override
    public Object export() {
        return null;
    }
}
