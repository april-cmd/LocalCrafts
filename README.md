# LocalCrafts

**A Mobile Application for Showcasing Local Handmade Crafts in Zambia**

## 1. Problem Statement

Many local artisans in Zambia struggle to market their handmade products and reach potential customers due to limited exposure and reliance on physical markets. This limits their opportunities to grow their businesses and attract customers beyond their immediate communities.

## 2. Proposed Solution

LocalCrafts is an Android mobile application designed to improve the visibility of local artisans and make their handmade products easier for potential customers to discover.

The application allows artisans to register, create business profiles and showcase their handmade products through images, descriptions and prices.

Customers can browse available crafts, search for specific products and view artisan profiles without creating an account.

## 3. Target Users

The application has two main categories of users:

- **Artisans/Vendors:** Can register, log in, create and manage their business profiles, and showcase their handmade products.
- **Customers:** Can browse handmade products, search for specific crafts and view artisan profiles without logging in or registering.

## 4. Application Screens

The application consists of the following main screens:

### 4.1 Landing Page
Introduces the LocalCrafts application and provides navigation to the customer browsing experience and artisan authentication pages.

### 4.2 Artisan Login/Registration Page
Allows artisans to create accounts and log in to access their account-related features.

### 4.3 Customer Homepage
Displays available handmade products to help customers discover crafts showcased by local artisans.

### 4.4 Product Comparison page
Allows customers to compare products from other artisans and provides full information about the particular artisan

### 4.5 Artisan Profile Page
Allows artisans to create and manage their business profiles by providing personal and business information, and allow artisans can also manage their handmade products by adding descriptions, prices and locations.

## 5. Technologies Used

- Kotlin
- Android Studio
- Jetpack Compose

## 6. How to Build and Run the Application

1. Clone this repository to your local computer.
2. Open Android Studio.
3. Select **Open** and locate the cloned `LocalCrafts` project folder.
4. Allow Android Studio to synchronize the Gradle files and download the required dependencies.
5. Set up an Android emulator or connect an Android device with USB debugging enabled.
6. Select the desired device.
7. Click the **Run** button in Android Studio.
8. Wait for the application to build and launch.

## 7. Research Notes

### 7.1 User Authentication and Login

**What it is:**
A security and identity management mechanism that allows users (artisans) to authenticate, create accounts, and securely manage their private business profiles and product inventory.

**Why it is needed in LocalCrafts:**
It enables local artisans in Zambia to securely manage their business information (artisan name, business name, location, email, and description) and maintain ownership over their showcased products.

**Implementation:**
Implemented using Jetpack Compose interactive forms for login and registration, backed by Room database storage (`Artisan` entity, `ArtisanDao`, and `LocalCraftsRepository`), managed through `LocalCraftsViewModel` using `StateFlow` and coroutines.

**Sources consulted:**
- Android Developer Documentation on Jetpack Compose and State management.
- Room Persistence Library guide.
- Android Architecture Components guide.

**Challenges encountered:**
Handling asynchronous Room database flows and ensuring seamless navigation and state synchronization between the authentication screen and the artisan dashboard.

### 7.2 Dark Mode

**What it is:**
A display setting that uses a dark color palette for low-light environments, reducing eye strain and improving visual comfort.

**Why it is needed in LocalCrafts:**
Provides user preference customization and accessibility for customers and artisans browsing handmade crafts in diverse lighting conditions.

**Implementation:**
Implemented using Material 3 `darkColorScheme` and `lightColorScheme`, a custom `ThemeController`, and a `CompositionLocalProvider` (`LocalThemeController`). Interactive toggle switches are provided on the Landing Page, Customer Homepage, and Artisan Dashboard to allow instant toggling between light and dark themes.

**Sources consulted:**
- Material 3 Theming and Color schemes documentation.
- Jetpack Compose Dark Theme implementation guide.
- Compose CompositionLocal documentation.

**Challenges encountered:**
Propagating the theme toggle state across different navigation destinations cleanly without modifying every navigation screen signature, solved elegantly via Compose `CompositionLocal`.

## 8. Group Members

| Full Name | Student Number |
|-----------|----------------|
| Balica Mukange | 2421009 |
| Chandi Chileshe | 2410226 |
| Benny Simpamba | 2410092 |
| John Mwando | 2410053|
| Robby Chungo | 2410051 |
