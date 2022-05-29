package com.tcdt.qlnvhang.service.quanlyphieunhapkholuongthuc;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLt;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLt;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.enums.QlPhieuNhapKhoLtStatus;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtReq;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtSearchReq;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtRes;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.donvi.QlnvDmDonViService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
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

@Service
public class QlPhieuNhapKhoLtServiceImpl extends BaseServiceImpl implements QlPhieuNhapKhoLtService {

    @Autowired
    private QlPhieuNhapKhoLtRepository qlPhieuNhapKhoLtRepository;

    @Autowired
    private QlPhieuNhapKhoHangHoaLtRepository qlPhieuNhapKhoHangHoaLtRepository;
    @Autowired
    private QlnvDmVattuRepository qlnvDmVattuRepository;

    @Autowired
    private QlnvDmDonViService qlnvDmDonViService;

    @Autowired
    private KtNganLoRepository ktNganLoRepository;

    @Autowired
    private KtDiemKhoRepository ktDiemKhoRepository;

    @Autowired
    private KtNhaKhoRepository ktNhaKhoRepository;

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
        phieu.setNgayTao(req.getNgayTao());
        phieu.setNguoiTaoId(userInfo.getId());
        phieu.setTrangThai(QlPhieuNhapKhoLtStatus.DU_THAO.getId());
        phieu.setMaDonVi(userInfo.getDvql());
        phieu.setCapDonVi(qlnvDmDonViService.getCapDviByMa(userInfo.getDvql()));
        qlPhieuNhapKhoLtRepository.save(phieu);

        List<QlPhieuNhapKhoHangHoaLtReq> hangHoaReqs = req.getHangHoaList();
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = this.saveListHangHoa(phieu.getId(), hangHoaReqs, new HashMap<>());
        phieu.setHangHoaList(hangHoaList);

        Set<String> maVatTus = hangHoaList.stream().map(QlPhieuNhapKhoHangHoaLt::getMaVatTu).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = !CollectionUtils.isEmpty(maVatTus) ? Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus)) : new HashSet<>();

        List<KtNganLo> byMaNganloIn = StringUtils.hasText(phieu.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(phieu.getMaNganLo())) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(phieu.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(phieu.getMaDiemKho())) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(phieu.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(phieu.getMaNhaKho())) : new ArrayList<>();
        return this.buildResponse(phieu, qlnvDmVattus, byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn);
    }

    private List<QlPhieuNhapKhoHangHoaLt> saveListHangHoa(Long phieuNhapKhoId, List<QlPhieuNhapKhoHangHoaLtReq> hangHoaReqs, Map<Long, QlPhieuNhapKhoHangHoaLt> mapHangHoa) throws Exception {
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = new ArrayList<>();
        Set<String> maVatTus = hangHoaReqs.stream().map(QlPhieuNhapKhoHangHoaLtReq::getMaVatTu).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus));
        for (QlPhieuNhapKhoHangHoaLtReq req : hangHoaReqs) {
            Long id = req.getId();
            QlPhieuNhapKhoHangHoaLt hangHoa = new QlPhieuNhapKhoHangHoaLt();
            QlnvDmVattu vatTu = qlnvDmVattus.stream().filter(v -> v.getMa().equals(req.getMaVatTu())).findFirst().orElse(null);
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
            hangHoa.setMaVatTu(req.getMaVatTu());
            hangHoaList.add(hangHoa);
        }

        if (!CollectionUtils.isEmpty(hangHoaList))
            qlPhieuNhapKhoHangHoaLtRepository.saveAll(hangHoaList);

        return hangHoaList;
    }

    private QlPhieuNhapKhoLtRes buildResponse(QlPhieuNhapKhoLt phieu,
                                              Set<QlnvDmVattu> qlnvDmVattus,
                                              Collection<KtNganLo> nganLos,
                                              Collection<KtDiemKho> diemKhos,
                                              Collection<KtNhaKho> nhaKhos) {
        QlPhieuNhapKhoLtRes response = new QlPhieuNhapKhoLtRes();
        BeanUtils.copyProperties(phieu, response);
        response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTenById(phieu.getTrangThai()));
        response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTrangThaiDuyetById(phieu.getTrangThai()));
        nganLos.stream().filter(l -> l.getMaNganlo().equals(phieu.getMaNganLo())).findFirst().ifPresent(l -> {
            response.setTenNganLo(l.getTenNganlo());
            response.setNganLoId(l.getId());
        });

        diemKhos.stream().filter(l -> l.getMaDiemkho().equals(phieu.getMaDiemKho())).findFirst().ifPresent(l -> {
            response.setTenDiemKho(l.getTenDiemkho());
            response.setDiemKhoId(l.getId());
        });

        nhaKhos.stream().filter(l -> l.getMaNhakho().equals(phieu.getMaNhaKho())).findFirst().ifPresent(l -> {
            response.setTenNhaKho(l.getTenNhakho());
            response.setNhaKhoId(l.getId());
        });
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = phieu.getHangHoaList();
        for (QlPhieuNhapKhoHangHoaLt hangHoa : hangHoaList) {
            QlPhieuNhapKhoHangHoaLtRes hangHoaRes = new QlPhieuNhapKhoHangHoaLtRes();
            BeanUtils.copyProperties(hangHoa, hangHoaRes);

            qlnvDmVattus.stream().filter(v -> v.getMa().equals(hangHoa.getMaVatTu())).findFirst().ifPresent(t -> {
                hangHoaRes.setTenVatTu(t.getTen());
            });
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

        if (!CollectionUtils.isEmpty(mapHangHoa.values()))
            qlPhieuNhapKhoHangHoaLtRepository.deleteAll(mapHangHoa.values());

        Set<String> maVatTus = hangHoaList.stream().map(QlPhieuNhapKhoHangHoaLt::getMaVatTu).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = !CollectionUtils.isEmpty(maVatTus) ? Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus)) : new HashSet<>();

        List<KtNganLo> byMaNganloIn = StringUtils.hasText(phieu.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(phieu.getMaNganLo())) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(phieu.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(phieu.getMaDiemKho())) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(phieu.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(phieu.getMaNhaKho())) : new ArrayList<>();

        return this.buildResponse(phieu, qlnvDmVattus,byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn);
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

        Set<String> maVatTus = phieu.getHangHoaList().stream().map(QlPhieuNhapKhoHangHoaLt::getMaVatTu).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = !CollectionUtils.isEmpty(maVatTus) ? Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus)) : new HashSet<>();

        List<KtNganLo> byMaNganloIn = StringUtils.hasText(phieu.getMaNganLo()) ? ktNganLoRepository.findByMaNganloIn(Sets.newHashSet(phieu.getMaNganLo())) : new ArrayList<>();
        List<KtDiemKho> byMaDiemkhoIn = StringUtils.hasText(phieu.getMaDiemKho()) ? ktDiemKhoRepository.findByMaDiemkhoIn(Sets.newHashSet(phieu.getMaDiemKho())) : new ArrayList<>();
        List<KtNhaKho> byMaNhakhoIn = StringUtils.hasText(phieu.getMaNhaKho()) ?  ktNhaKhoRepository.findByMaNhakhoIn(Sets.newHashSet(phieu.getMaNhaKho())) : new ArrayList<>();
        return this.buildResponse(phieu, qlnvDmVattus, byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn);
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

    @Override
    public Page<QlPhieuNhapKhoLtRes> timKiem(QlPhieuNhapKhoLtSearchReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").ascending());
        Page<QlPhieuNhapKhoLt> page = qlPhieuNhapKhoLtRepository.select(req.getSoPhieu(), convertDateToString(req.getNgayNhapKho()), req.getMaDiemKho(), req.getMaKhoNgan(), convertDateToString(req.getNgayTaoPhieu()), convertDateToString(req.getNgayGiaoNhanHang()), req.getMaNhaKho(), req.getNguoiGiaoHang(), pageable);
        List<QlPhieuNhapKhoLt> list = page.getContent();

        Set<String> maVatTus = list.stream()
                .flatMap(p -> p.getHangHoaList().stream())
                .map(QlPhieuNhapKhoHangHoaLt::getMaVatTu).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = !CollectionUtils.isEmpty(maVatTus) ? Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus)) : new HashSet<>();

        Set<String> maNganLos = list.stream()
                .map(QlPhieuNhapKhoLt::getMaNganLo).collect(Collectors.toSet());

        List<KtNganLo> byMaNganloIn = !CollectionUtils.isEmpty(maNganLos) ? ktNganLoRepository.findByMaNganloIn(maNganLos) : new ArrayList<>();

        Set<String> maDiemKhos = list.stream()
                .map(QlPhieuNhapKhoLt::getMaDiemKho).collect(Collectors.toSet());
        List<KtDiemKho> byMaDiemkhoIn = !CollectionUtils.isEmpty(maDiemKhos) ? ktDiemKhoRepository.findByMaDiemkhoIn(maDiemKhos) : new ArrayList<>();

        Set<String> maNhaKhos = list.stream()
                .map(QlPhieuNhapKhoLt::getMaNhaKho).collect(Collectors.toSet());
        List<KtNhaKho> byMaNhakhoIn = !CollectionUtils.isEmpty(maNhaKhos) ?  ktNhaKhoRepository.findByMaNhakhoIn(maNhaKhos) : new ArrayList<>();

        List<QlPhieuNhapKhoLtRes> resList = new ArrayList<>();
        for (QlPhieuNhapKhoLt phieu : list) {
            resList.add(this.buildResponse(phieu, qlnvDmVattus, byMaNganloIn, byMaDiemkhoIn, byMaNhakhoIn));
        }
        return new PageImpl<>(resList, pageable, page.getTotalElements());
    }

    private void setTenDvi(HhQdGiaoNvuNhapxuatHdr data) {
        Map<String, String> mapDmucDvi = getMapTenDvi();
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
    }
}
