<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" width="150" alt="Fari-Bank Logo">
</p>

# 📱 Fari-Bank (Android Edition)

> A fully featured Android NeoBank application with real-time loan management and automated background transactions.

---

### 📌 Overview
Built as the mobile evolution of the original Fari-Bank terminal system, this Android application brings complex digital banking to the smartphone. It transitions the core multithreaded architecture into a mobile environment, adding advanced financial capabilities like an interactive loan system, debt tracking, and automated background payments without freezing the user interface.

---

### ✨ Core Features
* **Interactive Mobile UI:** Clean, intuitive Android interfaces (built with XML) for dashboards, fund transfers, and detailed transaction reporting.
* **Loan & Debt Management:** Users can request loans, monitor their approval statuses, and track outstanding debts directly from the app.
* **Automated Background Payments:** Utilizes dedicated Java Threads (e.g., `PaymentThread`) to handle auto-deductions and scheduled debt settlements seamlessly in the background.
* **Comprehensive Banking Tools:** Supports SIM card charging, dynamic fund management (Savings, Bonus, Remains), and an internal contact book for fast peer-to-peer transfers.

---

### 🛠️ Tech Stack

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![XML](https://img.shields.io/badge/XML-00599C?style=for-the-badge&logo=xml&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

---

### 🚀 Quick Start

To test the application on an emulator or physical device, clone the repository and build it using the included Gradle wrapper:

```bash
# Build the debug APK
./gradlew assembleDebug

```

*Alternatively, you can open the project folder directly in **Android Studio** and click "Run".*
