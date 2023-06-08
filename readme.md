# Expense Manager

---

## Dokumentacja

## 1. Wstęp

***Expense Manager*** to program komputerowy, który umożliwia użytkownikowi kontrolowanie swojego budżetu poprzez zarządzanie wydatkami. Aplikacja wykorzystuje relacyjną bazę danych do gromadzenia, wyszukiwania i przetwarzania informacji o wydatkach użytkownika. Program pozwala użytkownikowi na wprowadzanie zmian w bazie danych, dodając, edytując lub usuwając rekord odpowiadający wydatkowi lub kontu użytkownika.

## 2. Projekt systemu

### 2.1. Projekt bazy danych

Zaprojektowana relacyjna baza danych przechowuje informacje o użytkownikach wraz z dokonanymi przez nich wydatkami w konkretnych kategoriach.
Użytkownik, przy pomocy stworzonego programu, może ingerować w bazę danych poprzez:

* tworzenie, edytowanie, usunięcie konta,
* dodanie, edytowanie, usunięcie wydatku.

Baza danych została zaimplementowana w systemie MySQL.

#### Encje

* **```users```** - konto użytkownika aplikacji do zażądzania wydatkami,
  * *```user_id```* - unikalny numer użytkownika,
  * *```name```* - unikalna nazwa konta użytkownika,
  * *```password```* - hasło do konta,
  * *```email```* - adres e-mail użytkownika,
* **```expenses```**- konkretny wydatek użytkownika,
  * *```expense_id```* - unikalny numer wydatku,
  * *```name```* - nazwa wydatku,
  * *```category_id```* - numer kategorii wydatku,
  * *```amount```* - kwota wydatku
  * *```expense_date```* - data wydatku
  * *```user_id```* - numer użytkownika, który dokonał dany wydatek
* **```expense_categories```** - kategoria wydatku,
  * *```category_id```* - unikalny numer kategorii wydatku,
  * *```name```* - nazwa kategorii wydaku
  
#### Diagram encji

![entity_diagram](documentation_files/entity_diagram.png)

### 2.2. Projekt aplikacji użytkownika

Zaprojektowana aplikacja umożliwia zarządzanie bazą danych przez użytkownika poprzez tworzenie, edytowanie oraz usuwanie konta, a także dodatnie, edytowanie oraz usunięcie wydatku.

Aplikacja została zaimplementowana w języku Java.

### Opis klas

* **```Main```** - klasa zawierająca metodę `main`, a także pola przechowujące obiekt zalogowanego użytkownika oraz obiekt połączenia z bazą danych. 

##### Klasy odpowiadające za komunikacje z bazą danych

* **```User```** - klasa reprezentuje konkretnego użytkownika aplikacji, posiada pola, które jednoznacznie go opisują oraz metody, umożliwiające zarządzeniem kontem oraz wydatkami.
* **```Expense```** - klasa reprezentuje pojedynczy wydatek dokonany przez uzytkownika, posiada pola go opisujące oraz metody, które pozwalają na dodawanie, usuwanie oraz jego edycję w bazie danych.
* **```DataBaseConnector```** - klasa umożliwiająca nawiązanie połączenia z bazą danych. Zawiera pola, które przechowują wszystkie niezbędne dane do nawiązania połączenia oraz metodę, która je nawiązuje.
  
##### Klasy odpowiadające za interfejs graficzny

* **```MainFrame```** - klasa reprezentuje główną ramkę programu.
* **```MainPanel```** - klasa reprezentuje główny panel aplikacji, zawierający informacje o wydatkach oraz o użytkowniku.
* **```ExpensePanel```** - klasa reprezentuje panel wyświetlany w panelu głównym, zawierający informacje o wydatkach użytkownika.
* **```ExpenseTabelPanel```** - klasa reprezentuje tabelę, wyświetlającą wydatki użytkownika w konkretnym roku oraz miesiącu pobrane z bazy danych.
* **```InfoPanel```** - klasa reprezentuje panel wyświetlany w panelu głównym, zawierający informacje o zalogowanym użytkowniku.
* **```MenuBar```** - klasa reprezentuje pasek menu, znajdujący się na górze okna wraz z jej elementami umożliwiającymi zarządzenie kontem oraz wydatkami.
* **```MyDialog```** - klasa reprezentuje proste okno dialogowe, niezawierające żadnych pól. Służy do dziedziczenia przez klasy potomne podstawowych właściwości okna dialogowego.
* **```LoggingDialog```** - klasa dziedzicząca po klasie `MyDialog`, reprezentuje okno dialogowe służące do logowania do programu.
* **```CreateAccountDialog```** -klasa dziedzicząca po klasie `MyDialog`, reprezentuje okno dialogowe służące do tworzenia konta.
* **```EditAccountDialog```** - klasa dziedzicząca po klasie `MyDialog`, reprezentuje okno dialogowe służące do edytowania danych konta użytkownika.
* **```AddNewExpenseDialog```** - klasa dziedzicząca po klasie `MyDialog`, reprezentuje okno dialogowe służące do dodania nowego wydatku przez użytkownika.
* **```EditExpenseDialog```** - klasa dziedzicząca po klasie `MyDialog`, reprezentuje okno dialogowe służące do edytowania wydatkow przez użytkownika.
* **```DialogFactory```** - klasa implementuje wzorzec projektory `factory`, udostępniając metodę tworzącą okno dialogowe zależne od przekazanego typu.

### 2.3. Struktura folderów

* ```database``` - skrypty SQL do utworzenia bazy danych oraz do zapełnienia jej przykładowymi danymi,
* ```icons``` - ikony używane przez aplikacje, pobrane ze strony [freepik](https://www.freepik.com/) oraz [FontAwesome](https://fontawesome.com/)
* ```lib``` - biblioteki zewnętrzne używane przez aplikacje
* ```out```
  * ```artifacts``` - pliki artefaktów
  * ```production``` - pliki klas z kodem bajtowym
* ```src``` - pliki z kodem źródłowym

  
## 3. Instalacja i konfigurowanie systemu

W celu poprawnego działania aplikacji konieczne jest uruchomienie skryptu `SQL`, tworzącego bazę danych, wykorzystując odpowiednie oprogramowanie zarządzające bazami danych systemu MySQL (np.: MySQL Workbench, phpMyAdmin) lub w konsoli/terminalu korzystając z opowiednich poleceń:

>`mysql -u [<username>] -p`
>
> `source database/create_database.sql`

Baza danych powinna zaostać utworzona na adresie IP `127.0.0.1` oraz porcie `3306`. 

Skrypt tworzący bazę danych znajduje się w `database/create_database.sql`. 

Aby zapełnić bazę danych przykładowymi danymi należy analogicznie uruchomić skrypty `SQL`:
* `database/insert_example_users.sql`  - w celu dodania przykładowych użytkowników,
* `database/insert_example_expenses.sql` - w celu dodania przykładowych wydatków.

Stworzona aplikacja znajduje się w skompresowanym pliku `.jar` znajdującym się w `out/artifacts/ExpenseManager_jar/ExpenseManager.jar`. 

Aby uruchomić ten plik należy zainstalować wirtualną maszynę Java (JVM), a następnie uruchomić plik `.jar` poprzez wiersz poleceń:
>`java -jar out/artifacts/ExpenseManager_jar/ExpenseManager.jar`

albo uruchamiając plik w zainstalowanym wcześniej dowolnym Java Runtime Environment (JRE).




## 4. Instrukcja użytkowania aplikacji

Po uruchomieniu programu, użytkownik ma możliwość zalogowania się, utworzenia nowego konta lub wyjścia z aplikacji. Opcje te dostępne są w pasku menu, w zakładce *Konto*.

Po wybraniu opcji *Zaloguj*, program wyświetla odpowiednie okno dialogowe, a użytkownik proszony jest o podanie nazwy konta oraz hasła. Następnie program weryfikuje istnienie użytkownik o takiej nazwie w bazie danych. Jeżeli weryfikacja przebiegnie poprawnie, program sprawdza zgodność wprowadzonego hasła, a następnie wyświetla odpowiednie komunikaty.

Po wybraniu opcji *Utwórz*, program wyświetla odpowiednie okno dialogowe, a użytkownik proszony jest o podanie nazwy konta, adresu e-mail, hasła oraz ponownie hasła w celu weryfikacji zgodności. Następnie program weryfikuje zgodność hasła, poprawność adresu e-mail oraz dostępność nazwy użytkownika i adresu e-mail w bazie danych. Na koniec program dodaje nowy rekord do tabeli `users` w bazie danych i wyświetla odpowieni komunikat.

Po wybraniu opcji *Wyjdź* program kończy działanie, a użytkownik zostaje wylogowany.

---

Po pozytywnym zalogowaniu się użytkownika do konta, informacjie w głównym panelu aplikacji zostają zaktualizowane.

W panelu wydatków zostają wyświetlone wydatki użytkownika w bierzącym miesiącu bierzącego roku. Użytkownik, korzystając z dwóch list rozwijalnych, ma możliwość zmiany miesiąca oraz roku w celu wyświetlenia innych wydatków. Rozwijala lista z lat zawiera lata od bierzącego do roku 2018 włącznie, natomiast rozwijalna lista miesięcy zawiera nazwy miesięcy zależne od wyświetlanego roku. Jeżeli użyutkownik wybrał bierzący rok, wyświetlone zostają nazwy miesięcy od pierwszego miesiąca roku (styczeń) do bierzącego miesiąca. W przeciwnym wypadku zostają wyświetlone wszystkie nazwy miesięcy.

W panelu informacji zostaje wyświetlona nazwa aktualnie zalogowanego użytkownika oraz *podsumoawanie*, na które składa się:

* średnia wydatków użytkownika, obliczona w bazie danych,
* nazwa bierzącego miesiąca,
* średnia wydatków użytkownika w bierzącym miesiącu, obliczona w bazie danych,
* nazwa kategori wydatku, którego największą ilość użytkownik dokonał w bierzącym miesiącu.

Po pozytywnym zalogowaniu się użytkownika zostaje również zaktualizowany pasek menu. Opcja *Zaloguj* oraz *Utwórz* zostaje zdezaktywowana, natomiast zakładka *Wydatek*, w której znajdują się opcje *Dodaj* oraz *Edytuj* oraz w zakładce *Konto* opcje *Wyloguj* oraz *Edytuj* są aktywowane.

Po wybraniu opcji *Wyloguj* użytkownik zostaje wylogowany z aplikacji, w panelu głównym zostają wyświetlone dane domyślne oraz dezaktywowany wybór miesiąca oraz roku w odpowiadających im listach rozwijalnych, a w pasku menu zostają dezaktywowane opcje i zakładki, które były aktywowane w momencie logowania użytkownika.

Po wybraniu opcji *Edytuj* program wyświetla okno dialogowe, a użytkownik zostaje proszony o wprowadzenie zaktualizowanch danych. Po zatwierdzeniu ich następuje sprawdzenie poprawności adresu e-mail, dostępności nazwy użytkownika w bazie danych oraz zaktualizowanie odpowiedniego rekordu w bazie danych.

W oknie dialogowym edycji konta użytkownik ma również opcję usunięcia konta. Wówczas, po wyświetleniu ostrzegającego komunikatu, zostaje usunięty dany rekord z bazy danych z tabeli `users` jak rónież wszyskie rekordy z tabele `expenses` dla odpowiadającego pola `user_id`.

Po wybraniu opcji *Dodaj* z zakładki *Wydatek* zostaje wyświetlone odpowiednie okno dialogowe, w którym użytkownik zostaje proszony o wprowadzenie danych nowego wydatku. W polu *nazwa wydatku* zostaje ustawiona domyślna nazwa *Wydatek* oraz numer kolejnego wydatku użytkownika. W polu *kowota wydatku* zostaje ustawiona wartość *0,00*. Do tego pola użytkownik ma możliwość wprowadzenia wyłącznie cyfr, gdzie separatorem części całkowitych od dziesiętych jest przecinek. Wprowadzona liczba zostaje zaokrąglona do dwóch miejsc po przecinku. W polu *data wydatku* zostaje ustawiona bierząca data w formacie `YYYY-MM-DD`. W takim też formacie użytkownik ma możliwość wprowadzania wyłącznie cyfr. Program sprawdza czy podana przez użytkownika data jest poprawna, sprawdzając numer miesiąca oraz ilość dni w danym miesiącu. Użytkownik nie ma możliwości wprowadzenia daty późniejszej od daty bierzącej. Po zaakceptowaniu wydatku zostaje dodany nowy rekord  w bazie danych do tabeli `expenses` oraz zostaje odświeżona zawartość panelu głównego.

Po wybraniu opcji *Edytuj* zostaje wyświetlone odpowiednie okno dialogowe, w którym użytkownik wybiera z listy rozwijalnej wydatek, który chce edytować albo usunąć. Następnie ma możliwość poprawienia wprowadzonych ówcześnie danych wydatku. Po zaakceptowaniu wydatku dany rekord w bazie danycbh w tabeli *expenses* zostaje zaktualizowany, a zawartość głównego panelu zostaje odświeżona.

W oknie dialogowym edycji konta użytkownik ma również opcję usunięcia określonego wydatku. Wówczas, po wyświetleniu ostrzegającego komunikatu, zostaje usunięty dany rekord z bazy danych z tabeli `expenses`.
