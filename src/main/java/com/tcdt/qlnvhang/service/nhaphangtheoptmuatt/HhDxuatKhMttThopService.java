package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhDxKhMttThopPreview;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhDxKhMttTChiThopReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhDxKhMttThopHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhDxKhMttThopReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class HhDxuatKhMttThopService extends BaseServiceImpl {

    @Autowired
    private HhDxuatKhMttThopRepository hhDxuatKhMttThopRepository;

    @Autowired
    private HhDxuatKhMttRepository hhDxuatKhMttRepository;

    @Autowired
    private HhDxuatKhMttThopDtlRepository hhDxuatKhMttThopDtlRepository;
    @Autowired
    private HhQdPheduyetKhMttHdrRepository hhQdPheduyetKhMttHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private HhDxuatKhMttSlddRepository hhDxuatKhMttSlddRepository;


    public Page<HhDxKhMttThopHdr> searchPage(SearchHhDxKhMttThopReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhDxKhMttThopHdr> data = hhDxuatKhMttThopRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapDmhh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            Optional<HhQdPheduyetKhMttHdr> hhQdPheduyetKhMttHdr = hhQdPheduyetKhMttHdrRepository.findByIdThHdrAndLastest(f.getId(), true);
            if(hhQdPheduyetKhMttHdr.isPresent()){
                f.setQdPdMttId(hhQdPheduyetKhMttHdr.get().getId());
            }
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
        });
        return data;
    }

    public HhDxKhMttThopHdr sumarryData(HhDxKhMttTChiThopReq objReq, HttpServletRequest req) throws Exception {
        List<HhDxuatKhMttHdr> dxuatList = hhDxuatKhMttRepository.listTongHop(
                objReq.getNamKh(),
                objReq.getLoaiVthh(),
                objReq.getCloaiVthh());
        if (dxuatList.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }

        HhDxKhMttThopHdr thopHdr = new HhDxKhMttThopHdr();

        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        List<HhDxKhMttThopDtl> thopDtls = new ArrayList<HhDxKhMttThopDtl>();
        String tChuanCluong = "";
        String soQdcc = "";
        Long idSoQdcc = 0L;
        for (HhDxuatKhMttHdr dXuat : dxuatList) {
            HhDxKhMttThopDtl thopDtl = new HhDxKhMttThopDtl();
            BeanUtils.copyProperties(dXuat,thopDtl,"id");
            thopDtl.setIdDxHdr(dXuat.getId());
            thopDtl.setTenDvi(mapDmucDvi.get(dXuat.getMaDvi()));
            tChuanCluong = tChuanCluong.concat(dXuat.getTchuanCluong() + "");
            soQdcc = soQdcc.concat(dXuat.getSoQdCc() + "");
            idSoQdcc = dXuat.getIdSoQdCc();
            thopDtls.add(thopDtl);
        }
        thopHdr.setTchuanCluong(tChuanCluong);
        thopHdr.setSoQdCc(soQdcc);
        thopHdr.setIdSoQdCc(idSoQdcc);
        thopHdr.setChildren(thopDtls);
        return thopHdr;
    }


    public HhDxKhMttThopHdr create(HhDxKhMttThopHdrReq objReq, HttpServletRequest req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();

        HhDxKhMttThopHdr thopHdr = sumarryData(objReq, req);
        thopHdr.setId(objReq.getIdTh());
        thopHdr.setNgayTao(new Date());
        thopHdr.setNguoiTaoId(getUser().getId());
        thopHdr.setLoaiVthh(objReq.getLoaiVthh());
        thopHdr.setCloaiVthh(objReq.getCloaiVthh());
        thopHdr.setTrangThai(Contains.CHUATAO_QD);
        thopHdr.setNgayThop(new Date());
        thopHdr.setNamKh(objReq.getNamKh());
        thopHdr.setMaDvi(objReq.getMaDvi());
        thopHdr.setNoiDungThop(objReq.getNoiDungThop());
        hhDxuatKhMttThopRepository.save(thopHdr);
        if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), thopHdr.getId(), HhDxKhMttThopHdr.TABLE_NAME);
        }
        for (HhDxKhMttThopDtl dtl : thopHdr.getChildren()) {
            dtl.setIdThopHdr(thopHdr.getId());
            hhDxuatKhMttThopDtlRepository.save(dtl);
        }
        if (thopHdr.getId() > 0 && thopHdr.getChildren().size() > 0) {
            List<String> soDxuatList = thopHdr.getChildren().stream().map(HhDxKhMttThopDtl::getSoDxuat)
                    .collect(Collectors.toList());
            hhDxuatKhMttRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP, thopHdr.getId());
        }
        return thopHdr;
    }

    @Transactional()
    public HhDxKhMttThopHdr update(HhDxKhMttThopHdrReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<HhDxKhMttThopHdr> qOptional = hhDxuatKhMttThopRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        HhDxKhMttThopHdr dataDTB = qOptional.get();
        HhDxKhMttThopHdr dataMap = ObjectMapperUtils.map(objReq, HhDxKhMttThopHdr.class);

        updateObjectToObject(dataDTB, dataMap);
        hhDxuatKhMttThopRepository.save(dataDTB);
        return dataDTB;

    }

    public HhDxKhMttThopHdr detail (String ids) throws Exception{
        if (StringUtils.isEmpty(ids))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Optional<HhDxKhMttThopHdr> qOptional = hhDxuatKhMttThopRepository.findById(Long.parseLong(ids));

        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        HhDxKhMttThopHdr hdrThop = qOptional.get();

        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();

        hdrThop.setTenLoaiVthh(hashMapDmHh.get(hdrThop.getLoaiVthh()));
        hdrThop.setTenCloaiVthh(hashMapDmHh.get(hdrThop.getCloaiVthh()));

        List<HhDxKhMttThopDtl> listTh = hhDxuatKhMttThopDtlRepository.findByIdThopHdr(hdrThop.getId());
        Map<String, String> mapDmucDvi = getMapTenDvi();
        listTh.forEach(f -> {
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));

        });
        hdrThop.setChildren(listTh);
        hdrThop.setFileDinhKems(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhDxKhMttThopHdr.TABLE_NAME)));
        return hdrThop;
    }

    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

        Optional<HhDxKhMttThopHdr> optional = hhDxuatKhMttThopRepository.findById(idSearchReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        List<HhDxKhMttThopDtl> listDls= hhDxuatKhMttThopDtlRepository.findByIdThopHdr(optional.get().getId());
        if(!CollectionUtils.isEmpty(listDls)){
            List<Long> idDxList = listDls.stream().map(HhDxKhMttThopDtl::getIdDxHdr).collect(Collectors.toList());
            List<HhDxuatKhMttHdr> listDxHdr = hhDxuatKhMttRepository.findByIdIn(idDxList);
            if(!CollectionUtils.isEmpty(listDxHdr)){
                listDxHdr.stream().map(item ->{
                    item.setTrangThaiTh(Contains.CHUATONGHOP);
                    item.setMaThop(null);
                    return item;
                }).collect(Collectors.toList());
            }
            hhDxuatKhMttRepository.saveAll(listDxHdr);
        }
        hhDxuatKhMttThopDtlRepository.deleteAll(listDls);
        hhDxuatKhMttThopRepository.delete(optional.get());
    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        List<HhDxKhMttThopHdr> listThop= hhDxuatKhMttThopRepository.findAllByIdIn(idSearchReq.getIdList());
        for (HhDxKhMttThopHdr thopHdr: listThop){
            List<HhDxKhMttThopDtl> listDls= hhDxuatKhMttThopDtlRepository.findByIdThopHdr(thopHdr.getId());
            if(!CollectionUtils.isEmpty(listDls)){
                List<Long> idDxList = listDls.stream().map(HhDxKhMttThopDtl::getIdDxHdr).collect(Collectors.toList());
                List<HhDxuatKhMttHdr> listDxHdr = hhDxuatKhMttRepository.findByIdIn(idDxList);
                if(!CollectionUtils.isEmpty(listDxHdr)){
                    listDxHdr.stream().map(item ->{
                        item.setTrangThaiTh(Contains.CHUATONGHOP);
                        return item;
                    }).collect(Collectors.toList());
                }
                hhDxuatKhMttRepository.saveAll(listDxHdr);
            }
            hhDxuatKhMttThopDtlRepository.deleteAll(listDls);
        }
        hhDxuatKhMttThopRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

    public void export(SearchHhDxKhMttThopReq searchReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        Page<HhDxKhMttThopHdr> page = this.searchPage(searchReq);
        List<HhDxKhMttThopHdr> data = page.getContent();

        String title = "Danh sách tổng hợp kế hoạch mua trực tiếp";
        String[] rowsName = new String[] { "STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp",
                "Năm kế hoạch","Chủng loại hàng hóa", "Trạng thái", "Số QĐ phê duyêt mua trực tiếp"};
        String filename = "Tong_hop_de_xuat_ke_hoach_lua_chon_nha_thau.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhDxKhMttThopHdr thop = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = thop.getId();
            objs[2] = thop.getNgayThop();
            objs[3] = thop.getNoiDungThop();
            objs[4] = thop.getNamKh();
            objs[5] = thop.getTenCloaiVthh();
            objs[6] = thop.getTenTrangThai();
            objs[7] = thop.getSoQdPduyet();
            dataList.add(objs);
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HhDxKhMttThopHdrReq req) throws Exception {
        Optional<HhDxKhMttThopHdr> qOptional = hhDxuatKhMttThopRepository.findById(req.getId());
        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }
        List<HhDxKhMttThopDtl> listTh = hhDxuatKhMttThopDtlRepository.findByIdThopHdr(qOptional.get().getId());
        Map<String, String> mapDmucDvi = getMapTenDvi();
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        AtomicReference<BigDecimal> tongSl = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> tongThanhTien = new AtomicReference<>(BigDecimal.ZERO);
        listTh.forEach(f -> {
            Optional<HhDxuatKhMttHdr> dxuatKhMttHdr = hhDxuatKhMttRepository.findById(f.getIdDxHdr());
            if (!dxuatKhMttHdr.isPresent()){
                throw new UnsupportedOperationException("Không tồn tại bản ghi đề xuất");
            }
            try {
                f.setTgianKthuc(convertDate(dxuatKhMttHdr.get().getTgianKthuc()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
            List<HhDxuatKhMttSldd> dsSlDdList = hhDxuatKhMttSlddRepository.findAllByIdHdr(f.getIdDxHdr());
            for(HhDxuatKhMttSldd dsG : dsSlDdList){
                dsG.setTenDvi(mapDmucDvi.get(dsG.getMaDvi()));
                dsG.setTongThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(dsG.getDonGia().multiply(dsG.getSoLuong())));
                tongThanhTien.updateAndGet(v -> v.add(dsG.getDonGia().multiply(dsG.getSoLuong())));
            }
            f.setDsChiCucPreviews(dsSlDdList);
            tongSl.updateAndGet(v -> v.add(f.getTongSoLuong()));
            f.setSoLuongStr(docxToPdfConverter.convertBigDecimalToStr(f.getTongSoLuong()));
        });
        HhDxKhMttThopPreview object = new HhDxKhMttThopPreview();
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        object.setNamKhoach(qOptional.get().getNamKh().toString());
        object.setTenCloaiVthh(hashMapDmHh.get(qOptional.get().getCloaiVthh()).toUpperCase());
        object.setDetails(listTh);
        object.setTongSl(docxToPdfConverter.convertBigDecimalToStr(tongSl.get()));
        object.setTongThanhTien(docxToPdfConverter.convertBigDecimalToStr(tongThanhTien.get()));
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }

}
