package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;


import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttSLDDReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhChiTietTTinChaoGiaPreview;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;

import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;

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


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhPthucTkhaiMuaTtService extends BaseServiceImpl {
    @Autowired
    private HhQdPheduyetKhMttHdrRepository hhQdPheduyetKhMttHdrRepository;

    @Autowired
    private HhCtietTtinCgiaRepository hhCtietTtinCgiaRepository;

    @Autowired
    private HhQdPheduyetKhMttDxRepository hhQdPheduyetKhMttDxRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private HhQdPheduyetKhMttSLDDRepository hhQdPheduyetKhMttSLDDRepository;

    @Autowired
    private HhQdPdKhMttSlddDtlRepository hhQdPdKhMttSlddDtlRepository;

    @Autowired
    private HhDcQdPdKhmttSlddDtlRepository hhDcQdPdKhmttSlddDtlRepository;

    @Autowired
    private HhTtChaoGiaKhmttSlddDtlRepository hhTtChaoGiaKhmttSlddDtlRepository;

    @Autowired
    private HhQdPheduyetKhMttHdrService hhQdPheduyetKhMttHdrService;
    @Autowired
    private HhDcQdPduyetKhMttRepository hhDcQdPduyetKhMttRepository;
    @Autowired
    private HhDcQdPduyetKhMttDxRepository hhDcQdPduyetKhMttDxRepository;
    @Autowired
    private HhDcQdPduyetKhmttSlddRepository hhDcQdPduyetKhmttSlddRepository;


    public Page<HhQdPheduyetKhMttDx> selectPage(SearchHhPthucTkhaiReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        req.setMaCuc(this.getUser().getDvql());
        Page<HhQdPheduyetKhMttDx> dtl = hhQdPheduyetKhMttDxRepository.search(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        dtl.getContent().forEach( f ->{
            try {
                HhQdPheduyetKhMttHdr hdr = hhQdPheduyetKhMttHdrRepository.findById(f.getIdQdHdr()).get();
                if(hdr.getIsChange() != null){
                    List<HhDcQdPduyetKhmttHdr> hhDcQdPduyetKhmttHdr = hhDcQdPduyetKhMttRepository.findAllByIdQdGocOrderByIdDesc(hdr.getId());
                    HhDcQdPduyetKhmttHdr data = hhDcQdPduyetKhmttHdr.get(0);
                    List<HhDcQdPduyetKhmttDx> listdx = hhDcQdPduyetKhMttDxRepository.findAllByIdDcHdr(data.getId());
                    List<Long> idDx = listdx.stream().map(HhDcQdPduyetKhmttDx::getId).collect(Collectors.toList());
                    List<HhDcQdPduyetKhmttSldd> listSlDd = hhDcQdPduyetKhmttSlddRepository.findAllByIdDcKhmttIn(idDx);
                    if(listSlDd.size() > 0 && this.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)){
                        if(listSlDd.stream().filter(x -> x.getMaDvi().equals(this.getUser().getDvql())).collect(Collectors.toList()).size() > 0){
                            f.setTongSoLuong(listSlDd.stream().filter(x -> x.getMaDvi().equals(this.getUser().getDvql())).collect(Collectors.toList()).get(0).getTongSoLuong());
                        }
                    }else{
                        if(listSlDd.stream().filter(x -> x.getMaDvi().contains(this.getUser().getDvql())).collect(Collectors.toList()).size() > 0){
                            f.setTongSoLuong(listSlDd.stream().filter(x -> x.getMaDvi().contains(this.getUser().getDvql())).collect(Collectors.toList()).stream().map(item -> item.getTongSoLuong()).reduce(BigDecimal.ZERO, BigDecimal::add));
                        }
                    }
                    hdr.setSoQdDc(data.getSoQdDc());
                }
                hdr.setTenLoaiVthh(hashMapVthh.get(hdr.getLoaiVthh()));
                hdr.setTenCloaiVthh(hashMapVthh.get(hdr.getCloaiVthh()));
                f.setHhQdPheduyetKhMttHdr(hdr);
                f.setSoQdDc(f.getHhQdPheduyetKhMttHdr().getSoQdDc());


                List<HhQdPheduyetKhMttSLDD> obj = hhQdPheduyetKhMttSLDDRepository.findAllByIdQdDtl(f.getId());
                for (HhQdPheduyetKhMttSLDD hhQdPheduyetKhMttSLDD : obj) {
                    List<HhChiTietTTinChaoGia> chaoGiaList = hhCtietTtinCgiaRepository.findAllByIdQdPdSldd(hhQdPheduyetKhMttSLDD.getId());
                    hhQdPheduyetKhMttSLDD.setListChaoGia(chaoGiaList);
                }
                f.setChildren(obj);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            f.setTenPthucMuaTrucTiep(Contains.getPthucMtt(f.getPthucMuaTrucTiep()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(f.getTrangThai()));
            f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
        });
        return dtl;
    }

    @Transactional
    public List<HhChiTietTTinChaoGia> create(HhCgiaReq objReq) throws Exception {
        Optional<HhQdPheduyetKhMttDx> byId = hhQdPheduyetKhMttDxRepository.findById(objReq.getIdQdDtl());
        HhQdPheduyetKhMttDx dx = byId.get();
        if (!byId.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }else {
//            dx.setTrangThai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            dx.setPthucMuaTrucTiep(objReq.getPthucMuaTrucTiep());
            dx.setDiaDiemChaoGia(objReq.getDiaDiemChaoGia());
            dx.setNgayNhanCgia(getDateTimeNow());
            dx.setNgayMkho(objReq.getNgayMkho());
            dx.setNgayMua(objReq.getNgayMua());
            dx.setGhiChuChaoGia(objReq.getGhiChuChaoGia());
            dx.setSoQd(objReq.getSoQd());

            if (objReq.getPthucMuaTrucTiep().equals(Contains.UY_QUYEN)) {
                dx.setTrangThai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
                if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKemUyQuyen())) {
                    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemUyQuyen(), dx.getId(), HhQdPheduyetKhMttDx.TABLE_NAME);
                    dx.setFileDinhKemUyQuyen(fileDinhKemList);
                }
            }
            if (objReq.getPthucMuaTrucTiep().equals(Contains.MUA_LE)) {
                dx.setTrangThai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
                if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKemMuaLe())) {
                    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemMuaLe(), dx.getId(), HhQdPheduyetKhMttDx.TABLE_NAME);
                    dx.setFileDinhKemMuaLe(fileDinhKemList);
                }
            }
            if (objReq.getPthucMuaTrucTiep().equals(Contains.CHAO_GIA)) {
                dx.setTrangThai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
                if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKem())) {
                    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), dx.getId(), HhQdPheduyetKhMttDx.TABLE_NAME);
                    dx.setFileDinhKem(fileDinhKemList);
                }
            }
            hhQdPheduyetKhMttDxRepository.save(dx);
        }

        Optional<HhQdPheduyetKhMttHdr> hdr = hhQdPheduyetKhMttHdrRepository.findById(dx.getIdQdHdr());
        HhQdPheduyetKhMttHdr qdHdr = hdr.get();
        if (!hdr.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }else {
            Optional<HhQdPheduyetKhMttHdr> hdrOptional = hhQdPheduyetKhMttHdrRepository.findById(qdHdr.getId());
            hdrOptional.get().setPtMuaTrucTiep(objReq.getPthucMuaTrucTiep());
            hhQdPheduyetKhMttHdrRepository.save(hdrOptional.get());
        }
        List<HhChiTietTTinChaoGia> chaoGiaList = new ArrayList<>();
        for (HhQdPheduyetKhMttSLDDReq hhQdPheduyetKhMttSLDDReq : objReq.getDanhSachCtiet()) {
            HhQdPheduyetKhMttSLDD sldd = new HhQdPheduyetKhMttSLDD();
            BeanUtils.copyProperties(hhQdPheduyetKhMttSLDDReq, sldd);
            hhQdPheduyetKhMttSLDDRepository.save(sldd);
//            if(hdr.get().getIsChange() != null){
                hhTtChaoGiaKhmttSlddDtlRepository.deleteAllByIdDiaDiem(sldd.getId());
                for (HhQdPdKhMttSlddDtlReq child : hhQdPheduyetKhMttSLDDReq.getChildren()) {
                    HhTtChaoGiaSlddDtl hhTtChaoGiaSlddDtl = new HhTtChaoGiaSlddDtl();
                    BeanUtils.copyProperties(child, hhTtChaoGiaSlddDtl);
                    hhTtChaoGiaKhmttSlddDtlRepository.save(hhTtChaoGiaSlddDtl);
                }
//            }else{
//                hhQdPdKhMttSlddDtlRepository.deleteByIdDiaDiem(sldd.getId());
//                for (HhQdPdKhMttSlddDtlReq child : hhQdPheduyetKhMttSLDDReq.getChildren()) {
//                    HhQdPdKhMttSlddDtl hhQdPdKhMttSlddDtl = new HhQdPdKhMttSlddDtl();
//                    BeanUtils.copyProperties(child, hhQdPdKhMttSlddDtl);
//                    hhQdPdKhMttSlddDtlRepository.save(hhQdPdKhMttSlddDtl);
//                }
//            }
            hhCtietTtinCgiaRepository.deleteAllByIdQdPdSldd(hhQdPheduyetKhMttSLDDReq.getId());
            for (HhChiTietTTinChaoGiaReq child : hhQdPheduyetKhMttSLDDReq.getListChaoGia()) {
                HhChiTietTTinChaoGia chaoGia = new HhChiTietTTinChaoGia();
                BeanUtils.copyProperties(child, chaoGia, "id");
                chaoGia.setDonGia(child.getDonGia() != null ? child.getDonGia() : child.getDonGiaVat());
                chaoGia.setThanhTien(chaoGia.getDonGia() != null ? chaoGia.getDonGia().multiply(chaoGia.getSoLuong()).multiply(BigDecimal.valueOf(1000)) : chaoGia.getDonGiaVat().multiply(chaoGia.getSoLuong()).multiply(BigDecimal.valueOf(1000)));
                chaoGia.setId(null);
                HhChiTietTTinChaoGia save = hhCtietTtinCgiaRepository.save(chaoGia);
                if (!DataUtils.isNullObject(child.getFileDinhKems())) {
                    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(child.getFileDinhKems()), save.getId(), HhChiTietTTinChaoGia.TABLE_NAME);
                    chaoGia.setFileDinhKems(fileDinhKems.get(0));
                }

                chaoGiaList.add(chaoGia);
            }
        }

        return chaoGiaList;
    }

//    public List<HhChiTietTTinChaoGia> detail(String ids) throws Exception {
//            Optional<HhQdPheduyetKhMttDx> byId = hhQdPheduyetKhMttDxRepository.findById(Long.parseLong(ids));
//            if(!byId.isPresent()){
//                throw new Exception("Bản ghi không tồn tại");
//            }
//        HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx = byId.get();
//        List<HhChiTietTTinChaoGia> byIdDtGt = hhCtietTtinCgiaRepository.findAllByIdTkhaiKh(Long.parseLong(ids));
//        byIdDtGt.forEach(f -> {
//            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
//        });
//        for (HhChiTietTTinChaoGia chaoGia : byIdDtGt){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(chaoGia.getId(), Arrays.asList(HhChiTietTTinChaoGia.TABLE_NAME));
//            chaoGia.setFileDinhKems(fileDinhKems.get(0));
//        }
//        if(hhQdPheduyetKhMttDx.getPtMuaTrucTiep().equals(Contains.UY_QUYEN)){
//            hhQdPheduyetKhMttDx.setFileDinhKemUyQuyen(fileDinhKemService.search(hhQdPheduyetKhMttDx.getId(), Collections.singleton(HhQdPheduyetKhMttDx.TABLE_NAME)));
//        }
//        if (hhQdPheduyetKhMttDx.getPtMuaTrucTiep().equals(Contains.MUA_LE)){
//            hhQdPheduyetKhMttDx.setFileDinhKemMuaLe(fileDinhKemService.search(hhQdPheduyetKhMttDx.getId(), Collections.singleton(HhQdPheduyetKhMttDx.TABLE_NAME)));
//        }
//        return byIdDtGt;
//        return null;
//    }

  public  void approve(HhCgiaReq stReq) throws Exception {
        Optional<HhQdPheduyetKhMttDx> optional = hhQdPheduyetKhMttDxRepository.findById(stReq.getId());
        if(!optional.isPresent()){
            throw new Exception("Bản nghi không tồn tại");
        }
         String status = stReq.getTrangThai() + optional.get().getTrangThai();
        if ((NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId()).equals(status) || (NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.CHUACAPNHAT.getId()).equals(status)) {
            optional.get().setTrangThai(stReq.getTrangThai());
        }else{
            throw new Exception("Cập nhật không thành công");
        }
      hhQdPheduyetKhMttDxRepository.save(optional.get());
  }

    public void export(SearchHhPthucTkhaiReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<HhQdPheduyetKhMttDx> page = this.selectPage(req);
        List<HhQdPheduyetKhMttDx> data = page.getContent();
        String title = " Danh sách thông tin triển khai kế hoạch mua trực tiếp";
        String[] rowsName = new String[]{"STT", "Số QĐ phê duyệt KH mua trực tiếp","Đơn vị", "Năm kế hoạch", "Phương thức bán trực tiếp", "Ngày nhận chào giá/Ngày ủy quyền", "Số QĐ PDKQ chào giá", "Loại hàng hóa", "Chủng loại hàng hóa", "Trạng thái"};
        String fileName = "Danh-sach-thong-tin-trien-khai-ke-hoach-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhQdPheduyetKhMttDx dtl = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dtl.getSoQd();
            objs[2] = dtl.getTenDvi();
            objs[3] = dtl.getNamKh();
            objs[4] = dtl.getPthucMuaTrucTiep();
            objs[5] = dtl.getNgayNhanCgia();
            objs[6] = dtl.getSoQdKq();
            objs[7] = dtl.getTenLoaiVthh();
            objs[8] = dtl.getTenCloaiVthh();
            objs[9] = dtl.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HhCgiaReq hhCgiaReq) throws Exception {
        HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx = hhQdPheduyetKhMttHdrService.detailDtl(hhCgiaReq.getIdQdDtl());
        ReportTemplate model = findByTenFile(hhCgiaReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        HhChiTietTTinChaoGiaPreview object = new HhChiTietTTinChaoGiaPreview();
        BigDecimal tongThanhTien = BigDecimal.ZERO;
        for (HhQdPheduyetKhMttSLDD child : hhQdPheduyetKhMttDx.getChildren()) {
            for (HhChiTietTTinChaoGia tTinChaoGia : child.getListChaoGia()) {
                tTinChaoGia.setThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(tTinChaoGia.getThanhTien()));
                tongThanhTien = tongThanhTien.add(docxToPdfConverter.convertNullToZero(tTinChaoGia.getThanhTien()));
            }
        }
        hhQdPheduyetKhMttDx.setTenCloaiVthh(hhQdPheduyetKhMttDx.getTenLoaiVthh().toUpperCase());
        hhQdPheduyetKhMttDx.setTenDvi(hhQdPheduyetKhMttDx.getTenDvi().toUpperCase());
        hhQdPheduyetKhMttDx.setTongThanhTien(docxToPdfConverter.convertBigDecimalToStr(tongThanhTien));
        return docxToPdfConverter.convertDocxToPdf(inputStream, hhQdPheduyetKhMttDx);
    }
}

