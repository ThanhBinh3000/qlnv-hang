package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.Operator;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhieuNhapKhoCtReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.docx4j.wml.U;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HhPhieuNhapKhoHdrService  extends BaseServiceImpl {

    @Autowired
    private HhPhieuNhapKhoHdrRepository hhPhieuNhapKhoHdrRepository;

    @Autowired
    private HhPhieuNhapKhoCtRepository hhPhieuNhapKhoCtRepository;

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

    @Transient
    public HhPhieuNhapKhoHdr create(HhPhieuNhapKhoHdrReq objReq) throws Exception{
        UserInfo userInfo =SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        HhPhieuNhapKhoHdr data = new ModelMapper().map(objReq, HhPhieuNhapKhoHdr.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        HhPhieuNhapKhoHdr created = hhPhieuNhapKhoHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_PHIEU_NHAP_KHO_HDR");
        created.setFileDinhKems(fileDinhKems);
        this.saveCtiet(data, objReq);
        return created;
    }


    public void saveCtiet(HhPhieuNhapKhoHdr data, HhPhieuNhapKhoHdrReq objReq){
        for (HhPhieuNhapKhoCtReq req: objReq.getPhieuNhapKhoCtList()){
            HhPhieuNhapKhoCt dtl = new  ModelMapper().map(req, HhPhieuNhapKhoCt.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            hhPhieuNhapKhoCtRepository.save(dtl);
        }
    }


}
