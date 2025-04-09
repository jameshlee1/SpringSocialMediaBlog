package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account) throws Exception {
        
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new Exception("Username cannot be null or empty");
        }
        
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new Exception("Password must be at least 4 characters long");
        }
    
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            throw new Exception("Account already exists");
        }
        return accountRepository.save(account);
    }
    
    public Account login(String username, String password) {
        Account account = accountRepository.findByUsername(username);

        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }


    public boolean accountExists(Integer accountId) {
        return accountRepository.existsById(accountId);
    }
}