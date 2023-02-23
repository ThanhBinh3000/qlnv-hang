package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
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
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;

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
        XhKqBttHdr bySoDxuat = xhKqBttHdrRepository.findBySoQdPd(req.getSoQdPd());
        if (!ObjectUtils.isEmpty(bySoDxuat)){
            throw new Exception("Số quyết định phê duyệt kế hoạch đã được quyết định kết quả bán trực tiếp, xin vui lòng chọn số quyết định khác");
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), data.getId(), XhKqBttHdr.TABLE_NAME);
        data.setFileDinhKems(fileDinhKems);
        Optional<XhQdPdKhBttHdr> dtl = xhQdPdKhBttHdrRepository.findById(req.getIdHdr());
        dtl.get().setSoQdKq(req.getSoQdKq());
        xhQdPdKhBttHdrRepository.save(dtl.get());
        xhKqBttHdrRepository.save(data);
        return data;
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
      List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(),data.getId(), XhKqBttHdr.TABLE_NAME);
      created.setFileDinhKems(fileDinhKems);
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
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhKqBttHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);
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

        String title="Danh sách quyết định phê duyệt kết quả chào giá";
        String[] rowsName = new String[]{"STT", "Số QĐ PDKQ chào giá", "Ngày ký QĐ", "Đơn vị", "Số QĐ PDKH bán trực tiếp", "Loại hàng hóa", "Chủng loại hàng hóa", "Trạng thái"};
        String filename="danh-sach-quyet-dinh-phe-duyet-ket-qua-chao-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhKqBttHdr hdr =data.get(i);
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
        ExportExcel exportExcel =  new ExportExcel(title, filename, rowsName, dataList, response);
        exportExcel.export();
    }
}
