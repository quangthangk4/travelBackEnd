# Cách khởi động dự án
## create images travel-backend
- docker build -t travel-backend:v1.0.0 . 
(build docker image với name là travel-backend:v1.0.0 với Dockerfile ở thư mục hiện tại)

  
## run spring app by docker compose
- docker compose --env-file .env up -d  (tạo ra 3 container và chạy)

### bonus command
- docker compose down -v (xóa container và dữ liệu volume)
- docker compose down (xóa container nhưng còn dữ liệu cho lần tạo sau)
- docker start (chạy các container có sẵn và được tạo từ file docker-compose)
- docker compose stop (tắt các container được tạo từ file docker-compose)

## truy cập terminal trong container (exec)
- mysql: docker exec -it <container_name or container_id> bash
  + mysql -u root -p (u=user, p=password)
- redis: docker exec -it bash
  + redis-cli


## các lệnh docker cơ bản
- docker ps (các container đang chạy)
- docker ps -a (tất cả các container đang có trong docker bất kể trạng thái nào)
- docker logs <container_name or container_id> (logs nội dung của container ra)
- docker images (list docker images)
- docker network create <network_name> (tạo ra 1 network)
- docker network ls (danh sách network đang có trong docker)
- docker start <container_name or container_id> (chạy container có sẵn)
- docker stop <container_name or container_id> (tắt container đang chạy)
- docker rmi <container_name or container_id> (xóa docker images)
- docker rm <container_name or container_id> (xóa container)
- docker run images:tag (tạo mới và chạy container đó)
  + ví dụ:docker run -d --name mysql-container --network todo-app-network --network-alias todo-app-network-alias ^
    -v my_data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=Abc@123456789 -e MYSQL_DATABASE=todoDB -d mysql
- docker pull x:tag,  tải docker image từ docker hub, nếu k có tag mặc định lấy latest
- 

## các option trong docker 
- -d (detach) chạy ngầm background
- -e (environment) biến môi trường
- -f force bắt buộc
- -p (publish) port
- --name tên của container
- -t (tag) tên của images
- -v volume (ánh xạ)




