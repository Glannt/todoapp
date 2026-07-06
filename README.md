# Todo List Backend (Spring Boot)

Dự án Spring Boot Backend cung cấp hệ thống API quản lý danh sách công việc (Todo List). Dự án sử dụng **PostgreSQL** để lưu trữ dữ liệu, tích học **Swagger UI** để xem tài liệu & kiểm thử API, đồng thời hỗ trợ container hóa bằng **Docker** để dễ dàng triển khai lên các nền tảng như Render.

Đường dẫn API production: [https://todo-backend-rntu.onrender.com](https://todo-backend-rntu.onrender.com)

---

##  Tính năng nổi bật

* **API CRUD công việc:** Hỗ trợ đầy đủ các thao tác thêm, sửa, xóa, tìm kiếm danh sách công việc.
* **Cơ sở dữ liệu tự động:** Tự động kết nối và khởi tạo Database PostgreSQL thông qua Docker Compose hoặc Spring Boot Docker Compose integration tại môi trường local.
* **Tài liệu API trực quan:** Tích hợp sẵn Swagger UI tiện lợi cho việc phát triển và tích hợp frontend.
* **Tối ưu hóa Docker:** Sẵn sàng container hóa bằng Dockerfile được tối ưu hóa.

---

##  Tài liệu API (Swagger UI)

Khi dự án đang chạy, bạn có thể truy cập vào các đường dẫn sau để xem định nghĩa và chạy thử nghiệm trực tiếp các API:

* **Swagger UI (Giao diện trực quan):**
  * Local: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  * Production: [https://todo-backend-rntu.onrender.com/swagger-ui.html](https://todo-backend-rntu.onrender.com/swagger-ui.html)
* **OpenAPI Specs (Định dạng JSON):**
  * Local: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
  * Production: [https://todo-backend-rntu.onrender.com/v3/api-docs](https://todo-backend-rntu.onrender.com/v3/api-docs)

---

##  Hướng dẫn khởi chạy dự án ở Local

Để chạy dự án ở local, máy tính của bạn cần được cài đặt sẵn:
* **Java Development Kit (JDK) 23**
* **Docker** và **Docker Compose** (đã khởi động sẵn ứng dụng Docker Desktop)

### Cách 1: Chạy trực tiếp bằng Gradle (Khuyên dùng khi lập trình)

Khi chạy bằng cách này, Spring Boot sẽ tự động nhận diện tệp `compose.yaml` ở thư mục gốc và khởi chạy container PostgreSQL trong Docker giúp bạn mà không cần cấu hình thủ công.

1. Mở terminal tại thư mục gốc của dự án.
2. Tạo tệp `.env` nếu chưa có (dùng để lưu trữ thông tin cấu hình nhạy cảm nếu cần).
3. Chạy lệnh sau để khởi động ứng dụng:

**Trên Windows (PowerShell/CMD):**
```cmd
gradlew.bat bootRun
```

**Trên Linux/macOS:**
```bash
chmod +x gradlew
./gradlew bootRun
```

Ứng dụng sẽ được khởi chạy tại cổng:  [**http://localhost:8080**](http://localhost:8080)

### Cách 2: Chạy PostgreSQL thủ công bằng Docker Compose

Nếu bạn muốn chạy riêng cơ sở dữ liệu PostgreSQL và khởi động ứng dụng Spring Boot từ IDE (như IntelliJ IDEA hoặc Eclipse):

1. Khởi động database PostgreSQL bằng Docker Compose:
   ```bash
   docker compose up -d
   ```
2. Chạy ứng dụng Spring Boot trực tiếp từ IDE của bạn.
3. Khi không sử dụng nữa, bạn có thể tắt cơ sở dữ liệu để tiết kiệm tài nguyên:
   ```bash
   docker compose down
   ```

---

## Hướng dẫn triển khai lên Render (Deployment)

Khi tạo một Web Service mới trên Render sử dụng Docker Image của bạn:

1. Nhập Image URL của bạn (ví dụ: `docker.io/glannt/todo-backend:latest`).
2. Truy cập tab **Environment** và thêm các biến môi trường cấu hình sau:

| Biến môi trường | Giá trị mẫu | Mô tả |
| :--- | :--- | :--- |
| **DB_URL** | `jdbc:postgresql://[host_db]:5432/[name_db]?sslmode=require` | Chuỗi kết nối đến CSDL PostgreSQL ngoại vi |
| **DB_USERNAME** | `[username_database]` | Tên tài khoản kết nối CSDL |
| **DB_PASSWORD** | `[password_database]` | Mật khẩu tài khoản kết nối CSDL |
| **SPRING_DOCKER_COMPOSE_ENABLED** | `false` | **Bắt buộc** để tránh lỗi tìm kiếm compose.yaml trên Production |
| **SPRING_PROFILES_ACTIVE** | `prod` | Kích hoạt profile production |
