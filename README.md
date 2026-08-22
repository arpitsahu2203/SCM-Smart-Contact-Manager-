# Smart Contact Manager

Smart Contact Manager (SCM) — an enterprise-grade Spring Boot application for securely storing, organizing and sharing contacts. Features include OAuth2 (Google/GitHub), Cloudinary-backed image uploads, paginated search, and a responsive Thymeleaf + Tailwind frontend.

## Screenshots

<div align="center">
  <img src="docs/screenshots/landing-page.png" alt="SCM landing page" width="900" />
  <p><em>Landing page</em></p>

  <img src="docs/screenshots/about-page.png" alt="SCM about page" width="900" />
  <p><em>About section</em></p>

  <img src="docs/screenshots/contacts-dashboard.png" alt="SCM contacts dashboard" width="900" />
  <p><em>Contact dashboard</em></p>

  <img src="docs/screenshots/features-section.png" alt="SCM feature highlights" width="900" />
  <p><em>Feature highlights</em></p>
</div>

## Features

- User registration and login
- Google and GitHub OAuth2 login
- Add, view, update, and delete contacts
- Paginated contact list
- Contact image upload
- Responsive UI with Thymeleaf, Tailwind CSS, and Flowbite

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Cloudinary
- Tailwind CSS

## Project Structure

- `src/main/java` - controllers, services, entities, repositories, and config
- `src/main/resources/templates` - Thymeleaf pages
- `src/main/resources/static` - static assets and generated CSS

## Prerequisites

- Java 21+
- Maven
- MySQL
- Node.js

## Configuration

Update `src/main/resources/application.properties` with your local values for:

- MySQL connection
- Google OAuth credentials
- GitHub OAuth credentials
- Cloudinary credentials

## Setup

```bash
git clone https://github.com/arpitsahu2203/SCM-Smart-Contact-Manager-.git
cd SCM-Smart-Contact-Manager-
npm install
npm run tw:build
```

## Run

```bash
mvn spring-boot:run
```

## Build

```bash
mvn clean package
```

## ScreenShots

![home page](C:\Users\USER\OneDrive\Pictures\Screenshots 1\Screenshot 2026-08-20 163456.png)   

## License

No license has been specified.
