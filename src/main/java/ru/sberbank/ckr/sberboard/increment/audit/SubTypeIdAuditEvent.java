package ru.sberbank.ckr.sberboard.increment.audit;

public enum SubTypeIdAuditEvent {
    A0("A0", "Аутентификация ТУЗ/УЗ",
                "входы пользователей в АС, так и выходы из нее попытки неправильного ввода пароля или превышения полномочий, с указанием IP адреса/имени компьютера;\n"
            + "все атрибуты пользователя, сессия которого была автоматически завершена по истечении установленного периода времени;\n"
            + "запросы на получение и использование специальных полномочий (например, на редактирование БД, на сторнирование операций), а также запросы любых дополнительных подтверждений с достаточной степенью детализации;"),
    B0("B0", "Операции над правами доступа или объекта",
            "изменение прав доступа;\n"
            + "события, связанные с действиями пользователей по регистрации клиентов;"),
    C0("C0", "Операции над данными/файлами данных",
            "Чтение\n"
            + "Запись\n"
            + "удаление"),
    D0("D0", "Действия с журналом аудита",
            "Попытки удаления журнала\n"
            + "попытка редактировать журнал аудита"),
    F0("F0", "Остальные действия", "изменения параметров и системных настроек АС с указанием старых и новых значений параметров и настроек;\n"
            + "события связанные с бизнес-сущностями, а не с объектами БД;\n"
            + "результат (успешный/неуспешный) события;\n"
            + "аудит сообщений, полученных соответствующим веб-сервисом включая: время, идентификатор АС, отправившей сообщение, IP-адрес сервера-отправителя, запрошенная операция и её результат, а также результат проверки контрольного значения процедуры ключевания или ЭП;\n"
            + "отрицательные результаты проверок целостности данных в АС;\n" + "все случаи недоступности интерфейсов составных частей АС;");

    private String id;
    private String description;
    private String fullDescription;

    SubTypeIdAuditEvent(String id, String description, String fullDescription) {
        this.id = id;
        this.description = description;
        this.fullDescription = fullDescription;
    }
}
