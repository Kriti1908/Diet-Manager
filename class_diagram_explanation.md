# This is for `UML CLASS DIAGRAM`

## Food Hierarchy

- `Food` is an abstract base class with common attributes like `identifier`, `keywords`, and `calories`.
- `BasicFood` and `CompositeFood` inherit from `Food`.
- `CompositeFood` allows creating complex food items by combining other foods.

## Food Database

- `FoodDatabase` manages the collection of foods.
- Supports adding basic and composite foods.
- Provides search functionality with flexible keyword matching.
- Can load and save the database to a text file.

## Diet Profile

- `DietProfile` stores user's physical information.
- Supports multiple calorie calculation methods.
- Allows easy extension of calorie calculation methods.

## Daily Log

- `DailyLog` tracks food consumption by date.
- Supports adding and removing foods.
- Implements an undo mechanism using the Command pattern.
- Can load and save the log to a text file.

## Command Pattern

- `Command` abstract class enables undo functionality.
- `AddFoodCommand` is an example implementation.
- Allows tracking and undoing user actions.

## Diet Manager

- Central class that coordinates between `FoodDatabase`, `DietProfile`, and `DailyLog`.
- Manages the CLI interface.
- Composes other key components.

## Undo Functionality

- Each user action (like adding or removing a food) becomes a command object.
- The `DailyLog` maintains a history of these commands.
- Users can undo actions by calling the `undo()` method on the most recent command.

## Decoupling

- Separates the object that invokes the operation from the object that knows how to perform it.
- Allows for easy extension of actions without modifying existing classes.

## Tracked Operations

- Provides a way to log and potentially replay actions.
- Enables complex undo/redo mechanisms.

## Naming Conventions

- Prefix classes with `C_` (Class).
- Prefix abstract classes with `A_`.
- Prefix interfaces with `I_`.
- Use `{abstract}` for abstract methods.
- Use `<|--` for inheritance.
- Use `<|..` for interface implementation.

## Key Design Principles Addressed

- **Low Coupling**: Components are loosely connected.
- **High Cohesion**: Each class has a single, well-defined responsibility.
- **Separation of Concerns**: Different aspects of the system are handled by separate classes.
- **Information Hiding**: Private members and method encapsulation.
- **Open/Closed Principle**: Easy to extend (e.g., calorie calculation methods, food sources).
