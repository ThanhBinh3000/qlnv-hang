package com.tcdt.qlnvhang.service.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class XhQdDchinhKhBttServiceImpl extends BaseServiceImpl implements XhQdDchinhKhBttService {

    @Autowired
    private XhQdDchinhKhBttHdrRepository xhQdDchinhKhBttHdrRepository;

    @Autowired
    private XhQdDchinhKhBttDtlRepository xhQdDchinhKhBttDtlRepository;

    @Autowired
    private XhQdDchinhKhBttSlRepository xhQdDchinhKhBttSlRepository;

    @Autowired
    private XhQdDchinhKhBttSlDtlRepository xhQdDchinhKhBttSlDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<XhQdDchinhKhBttHdr> searchPage(XhQdDchinhKhBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdDchinhKhBttHdr> data = xhQdDchinhKhBttHdrRepository.searchPage(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhQdDchinhKhBttHdr create(XhQdDchinhKhBttHdrReq req) throws Exception {

        UserInfo userInfo = SecurityContextService.getUser();
        List<XhQdDchinhKhBttHdr> checkSoQd = xhQdDchinhKhBttHdrRepository.findBySoQdDc(req.getSoQdDc());
        if (!checkSoQd.isEmpty()){
            throw new Exception("Số quyết định" + req.getSoQdDc() + "đã tồn tại");
        }

        XhQdDchinhKhBttHdr dataMap = ObjectMapperUtils.map(req, XhQdDchinhKhBttHdr.class);
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setNgayTao(new Date());
        dataMap.setNguoiTaoId(userInfo.getId());
        XhQdDchinhKhBttHdr created = xhQdDchinhKhBttHdrRepository.save(dataMap);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), dataMap.getId(), XhQdDchinhKhBttHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(),dataMap.getId(), XhQdDchinhKhBttHdr.TABLE_NAME);
        created.setCanCuPhapLy(canCuPhapLy);

        saveDetail(req, dataMap.getId());
        return dataMap;
    }

    void saveDetail( XhQdDchinhKhBttHdrReq req, Long idDcHdr){
        xhQdDchinhKhBttDtlRepository.deleteAllByIdDcHdr(idDcHdr);
        for (XhQdDchinhKhBttDtlReq dtlReq : req.getChildren()){
            XhQdDchinhKhBttDtl dtl =  ObjectMapperUtils.map(dtlReq, XhQdDchinhKhBttDtl.class);
            dtl.setId(null);
            dtl.setIdDcHdr(idDcHdr);
            xhQdDchinhKhBttDtlRepository.save(dtl);
            xhQdDchinhKhBttSlRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhQdDchinhKhBttSlReq slReq : dtlReq.getChildren()){
                XhQdDchinhKhBttSl sl =  ObjectMapperUtils.map(slReq, XhQdDchinhKhBttSl.class);
                sl.setId(null);
                sl.setIdDtl(dtl.getId());
                xhQdDchinhKhBttSlRepository.save(sl);
                xhQdDchinhKhBttSlDtlRepository.deleteAllByIdSl(slReq.getId());
                for (XhQdDchinhKhBttSlDtlReq slDtlReq : slReq.getChildren()){
                    XhQdDchinhKhBttSlDtl slDtl =  ObjectMapperUtils.map(slDtlReq, XhQdDchinhKhBttSlDtl.class);
                    slDtl.setId(null);
                    slDtl.setIdSl(sl.getId());
                    xhQdDchinhKhBttSlDtlRepository.save(slDtl);
                }
            }
        }
    }

    @Override
    public XhQdDchinhKhBttHdr update(XhQdDchinhKhBttHdrReq req) throws Exception {
       if (StringUtils.isEmpty(req.getId())){
           throw new Exception("Sủa thất bại không tìm thấy dữ liệu");
       }

        Optional<XhQdDchinhKhBttHdr> qOptional = xhQdDchinhKhBttHdrRepository.findById(req.getId());

       if (!qOptional.isPresent()){
           throw new UnsupportedOperationException("Không tồn tại bản ghi");
       }

       XhQdDchinhKhBttHdr data = qOptional.get();

        BeanUtils.copyProperties(req,data,"id");
        data.setNgaySua(new Date());
        data.setNguoiSuaId(getUser().getId());
        xhQdDchinhKhBttHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), data.getId(), XhQdDchinhKhBttHdr.TABLE_NAME);
        data.setFileDinhKems(fileDinhKems);
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), data.getId(), XhQdDchinhKhBttHdr.TABLE_NAME);
        data.setCanCuPhapLy(canCuPhapLy);
        saveDetail(req, data.getId());
        return data;
    }

    @Override
    public XhQdDchinhKhBttHdr detail(Long id) throws Exception {
       if (StringUtils.isEmpty(id)){
           throw new Exception("Bản ghi không tồm tại");
       }

       Optional<XhQdDchinhKhBttHdr> qOptional = xhQdDchinhKhBttHdrRepository.findById(id);

       if (!qOptional.isPresent()){
           throw new UnsupportedOperationException("Bản ghi không tồn tại");
       }

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        XhQdDchinhKhBttHdr hdr = qOptional.get();

        List<XhQdDchinhKhBttDtl> xhQdDchinhKhBttDtlList = new ArrayList<>();
        for (XhQdDchinhKhBttDtl dtl : xhQdDchinhKhBttDtlRepository.findAllByIdDcHdr(id)){
            List<XhQdDchinhKhBttSl> xhQdDchinhKhBttSlList = new ArrayList<>();
            for (XhQdDchinhKhBttSl sl : xhQdDchinhKhBttSlRepository.findByIdDtl(dtl.getId())){
                List<XhQdDchinhKhBttSlDtl> xhQdDchinhKhBttSlDtlList = xhQdDchinhKhBttSlDtlRepository.findByIdSl(sl.getId());
                xhQdDchinhKhBttSlDtlList.forEach(f->{
                    f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
                    f.setTenNhakho(hashMapDvi.get(f.getMaNhaKho()));
                    f.setTenNganKho(hashMapDvi.get(f.getMaNganKho()));
                    f.setTenLoKho(hashMapDvi.get(f.getMaLoKho()));
                });
                sl.setTenDvi(hashMapDvi.get(sl.getMaDvi()));
                sl.setChildren(xhQdDchinhKhBttSlDtlList);
                xhQdDchinhKhBttSlList.add(sl);
            }
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : hashMapDvi.get(dtl.getMaDvi()));
            dtl.setChildren(xhQdDchinhKhBttSlList);
            xhQdDchinhKhBttDtlList.add(dtl);
        }
        hdr.setChildren(xhQdDchinhKhBttDtlList);
        hdr.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(hdr.getTrangThai()));
        hdr.setTenDvi(StringUtils.isEmpty(hdr.getMaDvi())? null : hashMapDvi.get(hdr.getMaDvi()));
        hdr.setTenLoaiVthh(StringUtils.isEmpty(hdr.getLoaiVthh())? null : hashMapVthh.get(hdr.getLoaiVthh()));
        hdr.setTenCloaiVthh(StringUtils.isEmpty(hdr.getCloaiVthh())? null : hashMapVthh.get(hdr.getCloaiVthh()));
        return hdr;
    }

    @Override
    public XhQdDchinhKhBttHdr approve(XhQdDchinhKhBttHdrReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhQdDchinhKhBttHdr detail = detail(req.getId());
        String status = req.getTrangThai() + detail.getTrangThai();
        switch (status){
            case Contains.CHODUYET_LDV + Contains.DUTHAO:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                detail.setNguoiGuiDuyetId(getUser().getId());
                detail.setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
                detail.setNguoiPduyetId(getUser().getId());
                detail.setNgayPduyet(getDateTimeNow());
                detail.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
            case Contains.BAN_HANH + Contains.DADUYET_LDV:
                detail.setNguoiPduyetId(getUser().getId());
                detail.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        detail.setTrangThai(req.getTrangThai());
        XhQdDchinhKhBttHdr createCheck = xhQdDchinhKhBttHdrRepository.save(detail);
        return createCheck;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhQdDchinhKhBttHdr> optional = xhQdDchinhKhBttHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        XhQdDchinhKhBttHdr hdr = optional.get();

        if (!hdr.getTrangThai().equals(Contains.DUTHAO)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
        }

        List<XhQdDchinhKhBttDtl> xhQdDchinhKhBttDtlList = xhQdDchinhKhBttDtlRepository.findAllByIdDcHdr(hdr.getId());
        if (!CollectionUtils.isEmpty(xhQdDchinhKhBttDtlList)){
            for (XhQdDchinhKhBttDtl dtl : xhQdDchinhKhBttDtlList) {
                List<XhQdDchinhKhBttSl> byIdDcDtl = xhQdDchinhKhBttSlRepository.findByIdDtl(dtl.getId());
                for (XhQdDchinhKhBttSl sl : byIdDcDtl){
                    xhQdDchinhKhBttSlDtlRepository.deleteAllByIdSl(sl.getId());
                }
                xhQdDchinhKhBttSlRepository.deleteAllByIdDtl(dtl.getId());
            }
            xhQdDchinhKhBttDtlRepository.deleteAll(xhQdDchinhKhBttDtlList);
        }
        xhQdDchinhKhBttHdrRepository.delete(hdr);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)){
            throw new Exception("Xóa thất bại, Không tìm thấy dữ liệu");
        }

        List<XhQdDchinhKhBttHdr> list = xhQdDchinhKhBttHdrRepository.findByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (XhQdDchinhKhBttHdr hdr : list){
            this.delete(hdr.getId());
        }

    }

    @Override
    public void export(XhQdDchinhKhBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdDchinhKhBttHdr> page = this.searchPage(req);
        List<XhQdDchinhKhBttHdr> data = page.getContent();

        String title = "Danh sách điều chỉnh kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Số QĐ điều chỉnh KH bán trực tiếp", "Ngày ký QĐ điều chỉnh", "Số quyết định gốc", "Trích yếu", "Loaih hàng hóa", "Chủng loại hàng hóa", "Trạng thái"};
        String filename = "danh-sach-dieu-chinh-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhQdDchinhKhBttHdr hdr =data.get(i);
            objs[0]=i;
            objs[1]=hdr.getNamKh();
            objs[2]=hdr.getSoQdDc();
            objs[3]=hdr.getNgayPduyet();
            objs[4]=hdr.getSoQdGoc();
            objs[5]=hdr.getTrichYeu();
            objs[6]=hdr.getTenLoaiVthh();
            objs[7]=hdr.getTenCloaiVthh();
            objs[8]=hdr.getTenTrangThai();
           dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
