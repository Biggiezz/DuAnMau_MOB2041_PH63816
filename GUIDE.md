# Hướng dẫn sử dụng ứng dụng JP Mart

Tài liệu này mô tả cách sử dụng ứng dụng `DuAnMau_PH63816` theo đúng luồng chức năng hiện có trong mã nguồn. Ứng dụng hỗ trợ quản lý bán hàng cơ bản trên Android và lưu dữ liệu cục bộ bằng `SQLite`.

## 1. Giới thiệu chung

Ứng dụng hỗ trợ các nhóm chức năng chính sau:

- Đăng nhập và đăng ký tài khoản
- Quản lý sản phẩm
- Quản lý danh mục
- Giỏ hàng và thanh toán
- Quản lý hóa đơn
- Quản lý khách hàng
- Quản lý nhân viên
- Xem top khách hàng
- Xem top sản phẩm bán chạy
- Thống kê doanh thu
- Hồ sơ cá nhân và đổi mật khẩu

Màn hình đầu tiên khi mở ứng dụng là `SplashScreen`. Sau đó ứng dụng sẽ tự chuyển sang màn hình đăng nhập.

## 2. Đăng nhập nhanh

Người dùng có thể đăng nhập nhanh bằng các tài khoản demo dưới đây:

| Vai trò | Tên đăng nhập | Mật khẩu | Quyền sử dụng |
| --- | --- | --- | --- |
| Quản lý | `admin` | `123456a` | Truy cập toàn bộ chức năng |
| Nhân viên | `manhphuc` | `123456` | Sử dụng các chức năng bán hàng cơ bản |

Khuyến nghị:

- Nếu cần demo toàn bộ ứng dụng, nên đăng nhập bằng tài khoản `admin`.
- Nếu muốn kiểm tra giới hạn quyền, có thể đăng nhập bằng tài khoản `manhphuc`.
- Người dùng mới có thể tạo tài khoản từ màn hình `Đăng ký`.

## 3. Tổng quan giao diện

Sau khi đăng nhập thành công, người dùng sẽ thao tác chủ yếu trên các khu vực sau:

- Thanh tab dưới cùng: `Trang chủ`, `Sản phẩm`, `Thông báo`, `Hồ sơ`
- Thanh công cụ phía trên: hiển thị tiêu đề, nút quay lại hoặc giỏ hàng tùy màn hình
- Menu trượt bên trái: hỗ trợ thao tác `Đăng xuất`
- Các nút chức năng nhanh ở `Trang chủ`: điều hướng tới các module quản lý và thống kê

## 4. Hướng dẫn sử dụng theo chức năng

### 4.1. Đăng ký tài khoản

1. Tại màn hình đăng nhập, chọn `Đăng ký`.
2. Nhập họ tên, tên đăng nhập, mật khẩu và vai trò.
3. Nhấn nút tạo tài khoản.
4. Sau khi tạo thành công, ứng dụng sẽ quay về màn hình đăng nhập.

## 4.2. Trang chủ

`Trang chủ` là nơi điều hướng đến hầu hết chức năng của ứng dụng.

Từ đây, người dùng có thể mở:

- `Sản phẩm`
- `Danh mục`
- `Khách hàng`
- `Nhân viên`
- `Hóa đơn`
- `Top khách hàng`
- `Top sản phẩm bán chạy`
- `Thống kê doanh thu`

Lưu ý:

- `Khách hàng` và `Nhân viên` chỉ hiển thị đầy đủ với tài khoản quản lý.
- Các màn hình bán hàng và thống kê vẫn có thể dùng để demo với tài khoản nhân viên theo phạm vi quyền được cấp.

### 4.3. Quản lý sản phẩm

Màn hình `Sản phẩm` hiển thị danh sách hàng hóa hiện có trong hệ thống.

1. Mở tab `Sản phẩm` hoặc chọn biểu tượng sản phẩm từ `Trang chủ`.
2. Chạm vào một sản phẩm để xem chi tiết.
3. Nhấn nút thêm để đưa sản phẩm vào giỏ hàng.
4. Dùng thao tác tăng hoặc giảm để điều chỉnh số lượng trong giỏ.

Ứng dụng có sẵn dữ liệu sản phẩm mẫu nên có thể dùng thử ngay sau khi cài đặt.

### 4.4. Thêm sản phẩm mới

1. Tại màn hình `Sản phẩm`, chọn nút thêm sản phẩm.
2. Nhập tên sản phẩm, giá, số lượng tồn, danh mục, đơn vị và ngày nhập.
3. Chọn trạng thái bán hàng.
4. Nhấn `Thêm sản phẩm` để lưu.

Lưu ý:

- Giá và số lượng tồn phải là số hợp lệ.
- Dữ liệu sản phẩm mới sẽ được lưu cục bộ trong SQLite.

### 4.5. Giỏ hàng và thanh toán

1. Mở `Giỏ hàng` từ biểu tượng giỏ trên thanh công cụ.
2. Kiểm tra danh sách sản phẩm đã chọn.
3. Dùng nút tăng hoặc giảm để thay đổi số lượng.
4. Xem phần tạm tính và tổng thanh toán.
5. Nhấn `Thanh toán` để tạo hóa đơn mới.

Sau khi thanh toán thành công, ứng dụng sẽ tự động:

- Xóa giỏ hàng hiện tại
- Cập nhật số lượng tồn kho của sản phẩm
- Tạo hóa đơn mới
- Điều hướng người dùng sang tab `Thông báo`

### 4.6. Thông báo sau thanh toán

Sau khi checkout thành công, màn hình `Thông báo` sẽ hiển thị trạng thái thanh toán thành công.

Trên Android 13 trở lên:

- Ứng dụng có thể yêu cầu quyền `POST_NOTIFICATIONS`
- Nếu người dùng cấp quyền, hệ thống sẽ gửi thông báo xác nhận thanh toán
- Nếu chưa cấp quyền, ứng dụng sẽ hiển thị phần nhắc cấp quyền

### 4.7. Hóa đơn

Màn hình `Hóa đơn` dùng để xem danh sách hóa đơn đã tạo và mở chi tiết từng hóa đơn.

1. Từ `Trang chủ`, chọn `Hóa đơn`.
2. Danh sách hóa đơn sẽ hiển thị theo thứ tự mới nhất trước.
3. Chạm vào một hóa đơn để xem sản phẩm, tổng tiền, trạng thái và thông tin liên quan.

Quyền xem hóa đơn:

- Tài khoản `Quản lý` có thể xem toàn bộ hóa đơn trong hệ thống.
- Tài khoản `Nhân viên` chỉ xem được các hóa đơn do chính tài khoản đó tạo ra.

### 4.8. Quản lý danh mục

1. Từ `Trang chủ`, chọn `Danh mục`.
2. Danh sách danh mục hiện có sẽ được hiển thị.
3. Nhấn `Thêm danh mục` để tạo mới.
4. Nhập mã danh mục, tên danh mục, số lượng, mô tả và biểu tượng đại diện.
5. Có thể cập nhật hoặc xóa danh mục từ danh sách hiện có.

### 4.9. Quản lý khách hàng

Chức năng này dành cho tài khoản quản lý.

1. Từ `Trang chủ`, chọn `Khách hàng`.
2. Xem danh sách khách hàng hiện có.
3. Chọn nút thêm để tạo khách hàng mới.
4. Chạm vào một khách hàng để xem chi tiết, cập nhật hoặc xóa.

Ứng dụng có sẵn khách hàng mẫu để phục vụ demo hóa đơn, top khách hàng và thống kê.

### 4.10. Quản lý nhân viên

Chức năng này dành cho tài khoản quản lý.

1. Từ `Trang chủ`, chọn `Nhân viên`.
2. Xem danh sách nhân viên hiện có.
3. Nhấn `Thêm nhân viên` để tạo mới.
4. Nhấn `Sửa` để cập nhật thông tin.
5. Nhấn `Xóa` để loại bỏ nhân viên khỏi hệ thống.

Lưu ý:

- Khi thêm nhân viên từ màn hình quản lý, mật khẩu mặc định được gán là `123456`.

### 4.11. Top khách hàng

1. Mở màn hình `Top khách hàng` từ `Trang chủ`.
2. Chọn `Từ ngày` và `Đến ngày`.
3. Nhập số lượng khách hàng muốn hiển thị.
4. Nhấn nút hiển thị danh sách.

Kết quả sẽ được sắp xếp theo tổng chi tiêu giảm dần trong khoảng thời gian đã chọn.

### 4.12. Top sản phẩm bán chạy

1. Mở màn hình `Top sản phẩm bán chạy`.
2. Chọn khoảng thời gian cần thống kê.
3. Nhập số lượng sản phẩm muốn hiển thị.
4. Nhấn nút hiển thị danh sách.

Kết quả được sắp xếp theo số lượng bán ra. Nếu số lượng bằng nhau, hệ thống sẽ ưu tiên sản phẩm có doanh thu cao hơn.

### 4.13. Thống kê doanh thu

1. Mở màn hình `Thống kê doanh thu`.
2. Chọn `Từ ngày` và `Đến ngày`.
3. Nhấn nút xem thống kê.
4. Ứng dụng sẽ tính tổng doanh thu từ các hóa đơn trong khoảng thời gian đã chọn.

### 4.14. Hồ sơ và đổi mật khẩu

1. Mở tab `Hồ sơ`.
2. Xem tên đăng nhập hiện tại và vai trò của tài khoản.
3. Chọn `Đổi mật khẩu`.
4. Nhập mật khẩu cũ, mật khẩu mới và xác nhận mật khẩu mới.

Điều kiện đổi mật khẩu:

- Mật khẩu mới phải có ít nhất `6` ký tự
- Mật khẩu mới không được trùng với mật khẩu cũ

### 4.15. Đăng xuất

Người dùng có thể đăng xuất theo hai cách:

- Từ nút đăng xuất trong phần `Hồ sơ`
- Từ menu trượt bên trái ở `Trang chủ`

## 5. Dữ liệu mẫu phục vụ demo

Ứng dụng đã có sẵn dữ liệu mẫu để người dùng thử nghiệm ngay sau khi cài đặt:

- Tài khoản quản lý và nhân viên
- Sản phẩm mẫu
- Khách hàng mẫu
- Hóa đơn mẫu

Khoảng thời gian gợi ý khi demo thống kê:

- `01/05/2026` đến `31/05/2026`
- `01/01/2026` đến `31/03/2026`
- `28/05/2026` đến `31/05/2026`

## 6. Kịch bản demo nhanh

1. Đăng nhập bằng tài khoản `admin`.
2. Vào `Sản phẩm` và thêm một vài mặt hàng vào giỏ.
3. Mở `Giỏ hàng` và thực hiện `Thanh toán`.
4. Quan sát tab `Thông báo` sau khi thanh toán.
5. Mở `Hóa đơn` để xem hóa đơn vừa tạo.
6. Mở `Top sản phẩm bán chạy` và `Top khách hàng` để xem dữ liệu xếp hạng.
7. Mở `Thống kê doanh thu` để kiểm tra doanh thu theo khoảng ngày.

## 7. Lưu ý khi sử dụng

- Dữ liệu được lưu cục bộ trên thiết bị và không đồng bộ lên máy chủ.
- Quyền truy cập phụ thuộc vào vai trò của tài khoản đăng nhập.
- Để xem đầy đủ các chức năng quản trị, nên dùng tài khoản `admin`.
- Trên Android 13 trở lên, nên cấp quyền thông báo để nhận thông báo sau thanh toán.
- Nếu muốn đưa dữ liệu về trạng thái ban đầu, có thể xóa dữ liệu ứng dụng hoặc cài đặt lại app.

## 8. Kết luận

`DuAnMau_PH63816` là ứng dụng phù hợp để học tập và demo vì có đầy đủ các chức năng quản lý bán hàng cơ bản trong một project Android native. Tài liệu này có thể dùng trực tiếp cho phần hướng dẫn sử dụng trong báo cáo hoặc demo môn học.
