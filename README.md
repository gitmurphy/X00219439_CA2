# Simple To-Do Application

### Student No.: X00219439
### Module: DevOps Continuous Integration DEVOH4000: 2024-25
### Program: BSc (Hons) Computing with Machine Learning/AI

This is a simple command line interface (CLI) To-Do application implemented using Java. The application includes basic functionality for adding tasks to a non-persisted ArrayList and displaying the list of tasks.

## Technologies Used

- Programming language: Java 17
- Build Tool: Gradle
- Version Control: Git
- Continuous Integration Pipeline: Azure DevOps
- Unit Testing Framework: JUnit Jupiter 5.10.3

## Local Development Setup

1. Environment Setup
- Install dependencies
    - Git (to clone the remote repository)
    - Java 17 or greater
    - Install Gradle or use Gradle wrapper included in the project.
    - Install Gradle dependencies with the following command:
    ```
    ./gradlew installDependencies
    ```

2. Clone remote git repository to your local machine:
```
git clone https://github.com/gitmurphy/X00219439_CA2.git
```

3. Build the application
To build the application execute the following command:
```
./gradlew build
```

4. Run the application
- Navigate to X00219439_CA2\src\main\java\com\todo
- Execute the following command to run the Java application:
```
java ToDoApp
```

5. Test the application
To run all unit tests execute the following command:
```
./gradlew test
```

## Application Features
The application includes the following features:
1. Add a task to the to-do list
2. Display tasks currently stored in the list

Once the application is started/run the user is presented with the following output to the command line:

Welcome to the To-Do List App!
Task List is empty.

Options:
1. Add Task
2. Exit
Choose an option: 

If the user selects **"1"** (Add Task) and enters a task name when prompted, the output will be as follows:

Task added: task name
Task List:
task name

Options:
1. Add Task
2. Exit
Choose an option: 

If the user selects **"2"** then the application will end.

## CI Pipeline Implementation
The continuous integration pipeline for this application is implemented with Azure DevOps. The build and test phases of code deployment are automated by the pipeline.

### Configuration
The pipeline configuration is defined in the azure-pipelines.yml file which is under version control. The file can be found in the projects root folder (X00219439_CA2).

### YAML Configuration
```
trigger:
- main
- development

pool:
  vmImage: 'ubuntu-latest'

steps:
- task: Gradle@3
  inputs:
    workingDirectory: ''
    gradleWrapperFile: 'gradlew'
    gradleOptions: '-Xmx3072m'
    javaHomeOption: 'JDKVersion'
    jdkVersionOption: '17'
    jdkArchitectureOption: 'x64'
    publishJUnitResults: true
    testResultsFiles: '**/TEST-*.xml'
    tasks: 'build'
```

By setting the **trigger** to main and development, Azure is informed that the steps in this pipeline should be executed when a change has been pushed to the main or development branch of the Github repository.

The **pool** configuration defines what virtual machine type that the pipeline should run on. In the case of this project the pipeline is running on the latest version of a ubunutu (Linux) machine.

Tasks to be executed by the pipeline are defined under **steps**. This gradle application automatically builds and tests the application once a build is executed.

## Branch Policies and Protection
The branching strategy for this project was designed to maintain code quality and facilitate protection rules and pull requests. The main branch should act as a stable 'production' branch while the development branch acts as an integration branch before final merging.

### Branches
- main
- development

### Branch Protection Rules
- Require Pull Request Reviews Before Merging
- Require Status Checks to Pass Before Merging
- Restrict Direct Pushes to Main

Note: These rules were applied but not activated due to the use of a free GitHub account. A team or enterprise account would be required to activate the rules.

## Testing Strategy

### Unit Testing
Unit testing is a strategy used to test individual sections or 'units' of code. This project contains 2 features, both of which are unit tested.

### Code Coverage
 Analysis of code coverage performed using JaCoCo to measure percentage of coverage. First code coverage analysis reported 38% code coverage. Although a goal of 80% code coverage was established, a final 53% coverage was reached due to complexity in testing the main method. Testing the main method in this CLI application would require simulated user input and simulated outputs. The ultimate version of this to-do list application would take the form of a REST API where endpoints could be tested rather than inputs to the CLI.

 ## References
 The application logic in this project was inspired by another To-Do List application available at:
 https://github.com/cimtiaz/todo-list.git

 The code was adapted to suit project requirements.
