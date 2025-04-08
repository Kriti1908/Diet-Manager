# YADA - Yet Another Diet App

YADA is a Java-based diet management application designed to help users track food consumption, manage caloric intake, and maintain personalized dietary goals.

## Features

- User authentication (login/signup) with profile setup
- Food database with basic and composite food items
- Daily food log tracking and calorie monitoring
- Detailed activity level descriptions with calorie multipliers
- Calorie calculation based on user profile (gender, weight, height, age, activity level)
- Multiple calorie calculation formulas (Harris-Benedict, Mifflin-St Jeor)
- Command pattern for undo/redo functionality
- Modern UI with custom components and descriptive activity level information

## Recent Improvements

- **Fixed Profile Persistence**: Profiles are now correctly saved to and loaded from a dedicated profiles directory
- **Proper Empty Log Handling**: Added a friendly message for empty logs instead of default food items
- **Enhanced Undo/Redo System**: Updated command pattern to preserve both undo and redo operations
- **UI Feedback for Undo/Redo**: Menu items are dynamically enabled/disabled based on available operations
- **Improved Table Rendering**: Special handling for empty state with centered, gray text
- **Consistent Profile Storage**: All user profiles are now stored in a central location with proper migration
- **Modern UI Components**: Custom-styled buttons, text fields, and panels for a better visual experience
- **Better User Experience**: Clear feedback when actions are performed

## UI Features

- **UIStyler**: Utility class for consistent color schemes, fonts, and styling across the application
- **RoundButton**: Custom buttons with smooth hover effects and rounded corners
- **RoundPanel**: Card-like containers with shadows and rounded corners
- **RoundTextField**: Text input fields with modern styling and focus effects
- **Customized Dialogs**: Redesigned food entry and servings dialogs with improved aesthetics

## Requirements

- Java Development Kit (JDK) 8 or higher
- Swing GUI library (included in standard JDK)

## Project Structure

- `src/main/java/com/yada`: Main application code
  - `model`: Data model classes (Food, UserProfile, etc.)
  - `ui`: User interface components
    - `util`: Custom UI components and styling utilities
  - `util`: Utility classes and command pattern
  - `user`: User authentication and management

- `database`: Data storage directory (created automatically)
  - `foods.txt`: Food database storage
  - `logs.txt`: Daily logs storage
  - `users.txt`: User account storage
  - `profiles/`: Directory containing user profiles
    - `username_profile.txt`: Individual profile for each user

## Compiling and Running the Application

### Method 1: Using the Command Line

1. Compile the application (make sure to follow this exact order):
   ```bash
   mkdir -p bin
   javac -d bin -sourcepath src/main/java src/main/java/com/yada/ui/util/*.java src/main/java/com/yada/model/*.java src/main/java/com/yada/user/*.java src/main/java/com/yada/util/*.java src/main/java/com/yada/ui/*.java src/main/java/com/yada/DietManager.java
   ```

   *Note: The compilation order is important. We first compile the UI utility classes, then the model, user, util, and ui packages, and finally the DietManager class.*

2. Run the application:
   ```bash
   java -cp bin com.yada.DietManager
   ```

### Method 2: Using an IDE

1. Import the project into your favorite IDE (Eclipse, IntelliJ IDEA, etc.)
2. Set up the project to use JDK 8 or higher
3. Ensure the compilation order follows the dependencies:
   - First: ui/util package (custom UI components)
   - Next: model package
   - Next: user package
   - Next: util package
   - Next: ui package
   - Finally: DietManager.java
4. Run the `com.yada.DietManager` class which contains the main method

## Usage Guide

1. Launch the application
2. Create a new account through the registration screen or log in with existing credentials
3. New users will be prompted to set up their profile with:
   - Gender, height, weight, and age
   - Activity level (with detailed descriptions)
   - Calorie calculation method preference
4. Navigate through the application using the tabs:
   - **Foods**: Browse, search, and add foods to the database
   - **Daily Log**: Track your food consumption for each day
   - **Profile**: View and edit your profile information
5. Add foods to your daily log by selecting them from the Foods tab
6. Monitor your daily calorie intake against your target
7. Use the date navigation to track your diet over time
8. Save your data using the File menu

## Troubleshooting

If you encounter compilation errors, ensure you're compiling the files in the correct order as mentioned above. The application has dependencies between packages that require a specific compilation sequence.







