package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;







/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */


 @RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
    try {
        Account newAccount = accountService.registerAccount(account);
        return ResponseEntity.ok(newAccount);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account authenticatedAccount = accountService.login(account.getUsername(), account.getPassword());

        if (authenticatedAccount != null) {
            return ResponseEntity.ok(authenticatedAccount);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 254) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (!accountService.accountExists(message.getPostedBy())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Message newMessage = messageService.createMessage(message);
        if (newMessage != null) {
            return ResponseEntity.ok(newMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("message_id") Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable("account_id") Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable("message_id") Integer messageId,
                                              @RequestBody Message messageUpdate) {
        Integer rowsUpdated = messageService.updateMessage(messageId, messageUpdate.getMessageText());

        if (rowsUpdated > 0) {
            return ResponseEntity.ok(rowsUpdated);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rowsUpdated);
        }
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable("message_id") Integer messageId) {
        Integer rowsDeleted = messageService.deleteMessage(messageId);
        if (rowsDeleted == 0) {
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.ok(rowsDeleted);
        }
    }

}