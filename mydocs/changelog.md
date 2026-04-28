# Changelog

## [Unreleased] — 迭代二

待开发：路径规范化、`FIND`、`RM`、`LINK`、防环 `INFO`。

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
