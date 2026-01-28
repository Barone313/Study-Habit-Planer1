# Study Habit Planner

Der **Study Habit Planner** ist eine Android-App, mit der Studierende ihre Lerngewohnheiten planen, verfolgen und verbessern können. Die App bietet Funktionen zum Anmelden, Verwalten von Notizen, Bearbeiten des Profils und Einstellen individueller Gewohnheiten.

## Funktionen

- Benutzerregistrierung und Login
  - Neues Konto erstellen
  - Mit bestehendem Konto anmelden
  - Sicheres Speichern der Zugangsdaten (z.B. über Firebase Auth / eigenes Backend)

- Notizen erstellen und verwalten
  - Neue Notizen anlegen
  - Notizen bearbeiten und löschen
  - Optionale Zuordnung von Notizen zu bestimmten Gewohnheiten oder Fächern

- Profil bearbeiten
  - Nutzername, E-Mail und ggf. Profilbild ändern
  - Persönliche Lernziele festlegen
  - Einstellungen zu Sprache/Theme (z.B. Dark Mode)

- Gewohnheiten (Habits) einstellen
  - Neue Lerngewohnheiten definieren (z.B. „täglich 30 Minuten Mathe lernen“)
  - Häufigkeit und Wochentage festlegen
  - Erinnerungen/Notifications aktivieren
  - Fortschritt der Gewohnheiten anzeigen (z.B. Anzahl der erfüllten Tage)

## Zielgruppe

Die App richtet sich vor allem an Schüler:innen und Studierende, die ihre Lernorganisation verbessern und sich mit wiederkehrenden Lernroutinen motivieren wollen.

## Technische Details

- Plattform: Android
- Programmiersprache: Kotlin
- Architektur (Beispiel): MVVM
- Mögliche Libraries/Tools:
  - ViewModel + LiveData / StateFlow
  - Room oder eine andere lokale Datenbank für Notizen und Habits
  - Optional: Firebase Authentication / Firestore für Nutzerkonten und Sync
