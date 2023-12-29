# CMPE 451 Final Project Team Report

### Prepared By:

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

### Video :

## List Of Contents

- [1. Executive Summary](#1-executive-summary)
- [2. Final Release Notes](#2-final-release-notes)
- [3. Status Of Deliverables](#3-status-of-deliverables)
- [4. Requirements Coverage](#4-requirements-coverage)
- [5. API Endpoints](#5-api-endpoints)
- [6. User Interface & User Experience](#6-user-interface---user-experience)
- [7. Annotations](#7-annotations)
- [8. Scenario](#8-scenario)
- [9. Management](#9-management)

- [10. Individual Contribution Reports](#10-individual-contribution-reports)

### 1. Executive Summary

### 2. Final Release Notes

### 3. Status Of Deliverables

| Deliverable Name                                        | Delivery Status | Notes |
| ------------------------------------------------------- | --------------- | ----- |
| Project Video                                           | Completed       |
| Executive Summary                                       | Completed       |
| Summary of Your Project Status in Terms of Requirements | Completed       |
| Status of Deliverables                                  | Completed       |
| Final Release Notes                                     | Completed       |
| Management Related Parts of Report                      | Completed       |
| Summary of Work Performed by Team Members               | Completed       |
| API Endpoints                                           | Completed       |
| Status of Requirements                                  | Completed       |
| Status of Annotations                                   | Completed       |
| Scenarios                                               | Completed       |
| User Manual                                             | Completed       |
| System Manual                                           | Completed       |
| Emulator Instructions                                   | Completed       |
| Software Requirements Specification                     | Completed       |
| Software design documents                               | Completed       |
| User scenarios and mockups                              | Completed       |
| Research                                                | Completed       |
| Project plan                                            | Completed       |
| Generated Unit Test Reports                             | Completed       |
| Release of Software                                     | Completed       |
| Individual Contributions                                | Completed       |

### 4. Requirements Coverage

### 5. API Endpoints

### 6. User Interface & User Experience

### 7. Annotations

- Status: We have implemented both text and image annotations in our backend and fronted. However we weren't able to implement them on time on mobile.

- Compliance with W3C WADM: While creating the data models in our annotation backend we took the W3C model as our primary reference point and used a complient library for our frontend. We didn't fully cover all of the model but the functionalities that we have included are complient with the W3C model.

- Implementation:

  In the backend we have created an annotation model with the following properties from the W3C model: id, target, body, type, motivation, created and creator. However we don't use the last two in our frontend.

  For the body model we have only implemented the textual body with the folllowing properties: type, id, purpose, value, format and language. However we don't use the last two for the body as well in our frontend.

  We have created the target model with the following properties: id, source, selector, type, format, textDirection. This time the last three is not used in our frontend.

  For selectors, we have implemented three: TextPositionSelector, TextQuoteSelector and Fragment Selector. The first two is used together for textual annotation, while the second one is used for image annotations in our application. All three selectors are fully implemented and are not lacking any properties.

  The purpose for the fact that we added additional fields that we did not use in front end is that the usability of the annotation backend for other frontends. Thus we have implemented some additional fields that we thought that can be important for other use cases that isn't in our application.

  For creating new annotations we have added a single endpoint POST /api/annotation/create . It can be used for both creating image and textual annotations.

  The same is valid for updating and deleting annotations. They can be done by PUT /api/annotation/update and DELETE /api/annotation/delete regardless of their types. However we have separated the get endpoints for different kind of annotations.

  In order to get the textual annotations for a source, we have implemented the GET /api/annotation/get-source-annotations and for the image annotations, we have implemented /api/annotation/get-image-annotations

- API Calls to the annotation server:

  - Textual annotation creation & retrieval:

    To create new textual annotation first POST /api/annotation/create endpoint should be used with its request body in the following format:

    ```json
    {
      "@context": "http://www.w3.org/ns/anno.jsonld",
      "type": "Annotation",
      "body": [
        {
          "type": "TextualBody",
          "value": "selam",
          "purpose": "commenting"
        }
      ],
      "target": {
        "source": "http://localhost:5173/forum/detail/1583937f-687a-4122-af3b-ca1185cb0c4c/7671c222-3ebb-44a7-99a8-67b832095859",
        "selector": [
          {
            "type": "TextQuoteSelector",
            "exact": "dragonborn"
          },
          {
            "type": "TextPositionSelector",
            "start": 26,
            "end": 36
          }
        ]
      },
      "id": "http://localhost:5173/forum/detail/1583937f-687a-4122-af3b-ca1185cb0c4c/7671c222-3ebb-44a7-99a8-67b832095859/#6e90b961-5541-428a-a030-287636cc94ea"
    }
    ```

    In order to retrieve the textual annotations we can search for them using source id in the GET /api/annotation/get-source-annotations endpoint with the parameter source as in the example below:

    http://localhost:3002/api/annotation/get-source-annotations?source=http://localhost:5173/forum/detail/1583937f-687a-4122-af3b-ca1185cb0c4c/7671c222-3ebb-44a7-99a8-67b832095859

  - Image annotation creation & retrieval:

    To create new image annotation, same POST /api/annotation/create endpoint should be used with its request body in the following format:

    ```json
    {
      "@context": "http://www.w3.org/ns/anno.jsonld",
      "type": "Annotation",
      "body": [
        {
          "type": "TextualBody",
          "value": "selam",
          "purpose": "commenting"
        }
      ],
      "target": {
        "source": "http://localhost:5173/forum/detail/1583937f-687a-4122-af3b-ca1185cb0c4c/7671c222-3ebb-44a7-99a8-67b832095859",
        "selector": {
          "type": "FragmentSelector",
          "conformsTo": "http://www.w3.org/TR/media-frags/",
          "value": "xywh=pixel:99.46521759033203,147.68984985351562,94.94408416748047,84.39472961425781"
        }
      },
      "id": "http://localhost:5173/forum/detail/1583937f-687a-4122-af3b-ca1185cb0c4c/7671c222-3ebb-44a7-99a8-67b832095859/#6e90b961-5541-428a-a030-287636cc94ea"
    }
    ```

    In order to retrieve the image annotations we can search for them using source id in the GET /api/annotation/get-image-annotations endpoint with the parameter source as in the example below:

    http://localhost:3002/api/annotation/get-image-annotations?source=http://localhost:5173/forum/detail/1583937f-687a-4122-af3b-ca1185cb0c4c/7671c222-3ebb-44a7-99a8-67b832095859

    Note that even though both retrieval endpoints share the same source, they will only return the related annotations (image annotations for get-image-annotations and textual annotations for get-source annotations)

    Also note that while creating annotations only difference is in the selector part and all other fields are same.

### 8. Scenario

#### Persona

<img align="left" src="https://user-images.githubusercontent.com/68641237/230743155-bb4e3f71-b1a8-47e6-9001-0aca8996332a.jpeg" width=300 height=150 alt="Reset Password">

- Name: Josh Radnor
- Age: 21
- Nationality: American
- Personality: Patient, Friendly, Strict
- Interests: Video Games, Basketball, Teni

#### User Story

Josh Radnor is a junior computer engineering student at Stanford University. He loves playing videogames and interacting with other gamers. In his free time, he plays basketball and tenis other thanplaying video games. He loves playing RPG games however he is very picky about them, he likes toresearch a lot before commiting a game. He is a real Fallout fan. He heard this platform from hisfriends and wanted to give it a go.

#### Goals

- Engaging with new gamers
- Being aware of new games
- Joining online communities, participate in forums or chat rooms, or play multiplayer games with friends or strangers.
- Having a detailed information about the games

#### Scenario

This scenario will also be demonstrated through the video we provide.

1. Josh checks out the home page and finds compelling content, including Fallout-related posts.
2. Spots the register button in the top navigation bar, Josh clicks it and gets redirected to the registration page.
3. On the registration page, he provides a username, email, and password. He accepts the Game Guru User Agreement and completes the registration.
4. After successful registration, Josh is redirected to the home page.
5. Using the top navigation bar, he goes to his profile and clicks the "edit profile" button.
6. Josh adds his picture and links his Steam account in modal form.
7. Heading to the games page, he searches for Fallout.
8. Among the search results, Josh chooses Fallout 4. He briefly checks the description, annotations, and characters from the game’s details page.
9. Impressed, he follows the game.
10. He navigates to the reviews section, gives 5-star rating, and writes a detailed review about why he likes Fallout 4.
11. After having a look to the other reviews, upvotes some reviews, downvotes some others based on his preferences.
12. He checks out the Fallout 4 forum, writes a comment on a post he likes, and creates a post about Fallout 4.
13. Following this, he goes back to home page for some other content
14. Seeing a recommended Fallout 4 group on the home page, Josh checks out the group.
15. He likes it and decides to join. He applies for the group.
16. He writes a detailed message explaining why he wants to join.
17. After a while, Josh click notification section on the side bar .
18. He finds out achievements related to reviews and posts. He also sees that his group application is accepted.
19. He goes to the group's page by clicking the related notification.
20. He posts a group post.
21. Going back to home page, Josh sees a recommendation for ‘Baldur's Gate 3.’
22. He goes and checks out the game with the characters, annotation, description.
23. He gets really exited, he follows the game with intentions of playing.
24. After playing ‘Baldur's Gate 3’ and enjoying it, he searches for existing groups from Groups page by game name ‘Baldur’s Gate 3’
25. Josh decides to create a new group of this game, he swithches to Groups page.
26. Clicking the "create group" button, he adds an image, description, and sets quota and membership policies for the new Baldur's Gate 3 group.
27. Creates a welcome post for the incoming members and starts to wait for first members of his group.
28. He remembers the achievement he saw in game Baldur's Gate 3. He just won that.
29. He posts an achievement request post in the game’s forum
30. He gets notification when his achievement is granted.
31. He is happy knowing that he found a new game he really liked and he is good at it, he leaves the website.

#### Works Done To Realize This Scenario

To realize the scenario above in the application, we focused on several key aspects. Initially, we implemented a secure login and registration functionality using token-based authentication, laying the foundation for user identity and security.

The application is built on two fundamental pillars: domain specificity and community engagement. For the community engagement aspect, we developed robust features like reviews, forums, comments, and groups.

Reviews and all kinds of forum content encompasses posting, editing, deleting, and voting capabilities, integrated across the backend, frontend, and mobile platforms. On the backend, CRUD (Create, Read, Update, Delete) endpoints were implemented to manage these interactions efficiently. Forums were designed to allow users to create individual posts, comment on these posts, and even comment on other comments, facilitating rich community interactions.

Regarding groups, we introduced two types of membership policies to represent different group types and motivations, enhancing the social dynamics within the application. This feature allows users to connect and engage with like-minded individuals, creating a sense of community and shared interest.
For domain specificity, a key focus was on enriching the gaming experience. For users like John who seek in-depth information about games, we enriched our platform with comprehensive game details incorporating features like characters, annotations, and game-specific tags. These elements differentiate our platform from typical gaming platforms by providing deeper insights into each game.

Additionally, we introduced an achievements system to boost user engagement, tapping into the competitive spirit of gamers. To seamlessly blend domain specificity with social networking, these domain-specific elements - characters, achievements, annotations - were integrated into the posts and social features of the application.

A significant addition was the implementation of a recommendation algorithm. By tracking user activities on the backend, such as games followed and group participation, the algorithm suggests games, and groups that align with the user's interests.

Furthermore, related pages for games, characters, and groups were developed, with backend support for managing and presenting this data effectively. To further enhance the user experience, we added search, filter, and sort functionalities with a wide range of options across nearly all sections of our user interfaces.

To ensure that users like John have access to a wealth of content, we developed an admin panel. This panel empowers our administrators to effectively manage the platform's content. Through this panel, admins have the capability to create, delete, and edit various elements such as games, achievements, and tags. Additionally, it provides them with the functionality to add annotations, enriching the game descriptions and providing deeper insights.

### 9. Management

- In this milestone we tried to write backend unit test while implementing them. This gave us the opportunity to detect bugs beforehand. By writing these tests we could also ensure that future codes would not impact the functionalities of some other sections.
- Secondly, in this milestone, we paid more attention to reviews. For example we changed the styling of the login/register page in frontend and make some coloring adjustments based on the given feedbacks. This created a better UI and gave us a opportunity to see our mistakes.
- Also, by experiencing first two milestone we learned how to create a realistic and detailed scenario. So, in this milestone I think we created a better scenario. In this way we affect our customers more easily and make them more satisfied.
- In the final milestone demo, we realized that the objective of the annotation feature is to provide all users with the opportunity to annotate content on the page. We had thought that there might be constraints to this opportunity, but the demo demonstrated that such limitations were unnecessary.

## 10. Individual Contribution Reports

- [Ali Başaran]()
- [Alperen Bırçak]()
- [Arda Kabadayı]()
- [Bilal Atım]()
- [Can Uzduran]()
- [Çisel Zümbül](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-%C3%87isel-Z%C3%BCmb%C3%BCl-%E2%80%90-451-Final-Milestone)
- [Deniz Ünal]()
- [Ege Ekşi]()
- [Halis Bal]()
- [Harun Sami Çelik]()
- [Mehmet Said Yolcu]()
- [Zeynep Baydemir](https://github.com/bounswe/bounswe2023group5/wiki/Contributions-of-Zeynep-Baydemir-%E2%80%90-451-%E2%80%90-Final-Milestone)
