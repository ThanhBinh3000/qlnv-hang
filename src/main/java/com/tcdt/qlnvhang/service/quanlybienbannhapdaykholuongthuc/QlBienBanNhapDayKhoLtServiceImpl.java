package com.tcdt.qlnvhang.service.quanlybienbannhapdaykholuongthuc;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLt;
import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLt;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtRepository;
import com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtReq;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtReq;
import com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtSearchReq;
import com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtRes;
import com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    private KtNganLoRepository ktNganLoRepository;

    @Autowired
    private KtDiemKhoRepository ktDiemKhoRepository;

    @Autowired
    private KtNhaKhoRepository ktNhaKhoRepository;

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
        item.setTrangThai(TrangThaiEnum.DU_THAO.getMa());
        item.setMaDonVi(userInfo.getDvql());
        item.setCapDonVi(userInfo.getCapDvi());
        qlBienBanNhapDayKhoLtRepository.save(item);

        List<QlBienBanNdkCtLt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);

        List<KtNganLo> byMaNganloIn = StringUtils.hasText(item.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(item.getMaNganLo())) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(item.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(item.getMaDiemKho())) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(item.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(item.getMaNhaKho())) : new ArrayList<>();

        return buildResponse(item, byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn, userInfo);
    }

    private QlBienBanNhapDayKhoLtRes buildResponse(QlBienBanNhapDayKhoLt item,
                                                   Collection<KtNganLo> nganLos,
                                                   Collection<KtDiemKho> diemKhos,
                                                   Collection<KtNhaKho> nhaKhos,
                                                   UserInfo userInfo) {
        QlBienBanNhapDayKhoLtRes response = new QlBienBanNhapDayKhoLtRes();
        BeanUtils.copyProperties(item, response);
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

        List<QlBienBanNdkCtLt> chiTiets = item.getChiTiets();
        List<QlBienBanNdkCtLtRes> chiTietResList = new ArrayList<>();
        for (QlBienBanNdkCtLt chiTiet : chiTiets) {
            QlBienBanNdkCtLtRes chiTietRes = new QlBienBanNdkCtLtRes();
            BeanUtils.copyProperties(chiTiet, chiTietRes);
            chiTietResList.add(chiTietRes);
        }
        response.setChiTiets(chiTietResList);
        response.setTenDvi(userInfo.getTenDvi());
        response.setMaDvi(userInfo.getDvql());
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
        if (!CollectionUtils.isEmpty(map.values()))
            qlBienBanNdkCtLtRepository.deleteAll(map.values());

        List<KtNganLo> byMaNganloIn = StringUtils.hasText(item.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(item.getMaNganLo())) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(item.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(item.getMaDiemKho())) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(item.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(item.getMaNhaKho())) : new ArrayList<>();

        return this.buildResponse(item, byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn, userInfo);
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
        List<KtNganLo> byMaNganloIn = StringUtils.hasText(item.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(item.getMaNganLo())) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(item.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(item.getMaDiemKho())) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(item.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(item.getMaNhaKho())) : new ArrayList<>();

        return this.buildResponse(item, byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn, userInfo);
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
        if (TrangThaiEnum.BAN_HANH.getMa().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa bảng kê đã ban hành");
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
        List<QlBienBanNhapDayKhoLt> list = qlBienBanNhapDayKhoLtRepository.search(req);

        Set<String> maNganLos =  new HashSet<>();
        Set<String> maDiemKhos = new HashSet<>();
        Set<String> maNhaKhos = new HashSet<>();
        for (QlBienBanNhapDayKhoLt bb : list) {

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
        List<QlBienBanNhapDayKhoLtRes> data = new ArrayList<>();
        for (QlBienBanNhapDayKhoLt bb : list) {
            data.add(this.buildResponse(bb, byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn, userInfo));
        }
        return new PageImpl<>(data, PageRequest.of(req.getPaggingReq().getPage(),  req.getPaggingReq().getLimit()), qlBienBanNhapDayKhoLtRepository.count(req));
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

    public boolean updateStatus(StatusReq stReq, QlBienBanNhapDayKhoLt bangKe, UserInfo userInfo) throws Exception {
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

        qlBienBanNhapDayKhoLtRepository.save(bangKe);
        return true;
    }
}
