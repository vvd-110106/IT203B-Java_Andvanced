# SRS - Smart Traffic Simulator (Hệ thống Mô phỏng Giao thông Thông minh)

## I. Giới thiệu

Hệ thống Mô phỏng Giao thông Thông minh là một ứng dụng console (hoặc backend module) mô phỏng hoạt động của một ngã tư đường phố. Hệ thống quản lý các luồng phương tiện di chuyển tự do, phản ứng với tín hiệu đèn giao thông và giải quyết các bài toán về chia sẻ tài nguyên (mặt đường, giao lộ). Dự án tập trung vào xử lý đồng thời (concurrency), thiết kế kiến trúc linh hoạt và đảm bảo luồng dữ liệu an toàn khi có nhiều luồng (threads) cùng hoạt động.

## II. Mục tiêu dự án

1. Xây dựng cấu trúc chương trình chuẩn OOAD và tuân thủ chặt chẽ các nguyên lý SOLID.
2. Làm chủ lập trình đa luồng (Multithreading) và cơ chế đồng bộ hóa (Synchronization) trong Java.
3. Áp dụng các Design Patterns để giải quyết các bài toán thiết kế mô phỏng phức tạp.
4. Sử dụng Java 8+ và các cấu trúc dữ liệu đồng bộ (Concurrent Collections) để quản lý luồng phương tiện.
5. Đảm bảo tính ổn định của hệ thống bằng Exception Handling và kiểm thử đa luồng với Unit Test.

## III. Yêu cầu chức năng

### 1. Quản lý Hệ sinh thái Giao thông (Traffic Environment)

- Tự động sinh ra các loại phương tiện ngẫu nhiên (Xe máy, Ô tô, Xe tải, Xe cứu thương) với các mức độ ưu tiên và tốc độ khác nhau.
- Hệ thống đèn giao thông tự động chuyển trạng thái (Xanh - Vàng - Đỏ) theo chu kỳ thời gian thực.

### 2. Mô phỏng Di chuyển & Điều phối (Simulation Engine)

- Các phương tiện tự động di chuyển về phía ngã tư.
- Phương tiện phải dừng lại khi gặp đèn đỏ hoặc khi có xe khác đang chắn phía trước.
- Xử lý logic nhường đường: khi xe cứu thương xuất hiện, các phương tiện khác phải nhường đường hoặc xe cứu thương được phép vượt đèn đỏ.

### 3. Thống kê và Giám sát (Monitoring)

- Ghi log (Console) trạng thái hệ thống theo thời gian thực (VD: `[Time: 12s] Xe cứu thương #01 đang đi qua ngã tư`).
- Thống kê lưu lượng xe qua ngã tư thành công.
- Báo cáo số lần xảy ra tình trạng kẹt xe (khi hàng đợi chờ đèn đỏ quá dài).

## IV. Ràng buộc kỹ thuật

### 1. Object-Oriented Programming & SOLID

- **LSP & OCP**: Tạo lớp cha `Vehicle` và các lớp con `StandardVehicle`, `PriorityVehicle` (Xe cứu thương). Việc thêm loại xe mới (ví dụ: Xe cứu hỏa) không được làm thay đổi logic lõi của giao lộ.
- **SRP**: Tách biệt rõ ràng lớp `TrafficLight` (chỉ quản lý đèn) và `Intersection` (quản lý vùng không gian giao nhau).

### 2. Multithreading (Đa luồng)

- **Thread Lifecycle**: Mỗi `Vehicle` là một luồng độc lập, hoặc sử dụng `ExecutorService` (Thread Pool) để quản lý hàng trăm xe cùng lúc.
- **Synchronization**: Giao lộ (`Intersection`) là một critical section. Sử dụng `synchronized`, `ReentrantLock` hoặc `Semaphore` để đảm bảo chỉ có số lượng xe nhất định được đi qua một lúc, tránh race condition.

### 3. Design Patterns

- **State Pattern**: Quản lý trạng thái và hành vi của `TrafficLight` (`GreenState`, `YellowState`, `RedState`).
- **Observer Pattern**: Đèn giao thông (Subject) phát tín hiệu; các xe đang chờ (Observers) nhận tín hiệu để quyết định đi tiếp hay dừng lại.
- **Factory Method**: Xây dựng `VehicleFactory` để sinh ngẫu nhiên các loại phương tiện tham gia giao thông.

### 4. Collections Framework & Java 8

- Sử dụng cấu trúc dữ liệu thread-safe như `ConcurrentLinkedQueue` hoặc `BlockingQueue` để quản lý hàng đợi các xe đang chờ đèn đỏ.
- Dùng Stream API để thống kê số lượng phương tiện theo loại đang có mặt tại ngã tư.

### 5. Exception Handling

- Tự định nghĩa các Exception:
  - `TrafficJamException`: khi hàng chờ vượt quá công suất đường.
  - `CollisionException`: nếu xử lý lock không tốt khiến 2 xe lao vào nhau.

### 6. Unit Testing (JUnit 5 + Mockito)

- Viết test case kiểm tra logic chuyển đổi trạng thái của State Pattern.
- Test đa luồng: giả lập đẩy 100 phương tiện vào ngã tư cùng lúc (Stress test) để kiểm tra ReentrantLock có hoạt động đúng và ngăn chặn `CollisionException`.

## V. Nhiệm vụ

### Giai đoạn thiết kế

- Vẽ sơ đồ lớp (Class Diagram) thể hiện rõ các Design Patterns được sử dụng.
- Vẽ sơ đồ tuần tự (Sequence Diagram) cho kịch bản: 1 Xe ô tô và 1 Xe cứu thương cùng tiến vào ngã tư.

### Giai đoạn thực thi

- Cài đặt cấu trúc thư mục (Entity, Engine, Pattern, Exception, Util).
- Xây dựng hệ thống Thread và các cơ chế Lock/Unlock tài nguyên ngã tư.
- In ra Console diễn biến của ngã tư theo định dạng log sinh động.

### Giai đoạn kiểm thử

- Viết Unit Test đảm bảo đèn giao thông đếm đúng thời gian và xe không bị mất dữ liệu khi chạy đa luồng.

### Giai đoạn hoàn thiện

- Tối ưu hóa hiệu năng (ví dụ: thay vì lock toàn bộ ngã tư, có thể nâng cấp lên `ReadWriteLock` hoặc lock từng làn đường độc lập).
- Viết file `README.md` giải thích cách hệ thống xử lý deadlock.

## VI. Ghi chú thêm

- Luôn đảm bảo hệ thống có cơ chế stop an toàn (graceful shutdown) để tránh thread leak.
- Hệ thống cần có cơ chế cấu hình (ví dụ: thời gian chu kỳ đèn, tốc độ xe, mức ưu tiên) để dễ dàng thử nghiệm.
