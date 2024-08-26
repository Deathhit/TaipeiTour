# TaipeiTour

A sample project to demonstrate the practices of the Android app architecture illustrated below.

To see the related medium article, checkout the [`Android Application Architecture Showcase : Sunflower Clone`](https://medium.com/@b9915034/android-application-architecture-showcase-sunflower-clone-dee729f6e1f2).

## Screenshots

<p float="left">
<img src="docs/screenshots/Screenshot_1.png" width = 360> &nbsp;
<img src="docs/screenshots/Screenshot_2.png" width = 360> &nbsp;
<img src="docs/screenshots/Screenshot_3.png" width = 360> &nbsp;
<img src="docs/screenshots/Screenshot_4.png" width = 360> &nbsp;
<img src="docs/screenshots/Screenshot_5.png" width = 360> &nbsp;
<img src="docs/screenshots/Screenshot_6.png" width = 360> &nbsp;
</p>

## Architecture Overview

<img src="docs/diagrams/Android App Architecture Overview.png"/>
<img src="docs/diagrams/Taipei Tour Dependency Graph.png"/>

## Features

* Dark Mode
* Database Schema with CQRS Pattern
* CollapsingToolbarLayout
* Coroutine
* Kotlin
* Image Viewer with zoom in and zoom out capabilities
* Material 3
* MVVM
* Language Settings
* Supports offline functionality
* Paging 3
* Single State View Model
* Unidirectional Flow
* Unit tests implemented in the Data Layer and Core Layer
* ViewPager2

## Agile Development and Architecture

### 1. Rapid Iteration and Continuous Improvement
Starting with the Domain Layer: In Agile development, quickly establishing and validating core business logic is crucial. By designing the Domain Layer first, the team can establish the foundational functions and business logic of the application in the early stages. This not only ensures the stability of core functions but also enables us to rapidly conduct incremental development in subsequent iterations, ensuring steady progress in each iteration.

### 2. Flexible Collaboration and Division of Labor
Modular Architecture Promotes Team Collaboration: The modular design of the architecture allows the team to divide work flexibly. For example, once the Domain Layer and Core Layer are established, different teams can simultaneously develop the Feature Layer and Data Layer. This flexible division of labor not only increases development speed but also allows the team to adjust the development order of each module according to priority, better responding to changes in requirements and maintaining agility.

### 3. Incremental Development and Deliverable Outcomes
Gradual Integration of Modules: In Agile development, we expect deliverable outcomes at the end of each iteration. Due to the layered design of the architecture, different modules (such as the Feature Layer and Data Layer) can be developed and tested independently and gradually integrated into the system. This way, the deliverables of each iteration are fully functional and quality-assured, allowing the product to be ready for use at any time, meeting market demands.

### 4. Efficient Feedback and Adjustment
Quick Response to Changes: In Agile development, we emphasize quickly responding to changes in requirements. Due to the modular and layered design of the architecture, we can easily adjust or replace certain modules without affecting the overall stability of the system. Specifically, the Domain Layer provides contracts for business logic, with the actual implementations residing in the Data Layer. Therefore, we can independently modify the Data Layer without impacting other parts of the application.

### 5. Continuous Delivery and Stability
Stable System Foundation: The layered design of the architecture ensures the stability of the foundational layers (such as the Domain and Core Layers), which lays a solid foundation for continuous delivery. When the team engages in continuous delivery within the Agile process, this stability significantly reduces risks, ensuring the quality of each delivery, allowing us to steadily progress and achieve high-quality product delivery standards.

## Api Reference

https://www.travel.taipei/open-api/swagger/ui/index#/Attractions/Attractions_All
