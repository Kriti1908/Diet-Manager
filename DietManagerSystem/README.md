# YADA (Yet Another Diet Assistant)

YADA is a diet management system designed to help users track their food consumption and calorie intake.

## Features

- Food database management (basic and composite foods)
- Daily food consumption logging
- User profile management with different calorie calculation methods
- Command history with unlimited undo/redo functionality
- Keyword-based food search

## How to Run

1. Compile the code with a C++ compiler:
   ```
   g++ *.cpp -o yada -std=c++17
   ```

2. Run the application:
   ```
   ./yada
   ```

## Using the Application

### Food Database Management

- View all foods in the database
- Search for foods using keywords
- Add new basic foods (name, keywords, calories)
- Create composite foods from existing foods

### Daily Log Management

- View the current day's food consumption
- Add foods to your daily log
- Remove foods from your log
- Change the current date to view or update past logs
- See your calorie consumption vs. target

### Diet Profile Management

- Update your personal information (gender, height, weight, age, activity level)
- Change the calorie calculation method
- View your target calorie intake

### Command History

- Undo previous actions
- Redo undone actions
- View command history

## Initial Data

The system comes with a pre-populated food database (`foods.txt`) containing basic foods and some composite foods.

## File Formats

- `foods.txt`: Stores basic and composite foods
- `log.txt`: Stores daily food consumption logs

Both files are in text format and can be viewed or edited with a standard text editor if needed.
