package Session02;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Ex1 {
//    Lựa chọn Functional Interface phù hợp cho User:
//    1: Kiểm tra User và Admin:
//        - Interface: Predicate<User>
//        - Lý do: Nhận vào 1 đối tượng User và trả về giá trị boolean. Phù hợp với việc kiểm tra điều kiện
//    2: Chuyển đối tương User thành chuỗi String
//        - Interface: Function<User, String>
//        - Lý do: Nhận vào kiểu dữ lệu này (User) và biến đổi sang kiểu dữ liệu khác (String)
//    3: In thông tin chi tiết của User ra màn console:
//        - Interface: Consumer<User>
//        - Lý do: Nhận vào 1 User để tiêu thụ/ thực hiện hành động nhưng không trả lại kết quả (void)
//    4: Khởi tạo 1 đối tượng User mới với các giá trị mặc định
//        - Interface: Supplier<User>
//        - Lý do: Không nhận tham số đầu vào nhưng cung cấp (trả về) một đối tượng User mới
}
