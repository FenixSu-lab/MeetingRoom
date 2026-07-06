# 天杰会议室管理系统

一款用于会议室显示终端的Android应用，实时展示会议室状态、会议信息及倒计时。

## 功能特点

- **全屏显示**：无状态栏、无导航栏，沉浸式全屏展示
- **实时时间**：每秒更新时间显示
- **会议室状态**：当前会议进行中/空闲状态展示
- **会议信息**：显示预订人、部门、时间范围
- **倒计时功能**：当前会议剩余时间、下一场会议倒计时
- **自动轮询**：每分钟自动获取最新会议数据
- **屏幕常亮**：保持屏幕不熄灭

## 技术框架

- **语言**：Kotlin
- **架构**：MVVM (Model-View-ViewModel)
- **网络**：Retrofit + OkHttp
- **数据绑定**：LiveData + Observer
- **协程**：Kotlin Coroutines
- **构建工具**：Gradle (Kotlin DSL)

## 项目结构

```
app/src/main/java/com/example/meetroom/
├── MainActivity.kt                    # 主界面Activity
├── data/
│   ├── model/
│   │   ├── RoomReservation.kt        # 会议预订数据模型
│   │   ├── RoomReservationList.kt     # 会议列表数据模型
│   │   └── MeetingRoomResponse.kt     # API响应数据模型
│   ├── remote/
│   │   ├── RoomConfig.kt              # 会议室配置（含默认会议室）
│   │   ├── MeetingRoomApi.kt          # Retrofit API接口
│   │   └── RetrofitClient.kt          # Retrofit客户端单例
│   └── repository/
│       └── MeetingRoomRepository.kt   # 数据仓库
└── viewmodel/
    └── RoomDisplayViewModel.kt        # 视图模型（含轮询逻辑）
```

## 配置说明

### 修改默认会议室

编辑 `app/src/main/java/com/example/meetroom/data/remote/RoomConfig.kt` 文件：

```kotlin
const val DEFAULT_ROOM_ID = "omm_ad664f5245a1ded68f9d82486a405184"
const val DEFAULT_ROOM_NAME = "聚贤堂"
```

### 可用会议室列表

| 会议室名称 | Room ID |
|-----------|---------|
| 聚贤堂 | `omm_ad664f5245a1ded68f9d82486a405184` |
| 纳贤室 | `omm_fa99bff7b0907c3dfb7835708255e19f` |
| 培训教室 | `omm_fdb1eef204ca4fae27448e354a535fe8` |
| 行政2楼会议室 | `omm_ed84e689a2a229736bc01d8c46d209e0` |
| 业务会议室 | `omm_d91e3b30f10fdca8d091225146c0de39` |
| 天然居 | `omm_e55bf26a92ee364b12f5ea486a414e79` |
| 财务会议室 | `omm_e72584d700d953fdbae03945708075c3` |
| 生产副总会议室 | `omm_b5ff98e86f33cfc595ebdc12f525e150` |
| 生产计划会议室 | `omm_faa315033a9207f891560c247c6e6d26` |

### 修改轮询间隔

编辑 `app/src/main/java/com/example/meetroom/viewmodel/RoomDisplayViewModel.kt` 文件中的 `delay(60000)`：

- 30秒：`delay(30000)`
- 2分钟：`delay(120000)`
- 5分钟：`delay(300000)`

## 接口说明

### 获取会议列表

```
POST /open_api/v1/calendar_room/get_room_reservation_list
```

**请求参数**：
- `roomId`: 会议室ID
- `date`: 日期（格式：yyyy-MM-dd）

**响应结构**：
```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "data": {
            "roomReservationList": [
                {
                    "reservationStatus": "会议进行中",
                    "eventStartTime": "2026-06-26 17:00:00",
                    "eventEndTime": "2026-06-26 17:30:00",
                    "reserver": "戴印丽",
                    "departmentOfReserver": "总经理办公室"
                }
            ]
        }
    }
}
```

## 依赖列表

| 依赖 | 版本 | 用途 |
|------|------|------|
| Retrofit | 2.11.0 | 网络请求 |
| OkHttp | 4.12.0 | HTTP客户端 |
| Gson | 2.11.0 | JSON解析 |
| Lifecycle ViewModel | 2.8.6 | 视图模型 |
| Lifecycle LiveData | 2.8.6 | 数据观察 |

## 开发环境

- Android Studio Hedgehog | 2023.1.1
- Kotlin 2.0.0
- Gradle 9.4.1
- Android SDK 34

## 构建运行

```bash
# 克隆项目
git clone <repository-url>
cd MeetRoom

# 构建Debug版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

## License

MIT License