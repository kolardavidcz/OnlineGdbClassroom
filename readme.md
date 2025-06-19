# OnlineGdbClassroom | [Git codebase](https://gitlab.fit.cvut.cz/kolard20/onlinegdbclassroom)

A Java application for generating and managing programming assignments
on [onlineGDB/classroom](https://www.onlinegdb.com/classroom) platform.

![preview](https://i.imgur.com/NVc72Nb.png)

## Project Description

OnlineGdbClassroom is designed to streamline the process of creating programming assignments for the pre-PA1 course at
UCT Prague. It provides a user-friendly interface for generating test cases and managing assignments that can be
imported to the onlineGDB platform.

## Features

- JavaFX-based graphical user interface for assignment creation
- Test case generation using regular expressions
- File-based input/output management
- JSON export functionality for onlineGDB platform integration

## Dependencies

- JavaFX (UI framework)
- JSOUP (HTML parsing)
- org.json (JSON handling)
- rgxgen (Regular expression generation)
- Java 11 or higher

## Customization
- **tests** visibility and flexibility could be customized
  - setupable in `src/main/java/cz/vscht/bioinformatics/kolar/onlinegdbclassroom/Main.java`
  - default setup is set Strict case mating, and Enable with Input, User Output and Expected Output
  - ![visibility, mating sensitivity](https://i.imgur.com/e74XcV3.png)

## Development Notes

### Project Structure

- `src/main/java/`
  - UI components and controllers
  - Test builders and generators
  - File handling utilities
  - Connection management

### Testing

The project includes comprehensive unit tests for:
- Regular expression combinations
- File operations
- Builder connections
- Test case generation

### Controversial decidions
- `BuiledConnector`
  - `BuilderConnection.getInputs()` must be called before `BuilderConnection.getOutputs` (in case of `outputIdenticalToInput`)
    - I decided on this solution in contrast to having null flag, in case someone want to code metode, that add more inputs/outputs
- `MyJsonFormatBuilder`
  - text is purifed late at this final stage
    - text is collected from UI = `MainMenuController` -> `BuilderController` -> `FinalBuilder` -> `MyJsonFormatBuilder`
    - text is public in `BuilderConnection` to indicate something is wrong. I could make a getter, however
      - because its bit more complicated I decided, that it is not responsibility of connector (`BuilderController`)
      - and made it some else's problem (eg. classes that interacts with JSON files)