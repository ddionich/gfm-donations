# GFM Recurring Donation CLI

This is a command-line tool that allows donors to set up recurring monthly donations to GoFundMe campaigns. 
Donors can specify a monthly donation limit and make recurring donations to specific campaigns. 
The CLI can accept input from a file or standard input (`stdin`).


## Setup Instructions


### 1. Run the program
You do not need to compile the program. If you have JRE installed in your computer,
the script will compile it if it does not exist, or just use it if it was compiled before.

To run the tool more conveniently, make the script `gfm-recurring` executable:
```bash 
chmod +x gfm-recurring
```

You can execute the program in two ways:

- Using a file as input

    Pass the input file as an argument:
    ```bash 
    ./gfm-recurring input.txt
    ```

- Using `stdin`

    Use cat or other commands to pipe input into the program:
    ```bash 
    cat input.txt | ./gfm-recurring
    ```


## Example Input

Below is an example of an input file, input.txt:

```text
Add Donor Greg $1000
Add Donor Janine $100
Add Campaign SaveTheDogs
Add Campaign HelpTheKids
Donate Greg SaveTheDogs $100
Donate Greg HelpTheKids $200
Donate Janine SaveTheDogs $50
```
## Expected Output

Given the above input, the tool will produce the following output:

```text
Donors:
Greg: Total: $300 Average: $150
Janine: Total: $50 Average: $50

Campaigns:
HelpTheKids: Total: $200
SaveTheDogs: Total: $150
```

This output shows a summary of each donor's total and average donations, 
as well as each campaign's total received donations, all in alphabetical order.

## Testing

This project is using:
- Jetbrains Kotlin test
- MockK
- jUnit 5

To run the tests execute: 

```bash 
./gradlew test
```

## Explanation of project

A decision was made to develop the project with the least amount of dependencies possible,
resulting in the usage of libraries solely for STDIN and testing purposes.

The project was developed in different layers, following a modular approach. 
Basic logic for dependency injection was implemented. 
Techniques such as inheritance, interfaces, abstract classes, and various Kotlin operators were utilized. 
An in-memory repository was also implemented to store and manage information. 
The primary goal was to establish a scalable project structure ready for the integration of technologies 
such as dependency management libraries, databases, and modeling libraries.

Custom exceptions, unit tests, and some integration tests were implemented. 
It is believed that this project is complete and robust, considering it was developed within a few hours. 
Additionally, a script was included to make the program's execution as seamless as possible for the user.

There are several areas for improvement, such as better Gradle dependency management to avoid future conflicts, 
and the possibility of adding automatic versioning using Gradle.