package ru.sberbank.ckr.sberboard.increment.entity.enums;

public enum IncrementStateStatus {

    PACKAGE_IN_PROCESS(10),
    TABLE_IN_PROCESS(11),
    PACKAGE_PROCESSED_SUCCESSFULLY(100),
    TABLE_PROCESSED_SUCCESSFULLY(101),
    PACKAGE_ROLLED_BACK(401),
    TABLE_PROCESS_SUCCEDED_BUT_ROLLED_BACK(403),
    TABLE_PROCESS_FAILED_ROLLED_BACK(405);

    public final Integer status;

    private IncrementStateStatus(Integer status) {
        this.status = status;
    }
}