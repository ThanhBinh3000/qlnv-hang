package com.tcdt.qlnvhang.service.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.*;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtietRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.request.search.HhDthauSearchReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.response.dauthauvattu.ChiTietGoiThauRes;
import com.tcdt.qlnvhang.service.HhDauThauService;
import com.tcdt.qlnvhang.service.HhQdKhlcntHdrService;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhDauThauServiceImpl extends BaseServiceImpl implements HhDauThauService {

    @Autowired
    HhQdKhlcntDtlRepository dtlRepository;
    @Autowired
    HhQdKhlcntDsgthauRepository goiThauRepository;

    @Autowired
    HhDthauNthauDuthauRepository nhaThauDuthauRepository;

    @Autowired
    HhQdKhlcntHdrService hhQdKhlcntHdrService;

    @Autowired
    HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

    @Autowired
    HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

    @Autowired
    HhQdPduyetKqlcntHdrRepository hhQdPduyetKqlcntHdrRepository;

    @Autowired
    private HhDchinhDxKhLcntDsgthauRepository dchinhDxKhLcntDsgthauRepository;

    @Autowired
    private HhDchinhDxKhLcntHdrRepository hhDchinhDxKhLcntHdrRepository;

    @Autowired
    private HhQdPduyetKqlcntDtlRepository hhQdPduyetKqlcntDtlRepository;

    @Autowired
    private HhQdKhlcntDsgthauCtietRepository hhQdKhlcntDsgthauCtietRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    @Transactional
    public List<HhDthauNthauDuthau> create(HhDthauReq objReq) throws Exception {
        if(objReq.getLoaiVthh().startsWith("02")){
            return createVatTu(objReq);
        }else{
            return createLuongThuc(objReq);
        }
    }

    @Override
    @Transactional
    public void updateKqLcnt(HhDthauReq objReq) throws Exception {
        Optional<HhDthauNthauDuthau> nhaThau = nhaThauDuthauRepository.findById(objReq.getId());
        if(!nhaThau.isPresent()){
            throw new Exception("Nhà thầu không tồn tại");
        }
        if (objReq.getType().equals("DC")) {
            Optional<HhDchinhDxKhLcntDsgthau> byId = dchinhDxKhLcntDsgthauRepository.findById(objReq.getIdGoiThau());
            if(!byId.isPresent()){
                throw new Exception("Gói thầu không tồn tại");
            }
            Optional<HhDchinhDxKhLcntHdr> dchinhDxKhLcntHdr = hhDchinhDxKhLcntHdrRepository.findById(byId.get().getIdDcDxHdr());
            if (!dchinhDxKhLcntHdr.isPresent()) {
                throw new Exception("Quyết định điều chỉnh KH LCNT không tồn tại");
            }
            byId.get().setIdNhaThau(objReq.getId());
            byId.get().setDonGiaNhaThau(objReq.getDonGia());
            byId.get().setTrangThaiDt(objReq.getTrangThai());
            byId.get().setTenNhaThau(nhaThau.get().getTenNhaThau());
            dchinhDxKhLcntDsgthauRepository.save(byId.get());
        } else {
            Optional<HhQdKhlcntDsgthau> byId = goiThauRepository.findById(objReq.getIdGoiThau());
            if (!byId.isPresent()) {
                throw new Exception("Gói thầu không tồn tại");
            }
            byId.get().setIdNhaThau(objReq.getId());
            byId.get().setDonGiaNhaThau(objReq.getDonGia());
            byId.get().setTrangThaiDt(objReq.getTrangThai());
            byId.get().setTenNhaThau(nhaThau.get().getTenNhaThau());
            goiThauRepository.save(byId.get());
        }
    }

    @Override
    @Transactional
    public void updateGoiThau(HhDthauReq objReq) throws Exception {
        if(objReq.getLoaiVthh().startsWith("02") && objReq.getType().equals("DC")){
            Optional<HhDchinhDxKhLcntDsgthau> gthau = dchinhDxKhLcntDsgthauRepository.findById(objReq.getIdGoiThau());
            if(!gthau.isPresent()){
                throw new Exception("Gói thầu không tồn tại");
            }
//            gthau.get().setGhiChuTtdt(objReq.getGhiChuTtdt());
//            gthau.get().setTgianTrinhKqTcg(objReq.getTgianTrinhKqTcg());
//            gthau.get().setTgianTrinhTtd(objReq.getTgianTrinhTtd());
            dchinhDxKhLcntDsgthauRepository.save(gthau.get());
            fileDinhKemService.delete(gthau.get().getId(), Lists.newArrayList("HH_DC_DX_LCNT_DSGTHAU" + "_NHA_THAU"));
            if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
                fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), gthau.get().getId(), "HH_DC_DX_LCNT_DSGTHAU" + "_NHA_THAU");
            }
        }else{
            Optional<HhQdKhlcntDsgthau> gthau = goiThauRepository.findById(objReq.getIdGoiThau());
            if (!gthau.isPresent()) {
                throw new Exception("Gói thầu không tồn tại");
            }
            gthau.get().setGhiChuTtdt(objReq.getGhiChuTtdt());
            gthau.get().setTgianTrinhKqTcg(objReq.getTgianTrinhKqTcg());
            gthau.get().setTgianTrinhTtd(objReq.getTgianTrinhTtd());
            goiThauRepository.save(gthau.get());
            fileDinhKemService.delete(gthau.get().getId(), Lists.newArrayList("HH_QD_KHLCNT_DSGTHAU" + "_NHA_THAU"));
            if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
                fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), gthau.get().getId(), "HH_QD_KHLCNT_DSGTHAU" + "_NHA_THAU");
            }
        }
    }

    List<HhDthauNthauDuthau> createVatTu(HhDthauReq objReq) throws Exception {
        if (objReq.getType().equals("DC")) {
            Optional<HhDchinhDxKhLcntDsgthau> byId = dchinhDxKhLcntDsgthauRepository.findById(objReq.getIdGoiThau());
            if(!byId.isPresent()){
                throw new Exception("Gói thầu không tồn tại");
            }
            Optional<HhDchinhDxKhLcntHdr> dchinhDxKhLcntHdr = hhDchinhDxKhLcntHdrRepository.findById(byId.get().getIdDcDxHdr());
            if (!dchinhDxKhLcntHdr.isPresent()) {
                throw new Exception("Quyết định điều chỉnh KH LCNT không tồn tại");
            }
            Optional<HhQdKhlcntHdr> byId1 = hhQdKhlcntHdrRepository.findById(dchinhDxKhLcntHdr.get().getIdQdGoc());
            if(byId1.isPresent()){
                byId1.get().setTrangThaiDt(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
                hhQdKhlcntHdrRepository.save(byId1.get());
            }
        } else {
            Optional<HhQdKhlcntDsgthau> byId = goiThauRepository.findById(objReq.getIdGoiThau());
            if (!byId.isPresent()) {
                throw new Exception("Gói thầu không tồn tại");
            }
            Optional<HhQdKhlcntHdr> byId1 = hhQdKhlcntHdrRepository.findById(byId.get().getIdQdHdr());
            if(byId1.isPresent()){
                byId1.get().setTrangThaiDt(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
                hhQdKhlcntHdrRepository.save(byId1.get());
            }
        }
        nhaThauDuthauRepository.deleteAllByIdDtGt(objReq.getIdGoiThau());
        List<HhDthauNthauDuthau> listDuThau = new ArrayList<>();
        for (HhDthauNthauDuthauReq req : objReq.getNthauDuThauList()) {
            HhDthauNthauDuthau nthauDthau = new HhDthauNthauDuthau();
            BeanUtils.copyProperties(req, nthauDthau, "id");
            nthauDthau.setIdDtGt(objReq.getIdGoiThau());
            nthauDthau.setType(objReq.getType());
            HhDthauNthauDuthau save = nhaThauDuthauRepository.save(nthauDthau);
            listDuThau.add(nthauDthau);
        }
        return listDuThau;
    }

    List<HhDthauNthauDuthau> createLuongThuc(HhDthauReq objReq) throws Exception {
        Optional<HhQdKhlcntDsgthau> byId = goiThauRepository.findById(objReq.getIdGoiThau());
        if(!byId.isPresent()){
            throw new Exception("Gói thầu không tồn tại");
        }

        Optional<HhQdKhlcntDtl> byId1 = dtlRepository.findById(byId.get().getIdQdDtl());
        if(byId1.isPresent()){
            byId1.get().setTrangThai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            dtlRepository.save(byId1.get());
        }

//        HhQdKhlcntDsgthau hhQdKhlcntDsgthau = byId.get();
        nhaThauDuthauRepository.deleteAllByIdDtGt(objReq.getIdGoiThau());
        List<HhDthauNthauDuthau> listDuThau = new ArrayList<>();
//        HhDthauNthauDuthau nhaThauTt = new HhDthauNthauDuthau();
//        boolean isTrungThau = false;
        for (HhDthauNthauDuthauReq req : objReq.getNthauDuThauList()){
            HhDthauNthauDuthau nthauDthau = new HhDthauNthauDuthau();
            BeanUtils.copyProperties(req,nthauDthau,"id");
            nthauDthau.setIdDtGt(objReq.getIdGoiThau());
            HhDthauNthauDuthau save = nhaThauDuthauRepository.save(nthauDthau);
//            if(!isTrungThau){
//                isTrungThau = save.getTrangThai().equals(NhapXuatHangTrangThaiEnum.TRUNGTHAU.getId());
//                if(isTrungThau){
//                    nhaThauTt = save;
//                }
//            }
            listDuThau.add(nthauDthau);
        }
//        if(isTrungThau){
//            hhQdKhlcntDsgthau.setTrangThai(NhapXuatHangTrangThaiEnum.THANH_CONG.getId());
//            hhQdKhlcntDsgthau.setIdNhaThau(nhaThauTt.getId());
//            hhQdKhlcntDsgthau.setTenNhaThau(nhaThauTt.getTenNhaThau());
//            hhQdKhlcntDsgthau.setDonGiaNhaThau(nhaThauTt.getDonGia());
//        }else{
//            hhQdKhlcntDsgthau.setTrangThai(NhapXuatHangTrangThaiEnum.THAT_BAI.getId());
//            hhQdKhlcntDsgthau.setIdNhaThau(null);
//            hhQdKhlcntDsgthau.setTenNhaThau(null);
//            hhQdKhlcntDsgthau.setDonGiaNhaThau(null);
//        }
//        goiThauRepository.save(hhQdKhlcntDsgthau);
        return listDuThau;

    }

    @Override
    public Page<HhQdKhlcntDtl> selectPage(HhQdKhlcntSearchReq objReq) throws Exception {
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdKhlcntDtl> hhQdKhlcntDtls = dtlRepository.selectPage(objReq.getNamKhoach(), objReq.getLoaiVthh(),
                objReq.getMaDvi(), NhapXuatHangTrangThaiEnum.BAN_HANH.getId(),objReq.getTrangThaiDtl(),objReq.getTrangThaiDt(),
                objReq.getSoQd(),
                convertDateToString(objReq.getTuNgayQd()), convertDateToString(objReq.getDenNgayQd()),
                objReq.getSoQdPdKhlcnt(), objReq.getSoQdPdKqlcnt(), pageable);
        Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();

        hhQdKhlcntDtls.getContent().forEach(item ->{
            try {
                // Set Hdr
                HhQdKhlcntHdr hhQdKhlcntHdr = hhQdKhlcntHdrRepository.findById(item.getIdQdHdr()).get();
                hhQdKhlcntHdr.setTenPthucLcnt(hashMapPthucDthau.get(hhQdKhlcntHdr.getPthucLcnt()));
                hhQdKhlcntHdr.setTenCloaiVthh(hashMapDmHh.get(hhQdKhlcntHdr.getCloaiVthh()));
                hhQdKhlcntHdr.setTenLoaiVthh(hashMapDmHh.get(hhQdKhlcntHdr.getLoaiVthh()));
                item.setHhQdKhlcntHdr(hhQdKhlcntHdr);
                item.setNamKhoach(hhQdKhlcntHdr.getNamKhoach().toString());
                List<HhQdKhlcntDsgthau> byIdQdDtl = goiThauRepository.findByIdQdDtl(item.getId());
                long countThanhCong = byIdQdDtl.stream().filter(x -> (
                        x.getTrangThaiDt() != null &&
                        x.getTrangThaiDt().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId())
                )).count();
                long countThatBai = byIdQdDtl.stream().filter(x -> (
                        x.getTrangThaiDt() != null && !x.getTrangThaiDt().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId()))).count();
                item.setSoGthauTrung(countThanhCong);
                item.setSoGthauTruot(countThatBai);
                item.setSoGthau(Long.valueOf(byIdQdDtl.size()));
                if(!StringUtils.isEmpty(item.getSoDxuat())){
                    Optional<HhDxuatKhLcntHdr> bySoDxuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(item.getSoDxuat());
                    if (bySoDxuat.isPresent()) {
                        bySoDxuat.get().setTenPthucLcnt(hashMapPthucDthau.get(bySoDxuat.get().getPthucLcnt()));
                        item.setDxuatKhLcntHdr(bySoDxuat.get());
                    }
                }
                if(!StringUtils.isEmpty(item.getSoQdPdKqLcnt())){
                    Optional<HhQdPduyetKqlcntHdr> bySoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(item.getSoQdPdKqLcnt());
                    bySoQd.ifPresent(item::setHhQdPduyetKqlcntHdr);
                }
                Optional<HhDchinhDxKhLcntHdr> dchinh = hhDchinhDxKhLcntHdrRepository.findByIdQdGoc(item.getIdQdHdr());
                dchinh.ifPresent(hhDchinhDxKhLcntHdr -> item.setSoQdDc(hhDchinhDxKhLcntHdr.getSoQdDc()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        });
        return hhQdKhlcntDtls;
    }

    @Override
    public Page<HhQdKhlcntHdr> selectPageVt(HhQdKhlcntSearchReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setMaDvi(userInfo.getDvql());
        }
        int page = req.getPaggingReq().getPage();
        int limit = req.getPaggingReq().getLimit();
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
//        Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
        Page<HhQdKhlcntHdr> data = hhQdKhlcntHdrRepository.selectPageVt(req.getNamKhoach(), req.getLoaiVthh(), req.getSoQd(), req.getTrichYeu(),
                convertDateToString(req.getTuNgayQd()),
                convertDateToString(req.getDenNgayQd()),
                req.getTrangThai(), req.getLastest(),
                req.getMaDvi(),
                req.getTrangThaiDt(), req.getSoQdPdKhlcnt(), req.getSoQdPdKqlcnt(),
                pageable);
        data.getContent().forEach(f -> {
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
                if (f.getDieuChinh() != null && f.getDieuChinh().equals(Boolean.TRUE)) {
                    Optional<HhDchinhDxKhLcntHdr> dchinhDxKhLcntHdr = hhDchinhDxKhLcntHdrRepository.findByIdQdGocAndLastest(f.getId(), Boolean.TRUE);
                    if (dchinhDxKhLcntHdr.isPresent()) {
                        f.setDchinhDxKhLcntHdr(dchinhDxKhLcntHdr.get());
                        f.setSoQdDc(dchinhDxKhLcntHdr.get().getSoQdDc());
                    }
                }
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });
        return data;
    }

    @Override
    public ChiTietGoiThauRes detail(String ids, String loaiVthh, String type) throws Exception {
        ChiTietGoiThauRes chiTietGoiThauRes = new ChiTietGoiThauRes();
        List<HhDthauNthauDuthau> byIdDtGt = new ArrayList<>();
        if(loaiVthh.startsWith("02")){
            if (type.equals("DC")) {
                Optional<HhDchinhDxKhLcntDsgthau> byId = dchinhDxKhLcntDsgthauRepository.findById(Long.parseLong(ids));
                if(!byId.isPresent()){
                    throw new Exception("Gói thầu không tồn tại");
                }
                List<FileDinhKem> fileDinhKems = fileDinhKemService.search(byId.get().getId(), Collections.singletonList("HH_DC_DX_LCNT_DSGTHAU" + "_NHA_THAU"));
                chiTietGoiThauRes.setFileDinhKems(fileDinhKems);
                byIdDtGt = nhaThauDuthauRepository.findByIdDtGtAndType(Long.parseLong(ids), type);
            } else if (type.equals("GOC")) {
                Optional<HhQdKhlcntDsgthau> byId = goiThauRepository.findById(Long.parseLong(ids));
                if(!byId.isPresent()){
                    throw new Exception("Gói thầu không tồn tại");
                }
                chiTietGoiThauRes.setTgianTrinhKqTcg(byId.get().getTgianTrinhKqTcg());
                chiTietGoiThauRes.setTgianTrinhTtd(byId.get().getTgianTrinhTtd());
                chiTietGoiThauRes.setGhiChuTtdt(byId.get().getGhiChuTtdt());
                List<FileDinhKem> fileDinhKems = fileDinhKemService.search(byId.get().getId(), Collections.singletonList("HH_QD_KHLCNT_DSGTHAU" + "_NHA_THAU"));
                chiTietGoiThauRes.setFileDinhKems(fileDinhKems);
                byIdDtGt = nhaThauDuthauRepository.findByIdDtGtAndType(Long.parseLong(ids), type);
            }
            byIdDtGt.forEach(f -> {
                f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            });
        }else{
            Optional<HhQdKhlcntDsgthau> byId = goiThauRepository.findById(Long.parseLong(ids));
            if(!byId.isPresent()){
                throw new Exception("Gói thầu không tồn tại");
            }
            chiTietGoiThauRes.setTgianTrinhKqTcg(byId.get().getTgianTrinhKqTcg());
            chiTietGoiThauRes.setTgianTrinhTtd(byId.get().getTgianTrinhTtd());
            chiTietGoiThauRes.setGhiChuTtdt(byId.get().getGhiChuTtdt());
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(byId.get().getId(), Collections.singletonList("HH_QD_KHLCNT_DSGTHAU" + "_NHA_THAU"));
            chiTietGoiThauRes.setFileDinhKems(fileDinhKems);
            byIdDtGt = nhaThauDuthauRepository.findByIdDtGtAndType(Long.parseLong(ids), null);
            byIdDtGt.forEach(f -> {
                f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            });
        }
        chiTietGoiThauRes.setDsNhaThauDthau(byIdDtGt);
        return chiTietGoiThauRes;
    }

    @Override
    public List<HhDthauNthauDuthau> getDanhSachNhaThau() throws Exception {
        return nhaThauDuthauRepository.findAllByOrderByTenNhaThau();
    }

    @Override
    public void approve(HhDthauReq stReq) throws Exception {
        if(stReq.getLoaiVthh().startsWith("02")){
            approveVatTu(stReq);
        }else{
            approveLuongThuc(stReq);
        }

    }

    @Override
    public void exportList(HhQdKhlcntSearchReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        String title = "Danh sách các gói thầu";
        String[] rowsName;
        String filename = "Danh_sach_cac_goi_thau.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        if (objReq.getLoaiVthh().startsWith("02")) {
            rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/tờ trình", "Số QĐ PD KHLCNT", "Số QĐ PD KQLCNT", "Ngày QĐ PD KQLCNT", "Tổng số gói thầu", "Số gói thầu đã trúng", "Số gói thầu đã trượt",
                    "Thời gian thực hiện dự án", "Phương thức LCNT", "Chủng loại hàng DTQG", "Trạng thái"};
            Page<HhQdKhlcntHdr> page = selectPageVt(objReq);
            List<HhQdKhlcntHdr> data = page.getContent();
            for (int i = 0; i < data.size(); i++) {
                HhQdKhlcntHdr dx = data.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = dx.getNamKhoach();
                objs[2] = dx.getSoDxuatKhlcnt() != null ? dx.getSoDxuatKhlcnt() : dx.getSoTrHdr();
                objs[3] = dx.getSoQd();
                objs[4] = dx.getSoQdPdKqLcnt();
                objs[5] = objReq.getLoaiVthh().equals("02") ? dx.getNgayPduyet() : convertDate(dx.getNgayPduyet());
                objs[6] = dx.getSoGthau();
                objs[7] = dx.getSoGthauTrung();
                objs[8] = dx.getSoGthauTruot();
                objs[9] = convertDate(dx.getTgianNhang());
                objs[10] = dx.getTenPthucLcnt();
                objs[11] = dx.getTenCloaiVthh();
                objs[12] = objReq.getLoaiVthh().equals("02") ? dx.getTenTrangThaiDt() : dx.getTenTrangThai();
                dataList.add(objs);
            }
        } else {
            rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/tờ trình", "Số QĐ PD KHLCNT", "Số QĐ PD KQLCNT", "Ngày QĐ PD KQLCNT", "Tổng số gói thầu", "Số gói thầu đã trúng", "Số gói thầu đã trượt",
                    "Thời gian thực hiện dự án", "Phương thức LCNT", "Chủng loại hàng DTQG", "Trạng thái"};
            Page<HhQdKhlcntDtl> page = selectPage(objReq);
            List<HhQdKhlcntDtl> data = page.getContent();
            for (int i = 0; i < data.size(); i++) {
                HhQdKhlcntDtl dx = data.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = dx.getNamKhoach();
                objs[2] = dx.getSoDxuat();
                objs[3] = dx.getHhQdKhlcntHdr().getSoQd();
                objs[4] = dx.getSoQdPdKqLcnt();
                objs[5] = dx.getHhQdPduyetKqlcntHdr()!= null? convertDate(dx.getHhQdPduyetKqlcntHdr().getNgayKy()) : "";
                objs[6] = dx.getSoGthau();
                objs[7] = dx.getSoGthauTrung();
                objs[8] = dx.getSoGthauTruot();
                objs[9] = convertDate(dx.getDxuatKhLcntHdr().getTgianNhang());
                objs[10] = dx.getDxuatKhLcntHdr().getTenPthucLcnt();
                objs[11] = dx.getHhQdKhlcntHdr().getTenCloaiVthh();
                objs[12] =  dx.getTenTrangThai();
                dataList.add(objs);
            }
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(HhQdKhlcntHdrReq objReq) throws Exception {
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        HhQdKhlcntPreview object = new HhQdKhlcntPreview();
        if (objReq.getLoaiVthh().startsWith("02")) {
            Optional<HhQdKhlcntHdr> qOptional = hhQdKhlcntHdrRepository.findById(objReq.getId());
            if (!qOptional.isPresent()) {
                throw new UnsupportedOperationException("Không tồn tại bản ghi");
            }
            object.setSoQdPdKhlcnt(qOptional.get().getSoQd());
            object.setNgayPdKhlcnt(convertDate(qOptional.get().getNgayPduyet()));
            if (qOptional.get().getIdTrHdr() != null) {
                Optional<HhDxuatKhLcntHdr> dxuatKhLcntHdr = hhDxuatKhLcntHdrRepository.findById(qOptional.get().getIdTrHdr());
                if (dxuatKhLcntHdr.isPresent()) {
                    Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
                    object.setTenLoaiVthh(StringUtils.isEmpty(dxuatKhLcntHdr.get().getLoaiVthh()) ? null : hashMapDmHh.get(dxuatKhLcntHdr.get().getLoaiVthh()).toUpperCase());
                    object.setNamKhoach(dxuatKhLcntHdr.get().getNamKhoach().toString());
                }
            }
            BigDecimal tongSl = BigDecimal.ZERO;
            List<DsGthauPreview> dsGthau = new ArrayList<>();
            if (qOptional.get().getDieuChinh().equals(Boolean.TRUE)) {
                Optional<HhDchinhDxKhLcntHdr> dchinhDxKhLcntHdr = hhDchinhDxKhLcntHdrRepository.findByIdQdGocAndLastest(qOptional.get().getId(), Boolean.TRUE);
                if (dchinhDxKhLcntHdr.isPresent()) {
                    List<HhDchinhDxKhLcntDsgthau> gThauList = dchinhDxKhLcntDsgthauRepository.findAllByIdDcDxHdr(dchinhDxKhLcntHdr.get().getId());

                    for(HhDchinhDxKhLcntDsgthau gThau : gThauList){
                        DsGthauPreview gthauPreview = new DsGthauPreview();
                        gthauPreview.setGoiThau(gThau.getGoiThau());
                        gthauPreview.setSoLuong(gThau.getSoLuong());
                        gthauPreview.setThanhTien(docxToPdfConverter.convertBigDecimalToStr(gThau.getSoLuong().multiply(gThau.getDonGiaVat())));
                        gthauPreview.setThanhTienNhaThau(docxToPdfConverter.convertBigDecimalToStr(gThau.getThanhTienNhaThau()));
                        tongSl = tongSl.add(gThau.getSoLuong());
                        gthauPreview.setDvt(gThau.getDviTinh());
                        HhQdPduyetKqlcntDtl kq = hhQdPduyetKqlcntDtlRepository.findByIdGoiThauAndType(gThau.getId(), "DC");
                        if (kq != null) {
                            gthauPreview.setNhaThauTrungThau(kq.getTenNhaThau());
                        }
                        List<HhDthauNthauDuthau> byIdDtGt = nhaThauDuthauRepository.findByIdDtGtAndType(gThau.getId(), "DC");
                        gthauPreview.setDsNhaThau(byIdDtGt);
                        dsGthau.add(gthauPreview);
                    }
                }
            } else {
                List<HhQdKhlcntDsgthau> hhQdKhlcntDsgthauData = goiThauRepository.findByIdQdHdr(qOptional.get().getId());
                for(HhQdKhlcntDsgthau gThau : hhQdKhlcntDsgthauData){
                    DsGthauPreview gthauPreview = new DsGthauPreview();
                    gthauPreview.setGoiThau(gThau.getGoiThau());
                    gthauPreview.setSoLuong(gThau.getSoLuong());
                    gthauPreview.setThanhTien(docxToPdfConverter.convertBigDecimalToStr(gThau.getSoLuong().multiply(gThau.getDonGiaVat())));
                    gthauPreview.setThanhTienNhaThau(docxToPdfConverter.convertBigDecimalToStr(gThau.getThanhTienNhaThau()));
                    gthauPreview.setDvt(gThau.getDviTinh());
                    tongSl = tongSl.add(gThau.getSoLuong());
                    HhQdPduyetKqlcntDtl kq = hhQdPduyetKqlcntDtlRepository.findByIdGoiThauAndType(gThau.getId(), "GOC");
                    if (kq != null) {
                        gthauPreview.setNhaThauTrungThau(kq.getTenNhaThau());
                    }
                    List<HhDthauNthauDuthau> byIdDtGt = nhaThauDuthauRepository.findByIdDtGtAndType(gThau.getId(), "GOC");
                    byIdDtGt.forEach(item -> {
                        item.setGiaDuThau(docxToPdfConverter.convertBigDecimalToStr(item.getDonGia()));
                    });
                    gthauPreview.setDsNhaThau(byIdDtGt);
                    dsGthau.add(gthauPreview);
                }
            }
            object.setDsGthauKq(dsGthau);
            object.setTongSl(tongSl.toString());

        } else {
            Optional<HhQdKhlcntDtl> byId = dtlRepository.findById(objReq.getId());
            if(!byId.isPresent()){
                throw new Exception("Không tìm thấy dữ liệu");
            }
            Optional<HhQdKhlcntHdr> hdr = hhQdKhlcntHdrRepository.findById(byId.get().getIdQdHdr());
            if (!hdr.isPresent()) {
                throw new UnsupportedOperationException("Không tồn tại bản ghi");
            }
            Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
            Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
            object.setTenCloaiVthh(hashMapDmHh.get(hdr.get().getCloaiVthh()).toUpperCase());
            object.setNamKhoach(hdr.get().getNamKhoach().toString());
            object.setTenDvi(mapDmucDvi.get(byId.get().getMaDvi()).toUpperCase());
            List<HhQdKhlcntDsgthau> byIdQdDtl = goiThauRepository.findByIdQdDtl(byId.get().getId());
            BigDecimal soLuong = BigDecimal.ZERO;
            for(HhQdKhlcntDsgthau dsg : byIdQdDtl){
                dsg.setDsNhaThauDthau(nhaThauDuthauRepository.findByIdDtGtAndType(dsg.getId(), null));
                soLuong = soLuong.add(dsg.getSoLuong());
            }
            object.setDsGthau(byIdQdDtl);
            object.setTongSl(docxToPdfConverter.convertBigDecimalToStr(soLuong));
        }
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }

    void approveLuongThuc(HhDthauReq stReq) throws Exception {
        Optional<HhQdKhlcntDtl> optional = dtlRepository.findById(stReq.getId());
        if(!optional.isPresent()){
            throw new Exception("Thông tin đấu thầu không tồn tại");
        }
//        List<HhQdKhlcntDsgthau> byIdQdDtl = goiThauRepository.findByIdQdDtl(stReq.getId());
//
//        List<HhQdKhlcntDsgthau> collect = byIdQdDtl.stream().filter(item -> item.getTrangThai().equals(NhapXuatHangTrangThaiEnum.CHUACAPNHAT.getId())).collect(Collectors.toList());
//        if(!collect.isEmpty()){
//            throw new Exception("Vui lòng cập nhật thông tin các gói thầu");
//        }

        String status = stReq.getTrangThai() + optional.get().getTrangThai();
        if ((NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId()).equals(status)) {
            optional.get().setTrangThai(stReq.getTrangThai());
        }else{
            throw new Exception("Cập nhật không thành công");
        }
        dtlRepository.save(optional.get());
    }

    void approveVatTu(HhDthauReq stReq) throws Exception {
        Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findById(stReq.getId());
        if(!optional.isPresent()){
            throw new Exception("Thông tin đấu thầu không tồn tại");
        }
        String status = stReq.getTrangThai() + optional.get().getTrangThaiDt();
        if ((NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId()).equals(status)) {
            optional.get().setTrangThaiDt(stReq.getTrangThai());
        }else{
            throw new Exception("Cập nhật không thành công");
        }
        hhQdKhlcntHdrRepository.save(optional.get());
    }

    @Override
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId())) {
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        Optional<HhDthauNthauDuthau> nthauDuthau = nhaThauDuthauRepository.findById(idSearchReq.getId());
        if (!nthauDuthau.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        nhaThauDuthauRepository.delete(nthauDuthau.get());
    }
}
