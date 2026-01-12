# Filler Words Counter

An Android app that helps you track and reduce filler words in your speech by continuously listening and analyzing your conversations in real-time.

## Features

- 🎤 **Real-time Speech Analysis**: Continuously listens to your speech and transcribes it using OpenAI Whisper
- 📊 **Filler Word Tracking**: Tracks common filler words like "like", "um", and "basically"
- 📈 **Daily Statistics**: View your daily word counts and filler word usage
- 📉 **Trends**: Monitor your progress over time with trend visualization
- 🔔 **Background Service**: Runs as a foreground service to continuously monitor your speech
- 💾 **Local Storage**: Uses Room database to store your daily statistics

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with ViewModel
- **Database**: Room (SQLite)
- **Speech-to-Text**: OpenAI Whisper API
- **Networking**: OkHttp
- **Navigation**: Navigation Compose

## Requirements

- Android 8.0 (API level 26) or higher
- OpenAI API key
- Microphone permission
- Notification permission (Android 13+)

## Setup

1. Clone the repository:
```bash
git clone https://github.com/mnettem21/FillerWordsCounter.git
```

2. Add your OpenAI API key:
   - Create a `local.properties` file in the root directory
   - Add: `OPENAI_API_KEY=your_api_key_here`
   - Or set it as a Gradle property: `OPENAI_API_KEY=your_api_key_here`

3. Open the project in Android Studio

4. Build and run the app

## Permissions

The app requires the following permissions:
- `RECORD_AUDIO`: To capture audio for speech analysis
- `POST_NOTIFICATIONS`: To show a foreground service notification (Android 13+)

## Usage

1. Launch the app
2. Grant microphone and notification permissions when prompted
3. Tap "Start" to begin listening
4. The app will continuously transcribe your speech and count filler words
5. View your daily statistics and trends in the app

## Project Structure

```
app/src/main/java/com/example/fillerwordscounter/
├── audio/              # Audio recording and processing
├── data/               # Room database and DAOs
├── processing/         # Word counting logic
├── service/            # Background listening service
├── stt/                # Speech-to-text integration (OpenAI Whisper)
├── ui/                 # Compose UI screens and components
└── MainActivity.kt     # Main entry point
```

## License

This project is open source and available for personal use.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
