# University CMS

### Class Diagram

Below is a class diagram of our project. It helps to visualize the structure of the project and the relationships between different classes.

![class diagram](docs/university-cms.png)

**Student** - reflects the student in the learning process; <br>
**Teacher** - corresponds to the teacher in a certain educational institution, has a certain set of courses created by him; <br>
**Course** - corresponds to the training course created by a particular teacher, may have registered students; <br>
**Topic** - corresponds to a specific topic from the course, which has a name and description; <br>
**Mark** - has a certain meaning, topic and belongs to the student; <br>
**Group** - corresponds to a group in an educational institution, consists of a certain number of stuents; <br>
**Administrator** - a person who has advanced management of students, teachers, courses, groups; <br>
**Schedule** - contains a set of study days; <br>
**StudyDay** - corresponds to one study day with the date and day of the week, contains a set of lessons; <br>
**Lesson** - corresponds to one lesson in the study day, contains the start time of the lesson and the end as well as the course; <br>
**WeekDay** - contains seven days of the week, Monday to Sunday.


### Business Requirements

**1. Roles & Login**

- The participant of the educational process should be able to log in as a *teacher* or *student*.

- Also, the *administrator* must be able to log in.

**2. User logged in as Teacher**

- User can see and navigate to `My Schedule` menu.

- User should see own Teacher schedule according with selected date/range filter.

- The user should be able to create/update/delete courses and view their courses.

- The user as a teacher can add and remove students (or entire groups of students) from their courses.

- The user should be able to evaluate the student on a specific topic from his course.

**3. User logged in as Student**

- User can see and navigate to `My Schedule` menu.

- User should see own Student schedule according with selected date/range filter.

- The user can view the courses on which he is registered.

- The user can view his marks on a specific course.

- The user as a student can view other students who belong to the same course or group (each student must necessarily belong to a certain group).

**4. Administrator capabilities**

- The administrator must be able to create/update/delete teachers, students, groups and courses.

- The administrator distributes and adds students to the groups.

# Task 3.1 Planning: Decompose university

**Important: In the next series of tasks you're going to develop Univesity Schedule web application, make sure to give repo meaningful name (ex. university-cms)**

**Assignment:**

1. Analyze and decompose University (create UML class diagram for application).

You should make a research and describe university structure. The main feature of the application is Class Timetable for students and teachers. Students or teachers can get their timetable for a day or for a month.

2. Add png image to the separate GitLab project.
3. Add text description of main user stories using markup language Example:

```
Teacher can view own schedule flow:

Given user is logged on as Teacher

- User can see and navigate to `My Schedule` menu
- User should see own Teacher schedule according with selected date/range filter
```
