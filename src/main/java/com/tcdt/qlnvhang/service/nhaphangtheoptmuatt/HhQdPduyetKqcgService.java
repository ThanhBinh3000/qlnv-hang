package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.entities.FileDKemJoinKquaMttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhCtietTtinCgiaRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPduyetKqcgRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPheduyetKhMttDxRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPheduyetKhMttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPduyetKqcgHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhQdPduyetKqcg;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class HhQdPduyetKqcgService extends BaseServiceImpl {

    @Autowired
    HhQdPduyetKqcgRepository hhQdPduyetKqcgRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private HhQdPheduyetKhMttDxRepository hhQdPheduyetKhMttDxRepository;

    @Autowired
    HhCtietTtinCgiaRepository hhCtietTtinCgiaRepository;

    @Autowired
    HhQdPheduyetKhMttHdrRepository hhQdPheduyetKhMttHdrRepository;

    @Autowired
    private HhQdPheduyetKhMttHdrService qdKhMttService;

    public Page<HhQdPduyetKqcgHdr> searchPage(SearchHhQdPduyetKqcg objReq , HttpServletResponse response) throws Exception {
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdPduyetKqcgHdr> hhQdPduyetKqcgHdrs = hhQdPduyetKqcgRepository.selectPage(
                objReq.getNamKh(),
                convertDateToString(objReq.getNgayCgiaTu()),
                convertDateToString(objReq.getNgayCgiaDen()),
                objReq.getMaDvi(),
                objReq.getTrangThai(),
                pageable
        );
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> listDanhMucHh = getListDanhMucHangHoa();
        hhQdPduyetKqcgHdrs.forEach(item ->{
            try {
                item.setHhQdPheduyetKhMttDx(Objects.isNull(item.getIdQdPdKhDtl()) ? null : qdKhMttService.detailDtl(item.getIdQdPdKhDtl()));
                item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
                item.setTenLoaiVthh(listDanhMucHh.get(item.getLoaiVthh()));
                item.setTenCloaiVthh(listDanhMucHh.get(item.getCloaiVthh()));
                item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
                if (!StringUtils.isEmpty(item.getIdQdPdKhDtl())){
                    Optional<HhQdPheduyetKhMttDx> byId = hhQdPheduyetKhMttDxRepository.findById(item.getIdQdPdKhDtl());
                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return hhQdPduyetKqcgHdrs;
    }

    public HhQdPduyetKqcgHdr create (HhQdPduyetKqcgHdrReq objReq) throws Exception{
        List<HhQdPheduyetKhMttHdr> checkSoCc = hhQdPheduyetKhMttHdrRepository.findBySoQd(objReq.getSoQdPdKh());
        if (checkSoCc.isEmpty()){
            throw new Exception(
                    "Số quyết định phê duyệt kế hoạch mua trực tiếp" + objReq.getSoQdPdKh() + "đã tồn tại");
        }
        if (!StringUtils.isEmpty(objReq.getSoQd())){
            Optional<HhQdPduyetKqcgHdr> checkSoQd = hhQdPduyetKqcgRepository.findBySoQd(objReq.getSoQd());
            if (checkSoQd.isPresent()){
                throw new Exception(
                        "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getSoQd() + " đã tồn tại");
            }
        }

        List<FileDKemJoinKquaMttHdr> fileDinhKemList = new ArrayList<FileDKemJoinKquaMttHdr>();
        if (objReq.getFileDinhKems() != null){
            fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKquaMttHdr.class);
            fileDinhKemList.forEach(f->{
                f.setDataType(HhQdPduyetKqcgHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }
        HhQdPduyetKqcgHdr dataMap = new HhQdPduyetKqcgHdr();
        BeanUtils.copyProperties(objReq, dataMap);

        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiHd(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
        dataMap.setMaDvi(getUser().getDvql());
        dataMap.setChildren(fileDinhKemList);
        return hhQdPduyetKqcgRepository.save(dataMap);
    }

    public  HhQdPduyetKqcgHdr update (HhQdPduyetKqcgHdrReq objReq) throws Exception{
        if (StringUtils.isEmpty(objReq.getId())){
            throw new Exception("Sửa thất bại không tìm thấy dữ liệu ");
        }
        Optional<HhQdPduyetKqcgHdr> qOptional = hhQdPduyetKqcgRepository.findById(objReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        List<HhQdPheduyetKhMttHdr> checkSoCc = hhQdPheduyetKhMttHdrRepository.findBySoQd(objReq.getSoQdPdKh());
        if (checkSoCc.isEmpty()){
            throw new Exception("Số quyết định phê duyệt kế hoạch mua trực tiếp" + objReq.getSoQdPdKh() + "Không tồn tại");
        }

        if (!StringUtils.isEmpty(objReq.getSoQd())){
            if (!objReq.getSoQd().equals(qOptional.get().getSoQd())){
                Optional<HhQdPduyetKqcgHdr> checkSoQd = hhQdPduyetKqcgRepository.findBySoQd(objReq.getSoQd());
                if (checkSoQd.isPresent()){
                    throw new Exception("" +
                            "Số quyết định phê duyệt kết quả mua trực tiếp" + objReq.getSoQd() + "đã tồn tại");
                }
            }
        }
        // Add danh sach file dinh kem o Master
        List<FileDKemJoinKquaMttHdr> fileDinhKemList = new ArrayList<FileDKemJoinKquaMttHdr>();
        if (objReq.getFileDinhKems() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKquaMttHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhQdPduyetKqcgHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }
        HhQdPduyetKqcgHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(objReq, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSua(getUser().getUsername());
        dataDB.setChildren(fileDinhKemList);
        HhQdPduyetKqcgHdr createCheck = hhQdPduyetKqcgRepository.save(dataDB);
        return  createCheck;
    }

    public HhQdPduyetKqcgHdr detail(String ids) throws Exception{
        if (StringUtils.isEmpty(ids)){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Optional<HhQdPduyetKqcgHdr> qOptional = hhQdPduyetKqcgRepository.findById(Long.parseLong(ids));

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh())?null:hashMapDmHh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh())?null:hashMapDmHh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
        qOptional.get().setHhQdPheduyetKhMttDx(Objects.isNull(qOptional.get().getIdQdPdKhDtl()) ? null : qdKhMttService.detailDtl(qOptional.get().getIdQdPdKhDtl()));
        qOptional.get().setTenDvi(listDanhMucDvi.get(qOptional.get().getMaDvi()));
        return qOptional.get();
    }

    @Transactional(rollbackOn =  Exception.class)
    public HhQdPduyetKqcgHdr approve (StatusReq stReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (StringUtils.isEmpty(stReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhQdPduyetKqcgHdr> optional = hhQdPduyetKqcgRepository.findById(Long.valueOf(stReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        String status = stReq.getTrangThai() + optional.get().getTrangThai();
        switch (status){
            case Contains.BAN_HANH + Contains.DUTHAO:

                this.updateDataApprove(optional);

                optional.get().setNguoiPduyet(userInfo.getUsername());
                optional.get().setNgayPduyet(new Date());
                optional.get().setTrangThai(stReq.getTrangThai());
                break;
            case Contains.DA_HOAN_THANH + Contains.BAN_HANH:
                optional.get().setTrangThaiHd(stReq.getTrangThai());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        HhQdPduyetKqcgHdr createCheck = hhQdPduyetKqcgRepository.save(optional.get());
        return createCheck;
    }

    void updateDataApprove(Optional<HhQdPduyetKqcgHdr> optional) throws Exception {
        Optional<HhQdPheduyetKhMttDx> byId = hhQdPheduyetKhMttDxRepository.findById(optional.get().getIdQdPdKhDtl());
        if (byId.isPresent()){
            HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx = byId.get();
            if (!StringUtils.isEmpty(hhQdPheduyetKhMttDx.getSoQdPdKqMtt())){
                throw new Exception(
                        "Thông tin kế hoạch mua trực tiếp đã được bạn hành quyết định phê duyệt kế hoạch mua trực tiếp, xin vui lòng chọn lại");
            }
            hhQdPheduyetKhMttDx.setSoQdPdKqMtt(optional.get().getSoQd());
            hhQdPheduyetKhMttDxRepository.save(hhQdPheduyetKhMttDx);
        }else {
            throw new Exception("Số quyết định phê duyệt kế hoạch mua trực tiếp" + optional.get().getSoQdPdKh()+ " không tồn tại");

        }
    }

    public void delete(IdSearchReq idSearchReq) throws Exception{
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xóa thất bại, Không tìm thấy dữ liệu");
        Optional<HhQdPduyetKqcgHdr> optional = hhQdPduyetKqcgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)){
            throw new Exception("CHỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        hhQdPduyetKqcgRepository.delete(optional.get());
    }

    public void deleteMulti (IdSearchReq idSearchReq) throws Exception{
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        List<HhQdPduyetKqcgHdr> listQd = hhQdPduyetKqcgRepository.findByIdIn(idSearchReq.getIdList());
        if (listQd.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (HhQdPduyetKqcgHdr Qd : listQd){
            if (!Qd.getTrangThai().equals(Contains.DUTHAO)){
                throw new Exception("CHỉ thực hiện xóa với quyết định ở bản nháp hoặc từ chối");
            }
        }
        hhQdPduyetKqcgRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

    public void export(SearchHhQdPduyetKqcg objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhQdPduyetKqcgHdr> page = this.searchPage(objReq, response);
        List<HhQdPduyetKqcgHdr> data = page.getContent();

        String title ="Danh sách quyết định mua trưc tiếp";
        String[] rowsName=new String[]{"STT","Số QĐ PDKQ chào giá","Ngày ký QĐ","Đơn vi","Số QĐ PHKH mua trực tiếp","Loại hành hóa","Chủng loại hành hóa","Trạng thái"};
        String fileName="danh-sach-quyet-dinh-phe-duyet-kq-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhQdPduyetKqcgHdr qd=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=qd.getSoQd();
            objs[2]=qd.getNgayTao();
            objs[3]=qd.getTenDvi();
            objs[4]=qd.getSoQdPdKh();
            objs[5]=qd.getTenLoaiVthh();
            objs[6]=qd.getTenCloaiVthh();
            objs[7]=qd.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }


}
