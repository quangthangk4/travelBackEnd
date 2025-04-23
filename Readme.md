# Tên Dự Án Backend Của Bạn Flight Booking

* Demo: https://thang-flightbooking.netlify.app/
* frontEnd:  https://github.com/quangthangk4/travelBackEnd

<!-- [Optional] Thêm một câu mô tả ngắn gọn ở đây -->
Một API backend mạnh mẽ cho ứng dụng đặt vé máy bay, được xây dựng bằng Spring Boot.

## account admin dùng để test ứng dụng (vì vé được search theo ngày, nên bạn phải tạo chuyến bay trước vì dữ liệu chưa có sẵn :>)
- username: admin@gmail.com
- password: admin

## account user dùng để test ứng dụng (hoặc có thể tự tạo)
- username: thang@gmail.com
- password: 123456

<!-- [Optional] Thêm các значки (badges) ở đây nếu có: build status, coverage, license... -->
<!-- Ví dụ: [![Build Status](URL_BUILD_STATUS)](LINK_BUILD_JOB) -->
<!-- [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) -->
## Mục Lục (Table of Contents)

- [Giới thiệu](#giới-thiệu)
- [Tính năng chính](#tính-năng-chính)
- [Công nghệ sử dụng (Tech Stack)](#công-nghệ-sử-dụng-tech-stack)
- [Yêu cầu cài đặt (Prerequisites)](#yêu-cầu-cài-đặt-prerequisites)
- [Hướng dẫn cài đặt và Chạy](#hướng-dẫn-cài-đặt-và-chạy)
- [Triển khai (Deployment)](#triển-khai-deployment)
- [Liên hệ](#liên-hệ)

## Giới thiệu

<!-- Mô tả chi tiết hơn về mục đích của dự án backend này. Nó giải quyết vấn đề gì? Cung cấp những dịch vụ cốt lõi nào cho frontend hoặc các hệ thống khác? -->
Đây là phần backend cho ứng dụng Flight Booking, cung cấp các API RESTful để quản lý: người dùng, xác thực, đặt vé, quản lý chuyến bay, thanh toán,...].

## Tính năng chính

- 🔐 Xác thực và phân quyền người dùng (JWT).
- 🧑‍💻 Quản lý người dùng (CRUD).
- ✈️ Quản lý hãng bay và máy bay.
- ✈️ Quản lý chuyến bay.
- 🎫 Quản lý đặt vé.

## Công nghệ sử dụng (Tech Stack)

<!-- Sử dụng lại các badge bạn đã có hoặc liệt kê dạng text -->
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=flat&logo=java&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=flat&logo=spring&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-black?style=flat&logo=JSON%20web%20tokens) ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=flat&logo=docker&logoColor=white) ![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=flat&logo=Apache%20Maven&logoColor=white) <!-- Hoặc Gradle nếu dùng Gradle --> ![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=flat&logo=mysql&logoColor=white) <!-- Hoặc TiDB nếu muốn ghi rõ --> ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=flat&logo=redis&logoColor=white) ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=flat&logo=postgresql&logoColor=white) ![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=flat&logo=amazon-aws&logoColor=white) <!-- Nếu dùng dịch vụ AWS khác ngoài TiDB -->
<!-- Thêm các công nghệ khác nếu cần -->

- **Ngôn ngữ:** Java 21
- **Framework:** Spring Boot 3, spring security 6, spring data jpa
- **Database:** MySQL, Redis 
- **Build Tool:** Apache Maven 
- **API Docs:** Swagger (OpenAPI)
- **Containerization:** Docker, Docker compose
- **Deployment:** Render, netlify

## Yêu cầu cài đặt (Prerequisites)
<!-- Liệt kê các phần mềm cần cài đặt trên máy để chạy dự án local -->
- Docker (cần docker để tạo các service)

## Hướng dẫn cài đặt và Chạy

1.  **Clone repository:**
    ```bash
    git clone https://github.com/quangthangk4/travelBackEnd.git
    ```

3.  **Chạy docker compose:**
    *   Đảm bảo Docker đang chạy.
    *   bật cmd, powershell, wsl... (bất cứ thứ gì bạn có)
    *   Chạy lệnh:
        ```bash
        cd [Đường dẫn thư mục clone về]
        docker compose --env-file .env up -d
        ```
    *(Nếu không dùng Docker Compose, bạn cần đảm bảo có instance MySQL và Redis đang chạy và cấu hình kết nối đúng trong file properties)*

5.  **Chạy ứng dụng Spring Boot:**
    ```bash
    mvn spring-boot:run
    ```

6.  Ứng dụng sẽ khởi chạy tại `http://localhost:[8080]` 

## Cấu hình (Configuration)
<!-- Giải thích cách cấu hình ứng dụng, thường là qua file application.properties/yml hoặc biến môi trường -->
Ứng dụng được cấu hình chủ yếu thông qua file `src/main/resources/application.yml`

