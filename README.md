# Barandeh (برنده) - Persian AI Task Planner

> A modern Android planning application designed specifically for Persian-speaking users, featuring AI-powered assistance through Google Gemini integration and comprehensive Persian/Jalali calendar support.

## Features

### 🤖 AI-Powered Planning

  * **Smart Chat Assistant**: Multi-persona chat interface with Google Gemini integration (`libs.versions.toml:32`)
  * **Voice Commands**: Convert voice input to tasks using AI processing

### 📅 Persian Calendar Integration

  * **Jalali Calendar Support**: Full Persian calendar system with date picker (`libs.versions.toml:35`)
  * **RTL Layout**: Native right-to-left layout support

### ✅ Task Management

  * **Task Planning**: Create, edit, and manage tasks with Persian calendar dates
  * **Task Statistics**: Track planned and completed tasks (`StatsSection.kt:36-37`)
  * **Task Status Toggle**: Mark tasks as completed or pending (`MainTaskScreen.kt:162-164`)

### 📝 Note Taking

  * **Simple Notes**: Create and manage notes with color coding (`EditNoteBottomSheet.kt:55-57`)
  * **CRUD Operations**: Full create, read, update, delete functionality for notes

## Technology Stack

### Frontend

  * **Jetpack Compose**: Modern Android UI toolkit (`build.gradle.kts:48`)
  * **Material Design 3**: Latest Material Design components (`build.gradle.kts:52`)
  * **Navigation Compose**: Declarative navigation (`libs.versions.toml:41`)

### Backend & Data

  * **Room Database**: Local data persistence (`libs.versions.toml:56`)
  * **Google Generative AI**: Gemini API integration (`libs.versions.toml:32`)
  * **Retrofit**: HTTP client for API communication (`libs.versions.toml:39`)

### Persian Language Support

  * **PersianDate Library**: Persian calendar calculations (`libs.versions.toml:38`)
  * **Jalali DatePicker**: Persian date selection UI (`libs.versions.toml:35`)

## Requirements

  * **Android SDK**: Minimum API 26, Target API 34 (`build.gradle.kts:14-15`)
  * **Permissions**:
      * `INTERNET` - For AI API communication
      * `RECORD_AUDIO` - For voice command functionality

## Installation

1.  Clone the repository
2.  Open in Android Studio
3.  Sync Gradle dependencies
4.  Add your Google Gemini API key to the project
5.  Build and run on an Android device/emulator

## Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern with:

  * **Single Activity**: `MainActivity` hosting Compose navigation
  * **Compose UI**: Modern declarative UI with Material 3
  * **Room Database**: Local data persistence
  * **Repository Pattern**: Clean separation of data sources

## Package Structure

```
ir.amirrezaanari.barandehplanning/
├── ai/               # AI chat functionality
├── homescreen/       # Home screen with statistics
├── notes/            # Note management
├── planning/         # Task planning features
└── ui/theme/         # App theming
```
## Link to Documentation
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/amirreza-anari/Barandeh)

## Contributing

1.  Fork the repository
2.  Create a feature branch
3.  Make your changes
4.  Test thoroughly
5.  Submit a pull request
