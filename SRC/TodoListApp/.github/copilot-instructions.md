# Copilot Instructions for TodoListApp

## Architecture Overview
This is a Java Swing application following MVC + Dependency Injection pattern. Main components:
- **TodoListApp.java**: Entry point, initializes DI, global exception handler, and launches main window.
- **DI/MainWindowDI.java**: Dependency injection container creating MVC triads for MainWindow, Login, and Todo modules.
- **Controller/**: Handles UI events, coordinates between View and Model.
- **Model/**: Business logic, data access (DB, API clients).
- **View/**: Swing UI components, listeners.
- **Interface/**: Defines contracts for all components.

Data flow: UI events → Controller → Model (async DB ops via ExecutorService) → View updates via SwingUtilities.invokeLater.

## Key Patterns
- **Interface-driven design**: All major classes implement interfaces (e.g., IMainWindowController, IDBClient).
- **Dependency Injection**: MainWindowDI injects dependencies; avoid direct instantiation.
- **View Wrapping**: Use ViewProxyUtil.WrapView() for thread-safe Swing operations.
- **Async DB Operations**: Use single-thread ExecutorService for sequential DB access.
- **Global Exception Handling**: Uncaught exceptions logged and fatal errors notified via controller.
- **Configuration**: Settings in bin/Settings.properties; load via IGetProperties.

## Build & Run
- Compile: `javac -cp "lib/*" -d bin src/**/*.java`
- Run: `java -cp "bin;lib/*" TodoListApp`
- Dependencies: MySQL JDBC driver in lib/, LM Studio API integration.

## Examples
- Creating new MVC: Follow MainWindowDI pattern - create DI subclass, implement interfaces.
- DB Access: Use IDBClient with ExecutorService.submit() for async queries.
- AI Integration: ILMStudioAPIClient for chat completions with system prompt.

## Important Files
- [src/TodoListApp.java](src/TodoListApp.java): App initialization.
- [src/DI/MainWindowDI.java](src/DI/MainWindowDI.java): DI setup.
- [bin/Settings.properties](bin/Settings.properties): Configuration.</content>
<parameter name="filePath">d:\ドキュメント\Todo_Java_Swing\SRC\TodoListApp\.github\copilot-instructions.md