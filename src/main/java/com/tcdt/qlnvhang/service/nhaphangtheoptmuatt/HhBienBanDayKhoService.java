package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.Operator;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBienBanDayKhoDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBienBanDayKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanDayKhoDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanDayKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.docx4j.wml.P;
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
import java.util.*;

@Service
public class HhBienBanDayKhoService extends BaseServiceImpl {

    @Autowired
    private HhBienBanDayKhoHdrRepository hhBienBanDayKhoHdrRepository;

    @Autowired
    private HhBienBanDayKhoDtlRepository hhBienBanDayKhoDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    public Page<HhBienBanDayKhoHdr> searchPage(SearchHhBienBanDayKhoReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhBienBanDayKhoHdr> data = hhBienBanDayKhoHdrRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQuyetDinhNhap(),
                objReq.getSoBbNhapDayKho(),
                convertDateToString(objReq.getNgayBdauNhapTu()),
                convertDateToString(objReq.getNgayBdauNhapDen()),
                convertDateToString(objReq.getNgayKthucNhapTu()),
                convertDateToString(objReq.getNgayKthucNhapDen()),
                convertDateToString(objReq.getNgayLapBbanTu()),
                convertDateToString(objReq.getNgayLapBbanDen()),
                objReq.getTrangThai(),
                userInfo.getDvql(),
                pageable
        );
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(item ->{
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
            item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
            item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
            item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
            item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
        });
        return data;
    }


    @Transactional()
    public HhBienBanDayKhoHdr create(HhBienBanDayKhoHdrReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad requesr.");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        HhBienBanDayKhoHdr data = new HhBienBanDayKhoHdr();
        BeanUtils.copyProperties(objReq, data, "id");
        data.setNgayTao(getDateTimeNow());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : mapDmucDvi.get(userInfo.getDvql()));
        data.setId(Long.valueOf(objReq.getSoBbNhapDayKho().split("/")[0]));
        HhBienBanDayKhoHdr created = hhBienBanDayKhoHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),HhBienBanDayKhoHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        this.saveCtiet(data.getId(), objReq);
        return created;
    }

    @Transactional()
    void saveCtiet(Long idHdr , HhBienBanDayKhoHdrReq objReq){
        hhBienBanDayKhoDtlRepository.deleteByIdHdr(idHdr);
        for( HhBienBanDayKhoDtlReq bienBanDayKhoDtlReq : objReq.getHhBienBanDayKhoDtlReqList()){
            HhBienBanDayKhoDtl bienBanDayKhoDtl = new HhBienBanDayKhoDtl();
            BeanUtils.copyProperties(bienBanDayKhoDtlReq,bienBanDayKhoDtl,"id");
            bienBanDayKhoDtl.setIdHdr(idHdr);
            hhBienBanDayKhoDtlRepository.save(bienBanDayKhoDtl);
        }
    }


    @Transactional
    public HhBienBanDayKhoHdr update(HhBienBanDayKhoHdrReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        Optional<HhBienBanDayKhoHdr> optional = hhBienBanDayKhoHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại. ");
        }
        HhBienBanDayKhoHdr bienBanDayKhoHdr = optional.get();
        BeanUtils.copyProperties(objReq, bienBanDayKhoHdr, "id");
        bienBanDayKhoHdr.setNgaySua(getDateTimeNow());
        bienBanDayKhoHdr.setNguoiSua(userInfo.getUsername());
        HhBienBanDayKhoHdr createCheck = hhBienBanDayKhoHdrRepository.save(bienBanDayKhoHdr);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), bienBanDayKhoHdr.getId(), HhBienBanDayKhoHdr.TABLE_NAME);
        createCheck.setFileDinhKems(fileDinhKems);
        this.saveCtiet(bienBanDayKhoHdr.getId(), objReq);
        return createCheck;
    }


    public HhBienBanDayKhoHdr detail(Long ids) throws Exception{
        Optional<HhBienBanDayKhoHdr> qOptional = hhBienBanDayKhoHdrRepository.findById(ids);
        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Bản ghi không tồn tại. ");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        HhBienBanDayKhoHdr bienBanDayKhoHdr = qOptional.get();
        bienBanDayKhoHdr.setTenDvi(mapDmucDvi.get(bienBanDayKhoHdr.getMaDvi()));
        bienBanDayKhoHdr.setTenDiemKho(mapDmucDvi.get(bienBanDayKhoHdr.getMaDiemKho()));
        bienBanDayKhoHdr.setTenNhaKho(mapDmucDvi.get(bienBanDayKhoHdr.getMaNhaKho()));
        bienBanDayKhoHdr.setTenNganKho(mapDmucDvi.get(bienBanDayKhoHdr.getMaNganKho()));
        bienBanDayKhoHdr.setTenLoKho(mapDmucDvi.get(bienBanDayKhoHdr.getMaLoKho()));
        bienBanDayKhoHdr.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(bienBanDayKhoHdr.getTrangThai()));
        bienBanDayKhoHdr.setFileDinhKems(fileDinhKemService.search(bienBanDayKhoHdr.getId(), Collections.singleton(HhBienBanDayKhoHdr.TABLE_NAME)));
        List<HhBienBanDayKhoDtl> ctList = hhBienBanDayKhoDtlRepository.findAllByIdHdr(bienBanDayKhoHdr.getId());
        bienBanDayKhoHdr.setHhBienBanDayKhoDtlList(ctList);
        return bienBanDayKhoHdr;
    }

    @Transactional()
    public void delete(Long id) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad requesr.");
        }

        Optional<HhBienBanDayKhoHdr> optional = hhBienBanDayKhoHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Biên bản nhập đầy kho không tồn tại");
        }

        HhBienBanDayKhoHdr bienBanDayKhoHdr = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(bienBanDayKhoHdr.getTrangThai())){
            throw new Exception("Không thể xóa khi quyết định đã được duyệt");
        }

        List<HhBienBanDayKhoDtl> bienBanDayKhoDtls = hhBienBanDayKhoDtlRepository.findAllByIdHdr(bienBanDayKhoHdr.getId());
        if (!CollectionUtils.isEmpty(bienBanDayKhoDtls)){
            hhBienBanDayKhoDtlRepository.deleteAll(bienBanDayKhoDtls);
        }

        hhBienBanDayKhoHdrRepository.delete(bienBanDayKhoHdr);
        fileDinhKemService.delete(bienBanDayKhoHdr.getId(), Collections.singleton(HhBienBanDayKhoHdr.TABLE_NAME));
    }


    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(idSearchReq.getIdList())){
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }

        List<HhBienBanDayKhoHdr> list = hhBienBanDayKhoHdrRepository.findByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (HhBienBanDayKhoHdr hdr : list){
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                   && !hdr.getTrangThai().equals(Contains.TUCHOI_LDCC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }

        hhBienBanDayKhoDtlRepository.deleteAllByIdHdrIn(idSearchReq.getIdList());
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(HhBienBanDayKhoHdr.TABLE_NAME));
        hhBienBanDayKhoHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

    public HhBienBanDayKhoHdr approve (StatusReq statusReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();

        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<HhBienBanDayKhoHdr> optional = hhBienBanDayKhoHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status){
            case Contains.CHODUYET_KTVBQ + Contains.DUTHAO:
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
                optional.get().setNguoiGuiDuyet(getUser().getUsername());
                optional.get().setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhBienBanDayKhoHdr created = hhBienBanDayKhoHdrRepository.save(optional.get());
        return created;
    }

    public void export (SearchHhBienBanDayKhoReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhBienBanDayKhoHdr> page = this.searchPage(objReq);
        List<HhBienBanDayKhoHdr> data = page.getContent();
        String title = "Danh sách biên bản nhập đầy kho";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NN NH", "Năm Kh", "Thời gian Nh trước ngày", "Số BB NĐK", "Ngày bắt đầu nhập", "Ngày kết thúc nhập", "Số phiếu NK", "Số bảng kê", "Ngày nhập kho", "Điểm kho","Nhà kho", "Ngăn kho", "Lô kho", "Trạng thái"};
        String fileName="danh-sach-bien-ban-nhap-day-kho.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhBienBanDayKhoHdr hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQuyetDinhNhap();
            objs[2] = hdr.getNamKh();
            objs[3] = hdr.getNgayNkho();
            objs[4] = hdr.getSoBbNhapDayKho();
            objs[5] = hdr.getNgayBdauNhap();
            objs[6] = hdr.getNgayKthucNhap();
            objs[7] = hdr.getSoPhieuNhapKho();
            objs[8] = hdr.getSoBangKeCanHang();
            objs[9] = hdr.getNgayNkho();
            objs[10] = hdr.getTenDiemKho();
            objs[11] = hdr.getTenNhaKho();
            objs[12] = hdr.getTenNganKho();
            objs[13] = hdr.getTenLoKho();
            objs[14] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel exportExcel = new ExportExcel(title,fileName,rowsName,dataList,response);
        exportExcel.export();
    }
}