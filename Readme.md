# Tên Dự Án Backend Của Bạn Flight Booking

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
- [Cấu hình (Configuration)](#cấu-hình-configuration)
- [Tài liệu API (API Documentation)](#tài-liệu-api-api-documentation)
- [Chạy Kiểm thử (Running Tests)](#chạy-kiểm-thử-running-tests)
- [Triển khai (Deployment)](#triển-khai-deployment)
- [Đóng góp (Contributing)](#đóng-góp-contributing)
- [Giấy phép (License)](#giấy-phép-license)
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
    git clone [URL_REPOSITORY_CUA_BAN]
    cd [TEN_THU_MUC_DU_AN]
    ```

2.  **Cấu hình môi trường:**
    Xem phần [Cấu hình (Configuration)](#cấu-hình-configuration) bên dưới. Bạn cần tạo file `application.properties` (hoặc `application-dev.properties` nếu dùng profiles) và điền các biến môi trường cần thiết.

3.  **Build dự án (để tải dependencies):**
    ```bash
    mvn clean install
    # hoặc nếu dùng Gradle:
    # ./gradlew build
    ```

4.  **Chạy database và Redis (nếu dùng Docker Compose local):**
    *   (Nếu có) Đảm bảo Docker đang chạy.
    *   Chạy lệnh:
        ```bash
        docker-compose up -d # Chạy ngầm
        ```
    *(Nếu không dùng Docker Compose, bạn cần đảm bảo có instance MySQL và Redis đang chạy và cấu hình kết nối đúng trong file properties)*

5.  **Chạy ứng dụng Spring Boot:**
    ```bash
    mvn spring-boot:run
    # hoặc nếu dùng Gradle:
    # ./gradlew bootRun
    # hoặc chạy file JAR đã build:
    # java -jar target/[TEN_FILE_JAR].jar
    ```

6.  Ứng dụng sẽ khởi chạy tại `http://localhost:[PORT]` (ví dụ: `http://localhost:8080`).

## Cấu hình (Configuration)

<!-- Giải thích cách cấu hình ứng dụng, thường là qua file application.properties/yml hoặc biến môi trường -->
Ứng dụng được cấu hình chủ yếu thông qua file `src/main/resources/application.properties` (hoặc các file profile như `application-dev.properties`, `application-prod.properties`).

Các thuộc tính quan trọng cần cấu hình (hoặc đặt qua biến môi trường khi deploy):

```properties
# Database Configuration (Ví dụ)
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/travel_db}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:your_db_password}

# Redis Configuration (Ví dụ)
spring.data.redis.host=${SPRING_REDIS_HOST:localhost}
spring.data.redis.port=${SPRING_REDIS_PORT:6379}
spring.data.redis.password=${SPRING_REDIS_PASSWORD:} # Để trống nếu không có pass local
spring.data.redis.ssl.enabled=${SPRING_REDIS_SSL_ENABLED:false} # true khi deploy lên Upstash

# JWT Configuration (Ví dụ)
jwt.signerKey=${SIGNER_KEY:your_strong_default_secret_key_for_local_dev}
jwt.valid-duration=3600
jwt.refresh-duration=18000

# Server Port (Ví dụ)
server.port=8080

# ... (Thêm các cấu hình quan trọng khác)
