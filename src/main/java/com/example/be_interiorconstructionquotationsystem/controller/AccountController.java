package com.example.be_interiorconstructionquotationsystem.controller;


import com.example.be_interiorconstructionquotationsystem.DTO.AccountDTO;
import com.example.be_interiorconstructionquotationsystem.entity.Account;
import com.example.be_interiorconstructionquotationsystem.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AccountController {
    private final IAccountService accountService;
    private final ModelMapper modelMapper;

    @GetMapping("/admin/users")
    public List<Account> getAllAccount(){
        return accountService.getAllAccount();
    }

    @GetMapping("account/{id}")
    public AccountDTO getAccountById(@PathVariable("id") int id) {
        Account account =  accountService.getAccountById(id);
//        return account;
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        return accountDTO;
    }
}
