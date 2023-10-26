package com.basak.dalcom.spring.security.service;

import com.basak.dalcom.domain.accounts.data.Account;
import com.basak.dalcom.domain.accounts.data.AccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public DalcomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findById(Integer.parseInt(userId));
        if (account.isPresent()) {
            return new DalcomUserDetails(account.get());
        } else {
            return new DalcomUserDetails();
        }
    }

    Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }
}
