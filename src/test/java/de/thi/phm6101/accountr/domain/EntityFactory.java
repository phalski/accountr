package de.thi.phm6101.accountr.domain;


import java.util.*;

public class EntityFactory {

    static final private Random RANDOM = new Random();

    static public Account newAccount() {
        Account account = new Account();
        account.setName(String.format("Name - %s", UUID.randomUUID().toString()));
        account.setDescription(String.format("Description - %s", UUID.randomUUID().toString()));
        return account;
    }

    static public Transaction newTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDescription(String.format("Description - %s", UUID.randomUUID().toString()));
        transaction.setAmount(0 + RANDOM.nextDouble() * Double.MAX_VALUE);
        transaction.setDate(new Date(Math.abs(System.currentTimeMillis() - RANDOM.nextLong())));
        return transaction;
    }

    static public User newUser() {
        User user = new User();
        user.setName(String.format("Name - %s", UUID.randomUUID().toString()));
        user.setPassword(String.format("Password - %s", UUID.randomUUID().toString()));
        return user;
    }
}
