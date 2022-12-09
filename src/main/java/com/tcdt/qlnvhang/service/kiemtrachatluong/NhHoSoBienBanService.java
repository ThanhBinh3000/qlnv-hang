package com.tcdt.qlnvhang.service.kiemtrachatluong;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.kiemtrachatluong.NhHoSoBienBanCtRepository;
import com.tcdt.qlnvhang.repository.kiemtrachatluong.NhHoSoBienBanRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.kiemtrachatluong.NhHoSoBienBanCtReq;
import com.tcdt.qlnvhang.request.kiemtrachatluong.NhHoSoBienBanReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.kiemtrachatluong.NhHoSoBienBan;
import com.tcdt.qlnvhang.table.kiemtrachatluong.NhHoSoBienBanCt;
import com.tcdt.qlnvhang.util.Contains;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.time.LocalDate;
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
        Optional<NhHoSoBienBan> optional = nhHoSoBienBanRepository.findAllBySoBienBan(objReq.getSoBienBan());
        if (optional.isPresent()){
            throw new Exception("Số biên bản đã tồn tại");
        }
        NhHoSoBienBan data= new ModelMapper().map(objReq,NhHoSoBienBan.class);
        data.setMaDvi(userInfo.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        NhHoSoBienBan created= nhHoSoBienBanRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"NH_HO_SO_BIEN_BAN");
        created.setFileDinhKems(fileDinhKems);
        this.saveCtiet(data,objReq);
        return created;
    }

    @Transactional
    public NhHoSoBienBan update(NhHoSoBienBanReq objReq)throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if(userInfo== null){
            throw new Exception("Bad request.");
        }
        Optional<NhHoSoBienBan> optional = nhHoSoBienBanRepository.findById(objReq.getId());
        Optional<NhHoSoBienBan> soQd = nhHoSoBienBanRepository.findAllBySoBienBan(objReq.getSoBienBan());
        if (soQd.isPresent()){
            if (!soQd.get().getId().equals(objReq.getId())){
                throw new Exception("Số biên bản đã tồn tại");
            }
        }
        NhHoSoBienBan data=optional.get();
        BeanUtils.copyProperties(objReq,data,"id","maDvi");
        NhHoSoBienBan created= nhHoSoBienBanRepository.save(data);
        List<NhHoSoBienBanCt> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBan(data.getId());
        nhHoSoBienBanCtRepository.deleteAll(dtlList);
        this.saveCtiet(data,objReq);
        return created;
    }
    public void saveCtiet(NhHoSoBienBan data,NhHoSoBienBanReq objReq){
        for(NhHoSoBienBanCtReq dtlReq :objReq.getHoSoBienBanCtList()) {
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
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        data.setTenDvi(StringUtils.isEmpty(data.getTenDvi())?null:hashMapDmhh.get(data.getMaDvi()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        List<NhHoSoBienBanCt> dtlList = nhHoSoBienBanCtRepository.findAllByIdHoSoBienBan(data.getId());
        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Collections.singleton(NhHoSoBienBan.TABLE_NAME));
        data.setHoSoBienBanCtList(dtlList);
        data.setFileDinhKems(fileDinhKems);
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
        Optional<NhHoSoBienBan> optional =nhHoSoBienBanRepository.findById(Long.valueOf(statusReq.getId()));
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

}
