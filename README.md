# 🎬 Кинотека

## 📌 Описание проекта

"Кинотека" — это мобильное приложение для Android, разработанное на **Kotlin** с использованием **Firebase**. Приложение предоставляет пользователям удобный каталог фильмов с возможностью фильтрации по жанрам, добавления в избранное, а также административного управления контентом.

## ⚙️ Технологии

- **Язык программирования**: Kotlin
- **UI**: Jetpack Compose
- **База данных**: Firebase Firestore
- **Авторизация**: Firebase Authentication
- **Среда разработки**: Android Studio
- **Управление зависимостями**: Gradle
- **Дизайн**: Figma

## 📱 Функционал

### 🔐 Авторизация и регистрация

- Вход в систему через **Firebase Authentication**.
- Ошибки авторизации отображаются в интерфейсе.
- После входа доступ к основному функционалу.

### 🎥 Главный экран

- Список фильмов с отображением **постера, названия и года выпуска**.
- Кнопка **добавления в избранное**.
- **Фильтрация** фильмов по жанрам.
- Для **администратора** доступно **редактирование и создание** новых фильмов.

### ⭐ Избранное

- Список добавленных пользователем фильмов.
- Если список пуст — отображается соответствующее сообщение.

### ⚙️ Настройки профиля

- Отображение **почты пользователя**.
- Кнопка **удаления аккаунта** (не доступна администратору).

### 📝 Экран фильма

- Отображение **постера, названия, года выпуска, жанра и описания**.

### 🎬 Администрирование

- Администратор может **создавать и редактировать** фильмы.
- Ввод данных через **форму с полями и загрузкой изображения**.

## 🚀 Установка и запуск

### 🔹 1. Клонирование репозитория

```sh
git clone https://github.com/your-username/kino-app.git
cd kino-app
```

### 🔹 2. Открытие в Android Studio

- Открой **Android Studio**.
- Выбери "Open an Existing Project" и укажи путь к проекту.
- Дождись загрузки зависимостей **Gradle**.

### 🔹 3. Настройка Firebase

1. Создай проект в **Firebase Console**.
2. Добавь **google-services.json** в папку `app`.
3. Включи **Firebase Authentication** (Email/Password).
4. Создай базу данных в **Firestore**.

### 🔹 4. Запуск проекта

- Подключи **эмулятор** или реальное устройство.
- Нажми **Run** (Shift + F10).

## 🛠️ Разработчики

- **[Дедяев Данил]** – Разработка и дизайн

## 📄 Лицензия

Этот проект распространяется под лицензией **MIT**.

---

✨ Разработано с любовью на Kotlin! 🚀

