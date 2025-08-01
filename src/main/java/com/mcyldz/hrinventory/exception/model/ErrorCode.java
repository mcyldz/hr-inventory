package com.mcyldz.hrinventory.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("ERR-500", "Beklenmedik bir sunucu hatası oluştu."),
    INVALID_REQUEST_PAYLOAD("ERR-400", "İstek formatı veya içeriği geçersiz."),

    PERSONNEL_HAS_ACTIVE_ASSIGNMENTS("ERR-400-1", "Personelin üzerinde aktif zimmet varken bu işlem yapılamaz."),
    INVENTORY_ITEM_ALREADY_ASSIGNED("ERR-400-2", "Envanterdeki eşyanın üzerinde aktif zimmet varken bu işlem yapılamaz."),

    REFRESH_TOKEN_EXPIRED("ERR-401-1", "Refresh token'ın süresi doldu."),
    REFRESH_TOKEN_NOT_FOUND("ERR-401-2", "Refresh token bulunamadı."),

    RESOURCE_NOT_FOUND("ERR-404-1", "İstenen kaynak bulunamadı."),
    USER_NOT_FOUND("ERR-404-2", "Belirtilen ID'ye sahip kullanıcı bulunamadı."),
    ROLE_NOT_FOUND("ERR-404-3", "Belirtilen ID'ye sahip rol bulunamadı."),
    PERSONNEL_NOT_FOUND("ERR-404-4", "Belirtilen ID'ye sahip personel bulunamadı."),
    INVENTORY_ITEM_NOT_FOUND("ERR-404-5", "Belirtilen ID'ye sahip envanter bulunamadı."),
    ASSIGNMENT_NOT_FOUND("ERR-404-6", "Belirtilen ID'ye sahip zimmet işlemi bulunamadı."),
    DEPARTMENT_NOT_FOUND("ERR-404-7", "Belirtilen ID'ye sahip departman bulunamadı."),
    POSITION_NOT_FOUND("ERR-404-8", "Belirtilen ID'ye sahip pozisyon bulunamadı."),
    EDUCATION_LEVEL_NOT_FOUND("ERR-404-9", "Belirtilen ID'ye sahip eğitim düzeyi bulunamadı."),
    INVENTORY_TYPE_NOT_FOUND("ERR-404-10", "Belirtilen ID'ye sahip envanter tipi bulunamadı."),
    INVENTORY_STATUS_NOT_FOUND("ERR-404-11", "Belirtilen ID'ye sahip envanter statüsü bulunamadı."),

    USERNAME_ALREADY_EXISTS("ERR-409-1", "Bu kullanıcı adı zaten kullanılıyor."),
    TCKN_ALREADY_EXISTS("ERR-409-2", "Bu T.C. Kimlik Numarası zaten kayıtlı."),
    SERIAL_NUMBER_ALREADY_EXISTS("ERR-409-3", "Bu seri numarası zaten kayıtlı."),
    ROLE_NAME_ALREADY_EXISTS("ERR-409-4", "Bu rol zaten kayıtlı."),
    INVENTORY_TYPE_NAME_ALREADY_EXISTS("ERR-409-5", "Bu envanter tipi zaten kayıtlı."),
    INVENTORY_STATUS_NAME_ALREADY_EXISTS("ERR-409-6", "Bu envanter statü ismi zaten kayıtlı.");

    private final String code;
    private final String defaultMessage;
}
