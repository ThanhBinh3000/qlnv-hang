package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.*;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhQdPheduyetKhMttPreview;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhChiTietTTinChaoGiaReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPdKhMttSlddDtlReq;
import com.tcdt.qlnvhang.request.object.HhSlNhapHangReq;
import com.tcdt.qlnvhang.service.HhSlNhapHangService;
import com.tcdt.qlnvhang.service.UserService;
import com.tcdt.qlnvhang.service.feign.KeHoachService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopDtl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class HhQdPheduyetKhMttHdrService extends BaseServiceImpl {

    @Autowired
    private HhQdPheduyetKhMttHdrRepository hhQdPheduyetKhMttHdrRepository;

    @Autowired
    private HhSlNhapHangService hhSlNhapHangService;

    @Autowired
    private HhQdPheduyetKhMttDxRepository hhQdPheduyetKhMttDxRepository;

    @Autowired
    private HhQdPheduyetKhMttSLDDRepository hhQdPheduyetKhMttSLDDRepository;

    @Autowired
    private HhQdPdKhMttSlddDtlRepository hhQdPdKhMttSlddDtlRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    private HhDxuatKhMttThopRepository hhDxuatKhMttThopRepository;

    @Autowired
    private HhDxuatKhMttRepository hhDxuatKhMttRepository;

    @Autowired
    private HhCtietTtinCgiaRepository hhCtietTtinCgiaRepository;

    @Autowired
    private HhDcQdPduyetKhMttRepository hhDcQdPduyetKhMttRepository;

    @Autowired
    private HhDcQdPduyetKhMttDxRepository hhDcQdPduyetKhMttDxRepository;

    @Autowired
    private HhDcQdPduyetKhmttSlddRepository hhDcQdPduyetKhmttSlddRepository;
    @Autowired
    private HhDcQdPdKhmttSlddDtlRepository hhDcQdPdKhmttSlddDtlRepository;
    @Autowired
    private HhTtChaoGiaKhmttSlddDtlRepository hhTtChaoGiaKhmttSlddDtlRepository;

    @Autowired
    private HhQdPduyetKqcgRepository hhQdPduyetKqcgRepository;


    @Autowired
    private KeHoachService keHoachService;


    public Page<HhQdPheduyetKhMttHdr> searchPage(HhQdPheduyetKhMttHdrSearchReq req)throws Exception{

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdPheduyetKhMttHdr> data = hhQdPheduyetKhMttHdrRepository.searchPage(
                req,
                pageable);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        data.getContent().forEach(f->{
            f.setTenPtMuaTrucTiep(Contains.getPthucMtt(f.getPtMuaTrucTiep()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
        });
        return data;
    }

    public List<HhQdPheduyetKhMttHdr> searchDsTaoQdDc(HhQdPheduyetKhMttHdrSearchReq req) throws Exception {
        List<HhQdPheduyetKhMttHdr> data = hhQdPheduyetKhMttHdrRepository.searchDsTaoQdDc(
                req);
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        data.forEach(f -> {
            f.setTenPtMuaTrucTiep(Contains.getPthucMtt(f.getPtMuaTrucTiep()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
        });
        return data;
    }
    @Transactional
    public HhQdPheduyetKhMttHdr create (HhQdPheduyetKhMttHdrReq req) throws Exception{

        if(!StringUtils.isEmpty(req.getSoQd())){
            List<HhQdPheduyetKhMttHdr> checkSoQd = hhQdPheduyetKhMttHdrRepository.findBySoQd(req.getSoQd());
            if (!checkSoQd.isEmpty()) {
                throw new Exception("Số quyết định " + req.getSoQd() + " đã tồn tại");
            }
        }

        HhDxKhMttThopHdr dataTh = new HhDxKhMttThopHdr();
        HhDxuatKhMttHdr dataDx = new HhDxuatKhMttHdr();

        if(req.getPhanLoai().equals("TH")){
            Optional<HhDxKhMttThopHdr> qOptionalTh = hhDxuatKhMttThopRepository.findById(req.getIdThHdr());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch mua trực tiếp");
            }
            dataTh = qOptionalTh.get();
        }else{
            Optional<HhDxuatKhMttHdr> qOptionalDx = hhDxuatKhMttRepository.findById(req.getIdTrHdr());
            if (!qOptionalDx.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch mua trực tiếp");
            }
            dataDx = qOptionalDx.get();
        }

        HhQdPheduyetKhMttHdr dataMap = new HhQdPheduyetKhMttHdr();
        BeanUtils.copyProperties(req, dataMap, "id");
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNguoiTaoId(getUser().getId());
        dataMap.setTrangThai(Contains.DANG_NHAP_DU_LIEU);
//        dataMap.setLastest(false);
        dataMap.setMaDvi(getUser().getDvql());
        HhQdPheduyetKhMttHdr created=hhQdPheduyetKhMttHdrRepository.save(dataMap);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HhQdPheduyetKhMttHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        if (req.getPhanLoai().equals("TH")){
            dataTh.setTrangThai(Contains.DADUTHAO_QD);
            dataTh.setSoQdPduyet(dataMap.getSoQd());
            dataTh.setIdQdPduyet(created.getId());
            hhDxuatKhMttThopRepository.save(dataTh);
        }else{
            dataDx.setSoQdPduyet(dataMap.getSoQd());
            dataDx.setIdSoQdPduyet(dataMap.getId());
            hhDxuatKhMttRepository.save(dataDx);
        }

        saveDetail(req, dataMap.getId());
        return created;
    }


  @Transactional
  void saveDetail(HhQdPheduyetKhMttHdrReq req, Long idQdHdr){
        hhQdPheduyetKhMttDxRepository.deleteAllByIdQdHdr(idQdHdr);
        for (HhQdPheduyetKhMttDxReq dxReq : req.getChildren()){
            HhQdPheduyetKhMttDx dx = new HhQdPheduyetKhMttDx();
            BeanUtils.copyProperties(dxReq, dx, "id");
            dx.setIdQdHdr(idQdHdr);
            dx.setTrangThai(Contains.CHUACAPNHAT);
            dx.setNgayTao(new Date());
            hhQdPheduyetKhMttDxRepository.save(dx);
            hhQdPheduyetKhMttSLDDRepository.deleteByIdQdDtl(dxReq.getId());
            for (HhQdPheduyetKhMttSLDDReq slddReq : dxReq.getChildren()) {
                HhQdPheduyetKhMttSLDD sldd = new HhQdPheduyetKhMttSLDD();
                BeanUtils.copyProperties(slddReq, sldd, "id");
                sldd.setIdQdDtl(dx.getId());
                hhQdPheduyetKhMttSLDDRepository.save(sldd);
                for (HhChiTietTTinChaoGiaReq tTinChaoGiaReq: slddReq.getListChaoGia()){
                    HhChiTietTTinChaoGia tTinChaoGia = new HhChiTietTTinChaoGia();
                    BeanUtils.copyProperties(tTinChaoGiaReq, tTinChaoGia, "id");
                    tTinChaoGia.setIdQdPdSldd(slddReq.getId());
                    hhCtietTtinCgiaRepository.save(tTinChaoGia);
                }
                hhQdPdKhMttSlddDtlRepository.deleteByIdDiaDiem(slddReq.getId());
                for (HhQdPdKhMttSlddDtlReq slddDtlReq : slddReq.getChildren()){
                    HhQdPdKhMttSlddDtl slddDtl = new HhQdPdKhMttSlddDtl();
                    BeanUtils.copyProperties(slddDtlReq, slddDtl, "id");
                    slddDtl.setIdDiaDiem(sldd.getId());
                    hhQdPdKhMttSlddDtlRepository.save(slddDtl);
                }
            }
        }
  }

    @Transactional
    public HhQdPheduyetKhMttHdr update(HhQdPheduyetKhMttHdrReq req) throws Exception {
        HhDxKhMttThopHdr dataTh = new HhDxKhMttThopHdr();
        HhDxuatKhMttHdr dataDx = new HhDxuatKhMttHdr();
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<HhQdPheduyetKhMttHdr> qOptional = hhQdPheduyetKhMttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if(!StringUtils.isEmpty(req.getSoQd())){
            if (!req.getSoQd().equals(qOptional.get().getSoQd())) {
                List<HhQdPheduyetKhMttHdr> checkSoQd = hhQdPheduyetKhMttHdrRepository.findBySoQd(req.getSoQd());
                if (!checkSoQd.isEmpty()) {
                    throw new Exception("Số quyết định " + req.getSoQd() + " đã tồn tại");
                }
            }
        }
        if(req.getPhanLoai().equals("TH")){
            Optional<HhDxKhMttThopHdr> qOptionalTh = hhDxuatKhMttThopRepository.findById(req.getIdThHdr());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch mua trực tiếp");
            }
            dataTh = qOptionalTh.get();
        }else{
            Optional<HhDxuatKhMttHdr> byId = hhDxuatKhMttRepository.findById(req.getIdTrHdr());
            if(!byId.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch mua trực tiếp");
            }
            dataDx = byId.get();
        }

        HhQdPheduyetKhMttHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSuaId(getUser().getId());
//        dataDB.setMaDvi(dataDx.getMaDvi());
        dataDB.setMaDvi(getUser().getDvql());
        HhQdPheduyetKhMttHdr  createCheck = hhQdPheduyetKhMttHdrRepository.save(dataDB);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), createCheck.getId(), HhQdPheduyetKhMttHdr.TABLE_NAME);
            dataDB.setFileDinhKems(fileDinhKems);
        }

        this.saveDetail(req, dataDB.getId());
        return createCheck;
    }


    public HhQdPheduyetKhMttHdr detail(Long ids) throws Exception{
        if (StringUtils.isEmpty(ids))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Optional<HhQdPheduyetKhMttHdr> qOptional = hhQdPheduyetKhMttHdrRepository.findById(ids);


        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Bản ghi không tồn tại");
        }
        Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        HhQdPheduyetKhMttHdr data = qOptional.get();
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(HhQdPheduyetKhMttHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKem);

        List<HhQdPheduyetKhMttDx> dxList = new ArrayList<>();
        for (HhQdPheduyetKhMttDx dx : hhQdPheduyetKhMttDxRepository.findAllByIdQdHdr(ids)){
            List<HhQdPheduyetKhMttSLDD> slddList = new ArrayList<>();
            for (HhQdPheduyetKhMttSLDD sldd : hhQdPheduyetKhMttSLDDRepository.findAllByIdQdDtl(dx.getId())){
                List<HhQdPdKhMttSlddDtl> slddDtlList = hhQdPdKhMttSlddDtlRepository.findAllByIdDiaDiem(sldd.getId());
                slddDtlList.forEach(f ->{
                    f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
                });
                sldd.setTenDvi(hashMapDvi.get(sldd.getMaDvi()));
                sldd.setChildren(slddDtlList);
                slddList.add(sldd);
            }
            dx.setTenNguonVon(hashMapNguonVon.get(dx.getNguonVon()));
            dx.setTenDvi(StringUtils.isEmpty(dx.getMaDvi()) ? null : hashMapDvi.get(dx.getMaDvi()));
            dx.setTenLoaiVthh(hashMapVthh.get(dx.getLoaiVthh()));
            dx.setTenCloaiVthh(hashMapVthh.get(dx.getCloaiVthh()));
            dx.setChildren(slddList);
            dxList.add(dx);
        }
        data.setSoLanDieuChinh(Long.valueOf(hhDcQdPduyetKhMttRepository.findAllByIdQdGocOrderByIdDesc(data.getId()).size()));
        data.setChildren(dxList);
        return data;
    }

    public HhQdPheduyetKhMttHdr detailBySoQd(String soQd) throws Exception{
        if (StringUtils.isEmpty(soQd))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Optional<HhQdPheduyetKhMttHdr> qOptional = hhQdPheduyetKhMttHdrRepository.findBySoQdAndLastest(soQd, false);


        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Bản ghi không tồn tại");
        }

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        HhQdPheduyetKhMttHdr data = qOptional.get();
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(HhQdPheduyetKhMttHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKem);

        List<HhQdPheduyetKhMttDx> dxList = new ArrayList<>();
        for (HhQdPheduyetKhMttDx dx : hhQdPheduyetKhMttDxRepository.findAllByIdQdHdr(data.getId())){
            List<HhQdPheduyetKhMttSLDD> slddList = new ArrayList<>();
            for (HhQdPheduyetKhMttSLDD sldd : hhQdPheduyetKhMttSLDDRepository.findAllByIdQdDtl(dx.getId())){
                List<HhQdPdKhMttSlddDtl> slddDtlList = hhQdPdKhMttSlddDtlRepository.findAllByIdDiaDiem(sldd.getId());
                slddDtlList.forEach(f ->{
                    f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
                });
                sldd.setTenDvi(hashMapDvi.get(sldd.getMaDvi()));
                sldd.setChildren(slddDtlList);
                slddList.add(sldd);
            }
            dx.setTenDvi(StringUtils.isEmpty(dx.getMaDvi()) ? null : hashMapDvi.get(dx.getMaDvi()));
            dx.setChildren(slddList);
            dxList.add(dx);
        }

        data.setChildren(dxList);
        return data;
    }

    @Transactional()
    public void delete(Long id) throws Exception{
        if (StringUtils.isEmpty(id))
            throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");

        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xóa");

        if (!optional.get().getTrangThai().equals(Contains.DANG_NHAP_DU_LIEU)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }

        List<HhQdPheduyetKhMttDx> hhQdPheduyetKhMttDx = hhQdPheduyetKhMttDxRepository.findAllByIdQdHdr(optional.get().getId());
        if (!CollectionUtils.isEmpty(hhQdPheduyetKhMttDx)){
            for (HhQdPheduyetKhMttDx dtl: hhQdPheduyetKhMttDx){
                List<HhQdPheduyetKhMttSLDD> byIdQdDtl = hhQdPheduyetKhMttSLDDRepository.findAllByIdQdDtl(dtl.getId());
                for (HhQdPheduyetKhMttSLDD sldd : byIdQdDtl){
                    hhQdPdKhMttSlddDtlRepository.deleteByIdDiaDiem(sldd.getId());
                }
                hhQdPheduyetKhMttSLDDRepository.deleteByIdQdDtl(dtl.getId());
            }
            hhQdPheduyetKhMttDxRepository.deleteAll(hhQdPheduyetKhMttDx);
        }
        hhQdPheduyetKhMttHdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(HhQdPheduyetKhMttHdr.TABLE_NAME));

        if (optional.get().getPhanLoai().equals("TH")){
            hhDxuatKhMttThopRepository.updateTrangThaiAndSoQd(optional.get().getIdThHdr(), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
        }else {
            hhDxuatKhMttRepository.updateIdSoQdPd(optional.get().getIdTrHdr(), NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId(), NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
        }
    }


    @Transactional()
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)){
            throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
        }

        List<HhQdPheduyetKhMttHdr> list = hhQdPheduyetKhMttHdrRepository.findAllById(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (HhQdPheduyetKhMttHdr hdr : list){
            if (!hdr.getTrangThai().equals(Contains.DANG_NHAP_DU_LIEU)) {
                throw new Exception("Chỉ thực hiện xóa bản ghi ở trạng thái dự thảo");
            }else {
                this.delete(hdr.getId());
            }
        }

    }

    public HhQdPheduyetKhMttHdr approve(StatusReq req) throws Exception{
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        HhQdPheduyetKhMttHdr dataDB = detail(req.getId());
        String status = req.getTrangThai() + dataDB.getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DANG_NHAP_DU_LIEU:
                dataDB.setNgayPduyet(getDateTimeNow());
                dataDB.setNguoiPduyetId(getUser().getId());
                break;
            case Contains.DA_HOAN_THANH + Contains.BAN_HANH:

                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        HhQdPheduyetKhMttHdr createCheck = null;
        if (req.getTrangThai().equals(Contains.BAN_HANH)) {
            dataDB.setTrangThai(req.getTrangThai());
            if (dataDB.getPhanLoai().equals("TH")) {
                Optional<HhDxKhMttThopHdr> qOptional = hhDxuatKhMttThopRepository.findById(dataDB.getIdThHdr());
                if (qOptional.isPresent()) {
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
                    }
                    hhDxuatKhMttThopRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
                }else {
                    throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
                }
            }else {
                Optional<HhDxuatKhMttHdr> qOptional = hhDxuatKhMttRepository.findById(dataDB.getIdTrHdr());
                if (qOptional.isPresent()) {
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Đề xuất này đã được quyết định");
                    }
                    // Update trạng thái tờ trình
                    qOptional.get().setTrangThaiTh(Contains.DABANHANH_QD);
                    hhDxuatKhMttRepository.save(qOptional.get());
                }else {
                    throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
                }
            }
//            this.cloneProject(dataDB.getId());
            createCheck = hhQdPheduyetKhMttHdrRepository.save(dataDB);
        }
        if(req.getTrangThai().equals(Contains.DA_HOAN_THANH)){
            dataDB.setTrangThaiHd(req.getTrangThai());
            createCheck = hhQdPheduyetKhMttHdrRepository.save(dataDB);
        }
        if(createCheck.getTrangThai().equals(Contains.BAN_HANH)){
            HhSlNhapHangReq hhSlNhapHangReq = new HhSlNhapHangReq();
            BeanUtils.copyProperties(createCheck, hhSlNhapHangReq, "id");
            hhSlNhapHangReq.setKieuNhap(Contains.NHAP_TRUC_TIEP);
            hhSlNhapHangReq.setIdQdKhlcnt(createCheck.getId());
            hhSlNhapHangReq.setNamKhoach(createCheck.getNamKh());
            List<HhQdPheduyetKhMttDx> dxList = hhQdPheduyetKhMttDxRepository.findAllByIdQdHdr(createCheck.getId());
            for (HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx : dxList) {
                List<HhQdPheduyetKhMttSLDD> slddList = hhQdPheduyetKhMttSLDDRepository.findAllByIdQdDtl(hhQdPheduyetKhMttDx.getId());
                for (HhQdPheduyetKhMttSLDD hhQdPheduyetKhMttSLDD : slddList) {
                    hhSlNhapHangReq.setSoLuong(hhQdPheduyetKhMttSLDD.getTongSoLuong());
                    hhSlNhapHangReq.setMaDvi(hhQdPheduyetKhMttSLDD.getMaDvi());
                    hhSlNhapHangService.create(hhSlNhapHangReq);
                }
            }
        }

        return createCheck;
    }

//    public void validateData(HhQdPheduyetKhMttHdr objHdr) throws Exception {
//        for(HhQdPheduyetKhMttDx dtl : objHdr.getChildren()){
//            for(HhQdPheduyetKhMttSLDD dsgthau : dtl.getChildren()){
//                BigDecimal aLong = hhDxuatKhMttRepository.countSLDalenKh(objHdr.getNamKh(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(), NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
//                BigDecimal soLuongTotal = aLong.add(dsgthau.getSoLuong());
//                BigDecimal nhap = keHoachService.getChiTieuNhapXuat(objHdr.getNamKh(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(), "NHAP");
//                if(soLuongTotal.compareTo(nhap) > 0){
//                    throw new Exception(dsgthau.getTenDvi() + " đã nhập quá số lượng chi tiêu, vui lòng nhập lại");
//                }
//            }
//        }
//    }

    private void cloneProject(Long idClone) throws Exception{
        HhQdPheduyetKhMttHdr hdr = this.detail(idClone);
        HhQdPheduyetKhMttHdr hdrClone = new HhQdPheduyetKhMttHdr();
        BeanUtils.copyProperties(hdr, hdrClone);
        hdrClone.setId(null);
        hdrClone.setLastest(true);
        hdrClone.setIdGoc(hdr.getId());
        hhQdPheduyetKhMttHdrRepository.save(hdrClone);
        for (HhQdPheduyetKhMttDx dx: hdr.getChildren()){
            HhQdPheduyetKhMttDx dxClone = new HhQdPheduyetKhMttDx();
            BeanUtils.copyProperties(dx, dxClone);
            dxClone.setId(null);
            dxClone.setIdQdHdr(hdrClone.getId());
            hhQdPheduyetKhMttDxRepository.save(dxClone);
            for (HhQdPheduyetKhMttSLDD sldd : dxClone.getChildren()){
                HhQdPheduyetKhMttSLDD slddClone = new HhQdPheduyetKhMttSLDD();
                BeanUtils.copyProperties(sldd, slddClone);
                slddClone.setId(null);
                slddClone.setIdQdDtl(dxClone.getId());
                hhQdPheduyetKhMttSLDDRepository.save(slddClone);
                for (HhQdPdKhMttSlddDtl  slddDtl : sldd.getChildren()){
                    HhQdPdKhMttSlddDtl slddDtlClone = new HhQdPdKhMttSlddDtl();
                    BeanUtils.copyProperties(slddDtl, slddDtlClone);
                    slddDtlClone.setId(null);
                    slddDtlClone.setIdDiaDiem(slddClone.getId());
                    hhQdPdKhMttSlddDtlRepository.save(slddDtlClone);
                }
            }
        }
    }

    public  void  export(HhQdPheduyetKhMttHdrSearchReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhQdPheduyetKhMttHdr> page=this.searchPage(objReq);
        List<HhQdPheduyetKhMttHdr> data=page.getContent();
        String title = " Danh sách quyết định phê duyệt kế hoạch mua trưc tiếp";
        String[] rowsName =new String[]{"STT","Số quyết định","Ngày quyết định","Trích yếu","Số đề xuất/ tờ trình","Mã tổng hợp","Năm kế hoạch","Phương thức mua trực tiếp","Trạng Thái"};
        String fileName="danh-sach-dx-pd-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhQdPheduyetKhMttHdr pduyet=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=pduyet.getSoQd();
            objs[2]=pduyet.getNgayTao();
            objs[3]=pduyet.getTrichYeu();
            objs[4]=pduyet.getSoTrHdr();
            objs[5]=pduyet.getIdThHdr();
            objs[6]=pduyet.getNamKh();
            objs[7]=pduyet.getPtMuaTrucTiep();
            objs[8]=pduyet.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }


    public HhQdPheduyetKhMttDx detailDtl(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)){
            throw new Exception("Không tồn tại bản ghi.");
        }
        Optional<HhQdPheduyetKhMttDx> byId = hhQdPheduyetKhMttDxRepository.findById(id);
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        HhQdPheduyetKhMttDx dtl = byId.get();

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");

        HhQdPheduyetKhMttHdr hdr = hhQdPheduyetKhMttHdrRepository.findById(dtl.getIdQdHdr()).get();
        hdr.setTenLoaiVthh(hashMapVthh.get(hdr.getLoaiVthh()));
        hdr.setTenCloaiVthh(hashMapVthh.get(hdr.getCloaiVthh()));
        List<FileDinhKem> fileDinhKemHdr = fileDinhKemService.search(hdr.getId(), Arrays.asList(HhQdPheduyetKhMttHdr.TABLE_NAME));
        hdr.setFileDinhKems(fileDinhKemHdr);
        dtl.setHhQdPheduyetKhMttHdr(hdr);
        if (hdr.getIsChange() != null) {
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi())? null : hashMapDvi.get(dtl.getMaDvi()));
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            dtl.setTenLoaiVthh(StringUtils.isEmpty(dtl.getLoaiVthh())? null : hashMapVthh.get(dtl.getLoaiVthh()));
            dtl.setTenCloaiVthh(StringUtils.isEmpty(dtl.getCloaiVthh())? null : hashMapVthh.get(dtl.getCloaiVthh()));
            dtl.setTenNguonVon(StringUtils.isEmpty(dtl.getNguonVon())? null : hashMapNguonVon.get(dtl.getNguonVon()));
            dtl.setTenPthucMuaTrucTiep(Contains.getPthucMtt(dtl.getPthucMuaTrucTiep()));
            dtl.setChildren2(detailDc(hdr));
        }else{
            List<HhQdPheduyetKhMttSLDD> byIdSldd = hhQdPheduyetKhMttSLDDRepository.findAllByIdQdDtl(dtl.getId());
            for (HhQdPheduyetKhMttSLDD sldd : byIdSldd){
                List<HhQdPdKhMttSlddDtl> listDcSlddDtl = new ArrayList<>();
                List<HhTtChaoGiaSlddDtl> byIdSlddDtl = hhTtChaoGiaKhmttSlddDtlRepository.findAllByIdDiaDiem(sldd.getId());
                for (HhTtChaoGiaSlddDtl hhTtChaoGiaSlddDtl : byIdSlddDtl) {
                    HhQdPdKhMttSlddDtl dcSlddDtl = new HhQdPdKhMttSlddDtl();
                    hhTtChaoGiaSlddDtl.setTenDiemKho(hashMapDvi.get(hhTtChaoGiaSlddDtl.getMaDiemKho()));
                    BeanUtils.copyProperties(hhTtChaoGiaSlddDtl, dcSlddDtl);
                    listDcSlddDtl.add(dcSlddDtl);
                }
                sldd.setChildren(listDcSlddDtl);
                sldd.setTenDvi(hashMapDvi.get(sldd.getMaDvi()));
                List<HhChiTietTTinChaoGia> byIdQdDtl = hhCtietTtinCgiaRepository.findAllByIdQdPdSldd(sldd.getId());
                for (HhChiTietTTinChaoGia chaoGia : byIdQdDtl){
                    List<FileDinhKem> fileDinhKems = fileDinhKemService.search(chaoGia.getId(), Arrays.asList(HhChiTietTTinChaoGia.TABLE_NAME));
                    if (!DataUtils.isNullOrEmpty(fileDinhKems)) {
                        chaoGia.setFileDinhKems(fileDinhKems.get(0));
                    }
                }
                sldd.setListChaoGia(byIdQdDtl);
            }
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi())? null : hashMapDvi.get(dtl.getMaDvi()));
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            dtl.setTenLoaiVthh(StringUtils.isEmpty(dtl.getLoaiVthh())? null : hashMapVthh.get(dtl.getLoaiVthh()));
            dtl.setTenCloaiVthh(StringUtils.isEmpty(dtl.getCloaiVthh())? null : hashMapVthh.get(dtl.getCloaiVthh()));
            dtl.setTenNguonVon(StringUtils.isEmpty(dtl.getNguonVon())? null : hashMapNguonVon.get(dtl.getNguonVon()));
            dtl.setTenPthucMuaTrucTiep(Contains.getPthucMtt(dtl.getPthucMuaTrucTiep()));
            dtl.setChildren(byIdSldd);


            if (!DataUtils.isNullObject(dtl.getPthucMuaTrucTiep())) {
                if (dtl.getPthucMuaTrucTiep().equals(Contains.UY_QUYEN)){
                    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(id, Arrays.asList(HhQdPheduyetKhMttDx.TABLE_NAME));
                    dtl.setFileDinhKemUyQuyen(fileDinhKem);
                }
                if (dtl.getPthucMuaTrucTiep().equals(Contains.MUA_LE)){
                    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(id, Arrays.asList(HhQdPheduyetKhMttDx.TABLE_NAME));
                    dtl.setFileDinhKemMuaLe(fileDinhKem);
                }
                if (dtl.getPthucMuaTrucTiep().equals(Contains.CHAO_GIA)){
                    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(id, Arrays.asList(HhQdPheduyetKhMttDx.TABLE_NAME));
                    dtl.setFileDinhKem(fileDinhKem);
                }
            }
        }
        return dtl;
    }


    private List<HhDcQdPduyetKhmttSldd> detailDc(HhQdPheduyetKhMttHdr hhQdPheduyetKhMttDx) {
        List<HhDcQdPduyetKhmttHdr> hhDcQdPduyetKhmttHdr = hhDcQdPduyetKhMttRepository.findAllByIdQdGocOrderByIdDesc(hhQdPheduyetKhMttDx.getId());
        HhDcQdPduyetKhmttHdr data = hhDcQdPduyetKhmttHdr.get(0);
        hhQdPheduyetKhMttDx.setSoQdDc(data.getSoQdDc());
        List<HhDcQdPduyetKhmttSldd> children = new ArrayList<>();
        Map<String, String> hashMapDmdv = getListDanhMucDvi(null, null, "01");
        List<HhDcQdPduyetKhmttDx> listdx = hhDcQdPduyetKhMttDxRepository.findAllByIdDcHdr(data.getId());
        List<Long> idDx = listdx.stream().map(HhDcQdPduyetKhmttDx::getId).collect(Collectors.toList());
        List<HhDcQdPduyetKhmttSldd> listSlDd = hhDcQdPduyetKhmttSlddRepository.findAllByIdDcKhmttIn(idDx);
        for (HhDcQdPduyetKhmttSldd sldd : listSlDd) {
            List<HhChiTietTTinChaoGia> byIdQdDtl = hhCtietTtinCgiaRepository.findAllByIdQdPdSldd(sldd.getId());
            for (HhChiTietTTinChaoGia chaoGia : byIdQdDtl) {
                chaoGia.setDonGiaVat(chaoGia.getDonGia());
                List<FileDinhKem> fileDinhKems = fileDinhKemService.search(chaoGia.getId(), Arrays.asList(HhChiTietTTinChaoGia.TABLE_NAME));
                if (!DataUtils.isNullOrEmpty(fileDinhKems)) {
                    chaoGia.setFileDinhKems(fileDinhKems.get(0));
                }
            }
            sldd.setListChaoGia(byIdQdDtl);
            sldd.setTenDvi(StringUtils.isEmpty(sldd.getMaDvi()) ? null : hashMapDmdv.get(sldd.getMaDvi()));
            List<HhTtChaoGiaSlddDtl> listSlddDtl = hhTtChaoGiaKhmttSlddDtlRepository.findAllByIdDiaDiem(sldd.getId());
            List<HhDcQdPdKhmttSlddDtl> listDcSlddDtl = new ArrayList<>();
            for (HhTtChaoGiaSlddDtl slddDtl : listSlddDtl) {
                HhDcQdPdKhmttSlddDtl dcSlddDtl = new HhDcQdPdKhmttSlddDtl();
                slddDtl.setTenDiemKho(StringUtils.isEmpty(slddDtl.getMaDiemKho()) ? null : hashMapDmdv.get(slddDtl.getMaDiemKho()));
                BeanUtils.copyProperties(slddDtl, dcSlddDtl);
                listDcSlddDtl.add(dcSlddDtl);
            }
                sldd.setChildren(listDcSlddDtl);
            children.add(sldd);
        }
        return children;
    }

    public ReportTemplateResponse preview(HhQdPheduyetKhMttHdrReq req) throws Exception {
        HhQdPheduyetKhMttHdr qdPheduyetKhMttHdr = detail(req.getId());
        HhQdPheduyetKhMttPreview object = new HhQdPheduyetKhMttPreview();
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        object.setTenCloaiVthh(qdPheduyetKhMttHdr.getTenCloaiVthh().toUpperCase());
        object.setNamKhoach(qdPheduyetKhMttHdr.getNamKh().toString());
        qdPheduyetKhMttHdr.getChildren().forEach(cuc -> {
            AtomicReference<BigDecimal> tongThanhTien = new AtomicReference<>(BigDecimal.ZERO);
            cuc.getChildren().forEach(chiCuc -> {
                tongThanhTien.updateAndGet(v -> v.add(chiCuc.getDonGiaVat().multiply(chiCuc.getTongSoLuong())));
            });
            cuc.setTongThanhTien(docxToPdfConverter.convertBigDecimalToStr(tongThanhTien.get()));
        });
        object.setDetails(qdPheduyetKhMttHdr.getChildren());
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }
}
