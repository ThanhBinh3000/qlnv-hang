package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.hopdong.bangkethumuale;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.bangKeMuaLe.BangkethumualeRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.bangkethumuale.BangKeMuaLeReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.bangkethumuale.BangKeMuaLe;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class BangKeMuaLeService extends BaseServiceImpl {

    @Autowired
    private BangkethumualeRepository bangkethumualeRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<BangKeMuaLe> searchPage(CustomUserDetails currentUser, BangKeMuaLeReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<BangKeMuaLe> search = bangkethumualeRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            if (mapDmucDvi.get((s.getMaDvi())) != null) {
                s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
            }
            if (mapVthh.get((s.getLoaiVthh())) != null) {
                s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
            }
            if (mapVthh.get((s.getCloaiVthh())) != null) {
                s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
            }
        });
        return search;
    }

    @Transactional
    public BangKeMuaLe save(BangKeMuaLeReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<BangKeMuaLe> optional = bangkethumualeRepository.findBySoBangKe(objReq.getSoBangKe());
        if(optional.isPresent()){
            throw new Exception("số hợp đồng đã tồn tại");
        }
        BangKeMuaLe data = new ModelMapper().map(objReq,BangKeMuaLe.class);

        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setMaDvi(userInfo.getDvql());
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        BangKeMuaLe created=bangkethumualeRepository.save(data);
        return created;
    }

    @Transactional
    public BangKeMuaLe update(BangKeMuaLeReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<BangKeMuaLe> optional = bangkethumualeRepository.findById(objReq.getId());

        Optional<BangKeMuaLe> soBienBan = bangkethumualeRepository.findBySoBangKe(objReq.getSoBangKe());
        if (soBienBan.isPresent()){
            if (!soBienBan.get().getId().equals(objReq.getId())){
                throw new Exception("số hợp đồng đã tồn tại");
            }
        }
        BangKeMuaLe data = optional.get();
        BangKeMuaLe dataMap = new ModelMapper().map(objReq,BangKeMuaLe.class);
        updateObjectToObject(data,dataMap);
        BangKeMuaLe created=bangkethumualeRepository.save(data);
        return created;
    }


    public BangKeMuaLe detail( Long ids) throws Exception {
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Optional<BangKeMuaLe> allById = bangkethumualeRepository.findById(ids);
            allById.get().setTenDvi(mapDmucDvi.get(allById.get().getMaDvi()));
            allById.get().setTenLoaiVthh(mapVthh.get(allById.get().getLoaiVthh()));
        allById.get().setTenCloaiVthh(mapVthh.get(allById.get().getCloaiVthh()));
        return allById.get();
    }


    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<BangKeMuaLe> optional= bangkethumualeRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        BangKeMuaLe data = optional.get();
        bangkethumualeRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<BangKeMuaLe> list= bangkethumualeRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        bangkethumualeRepository.deleteAll(list);
    }


    public  void export(CustomUserDetails currentUser ,BangKeMuaLeReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<BangKeMuaLe> page=this.searchPage(currentUser,objReq);
        List<BangKeMuaLe> data=page.getContent();

        String title="Danh sách bảng kê thu mua lẻ";
        String[] rowsName=new String[]{"STT","Năm kế hoạch","Sô bảng kê","Số quyết định","Địa chỉ","Số CMT/CCCD","Loại hàng hóa","Chủng loại hàng hóa","Số lượng","Đơn giá(đ)","Thành tiên(đ)",};
        String fileName="danh-sach-bang-ke-thu-mua-le.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            BangKeMuaLe dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getNamQd();
            objs[2]=dx.getSoBangKe();
            objs[3]=dx.getSoQdGiaoNvNh();
            objs[4]=dx.getNguoiBan();
            objs[5]=dx.getDiaChiNguoiBan();
            objs[6]=dx.getSoCmt();
            objs[7]=dx.getTenLoaiVthh();
            objs[8]=dx.getTenCloaiVthh();
            objs[9]=dx.getDonGia();
            objs[10]=dx.getThanhTien();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }
}
