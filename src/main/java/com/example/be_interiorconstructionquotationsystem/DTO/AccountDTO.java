package com.example.be_interiorconstructionquotationsystem.DTO;

import com.example.be_interiorconstructionquotationsystem.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDTO {
    private String username;
    private String fullname;
    private Account.GENDER gender;
    private Date birthdate;
    private String phoneNumber;
    private String email;
    private String address;
    private Account.ROLE role;
}
