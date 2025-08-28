
# NewsDesk - JavaFX News Application

A modern JavaFX application for exploring news by category with Firebase Authentication and NewsAPI integration.

---

## 🌟 Features

- **User Authentication:** Sign up and login with email and password using Google Firebase
- **Live News Feed:** Real-time top headlines from NewsAPI
- **Category Navigation:** General, Business, Entertainment, Health, Science, Sports
- **Modern UI:** Beautiful JavaFX with gradients, transitions, and responsive cards
- **Open Full Article:** Clickable links to news sources
- **Image Support:** News and categories show attractive images
- **Error Handling:** Graceful messaging and UI fallbacks

---

## 🚀 Technologies Used

- Java 17+
- JavaFX 21.x
- Firebase Authentication (REST)
- NewsAPI REST API
- Maven (build and dependency management)
- JSON (org.json library)
- VS Code, IntelliJ, or Eclipse

---

## 📋 Prerequisites

- Java (JDK) 17 or newer installed
- JavaFX SDK downloaded (https://openjfx.io)
- Maven installed
- A Firebase project (use your API key in code)
- Free NewsAPI key (https://newsapi.org)
- Internet connection

---

## 🛠️ Setup & Installation

### 1. Clone the Repo
```

git clone https://github.com/Pawan-bari/NewsDesk.git
cd NewsDesk

```

### 2. Install JavaFX
Download from [here](https://openjfx.io/) and extract to (for example):  
`C:/Javafx-SDK/javafx-sdk-21.0.7/`

### 3. Configure API Keys

- **NewsAPI key:**  
  Edit `ApiController.java` and set your NewsAPI API key in `apikey`
- **Firebase Web API key:**  
  Edit `LoginController.java` and `Signupcontroller.java`, set your Firebase REST API key

### 4. Build & Run

#### Using Maven
```

mvn clean compile
mvn javafx:run

```

#### Using VS Code / IntelliJ
- Open the project
- For VS Code: Ensure `launch.json` `--module-path` points to your JavaFX SDK location
- Run the `Main` class

---

## 📁 Project Structure

```

NewsDesk/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── news/
│       │           ├── controller/
│       │           │   ├── ApiController.java
│       │           │   ├── LoginController.java
│       │           │   └── Signupcontroller.java
│       │           ├── view/
│       │           │   ├── SignUpView.java
│       │           │   └── Newsivew.java
│       │           └── Main.java
│       └── resources/
│           └── Designer.png
├── pom.xml
├── launch.json
└── README.md

```

---

## 🎨 User Interface

- **Login/Signup:** Elegant design, responsive entry fields, and interactive error handling
- **Category Dashboard:** Animated tiles, easy selection, logout option
- **News Feed:** Scrollable, image-laden cards each with title, description, and "Read More" link
- **Fallbacks:** Fallback gradient backgrounds if the app image can't be displayed

---

## ⚠️ Handling `.json` Files in This Project

- Do **NOT** commit `.json` API/service account credentials to GitHub!
- All `.json` files with sensitive credentials should be kept secure locally and are included in `.gitignore`:
    ```
    *.json
    newsapp/target/
    ```
- If you accidentally push keys or secrets:
    1. **Immediately remove** them from Git history (see [GitHub guide](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/removing-sensitive-data-from-a-repository))
    2. **Revoke/rotate leaked credentials** in your Cloud/Firebase Console
- To use credentials at runtime, load them **outside** your version-controlled folders or pass the path securely (see example in README above).

---

## 📝 .gitignore Recommendations

Always include keys and build directories:

```


# Credentials \& config

*.json

# Build outputs

/target/
/build/
/out/
/bin/

# IDE files

.vscode/
.idea/
*.iml

# OS files

.DS_Store
Thumbs.db

# Environment

.env
*.properties
config.properties

# Class files

*.class

```

---

## 🐛 Known Issues

- Background images require correct path and JavaFX image support
- Large images/articles may cause slow loading depending on internet
- `.json` file leaks via version control are *blocked by GitHub*

---

## 🔮 Roadmap & Enhancements

- [ ] Search news by keyword
- [ ] Favorite/save articles
- [ ] Offline support
- [ ] Dark/Light mode switch
- [ ] Social sharing
- [ ] Richer category and localization options

---

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Open a Pull Request

---

## License

This project is licensed under the MIT License.

---

## 👤 Author

**Pawan Bari**  
[GitHub Profile](https://github.com/Pawan-bari)

---

## 📸 Screenshots


<img width="547" height="832" alt="Screenshot 2025-08-28 165658" src="https://github.com/user-attachments/assets/d13cb268-1fb2-4073-985c-364cba34fba5" />
<img width="547" height="832" alt="Screenshot 2025-08-28 165617" src="https://github.com/user-attachments/assets/00e7ed0c-ed86-418b-932b-fc18ce870223" />
<img width="547" height="832" alt="Screenshot 2025-08-28 165634" src="https://github.com/user-attachments/assets/8341cee7-d675-4ece-8ff0-7afb65d8182e" />


---

## 📞 Support & Help

For issues/questions:
- Open a GitHub issue
- Submit a pull request for improvements
