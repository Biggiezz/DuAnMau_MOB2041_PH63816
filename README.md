# DuAnMau_PH63816

`DuAnMau_PH63816` là ứng dụng Android native hỗ trợ quản lý và bán hàng cho cửa hàng JP Mart. Dự án sử dụng `Java`, giao diện `XML` và cơ sở dữ liệu cục bộ `SQLite`, phù hợp để học tập, demo môn học hoặc chạy trực tiếp trên thiết bị mà không cần máy chủ riêng.

## 1. Tổng quan dự án

| Thuộc tính | Giá trị |
| --- | --- |
| Tên ứng dụng | `DuAnMau_PH63816` |
| Module chính | `app` |
| Package ID | `com.example.duanmau_ph63816` |
| Namespace | `com.example.DuAnMau_PH63816` |
| Màn hình khởi động | `.splash.SplashScreen` |
| Nền tảng | Android native |
| Kiểu lưu trữ | SQLite local |

## 2. Chức năng chính

- Đăng nhập và đăng ký tài khoản
- Quản lý sản phẩm và danh mục
- Giỏ hàng và thanh toán
- Quản lý hóa đơn và chi tiết hóa đơn
- Quản lý khách hàng
- Quản lý nhân viên
- Thông báo sau thanh toán
- Top khách hàng mua nhiều
- Top sản phẩm bán chạy
- Thống kê doanh thu theo khoảng ngày
- Hồ sơ cá nhân và đổi mật khẩu

## 3. Công nghệ và cấu hình

- Ngôn ngữ: `Java`
- Giao diện: `XML`, `AndroidX`, `Material Components`
- Dữ liệu: `SQLite`
- Thư viện sử dụng: `RecyclerView`, `ViewPager2`, `Picasso`
- Android Gradle Plugin: `8.9.1`
- Gradle Wrapper: `8.11.1`
- Java source/target: `17`
- `compileSdk = 36`
- `targetSdk = 35`
- `minSdk = 29`

APK debug được đặt tên theo mẫu:

```text
DuAnMau-PH63816-v1.0.apk
```

## 4. Tài khoản và dữ liệu demo

Khi cơ sở dữ liệu còn trống, ứng dụng sẽ tự sinh dữ liệu mẫu để thuận tiện cho việc demo và kiểm thử.

Tài khoản đăng nhập nhanh:

| Vai trò | Tên đăng nhập | Mật khẩu | Quyền hạn |
| --- | --- | --- | --- |
| Quản lý | `admin` | `123456a` | Toàn quyền chức năng |
| Nhân viên | `manhphuc` | `123456` | Bán hàng và xem dữ liệu theo quyền |

Ngoài tài khoản mẫu, ứng dụng cũng có sẵn dữ liệu sản phẩm, khách hàng và hóa đơn để phục vụ luồng demo.

## 5. Yêu cầu môi trường

- Android Studio hỗ trợ AGP `8.9.1`
- JDK `17` trở lên
- Android SDK Platform `36`
- Android Emulator hoặc thiết bị thật từ Android `10` trở lên

## 6. Hướng dẫn mở và chạy project

### Bước 1. Mở project trong Android Studio

1. Mở `Android Studio`.
2. Chọn `Open`.
3. Trỏ tới thư mục gốc của project `DuAnMau_MOB2041_PH63816`.

### Bước 2. Kiểm tra Android SDK

1. Mở `SDK Manager`.
2. Cài `Android SDK Platform 36`.
3. Đảm bảo đã có `Android SDK Platform-Tools` và các `Build Tools` cần thiết.

### Bước 3. Kiểm tra file `local.properties`

Nếu Android Studio chưa tự tạo file `local.properties`, hãy tạo file này tại thư mục gốc project:

```properties
sdk.dir=/đường/dẫn/tới/Android/sdk
```

Ví dụ trên macOS:

```properties
sdk.dir=/Users/tenmay/Library/Android/sdk
```

Ví dụ trên Windows:

```properties
sdk.dir=C:\\Users\\TenMay\\AppData\\Local\\Android\\Sdk
```

### Bước 4. Kiểm tra file `gradle.properties`

Project hiện có cấu hình `org.gradle.java.home` trỏ tới đường dẫn JDK của máy tạo source. Khi mở project trên máy khác, cần:

- sửa lại đường dẫn này cho đúng JDK trên máy đang dùng, hoặc
- xóa dòng đó để Android Studio tự quản lý JDK.

### Bước 5. Đồng bộ và chạy ứng dụng

1. Chờ Android Studio thực hiện `Gradle Sync`.
2. Chọn emulator hoặc thiết bị thật.
3. Nhấn `Run` để cài và chạy ứng dụng.

## 7. Build bằng dòng lệnh

Kiểm tra compile:

```bash
./gradlew :app:compileDebugJavaWithJavac
```

Build APK debug:

```bash
./gradlew assembleDebug
```

APK đầu ra thường nằm tại:

```text
app/build/outputs/apk/debug/
```

## 8. Phân quyền trong ứng dụng

- Tài khoản `Quản lý` có thể truy cập toàn bộ chức năng, bao gồm `Khách hàng` và `Nhân viên`.
- Tài khoản `Nhân viên` bị giới hạn các chức năng quản trị.
- Danh sách hóa đơn của `Nhân viên` chỉ hiển thị các hóa đơn gắn với tài khoản đang đăng nhập.

## 9. Gợi ý demo nhanh

1. Đăng nhập bằng tài khoản `admin`.
2. Vào màn hình `Sản phẩm` và thêm sản phẩm vào giỏ.
3. Mở `Giỏ hàng` và thực hiện `Thanh toán`.
4. Kiểm tra tab `Thông báo` sau khi thanh toán.
5. Mở `Hóa đơn` để xem hóa đơn vừa tạo.
6. Mở `Top khách hàng`, `Top sản phẩm bán chạy` và `Thống kê doanh thu` để demo dữ liệu mẫu.

Khoảng ngày gợi ý để demo thống kê:

- `01/05/2026` đến `31/05/2026`
- `01/01/2026` đến `31/03/2026`
- `28/05/2026` đến `31/05/2026`

## 10. Lỗi thường gặp

### Không tìm thấy Android SDK

- Kiểm tra lại file `local.properties`.
- Đảm bảo `sdk.dir` trỏ đúng thư mục SDK.

### Gradle không tìm thấy JDK

- Mở file `gradle.properties`.
- Sửa hoặc xóa dòng `org.gradle.java.home`.
- Thực hiện sync lại project.

### Gradle Sync thất bại do thiếu SDK 36

- Mở `SDK Manager`.
- Cài `Android SDK Platform 36`.

## 11. Ghi chú

- Dữ liệu được lưu cục bộ trên thiết bị.
- Muốn đưa dữ liệu về trạng thái ban đầu, có thể xóa dữ liệu ứng dụng hoặc gỡ và cài đặt lại app.
- Trên Android 13 trở lên, nên cấp quyền `POST_NOTIFICATIONS` để nhận thông báo sau thanh toán.

## 12. Tài liệu liên quan

- Hướng dẫn sử dụng chi tiết: [GUIDE.md](./GUIDE.md)
