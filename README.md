# **KeepUp**

KeepUp is an Android application that allows users to stay up-to-date with the top headlines of their country. It provides a convenient way to browse and read news articles, save them for later, and share them with others. Built using Kotlin and following the MVVM (Model-View-ViewModel) architecture pattern, KeepUp leverages coroutines for asynchronous programming.

## **Tech Stack**

Kotlin: The primary programming language used to develop the application.

MVVM Architecture: KeepUp follows the Model-View-ViewModel architectural pattern, separating the business logic from the UI.
Coroutines: Asynchronous programming is achieved using coroutines, which simplify handling long-running tasks without blocking the main thread.

Retrofit: KeepUp utilizes Retrofit library to make HTTP requests and retrieve news data from an API.

Room: Room is used for local data persistence, allowing users to save articles for offline reading.

Android Jetpack Components: KeepUp integrates various Android Jetpack components, including ViewModel, LiveData, and Data Binding, to enhance development efficiency and maintainability.

## **Features**

Top Headlines: KeepUp provides users with the latest top headlines from their country. Users can stay informed about the current news and browse articles from various categories.

Save Articles: Users have the option to save articles they find interesting to read later. Saved articles are stored locally using Room for offline access.

Share Articles: KeepUp allows users to share news articles with others through different sharing options, such as email, messaging apps, or social media platforms.

Read Articles: Users can view the full content of articles within the app. KeepUp provides a clean and intuitive reading experience, making consuming news on the go easy.

## **Getting Started**

To run the KeepUp application locally and explore its features, follow these steps:

Clone the repository:

```
git clone https://github.com/HrishiGodina/KeepUp.git
```

1. Open the project in Android Studio.

2. Build and run the project on an emulator or a physical device.

3. Explore the various features of KeepUp, such as browsing top headlines, saving articles, sharing articles, and reading articles within the app.

## **Contributing**

Contributions to KeepUp are welcome! If you encounter any issues or have ideas for improvements, please open an issue on the repository. Feel free to submit pull requests with bug fixes, new features, or enhancements.

Before contributing, please review the contribution guidelines for detailed instructions.

## **License**

This project is licensed under the MIT License. Please refer to the [LICENSE](LICENSE) file for more information.

## **Acknowledgments**

The KeepUp app utilizes the [NewsAPI]([url](https://newsapi.org/)) to retrieve news data. We express our gratitude to the developers and maintainers of NewsAPI for providing a valuable service.
We would like to thank all the contributors who have helped improve and enhance KeepUp.
Special thanks to the Android community for their continuous support and the availability of amazing open-source libraries and tools.
