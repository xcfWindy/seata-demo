package com.example.windy.controller;


import com.example.windy.service.AccountService;
import com.example.windy.service.AccountTCCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountTCCService accountTCCService;

    @PutMapping("/{userId}/{money}")
    public ResponseEntity<Void> deduct(@PathVariable("userId") String userId, @PathVariable("money") Integer money){
        //XA,AT
//        accountService.deduct(userId, money);
        //TCC
        accountTCCService.deduct(userId,money);
        return ResponseEntity.noContent().build();
    }
}
