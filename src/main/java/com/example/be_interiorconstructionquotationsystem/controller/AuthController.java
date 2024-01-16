package com.example.be_interiorconstructionquotationsystem.controller;

import com.example.be_interiorconstructionquotationsystem.entity.Account;
import com.example.be_interiorconstructionquotationsystem.entity.CustomUserDetail;
import com.example.be_interiorconstructionquotationsystem.entity.LoginRequest;
import com.example.be_interiorconstructionquotationsystem.form.ChangePasswordForm;
import com.example.be_interiorconstructionquotationsystem.form.CreateAccountForm;
import com.example.be_interiorconstructionquotationsystem.form.ForgetPasswordForm;
import com.example.be_interiorconstructionquotationsystem.security.JwtTokenProvider;
import com.example.be_interiorconstructionquotationsystem.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    private final IAccountService accountService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    //    @PostMapping("/login")
    @PostMapping("/auth/login")
    public String authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println(1);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        System.out.println(2);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Account account = accountService.getAccountByUsername(loginRequest.getUsername());
        String jwt = tokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal());
        String rs = account.getId() + "/" + account.getRole() + "/" + account.getUsername() + "/" + jwt;
        System.out.println(3);
        return rs;
    }

    //    @PostMapping("/register")
    @PostMapping("/auth/register")
    public ResponseEntity<String> registerAccount(@RequestBody CreateAccountForm form) {
        if (accountService.createAccount(form)) {
            return ResponseEntity.ok("Register successfully");
        } else {
            return ResponseEntity.badRequest().body("Username, Email đã tồn tại");
        }

    }

    //    @PostMapping("/forgetpassword")
    @PostMapping("/forgot_password")
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordForm form) throws MessagingException {
        if (accountService.forgetPassword(form) != null) {
            return ResponseEntity.ok("Mật khẩu đã được gửi tới email của bạn");
        }
        return ResponseEntity.badRequest().body("Email không tồn tại");
    }


    @PostMapping("/change_password/{id}")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordForm form, @PathVariable(name = "id") int id) {
        if (accountService.changePassword(id, form)) {
            return ResponseEntity.ok("Change password succsessfully");
        }
        return ResponseEntity.badRequest().body("Password incorect !!!");
    }


}
