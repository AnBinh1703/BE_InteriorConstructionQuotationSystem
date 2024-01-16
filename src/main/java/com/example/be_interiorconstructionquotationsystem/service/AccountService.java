package com.example.be_interiorconstructionquotationsystem.service;

import com.example.be_interiorconstructionquotationsystem.entity.Account;
import com.example.be_interiorconstructionquotationsystem.entity.CustomUserDetail;
import com.example.be_interiorconstructionquotationsystem.form.ChangePasswordForm;
import com.example.be_interiorconstructionquotationsystem.form.CreateAccountForm;
import com.example.be_interiorconstructionquotationsystem.form.ForgetPasswordForm;
import com.example.be_interiorconstructionquotationsystem.repository.IAccountRepository;
import com.example.be_interiorconstructionquotationsystem.utils.EmailSender;
import com.example.be_interiorconstructionquotationsystem.utils.RamdomNewPassword;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final IAccountRepository accountRepository;
    private final ModelMapper modelMapper;
    @Override
    public Account getAccountById(int id) {
        return accountRepository.findById(id).get();    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public boolean createAccount(CreateAccountForm form) {
        Account account = modelMapper.map(form, Account.class);
        account.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        account.setRole(Account.ROLE.User);
        if (accountRepository.existsByUsername(account.getUsername()) || accountRepository.existsByEmail(account.getEmail())) {
            return false;
        }else {
            accountRepository.save(account);
            return true;
        }
    }

    @Override
    public String forgetPassword(ForgetPasswordForm form) throws MessagingException {
        if(!accountRepository.existsByEmail(form.getEmail())) {
            return null;
        }
        Account account = accountRepository.findByEmail(form.getEmail());
        System.out.println(account.toString());
        String newPass = RamdomNewPassword.RamdomPass();
        System.out.println(newPass);
        account.setPassword(new BCryptPasswordEncoder().encode(newPass));
        accountRepository.save(account);
        EmailSender.sendNewPassword(form.getEmail(), newPass);
        return newPass;
    }

    @Override
    @Transactional
    public boolean changePassword(int id, ChangePasswordForm form) {
        Account account = accountRepository.findById(id).get();
        if (BCrypt.checkpw(form.getOldPassword(), account.getPassword())) {
            account.setPassword(new BCryptPasswordEncoder().encode(form.getNewPassword()));
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetail(account);

    }



}
