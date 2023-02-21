package com.tcdt.qlnvhang.service.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class XhHopDongBttServiceImpI extends BaseServiceImpl implements XhHopDongBttService {

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;

    @Autowired
    private XhHopDongBttDtlRepository xhHopDongBttDtlRepository;

    @Autowired
    private XhKqBttHdrRepository xhKqBttHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    @Override
    public Page<XhHopDongBttHdr> searchPage(XhHopDongBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhHopDongBttHdr> page = xhHopDongBttHdrRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        page.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
        });
        return page;
    }

    @Override
    public XhHopDongBttHdr create(XhHopDongBttHdrReq req) throws Exception {
        Optional<XhHopDongBttHdr> qOpHdong = xhHopDongBttHdrRepository.findBySoHd(req.getSoHd());
        if (qOpHdong.isPresent()){
            throw new Exception("Hợp đồng số" + req.getSoHd() + "đã tồn tại");
        }

        Optional<XhKqBttHdr> checkSoQd = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
        if (!checkSoQd.isPresent()){
            throw new Exception("Số quyết định phê duyệt kết quả chào giá " + req.getSoQdKq() + " không tồn tại");
        }else {
            checkSoQd.get().setTrangThaiHd(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
            xhKqBttHdrRepository.save(checkSoQd.get());
        }

        UserInfo userInfo = getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        XhHopDongBttHdr dataMap = new XhHopDongBttHdr();
        BeanUtils.copyProperties(req, dataMap, "id");

        dataMap.setNguoiTaoId(userInfo.getId());
        dataMap.setNgayTao(new Date());
        dataMap.setTrangThai(Contains.DU_THAO);
        dataMap.setMaDvi(userInfo.getDvql());
        dataMap.setMaDviTsan(String.join(",",req.getListMaDviTsan()));

        xhHopDongBttHdrRepository.save(dataMap);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), dataMap.getId(), XhHopDongBttHdr.TABLE_NAME);
        dataMap.setFileDinhKems(fileDinhKem);
        saveDetail(req,dataMap.getId());
        return dataMap;

    }

    void saveDetail(XhHopDongBttHdrReq req, Long idHdr){
        xhHopDongBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhHopDongBttDtlReq dtlReq : req.getChildren()){
            XhHopDongBttDtl dtl = new XhHopDongBttDtl();
            BeanUtils.copyProperties(dtlReq,dtl,"id");
            dtl.setId(null);
            dtl.setIdHdr(idHdr);
            xhHopDongBttDtlRepository.save(dtl);
        }
    }

    @Override
    public XhHopDongBttHdr update(XhHopDongBttHdrReq req) throws Exception {
        UserInfo userInfo = getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhHopDongBttHdr> qOptional = xhHopDongBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if (!qOptional.get().getSoHd().equals(req.getSoHd())) {
            Optional<XhHopDongBttHdr> qOpHdong = xhHopDongBttHdrRepository.findBySoHd(req.getSoHd());
            if (qOpHdong.isPresent())
                throw new Exception("Hợp đồng số " + req.getSoHd() + " đã tồn tại");
        }

        if (!qOptional.get().getSoQdKq().equals(req.getSoQdKq())) {
            Optional<XhKqBttHdr> checkSoQd = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
            if (!checkSoQd.isPresent())
                throw new Exception(
                        "Số quyết định phê duyệt kết quả chào giá " + req.getSoQdKq() + " không tồn tại");
        }

        XhHopDongBttHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req,dataDB, "id");

        dataDB.setNgaySua(new Date());
        dataDB.setNguoiSuaId(userInfo.getId());

        xhHopDongBttHdrRepository.save(dataDB);
        saveDetail(req, dataDB.getId());
        return dataDB;
    }

    @Override
    public XhHopDongBttHdr detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Optional<XhHopDongBttHdr> qOptional = xhHopDongBttHdrRepository.findById(id);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        XhHopDongBttHdr data = qOptional.get();
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
        data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.search(data.getId(), Arrays.asList(XhHopDongBttHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKemList);

        List<XhHopDongBttDtl> allByIdHdr = xhHopDongBttDtlRepository.findAllByIdHdr(id);
        allByIdHdr.forEach(item -> {
            item.setTenDvi(hashMapDvi.get(item.getMaDvi()));
        });
        data.setChildren(allByIdHdr);
        return data;
    }

    @Override
    public XhHopDongBttHdr approve(XhHopDongBttHdrReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = req.getTrangThai() + optional.get().getTrangThai();
        if ((Contains.DAKY + Contains.DUTHAO).equals(status)) {
            optional.get().setNguoiPduyetId(getUser().getId());
            optional.get().setNgayPduyet(getDateTimeNow());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(req.getTrangThai());

        return xhHopDongBttHdrRepository.save(optional.get());
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)){
            throw new Exception("Chỉ được xóa với bản ghi là dự thảo");
        }
        xhHopDongBttHdrRepository.delete(optional.get());
        xhHopDongBttDtlRepository.deleteAllByIdHdr(optional.get().getId());

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if(Objects.isNull(listMulti)){
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        for (Long id: listMulti) {
            this.delete(id);
        }
    }

    @Override
    public void export(XhHopDongBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq=new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhHopDongBttHdr> page = this.searchPage(req);
        List<XhHopDongBttHdr> data= page.getContent();

        String title="Danh sách hợp đồng mua";
        String fileName="danh-sach-hop-dong.xlsx";
        String[] rowsName=new String[]{"STT","Năm KH","QĐ PD KH BTT","QĐ PD KQ chào giá","SL HĐ cần ký","SL HĐ đã ký","Thời hạn xuất kho","Loại hàng hóa","Chủng loại hàng hóa","Tổng giá trị hợp đồng", "Trạng thái ký HĐ", "Trạng thái XH"};
        List<Object[]> dataList=new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            XhHopDongBttHdr hd=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hd.getSoHd();
            objs[2]=hd.getTenHd();
//            objs[3]=hd.getNgayKy();
            objs[4]=hd.getLoaiVthh();
            objs[5]=hd.getCloaiVthh();
            objs[6]=hd.getMaDvi();
//            objs[7]=hd.getIdNthau();
//            objs[8]=hd.getGtriHdSauVat();
            objs[9]=hd.getTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }
}
