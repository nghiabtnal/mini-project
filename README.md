# Giới thiệu
- Mini project áp dụng những nội dung đã học trong quá trình tham gia devtech club
- Bao gồm 3 nội dung chính `clean code`, `clean architecture`, `Unit testing`
## Cấu trúc folder dự án 
├── main
│   └── java/org/example/miniproject
│       ├── api ## **Web layer**
│       ├── dao ## **Data layer**
│       ├── models ## **Data layer**
│       ├── repositories ## **Data layer**
│       ├── service ## **Business layer**
│       └── libs ## **libs**
│   └── resources
├── test
│   └── java/org/example/miniproject ## **Unit testing**

### Clean code
1. Một số nội dung đã áp dụng clean code như
    - Quy định về đặt tên (hàm, biến)
    - Quy định về hàm
    - Quy định về comment
    - Quy định về test
2. Ví dụ :
    -  Functions : 
<img width="805" alt="image" src="https://github.com/user-attachments/assets/b063a016-49aa-4d53-bdf5-88ee05f0730a" />
      - Tên hàm : Là động từ, ngắn gọn (vừa tầm mắt), dễ hiểu.
      - Mỗi hàm chỉ làm việc duy nhất.
      - Doc : Có mô tả input, output, description.
    - Unit test
<img width="707" alt="image" src="https://github.com/user-attachments/assets/0cf2b8c0-cd3e-49ef-b357-83b80328130b" />
      - Dễ đọc
      - Tên hàm mô tả rõ nội dung test
      - Mỗi test chỉ bao gồm 1 assert (ở đây cụ thể là Assert status trả về của HTTP request là 200)
      - Độc lập với các test case khác.
### Clean architecture
1. S
2. O
3. L : liskov institution principle
4. I : Interface Segregation principle
- Sử dụng CQRS
5. D
### Unit testing
1. Sử dụng JUNIT 5, framework để viết, execute unit test
   - Mẫu unit test ở : test/java/org/example/miniproject/api/account/controller/AccountControllerTests.java
2. Sử dụng JACOCO để đo lường `test coverage`.
3. Để thực hiện đo test coverage chạy lệnh `./gradlew clean build jacocoTestReport`
