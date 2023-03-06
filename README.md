# Описание
Запросы: 
1)	Регистрация нового пользователя

Путь: localhost:8080/user/registrate
JSON (запрос): 
{
    "username": "vasya_vez",
    "email": "vasyu_ko@mail.ru"
}

JSON (ответ):
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5"
}
Или (если email уже зарегистрирован или username занят)
{
    "message": "User with current username or email already exists.",
    "timestamp": 1678105434976
}


2)	Просмотр баланса кошелька (пользователь)

Путь: localhost:8080/user/watch
JSON (запрос): 
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5"
}

JSON (ответ):
{
    "ton_wallet": "0.0",
    "rub_wallet": "10500.0",
    "btc_wallet": "10.0"
}
Или (если secret_key не зарегистрирован в базе)
{
    "message": "The user with secret key = fdhkdfghkudufhg does not exist.",
    "timestamp": 1678110523391
}


3)	Пополнение кошелька (пользователь)

Путь: localhost:8080/user/replenish
JSON (запрос): 
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5",
    "RUB_wallet": "10000"
}
Или
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5",
    "BTC_wallet": "10"
}

JSON (ответ):
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5"
}

4)	Вывод денег с биржи (пользователь)
Путь: localhost:8080/user/get
JSON (запрос): 
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5",
    "currency": "RUB",
    "count": "1500",
    "credit_card": "1234 5678 9012 3456"
}
Или
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5",
    "currency": "BTC",
    "count": "15",
    "wallet": "AsS5A2SASd2as3q5sd2asd53a1s5"
}

JSON (ответ):
{
    "rub_wallet": "500.0"
}
Или (если на счету недостаточно средств для вывода)
{
    "message": "You have less money, than you want to get HAHAHAH.",
    "timestamp": 1678105605216
}


5)	Просмотр актуальных курсов валют (пользователь)
Путь: localhost:8080/user/rate
JSON (запрос): 
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5",
    "currency": "RUB"
}
Или
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5",
    "currency": "BTC"
}

JSON (ответ):
(В ответе исходная валюта берётся как единица, относительно неё идет рассчёт остальных валют)
{
    "RUB_wallet": "1770833.3333333333",
    "TON_wallet": "57.86458333333333",
    "BTC_wallet": "1"
}
Или (если была введена не числящаяся в базе валюта)
{
    "message": "This type of currency does not exist.",
    "timestamp": 1678110624958
}


6)	Обмен валют по установленному курсу (пользователь)
Путь: localhost:8080/user/exchange

JSON (запрос): 
{
    "secretKey": "3261f7cc2618c14501c960e76c1244cc",
    "currency_from": "RUB",
    "currency_to": "TON",
    "amount": "100"
}
Или
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5",
    "currency_from": "BTC",
    "currency_to": "RUB",
    "amount": "20"
}

JSON (ответ):
{
    "currency_from": "RUB",
    "currency_to": "TON",
    "amount_from": "100",
    "amount_to": "0.0032676470588235296"
}
Или 
{
    "currency_from": "BTC",
    "currency_to": "RUB",
    "amount_from": "20",
    "amount_to": "3.5416666666666664E7"
}

7)	Просмотр актуальных курсов валют (админ)
Путь: localhost:8080/admin/rate

JSON (запрос): 
{
    "secretKey": "ghkhue232uhufhdu9438",
    "currency": "RUB"
}
Или
{
    "secretKey": "1ce21b405cf5dcaa8eb553f1ef9a03d5",
    "currency": "BTC"
}

JSON (ответ):
{
    "RUB_wallet": "1",
    "TON_wallet": "3.2676470588235294E-5",
    "BTC_wallet": "5.647058823529412E-7"
}
Или (если был введён не админский secret_key)
{
    "message": "Admin with secret key = 1ce21b405cf5dcaa8eb553f1ef9a03d5 does not exist.",
    "timestamp": 1678111230609
}

8)	Изменение курса валют (админ)
Путь: localhost:8080/admin/change

JSON (запрос): 
{
    "secretKey": "ghkhue232uhufhdu9438",
    "base_currency": "TON",
    "BTC": "0.000096",
    "RUB": "170.0"
}
Или (вводим несуществующую валюту)
{
    "secretKey": "ghkhue232uhufhdu9438",
    "base_currency": "gfjk",
    "BTC": "0.000096",
    "TON": "170"
}

JSON (ответ):
(получаем изменённые курсы валют, базовая не меняется)
{
    "ton": "not changed",
    "btc": "9.6E-5",
    "rub": "170.0"
}
Или (если была введена несуществующая валюта)
{
    "message": "This type of currency does not exist.",
    "timestamp": 1678108937722
}

9)	Просмотр общей суммы на всех пользовательских счетах (админ)
Путь: localhost:8080/admin/balance

JSON (запрос): 
{
    "secretKey": "ghkhue232uhufhdu9438",
    "currency": "BTC"
}
Или (вводим несуществующую валюту)
{
    "secretKey": "ghkhue232uhufhdu9438",
    "currency": "TYUT"
}

JSON (ответ):
{
    "btc": "1.1294117647058824E-6"
}
Или (если была введена несуществующая валюта)
{
    "message": "This type of currency does not exist.",
    "timestamp": 1678112074160
}

10)	Просмотр количества операций, проведённых за определённый срок (админ)

Путь: localhost:8080/admin/transaction

JSON (запрос): 
{
    "secretKey": "ghkhue232uhufhdu9438",
    "date_from": "04.03.2023",
    "date_to": "07.03.2023"
}
Или (вводим «unparseable» по шаблону дату)
{
    "secretKey": "ghkhue232uhufhdu9438",
    "date_from": "04.03.2023",
    "date_to": "06.2023"
}

JSON (ответ):
{
    "transaction_count": "117"
}
Или (если была введена несуществующая валюта)
{
    "message": "Unparseable date: \"06.2023\"",
    "timestamp": 1678105581385
}

Описание работы: 
Отправляем из Postman запрос на локальный сервер в формате JSON. В контроллере «парсим» входящий JSON в нужную нам Dto и передаем поля в сервис, который проделает
с ними необходимую нам работу. Возвращаем ответ в формате JSON. Если что-то пойдет не так (secret_key нет в базе, нет такой валюты, слишком мало средств,
введён не кошелёк, а банковская карта (для крипты), нет кошелька с таким названием), будет возвращён Exception с необходимым описанием. 
Была подключена база данных PostgreSQL для хранения данных.

Описание файлов и папок в src/main/java/com/virtya/CryptoMarket:
•	Controller
  •	AdminController – контроллер для обработки запросов со стороны админа;
  •	UserController – контроллер для обработки запросов со стороны пользователя;
•	dto
  •	error – объект ошибки
  •	forchangerate – объекты для смены курса валют админом
  •	forexchangemoney – объекты для обмена валютой пользователем
  •	forgetmoney – объект для вывода денег пользователем
  •	forgetusersbalance – объекты для получения админом суммы на кошельке пользователей
  •	forregister – объекты для регистрации пользователей
  •	forreplenishwallet – объекты для пополнения кошелька пользователя
  •	fortransaction – объекты для получения админом всех транзакций за определённый срок
  •	forwatchbalance – объект для получения баланса кошелька пользователя
  •	forwatchrate – объекты для просмотра курса валют
•	entity – сущности для БД
•	exception – для обработки нестандартных исключений
•	hander – для выбрасывания исключений
•	repository – репозитории, тут больше и не скажешь (UserRepository и AdminRepository реализуют запросы к БД)
•	service – интерфейс сервисов
•	serviceimpl – реализация сервисов

Примечание:
Смену курса валют админом тоже посчитал за транзакцию (подходит под описание «действие на рынке», но не подходит под «торги», поэтому не уверен в правильности).
При смене курса админом возвращается поле базовой валюты со значение «not changed», естественно это значение возвращается только в JSON, в базу не записывается.

