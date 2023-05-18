package com.tcdt.qlnvhang.service.xuathang.bantructiep.hopdong.bangkebanle;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.bangkebanle.XhBangKeBtt;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.bangkebanle.XhBangKeBttRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.bangkebanle.XhBangKeBttReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhBangKeBttServiceImpl extends BaseServiceImpl implements XhBangKeBttService {

    @Autowired
    private XhBangKeBttRepository xhBangKeBttRepository;

    @Override
    public Page<XhBangKeBtt> searchPage(XhBangKeBttReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhBangKeBtt> data = xhBangKeBttRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{

            if (hashMapDvi.get((f.getMaDvi())) != null) {
                f.setTenDvi(hashMapVthh.get(f.getMaDvi()));
            }
            if (hashMapVthh.get((f.getLoaiVthh())) !=null){
                f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            }
            if (hashMapVthh.get((f.getCloaiVthh())) !=null){
                f.setTenCloaiVthh(hashMapVthh.get(f.getTenCloaiVthh()));
            }
        });
        return data;
    }

    @Override
    public XhBangKeBtt create(XhBangKeBttReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        if (!StringUtils.isEmpty(req.getSoBangKe())){
            Optional<XhBangKeBtt> qOptional = xhBangKeBttRepository.findBySoBangKe(req.getSoBangKe());
            if (qOptional.isPresent()){
                throw new Exception("Số bảng kê" + req.getSoBangKe() + " đã tồn tại");
            }
        }
        XhBangKeBtt data = new XhBangKeBtt();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(userInfo.getId());
        xhBangKeBttRepository.save(data);
        return data;
    }

    @Override
    public XhBangKeBtt update(XhBangKeBttReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }
        Optional<XhBangKeBtt> qOptional = xhBangKeBttRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        if (!StringUtils.isEmpty(req.getSoBangKe())){
            Optional<XhBangKeBtt> bangKeBtt = xhBangKeBttRepository.findBySoBangKe(req.getSoBangKe());
            if (bangKeBtt.isPresent()){
                if (!bangKeBtt.get().getId().equals(req.getId())){
                    throw new Exception("Số bảng kê" + req.getSoBangKe() + "đã tồn tại");
                }
            }
        }
        XhBangKeBtt data = qOptional.get();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(userInfo.getId());
        xhBangKeBttRepository.save(data);
        return data;
    }

    @Override
    public XhBangKeBtt detail(Long id) throws Exception {
        Optional<XhBangKeBtt> qOptional = xhBangKeBttRepository.findById(id);
        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }
        XhBangKeBtt data = qOptional.get();
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        return data;
    }

    @Override
    public XhBangKeBtt approve(XhBangKeBttReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
        }
        Optional<XhBangKeBtt> optional = xhBangKeBttRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        xhBangKeBttRepository.delete(optional.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)) {
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }
        List<XhBangKeBtt> list = xhBangKeBttRepository.findByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception(" Không tìm thấy dữ liệu cần xóa");
        }
        for (XhBangKeBtt bangKeBtt : list){
            this.delete(bangKeBtt.getId());
        }
    }

    @Override
    public void export(XhBangKeBttReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhBangKeBtt> page = this.searchPage(req);
        List<XhBangKeBtt> data = page.getContent();
        String title="Danh sách bảng kê bán lẻ";
        String[] rowsName = new String[]{"STT","Năm kế hoạch", "Số bảng kê", "Số quyết định", "Tên người mua", "Địa chỉ", "Số CMT/CCCD", "Loại hàng hóa", "Chủng loại hàng hóa", "Số lượng", "Đơn giá", "Thanh tiền"};
        String filename="danh-sach-dx-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhBangKeBtt hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getNamKh();
            objs[2]=hdr.getSoBangKe();
            objs[3]=hdr.getSoQd();
            objs[4]=hdr.getTenNguoiMua();
            objs[5]=hdr.getDiaChi();
            objs[6]=hdr.getCmt();
            objs[7]=hdr.getTenLoaiVthh();
            objs[8]=hdr.getTenCloaiVthh();
            objs[9]=hdr.getSoLuong();
            objs[10]=hdr.getDonGia();
            objs[11]=hdr.getThanhTien();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
