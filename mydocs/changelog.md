# Changelog

## [2.0.0] — 2026-05-26 迭代二完成

### Added
- `LinkNode`：链接节点，`size()` 委托给目标节点，支持"共享底层节点"语义
- `PathUtil.normalize()`：路径规范化，处理 `//`、`.`、`..`、尾斜杠
- `FIND <路径> <name>`：递归搜索，匹配文件名/目录名/链接名，链接到目录的节点仅在作为起点时跟随
- `RM <路径>`：删除文件/链接直接删，目录仅空时可删，禁止删除根
- `LINK <src> <dst>`：创建链接指向 src 底层节点，目标父目录必须存在，禁止指向根
- 根目录保护：对 `/` 执行 MKDIR/TOUCH/RM/LINK(dst) 均被忽略

### Changed
- `PathUtil.parse()`：改用 `normalize()` 实现，不再拒绝 `//`、`.`、`..`、尾斜杠
- `DirNode.size()`、`FileNode.size()`：接入 `SizeContext.visit()` 防环去重
- `FileSystem`：新增 `followLinks()`、`resolveNode()`、`resolveEntry()`、`resolveParentDir()` 方法，所有命令支持链接感知的路径解析
- `Main`：新增 FIND、RM、LINK 命令分发

### Tests
- 更新 PathUtilTest、MkdirTest、TouchTest 适配迭代二路径规范化
- 新增 FindTest (12)、RmTest (9)、LinkTest (13) 单元测试
- 扩展 IntegrationTest 覆盖迭代二所有题目示例 (10 个新增)
- 总计 91 个测试全部通过

---

## [1.0.0] — 2026-04-28 迭代一完成

### Added
- `NodeType` 枚举（`FILE` / `DIR` / `LINK`）
- `Node` 接口：`name()` / `type()` / `size(SizeContext)`
- `FileNode`：文件节点，支持 `setSize()` 覆盖大小
- `DirNode`：目录节点，`TreeMap` 存子节点保证字典序，提供 `getChild` / `putChild` / `removeChild` / `listChildren`
- `SizeContext`：携带 `visited` 集合，为迭代二防环预留
- `PathUtil`：绝对路径解析，迭代一非法规则（`//` / 尾斜杠 / `.` / `..`）
- `FileSystem`：`mkdir` / `touch` / `ls` / `info` 四条命令实现
- `Main`：stdin 循环读取并分发命令
- 测试包：`PathUtilTest` / `MkdirTest` / `TouchTest` / `LsTest` / `InfoTest` / `IntegrationTest`，共 42 个用例全部通过