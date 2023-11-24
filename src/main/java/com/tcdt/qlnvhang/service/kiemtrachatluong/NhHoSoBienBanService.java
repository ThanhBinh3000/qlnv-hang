package com.tcdt.qlnvhang.service.kiemtrachatluong;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.kiemtrachatluong.NhHoSoBienBanCtRepository;
import com.tcdt.qlnvhang.repository.kiemtrachatluong.NhHoSoBienBanRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.kiemtrachatluong.NhHoSoBienBanCtReq;
import com.tcdt.qlnvhang.request.kiemtrachatluong.NhHoSoBienBanReq;
import com.tcdt.qlnvhang.request.kiemtrachatluong.SearchNhHoSoBienBan;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong.NhHoSoBienBanPreview;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.nhapkho.NhBienBanGuiHangPreview;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBan;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBanCt;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NhHoSoBienBanService extends BaseServiceImpl {

    @Autowired
    private NhHoSoBienBanRepository nhHoSoBienBanRepository;

    @Autowired
    private NhHoSoBienBanCtRepository nhHoSoBienBanCtRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public List<NhHoSoBienBan> findAll() throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        List<NhHoSoBienBan> data = nhHoSoBienBanRepository.findAll();
        data.forEach( f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });

        return data;
    }

    @Transactional
    public NhHoSoBienBan save(NhHoSoBienBanReq objReq)throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if(userInfo== null){
            throw new Exception("Bad request.");
        }
        NhHoSoBienBan data = new NhHoSoBienBan();
        BeanUtils.copyProperties(objReq,data);
        data.setMaDvi(userInfo.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.setNguoiTaoId(userInfo.getId());
        data.setNgayTao(new Date());
        data.setId(Long.parseLong(objReq.getSoBienBan().split("/")[0]));
        nhHoSoBienBanRepository.save(data);
        updateFileDinhKem(objReq, data.getId());
        this.saveCtiet(data,objReq);
        return data;
    }

    private void updateFileDinhKem (NhHoSoBienBanReq objReq, Long id){
        fileDinhKemService.delete(id, Lists.newArrayList(NhHoSoBienBan.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(NhHoSoBienBan.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(objReq.getListCanCu())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getListCanCu(), id, NhHoSoBienBan.TABLE_NAME + "_CAN_CU");
        }
        if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), id, NhHoSoBienBan.TABLE_NAME);
        }
    }

    @Transactional
    public NhHoSoBienBan update(NhHoSoBienBanReq objReq)throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if(userInfo== null){
            throw new Exception("Bad request.");
        }
        Optional<NhHoSoBienBan> optional = nhHoSoBienBanRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }
        NhHoSoBienBan data=optional.get();
        BeanUtils.copyProperties(objReq,data,"id","maDvi");
        NhHoSoBienBan created= nhHoSoBienBanRepository.save(data);
        updateFileDinhKem(objReq, created.getId());
        List<NhHoSoBienBanCt> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBan(data.getId());
        nhHoSoBienBanCtRepository.deleteAll(dtlList);
        this.saveCtiet(data,objReq);
        return created;
    }

    @Transactional
    public void saveCtiet(NhHoSoBienBan data,NhHoSoBienBanReq objReq){
        nhHoSoBienBanCtRepository.deleteAllByIdHoSoBienBan(data.getId());
        for(NhHoSoBienBanCtReq dtlReq :objReq.getChildren()) {
            NhHoSoBienBanCt dtl = new ModelMapper().map(dtlReq, NhHoSoBienBanCt.class);
            dtl.setId(null);
            dtl.setIdHoSoBienBan(data.getId());
            nhHoSoBienBanCtRepository.save(dtl);
        }
    }

    public NhHoSoBienBan detail(String ids) throws Exception{
        Optional<NhHoSoBienBan> optional = nhHoSoBienBanRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        NhHoSoBienBan data= optional.get();
        Map<String,String> mapVthh = getListDanhMucHangHoa();
        Map<String,String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
        List<NhHoSoBienBanCt> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBan(data.getId());
        data.setChildren(dtlList);
        data.setFileDinhKems(fileDinhKemService.search(data.getId(), Collections.singleton(NhHoSoBienBan.TABLE_NAME)));
        data.setListCanCu(fileDinhKemService.search(data.getId(), Collections.singleton(NhHoSoBienBan.TABLE_NAME + "_CAN_CU")));
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<NhHoSoBienBan> optional = nhHoSoBienBanRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        NhHoSoBienBan data = optional.get();
        List<NhHoSoBienBanCt> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBan(data.getId());
        nhHoSoBienBanCtRepository.deleteAll(dtlList);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("NH_HO_SO_BIEN_BAN"));
        nhHoSoBienBanRepository.delete(data);

    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<NhHoSoBienBan> list = nhHoSoBienBanRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listIdHdr=list.stream().map(NhHoSoBienBan::getId).collect(Collectors.toList());
        List<NhHoSoBienBanCt> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBanIn(listIdHdr);
        nhHoSoBienBanCtRepository.deleteAll(dtlList);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),  Lists.newArrayList("NH_HO_SO_BIEN_BAN"));
        nhHoSoBienBanRepository.deleteAll(list);

    }


    public NhHoSoBienBan approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<NhHoSoBienBan> optional =nhHoSoBienBanRepository.findById(statusReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.DAKY + Contains.DUTHAO:
                optional.get().setNguoiPduyetId(userInfo.getId());
                optional.get().setNgayPduyet(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        NhHoSoBienBan created = nhHoSoBienBanRepository.save(optional.get());
        return created;
    }

    public ReportTemplateResponse preview(SearchNhHoSoBienBan req) throws Exception {
        NhHoSoBienBan hoSoBienBan = detail(req.getId().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        if (hoSoBienBan == null) {
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");
        }
        NhHoSoBienBanPreview object = new NhHoSoBienBanPreview();
        BeanUtils.copyProperties(hoSoBienBan,object);
        object.setNgayTao(Objects.isNull(hoSoBienBan.getNgayTao()) ? null : formatter.format(hoSoBienBan.getNgayTao()));
        object.setNgayHd(Objects.isNull(hoSoBienBan.getNgayHd()) ? null : formatter.format(hoSoBienBan.getNgayHd()));
        object.setTgianNhap(Objects.isNull(hoSoBienBan.getTgianNhap()) ? null : formatter.format(hoSoBienBan.getTgianNhap()));
        List<NhHoSoBienBanCt> cuc = hoSoBienBan.getChildren().stream().filter(item -> item.getLoaiDaiDien().equals("cuc")).collect(Collectors.toList());
        List<NhHoSoBienBanCt> chiCuc = hoSoBienBan.getChildren().stream().filter(item -> item.getLoaiDaiDien().equals("chiCuc")).collect(Collectors.toList());
        object.setListChiCuc(chiCuc);
        object.setListCuc(cuc);
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }
}
