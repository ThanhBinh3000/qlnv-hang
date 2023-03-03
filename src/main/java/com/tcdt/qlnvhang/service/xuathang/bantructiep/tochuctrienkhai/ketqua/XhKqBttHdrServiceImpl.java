package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class XhKqBttHdrServiceImpl extends BaseServiceImpl implements XhKqBttHdrService {

    @Autowired
    private XhKqBttHdrRepository xhKqBttHdrRepository;

    @Autowired
    private XhKqBttDtlRepository xhKqBttDtlRepository;

    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<XhKqBttHdr> searchPage(XhKqBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhKqBttHdr> page = xhKqBttHdrRepository.search(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        page.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiHd()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
            f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
        });
        return page;
    }

    @Override
    public XhKqBttHdr create(XhKqBttHdrReq req) throws Exception {

        if (!StringUtils.isEmpty(req.getSoQdKq())){
            Optional<XhKqBttHdr> qOptional = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
            if (qOptional.isPresent()){
                throw new Exception("Số quyết định " + req.getSoQdKq() + " kết quả chào giá đã tồn tại");
            }
        }

       XhKqBttHdr data = new XhKqBttHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNamKh(LocalDate.now().getYear());
        data.setNgayTao(new Date());
        data.setNguoiTaoId(getUser().getId());
        data.setMaDvi(getUser().getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiHd(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
        data.setTrangThaiXh(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());

        Optional<XhQdPdKhBttDtl> dtl = xhQdPdKhBttDtlRepository.findById(req.getIdPdKhDtl());
        if (dtl.isPresent()){
            dtl.get().setSoQdKq(req.getSoQdKq());
            xhQdPdKhBttDtlRepository.save(dtl.get());
        }
        XhKqBttHdr created =  xhKqBttHdrRepository.save(data);

        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(req.getFileDinhKem()), created.getId(), XhKqBttHdr.TABLE_NAME);
            created.setFileDinhKem(fileDinhKem.get(0));
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhKqBttHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        this.saveDetail(req, data.getId());
        return created;
    }

    List<XhKqBttDtl> saveDetail(XhKqBttHdrReq req, Long idHdr){
        xhKqBttDtlRepository.deleteAllByIdHdr(idHdr);
        List<XhKqBttDtl> xhKqBttDtlArrayList = new ArrayList<>();
        for (XhKqBttDtlReq dtlReq : req.getChildren()){
            XhKqBttDtl dtl = new XhKqBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setId(null);
            dtl.setIdHdr(idHdr);
            XhKqBttDtl create =  xhKqBttDtlRepository.save(dtl);
            if (!DataUtils.isNullObject(dtlReq.getFileDinhKems())) {
                List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(dtlReq.getFileDinhKems()), create.getId(), XhKqBttDtl.TABLE_NAME);
                dtl.setFileDinhKems(fileDinhKem.get(0));
            }
            xhKqBttDtlArrayList.add(dtl);
        }
        return xhKqBttDtlArrayList;
    }

    @Override
    public XhKqBttHdr update(XhKqBttHdrReq req) throws Exception {
        if(ObjectUtils.isEmpty(req.getId())){
          throw new Exception("Không tìn thấy dữ liệu cần sửa");
      }

        Optional<XhKqBttHdr> byId = xhKqBttHdrRepository.findById(req.getId());
      if (!byId.isPresent()){
          throw new Exception("Không tìm thấy dữ liệu");
      }

      XhKqBttHdr data = byId.get();
      BeanUtils.copyProperties(req, data, "id");
      data.setNgaySua(new Date());
      data.setNguoiSuaId(getUser().getId());
      XhKqBttHdr created = xhKqBttHdrRepository.save(data);

     if (!DataUtils.isNullObject(req.getFileDinhKem())){
         List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), created.getId(), XhKqBttHdr.TABLE_NAME);
         data.setFileDinhKem(fileDinhKem.get(0));
     }

     if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
         List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhKqBttHdr.TABLE_NAME);
         data.setFileDinhKems(fileDinhKems);
     }

      this.saveDetail(req, data.getId());
      return created;
    }

    @Override
    public XhKqBttHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhKqBttHdr> byId = xhKqBttHdrRepository.findById(id);
        if (!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        XhKqBttHdr data = byId.get();

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiHd()));
        data.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiXh()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));

        List<XhHopDongBttHdr> allById = xhHopDongBttHdrRepository.findAllByIdQdKq(id);
        allById.forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
        });

        List<XhKqBttDtl> ByIdDtl = xhKqBttDtlRepository.findAllByIdHdr(id);
        for (XhKqBttDtl dtl : ByIdDtl){
                List<FileDinhKem> fileDinhKems = fileDinhKemService.search(dtl.getId(), Arrays.asList(XhKqBttDtl.TABLE_NAME));
            if (!DataUtils.isNullOrEmpty(fileDinhKems)) {
                dtl.setFileDinhKems(fileDinhKems.get(0));
            }
        }
        data.setChildren(ByIdDtl);
        data.setListHopDongBtt(allById);

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhKqBttHdr.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        data.setFileDinhKems(fileDinhKem);
        return data;
    }

    @Override
    public XhKqBttHdr approve(XhKqBttHdrReq req) throws Exception {
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(req.getId());
        if(!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        XhKqBttHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        if(req.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId())
                && data.getTrangThaiHd().equals(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId()))
        {
            data.setTrangThaiHd(req.getTrangThai());
        } else {
            switch (status) {
                case Contains.CHODUYET_TP + Contains.DUTHAO:
                case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                    data.setNguoiGuiDuyetId(userInfo.getId());
                    data.setNgayGuiDuyet(getDateTimeNow());
                case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
                case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                    data.setNguoiPduyetId(userInfo.getId());
                    data.setNgayPduyet(getDateTimeNow());
                    data.setLyDoTuChoi(req.getLyDoTuChoi());
                    break;
                case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                case Contains.BAN_HANH + Contains.DADUYET_LDC:
                    data.setNguoiPduyetId(userInfo.getId());
                    data.setNgayPduyet(getDateTimeNow());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            data.setTrangThai(req.getTrangThai());
        }
        return xhKqBttHdrRepository.save(data);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<XhKqBttHdr> byId = xhKqBttHdrRepository.findById(id);
        if (!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        if (!byId.get().getTrangThai().equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())){
            throw new Exception("Chỉ được xóa bản ghi khi ở trạng thái là dự thảo");
        }
        List<XhKqBttDtl> dtlList = xhKqBttDtlRepository.findAllByIdHdr(id);
        for (XhKqBttDtl dtl : dtlList){
            fileDinhKemService.delete(dtl.getId(), Collections.singleton(XhKqBttDtl.TABLE_NAME));
        }
        xhKqBttDtlRepository.deleteAllByIdHdr(byId.get().getId());
        xhKqBttHdrRepository.delete(byId.get());
        fileDinhKemService.delete(byId.get().getId(), Collections.singleton(XhKqBttHdr.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (ObjectUtils.isEmpty(listMulti)){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        listMulti.forEach(item ->{
            try {
                this.delete(item);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void export(XhKqBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhKqBttHdr> page = this.searchPage(req);
        List<XhKqBttHdr> data = page.getContent();
        String title = " Danh sách quyết định phê duyệt kết quả chào giá";
        String[] rowsName = new String[]{"STT", "Số QĐ PDKQ chào giá", "Ngày ký QĐ", "Đơn vị", "Số QĐ PDKH bán trực tiếp", "Loại hàng hóa", "Chủng loại hành hóa", "Trạng thái"};
        String fileName = "danh-sach-dx-pd-kq-chao-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhKqBttHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdKq();
            objs[2] = hdr.getNgayKy();
            objs[3] = hdr.getTenDvi();
            objs[4] = hdr.getSoQdPd();
            objs[5] = hdr.getTenLoaiVthh();
            objs[6] = hdr.getTenCloaiVthh();
            objs[7] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

}
