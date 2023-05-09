package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.thongbaokqhoso;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.thongbaokqhoso.XhTlThongBaoKqHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.thongbaokqhoso.XhTlThongBaoKqHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.thongbaokqhoso.XhTlThongBaoKqHdr;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class XhTlThongBaoKqServiceImpl extends BaseServiceImpl implements XhTlThongBaoKqService {

    @Autowired
    private XhTlThongBaoKqHdrRepository xhTlThongBaoKqHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<XhTlThongBaoKqHdr> searchPage(XhTlThongBaoKqHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
       Page<XhTlThongBaoKqHdr> data = xhTlThongBaoKqHdrRepository.searchPage(
               req,
               pageable);
       return data;
    }

    @Override
    public XhTlThongBaoKqHdr create(XhTlThongBaoKqHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if(!StringUtils.isEmpty(req.getSoThongBao())){
            Optional<XhTlThongBaoKqHdr> qOptional = xhTlThongBaoKqHdrRepository.findBySoThongBao(req.getSoThongBao());
            if (qOptional.isPresent()){
                throw new Exception("Số thông báo " + req.getSoThongBao() + " đã tồn tại");
            }
        }

        XhTlThongBaoKqHdr data = new XhTlThongBaoKqHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNguoiTaoId(userInfo.getId());
        data.setNgayTao(getDateTimeNow());
        data.setMaDvi(userInfo.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        XhTlThongBaoKqHdr created = xhTlThongBaoKqHdrRepository.save(data);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhTlThongBaoKqHdr.TABLE_NAME);
            created.setFileDinhKem(fileDinhKemList);
        }
        return created;
    }

    @Override
    public XhTlThongBaoKqHdr update(XhTlThongBaoKqHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sửa thất bại, khoobng tìm thấy dữ liệu");
        }

        Optional<XhTlThongBaoKqHdr> qOptional = xhTlThongBaoKqHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if (!StringUtils.isEmpty(req.getFileDinhKem())){
            Optional<XhTlThongBaoKqHdr> hdr = xhTlThongBaoKqHdrRepository.findBySoThongBao(req.getSoThongBao());
            if (hdr.isPresent()){
                if (!hdr.get().getId().equals(req.getId())){
                    throw new Exception("Số thông báo " + req.getSoThongBao() + " đã tồn tại");
                }
            }
        }

        XhTlThongBaoKqHdr data = qOptional.get();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(getDateTimeNow());
        data.setNguoiSuaId(userInfo.getId());
        XhTlThongBaoKqHdr created = xhTlThongBaoKqHdrRepository.save(data);
        fileDinhKemService.delete(created.getId(), Collections.singleton(XhTlThongBaoKqHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhTlThongBaoKqHdr.TABLE_NAME);
        data.setFileDinhKem(fileDinhKemList);
        return created;
    }

    @Override
    public XhTlThongBaoKqHdr detail(Long id) throws Exception {
       Optional<XhTlThongBaoKqHdr> optional = xhTlThongBaoKqHdrRepository.findById(id);
       if (!optional.isPresent()){
           throw new UnsupportedOperationException("Không tồn tại bản ghi");
       }

       XhTlThongBaoKqHdr data = optional.get();
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenTrangThaiThanhLy(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiThanhLy()));

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlThongBaoKqHdr.TABLE_NAME));
        data.setFileDinhKem(fileDinhKems);
        return data;
    }

    @Override
    public XhTlThongBaoKqHdr approve(XhTlThongBaoKqHdrReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<XhTlThongBaoKqHdr> optional = xhTlThongBaoKqHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = req.getTrangThai() + optional.get().getTrangThai();
        if ((Contains.DA_HOAN_THANH + Contains.DUTHAO).equals(status)) {
            optional.get().setNguoiPduyetId(getUser().getId());
            optional.get().setNgayPduyet(getDateTimeNow());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(req.getTrangThai());
        return xhTlThongBaoKqHdrRepository.save(optional.get());
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
        }

        Optional<XhTlThongBaoKqHdr> optional = xhTlThongBaoKqHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
        }
        xhTlThongBaoKqHdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhTlThongBaoKqHdr.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)) {
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }

        List<XhTlThongBaoKqHdr> hdrList = xhTlThongBaoKqHdrRepository.findByIdIn(listMulti);
        if (hdrList.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (XhTlThongBaoKqHdr hdr : hdrList){
            this.delete(hdr.getId());
        }
    }

    @Override
    public void export(XhTlThongBaoKqHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhTlThongBaoKqHdr> page = this.searchPage(req);
        List<XhTlThongBaoKqHdr> data = page.getContent();
        String title="Danh sách thông báo kết quả trình hồ sơ thanh lý";
        String[] rowsName = new String[]{"STT","Số thông báo KQ trình hồ sơ", "Trích yếu", "Ngày ký", "Số hồ sơ đề nghị thanh lý", "Trạng thái"};
        String filename="danh-sach-thong-bao-ket-qua-trinh-ho-so-thanh-ly.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhTlThongBaoKqHdr hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getSoThongBao();
            objs[2]=hdr.getTrichYeu();
            objs[3]=hdr.getNgayPduyet();
            objs[4]=hdr.getSoHoSo();
            objs[5]=hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
