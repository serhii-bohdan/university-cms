# University CMS

[![Java](https://img.shields.io/badge/Java-17-brightgreen)](https://docs.oracle.com/en/java/javase/17/)
[![Spring](https://img.shields.io/badge/Spring-Boot%20|%20MVC%20|%20Security-blue)](https://docs.spring.io/spring-framework/reference/index.html)
[![Database](https://img.shields.io/badge/Database-PostgreSQL-yellow)](https://www.postgresql.org/)
[![Lombok](https://img.shields.io/badge/Lombok-1.18.30-red)](https://projectlombok.org/download)
[![MapStruct](https://img.shields.io/badge/MapStruct-1.5.5.Final-purple)](https://mapstruct.org/documentation/1.5/reference/html/)

## Motivation & Goal

The creation of **University CMS** was driven by several key motivations:
- **Learning New Technologies**: To master modern Java web development tools like `Spring MVC`, `Spring Security`, `Bean Validation`, `Thymeleaf`, `HTML`, and `CSS`.
- **Training Milestone**: As part of the Foxminded training program, this project marks a significant step toward completing the course successfully.
- **Full Development Cycle**: To gain deeper insights into the complete software development lifecycle, from design to deployment.

### Technologies Used
- **Core**: *Java 17*
- **Frameworks**: *Spring Boot, Spring MVC, Spring Data, Spring Security*
- **Persistence**: *Hibernate, PostgreSQL, Flyway*
- **Utilities**: *MapStruct, Lombok*
- **Testing**: *JUnit 5, Mockito, Testcontainers*
- **Build & VCS**: *Maven, Git*
- **DevOps**: *Docker, GitLab CI*
- **Frontend**: *HTML, CSS, Thymeleaf, JavaScript*

## Note
From the very beginning, the development of this project was carried out on [GitLab](https://gitlab.com/), this explains the fact that there is a
configuration file `.gitlab-ci.yml` for CI. At the end of the main part of the development, the project was moved to [GitHub](https://github.com/).

## Description

### What is University CMS?
University CMS is a web platform designed to streamline and automate the educational process, fostering productive interaction between teachers and students.

### What Problems Does It Solve?
Effective time management, planning, and meeting deadlines are critical for successful learning. University CMS addresses these by:
- Allowing teachers to post educational materials.
- Enabling personalized scheduling and lesson planning for students and teachers.
- Promoting better organization and time management skills.

### How to Use It?
The platform supports four user roles with distinct responsibilities, adhering to the **Single Responsibility Principle**:

| Role          | Responsibilities                                                                                   |
|---------------|----------------------------------------------------------------------------------------------------|
| **Administrator** | Manages managers: adds, edits, and deletes accounts. No access to teachers or students.           |
| **Manager**       | Manages teachers, students, and groups: creates, edits, deletes accounts, and assigns groups. Has access to teachers‘ and students’ schedules. |
| **Teacher**       | Manages courses: creates, edits, adds students, schedules lessons, and evaluates student progress.|
| **Student**       | Views course materials, tracks progress via evaluations, and manages their own schedule.         |

## Features
- **Role-Based Access**: Secure, distinct functionalities for each user type.
- **Course Management**: Teachers can create, manage, and evaluate courses.
- **Scheduling**: Personalized schedules with lesson planning capabilities.
- **Database-Driven**: Uses PostgreSQL with Flyway for schema migrations.
- **CI Pipeline**: Automated builds via GitLab CI and Docker.

## Install & Run

### Prerequisites
- **Git**: Installed for cloning the repository ([Guide](https://git-scm.com/book/en/v2)).
- **Docker**: Optional for containerized runs or database setup ([Docker Desktop](https://www.docker.com/products/docker-desktop/)).
- **Java 17**: Required for building and running locally.
- **PostgreSQL**: Optional if not using Docker for the database.

Go to the folder where you want to install the project. Open Git Bash in it and enter the command:

```
git clone https://github.com/serhii-bohdan/university-cms.git
```

This way you will have the app installed.

There are several ways to **run** the application **locally**. Consider them.
1) Docker Container<br>
   To use this method you must have Docker installed on your machine. Run it and make sure the docker daemon is running. Next, you should go to the root of the project you just downloaded. To run an application in a docker container, you should run the following command:

   ```
   docker compose up -d
   ```
   After all containers are successfully launched, go to your browser and enter the following URL: `http://localhost:8083/ui/v1/home`. As a result, you should see the welcome page of the application.


2) Build jar<br>
   This path requires more settings and services. You must have installed:
   - [x] Java 17 (JDK)
   - [x] PostgreSQL or Docker

   1. If you have PostgreSQL installed:<br>
      You should make the following settings:<br>
        a) Create a database, name it `university`.<br>
        b) Next, you should change some settings in the [application.yml](src/main/resources/application.yml) file:<br>
        - change the username `postgres` to the name of the database owner `university` (usually the database owner is `postgres`);<br>
        - finally replace `pass` with the database user password you use to connect to your local database.

        At the end of these settings, you should get the following [application.yml](src/main/resources/application.yml) content:
        ```yml
        spring:
          datasource:
          driver-class-name: org.postgresql.Driver
          url: jdbc:postgresql://localhost:5432/university
          username: your-database-owner-name
          password: your-local-database-password
        ```
    2. If you have Docker installed:<br>
       a) Run Docker on your device and make sure docker daemon is running.<br>
       b) Perform the following command:
       ```
       docker run -it --rm --detach \
            --name db \
            -e POSTGRES_USER=postgres \
            -e POSTGRES_PASSWORD=pass \
            -e POSTGRES_DB=university \
            -p 5432:5432 \
            postgres:15.3
       ```

    Now you have a database in which the necessary data will be stored. And modifying the [application.yml](src/main/resources/application.yml) file will ensure that the application can successfully connect to this database at runtime. Now you can run the application by executing the following commands in the root of the project:<br>

    - for Windows (cmd)
    ```
     mvnw.cmd spring-boot:run
    ```
    - for Linux/MacOS
    ```
     ./mvnw spring-boot:run
    ```

   Next, go to your browser and enter the following URL: `http://localhost:8083/ui/v1/home`. As a result, you should see the welcome page of the application.

## Perform authorization
In order to log in, you need to click the "Log in" button in the upper right corner of the welcome page. Below are the data for authorizing users with different roles. Use them to continue working.
- admin: `username - anthony.taylor@gmail.com`, `password - admin1234`;
- manager: `username - alex.brown@gmail.com`, `password - 7aB#3mW8!yT4`;
- teacher: `username - john.doe@gmail.com`, `password - et!@-Lj^rd123`;
- student: `username - taylor.smith789@gmail.com`, `password - D1@F3^G%y&`.

## Tests
The application has a set of unit tests that you can also run and verify that they pass successfully. What you need to have to run the tests:
- [x] Java 17 (JDK)
- [x] Docker

*Why do you need Docker to run tests?* The reason is that the tests for some classes use a database that is deploying in a docker container, that is, we are dealing with Testcontainers. Therefore, before running the tests, make sure that the docker daemon is running on your device. Next, execute the following command in the root of the project:<br>

* for Windows (cmd)
```
mvnw.cmd test
```
* for Linux/MacOS
```
./mvnw test
```
She will do the tests.

### Class Diagram

Below is a class diagram of our project. It helps to visualize the structure of the project and the relationships between different classes.

![class diagram](docs/university-cms.svg)

**Admin** - a user who manages managers in the system. <br>
**Manager** - a user who has advanced management of students, teachers, groups. Also has access to teachers‘ and students’ schedules.; <br>
**Student** - reflects the student in the learning process; <br>
**Teacher** - corresponds to the teacher in a certain educational institution, has a certain set of courses created by him; <br>
**Course** - corresponds to the training course created by a particular teacher, may have registered students; <br>
**Topic** - corresponds to a specific topic from the course, which has a name and description; <br>
**Mark** - has a certain meaning, topic and belongs to the student; <br>
**Group** - corresponds to a group in an educational institution, consists of a certain number of students; <br>
**Schedule** - contains a set of lessons; <br>
**Lesson** - corresponds to one lesson in the schedule, contains the date, start time and end time of the lesson, and is linked to a specific course; <br>
