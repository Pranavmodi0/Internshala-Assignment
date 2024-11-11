# Notes Android App

This project outlines the design and development of a simple note-taking application for Android devices. The app prioritizes a user-friendly and efficient experience, leveraging the following features:

# ScreenShots

<img src = "https://github.com/Pranavmodi0/Notes-App/assets/106916341/d8d15699-924a-49be-b272-fcffab8eeb4e" width="200" height="400" />
<img src = "![1](https://github.com/user-attachments/assets/7252225a-ea56-45c3-be1b-d521093eb22e)" height="400" />
<img src = "![2](https://github.com/user-attachments/assets/d8014b3a-8dfb-45c6-997c-a178edbca43d)" width="200" height="400" />
<img src = "![3](https://github.com/user-attachments/assets/1301eaa9-d7ec-4e89-86b6-ca35d4c21601)" width="200" height="400" />

## Single Activity with Fragments

- The app adheres to a single-activity structure for streamlined navigation.
- Fragments manage distinct views within the activity, such as login, note list, and individual note editing.

## Google Sign-In

- Upon launch, a login screen appears for users who haven't authenticated yet.
- Google Sign-In is implemented for secure user authentication.
- Shared preferences is utilized to store and manage user login status locally.

## Note Management

- Once logged in, the app presents a complete list of the user's notes, eliminating previews for clarity and efficiency.
- A RecyclerView efficiently displays the list of notes, providing a smooth scrolling and loading experience for potentially large datasets.

## Note Creation, Editing, and Deletion

- A dedicated button facilitates the creation of new notes, allowing users to capture their thoughts and ideas promptly.
- Existing notes can be seamlessly edited, enabling users to modify or update their content as needed.
- A user-friendly deletion mechanism empowers users to remove notes that are no longer relevant or necessary.

## Getting Started

1. Clone the project repository.
2. Set up your Android development environment (Android Studio).
3. Configure the project with your Google Sign-In credentials (https://developers.google.com/workspace/guides/create-project).
4. Follow the project's code structure and comments to understand the implementation.
5. Run the app on an Android emulator or device to test its functionality.
