package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaPhanLoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.SearchXhDxKhBanDauGia;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaPhanLoReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaPhanLo;
import com.tcdt.qlnvhang.util.Contains;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhDxKhBanDauGiaService extends BaseServiceImpl {
    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    @Autowired
    private XhDxKhBanDauGiaDtlRepository xhDxKhBanDauGiaDtlRepository;

    @Autowired
     private XhDxKhBanDauGiaPhanLoRepository xhDxKhBanDauGiaPhanLoRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;


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
    public XhDxKhBanDauGia create(XhDxKhBanDauGiaReq objReq) throws Exception {
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        if (!StringUtils.isEmpty(objReq.getSoDxuat())) {
            Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findBySoDxuat(objReq.getSoDxuat());
            if (qOptional.isPresent()) {
                throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
            }
        }

        XhDxKhBanDauGia dataMap = new ModelMapper().map(objReq, XhDxKhBanDauGia.class);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNguoiTaoId(getUser().getUsername());
        dataMap.setTrangThai(Contains.DU_THAO);
        dataMap.setTrangThaiTh(Contains.CHUATONGHOP);
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> hashMapDmdv = getListDanhMucDvi(null, null, "01");
        dataMap.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        dataMap.setMaDvi(userInfo.getDvql());
        dataMap.setTenLoaiVthh(StringUtils.isEmpty(dataMap.getLoaiVthh()) ? null : hashMapDmHh.get(dataMap.getLoaiVthh()));
        dataMap.setTenCloaiVthh(StringUtils.isEmpty(dataMap.getCloaiVthh()) ? null : hashMapDmHh .get(dataMap.getCloaiVthh()));
        this.validateData(dataMap, dataMap.getTrangThai());
        XhDxKhBanDauGia created=xhDxKhBanDauGiaRepository.save(dataMap);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),dataMap.getId(),"XH_DX_KH_BAN_DAU_GIA");
        created.setFileDinhKems(fileDinhKems);

        for (XhDxKhBanDauGiaPhanLoReq pl : objReq.getDsPhanLoReq()){
            XhDxKhBanDauGiaPhanLo data = new ModelMapper().map(pl, XhDxKhBanDauGiaPhanLo.class);
            data.setIdDxKhbdg(dataMap.getId());
            BigDecimal giaKhoiDiem = data.getGiaKhongVat().multiply(data.getSoLuong());
            data.setGiaKhoiDiem(giaKhoiDiem);
            xhDxKhBanDauGiaPhanLoRepository.save(data);
            for (XhDxKhBanDauGiaDtlReq phanLoDl : pl.getChildren()) {
                XhDxKhBanDauGiaDtl dataphanLoDl = new ModelMapper().map(phanLoDl, XhDxKhBanDauGiaDtl.class);
                dataphanLoDl.setIdPhanLo(data.getId());
                dataphanLoDl.setGiaKhoiDiem(dataphanLoDl.getGiaKhongVat().multiply(dataphanLoDl.getSoLuong()));
                xhDxKhBanDauGiaDtlRepository.save(dataphanLoDl);
            }
        }
    return dataMap;
    }

    public void validateData(XhDxKhBanDauGia objHdr,String trangThai) throws Exception {
        if(trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())){
            XhDxKhBanDauGia dXuat = xhDxKhBanDauGiaRepository.findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(objHdr.getLoaiVthh(), objHdr.getCloaiVthh(), objHdr.getNamKh(), objHdr.getMaDvi(),NhapXuatHangTrangThaiEnum.DUTHAO.getId());
            if(!ObjectUtils.isEmpty(dXuat) && !dXuat.getId().equals(objHdr.getId())){
                throw new Exception("Chủng loại hàng hóa đã được tạo và gửi duyệt, xin vui lòng chọn lại chủng loại hàng hóa khác");
            }
        }
    }


    @Transactional
    public XhDxKhBanDauGia update(XhDxKhBanDauGiaReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữu liệu");

        Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        if (!StringUtils.isEmpty(objReq.getSoDxuat())){
            Optional<XhDxKhBanDauGia> deXuat = xhDxKhBanDauGiaRepository.findBySoDxuat(objReq.getSoDxuat());
            if (deXuat.isPresent()){
                if (!deXuat.get().getId().equals(objReq.getId())){
                    throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
                }
            }
        }

        XhDxKhBanDauGia dataDTB = qOptional.get();
        XhDxKhBanDauGia dataMap = ObjectMapperUtils.map(objReq, XhDxKhBanDauGia.class);

        updateObjectToObject(dataDTB, dataMap);

        dataDTB.setNgaySua(getDateTimeNow());
        dataDTB.setNguoiSuaId(getUser().getUsername());



        xhDxKhBanDauGiaRepository.save(dataDTB);

//        Xóa tất cả phân lô và lưu mới
        xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDxKhbdg(dataMap.getId());
        for (XhDxKhBanDauGiaPhanLoReq pl : objReq.getDsPhanLoReq()){
            XhDxKhBanDauGiaPhanLo data = new ModelMapper().map(pl, XhDxKhBanDauGiaPhanLo.class);
            data.setId(null);
            data.setIdDxKhbdg(dataDTB.getId());
            BigDecimal giaKhoiDiem = data.getGiaKhongVat().multiply(data.getSoLuong());
            data.setGiaKhoiDiem(giaKhoiDiem);
            xhDxKhBanDauGiaPhanLoRepository.save(data);
            xhDxKhBanDauGiaDtlRepository.deleteAllByIdPhanLo(data.getId());
            for (XhDxKhBanDauGiaDtlReq ddNhap : pl.getChildren()){
                XhDxKhBanDauGiaDtl dataDdNhap = new ModelMapper().map(ddNhap, XhDxKhBanDauGiaDtl.class);
                dataDdNhap.setId(null);
                dataDdNhap.setIdPhanLo(data.getId());
                xhDxKhBanDauGiaDtlRepository.save(dataDdNhap);
            }
        }
        return dataDTB;
    }

    public XhDxKhBanDauGia detail (Long ids) throws Exception{
        Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(ids);

        if(!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Map<String, String > hasMapVthh = getListDanhMucHangHoa();
        Map<String, String> hasMapDmucDvi = getListDanhMucDvi(null, null, "01");

        qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh())? null : hasMapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh())? null : hasMapVthh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(hasMapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
        qOptional.get().setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThaiTh()));

        List<XhDxKhBanDauGiaPhanLo>  dsPloList = xhDxKhBanDauGiaPhanLoRepository.findByIdDxKhbdg(qOptional.get().getId());
        for (XhDxKhBanDauGiaPhanLo dsP : dsPloList){
            dsP.setTenDvi(hasMapDmucDvi.get(dsP.getMaDvi()));
            dsP.setTenDiemKho(hasMapDmucDvi.get(dsP.getMaDiemKho()));
            dsP.setTenNhakho(hasMapDmucDvi.get(dsP.getMaNhaKho()));
            dsP.setTenNganKho(hasMapDmucDvi.get(dsP.getMaNganKho()));
            dsP.setTenLoKho(hasMapDmucDvi.get(dsP.getMaLoKho()));
            dsP.setTenCloaiVthh(hasMapVthh.get(dsP.getCloaiVthh()));

            List<XhDxKhBanDauGiaDtl> listDdNhap = xhDxKhBanDauGiaDtlRepository.findByIdPhanLo(dsP.getId());
            listDdNhap.forEach(f ->{
                f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())? null : hasMapDmucDvi.get(f.getMaDvi()));
                f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())? null : hasMapDmucDvi.get(f.getMaDiemKho()));
                f.setTenNhakho(StringUtils.isEmpty(f.getMaNhaKho())?null: hasMapDmucDvi.get(f.getMaNhaKho()));
                f.setTenNganKho(StringUtils.isEmpty(f.getMaNhaKho())?null: hasMapDmucDvi.get(f.getMaNganKho()));
                f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:hasMapDmucDvi.get(f.getMaLoKho()));
                f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hasMapVthh.get(f.getCloaiVthh()));
            });
            dsP.setChildren(listDdNhap);
        }
        qOptional.get().setDsPhanLoList(dsPloList);
        return qOptional.get();
    }

    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
        Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(idSearchReq.getId());

        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
        }
        List<XhDxKhBanDauGiaPhanLo> phanLo = xhDxKhBanDauGiaPhanLoRepository.findByIdDxKhbdg(idSearchReq.getId());
        if (phanLo != null && phanLo.size() > 0) {
            for (XhDxKhBanDauGiaPhanLo pl : phanLo) {
                List<XhDxKhBanDauGiaDtl> dsDtl = xhDxKhBanDauGiaDtlRepository.findByIdPhanLo(pl.getId());
                xhDxKhBanDauGiaDtlRepository.deleteAll(dsDtl);
            }
            xhDxKhBanDauGiaPhanLoRepository.deleteAll(phanLo);
        }
        xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDxKhbdg(idSearchReq.getId());
        xhDxKhBanDauGiaRepository.delete(optional.get());
    }

    public void deleteMulti (IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        List<XhDxKhBanDauGia> listDg = xhDxKhBanDauGiaRepository.findByIdIn(idSearchReq.getIdList());
        if(listDg.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (XhDxKhBanDauGia dg: listDg){
            if (!dg.getTrangThai().equals(Contains.DUTHAO)
            && !dg.getTrangThai().equals(Contains.TU_CHOI_TP)
            && !dg.getTrangThai().equals(Contains.TUCHOI_LDC)){
                throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
            }
            List<XhDxKhBanDauGiaPhanLo> phanlo = xhDxKhBanDauGiaPhanLoRepository.findByIdDxKhbdg(dg.getId());
            List<Long> listPhanLo = phanlo.stream().map(XhDxKhBanDauGiaPhanLo :: getId).collect(Collectors.toList());
            xhDxKhBanDauGiaDtlRepository.deleteAllByIdPhanLoIn(listPhanLo);
        }
        xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDxKhbdgIn(idSearchReq.getIdList());
        xhDxKhBanDauGiaRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

 @Transactional
 public XhDxKhBanDauGia approve (StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId()))
            throw new Exception("Không tìm thấy dữ liệu");

        XhDxKhBanDauGia optional = this.detail(stReq.getId());

        if (optional.getLoaiVthh().startsWith("02")){
            String status = stReq.getTrangThai() + optional.getTrangThai();
            switch (status) {
                case Contains.CHODUYET_LDV + Contains.DUTHAO:
                case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                    optional.setNguoiGduyetId(getUser().getUsername());
                    optional.setNgayGduyet(getDateTimeNow());
                    break;
                case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
                    optional.setNguoiGduyetId(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    optional.setLdoTuChoi(stReq.getLyDo());
                    break;
                case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
                    optional.setNguoiGduyetId(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
        }else{
            String status = stReq.getTrangThai() + optional.getTrangThai();
            switch (status) {
                case Contains.CHODUYET_TP + Contains.DUTHAO:
                case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                    this.validateData(optional,Contains.CHODUYET_TP);
                    optional.setNguoiGduyetId(getUser().getUsername());
                    optional.setNgayGduyet(getDateTimeNow());
                case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
                case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                    optional.setNguoiPduyetId(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    optional.setLdoTuChoi(stReq.getLyDo());
                    break;
                case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                    this.validateData(optional,stReq.getTrangThai());
                    optional.setNguoiPduyetId(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
        }
     optional.setTrangThai(stReq.getTrangThai());
     return xhDxKhBanDauGiaRepository.save(optional);
 }

    public void export (SearchXhDxKhBanDauGia objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhDxKhBanDauGia> page = this.searchPage(objReq);
        List<XhDxKhBanDauGia> data = page.getContent();

        String title = "Danh sách đề xuất kế hoạch mua trực tiếp";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số kế hoạch", "Đơn vị", "Ngày lập kế hoạch", "Ngày ký", "Trích yếu", "Loại hành hóa", "Chủng loại hành hóa", "Số QĐ giao chỉ tiêu", "Số QĐ phê duyệt KH bán đấu giá", "Trạng thái"};
        String fileName = "danh-sách-đề-xuất-kế-hoạch-mua-trực-tiếp.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i <data.size(); i ++){
            XhDxKhBanDauGia dx = data.get(i);
            objs[0] = i;
            objs[1] = dx.getNamKh();
            objs[2] = dx.getSoDxuat();
            objs[3] = dx.getTenDvi();
            objs[4] = dx.getNgayTao();
            objs[5] = dx.getNgayKy();
            objs[6] = dx.getTrichYeu();
            objs[7] = dx.getTenLoaiVthh();
            objs[8] = dx.getTenCloaiVthh();
            objs[9] = dx.getSoQdCtieu();
            objs[10] = dx.getSoQdPd();
            objs[11] = dx.getTenTrangThai();
        }
    }



}