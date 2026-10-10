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
}
