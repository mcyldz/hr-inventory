package com.mcyldz.hrinventory.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("ERR-500", "Beklenmedik bir sunucu hatası oluştu."),
    INVALID_REQUEST_PAYLOAD("ERR-400", "İstek formatı veya içeriği geçersiz."),

    PERSONNEL_HAS_ACTIVE_ASSIGNMENTS("ERR-400-1", "Personelin üzerinde aktif zimmet varken bu işlem yapılamaz."),

    RESOURCE_NOT_FOUND("ERR-404-1", "İstenen kaynak bulunamadı."),
    USER_NOT_FOUND("ERR-404-2", "Belirtilen ID'ye sahip kullanıcı bulunamadı."),
    ROLE_NOT_FOUND("ERR-404-3", "Belirtilen ID'ye sahip rol bulunamadı."),
    PERSONNEL_NOT_FOUND("ERR-404-4", "Belirtilen ID'ye sahip personel bulunamadı."),
    INVENTORY_ITEM_NOT_FOUND("ERR-404-5", "Belirtilen ID'ye sahip envanter bulunamadı."),

    USERNAME_ALREADY_EXISTS("ERR-409-1", "Bu kullanıcı adı zaten kullanılıyor."),
    TCKN_ALREADY_EXISTS("ERR-409-2", "Bu T.C. Kimlik Numarası zaten kayıtlı."),
    SERIAL_NUMBER_ALREADY_EXISTS("ERR-409-3", "Bu seri numarası zaten kayıtlı."),
    ROLE_NAME_ALREADY_EXISTS("ERR-409-4", "Bu rol zaten kayıtlı.");

    private final String code;
    private final String defaultMessage;
}
