package com.dinechain.service;

import com.dinechain.entity.StoredImageEntity;

import java.math.BigDecimal;
import java.util.Optional;

public interface SettingService {
    BigDecimal getVatPercent();
    BigDecimal updateVatPercent(Integer staffId, BigDecimal percent);
    // Link to the image of the chain's bank QR code that web takeaway guests transfer to; null when not set
    String getTransferQrUrl();
    String updateTransferQrUrl(Integer staffId, String url);
    // Stores an uploaded QR image and points the transfer QR at it; returns the new link
    String uploadTransferQr(Integer staffId, String contentType, byte[] data);
    // The uploaded QR image, if any: [contentType, bytes]
    Optional<StoredImageEntity> getTransferQrImage();
    // The chain's account guests transfer to; each order then gets its own QR with amount and order code. null when not set
    BankAccount getBankAccount();
    BankAccount updateBankAccount(Integer staffId, BankAccount account);
    // Guests can pick bank transfer when there is a bank account or a QR image
    boolean transferAvailable();

    // bank: the bank's short name as SePay/VietQR know it (MBBank, Vietcombank, ACB...)
    record BankAccount(String bank, String accountNumber, String accountName) {
    }
}
