package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBbanNghiemThuDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBienBanNghiemThuRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.KhCnCongTrinhNghienCuuReq;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.KhCnNghiemThuThanhLyReq;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.KhCnTienDoThucHienReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhBbanNghiemThuDtlReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhBienBanNghiemThuReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhBbNghiemThu;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnCongTrinhNghienCuu;
import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnNghiemThuThanhLy;
import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnTienDoThucHien;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBbanNghiemThuDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanNghiemThu;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhBienBanNghiemThuService extends BaseServiceImpl {

    @Autowired
    HhBienBanNghiemThuRepository hhBienBanNghiemThuRepository;

    @Autowired
    HhBbanNghiemThuDtlRepository hhBbanNghiemThuDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<HhBienBanNghiemThu> searchPage(SearchHhBbNghiemThu objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable= PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhBienBanNghiemThu> data =hhBienBanNghiemThuRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQd(),
                objReq.getSoBb(),
                Contains.convertDateToString(objReq.getNgayKnTu()),
                Contains.convertDateToString(objReq.getNgayKnDen()),
                objReq.getTrangThai(),
                objReq.getMaDvi(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null, null,"01");

        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
        });

        return data;
    }

    @Transactional
    public HhBienBanNghiemThu save(HhBienBanNghiemThuReq objReq)throws Exception{
        UserInfo userInfo =SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhBienBanNghiemThu> optional =hhBienBanNghiemThuRepository.findAllBySoBb(objReq.getSoBb());
        if(optional.isPresent()){
            throw new Exception("Số biên bản đã tồn tại");
        }
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        HhBienBanNghiemThu data = new ModelMapper().map(objReq,HhBienBanNghiemThu.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        HhBienBanNghiemThu created= hhBienBanNghiemThuRepository.save(data);
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(),"HH_BIEN_BAN_NGHIEM_THU");
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_BIEN_BAN_NGHIEM_THU");
        created.setFileDinhKems(fileDinhKems);
        this.saveCtiet(data,objReq);
        return created;
    }

    public void saveCtiet(HhBienBanNghiemThu data, HhBienBanNghiemThuReq objReq){
        for (HhBbanNghiemThuDtlReq dtlReq:objReq.getDviChuDongThucHien()){
            HhBbanNghiemThuDtl dtl = ObjectMapperUtils.map(dtlReq,HhBbanNghiemThuDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            dtl.setType(Contains.CHU_DONG);
            BigDecimal thanhTienTn = dtl.getDonGiaTn().multiply(dtl.getSoLuongTn());
            BigDecimal thanhTienQt = dtl.getDonGiaQt().multiply(dtl.getSoLuongQt());
            dtl.setThanhTienTn(thanhTienTn);
            dtl.setDonGiaQt(thanhTienQt);
            hhBbanNghiemThuDtlRepository.save(dtl);
        }
        for (HhBbanNghiemThuDtlReq dtlReq:objReq.getDmTongCucPdTruocThucHien()){
            HhBbanNghiemThuDtl dtl = ObjectMapperUtils.map(dtlReq,HhBbanNghiemThuDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            dtl.setType(Contains.PHE_DUYET_TRUOC);
            BigDecimal thanhTienTn = dtl.getDonGiaTn().multiply(dtl.getSoLuongTn());
            BigDecimal thanhTienQt = dtl.getDonGiaQt().multiply(dtl.getSoLuongQt());
            dtl.setThanhTienTn(thanhTienTn);
            dtl.setDonGiaQt(thanhTienQt);
            hhBbanNghiemThuDtlRepository.save(dtl);
        }
    }
    @Transactional
    public HhBienBanNghiemThu update(HhBienBanNghiemThuReq objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhBienBanNghiemThu> optional = hhBienBanNghiemThuRepository.findById(objReq.getId());

        Optional<HhBienBanNghiemThu> soBb = hhBienBanNghiemThuRepository.findAllBySoBb(objReq.getSoBb());
        if(soBb.isPresent()){
            if (soBb.isPresent()){
                if (!soBb.get().getId().equals(objReq.getId())){
                    throw new Exception("số biên bản đã tồn tại");
                }
            }
        }
        HhBienBanNghiemThu data= optional.get();
        HhBienBanNghiemThu dataMap = new ModelMapper().map(objReq,HhBienBanNghiemThu.class);
        updateObjectToObject(data,dataMap);
        data.setNguoiSua(userInfo.getUsername());
        data.setNgaySua(new Date());
        HhBienBanNghiemThu created= hhBienBanNghiemThuRepository.save(data);
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(),"HH_BIEN_BAN_NGHIEM_THU");
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_BIEN_BAN_NGHIEM_THU");
        created.setFileDinhKems(fileDinhKems);
        List<HhBbanNghiemThuDtl> listDtl =hhBbanNghiemThuDtlRepository.findAllByIdHdr(data.getId());
        hhBbanNghiemThuDtlRepository.deleteAll(listDtl);
        this.saveCtiet(data,objReq);
        return created;
    }

    public HhBienBanNghiemThu detail(String ids) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhBienBanNghiemThu> optional=hhBienBanNghiemThuRepository.findById(Long.valueOf(ids));

        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        HhBienBanNghiemThu data = optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : hashMapDmdv.get(userInfo.getTenDvi()));
        data.setTenDiemKho(StringUtils.isEmpty(data.getMaDiemKho()) ? null : hashMapDmdv.get(data.getMaDiemKho()));
        data.setTenNhaKho(StringUtils.isEmpty(data.getMaNhaKho()) ? null : hashMapDmdv.get(data.getMaNhaKho()));
        data.setTenNganKho(StringUtils.isEmpty(data.getMaNganKho()) ? null : hashMapDmdv.get(data.getMaNganKho()));
        data.setTenLoKho(StringUtils.isEmpty(data.getMaLoKho()) ? null : hashMapDmdv.get(data.getMaLoKho()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList("HH_BIEN_BAN_NGHIEM_THU"));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        List<FileDinhKem> fileDinhkems = fileDinhKemService.search(data.getId(), Arrays.asList("HH_BIEN_BAN_NGHIEM_THU"));
        if (!DataUtils.isNullOrEmpty(fileDinhkems)) {
            data.setFileDinhKems(fileDinhkems);
        }
        List<HhBbanNghiemThuDtl> listDtl = hhBbanNghiemThuDtlRepository.findAllByIdHdr(data.getId());
        data.setDviChuDongThucHien(listDtl.stream().filter(item -> item.getType().equals(Contains.CHU_DONG)).collect(Collectors.toList()));
        data.setDmTongCucPdTruocThucHien(listDtl.stream().filter(item -> item.getType().equals(Contains.PHE_DUYET_TRUOC)).collect(Collectors.toList()));
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<HhBienBanNghiemThu> optional= hhBienBanNghiemThuRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_TK)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_KT)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDCC)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        HhBienBanNghiemThu data = optional.get();
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("HH_BIEN_BAN_NGHIEM_THU"));
        List<HhBbanNghiemThuDtl>  listDtl = hhBbanNghiemThuDtlRepository.findAllByIdHdr(data.getId());
        hhBbanNghiemThuDtlRepository.deleteAll(listDtl);
        hhBienBanNghiemThuRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhBienBanNghiemThu> list= hhBienBanNghiemThuRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhBienBanNghiemThu HhBienBanNghiemThu : list ){
            if (!HhBienBanNghiemThu.getTrangThai().equals(Contains.DUTHAO)
                    && !HhBienBanNghiemThu.getTrangThai().equals(Contains.TUCHOI_TK)
                    && !HhBienBanNghiemThu.getTrangThai().equals(Contains.TUCHOI_KT)
                    && !HhBienBanNghiemThu.getTrangThai().equals(Contains.TUCHOI_LDCC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listIdBb=list.stream().map(HhBienBanNghiemThu::getId).collect(Collectors.toList());
        List<HhBbanNghiemThuDtl> listDtl = hhBbanNghiemThuDtlRepository.findAllByIdHdrIn(listIdBb);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),  Lists.newArrayList("HH_BIEN_BAN_NGHIEM_THU"));
        hhBbanNghiemThuDtlRepository.deleteAll(listDtl);
        hhBienBanNghiemThuRepository.deleteAll(list);
    }

    public void export(SearchHhBbNghiemThu objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhBienBanNghiemThu> page=this.searchPage(objReq);
        List<HhBienBanNghiemThu> data=page.getContent();

        String title="Danh sách quyết biên bản nghiệm thu bảo quản lần đầu nhập hàng dự trữ quốc gia";
        String[] rowsName=new String[]{"STT","Số biên bản","Số quyết định nhập","Ngày nghiệm thu","Điểm kho","Nhà kho","Ngăn Kho","Lô Kho","Chi phí thực hiện trong năm","Chi phí thực hiện năm trước","Tổng giá trị","Trạng Thái"};
        String fileName="danh-sach-bien-ban-nghiem-thu.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhBienBanNghiemThu dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getSoBb();
            objs[2]=dx.getSoQdGiaoNvNh();
            objs[3]=dx.getNgayNghiemThu();
            objs[4]=dx.getTenDiemKho();
            objs[5]=dx.getTenNhaKho();
            objs[6]=dx.getTenNganKho();
            objs[7]=dx.getTenLoKho();
            objs[8]=dx.getNgayNghiemThu();
            for (HhBbanNghiemThuDtl dtl : dx.getDviChuDongThucHien()){
                objs[9]=dtl.getTongGtri();
            }
            for (HhBbanNghiemThuDtl dtl : dx.getDviChuDongThucHien()){
                objs[10]=dtl.getTongGtri();
            }
            objs[11]=dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public HhBienBanNghiemThu approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhBienBanNghiemThu> optional =hhBienBanNghiemThuRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.CHODUYET_TK + Contains.DUTHAO:
            case Contains.CHODUYET_KT + Contains.CHODUYET_TK:
            case Contains.CHODUYET_LDCC + Contains.CHODUYET_KT:
            case Contains.CHODUYET_TK + Contains.TUCHOI_TK:
            case Contains.CHODUYET_TK + Contains.TUCHOI_KT:
            case Contains.CHODUYET_TK + Contains.TUCHOI_LDCC:
                optional.get().setNguoiGuiDuyet(userInfo.getUsername());
                optional.get().setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_TK + Contains.CHODUYET_TK:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLdoTuchoi(statusReq.getLyDo());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhBienBanNghiemThu created = hhBienBanNghiemThuRepository.save(optional.get());
        return created;
    }
}
