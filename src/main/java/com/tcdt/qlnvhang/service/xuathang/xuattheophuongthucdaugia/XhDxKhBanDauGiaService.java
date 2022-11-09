package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhDxKhBdgDdGiaoNhanHangRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.SearchXhDxKhBanDauGia;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhDxKhBdgDdGiaoNhanHangReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBdgDdGiaoNhanHang;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhDxKhBanDauGiaService extends BaseServiceImpl {
    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    @Autowired
    private XhDxKhBanDauGiaDtlRepository xhDxKhBanDauGiaDtlRepository;

    @Autowired
    private XhDxKhBdgDdGiaoNhanHangRepository xhDxKhBdgDdGiaoNhanHangRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    public Page<XhDxKhBanDauGia> searchPage(SearchXhDxKhBanDauGia objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhDxKhBanDauGia> data = xhDxKhBanDauGiaRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoDxuat(),
                Contains.convertDateToString(objReq.getNgayTaoTu()),
                Contains.convertDateToString(objReq.getNgayTaoDen()),
                Contains.convertDateToString(objReq.getNgayDuyetTu()),
                Contains.convertDateToString(objReq.getNgayDuyetDen()),
                objReq.getTrichYeu(),
                objReq.getLoaiVthh(),
                objReq.getTrangThai(),
                objReq.getTrangThaiTh(),
                userInfo.getDvql(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();

        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapDmhh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Transactional
    public XhDxKhBanDauGia save(XhDxKhBanDauGiaReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findBySoDxuat(objReq.getSoDxuat());
        if(optional.isPresent()){
            throw new Exception("số đề xuất đã tồn tại");
        }
        XhDxKhBanDauGia data = new ModelMapper().map(objReq,XhDxKhBanDauGia.class);
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiTh(Contains.CHUATONGHOP);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setMaDvi(userInfo.getDvql());
        this.validateData(data, data.getTrangThai());
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        XhDxKhBanDauGia created=xhDxKhBanDauGiaRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_DX_KHMTT_HDR");
        created.setFileDinhKems(fileDinhKems);
        for (XhDxKhBanDauGiaDtlReq banDauGiaDtlReq : objReq.getBanDauGiaDtlList()){
            XhDxKhBanDauGiaDtl banDauGiaDtl = ObjectMapperUtils.map(banDauGiaDtlReq, XhDxKhBanDauGiaDtl.class);
            banDauGiaDtl.setIdHdr(data.getId());
            xhDxKhBanDauGiaDtlRepository.save(banDauGiaDtl);
        }
        for (XhDxKhBdgDdGiaoNhanHangReq ddGiaoNhanHangReq : objReq.getDdGiaoNhanHangReqList()){
            XhDxKhBdgDdGiaoNhanHang ddGiaoNhanHang  =ObjectMapperUtils.map(ddGiaoNhanHangReq,XhDxKhBdgDdGiaoNhanHang.class);
            ddGiaoNhanHang.setIdHdr(data.getId());
            xhDxKhBdgDdGiaoNhanHangRepository.save(ddGiaoNhanHang);
        }
        return created;
    }

    @Transactional
    public XhDxKhBanDauGia update(XhDxKhBanDauGiaReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(objReq.getId());

        Optional<XhDxKhBanDauGia> soDxuat = xhDxKhBanDauGiaRepository.findBySoDxuat(objReq.getSoDxuat());
        if (soDxuat.isPresent()){
            if (!soDxuat.get().getId().equals(objReq.getId())){
                throw new Exception("số đề xuất đã tồn tại");
            }
        }
        XhDxKhBanDauGia data = optional.get();
        XhDxKhBanDauGia dataMap = new ModelMapper().map(objReq,XhDxKhBanDauGia.class);
        updateObjectToObject(data,dataMap);
        XhDxKhBanDauGia created=xhDxKhBanDauGiaRepository.save(data);
        List<XhDxKhBanDauGiaDtl> xhDxKhBanDauGiaDtls = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(objReq.getId());
        xhDxKhBanDauGiaDtlRepository.deleteAll(xhDxKhBanDauGiaDtls);
        for (XhDxKhBanDauGiaDtlReq banDauGiaDtlReq : objReq.getBanDauGiaDtlList()){
            XhDxKhBanDauGiaDtl banDauGiaDtl = ObjectMapperUtils.map(banDauGiaDtlReq, XhDxKhBanDauGiaDtl.class);
            banDauGiaDtl.setId(null);
            banDauGiaDtl.setIdHdr(data.getId());
            xhDxKhBanDauGiaDtlRepository.save(banDauGiaDtl);
        }
        List<XhDxKhBdgDdGiaoNhanHang> bdgDdGiaoNhanHangs= xhDxKhBdgDdGiaoNhanHangRepository.findAllByIdHdr(objReq.getId());
        xhDxKhBdgDdGiaoNhanHangRepository.deleteAll(bdgDdGiaoNhanHangs);
        for (XhDxKhBdgDdGiaoNhanHangReq ddGiaoNhanHangReq : objReq.getDdGiaoNhanHangReqList()){
            XhDxKhBdgDdGiaoNhanHang ddGiaoNhanHang  =ObjectMapperUtils.map(ddGiaoNhanHangReq,XhDxKhBdgDdGiaoNhanHang.class);
            ddGiaoNhanHang.setId(null);
            ddGiaoNhanHang.setIdHdr(data.getId());
            xhDxKhBdgDdGiaoNhanHangRepository.save(ddGiaoNhanHang);
        }
        return created;
    }

    public XhDxKhBanDauGia detail(String ids) throws Exception{
        Optional<XhDxKhBanDauGia> optional=xhDxKhBanDauGiaRepository.findById(Long.valueOf(ids));

        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        XhDxKhBanDauGia data = optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiTh()));
        List<FileDinhKem> fdk=fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton("HH_DX_KHMTT_HDR"));
        data.setFileDinhKems(fdk);
        List<XhDxKhBanDauGiaDtl> banDauGiaDtlList = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(data.getId());
        for (XhDxKhBanDauGiaDtl banDauGiaDtl : banDauGiaDtlList){
            banDauGiaDtl.setTenDvi(StringUtils.isEmpty(banDauGiaDtl.getMaDvi())? null:hashMapDmdv.get(banDauGiaDtl.getMaDvi()));
            banDauGiaDtl.setTenDiemKho(StringUtils.isEmpty(banDauGiaDtl.getMaDiemKho()) ? null : hashMapDmdv.get(banDauGiaDtl.getMaDiemKho()));
            banDauGiaDtl.setTenDiemKho(StringUtils.isEmpty(banDauGiaDtl.getMaDiemKho()) ? null : hashMapDmdv.get(banDauGiaDtl.getMaDiemKho()));
            banDauGiaDtl.setTenDiemKho(StringUtils.isEmpty(banDauGiaDtl.getMaDiemKho()) ? null : hashMapDmdv.get(banDauGiaDtl.getMaDiemKho()));
        }
        data.setBanDauGiaDtlList(banDauGiaDtlList);
        List<XhDxKhBdgDdGiaoNhanHang> ddGiaoNhanHangList = xhDxKhBdgDdGiaoNhanHangRepository.findAllByIdHdr(data.getId());
        for (XhDxKhBdgDdGiaoNhanHang ddGiaoNhanHang : ddGiaoNhanHangList){
            ddGiaoNhanHang.setTenDvi(StringUtils.isEmpty(ddGiaoNhanHang.getMaDvi())? null:hashMapDmdv.get(ddGiaoNhanHang.getMaDvi()));
        }
        data.setDdGiaoNhanHangList(ddGiaoNhanHangList);
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<XhDxKhBanDauGia> optional= xhDxKhBanDauGiaRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_TP)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        XhDxKhBanDauGia data = optional.get();
        List<XhDxKhBanDauGiaDtl> banDauGiaDtlList = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(data.getId());
        List<XhDxKhBdgDdGiaoNhanHang>  ddGiaoNhanHangList = xhDxKhBdgDdGiaoNhanHangRepository.findAllByIdHdr(data.getId());
        xhDxKhBanDauGiaDtlRepository.deleteAll(banDauGiaDtlList);
        xhDxKhBdgDdGiaoNhanHangRepository.deleteAll(ddGiaoNhanHangList);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("HH_DX_KHMTT_HDR"));
        xhDxKhBanDauGiaRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<XhDxKhBanDauGia> list= xhDxKhBanDauGiaRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (XhDxKhBanDauGia dxuatKhMttHdr : list){
            if (!dxuatKhMttHdr.getTrangThai().equals(Contains.DUTHAO)
                    && !dxuatKhMttHdr.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !dxuatKhMttHdr.getTrangThai().equals(Contains.TUCHOI_LDC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listId=list.stream().map(XhDxKhBanDauGia::getId).collect(Collectors.toList());
        List<XhDxKhBanDauGiaDtl> banDauGiaDtlList = xhDxKhBanDauGiaDtlRepository.findAllByIdHdrIn(listId);
        List<XhDxKhBdgDdGiaoNhanHang>  bdgDdGiaoNhanHangList = xhDxKhBdgDdGiaoNhanHangRepository.findAllByIdHdrIn(listId);
        xhDxKhBanDauGiaDtlRepository.deleteAll(banDauGiaDtlList);
        xhDxKhBdgDdGiaoNhanHangRepository.deleteAll(bdgDdGiaoNhanHangList);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),  Lists.newArrayList("HH_DX_KHMTT_HDR"));
        xhDxKhBanDauGiaRepository.deleteAll(list);
    }

    public  void export(SearchXhDxKhBanDauGia objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhDxKhBanDauGia> page=this.searchPage(objReq);
        List<XhDxKhBanDauGia> data=page.getContent();

        String title="Danh sách đề xuất kế hoạch mua trực tiếp";
        String[] rowsName=new String[]{"STT","Năm KH","Số KH/tờ trình","Ngày lập KH","Ngày duyệt KH","Ngày ký","Trích yếu","Loại hàng hóa","Chủng loại hàng hóa","Số DV tài sản","SL HĐ đã ký","Số QĐ giao chỉ tiêu","Số QĐ duyệt KH bán DG","Trạng thái"};
        String fileName="danh-sach-dx-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            XhDxKhBanDauGia dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getNamKh();
            objs[2]=dx.getSoDxuat();
            objs[3]=dx.getNgayTao();
            objs[4]=dx.getNgayPduyet();
            objs[5]=dx.getNgayKy();
            objs[6]=dx.getTrichYeu();
            objs[7]=dx.getTenLoaiVthh();
            objs[8]=dx.getTenCloaiVthh();
            objs[9]=dx.getSoDviTsan();
            objs[10]=dx.getSlHdDaKy();
            objs[11]=dx.getSoQdCtieu();
            objs[12]=dx.getSoQdPd();
            objs[13]=dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public XhDxKhBanDauGia approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhDxKhBanDauGia> optional =xhDxKhBanDauGiaRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.CHO_DUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_LDC:
                this.validateData(optional.get(),Contains.CHODUYET_TP);
                optional.get().setNguoiGduyetId(userInfo.getUsername());
                optional.get().setNgayGduyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_TP + Contains.CHO_DUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyetId(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLdoTuChoi(statusReq.getLyDo());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                this.validateData(optional.get(),statusReq.getTrangThai());
                optional.get().setNguoiPduyetId(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhDxKhBanDauGia created = xhDxKhBanDauGiaRepository.save(optional.get());
        return created;
    }

    public void validateData(XhDxKhBanDauGia objHdr,String trangThai) throws Exception {
        if(trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())){
            XhDxKhBanDauGia dXuat = xhDxKhBanDauGiaRepository.findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(objHdr.getLoaiVthh(), objHdr.getCloaiVthh(), objHdr.getNamKh(), objHdr.getMaDvi(),NhapXuatHangTrangThaiEnum.DUTHAO.getId());
            if(!ObjectUtils.isEmpty(dXuat) && !dXuat.getId().equals(objHdr.getId())){
                throw new Exception("Chủng loại hàng hóa đã được tạo và gửi duyệt, xin vui lòng chọn lại chủng loại hàng hóa khác");
            }
        }
    }
}
