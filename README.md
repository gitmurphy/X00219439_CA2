# Simple To-Do Application

### Student No.: X00219439
### Module: DevOps Continuous Integration DEVOH4000: 2024-25
### Program: BSc (Hons) Computing with Machine Learning/AI

This is a simple command line interface (CLI) To-Do application implemented using Java. The application includes basic functionality for adding tasks to a non-persisted ArrayList and displaying the list of tasks.

## Technologies Used

- Programming language: Java 17
- Build tool: Gradle
- Version control: Git
- CI/CD pipeline: Azure DevOps
- Unit testing framework: JUnit Jupiter 5.10.3
- Static code analysis and code quality checks: SonarCloud 
- Code coverage reporting: JaCoCo
- Security analysis: Microsoft Security DevOps
- Dependency vulnerability analysis: OWASP Dependency Check with National Vulnerability Database integration.

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

## CI/CD Pipeline Implementation
A continuous integration/ continuous deployment multi-stage pipeline for this application is implemented with Azure DevOps. Build, test, security check, deploy stages are automated by the pipeline.

1. Build Stage:
   - Runs Gradle build tasks to compile the code and create JAR artifacts.

2. Test Stage
   - Performs unit tests using JUnit
   - Generates code coverage reports using JaCoCo.
   - Connects to SonarCloud for static code analysis and quality reports.

3. Security Stage
   - Executes Microsoft Security DevOps tools like 'credscan' to check for hardcoded credentials.
   - Scans for OWASP dependency vulnerabilities leveraging a connection to the National Vulnerability Database API.

4. Deploy Stage
   - Deploy and run jar file in Dev environment

### Configuration
The pipeline configuration is defined in the azure-pipelines.yml file which is under version control. The file can be found in the projects root folder (X00219439_CA2).

### YAML Configuration
```
trigger:
  - main
  - development

pool:
  vmImage: 'ubuntu-latest'

variables:
  javaVersion: '17'

stages:
  - stage: build
    displayName: "Build Application"
    jobs:
      - job: Build
        displayName: "Build Job"
        steps:

          - script: chmod +x gradlew
            displayName: 'Make gradlew executable'

          # Ensure correct JDK is available
          - task: JavaToolInstaller@0
            inputs:
              versionSpec: '$(javaVersion)'
              jdkArchitectureOption: 'x64'
              jdkSourceOption: 'PreInstalled'

          # Run Gradle build task -> creates jar file
          - task: Gradle@3
            inputs:
              workingDirectory: ''
              gradleWrapperFile: 'gradlew'
              gradleOptions: '-Xmx3072m'
              javaHomeOption: 'JDKVersion'
              jdkVersionOption: '17'
              jdkArchitectureOption: 'x64'
              tasks: 'clean build'

          # Copy JAR files into the artifact staging directory for use in later stages
          - task: CopyFiles@2
            displayName: 'Copy jar files'
            inputs:
              contents: '**/build/libs/*.jar'
              targetFolder: '$(build.artifactStagingDirectory)'

          # Publish other build artifacts into the artifact staging directory for use in later stages
          - task: PublishBuildArtifacts@1
            inputs:
              pathToPublish: '$(Build.ArtifactStagingDirectory)'
              artifactName: 'drop'

  - stage: test
    displayName: "Test Application"
    dependsOn: build
    jobs:
      - job: Test
        displayName: "Test Job"
        steps:
          - checkout: self
            fetchDepth: 0

          - script: chmod +x gradlew
            displayName: 'Make gradlew executable'

          # Establish SonarCloud connection
          - task: SonarCloudPrepare@3
            inputs:
              SonarQube: 'SonarConnection'
              organization: 'x00219439'
              scannerMode: 'other'
              extraProperties: |
                sonar.projectKey=X00219439_X00219439_CA2
                sonar.projectName=X00219439_CA2

          # Download the JAR file and other build artifacts from the build stage
          - task: DownloadBuildArtifacts@0
            inputs:
              buildType: 'current'
              downloadType: 'single'
              artifactName: 'drop'
              downloadPath: '$(System.ArtifactsDirectory)'

          # Run Gradle test tasks
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
              tasks: 'test jacocoTestReport sonar'

          # Publish code coverage results to Azure
          - task: PublishCodeCoverageResults@2
            inputs:
              summaryFileLocation: '$(System.DefaultWorkingDirectory)/**/build/reports/jacoco/test/jacocoTestReport.xml'
              pathToSources: '$(System.DefaultWorkingDirectory)/app/src/main/java/'

          # Publish SonarCloud results
          - task: SonarCloudPublish@3
            inputs:
              pollingTimeoutSec: '300'

  - stage: security
    displayName: 'Security Scanning'
    dependsOn: test
    jobs:
      - job: "Security Checks"

        steps:
          # Download artifacts from the build stage
          - task: DownloadBuildArtifacts@0
            inputs:
              buildType: 'current'
              downloadType: 'single'
              artifactName: 'drop'
              downloadPath: '$(System.ArtifactsDirectory)'

          # Run Microsoft security scanning tools
          - task: MicrosoftSecurityDevOps@1
            inputs:
              categories: 'sources'  # Scan source code
              break: false
              tools: 'credscan'    # Basic credential scanning
              sourcePath: '$(System.DefaultWorkingDirectory)'

          # Run OWASP dependency security scanning
          - task: dependency-check-build-task@6
            displayName: 'Dependency Security Scanning'
            inputs:
              projectName: '$(Build.Repository.Name)'
              scanPath: '$(System.DefaultWorkingDirectory)'
              format: 'HTML'
              uploadReports: true
              uploadSARIFReport: true
              failOnCVSS: '7' # Fail build if vulnerabilities score is 7 - high severity
              nvdApiKey: 'ba4622e4-6fd2-40e0-b9ca-27e3939c7990'

  - stage: deploy
    displayName: "Deploy Application to Dev Environment"
    dependsOn: security
    jobs:
      - deployment: deploy
        environment: 'Dev'
        strategy:
          runOnce:
            deploy:
              steps:

                # Ensure correct JDK is available
                - task: JavaToolInstaller@0
                  inputs:
                    versionSpec: '$(javaVersion)'
                    jdkArchitectureOption: 'x64'
                    jdkSourceOption: 'PreInstalled'

                # Download the JAR file and other build artifacts from the build stage
                - task: DownloadBuildArtifacts@0
                  inputs:
                    buildType: 'current'
                    downloadType: 'single'
                    artifactName: 'drop'
                    downloadPath: '$(System.ArtifactsDirectory)'

                # Find the JAR file and run it
                - script: |
                    JAR_FILE=$(find $(System.ArtifactsDirectory) -name *.jar)
                    if [ -z "$JAR_FILE" ]; then
                      echo "No JAR file found"
                      exit 1
                    fi
                    echo "Found JAR file: $JAR_FILE"
                    java -jar $JAR_FILE &
                  displayName: 'Simple To-Do Application'
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
