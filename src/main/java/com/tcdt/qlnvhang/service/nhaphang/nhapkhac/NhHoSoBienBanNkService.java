package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBan;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBanCt;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoBienBanCtNk;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoBienBanNk;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoKyThuatNk;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.kiemtrachatluong.NhHoSoBienBanCtRepository;
import com.tcdt.qlnvhang.repository.kiemtrachatluong.NhHoSoBienBanRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.NhHoSoBienBanCtNkRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.NhHoSoBienBanNkRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.NhHoSoKyThuatNkRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.kiemtrachatluong.NhHoSoBienBanCtReq;
import com.tcdt.qlnvhang.request.kiemtrachatluong.NhHoSoBienBanReq;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NhHoSoBienBanNkService extends BaseServiceImpl {

    @Autowired
    private NhHoSoBienBanNkRepository nhHoSoBienBanRepository;

    @Autowired
    private NhHoSoKyThuatNkRepository nhHoSoKyThuatRepository;

    @Autowired
    private NhHoSoBienBanCtNkRepository nhHoSoBienBanCtRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    public List<NhHoSoBienBanNk> findAll() throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        List<NhHoSoBienBanNk> data = nhHoSoBienBanRepository.findAll();
        data.forEach( f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });

        return data;
    }

    @Transactional
    public NhHoSoBienBanNk save(NhHoSoBienBanReq objReq)throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if(userInfo== null){
            throw new Exception("Bad request.");
        }
        NhHoSoBienBanNk data = new NhHoSoBienBanNk();
        BeanUtils.copyProperties(objReq,data);
        data.setMaDvi(userInfo.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.setNguoiTaoId(userInfo.getId());
        data.setNgayTao(new Date());
        data.setId(Long.parseLong(objReq.getSoBienBan().split("/")[0]));
        nhHoSoBienBanRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"NH_HO_SO_BIEN_BAN_NK");
        data.setFileDinhKems(fileDinhKems);
        NhHoSoKyThuatNk nhHoSoKyThuatNk = nhHoSoKyThuatRepository.findByIdBbLayMau(data.getIdBbLayMau());
        if(data.getLoaiBb().equals("BBKTNQ")){
            nhHoSoKyThuatNk.setSoBbKtraNgoaiQuan(data.getSoBienBan());
        } else if (data.getLoaiBb().equals("BBKTVH")){
            nhHoSoKyThuatNk.setSoBbKtraVanHanh(data.getSoBienBan());
        } else {
            nhHoSoKyThuatNk.setSoBbKtraHskt(data.getSoBienBan());
        }
        nhHoSoKyThuatRepository.save(nhHoSoKyThuatNk);
        this.saveCtiet(data,objReq);
        return data;
    }

    @Transactional
    public NhHoSoBienBanNk update(NhHoSoBienBanReq objReq)throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if(userInfo== null){
            throw new Exception("Bad request.");
        }
        Optional<NhHoSoBienBanNk> optional = nhHoSoBienBanRepository.findById(objReq.getId());
        NhHoSoBienBanNk data=optional.get();
        BeanUtils.copyProperties(objReq,data,"id","maDvi");
        NhHoSoBienBanNk created= nhHoSoBienBanRepository.save(data);
        NhHoSoKyThuatNk nhHoSoKyThuatNk = nhHoSoKyThuatRepository.findByIdBbLayMau(created.getIdBbLayMau());
        if(created.getLoaiBb().equals("BBKTNQ")){
            nhHoSoKyThuatNk.setSoBbKtraNgoaiQuan(created.getSoBienBan());
        } else if (created.getLoaiBb().equals("BBKTVH")){
            nhHoSoKyThuatNk.setSoBbKtraVanHanh(created.getSoBienBan());
        } else {
            nhHoSoKyThuatNk.setSoBbKtraHskt(created.getSoBienBan());
        }
        List<NhHoSoBienBanCtNk> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBan(data.getId());
        nhHoSoBienBanCtRepository.deleteAll(dtlList);
        this.saveCtiet(data,objReq);
        return created;
    }

    @Transactional
    public void saveCtiet(NhHoSoBienBanNk data,NhHoSoBienBanReq objReq){
        nhHoSoBienBanCtRepository.deleteAllByIdHoSoBienBan(data.getId());
        for(NhHoSoBienBanCtReq dtlReq :objReq.getChildren()) {
            NhHoSoBienBanCtNk dtl = new ModelMapper().map(dtlReq, NhHoSoBienBanCtNk.class);
            dtl.setId(null);
            dtl.setIdHoSoBienBan(data.getId());
            nhHoSoBienBanCtRepository.save(dtl);
        }
    }

    public NhHoSoBienBanNk detail(String ids) throws Exception{
        Optional<NhHoSoBienBanNk> optional = nhHoSoBienBanRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        NhHoSoBienBanNk data= optional.get();
        Map<String,String> mapVthh = getListDanhMucHangHoa();
        Map<String,String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
        List<NhHoSoBienBanCtNk> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBan(data.getId());
        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Collections.singleton(NhHoSoBienBanNk.TABLE_NAME));
        data.setChildren(dtlList);
        data.setFileDinhKems(fileDinhKems);
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<NhHoSoBienBanNk> optional = nhHoSoBienBanRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        NhHoSoBienBanNk data = optional.get();
        List<NhHoSoBienBanCtNk> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBan(data.getId());
        nhHoSoBienBanCtRepository.deleteAll(dtlList);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("NH_HO_SO_BIEN_BAN_NK"));
        nhHoSoBienBanRepository.delete(data);

    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<NhHoSoBienBanNk> list = nhHoSoBienBanRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listIdHdr=list.stream().map(NhHoSoBienBanNk::getId).collect(Collectors.toList());
        List<NhHoSoBienBanCtNk> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBanIn(listIdHdr);
        nhHoSoBienBanCtRepository.deleteAll(dtlList);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),  Lists.newArrayList("NH_HO_SO_BIEN_BAN_NK"));
        nhHoSoBienBanRepository.deleteAll(list);

    }


    public NhHoSoBienBanNk approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<NhHoSoBienBanNk> optional =nhHoSoBienBanRepository.findById(statusReq.getId());
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
        NhHoSoBienBanNk created = nhHoSoBienBanRepository.save(optional.get());
        return created;
    }

    public ReportTemplateResponse preview(NhHoSoKyThuatReq objReq) throws Exception {
        NhHoSoBienBanNk optional = detail(String.valueOf(objReq.getId()));
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }

}
