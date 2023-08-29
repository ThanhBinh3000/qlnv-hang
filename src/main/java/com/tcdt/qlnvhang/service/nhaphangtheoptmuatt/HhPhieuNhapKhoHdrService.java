package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBcanKeHangHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhBienBanLayMauPreview;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhPhieuNhapKhoHdrPreview;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhBienBanLayMauReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhieuNhapKhoCtReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBcanKeHangHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanLayMau;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.*;

@Service
public class HhPhieuNhapKhoHdrService  extends BaseServiceImpl {

    @Autowired
    private HhPhieuNhapKhoHdrRepository hhPhieuNhapKhoHdrRepository;

    @Autowired
    private HhPhieuNhapKhoCtRepository hhPhieuNhapKhoCtRepository;

    @Autowired
    private HhBcanKeHangHdrRepository hhBcanKeHangHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<HhPhieuNhapKhoHdr> searchPage(SearchHhPhieuNhapKhoReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhPhieuNhapKhoHdr> data = hhPhieuNhapKhoHdrRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQuyetDinhNhap(),
                objReq.getSoPhieuNhapKho(),
                convertDateToString(objReq.getNgayNhapKhoTu()),
                convertDateToString(objReq.getNgayNhapKhoDen()),
                objReq.getTrangThai(),
                userInfo.getDvql(),
                pageable
        );
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        data.getContent().forEach(item ->{
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            item.setTenLoaiVthh(hashMapDmhh.get(item.getLoaiVthh()));
            item.setTenCloaiVthh(hashMapDmhh.get(item.getCloaiVthh()));
            item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
            item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
            item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
            item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
            item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
            data.getContent().forEach(f->{
                List<HhPhieuNhapKhoCt> hhPhieuNhapKhoCt = hhPhieuNhapKhoCtRepository.findAllByIdHdr(item.getId());
                f.setHhPhieuNhapKhoCtList(hhPhieuNhapKhoCt);
            });
        });
        return data;
    }

    @Transactional()
    public HhPhieuNhapKhoHdr create(HhPhieuNhapKhoHdrReq objReq) throws Exception{
        UserInfo userInfo =SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        HhPhieuNhapKhoHdr data = new HhPhieuNhapKhoHdr();
        BeanUtils.copyProperties(objReq, data, "id");
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setId(Long.valueOf(data.getSoPhieuNhapKho().split("/")[0]));
        HhPhieuNhapKhoHdr created = hhPhieuNhapKhoHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_PHIEU_NHAP_KHO_HDR");
        created.setFileDinhKems(fileDinhKems);
        this.saveCtiet(data.getId(), objReq);
        return created;
    }

    @Transactional()
    void saveCtiet(Long idHdr , HhPhieuNhapKhoHdrReq objReq){
        hhPhieuNhapKhoCtRepository.deleteByIdHdr(idHdr);
            for(HhPhieuNhapKhoCtReq nhapKhoCtReq : objReq.getPhieuNhapKhoCtList()){
                HhPhieuNhapKhoCt nhapKhoCt = new HhPhieuNhapKhoCt();
                BeanUtils.copyProperties(nhapKhoCtReq,nhapKhoCt,"id");
                nhapKhoCt.setIdHdr(idHdr);
                hhPhieuNhapKhoCtRepository.save(nhapKhoCt);
            }
    }

    @Transactional()
    public HhPhieuNhapKhoHdr update(HhPhieuNhapKhoHdrReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
                throw new Exception("Bad request.");
            }
        Optional<HhPhieuNhapKhoHdr> optional = hhPhieuNhapKhoHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Phiếu nhập kho không tồn tại.");
        }
        HhPhieuNhapKhoHdr phieuNhapKhoHdr = optional.get();
        BeanUtils.copyProperties(objReq, phieuNhapKhoHdr, "id");
        phieuNhapKhoHdr.setNgaySua(getDateTimeNow());
        phieuNhapKhoHdr.setNguoiSua(userInfo.getUsername());
        HhPhieuNhapKhoHdr  createCheck = hhPhieuNhapKhoHdrRepository.save(phieuNhapKhoHdr);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), phieuNhapKhoHdr.getId(), HhPhieuNhapKhoHdr.TABLE_NAME);
        createCheck.setFileDinhKems(fileDinhKems);
        this.saveCtiet(phieuNhapKhoHdr.getId(), objReq);
        return createCheck;
    }

    public HhPhieuNhapKhoHdr detail(Long ids) throws Exception{
        Optional<HhPhieuNhapKhoHdr> qOptional = hhPhieuNhapKhoHdrRepository.findById(ids);
        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi.");
        }
        Map<String,String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        qOptional.get().setTenLoaiVthh( StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh( StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null :mapVthh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setTenDiemKho(mapDmucDvi.get(qOptional.get().getMaDiemKho()));
        qOptional.get().setTenNhaKho(mapDmucDvi.get(qOptional.get().getMaNhaKho()));
        qOptional.get().setTenNganKho(mapDmucDvi.get(qOptional.get().getMaNganKho()));
        qOptional.get().setTenLoKho(mapDmucDvi.get(qOptional.get().getMaLoKho()));
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
        qOptional.get().setFileDinhKems(fileDinhKemService.search(qOptional.get().getId(), Collections.singleton(HhPhieuNhapKhoHdr.TABLE_NAME)));
        List<HhPhieuNhapKhoCt> Ctlist = hhPhieuNhapKhoCtRepository.findAllByIdHdr(qOptional.get().getId());
        qOptional.get().setHhPhieuNhapKhoCtList(Ctlist);
        HhBcanKeHangHdr bcanKeHangHdr  = hhBcanKeHangHdrRepository.findBySoPhieuNhapKho(qOptional.get().getSoPhieuNhapKho());
        if (!ObjectUtils.isEmpty(bcanKeHangHdr)){
            qOptional.get().setHhBcanKeHangHdr(bcanKeHangHdr);
        }
        return qOptional.get();

    }

    public HhPhieuNhapKhoHdr approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<HhPhieuNhapKhoHdr> optional = hhPhieuNhapKhoHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNguoiGuiDuyet(getUser().getFullName());
                optional.get().setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyet(getUser().getFullName());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyet(getUser().getFullName());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhPhieuNhapKhoHdr created = hhPhieuNhapKhoHdrRepository.save(optional.get());
        return created;
    }

    @Transactional()
    public  void delete(Long id) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhPhieuNhapKhoHdr> optional = hhPhieuNhapKhoHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Phiếu nhập kho không tồn tại");
        }
        HhPhieuNhapKhoHdr nhapKhoHdr = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(nhapKhoHdr.getTrangThai())){
            throw new Exception("Không thể xóa khi quyết định đã được phê duyệt.");
        }
        List<HhPhieuNhapKhoCt> phieuNhapKhoCtList = hhPhieuNhapKhoCtRepository.findAllByIdHdr(nhapKhoHdr.getId());
        if (!CollectionUtils.isEmpty(phieuNhapKhoCtList)){
            hhPhieuNhapKhoCtRepository.deleteAll(phieuNhapKhoCtList);
        }

        hhPhieuNhapKhoHdrRepository.delete(nhapKhoHdr);
        fileDinhKemService.delete(nhapKhoHdr.getId(), Collections.singleton(HhPhieuNhapKhoHdr.TABLE_NAME));
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        if (StringUtils.isEmpty(idSearchReq.getIdList())){
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        List<HhPhieuNhapKhoHdr> list = hhPhieuNhapKhoHdrRepository.findByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }

        for (HhPhieuNhapKhoHdr hdr: list){
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                  && !hdr.getTrangThai().equals(Contains.TUCHOI_LDCC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối.");
            }
        }
        hhPhieuNhapKhoCtRepository.deleteAllByIdHdrIn(idSearchReq.getIdList());
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(HhPhieuNhapKhoHdr.TABLE_NAME));
        hhPhieuNhapKhoHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

    public void export(SearchHhPhieuNhapKhoReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhPhieuNhapKhoHdr> page = this.searchPage(objReq);
        List<HhPhieuNhapKhoHdr> data = page.getContent();
        String title = "Danh sách phiếu nhập kho";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ nhập hàng", "Năm kế hoạch", "Thời hạn Nh trước ngày", "Số phiếu nhập kho", "Ngày nhập kho", "Điểm kho", "Nhà kho", "Ngăn kho", "Lô kho", "Số phiếu KTCL", "Ngày giám định", "Trạng thái"};
        String fileName="danh-sach-phieu-nhap-kho.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhPhieuNhapKhoHdr hdr=data.get(i);
            objs=new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQuyetDinhNhap();
            objs[2] = hdr.getNamKh();
            objs[3] = hdr.getNgayNkho();
            objs[4] = hdr.getSoPhieuNhapKho();
            objs[5] = hdr.getNgayTao();
            objs[6] = hdr.getTenDiemKho();
            objs[7] = hdr.getTenNhaKho();
            objs[8] = hdr.getTenNganKho();
            objs[9] = hdr.getTenLoKho();
            objs[10] = hdr.getSoPhieuKtraCluong();
            objs[11] = hdr.getNgayGdinh();
            objs[12] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public ReportTemplateResponse preview(HhPhieuNhapKhoHdrReq req) throws Exception {
        HhPhieuNhapKhoHdr phieuNhapKhoHdr = detail(req.getId());
        if (phieuNhapKhoHdr == null) {
            throw new Exception("Bản kê nhập vật tư không tồn tại.");
        }
        HhPhieuNhapKhoHdrPreview object = new HhPhieuNhapKhoHdrPreview();
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }
}
