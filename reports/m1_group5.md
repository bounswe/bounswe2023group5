# CMPE 451 Milestone 1 Report (Group 5)
### Contributors

- [Ali Başaran](https://github.com/bounswe/bounswe2023group5/wiki/Ali-Başaran)

- [Alperen Bırçak](https://github.com/bounswe/bounswe2023group5/wiki/Alperen-B%C4%B1r%C3%A7ak)

- [Arda Kabadayı](https://github.com/bounswe/bounswe2023group5/wiki/Arda-Kabaday%C4%B1)

- [Bilal Atım](https://github.com/bounswe/bounswe2023group5/wiki/Bilal-At%C4%B1m)

- [Can Uzduran](https://github.com/bounswe/bounswe2023group5/wiki/Can-Uzduran)

- [Çisel Zümbül](https://github.com/bounswe/bounswe2023group5/wiki/%C3%87isel-Z%C3%BCmb%C3%BCl)

- [Deniz Ünal](https://github.com/bounswe/bounswe2023group5/wiki/Deniz-%C3%9Cnal)

- [Ege Ekşi](https://github.com/bounswe/bounswe2023group5/wiki/Ege-Ek%C5%9Fi)

- [Halis Bal](https://github.com/bounswe/bounswe2023group5/wiki/Halis-Bal)

- [Harun Sami Çelik](https://github.com/bounswe/bounswe2023group5/wiki/Harun-Sami-%C3%87elik)

- [Mehmet Said Yolcu](https://github.com/bounswe/bounswe2023group5/wiki/Mehmet-Said-Yolcu)

- [Zeynep Baydemir](https://github.com/bounswe/bounswe2023group5/wiki/Zeynep-Baydemir)


# Table of contents

- [Table Of Contents](#table-of-contents)
- [1. Executive Summary](#1-executive-summary)
  - [1.1 Summary of the Project Status](#1-executive-summary)
  - [1.2 Changes That are Planned for Moving Forward](#1-executive-summary)
- [2. Customer Feedback and Reflections](#2-a-summary-of-the-customer-feedback-and-reflections)
- [3. List and Status of Deliverables](#3-list-and-status-of-deliverables)
- [4. Evaluation of the Status of Deliverables and its Impact on Our Project Plan (Reflection)](#4-evaluation-of-the-status-of-deliverables-and-its-impact-on-our-project-plan-reflection)
- [5. Evaluation of Tools and Processes](#5-evaluation-of-tools-and-processes-used-to-manage-the-project)
  - [5.1 Evaluation of Tools](#51-evaluation-of-tools)
  - [5.2 Evaluation of Processes](#52-evaluation-of-processes)
- [6. Requirements Addressed In This Milestone](#6-requirements-addressed-in-this-milestone)
- [7. Individual Contributions](#7-individual-contributions)
- [8. Deliverables](#8-deliverables)
  - [8.1 Requirements](#81-requirements)
    - [8.1.1 Software Requirement Specification](#811-software-requirement-specification)
    - [8.1.2 Mockups](#812-mockups)
    - [8.1.3 Scenarios](#813-scenarios)
  - [8.2 Software design documents in UML](#82-software-design-documents-in-uml)
    - [8.2.1 Sequence Diagrams](#821-sequence-diagrams)
  - [8.3 Project plan, RAM and Communication Plan](#83-project-plan-ram-and-communication-plan)
  - [8.4 Weekly Reports and Additional Meeting Notes](#84-weekly-reports)
  - [8.5 Pre-Release Version of Software](#85-pre-release-of-software)

## 1. Executive Summary
### 1.1 Summary of the Project Status
Up to the first milestone, we successfully implemented some of the required features for our game review platform. This includes all authentication features, game listing, filtering, and searching on the games page, as well as the game description page (except reviews and forums). We were also able to deploy our applications. In this way, we were able to complete the demo without any mock data. Additionally, we made significant improvements to the backend, particularly in the area of continuous integration and continuous deployment (CI/CD), although it's not fully functional at the moment. So, in general, we have reached the intended stage of development.
### 1.2 Changes That are Planned for Moving Forward
For the future, we have identified two changes in our plan. Firstly, we noticed that there were discrepancies in the appearance of our platform on the two different platforms (mobile and frontend) due to limited collaboration between the mobile and frontend teams for design. To solve this problem, we plan to increase the frequency of meetings between these teams to guarantee a consistent user interface between both platforms.

Secondly, based on feedback received during the demo, we believe that it is necessary to re-evaluate the planned avatar feature. We will conduct further discussions with our customers to meet their expectations and needs.

## 2. A summary of the customer feedback and reflections

Initially, we had negative customer feedback on our requirements. We learned that there has been some miscommunication on the importance of “Domain Specific” features. The meaning of that term, and how it applies to our project has been hard to grasp, so we had a long discussion with the product owners. After that discussion, we decided to research and brainstorm some ideas for domain specific features. We came up with a handful of ideas for features and compiled them in a wiki page. Following the research, we did a meeting with stakeholders to discuss those ideas. The meeting went great, and we decided on focusing on these new features while leaving some less important ones behind. 
Another important source of feedback was our milestone presentation. During the presentation, some issues with our initial release were discussed. One feedback was the inconsistent style and colors between the web ui and the mobile app. Moving forward, we will have better communication between the front-end teams so that this issue can be resolved. Another discussion topic was the “Avatars” function. While not implemented yet, this functionality seems important to the stakeholders and we will need to have a discussion on it.


## 3. List and Status of Deliverables
| Deliverable Name | Delivery Status | Due Date | Delivery Date |
| --- | --- | --- | --- |
| Software Requirements Specification | Delivered | 03.11.2023 | 03.11.2023 |
| Class Diagram |  Delivered | 03.11.2023 | 03.11.2023 |
| Sequence Diagram | Delivered | 03.11.2023 | 03.11.2023 |
| Use Case Diagram | Delivered | 03.11.2023 | 03.11.2023 |
| Scenarios | Delivered  | 03.11.2023 | 03.11.2023 |
| Mockups | Delivered  | 03.11.2023 | 03.11.2023 |
| Project Plan | Delivered  | 03.11.2023 | 03.11.2023 |
| Communication Plan | Delivered | 03.11.2023 | 03.11.2023 |
| Responsibility Assignment Matrix| Delivered | 03.11.2023 | 03.11.2023 |
| Weekly Reports | Delivered | 03.11.2023 | 03.11.2023 |
| Additional Meeting Notes | Delivered | 03.11.2023 | 03.11.2023 |
| Milestone Review | Delivered | 03.11.2023 | 03.11.2023 |
| Individual Contributions | Delivered | 03.11.2023 | 03.11.2023 |
| A Pre-Release Version of Software | Delivered | 31.10.2023 | 31.10.2023 |


## 4. Evaluation of the Status of Deliverables and its Impact on Our Project Plan (Reflection)
### [4.1 Software Requirements Specification](https://github.com/bounswe/bounswe2023group5/wiki/Requirements)
In our previous course (352), we had already completed the software requirement specifications. Nevertheless, during the initial two weeks of this semester, we made revisions and adjustments based on valuable customer feedback. As a result, the software requirements specification is now finalized and includes specific features relevant to our project's domain. [Here](https://github.com/bounswe/bounswe2023group5/wiki/Domain-Specific-Feature-Ideas) you can reach the domain-specific elements that are added to our requirements. Notably, we have removed any requirements related to events since they are no longer necessary. These changes were thoroughly discussed in our meetings, and you can access the relevant discussions by referring to the following issue numbers: [#370](https://github.com/bounswe/bounswe2023group5/issues/370), [#371](https://github.com/bounswe/bounswe2023group5/issues/371), [#377](https://github.com/bounswe/bounswe2023group5/issues/377), [#428](https://github.com/bounswe/bounswe2023group5/issues/428).
### 4.2 Software Design (UML Diagrams):Use-Case Diagram, Class Diagram, Sequence Diagram
In the context of our prior course, 352, we had already developed Use-Case Diagrams, Class Diagrams, and Sequence Diagrams. However, in the current milestone of our project, we have continued to enhance these diagrams as the project evolves. This process has involved the incorporation of domain-specific elements into the diagrams, aligning them more closely with the project's specific requirements. Additionally, to streamline the diagrams and focus on the project's core objectives, we have eliminated any components related to events that are no longer needed. You can see the related issues for the UML diagrams: <br>
Use-Case Diagram: [#614](https://github.com/bounswe/bounswe2023group5/issues/614) <br>
Class Diagram: [#424](https://github.com/bounswe/bounswe2023group5/issues/424), [#603](https://github.com/bounswe/bounswe2023group5/issues/603) <br>
Sequence Diagram: [#607](https://github.com/bounswe/bounswe2023group5/issues/607)
### 4.3 Scenarios and Mockups
While we had scenarios and mockups ready from the previous semester, the ongoing development of the project prompted necessary adjustments. These modifications were made to ensure that the scenarios and mockups aligned seamlessly with the evolving project dynamics. Our updates were primarily driven by changes in the requirements specification. In this process, we introduced domain-specific elements to enhance the accuracy of our representations, and we pruned functionalities that had become obsolete or were no longer deemed necessary. Notably, any features related to events were also removed to maintain focus on the project's current objectives.

### 4.4 Project Plan, Communication Plan, Responsibility Assignment Matrix
#### 4.4.1 Project Plan
The project plan is vital for managing the project's duration, much like scheduling processes in an operating system. A well-structured plan helps distribute tasks efficiently, enabling us to handle multiple tasks simultaneously in a shorter time, reducing last-minute rush and preventing critical task omissions.
<br>
In this context, we utilized GitHub's project functionality to formulate our project plan. Thanks to its integration with GitHub issues, it simplified the process of monitoring our work and enabled effortless tracking of milestone progress.
#### 4.4.2 Communication Plan
Because there were initially no communication issues on our side, we initially decided to stick with the same communication plan we had used in the previous semester without making any alterations.
<br>
However, we have since made a slight adjustment to the communication plan. We now conduct all of our meetings online, with the exception of a face-to-face meeting on Tuesday during the lab hour.
#### 4.4.3 Responsibility Assignment Matrix (RAM)
We've updated our Responsibility Assignment Matrix (RAM) from the previous semester to include all the tasks completed by each group member since the start of this semester. We've organized the tasks for this milestone into seven categories within the RAM: Frontend Web, Updating Requirements/Diagrams, Mobile, Backend, Management & Planning, Milestone 1, and lastly, Meeting Notes and Weekly Reports.

### 4.5 Weekly reports and any additional meeting notes
In each weekly meeting, we filled a weekly report that includes these sections:
- Progress Summary
- What was planned for the week? How did it go?
- Completed tasks that were not planned for the week
- Planned vs. Actual
- Your plans for the next week
- Risks
- Participants

<br> The meeting moderators generate report outlines, and each team completes the sections relevant to their tasks. This structured weekly report simplifies work tracking and aids in easy weekly planning for our team.

### 4.6 Milestone Review
We have successfully completed the milestone review deliverable, which also encompasses this evaluation that you are reading. To accomplish this deliverable, we held a meeting where we distributed the tasks outlined within it. Our team members completed these tasks, resulting in the creation of the milestone review file.

### 4.7 Individual Contributions
The Individual Contributions Reports encompass all the tasks completed by our team members since the start of this semester. These reports are structured like a tree, allowing easy access to the details of any mentioned issue or pull request contributed by team members on a weekly basis. They provide a comprehensive summary of the work accomplished during this semester. For this semester's individual contributions, we have categorized issues into code-related and management-related sections to distinguish between them. Additionally, we have included information about the pull requests we initiated, reviewed, and merged. In cases where conflicts arose in the pull requests, we have provided explanations. We have also included links to the issues and pull requests and any additional necessary information. Each team member individually documents their work, and the entire report is a collaborative effort by all team members.

### 4.8 A Pre-Release Version of Our Software
We have effectively developed the pre-release version of our software. Within the pre-release tag, we have provided a detailed description of the features we have implemented, along with the relevant links for accessing our application. Additionally, we have included essential assets such as the mobile application APK, a frontend startup guide, and the source code of our project.


## 5. Evaluation of Tools and Processes Used to Manage the Project

### 5.1 Evaluation of Tools
#### 5.1.1 Discord
Discord is an instant messaging and digital distribution platform where users communicate with voice calls, video calls, and text messages.It has the ability to save chats. Since it is used in class-wide communication for CMPE 451 course, we decided to use to Discord as our main communication platform between group members.

In discord we have created different channels for different matters. We try to discuss each subject on the channel specifically provided for it. This helps us to not lose the thread of conversation. If this were not the case and all the matters were to be discussed at a single channel, the threads of communication would quickly get messed up and the communication will be inefficient if not impossible. 

#### 5.1.2 GitHub
GitHub is a version control system that is used to store the codebase and document base of the project. It is the platform that provides the public face of the project team. On our page on GitHub, interested parties can find information about what our project is about, who the members of the team are, requirements for the project, our meeting notes etc. It also has many features that enable us to manage our codebase. This features include collaboration features, issue creation and management feature, version control feauture. 

Issue creation and management in particular is a great way to partition the problem of completing the project into many small issues of overcomeable size, and assigning these issues to particular people. To enhance the power of this feature we created labels that show the progress status, priority of completion, and type of the issue, and also the team that the issue is related to. We also assign a reviewer to each issue to check the quality of the work done by the assignee. Also each issue has a deadline, which allows us to plan ahead. The comment thread below the issues is a great place to conduct the communication relating to that issue.

Version control feature is founded upon the idea of branches. A branch is an ordered list of nodes, where each node represents the version of the project at a particular point of time. Branches and branch management allows the users of GitHub to create parallel histories of project and merging these histories or drawing back from a node on a history to a previous node. It also enables the users to concurrently work on and edit the project via creating different branches. We drew advantage from this functionality extensively.

#### 5.1.3 Trello
Trello is a web-based, kanban-style, list-making application. .It is composed of boards where another board can be created for each new subject. We used Trello in out online meetings to take notes and also create the preparatory task distribution. We also used Trello as a storage tool, to record the previous task distributions.


### 5.2 Evaluation of Processes
#### 5.2.1 Issues
We have extensively made use of issue feature of GitHub. It enables to explicitly and recordably assign a task to a certain person for a pre-determined amount of time. Comment facility below the issues enables issue related communication via giving it its own channel of communication. We did not just use this issue feature as it is provided by GitHub, but made certain improvements and tailorings to make it better meet our needs. First of all we created new labels to denote the priority level, current status, type of the issue, and the sub-team that the issue is related to. Also we created an issue template of our own to assign explicit reviewers and deadlines to issues created.

#### 5.2.2. Team Meetings
Each week at lab time we have a fixed meeting with the team. During this meeting we discuss and record the progress we made on the project during this week, specifically we detect the completed tasks that were planned last week, tasks that were completed but was not pre-planned, and tasks that were planned to be completed but are not. During these meetings we make plans for the succeeding week, also take note the risks we may encounter during the following week. We write a lab report in this meeting and record our discussion results.

Other than this fixed meeting, we meet via the internet whenever there arises  need. This meeting usually take place on Discord, as it enables us the share both our voices and screens. During this meetings, we discuss the current status of the project, sometimes distribute tasks among us, make decisions regarding to the project, and reach to a certain common understanding regarding to what needs to be done.

#### 5.2.3 Teaching Assistant / Stakeholder Meetings
In our fixed meetings we also meet with our stakeholder (which is also our teaching assistant). During these meeting we discuss the current status of our project, also showcase the working part of out project if the stakeholder requests. Other than that, we ask questions to our stakeholder and try to understand what he expects from us, what he wants the finished product to look, or what the functionality of the product must be at certain stages of its development. 

#### 5.2.4 Version Control
Version control, also known as source control, is the practice of tracking and managing changes to software code. Version control systems are software tools that help software teams manage changes to source code over time. As development environments have accelerated, version control systems help software teams work faster and smarter. They are especially useful for DevOps teams since they help them to reduce development time and increase successful deployments.

Version control software keeps track of every modification to the code in a special kind of database. If a mistake is made, developers can turn back the clock and compare earlier versions of the code to help fix the mistake while minimizing disruption to all team members.

We have used this process of GitHub extensively. We have stored our code in GitHub and while merging different histories, or undoing a modification, used version control related tools supplied by GitHub.

## 6. Requirements Addressed In This Milestone
In Milestone 1, we have made significant progress in addressing several key requirements, primarily focusing on the authentication aspect outlined under sub-section 1.1.1 of the project requirements. 


With the latest version of our platform that we’ve presented in the demo:
* Users are able to register to the platform providing a unique username, email and a valid password. (req. 1.1.1.1.1)
* Users are able to login to the platform providing their username and password. (1.1.1.2.1)
* Users are able to logout. (1.1.1.3.1)
* Users are able to change their passwords.  (1.1.1.4.1)
* Users are able to use forgot password getting through a verification process via providing their emails. (1.1.1.5.1)
Also we’ve focused on another fundamental requirements under the sub-section 1.1.8 of the project requirements which is game. After we’ve managed to complete main functionalities in game for registered users and the guest users,
* Users are able to filter games by their tags (1.1.8.1)
* Users are able to search games by their name (1.1.8.2)
* Users are able to access game specific info about the game like player type, genre, minimum system requirements. (1.1.8.4)


In addition to these functional requirements, we have also addressed security-related non-functional requirements:
* The platform generates a JWT token for every user, and each endpoint verifies the authorization header of incoming requests to ensure authentication of valid users' requests. (2.2.2.2 &  2.2.2.3)


## 7. Individual Contributions

- [Ali Başaran](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Ali-Başaran-‐-451)

- [Alperen Bırçak](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Alperen-B%C4%B1r%C3%A7ak-451)

- [Arda Kabadayı](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Arda-Kabaday%C4%B1-%E2%80%90-451)

- [Bilal Atım](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Bilal-At%C4%B1m-%E2%80%90-451)

- [Can Uzduran](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Can-Uzduran-%E2%80%90-451)

- [Çisel Zümbül](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-%C3%87isel-Z%C3%BCmb%C3%BCl-%E2%80%90-451-Milestone-1)

- [Deniz Ünal](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Deniz-%C3%9Cnal-%E2%80%90-451)

- [Ege Ekşi](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Ege-Ek%C5%9Fi-451)

- [Halis Bal](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Halis-Bal-451)

- [Harun Sami Çelik](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Harun-Sami-%C3%87elik-451)

- [Mehmet Said Yolcu](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Mehmet-Said-Yolcu-%E2%80%90-451)

- [Zeynep Baydemir](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Zeynep-Baydemir-%E2%80%90-451)

## 8. Deliverables
### 8.1 Requirements
#### 8.1.1 Software Requirement Specification
- [Requirements](https://github.com/bounswe/bounswe2023group5/wiki/Requirements)

#### 8.1.2 Mockups
- [Authentication Mockup](https://github.com/bounswe/bounswe2023group5/wiki/Authentication-Mock-Up)
- [Forum Mockup](https://github.com/bounswe/bounswe2023group5/wiki/Forum-Mockup)
- [Game Reviews Mockup](https://github.com/bounswe/bounswe2023group5/wiki/Game-Reviews-Mockup)
- [Games Page Mockup](https://github.com/bounswe/bounswe2023group5/wiki/Games-Page)
- [Group Page Mockup](https://github.com/bounswe/bounswe2023group5/wiki/Group-Pages)
- [Home Page Mockup](https://github.com/bounswe/bounswe2023group5/wiki/Home-Page)
- [Profile Page Mockup](https://github.com/bounswe/bounswe2023group5/wiki/Profile-Page-Mockup)

#### 8.1.3 Scenarios
- [Sign Up Scenario](https://github.com/bounswe/bounswe2023group5/wiki/Unregistered-User-Scenario:-Sign-Up)
- [Browsing Game Scenario](https://github.com/bounswe/bounswe2023group5/wiki/Unregistered-User-Scenario:-Search-for-a-Game-and-Browse-the-Game-Forum)
- [Create Post Scenario](https://github.com/bounswe/bounswe2023group5/wiki/Registered-User-Scenario:-Create-Post-in-a-Game-Forum)
- [Searching and Joining Group Scenario](https://github.com/bounswe/bounswe2023group5/wiki/Registered-User-Scenerio:-Search-For-Groups-and-Join-A-Group)

### 8.2 Software design documents in UML

- [Use Case Diagram](https://github.com/bounswe/bounswe2023group5/wiki/Use-Case-Diagram)
- [Sequence Diagrams](https://github.com/bounswe/bounswe2023group5/wiki/Sequence-Diagram) 
- [Class Diagrams](https://github.com/bounswe/bounswe2023group5/wiki/Class-Diagram)

### 8.3 Project plan, RAM and Communication Plan
- [Project Plan](https://github.com/orgs/bounswe/projects/21)
- [RAM](https://github.com/bounswe/bounswe2023group5/wiki/RAM)
- [Communication Plan](https://github.com/bounswe/bounswe2023group5/wiki/Communication-Plan)

### 8.4 Weekly Reports and Additional Meeting Notes
- [Weekly Reports](https://github.com/bounswe/bounswe2023group5/tree/main/reports)
- [Meeting 1](https://github.com/bounswe/bounswe2023group5/wiki/Meeting-%231-Notes-08.10.2023)

### 8.5 Pre-Release of Software 
- [Release](https://github.com/bounswe/bounswe2023group5/releases/tag/customer-milestone-1)


