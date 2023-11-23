package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBcanKeHangDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBcanKeHangHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhBcanKeHangHdrPreview;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.NumberToWord;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.*;

@Service
public class HhBcanKeHangService extends BaseServiceImpl {
    @Autowired
    private HhBcanKeHangHdrRepository hhBcanKeHangHdrRepository;
    @Autowired
    private HhBcanKeHangDtlRepository hhBcanKeHangDtlRepository;
    @Autowired
    private HhPhieuNhapKhoHdrRepository hhPhieuNhapKhoHdrRepository;

    public Page<HhBcanKeHangHdr> searchPage(SearchHhBcanKeHangReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhBcanKeHangHdr> data = hhBcanKeHangHdrRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQuyetDinhNhap(),
                objReq.getSoBangKeCanHang(),
                convertDateToString(objReq.getNgayNhapKhoTu()),
                convertDateToString(objReq.getNgayNhapKhoDen()),
                objReq.getTrangThai(),
                userInfo.getDvql(),
                pageable
        );
        Map<String,String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        data.getContent().forEach(item ->{
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            item.setTenLoaiVthh(mapVthh.get(item.getLoaiVthh()));
            item.setTenCloaiVthh(mapVthh.get(item.getCloaiVthh()));
            item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
            item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
            item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
            item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
            item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
        });
        return data;
    }

    @Transactional()
    public HhBcanKeHangHdr create(HhBcanKeHangHdrReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        HhBcanKeHangHdr data = new HhBcanKeHangHdr();
        BeanUtils.copyProperties(objReq, data,"id");
        data.setNgayTao(getDateTimeNow());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setId(Long.valueOf(data.getSoBangKeCanHang().split("/")[0]));
        hhBcanKeHangHdrRepository.save(data);

        Optional<HhPhieuNhapKhoHdr> hhPhieuNhapKhoHdr = hhPhieuNhapKhoHdrRepository.findById(data.getIdPhieuNhapKho());
        hhPhieuNhapKhoHdr.get().setSoBangKeCanHang(data.getSoBangKeCanHang());
        hhPhieuNhapKhoHdrRepository.save(hhPhieuNhapKhoHdr.get());
        this.saveCtiet(data.getId(), objReq);
        return data;
    }

    @Transactional()
    void saveCtiet(Long idHdr, HhBcanKeHangHdrReq objReq){
        hhBcanKeHangDtlRepository.deleteByIdHdr(idHdr);
            for (HhBcanKeHangDtlReq keHangDtlReq : objReq.getHhBcanKeHangDtlReqList()){
                HhBcanKeHangDtl hhBcanKeHangDtl = new HhBcanKeHangDtl();
                BeanUtils.copyProperties(keHangDtlReq, hhBcanKeHangDtl, "id");
                hhBcanKeHangDtl.setIdHdr(idHdr);
                hhBcanKeHangDtlRepository.save(hhBcanKeHangDtl);
            }
    }

    @Transactional()
    public HhBcanKeHangHdr update(HhBcanKeHangHdrReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhBcanKeHangHdr> optional = hhBcanKeHangHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()){
            throw new Exception(" Bảng cân kê hàng không tồn tại. ");
        }
        HhBcanKeHangHdr bcanKeHangHdr = optional.get();
        BeanUtils.copyProperties(objReq, bcanKeHangHdr, "id");
        bcanKeHangHdr.setNgaySua(getDateTimeNow());
        bcanKeHangHdr.setNguoiSua(userInfo.getUsername());
        hhBcanKeHangHdrRepository.save(bcanKeHangHdr);
        this.saveCtiet(bcanKeHangHdr.getId(), objReq);
        return bcanKeHangHdr;
    }

    public HhBcanKeHangHdr detail(Long ids) throws Exception{
        Optional<HhBcanKeHangHdr> qOptional = hhBcanKeHangHdrRepository.findById(ids);
        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException(" Bản ghi không tồn tại");
        }
        HhBcanKeHangHdr bcanKeHangHdr = qOptional.get();
        Map<String,String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        bcanKeHangHdr.setTenLoaiVthh( StringUtils.isEmpty(bcanKeHangHdr.getLoaiVthh()) ? null : mapVthh.get(bcanKeHangHdr.getLoaiVthh()));
        bcanKeHangHdr.setTenCloaiVthh( StringUtils.isEmpty(bcanKeHangHdr.getCloaiVthh()) ? null : mapVthh.get(bcanKeHangHdr.getTenCloaiVthh()));
        bcanKeHangHdr.setTenDvi(mapDmucDvi.get(bcanKeHangHdr.getMaDvi()));
        bcanKeHangHdr.setTenDiemKho(mapDmucDvi.get(bcanKeHangHdr.getMaDiemKho()));
        bcanKeHangHdr.setTenNhaKho(mapDmucDvi.get(bcanKeHangHdr.getMaNhaKho()));
        bcanKeHangHdr.setTenNganKho(mapDmucDvi.get(bcanKeHangHdr.getMaNganKho()));
        bcanKeHangHdr.setTenLoKho(mapDmucDvi.get(bcanKeHangHdr.getMaLoKho()));
        bcanKeHangHdr.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(bcanKeHangHdr.getTrangThai()));
        List<HhBcanKeHangDtl> listDtl = hhBcanKeHangDtlRepository.findAllByIdHdr(bcanKeHangHdr.getId());
        bcanKeHangHdr.setHhBcanKeHangDtlList(listDtl);
        return bcanKeHangHdr;
    }

    @Transactional()
    public void delete(Long id) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request. ");
        }
        Optional<HhBcanKeHangHdr> optional = hhBcanKeHangHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception(" Bảng cân kê hàng không tồn tại ");
        }
        HhBcanKeHangHdr bcanKeHangHdr = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(bcanKeHangHdr.getTrangThai())){
            throw new Exception(" Không thể xóa khi quyết định đã được phê duyệt");
        }

        List<HhBcanKeHangDtl> hhBcanKeHangDtlList = hhBcanKeHangDtlRepository.findAllByIdHdr(bcanKeHangHdr.getId());
        if (!CollectionUtils.isEmpty(hhBcanKeHangDtlList)){
            hhBcanKeHangDtlRepository.deleteAll(hhBcanKeHangDtlList);
        }

        hhBcanKeHangHdrRepository.delete(bcanKeHangHdr);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request .");
        }
        if (StringUtils.isEmpty(idSearchReq.getIdList())){
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        List<HhBcanKeHangHdr> list = hhBcanKeHangHdrRepository.findByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (HhBcanKeHangHdr hdr : list){
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                  && !hdr.getTrangThai().equals(Contains.TUCHOI_LDCC)){
                throw new Exception("Chỉ thực hiện xóa quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        hhBcanKeHangDtlRepository.deleteAllByIdHdrIn(idSearchReq.getIdList());
        hhBcanKeHangHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }


    public HhBcanKeHangHdr approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }
        if (StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        Optional<HhBcanKeHangHdr> optional = hhBcanKeHangHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        HhBcanKeHangHdr hdr = optional.get();
        String status = statusReq.getTrangThai() + hdr.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
               hdr.setNguoiGuiDuyet(getUser().getUsername());
               hdr.setNgayGuiDuyet(getDateTimeNow());
               break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
               hdr.setNguoiPduyet(getUser().getUsername());
               hdr.setNgayPduyet(getDateTimeNow());
               hdr.setLyDoTuChoi(statusReq.getLyDoTuChoi());
               break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                hdr.setNguoiPduyet(getUser().getUsername());
                hdr.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(statusReq.getTrangThai());
        HhBcanKeHangHdr created = hhBcanKeHangHdrRepository.save(hdr);
        return created;
    }

    public void export(SearchHhBcanKeHangReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhBcanKeHangHdr> page = this.searchPage(objReq);
        List<HhBcanKeHangHdr> data = page.getContent();
        String title = "Danh sách bảng cân kê hàng";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ NH", "Năm kế hoạch", "Thời hạn NH trước ngày", "Số bảng kê", "Số phiếu nhập kho", "Ngày nhập kho", "Điểm kho", "Nhà kho", "Ngăn kho", "Lô kho", "Trạng thái"};
        String fileName="danh-sach-bang-can-ke-hang.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i<data.size(); i++){
            HhBcanKeHangHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQuyetDinhNhap();
            objs[2] = hdr.getNamKh();
            objs[3] = hdr.getThoiGianGiaoNhan();
            objs[4] = hdr.getSoBangKeCanHang();
            objs[5] = hdr.getSoPhieuNhapKho();
            objs[6] = hdr.getNgayNkho();
            objs[7] = hdr.getTenDiemKho();
            objs[8] = hdr.getTenNhaKho();
            objs[9] = hdr.getTenNganKho();
            objs[10] = hdr.getTenLoKho();
            objs[11] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel exportExcel = new ExportExcel(title,fileName,rowsName,dataList,response);
        exportExcel.export();
    }

    public ReportTemplateResponse preview(HhBcanKeHangHdrReq req) throws Exception {
        HhBcanKeHangHdr bcanKeHangHdr = detail(req.getId());
        if (bcanKeHangHdr == null) {
            throw new Exception("Bảng kê cân hàng không tồn tại.");
        }
        String tongSlDaTruBaoBiBc = NumberToWord.convert(bcanKeHangHdr.getTongSlDaTruBaoBi());
        bcanKeHangHdr.setTongSlDaTruBaoBiBc(tongSlDaTruBaoBiBc);

        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, bcanKeHangHdr);
    }
}
