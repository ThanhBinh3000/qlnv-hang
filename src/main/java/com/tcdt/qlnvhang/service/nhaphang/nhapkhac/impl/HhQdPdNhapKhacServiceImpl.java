package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.FileDKemJoinKquaLcntHdr;
import com.tcdt.qlnvhang.entities.FileDKemJoinPaKhlcntHdr;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdKhlcntHdr;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdPdNkHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.*;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhThopKhNhapKhac;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhQdPdNhapKhacDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhQdPdNhapKhacHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhThopKhNhapKhacRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.*;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhQdPdNhapKhacService;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Service
public class HhQdPdNhapKhacServiceImpl extends BaseServiceImpl implements HhQdPdNhapKhacService {
    private static final String CAN_CU = "_CAN_CU";
    @Autowired
    private HhQdPdNhapKhacHdrRepository hhQdPdNhapKhacHdrRepository;
    @Autowired
    private HhQdPdNhapKhacDtlRepository hhQdPdNhapKhacDtlRepository;
    @Autowired
    private HhDxuatKhNhapKhacHdrRepository hhDxuatKhNhapKhacHdrRepository;
    @Autowired
    FileDinhKemService fileDinhKemService;
    @Autowired
    private HhThopKhNhapKhacRepository hhThopKhNhapKhacRepository;

    @Override
    public Page<HhQdPdNhapKhacHdr> timKiem(HhQdPdNhapKhacSearch req) {
        req.setTuNgayQdPdStr(convertFullDateToString(req.getTuNgayQd()));
        req.setDenNgayQdPdStr(convertFullDateToString(req.getDenNgayQd()));
        req.setTuNgayDuyetStr(convertFullDateToString(req.getTuNgayDuyet()));
        req.setDenNgayDuyetStr(convertFullDateToString(req.getDenNgayDuyet()));
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhQdPdNhapKhacHdr> data = hhQdPdNhapKhacHdrRepository.search(req, pageable);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
//            f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
//            f.setTenDvi(mapDmucDvi.get(f.getMaDviDxuat()));
        });
        return data;
    }

    @Override
    @Transactional
    public HhQdPdNhapKhacHdr themMoi (HhQdPdNhapKhacHdrReq req) throws Exception {


        if(!StringUtils.isEmpty(req.getSoQd())){
            List<HhQdPdNhapKhacHdr> checkSoQd = hhQdPdNhapKhacHdrRepository.findBySoQd(req.getSoQd());
            if (!checkSoQd.isEmpty()) {
                throw new Exception("Số quyết định " + req.getSoQd() + " đã tồn tại");
            }
        }

        if(req.getPhanLoai().equals("TH")){
            Optional<HhThopKhNhapKhac> qOptionalTh = hhThopKhNhapKhacRepository.findById(req.getIdTh());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch lựa chọn nhà thầu");
            }
        }else{
            Optional<HhDxuatKhNhapKhacHdr> byId = hhDxuatKhNhapKhacHdrRepository.findById(req.getDetails().get(0).getId());
            if(!byId.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch lựa chọn nhà thầu");
            }
        }
        // Add danh sach file dinh kem o Master
        List<FileDKemJoinQdPdNkHdr> fileDinhKemList = new ArrayList<FileDKemJoinQdPdNkHdr>();
        if (req.getFileDinhKems() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(req.getFileDinhKems(), FileDKemJoinQdPdNkHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhQdPdNhapKhacHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }

        HhQdPdNhapKhacHdr dataMap = ObjectMapperUtils.map(req, HhQdPdNhapKhacHdr.class);
        if(req.getPhanLoai().equals("TH")){
            dataMap.setIdTh(req.getIdTh());
        }else{
            dataMap.getDetails().forEach(item ->{
                dataMap.setIdDx(item.getId());
            });
        }


        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setFileDinhKems(fileDinhKemList);
        dataMap.setLastest(req.getLastest());
//        dataMap.setMaDvi(getUser().getDvql());
        hhQdPdNhapKhacHdrRepository.save(dataMap);

        // Update trạng thái tổng hợp dxkhclnt
        if(req.getPhanLoai().equals("TH")){
//            hhThopKhNhapKhacRepository.updateTrangThai(dataMap.getIdTh(), Contains.DADUTHAO_QD);
            Optional<HhThopKhNhapKhac> hhThopKhNhapKhac = hhThopKhNhapKhacRepository.findById(dataMap.getIdTh());
            hhThopKhNhapKhac.get().setSoQd(dataMap.getSoQd());
            hhThopKhNhapKhac.get().setTrangThai(Contains.DADUTHAO_QD);
            hhThopKhNhapKhacRepository.save(hhThopKhNhapKhac.get());
        }else{
            hhDxuatKhNhapKhacHdrRepository.updateStatusInList(Arrays.asList(req.getDetails().get(0).getSoDxuat()), Contains.DADUTHAO_QD);
        }

        saveDetail(req,dataMap);
        return dataMap;
    }

    @Transactional
    void saveDetail(HhQdPdNhapKhacHdrReq objReq, HhQdPdNhapKhacHdr dataMap){
        try {
            hhQdPdNhapKhacDtlRepository.deleteAllByIdHdr(dataMap.getId());
            for (HhDxuatKhNhapKhacHdrReq dxHdr : objReq.getDetails()){
                dxHdr.getDetails().forEach(dtl ->{
                    HhQdPdNhapKhacDtl qd = ObjectMapperUtils.map(dtl, HhQdPdNhapKhacDtl.class);
                    qd.setId(null);
                    qd.setIdHdr(dataMap.getId());
                    qd.setIdDxHdr(dxHdr.getId());
                    qd.setTrangThai(Contains.CHUACAPNHAT);
                    qd.setTongSlNhap(dxHdr.getTongSlNhap());
                    qd.setTongThanhTien(dxHdr.getTongThanhTien());
                    hhQdPdNhapKhacDtlRepository.save(qd);
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public HhQdPdNhapKhacHdr capNhat (HhQdPdNhapKhacHdrReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<HhQdPdNhapKhacHdr> qOptional = hhQdPdNhapKhacHdrRepository.findById(objReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if(!StringUtils.isEmpty(objReq.getSoQd())){
            if (!objReq.getSoQd().equals(qOptional.get().getSoQd())) {
                List<HhQdPdNhapKhacHdr> checkSoQd = hhQdPdNhapKhacHdrRepository.findBySoQd(objReq.getSoQd());
                if (!checkSoQd.isEmpty()) {
                    throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
                }
            }
        }

        if(objReq.getIdTh() != null){
            Optional<HhThopKhNhapKhac> qOptionalTh = hhThopKhNhapKhacRepository.findById(objReq.getIdTh());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch lựa chọn nhà thầu");
            }
        }else{
            Optional<HhDxuatKhNhapKhacHdr> byId = hhDxuatKhNhapKhacHdrRepository.findById(objReq.getIdDx());
            if(!byId.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch lựa chọn nhà thầu");
            }
        }

        // Add danh sach file dinh kem o Master
        List<FileDKemJoinQdPdNkHdr> fileDinhKemList = new ArrayList<FileDKemJoinQdPdNkHdr>();
        if (objReq.getFileDinhKems() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinQdPdNkHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhQdPdNhapKhacHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }

        HhQdPdNhapKhacHdr dataDB = qOptional.get();
        HhQdPdNhapKhacHdr dataMap = ObjectMapperUtils.map(objReq, HhQdPdNhapKhacHdr.class);

        updateObjectToObject(dataDB, dataMap);

        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSua(getUser().getUsername());
        dataDB.setFileDinhKems(fileDinhKemList);

        hhQdPdNhapKhacHdrRepository.save(dataDB);
        if(dataDB.getIdTh() != null){
            Optional<HhThopKhNhapKhac> hhThopKhNhapKhac = hhThopKhNhapKhacRepository.findById(dataDB.getIdTh());
            hhThopKhNhapKhac.get().setSoQd(dataMap.getSoQd());
            hhThopKhNhapKhacRepository.save(hhThopKhNhapKhac.get());
        }
        this.saveDetail(objReq,dataDB);

        return dataDB;
    }

    @Override
    public HhQdPdNhapKhacHdr chiTiet (Long id) throws Exception {
        Optional<HhQdPdNhapKhacHdr> qOptional = hhQdPdNhapKhacHdrRepository.findById(id);
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setTenLoaiHinhNx(mapLoaiHinhNx.get(qOptional.get().getLoaiHinhNx()));
        qOptional.get().setTenKieuNx(mapKieuNx.get(qOptional.get().getKieuNx()));
        qOptional.get().setTenLoaiVthh(mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
//        qOptional.get().setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThaiTh()));
//        qOptional.get().setCanCuPhapLy(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhQdPdNhapKhacHdr.TABLE_NAME + CAN_CU)));
//        qOptional.get().setFileDinhKems(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhQdPdNhapKhacHdr.TABLE_NAME)));
        List<HhQdPdNhapKhacDtl> dtls = new ArrayList<>();
        List<HhDxuatKhNhapKhacDtl> dxDtls = new ArrayList<>();
        if(qOptional.get().getIdTh() != null){
            qOptional.get().setPhanLoai("TH");
            qOptional.get().setChildren(hhDxuatKhNhapKhacHdrRepository.findAllByThopId(qOptional.get().getIdTh()));
            for (HhDxuatKhNhapKhacHdr child : qOptional.get().getChildren()) {
                dxDtls = new ArrayList<>();
                child.setTenDvi(mapDmucDvi.get(child.getMaDviDxuat()));
                dtls = hhQdPdNhapKhacDtlRepository.findAllByIdDxHdr(child.getId());
                for (HhQdPdNhapKhacDtl dtl : dtls) {
                    HhDxuatKhNhapKhacDtl dxDtl = new HhDxuatKhNhapKhacDtl();
                    BeanUtils.copyProperties(dtl, dxDtl);
                    dxDtl.setTenCloaiVthh(mapVthh.get(dxDtl.getCloaiVthh()));
                    dxDtls.add(dxDtl);
                }
                    child.setDetails(dxDtls);
//                child.getDetails().addAll(dxDtls);
            }
        }else{
            qOptional.get().setPhanLoai("TTr");
            dtls = hhQdPdNhapKhacDtlRepository.findAllByIdHdr(qOptional.get().getId());
        }
        BigDecimal tongSlNhap = BigDecimal.ZERO;
        BigDecimal tongThanhTien = BigDecimal.ZERO;
        for (HhQdPdNhapKhacDtl dtl : dtls) {
            dtl.setTenCuc(mapDmucDvi.get(dtl.getMaCuc()));
            dtl.setTenChiCuc(mapDmucDvi.get(dtl.getMaChiCuc()));
            dtl.setTenDiemKho(mapDmucDvi.get(dtl.getMaDiemKho()));
            dtl.setTenNhaKho(mapDmucDvi.get(dtl.getMaNhaKho()));
            dtl.setTenNganLoKho(mapDmucDvi.get(dtl.getMaLoKho()) + " - " + mapDmucDvi.get(dtl.getMaNganKho()));
            dtl.setTenCloaiVthh(mapVthh.get(dtl.getCloaiVthh()));
            if (dtl.getSlDoiThua() != null) {
                tongSlNhap = tongSlNhap.add(dtl.getSlDoiThua());
                tongThanhTien = tongThanhTien.add(dtl.getSlDoiThua().multiply(dtl.getDonGia()));
            } else if(dtl.getSlTonKhoThucTe() != null) {
                tongSlNhap = tongSlNhap.add(dtl.getSlTonKhoThucTe().subtract(dtl.getSlTonKho()) );
                tongThanhTien = tongThanhTien.add((dtl.getSlTonKhoThucTe().subtract(dtl.getSlTonKho())).multiply(dtl.getDonGia()));
            } else if (dtl.getSlNhap() != null) {
                tongSlNhap = tongSlNhap.add(dtl.getSlNhap());
                tongThanhTien = tongThanhTien.add(dtl.getSlNhap().multiply(dtl.getDonGia()));
            }
        }
        qOptional.get().setTongSlNhap(tongSlNhap);
        qOptional.get().setTongThanhTien(tongThanhTien);
        qOptional.get().setDetails(dtls);
        return qOptional.get();
    }

    @Override
    public HhQdPdNhapKhacHdr pheDuyet(StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhQdPdNhapKhacHdr> qOptional = hhQdPdNhapKhacHdrRepository.findById(stReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        String status = stReq.getTrangThai() + qOptional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDV + Contains.DUTHAO:
                qOptional.get().setNguoiGuiDuyet(getUser().getUsername());
                qOptional.get().setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
                qOptional.get().setNguoiPduyet(getUser().getUsername());
                qOptional.get().setNgayPduyet(getDateTimeNow());
                qOptional.get().setLyDoTuChoi(stReq.getLyDo());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDV:
                qOptional.get().setNguoiPduyet(getUser().getUsername());
                qOptional.get().setNgayPduyet(getDateTimeNow());
                if(qOptional.get().getIdTh() != null){
                    hhThopKhNhapKhacRepository.updateTrangThai(qOptional.get().getIdTh(), Contains.DABANHANH_QD);
                }else{
                    hhDxuatKhNhapKhacHdrRepository.updateStatusInList(Arrays.asList(qOptional.get().getSoDxuat()), Contains.DABANHANH_QD);
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        qOptional.get().setTrangThai(stReq.getTrangThai());
        return hhQdPdNhapKhacHdrRepository.save(qOptional.get());
    }

    @Override
    @Transactional
    public void xoa(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhQdPdNhapKhacHdr> qOptional = hhQdPdNhapKhacHdrRepository.findById(idSearchReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if (!qOptional.get().getTrangThai().equals(Contains.DUTHAO)
                && !qOptional.get().getTrangThai().equals(Contains.TU_CHOI_CBV)
                && !qOptional.get().getTrangThai().equals(Contains.TUCHOI_TP)
                && !qOptional.get().getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
        }
        if(qOptional.get().getIdTh() != null){
            hhThopKhNhapKhacRepository.updateTrangThai(qOptional.get().getIdTh(), Contains.CHUATAO_QD);
        }else{
            hhDxuatKhNhapKhacHdrRepository.updateStatusInList(Arrays.asList(qOptional.get().getSoDxuat()), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
        }
        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhQdPdNhapKhacHdr.TABLE_NAME + CAN_CU));
        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhQdPdNhapKhacHdr.TABLE_NAME));
        hhQdPdNhapKhacDtlRepository.deleteAllByIdHdr(qOptional.get().getId());
        hhQdPdNhapKhacHdrRepository.delete(qOptional.get());
    }

    @Override
    @Transactional
    public void xoaNhieu(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList())) {
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        List<HhQdPdNhapKhacHdr> listHdr = hhQdPdNhapKhacHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (listHdr.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        for (HhQdPdNhapKhacHdr hdr: listHdr) {
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                    && !hdr.getTrangThai().equals(Contains.TU_CHOI_CBV)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDC)) {
                throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
            }
            if(hdr.getIdTh() != null){
                hhThopKhNhapKhacRepository.updateTrangThai(hdr.getIdTh(), Contains.CHUATONGHOP);
            }else{
                hhDxuatKhNhapKhacHdrRepository.updateStatusInList(Arrays.asList(hdr.getSoDxuat()), NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
            }
            fileDinhKemService.delete(hdr.getId(), Lists.newArrayList(HhQdPdNhapKhacHdr.TABLE_NAME + CAN_CU));
            fileDinhKemService.delete(hdr.getId(), Lists.newArrayList(HhQdPdNhapKhacHdr.TABLE_NAME));
            hhQdPdNhapKhacDtlRepository.deleteAllByIdHdr(hdr.getId());
            hhQdPdNhapKhacHdrRepository.delete(hdr);
        }
    }

    @Override
    public void xuatFile(HhQdPdNhapKhacSearch req , HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<HhQdPdNhapKhacHdr> page = this.timKiem(req);
        List<HhQdPdNhapKhacHdr> data = page.getContent();

        String title = "Danh sách quyết định phê duyệt kế hoạch nhập khác";
        String[] rowsName = new String[]{"STT", "Năm KH", "Số quyết định", "Ngày quyết định", "Mã tổng hợp", "Số công văn/đề xuất", "Loại hàng hóa", "Tổng SL đề xuất", "ĐVT", "Nội dung tổng hợp", "Trạng thái"};
        String filename = "Danh_sach_quyet_dinh_phe_duyet_ke_hoach_nhap_khac.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhQdPdNhapKhacHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNamKhoach();
            objs[2] = dx.getSoQd();
            objs[3] = dx.getNgayKyQd();
            objs[4] = dx.getIdTh();
            objs[5] = dx.getSoDxuat();
            objs[6] = dx.getTenLoaiVthh();
            objs[7] = dx.getTongSlNhap();
            objs[8] = dx.getDvt();
            objs[9] = dx.getNoiDung();
            objs[10] = dx.getTenTrangThai();
            dataList.add(objs);
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

}
