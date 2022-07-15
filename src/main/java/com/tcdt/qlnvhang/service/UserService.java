package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserActionRepository;
import com.tcdt.qlnvhang.repository.UserHistoryRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.table.UserAction;
import com.tcdt.qlnvhang.table.UserHistory;
import com.tcdt.qlnvhang.table.UserInfo;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserInfoRepository userRepository;
	@Autowired
	UserHistoryRepository userHistoryRepository;

	@Autowired
	UserActionRepository userActionRepository;

	@Autowired
	private QlnvDmDonviRepository qlnvDmDonviRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserInfo user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		QlnvDmDonvi dvi = qlnvDmDonviRepository.findByMaDvi(user.getDvql());
		user.setMaQd(dvi.getMaQd());
		user.setMaTr(dvi.getMaTr());
		user.setMaKhqlh(dvi.getMaKhqlh());
		user.setMaKtbq(dvi.getMaKtbq());
		user.setMaTckt(dvi.getMaTckt());
		user.setCapDvi(dvi.getCapDvi());
		user.setTenDvi(dvi.getTenDvi());
		user.setMaPBb(dvi.getMaPBb());
		return new CustomUserDetails(user);
	}

	public Iterable<UserAction> findAll() {
		return userActionRepository.findAll();
	}

	public void saveUserHistory(UserHistory userHistory) {
		userHistoryRepository.save(userHistory);
	}

}