# Gallery Microservice Architecture

## Project Overview
Done throughout my second year of College in Computer Science, the Gallery Microservice Architecture project's purpose was to develop a 3-tier web application for a gallery to be able to view, manage, and modify artworks. 

## Table of Contents
- [Project Description](#project-description)
- [Installation](#installation)
- [Set Up](#set-up)
- [Usage](#usage)
- [Features](#features)
- [Future Improvements Ideas](#improvements)
- [Acknowledgments](#acknowledgments)

<a name="project-description"></a>
## Project Description 
The project is composed of 5 microservices: api-gateway service, exhibition service, and 3 low-level microservices of exhibition
(painting service, sculpture service, gallery service). Each microservice has its own database. 

### Domain-Driven Desing Model
![gallery_ddd_model](https://github.com/Mila5847/GalleryMicroserviceArchitecture/assets/46633364/97e183a8-46fb-458e-9168-1521f7bba117)

### Container Diagram
![image](https://github.com/Mila5847/GalleryMicroserviceArchitecture/assets/46633364/940c5a79-bacf-4965-a864-afbbe844bd44)

### Context Diagram
![image](https://github.com/Mila5847/GalleryMicroserviceArchitecture/assets/46633364/ce21727b-4916-4ede-9959-fe4c8d465459)

<a name="installation"></a>
## Installation 
1. Clone the repository
2. Navigate to the project directory
3. Install the dependencies, Docker Desktop, and Postman

<a name="set-up"></a>
### Set Up

#### UI
- Uses Postman as the presentation layer of the 3-layer pattern.

#### Platform
- Uses Docker platform for automated deployment, scaling, and management of applications within isolated environments (containers).
- Uses webflux: make the application asynchronous, non-blocking.

#### Framework & Dependencies
Spring Framework
- Testing was done with mock objects and WebTestClient dependency (integration tests, persistence tests, and unit tests)
- Uses Gradle, an open-source build automation tool and dependency management system.

#### Libraries
- Uses Lombok.
- Uses MapStruct for mapping beans.

#### Database
- Uses SQL database for microservices (low-level microservices).
- Uses MongoDB as the database for the aggregator service.

<a name="usage"></a>
## Usage 
1. Run the project with Docker using the commands:
   - `docker-compose build`
   - `docker-compose up`
   - `docker-compose down` (stop and remove containers)

2. In Postman, execute a request such as a POST exhibition (multiple paintings and sculptures from the same gallery can be placed in an exhibition):

```json
{
  "exhibitionName": "newhibition",
  "roomNumber": 213,
  "duration": 120,
  "startDay": "Monday",
  "endDay": "Wednesday",
  "paintings": [
    {
      "paintingId": "6ae9eaf7-5ec4-45da-a85d-45a317e7112",
      "title": "The Two Fridas",
      "yearCreated": 1939,
      "painterId": "0e1482bb-67a8-4620-842b-3f7bfb7ee175",
      "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8"
    },
    {
      "paintingId": "d2d6b05f-9cfb-4a54-ba3e-57dffe0fd56",
      "title": "Mona Lisa",
      "yearCreated": 1506,
      "painterId": "ede04dc9-9cf9-4191-8b4e-7d91234cb49c",
      "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8"
    }
  ],
  "sculptures": [
    {
      "sculptureId": "2872b8a2-e691-4115-891c-bed7187392d",
      "title": "Hand",
      "material": "Clay",
      "texture": "Bumpy",
      "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8"
    },
    {
      "sculptureId": "29e03f5-c8aa-475c-832e-8edbb2336778",
      "title": "Torso",
      "material": "Clay",
      "texture": "Rough",
      "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8"
    }
  ]
}
```
3. See updqate in MongoDB on localhost 8081
![mongo](https://github.com/Mila5847/GalleryMicroserviceArchitecture/assets/46633364/0006dc81-7db3-45f4-b76e-b1ed3c7facc5)

<a name="features"></a>
## Features 
The 3-layer web app allows to do the following 
- View, create, update, delete a gallery.
- View, create, update, delete a painting inside a gallery
- View, create, update, delete a sculpture inside a gallery
- View, create, update, delete an exhibition of paintings and sculptures of a specific gallery
- Exceptions
  - Gallery with existing address cannot be added again
  - Painting without a title cannot be added again
  - Sculpture with existing name cannot be added again
- View, create, update, delete an exhibition composed of paintings and sculptures from a gallery. 
- Update of exhibition can consist of upating the exhibition's information or add/remove paintings and sculptures.

<a name="improvements"></a>
## Future Improvements Ideas
- Currently, the aggregator is supposed to be the exhibition service. However, the exhibition service does not actually get anything
from the low-level microservices, so I would like to make it a real aggregator which gets paintings, and sculptures in a gallery, for example.
- I would like to do more testing, especially testing my exceptions.
- I would like to expand it with more microservices. For example, adding a tickets microservice to handle the visitors' visits. 

<a name="acknowledgments"></a>
## Acknowledgments 
I would like to thank my brother, Petar Kehayov, for spending countless hours with me, thinking of the best way to develop this project.
I would like to also thank my teacher, Christine Gerard, for fixing my impossible bugs, especially with Docker :).
