package com.tcdt.qlnvhang.service.quanlybangkecanhangluongthuc;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeChCtLt;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRepository;
import com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc.QlBangKeChCtLtRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeChCtLtReq;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRes;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeChCtLtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QlBangKeCanHangLtServiceImpl extends BaseServiceImpl implements QlBangKeCanHangLtService {

    @Autowired
    private QlBangKeCanHangLtRepository qlBangKeCanHangLtRepository;

    @Autowired
    private QlBangKeChCtLtRepository qlBangKeChCtLtRepository;

    @Autowired
    private KtNganLoRepository ktNganLoRepository;

    @Autowired
    private KtDiemKhoRepository ktDiemKhoRepository;

    @Autowired
    private KtNhaKhoRepository ktNhaKhoRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
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
        item.setTrangThai(TrangThaiEnum.DU_THAO.getMa());
        item.setMaDonVi(userInfo.getDvql());
        item.setCapDonVi(userInfo.getCapDvi());
        qlBangKeCanHangLtRepository.save(item);

        List<QlBangKeChCtLtReq> chiTietReqs = req.getChiTiets();
        List<QlBangKeChCtLt> chiTiets = this.saveListChiTiet(item.getId(), chiTietReqs, new HashMap<>());
        item.setChiTiets(chiTiets);

        List<KtNganLo> byMaNganloIn = StringUtils.hasText(item.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(item.getMaNganLo())) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(item.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(item.getMaDiemKho())) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(item.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(item.getMaNhaKho())) : new ArrayList<>();


        return this.buildResponse(item, byMaNganloIn , byMaDiemkhoIn, byMaNhakhoIn);
    }

    private QlBangKeCanHangLtRes buildResponse(QlBangKeCanHangLt item,
                                               Collection<KtNganLo> nganLos,
                                               Collection<KtDiemKho> diemKhos,
                                               Collection<KtNhaKho> nhaKhos) {
        QlBangKeCanHangLtRes response = new QlBangKeCanHangLtRes();
        BeanUtils.copyProperties(item, response);

        Map<String, String> mapDmucDvi = getMapTenDvi();
        response.setTenDonViLap(mapDmucDvi.get(item.getMaDonViLap()));
        response.setTenTrangThai(TrangThaiEnum.getTen(item.getTrangThai()));
        response.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));

        nganLos.stream().filter(l -> l.getMaNganlo().equals(item.getMaNganLo())).findFirst().ifPresent(l -> {
            response.setTenNganLo(l.getTenNganlo());
            response.setNganLoId(l.getId());
        });

        diemKhos.stream().filter(l -> l.getMaDiemkho().equals(item.getMaDiemKho())).findFirst().ifPresent(l -> {
            response.setTenDiemKho(l.getTenDiemkho());
            response.setDiemKhoId(l.getId());
        });

        nhaKhos.stream().filter(l -> l.getMaNhakho().equals(item.getMaNhaKho())).findFirst().ifPresent(l -> {
            response.setTenNhaKho(l.getTenNhakho());
            response.setNhaKhoId(l.getId());
        });


        List<QlBangKeChCtLt> chiTiets = item.getChiTiets();
        List<QlBangKeChCtLtRes> chiTietResList = new ArrayList<>();
        for (QlBangKeChCtLt chiTiet : chiTiets) {
            QlBangKeChCtLtRes chiTietRes = new QlBangKeChCtLtRes();
            BeanUtils.copyProperties(chiTiet, chiTietRes);
            chiTietResList.add(chiTietRes);
        }
        response.setChiTiets(chiTietResList);
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
    @Transactional(rollbackOn = Exception.class)
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

        if (!CollectionUtils.isEmpty(map.values()))
            qlBangKeChCtLtRepository.deleteAll(map.values());

        List<KtNganLo> byMaNganloIn = StringUtils.hasText(item.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(item.getMaNganLo())) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(item.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(item.getMaDiemKho())) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(item.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(item.getMaNhaKho())) : new ArrayList<>();
        return this.buildResponse(item,byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn);
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

        List<KtNganLo> byMaNganloIn = StringUtils.hasText(item.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(item.getMaNganLo())) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(item.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(item.getMaDiemKho())) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(item.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(item.getMaNhaKho())) : new ArrayList<>();
        return this.buildResponse(item,byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        QlBangKeCanHangLt item = optional.get();
        if (TrangThaiEnum.BAN_HANH.getMa().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa bảng kê đã ban hành");
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

    public boolean updateStatus(StatusReq stReq, QlBangKeCanHangLt bangKe, UserInfo userInfo) throws Exception {
        String trangThai = bangKe.getTrangThai();
        if (TrangThaiEnum.DU_THAO_TRINH_DUYET.getMa().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.DU_THAO.getMa().equals(trangThai))
                return false;

            bangKe.setTrangThai(TrangThaiEnum.DU_THAO_TRINH_DUYET.getMa());
            bangKe.setNguoiGuiDuyetId(userInfo.getId());
            bangKe.setNgayGuiDuyet(LocalDate.now());
        } else if (TrangThaiEnum.LANH_DAO_DUYET.getMa().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getMa().equals(trangThai))
                return false;
            bangKe.setTrangThai(TrangThaiEnum.LANH_DAO_DUYET.getMa());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
        } else if (TrangThaiEnum.BAN_HANH.getMa().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.LANH_DAO_DUYET.getMa().equals(trangThai))
                return false;

            bangKe.setTrangThai(TrangThaiEnum.BAN_HANH.getMa());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
        } else if (TrangThaiEnum.TU_CHOI.getMa().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getMa().equals(trangThai))
                return false;

            bangKe.setTrangThai(TrangThaiEnum.TU_CHOI.getMa());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
            bangKe.setLyDoTuChoi(stReq.getLyDo());
        }  else {
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

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<QlBangKeCanHangLt> data = qlBangKeCanHangLtRepository.search(req);
        Set<String> maNganLos =  new HashSet<>();
        Set<String> maDiemKhos = new HashSet<>();
        Set<String> maNhaKhos = new HashSet<>();
        for (QlBangKeCanHangLt bb : data) {

            if (StringUtils.hasText(bb.getMaNganLo()))
                maNganLos.add(bb.getMaNganLo());

            if (StringUtils.hasText(bb.getMaDiemKho()))
                maDiemKhos.add(bb.getMaDiemKho());

            if (StringUtils.hasText(bb.getMaNhaKho()))
                maNhaKhos.add(bb.getMaNhaKho());
        }

        List<KtNganLo> byMaNganloIn = !CollectionUtils.isEmpty(maNganLos) ? ktNganLoRepository.findByMaNganloIn(maNganLos) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = !CollectionUtils.isEmpty(maDiemKhos) ? ktDiemKhoRepository.findByMaDiemkhoIn(maDiemKhos) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = !CollectionUtils.isEmpty(maNhaKhos) ?  ktNhaKhoRepository.findByMaNhakhoIn(maNhaKhos) : new ArrayList<>();

        List<QlBangKeCanHangLtRes> responses = new ArrayList<>();
        data.forEach(d -> {
            responses.add(buildResponse(d, byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn));
        });
        return new PageImpl<>(responses, pageable, qlBangKeCanHangLtRepository.count(req));
    }
}
