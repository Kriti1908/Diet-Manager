#ifndef COMMAND_H
#define COMMAND_H

#include <memory>
#include <string>
#include <vector>
#include <stack>

// Interface for all commands that can be undone
class I_Command {
public:
    virtual ~I_Command() = default;
    virtual void execute() = 0;
    virtual void undo() = 0;
    virtual std::string getDescription() const = 0;
};

// Command to add food to the daily log
class C_AddFoodCommand : public I_Command {
private:
    class C_DietManager* manager;
    std::string foodName;
    double servings;

public:
    C_AddFoodCommand(C_DietManager* manager, const std::string& foodName, double servings);
    void execute() override;
    void undo() override;
    std::string getDescription() const override;
};

// Command to remove food from the daily log
class C_RemoveFoodCommand : public I_Command {
private:
    class C_DietManager* manager;
    std::string foodName;
    double originalServings;

public:
    C_RemoveFoodCommand(C_DietManager* manager, const std::string& foodName);
    void execute() override;
    void undo() override;
    std::string getDescription() const override;
};

// Command to update profile information
class C_UpdateProfileCommand : public I_Command {
private:
    class C_DietManager* manager;
    std::string gender;
    double height;
    double weight;
    int age;
    std::string activityLevel;
    
    std::string oldGender;
    double oldHeight;
    double oldWeight;
    int oldAge;
    std::string oldActivityLevel;

public:
    C_UpdateProfileCommand(C_DietManager* manager, 
                         const std::string& gender, 
                         double height, 
                         double weight, 
                         int age, 
                         const std::string& activityLevel);
    void execute() override;
    void undo() override;
    std::string getDescription() const override;
};

// Command Manager to handle the command history and undo functionality
class C_CommandManager {
private:
    std::stack<std::shared_ptr<I_Command>> history;
    std::stack<std::shared_ptr<I_Command>> redoStack;

public:
    void executeCommand(std::shared_ptr<I_Command> command);
    bool canUndo() const;
    bool canRedo() const;
    void undo();
    void redo();
    void clearHistory();
    
    // Get descriptions of commands in history
    std::vector<std::string> getCommandHistory() const;
};

#endif // COMMAND_H
