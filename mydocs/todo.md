# TODO

## 迭代一

- [x] 设计系统架构
- [x] 实现 `Node` 抽象层（`NodeType`, `Node`, `FileNode`, `DirNode`）
- [x] 实现 `SizeContext`
- [x] 实现 `PathUtil`（路径解析 + 合法性校验）
- [x] 实现 `FileSystem`（`mkdir` / `touch` / `ls` / `info`）
- [x] 实现 `Main`（stdin 循环 + 命令分发）
- [x] 编写测试包（`PathUtilTest`, `MkdirTest`, `TouchTest`, `LsTest`, `InfoTest`, `IntegrationTest`）
- [x] 打包 submission 并提交 Gradescope

## 迭代二（待开始）

- [ ] `PathUtil` 扩展：支持路径规范化（`//` / `.` / `..`）
- [ ] 新增 `FIND <路径> <name>` 递归搜索
- [ ] 新增 `RM <路径>` 删除节点
- [ ] 新增 `LinkNode` + `LINK <src> <dst>` 链接机制
- [ ] `SizeContext` 启用 `visited` 防环去重
- [ ] 更新测试包覆盖迭代二新功能
- [ ] 打包提交
