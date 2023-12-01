# CMPE 451 Milestone 2 Report (Group 5)
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
  - [1.1 Summary of the Project Status](#11-summary-of-the-project-status)
- [2. Customer Feedback and Reflections](#2-customer-feedback-and-reflections)
- [3. Development-Related Changes Made Since Milestone 1 Or Planned For The Future](#3-development-related-changes-made-since-milestone-1-or-planned-for-the-future)
   - [3.1 General](#31-general)
   - [3.2 Frontend](#32-frontend)
   - [3.3 Backend](#33-backend)
   - [3.4 Mobile](#34-mobile)
- [4. List and status of deliverables.](#4-List-and-Status-of-Deliverables)
- [5. Progress according to requirements](#5-progress-according-to-requirements)
- [6. API endpoints ](#6-api-endpoints)
- [7. Generated unit test reports](#7-Generated-Unit-Test-Reports)
- [8. The general test plan for the project](#8-general-test-plan-for-the-project)
   - [8.1 Backend](#81-backend)
   - [8.2 Frontend](#82-front-end)
   - [8.3 Mobile](#83-mobile)
- [9. Status Of The Features Making Use Of The Annotation Technology](#9-status-of-the-features-making-use-of-the-annotation-technology)
- [10. Plans For Implementing Annotation-Related Functionalities](#10-plans-for-implementing-annotation-related-functionalities)
- [11. Individiual Contibutions](#11-Individual-Contributions)

## 1. Executive Summary
### 1.1 Summary of the Project Status

   By our second milestone, we successfully implemented several essential features on our game review platform. Initially, we segregated our user base into two categories: basic users and administrators. Administrators possess the capability to create, update, and delete games. We introduced group forums and commenting functionalities, fostering a dynamic social environment where users engage in discussions about games.

   One of our pivotal features is the group creation tool, empowering users to form their own groups. Within these groups, members can communicate via dedicated group forums. Additionally, we implemented a voting mechanism for written comments, allowing users to sort comments based on their popularity through votes.

   Furthermore, administrators can boost user engagement by introducing tags and achievements for games, groups, and comments. Tags serve as an organizational tool, categorizing content and enabling users to efficiently filter their searches.

   The Achievement feature was designed to instill a sense of progress among users and enhance their interaction with the platform. To showcase these achievements, we developed individual profile pages for each user. Within these profiles, users can access information about their achievements, recent activities, and customize their usernames and profile photos.

   In summary, we've successfully achieved the envisioned stage of development for our platform.



## 2. Customer Feedback and Reflections

We listened to the feedback from Milestone 1 and improved our project. First, we added domain specific features to our app that were asked for. Now, our app has achievements that fits different games, making it more fun and personal for users. Also, we fixed the issue with colors not matching in our web and mobile apps. We've made the look more consistent and nice.

In Milestone 2, we continued to make progress on addressing customer feedback and enhancing our project based on the valuable input we received. Here is a summary of the feedback we received during our Milestone 2 presentation and our reflections on each point:

**Game Descriptions and User-Friendliness**

*Feedback*: One of the concerns raised was that for users who are not familiar with the games, obtaining information about them can be challenging. The feedback highlighted the need for improved game descriptions and richer content.

*Reflection*: We acknowledge this feedback and understand the importance of providing clear and informative game descriptions. In response, we will focus on enhancing our game descriptions to make them more comprehensive and user-friendly. This will help both new and experienced users to better understand the games available on our platform.

**User Game Creation**

*Feedback*: It was raised that currently, only admins have the capability to create games, and there was a question about why regular users are not allowed to create games.

*Reflection*: We have taken note of this feedback and will engage in a discussion with our stakeholders regarding the possibility of allowing regular users to create games. We will carefully assess the implications of such a feature and work towards a decision that aligns with our project goals and user needs.

**Avatar Implementation**

*Feedback*: There was a difference of opinion regarding the implementation of avatars, and it was suggested that a dedicated meeting should be held to address this matter.

*Reflection*: We understand that avatars are a topic of interest and contention among our stakeholders. To ensure a thorough discussion and consensus on this feature, we will schedule a dedicated meeting to explore the various perspectives and make an informed decision regarding the integration of avatars into our system.

In conclusion, Milestone 2 has been instrumental in addressing some of the feedback from our previous milestone and identifying new areas for improvement. We remain committed to refining our project based on customer feedback and continuously working towards a more robust and user-centric application. Customer feedback is very valuable to us, and we appreciate customer's ongoing support and collaboration as we strive to deliver an exceptional product.

## 3. Development-Related Changes Made Since Milestone 1 Or Planned For The Future

### 3.1 General
- **Avatar Feature:**
We have realized that there is a difference in interpretation between our own understanding and Suzan Hoca's, regarding the avatar feature. Therefore, in the upcoming days, through discussions with Professor Suzan, we may change the avatar feature.

- **Admins capabilities:**
As we discussed in the second milestone presentation there may be some changes regarding to the capabilities of admin. For example 
in the future, game creation may not be limited just to admins; regular users could also have a capability to create games or do some other stuffs.

### 3.2 Frontend
- **Snapshot Test:**
In the future we want to add snapshot tests to ensure the consistency and robustness of our code. Basically snapshot tests are used for deciding whether the components are affected in the expected way or not from the changes in the code. With these addition we are expecting to find the UI error more easily.
- **UI Similarity with Mobile:**
After the first milestone we got a feedback from Suzan Hoca about the non-similarities between frontend and mobile UI. So according to that feedback, now before implementing a feature, we come together with mobile team and talk about the UI to be a consistency. Now our designs look more similar and coherent.
- **Considering the Reviews:**
After the first milestone we got a lot of reviews about the frontend part. Because we had a chance to see those reviews a little bit late(closer to milestone 2), we could not find a time to make some changes according to that. However, after the second milestone we are planning to make some changes especially for the register and login page. Also there may be some changes in the color of the UI.

### 3.3 Backend
- **Added CI/CD:**
Even though the implementation has started before the first milestone and the code was already written, the CI/CD only started functioning after first milestone. This made out deployment much more effective. As we didn't need as much time deploying changes manually to our AWS server, we had more time to write code. Also since it runs the unit tests while deploying we were able to be aware of the problems earlier and didn't require to run our unit tests by hand which also consumes time. In addition to that, CI/CD for backend also helped other teams. As we could deploy our changes to server earlier, they had more time to implement the front/mobile side of the functionalities we have implemented.
- **Convention of Populating Fields:**
We have started to populate the objects in our backends. This made it easier for front and mobile teams to access the fields of entities and accelerated their progress. It also helped us because before the milestone 1 we had to go back and change the api if some new feature needed a populated field which we didn't populate before. These changes consumed our time and sometimes broke the connections between backend and front/mobile, which resulted in a lot of bugs. By populating them initially, we no longer require to go back to API and make these changes that causes a lot of problems.
- **Increased Communication:**
We have started to communicate with other teams more actively before implementing our APIs to make sure everybody is on the same page. Previously we had some misunderstandings between some of our members which resulted in more work to do and features running late. Now we are more careful about details and we are making sure there will be no misunderstandings.

### 3.4 Mobile

- **Branch Naming Consistency:**
Aligned with the broader development team strategy, the mobile team has revised its branch naming conventions to foster synchronization across all teams. Now utilizing a format like "mobile/group-controller," this modification enhances clarity and coherence in collaborative development efforts.
- **Folder Structure Optimization:**
To streamline organization and enhance project maintainability, the mobile team has reconfigured the folder structure. Controllers are now consolidated within a dedicated folder. Furthermore, related files have been grouped together within specific folders, promoting a cohesive and accessible codebase. The placement of the utilities folder within another utilities folder ensures a more organized and hierarchical structure.
- **UI Harmony with Frontend:**
Recognizing the importance of a unified user experience, the mobile team has prioritized aligning the mobile UI with the frontend UI. This involves maintaining consistency in color schemes, button styles, font choices, and other visual elements.




## 4. List and Status of Deliverables
| Deliverable Name | Delivery Status | Due Date | Delivery Date |
| --- | --- | --- | --- |
| Project Plan | Delivered  | 01.12.2023 | 01.12.2023 |
| Summary of the project status| Delivered | 01.12.2023 | 01.12.2023 |
| Customer Feedback and Reflections| Delivered | 01.12.2023 | 01.12.2023 |
| Weekly Reports | Delivered | 01.12.2023 | 01.12.2023 |
| A Pre-Release Version of Software | Delivered | 27.11.2023 | 27.11.2023 |
| Changes has made since Milestone 1 or planned for the future to improve the development | Delivered | 01.12.2023 | 01.12.2023 |
| Progress According to Requirements |  Delivered | 01.12.2023 | 01.12.2023 |
| API Endpoints | Delivered | 01.12.2023 | 01.12.2023 |
| Generated Unit Test Reports | Delivered | 01.12.2023 | 01.12.2023 |
| General Test Plan for the Project | Delivered  | 01.12.2023 | 01.12.2023 |
| The status of the features in your software making use of the annotation technology | Delivered  | 01.12.2023 | 01.12.2023 |
| Your plans for implementing functionalities associated with annotations | Delivered  | 01.12.2023 | 01.12.2023 |
| Individual Contributions | Delivered | 01.12.2023 | 01.12.2023 |
| Milestone Review | Delivered | 01.12.2023 | 01.12.2023 |





## 5. Progress According to Requirements

Abbreviations:
+ NS: Not Started
+ IP: In Progress
+ C: Completed
+ IR: Irrelevant, meaning that this issue has no relevance with that part of the project (back-end, front-end, or mobile)


| Requirement No | Requirement Name | Back-End | Front-End | Mobile | Explanation |
| --- | --- | --- | --- | --- | --- |
|| Functional Requirements ||||
| 1.1 | User Requirements ||||
| 1.1.1 | Authentication ||||
| 1.1.1.1 | Sign up |C|C|C||
| 1.1.1.2 | Login |C|C|C||
| 1.1.1.3 | Sign Out | C | C | C ||
| 1.1.1.4 | Change Password | C | IP | IP ||
| 1.1.1.5 | Forgot Password | C | C | IP ||
| 1.1.1.6 | Delete Account | C | NS | IP ||
| 1.1.2 | Profile ||||
| 1.1.2.1 | Profile Page | C|C|C||
| 1.1.2.2 | Edit Photo, Name, Username | C | C | NS | Can edit photo and username |
| 1.1.2.3 | List the Game They Played | NS|NS|NS||
| 1.1.2.4 | Put a Link of Their Game Store Accounts | C | C| NS||
| 1.1.2.5 | Last Activities | C|C| IP||
| 1.1.2.6 | Achievements | C|C|IP||
| 1.1.2.7 | Gain Achievements | C | IR | IR ||
| 1.1.3 | Group |||||
| 1.1.3.1 | Group Creation | C | C| IP||
| 1.1.3.2 | Group Finding | C | C | IP ||
| 1.1.3.3 | Group Membership | C | C | IP | In mobile, list of groups can be viewed, but filtering functionality is not functional |
| 1.1.3.4 | Group Moderation | C| C| IP| Moderator role is granted to the creator of group and moderators can ban users |
| 1.1.4 | Forum | ||||
| 1.1.4.1 | Forum Posting | C | C | IP | New posts can be create with necessary fields, posts can be voted in front-end and mobile , posts can be deleted and updated in front-end and for all actions back-end keep database updated accordingly |
| 1.1.4.2 | Forum Commenting | C | C| IP | Comments can be created and edited and deleted in front-end, back-end |
| 1.1.4.3 | Forum Search | C |C| IP | Posts can be filtered based on name, tags, and creation date in front-end using implemented endpoint in back-end |
| 1.1.4.4 | Forum Reporting | NS| NS| NS| |
| 1.1.4.5 | Forum Subscription | NS| NS| NS||
| 1.1.4.6 | Forum Moderation | IP | IP | NS | (For back-end and front-end) posts and comments can be deleted, and uses can be banned |
| 1.1.5 | Game Review ||||
| 1.1.5.1 | Reviews | C | C| IP| (For back-end and front-end) reviews can be created, edited, deleted and voted |
| 1.1.5.2 | Search | C | C | NS | |
| 1.1.5.3 | Sort | C | C | NS ||
| 1.1.6 | Promotion ||||
| 1.1.6.1 | Forum Promotion | NS | NS | NS ||
| 1.1.6.2 | Game Promotion | NS | NS | NS ||
| 1.1.7 | Games ||||
| 1.1.7.1 | Filtering Games by Tag | C | C | IP ||
| 1.1.7.2 | Searching Game by Name | C | C | IP ||
| 1.1.7.3 | Reporting Games | NS | IP | NS ||
| 1.1.7.4 | Achieve Game Specific Info | NS | NS | NS | |
| 1.1.7.5 | See Game-Related Achievements | C | C | IP | |
| 1.1.8 | Admins ||||
| 1.1.8.1 | Forum | C | C| IP ||
| 1.1.8.2 | Game Review | C | C | NS | (For front-end and back-end) Admins can delete reviews |
| 1.1.8.3 | Games | C | C | IP | Admins can create games and add tags to them |
| 1.1.8.4 | Tags | C | C | IP | |
| 1.2 | System Requirements  ||||
| 1.2.1 | Annotations | NS | NS | NS ||
| 1.2.2 | Group | C | C | IP | System keeps track of user's actions and makes sure that each group has a moderator |
| 1.2.3 | Recommendations | NS | NS | NS ||
|| Non-Functional Requirements ||||
| 2.1 | Privacy | IR | C | IP | (For mobile) platform follows the necessary rules and considers ethical concerns |
| 2.2 | Security | C | IR | IR ||
| 2.3 | Performance & Reliability | IP | IR | IR | Platform has a response time less than 2 seconds and its uptime is above the provided limit |
| 2.4 | Portability | C | C | C | |
| 2.5 | Accessibility | C | C | C ||
| 2.6 | Standards | NS | NS | NS ||


## 6. API Endpoints

For a comprehensive guide and detailed information on the API, visit the following link to the API documentation:
- **API Documentation Link:** [GitHub Wiki - API Documentation](https://github.com/bounswe/bounswe2023group5/wiki/API-Documentation)

To interact with the API through a user-friendly interface and to explore the available endpoints, use the Swagger UI:
- **Swagger Link:** [Swagger UI](http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/swagger-ui/index.html#/)

For direct access to the API, use the following link:
- **API Link:** [API Access](http://ec2-16-16-166-22.eu-north-1.compute.amazonaws.com/)

## 7. Generated Unit Test Reports

<blockquote>
<details><summary><h4>Forum Service</h4></summary>

<details><summary><h4> 1. banUser Method Tests:</h4></summary>

**Test Case 1: banUser_SuccessfullyBanned**
<blockquote>
<ul>
<li>Functionality Tested: Banning a user in a forum successfully.</li>
<li>Tested Cases:
  <ul>
    <li>Forum exists, user exists in the forum, and banning is successful.</li>
  </ul>
</li>
<li>Expected Outcome: The user should be successfully banned in the specified forum.</li>
</ul>
</blockquote>

**Test Case 2: banUser_ForumNotFound**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when the specified forum doesn't exist.</li>
<li>Tested Cases:
  <ul>
    <li>Forum does not exist.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>
<details>
  <summary><h4> 2. unbanUser Method Tests:</h4></summary>

**Test Case 3: unbanUser_SuccessfullyUnbanned**
<blockquote>
  <ul>
    <li>Functionality Tested: Unbanning a user in a forum.</li>
    <li>Tested Cases:
      <ul>
        <li>Forum exists, user is banned in the forum, and unbanning is successful.</li>
      </ul>
    </li>
    <li>Expected Outcome: The user should be successfully unbanned in the specified forum.</li>
  </ul>
</blockquote>

**Test Case 4: unbanUser_ForumNotFound**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the specified forum doesn't exist.</li>
    <li>Tested Cases:
      <ul>
        <li>Forum does not exist.</li>
      </ul>
    </li>
    <li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
  </ul>
</blockquote>
</details>
</details>

<details><summary><h4>Group Service</h4></summary>

<details><summary><h4> 1. createGroup Method Tests:</h4></summary>

**Test Case 1: createGroup_Success**
<blockquote>
<ul>
<li>Functionality Tested: Creating a group.</li>
<li>Tested Cases:
  <ul>
    <li>Group title is unique.</li>
    <li>Tags exist and are associated with the group.</li>
    <li>Game exists and is associated with the group.</li>
  </ul>
</li>
<li>Expected Outcome: The group should be successfully created with the specified details.</li>
</ul>
</blockquote>

**Test Case 2: createGroup_GroupWithTitleExists_ThrowsBadRequestException**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when the group title already exists.</li>
<li>Tested Cases:
  <ul>
    <li>Group with the same title already exists.</li>
  </ul>
</li>
<li>Expected Outcome: BadRequestException should be thrown.</li>
</ul>
</blockquote>

**Test Case 3: createGroup_TagNotFound_ThrowsResourceNotFoundException**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when a specified tag is not found.</li>
<li>Tested Cases:
  <ul>
    <li>Tag specified in the request is not found.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>

<details>
  <summary><h4> 2. getAllGroups Method Tests:</h4></summary>

**Test Case 4: testGetAllGroups**
<blockquote>
  <ul>
    <li>Functionality Tested: Retrieving all groups based on filter criteria.</li>
    <li>Tested Cases:
      <ul>
        <li>Filter by title, sort by creation date, descending order, excluding deleted groups.</li>
      </ul>
    </li>
    <li>Expected Outcome: List of groups should be retrieved based on the specified filter.</li>
  </ul>
</blockquote>
</details>

<details>
  <summary><h4> 3. getGroupById Method Tests:</h4></summary>

**Test Case 5: testGetGroupById**
<blockquote>
  <ul>
    <li>Functionality Tested: Retrieving detailed information about a group by its ID.</li>
    <li>Tested Cases:
      <ul>
        <li>Group ID exists, user is a member, and user has a profile.</li>
      </ul>
    </li>
    <li>Expected Outcome: Detailed information about the group should be retrieved.</li>
  </ul>
</blockquote>

**Test Case 6: testGetGroupByIdNotFound**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the specified group ID is not found.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID does not exist.</li>
      </ul>
    </li>
    <li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
  </ul>
</blockquote>
</details>

<details>
  <summary><h4> 4. deleteGroup Method Tests:</h4></summary>



**Test Case 7: testDeleteGroupByTitle**
<blockquote>
  <ul>
    <li>Functionality Tested: Deleting a group by its title.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified title exists.</li>
      </ul>
    </li>
    <li>Expected Outcome: The group should be successfully deleted.</li>
  </ul>
</blockquote>

**Test Case 8: testDeleteGroupNotFound**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the specified group is not found.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified title does not exist.</li>
      </ul>
    </li>
    <li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
  </ul>
</blockquote>
</details>

<details>
  <summary><h4> 5. updateGroup Method Tests:</h4></summary>

**Test Case 9: testUpdateGroup**
<blockquote>
  <ul>
    <li>Functionality Tested: Updating details of a group.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID exists.</li>
      </ul>
    </li>
    <li>Expected Outcome: The group details should be successfully updated.</li>
  </ul>
</blockquote>

**Test Case 10: testUpdateGroupNotFound**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the specified group is not found.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID does not exist.</li>
      </ul>
    </li>
    <li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
  </ul>
</blockquote>

**Test Case 11: testUpdateGroupInvalidQuota**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the new quota is invalid.</li>
    <li>Tested Cases:
      <ul>
        <li>Quota is less than the current number of members.</li>
      </ul>
    </li>
    <li>Expected Outcome: BadRequestException should be thrown.</li>
  </ul>
</blockquote>
</details>

<details>
  <summary><h4> 6. joinGroup Method Tests:</h4></summary>

**Test Case 12: testJoinGroup_Success**
<blockquote>
  <ul>
    <li>Functionality Tested: Joining a public group.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID is public.</li>
      </ul>
    </li>
    <li>Expected Outcome: User should successfully join the group.</li>
  </ul>
</blockquote>

**Test Case 13: testJoinPrivateGroup**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when attempting to join a private group.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID is private.</li>
      </ul>
    </li>
    <li>Expected Outcome: BadRequestException should be thrown.</li>
  </ul>
</blockquote>

**Test Case 14: testJoinFullGroup**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when attempting to join a full group.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID has reached its quota.</li>
      </ul>
    </li>
    <li>Expected Outcome: BadRequestException should be thrown.</li>
  </ul>
</blockquote>
</details>

<details>
  <summary><h4> 7. leaveGroup Method Tests:</h4></summary>

**Test Case 15: testLeaveGroup_Success**
<blockquote>
  <ul>
    <li>Functionality Tested: Leaving a group.</li>
    <li>Tested Cases:
      <ul>
        <li>User is a member of the group.</li>
      </ul>
    </li>
    <li>Expected Outcome: User should successfully leave the group.</li>
  </ul>
</blockquote>

**Test Case 16: testLeaveGroupNotFound**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the specified group ID is not found.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID does not exist.</li>
      </ul>
    </li>
    <li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
  </ul>
</blockquote>
</details>

<details>
  <summary><h4> 8. applyGroup Method Tests:</h4></summary>
<blockquote>

**Test Case 17: testApplyGroup**
<blockquote>
  <ul>
    <li>Functionality Tested: Applying to join a group.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID is private.</li>
      </ul>
    </li>
    <li>Expected Outcome: User should successfully apply to join the group.</li>
  </ul>
</blockquote>

**Test Case 18: testApplyGroupNotFound**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the specified group ID is not found.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID does not exist.</li>
      </ul>
    </li>
    <li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
  </ul>
</blockquote>

**Test Case 19: testApplyGroupPublicGroup**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when attempting to apply to join a public group.</li>
    <li>Tested Cases:
      <ul>
        <li>Group with the specified ID is public.</li>
      </ul>
    </li>
    <li>Expected Outcome: BadRequestException should be thrown.</li>
  </ul>
</blockquote>

**Test Case 20: testApplyGroupAlreadyMember**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the user is already a member of the group.</li>
    <li>Tested Cases:
      <ul>
        <li>User is already a member of the group.</li>
      </ul>
    </li>
    <li>Expected Outcome: BadRequestException should be thrown.</li>
  </ul>
</blockquote>

**Test Case 21: testApplyGroupBannedMember**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the user is banned from the group.</li>
    <li>Tested Cases:
      <ul>
        <li>User is banned from the group.</li>
      </ul>
    </li>
    <li>Expected Outcome: BadRequestException should be thrown.</li>
  </ul>
</blockquote>

**Test Case 22: testApplyGroupPendingRequest**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the user already has a pending application for the group.</li>
    <li>Tested Cases:
      <ul>
        <li>User already has a pending application for the group.</li>
      </ul>
    </li>
    <li>Expected Outcome: BadRequestException should be thrown.</li>
  </ul>
</blockquote>
</details>
</details>
</blockquote>

<blockquote>
<details><summary><h4>Comment Service</h4></summary>

<details><summary><h4> 1. createComment Method Tests:</h4></summary>

**Test Case 1: createComment_SuccessfulCreation**
<blockquote>
<ul>
<li>Functionality Tested: Creating a comment on a post successfully.</li>
<li>Tested Cases:
  <ul>
    <li>Post exists, profile exists, and comment creation is successful.</li>
  </ul>
</li>
<li>Expected Outcome: A new comment should be successfully created and saved.</li>
</ul>
</blockquote>

**Test Case 2: createComment_PostNotFound**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when the specified post doesn't exist.</li>
<li>Tested Cases:
  <ul>
    <li>Post does not exist.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 2. replyComment Method Tests:</h4></summary>

**Test Case 3: replyComment_SuccessfulReply**
<blockquote>
<ul>
<li>Functionality Tested: Replying to a comment on a post.</li>
<li>Tested Cases:
  <ul>
    <li>Parent comment exists, profile exists, and reply comment creation is successful.</li>
  </ul>
</li>
<li>Expected Outcome: A reply to the comment should be successfully created and saved.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 3. editComment Method Tests:</h4></summary>

**Test Case 4: editComment_SuccessfulEdit**
<blockquote>
<ul>
<li>Functionality Tested: Editing an existing comment.</li>
<li>Tested Cases:
  <ul>
    <li>Comment exists and edit operation is successful.</li>
  </ul>
</li>
<li>Expected Outcome: The comment should be successfully edited and updated.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 4. deleteComment Method Tests:</h4></summary>

**Test Case 5: deleteComment_SuccessfulDeletion**
<blockquote>
<ul>
<li>Functionality Tested: Deleting an existing comment.</li>
<li>Tested Cases:
  <ul>
    <li>Comment exists and deletion operation is successful.</li>
  </ul>
</li>
<li>Expected Outcome: The comment should be successfully marked as deleted.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 5. getUserCommentList Method Tests:</h4></summary>

**Test Case 6: getUserCommentList_SuccessfulRetrieval**
<blockquote>
<ul>
<li>Functionality Tested: Retrieving a list of comments made by a user.</li>
<li>Tested Cases:
  <ul>
    <li>User has made comments and retrieval operation is successful.</li>
  </ul>
</li>
<li>Expected Outcome: A list of comments made by the user should be successfully retrieved.</li>
</ul>
</blockquote>
</details>

</details>
</blockquote>

<blockquote>
<details><summary><h4>Profile Service</h4></summary>

<details><summary><h4> 1. Edit Profile Method Tests:</h4></summary>

**Test Case 1: editProfile_SuccessfulEdit**
<blockquote>
<ul>
<li>Functionality Tested: Editing a user's profile successfully.</li>
<li>Tested Cases:
  <ul>
    <li>Profile exists and edit operation is successful.</li>
  </ul>
</li>
<li>Expected Outcome: The profile should be successfully updated.</li>
</ul>
</blockquote>

**Test Case 2: editProfile_ProfileNotFound**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when the specified profile doesn't exist.</li>
<li>Tested Cases:
  <ul>
    <li>Profile does not exist.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 2. Add Game to Profile Method Tests:</h4></summary>

**Test Case 3: addGameToProfile_SuccessfulAddition**
<blockquote>
<ul>
<li>Functionality Tested: Adding a game to a user's profile.</li>
<li>Tested Cases:
  <ul>
    <li>Profile and game exist, and addition operation is successful.</li>
  </ul>
</li>
<li>Expected Outcome: The game should be successfully added to the profile.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 3. Remove Game from Profile Method Tests:</h4></summary>

**Test Case 4: removeGameFromProfile_SuccessfulRemoval**
<blockquote>
<ul>
<li>Functionality Tested: Removing a game from a user's profile.</li>
<li>Tested Cases:
  <ul>
    <li>Profile and game exist, and removal operation is successful.</li>
  </ul>
</li>
<li>Expected Outcome: The game should be successfully removed from the profile.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 4. Get Profile Method Tests:</h4></summary>

**Test Case 5: getProfile_PrivateProfileAccessDenied**
<blockquote>
<ul>
<li>Functionality Tested: Accessing a private profile by a non-owner and non-admin user.</li>
<li>Tested Cases:
  <ul>
    <li>Profile is private, and accessed by a non-owner and non-admin user.</li>
  </ul>
</li>
<li>Expected Outcome: BadRequestException should be thrown.</li>
</ul>
</blockquote>

**Test Case 6: getProfile_SuccessfulRetrieval**
<blockquote>
<ul>
<li>Functionality Tested: Successfully retrieving a public profile.</li>
<li>Tested Cases:
  <ul>
    <li>Profile is public and accessible.</li>
  </ul>
</li>
<li>Expected Outcome: The profile should be successfully retrieved.</li>
</ul>
</blockquote>
</details>

</details>


<details><summary><h4>Achievement Service</h4></summary>

<details><summary><h4> 1. createAchievement Method Tests:</h4></summary>

**Test Case 1: testCreateAchievement_GameAchievement_Success**
<blockquote>
<ul>
<li>Functionality Tested: Creating a game achievement successfully.</li>
<li>Tested Cases:
  <ul>
    <li>Game exists. No existing achievements with the same title.</li>
  </ul>
</li>
<li>Expected Outcome: The game achievement should be successfully created.
</li>
</ul>
</blockquote>

**Test Case 2: testCreateAchievement_MetaAchievement_Success**
<blockquote>
<ul>
<li>Functionality Tested: Creating a meta achievement successfully.</li>
<li>Tested Cases:
  <ul>
    <li>No existing achievements with the same title.</li>
  </ul>
</li>
<li>Expected Outcome: The meta achievement should be successfully created.</li>
</ul>
</blockquote>
</details>
<details>
  <summary><h4> 2. updateAchievement Method Tests:</h4></summary>

**Test Case 3: testUpdateAchievement_Success**
<blockquote>
  <ul>
    <li>Functionality Tested: Updating an achievement successfully.</li>
    <li>Tested Cases:
      <ul>
        <li>Achievement exists, and is not deleted.</li>
      </ul>
    </li>
    <li>Expected Outcome: The achievement should be successfully updated with the new fields.</li>
  </ul>
</blockquote>
</details>

<details>
  <summary><h4> 3. deleteAchievement Method Tests:</h4></summary>

**Test Case 4: testDeleteAchievementById_Success**
<blockquote>
  <ul>
    <li>Functionality Tested: Deleting an achievement by ID successfully.</li>
    <li>Tested Cases:
      <ul>
        <li>Achievement exists, and is not deleted.</li>
      </ul>
    </li>
    <li>Expected Outcome: The achievement should be successfully marked as deleted.</li>
  </ul>
</blockquote>

**Test Case 5: testDeleteAchievement_NotFoundById**
<blockquote>
  <ul>
    <li>Functionality Tested: Handling the case when the achievement is not found by ID.</li>
    <li>Tested Cases:
      <ul>
        <li>Achievement does not exist or already deleted.</li>
      </ul>
    </li>
    <li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
  </ul>
</blockquote>

**Test Case 6: testDeleteAchievementByNameAndGame_Success**
<blockquote>
  <ul>
    <li>Functionality Tested: Deleting an achievement by name and game successfully.</li>
    <li>Tested Cases:
      <ul>
        <li>Achievement exists, game exists, and the achievement is linked to the given game.</li>
      </ul>
    </li>
    <li>Expected Outcome: The achievement should be successfully marked as deleted.</li>
  </ul>
</blockquote>

</details>

<details>
  <summary><h4> 4. grantAchievement Method Tests:</h4></summary>

**Test Case 7: testGrantAchievement_Success**
<blockquote>
  <ul>
    <li>Functionality Tested: Granting an achievement successfully.</li>
    <li>Tested Cases:
      <ul>
        <li>Achievement and user exist.</li>
      </ul>
    </li>
    <li>Expected Outcome: The achievement should be successfully granted to the user.</li>
  </ul>
</blockquote>
</details>

<details>
  <summary><h4> 5. getGameAchievements Method Tests:</h4></summary>

**Test Case 8: testGetGameAchievements_Success**
<blockquote>
  <ul>
    <li>Functionality Tested: Retrieving achievements for a game successfully.</li>
    <li>Tested Cases:
      <ul>
        <li>Game exists.</li>
      </ul>
    </li>
    <li>Expected Outcome: The list of achievements for the specified game should be retrieved successfully.</li>
  </ul>
</blockquote>
</details>

</details>
</blockquote>

<blockquote>
<details><summary><h4>Post Service</h4></summary>

<details><summary><h4> 1. GetPostList Method Tests:</h4></summary>

**Test Case 1: testGetPostList_Success**
<blockquote>
<ul>
<li>Functionality Tested: Retrieving a list of posts successfully.</li>
<li>Tested Cases:
  <ul>
    <li>Valid user email is provided, and a list of posts is returned.</li>
  </ul>
</li>
<li>Expected Outcome: A non-null list of GetPostListResponseDto should be returned.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 2. GetPostById Method Tests:</h4></summary>

**Test Case 2: testGetPostById_Success**
<blockquote>
<ul>
<li>Functionality Tested: Successfully retrieving a post by its ID.</li>
<li>Tested Cases:
  <ul>
    <li>Post exists and is retrieved successfully for the given ID and user email.</li>
  </ul>
</li>
<li>Expected Outcome: A non-null GetPostDetailResponseDto should be returned.</li>
</ul>
</blockquote>

**Test Case 3: testGetPostById_NotFound**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when the specified post ID doesn't exist.</li>
<li>Tested Cases:
  <ul>
    <li>Post ID does not exist.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 3. CreatePost Method Tests:</h4></summary>

**Test Case 4: testCreatePost_Success**
<blockquote>
<ul>
<li>Functionality Tested: Successfully creating a new post.</li>
<li>Tested Cases:
  <ul>
    <li>Valid post creation request and user are provided.</li>
  </ul>
</li>
<li>Expected Outcome: A non-null Post object should be returned and saved.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 4. EditPost Method Tests:</h4></summary>

**Test Case 5: testEditPost_Success**
<blockquote>
<ul>
<li>Functionality Tested: Successfully editing an existing post.</li>
<li>Tested Cases:
  <ul>
    <li>Valid post ID, edit request, and user are provided.</li>
  </ul>
</li>
<li>Expected Outcome: The post should be updated with the new details and returned.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 5. DeletePost Method Tests:</h4></summary>

**Test Case 6: testDeletePost_Success**
<blockquote>
<ul>
<li>Functionality Tested: Successfully deleting an existing post.</li>
<li>Tested Cases:
  <ul>
    <li>Valid post ID and user are provided.</li>
  </ul>
</li>
<li>Expected Outcome: The post should be marked as deleted and returned.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 6. GetUserPostList Method Tests:</h4></summary>

**Test Case 7: testGetUserPostList_Success**
<blockquote>
<ul>
<li>Functionality Tested: Retrieving a list of posts created by a specific user.</li>
<li>Tested Cases:
  <ul>
    <li>Valid user is provided.</li>
  </ul>
</li>
<li>Expected Outcome: A non-null list of Post objects should be returned.</li>
</ul>
</blockquote>
</details>

</details>
</blockquote>

<blockquote>
<details><summary><h4>Vote Service</h4></summary>

<details><summary><h4> 1. getAllVotes Method Tests:</h4></summary>

**Test Case 1: getAllVotes_RetrieveAllVotes**
<blockquote>
<ul>
<li>Functionality Tested: Retrieving all votes based on a given filter.</li>
<li>Tested Cases:
  <ul>
    <li>Filter applied, and votes are retrieved successfully.</li>
  </ul>
</li>
<li>Expected Outcome: A list of votes should be returned, matching the applied filter.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 2. getVote Method Tests:</h4></summary>

**Test Case 2: getVote_VoteFound**
<blockquote>
<ul>
<li>Functionality Tested: Retrieving a specific vote by its ID.</li>
<li>Tested Cases:
  <ul>
    <li>Vote exists and is successfully retrieved.</li>
  </ul>
</li>
<li>Expected Outcome: The specified vote should be returned.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 3. addVote Method Tests:</h4></summary>

**Test Case 3: addVote_SuccessfullyAdded**
<blockquote>
<ul>
<li>Functionality Tested: Adding a new vote.</li>
<li>Tested Cases:
  <ul>
    <li>New vote is added successfully.</li>
  </ul>
</li>
<li>Expected Outcome: The new vote should be added and returned.</li>
</ul>
</blockquote>

**Test Case 4: addVote_ResourceNotFound**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when the resource for adding a vote doesn't exist.</li>
<li>Tested Cases:
  <ul>
    <li>Resource for vote addition does not exist.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>

<details><summary><h4> 4. deleteVote Method Tests:</h4></summary>

**Test Case 5: deleteVote_ResourceNotFound**
<blockquote>
<ul>
<li>Functionality Tested: Deleting a vote where the vote does not exist.</li>
<li>Tested Cases:
  <ul>
    <li>Attempt to delete a non-existing vote.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>

</details>
</blockquote>

<blockquote>
<details><summary><h4>Review Service</h4></summary>

<details><summary><h4>1. GetAllReviews Method Tests:</h4></summary>

**Test Case 1: GetAllReviews_ReturnsAllReviews**
<blockquote>
<ul>
<li>Functionality Tested: Retrieving all reviews based on provided filters.</li>
<li>Tested Cases:
  <ul>
    <li>Filters set for gameId, reviewedBy, withDeleted, sortBy, and sortDirection.</li>
    <li>Valid return of review list from the repository.</li>
  </ul>
</li>
<li>Expected Outcome: A non-null list of GetAllReviewsResponseDto is returned.</li>
</ul>
</blockquote>
</details>

<details><summary><h4>2. GetReview Method Tests:</h4></summary>

**Test Case 2: GetReview_ReturnsReview**
<blockquote>
<ul>
<li>Functionality Tested: Retrieving a single review by its ID.</li>
<li>Tested Cases:
  <ul>
    <li>Review with the specified ID exists.</li>
  </ul>
</li>
<li>Expected Outcome: A non-null GetAllReviewsResponseDto corresponding to the given ID is returned.</li>
</ul>
</blockquote>

**Test Case 3: GetReview_ReviewNotFound**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when the specified review ID does not exist.</li>
<li>Tested Cases:
  <ul>
    <li>Review with the specified ID does not exist.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>

<details><summary><h4>3. AddReview Method Tests:</h4></summary>

**Test Case 4: AddReview_SuccessfullyAdded**
<blockquote>
<ul>
<li>Functionality Tested: Successfully adding a new review.</li>
<li>Tested Cases:
  <ul>
    <li>Valid review data is provided.</li>
  </ul>
</li>
<li>Expected Outcome: The review should be successfully added and returned.</li>
</ul>
</blockquote>
</details>

<details><summary><h4>4. UpdateReview Method Tests:</h4></summary>

**Test Case 5: UpdateReview_SuccessfullyUpdated**
<blockquote>
<ul>
<li>Functionality Tested: Successfully updating an existing review.</li>
<li>Tested Cases:
  <ul>
    <li>Review exists and valid update data is provided.</li>
  </ul>
</li>
<li>Expected Outcome: Review should be successfully updated.</li>
</ul>
</blockquote>

**Test Case 6: UpdateReview_ReviewNotFound**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when the review to update is not found.</li>
<li>Tested Cases:
  <ul>
    <li>Review with the specified ID does not exist.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>

<details><summary><h4>5. DeleteReview Method Tests:</h4></summary>

**Test Case 7: DeleteReview_SuccessfullyDeleted**
<blockquote>
<ul>
<li>Functionality Tested: Successfully deleting a review.</li>
<li>Tested Cases:
  <ul>
    <li>Review exists for the given ID.</li>
  </ul>
</li>
<li>Expected Outcome: Review should be successfully deleted.</li>
</ul>
</blockquote>

**Test Case 8: DeleteReview_ReviewNotFound**
<blockquote>
<ul>
<li>Functionality Tested: Handling the case when the review to delete is not found.</li>
<li>Tested Cases:
  <ul>
    <li>Review with the specified ID does not exist.</li>
  </ul>
</li>
<li>Expected Outcome: ResourceNotFoundException should be thrown.</li>
</ul>
</blockquote>
</details>

</details>
</blockquote>



## 8. General Test Plan For The Project

### 8.1 Backend
The testing plan for the backend part of the project involves different tests to make sure functionality and reliability of the project. JUnit is used for checking individual parts, which means each piece of the project is tested by itself to ensure it performs the way it's supposed to. These tests cover a wide range of scenarios and ensure that each service method behaves as expected under the various inputs provided. Additionally, we use mock data to create pretend information, allowing us to simulate various situations and conditions in unit tests. This helps us see how our project handles different scenarios, ensuring it responds appropriately even before real data is involved. Using mock data also prevents changes to normal data for testing. Postman and Swagger are used to test how different parts of our project communicate with each other. It helps us make sure that data flows smoothly between components, and our API functions correctly. Also, our testing practices extend to database interactions. This involves the execution of tests to confirm that any changes made through the application are accurately reflected in the database, thus preserving data integrity. To further enhance our development process, we actively seek and incorporate feedback from the front-end and mobile teams. This iterative feedback loop allows us to adapt and refine our implementation of endpoints and service methods, ensuring that our backend code remains responsive to the evolving needs and expectations of our collaborative development environment.

### 8.2 Front-End

The front-end testing plan consists of two types of manual testing to ensure code functionality. Each of these testing phases plays an important role. Pre-Merge Testing focuses on individual features, while Pre-Presentation Testing ensures the collective functioning of all features in a realistic scenario, similar to a final quality assurance before showcasing the product.

#### 8.2.1 Pre-Merge Testing
  
When reviewing a pull request (PR), the reviewer downloads the code to their own computer to run it. The main purpose of this is to check if the PR fixes the problems or adds the new features it's supposed to. If the reviewer finds any bugs or problems, they stop the process of adding the code to the main branch. These issues need to be sorted out first before the code can be merged.

#### 8.2.2 Pre-Presentation Testing

This testing is planned one or two days before important milestone presentations. It includes a thorough check of all the features that will be shown during the presentation. If bugs are found, they are treated as urgent. The developer who knows the most about the affected feature is quickly told about the problem. The goal is to fix these issues quickly so that the presentation goes smoothly.

### 8.3 Mobile

The mobile testing plan consists of manual testing. As one of the parts of the project (the other being front-end), that the user directly interacts, we need to simulate the user behaviour as close as possible, and best way to do it is manually, we deemed. The testing process can be divided to 4 phases:

#### 8.3.1 Pre-Merge Prefab Testing

After the coder of the functionality finished its coding. It first test the Http functions of the related C# class. After thorough testing, it is ensured that these functions indeed make requests as they supposed to do and they handle edge cases and unsuccessful responses. At this stage aldo the view of the respective page is checked and edited if necessary to suit to the decisions user interface and user experience.

#### 8.3.2 Post-Merge Local Testing 

After the prefab and its script is tested in isolation, it is tested together with other prefabs and their scripts in the local branch. Some of the functionalities is closely interwoven with other functionalities, thus some errors that are undetectable in the first phase, can be detected here. During this phase, the tester tries to mimic the user behaviour as close as possible, also keeps in mind and tests the edge cases, not only happy paths.

#### 8.3.3 Reviewer Testing

The reviewer of the task merges the changes and the current version of the remote main to her local environment and repeats the above steps. At this stage she tries various other scenarios that may not have come into the mind of the developer. Hence the common proverb: two eyes are better judge than a single eye.

After the reviewer has tested the newly developed code, it is merged to the main and pushed to the remote.

#### 8.3.4 Pre-Presentation Testing

In the days leading to the milestone, the functionalities of the mobile part of the project is tested repeatedly and excessively. This is made to ensure that previously working parts are still working and a beautiful user experience is provided. As a last part of this testing phase, builds of the project is created, and the mobile application is tested on Android phones. 





## 9. Status Of The Features Making Use Of The Annotation Technology

As of now, unfortunately we don’t have any feature that is making use of the annotations. Despite assigning research tasks on annotations to a couple of teammates for last two weeks, we couldn’t find time to delve deeper on the subject. This was mainly due to our focus on developing the core functionalities of the platform.

## 10. Plans For Implementing Annotation-Related Functionalities

Regarding our plans for implementing functionalities associated with annotations, our first step involves dedicating time to understand annotations. We’re going to achieve this by studying the Web Annotation Data Model and reviewing relevant examples. Once we are comfortable with the annotations and we can agree upon a common approach we’re going to proceed with their implementation and integration into both our games and posts. Later we can utilize these annotations to strengthen links between different games, calculating similarities.

## 11. Individual Contributions

- [Ali Başaran](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Ali-Başaran-‐-451-Milestone-2)

- [Alperen Bırçak](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Alperen-B%C4%B1r%C3%A7ak-451)

- [Arda Kabadayı](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Arda-Kabaday%C4%B1-%E2%80%90-451-Milestone-2)

- [Bilal Atım](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Bilal-At%C4%B1m-%E2%80%90-451-Milestone-2)

- [Can Uzduran](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Can-Uzduran-%E2%80%90-451-Milestone-2)

- [Çisel Zümbül](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-%C3%87isel-Z%C3%BCmb%C3%BCl-%E2%80%90-451-Milestone-2)

- [Deniz Ünal](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Deniz-%C3%9Cnal-%E2%80%90-451)

- [Ege Ekşi](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Ege-Ek%C5%9Fi-451)

- [Halis Bal](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Halis-Bal-451)

- [Harun Sami Çelik](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Harun-Sami-%C3%87elik-%E2%80%90-451-Milestone-2)

- [Mehmet Said Yolcu](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Mehmet-Said-Yolcu-%E2%80%90-451-Milestone-2)

- [Zeynep Baydemir](https://github.com/bounswe/bounswe2023group5/wiki/Zeynep-Baydemir-Individual-Contributions-%E2%80%90-451-Milestone-2)

## 12. Pre-Release
[Here](https://github.com/bounswe/bounswe2023group5/releases/tag/customer-milestone-2) is the Pre-Release of the application

